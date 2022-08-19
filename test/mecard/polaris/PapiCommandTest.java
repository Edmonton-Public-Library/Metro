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

import api.CommandStatus;
import java.util.Properties;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PapiPropertyTypes;
import mecard.config.PropertyReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com> 
 */


public class PapiCommandTest
{
    private final Properties papiProperties;
    private final String baseUri;
    private final boolean authenticationMethodsTest;
    public PapiCommandTest()
    {
        this.papiProperties = PropertyReader.getProperties(ConfigFileTypes.PAPI);
        StringBuilder uriSB    = new StringBuilder();
        uriSB.append(papiProperties.getProperty(PapiPropertyTypes.HOST.toString()))
            .append(papiProperties.getProperty(PapiPropertyTypes.REST_PATH.toString()))
            .append("/").append(papiProperties.getProperty(PapiPropertyTypes.VERSION.toString()))
            .append("/").append(papiProperties.getProperty(PapiPropertyTypes.LANGUAGE_ID.toString()))
            .append("/").append(papiProperties.getProperty(PapiPropertyTypes.APP_ID.toString()))
            .append("/").append(papiProperties.getProperty(PapiPropertyTypes.ORG_ID.toString()))
            .append("/");
        this.baseUri = uriSB.toString();
        this.authenticationMethodsTest = false;
    }

    /**
     * Test of execute method, of class PapiCommand.
     */
    @Test
    public void testExecute()
    {
        System.out.println("==execute==");
        PapiCommand command = new PapiCommand.Builder(papiProperties, "GET")
            .uri(this.baseUri + "api")
            .build();
        
        CommandStatus result = command.execute();
        System.out.println("TEST_OUTPUT: '" + result.getStdout() + "'");
        assertNotNull(result);
        assertTrue(result.getStatus() == ResponseTypes.SUCCESS);
        
        command = new PapiCommand.Builder(papiProperties, "GET")
            .uri(this.baseUri + "patron/21221011111111")
            .build();
        result = command.execute();
        System.out.println("TEST_OUTPUT: '" + result.getStdout() + "'");
        assertNotNull(result);
        assertTrue(result.getStatus() == ResponseTypes.UNAUTHORIZED);
    }
    
    @Test
    public void testAuthenticate()
    {
        // Be careful the PAPI web service is set to reject more than 
        // three authentications for the same customer in the space of 5 minutes.
        if (this.authenticationMethodsTest)
        {
            System.out.println("== Authenticate ==");
            // TODO use the new PapiXmlPatronAuthenticateMessage class.
            String xml = "<PatronAuthenticationData xmlns:i=\"http://www.w3.org/2001/XMLSchemainstance\">"
                + "<Barcode>21221012345678</Barcode>"
                + "<Password>64058</Password>"
                + "</PatronAuthenticationData>";

            PapiCommand command = new PapiCommand.Builder(papiProperties, "POST")
                .uri(this.baseUri + "authenticator/patron")
                .bodyXML(xml)
                .build();
            CommandStatus result = command.execute();
            System.out.println("Authentication OUTPUT: '" + result.getStdout() + "'");
            assertNotNull(result);
            assertTrue(result.getStatus() == ResponseTypes.SUCCESS);
        }
    }

    /**
     * Test of accessToken method, of class PapiCommand.
     */
    @Test
    public void testAccessToken()
    {
        System.out.println("patronPassword");
        // This is a simple setter method but leave the stub so update tests
        // doesn't add it again.
        assertTrue(true);
    }

    /**
     * Test of toString method, of class PapiCommand.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
//        PapiCommand instance = new PapiCommand.Builder(papiProperties, "GET")
//                .uri(this.baseUri + "api")
//                .build();
//        String expResult = "<PatronAuthenticationResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><PAPIErrorCode>0</PAPIErrorCode><ErrorMessage i:nil=\"true\" /><AccessSecret>$2a$10$MD1PFF/65owmn0uMMnV6lesArBATfiXOmNSn7kQwU7YoqoqEVBU3W</AccessSecret><AccessToken>$2a$10$MD1PFF/65owmn0uMMnV6lesArBATfiXOmNSn7kQwU7YoqoqEVBU3W</AccessToken><PatronID>2022</PatronID><AuthExpDate i:nil=\"true\" /></PatronAuthenticationResult>";
//        String result = instance.toString();
//        System.out.println(">>>TO_STRING:" + result);
//        assertEquals(expResult, result);
        // This is a simple getter method but leave the stub so update tests
        // doesn't add it again. You can uncomment the code above, but it
        // will fail on repeated attempts because the AccessSecret and AccessToken
        // will change over time.
        assertTrue(true);
    }
    
}
