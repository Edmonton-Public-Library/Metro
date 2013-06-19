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

import api.SymphonyAPIBuilder;
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
        String command = "QA0|";
        APIResponder instance = new APIResponder(command, false);
        String expResult = "RA1||";
        String result = instance.getResponse();
//        System.out.println("RESULT:"+result);
        assertEquals(expResult.compareTo(result.toString()), 0);
        // to test failure you have to edit the sip2_config.xml file.
        // blocked user
        command = "QB0|12345678abcdef|21221012345678|64058|";
        instance = new APIResponder(command, false);
        expResult = "RA5|there is a problem with your account, please contact your home library for assistance|";
//        expResult = "RA1|21221012345678|64058|Billy, Balzac|7 Sir Winston Churchill Square|Edmonton|AB|T5J2V4|M|ilsteam@epl.ca|X|20050303|20140321|X|X|X|X|X|X|X|X|Balzac|Billy||";
        result = instance.getResponse();
//        System.out.println("RESULT:"+result);
        assertEquals(expResult, result.toString());
        
        // Ok user.
        command = "QB0|12345678abcdef|21221015133926|64058|";
        instance = new APIResponder(command, true);
//      63                               AO|AA21221015133926|AD64058|AY0AZF37A
//      recv:64              00020130610    095814000000000000000000000000AO|AA21221015133926|AEBalzac, William (Dr)|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 0.00|BD11811 72 Ave. Edmonton, AB T6G 2B2|BEilsteam@epl.ca|BHUSD|PA20140514    235900|PD|PCEPL-ADULT|DB$0.00|DM$0.00|AFOK|AY0AZBA2C
        expResult = "RA1|21221015133926|64058|Balzac, William (Dr)|11811 72 Ave.|Edmonton|AB|T6G2B2|X|ilsteam@epl.ca|X|X|20140514|X|X|X|Y|N|Y|Y|X|William (Dr)|Balzac||";
        result = instance.getResponse();
        System.out.println("RESULT:"+result);
        assertEquals(expResult, result.toString());
        
        // Fail because PIN is wrong.
        command = "QB0|12345678abcdef|21221012345678|1111|";
        instance = new APIResponder(command, false);
        expResult = "RA6|invalid PIN|";
        result = instance.getResponse();
//        System.out.println("RESULT:"+result);
        assertEquals(expResult, result.toString());
    }

    /**
     * Test of updateCustomer method, of class APIResponder.
     */
    @Test
    public void testUpdateCustomer()
    {
        System.out.println("updateCustomer");
        StringBuffer responseBuffer = null;
        APIResponder instance = null;
        ResponseTypes expResult = null;
        ResponseTypes result = instance.updateCustomer(responseBuffer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createCustomer method, of class APIResponder.
     */
    @Test
    public void testCreateCustomer()
    {
        System.out.println("createCustomer");
        StringBuffer responseBuffer = null;
        APIResponder instance = null;
        ResponseTypes expResult = null;
        ResponseTypes result = instance.createCustomer(responseBuffer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCustomer method, of class APIResponder.
     */
    @Test
    public void testGetUser()
    {
        System.out.println("==getUser==");
        StringBuffer responseBuffer = new StringBuffer();
        String command = "QB0|12345678abcdef|21221015133926|64058|";
        APIResponder instance = new APIResponder(command, true);
//      63                               AO|AA21221015133926|AD64058|AY0AZF37A
//      recv:64              00020130610    095814000000000000000000000000AO|AA21221015133926|AEBalzac, William (Dr)|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 0.00|BD11811 72 Ave. Edmonton, AB T6G 2B2|BEilsteam@epl.ca|BHUSD|PA20140514    235900|PD|PCEPL-ADULT|DB$0.00|DM$0.00|AFOK|AY0AZBA2C
        String expResult = "RA1|21221015133926|64058|Balzac, William (Dr)|11811 72 Ave.|Edmonton|AB|T6G2B2|X|ilsteam@epl.ca|X|X|20140514|X|X|X|Y|N|Y|Y|X|William (Dr)|Balzac||";
        ResponseTypes result = instance.getCustomer(responseBuffer);
        System.out.println("RESULT:"+result);
        String response = instance.getResponse();
        System.out.println("RESPONSE:"+response);
        assertEquals(expResult, response);
    }

    /**
     * Test of getServerStatus method, of class APIResponder.
     */
    @Test
    public void testGetServerStatus()
    {
        System.out.println("getServerStatus");
        StringBuffer responseBuffer = null;
        APIResponder instance = null;
        ResponseTypes expResult = null;
        ResponseTypes result = instance.getILSStatus(responseBuffer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}