/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class ProtocolPayloadTest {
    
    public ProtocolPayloadTest() {
    }

    /**
     * Test of addResponse method, of class ProtocolPayload.
     */
    @Test
    public void testSetResponse() {
        System.out.println("===setResponse===");
        int pos = 0;
        String s = "value 0";
        ProtocolPayload instance = new ProtocolPayload(5);
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("value 0|X|X|X|X|") == 0);
        
        pos = 0;
        s = "value again";
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("value again|X|X|X|X|") == 0);
        
        pos = 2;
        s = "two";
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("value again|X|two|X|X|") == 0);
        
        pos = 3;
        s = "three";
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("value again|X|two|three|X|") == 0);
        
        pos = 1;
        s = "one";
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("value again|one|two|three|X|") == 0);

        pos = 4;
        s = "four";
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("value again|one|two|three|four|") == 0);
        
        pos = 5;
        s = "five";
        try
        {
            instance.setPayloadSlot(pos, s);
        }
        catch (IndexOutOfBoundsException e)
        {
            System.out.println("expected exception, now try setResponse()");
        }
        instance.addResponse(s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("value again|one|two|three|four|five|") == 0);

        
    }
}