package mecard.customer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import json.RequestDeserializer;
import mecard.Request;
import mecard.config.BImportTableTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class BImportFormattedCustomerTest
{
    private Customer customer;
    public BImportFormattedCustomerTest()
    {
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        this.customer = request.getCustomer();
    }

    /**
     * Test of getFormattedCustomer method, of class BImportFormattedCustomer.
     */
    @Test
    public void testGetFormattedCustomer()
    {
        System.out.println("==getFormattedCustomer==");
        BImportFormattedCustomer instance = new BImportFormattedCustomer(customer);
        List<String> result = instance.getFormattedHeader();
        for (String s: result)
        {
            System.out.print("r>"+s);
        }
        result = instance.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("r>"+s);
        }
        System.out.println();
    }

    /**
     * Test of setValue method, of class BImportFormattedCustomer.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        BImportFormattedCustomer instance = new BImportFormattedCustomer(customer);
        String key = "birth_date";
        String value = "19630822";
        boolean result = instance.setValue(key, value);
        assertTrue(result);
        List<String> resultList = instance.getFormattedCustomer();
//        for (String s: resultList)
//        {
//            System.out.print("r>"+s);
//        }
//        System.out.println();
        
        key = "swan_song";
        value = "SHOULD NOT SEE ME";
        result = instance.setValue(key, value);
        assertFalse(result);
        resultList = instance.getFormattedCustomer();
        for (String s: resultList)
        {
            System.out.print("swan_song>"+s);
        }
        System.out.println();
    }
    
    @Test
    public void testInsertTable()
    {
        System.out.println("==insertTable==");
        BImportFormattedCustomer instance = new BImportFormattedCustomer(customer);
        BImportTable table = BImportTable.getInstanceOf(
                BImportTableTypes.BORROWER_BSTAT, new HashMap<String, String>());
        table.setValue("bstat", "unknown");
        instance.insertTable(table, 3);
        List<String> resultList = instance.getFormattedCustomer();
        for (String s: resultList)
        {
            System.out.print("new_table>"+s);
        }
        resultList = instance.getFormattedHeader();
        for (String s: resultList)
        {
            System.out.print("new_table>"+s);
        }
        System.out.println();
        
        instance = new BImportFormattedCustomer(customer);
        table = BImportTable.getInstanceOf(
                BImportTableTypes.BORROWER_BSTAT, new HashMap<String, String>());
        instance.insertTable(table, -1);
        resultList = instance.getFormattedCustomer();
        for (String s: resultList)
        {
            System.out.print("new_table>"+s);
        }
        System.out.println();
        
        instance = new BImportFormattedCustomer(customer);
        table = BImportTable.getInstanceOf(
                BImportTableTypes.BORROWER_BSTAT, new HashMap<String, String>());
        instance.insertTable(table, 100);
        resultList = instance.getFormattedCustomer();
        for (String s: resultList)
        {
            System.out.print("new_table>"+s);
        }
        System.out.println();
    }

    /**
     * Test of containsKey method, of class BImportFormattedCustomer.
     */
    @Test
    public void testContainsKey()
    {
        System.out.println("==containsKey==");
        BImportFormattedCustomer instance = new BImportFormattedCustomer(customer);
        String key = "birth_date";
        boolean result = instance.containsKey(key);
        assertTrue(result);
        key = "swan_song";
        result = instance.containsKey(key);
        assertFalse(result);
    }

    /**
     * Test of getValue method, of class BImportFormattedCustomer.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("==getValue==");
        BImportFormattedCustomer instance = new BImportFormattedCustomer(customer);
        String key = "birth_date";
        String result = instance.getValue(key);
        assertTrue(result.compareTo("19750822") == 0);
        key = "swan_song";
        result = instance.getValue(key);
        assertTrue(result.isEmpty());
    }

    /**
     * Test of computeEmailName method, of class BImportFormattedCustomer.
     */
    @Test
    public void testComputeEmailName()
    {
        System.out.println("==computeEmailName==");
        String email = "anisbet@epl.ca";
        BImportFormattedCustomer instance = new BImportFormattedCustomer(customer);
        String expResult = "anisbet";
        String result = instance.computeEmailName(email);
        assertTrue(expResult.compareTo(result) == 0);
    }
}