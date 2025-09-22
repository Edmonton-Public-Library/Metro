/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025 Edmonton Public Library
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

package mecard.calgary.cplapi;


import java.util.ArrayList;
import static org.junit.Assert.*;

import java.util.List;
import json.RequestDeserializer;
import mecard.Request;
import mecard.config.CPLapiUserFields;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class MeCardCustomerToCPLapiTest 
{
    private final Customer updateCustomer;
    private final Customer createCustomer;
    List<String> expResult = null;
    
    public MeCardCustomerToCPLapiTest() 
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
                + "\\\"PRIVILEGE_EXPIRES\\\":\\\"20260822\\\","
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
        
        expResult = new ArrayList<>();
        expResult.add("""
            {"cardNumber":"21221012345678","pin":"64058","firstName":"Billy","lastName":"Balzac","birthDate":"1975-08-22","gender":"Male","emailAddress":"ilsteam@epl.ca","phoneNumber":"780-496-4058","address":"12345 123 St.","city":"Edmonton","province":"Alberta","postalCode":"H0H 0H0","expiryDate":"2026-08-22"}""");
    }

    /**
     * Test of getFormattedCustomer method, of class MeCardCustomerToCPLapi.
     */
    @Test
    public void testGetFormattedCustomer() 
    {
        System.out.println("==getFormattedCustomer==");
        MeCardCustomerToCPLapi instance = new MeCardCustomerToCPLapi(updateCustomer, MeCardDataToCPLapiData.QueryType.UPDATE);
        
        List<String> result = instance.getFormattedCustomer();
        System.out.println("    EXPECTED JSON:"+expResult);
        System.out.println("GET_CUSTOMER JSON:"+result);
        assertTrue(result.get(0).equals(expResult.get(0)));
    }

    /**
     * Test of getFormattedHeader method, of class MeCardCustomerToCPLapi.
     */
    @Test
    public void testGetFormattedHeader() 
    {
        System.out.println("==getFormattedHeader==");
        MeCardCustomerToCPLapi instance = new MeCardCustomerToCPLapi(createCustomer, MeCardDataToCPLapiData.QueryType.CREATE);
        List<String> result = instance.getFormattedHeader();
        for (String s : result)
        {
            System.out.println(">>>"+s+"<<< (should be empty)");
            assertTrue(s.isEmpty());
        }
    }

    /**
     * Test of setValue method, of class MeCardCustomerToCPLapi.
     */
    @Test
    public void testSetValue() 
    {
        System.out.println("==setValue==");
        MeCardCustomerToCPLapi instance = new MeCardCustomerToCPLapi(updateCustomer, MeCardDataToCPLapiData.QueryType.UPDATE);
        String key = CPLapiUserFields.USER_FIRST_NAME.toString();
        String value = "Some Stupid Value";
        assertEquals(true, instance.setValue(key, value));
        System.out.println("New first name '" + instance.getValue(CPLapiUserFields.USER_FIRST_NAME.toString())+"'");
        key = CPLapiUserFields.USER_PASSWORD.toString();
        value = "abc123";
        assertEquals(true, instance.setValue(key, value));
        System.out.println("Should be Calgary in City: " + instance.getValue(CPLapiUserFields.USER_PASSWORD.toString()));
        System.out.println(">>" + instance.getFormattedCustomer());
        instance.removeField("", CPLapiUserFields.USER_PASSWORD.toString());
        System.out.println(">>" + instance.getFormattedCustomer());
//        var removedCity = """
//            {"cardNumber":"21221012345678","firstName":"Some Stupid Value","lastName":"Balzac","birthDate":"1975-08-22","gender":"Male","emailAddress":"ilsteam@epl.ca","phoneNumber":"780-496-4058","address":"12345 123 St.","city":"Calgary","province":"Alberta","postalCode":"H0H 0H0","expiryDate":"2026-08-22"}""";
//        assertEquals(instance.getFormattedCustomer(), removedCity);
    }

    /**
     * Test of insertValue method, of class MeCardCustomerToCPLapi.
     */
    @Test
    public void testInsertValue() 
    {
        System.out.println("==insertValue==");
        MeCardCustomerToCPLapi instance = new MeCardCustomerToCPLapi(updateCustomer, MeCardDataToCPLapiData.QueryType.UPDATE);
        String key = CPLapiUserFields.USER_FIRST_NAME.toString();
        String value = "Some Stupid Value";
        assertEquals(true, instance.insertValue(null, key, value));
        System.out.println("New first name '" + instance.getValue(CPLapiUserFields.USER_FIRST_NAME.toString())+"'");
        value = "William";
        assertEquals(true, instance.insertValue("You can use any table name, it won't matter", key, value));
        System.out.println(">>" + instance.getFormattedCustomer());
        value = "21221087654321";
        assertEquals(true, instance.insertValue("", CPLapiUserFields.USER_ID.toString(), value));
        System.out.println(">>" + instance.getFormattedCustomer());
    }

    /**
     * Test of containsKey method, of class MeCardCustomerToCPLapi.
     */
    @Test
    public void testContainsKey() 
    {
        System.out.println("==containsKey==");
        MeCardCustomerToCPLapi instance = new MeCardCustomerToCPLapi(updateCustomer, MeCardDataToCPLapiData.QueryType.UPDATE);
        assertEquals(true, instance.containsKey(CPLapiUserFields.USER_FIRST_NAME.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.USER_ID.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.POSTALCODE.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.STREET.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.USER_BIRTHDATE.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.PHONE.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.USER_PASSWORD.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.GENDER.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.EMAIL.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.CITY.toString()));
        assertEquals(true, instance.containsKey(CPLapiUserFields.PROVINCE.toString()));
        assertEquals(false, instance.containsKey(CPLapiUserFields.STATUS.toString()));
    }

    /**
     * Test of getValue method, of class MeCardCustomerToCPLapi.
     */
    @Test
    public void testGetValue() 
    {
        System.out.println("==getValue==");
        MeCardCustomerToCPLapi instance = new MeCardCustomerToCPLapi(updateCustomer, MeCardDataToCPLapiData.QueryType.UPDATE);
        assertEquals("Billy", instance.getValue(CPLapiUserFields.USER_FIRST_NAME.toString()));
        assertEquals("Balzac", instance.getValue(CPLapiUserFields.USER_LAST_NAME.toString()));
        assertEquals("21221012345678", instance.getValue(CPLapiUserFields.USER_ID.toString()));
        assertEquals("H0H 0H0", instance.getValue(CPLapiUserFields.POSTALCODE.toString()));
//        assertEquals("PUBLIC", instance.getValue(CPLapiUserFields.USER_ACCESS.toString()));
        assertEquals("1975-08-22", instance.getValue(CPLapiUserFields.USER_BIRTHDATE.toString()));
        assertEquals("Edmonton", instance.getValue(CPLapiUserFields.CITY.toString()));
        assertEquals("780-496-4058", instance.getValue(CPLapiUserFields.PHONE.toString()));
        assertEquals("64058", instance.getValue(CPLapiUserFields.USER_PASSWORD.toString()));
        assertEquals("Alberta", instance.getValue(CPLapiUserFields.PROVINCE.toString()));
        assertEquals("", instance.getValue(CPLapiUserFields.STATUS.toString()));
    }

    /**
     * Test of renameField method, of class MeCardCustomerToCPLapi.
     */
    @Test
    public void testRenameField() 
    {
        System.out.println("==renameField==");
        String tableName = "";
        MeCardCustomerToNativeFormat instance = new MeCardCustomerToCPLapi(updateCustomer, MeCardDataToCPLapiData.QueryType.UPDATE);
        System.out.println(CPLapiUserFields.USER_FIRST_NAME.toString() + " before>>>" + instance.getValue(CPLapiUserFields.USER_FIRST_NAME.toString()));
        System.out.println(CPLapiUserFields.USER_LAST_NAME.toString() + " before>>>" + instance.getValue(CPLapiUserFields.USER_LAST_NAME.toString()));
        System.out.println(" we're going to replace old field '" + CPLapiUserFields.USER_FIRST_NAME.toString() + "'"
                + " with new field '" + CPLapiUserFields.USER_LAST_NAME.toString() + "'");
        boolean result = instance.renameField(
                tableName, 
                CPLapiUserFields.USER_FIRST_NAME.toString(), // original field name
                CPLapiUserFields.USER_LAST_NAME.toString()); // new field name
        assertEquals(true, result);
        System.out.println(CPLapiUserFields.USER_FIRST_NAME.toString() + " after>>>" + instance.getValue(CPLapiUserFields.USER_FIRST_NAME.toString()));
        System.out.println(CPLapiUserFields.USER_LAST_NAME.toString() + " after>>>" + instance.getValue(CPLapiUserFields.USER_LAST_NAME.toString()));
    }

    /**
     * Test of removeField method, of class MeCardCustomerToCPLapi.
     */
    @Test
    public void testRemoveField() 
    {
        System.out.println("==removeField==");
        MeCardCustomerToNativeFormat instance = new MeCardCustomerToCPLapi(createCustomer, MeCardDataToCPLapiData.QueryType.CREATE);
        instance.setValue(CPLapiUserFields.STATUS.toString(), "BARRED");
        assertEquals("BARRED", instance.getValue(CPLapiUserFields.STATUS.toString()));
        instance.removeField(null, CPLapiUserFields.STATUS.toString());
        assertEquals("", instance.getValue(CPLapiUserFields.STATUS.toString()));
    }
    
}
