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


import mecard.config.SDapiUserFields;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class SDapiUserPatronSearchCustomerMessageTest 
{
    private final String userPatronSearchFullInfo;
    private final String userPatronSearchFailed;
    private final String userPatronSearchLite;
    private final String userPatronSearchLost;

    public SDapiUserPatronSearchCustomerMessageTest() 
    {
        //  /user/patron/search?rw=1&q=ID:21221012345678&includeFields=*,address1{*}
        userPatronSearchFailed = """
                     {
                        "searchQuery": "ID:212210123456789",
                        "startRow": 1,
                        "lastRow": 10,
                        "rowsPerPage": 10,
                        "totalResults": 0,
                        "result": []
                     }
                     """;
        
        userPatronSearchFullInfo = """
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
        //  GET: https://{{HOST}}/{{WEBAPP}}/user/patron/search?rw=1&q=ID:21221012345678
        userPatronSearchLite = """
                               {
                                  "searchQuery": "ID:21221012345678",
                                  "startRow": 1,
                                  "lastRow": 1,
                                  "rowsPerPage": 10,
                                  "totalResults": 1,
                                  "result": [
                                      {
                                          "resource": "/user/patron",
                                          "key": "301585"
                                      }
                                  ]
                               }
                               """;
        userPatronSearchLost = """
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
                                    ]
                                 }
                               """;
    }

    /**
     * Test of succeeded method, of class SDapiUserPatronSearchCustomerMessage.
     */
    @Test
    public void testSucceeded() {
        System.out.println("==succeeded==");
        SDapiUserPatronSearchCustomerMessage instance1 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        boolean expResult = true;
        boolean result = instance1.succeeded();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance2 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFailed);
        expResult = false;
        result = instance2.succeeded();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance3 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchLite);
        expResult = true;
        result = instance3.succeeded();
        assertEquals(expResult, result);
    }

    /**
     * Test of errorMessage method, of class SDapiUserPatronSearchCustomerMessage.
     */
    @Test
    public void testErrorMessage() 
    {
        System.out.println("==errorMessage==");
        SDapiUserPatronSearchCustomerMessage instance = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFailed);
        String expResult = "Account not found.";
        String result = instance.errorMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomerProfile method, of class SDapiUserPatronSearchCustomerMessage.
     */
    @Test
    public void testGetCustomerProfile() 
    {
        System.out.println("==getCustomerProfile==");
        SDapiUserPatronSearchCustomerMessage instance = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        String expResult = "EPL_ADULT";
        String result = instance.getCustomerProfile();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance1 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchLost);
        expResult = "LOST";
        result = instance1.getCustomerProfile();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance2 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFailed);
        expResult = "";
        result = instance2.getCustomerProfile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getField method, of class SDapiUserPatronSearchCustomerMessage.
     */
    @Test
    public void testGetField() 
    {
        System.out.println("==getField==");
//        SDapiUserPatronSearchCustomerMessage instance = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        SDapiUserPatronSearchCustomerMessage instance = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        String result = instance.getField(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString());
        assertEquals("", result);
        assertEquals("301585", instance.getField(SDapiUserFields.USER_KEY.toString()));
        assertEquals("BILLY", instance.getField(SDapiUserFields.USER_LAST_NAME.toString()));
        assertEquals("Balzac", instance.getField(SDapiUserFields.USER_FIRST_NAME.toString()));
        assertEquals("EPL_ADULT", instance.getField(SDapiUserFields.PROFILE.toString()));
        assertEquals("ilsadmins@epl.ca", instance.getField(SDapiUserFields.EMAIL.toString()));
        assertEquals("780-496-4058", instance.getField(SDapiUserFields.PHONE.toString()));
        assertEquals("2000-02-29T00:00:00", instance.getField(SDapiUserFields.USER_BIRTHDATE.toString()));
        assertEquals("", instance.getField(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()));
        assertEquals("T5J-2V4", instance.getField(SDapiUserFields.POSTALCODE.toString()));
        assertEquals("Edmonton", instance.getField(SDapiUserFields.CITY_SLASH_STATE.toString()));
        assertEquals("Edmonton", instance.getField(SDapiUserFields.CITY_SLASH_PROV.toString()));
        assertEquals("Alberta", instance.getField(SDapiUserFields.PROV.toString()));
        
        instance = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFailed);
        assertEquals("", result);
        assertEquals("", instance.getField(SDapiUserFields.USER_KEY.toString()));
    }

    /**
     * Test of getDateField method, of class SDapiUserPatronSearchCustomerMessage.
     */
    @Test
    public void testGetDateField() 
    {
        System.out.println("==getDateField==");
        SDapiUserPatronSearchCustomerMessage instance1 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        String expResult = "";
        String result = instance1.getDateField(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString());
        assertEquals(expResult, result);
        expResult = "2000-02-29T00:00:00";
        result = instance1.getDateField(SDapiUserFields.USER_BIRTHDATE.toString());
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class SDapiUserPatronSearchCustomerMessage.
     */
    @Test
    public void testIsEmpty() 
    {
        System.out.println("==isEmpty==");
        SDapiUserPatronSearchCustomerMessage instance1 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        boolean expResult = true;
        boolean result = instance1.isEmpty(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString());
        assertEquals(expResult, result);
    }

    /**
     * Test of getStanding method, of class SDapiUserPatronSearchCustomerMessage.
     */
    @Test
    public void testGetStanding() 
    {
        System.out.println("==getStanding==");
        SDapiUserPatronSearchCustomerMessage instance1 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        String expResult = "OK";
        String result = instance1.getStanding();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance2 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchLite);
        expResult = "";
        result = instance2.getStanding();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance3 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFailed);
        expResult = "";
        result = instance3.getStanding();
        assertEquals(expResult, result);
    }

    /**
     * Test of cardReportedLost method, of class SDapiUserPatronSearchCustomerMessage.
     */
    @Test
    public void testCardReportedLost() 
    {
        System.out.println("==cardReportedLost==");
        SDapiUserPatronSearchCustomerMessage instance1 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        boolean expResult = false;
        boolean result = instance1.cardReportedLost();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance2 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchLite);
        expResult = false;
        result = instance2.cardReportedLost();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance3 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFailed);
        expResult = false;
        result = instance3.cardReportedLost();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance4 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchLost);
        expResult = true;
        result = instance4.cardReportedLost();
        assertEquals(expResult, result);
    }

    /**
     * Test of isInGoodStanding method, of class SDapiUserPatronSearchCustomerMessage.
     */
    @Test
    public void testIsInGoodStanding() 
    {
        System.out.println("==isInGoodStanding==");
        SDapiUserPatronSearchCustomerMessage instance1 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        boolean expResult = true;
        boolean result = instance1.isInGoodStanding();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance2 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchLite);
        expResult = false;
        result = instance2.isInGoodStanding();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance3 = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFailed);
        expResult = false;
        result = instance3.isInGoodStanding();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTotalResults method, of class SDapiUserPatronSearchCustomerMessage.
     */
    @Test
    public void testGetTotalResults() {
        System.out.println("==getTotalResults==");
        SDapiUserPatronSearchCustomerMessage instance1 = 
                (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        int expResult = 1;
        int result = instance1.getTotalResults();
        assertEquals(expResult, result);
        
        SDapiUserPatronSearchCustomerMessage instance2 = 
                (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFailed);
        expResult = 0;
        result = instance2.getTotalResults();
        assertEquals(expResult, result);
    }

}
