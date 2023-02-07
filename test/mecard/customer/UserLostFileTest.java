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
package mecard.customer;

import java.io.File;
import mecard.config.CustomerFieldTypes;
import mecard.requestbuilder.SymphonyRequestBuilder;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */


public class UserLostFileTest
{
    
    public UserLostFileTest()
    {
        
    }

    /**
     * Test of setStatus method, of class UserLostFile.
     */
    @Test
    public void testSetStatus()
    {
        System.out.println("==setStatus==");
        String name = "Doe, John";
        Customer customer = new Customer();
        customer.setName(name);
        customer.set(CustomerFieldTypes.ISLOSTCARD, "Y");
        boolean test = customer.isLostCard();
        assertTrue(customer.isLostCard());
        customer.set(CustomerFieldTypes.ALTERNATE_ID, "22222012345678");
        customer.set(CustomerFieldTypes.ID, "22222012222222");
        String loadDir = new SymphonyRequestBuilder(true).getCustomerLoadDirectory();
        System.err.println("loadDir set to " + loadDir);
        System.err.println(customer);
        UserLostFile userFile = new UserLostFile(customer, loadDir);
        userFile.recordUserDataMessage("LOST CARD detected. This message is read from message.properties normally.");
        File f = new File("22222012222222.lost");
        assertTrue(f.exists());
//        f.delete();
    }
    
}
