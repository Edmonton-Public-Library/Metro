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

import mecard.polaris.sql.MeCardDataToPolarisSQLData;
import mecard.config.PolarisTable;
import org.junit.Test;
import static org.junit.Assert.*;
import mecard.customer.MeCardDataToNativeData;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com> 
 */
public class MeCardDataToPolarisSQLDataTest
{
    
    public MeCardDataToPolarisSQLDataTest()
    {
    }

    /**
     * Test of getData method, of class MeCardDataToPolarisSQLData.
     */
    @Test
    public void testGetData()
    {
        System.out.println("==getData==");
        MeCardDataToNativeData pTable = new MeCardDataToPolarisSQLData(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + pTable.getData() + "'");
    }

    /**
     * Test of getHeader method, of class MeCardDataToPolarisSQLData.
     */
    @Test
    public void testGetHeader()
    {
        System.out.println("==getHeader==");
        MeCardDataToNativeData pTable = new MeCardDataToPolarisSQLData(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + pTable.getHeader() + "'");
    }

    /**
     * Test of getName method, of class MeCardDataToPolarisSQLData.
     */
    @Test
    public void testGetName()
    {
        System.out.println("==getName==");
        MeCardDataToNativeData pTable = new MeCardDataToPolarisSQLData(PolarisTable.PATRONS);
        System.out.println("GET_NAME: '" + pTable.getName() + "'");
        assertTrue(pTable.getName().compareTo("Patrons") == 0);
        
        pTable = new MeCardDataToPolarisSQLData(PolarisTable.ADDRESSES);
        System.out.println("GET_NAME: '" + pTable.getName() + "'");
        assertTrue(pTable.getName().compareTo("Addresses") == 0);
        
        pTable = new MeCardDataToPolarisSQLData(PolarisTable.PATRON_ADDRESSES);
        System.out.println("GET_NAME: '" + pTable.getName() + "'");
        assertTrue(pTable.getName().compareTo("PatronAddresses") == 0);
        
        pTable = new MeCardDataToPolarisSQLData(PolarisTable.PATRON_REGISTRATION);
        System.out.println("GET_NAME: '" + pTable.getName() + "'");
        assertTrue(pTable.getName().compareTo("PatronRegistration") == 0);
        
        pTable = new MeCardDataToPolarisSQLData(PolarisTable.POSTAL_CODES);
        System.out.println("GET_NAME: '" + pTable.getName() + "'");
        assertTrue(pTable.getName().compareTo("PostalCodes") == 0);
    }

    /**
     * Test of getValue method, of class MeCardDataToPolarisSQLData.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("==getValue==");
        MeCardDataToNativeData pTable = new MeCardDataToPolarisSQLData(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + 
                pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()) + "'");
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") == 0);
        assertNotNull(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()));
    }

    /**
     * Test of setValue method, of class MeCardDataToPolarisSQLData.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        MeCardDataToNativeData pTable = new MeCardDataToPolarisSQLData(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + 
                pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()) + "'");
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") == 0);
        assertNotNull(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()));
        assertTrue(pTable.getValue("Henry").compareTo("") == 0);
    }
    
    @Test
    public void testGetKeys()
    {
        System.out.println("==getKeys==");
        MeCardDataToPolarisSQLData pTable = new MeCardDataToPolarisSQLData(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + 
                pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()) + "'");
        System.out.println("GET_KEYS:");
        for (String s: pTable.getKeys())
        {
            System.out.println("KEY:" + s);
            assertTrue(s.compareTo("EmailAddress") == 0);
        }
    }

    /**
     * Test of renameKey method, of class MeCardDataToPolarisSQLData.
     */
    @Test
    public void testRenameKey()
    {
        System.out.println("==renameKey==");
        MeCardDataToNativeData pTable = new MeCardDataToPolarisSQLData(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + 
                pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()) + "'");
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") != 0);
        assertTrue(pTable.renameKey(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(),
                PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()));
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") == 0);
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()).isEmpty());
    }

    /**
     * Test of deleteValue method, of class MeCardDataToPolarisSQLData.
     */
    @Test
    public void testDeleteValue()
    {
        System.out.println("==deleteValue==");
        MeCardDataToNativeData pTable = new MeCardDataToPolarisSQLData(PolarisTable.PATRON_REGISTRATION);
        pTable.setValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), "anisbet@ualberta.ca");
        System.out.println("GET_VALUE: '" + 
                pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()) + "'");
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") != 0);
        assertTrue(pTable.renameKey(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(),
                PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()));
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") == 0);
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()).isEmpty());
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).compareTo("anisbet@ualberta.ca") == 0);
        pTable.deleteValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString());
        assertTrue(pTable.getValue(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString()).isEmpty());
    }
    
}
