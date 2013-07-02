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

    /**
     * Test of createUser method, of class SymphonyAPIBuilder.
     */
    @Test
    public void testCreateUser()
    {
        System.out.println("===createUser===");
        String custReq =
                "[\"QC0\",\"342abf3cb129ffccb74\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\""
                + "Edmonton\",\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\",\"7804964058\",\"19750822\",\"20140602\",\"Balzac\",\"Billy\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"Balzac\",\"Billy\"]";
        Customer customer = new Customer(custReq);
        Response response = new Response();
        SymphonyAPIBuilder instance = new SymphonyAPIBuilder();
        String expResult = ""; //"echo <flatuser> /home/metro/bimport/loadflatuser -aR -bR -l\"ADMIN|PCGUI-DISP\" -mu -n ";
        Command result = instance.createUser(customer, response);
        assertEquals(expResult, result.toString());
    }

    /**
     * Test of updateUser method, of class SymphonyAPIBuilder.
     */
    @Test
    public void testUpdateUser()
    {
        System.out.println("updateUser");
        Customer customer = null;
        Response response = new Response();
        SymphonyAPIBuilder instance = new SymphonyAPIBuilder();
        Command expResult = null;
        Command result = instance.updateUser(customer, response);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}