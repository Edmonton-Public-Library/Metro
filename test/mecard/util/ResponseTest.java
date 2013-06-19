/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import api.Response;
import mecard.ResponseTypes;
import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class ResponseTest 
{
    
    public ResponseTest() 
    {    }

    /**
     * Test of setCode method, of class Response.
     */
    @Test
    public void testSetCode() {
        System.out.println("==setCode==");
        Response instance = new Response(ResponseTypes.OK);
//        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("RA1|") == 0);
        instance = new Response(ResponseTypes.BUSY);
        assertTrue(instance.toString().compareTo("RA2|") == 0);
        instance = new Response(ResponseTypes.ERROR);
        assertTrue(instance.toString().compareTo("RA9|") == 0);
        instance = new Response(ResponseTypes.FAIL);
        assertTrue(instance.toString().compareTo("RA5|") == 0);
        instance = new Response(ResponseTypes.INIT);
        assertTrue(instance.toString().compareTo("RA0|") == 0);
        instance = new Response(ResponseTypes.OK);
        assertTrue(instance.toString().compareTo("RA1|") == 0);
        instance = new Response(ResponseTypes.SUCCESS);
        assertTrue(instance.toString().compareTo("RA4|") == 0);
        instance = new Response(ResponseTypes.UNAVAILABLE);
        assertTrue(instance.toString().compareTo("RA3|") == 0);
        instance = new Response(ResponseTypes.UNAUTHORIZED);
        assertTrue(instance.toString().compareTo("RA6|") == 0);
    }

    
    /**
     * Test of toString method, of class Response.
     */
    @Test
    public void testToString() {
        System.out.println("===toString===");
        Response instance = new Response(ResponseTypes.OK);
//        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("RA1|") == 0);
    }

    /**
     * Test of addPayload method, of class Response.
     */
    @Test
    public void testSetResponse() {
        System.out.println("===setResponse===");
        String value = "test";
        Response instance = new Response(ResponseTypes.UNAUTHORIZED);
//        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("RA6|") == 0);
        
        instance.setResponse(value);
//        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("RA6|test|") == 0);
    }

    /**
     * Test of setCustomer method, of class Response.
     */
    @Test
    public void testSetCustomer() {
        System.out.println("===setCustomer===");
        Customer c = new Customer();
        c.setName("Billy, Balzac");
        Response instance = new Response();
        instance.setCustomer(c);
//        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("RA0|||Billy, Balzac||||||||||||||||||Balzac|Billy|") == 0);
    }
}