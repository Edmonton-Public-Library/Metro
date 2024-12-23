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

import java.util.Set;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class MeCardDataToSDapiDataTest {
    
    public MeCardDataToSDapiDataTest() 
    {
        
    }

    /**
     * Test of getInstanceOf method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetInstanceOf() {
        System.out.println("getInstanceOf");
        MeCardDataToSDapiData.QueryType type = null;
        MeCardDataToSDapiData expResult = null;
        MeCardDataToSDapiData result = MeCardDataToSDapiData.getInstanceOf(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toJson method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testToJson() {
        System.out.println("toJson");
        MeCardDataToSDapiData instance = null;
        String expResult = "";
        String result = instance.toJson();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getData method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetData() 
    {
        System.out.println("getData");
        MeCardDataToSDapiData instance = null;
        String expResult = "";
        String result = instance.getData();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeader method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetHeader() {
        System.out.println("getHeader");
        MeCardDataToSDapiData instance = null;
        String expResult = "";
        String result = instance.getHeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        MeCardDataToSDapiData instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValue method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        String key = "";
        MeCardDataToSDapiData instance = null;
        String expResult = "";
        String result = instance.getValue(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValue method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        String key = "";
        String value = "";
        MeCardDataToSDapiData instance = null;
        boolean expResult = false;
        boolean result = instance.setValue(key, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of renameKey method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testRenameKey() {
        System.out.println("renameKey");
        String originalkey = "";
        String replacementKey = "";
        MeCardDataToSDapiData instance = null;
        boolean expResult = false;
        boolean result = instance.renameKey(originalkey, replacementKey);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getKeys method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetKeys() {
        System.out.println("getKeys");
        MeCardDataToSDapiData instance = null;
        Set<String> expResult = null;
        Set<String> result = instance.getKeys();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteValue method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testDeleteValue() {
        System.out.println("deleteValue");
        String key = "";
        MeCardDataToSDapiData instance = null;
        boolean expResult = false;
        boolean result = instance.deleteValue(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
