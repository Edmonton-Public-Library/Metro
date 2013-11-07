/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
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
package api;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SIPCustomerMessageTest
{
    private String message;
    public SIPCustomerMessageTest()
    {
//        63                               AO|AA29335002291042|AD2003|AY1AZF3B7
//        recv:64              00120131107    134857000000000000000000000000AO203|AA29335002291042|AEHunting, Will|BZ0025|CA0010|CB0100|BLY|CQY|BHCAD|BV0.00|CC10.00|BD433 King Street, Spruce Grove, AB T7X 3B4|BEsthero@yrl.ab.ca|BC|PA17|PEASGY|PSAdult (18-64)|U4(none)|U5|PZT7X 3B4|PX20201025    235959|PYN|AFPatron status is ok.|AGPatron status is ok.|AY1AZ9FF9
        this.message = "64              00120131107    134857000000000000000000000000AO203|AA29335002291042|AEHunting, Will|BZ0025|CA0010|CB0100|BLY|CQY|BHCAD|BV0.00|CC10.00|BD433 King Street, Spruce Grove, AB T7X 3B4|BEsthero@yrl.ab.ca|BC|PA17|PEASGY|PSAdult (18-64)|U4(none)|U5|PZT7X 3B4|PX20201025    235959|PYN|AFPatron status is ok.|AGPatron status is ok.|AY1AZ9FF9";
    }

    /**
     * Test of getCustomerProfile method, of class SIPCustomerMessage.
     */
    @Test
    public void testGetCustomerProfile()
    {
        System.out.println("getCustomerProfile");
        SIPCustomerMessage instance = null;
        String expResult = "";
        String result = instance.getCustomerProfile();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMessage method, of class SIPCustomerMessage.
     */
    @Test
    public void testGetMessage()
    {
        System.out.println("==getMessage==");
        SIPCustomerMessage instance = new SIPCustomerMessage(this.message);
        String expResult = "Patron status is ok.";
        String result = instance.getMessage();
        System.out.println("MESSAGE:" + result);
        assertTrue(result.compareTo(expResult) == 0);
        System.out.println("FIELD_NAMES:");
        for (String fName: instance.getFieldNames())
        {
            System.out.println("F_NAME:" + fName + " VALUE:"+instance.getField(fName));
        }
        
    }
}