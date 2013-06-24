/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard;

import api.Request;
import mecard.responder.APIResponder;
import mecard.responder.BImportResponder;
import mecard.responder.Responder;
import mecard.responder.SIP2Responder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class ProtocolTest
{
    
    public ProtocolTest()
    {
    }

    /**
     * Test of getCommand method, of class Protocol.
     */
    @Test
    public void testGetCommand()
    {
//        System.out.println("=== getCommand ===");
//        String cmd = "[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"Blah blah blah\",\"something_else\"]";
//        Protocol instance = new Protocol();
//        QueryTypes expResult = QueryTypes.GET_STATUS;
//        QueryTypes result = instance.getCommand(cmd);
//        assertEquals(expResult, result);
//        
//        cmd = "[\"QB0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
//        expResult = QueryTypes.GET_CUSTOMER;
//        result = instance.getCommand(cmd);
//        assertEquals(expResult, result);
//        
//        cmd = "[\"QC0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"Blah blah blah\",\"something_else\"]";
//        expResult = QueryTypes.CREATE_CUSTOMER;
//        result = instance.getCommand(cmd);
//        assertEquals(expResult, result);
//        
//        cmd = "[\"QD0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
//        expResult = QueryTypes.UPDATE_CUSTOMER;
//        result = instance.getCommand(cmd);
//        assertEquals(expResult, result);
//        
//        cmd = "[\"QD\"]";
//        try
//        {
//            result = instance.getCommand(cmd);
//        }
//        catch (MalformedCommandException ex)
//        {
//            System.out.println("Successfully thrown exception");
//        }
//        
//        cmd = "";
//        try
//        {
//            result = instance.getCommand(cmd);
//        }
//        catch (MalformedCommandException ex)
//        {
//            System.out.println("Successfully thrown exception");
//        }
//        
//        cmd = null;
//        try
//        {
//            result = instance.getCommand(cmd);
//        }
//        catch (MalformedCommandException ex)
//        {
//            System.out.println("Successfully thrown exception");
//        }
       
    }

    /**
     * Test of processInput method, of class Protocol.
     */
    @Test
    public void testProcessInput()
    {
        System.out.println("=== processInput ===");
        
        String cmd = "[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
        Protocol instance = new Protocol();
        String expResult = "[\"RA1\",\"\"]";
        String result = instance.processInput(cmd);
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponder method, of class Protocol.
     */
    @Test
    public void testGetResponder()
    {
        System.out.println("=== getResponder These tests depend on contents of env_config.xml ===");
        // WARNING: these tests are based on values set in the env_config.xml 
        // files. Please match expected response types with config requested
        // respondertypes.
        // QueryTypes.GET_STATUS;
        String command = "[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
        Request r = new Request(command);
        Protocol instance = new Protocol();
        Responder result = instance.getResponder(r);
        assertTrue(result instanceof SIP2Responder);
        
        // QueryTypes.GET_CUSTOMER;
        command = "[\"QB0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
        instance = new Protocol();
        r = new Request(command);
        result = instance.getResponder(r);
        assertTrue(result instanceof SIP2Responder);
        
        // QueryTypes.CREATE_CUSTOMER;
        command = "[\"QC0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
        instance = new Protocol();
        r = new Request(command);
        result = instance.getResponder(r);
        assertTrue(result instanceof BImportResponder);
        
        // QueryTypes.UPDATE_CUSTOMER;
        command = "[\"QD0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
        instance = new Protocol();
        r = new Request(command);
        result = instance.getResponder(r);
        assertTrue(result instanceof APIResponder);
    }
}