
package mecard.customer.polaris;

import java.util.HashMap;
import java.util.List;
import json.RequestDeserializer;
import mecard.Request;
import mecard.config.PolarisTable;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import mecard.customer.FormattedTable;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class PolarisSQLFormattedCustomerTest
{
    private Customer customer;
    public PolarisSQLFormattedCustomerTest()
    {
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        customer = request.getCustomer();
    }

    /**
     * Test of getFormattedCustomer method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testGetFormattedCustomer()
    {
        System.out.println("==getFormattedCustomer==");
        FormattedCustomer formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        System.out.println("FormattedCustomer: '" + formattedCustomer.getFormattedCustomer()+ "' with size: "
        + formattedCustomer.getFormattedCustomer().size());
    }

    /**
     * Test of getFormattedHeader method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testGetFormattedHeader()
    {
        System.out.println("==getFormattedHeader==");
        FormattedCustomer formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        System.out.println("FormattedCustomer: '" + formattedCustomer.getFormattedHeader()+ "' with size: "
        + formattedCustomer.getFormattedHeader().size());
    }

    /**
     * Test of setValue method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        FormattedCustomer formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        formattedCustomer.setValue(PolarisTable.PatronRegistration.NAME_FIRST.toString(), "Robot");
        String result = formattedCustomer.getValue(PolarisTable.PatronRegistration.NAME_FIRST.toString());
        System.out.println("USER_FIRST_NAME:"+result);
//        System.out.println("FormattedCustomer: '" + formattedCustomer.getFormattedCustomer() + "'");
        assertTrue(result.compareTo("Robot") == 0);
    }

    /**
     * Test of insertValue method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testInsertValue()
    {
        System.out.println("==insertValue==");
        FormattedCustomer formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        formattedCustomer.insertValue(PolarisTable.PATRONS, PolarisTable.PatronRegistration.NAME_FIRST.toString(), "Bilbo");
        String result = formattedCustomer.getValue(PolarisTable.PatronRegistration.NAME_FIRST.toString());
        System.out.println("USER_FIRST_NAME:"+result);
//        System.out.println("FormattedCustomer: '" + formattedCustomer.getFormattedCustomer() + "'");
        assertTrue(result.compareTo("Bilbo Balzac") == 0); // an additional value is added.
        
        formattedCustomer.insertValue(PolarisTable.PATRON_REGISTRATION, "Some Silly Value", "sill_value");
        result = formattedCustomer.getValue("Some Silly Value");
        System.out.println("\"Some Silly Value\":"+result);
//        System.out.println("FormattedCustomer: '" + formattedCustomer.getFormattedCustomer() + "'");
        assertTrue(result.compareTo("sill_value") == 0); // an additional value is added.
    }

    /**
     * Test of containsKey method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testContainsKey()
    {
        System.out.println("==containsKey==");
        FormattedCustomer formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        assertFalse(formattedCustomer.containsKey("Some_Silly_Value"));
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.NAME_FIRST.toString()));
        assertTrue(formattedCustomer.containsKey(PolarisTable.Patrons.BARCODE.toString()));
    }

    /**
     * Test of getValue method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("==getValue==");
        FormattedCustomer formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        assertFalse(formattedCustomer.containsKey("Some_Silly_Value"));
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.NAME_FIRST.toString()));
        assertTrue(formattedCustomer.containsKey(PolarisTable.Patrons.BARCODE.toString()));
        
        String v = formattedCustomer.getValue("Some_Silly_Value");
        System.out.println(">>>"+ v +"<<<");
        assertTrue(v.isEmpty());
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.Patrons.CREATOR_ID.toString()));
        v = formattedCustomer.getValue(PolarisTable.Patrons.CREATOR_ID.toString());
        System.out.println(">>>"+ v +"<<<");
        assertTrue(v.compareTo("1831") == 0);
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.Patrons.ORGANIZATION_ID.toString()));
        v = formattedCustomer.getValue(PolarisTable.Patrons.ORGANIZATION_ID.toString());
        System.out.println(">>>"+ v +"<<<");
        assertTrue(v.compareTo("303") == 0);
    }

    /**
     * Test of insertTable method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testInsertTable()
    {
        // Places an empty table as the second item on the list (index 1, zero indexed list).
        System.out.println("==insertTable==");
        FormattedCustomer formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        FormattedTable table = new PolarisSQLFormattedTable("some_table", new HashMap<String, String>());
        formattedCustomer.insertTable(table, 1);
        List<String> result = formattedCustomer.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("\n1 NEW_TABLE: "+s);
        }
        
        // Places an empty table at the end of the list.
        formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        table = new PolarisSQLFormattedTable(PolarisTable.POSTAL_CODES, new HashMap<String, String>());
        formattedCustomer.insertTable(table, 99);
        result = formattedCustomer.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("\n2 NEW_TABLE: "+s);
        }
        
        // Places an empty table at beginning of the  list.
        formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        table = new PolarisSQLFormattedTable(PolarisTable.POSTAL_CODES, new HashMap<String, String>());
        formattedCustomer.insertTable(table, -1);
        result = formattedCustomer.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("\n3 NEW_TABLE: "+s);
        }
    }

    /**
     * Test of renameField method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testRenameField()
    {
        System.out.println("==renameField==");
        FormattedCustomer formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.NAME_FIRST.toString()));
        formattedCustomer.renameField(
                PolarisTable.PATRON_REGISTRATION.toString(), 
                PolarisTable.PatronRegistration.NAME_FIRST.toString(), 
                "Silly Name");
        assertFalse(formattedCustomer.containsKey(PolarisTable.PatronRegistration.NAME_FIRST.toString()));
        assertTrue(formattedCustomer.containsKey("Silly Name"));
        System.out.println(">>>"+ formattedCustomer.containsKey("Silly Name") +"<<<");
    }

    /**
     * Test of removeField method, of class PolarisSQLFormattedCustomer.
     */
    @Test
    public void testRemoveField()
    {
        System.out.println("==removeField==");
        FormattedCustomer formattedCustomer = new PolarisSQLFormattedCustomer(customer);
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.NAME_FIRST.toString()));
        formattedCustomer.removeField(
                PolarisTable.PATRON_REGISTRATION.toString(), 
                PolarisTable.PatronRegistration.NAME_FIRST.toString());
        assertFalse(formattedCustomer.containsKey(PolarisTable.PatronRegistration.NAME_FIRST.toString()));
    }
}
