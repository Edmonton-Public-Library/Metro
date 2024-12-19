/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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
import api.DummyCommand;
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
import mecard.polaris.papi.MeCardDataToPapiData.QueryType;
import mecard.security.SDapiSecurity;
import mecard.security.TokenManager;
import mecard.sirsidynix.sdapi.MeCardCustomerToSDapi;
import mecard.sirsidynix.sdapi.SDWebServiceCommand;
import mecard.sirsidynix.sdapi.SDapiAuthenticationData;
import mecard.sirsidynix.sdapi.SDapiResponse;
import mecard.sirsidynix.sdapi.SDapiUserPatronLoginResponse;
import mecard.sirsidynix.sdapi.SDapiUserPatronSearchCustomerMessage;
import mecard.sirsidynix.sdapi.SDapiUserStaffLoginResponse;
import mecard.sirsidynix.sdapi.SDapiToMeCardCustomer;
import mecard.sirsidynix.sdapi.WebServiceDummyCommand;
import org.apache.http.HttpStatus;
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
     */
    public SDapiRequestBuilder()
    {
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        this.sdapiProperties = PropertyReader.getProperties(ConfigFileTypes.SIRSIDYNIX_API);
        this.debug = Boolean.parseBoolean(sdapiProperties.getProperty(SDapiPropertyTypes.DEBUG.toString(),"false"));
        
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
        String loginBodyText = authData.getPatronAuthentication(userId, userId);
        SDWebServiceCommand command = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/patron/login")
            .bodyText(loginBodyText)
            .build();
        
        // Execute and if successful create a request for customer info by 
        // search by user key.
        HttpCommandStatus status = command.execute();
        // Search for customer info with the user key later.
        String userKey = "";
        if (! status.okay())
        {
            return getFailedWebServiceCommand(response, status);
        }
            // Check response for any errors.
        SDapiUserPatronLoginResponse authResponse; 
        try
        {
            authResponse = (SDapiUserPatronLoginResponse) SDapiUserPatronLoginResponse.parseJson(status.getStdout());
            if (authResponse.succeeded())
            {
                if (this.debug)
                {
                    System.out.println(new Date() + " " + userId + " authenticated successfully.");
                }
                response.setCode(ResponseTypes.SUCCESS);
                response.setResponse("");
                userKey = authResponse.getUserKey();
            }
            else
            {
                if (this.debug)
                {
                    System.out.println(new Date() + " customer " + userId + " FAILED to authenticate.");
                }
                response.setCode(ResponseTypes.USER_PIN_INVALID);
                response.setResponse(authResponse.errorMessage());
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
               
        // Get customer info by user key.
        command = new SDWebServiceCommand.Builder(sdapiProperties, "GET")
            .endpoint("/user/patron/key/" + userKey + "?includeFields=*,circRecordList{*},address1{*}")
            .sessionToken(this.getSessionToken(response))
            .build();
        return command;
    }
    
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
        //  Start with the user key  https://{{HOST}}/{{WEBAPP}}/user/patron/search?rw=1&q=ID:21221012345678
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        
        SDapiUserPatronSearchCustomerMessage searchResponse; 
        try
        {
            searchResponse = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(status.getStdout());
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
        MeCardCustomerToNativeFormat createCustomer = new MeCardCustomerToSDapi(customer, QueryType.CREATE);
        // apply library centric normalization to the customer account.
        normalizer.finalize(customer, createCustomer, response);
        // Output the customer's data as a receipt in case they come back with questions.
        List<String> customerReceipt = createCustomer.getFormattedCustomer();
        if (this.debug)
        {
            System.out.println("CREATE XML body: " + createCustomer.toString());
        }
        new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.txt)
            .set(customerReceipt)
            .build();
        // /user/patron/key/2139681
        SDWebServiceCommand createCustomer = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
                .endpoint("/user/patron/key/" + userKey)
                .bodyText(createCustomer.toString())
                .sessionToken(getSessionToken(response))
                .build();
        throw new UnsupportedOperationException("Not supported yet.");
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
        String loginBodyText = authData.getStaffAuthentication("", staffId, staffPassword);
        SDWebServiceCommand command = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/staff/login")
            .bodyText(loginBodyText)
            .build();
        
        return command;
    }

    @Override
    public Command testCustomerExists(String userId, String userPin, Response response) 
    {
        // Customer has to prove they are legit allowed to update or get
        // their information. That requires knowing both user ID and password.
        //POST: https://{{HOST}}/{{WEBAPP}}/user/patron/login
        //Body: {
        //   "login": "21221012345678",
        //   "password": "64058"
        //}
        //
        //
        //Response: {
        //   "pinCreateDate": "2024-03-26",
        //   "pinExpirationDate": null,
        //   "name": "BILLY, Balzac",
        //   "sessionToken": "42c08d87-61d0-475a-8480-ddaa05b60506",
        //   "pinStatus": {
        //       "resource": "/policy/userPinStatus",
        //       "key": "A",
        //       "fields": {
        //           "policyNumber": 1,
        //           "description": "$<userpin_active_status>",
        //           "displayName": "A",
        //           "translatedDescription": "User's PIN is active"
        //       }
        //   },
        //   "patronKey": "301585",
        //   "message": null
        //}
        //Failed Response: (Code 401)
        //{
        //   "messageList": [
        //       {
        //           "code": "unableToLogin",
        //           "message": "Unable to log in."
        //       }
        //   ]
        //}
        // This query relies on the user being able to login but 
        // another method is to allow a staff search of the customer with 
        // https://{{HOST}}/{{WEBAPP}}/user/patron/login
        SDapiAuthenticationData authData = new SDapiAuthenticationData();
        String loginTextBody = authData.getPatronAuthentication(userId, userPin);
        
        Command customerExists = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/patron/login")
            .bodyText(loginTextBody)
            .build();
        
        return customerExists;
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
                        (SDapiUserPatronSearchCustomerMessage) 
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
            case GET_CUSTOMER -> {
                // Search request with user barcode.
                SDapiResponse testGetCustomer;
                try
                {
                    testGetCustomer =                
                        (SDapiUserPatronSearchCustomerMessage) 
                            SDapiUserPatronSearchCustomerMessage.parseJson(status.getStdout());
                }
                catch (NullPointerException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                if (testGetCustomer.succeeded())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    if (debug) System.out.println("Customer login succeeded.");
                    return true;
                }
                // Otherwise the response was a failure.
                response.setCode(ResponseTypes.USER_NOT_FOUND);
                response.setResponse(messageProperties.getProperty(
                        MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                System.out.println("**get customer account info failed: "
                        + testGetCustomer.errorMessage());
                return false;
            }
            case UPDATE_CUSTOMER -> {
                return false;
            }
            case CREATE_CUSTOMER -> {
                
                return false;
            }
            case TEST_CUSTOMER -> {
                // This is a user login request.
                // DummyCommand puts a '1' in stdout.
                // Authentication failures return status error authentication error, or an empty JSON document.
                // The response has to match the request, which in the above is 'patron login'
                SDapiResponse testCustomerAuthenticates;
                try
                {
                    testCustomerAuthenticates = 
                            (SDapiUserPatronLoginResponse) 
                            SDapiUserPatronLoginResponse.parseJson(status.getStdout());
                }
                catch (NullPointerException se)
                {
                    System.out.println("*error, " + se.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }     
                if (testCustomerAuthenticates.succeeded())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    if (debug) System.out.println("Customer login succeeded.");
                    return true;
                }
                // Otherwise the response was a failure.
                response.setCode(ResponseTypes.USER_PIN_INVALID);
                response.setResponse(messageProperties.getProperty(
                        MessagesTypes.USERID_PIN_MISMATCH.toString()));
                System.out.println("**customer login failed, are the pin and id correct? "
                        + testCustomerAuthenticates.errorMessage());
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
        SDapiUserPatronSearchCustomerMessage customerResponse = 
                (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(stdout);
        return customerResponse;
    }
    
}
