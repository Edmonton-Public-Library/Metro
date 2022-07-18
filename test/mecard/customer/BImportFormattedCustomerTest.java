/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 */
package mecard.customer;
import mecard.horizon.MeCardCustomerToBImport;
import mecard.horizon.MeCardDataToBImportData;
import java.util.HashMap;
import java.util.List;
import json.RequestDeserializer;
import mecard.Request;
import mecard.config.BImportTableTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class BImportFormattedCustomerTest
{
    private final Customer customer;
    public BImportFormattedCustomerTest()
    {
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        this.customer = request.getCustomer();
    }

    /**
     * Test of getFormattedCustomer method, of class MeCardCustomerToBImport.
     */
    @Test
    public void testGetFormattedCustomer()
    {
        System.out.println("==getFormattedCustomer==");
        MeCardCustomerToBImport instance = new MeCardCustomerToBImport(customer);
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
     * Test of setValue method, of class MeCardCustomerToBImport.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        MeCardCustomerToBImport instance = new MeCardCustomerToBImport(customer);
        String key = "birth_date";
        String value = "19630822";
        boolean result = instance.setValue(key, value);
        assertTrue(result);
        List<String> resultList = instance.getFormattedCustomer();
        for (String s: resultList)
        {
            System.out.print("r>"+s);
        }
        System.out.println();
        
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
        MeCardCustomerToBImport instance = new MeCardCustomerToBImport(customer);
        MeCardDataToBImportData table = MeCardDataToBImportData.getInstanceOf(
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
        
        instance = new MeCardCustomerToBImport(customer);
        table = MeCardDataToBImportData.getInstanceOf(
                BImportTableTypes.BORROWER_BSTAT, new HashMap<String, String>());
        instance.insertTable(table, -1);
        resultList = instance.getFormattedCustomer();
        for (String s: resultList)
        {
            System.out.print("new_table>"+s);
        }
        System.out.println();
        
        instance = new MeCardCustomerToBImport(customer);
        table = MeCardDataToBImportData.getInstanceOf(
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
     * Test of containsKey method, of class MeCardCustomerToBImport.
     */
    @Test
    public void testContainsKey()
    {
        System.out.println("==containsKey==");
        MeCardCustomerToBImport instance = new MeCardCustomerToBImport(customer);
        String key = "birth_date";
        boolean result = instance.containsKey(key);
        assertTrue(result);
        key = "swan_song";
        result = instance.containsKey(key);
        assertFalse(result);
    }

    /**
     * Test of getValue method, of class MeCardCustomerToBImport.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("==getValue==");
        MeCardCustomerToBImport instance = new MeCardCustomerToBImport(customer);
        String key = "birth_date";
        String result = instance.getValue(key);
        assertTrue(result.compareTo("1975-08-22") == 0);
        key = "swan_song";
        result = instance.getValue(key);
        assertTrue(result.isEmpty());
    }

    /**
     * Test of computeEmailName method, of class MeCardCustomerToBImport.
     */
    @Test
    public void testComputeEmailName()
    {
        System.out.println("==computeEmailName==");
        String email = "anisbet@epl.ca";
        MeCardCustomerToBImport instance = new MeCardCustomerToBImport(customer);
        String expResult = "anisbet";
        String result = instance.computeEmailName(email);
        assertTrue(expResult.compareTo(result) == 0);
    }
    
    /**
     * Test of InsertKeyValuePair method, of class FlatFormattedCustomer.
     */
    @Test
    public void testInsertKeyValuePair()
    {
        System.out.println("==InsertKeyValuePair==");
        MeCardCustomerToNativeFormat formatter = new MeCardCustomerToBImport(customer);
        assertTrue(formatter.insertValue(BImportTableTypes.BORROWER_TABLE.toString(), "cell_phone", "780-999-1212"));
        assertFalse(formatter.insertValue("NON_EXISTANT_TABLE", "SOCCER_STAR", "Beckem"));
        List<String> result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("INSERT:\n"+s);
        }
        result = formatter.getFormattedHeader();
        for (String s: result)
        {
            System.out.print("INSERT:\n"+s);
        }
    }

    /**
     * Test of getFormattedHeader method, of class MeCardCustomerToBImport.
     */
    @Test
    public void testGetFormattedHeader()
    {
        System.out.println("==getFormattedHeader==");
        MeCardCustomerToBImport instance = new MeCardCustomerToBImport(customer);
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
     * Test of insertValue method, of class BImportFormattedCustomer.
     */
//    @Test
//    public void testInsertValue()
//    {
//        System.out.println("insertValue");
//        String tableName = "";
//        String key = "";
//        String value = "";
//        MeCardCustomerToBImport instance = null;
//        boolean expResult = false;
//        boolean result = instance.insertValue(tableName, key, value);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}