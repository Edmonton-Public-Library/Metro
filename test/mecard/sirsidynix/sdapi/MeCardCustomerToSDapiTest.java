/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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
package mecard.sirsidynix.sdapi;


import static org.junit.Assert.*;

import java.util.List;
import mecard.customer.MeCardDataToNativeData;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class MeCardCustomerToSDapiTest {
    
    public MeCardCustomerToSDapiTest() {
    }

    /**
     * Test of getFormattedCustomer method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testGetFormattedCustomer() {
        System.out.println("getFormattedCustomer");
        MeCardCustomerToSDapi instance = null;
        List<String> expResult = null;
        List<String> result = instance.getFormattedCustomer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFormattedHeader method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testGetFormattedHeader() {
        System.out.println("getFormattedHeader");
        MeCardCustomerToSDapi instance = null;
        List<String> expResult = null;
        List<String> result = instance.getFormattedHeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValue method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        String key = "";
        String value = "";
        MeCardCustomerToSDapi instance = null;
        boolean expResult = false;
        boolean result = instance.setValue(key, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertValue method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testInsertValue() {
        System.out.println("insertValue");
        String tableName = "";
        String key = "";
        String value = "";
        MeCardCustomerToSDapi instance = null;
        boolean expResult = false;
        boolean result = instance.insertValue(tableName, key, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of containsKey method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testContainsKey() {
        System.out.println("containsKey");
        String key = "";
        MeCardCustomerToSDapi instance = null;
        boolean expResult = false;
        boolean result = instance.containsKey(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValue method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        String key = "";
        MeCardCustomerToSDapi instance = null;
        String expResult = "";
        String result = instance.getValue(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertTable method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testInsertTable() {
        System.out.println("insertTable");
        MeCardDataToNativeData formattedTable = null;
        int index = 0;
        MeCardCustomerToSDapi instance = null;
        instance.insertTable(formattedTable, index);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of renameField method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testRenameField() {
        System.out.println("renameField");
        String tableName = "";
        String originalFieldName = "";
        String newFieldName = "";
        MeCardCustomerToSDapi instance = null;
        boolean expResult = false;
        boolean result = instance.renameField(tableName, originalFieldName, newFieldName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeField method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testRemoveField() {
        System.out.println("removeField");
        String tableName = "";
        String fieldName = "";
        MeCardCustomerToSDapi instance = null;
        boolean expResult = false;
        boolean result = instance.removeField(tableName, fieldName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
