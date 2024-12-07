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
import api.HttpCommandStatus;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import mecard.QueryTypes;
import mecard.Response;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiPropertyTypes;
import mecard.customer.Customer;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.exception.ConfigurationException;
import mecard.security.SDapiSecurity;
import mecard.security.TokenManager;
import mecard.sirsidynix.sdapi.SDWebServiceCommand;
import mecard.sirsidynix.sdapi.SDapiAuthenticationData;
import mecard.sirsidynix.sdapi.SDapiJsonCustomerResponse;
import mecard.sirsidynix.sdapi.SDapiToMeCardCustomer;
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
    private final TokenManager tokenManager;
    @SuppressWarnings("FieldMayBeFinal")
    private long tokenExpiry;
    /**
     * Creates the Sirsi Dynix request builder.
     */
    public SDapiRequestBuilder()
    {
        // Get the SDapi properties file and set up a token manager.
        tokenManager = new TokenManager();
        sdapiProperties = PropertyReader.getProperties(ConfigFileTypes.SIRSIDYNIX_API);
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
    
    protected String getSessionToken()
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
//            if (status.okay())
//            {
//                // Check response for any errors.
//                PapiXmlPatronAuthenticateResponse authResponse = new PapiXmlPatronAuthenticateResponse(status.getStdout());
//                if (authResponse.authenticated())
//                {
//                    if (this.debug)
//                    {
//                        System.out.println(new Date() + "customer " + patronId + " authenticated.");
//                    }
//                    tokenCache.writeToCache(authResponse.getTokenExpirationAsString(), authResponse.getAccessSecret());
//                    response.setCode(ResponseTypes.SUCCESS);
//                    response.setResponse("");
//                }
//                else
//                {
//                    if (this.debug)
//                    {
//                        System.out.println(new Date() + "customer " + patronId + " FAILED to authenticate.");
//                    }
//                    response.setCode(ResponseTypes.USER_PIN_INVALID);
//                    response.setResponse(authResponse.errorMessage());
//                }
//            }
//            else // The web service send back an HTTP error, translate and return to customer.
//            {
//                System.out.println("**error web service: "
//                    + status.getHttpStatusCode() + " : " + status.toString());
//                response.setCode(status.getStatus());
//                switch (status.getStatus())
//                {
//                    case UNAUTHORIZED:
//                        response.setResponse(messageProperties.getProperty(
//                                MessagesTypes.USERID_PIN_MISMATCH.toString()));
//                        break;
//                    case CONFIG_ERROR:
//                    case BUSY:
//                    case UNAVAILABLE:
//                    case UNKNOWN:
//                        response.setResponse(messageProperties.getProperty(
//                                MessagesTypes.UNAVAILABLE_SERVICE.toString()));
//                        break;
//                    default:
//                        response.setResponse(messageProperties.getProperty(
//                                MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
//                        break;
//                }
//            }
            tokenManager.writeTokenFromStdout("sessionToken", 
                    status.getStdout(), 
                    Duration.ofMinutes(this.tokenExpiry));
        }
        
        return tokenManager.getToken();
    }

    @Override
    public NativeFormatToMeCardCustomer getFormatter() 
    {
        return new SDapiToMeCardCustomer();
    }

    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response) 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer) 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer) 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Command getStatusCommand(Response response) 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Command testCustomerExists(String userId, String userPin, Response response) 
    {
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean tidy()
    {
        return true;
    }

    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        SDapiJsonCustomerResponse customerResponse = new SDapiJsonCustomerResponse();
        customerResponse.parseJson(stdout);
        return customerResponse;
    }
    
}
