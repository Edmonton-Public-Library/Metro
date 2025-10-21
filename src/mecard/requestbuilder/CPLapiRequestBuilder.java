/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025 Edmonton Public Library
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
import java.util.Date;
import java.util.List;
import java.util.Properties;
import mecard.QueryTypes;
import static mecard.QueryTypes.CREATE_CUSTOMER;
import static mecard.QueryTypes.GET_CUSTOMER;
import static mecard.QueryTypes.GET_STATUS;
import static mecard.QueryTypes.TEST_CUSTOMER;
import static mecard.QueryTypes.UPDATE_CUSTOMER;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.calgary.cplapi.CPLWebServiceCommand;
import mecard.calgary.cplapi.CPLapiCardNumberPin;
import mecard.calgary.cplapi.CPLapiCustomerResponse;
import mecard.calgary.cplapi.CPLapiResponse;
import mecard.calgary.cplapi.CPLapiToMeCardCustomer;
import mecard.calgary.cplapi.MeCardCustomerToCPLapi;
import mecard.calgary.cplapi.MeCardDataToCPLapiData;
import mecard.config.ConfigFileTypes;
import mecard.config.MessagesTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiPropertyTypes;
import mecard.customer.Customer;
import mecard.customer.DumpUser;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.exception.ConfigurationException;
import mecard.security.CPLapiSecurity;
import site.CustomerLoadNormalizer;

/**
 * This class supports customer registration commands using the SirsiDynxi web
 * service API.
 * 
 * Symphony can use restful web services for all ILS transactions.
 * @author Andrew Nisbet andrew at dev-ils.com
 */
public class CPLapiRequestBuilder extends ILSRequestBuilder
{

    private final Properties cplapiProperties;
    private final Properties messageProperties;
    private final boolean debug;
    private String apiKey;

    public CPLapiRequestBuilder(boolean debug)
    {
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        this.cplapiProperties = PropertyReader.getProperties(ConfigFileTypes.CPL_API);
        String envFilePath = this.cplapiProperties.getProperty(SDapiPropertyTypes.ENV.toString());
        this.debug = debug;
        String loadDirProperty = this.cplapiProperties.getProperty(SDapiPropertyTypes.LOAD_DIR.toString());
        // Which looks like <entry key="load-dir">/home/anisbet/MeCard/logs/Customers</entry>
        this.loadDir = loadDirProperty + File.separator;
        
        if (envFilePath == null || envFilePath.isBlank())
        {
            throw new ConfigurationException("""
                **error, the cplapi.properties file does not contain an entry
                for the environment file (.env). The entry should look like this example:
                <entry key="env-file-path">/MeCard/path/to/.env</entry>
                Add entry or check for spelling mistakes.
                 """);
        }

        // Read the API key.
        try 
        {
            CPLapiSecurity cs = new CPLapiSecurity(envFilePath);
            this.apiKey = cs.getApiKey();
        } 
        catch (IOException e) 
        {
            System.out.println("""
                **error, expected an .env file but it is missing or can't be found.
                The .env file should include entries for the API key. For example,
                API_KEY="aaaa-bbbb-ccccc-dddd..."
                """ + e);
        }
    }
    
    @Override
    public NativeFormatToMeCardCustomer getFormatter() 
    {
        return new CPLapiToMeCardCustomer();
    }

    @Override
    public Command getCreateUserCommand(
            Customer customer, 
            Response response, 
            CustomerLoadNormalizer normalizer) 
    {
        // Convert the ME Card data to CPL api JSON object.
        MeCardCustomerToNativeFormat jsonNativeUpdateCustomer = 
            new MeCardCustomerToCPLapi(customer, MeCardDataToCPLapiData.QueryType.CREATE);
        // apply library centric normalization to the customer account.
        // TODO: Revisit this as it is set to use SSH and native Symphony API now.
        normalizer.finalize(customer, jsonNativeUpdateCustomer, response);
        // Output the customer's data as a receipt in case they come back with questions
        // or there was an issue with updating the account, the info sent is kept
        // until cleaned up.
        List<String> customerReceipt = jsonNativeUpdateCustomer.getFormattedCustomer();
        if (this.debug)
        {
            System.out.println("CREATE JSON body: " + jsonNativeUpdateCustomer.toString());
        }
        // Create a receipt incase something goes wrong.
        new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.json)
            .set(customerReceipt)
            .build();
        // Create the actual web service call object.
        CPLWebServiceCommand createCustomerCommand = new CPLWebServiceCommand.Builder(cplapiProperties, "POST")
            .endpoint("/AddCustomer")
            .apiKey(this.apiKey)
            .setDebug(this.debug)
            .bodyText(jsonNativeUpdateCustomer.toString())
            .build();
        return createCustomerCommand;
    }

    @Override
    public Command getUpdateUserCommand(
            Customer customer, 
            Response response, 
            CustomerLoadNormalizer normalizer) 
    {
        // Convert the ME Card data to CPL api JSON object.
        MeCardCustomerToNativeFormat jsonNativeUpdateCustomer = 
            new MeCardCustomerToCPLapi(customer, MeCardDataToCPLapiData.QueryType.UPDATE);
        // apply library centric normalization to the customer account.
        // TODO: Revisit this as it is set to use SSH and native Symphony API now.
        normalizer.finalize(customer, jsonNativeUpdateCustomer, response);
        // Output the customer's data as a receipt in case they come back with questions
        // or there was an issue with updating the account, the info sent is kept
        // until cleaned up.
        List<String> customerReceipt = jsonNativeUpdateCustomer.getFormattedCustomer();
//        if (this.debug)
//        {
            System.out.println("+++++++++UPDATE JSON body: " + jsonNativeUpdateCustomer.toString());
//        }
        // Create a receipt incase something goes wrong.
        new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.json)
            .set(customerReceipt)
            .build();
        // Create the actual web service call object.
        CPLWebServiceCommand updateCommand = new CPLWebServiceCommand.Builder(cplapiProperties, "POST")
            .endpoint("/UpdateCustomer")
            .apiKey(this.apiKey)
            .setDebug(this.debug)
            .bodyText(jsonNativeUpdateCustomer.toString())
            .build();
        return updateCommand;
    }

    @Override
    public Command getStatusCommand(Response response) 
    {
        CPLWebServiceCommand getStatusCommand = new CPLWebServiceCommand.Builder(this.cplapiProperties, "GET")
            .endpoint("/GetStatus")
            .apiKey(this.apiKey)
            .setDebug(this.debug)
            .build();
        return getStatusCommand;
    }

    @Override
    public Command testCustomerExists(String userId, String userPin, Response response) 
    {
        CPLapiCardNumberPin cardNumberPin = new CPLapiCardNumberPin(userId, userPin);
        CPLWebServiceCommand verifyCustomerCommand = 
                new CPLWebServiceCommand.Builder(this.cplapiProperties, "POST")
            .endpoint("/VerifyCustomer")
            .apiKey(this.apiKey)
            .bodyText(cardNumberPin.toString())
            .setDebug(this.debug)
            .build();
        return verifyCustomerCommand;
    }
    
    /**
     * Tests if a string contains the invalid credentials message (case-insensitive)
     * @param message The string to test
     * @return true if the message matches the pattern, false otherwise
     */
    public boolean isInvalidCredentialsMessageIgnoreCase(String message) {
        if (message == null) 
        {
            return false;
        }
        
        // Create a regex pattern that matches the message ignoring case and punctuation
        // \\W* matches any non-word characters (punctuation, spaces, etc.)
        String pattern = "(?i).*\\W*invalid\\W*credentials.*";
        return message.matches(pattern);
    }

    @Override
    public boolean isSuccessful(
            QueryTypes commandType, 
            CommandStatus status, 
            Response response) 
    {
        String nullResponseMessage = """
                                     possibly the web service is down or too busy""";
        switch (commandType)
        {
            case GET_STATUS -> {
                // Calgary does not return any payload response, just the status code.
                // Cast the status to HttpCommandStatus to get the error code.
                HttpCommandStatus commStatus = (HttpCommandStatus)status;
                if (commStatus.getHttpStatusCode() == 200)
                {
                    if (this.debug)
                    {
                        System.out.println(new Date() + " getStatus() responded as expected.");
                    }
                    response.setCode(ResponseTypes.SUCCESS);
                    return true;
                }
                // anything other than 200 is a problem, but users shouldn't see 
                // any cryptic error codes or messages.
                response.setCode(ResponseTypes.UNAVAILABLE);
                response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                // But do log it.
                if (this.debug)
                {
                    System.out.println(new Date() + """
                                   **error, the getStatus WS requested failed to respond.""");
                }
                return false;
            }
            case TEST_CUSTOMER -> {
                // Calgary does not return any payload response, just the status code.
                // Cast the status to HttpCommandStatus to get the error code.
                HttpCommandStatus httpCommandStatus = (HttpCommandStatus)status;
                if (httpCommandStatus.getHttpStatusCode() == 200)
                {
                    if (this.debug)
                    {
                        System.out.println(new Date() + " VerifyCustomer() found the customer.");
                    }
                    response.setCode(ResponseTypes.SUCCESS);
                    return true;
                }
                // else We can convert an error response using the CPLapiCustomerResponse object.
                CPLapiResponse customerResponse;
                try
                {
                    customerResponse= CPLapiCustomerResponse.parseJson(status.getStdout());
                    response.setCode(ResponseTypes.USER_NOT_FOUND);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                    if (this.debug)
                    { 
                        System.out.println(new Date() + """
                                       TEST_CUSTOMER returned: """ + customerResponse.errorMessage());
                    }
                }
                // If nothing was returned, or the JSON was broken tell the 
                // user to come back later.
                catch (NullPointerException | JsonSyntaxException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.UNAVAILABLE);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                    return false;
                }
                return false;
            }
            case GET_CUSTOMER -> {
                // Cast the status to HttpCommandStatus to get the error code.
                HttpCommandStatus httpCommandStatus = (HttpCommandStatus)status;
                if (httpCommandStatus.getHttpStatusCode() == 200)
                {
                    if (this.debug)
                    {
                        System.out.println(new Date() + " GetCustomer() succeeded.");
                    }
                    response.setCode(ResponseTypes.OK);
                    return true;
                }
                // else We can convert an error response using the CPLapiCustomerResponse object.
                CPLapiResponse customerResponse;
                try
                {
                    customerResponse = CPLapiCustomerResponse.parseJson(status.getStdout());
                    // I've sent a message to Calgary asking if we can add more
                    // error codes to help customers figure out what they might
                    // be doing wrong, they write:
                    // For authentication, we never inform the user if the card 
                    // or PIN is incorrect. They work together. This is for security purposes.
                    // which will contain the following error message:
                    // 'CardNumber/PinNumber: [Invalid Credentials.]' if there was a PIN User name error.
                    response.setCode(ResponseTypes.USER_PIN_INVALID);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.USERID_PIN_MISMATCH.toString()));
                    if (this.debug)
                    { 
                        System.out.println(new Date() + """
                            GET_CUSTOMER returned: """ + customerResponse.errorMessage());
                    }
                } 
                catch (NullPointerException | JsonSyntaxException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.UNAVAILABLE);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                    return false;
                }
                return false;
            }
            case UPDATE_CUSTOMER -> {
                // The best case; the customer is successfully updated. In this
                // case there is a status code '200', but no body text.
                HttpCommandStatus httpUpdateCommandStatus = (HttpCommandStatus)status;
                if (httpUpdateCommandStatus.getHttpStatusCode() == 200)
                {
                    if (this.debug)
                    {
                        System.out.println(new Date() + " customer updated successfully.");
                    }
                    response.setCode(ResponseTypes.SUCCESS);
                    return true;
                }
                
                // There are a bunch of issues that can happen with an update
                // and create account call.
                // If the account can't be found - this would be very unusual 
                // because a call was just made to find the account and it said
                // it existed... In any case the return body text doesn't contain
                // an error message, just a "title": "User not found."
                if (httpUpdateCommandStatus.getHttpStatusCode() == 404)
                {
                    if (this.debug)
                    {
                        System.out.println(new Date() + " customer not updated because the account wasn't found.");
                    }
                    response.setCode(ResponseTypes.USER_NOT_FOUND);
                    response.setResponse(apiKey);
                    return false;
                }
                // There is a special error if the supplied expiry is too distant
                // in the future.
                // {
                //   "type": "https://tools.ietf.org/html/rfc7231#section-6.6.4",
                //   "title": "Year must be less than 2038",
                //   "status": 503,
                //   "traceId": "400017b9-0000-f200-b63f-84710c7967bb"
                //}
                if (httpUpdateCommandStatus.getHttpStatusCode() == 503)
                {
                    if (this.debug)
                    {
                        System.out.println(new Date() + """
                            customer not updated because of a 503 error.
                            This can happen if expiry is too far in the future (>2038),
                            but there may be another issue with dates on the account.""");
                    }
                    response.setCode(ResponseTypes.FAIL);
                    String failedExpiry = messageProperties.getProperty(
                            MessagesTypes.ACCOUNT_NOT_UPDATED.toString()) +
                            " The customer's expiry may be too far in the future.";
                    response.setResponse(failedExpiry);
                    return false;
                }
                // The other errors can happen if there is missing data.
                // We should report them to the customer so they have some 
                // details when they talk to staff.
                CPLapiResponse customerErrorResponse;
                try
                {
                    customerErrorResponse = CPLapiCustomerResponse.parseJson(status.getStdout());
                    // In the worst case you will get the following message.
                    // {
                    //   "type": "https://tools.ietf.org/html/rfc7231#section-6.5.1",
                    //   "title": "One or more validation errors occurred.",
                    //   "status": 400,
                    //   "traceId": "40001-blah",
                    //   "errors": {
                    //       "firstName": [
                    //           "Firstname is required."
                    //       ],
                    //       "lastName": [
                    //           "Lastname is required."
                    //       ],
                    //       "emailAddress": [
                    //           "Email is required."
                    //       ],
                    //       "address.street": [
                    //           "Street is required."
                    //       ],
                    //       "address.city": [
                    //           "City is required."
                    //       ],
                    //       "address.province": [
                    //           "Province is required."
                    //       ],
                    //       "address.postalCode": [
                    //           "Postal code is required."
                    //       ]
                    //   }
                    //}
                    // In any and all of these cases the response object will 
                    // parse and concatinate all the meaningful data together
                    // into a human readable response message.
                    response.setCode(ResponseTypes.FAIL);
                    String completeMessage = messageProperties.getProperty(MessagesTypes.FAIL_COMPLETENESS_TEST.toString())
                            + " " + customerErrorResponse.errorMessage();
                    response.setResponse(completeMessage);
                    if (this.debug)
                    { 
                        System.out.println(new Date() + """
                            UPDATE_CUSTOMER failed with: """ + customerErrorResponse.errorMessage());
                    }
                } 
                catch (NullPointerException | JsonSyntaxException e)
                {
                    System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.UNAVAILABLE);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                    return false;
                }
                return false;
            }
            case CREATE_CUSTOMER -> {
                // The best case; the customer is successfully updated. In this
                // case there is a status code '200', but no body text.
                HttpCommandStatus httpUpdateCommandStatus = (HttpCommandStatus)status;
                if (httpUpdateCommandStatus.getHttpStatusCode() == 200)
                {
                    if (this.debug)
                    {
                        System.out.println(new Date() + " customer created!.");
                    }
                    response.setCode(ResponseTypes.SUCCESS);
                    return true;
                }
                if (httpUpdateCommandStatus.getHttpStatusCode() == 400)
                {
                    // The other errors can happen if there is missing data, 
                    // or the account already exists.
                    // We should report them to the customer so they have some 
                    // details when they talk to staff.
                    CPLapiResponse customerErrorResponse;
                    try
                    {
                        customerErrorResponse = CPLapiCustomerResponse.parseJson(status.getStdout());
                        //  {
                        //    "type": "https://tools.ietf.org/html/rfc7231#section-6.5.1",
                        //    "title": "One or more validation errors occurred.",
                        //    "status": 400,
                        //    "traceId": "40004984-0000-db00-b63f-84710c7967bb",
                        //    "errors": {
                        //        "pinNumber": [
                        //            "Pinnumber is required."
                        //        ],
                        //        "firstName": [
                        //            "Firstname is required."
                        //        ],
                        //        "lastName": [
                        //            "Lastname is required."
                        //        ],
                        //        "birthDate": [
                        //            "Birth date is required."
                        //        ],
                        //        "emailAddress": [
                        //            "Email is required."
                        //        ],
                        //        "address.street": [
                        //            "Street is required."
                        //        ],
                        //        "address.city": [
                        //            "City is required."
                        //        ],
                        //        "address.province": [
                        //            "Province is required."
                        //        ],
                        //        "address.postalCode": [
                        //            "Postal code is required."
                        //        ]
                        //    }
                        //}
                        // or 
                        //{
                        //        "type": "https://tools.ietf.org/html/rfc7231#section-6.5.1",
                        //        "title": "One or more validation errors occurred.",
                        //        "status": 400,
                        //        "traceId": "4002f3f2-0000-f800-b63f-84710c7967bb",
                        //        "errors": {
                        //            "cardNumber": [
                        //                "User already exists."
                        //            ]
                        //        }
                        //    }
                        // In any and all of these cases the response object will 
                        // parse and concatinate all the meaningful data together
                        // into a human readable response message.
                        response.setCode(ResponseTypes.FAIL);
                        response.setResponse(customerErrorResponse.errorMessage());
                        System.out.println(new Date() + """
                            CREATE_CUSTOMER failed with: """ + customerErrorResponse.errorMessage());
                        
                        return false;
                    } 
                    catch (NullPointerException | JsonSyntaxException e)
                    {
                        System.out.println("*error, " + e.getMessage() + nullResponseMessage);
                        response.setCode(ResponseTypes.UNAVAILABLE);
                        response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                        return false;
                    }
                }
                return false;
            }
            default -> { // This should never happen, but we've heard that before.
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
        CPLapiCustomerResponse customerResponse = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(stdout);
        return customerResponse;
    }

    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response) 
    {
        CPLapiCardNumberPin cardNumberPin = new CPLapiCardNumberPin(userId, userPin);
        CPLWebServiceCommand getCustomerCommand = 
                new CPLWebServiceCommand.Builder(this.cplapiProperties, "POST")
            .endpoint("/GetCustomer")
            .apiKey(this.apiKey)
            .bodyText(cardNumberPin.toString())
            .setDebug(this.debug)
            .build();
        return getCustomerCommand;
    }
    
    /**
     * This method is used for lost cards which get output to the customer load
     * directory. This directory is located in each ILS property file as a load-dir. 
     * Because the lost user has no insight into which ILS is being used the ILSBuilder
     * must be able to signal the failed customer files with the load directory.
     * 
     * @return string of cache directory defined in cplapi.properties.
     */
    @Override
    public String getCustomerLoadDirectory()
    {
        return this.loadDir;
    }
    
}
