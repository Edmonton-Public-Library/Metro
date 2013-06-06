/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import mecard.QueryTypes;
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
        Request instance = new Request("QA0|");
        QueryTypes expResult = QueryTypes.GET_STATUS;
        QueryTypes result = instance.getCommandType();
        assertEquals(expResult, result);
        
        instance = new Request("QB0|123456789abcdef|21221012345678|64058|");
        expResult = QueryTypes.GET_CUSTOMER;
        result = instance.getCommandType();
        assertEquals(expResult, result);
        
        String custReq = 
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|20140602|||Y|Y|N|Y|Y|N|";
        instance = new Request(custReq);
        expResult = QueryTypes.CREATE_CUSTOMER;
        result = instance.getCommandType();
        assertEquals(expResult, result);
        
        custReq = 
                "QD0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|20140602|||Y|Y|N|Y|Y|N|";
        instance = new Request(custReq);
        expResult = QueryTypes.UPDATE_CUSTOMER;
        result = instance.getCommandType();
        assertEquals(expResult, result);
        
        instance = new Request("QN0|");
        expResult = QueryTypes.NULL;
        result = instance.getCommandType();
        assertEquals(expResult, result);
     
    }

    /**
     * Test of getArgs method, of class Request.
     */
    @Test
    public void testGetArgs() {
        System.out.println("===getArgs===");
        Request instance = new Request("QA0|123456789abcdef|");
        String expResult = "";
        String result = instance.getArgs();
        assertEquals(expResult, result);
        
        instance = new Request("QA0|123456789abcdef|one|two|three|");
        expResult = "one|two|three|";
        result = instance.getArgs();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Request.
     */
    @Test
    public void testToString() {
        System.out.println("===toString===");
        String r = "QB0|123456789abcdef|21221012345678|64058|";
        Request instance = new Request(r);
        String expResult = r;
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransactionId method, of class Request.
     */
    @Test
    public void testGetTransactionId() {
        System.out.println("===getTransactionId===");
        Request instance = new Request("QA0|");
        String expResult = "";
        String result = instance.getTransactionId();
        assertEquals(expResult, result);
        
        instance = new Request("QB0|");
        expResult = "";
        result = instance.getTransactionId();
        assertEquals(expResult, result);
        
        instance = new Request("QB0|123456789abcdef|one|two|");
        expResult = "123456789abcdef";
        result = instance.getTransactionId();
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class Request.
     */
    @Test
    public void testGet() {
        System.out.println("===get===");
        int ordinal = 0;
        Request instance = new Request("QA0|123456789abcdef|one|two|three|");
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
}