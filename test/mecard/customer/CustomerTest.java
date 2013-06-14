/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.customer;

import mecard.Exception.InvalidCustomerException;
import mecard.Protocol;
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
    
    /**
     * Test of bit field method, of class Customer.
     */
    @Test
    public void testBitFields() {
        System.out.println("===BitFields===");
        String customerString = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|CB0040|"
                + "BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|PCEPL-THREE|PFM|"
                + "DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
        
        SIPCustomerFormatter instance = new SIPCustomerFormatter();
        String expResult = "21221012345678|X|Billy, Balzac|7 Sir Winston Churchill Square|"
                + "Edmonton|AB|T5J2V4|M|ilsteam@epl.ca|X|20050303|20140321|X|X|X|X|X|X|X|X|Balzac|Billy|";
        Customer result = instance.getCustomer(customerString);
        assertEquals(expResult.compareTo(result.toString()), 0);
        
        // now let's set the customer's is good standing.
        expResult = "21221012345678|X|Billy, Balzac|7 Sir Winston Churchill Square|"
                + "Edmonton|AB|T5J2V4|M|ilsteam@epl.ca|X|20050303|20140321|X|X|X|X|X|X|N|X|Balzac|Billy|";
        result.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.FALSE);
        System.out.println("RESULT:"+result.toString());
        assertEquals(expResult.compareTo(result.toString()), 0);
        
        // now let's set the customer's is good standing.
        expResult = "21221012345678|X|Billy, Balzac|7 Sir Winston Churchill Square|"
                + "Edmonton|AB|T5J2V4|M|ilsteam@epl.ca|X|20050303|20140321|X|X|X|X|X|X|N|Y|Balzac|Billy|";
        result.set(CustomerFieldTypes.ISLOSTCARD, Protocol.TRUE);
        System.out.println("RESULT:"+result.toString());
        assertEquals(expResult.compareTo(result.toString()), 0);
        
        // now let's set the customer's is good standing.
        expResult = "21221012345678|X|Billy, Balzac|7 Sir Winston Churchill Square|"
                + "Edmonton|AB|T5J2V4|M|ilsteam@epl.ca|X|20050303|20140321|X|X|Y|X|X|X|N|Y|Balzac|Billy|";
        result.set(CustomerFieldTypes.ISVALID, Protocol.TRUE);
        System.out.println("RESULT:"+result.toString());
        assertEquals(expResult.compareTo(result.toString()), 0);
        
        // now let's set the customer's is good standing.
        expResult = "21221012345678|X|Billy, Balzac|7 Sir Winston Churchill Square|"
                + "Edmonton|AB|T5J2V4|M|ilsteam@epl.ca|X|20050303|20140321|X|X|Y|N|X|X|N|Y|Balzac|Billy|";
        result.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
        System.out.println("RESULT:"+result.toString());
        assertEquals(expResult.compareTo(result.toString()), 0);
        
        // now let's set the customer's is good standing.
        expResult = "21221012345678|X|Billy, Balzac|7 Sir Winston Churchill Square|"
                + "Edmonton|AB|T5J2V4|M|ilsteam@epl.ca|X|20050303|20140321|X|X|Y|N|Y|X|N|Y|Balzac|Billy|";
        result.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.TRUE);
        System.out.println("RESULT:"+result.toString());
        assertEquals(expResult.compareTo(result.toString()), 0);
        
        // now let's set the customer's is good standing.
        expResult = "21221012345678|X|Billy, Balzac|7 Sir Winston Churchill Square|"
                + "Edmonton|AB|T5J2V4|M|ilsteam@epl.ca|X|20050303|20140321|X|X|Y|N|Y|N|N|Y|Balzac|Billy|";
        result.set(CustomerFieldTypes.ISRESIDENT, Protocol.FALSE);
        System.out.println("RESULT:"+result.toString());
        assertEquals(expResult.compareTo(result.toString()), 0);
    }

    /**
     * Test of toString method, of class Customer.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        Customer instance = new Customer();
        String expResult = "X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|X|";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Customer.
     */
    @Test
    public void testEquals()
    {
        System.out.println("equals");
        Object o = null;
        String custRequest1 =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H0H0|M|ilsteam@epl.ca|7804964058|19750822|20140602|X|X|Y|Y|N|Y|Y|N|Balzac|Billy|";
        String custRequest2 =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H0H0|X|ilsteam@epl.ca|7804964058|19750822|20140602|X|X|Y|Y|N|Y|Y|N|Balzac|Billy|";
        Customer c1 = new Customer(custRequest1);
        Customer c2 = new Customer(custRequest1);
        boolean expResult = true;
        boolean result = c1.equals(c2);
        assertEquals(expResult, result);
        
        c1 = new Customer(custRequest1);
        c2 = new Customer(custRequest2);
        expResult = false;
        result = c1.equals(c2);
        assertEquals(expResult, result);
    }
}