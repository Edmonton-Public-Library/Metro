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
        expResult = "436-6077";
        result = Phone.formatPhone(p);
        assertEquals(expResult, result);
        
        p = "366077";
        expResult = "6077";
        result = Phone.formatPhone(p);
        assertEquals(expResult, result);
        
        p = "077";
        expResult = "077";
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
        expResult = "";
        result = phone.toString();
        assertEquals(expResult, result);
        
        phone = new Phone("");
        expResult = "";
        result = phone.toString();
        assertEquals(expResult, result);
        
        phone = new Phone("    ");
        expResult = "";
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
        System.out.println("===formatPhone===");
    }
}