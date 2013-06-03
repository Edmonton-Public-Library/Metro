/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.config;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class BImportPropertyTypesTest {
    
    public BImportPropertyTypesTest() 
    {
    }

    /**
     * Test of toString method, of class BImportPropertyTypes.
     */
    @Test
    public void testToString() 
    {
        System.out.println("===toString===");
        BImportPropertyTypes instance = BImportPropertyTypes.DATABASE;
        String expResult = "database";
        String result = instance.toString();
        System.out.println("RESULT:"+result);
        assertEquals(expResult, result);
    }
}