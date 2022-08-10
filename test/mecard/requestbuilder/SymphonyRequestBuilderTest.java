/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
package mecard.requestbuilder;

import org.junit.Test;

/**
 *
 * @author anisbet
 */


public class SymphonyRequestBuilderTest
{
    
    public SymphonyRequestBuilderTest()
    {
    }

    /**
     * Test of getCreateUserCommand method, of class SymphonyRequestBuilder.
     */
    @Test
    public void testGetCreateUserCommand()
    {
        System.out.println("==getCreateUserCommand==");
        String property = "DEFER:C:\\Users\\ANisbet\\Dropbox\\development\\MeCard";
        String path = property.substring(SymphonyRequestBuilder.SSH_DEFER_MARKER.length());
        System.out.println("DEFER= '" + path + "'");
    }

    /**
     * Test of getUpdateUserCommand method, of class SymphonyRequestBuilder.
     */
//    @Test
//    public void testGetUpdateUserCommand()
//    {
//        System.out.println("==getUpdateUserCommand==");
//        Customer customer = null;
//        Response response = null;
//        CustomerLoadNormalizer normalizer = null;
//        SymphonyRequestBuilder instance = null;
//        Command expResult = null;
//        Command result = instance.getUpdateUserCommand(customer, response, normalizer);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
