package mecard.customer;
import mecard.customer.symphony.FlatFormattedCustomer;
import mecard.customer.symphony.FlatTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import json.RequestDeserializer;
import mecard.Request;
import mecard.config.FlatUserExtendedFieldTypes;
import mecard.config.FlatUserFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrew
 */
public class FlatFormattedCustomerTest
{
    private Customer customer;
    public FlatFormattedCustomerTest()
    {
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        customer = request.getCustomer();
    }

    /**
     * Test of getFormattedCustomer method, of class FlatFormattedCustomer.
     */
    @Test
    public void testGetFormattedCustomer()
    {
        System.out.println("==getFormattedCustomer==");
        FormattedCustomer formatter = new FlatFormattedCustomer(customer);
        List<String> expResult = new ArrayList<>();
        expResult.add("*** DOCUMENT BOUNDARY ***\n");
        expResult.add(".USER_ID.   |a21221012345678\n");
        expResult.add(".USER_FIRST_NAME.   |aBalzac\n");
        expResult.add(".USER_LAST_NAME.   |aBilly\n");
        expResult.add(".USER_PREFERRED_NAME.   |aBilly, Balzac\n");
        expResult.add(".USER_LIBRARY.   |aEPLMNA\n");
        expResult.add(".USER_PROFILE.   |aEPL-METRO\n");
        expResult.add(".USER_PREF_LANG.   |aENGLISH\n");
        expResult.add(".USER_PIN.   |a64058\n");
        expResult.add(".USER_STATUS.   |aOK\n");
        expResult.add(".USER_ROUTING_FLAG.   |aY\n");
        expResult.add(".USER_CHG_HIST_RULE.   |aALLCHARGES\n");
        expResult.add(".USER_PRIV_GRANTED.   |a20130724\n");
        expResult.add(".USER_PRIV_EXPIRES.   |a20140602\n");
        expResult.add(".USER_BIRTH_DATE.   |a19750822\n");
        expResult.add(".USER_CATEGORY2.   |aM\n");
        expResult.add(".USER_ACCESS.   |aPUBLIC\n");
        expResult.add(".USER_ENVIRONMENT.   |aPUBLIC\n");
        expResult.add(".USER_ADDR1_BEGIN.\n");
        expResult.add(".STREET.   |a12345 123 St.\n");
        expResult.add(".CITY/STATE.   |aEdmonton, ALBERTA\n");
        expResult.add(".POSTALCODE.   |aH0H0H0\n");
        expResult.add(".PHONE.   |a7804964058\n");
        expResult.add(".EMAIL.   |ailsteam@epl.ca\n");
        expResult.add(".USER_ADDR1_END.\n");

        List<String> result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("=>"+s);
        }
//        assertEquals(expResult, result);
    }

    /**
     * Test of setValue method, of class FlatFormattedCustomer.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        FormattedCustomer formatter = new FlatFormattedCustomer(customer);
        formatter.setValue(FlatUserFieldTypes.USER_FIRST_NAME.name(), "Robot");
        String result = formatter.getValue(FlatUserFieldTypes.USER_FIRST_NAME.name());
        System.out.println("USER_FIRST_NAME:"+result);
        assertTrue(result.compareTo("Robot") == 0);
    }

    /**
     * Test of containsKey method, of class FlatFormattedCustomer.
     */
    @Test
    public void testContainsKey()
    {
        System.out.println("==containsKey==");
        FormattedCustomer formatter = new FlatFormattedCustomer(customer);
        assertTrue(formatter.containsKey(FlatUserFieldTypes.USER_FIRST_NAME.name()));
        assertFalse(formatter.containsKey("INVALID_FIELD"));
    }

    /**
     * Test of insertTable method, of class FlatFormattedCustomer.
     */
    @Test
    public void testInsertTable()
    {
        System.out.println("==insertTable==");
        FormattedCustomer formatter = new FlatFormattedCustomer(customer);
        FormattedTable table = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER_ADDR1, new HashMap<String, String>());
        formatter.insertTable(table, 1);
        List<String> result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("NEW_TABLE:\n"+s);
        }
        
        formatter = new FlatFormattedCustomer(customer);
        table = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER_ADDR2, new HashMap<String, String>());
        formatter.insertTable(table, 99);
        result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("NEW_TABLE:\n"+s);
        }
    }
    
    /**
     * Test of InsertKeyValuePair method, of class FlatFormattedCustomer.
     */
    @Test
    public void testInsertKeyValuePair()
    {
        System.out.println("==InsertKeyValuePair==");
        FormattedCustomer formatter = new FlatFormattedCustomer(customer);
        assertTrue(formatter.containsKey(FlatUserFieldTypes.USER_FIRST_NAME.name()));
        assertTrue(formatter.insertValue(FlatUserExtendedFieldTypes.USER.name(), "HOCKEY_STAR", "Gretzky"));
        assertFalse(formatter.insertValue("NON_EXISTANT_TABLE", "SOCCER_STAR", "Beckem"));
        List<String> result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("INSERT:\n"+s);
        }
    }

    /**
     * Test of getFormattedHeader method, of class FlatFormattedCustomer.
     */
    @Test
    public void testGetFormattedHeader()
    {
        System.out.println("getFormattedHeader");
        FlatFormattedCustomer instance = null;
        List<String> expResult = null;
        List<String> result = instance.getFormattedHeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValue method, of class FlatFormattedCustomer.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("getValue");
        String key = "";
        FlatFormattedCustomer instance = null;
        String expResult = "";
        String result = instance.getValue(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertValue method, of class FlatFormattedCustomer.
     */
    @Test
    public void testInsertValue()
    {
        System.out.println("insertValue");
        String tableName = "";
        String key = "";
        String value = "";
        FlatFormattedCustomer instance = null;
        boolean expResult = false;
        boolean result = instance.insertValue(tableName, key, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}