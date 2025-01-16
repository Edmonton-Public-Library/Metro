/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022 - 2025 Edmonton Public Library
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

package site.woodbuffalo;

import static org.junit.Assert.*;

import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.polaris.papi.MeCardCustomerToPapi;
import mecard.polaris.papi.MeCardDataToPapiData;
import mecard.sip.SIPToMeCardCustomer;
import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class WBRLCustomerNormalizerTest
{
    
    public WBRLCustomerNormalizerTest()
    {
    }

    /**
     * Test of normalize method, of class WBRLCustomerNormalizer.
     */
    @Test
    public void testNormalize()
    {
        System.out.println("===normalize===");
        
        WBRLCustomerNormalizer instance = new WBRLCustomerNormalizer(true);
        StringBuilder responseStringBuilder = new StringBuilder();
        
        Customer customer = new Customer();
        customer.setName("Billy");
        customer.set(CustomerFieldTypes.PIN, "12345");
        ResponseTypes response = instance.normalize(customer, responseStringBuilder);
        assertEquals(response.name(), "SUCCESS");
        System.out.println("DEBUG:" + response.toString() + "\n"
            + customer.toString());
        
        customer.set(CustomerFieldTypes.PIN, "12345a");
        response = instance.normalize(customer, responseStringBuilder);
        assertEquals(response.name(), "PIN_CHANGE_REQUIRED");
        System.out.println("DEBUG:" + response.toString() + "\n"
            + customer.toString());
    }

    /**
     * Test of finalize method, of class WBRLCustomerNormalizer.
     */
    @Test
    public void testFinalize()
    {
        System.out.println("===finalize===");
        NativeFormatToMeCardCustomer formatter = new SIPToMeCardCustomer();
        Customer unformattedCustomer = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PECALGARY|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20");
        MeCardCustomerToNativeFormat formattedCustomer = 
                new MeCardCustomerToPapi(
                        unformattedCustomer, 
                        MeCardDataToPapiData.QueryType.CREATE);
        String password = "the password";
        unformattedCustomer.set(CustomerFieldTypes.PIN, "12345a");
        
        Response response = new Response();
        WBRLCustomerNormalizer instance = new WBRLCustomerNormalizer(true);
        instance.finalize(unformattedCustomer, formattedCustomer, response);
        System.out.println("DEBUG UN-formatted customer data:\n" 
                + unformattedCustomer.toString());
        assertTrue(formattedCustomer.containsKey("USER1"));
        // In this output, if the environment.properties are set as follows:
        // <entry key="password-max-length">16</entry>
        // <entry key="password-min-length">4</entry>
        // <!-- this means only allow digits 0-9 in passwords. -->
        // <entry key="allowed-password-characters">1234567890</entry>
        // the password will be converted to a hash of 1450575502, since 
        // the customer's password contains an 'a' which is not allowed.
        System.out.println("DEBUG formatted customer data:\n" 
                + formattedCustomer.toString());
    }
    
}
