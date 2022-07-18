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
import mecard.config.CustomerFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class CustomerTest
{
    
    public CustomerTest()
    {
        
    }

    /**
     * Test of setName method, of class Customer.
     */
    @Test
    public void testSetName()
    {
        System.out.println("== setName ==");
        String name = "name, some";
        Customer instance = new Customer();
        instance.setName(name);
        assertTrue(instance.get(CustomerFieldTypes.FIRSTNAME).compareTo("some") == 0);
        assertTrue(instance.get(CustomerFieldTypes.LASTNAME).compareTo("name") == 0);
    }

    /**
     * Test of toString method, of class Customer.
     */
    @Test
    public void testToString()
    {
        System.out.println("==toString==");
        Customer instance = new Customer();
        String expResult = "[X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X]";
        String result = instance.toString();
        assertTrue(expResult.compareTo(result) == 0);
    }

    /**
     * Test of isEmpty method, of class Customer.
     */
    @Test
    public void testIsEmpty()
    {
        System.out.println("==isEmpty==");
        String name = "Doe, John";
        Customer customer = new Customer();
        customer.setName(name);
        customer.set(CustomerFieldTypes.ISLOSTCARD, "Y");
        assertTrue(customer.isLostCard());
        customer.set(CustomerFieldTypes.ISLOSTCARD, "X");
        assertFalse(customer.isLostCard());
    }

    /**
     * Test of isLostCard method, of class Customer.
     */
    @Test
    public void testIsLostCard()
    {
        System.out.println("===isLostCard===");
        String name = "Doe, John";
        Customer customer = new Customer();
        customer.setName(name);
        boolean test = customer.isLostCard();
        assertFalse(customer.isLostCard());
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISGOODSTANDING));
        customer.set(CustomerFieldTypes.ISLOSTCARD, "N");
        assertTrue(customer.isFlagDefined(CustomerFieldTypes.ISLOSTCARD));
        assertTrue(customer.isFlagSetFalse(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetTrue(CustomerFieldTypes.ISLOSTCARD));
        test = customer.isLostCard();
        assertFalse(customer.isLostCard());
        customer.set(CustomerFieldTypes.ISLOSTCARD, "Y");
        assertTrue(customer.isFlagDefined(CustomerFieldTypes.ISLOSTCARD));
        assertTrue(customer.isFlagSetTrue(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetFalse(CustomerFieldTypes.ISLOSTCARD));
        test = customer.isLostCard();
        assertTrue(customer.isLostCard());
        
        customer.set(CustomerFieldTypes.ISLOSTCARD, "X");
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetFalse(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetTrue(CustomerFieldTypes.ISLOSTCARD));
        test = customer.isLostCard();
        assertFalse(customer.isLostCard());
        
        assertTrue(customer.isEmpty(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISGOODSTANDING));
    }
}