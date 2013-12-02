
package mecard;

import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */


public class ResponseTest
{
    
    public ResponseTest()
    {
    }

    /**
     * Test of setResponse method, of class Response.
     */
    @Test
    public void testSetResponse()
    {
        System.out.println("==setResponse==");
        String s = "this";
        Response instance = new Response();
        instance.setResponse(s);
        assertTrue(instance.getMessage().compareTo(s) == 0);
        instance.setResponse(s);
        System.out.println("MSG:'" + instance.getMessage() + "'");
        assertTrue(instance.getMessage().compareTo(s + "\\a" + s) == 0);
    }
    
}
