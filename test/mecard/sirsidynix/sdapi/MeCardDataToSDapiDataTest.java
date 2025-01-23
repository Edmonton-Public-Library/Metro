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
        MeCardDataToSDapiData updateUser = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.UPDATE);
        updateUser.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        updateUser.setValue(SDapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        updateUser.setValue(SDapiUserFields.USER_KEY.toString(), "654321");
        updateUser.setValue(SDapiUserFields.USER_ID.toString(), "21221012345678");
        updateUser.setValue(SDapiUserFields.USER_LIBRARY.toString(), "EPL_MNA");
        updateUser.setValue(SDapiUserFields.USER_ALTERNATE_ID.toString(), "");
        updateUser.setValue(SDapiUserFields.EMAIL.toString(), "ilsadmins@epl.ca");
        updateUser.setValue(SDapiUserFields.PROFILE.toString(), "EPL_ADULT");
        updateUser.setValue(SDapiUserFields.PHONE.toString(), "780-555-1234");
        updateUser.setValue(SDapiUserFields.USER_BIRTHDATE.toString(), "2001-08-22");
        updateUser.setValue(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString(), "2026-08-22");
        updateUser.setValue(SDapiUserFields.CITY_SLASH_PROV.toString(), "Edmonton, AB");
        updateUser.setValue(SDapiUserFields.CITY_SLASH_STATE.toString(), "Calgary, AB");
        updateUser.setValue(SDapiUserFields.STREET.toString(), "7 Sir Winston Churchill Square");
        updateUser.setValue(SDapiUserFields.POSTALCODE.toString(), "L6H 2T2");
        updateUser.setValue(SDapiUserFields.CATEGORY05.toString(), "ECONSENT");
        
        System.out.println("TO JSON():" + updateUser);
        var data = """
            {"resource":"/user/patron","key":"654321","fields":{"barcode":"21221012345678","lastName":"Balzac","firstName":"Billy","privilegeExpiresDate":"2026-08-22","birthDate":"2001-08-22","alternateID":"","library":{"resource":"/policy/library","key":"EPL_MNA"},"profile":{"resource":"/policy/userProfile","key":"EPL_ADULT"},"category05":{"resource":"/policy/patronCategory05","key":"ECONSENT"},"address1":[{"@resource":"/user/patron/address1","@key":"6","code":{"@resource":"/policy/patronAddress1","@key":"EMAIL"},"data":"ilsadmins@epl.ca"},{"@resource":"/user/patron/address1","@key":"2","code":{"@resource":"/policy/patronAddress1","@key":"PHONE"},"data":"780-555-1234"},{"@resource":"/user/patron/address1","@key":"1","code":{"@resource":"/policy/patronAddress1","@key":"CITY/PROV"},"data":"Edmonton, AB"},{"@resource":"/user/patron/address1","@key":"1","code":{"@resource":"/policy/patronAddress1","@key":"CITY/STATE"},"data":"Calgary, AB"},{"@resource":"/user/patron/address1","@key":"4","code":{"@resource":"/policy/patronAddress1","@key":"STREET"},"data":"7 Sir Winston Churchill Square"},{"@resource":"/user/patron/address1","@key":"5","code":{"@resource":"/policy/patronAddress1","@key":"POSTALCODE"},"data":"L6H 2T2"}]}}""";
        assertEquals(updateUser.toString(), data);
        
        MeCardDataToSDapiData createUser = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.CREATE);
        createUser.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        createUser.setValue(SDapiUserFields.USER_LAST_NAME.toString(), "Balzac");

        createUser.setValue(SDapiUserFields.USER_ID.toString(), "21221012345678");
        createUser.setValue(SDapiUserFields.USER_LIBRARY.toString(), "EPL_MNA");
        createUser.setValue(SDapiUserFields.USER_ALTERNATE_ID.toString(), "");
        createUser.setValue(SDapiUserFields.EMAIL.toString(), "ilsadmins@epl.ca");
        createUser.setValue(SDapiUserFields.PROFILE.toString(), "EPL_ADULT");
        createUser.setValue(SDapiUserFields.PHONE.toString(), "780-555-1234");
        createUser.setValue(SDapiUserFields.USER_BIRTHDATE.toString(), "2001-08-22");
        createUser.setValue(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString(), "2026-08-22");
        createUser.setValue(SDapiUserFields.CITY_SLASH_PROV.toString(), "Edmonton, AB");
        createUser.setValue(SDapiUserFields.CITY_SLASH_STATE.toString(), "Calgary, AB");
        createUser.setValue(SDapiUserFields.STREET.toString(), "7 Sir Winston Churchill Square");
        createUser.setValue(SDapiUserFields.POSTALCODE.toString(), "L6H 2T2");
        createUser.setValue(SDapiUserFields.CATEGORY05.toString(), "ECONSENT");
        
        System.out.println("TO JSON():" + createUser);
        data = """
            {"resource":"/user/patron","fields":{"barcode":"21221012345678","lastName":"Balzac","firstName":"Billy","privilegeExpiresDate":"2026-08-22","birthDate":"2001-08-22","alternateID":"","library":{"resource":"/policy/library","key":"EPL_MNA"},"profile":{"resource":"/policy/userProfile","key":"EPL_ADULT"},"category05":{"resource":"/policy/patronCategory05","key":"ECONSENT"},"address1":[{"@resource":"/user/patron/address1","@key":"6","code":{"@resource":"/policy/patronAddress1","@key":"EMAIL"},"data":"ilsadmins@epl.ca"},{"@resource":"/user/patron/address1","@key":"2","code":{"@resource":"/policy/patronAddress1","@key":"PHONE"},"data":"780-555-1234"},{"@resource":"/user/patron/address1","@key":"1","code":{"@resource":"/policy/patronAddress1","@key":"CITY/PROV"},"data":"Edmonton, AB"},{"@resource":"/user/patron/address1","@key":"1","code":{"@resource":"/policy/patronAddress1","@key":"CITY/STATE"},"data":"Calgary, AB"},{"@resource":"/user/patron/address1","@key":"4","code":{"@resource":"/policy/patronAddress1","@key":"STREET"},"data":"7 Sir Winston Churchill Square"},{"@resource":"/user/patron/address1","@key":"5","code":{"@resource":"/policy/patronAddress1","@key":"POSTALCODE"},"data":"L6H 2T2"}]}}""";
        assertEquals(createUser.toString(), data);
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

    /**
     * Test of getKeys method, of class MeCardDataToSDapiData.
     */
    @Test
    public void testGetKeys() 
    {
        System.out.println("==getKeys==");
        MeCardDataToSDapiData userTable = MeCardDataToSDapiData.getInstanceOf(MeCardDataToSDapiData.QueryType.CREATE);
        userTable.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(SDapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        System.out.println("GET KEYS() " + userTable);
        System.out.println("GET KEYS() " + userTable.getKeys());
        List<String> columns = new ArrayList<>();
        columns.add("firstName");
        columns.add("lastName");
//        System.out.println("  " + columns.toString());
//        System.out.println("  " + updateUser.getKeys().toString());
        var sTable = userTable.getKeys();
        var sTest  = columns.toString();
        assertEquals(sTable.toString(), sTest);
    }

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
