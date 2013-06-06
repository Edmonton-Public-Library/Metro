/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.customer;

import mecard.Exception.InvalidCustomerException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class CustomerTest {

    public CustomerTest() {
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
//        System.out.println("RESP:" + c1);
        assertTrue(c1.toString().compareTo("X|X|Billy, Balzac|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|") == 0);

        String custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        Customer c = new Customer(custReq);
        
        custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|";
        try
        {
            Customer c2 = new Customer(custReq);
        }
        catch (Exception e)
        {
            assertTrue(e instanceof InvalidCustomerException);
        }
        
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
//        System.out.println("RESP:" + c1);
        assertTrue(c1.toString().compareTo("X|X|Billy, Balzac|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|Balzac|Billy|") == 0);

        value = "Billy,";
        Customer c2 = new Customer();
        c2.setName(value);
//        System.out.println("RESP:" + c2);
        assertTrue(c2.toString().compareTo("X|X|Billy,|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|Billy|") == 0);

        value = ", Balzac";
        c2 = new Customer();
        c2.setName(value);
//        System.out.println("RESP:" + c2);
        assertTrue(c2.toString().compareTo("X|X|, Balzac|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|Balzac|X|") == 0);
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
        assertTrue(instance.toString().compareTo("X|X|X|X|X|X|X|X|ilsteam@epl.ca|X|X|X|X|X|X|X|X|X|X|X|X|X|") == 0);
    }
}