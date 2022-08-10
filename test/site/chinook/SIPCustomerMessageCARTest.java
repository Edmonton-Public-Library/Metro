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
package site.chinook;

import mecard.sip.SIPMessage;
import mecard.sip.SIPCustomerMessage;
import api.*;
import mecard.config.CustomerFieldTypes;
import mecard.util.Address3;
import mecard.util.DateComparer;
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


public class SIPCustomerMessageCARTest
{
    private final String goodStanding;
    private final String juvenile;
    private final String suspended;
    private final String nonResident;
    private final String expired;
    private final String lost;
    
    public SIPCustomerMessageCARTest()
    {
//        Charge Privileges Denied :          Y
//        Renewal Privileges Denied :         Y
//        Recall Privileges Denied :          Y
//        Hold Privileges Denied:             Y
//        Card Reported Lost :                Y
//        Too Many Items Charged :             
//        Too many Items Overdue :             
//        Too Many Renewals :                  
//        Too Many Claims Of Items Returned :  
//        Too Many Items Lost :                
//        Excessive Outstanding Fines :        
//        Excessive Outstanding Fees :         
//        Recall Overdue :                     
//        Too Many Items Billed :
        // these are returned from CAR (ChinookArch).
        this.goodStanding = "64              00020140304    070512000000000000000000000000AO|AA21817002446849|AEGAMACHE, ARMAND|AQLPL|BZ9999|CA9999|CB9999|BLY|CQY|BV 0.00|BDBOX 43 LETHBRIDGE, ALBERTA T1J 3Y3 403-555-1234|BEpwauters@hotmail.com|BF403-555-1234|BHUSD|PA20150218    235900|PD|PCLPLADULT|PELETHCITY|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY0AZACA0";
        this.suspended    = "64YYYY          00020140304    152635000000000000000000000000AO|AA21817002446906|AEZARDO, RUTH|AQCARD|BZ9999|CA0030|CB9999|BLY|CQY|BV 0.00|BD78 WOODBINE ROAD CARDSTON, ALBERTA T1X 3Y3 403-555-2345|BEpwauters@hotmail.com|BF403-555-2345|BHUSD|PA20150218    235900|PD|PCCARDSENIOR|PECARDTOWN|PFADULT|PGFEMALE|DB$0.00|DM$500.00|AFUser BARRED|AY0AZA646";
        this.juvenile     = "64              00020140304    152748000000000000000000000000AO|AA21817002447029|AELACOSTE, ISABELLE|AQMAG|BZ9999|CA9999|CB9999|BLY|CQY|BV 0.00|BD12 STREET MAGRATH, ALBERTA T5J 3Y3 403-555-4567|BEpwauters@hotmail.com|BF403-555-4567|BHUSD|PA20150218    235900|PD|PCMAGJUV|PEMAGRATH|PFJUVENILE|PGFEMALE|DB$0.00|DM$500.00|AFOK|AY1AZAB53";
        this.nonResident  = "64              00020140304    152856000000000000000000000000AO|AA21817002447086|AEMORROW, CLARA|AQTAB|BZ9999|CA0030|CB9999|BLY|CQY|BV 0.00|BD6 STREET BARONS, ALBERTA T5J 3Y3 403-555-4567|BEpwauters@hotmail.com|BF403-555-4567|BHUSD|PA20150219    235900|PD|PCTABLOCNR|PEBARONS|PFADULT|PGFEMALE|DB$0.00|DM$500.00|AFOK|AY2AZAD78";
        this.expired      = "64YYYY          00020140304    152954000000000000000000000000AO|AA21817002446963|AEBEAUVOIR, JEAN-GUY|AQGRAN|BZ9999|CA0030|CB9999|BLY|CQY|BV 0.00|BD56 POTTER AVENUE GRANUM, ALBERTA T1X 3Y3 403-555-3456|BEpwauters@hotmail.com|BF403-555-3456|BHUSD|PA20140201    235900|PD|PCGRANADULT|PEGRANUM|PFADULT|PGMALE|DB$0.00|DM$500.00|AFPrivilege has expired|AY3AZA18E";
        this.lost         = "64YYYYY         00020140304    153041000000000000000100000000AO|AA21817002446781|AEBRULE, OLIVIER|AQKAI|BZ0000|CA0000|CB0000|BLN|CQY|BV 50.00|BD6 STREET BLOOD RESERVE, ALBERTA T5J 3Y3 403-555-4567|BEpwauters@hotmail.com|BF403-555-4567|BHUSD|PA20150219    235900|PD|PCLOSTCARD|PEBLOODTRIBE|PFADULT|PGMALE|DB$0.00|DM$500.00|AFUser card marked LOST|AY4AZA303";
        
        
        
//        recv:64              00020140304    070512000000000000000000000000AO|AA21817002446849|AEGAMACHE, ARMAND|AQLPL|BZ9999|CA9999|CB9999|BLY|CQY|BV 0.00|BDBOX 43 LETHBRIDGE, ALBERTA T1J 3Y3 403-555-1234|BEpwauters@hotmail.com|BF403-555-1234|BHUSD|PA20150218    235900|PD|PCLPLADULT|PELETHCITY|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY0AZACA0
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
        String cleanDate = DateComparer.cleanAnsiDateTime(sipMessage.getField("PA"));
        if (DateComparer.isAnsiDate(cleanDate))
        {
            System.out.println(CustomerFieldTypes.PRIVILEGE_EXPIRES + ":" + cleanDate);
        }
        // PE can also be privilege expires, so if PA fails try this field.
        else
        {
            cleanDate = DateComparer.cleanAnsiDateTime(sipMessage.getField("PE"));
            System.out.println(CustomerFieldTypes.PRIVILEGE_EXPIRES + ":" + cleanDate);
        }
        // DOB same thing
        cleanDate = DateComparer.cleanAnsiDateTime(sipMessage.getField("PB"));
        if (DateComparer.isAnsiDate(cleanDate))
        {
            System.out.println(CustomerFieldTypes.DOB + ":" + DateComparer.cleanAnsiDateTime(sipMessage.getField("PB"))); // Strathcona.
        }
        else
        {
            cleanDate = DateComparer.cleanAnsiDateTime(sipMessage.getField("PD"));
            System.out.println(CustomerFieldTypes.DOB + ":" + cleanDate);
        }
        System.out.println(CustomerFieldTypes.SEX + ":" + sipMessage.getField("PF"));
        // Complete address
        System.out.println("===ADDRESS===\nShould be in field BD>>"
                + sipMessage.getField("BD") + "<<");
        Address3 address = new Address3(sipMessage.getField("BD"));
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

    /**
     * Test of getMessage method, of class SIPCustomerMessage.
     */
    @Test
    public void testGetMessage()
    {
        System.out.println("==getMessage==");
        SIPCustomerMessage sipMessage = new SIPCustomerMessage(this.goodStanding);
        assertFalse(sipMessage.isReported(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
        sipMessage = new SIPCustomerMessage(this.lost);
        assertTrue(sipMessage.isReported(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
        assertTrue(sipMessage.isTrue(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
        assertFalse(sipMessage.isFalse(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
    }

    /**
     * Test of getStanding method, of class SIPCustomerMessage.
     */
    @Test
    public void testGetStanding()
    {
        System.out.println("==getStanding==");
        SIPCustomerMessage sipMessage = new SIPCustomerMessage(this.goodStanding);
        assertFalse(sipMessage.cardReportedLost());
    }

    /**
     * Test of cardReportedLost method, of class SIPCustomerMessage.
     */
    @Test
    public void testCardReportedLost()
    {
        System.out.println("==cardReportedLost==");
        SIPCustomerMessage sipMessage = new SIPCustomerMessage(this.goodStanding);
        assertFalse(sipMessage.cardReportedLost());
        sipMessage = new SIPCustomerMessage(this.lost);
        assertTrue(sipMessage.cardReportedLost());
    }
}
