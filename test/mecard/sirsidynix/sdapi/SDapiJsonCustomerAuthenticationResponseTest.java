/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either httpVersion 2 of the License, or
 * (at your option) any later httpVersion.
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

import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class SDapiJsonCustomerAuthenticationResponseTest {
    
    private final String jsonString;
    public SDapiJsonCustomerAuthenticationResponseTest() 
    {
        jsonString = """
                {
                    "pinCreateDate": "2024-03-26",
                    "pinExpirationDate": null,
                    "customerName": "BILLY, Balzac",
                    "sessionToken": "42c08d87-61d0-475a-8480-ddaa05b60506",
                    "pinStatus": {
                        "resource": "/policy/userPinStatus",
                        "key": "A",
                        "fields": {
                            "policyNumber": 1,
                            "description": "$<userpin_active_status>",
                            "displayName": "A",
                            "translatedDescription": "User's PIN is active"
                        }
                    },
                    "patronKey": "301585",
                    "message": null
                    }
            """;
    }

    /**
     * Test of parseJson method, of class SDapiJsonCustomerAuthenticationResponse.
     */
    @Test
    public void testParseJson() {
        System.out.println("==parseJson==");
        
        SDapiJsonCustomerAuthenticationResponse testResponse = 
                (SDapiJsonCustomerAuthenticationResponse) SDapiJsonCustomerAuthenticationResponse.parseJson(jsonString);
        boolean expResult = true;
//        System.out.println(">>>" + testResponse.getSessionToken());
//        System.out.println(">>>>" + testResponse.errorMessage());
        boolean result = testResponse.succeeded();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSessionToken method, of class SDapiJsonCustomerAuthenticationResponse.
     */
    @Test
    public void testGetSessionToken() {
        System.out.println("==getSessionToken==");
        SDapiJsonCustomerAuthenticationResponse testResponse = 
                (SDapiJsonCustomerAuthenticationResponse) SDapiJsonCustomerAuthenticationResponse.parseJson(jsonString);
        String expResult = "42c08d87-61d0-475a-8480-ddaa05b60506";
//        System.out.println(">>>" + testResponse.getSessionToken());
//        System.out.println(">>>>" + testResponse.errorMessage());
        String result = testResponse.getSessionToken();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomerKey method, of class SDapiJsonCustomerAuthenticationResponse.
     */
    @Test
    public void testGetCustomerKey() {
        System.out.println("==getCustomerKey==");
        SDapiJsonCustomerAuthenticationResponse testResponse = 
                (SDapiJsonCustomerAuthenticationResponse) SDapiJsonCustomerAuthenticationResponse.parseJson(jsonString);
        String expResult = "301585";
        String result = testResponse.getCustomerKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomerName method, of class SDapiJsonCustomerAuthenticationResponse.
     */
    @Test
    public void testGetCustomerName() {
        System.out.println("==getCustomerName==");
        SDapiJsonCustomerAuthenticationResponse testResponse = 
                (SDapiJsonCustomerAuthenticationResponse) SDapiJsonCustomerAuthenticationResponse.parseJson(jsonString);
        String expResult = "BILLY, Balzac";
        String result = testResponse.getCustomerName();
        assertEquals(expResult, result);
    }

    /**
     * Test of succeeded method, of class SDapiJsonCustomerAuthenticationResponse.
     */
    @Test
    public void testSucceeded() {
        
        System.out.println("==succeeded==");
        SDapiJsonCustomerAuthenticationResponse testResponse = 
                (SDapiJsonCustomerAuthenticationResponse) SDapiJsonCustomerAuthenticationResponse.parseJson(jsonString);
        boolean expResult = true;
//        System.out.println(">>>" + testResponse.getSessionToken());
//        System.out.println(">>>>" + testResponse.errorMessage());
        boolean result = testResponse.succeeded();
        assertEquals(expResult, result);
    }

    /**
     * Test of errorMessage method, of class SDapiJsonCustomerAuthenticationResponse.
     */
    @Test
    public void testErrorMessage() {
        System.out.println("==errorMessage==");
        String jsonStringError = """
                     {
                         "messageList": [
                             {
                                 "code": "unableToLogin",
                                 "message": "Unable to log in."
                             }
                         ]
                     }
                     """;

        SDapiJsonCustomerAuthenticationResponse testResponse = 
                (SDapiJsonCustomerAuthenticationResponse) SDapiJsonCustomerAuthenticationResponse.parseJson(jsonStringError);
//        System.out.println(">>>" + testResponse.getSessionToken());
//        System.out.println(">>>>" + testResponse.errorMessage());
        assertEquals("Unable to log in.\n", testResponse.errorMessage());
        boolean result = testResponse.succeeded();
        assertFalse(result);
    }
    
}
