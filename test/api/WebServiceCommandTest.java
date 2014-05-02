
package api;

import mecard.ResponseTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class WebServiceCommandTest
{
    
    public WebServiceCommandTest()
    {
    }

    /**
     * Test of execute method, of class WebServiceCommand.
     */
    @Test
    public void testExecute()
    {
        System.out.println("==execute==");
        WebServiceCommand command = new WebServiceCommand
                .Builder()
                .setURI("http://207.167.28.31/PAPIService/REST/public/v1/1033/100/1/patron/29335002291059/messages")
                .setHTTPVerb("GET")
//                .debug()
                .build();
        CommandStatus result = command.execute();
        System.out.println("TEST_OUTPUT: '" + result.getStdout() + "'");
        assertNotNull(result);
        assertTrue(result.getStatus() == ResponseTypes.SUCCESS);
        
        command = new WebServiceCommand
                .Builder()
                .setURI("http://207.167.28.31/UhOh.html")
                .setHTTPVerb("GET")
//                .debug()
                .build();
        result = command.execute();
        System.out.println("TEST_OUTPUT: '" + result.getStdout() + "'");
        assertNotNull(result);
        assertTrue(result.getStatus() == ResponseTypes.CONFIG_ERROR);
    }
    
}
