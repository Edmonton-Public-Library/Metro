/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.responder;

import mecard.Protocol;
import mecard.ResponseTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class BImportResponderTest
{
    
    public BImportResponderTest()
    {
    }

    /**
     * Test of getResponse method, of class BImportResponder.
     */
    @Test
    public void testGetResponse()
    {
        System.out.println("=== getResponse ===");
        // QueryTypes.CREATE_CUSTOMER;
        String cmd = "QC0";
        Protocol instance = new Protocol();
        String expResult = ResponseTypes.SUCCESS+"|hello world|";
        String result = instance.processInput(cmd);
        assertEquals(expResult, result);
    }
}