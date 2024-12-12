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
public class SDapiCustomerResponseTest 
{
    private String jsonString;
    public SDapiCustomerResponseTest() 
    {
        jsonString = """
                     {
                        "searchQuery": "ID:21221012345678",
                        "startRow": 1,
                        "lastRow": 1,
                        "rowsPerPage": 10,
                        "totalResults": 1,
                        "result": [
                            {
                                "resource": "/user/patron",
                                "key": "301585",
                                "fields": {
                                    "displayName": "BILLY, Balzac",
                                    "access": {
                                        "resource": "/policy/userAccess",
                                        "key": "PUBLIC"
                                    },
                                    "alternateID": "",
                                    "barcode": "21221012345678",
                                    "birthDate": "2000-02-29",
                                    "checkoutLocation": {
                                        "resource": "/policy/location",
                                        "key": "CHECKEDOUT"
                                    },
                                    "createdDate": "2003-11-24",
                                    "department": "PROD",
                                    "environment": {
                                        "resource": "/policy/environment",
                                        "key": "PUBLIC"
                                    },
                                    "firstName": "Balzac",
                                    "language": {
                                        "resource": "/policy/language",
                                        "key": "ENGLISH"
                                    },
                                    "lastName": "BILLY",
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
                                    "category02": {
                                        "resource": "/policy/patronCategory02",
                                        "key": "F"
                                    },
                                    "category03": null,
                                    "category04": {
                                        "resource": "/policy/patronCategory04",
                                        "key": "NNELS"
                                    },
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
                                    "claimsReturnedCount": 1,
                                    "standing": {
                                        "resource": "/policy/patronStanding",
                                        "key": "OK"
                                    },
                                    "groupId": "BALZAC",
                                    "address1": [
                                        {
                                            "resource": "/user/patron/address1",
                                            "key": "22778",
                                            "fields": {
                                                "code": {
                                                    "resource": "/policy/patronAddress1",
                                                    "key": "ZIP"
                                                },
                                                "data": "T5J-2V4"
                                            }
                                        },
                                        {
                                            "resource": "/user/patron/address1",
                                            "key": "87",
                                            "fields": {
                                                "code": {
                                                    "resource": "/policy/patronAddress1",
                                                    "key": "EMAIL"
                                                },
                                                "data": "ilsadmins@epl.ca"
                                            }
                                        },
                                        {
                                            "resource": "/user/patron/address1",
                                            "key": "88",
                                            "fields": {
                                                "code": {
                                                    "resource": "/policy/patronAddress1",
                                                    "key": "POSTALCODE"
                                                },
                                                "data": "T5J-2V4"
                                            }
                                        },
                                        {
                                            "resource": "/user/patron/address1",
                                            "key": "89",
                                            "fields": {
                                                "code": {
                                                    "resource": "/policy/patronAddress1",
                                                    "key": "PHONE"
                                                },
                                                "data": "780-496-4058"
                                            }
                                        },
                                        {
                                            "resource": "/user/patron/address1",
                                            "key": "22779",
                                            "fields": {
                                                "code": {
                                                    "resource": "/policy/patronAddress1",
                                                    "key": "CARE/OF"
                                                },
                                                "data": null
                                            }
                                        },
                                        {
                                            "resource": "/user/patron/address1",
                                            "key": "90",
                                            "fields": {
                                                "code": {
                                                    "resource": "/policy/patronAddress1",
                                                    "key": "STREET"
                                                },
                                                "data": "7 Sir Winston Churchill Sq."
                                            }
                                        },
                                        {
                                            "resource": "/user/patron/address1",
                                            "key": "91",
                                            "fields": {
                                                "code": {
                                                    "resource": "/policy/patronAddress1",
                                                    "key": "CITY/STATE"
                                                },
                                                "data": "Edmonton, Alberta"
                                            }
                                        }
                                    ],
                                    "circRecordCount": 0,
                                    "keepCircHistory": "ALLCHARGES"
                                }
                            }
                        ]
                     }
                     """;
    }

    /**
     * Test of succeeded method, of class SDapiCustomerResponse.
     */
    @Test
    public void testSucceeded() {
        System.out.println("==succeeded==");
        SDapiCustomerResponse instance = (SDapiCustomerResponse) SDapiCustomerResponse.parseJson(jsonString);
        boolean expResult = true;
        boolean result = instance.succeeded();
        assertEquals(expResult, result);
    }

    /**
     * Test of errorMessage method, of class SDapiCustomerResponse.
     */
    @Test
    public void testErrorMessage() {
        System.out.println("errorMessage");
        SDapiCustomerResponse instance = new SDapiCustomerResponse();
        String expResult = "";
        String result = instance.errorMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCustomerProfile method, of class SDapiCustomerResponse.
     */
    @Test
    public void testGetCustomerProfile() {
        System.out.println("==getCustomerProfile==");
        SDapiCustomerResponse instance = (SDapiCustomerResponse) SDapiCustomerResponse.parseJson(jsonString);
        String expResult = "EPL_ADULT";
        String result = instance.getCustomerProfile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getField method, of class SDapiCustomerResponse.
     */
    @Test
    public void testGetField() {
        System.out.println("getField");
        String fieldName = "";
        SDapiCustomerResponse instance = new SDapiCustomerResponse();
        String expResult = "";
        String result = instance.getField(fieldName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDateField method, of class SDapiCustomerResponse.
     */
    @Test
    public void testGetDateField() {
        System.out.println("getDateField");
        String fieldName = "";
        SDapiCustomerResponse instance = new SDapiCustomerResponse();
        String expResult = "";
        String result = instance.getDateField(fieldName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isEmpty method, of class SDapiCustomerResponse.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        String fieldName = "";
        SDapiCustomerResponse instance = new SDapiCustomerResponse();
        boolean expResult = false;
        boolean result = instance.isEmpty(fieldName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStanding method, of class SDapiCustomerResponse.
     */
    @Test
    public void testGetStanding() {
        System.out.println("getStanding");
        SDapiCustomerResponse instance = new SDapiCustomerResponse();
        String expResult = "";
        String result = instance.getStanding();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cardReportedLost method, of class SDapiCustomerResponse.
     */
    @Test
    public void testCardReportedLost() {
        System.out.println("cardReportedLost");
        SDapiCustomerResponse instance = new SDapiCustomerResponse();
        boolean expResult = false;
        boolean result = instance.cardReportedLost();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isInGoodStanding method, of class SDapiCustomerResponse.
     */
    @Test
    public void testIsInGoodStanding() {
        System.out.println("isInGoodStanding");
        SDapiCustomerResponse instance = new SDapiCustomerResponse();
        boolean expResult = false;
        boolean result = instance.isInGoodStanding();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parseJson method, of class SDapiCustomerResponse.
     */
    @Test
    public void testParseJson() {
        System.out.println("parseJson");
        String jsonString = "";
        SDapiResponse expResult = null;
        SDapiResponse result = SDapiCustomerResponse.parseJson(jsonString);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCustomerFields method, of class SDapiCustomerResponse.
     */
    @Test
    public void testGetCustomerFields() {
        System.out.println("getCustomerFields");
        SDapiCustomerResponse instance = new SDapiCustomerResponse();
        SDapiCustomerResponse.CustomerFields expResult = null;
        SDapiCustomerResponse.CustomerFields result = instance.getCustomerFields();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
