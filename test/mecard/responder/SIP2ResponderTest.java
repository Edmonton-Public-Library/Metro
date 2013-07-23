/*
 * To change this template, choose Tools \"] Templates
 * and open the template in the editor.
 */
package mecard.responder;

import api.Request;
import api.Response;
import json.RequestDeserializer;
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
    private final String meta;
    private final Customer c;
    private Request request;
    private Request customerRequest;
    
    public SIP2ResponderTest() 
    {
        this.meta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
        String statusRequest =
                "{\"code\":\"GET_STATUS\",\"authorityToken\":\"12345678\",\"userId\":\"\",\"pin\":\"\",\"customer\":\"null\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        this.request = deserializer.getDeserializedRequest(statusRequest);
        this.c = request.getCustomer();
        //
        String stringCustomerRequest =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"null\"}";
        this.customerRequest = deserializer.getDeserializedRequest(stringCustomerRequest);
        
    }

    /**
     * Test of getResponse method, of class SIP2Responder.
     */
    @Test
    public void testGetResponse() {
        System.out.println("===getResponse===");
        SIP2Responder instance = new SIP2Responder(request, false);
        Response response = new Response();
        instance.getILSStatus(response);
//        System.out.println("RESPONSE:"+response.toString());
        String expResult = "[\"OK\", \"\"]";
        assertTrue(expResult.compareTo(response.toString()) == 0);
    }

    /**
     * Test of getCustomer method, of class SIP2Responder.
     */
    @Test
    public void testGetCustomer()
    {
        System.out.println("=== getCustomer ===");
        SIP2Responder sip = new SIP2Responder(this.customerRequest, false);
        Response response = new Response();
        sip.getCustomer(response);
//        System.out.println("RESPONSE:"+response.toString());
        String expectedResponse = "[\"OK\", \"\", \"[21221012345678, 64058, Billy, Balzac, 7 Sir Winston Churchill Square, Edmonton, AB, T5J2V4, M, ilsteam@epl.ca, , 20050303, 20140321, X, X, X, Y, N, Y, Y, N, Balzac, Billy]\"]";
        assertTrue(response.toString().compareTo(expectedResponse) == 0);
    }

    /**
     * Test of getILSStatus method, of class SIP2Responder.
     */
    @Test
    public void testGetILSStatus()
    {
        System.out.println("===getILSStatus===");
        SIP2Responder instance = new SIP2Responder(request, false);
        Response response = new Response();
        instance.getILSStatus(response);
//        System.out.println("RESPONSE:"+response.toString());
        String expResult = "[\"OK\", \"\"]";
        assertTrue(expResult.compareTo(response.toString()) == 0);
    }
}