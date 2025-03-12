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
package mecard.security;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class SDapiSecurityTest 
{
    
    private final String envFilePath;
    public SDapiSecurityTest() 
    {
        envFilePath = "/home/anisbet/MeCard/.testenv";
    }

    /**
     * Test of getStaffPassword method, of class SDapiSecurity.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetStaffPassword() throws Exception 
    {
        System.out.println("==getStaffPassword==");
        String expResult = "testStaffPassword";
        SDapiSecurity sds = new SDapiSecurity(envFilePath);
        String result = sds.getStaffPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStaffId method, of class SDapiSecurity.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetStaffId() throws Exception {
        System.out.println("==getStaffId==");
        SDapiSecurity instance = new SDapiSecurity(envFilePath);
        String expResult = "testStaffId";
        String result = instance.getStaffId();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getStaffId method, of class SDapiSecurity.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetStaffOverrideCode() throws Exception {
        System.out.println("==getStaffOverrideCode==");
        SDapiSecurity instance = new SDapiSecurity(envFilePath);
        String expResult = "XXXX";
        String result = instance.getStaffOverrideCode();
        assertEquals(expResult, result);
    }
}
