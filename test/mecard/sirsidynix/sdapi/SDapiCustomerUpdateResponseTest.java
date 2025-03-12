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

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;
import mecard.sirsidynix.sdapi.SDapiCustomerUpdateResponse;
import mecard.sirsidynix.sdapi.SDapiResponse;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class SDapiCustomerUpdateResponseTest 
{
    private final String jsonSuccess;
    private final String jsonFail;
    
    public SDapiCustomerUpdateResponseTest() 
    {
        jsonSuccess = """
          {
          "resource": "/user/patron",
          "key": "1643600",
          "fields": {
            "displayName": "Huffman, Ian",
            "access": {
              "resource": "/policy/userAccess",
              "key": "PUBLIC"
            },
            "alternateID": "",
            "barcode": "20400260002065",
            "birthDate": "2001-10-14",
            "checkoutLocation": {
              "resource": "/policy/location",
              "key": "CHECKEDOUT"
            },
            "createdDate": "2019-05-22",
            "department": "",
            "environment": {
              "resource": "/policy/environment",
              "key": "PUBLIC"
            },
            "firstName": "Ian",
            "language": {
              "resource": "/policy/language",
              "key": "ENGLISH"
            },
            "lastName": "Huffman",
            "library": {
              "resource": "/policy/library",
              "key": "EPLMNA"
            },
            "middleName": "",
            "preferredName": "Huffman, Ian",
            "privilegeExpiresDate": "2025-08-05",
            "profile": {
              "resource": "/policy/userProfile",
              "key": "EPL_METRO"
            },
            "suffix": "",
            "title": "",
            "usePreferredName": false,
            "webAuthID": "",
            "category01": null,
            "category02": null,
            "category03": null,
            "category04": null,
            "category05": {
              "resource": "/policy/patronCategory05",
              "key": "ECONSENT"
            },
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
            "keepCircHistory": "ALLCHARGES"
          }
        }""";
        
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
     * Test of succeeded method, of class SDapiCustomerUpdateResponse.
     */
    @Test
    public void testSucceeded() 
    {
        System.out.println("==succeeded==");
        SDapiCustomerUpdateResponse instance = (SDapiCustomerUpdateResponse) 
                SDapiCustomerUpdateResponse.parseJson(jsonSuccess);
        assertEquals(true, instance.succeeded());
        instance = (SDapiCustomerUpdateResponse) 
                SDapiCustomerUpdateResponse.parseJson(jsonFail);
        assertEquals(false, instance.succeeded());
    }

    /**
     * Test of errorMessage method, of class SDapiCustomerUpdateResponse.
     */
    @Test
    public void testErrorMessage() 
    {
        System.out.println("==errorMessage==");
        SDapiCustomerUpdateResponse instance = 
                (SDapiCustomerUpdateResponse) SDapiCustomerUpdateResponse.parseJson(jsonFail);
        String expResult = "Missing required field: barcode.Missing required field: lastName.";
        String result = instance.errorMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of parseJson method, of class SDapiCustomerUpdateResponse.
     */
    @Test
    public void testParseJson() 
    {
        System.out.println("==parseJson==");
        SDapiCustomerUpdateResponse instance = (SDapiCustomerUpdateResponse) 
                SDapiCustomerUpdateResponse.parseJson(jsonSuccess);
        assertEquals(true, instance.succeeded());
    }

    /**
     * Test of hasErrors method, of class SDapiCustomerUpdateResponse.
     */
    @Test
    public void testHasErrors() 
    {
        System.out.println("==hasErrors==");
        SDapiCustomerUpdateResponse instance = (SDapiCustomerUpdateResponse) 
                SDapiCustomerUpdateResponse.parseJson(jsonFail);
        assertEquals(true, instance.hasErrors());
    }
    
}
