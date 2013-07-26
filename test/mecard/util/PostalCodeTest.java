/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrew
 */
public class PostalCodeTest
{
    /**
     * Test of setPostalCode method, of class PostalCode.
     */
    @Test
    public void testSetPostalCode()
    {
        System.out.println("==setPostalCode==");
        String postalCode = "T6G-0G4";
        PostalCode instance = new PostalCode(postalCode);
        boolean result = instance.isValid();
        assertEquals(true, result);
        
        postalCode = "T6G0G4";
        instance = new PostalCode(postalCode);
        result = instance.isValid();
        assertEquals(true, result);
        
        postalCode = "T6GOG4"; // contains an 'O'
        instance = new PostalCode(postalCode);
        result = instance.isValid();
        assertEquals(false, result);
        
        postalCode = "TTG0G4";
        instance = new PostalCode(postalCode);
        result = instance.isValid();
        assertEquals(false, result);
        
        postalCode = "      ";
        instance = new PostalCode(postalCode);
        result = instance.isValid();
        assertEquals(false, result);
        
        postalCode = "xxxxxxxxxxxxx";
        instance = new PostalCode(postalCode);
        result = instance.isValid();
        assertEquals(false, result);
        
        postalCode = "";
        instance = new PostalCode(postalCode);
        result = instance.isValid();
        assertEquals(false, result);
        
        postalCode = "";
        instance = new PostalCode(postalCode);
        result = instance.isValid();
        assertEquals(false, result);
        
        postalCode = "TTG0G4";
        instance = new PostalCode(null);
        result = instance.isValid();
        assertEquals(false, result);
    }

    /**
     * Test of formatPostalCode method, of class PostalCode.
     */
    @Test
    public void testFormatPostalCode()
    {
        System.out.println("==formatPostalCode==");
        String postalCode = "H0H0H0";
        String expResult = "H0H 0H0";
        String result = PostalCode.formatPostalCode(postalCode);
        assertEquals(expResult, result);
        
        postalCode = "H0H0H0bima";
        expResult = "H0H 0H0bima";
        result = PostalCode.formatPostalCode(postalCode);
        assertEquals(expResult, result);
    }
}