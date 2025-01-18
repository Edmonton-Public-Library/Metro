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


import java.util.ArrayList;
import static org.junit.Assert.*;

import java.util.List;
import json.RequestDeserializer;
import mecard.Request;
import mecard.customer.Customer;
import mecard.customer.MeCardDataToNativeData;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class MeCardCustomerToSDapiTest {
    
    private final Customer updateCustomer;
    private final Customer createCustomer;
    public MeCardCustomerToSDapiTest()
    {
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"7 Sir Winston Churchill Square\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"20010822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20260822\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Billy\\\",\\\"LASTNAME\\\":\\\"Balzac\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        updateCustomer = request.getCustomer();
        custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346000\",\"pin\":\"Pl3as3!LeTM3regist3r\",\"customer\":\"{\\\"ID\\\":\\\"21221012346000\\\",\\\"PIN\\\":\\\"Pl3as3!LeTM3regist3r\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"AB\\\",\\\"POSTALCODE\\\":\\\"90210\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsadmins@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20230822\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Williams\\\"}\"}";
        request = deserializer.getDeserializedRequest(custReq);
        createCustomer = request.getCustomer();
    }

    /**
     * Test of getFormattedCustomer method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testGetFormattedCustomer() 
    {
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
    public void testGetFormattedHeader() 
    {
        System.out.println("getFormattedHeader");
        MeCardCustomerToSDapi instance = new MeCardCustomerToSDapi(createCustomer, MeCardDataToSDapiData.QueryType.CREATE);
        List<String> expResult = new ArrayList<>();
        //        {
        //  "resource": "/user/patron",
        //  "key": "654321",
        //  "fields": {
        //    "barcode": "21221012345678",
        //    "lastName": "Balzac",
        //    "firstName": "Billy",
        //    "privilegeExpiresDate": "2026-08-22",
        //    "birthDate": "2001-08-22",
        //    "alternateID": "",
        //    "library": {
        //      "resource": "/policy/library",
        //      "key": "EPL_MNA"
        //    },
        //    "profile": {
        //      "resource": "/policy/userProfile",
        //      "key": "EPL_ADULT"
        //    },
        //    "address1": [
        //      {
        //        "@resource": "/user/patron/address1",
        //        "@key": "6",
        //        "code": {
        //          "@resource": "/policy/patronAddress1",
        //          "@key": "EMAIL"
        //        },
        //        "data": "ilsadmins@epl.ca"
        //      },
        //      {
        //        "@resource": "/user/patron/address1",
        //        "@key": "2",
        //        "code": {
        //          "@resource": "/policy/patronAddress1",
        //          "@key": "PHONE"
        //        },
        //        "data": "780-555-1234"
        //      },
        //      {
        //        "@resource": "/user/patron/address1",
        //        "@key": "1",
        //        "code": {
        //          "@resource": "/policy/patronAddress1",
        //          "@key": "CITY/STATE"
        //        },
        //        "data": "Edmonton, AB"
        //      },
        //      {
        //        "@resource": "/user/patron/address1",
        //        "@key": "1",
        //        "code": {
        //          "@resource": "/policy/patronAddress1",
        //          "@key": "CITY/STATE"
        //        },
        //        "data": "Calgary, AB"
        //      },
        //      {
        //        "@resource": "/user/patron/address1",
        //        "@key": "4",
        //        "code": {
        //          "@resource": "/policy/patronAddress1",
        //          "@key": "STREET"
        //        },
        //        "data": "7 Sir Winston Churchill Square"
        //      },
        //      {
        //        "@resource": "/user/patron/address1",
        //        "@key": "5",
        //        "code": {
        //          "@resource": "/policy/patronAddress1",
        //          "@key": "POSTALCODE"
        //        },
        //        "data": "L6H 2T2"
        //      }
        //    ]
        //  }
        //}

        expResult.add("""
                      {"resource":"/user/patron","key":"654321","fields":{"barcode":"21221012345678","lastName":"Balzac","firstName":"Billy","privilegeExpiresDate":"2026-08-22","birthDate":"2001-08-22","alternateID":"","library":{"resource":"/policy/library","key":"EPL_MNA"},"profile":{"resource":"/policy/userProfile","key":"EPL_ADULT"},"address1":[{"@resource":"/user/patron/address1","@key":"6","code":{"@resource":"/policy/patronAddress1","@key":"EMAIL"},"data":"ilsadmins@epl.ca"},{"@resource":"/user/patron/address1","@key":"2","code":{"@resource":"/policy/patronAddress1","@key":"PHONE"},"data":"780-496-4058"},{"@resource":"/user/patron/address1","@key":"1","code":{"@resource":"/policy/patronAddress1","@key":"CITY/STATE"},"data":"Edmonton, AB"},{"@resource":"/user/patron/address1","@key":"4","code":{"@resource":"/policy/patronAddress1","@key":"STREET"},"data":"7 Sir Winston Churchill Square"},{"@resource":"/user/patron/address1","@key":"5","code":{"@resource":"/policy/patronAddress1","@key":"POSTALCODE"},"data":"H0H 0H0"}]}}"""
        );
        List<String> result = instance.getFormattedHeader();
        for (String s : result)
            System.out.println(">>>" + s);
    }

    /**
     * Test of setValue method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testSetValue() 
    {
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
