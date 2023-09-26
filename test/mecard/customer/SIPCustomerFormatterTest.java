/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2023  Edmonton Public Library
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
package mecard.customer;

import mecard.sip.SIPToMeCardCustomer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class is almost useless, use the specific getNormalizer Test classes for each library
 * for concise results.
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class SIPCustomerFormatterTest
{
    private final String customerString;
    
    public SIPCustomerFormatterTest()
    {
        this.customerString = "64YYYY      Y   00020130606    115820000000000000000100000000AO|AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4 780-496-4058|BEilsteam@epl.ca|BF780-499-1234|BHUSD|PA20140321    235900|PD20050303|PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
//        this.customerString = "64              00120230707    102544000000020007000000000000AO203|AA20400260002117|AEBender, Teagan|BZ0100|CA0100|CB0100|BLY|CQY|BHCAD|BV0.00|CC10.00|BDBox 1146, Diamond Valley, AB T0L 2A0|BElizzie10-1@hotmail.com|BF403-554-1783|BC|PA4|PEABDSRC|PSAdult (18-64)|U1Diamond Valley - Town|U2Davies, Kim|U3FR|U4(none)|U5|PZT0L 2A0|PX20240213    235959|PYN|FA2.70|PC180565|PB20400260002117|AFPatron status is ok.|AGPatron status is ok.|AY1AZ85F3";
// produces:
// [20400260002117, 1783, Bender, Teagan, Box 1146 Diamond Valley, X, AB, T0L 2A0, X, lizzie10-1@hotmail.com, 4035541783, X, 20240213, Patron status is ok., X, Y, Y, N, Y, Y, X, Teagan, Bender]
    }

    /**
     * Test of getCustomer method, of class SIPToMeCardCustomer.
     */
    @Test
    public void testGetCustomer_String()
    {
        System.out.println("== getCustomer ==");
        SIPToMeCardCustomer instance = new SIPToMeCardCustomer();
        String expResult = "[\"21221012345678\",\"X\",\"Billy, Balzac\",\"7 Sir Winston Churchill Square\",\""
                + "Edmonton\",\"AB\",\"T5J2V4\",\"M\",\"ilsteam@epl.ca\",\"X\",\"20050303\",\"20140321\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"Balzac\",\"Billy\"]";
        Customer result = instance.getCustomer(this.customerString);
        assertTrue(expResult.compareTo(result.toString()) == 0);
    }

    /**
     * Test of getCustomer method, of class SIPToMeCardCustomer.
     */
    @Test
    public void testGetCustomer_List()
    {
        System.out.println("== getCustomer (list) ==");
        List<String> s = new ArrayList<>();
        s.add(this.customerString);
        SIPToMeCardCustomer instance = new SIPToMeCardCustomer();
        // SIP doesn't return the PIN and currently does not return phone number.
        String expResult = "21221012345678\",\"X\",\"Billy, Balzac\",\"7 Sir Winston Churchill Square\",\""
                + "Edmonton\",\"AB\",\"T5J2V4\",\"M\",\"ilsteam@epl.ca\",\"X\",\"20050303\",\"20140321\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"Balzac\",\"Billy\"]";
        Customer result = instance.getCustomer(s);
        assertTrue(expResult.compareTo(result.toString()) == 0);
    }
}