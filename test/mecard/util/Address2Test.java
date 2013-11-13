/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
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
package mecard.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */


public class Address2Test
{
    
    public Address2Test()
    {
    }

    /**
     * Test of getStreet method, of class Address2.
     */
    @Test
    public void testGetStreet()
    {
        System.out.println("getStreet (See AddressTest)");
        
    }

    /**
     * Test of getCity method, of class Address2.
     */
    @Test
    public void testGetCity()
    {
        System.out.println("getCity (See AddressTest)");
        
    }

    /**
     * Test of getProvince method, of class Address2.
     */
    @Test
    public void testGetProvince()
    {
        System.out.println("getProvince (See AddressTest)");
        
    }

    /**
     * Test of getPostalCode method, of class Address2.
     */
    @Test
    public void testGetPostalCode()
    {
        System.out.println("getPostalCode (See AddressTest)");
        
    }

    /**
     * Test of getPhone method, of class Address2.
     */
    @Test
    public void testGetPhone()
    {
        System.out.println("==getPhone==");
        Address2 instance = new Address2("275 Lancaster Terrace Edmonton, AB T5X 5T6 780 496 8345");
        String expResult = "780-496-8345";
        String result = instance.getPhone();
        assertEquals(expResult, result);
        System.out.println("RESULT:"+result);
        System.out.println("RESULT:"+instance.getCity());
        System.out.println("RESULT:"+instance.getProvince());
    }

    /**
     * Test of toString method, of class Address2.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString (See AddressTest)");
    }
    
}
