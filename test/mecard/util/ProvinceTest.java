/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class ProvinceTest
{
    
    public ProvinceTest()
    {
    }

    /**
     * Test of setContent method, of class Province.
     */
    @Test
    public void testSetContent()
    {
        System.out.println("=== setContent ===");
        String province = "";
        Province instance = new Province(province);
        boolean expResult = true;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
        
        province = "";
        instance = new Province(province);
        expResult = true;
        result = instance.isValid();
        assertEquals(expResult, result);
        
        province = "Texas";
        instance = new Province(province);
        expResult = true;
        result = instance.isValid();
        assertEquals(expResult, result);
        
        province = "Alberta";
        instance = new Province(province);
        expResult = true;
        result = instance.isValid();
        assertEquals(expResult, result);
        
        province = "AB";
        instance = new Province(province);
        expResult = true;
        result = instance.isValid();
        assertEquals(expResult, result);
        
        province = "AB";
        instance = new Province(province);
        expResult = true;
        result = instance.isValid();
        assertEquals(expResult, result);
    }
}