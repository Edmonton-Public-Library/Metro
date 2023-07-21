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

import mecard.symphony.MeCardCustomerToFlat;
import mecard.symphony.MeCardDataToFlatData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import json.RequestDeserializer;
import mecard.Request;
import mecard.config.FlatUserTableTypes;
import mecard.config.FlatUserFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com> 
 */
public class FlatFormattedCustomerTest
{
    private final Customer customer;
    public FlatFormattedCustomerTest()
    {
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        customer = request.getCustomer();
    }

    /**
     * Test of getFormattedCustomer method, of class MeCardCustomerToFlat.
     */
    @Test
    public void testGetFormattedCustomer()
    {
        System.out.println("==getFormattedCustomer==");
        MeCardCustomerToNativeFormat formatter = new MeCardCustomerToFlat(customer);
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
     * Test of setValue method, of class MeCardCustomerToFlat.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        MeCardCustomerToNativeFormat formatter = new MeCardCustomerToFlat(customer);
        formatter.setValue(FlatUserFieldTypes.USER_FIRST_NAME.name(), "Robot");
        String result = formatter.getValue(FlatUserFieldTypes.USER_FIRST_NAME.name());
        System.out.println("USER_FIRST_NAME:"+result);
        assertTrue(result.compareTo("Robot") == 0);
    }

    /**
     * Test of containsKey method, of class MeCardCustomerToFlat.
     */
    @Test
    public void testContainsKey()
    {
        System.out.println("==containsKey==");
        MeCardCustomerToNativeFormat formatter = new MeCardCustomerToFlat(customer);
        assertTrue(formatter.containsKey(FlatUserFieldTypes.USER_FIRST_NAME.name()));
        assertFalse(formatter.containsKey("INVALID_FIELD"));
    }

    /**
     * Test of insertTable method, of class MeCardCustomerToFlat.
     */
    @Test
    public void testInsertTable()
    {
        System.out.println("==insertTable==");
        MeCardCustomerToNativeFormat formatter = new MeCardCustomerToFlat(customer);
        MeCardDataToNativeData table = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER_ADDR1, new HashMap<String, String>());
        formatter.insertTable(table, 1);
        List<String> result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("NEW_TABLE:\n"+s);
        }
        
        formatter = new MeCardCustomerToFlat(customer);
        table = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER_ADDR2, new HashMap<String, String>());
        formatter.insertTable(table, 99);
        result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("NEW_TABLE:\n"+s);
        }
    }
    
    /**
     * Test of InsertKeyValuePair method, of class MeCardCustomerToFlat.
     */
    @Test
    public void testInsertKeyValuePair()
    {
        System.out.println("==InsertKeyValuePair==");
        MeCardCustomerToNativeFormat formatter = new MeCardCustomerToFlat(customer);
        assertTrue(formatter.containsKey(FlatUserFieldTypes.USER_FIRST_NAME.name()));
        assertTrue(formatter.insertValue(FlatUserTableTypes.USER.name(), "HOCKEY_STAR", "Gretzky"));
        assertFalse(formatter.insertValue("NON_EXISTANT_TABLE", "SOCCER_STAR", "Beckem"));
        List<String> result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("INSERT:\n"+s);
        }
    }

    /**
     * Test of getFormattedHeader method, of class MeCardCustomerToFlat.
     */
    @Test
    public void testGetFormattedHeader()
    {
        System.out.println("getFormattedHeader");
        MeCardCustomerToFlat instance = null;
        List<String> expResult = null;
        List<String> result = instance.getFormattedHeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValue method, of class MeCardCustomerToFlat.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("getValue");
        String key = "";
        MeCardCustomerToFlat instance = null;
        String expResult = "";
        String result = instance.getValue(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertValue method, of class MeCardCustomerToFlat.
     */
    @Test
    public void testInsertValue()
    {
        System.out.println("insertValue");
        String tableName = "";
        String key = "";
        String value = "";
        MeCardCustomerToFlat instance = null;
        boolean expResult = false;
        boolean result = instance.insertValue(tableName, key, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}