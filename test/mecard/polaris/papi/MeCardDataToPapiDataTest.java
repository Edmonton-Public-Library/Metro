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
import mecard.polaris.papi.MeCardDataToPapiData.QueryType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */


public class MeCardDataToPapiDataTest
{
    
    public MeCardDataToPapiDataTest()
    {
    }

    /**
     * Test of getData method, of class MeCardDataToPapiData.
     */
    @Test
    public void testGetData()
    {
        System.out.println("==getData==");
        MeCardDataToPapiData papiTable = MeCardDataToPapiData.getInstanceOf(MeCardDataToPapiData.QueryType.CREATE);
        assertNotNull(papiTable);
        assertTrue(papiTable.setValue(PapiElementOrder.LOGON_BRANCH_ID.name(), "1"));
        String testXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><PatronRegistrationCreateData><LogonBranchID>1</LogonBranchID></PatronRegistrationCreateData>";
        papiTable.setValue("LOGON_BRANCH_ID", "1");
        System.out.println("PAPI Table data: " + papiTable.toString());
        assertTrue(papiTable.getData().compareTo(testXML) == 0);
    }

    /**
     * Test of getHeader method, of class MeCardDataToPapiData.
     */
//    @Test
//    public void testGetHeader()
//    {
//        System.out.println("==getHeader==");
//        MeCardDataToPapiData papiTable = MeCardDataToPapiData.getInstanceOf(MeCardDataToPapiData.QueryType.UPDATE);
//        assertNotNull(papiTable.getHeader());
//        papiTable.setValue(PapiElementOrder.BARCODE.name(), "21221012345678");
//        assertNotNull(papiTable.getHeader());
//        assertTrue(papiTable.getHeader().compareTo("21221012345678") == 0);
//        System.out.println("GET_VALUE: '" + papiTable.getHeader() + "'");
//    }

    /**
     * Test of getName method, of class MeCardDataToPapiData.
     */
    @Test
    public void testGetName()
    {
        System.out.println("==getName==");
        MeCardDataToPapiData papiTable = MeCardDataToPapiData.getInstanceOf(MeCardDataToPapiData.QueryType.UPDATE);
        System.out.println("GET_NAME: '" + papiTable.getName() + "'");
        assertTrue(papiTable.getName().compareTo("PatronUpdateData") == 0);
    }

    /**
     * Test of getValue method, of class MeCardDataToPapiData.
     */
//    @Test
//    public void testGetValue()
//    {
//        System.out.println("==getValue==");
//        MeCardDataToPapiData papiTable = MeCardDataToPapiData.getInstanceOf(MeCardDataToPapiData.QueryType.UPDATE);
//        papiTable.setValue(PapiElementOrder.CITY.name(), "Edmonton");
//        System.out.println("GET_VALUE: '" + 
//                papiTable.getValue(PapiElementOrder.CITY.name()) + "'");
//        assertTrue(papiTable.getValue(PapiElementOrder.CITY.name()).compareTo("Edmonton") == 0);
//        assertNotNull(papiTable.getValue(PapiElementOrder.POSTAL_CODE.name()));
//        assertTrue(papiTable.getValue(PapiElementOrder.POSTAL_CODE.name()).compareTo("") == 0);
//    }
    
    /**
     * Test of getKeys method, of class MeCardDataToPapiData.
     */
    @Test
    public void testGetKeys()
    {
        System.out.println("==getKeys==");
        MeCardDataToPapiData papiTable = MeCardDataToPapiData.getInstanceOf(MeCardDataToPapiData.QueryType.CREATE);
        // RIGHT:PapiElementOrder.BARCODE.name()
        //      <NameFirst>string</NameFirst>
        //	<NameLast>string</NameLast>
        //	<User1>string</User1>
        //	<User2>string</User2>
        //	<User3>string</User3>
        //	<User4>string</User4>
        //	<User5>string</User5>
        //	<Gender>string</Gender>
        papiTable.setValue(PapiElementOrder.USER5.name(), "9");
        papiTable.setValue(PapiElementOrder.USER4.name(), "8");
        papiTable.setValue(PapiElementOrder.USER3.name(), "7");
        papiTable.setValue(PapiElementOrder.USER2.name(), "6");
        papiTable.setValue(PapiElementOrder.USER1.name(), "5");
        papiTable.setValue(PapiElementOrder.NAME_LAST.name(), "4");
        papiTable.setValue(PapiElementOrder.NAME_FIRST.name(), "3");
        papiTable.setValue(PapiElementOrder.STREET_TWO.name(), "2");
        papiTable.setValue(PapiElementOrder.STREET_ONE.name(), "1");
        papiTable.setValue(PapiElementOrder.POSTAL_CODE.name(), "");
        
        System.out.println("get KEYS:");
        int i = 1;
        for (String key: papiTable.getKeys())
        {
            System.out.println("KEY:" + key);
        }
    }

    /**
     * Test of renameKey method, of class MeCardDataToPapiData.
     */
//    @Test
//    public void testRenameKey()
//    {
//        System.out.println("==renameKey==");
//        MeCardDataToPapiData papiTable = MeCardDataToPapiData.getInstanceOf(MeCardDataToPapiData.QueryType.UPDATE);
//        assertTrue(papiTable.setValue(PapiElementOrder.LOGON_BRANCH_ID.name(), "12345"));
//        assertTrue(papiTable.renameKey(PapiElementOrder.LOGON_BRANCH_ID.name(), "C_PASSWORD_2"));
//    }

    /**
     * Test of deleteValue method, of class MeCardDataToPapiData.
     */
//    @Test
//    public void testDeleteValue()
//    {
//        System.out.println("==deleteValue==");
//        MeCardDataToPapiData papiTable = MeCardDataToPapiData.getInstanceOf(MeCardDataToPapiData.QueryType.UPDATE);
//        assertFalse(papiTable.deleteValue(PapiElementOrder.LOGON_BRANCH_ID.name()));
//        assertTrue(papiTable.setValue(PapiElementOrder.LOGON_BRANCH_ID.name(), "12345"));
//        System.out.println("Before GET_VALUE: '" + papiTable.getValue(PapiElementOrder.LOGON_BRANCH_ID.name()) + "'");
//        assertTrue(papiTable.deleteValue(PapiElementOrder.LOGON_BRANCH_ID.name()));
//        System.out.println("After GET_VALUE: '" + papiTable.getValue(PapiElementOrder.LOGON_BRANCH_ID.name()) + "'");
//    }

    /**
     * Test of getCreateXml method, of class MeCardDataToPapiData.
     */
    @Test
    public void testGetCreateXml()
    {
        System.out.println("getCreateXml");
        MeCardDataToPapiData instance = MeCardDataToPapiData.getInstanceOf(QueryType.CREATE);
        String expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><PatronRegistrationCreateData/>";
        String result = instance.getCreateXml();
        assertEquals(expResult, result);
        
        instance = MeCardDataToPapiData.getInstanceOf(QueryType.UPDATE);
        expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><PatronUpdateData/>";
        result = instance.getCreateXml();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUpdateXml method, of class MeCardDataToPapiData.
     */
    @Test
    public void testGetUpdateXml()
    {
        System.out.println("getUpdateXml");
        MeCardDataToPapiData papiTable = MeCardDataToPapiData.getInstanceOf(QueryType.UPDATE);
        papiTable.setValue(PapiElementOrder.NAME_LAST.name(), "4");
        papiTable.setValue(PapiElementOrder.NAME_FIRST.name(), "3");
        papiTable.setValue(PapiElementOrder.STREET_TWO.name(), "2");
        papiTable.setValue(PapiElementOrder.STREET_ONE.name(), "1");
        String expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><PatronUpdateData><PatronAddresses><Address><StreetOne>1</StreetOne><StreetTwo>2</StreetTwo></Address></PatronAddresses></PatronUpdateData>";
        String result = papiTable.getUpdateXml();
        System.out.println("UPDATE result:" + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class MeCardDataToPapiData.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        MeCardDataToPapiData instance = MeCardDataToPapiData.getInstanceOf(MeCardDataToPapiData.QueryType.CREATE);
        String expResult = instance.getCreateXml();
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
