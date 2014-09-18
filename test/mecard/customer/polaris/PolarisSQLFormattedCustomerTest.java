
package mecard.customer.polaris;

import java.util.List;
import mecard.customer.FormattedTable;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class PolarisSQLFormattedCustomerTest
{
    
    public PolarisSQLFormattedCustomerTest()
    {
    }

    /**
     * Test of getFormattedCustomer method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testGetFormattedCustomer()
    {
        System.out.println("getFormattedCustomer");
        PolarisSQLFormattedCustomer instance = null;
        List<String> expResult = null;
        List<String> result = instance.getFormattedCustomer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFormattedHeader method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testGetFormattedHeader()
    {
        System.out.println("getFormattedHeader");
        PolarisSQLFormattedCustomer instance = null;
        List<String> expResult = null;
        List<String> result = instance.getFormattedHeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValue method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("setValue");
        String key = "";
        String value = "";
        PolarisSQLFormattedCustomer instance = null;
        boolean expResult = false;
        boolean result = instance.setValue(key, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertValue method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testInsertValue()
    {
        System.out.println("insertValue");
        String tableName = "";
        String key = "";
        String value = "";
        PolarisSQLFormattedCustomer instance = null;
        boolean expResult = false;
        boolean result = instance.insertValue(tableName, key, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of containsKey method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testContainsKey()
    {
        System.out.println("containsKey");
        String key = "";
        PolarisSQLFormattedCustomer instance = null;
        boolean expResult = false;
        boolean result = instance.containsKey(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValue method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("getValue");
        String key = "";
        PolarisSQLFormattedCustomer instance = null;
        String expResult = "";
        String result = instance.getValue(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertTable method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testInsertTable()
    {
        System.out.println("insertTable");
        FormattedTable formattedTable = null;
        int index = 0;
        PolarisSQLFormattedCustomer instance = null;
        instance.insertTable(formattedTable, index);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of renameField method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testRenameField()
    {
        System.out.println("renameField");
        String tableName = "";
        String originalFieldName = "";
        String newFieldName = "";
        PolarisSQLFormattedCustomer instance = null;
        boolean expResult = false;
        boolean result = instance.renameField(tableName, originalFieldName, newFieldName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeField method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testRemoveField()
    {
        System.out.println("removeField");
        String tableName = "";
        String fieldName = "";
        PolarisSQLFormattedCustomer instance = null;
        boolean expResult = false;
        boolean result = instance.removeField(tableName, fieldName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
