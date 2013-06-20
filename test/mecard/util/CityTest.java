
package mecard.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class CityTest
{
    
    public CityTest()
    {
    }

    /**
     * Test of getCity method, of class City.
     */
    @Test
    public void testGetCity_String()
    {
        System.out.println("==getCity==");
        String cityName = "Edmonton";
        String expResult = "ed";
        String result = City.getCity(cityName, true);
        assertEquals(expResult, result);
        
        cityName = "St. Albert";
        expResult = "sa";
        result = City.getCity(cityName, true);
        assertEquals(expResult, result);
       
        cityName = "fort saskatchewan";
        expResult = "fs";
        result = City.getCity(cityName, true);
        assertEquals(expResult, result);
        
        cityName = "strathcona";
        expResult = "sc";
        result = City.getCity(cityName, true);
        assertEquals(expResult, result);
        
        cityName = "Calgary";
        expResult = "Calgary"; // not listed as a member yet.
        result = City.getCity(cityName, true);
        assertEquals(expResult, result);
        
        cityName = null;
        expResult = ""; // 
        result = City.getCity(cityName, true);
        assertEquals(expResult, result);
        
        cityName = "    ";
        expResult = ""; // 
        result = City.getCity(cityName, true);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCity method, of class City.
     */
    @Test
    public void testGetCity_String_boolean()
    {
        System.out.println("==getCity (non-verbose)==");
        String cityName = "Edmonton";
        String expResult = "ed";
        String result = City.getCity(cityName);
        assertEquals(expResult, result);
        
        cityName = "St. Albert";
        expResult = "sa";
        result = City.getCity(cityName);
        assertEquals(expResult, result);
       
        cityName = "fort saskatchewan";
        expResult = "fs";
        result = City.getCity(cityName);
        assertEquals(expResult, result);
        
        cityName = "strathcona";
        expResult = "sc";
        result = City.getCity(cityName);
        assertEquals(expResult, result);
        
        cityName = "Calgary";
        expResult = "Calgary"; // not listed as a member yet.
        result = City.getCity(cityName);
        assertEquals(expResult, result);
        
        cityName = null;
        expResult = ""; // 
        result = City.getCity(cityName);
        assertEquals(expResult, result);
        
        cityName = "    ";
        expResult = ""; // 
        result = City.getCity(cityName);
        assertEquals(expResult, result);
    }
}