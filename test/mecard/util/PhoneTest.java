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
        String result = Phone.formatPhone(p);
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
}