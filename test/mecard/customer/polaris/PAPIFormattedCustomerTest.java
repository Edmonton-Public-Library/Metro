
package mecard.customer.polaris;

import java.util.ArrayList;
import java.util.List;
import json.RequestDeserializer;
import mecard.Request;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class PAPIFormattedCustomerTest
{
    private Customer customer;
    public PAPIFormattedCustomerTest()
    {
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        customer = request.getCustomer();
    }

    /**
     * Test of getFormattedCustomer method, of class PAPIFormattedCustomer.
     */
    @Test
    public void testGetFormattedCustomer()
    {
        System.out.println("getFormattedCustomer");
        FormattedCustomer formatter = new PAPIFormattedCustomer(customer);
        List<String> expResult = new ArrayList<>();
        expResult.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?><PatronRegistrationCreateData><PostalCode>H0H0H0</PostalCode><City>Edmonton</City><State>Alberta</State><StreetOne>12345 123 St.</StreetOne><NameFirst>Balzac</NameFirst><NameLast>Billy</NameLast><Gender>M</Gender><Birthdate>19750822</Birthdate><PhoneVoice1>780-496-4058</PhoneVoice1><EmailAddress>ilsteam@epl.ca</EmailAddress><Password>64058</Password><Barcode>21221012345678</Barcode></PatronRegistrationCreateData>");

        List<String> result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("=>"+s);
        }
    }

    /**
     * Test of getFormattedHeader method, of class PAPIFormattedCustomer.
     */
    @Test
    public void testGetFormattedHeader()
    {
        System.out.println("==getFormattedHeader==");
        FormattedCustomer formatter = new PAPIFormattedCustomer(customer);
        List<String> expResult = new ArrayList<>();
        expResult.add("21221012345678");

        List<String> result = formatter.getFormattedHeader();
        for (String s: result)
        {
            System.out.println("=>'"+s+"'");
            assertTrue(s.compareTo("21221012345678") == 0);
        }
    }

    /**
     * Test of setValue method, of class PAPIFormattedCustomer.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        FormattedCustomer formattedCustomer = new PAPIFormattedCustomer(customer);
        String storedValue = "12345678";
        formattedCustomer.setValue(PAPIElementOrder.C_BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PAPIElementOrder.C_BARCODE.name()).compareTo("12345678") == 0);
    }

    /**
     * Test of insertValue method, of class PAPIFormattedCustomer.
     */
    @Test
    public void testInsertValue()
    {
        System.out.println("==insertValue==");
        FormattedCustomer formattedCustomer = new PAPIFormattedCustomer(customer);
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.insertValue(PAPIFormattedTable.TABLE_NAME, PAPIElementOrder.C_BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PAPIElementOrder.C_BARCODE.name()).compareTo("12345678") == 0);
    }

    /**
     * Test of containsKey method, of class PAPIFormattedCustomer.
     */
    @Test
    public void testContainsKey()
    {
        System.out.println("==containsKey==");
        FormattedCustomer formattedCustomer = new PAPIFormattedCustomer(customer);
        assertFalse(formattedCustomer.containsKey(PAPIElementOrder.U_LOGON_USER_ID.name()));
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.insertValue(PAPIFormattedTable.TABLE_NAME, PAPIElementOrder.C_BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.containsKey(PAPIElementOrder.C_BARCODE.name()));
    }

    /**
     * Test of getValue method, of class PAPIFormattedCustomer.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("==getValue==");
        FormattedCustomer formattedCustomer = new PAPIFormattedCustomer(customer);
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.setValue(PAPIElementOrder.C_BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PAPIElementOrder.C_BARCODE.name()).compareTo("12345678") == 0);
        assertTrue(formattedCustomer.getValue(PAPIElementOrder.U_DELIVERY_OPTION.name()).isEmpty());
    }

    /**
     * Test of insertTable method, of class PAPIFormattedCustomer.
     */
    @Test
    public void testInsertTable()
    {
        System.out.println("==insertTable==");
        FormattedCustomer formattedCustomer = new PAPIFormattedCustomer(customer);
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.insertValue(PAPIFormattedTable.TABLE_NAME, PAPIElementOrder.C_BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PAPIElementOrder.C_BARCODE.name()).compareTo("12345678") == 0);
        PAPIFormattedTable newTable = PAPIFormattedTable.getInstanceOf();
        newTable.setValue(PAPIElementOrder.C_BARCODE.name(), "111111");
        formattedCustomer.insertTable(newTable, 0);
        assertTrue(formattedCustomer.getValue(PAPIElementOrder.C_BARCODE.name()).compareTo("111111") == 0);
        System.out.println(">>>"+formattedCustomer.toString());
    }

    /**
     * Test of renameField method, of class PAPIFormattedCustomer.
     */
    @Test
    public void testRenameField()
    {
        System.out.println("==renameField==");
        FormattedCustomer formattedCustomer = new PAPIFormattedCustomer(customer);
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.setValue(PAPIElementOrder.C_BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PAPIElementOrder.C_BARCODE.name()).compareTo("12345678") == 0);
        formattedCustomer.renameField(
                PAPIFormattedTable.TABLE_NAME, 
                PAPIElementOrder.C_BARCODE.name(), 
                PAPIElementOrder.C_BIRTHDATE.name());
        assertTrue(formattedCustomer.getValue(PAPIElementOrder.C_BIRTHDATE.name()).compareTo("12345678") == 0);
    }

    /**
     * Test of removeField method, of class PAPIFormattedCustomer.
     */
    @Test
    public void testRemoveField()
    {
        System.out.println("==removeField==");
        FormattedCustomer formattedCustomer = new PAPIFormattedCustomer(customer);
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.setValue(PAPIElementOrder.C_BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PAPIElementOrder.C_BARCODE.name()).compareTo("12345678") == 0);
        formattedCustomer.removeField(PAPIFormattedTable.TABLE_NAME, PAPIElementOrder.C_BARCODE.name());
        assertFalse(formattedCustomer.containsKey(PAPIElementOrder.C_BARCODE.name()));
    }
    
}
