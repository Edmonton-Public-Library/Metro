/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024 - 2026 Edmonton Public Library
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
import com.google.gson.JsonSyntaxException;
import java.io.File;
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
import mecard.sirsidynix.sdapi.PatronSearchResponse;
import mecard.sirsidynix.sdapi.SDWebServiceCommand;
import mecard.sirsidynix.sdapi.SDapiAuthenticationData;
import mecard.sirsidynix.sdapi.SDapiCustomerCreateResponse;
import mecard.sirsidynix.sdapi.SDapiCustomerUpdateResponse;
import mecard.sirsidynix.sdapi.SDapiResponse;
import mecard.sirsidynix.sdapi.SDapiUserPatronLoginResponse;
import mecard.sirsidynix.sdapi.SDapiUserPatronSearchResponse;
import mecard.sirsidynix.sdapi.SDapiUserStaffLoginResponse;
import mecard.sirsidynix.sdapi.SDapiToMeCardCustomer;
import mecard.sirsidynix.sdapi.SDapiUserPatronBarcodeResponse;
import mecard.sirsidynix.sdapi.SDapiUserPatronKeyCustomerResponse;
import mecard.webservice.WebServiceDummyCommand;
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
    private SDapiSecurity sdApiSecurity;
    /*
        From: https://support.sirsidynix.com/kb/162976
        When attempting to create or modify a user using the ILSWS Resource Oriented 
        Architecture, get the error "A station user can only change his own PIN"

        SOLUTION:
        First, be sure you are using a session token that was created by logging in as a user with staff privileges.  
        In particular the command list for the user access needs to have the Edit User Part B command active.  
        Also, you need to be using a client ID that is set to be a staff symphony interface in the client IDs 
        page of the web service administrator. 

        Setting the PIN also requires a user override, this will result in a "user privilege override Required field missing" 
        message with a promptType of USER_PRIVILEGE_OVRCD.  As described in the Acknowledging a prompt returned by an 
        ROAFault object page of the WSHS SDK documentation, use the SD-Prompt-Return header to specify this override.  
        If your override policy is XXXX, the header would look like SD-Prompt-Return:USER_PRIVILEGE_OVRCD/XXXX

        The  user privilege override code is the one that staff use in Workflows to override things like
        checkouts, and renew dates.
    */
    private String staffOverrideCode;
    
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
        String envFilePath = this.sdapiProperties.getProperty(SDapiPropertyTypes.ENV.toString());
        this.debug = debug;
        String loadDirProperty = this.sdapiProperties.getProperty(SDapiPropertyTypes.LOAD_DIR.toString());
        // Which looks like <entry key="load-dir">/home/anisbet/MeCard/logs/Customers</entry>
        this.loadDir = loadDirProperty + File.separator;
        
        
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
            this.sdApiSecurity = new SDapiSecurity(envFilePath);
            this.staffOverrideCode = this.sdApiSecurity.getStaffOverrideCode();
        } 
        catch (IOException | NullPointerException e) 
        {
            throw new ConfigurationException("""
                **error, expected an .env file but it is missing or can't be found.
                The .env file should include entries for staff ID and password. For example,
                STAFF_ID="SomeStaffId"
                STAFF_PASSWORD="SomeStaffPassword"
                STAFF_PROMPT_OVERRIDE_CODE="XXXX"
                """ + e);
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
            String staffId = sdApiSecurity.getStaffId();
            String staffPassword = sdApiSecurity.getStaffPassword();
            SDapiAuthenticationData authData = new SDapiAuthenticationData();
            String loginBodyText = authData.getStaffAuthentication("", staffId, staffPassword);
            SDWebServiceCommand command = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
                .endpoint("/user/staff/login")
                .bodyText(loginBodyText)
                .setDebug(this.debug)
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
            .setDebug(this.debug)
            .build();
        
        HttpCommandStatus status = loginCustomer.execute();
        SDapiResponse loginResponse = 
                        (SDapiUserPatronLoginResponse) 
                            SDapiUserPatronLoginResponse.parseJson(status.getStdout());
        if (this.debug)
        {
            System.out.println("GET_CUSTOMER key:" + loginResponse);
        }
        if (loginResponse.succeeded())
        {
            // Get customer info by user key.
            String userKey = loginResponse.toString();
            // String params  = "includeFields=*,address1{*},circRecordList{*}";
            SDWebServiceCommand patronKeyCommand = new SDWebServiceCommand.Builder(sdapiProperties, "GET")
                .endpoint("/user/patron/key/" + userKey + getPatronFormattedInformationQueryString())
                .sessionToken(this.getSessionToken(response))
                .setDebug(this.debug)
                .build();
            
            if (this.debug)
            {
                System.out.println(patronKeyCommand.toString());
            }
            
            return patronKeyCommand;
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
     * This method simply creates the additional query parameters for the 
     * /user/patron/{key}{barcode}/{DATA}?{your_query_here}. This is 
     * required because some versions of Symphony web services use 
     * '?includeFields=*,address1%7B*%7D,circRecordList%7B*%7D' while others
     * will fail if the address1 or circRecordsList include the additional "{*}".
     * 
     * Adding the additional optional switch 'use-starred-parameters' set to false
     * will not include the '{*}'. The default is true to be backwards compatible.
     * 
     * A cleaner way to do this would be to do it by the Web Services version
     * number but I'm not sure it is tied to the version number or a capricious 
     * notion of Sirsi Dynix developers. Their most up-to-date documentation 
     * says the '{*}' is required.
     * 
     * @return String - query appropriate for the version / configuration of 
     * the library's web services.
     */
    protected String getPatronFormattedInformationQueryString()
    {
        // Get the optional value from the sdapi.properties. By default this 
        // gets turned on, but if the key appears and it is not a valid 'false'
        // (case-insensitive), the returned value will be false, ergo, no
        // '{*}'.
        boolean useStarredParams = Boolean.parseBoolean(this.sdapiProperties.getProperty("use-starred-parameters", "true"));
        String queryParams = "includeFields=*,address1,circRecordList";
        if (useStarredParams)
            queryParams = "includeFields=*,address1%7B*%7D,circRecordList%7B*%7D";
        if (this.debug)
            System.out.println("use-starred-parameters:'"+useStarredParams+"' '"+queryParams+"'");
        return queryParams;
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
    public Command getCreateUserCommand(
            Customer customer, 
            Response response, 
            CustomerLoadNormalizer normalizer
    ) 
    {
        // Use the create user API
        // POST: https://{{HOST}}/{{WEBAPP}}/user/patron
        String sessionToken = this.getSessionToken(response);
        if (this.debug)
        {
            System.out.println("STAFF OVERRIDE CODE: " + this.staffOverrideCode);
        }
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
        new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.json)
            .set(customerReceipt)
            .build();
        
        SDWebServiceCommand createCustomerCommand = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/patron/")
            .bodyText(jsonNativeCustomer.toString())
            .sessionToken(sessionToken)
            .setOverrideCode("USER_PRIVILEGE_OVRCD/" + this.staffOverrideCode)
            .setDebug(this.debug)
            .build();
        if (this.debug)
        {
            System.out.println(createCustomerCommand.toString());
        }
        return createCustomerCommand;
    }
    
    /**
     * To date, all (modern) Sirsi Dynix web services allow the following searches:
     * '"/user/patron/search?rw=1&q=ID:"+barcode', and while SD's current documentation
     * still contains this endpoint, some system's (Chinook Arch) do not. Instead
     * their system can use either:
     * <ul>
     * <li>/user/patron/key/{USER_KEY}</li>
     * <li>/user/patron/barcode/{USER_BARCODE}</li>
     * </ul>
     * This instance of the Metro server will support '/user/patron/barcode' and
     * '/user/patron/search'. For backward compatability '/user/patron/search'
     * will be the default if 'use-patron-search-method' is not included in sdapi.properties,
     * or the value of 'use-patron-search-method' is set to 'search'. The other possible
     * value is 'barcode'.
     * 
     * @param userId Customer barcode. Can also be modified to use key.
     * @return String - endpoint of customer search preferred by the system's
     * config / version of Sirsi Dynix's web services.
     */
    protected String getPreferredPatronSearchMethod(String userId)
    {
        // Get the optional value from the sdapi.properties. By default this 
        // gets turned on, but if the key appears and it is not 'search'
        // (case-insensitive), the returned value will be '/user/patron/barcode'.
        String useSearchString = this.sdapiProperties.getProperty("use-patron-search-method", "search");
        String endPoint;
        if (useSearchString.equalsIgnoreCase("barcode"))
            endPoint = "/user/patron/barcode/" + userId + "?" + this.getPatronFormattedInformationQueryString();
        else if (useSearchString.equalsIgnoreCase("key"))
            endPoint = "/user/patron/key/" + userId + "?" + this.getPatronFormattedInformationQueryString();
        else
            // ?rw=1&q=ID:21221012345678&includeFields=*,address1{*}
            endPoint = "/user/patron/search?rw=1&q=ID:" + userId + "&" + this.getPatronFormattedInformationQueryString();
        if (this.debug)
            System.out.println("use-patron-search-method:'"+useSearchString+"' '"+endPoint+"'");
        return endPoint;
    }

    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer) 
    {
        // first get the customer's key.
        String barcode = customer.get(CustomerFieldTypes.ID);
        String endPoint = getPreferredPatronSearchMethod(barcode);
        SDWebServiceCommand searchPatron = new SDWebServiceCommand.Builder(sdapiProperties, "GET")
                .endpoint(endPoint+barcode+getPatronFormattedInformationQueryString())
                .sessionToken(this.getSessionToken(response))
                .setDebug(this.debug)
                .build();
        
        if (this.debug)
        {
            System.out.println(searchPatron.toString());
        }
        // Execute and if successful create a request for customer info by 
        // search by user key.
        HttpCommandStatus status = searchPatron.execute();
        if (! status.okay())
        {
            return getFailedWebServiceCommand(response, status);
        }
        
        // Search for customer info with the user key later.
        PatronSearchResponse searchResponse; 
        try
        {
            String useSearchString = this.sdapiProperties.getProperty("use-patron-search-method", "search");
            if (useSearchString.equalsIgnoreCase("barcode") ||
                    useSearchString.equalsIgnoreCase("key"))
            {
                searchResponse = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(status.getStdout());
            }
            else
            {
                searchResponse = (SDapiUserPatronSearchResponse) SDapiUserPatronSearchResponse.parseJson(status.getStdout());
            }
            
            if (searchResponse.succeeded())
            {
                String userKey = searchResponse.getField(SDapiUserFields.USER_KEY.toString());
                if (this.debug)
                {
                    System.out.println(new Date() + " " + barcode + " account found." +
                            "  USER'S KEY ==>'" + userKey + "'");
                }
                customer.set(CustomerFieldTypes.RESERVED, userKey);
                // Now use the customer data as the update command body.
                MeCardCustomerToNativeFormat jsonNativeUpdateCustomer = 
                        new MeCardCustomerToSDapi(customer, MeCardDataToSDapiData.QueryType.UPDATE);
                // apply library centric normalization to the customer account.
                normalizer.finalize(customer, jsonNativeUpdateCustomer, response);
                // Output the customer's data as a receipt in case they come back with questions.
                List<String> customerReceipt = jsonNativeUpdateCustomer.getFormattedCustomer();
                if (this.debug)
                {
                    System.out.println("UPDATE JSON body: " + jsonNativeUpdateCustomer.toString());
                }
                new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.json)
                    .set(customerReceipt)
                    .build();
                // /user/patron/key/2139681
                SDWebServiceCommand updateCustomerCommand = new SDWebServiceCommand.Builder(sdapiProperties, "PUT")
                        .endpoint("/user/patron/key/" + userKey)
                        .bodyText(jsonNativeUpdateCustomer.toString())
                        .sessionToken(getSessionToken(response))
                        .setOverrideCode("USER_PRIVILEGE_OVRCD/" + this.staffOverrideCode)
                        .setDebug(this.debug)
                        .build();
                return updateCustomerCommand;
            }
            else
            {
                if (this.debug)
                {
                    System.out.println(new Date() + " failed to find customer " + barcode);
                }
                response.setCode(ResponseTypes.USER_NOT_FOUND);
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
    }

    @Override
    public Command getStatusCommand(Response response) 
    {
        // This query relies on the user being able to login but 
        // another method is to allow a staff search of the customer with 
        // https://{{HOST}}/{{WEBAPP}}/user/staff/login
        SDWebServiceCommand command = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/staff/login")
            .bodyText(this.getSessionToken(response))
            .setDebug(this.debug)
            .build();
        if (this.debug)
        {
            System.out.println(command.toString());
        }
        return command;
    }

    @Override
    public Command testCustomerExists(String userId, String userPin, Response response) 
    {
        // A basic test if the customer has an account is to do a search as the staff account.
        String endPoint = this.getPreferredPatronSearchMethod(userId);
        SDWebServiceCommand testCustomerExists = new SDWebServiceCommand.Builder(sdapiProperties, "GET")
                .endpoint(endPoint)
                .sessionToken(this.getSessionToken(response))
                .setDebug(this.debug)
                .build();
        if (this.debug)
        {
            System.out.println(testCustomerExists.toString());
        }
        return testCustomerExists;
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
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                    return false;
                }
                catch (JsonSyntaxException e)
                {
                    // Otherwise the response was a failure.
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(
                            MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                    System.out.println("STDOUT: '" + status.getStdout() + "' *JsonSyntaxException: "
                        + e.getLocalizedMessage());
                    return false;
                }
                if (staffAuthenticates.succeeded())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    if (this.debug) System.out.println("Status succeeded.");
                    return true;
                }
                // Otherwise the response was a failure.
                response.setCode(ResponseTypes.FAIL);
                response.setResponse(messageProperties.getProperty(
                        MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                System.out.println("**get status failed: "
                        + staffAuthenticates.errorMessage());
                return false;
            }
            // Search for the barcode with the staff account.
            case TEST_CUSTOMER -> {
                PatronSearchResponse searchResponse; 
                try
                {
                    String useSearchString = this.sdapiProperties.getProperty("use-patron-search-method", "search");
                    if (useSearchString.equalsIgnoreCase("barcode") ||
                            useSearchString.equalsIgnoreCase("key"))
                    {
                        searchResponse = (SDapiUserPatronBarcodeResponse) 
                            SDapiUserPatronBarcodeResponse.parseJson(status.getStdout());
                    }
                    else
                    {
                        searchResponse = (SDapiUserPatronSearchResponse) 
                            SDapiUserPatronSearchResponse.parseJson(status.getStdout());
                    }
                    if (searchResponse.succeeded())
                    {
                        if (this.debug)
                        {
                            System.out.println(new Date() + " " + 
                                    searchResponse.getField(SDapiUserFields.USER_ID.toString()) + 
                                    " found.");
                        }
                        response.setCode(ResponseTypes.SUCCESS);
                        response.setResponse("");
                        return true;
                    }
                    else
                    {
                        response.setCode(ResponseTypes.USER_NOT_FOUND);
                        response.setResponse(messageProperties.getProperty(
                                MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                        System.out.println("**test customer account exists: "
                                + searchResponse.errorMessage());
                        return false;
                    }
                }
                catch (NullPointerException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                catch (JsonSyntaxException e)
                {
                    // Otherwise the response was a failure.
                    response.setCode(ResponseTypes.USER_NOT_FOUND);
                    response.setResponse(messageProperties.getProperty(
                            MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                    // Usually the web service sends plain text: 'Unable to log in.\n401'
                    System.out.println("STDOUT: '" + status.getStdout() + "' *JsonSyntaxException: "
                        + e.getLocalizedMessage());
                    return false;
                }
            }
            case GET_CUSTOMER -> {
                // Let the customer loging and see if they are legit.
                SDapiResponse testGetCustomerData;
                try
                {
                    testGetCustomerData =                
                        (SDapiUserPatronKeyCustomerResponse) 
                            SDapiUserPatronKeyCustomerResponse.parseJson(status.getStdout());
                    if (testGetCustomerData.succeeded())
                    {
                        response.setCode(ResponseTypes.SUCCESS);
                        if (debug)
                        {
                            System.out.println("GET CUSTOMER succeeded.");
                        }
                        return true;
                    }
                    else
                    {
                        // Otherwise the response was a failure.
                        response.setCode(ResponseTypes.USER_NOT_FOUND);
                        response.setResponse(messageProperties.getProperty(
                                MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                        System.out.println("**get (or test) customer account info failed: "
                                + testGetCustomerData.errorMessage());
                        return false;
                    }
                }
                catch (NullPointerException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                catch (JsonSyntaxException e)
                {
                    // Otherwise the response was a failure.
                    response.setCode(ResponseTypes.USER_NOT_FOUND);
                    response.setResponse(messageProperties.getProperty(
                            MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                    // Usually the web service sends plain text: 'Unable to log in.\n401'
                    System.out.println("STDOUT: '" + status.getStdout() + "' *JsonSyntaxException: "
                        + e.getLocalizedMessage());
                    return false;
                }
            }
            case UPDATE_CUSTOMER -> {
                try
                {
                    SDapiResponse customerUpdated =                
                        (SDapiCustomerUpdateResponse) 
                            SDapiCustomerUpdateResponse.parseJson(status.getStdout());
                    if (customerUpdated.succeeded())
                    {
                        response.setCode(ResponseTypes.SUCCESS);
                        response.setResponse(messageProperties.getProperty(MessagesTypes.SUCCESS_UPDATE.toString()));
                        if (debug) System.out.println("Customer updated.");
                        return true;
                    }
                    else
                    {
                        // Otherwise the response was a failure.
                        response.setCode(ResponseTypes.FAIL);
                        response.setResponse(messageProperties.getProperty(
                                MessagesTypes.ACCOUNT_NOT_UPDATED.toString()));
                        System.out.println("**update account failed: "
                                + customerUpdated.errorMessage());
                        return false;
                    }
                }
                catch (NullPointerException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                catch (JsonSyntaxException e)
                {
                    // Otherwise the response was a failure.
                    response.setCode(ResponseTypes.USER_NOT_FOUND);
                    response.setResponse(messageProperties.getProperty(
                            MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                    // Usually the web service sends plain text: 'Unable to log in.\n401'
                    System.out.println("STDOUT: '" + status.getStdout() + "' *JsonSyntaxException: "
                        + e.getLocalizedMessage());
                    return false;
                }
                
            }
            case CREATE_CUSTOMER -> {
                try
                {
                    SDapiResponse customerCreated =                
                        (SDapiCustomerCreateResponse) 
                            SDapiCustomerCreateResponse.parseJson(status.getStdout());
                    if (customerCreated.succeeded())
                    {
                        response.setCode(ResponseTypes.SUCCESS);
                        response.setResponse(messageProperties.getProperty(MessagesTypes.SUCCESS_JOIN.toString()));
                        if (debug) System.out.println("Customer created.");
                        return true;
                    }
                    else
                    {
                        // Otherwise the response was a failure.
                        response.setCode(ResponseTypes.FAIL);
                        response.setResponse(messageProperties.getProperty(
                                MessagesTypes.ACCOUNT_NOT_CREATED.toString()));
                        System.out.println("**create failed: "
                                + customerCreated.errorMessage());
                        return false;
                    }
                }
                catch (NullPointerException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                catch (JsonSyntaxException e)
                {
                    // Otherwise the response was a failure.
                    response.setCode(ResponseTypes.UNAVAILABLE);
                    response.setResponse(messageProperties.getProperty(
                            MessagesTypes.ACCOUNT_NOT_CREATED.toString()));
                    // Usually the web service sends plain text: 'Unable to log in.\n401'
                    System.out.println("STDOUT: '" + status.getStdout() + "' *JsonSyntaxException: "
                        + e.getLocalizedMessage());
                    return false;
                }
            }
            default -> {
                response.setCode(ResponseTypes.CONFIG_ERROR);
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
        String useSearchString = this.sdapiProperties.getProperty("use-patron-search-method", "search");
        PatronSearchResponse customerResponse;
        if (useSearchString.equalsIgnoreCase("barcode") ||
                useSearchString.equalsIgnoreCase("key"))
        {
            customerResponse = (SDapiUserPatronBarcodeResponse) 
                SDapiUserPatronBarcodeResponse.parseJson(stdout);
        }
        else
        {
            customerResponse = (SDapiUserPatronSearchResponse) 
                SDapiUserPatronSearchResponse.parseJson(stdout);
        }
//        SDapiUserPatronSearchResponse customerResponse = 
//                (SDapiUserPatronSearchResponse) SDapiUserPatronSearchResponse.parseJson(stdout);
        return customerResponse;
    }
    
    /**
     * This method is used for lost cards which get output to the customer load
     * directory. This directory is located in each ILS property file as a load-dir. 
     * Because the lost user has no insight into which ILS is being used the ILSBuilder
     * must be able to signal the failed customer files with the load directory.
     * 
     * @return string of cache directory defined in sdapi.properties.
     */
    @Override
    public String getCustomerLoadDirectory()
    {
        return this.loadDir;
    }
    
}
