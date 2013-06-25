/*
 * To change this template, choose Tools \",\" Templates
 * and open the template in the editor.
 */
package mecard.responder;

import api.Request;
import java.io.File;
import java.util.List;
import mecard.ResponseTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class BImportResponderTest
{
    
    public BImportResponderTest()
    {
    }

    /**
     * Test of getResponse method, of class BImportResponder.
     */
    @Test
    public void testGetResponse()
    {
//        System.out.println("=== getResponse ===");
//        // QueryTypes.CREATE_CUSTOMER;
//        String cmd = "QC0";
//        Protocol instance = new Protocol();
//        String expResult = ResponseTypes.SUCCESS+"\",\"hello world|";
//        String result = instance.processInput(cmd);
//        assertEquals(expResult, result);
    }

    /**
     * Test of submitCustomer method, of class BImportResponder.
     */
    @Test
    public void testSubmitCustomer() {
        System.out.println("===submitCustomer===");
        String hName = "header.txt";
        String dName = "data.txt";
        // cmd(-2),
        // md5(-1),
//        ID(0), 
//        PIN(1),
//        NAME(2),  // last name first name comma-separated.
//        STREET(3), 
//        CITY(4), 
//        PROVINCE(5), 
//        POSTALCODE(6),
//        GENDER(7), 
//        EMAIL(8), 
//        PHONE(9),
//        DOB(10), 
//        PRIVILEGE_EXPIRES(11),
//        RESERVED(12), // General field can be used for what ever, currently empty.
//        DEFAULT(13), // Used for fields that can remain empty like 'province' on Horizon.
//        ISVALID(14), // start of flagged fields. This one means the host library has all customer information.
//        ISMINAGE(15),
//        ISRECIPROCAL(16),
//        ISRESIDENT(17),
//        ISGOODSTANDING(18),
//        ISLOSTCARD(19);
        String custReq = 
                "[\"QC0\",\"342abf3cb129ffccb74\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\""
                + "Edmonton\",\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\",\"7804964058\",\"19750822\",\"20140602\",\"\",\"\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\"]";
        
        BImportResponder br = new BImportResponder(new Request(custReq), true);
        System.out.println("RESPONSE: '"+br.getResponse()+"'");
        
        File f = new File(hName);
        assertTrue(f.exists());
        f = new File(dName);
        assertTrue(f.exists());
        
    }

    /**
     * Test of computeEmailName method, of class BImportResponder.
     */
    @Test
    public void testComputeEmailName() {
        System.out.println("===computeEmailName===");
        String email = "ilsteam@epl.ca";
        // QueryTypes.CREATE_CUSTOMER;
        String cmd = "[\"QC0\",\"123456789abcdef\"]";
        Request r = new Request(cmd);
        BImportResponder instance = new BImportResponder(r, true);
        String expResult = "ilsteam";
        String result = instance.computeEmailName(email);
        assertEquals(expResult, result);
    }

    /**
     * Test of createCustomer method, of class BImportResponder.
     */
    @Test
    public void testCreateCustomer()
    {
        System.out.println("createCustomer");
        StringBuffer responseBuffer = null;
        BImportResponder instance = null;
        ResponseTypes expResult = null;
        ResponseTypes result = instance.createCustomer(responseBuffer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of convert method, of class BImportResponder.
     */
    @Test
    public void testConvert()
    {
        System.out.println("convert");
        List<String> customerCommands = null;
        BImportResponder instance = null;
        boolean expResult = false;
        boolean result = instance.convert(customerCommands);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateCustomer method, of class BImportResponder.
     */
    @Test
    public void testUpdateCustomer()
    {
        System.out.println("updateCustomer");
        StringBuffer responseBuffer = null;
        BImportResponder instance = null;
        ResponseTypes expResult = null;
        ResponseTypes result = instance.updateCustomer(responseBuffer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}