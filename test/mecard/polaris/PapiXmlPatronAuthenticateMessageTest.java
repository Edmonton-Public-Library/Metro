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
package mecard.polaris;

import static org.junit.Assert.*;

import java.time.LocalDate;
import mecard.util.DateComparer;
import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com> 
 */
public class PapiXmlPatronAuthenticateMessageTest {

    private final String xml;
    
    public PapiXmlPatronAuthenticateMessageTest() 
    {
        // Use a canned example for testing the basic class because the signature
        // changes with timestamp.
        this.xml = "<PatronAuthenticationResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<PAPIErrorCode>0</PAPIErrorCode>"
                + "<ErrorMessage>\n</ErrorMessage>"
                + "<AccessToken>$2a$10$MD1PFF/65owmn0uMMnV6lesArBATfiXOmNSn7kQwU7YoqoqEVBU3W</AccessToken>"
                + "<AccessSecret>$2a$10$MD1PFF/65owmn0uMMnV6lesArBATfiXOmNSn7kQwU7YoqoqEVBU3W</AccessSecret>"
                + "<PatronID>2022</PatronID>"
                + "<AuthExpDate></AuthExpDate>"  // the web service on the sandbox returns an empty string.
                + "</PatronAuthenticationResult>";
        // Here is a real response:
        //<PatronAuthenticationResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
        // <PAPIErrorCode>0</PAPIErrorCode>
        // <ErrorMessage i:nil="true" />
        // <AccessSecret>$2a$10$MD1PFF/65owmn0uMMnV6lesArBATfiXOmNSn7kQwU7YoqoqEVBU3W</AccessSecret>
        // <AccessToken>$2a$10$MD1PFF/65owmn0uMMnV6lesArBATfiXOmNSn7kQwU7YoqoqEVBU3W</AccessToken>
        // <PatronID>2022</PatronID>
        // <AuthExpDate i:nil="true" />
        //</PatronAuthenticationResult>
    }

    /**
     * Test of getAccessToken method, of class PapiXmlPatronAuthenticateResponse.
     */
    @Test
    public void testGetAccessToken() {
        System.out.println("getAccessToken");
        PapiXmlPatronAuthenticateResponse instance = new PapiXmlPatronAuthenticateResponse(xml);
        String expResult = "$2a$10$MD1PFF/65owmn0uMMnV6lesArBATfiXOmNSn7kQwU7YoqoqEVBU3W";
        String result = instance.getAccessToken();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAccessSecret method, of class PapiXmlPatronAuthenticateResponse.
     */
    @Test
    public void testGetAccessSecret() {
        System.out.println("getAccessSecret");
        PapiXmlPatronAuthenticateResponse instance = new PapiXmlPatronAuthenticateResponse(xml);
        String expResult = "$2a$10$MD1PFF/65owmn0uMMnV6lesArBATfiXOmNSn7kQwU7YoqoqEVBU3W";
        String result = instance.getAccessSecret();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTokenExpiration method, of class PapiXmlPatronAuthenticateResponse.
     */
    @Test
    public void testGetTokenExpiration() {
        System.out.println("getTokenExpiration");
        PapiXmlPatronAuthenticateResponse instance = new PapiXmlPatronAuthenticateResponse(xml);
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
        PapiXmlPatronAuthenticateResponse instance = new PapiXmlPatronAuthenticateResponse(xml);
        String expResult = "";
        String result = instance.getTokenExpirationAsString();
        assertEquals(expResult, result);
    }

    /**
     * Test of authenticated method, of class PapiXmlPatronAuthenticateResponse.
     */
    @Test
    public void testAuthenticated() {
        System.out.println("authenticated");
        PapiXmlPatronAuthenticateResponse instance = new PapiXmlPatronAuthenticateResponse(xml);
        boolean expResult = true;
        boolean result = instance.authenticated();
        assertEquals(expResult, result);
    }
    
}
