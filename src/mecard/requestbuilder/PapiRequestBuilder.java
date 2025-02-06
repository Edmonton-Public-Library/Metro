/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022 - 2024  Edmonton Public Library
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

import mecard.polaris.papi.PapiXmlStaffAuthenticateResponse;
import api.Command;
import api.CommandStatus;
import api.CustomerMessage;
import api.HttpCommandStatus;
import java.util.Date;
import java.util.List;
import mecard.polaris.papi.PapiCommand;
import mecard.polaris.papi.PapiXmlCustomerResponse;
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
import mecard.polaris.papi.PapiToMeCardCustomer;
import mecard.polaris.papi.PapiXmlStatusResponse;
import site.CustomerLoadNormalizer;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.exception.ConfigurationException;
import mecard.exception.PapiException;
import mecard.polaris.papi.MeCardCustomerToPapi;
import mecard.polaris.papi.MeCardDataToPapiData.QueryType;
import mecard.polaris.papi.PapiXmlPatronAuthenticateResponse;
import mecard.polaris.papi.PapiXmlPatronBasicDataResponse;
import mecard.polaris.papi.PapiXmlRequestPatronValidateResponse;
import mecard.polaris.papi.PapiXmlResponse;
import mecard.polaris.papi.PapiAuthenticationData;
import mecard.polaris.papi.TokenCache;
import mecard.security.AuthenticationData;

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
//    private final String baseUri;
    // The version of web services MeCard currently supports.
    private final String WEB_SERVICE_VERSION;
    private final boolean debug;
    private final boolean runAsStaff;
    private final String internalDomain;
    private final String staffPassword;
    private final String staffAccessId;
    private final String host;
    private final String restPath;
    private final String apiVersion;
    private final String languageId;
    private final String appId;
    private final String orgId;
    
    PapiRequestBuilder(boolean debug)
    {
        // read all the properties from the Polaris table
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        this.papiProperties    = PropertyReader.getProperties(ConfigFileTypes.PAPI);
        this.loadDir           = papiProperties.getProperty(PapiPropertyTypes.LOAD_DIR.toString());
        this.host              = papiProperties.getProperty(PapiPropertyTypes.HOST.toString());
        this.restPath          = papiProperties.getProperty(PapiPropertyTypes.REST_PATH.toString());
        this.apiVersion        = papiProperties.getProperty(PapiPropertyTypes.VERSION.toString());
        this.languageId        = papiProperties.getProperty(PapiPropertyTypes.LANGUAGE_ID.toString());
        this.appId             = papiProperties.getProperty(PapiPropertyTypes.APP_ID.toString());
        this.orgId             = papiProperties.getProperty(PapiPropertyTypes.ORG_ID.toString());
        // Get the papi version because iii removes functions from API over minor
        // versions of their web service. For example 7.0 allows query of the api version
        // 7.1 does not! Default to 7.0, it has more functionality than 7.1.
        this.WEB_SERVICE_VERSION = papiProperties.getProperty(
                PapiPropertyTypes.PAPI_VERSION.toString());
        this.debug = Boolean.parseBoolean(papiProperties.getProperty(PapiPropertyTypes.DEBUG.toString(),"false"));
        // All these are needed to run the service as staff.
        this.internalDomain = papiProperties.getProperty(PapiPropertyTypes.INTERNAL_DOMAIN.toString());
        this.staffPassword  = papiProperties.getProperty(PapiPropertyTypes.STAFF_PASSWORD.toString());
        this.staffAccessId  = papiProperties.getProperty(PapiPropertyTypes.STAFF_ID.toString());
        if (this.internalDomain.isEmpty())
        {
            this.runAsStaff = false;
        }
        else if (this.staffAccessId.isEmpty())
        {
            this.runAsStaff = false;
        }
        else if (this.staffPassword.isEmpty())
        {
            this.runAsStaff = false;
        }
        else
        {
            this.runAsStaff = true;
        }
    }
    
    private String getProtectedBaseUri()
    {
        StringBuilder uriSB = new StringBuilder();
        uriSB.append(this.host)
            .append(this.restPath)
            .append("/").append("protected")
            .append("/").append(this.apiVersion)
            .append("/").append(this.languageId)
            .append("/").append(this.appId)
            .append("/").append(this.orgId)
            .append("/");
        return uriSB.toString();
    }
    
    private String getPublicBaseUri()
    {
        StringBuilder uriSB = new StringBuilder();
        uriSB.append(this.host)
            .append(this.restPath)
            .append("/").append("public")
            .append("/").append(this.apiVersion)
            .append("/").append(this.languageId)
            .append("/").append(this.appId)
            .append("/").append(this.orgId)
            .append("/");
        return uriSB.toString();
    }
    
    /**
     * Gets the patron's access token if available. If the token is not available
     * because it expired or has never been made, a new one will be 
     * generated and returned.
     * 
     * @param domain - Internal domain name. Example: 'SB-DEWEY'
     * @param accessId - The user name of the staff account.
     * @param password - The password for the staff account.
     * @return The current valid access token for staff.
     */
    private String getStaffAccessToken(String domain, String accessId, String password)
    {
        TokenCache tokenCache = new TokenCache(accessId, this.loadDir);
        // If the token has expired or there is no token, the returned string
        // will be empty.
        if (tokenCache.getValidToken().isEmpty())
        {
            AuthenticationData authData = (AuthenticationData) new PapiAuthenticationData();
            String authentication = authData.getStaffAuthentication(domain, accessId, password);
            if (this.debug)
            {
                System.out.println("AUTHENTICATE XML body: " + authentication);
            }
            PapiCommand command = new PapiCommand.Builder(papiProperties, "POST")
                .uri(this.getProtectedBaseUri() + "authenticator/staff")
                .debug(this.debug)
                .bodyXML(authentication)
                .build();
            System.out.println(new Date() + "DEBUG 'POST' auth to URL: " + this.getProtectedBaseUri() + "authenticator/staff");
            // HttpCommandStatus has methods for testing if there was HTTP errors
            HttpCommandStatus status = (HttpCommandStatus) command.execute();
            System.out.println(new Date() + "staff authentication response: " + status.toString());
            // Can't use isSuccessful() because authentication is not a
            // query type, so test here.
            if (status.okay())
            {
                // Check response for any errors.
                PapiXmlStaffAuthenticateResponse authResponse = new PapiXmlStaffAuthenticateResponse(status.getStdout());
                if (authResponse.authenticated())
                {
                    if (this.debug)
                    {
                        System.out.println(new Date() + "staff authenticated.");
                    }
                    tokenCache.writeToCache(authResponse.getTokenExpirationAsString(), authResponse.getAccessSecret());
                }
                else
                {
                    // Fail fast (during testing) so IT knows that there is a 
                    // problem with staff credentials.
                    if (this.debug)
                    {
                        System.out.println(new Date() + "staff failed to authenticate with message: '" + authResponse.errorMessage() + "'");
                    }
                    throw new ConfigurationException("**error authenticating staff: \n" 
                        + authResponse.errorMessage());
                }
            }
            else
            {
                // Fail fast (during testing) so IT knows that there is a 
                // problem with staff credentials.
                throw new ConfigurationException("**error authenticating staff: \n" 
                    + status.getHttpStatusCode() + " : " + status.toString());
            }
        }
        return tokenCache.getValidToken();
    }
    
    /**
     * Gets the patron's access token if available. If the token is not available
     * a new one will be generated and returned.
     * 
     * @param patronId - The user bar code of the patron.
     * @param password - The password for the patron account.
     * @return The current valid access token for patron.
     */
    private String getPatronAccessToken(String patronId, String password, Response response)
    {
        TokenCache tokenCache = new TokenCache(patronId, this.loadDir);
        // If the token has expired or there is no token, the returned string
        // will be empty.
        if (tokenCache.getValidToken().isEmpty())
        {
            AuthenticationData authData = (AuthenticationData) new PapiAuthenticationData();
            String authentication = authData.getPatronAuthentication(patronId, password);
            if (this.debug)
            {
                System.out.println("AUTHENTICATE XML body: " + authentication);
            }
            PapiCommand command = new PapiCommand.Builder(papiProperties, "POST")
                .uri(this.getPublicBaseUri() + "authenticator/patron")
                .bodyXML(authentication)
                .debug(this.debug)
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
                        System.out.println(new Date() + "customer " + patronId + " authenticated.");
                    }
                    tokenCache.writeToCache(authResponse.getTokenExpirationAsString(), authResponse.getAccessSecret());
                    response.setCode(ResponseTypes.SUCCESS);
                    response.setResponse("");
                }
                else
                {
                    if (this.debug)
                    {
                        System.out.println(new Date() + "customer " + patronId + " FAILED to authenticate.");
                    }
                    response.setCode(ResponseTypes.USER_PIN_INVALID);
                    response.setResponse(authResponse.errorMessage());
                }
            }
            else // The web service send back an HTTP error, translate and return to customer.
            {
                System.out.println("**error web service: "
                    + status.getHttpStatusCode() + " : " + status.toString());
                response.setCode(status.getStatus());
                switch (status.getStatus())
                {
                    case UNAUTHORIZED -> response.setResponse(messageProperties.getProperty(
                                MessagesTypes.USERID_PIN_MISMATCH.toString()));
                    case CONFIG_ERROR, BUSY, UNAVAILABLE, UNKNOWN -> response.setResponse(messageProperties.getProperty(
                            MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                    default -> response.setResponse(messageProperties.getProperty(
                            MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                }
            }
        }
        // Which will be empty or have a valid token in it.
        return tokenCache.getValidToken();
    }
    
    
    @Override
    public final Command getCreateUserCommand(
            Customer customer, 
            Response response, 
            CustomerLoadNormalizer normalizer)
    {
        String staffSecret = this.getStaffAccessToken(this.internalDomain, this.staffAccessId, this.staffPassword); 
        MeCardCustomerToNativeFormat papiCustomer = new MeCardCustomerToPapi(customer, QueryType.CREATE);
        // apply library centric normalization to the customer account.
        normalizer.finalize(customer, papiCustomer, response);
        // Output the customer's data as a receipt in case they come back with questions.
        List<String> customerReceipt = papiCustomer.getFormattedCustomer();
        if (this.debug)
        {
            System.out.println("CREATE XML body: " + papiCustomer.toString());
        }
        new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.txt)
            .set(customerReceipt)
            .build();
        return new PapiCommand.Builder(papiProperties, "POST")
            .uri(this.getPublicBaseUri() + "patron")
            .debug(this.debug)
            .bodyXML(papiCustomer.toString())
            .staffPassword(staffSecret)
            .build();
    }
    
    @Override
    public final Command getUpdateUserCommand(
            Customer customer, 
            Response response, 
            CustomerLoadNormalizer normalizer)
    {
        //
        // Note Patron Update Data request: PUT /public/1/patron/{PatronBarcode}
        //  https://dewey.polarislibrary.com/PAPIService/REST/public/v1/1033/100/1/patron/21221012345678
        // with the patrons XML data as body.
        // NOTE: only email, address fields, expiration, phone_voice1,2,3 and password 
        // can be updated. Testing of staff account permission-related changes TBD.
        //
        // The MeCardDataToPapiData object will filter for fields that are update-able.
        //
        // There is a userName update, but it refers to a login name 
        // for the user, not the user's name, and which is beyond the scope of this project. 
        //
        String staffSecret = this.getStaffAccessToken(this.internalDomain, this.staffAccessId, this.staffPassword);
        String userId      = customer.get(CustomerFieldTypes.ID);
        String patronToken = "";
        if (! this.runAsStaff)
        {
            String password = customer.get(CustomerFieldTypes.PIN);
            patronToken = this.getPatronAccessToken(userId, password, response);
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
        if (this.debug)
        {
            System.out.println("UPDATE XML body: " + papiCustomer.toString());
        }
        PapiCommand command = new PapiCommand.Builder(papiProperties, "PUT")
            .uri(this.getPublicBaseUri() + "patron/" + userId)
            .debug(this.debug)
            .bodyXML(papiCustomer.toString())
            .staffPassword(staffSecret)
            .build();
        command.patronAccessToken(patronToken);
        return command;
    }
    
    @Override
    public final Command getStatusCommand(Response response)
    {
        // which will return the version of the API and a 200 status if available.
        // "https://dewey.polarislibrary.com/PAPIService/REST/public/v1/1033/100/1/api"
        String staffSecret = this.getStaffAccessToken(this.internalDomain, this.staffAccessId, this.staffPassword);
        PapiCommand command = new PapiCommand.Builder(this.papiProperties, "GET")
            .uri(this.getPublicBaseUri() + "api")
            .debug(this.debug)
            .staffPassword(staffSecret)
            .build();
        return command;
    }
    
    @Override
    public final Command getCustomerCommand(String userId, String userPin, Response response)
    {
        // The request must be completed by patron access or else no matter what
        // password is used, the customer's data will be returned to the ME Libraries
        // web site for the customer to confirm they consent for their information
        // to be passed on to the guest library.
        String patronAccessToken = this.getPatronAccessToken(userId, userPin, response);
        PapiCommand command = new PapiCommand.Builder(papiProperties, "GET")
            .uri(this.getPublicBaseUri() + "patron/" + userId + "/basicdata?addresses=true&notes=true")
            .debug(this.debug)
            .patronAccessToken(patronAccessToken) // which not get filled if staff password is non-empty.
            .build();
        return command;
    }
    
    @Override
    public Command testCustomerExists(
            String userId, 
            String userPin, 
            Response response)
    {
        String staffSecret = this.getStaffAccessToken(this.internalDomain, this.staffAccessId, this.staffPassword);
        String patronToken = "";
        if (! this.runAsStaff)
        {
            patronToken = this.getPatronAccessToken(userId, userPin, response);
        }
        PapiCommand command = new PapiCommand.Builder(papiProperties, "GET")
            .uri(this.getPublicBaseUri() + "patron/" + userId)
            .debug(this.debug)
            .staffPassword(staffSecret)
            .build();
        command.patronAccessToken(patronToken);
        return command;
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
            case GET_STATUS -> {
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
                System.out.println("""
                                   **error, the PAPI web service version your library
                                    currently uses is older than the version
                                   specified in the papi.properties file.
                                   Please check the ''minimum-web-service-compliance-version'
                                   value. The ILS's PAPI version is """ + papiStatus.getPAPIVersion()
                        + " and the configured value is " + WEB_SERVICE_VERSION
                );
                return false;
            }
                
            case TEST_CUSTOMER -> {
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
                response.setCode(ResponseTypes.USER_NOT_FOUND);
                response.setResponse(messageProperties.getProperty(
                        MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                System.out.println("**failed to find customer with message: "
                        + papiTestCustomer.errorMessage());
                return false;
            }
                
            case GET_CUSTOMER -> {
                PapiXmlPatronBasicDataResponse papiGetCustomer;
                try
                {
                    papiGetCustomer = new PapiXmlPatronBasicDataResponse(
                            status.getStdout());
                }
                catch (PapiException pe)
                {
                    System.out.println("*error, " + pe.getMessage() + nullResponseMessage);
                    response.setCode(ResponseTypes.TOO_MANY_TRIES);
                    response.setResponse(messageProperties.getProperty(
                            MessagesTypes.TOO_MANY_TRIES.toString()));
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
                        response.setResponse(messageProperties.getProperty(
                                MessagesTypes.USERID_PIN_MISMATCH.toString()));
                        break;
                    case USER_NOT_FOUND:
                    default:
                        response.setResponse(messageProperties.getProperty(
                                MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                        break;
                }
                System.out.println("**error getting customer: "
                        + papiGetCustomer.errorMessage());
                return false;
            }
                
            case UPDATE_CUSTOMER -> {
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
                    response.setResponse(messageProperties.getProperty(
                            MessagesTypes.TOO_MANY_TRIES.toString()));
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
                    case UNAUTHORIZED, USER_PIN_INVALID -> response.setResponse(messageProperties.getProperty(
                            MessagesTypes.USERID_PIN_MISMATCH.toString()));
                    case USER_NOT_FOUND -> response.setResponse(messageProperties.getProperty(
                            MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                    default -> response.setResponse(messageProperties.getProperty(
                            MessagesTypes.ACCOUNT_NOT_UPDATED.toString()));
                }
                System.out.println("**error update: "
                        + papiUpdateCustomer.errorMessage());
                return false;
            }

            case CREATE_CUSTOMER -> {
                PapiXmlResponse papiCreateCustomer;
                /*
                Customers are not being found in a search for customer with a given barcode, but then 
                failing the create user because of a duplicate name error (-3528 or -3529). This happens
                when a user does a lost card at their home library, and tries to update their account 
                at a Polaris library when duplicate name checking is used. On those systems the 
                first name, last name, and birthdate are checked.

                Since any error throws a PapiException we have to search for this case 
                before we let the PapiXmlResponse parse the results.
                */
                if (status.getStdout().contains(String.valueOf("-3528")) ||
                    status.getStdout().contains(String.valueOf("-3529")))
                {
                    response.setCode(ResponseTypes.DUPLICATE_USER);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.DUPLICATE_USER.toString()));
                    return false;
                }
                /*
                Also invalid birthdates are getting in from other libraries.
                The error in those cases is -3540
                */
                if (status.getStdout().contains(String.valueOf("-3540")))
                {
                    response.setCode(ResponseTypes.INVALID_BIRTHDATE);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.FAIL_MIN_AGE_TEST.toString()));
                    return false;
                }
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
    public String toString()
    {
        return "";
    }

    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        return new PapiXmlCustomerResponse(stdout, this.debug);
    }

}
