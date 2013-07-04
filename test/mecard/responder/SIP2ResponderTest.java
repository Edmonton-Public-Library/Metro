/*
 * To change this template, choose Tools \"] Templates
 * and open the template in the editor.
 */
package mecard.responder;

import api.Request;
import api.Response;
import mecard.ResponseTypes;
import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class SIP2ResponderTest {
    private String customerString = "[\"QA0\",\"afsfdasfdsafds\",\"21221012345678\",\"64058\",\"Billy, Balzac\",\"7 Sir Winston Churchill Square\",\"Edmonton\",\"AB\",\"T5J2V4\",\"M\",\"ilsteam@epl.ca\",\"7804309998\",\"20050303\",\"20140321\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"Balzac\",\"Billy\"]";
    public SIP2ResponderTest() 
    {
        
    }

    /**
     * Test of getResponse method, of class SIP2Responder.
     */
    @Test
    public void testGetResponse() {
        System.out.println("===getResponse===");
//        String command = "[\"QA0\",\"abcdef88374667623243\"]";
//        Request r = new Request(command);
//        SIP2Responder instance = new SIP2Responder(r, false);
//        String expResult = "[\"RA1\",\"\"]";
//        String result = instance.getResponse().toString();
////        System.out.println("RESULT:"+result);
//        assertEquals(expResult.compareTo(result.toString()), 0);
//        // to test failure you have to edit the sip2_config.xml file.
//        
//        // blocked user
//        command = "[\"QB0\",\"12345678abcdef\",\"21221012345678\",\"64058\"]";
//        r = new Request(command);
//        instance = new SIP2Responder(r, false);
//        expResult = "[\"RA5\",\"there is a problem with your account, please contact your home library for assistance\"]";
////        expResult = "[\"RA1\",\"21221012345678\",\"64058\",\"Billy, Balzac\",\"7 Sir Winston Churchill Square\",\"Edmonton\",\"AB\",\"T5J2V4\",\"M\",\"ilsteam@epl.ca\",\"X\",\"20050303\",\"20140321\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"Balzac\",\"Billy\",\"\"]";
//        result = instance.getResponse().toString();
////        System.out.println("RESULT:"+result);
//        assertEquals(expResult, result.toString());
//        
//        // Ok user.
//        command = "[\"QB0\",\"12345678abcdef\",\"21221015133926\",\"64058\"]";
//        r = new Request(command);
//        instance = new SIP2Responder(r, true);
////      63                               AO|AA21221015133926|AD64058|AY0AZF37A
////      recv:64              00020130610    095814000000000000000000000000AO|AA21221015133926|AEBalzac, William (Dr)|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 0.00|BD11811 72 Ave. Edmonton, AB T6G 2B2|BEilsteam@epl.ca|BHUSD|PA20140514    235900|PD|PCEPL-ADULT|DB$0.00|DM$0.00|AFOK|AY0AZBA2C
//        expResult = "[\"RA1\",\"21221015133926\",\"64058\",\"Balzac, William (Dr)\","
//                + "\"11811 72 Ave.\",\"Edmonton\",\"AB\",\"T6G2B2\",\"X\","
//                + "\"ilsteam@epl.ca\",\"7803409998\",\"X\",\"20140514\",\"X\","
//                + "\"X\",\"X\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"William (Dr)\","
//                + "\"Balzac\"]";
//        result = instance.getResponse().toString();
//        System.out.println("RESULT:"+result);
//        assertEquals(expResult, result.toString());
//        
//        // Fail because PIN is wrong.
//        command = "[\"QB0\",\"12345678abcdef\",\"21221012345678\",\"1111\"]";
//        r = new Request(command);
//        instance = new SIP2Responder(r, false);
//        expResult = "[\"RA6\",\"invalid PIN\"]";
//        result = instance.getResponse().toString();
////        System.out.println("RESULT:"+result);
//        assertEquals(expResult, result.toString());
    }

    /**
     * Test of meetsMeCardRequirements method, of class SIP2Responder.
     */
    @Test
    public void testMeetsMeCardRequirements()
    {
        System.out.println("== meetsMeCardRequirements ==");
//        Customer customer = new Customer(this.customerString);
//        String additionalData = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
//                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 12.00|"
//                + "BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4 780-340-9998|BEilsteam@epl.ca|BHUSD|"
//                + "PA20140321    235900|PD20050303|PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
//        SIP2Responder instance = new SIP2Responder(new Request("[\"QB0\",\"12345678abcdef\",\"21221012345678\",\"1111\"]"), false);
//        System.out.println(">>>"+additionalData);
//        boolean expResult = false;
//        boolean result = instance.meetsMeCardRequirements(customer, additionalData);
////        System.out.println(">>>"+customer.toString());
//        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomer method, of class SIP2Responder.
     */
    @Test
    public void testGetCustomer()
    {
        System.out.println("getCustomer");
        Response responseBuffer = null;
        SIP2Responder instance = null;
        ResponseTypes expResult = null;
        instance.getCustomer(responseBuffer);
        ResponseTypes result = responseBuffer.getCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getILSStatus method, of class SIP2Responder.
     */
    @Test
    public void testGetILSStatus()
    {
        System.out.println("getILSStatus");
        Response responseBuffer = null;
        SIP2Responder instance = null;
        ResponseTypes expResult = null;
        instance.getCustomer(responseBuffer);
        ResponseTypes result = responseBuffer.getCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}