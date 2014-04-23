
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
public class PAPISecurityTest
{
    
    public PAPISecurityTest()
    {
    }
    
    /**
     * Test of getPAPISecurity method, of class PAPISecurity.
     */
    @Test
    public void testGetHash()
    {
        System.out.println("===getHash===");
        String strAccessKey = "1234";
        String data = "GET";
        PAPISecurity instance = PAPISecurity.getInstanceOf();
        
        String expResult = "S1B0+M48Z/LOc32PFEfX67uwFyk=";
        String result = instance.getHash(strAccessKey, data);
        System.out.println(">>HASH: '"+result+"'");
        assertEquals(expResult, result);
    }

    /**
     * Test of getPAPISecurity method, of class PAPISecurity.
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
        PAPISecurity instance = PAPISecurity.getInstanceOf();
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
     * Test of getInstanceOf method, of class PAPISecurity.
     */
    @Test
    public void testGetInstanceOf()
    {
        System.out.println("==getInstanceOf==");
        PAPISecurity result = PAPISecurity.getInstanceOf();
        assertNotNull(result); // gotta start somewhere. Baby steps.
    }

    /**
     * Test of getCommandSignature method, of class PAPISecurity.
     */
    @Test
    public void testGetCommandSignature()
    {
        try
        {
            System.out.println("==getCommandSignature==");
            String HTTPMethod = "GET";
            URI uri = new URI("http://localhost/PAPIService/REST/public/v1/1033/100/1/patron/21756003332022");
            String accessSecret = "1234";
            PAPISecurity instance = PAPISecurity.getInstanceOf();
            String expResult = "";
            String result = instance.getCommandSignature(HTTPMethod, uri, accessSecret);
            // We can't test because the date requirement changes each time it runs.
            System.out.println("PAPI_Command sig: '" + result + "'");
        } catch (URISyntaxException ex)
        {
            Logger.getLogger(PAPISecurityTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getAuthenticationSignature method, of class PAPISecurity.
     */
    @Test
    public void testGetAuthenticationSignature()
    {
        try
        {
            System.out.println("==getAuthenticationSignature==");
            String HTTPMethod = "GET";
            URI uri = new URI("http://localhost/PAPIService/REST/public/v1/1033/100/1/patron/21756003332022");
            PAPISecurity instance = PAPISecurity.getInstanceOf();
            String result = instance.getAuthenticationSignature(HTTPMethod, uri);
            // We can't test because the date requirement changes each time it runs.
            System.out.println("PAPI_Authorization sig: '" + result + "'");
        } catch (URISyntaxException ex)
        {
            Logger.getLogger(PAPISecurityTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getPolarisDate method, of class PAPISecurity.
     */
    @Test
    public void testGetPolarisDate()
    {
        System.out.println("==getPolarisDate==");
        PAPISecurity instance = PAPISecurity.getInstanceOf();
        System.out.println("DATE_HTTP_FORMAT: '" + instance.getPolarisDate() + "'");
    }
    
}
