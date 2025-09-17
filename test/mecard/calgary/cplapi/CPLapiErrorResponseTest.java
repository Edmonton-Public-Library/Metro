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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class CPLapiErrorResponseTest 
{
    private final String errorJson;
    private final String nonErrorJson;
    
    public CPLapiErrorResponseTest() 
    {
        errorJson = """
            {
              "type": "https://tools.ietf.org/html/rfc7231#section-6.5.1",
              "title": "One or more validation errors occurred.",
              "status": 400,
              "traceId": "400006a3-0000-be00-b63f-84710c7967bb",
              "errors": {
                "CardNumber/PinNumber": [
                  "Invalid Credentials."
                ]
              }
            }
        """;
        
        nonErrorJson = """
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

    }

    /**
     * Test of parseJson method, of class CPLapiErrorResponse.
     */
    @Test
    public void testParseJson() 
    {
        System.out.println("==parseJson==");
        CPLapiErrorResponse instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(errorJson);
        System.out.println(instance.toString());
    }

    /**
     * Test of succeeded method, of class CPLapiErrorResponse.
     */
    @Test
    public void testSucceeded() 
    {
        System.out.println("==succeeded==");
        CPLapiErrorResponse instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(errorJson);
        assertFalse(instance.succeeded());
    }

    /**
     * Test of errorMessage method, of class CPLapiErrorResponse.
     */
    @Test
    public void testErrorMessage() 
    {
        System.out.println("==errorMessage==");
        CPLapiErrorResponse instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(errorJson);
        assertEquals("CardNumber/PinNumber: [Invalid Credentials.]", instance.errorMessage());
    }

    /**
     * Test of getErrors method, of class CPLapiErrorResponse.
     */
    @Test
    public void testGetErrors() 
    {
        System.out.println("==getErrors==");
        CPLapiErrorResponse instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(errorJson);
        assertEquals("CardNumber/PinNumber: [Invalid Credentials.]", instance.errorMessage());
        assertEquals("400", instance.getStatus());
        
        instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(nonErrorJson);
        assertTrue(instance.succeeded());
        assertEquals("", instance.errorMessage());
        assertEquals("", instance.getStatus());
    }

    /**
     * Test of toString method, of class CPLapiErrorResponse.
     */
    @Test
    public void testToString() 
    {
        System.out.println("==toString==");
        CPLapiErrorResponse instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(errorJson);
        System.out.println(instance.toString());
    }

    /**
     * Test of getType method, of class CPLapiErrorResponse.
     */
    @Test
    public void testGetType() {
        System.out.println("==getType==");
        CPLapiErrorResponse instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(errorJson);
        assertEquals("https://tools.ietf.org/html/rfc7231#section-6.5.1", instance.getType());
        
        instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(nonErrorJson);
        assertEquals("", instance.getType());
    }

    /**
     * Test of getTitle method, of class CPLapiErrorResponse.
     */
    @Test
    public void testGetTitle() {
        System.out.println("==getTitle==");
        CPLapiErrorResponse instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(errorJson);
        assertEquals("One or more validation errors occurred.", instance.getTitle());
        
        instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(nonErrorJson);
        assertEquals("", instance.getTitle());
    }

    /**
     * Test of getTraceId method, of class CPLapiErrorResponse.
     */
    @Test
    public void testGetTraceId() {
        System.out.println("==getTraceId==");
        CPLapiErrorResponse instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(errorJson);
        assertEquals("400006a3-0000-be00-b63f-84710c7967bb", instance.getTraceId());
        
        instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(nonErrorJson);
        assertEquals("", instance.getTraceId());
    }

    /**
     * Test of getStatus method, of class CPLapiErrorResponse.
     */
    @Test
    public void testGetStatus() {
        System.out.println("==getStatus==");
        CPLapiErrorResponse instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(errorJson);
        assertEquals("CardNumber/PinNumber: [Invalid Credentials.]", instance.errorMessage());
        assertEquals("400", instance.getStatus());
        
        instance = 
                (CPLapiErrorResponse) CPLapiErrorResponse.parseJson(nonErrorJson);
        assertTrue(instance.succeeded());
        assertEquals("", instance.errorMessage());
        assertEquals("", instance.getStatus());
    }
    
}
