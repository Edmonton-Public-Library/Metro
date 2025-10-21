/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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

package mecard.calgary.cplapi;

import mecard.config.CPLapiUserFields;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class CPLapiCustomerResponseTest {
    
    private final String jsonSuccess;
    private final String jsonFail;
    private final String jsonError;
    
    public CPLapiCustomerResponseTest() 
    {
        jsonSuccess = """
                     {
                        "cardNumber": "21221012345678",
                        "birthDate": "2000-02-29",
                        "firstName": "Balzac",
                        "lastName": "BILLY",
                        "expiryDate": "2026-08-22",
                        "profile": "CPL_ADULT",
                        "status": "OK",
                        "address": "1234 37 Avenue",
                        "city": "Calgary",
                        "province": "Alberta",
                        "phoneNumber": "780-333-4444",
                        "emailAddress": "ilsadmins@epl.ca",
                        "gender": "Male",
                        "pin": "123456",
                        "postalCode": "T5J-2V4"
                     }
                     """;
        jsonFail = """
                     {
                        "cardNumber": "",
                        "birthDate": null,
                        "firstName": null,
                        "lastName": null,
                        "expiryDate": "",
                        "profile": "LOST",
                        "status": BLOCKED,
                        "address": null,
                        "city": null,
                        "province": null,
                        "phoneNumber": null,
                        "emailAddress": null,
                        "gender": null,
                        "pin": null,
                        "postalCode": null
                     }
                     """;
        jsonError = """
                    {"type":"https://tools.ietf.org/html/rfc7231#section-6.5.1",
                    "title":"One or more validation errors occurred.",
                    "status":400,"traceId":"40001997-0000-fe00-b63f-84710c7967bb",
                    "errors":{"CardNumber/PinNumber":["Invalid Credentials."]}}""";
    }

    /**
     * Test of succeeded method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testSucceeded() 
    {
        System.out.println("==succeeded==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        assertEquals(true, instance.succeeded());
        instance = (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonFail);
        assertEquals(false, instance.succeeded());
    }

    /**
     * Test of errorMessage method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testErrorMessage() 
    {
        System.out.println("==errorMessage==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        String expResult = "";
        String result = instance.errorMessage();
        assertEquals(expResult, result);
        instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonFail);
        assertEquals("Account not found.", instance.errorMessage());
        CPLapiResponse instance2 = 
                CPLapiCustomerResponse.parseJson(jsonError);
        assertEquals("Invalid Credentials", instance2.errorMessage());
    }
    
    /**
     * Tests if a string contains the invalid credentials message (case-insensitive)
     * @param message The string to test
     * @return true if the message matches the pattern, false otherwise
     */
    public boolean isInvalidCredentialsMessageIgnoreCase(String message) {
        if (message == null) 
        {
            return false;
        }
        
        // Pattern: CardNumber/PinNumber: [Invalid Credentials.] (case-insensitive)
//        String pattern = "(?i).*card\\W*number\\W*pin\\W*number\\W*invalid\\W*credentials.*";
        String pattern = "(?i).*\\W*invalid\\W*credentials.*";
        return message.matches(pattern);
    }

    /**
     * Test of getCustomerProfile method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testGetCustomerProfile() 
    {
        System.out.println("==getCustomerProfile==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        assertEquals("CPL_ADULT", instance.getCustomerProfile());
        instance = (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonFail);
        assertFalse(instance.getCustomerProfile().isBlank());
    }

    /**
     * Test of getField method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testGetField() 
    {
        System.out.println("==getField==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        assertEquals("BILLY", instance.getField(CPLapiUserFields.USER_LAST_NAME.toString()));
        assertEquals("Balzac", instance.getField(CPLapiUserFields.USER_FIRST_NAME.toString()));
        assertEquals("CPL_ADULT", instance.getField(CPLapiUserFields.PROFILE.toString()));
        assertEquals("ilsadmins@epl.ca", instance.getField(CPLapiUserFields.EMAIL.toString()));
        assertEquals("780-333-4444", instance.getField(CPLapiUserFields.PHONE.toString()));
        assertEquals("2000-02-29", instance.getField(CPLapiUserFields.USER_BIRTHDATE.toString()));
        assertEquals("2026-08-22", instance.getField(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()));
        assertEquals("T5J-2V4", instance.getField(CPLapiUserFields.POSTALCODE.toString()));
        assertEquals("Calgary", instance.getField(CPLapiUserFields.CITY.toString()));
        assertEquals("Alberta", instance.getField(CPLapiUserFields.PROVINCE.toString()));
        assertEquals("OK", instance.getField(CPLapiUserFields.STATUS.toString()));
        assertEquals("Male", instance.getField(CPLapiUserFields.GENDER.toString()));
        assertEquals("1234 37 Avenue", instance.getField(CPLapiUserFields.STREET.toString()));
        assertEquals("123456", instance.getField(CPLapiUserFields.USER_PASSWORD.toString()));
        instance = (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonFail);
        assertEquals("BLOCKED", instance.getStanding());
        assertEquals("", instance.getField(CPLapiUserFields.USER_PASSWORD.toString()));
    }

    /**
     * Test of getDateField method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testGetDateField() 
    {
        System.out.println("==getDateField==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        assertEquals("2026-08-22", instance.getDateField(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()));
        assertEquals("2000-02-29", instance.getDateField(CPLapiUserFields.USER_BIRTHDATE.toString()));
        assertEquals("", instance.getDateField(CPLapiUserFields.USER_FIRST_NAME.toString()));
        instance = (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonFail);
        assertEquals("", instance.getDateField(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()));
        assertEquals("", instance.getDateField(CPLapiUserFields.USER_BIRTHDATE.toString()));
        assertEquals("", instance.getDateField(CPLapiUserFields.USER_FIRST_NAME.toString()));
    }

    /**
     * Test of isEmpty method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testIsEmpty() 
    {
        System.out.println("==isEmpty==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonFail);
        assertTrue(instance.isEmpty(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()));
        instance = (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        assertFalse(instance.isEmpty(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()));
    }

    /**
     * Test of getStanding method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testGetStanding() 
    {
        System.out.println("==getStanding==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        assertEquals("OK", instance.getStanding());
        assertTrue(instance.isInGoodStanding());
        instance = (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonFail);
        assertEquals("BLOCKED", instance.getStanding());
        assertFalse(instance.isInGoodStanding());
    }

    /**
     * Test of cardReportedLost method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testCardReportedLost() 
    {
        System.out.println("==cardReportedLost==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        assertFalse(instance.cardReportedLost());
        instance = (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonFail);
        assertTrue(instance.cardReportedLost());
    }

    /**
     * Test of isInGoodStanding method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testIsInGoodStanding() {
        System.out.println("==isInGoodStanding==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        assertTrue(instance.isInGoodStanding());
        instance = (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonFail);
        assertEquals("BLOCKED", instance.getStanding());
        assertFalse(instance.isInGoodStanding());
    }

    /**
     * Test of parseJson method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testParseJson() 
    {
        System.out.println("==parseJson==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        assertNotNull(instance.toString());
        CPLapiCustomerResponse instance2 = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonFail);
        assertEquals("", instance2.getField(CPLapiUserFields.USER_PASSWORD.toString()));
        System.out.println(instance2.toString());
        assertNotNull(instance2.toString());
    }

    /**
     * Test of toString method, of class CPLapiCustomerResponse.
     */
    @Test
    public void testToString() {
        System.out.println("==toString==");
        CPLapiCustomerResponse instance = 
                (CPLapiCustomerResponse) CPLapiCustomerResponse.parseJson(jsonSuccess);
        assertNotNull(instance.toString());
        System.out.println(instance.toString());
    }
    
}
