
package mecard.security;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PapiSecurityTest
{
    
    public PapiSecurityTest()
    {
    }
    
    /**
     * Test of getPAPISecurity method, of class PapiSecurity.
     */
    @Test
    public void testGetHash()
    {
        System.out.println("===getHash===");
        String strAccessKey = "1234";
        String data = "GET";
        PapiSecurity instance = PapiSecurity.getInstanceOf();
        
        String expResult = "S1B0+M48Z/LOc32PFEfX67uwFyk=";
        String result = instance.getHash(strAccessKey, data);
        System.out.println(">>HASH: '"+result+"' from: '"+data+"'");
        assertEquals(expResult, result);
    }

    /**
     * Test of getPAPISecurity method, of class PapiSecurity.
     */
    @Test
    public void testGetPAPIHash()
    {
        System.out.println("===getPAPIHash===");
        // With help from http://caligatio.github.io/jsSHA/
        String strAccessKey = "1234";
        String strHTTPMethod = "GET";
        String strURI = "http://localhost/PAPIService/REST/public/v1/1033/100/1/patron/21756003332022";
        String strHTTPDate = "Wed, 09 Oct 2009 22:23:32 GMT";
        String strPatronPassword = "";
        PapiSecurity instance = PapiSecurity.getInstanceOf();
        String expResult = "9NoK01FefkvfgDkzOG3FnE29+o0=";
        String result = instance.getPAPIHash(strAccessKey, strHTTPMethod, strURI, strHTTPDate, strPatronPassword);
        System.out.println("PAPI_HASH: '"+result+"'");
        assertEquals(expResult, result);
        
        strPatronPassword = "1234";
        expResult = "flrYL2V+sr4CPbkynavtAaMcizY=";
        result = instance.getPAPIHash(strAccessKey, strHTTPMethod, strURI, strHTTPDate, strPatronPassword);
        System.out.println("PAPI_HASH: '"+result+"'");
        assertEquals(expResult, result);
    }

    /**
     * Test of getInstanceOf method, of class PapiSecurity.
     */
    @Test
    public void testGetInstanceOf()
    {
        System.out.println("==getInstanceOf==");
        PapiSecurity result = PapiSecurity.getInstanceOf();
        assertNotNull(result); // gotta start somewhere. Baby steps.
    }

    /**
     * Test of getSignature method, of class PapiSecurity.
     */
    @Test
    public void testGetSignature()
    {
        try
        {
            System.out.println("==getCommandSignature==");
            String HTTPMethod = "GET";
            URI uri = new URI("http://localhost/PAPIService/REST/public/v1/1033/100/1/patron/21756003332022");
            String accessSecret = "1234";
            PapiSecurity instance = PapiSecurity.getInstanceOf();
            String expResult = "";
            String result = instance.getSignature(HTTPMethod, uri, accessSecret);
            // We can't test because the date requirement changes each time it runs.
            System.out.println("PAPI_Command sig: '" + result + "'");
        } catch (URISyntaxException ex)
        {
            Logger.getLogger(PapiSecurityTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Test of getSignature method, of class PapiSecurity.
     */
    @Test
    public void testGetAuthorizedSignature()
    {
        try
        {
            System.out.println("==getX-PAPIAccessTokenSignature==");
            String strAccessKey = "04b44247-6b91-47b7-a328-fcb8cf6eda5f";
            String strHTTPMethod = "POST";
            String strURI = "/PAPIService/REST/public/v1/1033/100/1/patron";
            String strHTTPDate = "Wed, 09 Oct 2009 22:23:32 GMT";
            String strPatronPassword = "";
            // This one works for basic public calls, don't use the www.tracpac.ab.ca domain name.
//            URI uri = new URI("http://207.167.28.31/PAPIService/REST/public/v1/1033/100/1/patron");
//            URI uri = new URI("http://www.tracpac.ab.ca/PAPIService/REST/public/v1/1033/100/1/patron");
            URI uri = new URI(strURI);
            PapiSecurity instance = PapiSecurity.getInstanceOf();
            String compHash = instance.getPAPIHash(strAccessKey, strHTTPMethod, strURI, strHTTPDate, strPatronPassword);
            System.out.println(">>>>>COmputed HASH: '" + compHash + "'");
            String result = instance.getXPAPIAccessTokenHeader(strHTTPMethod, uri);
            // We can't test because the date requirement changes each time it runs.
            System.out.println("Authorization: '" + result + "'");
        } catch (URISyntaxException ex)
        {
            Logger.getLogger(PapiSecurityTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getSignature method, of class PapiSecurity.
     */
    @Test
    public void testGetAuthenticationSignature()
    {
        try
        {
            System.out.println("==getAuthenticationSignature==");
            String HTTPMethod = "GET";
            URI uri = new URI("http://localhost/PAPIService/REST/public/v1/1033/100/1/patron/21756003332022");
            PapiSecurity instance = PapiSecurity.getInstanceOf();
            String result = instance.getSignature(HTTPMethod, uri);
            // We can't test because the date requirement changes each time it runs.
            System.out.println("PAPI_Authorization sig: '" + result + "'");
        } catch (URISyntaxException ex)
        {
            Logger.getLogger(PapiSecurityTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getPolarisDate method, of class PapiSecurity.
     */
    @Test
    public void testGetPolarisDate()
    {
        System.out.println("==getPolarisDate==");
        PapiSecurity instance = PapiSecurity.getInstanceOf();
        System.out.println("DATE_HTTP_FORMAT: '" + instance.getPolarisDate() + "'");
    }
    
}
