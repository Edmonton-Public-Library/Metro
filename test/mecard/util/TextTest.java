package mecard.util;
import mecard.config.CustomerFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;
import site.MeCardPolicy;

/**
 *
 * @author andrew
 */
public class TextTest
{
    
    public TextTest()
    {
    }

    /**
     * Test of isValidExpiryDate method, of class MeCardPolicy.
     */
    @Test
    public void testProperCase()
    {
        System.out.println("==setProperCase==");
        // TODO Set up tests.
        String custData = "initial string";
        String result   = Text.toDisplayCase(custData);
        String expected = "Initial String";
        assertTrue(expected.compareTo(result) == 0);
        
        custData = "Initial String";
        result   = Text.toDisplayCase(custData);
        expected = "Initial String";
        assertTrue(expected.compareTo(result) == 0);
        
        custData = " initial String";
        result   = Text.toDisplayCase(custData);
        expected = " Initial String";
        assertTrue(expected.compareTo(result) == 0);
        
        custData = "";
        result   = Text.toDisplayCase(custData);
        expected = "";
        assertTrue(expected.compareTo(result) == 0);
        
        custData = "mark-antony";
        result   = Text.toDisplayCase(custData);
        expected = "Mark-Antony";
        assertTrue(expected.compareTo(result) == 0);
        
        custData = "ALL CAPS";
        result   = Text.toDisplayCase(custData);
        expected = "All Caps";
        assertTrue(expected.compareTo(result) == 0);
    }

    /**
     * Test of getNew4DigitPin method, of class Text.
     */
    @Test
    public void testGetNewPin()
    {
        System.out.println("===getNewPin===");
        System.out.println("PIN:'" + Text.getNew4DigitPin() + "'");
        System.out.println("PIN:'" + Text.getNew4DigitPin() + "'");
        System.out.println("PIN:'" + Text.getNew4DigitPin() + "'");
        System.out.println("PIN:'" + Text.getNew4DigitPin() + "'");
        System.out.println("PIN:'" + Text.getNew4DigitPin() + "'");
        System.out.println("PIN:'" + Text.getNew4DigitPin() + "'");
        System.out.println("PIN:'" + Text.getNew4DigitPin() + "'");
        System.out.println("PIN:'" + Text.getNew4DigitPin() + "'");
        System.out.println("PIN:'" + Text.getNew4DigitPin() + "'");
        System.out.println("PIN:'" + Text.getNew4DigitPin() + "'");
    }
    
    /**
     * Test of getNew4DigitPin method, of class Text.
     */
    @Test
    public void testIsMaxDigits()
    {
        System.out.println("===isMaxDigits===");
        String userPin = "1234";
        assertTrue(Text.isMaximumDigits(userPin, 4));
        
        userPin = "a234";
        assertFalse(Text.isMaximumDigits(userPin, 4));
        
        userPin = "";
        assertFalse(Text.isMaximumDigits(userPin, 4));
        
        userPin = "12345";
        assertFalse(Text.isMaximumDigits(userPin, 4));
                
        userPin = "X";
        assertFalse(Text.isMaximumDigits(userPin, 4));
        
        userPin = "12345";
        assertTrue(Text.isMaximumDigits(userPin, 5));
        
        userPin = "a";
        assertFalse(Text.isMaximumDigits(userPin, 0)); // Intersting corner case.
    }
}