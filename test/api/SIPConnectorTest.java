
package api;

import mecard.requestbuilder.SIPRequestBuilder;
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
    private final String institution;
    private final String timeout;
    private final String user;
    private final String password;
    private final String location;
    
    public SIPConnectorTest()
    {
        host = "207.167.28.31";
        port = "6197";     // TRAC
        timeout = "5000";
        user = "3MLogin";
        password = "3MLogin";
        institution = "SOME_INSTITUTION";
        location = "AUTHENTICATE";
        
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
                .sipUser("")
                .password("")
                .institution(institution)
                .timeout(timeout)
                .locationCode(location)
                .debug(true)
                .build();
        boolean expResult = true; // If online.
        boolean result = instance.test();
        assertEquals(expResult, result);
        
        instance = new SIPConnector
                .Builder(host, port)
                .sipUser(user)
                .password(password)
                .institution(institution)
                .timeout(timeout)
                .locationCode(location)
                .debug(true)
                .build();
        // should be sending: '93  CN3MLogin|CO3MLogin|CP|AY0AZF5D3'
        expResult = true; // If online.
        result = instance.test(); // should not show config info again.
        assertEquals(expResult, result);
        String returnString = instance.send("63                               AO|AA29335002291042|AD2003|AY2AZF3B6");
        SIPCustomerMessage customerMessage = new SIPCustomerMessage(returnString);
//        System.out.println("recv:'" + returnString + "'");
        assertTrue(customerMessage.getCode().compareTo("64") == 0);
        // This won't return a customer since we are
        Command command = new SIPCommand.Builder(instance)
                .setUser(user, password)
                .build();
        command.execute();
    }
    
}
