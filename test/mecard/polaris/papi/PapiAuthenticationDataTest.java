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

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class PapiAuthenticationDataTest
{


    /**
     * Test of getPatronAuthentication method, of class PapiAuthenticationData.
     */
    @Test
    public void testGetPatronAuthenticationData()
    {
        System.out.println("getPatronAuthenticationData");
        String userId = "21221012345678";
        String password = "mYp@sSw0rd!";
        String expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<PatronAuthenticationData>"
                + "<Barcode>21221012345678</Barcode>"
                + "<Password>mYp@sSw0rd!</Password>"
                + "</PatronAuthenticationData>";
        String result = PapiAuthenticationData.getPatronAuthentication(userId, password);
        System.out.println(result);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getStaffAuthentication method, of class StaffAuthenticationData.
     */
    @Test
    public void testGetStaffAuthentication()
    {
        System.out.println("getStaffAuthentication");
        String domain = "SB-DEWEY";
        String userId = "SB-DEWEY\\PolarisStaff";
        String password = "PolarisSandbox01";
        String expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<AuthenticationData>"
                + "<Domain>SB-DEWEY</Domain>"
                + "<Username>SB-DEWEY\\PolarisStaff</Username>"
                + "<Password>PolarisSandbox01</Password>"
                + "</AuthenticationData>";
        String result = PapiAuthenticationData.getStaffAuthentication(domain, userId, password);
        System.out.println(result);
        assertEquals(expResult, result);
        
    }
    
}
