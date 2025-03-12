/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025  Edmonton Public Library
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
public class SDapiCustomerCreateResponseTest 
{
    private final String jsonSuccess;
    private final String jsonFail;
    
    public SDapiCustomerCreateResponseTest() 
    {
        jsonSuccess = """
                      {
                         "resource": "/user/patron",
                         "key": "2280887",
                         "fields": {
                             "displayName": "Nortonopolis, Andrewzki",
                             "access": {
                                 "resource": "/policy/userAccess",
                                 "key": "PUBLIC"
                             },
                             "alternateID": "",
                             "barcode": "21221031494999",
                             "birthDate": null,
                             "checkoutLocation": {
                                 "resource": "/policy/location",
                                 "key": "CHECKEDOUT"
                             },
                             "createdDate": "2025-02-11",
                             "department": "",
                             "environment": {
                                 "resource": "/policy/environment",
                                 "key": "PUBLIC"
                             },
                             "firstName": "Andrewzki",
                             "language": {
                                 "resource": "/policy/language",
                                 "key": "ENGLISH"
                             },
                             "lastName": "Nortonopolis",
                             "library": {
                                 "resource": "/policy/library",
                                 "key": "EPLMNA"
                             },
                             "middleName": "",
                             "preferredName": "",
                             "privilegeExpiresDate": null,
                             "profile": {
                                 "resource": "/policy/userProfile",
                                 "key": "EPL_ADULT"
                             },
                             "suffix": "",
                             "title": "",
                             "usePreferredName": false,
                             "webAuthID": "",
                             "category01": null,
                             "category02": null,
                             "category03": null,
                             "category04": null,
                             "category05": null,
                             "category06": null,
                             "category07": null,
                             "category08": null,
                             "category09": null,
                             "category10": null,
                             "category11": null,
                             "category12": null,
                             "claimsReturnedCount": 0,
                             "standing": {
                                 "resource": "/policy/patronStanding",
                                 "key": "OK"
                             },
                             "groupId": "",
                             "circRecordCount": 0,
                             "keepCircHistory": "NOHISTORY"
                         }
                      }
                      """;
        jsonFail = """
                   {
                      "messageList": [
                          {
                              "code": "requiredFieldMissing",
                              "message": "Missing required field: barcode."
                          },
                          {
                              "code": "requiredFieldMissing",
                              "message": "Missing required field: lastName."
                          }
                      ]
                   }
                   """;
    }

    /**
     * Test of succeeded method, of class SDapiCustomerCreateResponse.
     */
    @Test
    public void testSucceeded() 
    {
        System.out.println("==succeeded==");
        SDapiCustomerCreateResponse instance = (SDapiCustomerCreateResponse) 
                SDapiCustomerCreateResponse.parseJson(jsonSuccess);
        assertEquals(true, instance.succeeded());
        instance = (SDapiCustomerCreateResponse) 
                SDapiCustomerCreateResponse.parseJson(jsonFail);
        assertEquals(false, instance.succeeded());
    }

    /**
     * Test of errorMessage method, of class SDapiCustomerCreateResponse.
     */
    @Test
    public void testErrorMessage() 
    {
        System.out.println("==errorMessage==");
        SDapiCustomerCreateResponse instance = 
                (SDapiCustomerCreateResponse) SDapiCustomerCreateResponse.parseJson(jsonFail);
        String expResult = "Missing required field: barcode.Missing required field: lastName.";
        String result = instance.errorMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of parseJson method, of class SDapiCustomerCreateResponse.
     */
    @Test
    public void testParseJson() 
    {
        System.out.println("==parseJson==");
        SDapiCustomerCreateResponse instance = (SDapiCustomerCreateResponse) 
                SDapiCustomerCreateResponse.parseJson(jsonSuccess);
        assertEquals(true, instance.succeeded());
    }

    /**
     * Test of hasErrors method, of class SDapiCustomerCreateResponse.
     */
    @Test
    public void testHasErrors() 
    {
        System.out.println("==hasErrors==");
        SDapiCustomerCreateResponse instance = (SDapiCustomerCreateResponse) 
                SDapiCustomerCreateResponse.parseJson(jsonFail);
        assertEquals(true, instance.hasErrors());
    }
    
}
