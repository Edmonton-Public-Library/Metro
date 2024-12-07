
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
        
        jsonString = """
                     {
                         "messageList": [
                             {
                                 "code": "unableToLogin",
                                 "message": "Unable to log in."
                             }
                         ]
                     }
                     """;

        testResponse = (SDapiJsonStaffAuthenticationResponse) SDapiJsonStaffAuthenticationResponse.parseJson(jsonString);
//        System.out.println(">>>" + testResponse.getSessionToken());
//        System.out.println(">>>>" + testResponse.errorMessage());
        assertEquals("Unable to log in.\n", testResponse.errorMessage());
    }

    /**
     * Test of parseJson method, of class SDapiJsonStaffAuthenticationResponse.
     */
    @Test
    public void testParseJson() {
        System.out.println("parseJson");
//        String jsonString = "";
//        SDapiJsonStaffAuthenticationResponse instance = new SDapiJsonStaffAuthenticationResponse();
//        SDapiJsonResponse expResult = null;
//        SDapiJsonResponse result = instance.parseJson(jsonString);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of errorMessage method, of class SDapiJsonStaffAuthenticationResponse.
     */
    @Test
    public void testErrorMessage() {
        System.out.println("errorMessage");
//        SDapiJsonStaffAuthenticationResponse instance = new SDapiJsonStaffAuthenticationResponse();
//        String expResult = "";
//        String result = instance.errorMessage();
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSessionToken method, of class SDapiJsonStaffAuthenticationResponse.
     */
    @Test
    public void testGetSessionToken() {
        System.out.println("getSessionToken");
//        SDapiJsonStaffAuthenticationResponse instance = new SDapiJsonStaffAuthenticationResponse();
//        String expResult = "";
//        String result = instance.getSessionToken();
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isErrorResponse method, of class SDapiJsonStaffAuthenticationResponse.
     */
    @Test
    public void testIsErrorResponse() {
        System.out.println("isErrorResponse");
//        SDapiJsonStaffAuthenticationResponse instance = new SDapiJsonStaffAuthenticationResponse();
//        boolean expResult = false;
//        boolean result = instance.isErrorResponse();
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
