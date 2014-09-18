
package mecard.customer.polaris;

import mecard.config.PolarisTable;
import mecard.customer.FormattedTable;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */
public class PolarisSQLFormattedTableTest
{
    
    public PolarisSQLFormattedTableTest()
    {
    }

    /**
     * Test of getData method, of class PolarisSQLFormattedTable.
     */
    @Test
    public void testGetData()
    {
        System.out.println("==getData==");
        FormattedTable pTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + pTable.getData() + "'");
    }

    /**
     * Test of getHeader method, of class PolarisSQLFormattedTable.
     */
    @Test
    public void testGetHeader()
    {
        System.out.println("==getHeader==");
        FormattedTable pTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + pTable.getHeader() + "'");
    }

    /**
     * Test of getName method, of class PolarisSQLFormattedTable.
     */
    @Test
    public void testGetName()
    {
        System.out.println("==getName==");
        FormattedTable pTable = new PolarisSQLFormattedTable(PolarisTable.PATRONS);
        System.out.println("GET_NAME: '" + pTable.getName() + "'");
        assertTrue(pTable.getName().compareTo("Patrons") == 0);
        
        pTable = new PolarisSQLFormattedTable(PolarisTable.ADDRESSES);
        System.out.println("GET_NAME: '" + pTable.getName() + "'");
        assertTrue(pTable.getName().compareTo("Addresses") == 0);
        
        pTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_ADDRESSES);
        System.out.println("GET_NAME: '" + pTable.getName() + "'");
        assertTrue(pTable.getName().compareTo("PatronAddresses") == 0);
        
        pTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_REGISTRATION);
        System.out.println("GET_NAME: '" + pTable.getName() + "'");
        assertTrue(pTable.getName().compareTo("PatronRegistration") == 0);
        
        pTable = new PolarisSQLFormattedTable(PolarisTable.POSTAL_CODES);
        System.out.println("GET_NAME: '" + pTable.getName() + "'");
        assertTrue(pTable.getName().compareTo("PostalCodes") == 0);
    }

    /**
     * Test of getValue method, of class PolarisSQLFormattedTable.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("==getValue==");
        FormattedTable pTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + 
                pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()) + "'");
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") == 0);
        assertNotNull(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()));
    }

    /**
     * Test of setValue method, of class PolarisSQLFormattedTable.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        FormattedTable pTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + 
                pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()) + "'");
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") == 0);
        assertNotNull(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()));
        assertTrue(pTable.getValue("Henry").compareTo("") == 0);
    }

    /**
     * Test of renameKey method, of class PolarisSQLFormattedTable.
     */
    @Test
    public void testRenameKey()
    {
        System.out.println("==renameKey==");
        FormattedTable pTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + 
                pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()) + "'");
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") != 0);
        assertTrue(pTable.renameKey(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(),
                PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()));
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") == 0);
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()).isEmpty());
    }

    /**
     * Test of deleteValue method, of class PolarisSQLFormattedTable.
     */
    @Test
    public void testDeleteValue()
    {
        System.out.println("==deleteValue==");
        FormattedTable pTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + 
                pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()) + "'");
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") != 0);
        assertTrue(pTable.renameKey(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(),
                PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()));
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") == 0);
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()).isEmpty());
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") == 0);
        pTable.deleteValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString());
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).isEmpty());
    }
    
}
