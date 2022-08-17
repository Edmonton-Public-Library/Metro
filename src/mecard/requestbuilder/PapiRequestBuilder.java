/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
import java.util.Date;
import java.util.List;
import mecard.polaris.PapiCommand;
import mecard.polaris.PapiXmlCustomerResponse;
import java.util.Properties;
import mecard.QueryTypes;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.MessagesTypes;
import mecard.config.PapiPropertyTypes;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.customer.DumpUser;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.polaris.PapiToMeCardCustomer;
import mecard.polaris.PapiXmlStatusResponse;
import site.CustomerLoadNormalizer;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.exception.PapiException;
import mecard.polaris.MeCardCustomerToPapi;
import mecard.polaris.MeCardDataToPapiData.QueryType;
import mecard.polaris.PapiXmlPatronAuthenticateResponse;
import mecard.polaris.PapiXmlPatronBasicDataResponse;
import mecard.polaris.PapiXmlRequestPatronValidateResponse;
import mecard.polaris.PapiXmlResponse;
import mecard.polaris.PatronAuthenticationData;

/**
 * This class supports customer registration commands using the Polaris API 
 * or PAPI. This is not LEAP, that would require another request builder.
 * 
 * Polaris can use restful web services for all ILS transactions.
 * @author Andrew Nisbet andrew at dev-ils.com
 */
public class PapiRequestBuilder extends ILSRequestBuilder
{
    private final Properties messageProperties;
    private final Properties papiProperties;
    private final String baseUri;
    // The version of web services MeCard currently supports.
    private static final String WEB_SERVICE_VERSION = "7.0";
    private final boolean debug;
    
    PapiRequestBuilder(boolean debug)
    {
        // read all the properties from the Polaris table
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        this.papiProperties    = PropertyReader.getProperties(ConfigFileTypes.PAPI);
        StringBuilder uriSB    = new StringBuilder();
        uriSB.append(papiProperties.getProperty(PapiPropertyTypes.HOST.toString()))
            .append(papiProperties.getProperty(PapiPropertyTypes.AUTHENTICATE_DOMAIN.toString()))
            .append("/").append(papiProperties.getProperty(PapiPropertyTypes.VERSION.toString()))
            .append("/").append(papiProperties.getProperty(PapiPropertyTypes.LANGUAGE_ID.toString()))
            .append("/").append(papiProperties.getProperty(PapiPropertyTypes.APP_ID.toString()))
            .append("/").append(papiProperties.getProperty(PapiPropertyTypes.ORG_ID.toString()))
            .append("/");
        this.baseUri = uriSB.toString();
        this.debug   = debug;
    }
    
    
    @Override
    public final Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {

        MeCardCustomerToNativeFormat papiCustomer = new MeCardCustomerToPapi(customer, QueryType.CREATE);
        // apply library centric normalization to the customer account.
        normalizer.finalize(customer, papiCustomer, response);
        // Output the customer's data as a receipt in case they come back with questions.
        List<String> customerReceipt = papiCustomer.getFormattedCustomer();
        System.out.println("CREATE: " + papiCustomer.toString());
        new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.txt)
                .set(customerReceipt)
                .build();
        return new PapiCommand.Builder(papiProperties, "POST")
            .uri(this.baseUri + "patron")
            .bodyXML(papiCustomer.toString())
            .build();
    }
    
    @Override
    public final Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        //
        // Note Patron Update Data request: PUT /public/1/patron/{PatronBarcode}
        //  https://dewey.polarislibrary.com/PAPIService/REST/public/v1/1033/100/1/patron/21221012345678
        // with the patrons XML data as body.
        // NOTE: only email, address fields, expiration, phone_voice1,2,3 and password 
        // can be updated.
        //
        // The MeCardDataToPapiData object will filter for fields that are update-able.
        //
        // There is a userName update, but it refers to a login name 
        // for the user, not the user's name, and which is beyond the scope of this project. 
        //
        String userId = customer.get(CustomerFieldTypes.ID);
        String password = customer.get(CustomerFieldTypes.PIN);
        String authentication = PatronAuthenticationData.getAuthentication(userId, password);
        PapiCommand command = new PapiCommand.Builder(papiProperties, "POST")
            .uri(this.baseUri + "authenticator/patron")
            .bodyXML(authentication)
            .build();
        // HttpCommandStatus
        HttpCommandStatus status = (HttpCommandStatus) command.execute();
        // Can't use isSuccessful() because authentication is not a
        // query type.
        if (status.okay())
        {
            // Check response for any errors.
            PapiXmlPatronAuthenticateResponse authResponse = new PapiXmlPatronAuthenticateResponse(status.getStdout());
            if (authResponse.authenticated())
            {
                if (this.debug)
                {
                    System.out.println(new Date() + "customer " + userId + " authenticated.");
                }
                // Time to update the customer, so normalize the data.
                // we have a customer let's convert them to a PolarisSQLFormatted user.
                MeCardCustomerToNativeFormat papiCustomer = new MeCardCustomerToPapi(customer,QueryType.UPDATE);
                // apply library centric normalization to the customer account.
                normalizer.finalize(customer, papiCustomer, response);
                // Output the customer's data as a receipt in case they come back with questions.
                List<String> customerReceipt = papiCustomer.getFormattedCustomer();
                new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.txt)
                        .set(customerReceipt)
                        .build();
                command = new PapiCommand.Builder(papiProperties, "PUT")
                    .uri(this.baseUri + "patron/" + userId)
                    .bodyXML(papiCustomer.toString())
                    .build();
                command.accessToken(authResponse.getAccessSecret());
                return command;
            }
            else
            {
                System.out.println("**error web service: " + authResponse.errorMessage());
            }
        }
        // else
        return getFailedAuthenticationCommand(status, response);
    }
    
    @Override
    public final Command getStatusCommand(Response response)
    {
        // which will return the version of the API and a 200 status if available.
        // "https://dewey.polarislibrary.com/PAPIService/REST/public/v1/1033/100/1/api"
        PapiCommand command = new PapiCommand.Builder(this.papiProperties, "GET")
            .uri(baseUri + "api")
            .build();
        return command;
    }
    
    @Override
    public final Command getCustomerCommand(String userId, String userPin, Response response)
    {
        // Get the customer's user ID and PIN/Password as an XML document body.
        String authentication = PatronAuthenticationData.getAuthentication(userId, userPin);
        PapiCommand command = new PapiCommand.Builder(papiProperties, "POST")
            .uri(this.baseUri + "authenticator/patron")
            .bodyXML(authentication)
            .build();
        // HttpCommandStatus
        HttpCommandStatus status = (HttpCommandStatus) command.execute();
        // Can't use isSuccessful() because authentication is not a
        // query type.
        if (status.okay())
        {
            // Check response for any errors.
            PapiXmlPatronAuthenticateResponse authResponse = new PapiXmlPatronAuthenticateResponse(status.getStdout());
            if (authResponse.authenticated())
            {
                if (this.debug)
                {
                    System.out.println(new Date() + "customer " + userId + " authenticated.");
                }
                command = new PapiCommand.Builder(papiProperties, "GET")
                    .uri(this.baseUri + "patron/" + userId + "/basicdata?addresses=true")
                    .build();
                command.accessToken(authResponse.getAccessSecret());
                return command;
            }
            else
            {
                System.out.println("**error web service: " + authResponse.errorMessage());
            }
        }
        // else
        return getFailedAuthenticationCommand(status, response);
    }
    
    /**
     * Logs failed command and returns a dummy command with the correct MessageType.
     * 
     * This gets returned by any command that requires authentication, and that
     * authentication failed.
     * 
     * @param status - HttpCommandStatus object in this case.
     * @param response - response object for reference if needed by other processes.
     * @return DummyCommand object.
     */
    private Command getFailedAuthenticationCommand(CommandStatus status, Response response)
    {
        System.out.println(new Date() + "**error during user authentication: " + status.toString());
        // Test the result of the authentication request which is an HttpStatus object.
        String errorMessageForCustomer;
        switch(status.getStatus())
        {
            case UNAUTHORIZED:
                errorMessageForCustomer = messageProperties.getProperty(MessagesTypes.USERID_PIN_MISMATCH.toString());
                break;
            case CONFIG_ERROR:
            case BUSY:
            case UNAVAILABLE:
            case UNKNOWN:
                errorMessageForCustomer = messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString());
                break;
            default:
                errorMessageForCustomer = messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString());
                break;
        }
        response = new Response(status.getStatus());
        response.setResponse(errorMessageForCustomer);
        return new DummyCommand.Builder()
            .setStatus(1)
            .setStderr(errorMessageForCustomer)
            .build();
    }
    
    @Override
    public final NativeFormatToMeCardCustomer getFormatter()
    {
        return new PapiToMeCardCustomer();
    }

    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response)
    {
        String nullResponseMessage = " timeout because of too many tries?";
        switch (commandType)
        {
            case GET_STATUS:
                PapiXmlStatusResponse papiStatus;
                try
                {
                    papiStatus = new PapiXmlStatusResponse(status.getStdout());
                }
                catch (PapiException pe)
                {
                    System.out.println("*error, " + pe.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.UNAVAILABLE);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                    return false;
                }
                if (papiStatus.meetsMinimumApiVersion(WEB_SERVICE_VERSION))
                {
                    response.setCode(ResponseTypes.OK);
                    response.setResponse("Services up."); // doesn't require personalized message.
                    return true;
                }
                response.setCode(ResponseTypes.FAIL);
                response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                System.out.println("**error, the PAPI web service version your"
                        + " library\n currently uses is older than the version\n"
                        + "specified in the papi.properties file.\n"
                        + "Please check the ''minimum-web-service-compliance-version'\n"
                        + "value. The ILS's PAPI version is " + papiStatus.getPAPIVersion()
                        + " and the configured value is " + WEB_SERVICE_VERSION
                );
                return false;
                
            case TEST_CUSTOMER:
                // but see ILSRequestBuilder for how to implement in future.
                // DummyCommand puts a '1' in stdout.
                // Authentication failures return status error authentication error, and an empty xml document.
                PapiXmlRequestPatronValidateResponse papiTestCustomer;
                try
                {
                    papiTestCustomer = new PapiXmlRequestPatronValidateResponse(status.getStdout());
                }
                catch (PapiException pe)
                {
                    System.out.println("*error, " + pe.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }     
                if (papiTestCustomer.isValidPatron())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    if (debug) System.out.println("Get customer succeeded.");
                    return true;
                }
                response.setCode(ResponseTypes.FAIL);
                response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                System.out.println("**failed to find customer with message: " 
                            + papiTestCustomer.errorMessage());
                return false;
                
            case GET_CUSTOMER:
                PapiXmlPatronBasicDataResponse papiGetCustomer;
                try
                {
                    papiGetCustomer = new PapiXmlPatronBasicDataResponse(status.getStdout());
                }
                catch (PapiException pe)
                {
                    System.out.println("*error, " + pe.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                if (papiGetCustomer.succeeded())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    System.out.println("Get customer succeeded.");
                    return true;
                }
                response.setCode(ResponseTypes.FAIL);
                switch (status.getStatus())
                {
                    case UNAUTHORIZED:
                    case USER_PIN_INVALID:
                        response.setResponse(messageProperties.getProperty(MessagesTypes.USERID_PIN_MISMATCH.toString()));
                        break;
                    case USER_NOT_FOUND:
                    default:
                        response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                        break;
                }
                System.out.println("**error getting customer: " 
                            + papiGetCustomer.errorMessage());
                return false;
                
            case UPDATE_CUSTOMER:
                // DummyCommand puts a '1' in stdout.
                PapiXmlResponse papiUpdateCustomer;
                try
                {
                    papiUpdateCustomer = new PapiXmlResponse(status.getStdout());
                }
                catch (PapiException pe)
                {
                    System.out.println("*error, " + pe.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                if (papiUpdateCustomer.succeeded())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    System.out.println("Update customer succeeded.");
                    return true;
                }
                response.setCode(ResponseTypes.FAIL);
                switch (status.getStatus())
                {
                    case UNAUTHORIZED:
                    case USER_PIN_INVALID:
                        response.setResponse(messageProperties.getProperty(MessagesTypes.USERID_PIN_MISMATCH.toString()));
                        break;
                    case USER_NOT_FOUND:
                        response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                        break;
                    default:
                        response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()));
                        break;
                }
                System.out.println("**error update: " 
                            + papiUpdateCustomer.errorMessage());
                return false;

            case CREATE_CUSTOMER:
                PapiXmlResponse papiCreateCustomer;
                try
                {
                    papiCreateCustomer = new PapiXmlResponse(status.getStdout());
                }
                catch (PapiException pe)
                {
                    System.out.println("*error, " + pe.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.TOO_MANY_TRIES.toString()));
                    return false;
                }
                if (papiCreateCustomer.succeeded())
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    System.out.println("Create customer succeeded.");
                    return true;
                }
                else
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()));
                    System.out.println("**error create: " 
                        + papiCreateCustomer.errorMessage());
                    return false;
                }
            default:
                response.setCode(ResponseTypes.FAIL);
                response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                System.out.println("**error, the requested command " 
                        + commandType.name() + " has no test for success.");
                return false;
        }
    }

    @Override
    public boolean tidy()
    {
        return true;
    }
    
    @Override
    public String toString()
    {
        return "";
    }

    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        return new PapiXmlCustomerResponse(stdout, true);
    }

    @Override
    public Command testCustomerExists(
            String userId, 
            String userPin, 
            Response response)
    {
        // Get the customer's user ID and PIN/Password as an XML document body.
        String authentication = PatronAuthenticationData.getAuthentication(userId, userPin);
        PapiCommand command = new PapiCommand.Builder(papiProperties, "POST")
            .uri(this.baseUri + "authenticator/patron")
            .bodyXML(authentication)
            .build();
        // HttpCommandStatus
        HttpCommandStatus status = (HttpCommandStatus) command.execute();
        // Can't use isSuccessful() because authentication is not a
        // query type.
        if (status.okay())
        {
            // Check response for any errors.
            PapiXmlPatronAuthenticateResponse authResponse = new PapiXmlPatronAuthenticateResponse(status.getStdout());
            if (authResponse.authenticated())
            {
                if (this.debug)
                {
                    System.out.println(new Date() + "customer " + userId + " authenticated.");
                }
                command = new PapiCommand.Builder(papiProperties, "GET")
                    .uri(this.baseUri + "patron/" + userId)
                    .build();
                command.accessToken(authResponse.getAccessSecret());
                return command;
            }
            else
            {
                System.out.println("**error web service: " + authResponse.errorMessage());
            }
        }
        // else
        return getFailedAuthenticationCommand(status, response);
    }
}
