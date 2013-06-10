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

import mecard.customer.Customer;
import mecard.customer.CustomerFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;


    /**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class MeCardPolicyTest
{
    private final String meta;
    
    public MeCardPolicyTest()
    {
        this.meta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
    }

    /**
     * Test of isMinimumAgeByDate method, of class MeCardPolicy.
     */
    @Test
    public void testIsMinimumAgeByDate()
    {
        System.out.println("==isMinimumAge by Date String==");
        String custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        Customer c = new Customer(custReq);
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isMinimumAgeByDate(c, meta);
//        System.out.println("C's Age is:"+c.get(CustomerFieldTypes.DOB));
        boolean expected= true;
        assertTrue(expected == result);
        
        custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|20050101|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        String modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|"
                + "BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|" // DOB
                + "PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isMinimumAgeByDate(c, modeMeta);
//        System.out.println("C's Age is:"+c.get(CustomerFieldTypes.DOB));
        expected= false;
        assertTrue(expected == result);
    }
    
/**
     * Test of isEmailable method, of class MeCardPolicy.
     */
    @Test
    public void testIsEmailable()
    {
        System.out.println("==isEmailable==");
        String custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        Customer c = new Customer(custReq);
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isEmailable(c, meta);
        boolean expected= true;
        assertTrue(expected == result);
        
        custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|X|7804964058|19750822|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isEmailable(c, meta);
        expected= false;
        assertTrue(expected == result);
    }

    /**
     * Test of isValidCustomerData method, of class MeCardPolicy.
     */
    @Test
    public void testIsValidCustomerData()
    {
        System.out.println("==isValidCustomerData==");
        String custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        Customer c = new Customer(custReq);
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isValidCustomerData(c);
        boolean expected= true;
        assertTrue(expected == result);
        
        custReq =
                "QC0|342abf3cb129ffccb74|X|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        c = new Customer(custReq);
        result = p.isValidCustomerData(c);
        expected= false;
        assertTrue(expected == result);
    }

    /**
     * Test of isValidExpiryDate method, of class MeCardPolicy.
     */
    @Test
    public void testIsValidExpiryDate()
    {
        System.out.println("==isValidExpiryDate==");
        String custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        Customer c = new Customer(custReq);
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isValidExpiryDate(c, meta);
        boolean expected= true;
        assertTrue(expected == result);
        
        custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|X|7804964058|19750822|"
                + "19140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isValidExpiryDate(c, meta);
        expected= false;
        assertTrue(expected == result);
        
        custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|X|7804964058|19750822|"
                + "x|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isValidExpiryDate(c, meta);
        expected= false;
        assertTrue(expected == result);
        
        custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|X|7804964058|19750822|"
                + "0|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isValidExpiryDate(c, meta);
        expected= false;
        assertTrue(expected == result);
    }

//    public class MeCardPolicyImpl extends MeCardPolicy
//    {
//
//        public boolean isResident(Customer customer, String meta)
//        {
//            return false;
//        }
//
//        public boolean isReciprocal(Customer customer, String meta)
//        {
//            return false;
//        }
//
//        public boolean isInGoodStanding(Customer customer, String meta)
//        {
//            return false;
//        }
//
//        public boolean isMinimumAge(Customer customer, String meta)
//        {
//            return false;
//        }
//    }
}