/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024 - 2025 Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 */
package mecard.requestbuilder;

import api.Command;
import api.CommandStatus;
import api.CustomerMessage;
import api.HttpCommandStatus;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import mecard.QueryTypes;
import static mecard.QueryTypes.CREATE_CUSTOMER;
import static mecard.QueryTypes.GET_CUSTOMER;
import static mecard.QueryTypes.GET_STATUS;
import static mecard.QueryTypes.UPDATE_CUSTOMER;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.MessagesTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiPropertyTypes;
import mecard.config.SDapiUserFields;
import mecard.customer.Customer;
import mecard.customer.DumpUser;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.exception.ConfigurationException;
import mecard.security.SDapiSecurity;
import mecard.security.TokenManager;
import mecard.sirsidynix.sdapi.MeCardCustomerToSDapi;
import mecard.sirsidynix.sdapi.MeCardDataToSDapiData;
import mecard.sirsidynix.sdapi.SDWebServiceCommand;
import mecard.sirsidynix.sdapi.SDapiAuthenticationData;
import mecard.sirsidynix.sdapi.SDapiCustomerCreateResponse;
import mecard.sirsidynix.sdapi.SDapiCustomerUpdateResponse;
import mecard.sirsidynix.sdapi.SDapiResponse;
import mecard.sirsidynix.sdapi.SDapiUserPatronLoginResponse;
import mecard.sirsidynix.sdapi.SDapiUserPatronSearchCustomerResponse;
import mecard.sirsidynix.sdapi.SDapiUserStaffLoginResponse;
import mecard.sirsidynix.sdapi.SDapiToMeCardCustomer;
import mecard.sirsidynix.sdapi.SDapiUserPatronKeyCustomerResponse;
import mecard.sirsidynix.sdapi.WebServiceDummyCommand;
import site.CustomerLoadNormalizer;

/**
 * This class supports customer registration commands using the SirsiDynxi web
 * service API.
 * 
 * Symphony can use restful web services for all ILS transactions.
 * @author Andrew Nisbet andrew at dev-ils.com
 */
public class SDapiRequestBuilder extends ILSRequestBuilder
{
    private final Properties sdapiProperties;
    private final Properties messageProperties;
    private final TokenManager tokenManager;
    private final boolean debug;
    @SuppressWarnings("FieldMayBeFinal")
    private long tokenExpiry;
    /**
     * Creates the Sirsi Dynix Web Service request builder.
     * @param debug
     */
    public SDapiRequestBuilder(boolean debug)
    {
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        this.sdapiProperties = PropertyReader.getProperties(ConfigFileTypes.SIRSIDYNIX_API);
        this.debug = debug;
        
        // Get the SDapi properties file and set up a token manager.
        this.tokenManager = new TokenManager();
        String strTokenExpiry = sdapiProperties.getProperty(
            SDapiPropertyTypes.SESSION_TOKEN_EXPIRY_TIME.toString());
        try
        {
            this.tokenExpiry = Long.parseLong(strTokenExpiry);
        }
        catch (NumberFormatException e)
        {
            System.out.println("""
                *warn: invalid sessionToken expiry timelimit set in sdapi.properties 
                value must be a long integer type. Defaulting to 60 minutes.""");
            this.tokenExpiry = 60;
        }
    }
    
    /**
     * Gets the session token and manages refreshing it if necessary.
     * @param response object that is used at various stages of the pipeline
     * to add useful feedback of any issues.
     * @return The session token as a string.
     */
    protected String getSessionToken(Response response)
    {
        if (tokenManager.isTokenExpired(this.tokenExpiry))
        {
            String envFilePath = sdapiProperties.getProperty(SDapiPropertyTypes.ENV.toString());
            String staffId = "";
            String staffPassword = "";
            
            if (envFilePath == null || envFilePath.isBlank())
            {
                throw new ConfigurationException("""
                    **error, the sdapi.properties file does not contain an entry
                    for the environment file (.env). The entry should look like this example:
                    <entry key="env-file-path">/MeCard/path/to/.env</entry>
                    Add entry or check for spelling mistakes.
                     """);
            }
            try 
            {
                SDapiSecurity sds = new SDapiSecurity(envFilePath);
                staffId = sds.getStaffId();
                staffPassword = sds.getStaffPassword();
            } 
            catch (IOException e) 
            {
                System.out.println("""
                    **error, expected an .env file but it is missing or can't be found.
                    The .env file should include entries for staff ID and password. For example,
                    STAFF_ID="SomeStaffId"
                    STAFF_PASSWORD="SomeStaffPassword"
                    """ + e);
            }
            
            SDapiAuthenticationData authData = new SDapiAuthenticationData();
            String loginBodyText = authData.getStaffAuthentication("", staffId, staffPassword);
            SDWebServiceCommand command = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
                .endpoint("/user/staff/login")
                .bodyText(loginBodyText)
                .build();
            HttpCommandStatus status = command.execute();
            if (status.okay())
            {
                // Check response for any errors.
                SDapiUserStaffLoginResponse authResponse; 
                try
                {
                    authResponse = (SDapiUserStaffLoginResponse) SDapiUserStaffLoginResponse.parseJson(status.getStdout());
                    if (authResponse.succeeded())
                    {
                        if (this.debug)
                        {
                            System.out.println(new Date() + " staff authenticated successfully.");
                        }
                        this.tokenManager.writeToken(
                                authResponse.getSessionToken(), 
                                Duration.ofMinutes(this.tokenExpiry));
                        
                        response.setCode(ResponseTypes.SUCCESS);
                        response.setResponse("");
                    }
                    else
                    {
                        if (this.debug)
                        {
                            System.out.println(new Date() + " staff FAILED to authenticate.");
                        }
                        response.setCode(ResponseTypes.CONFIG_ERROR);
                        response.setResponse(authResponse.errorMessage());
                    }
                }
                catch (NullPointerException ne)
                {
                    response.setResponse(messageProperties.getProperty(
                        MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                }
            }
            else // The web service send back an HTTP error, translate and return to customer.
            {
                System.out.println("**error web service: "
                    + status.getHttpStatusCode() + " : " + status.toString());
                response.setCode(status.getStatus());
                switch (status.getStatus())
                {
                    case UNAUTHORIZED -> 
                            response.setResponse(messageProperties.getProperty(
                                MessagesTypes.USERID_PIN_MISMATCH.toString()));
                    case CONFIG_ERROR, BUSY, UNAVAILABLE, UNKNOWN -> 
                            response.setResponse(messageProperties.getProperty(
                            MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                    default -> response.setResponse(messageProperties.getProperty(
                            MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                }
            }
            tokenManager.writeTokenFromStdout("sessionToken", 
                    status.getStdout(), 
                    Duration.ofMinutes(this.tokenExpiry));
        }
        
        return tokenManager.getToken();
    }

    @Override
    public final NativeFormatToMeCardCustomer getFormatter() 
    {
        return new SDapiToMeCardCustomer();
    }

    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response) 
    {
        //  Start with the user logging in to prove they are legit customer.
        SDapiAuthenticationData authData = new SDapiAuthenticationData();
        String loginBodyText = authData.getPatronAuthentication(userId, userPin);
        if (this.debug)
            System.out.println("loginBodyText: " + loginBodyText);
        SDWebServiceCommand loginCustomer = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/patron/login")
            .bodyText(loginBodyText)
            .build();
        
        HttpCommandStatus status = loginCustomer.execute();
        SDapiResponse loginResponse = 
                        (SDapiUserPatronLoginResponse) 
                            SDapiUserPatronLoginResponse.parseJson(status.getStdout());
        if (this.debug)
            System.out.println("GET_CUSTOMER key:" + loginResponse);
        if (loginResponse.succeeded())
        {
            // Get customer info by user key.
            String userKey = loginResponse.toString();
            // String params  = "includeFields=*,address1{*},circRecordList{*}";
            return new SDWebServiceCommand.Builder(sdapiProperties, "GET")
                .endpoint("/user/patron/key/" + userKey + "?includeFields=*,address1%7B*%7D,circRecordList%7B*%7D")
                .sessionToken(this.getSessionToken(response))
                .build();
        }
        else
        {
            if (this.debug)
            {
                System.out.println(new Date() + " customer " + userId + " FAILED to authenticate.");
            }
            response.setCode(ResponseTypes.USER_PIN_INVALID);
            response.setResponse(loginResponse.errorMessage());
            return new WebServiceDummyCommand.Builder()
                .setStatus(status.getHttpStatusCode())
                .setStderr(response.getMessage())
                .setStdout(response.getMessage())
                .build();
        }
    }
    
    /**
     * Special command used if the Web Service is not accessible. This command
     * will be executed as a loop-back command and a canned response returned.
     * @param response
     * @param status
     * @return 
     */
    private WebServiceDummyCommand getFailedWebServiceCommand(Response response, HttpCommandStatus status)
    {
        System.out.println("**error web service: "
                + status.getHttpStatusCode() + " : " + status.toString());
        response.setCode(status.getStatus());
        switch (status.getStatus())
        {
            case UNAUTHORIZED -> 
                    response.setResponse(messageProperties.getProperty(
                        MessagesTypes.USERID_PIN_MISMATCH.toString()));
            case CONFIG_ERROR, BUSY, UNAVAILABLE, UNKNOWN -> 
                    response.setResponse(messageProperties.getProperty(
                    MessagesTypes.UNAVAILABLE_SERVICE.toString()));
            default -> response.setResponse(messageProperties.getProperty(
                    MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
        }
        return new WebServiceDummyCommand.Builder()
            .setStatus(status.getHttpStatusCode())
            .setStderr(response.getMessage())
            .setStdout(response.getMessage())
            .build();
    }

    @Override
    public Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer) 
    {
        // Use the create user API
        // POST: https://{{HOST}}/{{WEBAPP}}/user/patron
       
        // Now use the customer data as the update command body.
        MeCardCustomerToNativeFormat jsonNativeCustomer = 
                new MeCardCustomerToSDapi(customer, MeCardDataToSDapiData.QueryType.CREATE);
        // apply library centric normalization to the customer account.
        normalizer.finalize(customer, jsonNativeCustomer, response);
        // Output the customer's data as a receipt in case they come back with questions.
        List<String> customerReceipt = jsonNativeCustomer.getFormattedCustomer();
        if (this.debug)
        {
            System.out.println("CREATE JSON body: " + jsonNativeCustomer.toString());
        }
        new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.txt)
            .set(customerReceipt)
            .build();
        
        SDWebServiceCommand createCustomerCommand = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/patron/")
            // TODO check this is right?!
            .bodyText(jsonNativeCustomer.toString())
            .sessionToken(getSessionToken(response))
            .build();
        return createCustomerCommand;
    }

    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer) 
    {
        // first get the customer's key.
        String barcode = customer.get(CustomerFieldTypes.ID);
        
        SDWebServiceCommand searchPatron = new SDWebServiceCommand.Builder(sdapiProperties, "GET")
                .endpoint("/user/patron/search?rw=1&q=ID:"+barcode)
                .sessionToken(this.getSessionToken(response))
                .build();
        // Execute and if successful create a request for customer info by 
        // search by user key.
        HttpCommandStatus status = searchPatron.execute();
        if (! status.okay())
        {
            return getFailedWebServiceCommand(response, status);
        }
        // Search for customer info with the user key later.
        String userKey = "";
        
        SDapiUserPatronSearchCustomerResponse searchResponse; 
        try
        {
            searchResponse = (SDapiUserPatronSearchCustomerResponse) SDapiUserPatronSearchCustomerResponse.parseJson(status.getStdout());
            if (searchResponse.succeeded())
            {
                if (this.debug)
                {
                    System.out.println(new Date() + " " + barcode + " authenticated successfully.");
                }
                response.setCode(ResponseTypes.SUCCESS);
                response.setResponse("");
                userKey = searchResponse.getField(SDapiUserFields.USER_KEY.toString());
            }
            else
            {
                if (this.debug)
                {
                    System.out.println(new Date() + " customer " + barcode + " FAILED to authenticate.");
                }
                response.setCode(ResponseTypes.USER_PIN_INVALID);
                response.setResponse(searchResponse.errorMessage());
                return new WebServiceDummyCommand.Builder()
                    .setStatus(status.getHttpStatusCode())
                    .setStderr(response.getMessage())
                    .setStdout(response.getMessage())
                    .build();
            }
        }
        catch (NullPointerException ne)
        {
            response.setResponse(messageProperties.getProperty(
            MessagesTypes.UNAVAILABLE_SERVICE.toString()));
            return new WebServiceDummyCommand.Builder()
                .setStatus(status.getHttpStatusCode())
                .setStderr(response.getMessage())
                .setStdout(response.getMessage())
                .build();
        }
        
        // Now use the customer data as the update command body.
        MeCardCustomerToNativeFormat jsonNativeUpdateCustomer = 
                new MeCardCustomerToSDapi(customer, MeCardDataToSDapiData.QueryType.UPDATE);
        // apply library centric normalization to the customer account.
        normalizer.finalize(customer, jsonNativeUpdateCustomer, response);
        // Output the customer's data as a receipt in case they come back with questions.
        List<String> customerReceipt = jsonNativeUpdateCustomer.getFormattedCustomer();
        if (this.debug)
        {
            System.out.println("CREATE JSON body: " + jsonNativeUpdateCustomer.toString());
        }
        new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.txt)
            .set(customerReceipt)
            .build();
        // /user/patron/key/2139681
        SDWebServiceCommand updateCustomerCommand = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
                .endpoint("/user/patron/key/" + userKey)
                // TODO check this is right?!
                .bodyText(jsonNativeUpdateCustomer.toString())
                .sessionToken(getSessionToken(response))
                .build();
        return updateCustomerCommand;
    }

    @Override
    public Command getStatusCommand(Response response) 
    {
        // Reuse the staff login command.
        String envFilePath = sdapiProperties.getProperty(SDapiPropertyTypes.ENV.toString());
        String staffId = "";
        String staffPassword = "";

        if (envFilePath == null || envFilePath.isBlank())
        {
            throw new ConfigurationException("""
                **error, the sdapi.properties file does not contain an entry
                for the environment file (.env). The entry should look like this example:
                <entry key="env-file-path">/MeCard/path/to/.env</entry>
                Add entry or check for spelling mistakes.
                 """);
        }
        try 
        {
            SDapiSecurity sds = new SDapiSecurity(envFilePath);
            staffId = sds.getStaffId();
            staffPassword = sds.getStaffPassword();
        } 
        catch (IOException e) 
        {
            System.out.println("""
                **error, expected an .env file but it is missing or can't be found.
                The .env file should include entries for staff ID and password. For example,
                STAFF_ID="SomeStaffId"
                STAFF_PASSWORD="SomeStaffPassword"
                """ + e);
        }
        // This query relies on the user being able to login but 
        // another method is to allow a staff search of the customer with 
        // https://{{HOST}}/{{WEBAPP}}/user/staff/login
        SDapiAuthenticationData authData = new SDapiAuthenticationData();
        String authenticationJSONData = authData.getStaffAuthentication("", staffId, staffPassword);
        SDWebServiceCommand command = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/staff/login")
            .bodyText(authenticationJSONData)
            .build();
        
        return command;
    }

    @Override
    public Command testCustomerExists(String userId, String userPin, Response response) 
    {
        // Customer has to prove they are legit allowed to update or get
        // their information. That requires knowing both user ID and password.
        //POST: https://{{HOST}}/{{WEBAPP}}/user/patron/login
        
        /*
        Ultimately this works best because any search strategy that identifies 
        a existing customer, say John Smith who happens to have the same birthday
        as this customer will get their account over-written, but ultimately 
        if the customer can login with their ID and PIN it can only be the one 
        account.
        */
        return this.getCustomerCommand(userId, userPin, response);
    }

    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response) 
    {
        String nullResponseMessage = " no json received. Did the server timeout?";
        switch (commandType)
        {
            case GET_STATUS -> {
                // uses staff authentication command.
                SDapiResponse staffAuthenticates;
                try
                {
                    staffAuthenticates =                
                        (SDapiUserStaffLoginResponse) 
                            SDapiUserStaffLoginResponse.parseJson(status.getStdout());
                }
                catch (NullPointerException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                if (staffAuthenticates.succeeded())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    if (debug) System.out.println("Status succeeded.");
                    return true;
                }
                // Otherwise the response was a failure.
                response.setCode(ResponseTypes.UNAVAILABLE);
                response.setResponse(messageProperties.getProperty(
                        MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                System.out.println("**get status failed: "
                        + staffAuthenticates.errorMessage());
                return false;
            }
            case TEST_CUSTOMER, GET_CUSTOMER -> {
                // Let the customer loging and see if they are legit.
                SDapiResponse testGetCustomerData;
                try
                {
                    testGetCustomerData =                
                        (SDapiUserPatronKeyCustomerResponse) 
                            SDapiUserPatronKeyCustomerResponse.parseJson(status.getStdout());
                }
                catch (NullPointerException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                if (testGetCustomerData.succeeded())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    if (debug) 
                        System.out.println("GET CUSTOMER succeeded.");
                    return true;
                }
                // Otherwise the response was a failure.
                response.setCode(ResponseTypes.USER_NOT_FOUND);
                response.setResponse(messageProperties.getProperty(
                        MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                System.out.println("**get customer account info failed: "
                        + testGetCustomerData.errorMessage());
                return false;
            }
            case UPDATE_CUSTOMER -> {
                SDapiResponse customerUpdated;
                try
                {
                    customerUpdated =                
                        (SDapiCustomerUpdateResponse) 
                            SDapiCustomerUpdateResponse.parseJson(status.getStdout());
                }
                catch (NullPointerException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                if (customerUpdated.succeeded())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    if (debug) System.out.println("Customer updated.");
                    return true;
                }
                // Otherwise the response was a failure.
                response.setCode(ResponseTypes.UNAVAILABLE);
                response.setResponse(messageProperties.getProperty(
                        MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                System.out.println("**get status failed: "
                        + customerUpdated.errorMessage());
                return false;
            }
            case CREATE_CUSTOMER -> {
                SDapiResponse customerCreated;
                try
                {
                    customerCreated =                
                        (SDapiCustomerCreateResponse) 
                            SDapiCustomerCreateResponse.parseJson(status.getStdout());
                }
                catch (NullPointerException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                if (customerCreated.succeeded())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    if (debug) System.out.println("Customer created.");
                    return true;
                }
                // Otherwise the response was a failure.
                response.setCode(ResponseTypes.UNAVAILABLE);
                response.setResponse(messageProperties.getProperty(
                        MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                System.out.println("**get status failed: "
                        + customerCreated.errorMessage());
                return false;
            }
            default -> {
                response.setCode(ResponseTypes.FAIL);
                response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                System.out.println("**error, the requested command " 
                        + commandType.name() + " has no test for success.");
                return false;
            }
        }
    }

    @Override
    public boolean tidy()
    {
        return true;
    }

    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        SDapiUserPatronSearchCustomerResponse customerResponse = 
                (SDapiUserPatronSearchCustomerResponse) SDapiUserPatronSearchCustomerResponse.parseJson(stdout);
        return customerResponse;
    }
    
}
