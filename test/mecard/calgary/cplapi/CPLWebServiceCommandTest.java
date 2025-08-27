/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025  Edmonton Public Library
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
package mecard.calgary.cplapi;

import api.HttpCommandStatus;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.CPLapiPropertyTypes;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import mecard.exception.ConfigurationException;
import mecard.security.CPLapiSecurity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author anisbet
 */
public class CPLWebServiceCommandTest 
{
    
    private final Properties cplApiProperties;
    private String apiKey;

    public CPLWebServiceCommandTest() 
    {
        this.cplApiProperties = PropertyReader.getProperties(ConfigFileTypes.CPL_API);
//        JsonObject userGson = new JsonObject();
//        userGson.addProperty("name", "John Doe");
//        userGson.addProperty("age", 30);
//        userGson.addProperty("isEmployee", true);
//        
//        // Pretty print the JSON
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println("Simple GSON example:");
//        System.out.println(gson.toJson(userGson));
    }

    /**
     * Test of execute method, of class SDWebServiceCommand.
     */
    @Test
    public void testExecute() {
        System.out.println("==execute==");
        // Get staff ID and password from the .env file
        String envFilePath = cplApiProperties.getProperty(CPLapiPropertyTypes.ENV.toString());
        
        if (envFilePath == null || envFilePath.isBlank())
        {
            throw new ConfigurationException("""
                **error, the sdapi.properties file does not contain an entry
                for the environment file (.env). The entry should look like this example:
                <entry key="env-file-path">/MeCard/path/to/.env</entry>
                Add entry or check for spelling mistakes.
                 """);
        }

        // Read the API key.
        try 
        {
            CPLapiSecurity sds = new CPLapiSecurity(envFilePath);
            this.apiKey = sds.getApiKey();
        } 
        catch (IOException e) 
        {
            System.out.println("""
                **error, expected an .env file but it is missing or can't be found.
                The .env file should include entries for the API key. For example,
                API_KEY="aaaa-bbbb-ccccc-dddd"
                """ + e);
        }
        CPLWebServiceCommand command = new CPLWebServiceCommand.Builder(cplApiProperties, "GET")
            .endpoint("/GetStatus")
            .apiKey(apiKey)
            .setDebug(false)
            .build();
        
        HttpCommandStatus result = command.execute();
        System.out.print("GetStatus test");
        assertTrue(result.getHttpStatusCode() == 200);
        System.out.println(" succeeded");
        
        // Test verify if a customer exists with the wrong PIN.
        String cardNumber = "29065024681012";
        String pin = "2468101211111";
        CPLapiCardNumberPin cardNumberPin = new CPLapiCardNumberPin(cardNumber, pin);
        CPLWebServiceCommand verifyCustomerCommand = new CPLWebServiceCommand.Builder(cplApiProperties, "POST")
            .endpoint("/VerifyCustomer")
            .apiKey(this.apiKey)
            .bodyText(cardNumberPin.toString())
            .setDebug(false)
            .build();
        HttpCommandStatus verifyStatus = verifyCustomerCommand.execute();
        System.out.print("VerifyCustomer test bad PIN");
        assertFalse(verifyStatus.getHttpStatusCode() == 200);
        System.out.println(" succeeded");
        
        // Test verify with correct PIN
        // Now the correct one.
        pin = "24681012";
        cardNumberPin = new CPLapiCardNumberPin(cardNumber, pin);
        verifyCustomerCommand = new CPLWebServiceCommand.Builder(cplApiProperties, "POST")
            .endpoint("/VerifyCustomer")
            .apiKey(this.apiKey)
            .bodyText(cardNumberPin.toString())
            .setDebug(false)
            .build();
        verifyStatus = verifyCustomerCommand.execute();
        System.out.print("VerifyCustomer test good PIN");
        assertTrue(verifyStatus.getHttpStatusCode() == 200);
        System.out.println(" succeeded");
        
        
        // Test getting customer data
        cardNumber = "29065024681012";
        pin = "246810120000";
        cardNumberPin = new CPLapiCardNumberPin(cardNumber, pin);
        CPLWebServiceCommand getCustomerCommand = new CPLWebServiceCommand.Builder(cplApiProperties, "POST")
            .endpoint("/GetCustomer")
            .apiKey(this.apiKey)
            .bodyText(cardNumberPin.toString())
            .setDebug(false)
            .build();
        HttpCommandStatus status = getCustomerCommand.execute();
        CPLapiResponse testGetCustomerData =                 
                CPLapiGetCustomerResponse.parseJson(status.getStdout());
        System.out.print("GetCustomer test bad PIN");
        assertFalse(testGetCustomerData.succeeded());
        System.out.println(" succeeded");
//        System.out.println("GET_CUSTOMER data: " + testGetCustomerData.toString());
        
        // Test getting customer with correct PIN.
        pin = "24681012";
        cardNumberPin = new CPLapiCardNumberPin(cardNumber, pin);
        getCustomerCommand = new CPLWebServiceCommand.Builder(cplApiProperties, "POST")
            .endpoint("/GetCustomer")
            .apiKey(this.apiKey)
            .bodyText(cardNumberPin.toString())
            .setDebug(false)
            .build();
        status = getCustomerCommand.execute();
        testGetCustomerData =                 
                CPLapiGetCustomerResponse.parseJson(status.getStdout());
        System.out.print("GetCustomer test good PIN");
        assertTrue(testGetCustomerData.succeeded());
        System.out.println(" succeeded");
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
//        "login": "{{apiKey}}",
//        "password": "{{staffPassword}}"
//        }

//        String envFilePath = cplApiProperties.getProperty(CPLapiPropertyTypes.ENV.toString());
//        if (envFilePath == null || envFilePath.isBlank())
//        {
//            throw new ConfigurationException("""
//                **error, the sdapi.properties file does not contain an entry
//                for the environment file (.env). The entry should look like this example:
//                <entry key="env-file-path">/MeCard/path/to/.env</entry>
//                Add entry or check for spelling mistakes.
//                 """);
//        }
//
//
//        // Read the staff ID and password.
//        try 
//        {
//            CPLapiSecurity sds = new CPLapiSecurity(envFilePath);
//            this.apiKey = sds.getApiKey();
//            System.out.println(">>>api-key:" + this.apiKey);
//        } 
//        catch (IOException e) 
//        {
//            System.out.println("""
//                **error, expected an .env file but it is missing or can't be found.
//                The .env file should include entries for the API key. For example,
//                API_KEY="aaaa-bbbb-cccc-dddd"
//                """ + e);
//        }

//        String loginBodyText = "{\"api-key\":\""+this.apiKey +"\"}";
//        // TODO: Fix
//        CPLWebServiceCommand command = new CPLWebServiceCommand.Builder(cplApiProperties, "POST")
//            .endpoint("")
//            .bodyText(loginBodyText)
//            .build();
//        
//        System.out.println("toString()>>>\n'" + command.toString() + "'");
    }
}
