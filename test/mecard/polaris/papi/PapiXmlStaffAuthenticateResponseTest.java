/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
package mecard.polaris.papi;

import mecard.polaris.papi.PapiXmlStaffAuthenticateResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import mecard.util.DateComparer;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class PapiXmlStaffAuthenticateResponseTest
{

    private final String xmlFail;
    private final String xmlSuccess;
    
    public PapiXmlStaffAuthenticateResponseTest()
    {
        this.xmlFail    = "<AuthenticationResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
        "    <PAPIErrorCode>-8001</PAPIErrorCode>\n" +
        "    <ErrorMessage i:nil=\"true\" />\n" +
        "    <AccessToken i:nil=\"true\" />\n" +
        "    <AccessSecret i:nil=\"true\" />\n" +
        "    <PolarisUserID>0</PolarisUserID>\n" +
        "    <BranchID>0</BranchID>\n" +
        "    <AuthExpDate i:nil=\"true\" />\n" +
        "</AuthenticationResult>";
        this.xmlSuccess = "<AuthenticationResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
        "    <PAPIErrorCode>0</PAPIErrorCode>\n" +
        "    <ErrorMessage i:nil=\"true\" />\n" +
        "    <AccessToken>JzZ33Wcpl61ka6znZlkmrfG6ITf6lHW7</AccessToken>\n" +
        "    <AccessSecret>xKOtQZAaPAs7alEy</AccessSecret>\n" +
        "    <PolarisUserID>18</PolarisUserID>\n" +
        "    <BranchID>3</BranchID>\n" +
        "    <AuthExpDate>2022-08-20T22:16:02.45</AuthExpDate>\n" +
        "</AuthenticationResult>";
    }

    /**
     * Test of getAccessToken method, of class PapiXmlPatronAuthenticateResponse.
     */
    @Test
    public void testGetAccessToken() {
        System.out.println("getAccessToken");
        PapiXmlStaffAuthenticateResponse instance = new PapiXmlStaffAuthenticateResponse(xmlSuccess);
        String expResult = "JzZ33Wcpl61ka6znZlkmrfG6ITf6lHW7";
        String result = instance.getAccessToken();
        assertEquals(expResult, result);
        
        instance = new PapiXmlStaffAuthenticateResponse(xmlFail);
        expResult = "";
        result = instance.getAccessToken();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAccessSecret method, of class PapiXmlPatronAuthenticateResponse.
     */
    @Test
    public void testGetAccessSecret() {
        System.out.println("getAccessSecret");
        PapiXmlStaffAuthenticateResponse instance = new PapiXmlStaffAuthenticateResponse(xmlSuccess);
        String expResult = "xKOtQZAaPAs7alEy";
        String result = instance.getAccessSecret();
        assertEquals(expResult, result);
        
        instance = new PapiXmlStaffAuthenticateResponse(xmlFail);
        expResult = "";
        result = instance.getAccessSecret();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTokenExpiration method, of class PapiXmlPatronAuthenticateResponse.
     */
    @Test
    public void testGetTokenExpiration() {
        System.out.println("getTokenExpiration");
        PapiXmlStaffAuthenticateResponse instance = new PapiXmlStaffAuthenticateResponse(xmlFail);
        LocalDate expResult = DateComparer.getRFC1123Date(DateComparer.getRFC1123Date());
        LocalDate result = instance.getTokenExpiration();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTokenExpirationAsString method, of class PapiXmlPatronAuthenticateResponse.
     */
    @Test
    public void testGetTokenExpirationAsString() {
        System.out.println("getTokenExpirationAsString");
        PapiXmlStaffAuthenticateResponse instance = new PapiXmlStaffAuthenticateResponse(xmlSuccess);
        String expResult = "2022-08-20T22:16:02.45";
        String result = instance.getTokenExpirationAsString();
        assertEquals(expResult, result);
        instance = new PapiXmlStaffAuthenticateResponse(xmlFail);
        expResult = "";
        result = instance.getTokenExpirationAsString();
        assertEquals(expResult, result);
    }

    /**
     * Test of authenticated method, of class PapiXmlPatronAuthenticateResponse.
     */
    @Test
    public void testAuthenticated() {
        System.out.println("authenticated");
        PapiXmlStaffAuthenticateResponse instance = new PapiXmlStaffAuthenticateResponse(xmlSuccess);
        boolean expResult = true;
        boolean result = instance.authenticated();
        assertEquals(expResult, result);
        instance = new PapiXmlStaffAuthenticateResponse(xmlFail);
        expResult = false;
        result = instance.authenticated();
        assertEquals(expResult, result);
    }
    
}
