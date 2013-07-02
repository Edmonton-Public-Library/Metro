/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Andrew Nisbet
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
package mecard.responder;

import api.Request;
import api.Response;
import mecard.ResponseTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class APIResponderTest
{
    
    public APIResponderTest()
    {
        
    }

    /**
     * Test of getResponse method, of class APIResponder.
     */
    @Test
    public void testGetResponse()
    {
        System.out.println("==getResponse==");
        String command = "[\"QA0\"]";
        Request r = new Request(command);
        APIResponder instance = new APIResponder(r, false);
        String expResult = "[\"RA1\",\"\"]";
        String result = instance.getResponse();
//        System.out.println("RESULT:"+result);
        assertEquals(expResult.compareTo(result.toString()), 0);
        // to test failure you have to edit the sip2_config.xml file.
        // blocked user
        command = "[\"QB0\",\"12345678abcdef\",\"21221012345678\",\"64058\"]";
        r = new Request(command);
        instance = new APIResponder(r, false);
        expResult = "[\"RA5\",\"there is a problem with your account, please contact your home library for assistance\"]";
//        expResult = "RA1\",\"21221012345678\",\"64058\",\"Billy, Balzac\",\"7 Sir Winston Churchill Square\",\"Edmonton\",\"AB\",\"T5J2V4\",\"M\",\"ilsteam@epl.ca\",\"X\",\"20050303\",\"20140321\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"Balzac\",\"Billy\",\"\"]";
        result = instance.getResponse();
//        System.out.println("RESULT:"+result);
        assertEquals(expResult, result.toString());
        
        // Ok user.
        command = "[\"QB0\",\"12345678abcdef\",\"21221015133926\",\"64058\"]";
        r = new Request(command);
        instance = new APIResponder(r, true);
//      63                               AO|AA21221015133926|AD64058|AY0AZF37A
//      recv:64              00020130610    095814000000000000000000000000AO|AA21221015133926|AEBalzac, William (Dr)|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 0.00|BD11811 72 Ave. Edmonton, AB T6G 2B2|BEilsteam@epl.ca|BHUSD|PA20140514    235900|PD|PCEPL-ADULT|DB$0.00|DM$0.00|AFOK|AY0AZBA2C
        expResult = "[\"RA1\",\"21221015133926\",\"64058\",\"Balzac, William (Dr)\",\"11811 72 Ave.\",\"Edmonton\",\"AB\",\"T6G2B2\",\"X\",\"ilsteam@epl.ca\",\"X\",\"X\",\"20140514\",\"X\",\"X\",\"X\",\"Y\",\"N\",\"Y\",\"Y\",\"X\",\"William (Dr)\",\"Balzac\",\"\"]";
        result = instance.getResponse();
        System.out.println("RESULT:"+result);
        assertEquals(expResult, result.toString());
        
        // Fail because PIN is wrong.
        command = "[\"QB0\",\"12345678abcdef\",\"21221012345678\",\"1111\"]";
        r = new Request(command);
        instance = new APIResponder(r, false);
        expResult = "[\"RA6\",\"invalid PIN\"]";
        result = instance.getResponse();
//        System.out.println("RESULT:"+result);
        assertEquals(expResult, result.toString());
    }

    /**
     * Test of getCustomer method, of class APIResponder.
     */
    @Test
    public void testGetUser()
    {
        System.out.println("==getUser==");
        Response response = new Response();
        String command = "[\"QB0\",\"12345678abcdef\",\"21221015133926\",\"64058\"]";
        Request r = new Request(command);
        APIResponder instance = new APIResponder(r, true);
//      63                               AO|AA21221015133926|AD64058|AY0AZF37A
//      recv:64              00020130610    095814000000000000000000000000AO|AA21221015133926|AEBalzac, William (Dr)|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 0.00|BD11811 72 Ave. Edmonton, AB T6G 2B2|BEilsteam@epl.ca|BHUSD|PA20140514    235900|PD|PCEPL-ADULT|DB$0.00|DM$0.00|AFOK|AY0AZBA2C
        String expResult = "[\"RA1\",\"21221015133926\",\"64058\",\"Balzac, William (Dr)\",\"11811 72 Ave.\",\"Edmonton\",\"AB\",\"T6G2B2\",\"X\",\"ilsteam@epl.ca\",\"X\",\"X\",\"20140514\",\"X\",\"X\",\"X\",\"Y\",\"N\",\"Y\",\"Y\",\"X\",\"William (Dr)\",\"Balzac\",\"\"]";
        instance.getCustomer(response);

        String result = response.toString();
        System.out.println("RESULT:"+result);
        String responseString = instance.getResponse();
        System.out.println("RESPONSE:"+responseString);
        assertEquals(expResult, responseString);
    }
}