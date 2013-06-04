/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import mecard.ResponseTypes;
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
        ResponseTypes code = ResponseTypes.OK;
        Response instance = new Response(code);
        assertTrue(instance.toString().equals(ResponseTypes.OK.toString()));
    }

    /**
     * Test of addPayload method, of class Response.
     */
    @Test
    public void testSetResponse_String() {
        System.out.println("==setResponseString==");
        ResponseTypes code = ResponseTypes.OK;
        Response instance = new Response(code);
        instance.addPayload("Did well");
//        assertTrue(instance.toString().equals(ResponseTypes.OK.toString()));
        System.out.println("RESP:'"+instance+"'");
    }

    
    /**
     * Test of toString method, of class Response.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Response instance = new Response();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPayload method, of class Response.
     */
    @Test
    public void testSetResponse() {
        System.out.println("setResponse");
        String value = "";
        Response instance = new Response();
        instance.addPayload(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}