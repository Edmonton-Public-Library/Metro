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
        String command = "QA0|";
        SIP2Responder instance = new SIP2Responder(command, true);
        String expResult = "RA1||";
        String result = instance.getResponse();
//        System.out.println("RESULT:"+result);
        assertEquals(expResult, result);
        // to test failure you have to edit the sip2_config.xml file.
        
        command = "QA0|12345678abcdef|21221012345678|64058|";
        instance = new SIP2Responder(command, true);
        expResult = "RA1||";
        result = instance.getResponse();
        System.out.println("RESULT:"+result);
        assertEquals(expResult, result);
        
        // Fail because PIN is wrong.
        command = "QA0|12345678abcdef|21221012345678|1111|";
        instance = new SIP2Responder(command, true);
        expResult = "RA5||";
        result = instance.getResponse();
        System.out.println("RESULT:"+result);
        assertEquals(expResult, result);
    }
}