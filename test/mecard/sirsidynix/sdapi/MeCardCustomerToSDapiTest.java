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
import mecard.config.SDapiUserFields;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class MeCardCustomerToSDapiTest {
    
    private final Customer updateCustomer;
    private final Customer createCustomer;
    List<String> expResult = null;
    
    public MeCardCustomerToSDapiTest()
    {
        String custCreateReq = "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\","
                + "\"userId\":\"21221012345678\","
                + "\"alternateId\":\"21221012345677\","
                + "\"pin\":\"64058\","
                + "\"customer\":\"{"
                + "\\\"ID\\\":\\\"21221012345678\\\","
                + "\\\"PIN\\\":\\\"64058\\\","
                + "\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\","
                + "\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\","
                + "\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\","
                + "\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\","
                + "\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\","
                + "\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\","
                + "\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\","
                + "\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\","
                + "\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\","
                + "\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\","
                + "\\\"FIRSTNAME\\\":\\\"Billy\\\",\\\"LASTNAME\\\":\\\"Balzac\\\"}"
                + "\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custCreateReq);
        updateCustomer = request.getCustomer();
                
        String custUpdateReq = """
        {"code":"UPDATE_CUSTOMER","authorityToken":"12345678","userId":"21221012345666","pin":"64058","customer":"{\\"ID\\":\\"21221012345666\\",\\"PIN\\":\\"64058\\",\\"PREFEREDNAME\\":\\"Billy, Balzac\\",\\"STREET\\":\\"7 Sir Winston Churchill Square\\",\\"CITY\\":\\"Edmonton\\",\\"PROVINCE\\":\\"Alberta\\",\\"POSTALCODE\\":\\"H0H0H0\\",\\"SEX\\":\\"M\\",\\"EMAIL\\":\\"ilsteam@epl.ca\\",\\"PHONE\\":\\"7804964058\\",\\"DOB\\":\\"20010822\\",\\"PRIVILEGE_EXPIRES\\":\\"20260822\\",\\"RESERVED\\":\\"X\\",\\"ALTERNATE_ID\\":\\"X\\",\\"ISVALID\\":\\"Y\\",\\"ISMINAGE\\":\\"Y\\",\\"ISRECIPROCAL\\":\\"N\\",\\"ISRESIDENT\\":\\"Y\\",\\"ISGOODSTANDING\\":\\"Y\\",\\"ISLOSTCARD\\":\\"N\\",\\"FIRSTNAME\\":\\"Billy\\",\\"LASTNAME\\":\\"Balzac\\"}"}""";
        request = deserializer.getDeserializedRequest(custUpdateReq);
        createCustomer = request.getCustomer();
        
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
        expResult = new ArrayList<>();
        expResult.add("""
        {"resource":"/user/patron","fields":{"barcode":"21221012345666","lastName":"Balzac","firstName":"Billy","privilegeExpiresDate":"2026-08-22","birthDate":"2001-08-22","pin":"64058","keepCircHistory":"ALLCHARGES","library":{"resource":"/policy/library","key":"EPLMNA"},"profile":{"resource":"/policy/userProfile","key":"EPL_METRO"},"access":{"resource":"/policy/userAccess","key":"PUBLIC"},"language":{"resource":"/policy/language","key":"ENGLISH"},"environment":{"resource":"/policy/environment","key":"PUBLIC"},"address1":[{"@resource":"/user/patron/address1","@key":"6","code":{"@resource":"/policy/patronAddress1","@key":"EMAIL"},"data":"ilsteam@epl.ca"},{"@resource":"/user/patron/address1","@key":"4","code":{"@resource":"/policy/patronAddress1","@key":"STREET"},"data":"7 Sir Winston Churchill Square"},{"@resource":"/user/patron/address1","@key":"1","code":{"@resource":"/policy/patronAddress1","@key":"CITY/STATE"},"data":"Edmonton, Alberta"},{"@resource":"/user/patron/address1","@key":"5","code":{"@resource":"/policy/patronAddress1","@key":"POSTALCODE"},"data":"H0H 0H0"},{"@resource":"/user/patron/address1","@key":"2","code":{"@resource":"/policy/patronAddress1","@key":"PHONE"},"data":"780-496-4058"}]}}""");
    }

    /**
     * Test of getFormattedCustomer method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testGetFormattedCustomer() 
    {
        System.out.println("==getFormattedCustomer==");
        MeCardCustomerToSDapi instance = new MeCardCustomerToSDapi(updateCustomer, MeCardDataToSDapiData.QueryType.UPDATE);
        
        List<String> result = instance.getFormattedCustomer();
        System.out.println("    EXPECTED JSON:"+expResult);
        System.out.println("GET_CUSTOMER JSON:"+result);
        assertTrue(result.get(0).equals(expResult.get(0)));
    }

    /**
     * Test of getFormattedHeader method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testGetFormattedHeader() 
    {
        System.out.println("==getFormattedHeader==");
        MeCardCustomerToSDapi instance = new MeCardCustomerToSDapi(createCustomer, MeCardDataToSDapiData.QueryType.CREATE);
        List<String> result = instance.getFormattedHeader();
        for (String s : result)
        {
            System.out.println(">>>"+s+"<<< (should be empty)");
            assertTrue(s.isEmpty());
        }
    }

    /**
     * Test of setValue method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testSetValue() 
    {
        System.out.println("==setValue==");
        MeCardCustomerToSDapi instance = new MeCardCustomerToSDapi(updateCustomer, MeCardDataToSDapiData.QueryType.UPDATE);
        String key = SDapiUserFields.USER_FIRST_NAME.toString();
        String value = "Some Stupid Value";
        assertEquals(true, instance.setValue(key, value));
        System.out.println("New first name '" + instance.getValue(SDapiUserFields.USER_FIRST_NAME.toString())+"'");
        key = SDapiUserFields.CATEGORY05.toString();
        value = "ENOCONSENT";
        assertEquals(true, instance.setValue(key, value));
        System.out.println("Should be ENOCONSENT in Cat05: " + instance.getValue(SDapiUserFields.CATEGORY05.toString()));
        System.out.println(">>" + instance.getFormattedCustomer());
        instance.removeField("", SDapiUserFields.CATEGORY05.toString());
        System.out.println(">>" + instance.getFormattedCustomer());
    }

    /**
     * Test of insertValue method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testInsertValue() 
    {
        System.out.println("==insertValue==");
        MeCardCustomerToSDapi instance = new MeCardCustomerToSDapi(updateCustomer, MeCardDataToSDapiData.QueryType.UPDATE);
        String key = SDapiUserFields.USER_FIRST_NAME.toString();
        String value = "Some Stupid Value";
        assertEquals(true, instance.insertValue(null, key, value));
        System.out.println("New first name '" + instance.getValue(SDapiUserFields.USER_FIRST_NAME.toString())+"'");
        value = "William";
        assertEquals(true, instance.insertValue(null, key, value));
        System.out.println(">>" + instance.getFormattedCustomer());
        value = "21221087654321";
        assertEquals(true, instance.insertValue(null, SDapiUserFields.USER_ALTERNATE_ID.toString(), value));
        System.out.println(">>" + instance.getFormattedCustomer());
    }

    /**
     * Test of containsKey method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testContainsKey() 
    {
        System.out.println("==containsKey==");
        MeCardCustomerToSDapi instance = new MeCardCustomerToSDapi(updateCustomer, MeCardDataToSDapiData.QueryType.UPDATE);
        assertEquals(true, instance.containsKey(SDapiUserFields.USER_FIRST_NAME.toString()));
        assertEquals(true, instance.containsKey(SDapiUserFields.USER_ID.toString()));
        assertEquals(true, instance.containsKey(SDapiUserFields.POSTALCODE.toString()));
        assertEquals(true, instance.containsKey(SDapiUserFields.USER_ACCESS.toString()));
        assertEquals(true, instance.containsKey(SDapiUserFields.USER_BIRTHDATE.toString()));
        assertEquals(true, instance.containsKey(SDapiUserFields.USER_LIBRARY.toString()));
        assertEquals(true, instance.containsKey(SDapiUserFields.PHONE.toString()));
        assertEquals(true, instance.containsKey(SDapiUserFields.USER_PASSWORD.toString()));
        assertEquals(true, instance.containsKey(SDapiUserFields.LANGUAGE.toString()));
        assertEquals(false, instance.containsKey(SDapiUserFields.USER_ALTERNATE_ID.toString()));
        
    }

    /**
     * Test of getValue method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testGetValue() 
    {
        System.out.println("==getValue==");
        MeCardCustomerToSDapi instance = new MeCardCustomerToSDapi(updateCustomer, MeCardDataToSDapiData.QueryType.UPDATE);
        assertEquals("Billy", instance.getValue(SDapiUserFields.USER_FIRST_NAME.toString()));
        assertEquals("Balzac", instance.getValue(SDapiUserFields.USER_LAST_NAME.toString()));
        assertEquals("21221012345678", instance.getValue(SDapiUserFields.USER_ID.toString()));
        assertEquals("H0H 0H0", instance.getValue(SDapiUserFields.POSTALCODE.toString()));
        assertEquals("PUBLIC", instance.getValue(SDapiUserFields.USER_ACCESS.toString()));
        assertEquals("1975-08-22", instance.getValue(SDapiUserFields.USER_BIRTHDATE.toString()));
        assertEquals("EPLMNA", instance.getValue(SDapiUserFields.USER_LIBRARY.toString()));
        assertEquals("780-496-4058", instance.getValue(SDapiUserFields.PHONE.toString()));
        assertEquals("64058", instance.getValue(SDapiUserFields.USER_PASSWORD.toString()));
        assertEquals("ENGLISH", instance.getValue(SDapiUserFields.LANGUAGE.toString()));
        assertEquals("", instance.getValue(SDapiUserFields.USER_ALTERNATE_ID.toString()));
    }


    /**
     * Test of renameField method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testRenameField() 
    {
        System.out.println("==renameField==");
        String tableName = "";
        MeCardCustomerToNativeFormat instance = new MeCardCustomerToSDapi(updateCustomer, MeCardDataToSDapiData.QueryType.UPDATE);
        System.out.println(SDapiUserFields.USER_FIRST_NAME.toString() + " before>>>" + instance.getValue(SDapiUserFields.USER_FIRST_NAME.toString()));
        System.out.println(SDapiUserFields.USER_LAST_NAME.toString() + " before>>>" + instance.getValue(SDapiUserFields.USER_LAST_NAME.toString()));
        System.out.println(" we're going to replace old field '" + SDapiUserFields.USER_FIRST_NAME.toString() + "'"
                + " with new field '" + SDapiUserFields.USER_LAST_NAME.toString() + "'");
        boolean result = instance.renameField(
                tableName, 
                SDapiUserFields.USER_FIRST_NAME.toString(), // original field name
                SDapiUserFields.USER_LAST_NAME.toString()); // new field name
        assertEquals(true, result);
        System.out.println(SDapiUserFields.USER_FIRST_NAME.toString() + " after>>>" + instance.getValue(SDapiUserFields.USER_FIRST_NAME.toString()));
        System.out.println(SDapiUserFields.USER_LAST_NAME.toString() + " after>>>" + instance.getValue(SDapiUserFields.USER_LAST_NAME.toString()));
    }

    /**
     * Test of removeField method, of class MeCardCustomerToSDapi.
     */
    @Test
    public void testRemoveField() 
    {
        System.out.println("==removeField==");
        MeCardCustomerToNativeFormat instance = new MeCardCustomerToSDapi(createCustomer, MeCardDataToSDapiData.QueryType.CREATE);
        assertEquals("PUBLIC", instance.getValue(SDapiUserFields.USER_ACCESS.toString()));
        instance.removeField("", SDapiUserFields.USER_ACCESS.toString());
        assertEquals("", instance.getValue(SDapiUserFields.USER_ACCESS.toString()));
    }
    
}
