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
    private String expectedEmpty;
    private String expectedName;
    private String expectedCustomerFull;
    private String expectedLastName;
    private String expectedFirstName;
    private String expectedEmail;
    private String expectedBitFields;

    public CustomerTest() 
    {
        this.expectedEmpty = "{\"ID\":\"X\",\"PIN\":\"X\",\"NAME\":\"X\",\"STREET\":\"X\",\"CITY\":\"X\",\"PROVINCE\":\"X\",\"POSTALCODE\":\"X\",\"GENDER\":\"X\",\"EMAIL\":\"X\",\"PHONE\":\"X\",\"DOB\":\"X\",\"PRIVILEGE_EXPIRES\":\"X\",\"RESERVED\":\"X\",\"DEFAULT\":\"X\",\"ISVALID\":\"X\",\"ISMINAGE\":\"X\",\"ISRECIPROCAL\":\"X\",\"ISRESIDENT\":\"X\",\"ISGOODSTANDING\":\"X\",\"ISLOSTCARD\":\"X\",\"FIRSTNAME\":\"X\",\"LASTNAME\":\"X\"}";
        this.expectedName = "{\"ID\":\"X\",\"PIN\":\"X\",\"NAME\":\"Billy, Balzac\",\"STREET\":\"X\",\"CITY\":\"X\",\"PROVINCE\":\"X\",\"POSTALCODE\":\"X\",\"GENDER\":\"X\",\"EMAIL\":\"X\",\"PHONE\":\"X\",\"DOB\":\"X\",\"PRIVILEGE_EXPIRES\":\"X\",\"RESERVED\":\"X\",\"DEFAULT\":\"X\",\"ISVALID\":\"X\",\"ISMINAGE\":\"X\",\"ISRECIPROCAL\":\"X\",\"ISRESIDENT\":\"X\",\"ISGOODSTANDING\":\"X\",\"ISLOSTCARD\":\"X\",\"FIRSTNAME\":\"Balzac\",\"LASTNAME\":\"Billy\"}";
        this.expectedLastName = "{\"ID\":\"X\",\"PIN\":\"X\",\"NAME\":\"Billy,\",\"STREET\":\"X\",\"CITY\":\"X\",\"PROVINCE\":\"X\",\"POSTALCODE\":\"X\",\"GENDER\":\"X\",\"EMAIL\":\"X\",\"PHONE\":\"X\",\"DOB\":\"X\",\"PRIVILEGE_EXPIRES\":\"X\",\"RESERVED\":\"X\",\"DEFAULT\":\"X\",\"ISVALID\":\"X\",\"ISMINAGE\":\"X\",\"ISRECIPROCAL\":\"X\",\"ISRESIDENT\":\"X\",\"ISGOODSTANDING\":\"X\",\"ISLOSTCARD\":\"X\",\"FIRSTNAME\":\"X\",\"LASTNAME\":\"Billy\"}";
        this.expectedFirstName = "{\"ID\":\"X\",\"PIN\":\"X\",\"NAME\":\", Balzac\",\"STREET\":\"X\",\"CITY\":\"X\",\"PROVINCE\":\"X\",\"POSTALCODE\":\"X\",\"GENDER\":\"X\",\"EMAIL\":\"X\",\"PHONE\":\"X\",\"DOB\":\"X\",\"PRIVILEGE_EXPIRES\":\"X\",\"RESERVED\":\"X\",\"DEFAULT\":\"X\",\"ISVALID\":\"X\",\"ISMINAGE\":\"X\",\"ISRECIPROCAL\":\"X\",\"ISRESIDENT\":\"X\",\"ISGOODSTANDING\":\"X\",\"ISLOSTCARD\":\"X\",\"FIRSTNAME\":\"Balzac\",\"LASTNAME\":\"X\"}";
                
        this.expectedCustomerFull = //"{\"ID\":\"X\",\"PIN\":\"X\",\"NAME\":\"X\",\"STREET\":\"X\",\"CITY\":\"X\",\"PROVINCE\":\"X\",\"POSTALCODE\":\"X\",\"GENDER\":\"X\",\"EMAIL\":\"X\",\"PHONE\":\"X\",\"DOB\":\"X\",\"PRIVILEGE_EXPIRES\":\"X\",\"RESERVED\":\"X\",\"DEFAULT\":\"X\",\"ISVALID\":\"X\",\"ISMINAGE\":\"X\",\"ISRECIPROCAL\":\"X\",\"ISRESIDENT\":\"X\",\"ISGOODSTANDING\":\"X\",\"ISLOSTCARD\":\"X\",\"FIRSTNAME\":\"X\",\"LASTNAME\":\"X\"}";
                "{\"ID\":\"21221012345678\",\"PIN\":\"6058\",\"NAME\":\"Billy, Balzac\",\"STREET\":\"12345 123 St.\",\"CITY\":\"Edmonton\",\"PROVINCE\":\"Alberta\",\"POSTALCODE\":\"H0H0H0\",\"GENDER\":\"M\",\"EMAIL\":\"ilsteam@epl.ca\",\"PHONE\":\"7804964058\",\"DOB\":\"19750822\",\"PRIVILEGE_EXPIRES\":\"20140602\",\"RESERVED\":\"X\",\"DEFAULT\":\"X\",\"ISVALID\":\"Y\",\"ISMINAGE\":\"Y\",\"ISRECIPROCAL\":\"N\",\"ISRESIDENT\":\"Y\",\"ISGOODSTANDING\":\"Y\",\"ISLOSTCARD\":\"N\",\"FIRSTNAME\":\"Balzac\",\"LASTNAME\":\"Billy\"}";
        this.expectedEmail = "{\"ID\":\"X\",\"PIN\":\"X\",\"NAME\":\"X\",\"STREET\":\"X\",\"CITY\":\"X\",\"PROVINCE\":\"X\",\"POSTALCODE\":\"X\",\"GENDER\":\"X\",\"EMAIL\":\"ilsteam@epl.ca\",\"PHONE\":\"X\",\"DOB\":\"X\",\"PRIVILEGE_EXPIRES\":\"X\",\"RESERVED\":\"X\",\"DEFAULT\":\"X\",\"ISVALID\":\"X\",\"ISMINAGE\":\"X\",\"ISRECIPROCAL\":\"X\",\"ISRESIDENT\":\"X\",\"ISGOODSTANDING\":\"X\",\"ISLOSTCARD\":\"X\",\"FIRSTNAME\":\"X\",\"LASTNAME\":\"X\"}";
        this.expectedBitFields = "{\"ID\":\"X\","
                + "\"PIN\":\"X\","
                + "\"NAME\":\"X\","
                + "\"STREET\":\"X\","
                + "\"CITY\":\"X\","
                + "\"PROVINCE\":\"X\","
                + "\"POSTALCODE\":\"X\","
                + "\"GENDER\":\"X\","
                + "\"EMAIL\":\"ilsteam@epl.ca\","
                + "\"PHONE\":\"X\","
                + "\"DOB\":\"X\","
                + "\"PRIVILEGE_EXPIRES\":\"X\","
                + "\"RESERVED\":\"X\","
                + "\"DEFAULT\":\"X\","
                + "\"ISVALID\":\"Y\","
                + "\"ISMINAGE\":\"X\","
                + "\"ISRECIPROCAL\":\"X\","
                + "\"ISRESIDENT\":\"X\","
                + "\"ISGOODSTANDING\":\"X\","
                + "\"ISLOSTCARD\":\"X\","
                + "\"FIRSTNAME\":\"X\","
                + "\"LASTNAME\":\"X\"}";
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
        assertTrue(c1.toString().compareTo(this.expectedName) == 0);

//        System.out.println(">:" + c1);
//        System.out.println(">:" + expectedCustomerFull);
        Customer c = new Customer(expectedCustomerFull);
        
        
        try
        {
            Customer c2 = new Customer(expectedCustomerFull);
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
        assertTrue(c1.toString().compareTo(expectedName) == 0);

        value = "Billy,";
        Customer c2 = new Customer();
        c2.setName(value);
//        System.out.println("RESP LST_NAME:" + c2);
//        System.out.println("RESP LST_NAME:" + this.expectedLastName);
        assertTrue(c2.toString().compareTo(this.expectedLastName) == 0);

        value = ", Balzac";
        c2 = new Customer();
        c2.setName(value);
//        System.out.println("RESP:" + c2);
        assertTrue(c2.toString().compareTo(this.expectedFirstName) == 0);
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
        assertTrue(instance.toString().compareTo(this.expectedEmail) == 0);
        expResult = "X";
        result = instance.get(CustomerFieldTypes.CITY);
        assertEquals(expResult, result);
        assertTrue(instance.toString().compareTo(this.expectedEmail) == 0);
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
        
        SIPFormatter instance = new SIPFormatter();
//        String expResult = "[\"21221012345678\",\"X\",\"Billy, Balzac\",\"7 Sir Winston Churchill Square\",\""
//                + "Edmonton\",\"AB\",\"T5J2V4\",\"M\",\"ilsteam@epl.ca\",\"X\",\"20050303\",\"20140321\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"Balzac\",\"Billy\"]";
        this.expectedBitFields = "{\"ID\":\"21221012345678\","
                + "\"PIN\":\"X\","
                + "\"NAME\":\"Billy, Balzac\","
                + "\"STREET\":\"7 Sir Winston Churchill Square\","
                + "\"CITY\":\"Edmonton\","
                + "\"PROVINCE\":\"AB\","
                + "\"POSTALCODE\":\"T5J2V4\","
                + "\"GENDER\":\"M\","
                + "\"EMAIL\":\"ilsteam@epl.ca\","
                + "\"PHONE\":\"X\","
                + "\"DOB\":\"20050303\","
                + "\"PRIVILEGE_EXPIRES\":\"20140321\","
                + "\"RESERVED\":\"X\","
                + "\"DEFAULT\":\"X\","
                + "\"ISVALID\":\"X\","
                + "\"ISMINAGE\":\"X\","
                + "\"ISRECIPROCAL\":\"X\","
                + "\"ISRESIDENT\":\"X\","
                + "\"ISGOODSTANDING\":\"X\","
                + "\"ISLOSTCARD\":\"X\","
                + "\"FIRSTNAME\":\"Balzac\","
                + "\"LASTNAME\":\"Billy\"}";
        Customer result = instance.getCustomer(customerString);
        assertEquals(this.expectedBitFields.compareTo(result.toString()), 0);
        expectedBitFields = "{\"ID\":\"21221012345678\","
                + "\"PIN\":\"X\","
                + "\"NAME\":\"Billy, Balzac\","
                + "\"STREET\":\"7 Sir Winston Churchill Square\","
                + "\"CITY\":\"Edmonton\","
                + "\"PROVINCE\":\"AB\","
                + "\"POSTALCODE\":\"T5J2V4\","
                + "\"GENDER\":\"M\","
                + "\"EMAIL\":\"ilsteam@epl.ca\","
                + "\"PHONE\":\"X\","
                + "\"DOB\":\"20050303\","
                + "\"PRIVILEGE_EXPIRES\":\"20140321\","
                + "\"RESERVED\":\"X\","
                + "\"DEFAULT\":\"X\","
                + "\"ISVALID\":\"X\","
                + "\"ISMINAGE\":\"X\","
                + "\"ISRECIPROCAL\":\"X\","
                + "\"ISRESIDENT\":\"X\","
                + "\"ISGOODSTANDING\":\"N\","
                + "\"ISLOSTCARD\":\"X\","
                + "\"FIRSTNAME\":\"Balzac\","
                + "\"LASTNAME\":\"Billy\"}";
        result.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.FALSE);
//        System.out.println("RESULT:"+result.toString());
        assertEquals(expectedBitFields.compareTo(result.toString()), 0);
//        
//        // now let's set the customer's is good standing.
        expectedBitFields = "{\"ID\":\"21221012345678\","
                + "\"PIN\":\"X\","
                + "\"NAME\":\"Billy, Balzac\","
                + "\"STREET\":\"7 Sir Winston Churchill Square\","
                + "\"CITY\":\"Edmonton\","
                + "\"PROVINCE\":\"AB\","
                + "\"POSTALCODE\":\"T5J2V4\","
                + "\"GENDER\":\"M\","
                + "\"EMAIL\":\"ilsteam@epl.ca\","
                + "\"PHONE\":\"X\","
                + "\"DOB\":\"20050303\","
                + "\"PRIVILEGE_EXPIRES\":\"20140321\","
                + "\"RESERVED\":\"X\","
                + "\"DEFAULT\":\"X\","
                + "\"ISVALID\":\"X\","
                + "\"ISMINAGE\":\"X\","
                + "\"ISRECIPROCAL\":\"X\","
                + "\"ISRESIDENT\":\"X\","
                + "\"ISGOODSTANDING\":\"N\","
                + "\"ISLOSTCARD\":\"Y\","
                + "\"FIRSTNAME\":\"Balzac\","
                + "\"LASTNAME\":\"Billy\"}";
        result.set(CustomerFieldTypes.ISLOSTCARD, Protocol.TRUE);
//        System.out.println("RESULT:"+result.toString());
        assertEquals(expectedBitFields.compareTo(result.toString()), 0);
        
        // now let's set the customer's is good standing.
        expectedBitFields = "{\"ID\":\"21221012345678\","
                + "\"PIN\":\"X\","
                + "\"NAME\":\"Billy, Balzac\","
                + "\"STREET\":\"7 Sir Winston Churchill Square\","
                + "\"CITY\":\"Edmonton\","
                + "\"PROVINCE\":\"AB\","
                + "\"POSTALCODE\":\"T5J2V4\","
                + "\"GENDER\":\"M\","
                + "\"EMAIL\":\"ilsteam@epl.ca\","
                + "\"PHONE\":\"X\","
                + "\"DOB\":\"20050303\","
                + "\"PRIVILEGE_EXPIRES\":\"20140321\","
                + "\"RESERVED\":\"X\","
                + "\"DEFAULT\":\"X\","
                + "\"ISVALID\":\"Y\","
                + "\"ISMINAGE\":\"X\","
                + "\"ISRECIPROCAL\":\"X\","
                + "\"ISRESIDENT\":\"X\","
                + "\"ISGOODSTANDING\":\"N\","
                + "\"ISLOSTCARD\":\"Y\","
                + "\"FIRSTNAME\":\"Balzac\","
                + "\"LASTNAME\":\"Billy\"}";
        result.set(CustomerFieldTypes.ISVALID, Protocol.TRUE);
//        System.out.println("RESULT:"+result.toString());
        assertEquals(expectedBitFields.compareTo(result.toString()), 0);
        
        // now let's set the customer's is good standing.
        expectedBitFields = "{\"ID\":\"21221012345678\","
                + "\"PIN\":\"X\","
                + "\"NAME\":\"Billy, Balzac\","
                + "\"STREET\":\"7 Sir Winston Churchill Square\","
                + "\"CITY\":\"Edmonton\","
                + "\"PROVINCE\":\"AB\","
                + "\"POSTALCODE\":\"T5J2V4\","
                + "\"GENDER\":\"M\","
                + "\"EMAIL\":\"ilsteam@epl.ca\","
                + "\"PHONE\":\"X\","
                + "\"DOB\":\"20050303\","
                + "\"PRIVILEGE_EXPIRES\":\"20140321\","
                + "\"RESERVED\":\"X\","
                + "\"DEFAULT\":\"X\","
                + "\"ISVALID\":\"Y\","
                + "\"ISMINAGE\":\"N\","
                + "\"ISRECIPROCAL\":\"X\","
                + "\"ISRESIDENT\":\"X\","
                + "\"ISGOODSTANDING\":\"N\","
                + "\"ISLOSTCARD\":\"Y\","
                + "\"FIRSTNAME\":\"Balzac\","
                + "\"LASTNAME\":\"Billy\"}";
        result.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
//        System.out.println("RESULT:"+result.toString());
        assertEquals(expectedBitFields.compareTo(result.toString()), 0);
        
        // now let's set the customer's is good standing.
        expectedBitFields = "{\"ID\":\"21221012345678\","
                + "\"PIN\":\"X\","
                + "\"NAME\":\"Billy, Balzac\","
                + "\"STREET\":\"7 Sir Winston Churchill Square\","
                + "\"CITY\":\"Edmonton\","
                + "\"PROVINCE\":\"AB\","
                + "\"POSTALCODE\":\"T5J2V4\","
                + "\"GENDER\":\"M\","
                + "\"EMAIL\":\"ilsteam@epl.ca\","
                + "\"PHONE\":\"X\","
                + "\"DOB\":\"20050303\","
                + "\"PRIVILEGE_EXPIRES\":\"20140321\","
                + "\"RESERVED\":\"X\","
                + "\"DEFAULT\":\"X\","
                + "\"ISVALID\":\"Y\","
                + "\"ISMINAGE\":\"N\","
                + "\"ISRECIPROCAL\":\"Y\","
                + "\"ISRESIDENT\":\"X\","
                + "\"ISGOODSTANDING\":\"N\","
                + "\"ISLOSTCARD\":\"Y\","
                + "\"FIRSTNAME\":\"Balzac\","
                + "\"LASTNAME\":\"Billy\"}";
        result.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.TRUE);
//        System.out.println("RESULT:"+result.toString());
        assertEquals(expectedBitFields.compareTo(result.toString()), 0);
        
        // now let's set the customer's is good standing.
        expectedBitFields = "{\"ID\":\"21221012345678\","
                + "\"PIN\":\"X\","
                + "\"NAME\":\"Billy, Balzac\","
                + "\"STREET\":\"7 Sir Winston Churchill Square\","
                + "\"CITY\":\"Edmonton\","
                + "\"PROVINCE\":\"AB\","
                + "\"POSTALCODE\":\"T5J2V4\","
                + "\"GENDER\":\"M\","
                + "\"EMAIL\":\"ilsteam@epl.ca\","
                + "\"PHONE\":\"X\","
                + "\"DOB\":\"20050303\","
                + "\"PRIVILEGE_EXPIRES\":\"20140321\","
                + "\"RESERVED\":\"X\","
                + "\"DEFAULT\":\"X\","
                + "\"ISVALID\":\"Y\","
                + "\"ISMINAGE\":\"N\","
                + "\"ISRECIPROCAL\":\"Y\","
                + "\"ISRESIDENT\":\"N\","
                + "\"ISGOODSTANDING\":\"N\","
                + "\"ISLOSTCARD\":\"Y\","
                + "\"FIRSTNAME\":\"Balzac\","
                + "\"LASTNAME\":\"Billy\"}";
        result.set(CustomerFieldTypes.ISRESIDENT, Protocol.FALSE);
        System.out.println("RESULT:"+result.toString());
        assertEquals(expectedBitFields.compareTo(result.toString()), 0);
    }

    /**
     * Test of toString method, of class Customer.
     */
    @Test
    public void testToString()
    {
        System.out.println("==toString==");
        Customer instance = new Customer();
        String result = instance.toString();
        System.out.println("RESULT:"+instance.toString());
        assertEquals(this.expectedEmpty, result);
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
                "[\"QC0\",\"342abf3cb129ffccb74\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\""
                + "Edmonton\",\"Alberta\",\"H0H0H0\",\"M\",\"ilsteam@epl.ca\",\"7804964058\",\"19750822\",\"20140602\",\"X\",\"X\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"Balzac\",\"Billy\"]";
        String custRequest2 =
                "[\"QC0\",\"342abf3cb129ffccb74\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\""
                + "Edmonton\",\"Alberta\",\"H0H0H0\",\"X\",\"ilsteam@epl.ca\",\"7804964058\",\"19750822\",\"20140602\",\"X\",\"X\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"Balzac\",\"Billy\"]";
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