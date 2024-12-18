
package mecard.sirsidynix.sdapi;


import mecard.config.SDapiUserFields;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class SDapiUserPatronKeyCustomerMessageTest 
{
    private final String jsonSuccess;
    private final String jsonSuccess2;
    private final String jsonFail;
    
    public SDapiUserPatronKeyCustomerMessageTest() 
    {
        jsonFail = """
                   {
                        "messageList": [
                            {
                                "code": "recordNotFound",
                                "message": "Could not find a(n) /user/patron record with the key 3015800."
                            }
                        ]
                   }
                   """;
        
        jsonSuccess = """
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
                            "circRecordList": [],
                            "keepCircHistory": "ALLCHARGES"
                        }
                     }
                     """;
        
        // GET:https://{{HOST}}/{{WEBAPP}}/user/patron/key/301585?includeFields=*,circRecordList{*},address1{*}
        this.jsonSuccess2 = """
                             {
                                "resource": "/user/patron",
                                "key": "301585",
                                "fields": {
                                    "displayName": "BILLY, Balzac",
                                    "access": {
                                        "resource": "/policy/userAccess",
                                        "key": "PUBLIC"
                                    },
                                    "alternateID": "21221087654321",
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
                                        "key": "BARRED"
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
                                    "circRecordList": [],
                                    "keepCircHistory": "ALLCHARGES"
                                }
                             }
                             """;
    }

    /**
     * Test of succeeded method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testSucceeded() 
    {
        System.out.println("==succeeded==");
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(jsonSuccess);
        boolean expResult = true;
        boolean result = instance.succeeded();
        assertEquals(expResult, result);
    }

    /**
     * Test of errorMessage method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testErrorMessage() 
    {
        System.out.println("==errorMessage==");
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(jsonFail);
        String expResult = "recordNotFound";
        String result = instance.errorMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFields method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testGetField() 
    {
        System.out.println("==getField==");
//        SDapiUserPatronSearchCustomerMessage instance = (SDapiUserPatronSearchCustomerMessage) SDapiUserPatronSearchCustomerMessage.parseJson(userPatronSearchFullInfo);
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(jsonSuccess);
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
        
        instance = (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(jsonSuccess2);
        assertEquals("21221087654321", instance.getField(SDapiUserFields.USER_ALTERNATE_ID.toString()));
        assertEquals("301585", instance.getField(SDapiUserFields.USER_KEY.toString()));
    }

    /**
     * Test of getUserKey method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testGetUserKey() 
    {
        System.out.println("==getUserKey==");
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess);
        String expResult = "301585";
        String result = instance.getUserKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAlternateId method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testGetAlternateId() 
    {
        System.out.println("==getAlternateId==");
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess2);
        String expResult = "21221087654321";
        String result = instance.getAlternateId();
        assertEquals(expResult, result);
        
        instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess);
        expResult = "";
        result = instance.getAlternateId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomerProfile method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testGetCustomerProfile() 
    {
        System.out.println("==getCustomerProfile==");
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess);
        String expResult = "EPL_ADULT";
        String result = instance.getCustomerProfile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDateField method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testGetDateField()
    {
        System.out.println("==getDateField==");
        String fieldName = SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString();
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess);
        String expResult = "";
        String result = instance.getDateField(fieldName);
        assertEquals(expResult, result);
        
        fieldName = SDapiUserFields.USER_BIRTHDATE.toString();
        instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess2);
        expResult = "2000-02-29T00:00:00";
        result = instance.getDateField(fieldName);
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testIsEmpty() 
    {
        System.out.println("==isEmpty==");
        String fieldName = SDapiUserFields.CATEGORY07.toString();
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess);
        boolean expResult = true;
        boolean result = instance.isEmpty(fieldName);
        assertEquals(expResult, result);
        
        fieldName = SDapiUserFields.USER_FIRST_NAME.toString();
        instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess);
        expResult = false;
        result = instance.isEmpty(fieldName);
        assertEquals(expResult, result);
    }

    /**
     * Test of getStanding method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testGetStanding() 
    {
        System.out.println("==getStanding==");
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess);
        String expResult = "OK";
        String result = instance.getStanding();
        assertEquals(expResult, result);
        
        instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess2);
        expResult = "BARRED";
        result = instance.getStanding();
        assertEquals(expResult, result);
    }

    /**
     * Test of cardReportedLost method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testCardReportedLost() 
    {
        System.out.println("==cardReportedLost==");
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess);
        boolean expResult = false;
        boolean result = instance.cardReportedLost();
        assertEquals(expResult, result);
        
        instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess2);
        expResult = true;
        result = instance.cardReportedLost();
        assertEquals(expResult, result);
    }

    /**
     * Test of isInGoodStanding method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testIsInGoodStanding() 
    {
        System.out.println("==isInGoodStanding==");
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess);
        boolean expResult = true;
        boolean result = instance.isInGoodStanding();
        assertEquals(expResult, result);
        
        instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonSuccess2);
        expResult = false;
        result = instance.isInGoodStanding();
        assertEquals(expResult, result);
    }
    
}
