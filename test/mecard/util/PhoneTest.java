package mecard.util;
import mecard.util.Phone;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PhoneTest
{
    
    public PhoneTest()
    {
    }

    /**
     * Test of formatPhone method, of class Phone.
     */
    @Test
    public void testFormatPhone()
    {
        System.out.println("==formatPhone==");
        String p = "7804366077";
        String expResult = "780-436-6077";
        String result = Phone.formatPhone(p, true);
        assertEquals(expResult, result);
        
        p = "7804366077";
        expResult = "780-436-6077";
        result = Phone.formatPhone(p);
        System.out.println(">>>>>>>"+result+"<<<<<<<<<");
        assertEquals(expResult, result);
        
        p = "780-436-6077";
        expResult = "780-436-6077";
        result = Phone.formatPhone(p);
        assertEquals(expResult, result);
        
        p = " 7804366077    ";
        expResult = "780-436-6077";
        result = Phone.formatPhone(p);
        assertEquals(expResult, result);
        
        p = "804366077    ";
        expResult = "000-000-0000";
        result = Phone.formatPhone(p);
        assertEquals(expResult, result);
        
        p = "366077";
        expResult = "000-000-0000";
        result = Phone.formatPhone(p);
        assertEquals(expResult, result);
        
        p = "077";
        expResult = "000-000-0000";
        result = Phone.formatPhone(p);
        assertEquals(expResult, result);
    }

    /**
     * Test of formatPhone method, of class Phone.
     */
    @Test
    public void testConstructor()
    {
        System.out.println("==Phone==");
        
        String p = "7804366077";
        String expResult = "780-436-6077";
        String result = Phone.formatPhone(p, false);
        assertEquals(expResult, result);
        
        p = "780-436-6077";
        expResult = "780-436-6077";
        result = Phone.formatPhone(p);
        assertEquals(expResult, result);
        
        Phone phone = new Phone(null);
        expResult = "000-000-0000";
        result = phone.toString();
        assertEquals(expResult, result);
        
        phone = new Phone("");
        expResult = "000-000-0000";
        result = phone.toString();
        assertEquals(expResult, result);
        
        phone = new Phone("    ");
        expResult = "000-000-0000";
        result = phone.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of formatPhone method, of class Phone.
     */
    @Test
    public void testFormatPhone_String()
    {
        System.out.println("===formatPhone===");
        String p = "780 436 6071";
        String expResult = "780-436-6071";
        String result = Phone.formatPhone(p);
        assertTrue(expResult.compareTo(result) == 0);
        System.out.println("RESULT:"+result);
        
        p = "780 436 6071";
        result = Phone.formatPhone(p);
        assertTrue("780-436-6071".compareTo(result) == 0);
        System.out.println("RESULT:"+result);
        
        p = "40394813-81s-taff";
        result = Phone.formatPhone(p);
        assertTrue("403-948-1381".compareTo(result) == 0);
        System.out.println("RESULT:"+result);
        
        p = "40394652-49e-mail";
        result = Phone.formatPhone(p);
        assertTrue("403-946-5249".compareTo(result) == 0);
        System.out.println("RESULT:"+result);
        
        p = "403.7-42.-0868";
        result = Phone.formatPhone(p);
        assertTrue("403-742-0868".compareTo(result) == 0);
        System.out.println("RESULT:"+result);
        
        p = "867.8-75.-7658";
        result = Phone.formatPhone(p);
        assertTrue("867-875-7658".compareTo(result) == 0);
        System.out.println("RESULT:"+result);
        p = "403.7-42.-0868";
        result = Phone.formatPhone(p);
        assertTrue("403-742-0868".compareTo(result) == 0);
        System.out.println("RESULT:"+result);
        
        p = "4033182-542-cell";
        result = Phone.formatPhone(p);
        assertTrue("403-318-2542".compareTo(result) == 0);
        System.out.println("RESULT:"+result);
        
        p = "123";
        result = Phone.formatPhone(p);
        assertTrue("000-000-0000".compareTo(result) == 0);
        System.out.println("RESULT:"+result);
        
        p = "527-4943";
        result = Phone.formatPhone(p);
        assertTrue("000-000-0000".compareTo(result) == 0);
        System.out.println("RESULT:"+result);
        
        p = "587-550-2008 EXT 121";
        System.out.println("TESTING =>"+p+"<=");
        result = Phone.formatPhone(p);
        System.out.println("RESULT =>"+result+"<=");
        assertTrue("587-550-2008".compareTo(result) == 0);
        
        
        p = "587-550-2008 EXT 12345";
        result = Phone.formatPhone(p);
        assertTrue("587-550-2008".compareTo(result) == 0);
        
        Phone phone = new Phone("");
        assertTrue(phone.isUnset());
        
        phone = new Phone("999-999-9999");
        assertFalse(phone.isUnset());
        
        phone = new Phone("999-999-9999 bob");
        assertFalse(phone.isUnset());
        
        phone = new Phone("1");
        assertTrue(phone.isUnset());
        
        System.out.println("===formatPhone===");
    }
}