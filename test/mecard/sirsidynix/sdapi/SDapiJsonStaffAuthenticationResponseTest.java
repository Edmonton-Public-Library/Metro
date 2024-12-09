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
public class SDapiJsonStaffAuthenticationResponseTest
{

    /**
     * Test of succeeded method, of class SDapiJsonStaffAuthenticationResponse.
     */
    @Test
    public void testSucceeded()
    {
        String jsonString = """
            {
              "staffKey": "776715",
              "pinCreateDate": "2024-03-26",
              "pinExpirationDate": null,
              "name": "Web Service Requests for Online Registration",
              "sessionToken": "331789a2-e11d-4f77-ae80-3e4bd216d080",
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
              "message": null
            }
            """;
        System.out.println("==succeeded==");
        SDapiJsonStaffAuthenticationResponse testResponse = (SDapiJsonStaffAuthenticationResponse) SDapiJsonStaffAuthenticationResponse.parseJson(jsonString);
        boolean expResult = true;
//        System.out.println(">>>" + testResponse.getSessionToken());
//        System.out.println(">>>>" + testResponse.errorMessage());
        boolean result = testResponse.succeeded();
        assertEquals(expResult, result);
    }

    /**
     * Test of parseJson method, of class SDapiJsonStaffAuthenticationResponse.
     */
    @Test
    public void testParseJson() {
        String jsonString = """
            {
              "staffKey": "776715",
              "pinCreateDate": "2024-03-26",
              "pinExpirationDate": null,
              "name": "Web Service Requests for Online Registration",
              "sessionToken": "331789a2-e11d-4f77-ae80-3e4bd216d080",
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
              "message": null
            }
            """;
        System.out.println("==succeeded==");
        SDapiJsonStaffAuthenticationResponse testResponse = (SDapiJsonStaffAuthenticationResponse) SDapiJsonStaffAuthenticationResponse.parseJson(jsonString);
        boolean expResult = true;
//        System.out.println(">>>" + testResponse.getSessionToken());
//        System.out.println(">>>>" + testResponse.errorMessage());
        boolean result = testResponse.succeeded();
        assertEquals(expResult, result);
    }

    /**
     * Test of errorMessage method, of class SDapiJsonStaffAuthenticationResponse.
     */
    @Test
    public void testErrorMessage() {
        System.out.println("==errorMessage==");
//        SDapiJsonStaffAuthenticationResponse instance = new SDapiJsonStaffAuthenticationResponse();
        String jsonString = """
                     {
                         "messageList": [
                             {
                                 "code": "unableToLogin",
                                 "message": "Unable to log in."
                             }
                         ]
                     }
                     """;

        SDapiJsonStaffAuthenticationResponse testResponse = (SDapiJsonStaffAuthenticationResponse) SDapiJsonStaffAuthenticationResponse.parseJson(jsonString);
//        System.out.println(">>>" + testResponse.getSessionToken());
//        System.out.println(">>>>" + testResponse.errorMessage());
        assertEquals("Unable to log in.\n", testResponse.errorMessage());
        boolean result = testResponse.succeeded();
        assertFalse(result);
    }

    /**
     * Test of getSessionToken method, of class SDapiJsonStaffAuthenticationResponse.
     */
    @Test
    public void testGetSessionToken() {
        System.out.println("==getSessionToken==");
        String jsonString = """
            {
              "staffKey": "776715",
              "pinCreateDate": "2024-03-26",
              "pinExpirationDate": null,
              "name": "Web Service Requests for Online Registration",
              "sessionToken": "ae80-abcdeieio",
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
              "message": null
            }
            """;

        SDapiJsonStaffAuthenticationResponse testResponse = (SDapiJsonStaffAuthenticationResponse) SDapiJsonStaffAuthenticationResponse.parseJson(jsonString);
        boolean expResult = true;
//        System.out.println(">>>" + testResponse.getSessionToken());
//        System.out.println(">>>>" + testResponse.errorMessage());
        boolean result = testResponse.succeeded();
        assertEquals(expResult, result);
        assertEquals(testResponse.getSessionToken(), "ae80-abcdeieio");
    }
    
}
