
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
        assertTrue(papiTable.setValue(PAPIElementOrder.C_LOGON_BRANCH_ID.name(), "12345"));
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
        assertTrue(papiTable.setValue(PAPIElementOrder.C_LOGON_BRANCH_ID.name(), "1"));
        String testXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PatronRegistrationCreateData><LogonBranchID>1</LogonBranchID></PatronRegistrationCreateData>";
        
        assertTrue(papiTable.getData().compareTo(testXML) == 0);
        assertTrue(papiTable.setValue(PAPIElementOrder.C_PATRON_BRANCH_ID.name(), String.valueOf(PAPIElementOrder.C_PATRON_BRANCH_ID.ordinal())));
        assertTrue(papiTable.getValue(PAPIElementOrder.C_PATRON_BRANCH_ID.name()).compareTo("4") == 0);
        // Test if the insertion doesn't matter for well formed order on output.
        assertTrue(papiTable.setValue(PAPIElementOrder.C_ERECEIPT_OPTION_ID.name(), "39"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_BARCODE.name(), "38"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_PASSWORD.name(), "29"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_DELIVERY_OPTION_ID.name(), String.valueOf(PAPIElementOrder.C_DELIVERY_OPTION_ID.ordinal())));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_EMAIL_ADDRESS.name(), "25"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_USER_1.name(), "16"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_NAME_LAST.name(), "14"));
        
        assertTrue(papiTable.setValue(PAPIElementOrder.C_POSTAL_CODE.name(), "5"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_CITY.name(), "7"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_STATE.name(), "8"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_COUNTRY_ID.name(), "9"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_STREET_ONE.name(), "11"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_NAME_FIRST.name(), "13"));
        
        
        
        
        
        
        System.out.println("XML:'\n" + papiTable.getData() + "'");
    }
    
    /**
     * Test of getData method, of class PAPIFormattedTable.
     */
    @Test
    public void testGetDataJSON()
    {
        System.out.println("==getDataJSON==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.JSON);
        assertNotNull(papiTable);
        assertTrue(papiTable.setValue(PAPIElementOrder.C_LOGON_BRANCH_ID.name(), "1"));
        String testXML = "{\"LogonBranchID\":\"1\"}";
        System.out.println("JSON:"+papiTable.getData());
        assertTrue(papiTable.getData().compareTo(testXML) == 0);
        assertTrue(papiTable.setValue(PAPIElementOrder.C_PATRON_BRANCH_ID.name(), String.valueOf(PAPIElementOrder.C_PATRON_BRANCH_ID.ordinal())));
        assertTrue(papiTable.getValue(PAPIElementOrder.C_PATRON_BRANCH_ID.name()).compareTo("4") == 0);
        // Test if the insertion doesn't matter for well formed order on output.
        assertTrue(papiTable.setValue(PAPIElementOrder.C_ERECEIPT_OPTION_ID.name(), "39"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_BARCODE.name(), "38"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_PASSWORD.name(), "29"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_DELIVERY_OPTION_ID.name(), String.valueOf(PAPIElementOrder.C_DELIVERY_OPTION_ID.ordinal())));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_EMAIL_ADDRESS.name(), "25"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_USER_1.name(), "16"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_NAME_LAST.name(), "14"));
        
        assertTrue(papiTable.setValue(PAPIElementOrder.C_POSTAL_CODE.name(), "5"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_CITY.name(), "7"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_STATE.name(), "8"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_COUNTRY_ID.name(), "9"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_STREET_ONE.name(), "11"));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_NAME_FIRST.name(), "13"));

        System.out.println("JSON:'\n" + papiTable.getData() + "'");
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
        papiTable.setValue(PAPIElementOrder.C_BARCODE.name(), "21221012345678");
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
        papiTable.setValue(PAPIElementOrder.C_CITY.name(), "Edmonton");
        System.out.println("GET_VALUE: '" + 
                papiTable.getValue(PAPIElementOrder.C_CITY.name()) + "'");
        assertTrue(papiTable.getValue(PAPIElementOrder.C_CITY.name()).compareTo("Edmonton") == 0);
        assertNotNull(papiTable.getValue(PAPIElementOrder.C_POSTAL_CODE.name()));
        assertTrue(papiTable.getValue(PAPIElementOrder.C_POSTAL_CODE.name()).compareTo("") == 0);
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
        // RIGHT:PAPIElementOrder.C_BARCODE.name()
        assertTrue(papiTable.setValue(PAPIElementOrder.C_LOGON_BRANCH_ID.name(), "12345"));
    }

    /**
     * Test of renameKey method, of class PAPIFormattedTable.
     */
    @Test
    public void testRenameKey()
    {
        System.out.println("==renameKey==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML);
        assertTrue(papiTable.setValue(PAPIElementOrder.C_LOGON_BRANCH_ID.name(), "12345"));
        assertTrue(papiTable.renameKey(PAPIElementOrder.C_LOGON_BRANCH_ID.name(), "C_PASSWORD_2"));
    }

    /**
     * Test of deleteValue method, of class PAPIFormattedTable.
     */
    @Test
    public void testDeleteValue()
    {
        System.out.println("==deleteValue==");
        PAPIFormattedTable papiTable = PAPIFormattedTable.getInstanceOf(PAPIFormattedTable.ContentType.XML);
        assertFalse(papiTable.deleteValue(PAPIElementOrder.C_LOGON_BRANCH_ID.name()));
        assertTrue(papiTable.setValue(PAPIElementOrder.C_LOGON_BRANCH_ID.name(), "12345"));
        System.out.println("Before GET_VALUE: '" + papiTable.getValue(PAPIElementOrder.C_LOGON_BRANCH_ID.name()) + "'");
        assertTrue(papiTable.deleteValue(PAPIElementOrder.C_LOGON_BRANCH_ID.name()));
        System.out.println("After GET_VALUE: '" + papiTable.getValue(PAPIElementOrder.C_LOGON_BRANCH_ID.name()) + "'");
    }
    
}
