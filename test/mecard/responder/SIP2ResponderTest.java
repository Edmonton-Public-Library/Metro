/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.responder;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class SIP2ResponderTest {
    
    public SIP2ResponderTest() {
    }

    /**
     * Test of getResponse method, of class SIP2Responder.
     */
    @Test
    public void testGetResponse() {
        System.out.println("===getResponse===");
        SIP2Responder instance = null;
        String expResult = "";
        String result = instance.getResponse();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}