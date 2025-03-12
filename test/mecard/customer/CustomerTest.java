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

import json.RequestDeserializer;
import mecard.Request;
import mecard.config.CustomerFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class CustomerTest
{
    private final Customer testCustomer;
    public CustomerTest()
    {
        String custReq =
                "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"29065029317195\",\"pin\":\"26X4\",\"customer\":\"{\\\"ID\\\":\\\"29065029317195\\\",\\\"PIN\\\":\\\"26262626\\\",\\\"PREFEREDNAME\\\":\\\"Ganton, Robin\\\",\\\"STREET\\\":\\\"6 Hidden Ridge View Nw\\\",\\\"CITY\\\":\\\"Calgary\\\",\\\"PROVINCE\\\":\\\"AB\\\",\\\"POSTALCODE\\\":\\\"T3A 5V7\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"rganton@telus.net\\\",\\\"PHONE\\\":\\\"4032412626\\\",\\\"DOB\\\":\\\"19650113\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20260129\\\",\\\"RESERVED\\\":\\\"OK\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Robin\\\",\\\"LASTNAME\\\":\\\"Ganton\\\"}\"}";
//        String custReq =
//                "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        this.testCustomer = request.getCustomer();
        
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
        assertFalse(customer.isLostCard());
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISGOODSTANDING));
        customer.set(CustomerFieldTypes.ISLOSTCARD, "N");
        assertTrue(customer.isFlagDefined(CustomerFieldTypes.ISLOSTCARD));
        assertTrue(customer.isFlagSetFalse(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetTrue(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isLostCard());
        customer.set(CustomerFieldTypes.ISLOSTCARD, "Y");
        assertTrue(customer.isFlagDefined(CustomerFieldTypes.ISLOSTCARD));
        assertTrue(customer.isFlagSetTrue(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetFalse(CustomerFieldTypes.ISLOSTCARD));
        assertTrue(customer.isLostCard());
        
        customer.set(CustomerFieldTypes.ISLOSTCARD, "X");
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetFalse(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetTrue(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isLostCard());
        
        assertTrue(customer.isEmpty(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISGOODSTANDING));
    }

    /**
     * Test of set method, of class Customer.
     */
    @Test
    public void testSet() {
        System.out.println("==set==");
        Customer customer = new Customer();
        assertFalse(customer.hasValidBirthDate());
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISMINAGE));
        customer.set(CustomerFieldTypes.DOB, "20010206");
        assertTrue(customer.hasValidBirthDate());
        assertTrue(customer.isFlagDefined(CustomerFieldTypes.ISMINAGE));
    }

    /**
     * Test of get method, of class Customer.
     */
    @Test
    public void testGet() {
        System.out.println("==get==");
        Customer customer = new Customer();
        assertFalse(customer.hasValidBirthDate());
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISMINAGE));
        customer.set(CustomerFieldTypes.DOB, "20010206");
        assertTrue(customer.get(CustomerFieldTypes.ISMINAGE).equals("Y"));
        assertTrue(customer.get(CustomerFieldTypes.ISRECIPROCAL).equals("X"));
    }

    /**
     * Test of isFlagSetTrue method, of class Customer.
     */
    @Test
    public void testIsFlagSetTrue() {
        System.out.println("==isFlagSetTrue==");
        Customer customer = new Customer();
        assertFalse(customer.hasValidBirthDate());
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISMINAGE));
        customer.set(CustomerFieldTypes.DOB, "20010206");
        assertTrue(customer.isFlagDefined(CustomerFieldTypes.ISMINAGE));
        assertTrue(customer.isFlagSetTrue(CustomerFieldTypes.ISMINAGE));
        System.out.println("RES>>>" + customer.toString());
        assertFalse(customer.isFlagSetTrue(CustomerFieldTypes.ISRESIDENT));
        assertFalse(customer.isFlagSetFalse(CustomerFieldTypes.ISRESIDENT));
        
        assertFalse(customer.isFlagSetTrue(CustomerFieldTypes.ALTERNATE_ID));
        // Field must match 'N', 'X' does not count.
        assertFalse(customer.isFlagSetFalse(CustomerFieldTypes.ALTERNATE_ID));
    }

    /**
     * Test of isFlagSetFalse method, of class Customer.
     */
    @Test
    public void testIsFlagSetFalse() {
        System.out.println("==isFlagSetFalse==");
        Customer customer = new Customer();
        assertFalse(customer.hasValidBirthDate());
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISMINAGE));
        customer.set(CustomerFieldTypes.DOB, "20010206");
        assertTrue(customer.isFlagSetTrue(CustomerFieldTypes.ISMINAGE));
        assertFalse(customer.isFlagSetFalse(CustomerFieldTypes.ISMINAGE));
    }

    /**
     * Test of isFlagDefined method, of class Customer.
     */
    @Test
    public void testIsFlagDefined() {
        System.out.println("==isFlagDefined==");
        Customer customer = new Customer();
        assertFalse(customer.hasValidBirthDate());
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISMINAGE));
        customer.set(CustomerFieldTypes.DOB, "20010206");
        assertTrue(customer.isFlagDefined(CustomerFieldTypes.ISMINAGE));
    }

    /**
     * Test of hasValidBirthDate method, of class Customer.
     */
    @Test
    public void testHasValidBirthDate() {
        System.out.println("==isValidBirthDate==");
        Customer customer = new Customer();
        assertFalse(customer.hasValidBirthDate());
        customer.set(CustomerFieldTypes.DOB, "20010206");
        assertTrue(customer.hasValidBirthDate());
        customer.set(CustomerFieldTypes.DOB, "20240206");
        assertFalse(customer.hasValidBirthDate());
        customer.set(CustomerFieldTypes.DOB, "1234567ab");
        assertFalse(customer.hasValidBirthDate());
        customer.setDob("19630822");
        assertTrue(customer.hasValidBirthDate());
        customer.setDob("");
        assertFalse(customer.hasValidBirthDate());
        customer.setDob(null);
        assertFalse(customer.hasValidBirthDate());
        customer.set(CustomerFieldTypes.DOB, "20010206");
        assertTrue(customer.hasValidBirthDate());
        System.out.println(">>>" + customer.toString());
        
        
        
        // Try the problem customer's birthdate.
        assertTrue(this.testCustomer.hasValidBirthDate());
        assertTrue(this.testCustomer.get(CustomerFieldTypes.DOB).endsWith("19650113"));
    }
}