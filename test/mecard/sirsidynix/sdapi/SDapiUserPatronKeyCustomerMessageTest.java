
package mecard.sirsidynix.sdapi;


import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class SDapiUserPatronKeyCustomerMessageTest 
{
    private final String jsonString;
    public SDapiUserPatronKeyCustomerMessageTest() 
    {
        jsonString = """
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
    }

    /**
     * Test of succeeded method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testSucceeded() {
        System.out.println("succeeded");
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(jsonString);
        boolean expResult = true;
        boolean result = instance.succeeded();
        assertEquals(expResult, result);
    }

    /**
     * Test of errorMessage method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testErrorMessage() {
        System.out.println("errorMessage");
        SDapiUserPatronKeyCustomerMessage instance = new SDapiUserPatronKeyCustomerMessage();
        String expResult = "";
        String result = instance.errorMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parseJson method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testParseJson() {
        System.out.println("parseJson");
        String jsonString = "";
        SDapiResponse expResult = null;
        SDapiResponse result = SDapiUserPatronKeyCustomerMessage.parseJson(jsonString);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFields method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testGetFields() {
        System.out.println("getFields");
        SDapiUserPatronKeyCustomerMessage instance = new SDapiUserPatronKeyCustomerMessage();
        SDapiUserPatronKeyCustomerMessage.PatronFields expResult = null;
        SDapiUserPatronKeyCustomerMessage.PatronFields result = instance.getFields();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserKey method, of class SDapiUserPatronKeyCustomerMessage.
     */
    @Test
    public void testGetUserKey() 
    {
        System.out.println("==getUserKey==");
        SDapiUserPatronKeyCustomerMessage instance = 
                (SDapiUserPatronKeyCustomerMessage) SDapiUserPatronKeyCustomerMessage.parseJson(this.jsonString);
        String expResult = "301585";
        String result = instance.getUserKey();
        assertEquals(expResult, result);
    }
    
}
