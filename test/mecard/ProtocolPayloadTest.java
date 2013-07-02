/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard;

import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
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
        assertTrue(instance.toString().compareTo("[\"value 0\",\"X\",\"X\",\"X\",\"X\"]") == 0);
        
        pos = 0;
        s = "value again";
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("[\"value again\",\"X\",\"X\",\"X\",\"X\"]") == 0);
        
        pos = 2;
        s = "two";
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("[\"value again\",\"X\",\"two\",\"X\",\"X\"]") == 0);
        
        pos = 3;
        s = "three";
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("[\"value again\",\"X\",\"two\",\"three\",\"X\"]") == 0);
        
        pos = 1;
        s = "one";
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("[\"value again\",\"one\",\"two\",\"three\",\"X\"]") == 0);

        pos = 4;
        s = "four";
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("[\"value again\",\"one\",\"two\",\"three\",\"four\"]") == 0);
        
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
        assertTrue(instance.toString().compareTo("[\"value again\",\"one\",\"two\",\"three\",\"four\",\"five\"]") == 0);

        
    }

    /**
     * Test of size method, of class ProtocolPayload.
     */
    @Test
    public void testSize()
    {
        System.out.println("size");
        ProtocolPayload instance = new ProtocolPayload();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        
        int pos = 0;
        String s = "value 0";
        instance = new ProtocolPayload(5);
        instance.setPayloadSlot(pos, s);
        System.out.println("RESP:"+instance);
        assertTrue(instance.toString().compareTo("[\"value 0\",\"X\",\"X\",\"X\",\"X\"]") == 0);
        assertTrue(instance.getPayload().size() == 5);
    }

    /**
     * Test of setPayloadSlot method, of class ProtocolPayload.
     */
    @Test
    public void testSetPayloadSlot()
    {
        System.out.println("==setPayloadSlot==");
        int pos = 0;
        String s = "slot_0";
        ProtocolPayload instance = new ProtocolPayload(10);
        instance.setPayloadSlot(pos, s);
        assertTrue(instance.toString().compareTo("[\"slot_0\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\"]") == 0);
        
        instance.setPayloadSlot(1, "slot_1");
        assertTrue(instance.toString().compareTo("[\"slot_0\",\"slot_1\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\"]") == 0);
        
        instance.setPayloadSlot(9, "slot_9");
        assertTrue(instance.toString().compareTo("[\"slot_0\",\"slot_1\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"slot_9\"]") == 0);
        
        instance.setPayloadSlot(8, "slot_8");
        assertTrue(instance.toString().compareTo("[\"slot_0\",\"slot_1\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"slot_8\",\"slot_9\"]") == 0);
        
        instance.setPayloadSlot(7, "slot_7");
        assertTrue(instance.toString().compareTo("[\"slot_0\",\"slot_1\",\"X\",\"X\",\"X\",\"X\",\"X\",\"slot_7\",\"slot_8\",\"slot_9\"]") == 0);
        
        System.out.println("RESULT:"+instance.toString());
    }


    /**
     * Test of toString method, of class ProtocolPayload.
     */
    @Test
    public void testToString()
    {
        System.out.println("==toString==");
        ProtocolPayload instance = new ProtocolPayload();
        instance.addResponse("this");
        instance.addResponse("is");
        instance.addResponse("a");
        instance.addResponse("test");
        String expResult = "[\"this\",\"is\",\"a\",\"test\"]";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}