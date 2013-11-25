
package mecard.security;

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
    public void testGetPAPIHash()
    {
        System.out.println("===getPAPIHash===");
        String strAccessKey = "";
        String strHTTPMethod = "";
        String strURI = "";
        String strHTTPDate = "";
        String strPatronPassword = "";
        PAPISecurity instance = PAPISecurity.getInstanceOf();
//        String expResult = "";
//        String result = instance.getPAPIHash(strAccessKey, strHTTPMethod, strURI, strHTTPDate, strPatronPassword);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
