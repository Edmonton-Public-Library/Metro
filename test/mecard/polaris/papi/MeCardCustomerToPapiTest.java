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
package mecard.polaris.papi;

import mecard.polaris.papi.MeCardDataToPapiData;
import mecard.polaris.papi.PapiElementOrder;
import mecard.polaris.papi.MeCardCustomerToPapi;
import java.util.ArrayList;
import java.util.List;
import json.RequestDeserializer;
import mecard.Request;
import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.polaris.papi.MeCardDataToPapiData.QueryType;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com> 
 */


public class MeCardCustomerToPapiTest
{
    private final Customer customer;
    private final Customer createCustomer;
    public MeCardCustomerToPapiTest()
    {
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        customer = request.getCustomer();
        custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346000\",\"pin\":\"Pl3as3!LeTM3regist3r\",\"customer\":\"{\\\"ID\\\":\\\"21221012346000\\\",\\\"PIN\\\":\\\"Pl3as3!LeTM3regist3r\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"AB\\\",\\\"POSTALCODE\\\":\\\"90210\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsadmins@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20230822\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Williams\\\"}\"}";
        request = deserializer.getDeserializedRequest(custReq);
        createCustomer = request.getCustomer();
    }

    /**
     * Test of getFormattedCustomer method, of class MeCardCustomerToPapi.
     */
    @Test
    public void testGetFormattedCustomer()
    {
        System.out.println("getFormattedCustomer");
        MeCardCustomerToNativeFormat formatter = new MeCardCustomerToPapi(createCustomer, QueryType.CREATE);
        List<String> expResult = new ArrayList<>();
        expResult.add("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><PatronRegistrationCreateData>" +
            "<LogonBranchID>1</LogonBranchID>" +
            "<LogonUserID>1</LogonUserID>" +
            "<LogonWorkstationID>1</LogonWorkstationID>" +
            "<PatronBranchID>3</PatronBranchID>" +
            "<PostalCode>902 10</PostalCode>" +      // The web service I'm testing with uses US postal codes and are converted as Canandian.
            "<City>Edmonton</City>" +
            "<State>AB</State>" +
            "<StreetOne>12345 123 St.</StreetOne>" +
            "<NameFirst>Balzac</NameFirst>" +
            "<NameLast>Williams</NameLast>" +
            "<Gender>M</Gender>" +
            "<Birthdate>1975-08-22T00:00:00</Birthdate>" +
            "<PhoneVoice1>780-496-4058</PhoneVoice1>" +
            "<EmailAddress>ilsadmins@epl.ca</EmailAddress>" +
            "<Password>Pl3as3!LeTM3regist3r</Password>" +
            "<Password2>Pl3as3!LeTM3regist3r</Password2>" +
            "<Barcode>21221012346000</Barcode>" +
            "<ExpirationDate>2023-08-22T00:00:00</ExpirationDate>" +
            "<AddrCheckDate>2023-08-10T00:00:00</AddrCheckDate>" +
            "<RequestPickupBranchID>3</RequestPickupBranchID>" +
            "</PatronRegistrationCreateData>");

        List<String> result = formatter.getFormattedCustomer();
        System.out.println("*** NOTE: This test can fail because the checkAddressDate is computed and different from day to day. ***");
        for (String s: result)
            System.out.println("R:" + s);
        for (String s: expResult)
            System.out.println("E:" + s);
        assertTrue(result.get(0).equals(expResult.get(0)));
    }

    /**
     * Test of getFormattedHeader method, of class MeCardCustomerToPapi.
     */
    @Test
    public void testGetFormattedHeader()
    {
        System.out.println("==getFormattedHeader==");
        MeCardCustomerToNativeFormat formatter = new MeCardCustomerToPapi(customer, QueryType.CREATE);
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
     * Test of setValue method, of class MeCardCustomerToPapi.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToPapi(customer, QueryType.CREATE);
        String storedValue = "12345678";
        formattedCustomer.setValue(PapiElementOrder.BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PapiElementOrder.BARCODE.name()).compareTo("12345678") == 0);
    }

    /**
     * Test of insertValue method, of class MeCardCustomerToPapi.
     */
    @Test
    public void testInsertValue()
    {
        System.out.println("==insertValue==");
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToPapi(customer, QueryType.CREATE);
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.insertValue(MeCardDataToPapiData.TABLE_NAME.toString(), PapiElementOrder.BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PapiElementOrder.BARCODE.name()).compareTo("12345678") == 0);
    }

    /**
     * Test of containsKey method, of class MeCardCustomerToPapi.
     */
    @Test
    public void testContainsKey()
    {
        System.out.println("==containsKey==");
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToPapi(customer, QueryType.UPDATE);
        assertTrue(formattedCustomer.containsKey(PapiElementOrder.LOGON_USER_ID.name()));
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.insertValue(MeCardDataToPapiData.TABLE_NAME.toString(), PapiElementOrder.BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.containsKey(PapiElementOrder.BARCODE.name()));
    }

    /**
     * Test of getValue method, of class MeCardCustomerToPapi.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("==getValue==");
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToPapi(customer, QueryType.UPDATE);
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.setValue(PapiElementOrder.BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PapiElementOrder.BARCODE.name()).compareTo("12345678") == 0);
        assertTrue(formattedCustomer.getValue(PapiElementOrder.UPDATE_DELIVERY_OPTION.name()).isEmpty());
    }

    /**
     * Test of insertTable method, of class MeCardCustomerToPapi.
     */
    @Test
    public void testInsertTable()
    {
        System.out.println("==insertTable==");
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToPapi(customer, QueryType.UPDATE);
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.insertValue(MeCardDataToPapiData.TABLE_NAME.toString(), PapiElementOrder.BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PapiElementOrder.BARCODE.name()).compareTo("12345678") == 0);
        MeCardDataToPapiData newTable = MeCardDataToPapiData.getInstanceOf(QueryType.UPDATE);
        newTable.setValue(PapiElementOrder.BARCODE.name(), "111111");
        formattedCustomer.insertTable(newTable, 0);
        assertTrue(formattedCustomer.getValue(PapiElementOrder.BARCODE.name()).compareTo("111111") == 0);
        System.out.println(">>>"+formattedCustomer.toString());
    }

    /**
     * Test of renameField method, of class MeCardCustomerToPapi.
     */
    @Test
    public void testRenameField()
    {
        System.out.println("==renameField==");
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToPapi(customer, QueryType.UPDATE);
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.setValue(PapiElementOrder.BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PapiElementOrder.BARCODE.name()).compareTo("12345678") == 0);
        formattedCustomer.renameField(MeCardDataToPapiData.TABLE_NAME.toString(), 
                PapiElementOrder.BARCODE.name(), 
                PapiElementOrder.BIRTHDATE.name());
        assertTrue(formattedCustomer.getValue(PapiElementOrder.BIRTHDATE.name()).compareTo("12345678") == 0);
    }

    /**
     * Test of removeField method, of class MeCardCustomerToPapi.
     */
    @Test
    public void testRemoveField()
    {
        System.out.println("==removeField==");
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToPapi(customer, QueryType.UPDATE);
        String storedValue = "12345678";
        // Anything for a name will do but formally:
        formattedCustomer.setValue(PapiElementOrder.BARCODE.name(), storedValue);
        assertTrue(formattedCustomer.getValue(PapiElementOrder.BARCODE.name()).compareTo("12345678") == 0);
        formattedCustomer.removeField(MeCardDataToPapiData.TABLE_NAME.toString(), PapiElementOrder.BARCODE.name());
        assertFalse(formattedCustomer.containsKey(PapiElementOrder.BARCODE.name()));
    }

    /**
     * Test of toString method, of class MeCardCustomerToPapi.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        MeCardCustomerToNativeFormat xmlCustomer = new MeCardCustomerToPapi(customer, QueryType.CREATE);
        System.out.println("CREATE==>>"+xmlCustomer.toString());
        xmlCustomer = new MeCardCustomerToPapi(customer, QueryType.UPDATE);
        System.out.println("UPDATE==>>"+xmlCustomer.toString());
    }
    
}
