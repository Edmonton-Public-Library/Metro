/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2026 Edmonton Public Library
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

import mecard.config.SDapiUserFields;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class SDapiUserPatronBarcodeResponseTest 
{
    private final String patronBarcodeFound;
    private final String patronNotFound;
    private final String patronLost;
    public SDapiUserPatronBarcodeResponseTest() 
    {
        patronBarcodeFound = """
                                  {
                                    "resource": "/user/patron",
                                    "key": "218427",
                                    "fields": {
                                      "displayName": "Nisbet, Andrew",
                                      "access": {
                                        "resource": "/policy/userAccess",
                                        "key": "PUBLIC"
                                      },
                                      "alternateID": "",
                                      "barcode": "21221019003333",
                                      "birthDate": "1969-08-22",
                                      "checkoutLocation": {
                                        "resource": "/policy/location",
                                        "key": "CHECKEDOUT"
                                      },
                                      "createdDate": "2020-10-09",
                                      "department": "",
                                      "environment": {
                                        "resource": "/policy/environment",
                                        "key": "PUBLIC"
                                      },
                                      "firstName": "Andrew",
                                      "language": {
                                        "resource": "/policy/language",
                                        "key": "ENGLISH"
                                      },
                                      "lastName": "Nisbet",
                                      "library": {
                                        "resource": "/policy/library",
                                        "key": "HQ"
                                      },
                                      "middleName": "",
                                      "preferredName": "Nisbet ITS STAFF, Andrew",
                                      "privilegeExpiresDate": "2027-03-10",
                                      "profile": {
                                        "resource": "/policy/userProfile",
                                        "key": "MECARD"
                                      },
                                      "suffix": "",
                                      "title": "",
                                      "usePreferredName": false,
                                      "webAuthID": "",
                                      "category01": null,
                                      "category02": {
                                        "resource": "/policy/patronCategory02",
                                        "key": "ADULT"
                                      },
                                      "category03": {
                                        "resource": "/policy/patronCategory03",
                                        "key": "UNKNOWN"
                                      },
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
                                      "address1": [
                                        {
                                          "resource": "/user/patron/address1",
                                          "key": "26",
                                          "fields": {
                                            "code": {
                                              "resource": "/policy/patronAddress1",
                                              "key": "POSTALCODE"
                                            },
                                            "data": "T5J 2V4"
                                          }
                                        },
                                        {
                                          "resource": "/user/patron/address1",
                                          "key": "27",
                                          "fields": {
                                            "code": {
                                              "resource": "/policy/patronAddress1",
                                              "key": "PHONE"
                                            },
                                            "data": "780-436-6071"
                                          }
                                        },
                                        {
                                          "resource": "/user/patron/address1",
                                          "key": "28",
                                          "fields": {
                                            "code": {
                                              "resource": "/policy/patronAddress1",
                                              "key": "STREET"
                                            },
                                            "data": "7 Sir Winston Churchill Square Nw"
                                          }
                                        },
                                        {
                                          "resource": "/user/patron/address1",
                                          "key": "31",
                                          "fields": {
                                            "code": {
                                              "resource": "/policy/patronAddress1",
                                              "key": "ALTPHONE"
                                            },
                                            "data": null
                                          }
                                        },
                                        {
                                          "resource": "/user/patron/address1",
                                          "key": "29",
                                          "fields": {
                                            "code": {
                                              "resource": "/policy/patronAddress1",
                                              "key": "EMAIL"
                                            },
                                            "data": "andrew.nisbet@epl.ca"
                                          }
                                        },
                                        {
                                          "resource": "/user/patron/address1",
                                          "key": "32",
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
                                          "key": "33",
                                          "fields": {
                                            "code": {
                                              "resource": "/policy/patronAddress1",
                                              "key": "SCHOOL"
                                            },
                                            "data": null
                                          }
                                        },
                                        {
                                          "resource": "/user/patron/address1",
                                          "key": "34",
                                          "fields": {
                                            "code": {
                                              "resource": "/policy/patronAddress1",
                                              "key": "STREET2"
                                            },
                                            "data": null
                                          }
                                        },
                                        {
                                          "resource": "/user/patron/address1",
                                          "key": "30",
                                          "fields": {
                                            "code": {
                                              "resource": "/policy/patronAddress1",
                                              "key": "CITYPROV"
                                            },
                                            "data": "Edmonton, AB"
                                          }
                                        }
                                      ],
                                      "circRecordCount": 0,
                                      "circRecordList": [],
                                      "keepCircHistory": "NOHISTORY"
                                    }
                                  }
                                  """;
        patronNotFound = """
                                    {
                                        "messageList": [
                                            {
                                                "code": "recordNotFound",
                                                "message": "Could not find a(n) /user/patron record with the key 212210123456780."
                                            }
                                        ]
                                    }
                                    """;
        patronLost = """
                {
                    "resource": "/user/patron",
                    "key": "301585",
                    "fields": {
                        "displayName": "BILLY, Balzac",
                        "access": {
                            "resource": "/policy/userAccess",
                            "key": "PUBLIC"
                        },
                        "alternateID": "21221012345677",
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
                            "key": "LOST"
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
                """;
    }

    /**
     * Test of succeeded method, of class SDapiUserPatronBarcodeResponse.
     */
    @Test
    public void testSucceeded() 
    {
        System.out.println("==succeeded==");
        PatronSearchResponse instance1 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronBarcodeFound);
        boolean expResult = true;
        boolean result = instance1.succeeded();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance2 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronNotFound);
        expResult = false;
        result = instance2.succeeded();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance3 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronLost);
        expResult = true;
        result = instance3.succeeded();
        assertEquals(expResult, result);
    }

    /**
     * Test of errorMessage method, of class SDapiUserPatronBarcodeResponse.
     */
    @Test
    public void testErrorMessage() 
    {
        System.out.println("==errorMessage==");
        PatronSearchResponse instance1 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronBarcodeFound);
        String expResult = "";
        String result = instance1.errorMessage();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance2 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronNotFound);
        expResult = "Account not found.";
        result = instance2.errorMessage();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance3 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronLost);
        expResult = "";
        result = instance3.errorMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomerProfile method, of class SDapiUserPatronBarcodeResponse.
     */
    @Test
    public void testGetCustomerProfile() 
    {
        System.out.println("==getCustomerProfile==");
        PatronSearchResponse instance1 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronBarcodeFound);
        String expResult = "MECARD";
        String result = instance1.getCustomerProfile();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance2 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronNotFound);
        expResult = "";
        result = instance2.getCustomerProfile();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance3 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronLost);
        expResult = "LOST";
        result = instance3.getCustomerProfile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getField method, of class SDapiUserPatronBarcodeResponse.
     */
    @Test
    public void testGetField() 
    {
        System.out.println("==getField==");
        PatronSearchResponse instance = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronBarcodeFound);
        
        assertEquals("218427", instance.getField(SDapiUserFields.USER_KEY.toString()));
        assertEquals("Nisbet", instance.getField(SDapiUserFields.USER_LAST_NAME.toString()));
        assertEquals("Andrew", instance.getField(SDapiUserFields.USER_FIRST_NAME.toString()));
        assertEquals("MECARD", instance.getField(SDapiUserFields.PROFILE.toString()));
        assertEquals("andrew.nisbet@epl.ca", instance.getField(SDapiUserFields.EMAIL.toString()));
        assertEquals("780-436-6071", instance.getField(SDapiUserFields.PHONE.toString()));
        assertEquals("1969-08-22T00:00:00", instance.getField(SDapiUserFields.USER_BIRTHDATE.toString()));
        assertEquals("2027-03-10T00:00:00", instance.getField(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()));
        assertEquals("T5J 2V4", instance.getField(SDapiUserFields.POSTALCODE.toString()));
        assertEquals("Edmonton", instance.getField(SDapiUserFields.CITY_SLASH_STATE.toString()));
        assertEquals("Edmonton", instance.getField(SDapiUserFields.CITYPROV.toString()));
        assertEquals("AB", instance.getField(SDapiUserFields.PROV.toString()));
        assertEquals("OK", instance.getField(SDapiUserFields.STANDING.toString()));
        assertEquals("21221019003333", instance.getField(SDapiUserFields.USER_ID.toString()));
        
        instance = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronNotFound);
        String result = instance.getField(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString());
        assertEquals("", result);
        assertEquals("", instance.getField(SDapiUserFields.USER_KEY.toString()));
    }

    /**
     * Test of getDateField method, of class SDapiUserPatronBarcodeResponse.
     */
    @Test
    public void testGetDateField() 
    {
        System.out.println("==testGetDateField==");
        PatronSearchResponse instance1 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronBarcodeFound);
        String expResult = "2027-03-10T00:00:00";
        String result = instance1.getDateField(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString());
        assertEquals(expResult, result);
        expResult = "1969-08-22T00:00:00";
        result = instance1.getDateField(SDapiUserFields.USER_BIRTHDATE.toString());
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class SDapiUserPatronBarcodeResponse.
     */
    @Test
    public void testIsEmpty() 
    {
        System.out.println("==isEmpty==");
        PatronSearchResponse instance1 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronBarcodeFound);
        boolean expResult = false;
        boolean result = instance1.isEmpty(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString());
        assertEquals(expResult, result);
        
        PatronSearchResponse instance2 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronNotFound);
        expResult = true;
        result = instance2.isEmpty(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString());
        assertEquals(expResult, result);
    }

    /**
     * Test of getStanding method, of class SDapiUserPatronBarcodeResponse.
     */
    @Test
    public void testGetStanding() 
    {
        PatronSearchResponse instance1 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronBarcodeFound);
        String expResult = "OK";
        String result = instance1.getStanding();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance2 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronNotFound);
        expResult = "";
        result = instance2.getStanding();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance3 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronLost);
        expResult = "OK";
        result = instance3.getStanding();
        assertEquals(expResult, result);
    }

    /**
     * Test of cardReportedLost method, of class SDapiUserPatronBarcodeResponse.
     */
    @Test
    public void testCardReportedLost() 
    {
        System.out.println("==cardReportedLost==");
        PatronSearchResponse instance1 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronLost);
        boolean expResult = true;
        boolean result = instance1.cardReportedLost();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance2 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronBarcodeFound);
        expResult = false;
        result = instance2.cardReportedLost();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance3 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronNotFound);
        expResult = false;
        result = instance3.cardReportedLost();
        assertEquals(expResult, result);
    }

    /**
     * Test of isInGoodStanding method, of class SDapiUserPatronBarcodeResponse.
     */
    @Test
    public void testIsInGoodStanding() 
    {
        System.out.println("==isInGoodStanding==");
        PatronSearchResponse instance1 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronBarcodeFound);
        boolean expResult = true;
        boolean result = instance1.isInGoodStanding();
        assertEquals(expResult, result);
        
        PatronSearchResponse instance2 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronNotFound);
        expResult = false;
        result = instance2.isInGoodStanding();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTotalResults method, of class SDapiUserPatronBarcodeResponse.
     */
    @Test
    public void testGetTotalResults()
    {
        System.out.println("==getTotalResults==");
        PatronSearchResponse instance1 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronBarcodeFound);
        int expResult = 1;
        int result = instance1.getTotalResults();
        assertEquals(expResult, result);
        
        // Test if the json returned is a response from /user/patron/barcode/{barcode}
        PatronSearchResponse instance3 = (SDapiUserPatronBarcodeResponse) SDapiUserPatronBarcodeResponse.parseJson(patronNotFound);
        expResult = 0;
        result = instance3.getTotalResults();
        assertEquals(expResult, result);
    }
    
}
