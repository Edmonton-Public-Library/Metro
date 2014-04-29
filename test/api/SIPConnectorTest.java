
package api;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class SIPConnectorTest
{
    private final String port;
    private final String host;
    private String institution;
    private String user;
    private String password;
    private String timeout;
    
    public SIPConnectorTest()
    {
        host = "207.167.28.31";
        port = "6197";     // TRAC
        timeout = "5000";
        institution = "";
        user = "";
        password = "";
    }

    /**
     * Test of test method, of class SIPConnector.
     */
    @Test
    public void testTest()
    {
        System.out.println("==test==");
        SIPConnector instance = new SIPConnector
                .Builder(host, port)
                .sipUser(user)
                .password(password)
                .institution(institution)
                .sipUser(user)
                .timeout(timeout)
                .build();
        boolean expResult = true; // If online.
        boolean result = instance.test();
        assertEquals(expResult, result);
        // TODO test login with getting customer information.
    }
    
}
