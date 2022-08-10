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
package site.edmonton;

import mecard.Response;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.symphony.MeCardCustomerToFlat;
import mecard.sip.SIPToMeCardCustomer;
import org.junit.Test;
import static org.junit.Assert.*;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.NativeFormatToMeCardCustomer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */


public class EPLCustomerNormalizerTest
{
    
    public EPLCustomerNormalizerTest()
    {
    }

    /**
     * Test of finalize method, of class EPLCustomerNormalizer.
     */
    @Test
    public void testFinalize()
    {
        System.out.println("===finalize===");
        
        
        
        NativeFormatToMeCardCustomer formatter = new SIPToMeCardCustomer();
        Customer unformattedCustomer = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20");
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToFlat(unformattedCustomer);
        Response response = new Response();
        EPLCustomerNormalizer instance = new EPLCustomerNormalizer(true);
        instance.finalize(unformattedCustomer, formattedCustomer, response);
        assertTrue(formattedCustomer.containsKey("USER_CATEGORY2"));
    }
    
}
