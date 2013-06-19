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
package epl;

import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;
import site.mecard.MeCardPolicy;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class EPLMeCardPolicyTest
{
    private String meta;
    public EPLMeCardPolicyTest()
    {
        this.meta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
    }

    /**
     * Test of isResident method, of class EPLPolicy.
     */
    @Test
    public void testIsResident()
    {
        System.out.println("==isResident==");
        String custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        Customer c = new Customer(custReq);
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isResident(c, meta);
        boolean expected= true;
        assertTrue(expected == result);
        String modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|"
                + "PCEPL-VISITR|"
                + "PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
        
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isResident(c, modeMeta);
        expected= false;
        assertTrue(expected == result);
    }

    /**
     * Test of isReciprocal method, of class EPLPolicy.
     */
    @Test
    public void testIsReciprocal()
    {
        System.out.println("==isReciprocal==");
         String custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        Customer c = new Customer(custReq);
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isReciprocal(c, meta);
//        System.out.println(meta);
        boolean expected= false;
        assertTrue(expected == result);
        String modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|"
                + "PCEPL-RECIP|"
                + "PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
        
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isReciprocal(c, modeMeta);
        expected= true;
        assertTrue(expected == result);
    }

    /**
     * Test of isInGoodStanding method, of class EPLPolicy.
     */
    @Test
    public void testIsInGoodStanding()
    {
        System.out.println("==isInGoodStanding==");
        String custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        Customer c = new Customer(custReq);
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isInGoodStanding(c, meta);
        boolean expected= false;
        assertTrue(expected == result);
        String modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|"
                + "PCEPL-VISITR|"
                + "PFM|DB$0.00|DM$0.00|AFOk|AY0AZACC6";
        
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isInGoodStanding(c, modeMeta);
        expected= true;
        assertTrue(expected == result);
    }

    /**
     * Test of isMinimumAge method, of class EPLPolicy.
     */
    @Test
    public void testIsMinimumAge()
    {
        System.out.println("==isMinimumAge==");
        String custReq =
                "QC0|342abf3cb129ffccb74|21221012345678|6058|Billy, Balzac|12345 123 St.|"
                + "Edmonton|Alberta|H0H 0H0|M|ilsteam@epl.ca|7804964058|19750822|"
                + "20140602|Balzac|Billy|Y|Y|N|Y|Y|N|Balzac|Billy|";
        Customer c = new Customer(custReq);
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        boolean result = p.isMinimumAge(c, meta);
        boolean expected= true;
        assertTrue(expected == result);
        
        String modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|"
                + "PCEPL-JUV|"
                + "PFM|DB$0.00|DM$0.00|AFOk|AY0AZACC6";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isMinimumAge(c, modeMeta);
        expected= false;
        assertTrue(expected == result);
        
        modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|"
                + "PCEPL-JUV01|"
                + "PFM|DB$0.00|DM$0.00|AFOk|AY0AZACC6";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isMinimumAge(c, modeMeta);
        expected= false;
        assertTrue(expected == result);
        
        modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|"
                + "PCEPL-JUV05|"
                + "PFM|DB$0.00|DM$0.00|AFOk|AY0AZACC6";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isMinimumAge(c, modeMeta);
        expected= false;
        assertTrue(expected == result);
        
        modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|"
                + "PCEPL-JUV10|"
                + "PFM|DB$0.00|DM$0.00|AFOk|AY0AZACC6";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isMinimumAge(c, modeMeta);
        expected= false;
        assertTrue(expected == result);
        
        modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|"
                + "PCEPL-JUVNR|"
                + "PFM|DB$0.00|DM$0.00|AFOk|AY0AZACC6";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isMinimumAge(c, modeMeta);
        expected= false;
        assertTrue(expected == result);
        
        modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|"
                + "PCEPL-JUVGR|"
                + "PFM|DB$0.00|DM$0.00|AFOk|AY0AZACC6";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isMinimumAge(c, modeMeta);
        expected= false;
        assertTrue(expected == result);
        
        modeMeta = "64YYYY      Y   00020130606    115820000000000000000100000000AO|"
                + "AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|"
                + "CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|"
                + "BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|"
                + "PCEPL-JUVIND|"
                + "PFM|DB$0.00|DM$0.00|AFOk|AY0AZACC6";
        c = new Customer(custReq);
        p = MeCardPolicy.getInstanceOf(false);
        result = p.isMinimumAge(c, modeMeta);
        expected= false;
        assertTrue(expected == result);
    
    }
    
   
}