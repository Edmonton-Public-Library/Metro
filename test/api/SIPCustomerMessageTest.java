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

import mecard.config.CustomerFieldTypes;
import mecard.util.Address2;
import mecard.util.Phone;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test tests the responses based on SIP2 data collected during testing
 * of various customer accounts. The idea is once a library sends you their
 * sip port use the perl sip2emu.pl to test connection and collect data for
 * good and bad standing customers. Once done you can use this class to test 
 * that data and make sure the metro server will recieve and be able to make 
 * use of what you get back.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */


public class SIPCustomerMessageTest
{
    private final String goodStanding;
    private final String juvenile;
    private final String suspended;
    private final String nonResident;
    private final String expired;
    
    public SIPCustomerMessageTest()
    {
        this.goodStanding = "64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20";
        this.suspended    = "64YYYY          00020140110    161521000000000000000100000000AO|AA25021000719309|AEJackson, Jonathan|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 5.00|BD31 Chansellorsville Street apt. 3 T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19740121|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFUser BARRED|AY2AZA683";
        this.juvenile     = "64              00020140110    161942000000000000000000000000AO|AA25021000719333|AEMcClellan, George|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD34 Potomac Ave., apt. 45 T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD20051203|PCSGMEDCH|PEMEDICINEHA|PFCHILD|PGMALE|DB$0.00|DM$500.00|AFOK|AY5AZAEDA";
        this.nonResident  = "64              00020140110    161812000000000000000000000000AO|AA25021000719325|AEGrant, Hiram Ulysses|AQSGMED|BZ0050|CA0100|CB0999|BLY|CQY|BV 0.00|BD11 Shiloh Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19690304|PCSGMEDNRI|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY4AZAE9E";
        this.expired      = "64YYYY          00020140110    161646000000000000000100000000AO|AA25021000719317|AELee, Robert Edward|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 5.00|BD18 Appomattox Street, apt. 9 T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20120409    235900|PD19640119|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFYou membership has expired. To renew your membership, please contact your library.|AY3AZ8D93";
    }

    /**
     * Test of getCustomerProfile method, of class SIPCustomerMessage.
     */
    @Test
    public void testGetCustomerProfile()
    {
        System.out.println("===getCustomerProfile===");
        SIPCustomerMessage sipMessage = new SIPCustomerMessage(goodStanding);
        System.out.println("GOOD:>>"+sipMessage + "<<");
        this.printContents(sipMessage);
        
        
        sipMessage = new SIPCustomerMessage(suspended);
        System.out.println("suspended:>>"+sipMessage + "<<");
        this.printContents(sipMessage);
       
        
        sipMessage = new SIPCustomerMessage(juvenile);
        System.out.println("juvenile:>>"+sipMessage + "<<");
        this.printContents(sipMessage);
        
        
        sipMessage = new SIPCustomerMessage(nonResident);
        System.out.println("nonResident:>>"+sipMessage + "<<");
        this.printContents(sipMessage);
        
        
        sipMessage = new SIPCustomerMessage(expired);
        System.out.println("expired:>>"+sipMessage + "<<");
        this.printContents(sipMessage);
        
    }
    
    private void printContents(SIPMessage sipMessage)
    {
        System.out.println(CustomerFieldTypes.ID + ":" + sipMessage.getField("AA"));
        System.out.println(CustomerFieldTypes.PREFEREDNAME + ":" + sipMessage.getField("AE"));
        System.out.println(CustomerFieldTypes.RESERVED + ":" + sipMessage.getField("AF"));
        System.out.println(CustomerFieldTypes.EMAIL + ":" +  sipMessage.getField("BE"));
        // Phone object
        Phone phone = new Phone(sipMessage.getField("BF"));
        System.out.println(CustomerFieldTypes.PHONE + ":" + phone.getUnformattedPhone());
        // Privilege date dates: horizon uses PE and puts profile in PA.
        // The test is slightly less expensive 
        String cleanDate = SIPMessage.cleanDateTime(sipMessage.getField("PA"));
        if (SIPMessage.isDate(cleanDate))
        {
            System.out.println(CustomerFieldTypes.PRIVILEGE_EXPIRES + ":" + cleanDate);
        }
        // PE can also be privilege expires, so if PA fails try this field.
        else
        {
            cleanDate = SIPMessage.cleanDateTime(sipMessage.getField("PE"));
            System.out.println(CustomerFieldTypes.PRIVILEGE_EXPIRES + ":" + cleanDate);
        }
        // DOB same thing
        cleanDate = SIPMessage.cleanDateTime(sipMessage.getField("PB"));
        if (SIPMessage.isDate(cleanDate))
        {
            System.out.println(CustomerFieldTypes.DOB + ":" + SIPMessage.cleanDateTime(sipMessage.getField("PB"))); // Strathcona.
        }
        else
        {
            cleanDate = SIPMessage.cleanDateTime(sipMessage.getField("PD"));
            System.out.println(CustomerFieldTypes.DOB + ":" + cleanDate);
        }
        System.out.println(CustomerFieldTypes.SEX + ":" + sipMessage.getField("PF"));
        // Complete address
        System.out.println("===ADDRESS===\nShould be in field BD>>"
                + sipMessage.getField("BD") + "<<");
        Address2 address = new Address2(sipMessage.getField("BD"));
        System.out.println(CustomerFieldTypes.STREET + ":" + address.getStreet());
        System.out.println(CustomerFieldTypes.CITY + ":" + address.getCity());
        System.out.println(CustomerFieldTypes.PROVINCE + ":" + address.getProvince());
        System.out.println(CustomerFieldTypes.POSTALCODE + ":" + address.getPostalCode());
        // Next careful, EPL gloms the phone on the end of the address, but if a lib returns
        // the phone in the correct field parsing this will erase the phone we already
        // collected.
        System.out.println(CustomerFieldTypes.PHONE + ":" + address.getPhone());
        System.out.println("===ADDRESS===\n\n");
    }
}
