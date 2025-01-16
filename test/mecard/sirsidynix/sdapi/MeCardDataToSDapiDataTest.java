/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024 - 2025  Edmonton Public Library
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
import java.util.List;
import static org.junit.Assert.*;

import mecard.config.SDapiUserFields;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class MeCardDataToSDapiDataTest 
{

    /**
     * Test of toJson method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testToJson() 
    {
        System.out.println("==toJson==");
        MeCardDataToSDapiData userTable = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.UPDATE);
        userTable.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(SDapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        userTable.setValue(SDapiUserFields.USER_KEY.toString(), "654321");
        userTable.setValue(SDapiUserFields.USER_ID.toString(), "21221012345678");
        userTable.setValue(SDapiUserFields.USER_LIBRARY.toString(), "EPL_MNA");
        userTable.setValue(SDapiUserFields.USER_ALTERNATE_ID.toString(), "");
        userTable.setValue(SDapiUserFields.EMAIL.toString(), "ilsadmins@epl.ca");
        userTable.setValue(SDapiUserFields.PROFILE.toString(), "EPL_ADULT");
        userTable.setValue(SDapiUserFields.PHONE.toString(), "780-555-1234");
        userTable.setValue(SDapiUserFields.USER_BIRTHDATE.toString(), "2001-08-22");
        userTable.setValue(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString(), "2026-08-22");
        userTable.setValue(SDapiUserFields.CITY_SLASH_PROV.toString(), "Edmonton, AB");
        userTable.setValue(SDapiUserFields.CITY_SLASH_STATE.toString(), "Calgary, AB");
        userTable.setValue(SDapiUserFields.STREET.toString(), "7 Sir Winston Churchill Square");
        userTable.setValue(SDapiUserFields.POSTALCODE.toString(), "L6H 2T2");
        
        System.out.println("TO JSON():" + userTable);
        var data = """
            {"resource":"/user/patron","key":"654321","fields":{"barcode":"21221012345678","lastName":"Balzac","firstName":"Billy","privilegeExpiresDate":"2026-08-22","birthDate":"2001-08-22","alternateID":"","library":{"resource":"/policy/library","key":"EPL_MNA"},"profile":{"resource":"/policy/userProfile","key":"EPL_ADULT"},"address1":[{"@resource":"/user/patron/address1","@key":"6","code":{"@resource":"/policy/patronAddress1","@key":"EMAIL"},"data":"ilsadmins@epl.ca"},{"@resource":"/user/patron/address1","@key":"2","code":{"@resource":"/policy/patronAddress1","@key":"PHONE"},"data":"780-555-1234"},{"@resource":"/user/patron/address1","@key":"1","code":{"@resource":"/policy/patronAddress1","@key":"CITY/STATE"},"data":"Edmonton, AB"},{"@resource":"/user/patron/address1","@key":"1","code":{"@resource":"/policy/patronAddress1","@key":"CITY/STATE"},"data":"Calgary, AB"},{"@resource":"/user/patron/address1","@key":"4","code":{"@resource":"/policy/patronAddress1","@key":"STREET"},"data":"7 Sir Winston Churchill Square"},{"@resource":"/user/patron/address1","@key":"5","code":{"@resource":"/policy/patronAddress1","@key":"POSTALCODE"},"data":"L6H 2T2"}]}}""";
        assertEquals(userTable.toString(), data);
    }

    /**
     * Test of getData method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetData() 
    {
        System.out.println("==getData==");
        MeCardDataToSDapiData userTable = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.CREATE);
        assertNotNull(userTable);
        assertTrue(userTable.setValue(SDapiUserFields.USER_ID.toString(), "21221012345678"));
        String testJSON = "{\"resource\":\"/user/patron\",\"fields\":{\"barcode\":\"21221012345678\"}}";
        System.out.println("GET DATA(): " + userTable);
        assertTrue(userTable.getData().compareTo(testJSON) == 0);
    }

    /**
     * Test of getHeader method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetHeader() 
    {
        System.out.println("==getHeader==");
        // There is no such thing as a header for this type of data object.
        MeCardDataToSDapiData userTable = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.UPDATE);
        userTable.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(SDapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        var value = userTable.getHeader();
        assertEquals(value, "");
    }

    /**
     * Test of getName method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetName() 
    {
        System.out.println("==getName==");
        MeCardDataToSDapiData userTable = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.UPDATE);
        userTable.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(SDapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        var name = userTable.getName();
        System.out.println("GET NAME(): " + name);
        assertEquals(name, "class mecard.sirsidynix.sdapi.MeCardDataToSDapiData");
    }

    /**
     * Test of getValue method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetValue() 
    {
        System.out.println("==getValue==");
        MeCardDataToSDapiData userTable = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.UPDATE);
        userTable.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(SDapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        System.out.println("GET VALUE() " + userTable);
        var value = userTable.getValue(SDapiUserFields.USER_FIRST_NAME.toString());
        assertEquals(value, "Billy");
        value = userTable.getValue(SDapiUserFields.USER_ID.toString());
        assertEquals(value, "");
    }

    /**
     * Test of setValue method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testSetValue() 
    {
        System.out.println("==setValue==");
        String firstName = "Fish-cakes";
        MeCardDataToSDapiData userTable = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.CREATE);
        userTable.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), firstName);
        System.out.println("SET VALUE()  after:" + userTable);
        assertEquals(userTable.getValue(SDapiUserFields.USER_FIRST_NAME.toString()), firstName);
        // What happens if you try and set a name that isn't supported in the underlying object.
        assertFalse(userTable.setValue("fishName", firstName));
    }

    /**
     * Test of renameKey method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testRenameKey() 
    {
        System.out.println("==renameKey==");
        String originalkey = SDapiUserFields.USER_FIRST_NAME.toString();
        String replacementKey = SDapiUserFields.USER_LAST_NAME.toString();
        MeCardDataToSDapiData userTable = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.CREATE);
        userTable.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), "Balzac");
        System.out.println("RENAME KEY() before, first name set to " + userTable);
        boolean expResult = true;
        boolean result = userTable.renameKey(originalkey, replacementKey);
        System.out.println("RENAME KEY() after, last name set to " + userTable);
        assertEquals(expResult, result);
    }

//    /**
//     * Test of getKeys method, of class MeCardDataToSDapiData.
//     */
//    @Test
//    public void testGetKeys() 
//    {
//        System.out.println("==getKeys==");
//        MeCardDataToSDapiData userTable = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.CREATE);
//        userTable.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), "Billy");
//        userTable.setValue(SDapiUserFields.USER_LAST_NAME.toString(), "Balzac");
//        System.out.println("GET KEYS() " + userTable);
//        System.out.println("GET KEYS() " + userTable.getKeys());
//        List<String> columns = new ArrayList<>();
//        columns.add("firstName");
//        columns.add("lastName");
////        System.out.println("  " + columns.toString());
////        System.out.println("  " + userTable.getKeys().toString());
//        var sTable = userTable.getKeys();
//        var sTest  = columns.toString();
//        assertEquals(sTable.toString(), sTest);
//    }

    /**
     * Test of deleteValue method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testDeleteValue() 
    {
        System.out.println("==deleteValue==");
        MeCardDataToSDapiData userTable = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.CREATE);
        userTable.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(SDapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        System.out.println("DEL VALUE() before:" + userTable);
        userTable.deleteValue(SDapiUserFields.USER_FIRST_NAME.toString());
        System.out.println("DEL VALUE()  after:" + userTable);
        assertEquals(userTable.getValue(SDapiUserFields.USER_LAST_NAME.toString()), "Balzac");
        // What happens if you delete a non-sensical key?
        userTable.deleteValue(SDapiUserFields.USER_ID.toString());
        System.out.println("DEL VALUE()  ID:" + userTable);
        userTable.deleteValue("appleWoodSmokedSausage");
        System.out.println("DEL VALUE()  SS:" + userTable);
    }
    
}
