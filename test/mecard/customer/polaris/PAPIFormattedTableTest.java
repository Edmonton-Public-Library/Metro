
package mecard.customer.polaris;

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
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML);
        assertNotNull(papiTable);
        assertFalse(papiTable.setValue("LogonBranchID", "12345"));
        assertTrue(papiTable.setValue("C_LOGON_BRANCH_ID", "12345"));
    }

    /**
     * Test of getData method, of class PAPIFormattedTable.
     */
    @Test
    public void testGetData()
    {
        System.out.println("==getData==");
//        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML_CUSTOMER_CREATE);
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML);
        assertNotNull(papiTable);
        assertTrue(papiTable.setValue("C_LOGON_BRANCH_ID", "1"));
        String testXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PatronRegistrationCreateData><LogonBranchID>1</LogonBranchID></PatronRegistrationCreateData>";
        
        assertTrue(papiTable.getData().compareTo(testXML) == 0);
        assertTrue(papiTable.setValue("C_PATRON_BRANCH_ID", String.valueOf(PAPIFormattedTable.Order.C_PATRON_BRANCH_ID.ordinal())));
        assertTrue(papiTable.getValue(PAPIFormattedTable.Order.C_PATRON_BRANCH_ID.name()).compareTo("4") == 0);
        // Test if the insertion doesn't matter for well formed order on output.
        assertTrue(papiTable.setValue("C_ERECEIPT_OPTION_ID", "39"));
        assertTrue(papiTable.setValue("C_BARCODE", "38"));
        assertTrue(papiTable.setValue("C_PASSWORD", "29"));
        assertTrue(papiTable.setValue("C_DELIVERY_OPTION_ID", String.valueOf(PAPIFormattedTable.Order.C_DELIVERY_OPTION_ID.ordinal())));
        assertTrue(papiTable.setValue("C_EMAIL_ADDRESS", "25"));
        assertTrue(papiTable.setValue("C_USER_1", "16"));
        assertTrue(papiTable.setValue("C_NAME_LAST", "14"));
        
        assertTrue(papiTable.setValue("C_POSTAL_CODE", "5"));
        assertTrue(papiTable.setValue("C_CITY", "7"));
        assertTrue(papiTable.setValue("C_STATE", "8"));
        assertTrue(papiTable.setValue("C_COUNTRY_ID", "9"));
        assertTrue(papiTable.setValue("C_STREET_ONE", "11"));
        assertTrue(papiTable.setValue("C_NAME_FIRST", "13"));
        
        
        
        
        
        
        System.out.println("XML:'\n" + papiTable.getData() + "'");
    }

    /**
     * Test of getHeader method, of class PAPIFormattedTable.
     */
    @Test
    public void testGetHeader()
    {
        System.out.println("==getHeader==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML);
        assertNotNull(papiTable.getHeader());
        papiTable.setValue("C_BARCODE", "21221012345678");
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
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML);
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
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML);
        papiTable.setValue("C_CITY", "Edmonton");
        System.out.println("GET_VALUE: '" + papiTable.getValue("C_CITY") + "'");
        assertTrue(papiTable.getValue("C_CITY").compareTo("Edmonton") == 0);
        assertNotNull(papiTable.getValue("C_POSTAL_CODE"));
        assertTrue(papiTable.getValue("C_POSTAL_CODE").compareTo("") == 0);
    }

    /**
     * Test of setValue method, of class PAPIFormattedTable.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML);
        // Pass the values, or names of the the setValue method.
        // WRONG:
        assertFalse(papiTable.setValue("LogonBranchID", "12345"));
        // RIGHT:
        assertTrue(papiTable.setValue("C_LOGON_BRANCH_ID", "12345"));
    }

    /**
     * Test of renameKey method, of class PAPIFormattedTable.
     */
    @Test
    public void testRenameKey()
    {
        System.out.println("==renameKey==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML);
        assertTrue(papiTable.setValue("C_LOGON_BRANCH_ID", "12345"));
        assertTrue(papiTable.renameKey("C_LOGON_BRANCH_ID", "C_PASSWORD_2"));
    }

    /**
     * Test of deleteValue method, of class PAPIFormattedTable.
     */
    @Test
    public void testDeleteValue()
    {
        System.out.println("==deleteValue==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML);
        assertFalse(papiTable.deleteValue("C_LOGON_BRANCH_ID"));
        assertTrue(papiTable.setValue("C_LOGON_BRANCH_ID", "12345"));
        System.out.println("Before GET_VALUE: '" + papiTable.getValue("C_LOGON_BRANCH_ID") + "'");
        assertTrue(papiTable.deleteValue("C_LOGON_BRANCH_ID"));
        System.out.println("After GET_VALUE: '" + papiTable.getValue("C_LOGON_BRANCH_ID") + "'");
    }
    
}
