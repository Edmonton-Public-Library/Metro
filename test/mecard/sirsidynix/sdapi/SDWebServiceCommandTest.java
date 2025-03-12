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
package mecard.sirsidynix.sdapi;


import static org.junit.Assert.*;

import api.HttpCommandStatus;
import java.util.Properties;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiPropertyTypes;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.time.Duration;
import mecard.exception.ConfigurationException;
import mecard.security.SDapiSecurity;
import mecard.security.TokenManager;

/**
 *
 * @author anisbet
 */
public class SDWebServiceCommandTest 
{
    
    private final Properties sdapiProperties;
    private String staffId;
    private String staffPassword;

    public SDWebServiceCommandTest() 
    {
        this.sdapiProperties = PropertyReader.getProperties(ConfigFileTypes.SIRSIDYNIX_API);
        JsonObject userGson = new JsonObject();
        userGson.addProperty("name", "John Doe");
        userGson.addProperty("age", 30);
        userGson.addProperty("isEmployee", true);
        
        // Pretty print the JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Simple GSON example:");
        System.out.println(gson.toJson(userGson));
    }

    /**
     * Test of execute method, of class SDWebServiceCommand.
     */
    @Test
    public void testExecute() {
        System.out.println("==execute==");
////        POST: https://{{HOST}}/{{WEBAPP}}/user/staff/login
////        Body: {
////        "login": "{{staffId}}",
////        "password": "{{staffPassword}}"
////        }
        // Get staff ID and password from the .env file
        String envFilePath = sdapiProperties.getProperty(SDapiPropertyTypes.ENV.toString());
        if (envFilePath == null || envFilePath.isBlank())
        {
            throw new ConfigurationException("""
                **error, the sdapi.properties file does not contain an entry
                for the environment file (.env). The entry should look like this example:
                <entry key="env-file-path">/MeCard/path/to/.env</entry>
                Add entry or check for spelling mistakes.
                 """);
        }

        // Read the staff ID and password.
        try 
        {
            SDapiSecurity sds = new SDapiSecurity(envFilePath);
            this.staffId = sds.getStaffId();
            this.staffPassword = sds.getStaffPassword();
            System.out.println(">>>login:" + this.staffId + "\n>>>password:");
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
        
        String loginBodyText = "{\"login\":\""+this.staffId
                +"\",\"password\":\""+ this.staffPassword +"\"}";
        
        SDWebServiceCommand command = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/staff/login")
            .bodyText(loginBodyText)
            .build();
        HttpCommandStatus result = command.execute();
        System.out.println("login toString: '" + command.toString() + "'");
        System.out.println("Authentication OUTPUT: '" + result.getStdout() + "'");
        
        System.out.println("==toString==\n" + command.toString());
        
        
        // Test token manager methods.
        TokenManager tokenManager = new TokenManager();
        tokenManager.writeTokenFromStdout("sessionToken", result.getStdout(), Duration.ofMinutes(10));
        assertNotNull(result);
        assertTrue(result.getStatus() == ResponseTypes.SUCCESS);
        assertFalse(tokenManager.isTokenExpired(50L));
        
        // Test getting customer data
        String userKey = "301585";
        SDWebServiceCommand getCustomerCommand = new SDWebServiceCommand.Builder(sdapiProperties, "GET")
            .endpoint("/user/patron/key/" + userKey + "?includeFields=*,address1%7B*%7D,circRecordList%7B*%7D")
            .sessionToken(tokenManager.getToken())
            .build();
        HttpCommandStatus getCustomerResult = getCustomerCommand.execute();
        
        SDapiResponse testGetCustomerData =                
            (SDapiUserPatronKeyCustomerResponse) 
                SDapiUserPatronKeyCustomerResponse.parseJson(getCustomerResult.getStdout());
        
        System.out.println("GET_CUSTOMER data: " + testGetCustomerData.toString());
        
    }

    /**
     * Test of toString method, of class SDWebServiceCommand.
     */
    @Test
    public void testToString() 
    {
        System.out.println("==toString==");
//        POST: https://{{HOST}}/{{WEBAPP}}/user/staff/login
//        Body: {
//        "login": "{{staffId}}",
//        "password": "{{staffPassword}}"
//        }

        String envFilePath = sdapiProperties.getProperty(SDapiPropertyTypes.ENV.toString());
        if (envFilePath == null || envFilePath.isBlank())
        {
            throw new ConfigurationException("""
                **error, the sdapi.properties file does not contain an entry
                for the environment file (.env). The entry should look like this example:
                <entry key="env-file-path">/MeCard/path/to/.env</entry>
                Add entry or check for spelling mistakes.
                 """);
        }


        // Read the staff ID and password.
        try 
        {
            SDapiSecurity sds = new SDapiSecurity(envFilePath);
            this.staffId = sds.getStaffId();
            this.staffPassword = sds.getStaffPassword();
            System.out.println(">>>login:" + this.staffId + "\n>>>password:");
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

        String loginBodyText = "{\"login\":\""+this.staffId
                +"\",\"password\":\""+ this.staffPassword +"\"}";
        
        SDWebServiceCommand command = new SDWebServiceCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/staff/login")
            .bodyText(loginBodyText)
            .build();
        
        System.out.println("toString()>>>\n'" + command.toString() + "'");
    }
}
