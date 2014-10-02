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
package site.mecard;

import api.CustomerMessage;
import api.SIPCustomerMessage;
import site.MeCardPolicy;
import mecard.Request;
import json.RequestDeserializer;
import mecard.customer.Customer;
import mecard.config.CustomerFieldTypes;
import mecard.util.Text;
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
        this.meta = "64YYYY      Y   00020130606    115820000000000000000100000000AO"
                + "|AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|CB0040"
                + "|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4"
                + "|BEilsteam@epl.ca|BHUSD|PA20150321    235900|PD20050303|PCEPL-THREE"
                + "|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
        String custReq =
                "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"\",\"pin\":\"\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        this.c = request.getCustomer();
    }

    /**
     * Test of isGoodStanding method, of class MeCardPolicy.
     */
    @Test
    public void testIsGoodStanding()
    {
        String custReq =
                "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"\",\"pin\":\"\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        Customer c = request.getCustomer();
        System.out.println("==isGoodStanding==");
        StringBuilder sb = new StringBuilder();
        MeCardPolicy policy = MeCardPolicy.getInstanceOf(false);
//        String msg = "64              00020131119    150500000000000000000000000000AOalap|AA21000005874370|AEME card, testone|AQalap|BZ0249|CA0001|CB0200|BLY|BHCAD|CC10.|BD123 Somewhere St, Lacombe, AB, T4L 1G1|BEtest@prl.ab.ca|DHtestone|DJME card|PCsus|PE20141113    235900|PS20141113    235900|ZYsus|AY1AZB304";
        String msg = "64              00020131119    150500000000000000000000000000AOalap|AA21000005874370|AEME card, testone|AQalap|BZ0249|CA0001|CB0200|BLY|BHCAD|CC10.|BD123 Somewhere St, Lacombe, AB, T4L 1G1|BEtest@prl.ab.ca|DHtestone|DJME card|PCsus|PE20141113    235900|PS20141113    235900|ZYsus|AFPatron has blocks.|AY1AZB304";
        CustomerMessage customerMessage = new SIPCustomerMessage(msg);
        boolean result = policy.isInGoodStanding(c, customerMessage, sb);
        boolean expected= false;
        assertTrue(expected == result);
    }
    
    /**
     * Test of isMinimumAgeByDate method, of class MeCardPolicy.
     */
    @Test
    public void testIsMinimumAgeByDate()
    {
        System.out.println("==isMinimumAge by Date String==");
        StringBuilder sb = new StringBuilder();
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        CustomerMessage customerMessage = new SIPCustomerMessage(meta);
        boolean result = p.isMinimumAgeByDate(c, customerMessage, sb);
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
        customerMessage = new SIPCustomerMessage(modeMeta);
        result = p.isMinimumAgeByDate(c, customerMessage, sb);
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

        StringBuilder sb = new StringBuilder();
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        CustomerMessage customerMessage = new SIPCustomerMessage(this.meta);
        boolean result = p.isEmailable(c, customerMessage, sb);
        assertTrue(true == result);
        
        c.set(CustomerFieldTypes.EMAIL, "X");
        result = p.isEmailable(c, customerMessage, sb);
        
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
        StringBuilder sb = new StringBuilder();
        boolean result = p.isValidCustomerData(c, sb);
        boolean expected= true;
        assertTrue(expected == result);

        c.set(CustomerFieldTypes.POSTALCODE, "X");
        result = p.isValidCustomerData(c, sb);
        assertTrue(result == false);
    }

    /**
     * Test of isValidExpiryDate method, of class MeCardPolicy.
     */
    @Test
    public void testIsValidExpiryDate()
    {
        System.out.println("==isValidExpiryDate==");
        StringBuilder sb = new StringBuilder();
        MeCardPolicy p = MeCardPolicy.getInstanceOf(false);
        CustomerMessage customerMessage = new SIPCustomerMessage(this.meta);
        System.out.println("VALID EXPIRY???: '" +customerMessage.getField("PA")+ "'");
        assertFalse(p.isValidExpiryDate(c, customerMessage, sb));
        System.out.println("VALID EXPIRY???: '" +customerMessage.getDateField("PA")+ "'");
        String newDate = customerMessage.getDateField("PA");
        c.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, newDate);
        assertTrue(p.isValidExpiryDate(c, customerMessage, sb));
        
        System.out.println("VALID EXPIRY???: '" +customerMessage.getDateField("BX")+ "'");
        newDate = customerMessage.getDateField("BX");
        c.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, newDate);
        assertFalse(p.isValidExpiryDate(c, customerMessage, sb));
    }
    
    /**
     * Test of isValidExpiryDate method, of class MeCardPolicy.
     */
    @Test
    public void testProperCase()
    {
        System.out.println("==setProperCase==");
        // TODO Set up tests.
        String custData = "initial string";
        String result   = Text.toDisplayCase(custData);
        String expected = "Initial String";
        assertTrue(expected.compareTo(result) == 0);
        
        custData = "Initial String";
        result   = Text.toDisplayCase(custData);
        expected = "Initial String";
        assertTrue(expected.compareTo(result) == 0);
        
        custData = " initial String";
        result   = Text.toDisplayCase(custData);
        expected = " Initial String";
        assertTrue(expected.compareTo(result) == 0);
        
        custData = "";
        result   = Text.toDisplayCase(custData);
        expected = "";
        assertTrue(expected.compareTo(result) == 0);
        
        custData = "mark-antony";
        result   = Text.toDisplayCase(custData);
        expected = "Mark-Antony";
        assertTrue(expected.compareTo(result) == 0);
        
        custData = "ALL CAPS";
        result   = Text.toDisplayCase(custData);
        expected = "All Caps";
        assertTrue(expected.compareTo(result) == 0);
        
        this.c.set(CustomerFieldTypes.FIRSTNAME, "BALZAC");
        System.out.println("NO_NORM:" + this.c.toString());
        MeCardPolicy policy = MeCardPolicy.getInstanceOf(true);
        policy.normalizeCustomerFields(this.c);
        System.out.println("NORM:" + this.c.toString());
        
        this.c.set(CustomerFieldTypes.LASTNAME, "BILLY");
        System.out.println("NO_NORM:" + this.c.toString());
        policy.normalizeCustomerFields(this.c);
        System.out.println("NORM:" + this.c.toString());
        
        this.c.set(CustomerFieldTypes.STREET, "123 ADA LACE LANE");
        System.out.println("NO_NORM:" + this.c.toString());
        policy.normalizeCustomerFields(this.c);
        System.out.println("NORM:" + this.c.toString());
        
        this.c.set(CustomerFieldTypes.PREFEREDNAME, "BILLY, BALZAC");
        System.out.println("NO_NORM:" + this.c.toString());
        policy.normalizeCustomerFields(this.c);
        System.out.println("NORM:" + this.c.toString());
    }
}