/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.customer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class CustomerTest 
{
    
    public CustomerTest() 
    {
        
    }

    /**
     * Test of set method, of class Customer.
     */
    @Test
    public void testSet() {
        System.out.println("== set ==");
        CustomerFieldTypes ft = CustomerFieldTypes.NAME;
        String value = "Billy, Balzac";
        Customer c1 = new Customer();
        c1.set(ft, value);
        System.out.println("RESP:"+c1);
        assertTrue(c1.toString().compareTo("||Billy, Balzac||||||||||||||||||||") == 0);
    }

    /**
     * Test of setName method, of class Customer.
     */
    @Test
    public void testSetName() {
        System.out.println("===setName===");
        CustomerFieldTypes ft = CustomerFieldTypes.NAME;
        String value = "Billy, Balzac";
        Customer c1 = new Customer();
        c1.setName(value);
        System.out.println("RESP:"+c1);
        assertTrue(c1.toString().compareTo("||Billy, Balzac||||||||||||||||||Balzac|Billy|") == 0);
        
        value = "Billy,";
        Customer c2 = new Customer();
        c2.setName(value);
        System.out.println("RESP:"+c2);
        assertTrue(c2.toString().compareTo("||Billy,|||||||||||||||||||Billy|") == 0);
        
        value = ", Balzac";
        c2 = new Customer();
        c2.setName(value);
        System.out.println("RESP:"+c2);
        assertTrue(c2.toString().compareTo("||, Balzac||||||||||||||||||Balzac||") == 0);
    }

    /**
     * Test of get method, of class Customer.
     */
    @Test
    public void testGet() {
        System.out.println("===get===");
        CustomerFieldTypes t = CustomerFieldTypes.EMAIL;
        Customer instance = new Customer();
        String email = "ilsteam@epl.ca";
        instance.set(CustomerFieldTypes.EMAIL, email);
        String expResult = "ilsteam@epl.ca";
        String result = instance.get(t);
        assertEquals(expResult, result);
        assertTrue(instance.toString().compareTo("||||||||ilsteam@epl.ca||||||||||||||") == 0);
    }
}