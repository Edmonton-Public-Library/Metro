/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.customer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class CustomerFieldTypesTest
{
    
    public CustomerFieldTypesTest()
    {
    }

    /**
     * Test of values method, of class CustomerFieldTypes.
     */
    @Test
    public void testValues()
    {
        System.out.println("values");
        CustomerFieldTypes[] expResult = null;
        CustomerFieldTypes[] result = CustomerFieldTypes.values();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of valueOf method, of class CustomerFieldTypes.
     */
    @Test
    public void testValueOf()
    {
        System.out.println("==valueOf==");
        String name = "STREET";
        CustomerFieldTypes expResult = CustomerFieldTypes.STREET;
        CustomerFieldTypes result = CustomerFieldTypes.valueOf(name);
        assertEquals(expResult, result);

    }

    /**
     * Test of toString method, of class CustomerFieldTypes.
     */
    @Test
    public void testToString()
    {
        System.out.println("==toString==");
        CustomerFieldTypes instance = CustomerFieldTypes.STREET;
        String expResult = "STREET";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}