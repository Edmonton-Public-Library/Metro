package api;

import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Note on usage: the environment.properties file must have Symphony set in all 
 * ILS interaction methods.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SymphonyAPIBuilderTest
{
    
    public SymphonyAPIBuilderTest()
    {
    }

    /**
     * Test of getCustomer method, of class SymphonyAPIBuilder.
     */
    @Test
    public void testGetUser()
    {
        System.out.println("==getUser==");
        String userId = "21221012345678";
        String userPin = "64058";
        SymphonyAPIBuilder api = new SymphonyAPIBuilder();
        Response responder = new Response();
        Command command = api.getCustomer(userId, userPin, responder);
        System.out.println("CMD:" + command.toString());
    }

    /**
     * Test of getFormatter method, of class SymphonyAPIBuilder.
     */
    @Test
    public void testGetFormatter()
    {
        System.out.println("==getFormatter==");
        SymphonyAPIBuilder instance = new SymphonyAPIBuilder();
        assertTrue(instance.getFormatter() != null);
    }

    /**
     * Test of getCustomer method, of class SymphonyAPIBuilder.
     */
    @Test
    public void testGetCustomer()
    {
        System.out.println("===getCustomer===");
        String userId = "21221012345678";
        String userPin = "64058";
        Response response = new Response();
        SymphonyAPIBuilder instance = new SymphonyAPIBuilder();
        String expResult = "/home/metro/bimport/dumpflatuser ";
        Command result = instance.getCustomer(userId, userPin, response);
        assertEquals(expResult, result.toString());
    }
}