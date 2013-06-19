/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard;

import mecard.Exception.UnsupportedCommandException;
import mecard.Exception.MalformedCommandException;
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
        System.out.println("=== getCommand ===");
        String cmd = "QA0|Blah blah blah|something_else|\r";
        Protocol instance = new Protocol();
        QueryTypes expResult = QueryTypes.GET_STATUS;
        QueryTypes result = instance.getCommand(cmd);
        assertEquals(expResult, result);
        
        cmd = "QB0";
        expResult = QueryTypes.GET_CUSTOMER;
        result = instance.getCommand(cmd);
        assertEquals(expResult, result);
        
        cmd = "QC0|Blah blah blah|something_else|\r";
        expResult = QueryTypes.CREATE_CUSTOMER;
        result = instance.getCommand(cmd);
        assertEquals(expResult, result);
        
        cmd = "QD0|";
        expResult = QueryTypes.UPDATE_CUSTOMER;
        result = instance.getCommand(cmd);
        assertEquals(expResult, result);
        
        cmd = "QD";
        try
        {
            result = instance.getCommand(cmd);
        }
        catch (UnsupportedCommandException ex)
        {
            System.out.println("Successfully thrown exception");
        }
        
        cmd = "";
        try
        {
            result = instance.getCommand(cmd);
        }
        catch (MalformedCommandException ex)
        {
            System.out.println("Successfully thrown exception");
        }
        
        cmd = null;
        try
        {
            result = instance.getCommand(cmd);
        }
        catch (MalformedCommandException ex)
        {
            System.out.println("Successfully thrown exception");
        }
       
    }

    /**
     * Test of processInput method, of class Protocol.
     */
    @Test
    public void testProcessInput()
    {
        System.out.println("=== processInput ===");
        // QueryTypes.CREATE_CUSTOMER;
        String cmd = "QC0";
        Protocol instance = new Protocol();
        String expResult = "RA4|hello world|";
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
        String command = "QA0";
        Protocol instance = new Protocol();
        Responder result = instance.getResponder(command);
        assertTrue(result instanceof SIP2Responder);
        
        // QueryTypes.GET_CUSTOMER;
        command = "QB0";
        instance = new Protocol();
        result = instance.getResponder(command);
        assertTrue(result instanceof BImportResponder);
        
        // QueryTypes.CREATE_CUSTOMER;
        command = "QC0";
        instance = new Protocol();
        result = instance.getResponder(command);
        assertTrue(result instanceof BImportResponder);
        
        // QueryTypes.UPDATE_CUSTOMER;
        command = "QD0";
        instance = new Protocol();
        result = instance.getResponder(command);
        assertTrue(result instanceof APIResponder);
    }
}