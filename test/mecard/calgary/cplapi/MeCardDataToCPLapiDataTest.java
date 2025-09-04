/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025 Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either httpVersion 2 of the License, or
 * (at your option) any later httpVersion.
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
import java.util.List;
import static org.junit.Assert.*;

import org.junit.Test;
import mecard.config.CPLapiUserFields;

/**
 *
 * @author anisbet
 */
public class MeCardDataToCPLapiDataTest 
{
    private MeCardDataToCPLapiData testUser;
    public MeCardDataToCPLapiDataTest() 
    {
        testUser = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.UPDATE);
        testUser.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        testUser.setValue(CPLapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        testUser.setValue(CPLapiUserFields.USER_PASSWORD.toString(), "AbC123");
        testUser.setValue(CPLapiUserFields.STATUS.toString(), "OK");
        testUser.setValue(CPLapiUserFields.USER_ID.toString(), "21221012345678");
        testUser.setValue(CPLapiUserFields.EMAIL.toString(), "ilsadmins@epl.ca");
        testUser.setValue(CPLapiUserFields.PROFILE.toString(), "CPL_ADULT");
        testUser.setValue(CPLapiUserFields.PHONE.toString(), "780-555-1234");
        testUser.setValue(CPLapiUserFields.USER_BIRTHDATE.toString(), "2001-08-22");
        testUser.setValue(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString(), "2026-08-22");
        testUser.setValue(CPLapiUserFields.CITY.toString(), "Calgary");
        testUser.setValue(CPLapiUserFields.PROVINCE.toString(), "AB");
        testUser.setValue(CPLapiUserFields.STREET.toString(), "300 8 Avenue SE");
        testUser.setValue(CPLapiUserFields.POSTALCODE.toString(), "L6H 2T2");
        testUser.setValue(CPLapiUserFields.GENDER.toString(), "M");
    }

    /**
     * Test of toJson method, of class MeCardDataToCPLapiData.
     */
    @Test
    public void testToJson() 
    {
        System.out.println("==toJson=");
        MeCardDataToCPLapiData updateUser = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.UPDATE);
        updateUser.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        updateUser.setValue(CPLapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        updateUser.setValue(CPLapiUserFields.USER_PASSWORD.toString(), "AbC123");
        updateUser.setValue(CPLapiUserFields.STATUS.toString(), "OK");
        updateUser.setValue(CPLapiUserFields.USER_ID.toString(), "21221012345678");
        updateUser.setValue(CPLapiUserFields.EMAIL.toString(), "ilsadmins@epl.ca");
        updateUser.setValue(CPLapiUserFields.PROFILE.toString(), "CPL_ADULT");
        updateUser.setValue(CPLapiUserFields.PHONE.toString(), "780-555-1234");
        updateUser.setValue(CPLapiUserFields.USER_BIRTHDATE.toString(), "2001-08-22");
        updateUser.setValue(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString(), "2026-08-22");
        updateUser.setValue(CPLapiUserFields.CITY.toString(), "Calgary");
        updateUser.setValue(CPLapiUserFields.PROVINCE.toString(), "AB");
        updateUser.setValue(CPLapiUserFields.STREET.toString(), "300 8 Avenue SE");
        updateUser.setValue(CPLapiUserFields.POSTALCODE.toString(), "L6H 2T2");
        updateUser.setValue(CPLapiUserFields.GENDER.toString(), "M");
        
        System.out.println("UPDATE TO JSON():" + updateUser);
        var data = """
            {"cardNumber":"21221012345678","pin":"AbC123","firstName":"Billy","lastName":"Balzac","birthDate":"2001-08-22","gender":"M","emailAddress":"ilsadmins@epl.ca","phoneNumber":"780-555-1234","address":"300 8 Avenue SE","city":"Calgary","province":"AB","postalCode":"L6H 2T2","expiryDate":"2026-08-22","status":"OK","profile":"CPL_ADULT"}""";
        assertEquals(updateUser.toJson(), data);
        
        MeCardDataToCPLapiData createUser = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.CREATE);
        createUser.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        createUser.setValue(CPLapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        createUser.setValue(CPLapiUserFields.USER_ID.toString(), "21221012345678");
        createUser.setValue(CPLapiUserFields.EMAIL.toString(), "ilsadmins@epl.ca");
        createUser.setValue(CPLapiUserFields.PROFILE.toString(), "CPL_ADULT");
        createUser.setValue(CPLapiUserFields.PHONE.toString(), "780-555-1234");
        createUser.setValue(CPLapiUserFields.USER_BIRTHDATE.toString(), "2001-08-22");
        createUser.setValue(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString(), "2026-08-22");
        createUser.setValue(CPLapiUserFields.CITY.toString(), "Calgary");
        createUser.setValue(CPLapiUserFields.PROVINCE.toString(), "AB");
        createUser.setValue(CPLapiUserFields.STREET.toString(), "300 8 Avenue SE");
        createUser.setValue(CPLapiUserFields.POSTALCODE.toString(), "L6H 2T2");
        createUser.setValue(CPLapiUserFields.GENDER.toString(), "M");
        createUser.setValue(CPLapiUserFields.USER_PASSWORD.toString(), "AbC123");
        createUser.setValue(CPLapiUserFields.STATUS.toString(), "OK");
        
        System.out.println("CREATE TO JSON():" + createUser);
        data = """
            {"cardNumber":"21221012345678","pin":"AbC123","firstName":"Billy","lastName":"Balzac","birthDate":"2001-08-22","gender":"M","emailAddress":"ilsadmins@epl.ca","phoneNumber":"780-555-1234","address":"300 8 Avenue SE","city":"Calgary","province":"AB","postalCode":"L6H 2T2","expiryDate":"2026-08-22","status":"OK","profile":"CPL_ADULT"}""";
        assertEquals(createUser.toJson(), data);
    }

    /**
     * Test of getData method, of class MeCardDataToCPLapiData.
     */
    @Test
    public void testGetData() 
    {
        System.out.println("==getData==");
        MeCardDataToCPLapiData userTable = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.CREATE);
        assertNotNull(userTable);
        assertTrue(userTable.setValue(CPLapiUserFields.USER_ID.toString(), "21221012345678"));
        String testJSON = """
                          {"cardNumber":"21221012345678"}""";
        System.out.println("GET DATA(): " + userTable);
        assertTrue(userTable.getData().compareTo(testJSON) == 0);
    }

    /**
     * Test of getHeader method, of class MeCardDataToCPLapiData.
     */
    @Test
    public void testGetHeader() 
    {
        System.out.println("==getHeader==");
        // There is no such thing as a header for this type of data object.
        MeCardDataToCPLapiData userTable = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.UPDATE);
        userTable.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(CPLapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        var value = userTable.getHeader();
        assertEquals(value, "");
    }

    /**
     * Test of getName method, of class MeCardDataToCPLapiData.
     */
    @Test
    public void testGetName() 
    {
        System.out.println("==getName==");
        MeCardDataToCPLapiData userTable = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.UPDATE);
        userTable.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(CPLapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        var name = userTable.getName();
        System.out.println("GET NAME(): " + name);
        assertEquals(name, "class mecard.calgary.cplapi.MeCardDataToCPLapiData");
    }

    /**
     * Test of getValue method, of class MeCardDataToCPLapiData.
     */
    @Test
    public void testGetValue() 
    {
         System.out.println("==getValue==");
        MeCardDataToCPLapiData userTable = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.UPDATE);
        userTable.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(CPLapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        System.out.println("GET VALUE() " + userTable);
        var value = userTable.getValue(CPLapiUserFields.USER_FIRST_NAME.toString());
        assertEquals(value, "Billy");
        value = userTable.getValue(CPLapiUserFields.USER_ID.toString());
        assertEquals(value, "");
    }

    /**
     * Test of setValue method, of class MeCardDataToCPLapiData.
     */
    @Test
    public void testSetValue() 
    {
        System.out.println("==setValue==");
        String firstName = "Fish-cakes";
        MeCardDataToCPLapiData userTable = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.CREATE);
        userTable.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), firstName);
        System.out.println("SET VALUE()  after:" + userTable);
        assertEquals(userTable.getValue(CPLapiUserFields.USER_FIRST_NAME.toString()), firstName);
        // What happens if you try and set a name that isn't supported in the underlying object.
        assertFalse(userTable.setValue("fishName", firstName));
    }

    /**
     * Test of renameKey method, of class MeCardDataToCPLapiData.
     */
    @Test
    public void testRenameKey() 
    {
        System.out.println("==renameKey==");
        String originalkey = CPLapiUserFields.USER_FIRST_NAME.toString();
        String replacementKey = CPLapiUserFields.USER_LAST_NAME.toString();
        MeCardDataToCPLapiData userTable = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.CREATE);
        userTable.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), "Balzac");
        System.out.println("RENAME KEY() before, first name set to " + userTable);
        boolean expResult = true;
        boolean result = userTable.renameKey(originalkey, replacementKey);
        System.out.println("RENAME KEY() after, last name set to " + userTable);
        assertEquals(expResult, result);
    }

    /**
     * Test of getKeys method, of class MeCardDataToCPLapiData.
     */
    @Test
    public void testGetKeys() 
    {
        System.out.println("==getKeys==");
        MeCardDataToCPLapiData userTable = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.CREATE);
        userTable.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(CPLapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        System.out.println("GET KEYS() " + userTable);
        System.out.println("GET KEYS() " + userTable.getKeys());
        List<String> columns = new ArrayList<>();
        columns.add("firstName");
        columns.add("lastName");
        System.out.println("  " + columns.toString());
        System.out.println("  " + userTable.getKeys().toString());
        var sTable = userTable.getKeys();
        var sTest  = columns.toString();
        assertEquals(sTable.toString(), sTest);
        
        assertTrue(testUser.getKeys().size() == 15);
    }

    /**
     * Test of deleteValue method, of class MeCardDataToCPLapiData.
     */
    @Test
    public void testDeleteValue() 
    {
        System.out.println("==deleteValue==");
        MeCardDataToCPLapiData userTable = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.CREATE);
        userTable.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        userTable.setValue(CPLapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        System.out.println("DEL VALUE() before:" + userTable);
        userTable.deleteValue(CPLapiUserFields.USER_FIRST_NAME.toString());
        System.out.println("DEL VALUE()  after:" + userTable);
        assertEquals(userTable.getValue(CPLapiUserFields.USER_LAST_NAME.toString()), "Balzac");
        // What happens if you delete a non-sensical key?
        userTable.deleteValue(CPLapiUserFields.USER_ID.toString());
        System.out.println("DEL VALUE()  ID:" + userTable);
        userTable.deleteValue("appleWoodSmokedSausage");
        System.out.println("DEL VALUE()  SS:" + userTable);
    }

    /**
     * Test of toString method, of class MeCardDataToCPLapiData.
     */
    @Test
    public void testToString() 
    {
        System.out.println("==toString=");
        MeCardDataToCPLapiData updateUser = MeCardDataToCPLapiData.getInstanceOf(MeCardDataToCPLapiData.QueryType.UPDATE);
        updateUser.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), "Billy");
        updateUser.setValue(CPLapiUserFields.USER_LAST_NAME.toString(), "Balzac");
        updateUser.setValue(CPLapiUserFields.USER_PASSWORD.toString(), "AbC123");
        updateUser.setValue(CPLapiUserFields.STATUS.toString(), "OK");
        updateUser.setValue(CPLapiUserFields.USER_ID.toString(), "21221012345678");
        updateUser.setValue(CPLapiUserFields.EMAIL.toString(), "ilsadmins@epl.ca");
        updateUser.setValue(CPLapiUserFields.PROFILE.toString(), "CPL_ADULT");
        updateUser.setValue(CPLapiUserFields.PHONE.toString(), "780-555-1234");
        updateUser.setValue(CPLapiUserFields.USER_BIRTHDATE.toString(), "2001-08-22");
        updateUser.setValue(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString(), "2026-08-22");
        updateUser.setValue(CPLapiUserFields.CITY.toString(), "Calgary");
        updateUser.setValue(CPLapiUserFields.PROVINCE.toString(), "AB");
        updateUser.setValue(CPLapiUserFields.STREET.toString(), "300 8 Avenue SE");
        updateUser.setValue(CPLapiUserFields.POSTALCODE.toString(), "L6H 2T2");
        updateUser.setValue(CPLapiUserFields.GENDER.toString(), "M");
        
        System.out.println("UPDATE TO JSON():" + updateUser);
        var data = """
            {"cardNumber":"21221012345678","pin":"AbC123","firstName":"Billy","lastName":"Balzac","birthDate":"2001-08-22","gender":"M","emailAddress":"ilsadmins@epl.ca","phoneNumber":"780-555-1234","address":"300 8 Avenue SE","city":"Calgary","province":"AB","postalCode":"L6H 2T2","expiryDate":"2026-08-22","status":"OK","profile":"CPL_ADULT"}""";
        assertEquals(updateUser.toJson(), data);
    }
        
}
