/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
import mecard.config.CustomerFieldTypes;



import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class SIPFormatterTest
{
    
    public SIPFormatterTest()
    {
    }


    /**
     * Test of setCustomer method, of class SIPToMeCardCustomer.
     */
    @Test
    public void testSetCustomer()
    {
        System.out.println("==getCustomer==");
        SIPToMeCardCustomer formatter = new SIPToMeCardCustomer();
        
        Customer c = formatter.getCustomer("64              00020130903    143600000000000002000000000010AOsps|AA21974011602274|AENUTTYCOMBE, SHARON|AQsps|BZ0200|CA0020|CB0150|BLY|CQY|BD66 Great Oaks, Sherwood Park, Ab, T8A 0V8|BEredtarot@telus.net|BF780-416-5518|DHSHARON|DJNUTTYCOMBE|PA20140903    235900STAFF|PB19680920|PCs|PS20140903    235900STAFF|ZYs|AY1AZA949");
        System.out.println("C_EXPIRY:'" + c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES)+"'");
        assertTrue(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20140903") == 0);
        
        c = formatter.getCustomer("64YYYY          00020130904    114900000000000000000000000002AOsps|AA21974012381670|AEDUGUID, JENNIFER|AQsps|BZ0200|CA0020|CB0150|BLY|CQN|BD10255 PRINCESS ELIZABETH AVENUE, Edmonton, Ab, T5G 0Y1|BEJDUGUID@SCLIBRARY.AB.CA|BF780-862-4431|DHJENNIFER|DJDUGUID|PAstaff|PB19750517|PCs|PE20140226    235900staff|PS20140226    235900staff|ZYs|AF#Incorrect password.|AY1AZ9E34");
        System.out.println("ADDR:'" + c.get(CustomerFieldTypes.STREET) + "'");
        System.out.println("ADDR:'" + c.get(CustomerFieldTypes.CITY) + "'");
        System.out.println("ADDR:'" + c.get(CustomerFieldTypes.PROVINCE) + "'");
        System.out.println("ADDR:'" + c.get(CustomerFieldTypes.POSTALCODE) + "'");
//        assertTrue(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20140903") == 0);
        
        c = formatter.getCustomer("64              00020140429    152600000000000014000000000001AOalap|AA21000006500560|AEFLYNN, GRACE|AQade|BZ0249|CA0010|CB0200|BLY|BHCAD|CC10.|BDRR#2, Delburne, AB, T0M 0V0|BEflynnstrings@gmail.com|BF403 749-3480|DHGRACE|DJFLYNN|PCra|PE20150430    235900|PS20150430    235900|ZYra|AY1AZB606");
        System.out.println("C_EXPIRY:'" + c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES)+"'");
        // These tests don't work because they are not standard PA field but PE which the default SIPToMeCardCustomer doesn't honour
        // any more you have to add this to each Horizon GetNormalizer.
//        assertTrue(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20150430") == 0);
    }
}