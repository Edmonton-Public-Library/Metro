
package mecard.customer.polaris;

import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class PAPIFormattedTableTest
{
    
    public PAPIFormattedTableTest()
    {
    }

    /**
     * Test of getInstanceOf method, of class PAPIFormattedTable.
     */
    @Test
    public void testGetInstanceOf()
    {
        System.out.println("==getInstanceOf==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.DataType.XML_CREATE);
        assertNotNull(papiTable);
        assertFalse(papiTable.setValue("LogonBranchID", "12345"));
        assertTrue(papiTable.setValue("LOGON_BRANCH_ID", "12345"));
    }

    /**
     * Test of getData method, of class PAPIFormattedTable.
     */
    @Test
    public void testGetData()
    {
        System.out.println("==getData==");
//        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.DataType.XML_CUSTOMER_CREATE);
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.DataType.XML_CREATE);
        assertNotNull(papiTable);
        assertTrue(papiTable.setValue("LOGON_BRANCH_ID", "1"));
        String testXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PatronRegistrationCreateData><LogonBranchID>1</LogonBranchID></PatronRegistrationCreateData>";
        
        assertTrue(papiTable.getData().compareTo(testXML) == 0);
        assertTrue(papiTable.setValue("PATRON_BRANCH_ID", String.valueOf(PAPIFormattedTable.CreateOrder.PATRON_BRANCH_ID.ordinal())));
        assertTrue(papiTable.getValue(PAPIFormattedTable.CreateOrder.PATRON_BRANCH_ID.name()).compareTo("4") == 0);
        // Test if the insertion doesn't matter for well formed order on output.
        assertTrue(papiTable.setValue("ERECEIPT_OPTION_ID", "39"));
        assertTrue(papiTable.setValue("BARCODE", "38"));
        assertTrue(papiTable.setValue("PASSWORD", "29"));
        assertTrue(papiTable.setValue("DELIVERY_OPTION_ID", String.valueOf(PAPIFormattedTable.CreateOrder.DELIVERY_OPTION_ID.ordinal())));
        assertTrue(papiTable.setValue("EMAIL_ADDRESS", "25"));
        assertTrue(papiTable.setValue("USER_1", "16"));
        assertTrue(papiTable.setValue("NAME_LAST", "14"));
        
        assertTrue(papiTable.setValue("POSTAL_CODE", "5"));
        assertTrue(papiTable.setValue("CITY", "7"));
        assertTrue(papiTable.setValue("STATE", "8"));
        assertTrue(papiTable.setValue("COUNTRY_ID", "9"));
        assertTrue(papiTable.setValue("STREET_ONE", "11"));
        assertTrue(papiTable.setValue("NAME_FIRST", "13"));
        
        
        
        
        
        
        System.out.println("XML:'\n" + papiTable.getData() + "'");
    }

    /**
     * Test of getHeader method, of class PAPIFormattedTable.
     */
    @Test
    public void testGetHeader()
    {
        System.out.println("==getHeader==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.DataType.XML_CREATE);
        assertNotNull(papiTable.getHeader());
        papiTable.setValue("BARCODE", "21221012345678");
        assertNotNull(papiTable.getHeader());
        assertTrue(papiTable.getHeader().compareTo("21221012345678") == 0);
        System.out.println("GET_VALUE: '" + papiTable.getHeader() + "'");
    }

    /**
     * Test of getName method, of class PAPIFormattedTable.
     */
    @Test
    public void testGetName()
    {
        System.out.println("==getName==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.DataType.XML_CREATE);
        System.out.println("GET_NAME: '" + papiTable.getName() + "'");
        assertTrue(papiTable.getName().compareTo("USER") == 0);
    }

    /**
     * Test of getValue method, of class PAPIFormattedTable.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("==getValue==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.DataType.XML_CREATE);
        papiTable.setValue("CITY", "Edmonton");
        System.out.println("GET_VALUE: '" + papiTable.getValue("CITY") + "'");
        assertTrue(papiTable.getValue("CITY").compareTo("Edmonton") == 0);
        assertNotNull(papiTable.getValue("POSTAL_CODE"));
        assertTrue(papiTable.getValue("POSTAL_CODE").compareTo("") == 0);
    }

    /**
     * Test of setValue method, of class PAPIFormattedTable.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.DataType.XML_CREATE);
        // Pass the values, or names of the the setValue method.
        // WRONG:
        assertFalse(papiTable.setValue("LogonBranchID", "12345"));
        // RIGHT:
        assertTrue(papiTable.setValue("LOGON_BRANCH_ID", "12345"));
    }

    /**
     * Test of renameKey method, of class PAPIFormattedTable.
     */
    @Test
    public void testRenameKey()
    {
        System.out.println("==renameKey==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.DataType.XML_CREATE);
        assertTrue(papiTable.setValue("LOGON_BRANCH_ID", "12345"));
        assertTrue(papiTable.renameKey("LOGON_BRANCH_ID", "PASSWORD_2"));
    }

    /**
     * Test of deleteValue method, of class PAPIFormattedTable.
     */
    @Test
    public void testDeleteValue()
    {
        System.out.println("==deleteValue==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.DataType.XML_CREATE);
        assertFalse(papiTable.deleteValue("LOGON_BRANCH_ID"));
        assertTrue(papiTable.setValue("LOGON_BRANCH_ID", "12345"));
        System.out.println("Before GET_VALUE: '" + papiTable.getValue("LOGON_BRANCH_ID") + "'");
        assertTrue(papiTable.deleteValue("LOGON_BRANCH_ID"));
        System.out.println("After GET_VALUE: '" + papiTable.getValue("LOGON_BRANCH_ID") + "'");
    }
    
}
