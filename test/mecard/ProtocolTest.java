/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard;

import mecard.Exception.UnsupportedCommandException;
import mecard.Exception.MalformedCommandException;
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
}