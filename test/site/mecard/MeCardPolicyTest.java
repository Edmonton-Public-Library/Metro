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
package site.mecard;

import api.Request;
import json.RequestDeserializer;
import mecard.customer.Customer;
import mecard.config.CustomerFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;


    /**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class MeCardPolicyTest
{
    private final String meta;
    private final Customer c;
    
    public MeCardPolicyTest()
    {
        this.meta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"NAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"GENDER\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"DEFAULT\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        this.c = request.getCustomer();
    }

    /**
     * Test of isMinimumAgeByDate method, of class MeCardPolicy.
     */
    @Test
    public void testIsMinimumAgeByDate()
    {
        System.out.println("==isMinimumAge by Date String==");
        
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isMinimumAgeByDate(c, meta);
//        System.out.println("C's Age is:"+c.get(CustomerFieldTypes.DOB));
        boolean expected= true;
        assertTrue(expected == result);
       
        String modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|"
                + "BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|" // DOB
                + "PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
        
        c.set(CustomerFieldTypes.DOB, "20050101");
        result = p.isMinimumAgeByDate(c, modeMeta);
//        System.out.println("C's Age is:"+c.get(CustomerFieldTypes.DOB));
        
        assertTrue(false == result);
    }
    
/**
     * Test of isEmailable method, of class MeCardPolicy.
     */
    @Test
    public void testIsEmailable()
    {
        System.out.println("==isEmailable==");

        
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isEmailable(c, meta);
        assertTrue(true == result);
        
        c.set(CustomerFieldTypes.EMAIL, "X");
        result = p.isEmailable(c, meta);
        
        assertTrue(false == result);
    }

    /**
     * Test of isValidCustomerData method, of class MeCardPolicy.
     */
    @Test
    public void testIsValidCustomerData()
    {
        System.out.println("==isValidCustomerData==");

        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isValidCustomerData(c);
        boolean expected= true;
        assertTrue(expected == result);

        c.set(CustomerFieldTypes.POSTALCODE, "X");
        result = p.isValidCustomerData(c);
        assertTrue(result == false);
    }

    /**
     * Test of isValidExpiryDate method, of class MeCardPolicy.
     */
    @Test
    public void testIsValidExpiryDate()
    {
        System.out.println("==isValidExpiryDate==");
        
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isValidExpiryDate(c, meta);
        boolean expected= true;
        assertTrue(expected == result);

        c.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, "20120602");
        result = p.isValidExpiryDate(c, meta);
        expected= false;
        assertTrue(expected == result);
    }
}