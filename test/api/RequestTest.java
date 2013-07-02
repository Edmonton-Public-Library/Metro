
package api;

import mecard.Exception.MalformedCommandException;
import mecard.QueryTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class RequestTest
{
    
    public RequestTest()
    {
    }

    /**
     * Test of getCommandType method, of class Request.
     */
    @Test
    public void testGetCommandType()
    {
        System.out.println("==getCommandType==");
        String cmd = "[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
        Request instance = new Request(cmd);
        QueryTypes expResult = QueryTypes.GET_STATUS;
        QueryTypes result = instance.getCommandType();
        assertEquals(expResult, result);
        
        cmd = "[\"QB0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
        instance = new Request(cmd);
        expResult = QueryTypes.GET_CUSTOMER;
        result = instance.getCommandType();
        assertEquals(expResult, result);
        
        cmd = "[\"QC0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"Blah blah blah\",\"something_else\"]";
        instance = new Request(cmd);
        expResult = QueryTypes.CREATE_CUSTOMER;
        result = instance.getCommandType();
        assertEquals(expResult, result);
        
        cmd = "[\"QD0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
        instance = new Request(cmd);
        expResult = QueryTypes.UPDATE_CUSTOMER;
        result = instance.getCommandType();
        assertEquals(expResult, result);
        
        cmd = "[\"QD\"]";
        try
        {
            instance = new Request(cmd);
            result = instance.getCommandType();
        }
        catch (MalformedCommandException ex)
        {
            System.out.println("Successfully thrown exception");
        }
        
        cmd = "";
        try
        {
            instance = new Request(cmd);
            result = instance.getCommandType();
        }
        catch (MalformedCommandException ex)
        {
            System.out.println("Successfully thrown exception");
        }
        
        cmd = null;
        try
        {
            instance = new Request(cmd);
            result = instance.getCommandType();
        }
        catch (MalformedCommandException ex)
        {
            System.out.println("Successfully thrown exception");
        }
    }

    /**
     * Test of getArgs method, of class Request.
     */
    @Test
    public void testGetArgs()
    {
        System.out.println("==getArgs==");
        String cmd = "[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"21221\",\"12345\"]";
        Request instance = new Request(cmd);
        
        String expResult = "[\"21221\",\"12345\"]";
        String result = instance.getArgs();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Request.
     */
    @Test
    public void testToString()
    {
        System.out.println("==toString==");
        String cmd = "[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"21221\",\"12345\"]";
        Request instance = new Request(cmd);
        
        String expResult = cmd;
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransactionId method, of class Request.
     */
    @Test
    public void testGetTransactionId()
    {
        System.out.println("==getTransactionId==");
        String cmd = "[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"21221\",\"12345\"]";
        Request instance = new Request(cmd);
        
        String expResult = "55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d";
        String result = instance.getTransactionId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomerField method, of class Request.
     */
    @Test
    public void testGet()
    {
        System.out.println("==get==");
        
        String cmd = "[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"21221\",\"12345\"]";
        Request instance = new Request(cmd);
        String expResult = "21221";
        String result = instance.getUserId();
        assertEquals(expResult, result);
        
        instance = new Request(cmd);
        expResult = "12345";
        result = instance.getUserPin();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomerField method, of class Request.
     */
    @Test
    public void testGetCustomerField()
    {
        System.out.println("getCustomerField");
        CustomerFieldTypes cField = null;
        Request instance = null;
        String expResult = "";
        String result = instance.getCustomerField(cField);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserId method, of class Request.
     */
    @Test
    public void testGetUserId()
    {
        System.out.println("getUserId");
        Request instance = null;
        String expResult = "";
        String result = instance.getUserId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserPin method, of class Request.
     */
    @Test
    public void testGetUserPin()
    {
        System.out.println("getUserPin");
        Request instance = null;
        String expResult = "";
        String result = instance.getUserPin();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCustomer method, of class Request.
     */
    @Test
    public void testGetCustomer()
    {
        System.out.println("getCustomer");
        Request instance = null;
        Customer expResult = null;
        Customer result = instance.getCustomer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}