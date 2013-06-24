/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import api.Request;
import mecard.Exception.MalformedCommandException;
import mecard.QueryTypes;
import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class RequestTest {
    
    public RequestTest() {
    }

    /**
     * Test of getCommandType method, of class Request.
     */
    @Test
    public void testGetCommandType() {
        System.out.println("===getCommandType===");
        Request instance = new Request("[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]");
        QueryTypes expResult = QueryTypes.GET_STATUS;
        QueryTypes result = instance.getCommandType();
        assertEquals(expResult, result);
        
        instance = new Request("[\"QB0\",\"123456789abcdef\",\"21221012345678\",\"64058\"]");
        expResult = QueryTypes.GET_CUSTOMER;
        result = instance.getCommandType();
        assertEquals(expResult, result);
        
        String custReq = 
                "[\"QC0\",\"342abf3cb129ffccb74\",\"21221012345678\",\"6058\","
                + "\"Billy, Balzac\",\"12345 123 St.\",\""
                + "Edmonton\",\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\","
                + "\"7804964058\",\"19750822\",\"20140602\",\"\",\"\",\"Y\",\"Y\","
                + "\"N\",\"Y\",\"Y\",\"N\"]";
        instance = new Request(custReq);
        expResult = QueryTypes.CREATE_CUSTOMER;
        result = instance.getCommandType();
        assertEquals(expResult, result);
        
        custReq = 
                "[\"QD0\",\"342abf3cb129ffccb74\",\"21221012345678\",\"6058\","
                + "\"Billy, Balzac\",\"12345 123 St.\",\""
                + "Edmonton\",\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\","
                + "\"7804964058\",\"19750822\",\"20140602\",\"\",\"\",\"Y\","
                + "\"Y\",\"N\",\"Y\",\"Y\",\"N\"]";
        instance = new Request(custReq);
        expResult = QueryTypes.UPDATE_CUSTOMER;
        result = instance.getCommandType();
        assertEquals(expResult, result);
        
        try
        {
            instance = new Request("[\"QN0\"]");
        }
        catch (Exception ex)
        {
            assertTrue(ex instanceof MalformedCommandException);
        }
     
    }

    /**
     * Test of getArgs method, of class Request.
     */
    @Test
    public void testGetArgs() {
        System.out.println("===getArgs===");
        Request instance = new Request("[\"QA0\",\"123456789abcdef\"]");
        String expResult = "[]";
        String result = instance.getArgs();
        assertEquals(expResult, result);
        
        instance = new Request("[\"QA0\",\"123456789abcdef\",\"one\",\"two\",\"three\"]");
        expResult = "[\"one\",\"two\",\"three\"]";
        result = instance.getArgs();
        assertEquals(expResult, result);        
    }

    /**
     * Test of toString method, of class Request.
     */
    @Test
    public void testToString() {
        System.out.println("===toString===");
        String r = "[\"QB0\",\"123456789abcdef\",\"21221012345678\",\"64058\"]";
        Request instance = new Request(r);
        String expResult = r;
        String result = instance.toString();
        System.out.println("RESULT:"+instance.toString());
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransactionId method, of class Request.
     */
    @Test
    public void testGetTransactionId() {
        System.out.println("===getTransactionId===");
        Request instance = null;
        try
        {
            instance = new Request("[\"QA0\"]");
        }
        catch (Exception ex)
        {
            assertTrue(ex instanceof MalformedCommandException);
        }
        
        instance = new Request("[\"QB0\",\"123456789abcdef\",\"one\",\"two\"]");
        String expResult = "123456789abcdef";
        String result = instance.getTransactionId();
        assertTrue(expResult.compareTo(result) == 0);
    }

    /**
     * Test of get method, of class Request.
     */
    @Test
    public void testGet() {
        System.out.println("===get===");
        int ordinal = 0;
        Request instance = new Request("[\"QA0\",\"123456789abcdef\",\"one\",\"two\",\"three\"]");
        String expResult = "one";
        String result = instance.get(ordinal);
        assertEquals(expResult, result);
        
        ordinal = 1;
        expResult = "two";
        result = instance.get(ordinal);
        assertEquals(expResult, result);
        
        ordinal = 2;
        expResult = "three";
        result = instance.get(ordinal);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomer method, of class Request.
     */
    @Test
    public void testGetCustomer()
    {
        System.out.println("==getCustomer==");           
        String custRequest =
                "[\"QC0\",\"342abf3cb129ffccb74\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\""
                + "Edmonton\",\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\",\"7804964058\",\"20050101\",\""
                + "20140602\",\"X\",\"X\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"Balzac\",\"Billy\"]";
        String custString =
                "[\"QC0\",\"342abf3cb129ffccb74\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\""
                + "Edmonton\",\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\",\"7804964058\",\"20050101\",\""
                + "20140602\",\"X\",\"X\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"Balzac\",\"Billy\"]";
        Request request = new Request(custRequest);
        Customer c1 = new Customer(request.toString());
        Customer c2 = new Customer(custString);
        System.out.println("C1:"+c1);
        System.out.println("C2:"+c2);
        assertEquals(c1, c2);
    }
}