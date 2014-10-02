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
package site.reddeer;

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


public class SIPCustomerMessageRDPLTest
{
    private final String goodStanding;
    private final String juvenile;
    private final String suspended;
    private final String nonResident;
    private final String expired;
    private final String lost;
    
    public SIPCustomerMessageRDPLTest()
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
        this.goodStanding = "64              00020140624    153614000000000000000000000000AO|AA00000003161957|AEBibliocommons, Adult1 ***TEST ACCT***|AQDOWNTOWN|BZ0025|CA0050|CB0050|BLY|CQY|BV0.00|BD353 Primary Way Red Deer AB T4N 1T9|BEitsg@rdpl.org|BF403-555-0178|BHUSD|PA20150311    235900|PD|PCPRIMARY|DB$0.00|DM$0.00|AFOK|AY1AZB0A0";
        this.suspended    = "64Y             00020140526    160200000000000000000100000000AOmain|AA25555000907570|AEadmin2 test|AQmain|BZ0025|CA0026|CB0099|BLY|CQY|BHCAN|BV1.|CC15.|BD151 MacDonald Dr., Fort McMurray, AB, T9H 5C5|BEcarlos.moran@fmpl.ca|BF780-743-7800|DJadmin2 test|LGeng|PCa|PE20150219    235900|PS20150219    235900|ZYa|AF#You are barred from borrowing - Please refer to the circulation desk.|AY0AZ90EC";
        this.juvenile     = "64              00020140624    154217000000000000000000000000AO|AA00000001720523|AEBibliocommons, Child ***TEST ACCT***|AQDOWNTOWN|BZ0025|CA0050|CB0050|BLY|CQY|BV0.00|BD352 Child Road Red Deer AB T4N 1T9|BEitsg@rdpl.org|BF403-555-0123|BHUSD|PA20150311    235900|PD|PCCHILD|DB$0.00|DM$0.00|AFOK|AY2AZB268";
        this.nonResident  = "64              00020140624    154542000000000000000000000000AO|AA00000003171261|AEBibliocommons, NRPrimary ***TEST ACCT***|AQDOWNTOWN|BZ0025|CA0050|CB0050|BLY|CQY|BV0.00|BD26 NRPrimary Street Innisfail AB T4G 1E8|BEitsg@rdpl.org|BF403-444-4482|BHUSD|PA20150311    235900|PD|PCNRPRIMARY|DB$0.00|DM$0.00|AFOK|AY4AZAC31";
        this.expired      = "64YYYY          00020140624    154356000000000000000100000000AO|AA00000003161890|AEBibliocommons, Expired ***TEST ACCT***|AQDOWNTOWN|BZ0025|CA0050|CB0050|BLY|CQY|BV1.75|BD352 Expired Avenue Red Deer AB T4R 1T9|BEitsg@rdpl.org|BF403-555-0126|BHUSD|PA20140311    235900|PD|PCTEEN|DB$0.00|DM$0.00|AFPrivilege has expired|AY3AZA769";
        this.lost         = "";
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
        String cleanDate = DateComparer.cleanDateTime(sipMessage.getField("PA"));
        if (DateComparer.isDate(cleanDate))
        {
            System.out.println(CustomerFieldTypes.PRIVILEGE_EXPIRES + ":" + cleanDate);
        }
        // PE can also be privilege expires, so if PA fails try this field.
        else
        {
            cleanDate = DateComparer.cleanDateTime(sipMessage.getField("PE"));
            System.out.println(CustomerFieldTypes.PRIVILEGE_EXPIRES + ":" + cleanDate);
        }
        // DOB same thing
        cleanDate = DateComparer.cleanDateTime(sipMessage.getField("PB"));
        if (DateComparer.isDate(cleanDate))
        {
            System.out.println(CustomerFieldTypes.DOB + ":" + DateComparer.cleanDateTime(sipMessage.getField("PB"))); // Strathcona.
        }
        else
        {
            cleanDate = DateComparer.cleanDateTime(sipMessage.getField("PD"));
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
//        sipMessage = new SIPCustomerMessage(this.lost);
//        assertTrue(sipMessage.isReported(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
//        assertTrue(sipMessage.isTrue(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
//        assertFalse(sipMessage.isFalse(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
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
//        sipMessage = new SIPCustomerMessage(this.lost);
//        assertTrue(sipMessage.cardReportedLost());
    }
}
