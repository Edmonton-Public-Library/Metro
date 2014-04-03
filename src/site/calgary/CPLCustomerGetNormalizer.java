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
package site.calgary;

import api.CustomerMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import mecard.Protocol;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.util.DateComparer;
import site.CustomerGetNormalizer;
import site.MeCardPolicy;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class CPLCustomerGetNormalizer extends CustomerGetNormalizer
{
    /**
     * This is the default class for a library that does not require custom
     * interpretation of {@link CustomerMessage}s.
     * @param customer
     * @param message 
     */
    @Override
    public void setCustomerValuesFromSiteSpecificFields(
            Customer customer, 
            CustomerMessage message)
    {
                // parse the string appart.
//    sent:63                               AO|AA55544466677788|AD1234|AY0AZF38B
//    recv:64              00020140328    130720000000000000000000000000AO|AA55544466677788|AEkelso, test adult|AQCENT|BZ0050|CA0000|CB0099|BLY|CQY|BV0.00|BD123 sad street nw CALGARY, AB t3g 1m1|BE21@test.ca|BF403-123-4567|BHUSD|PA20141008    235900|PD|PCADULT|PEAR|DB$0.00|DM$500.00|AFOK|AY0AZB6B0
//
//    Response code:64
//    Patron Information Response
//      (O) Patron Status :               
//            Charge Privileges Denied :           
//            Renewal Privileges Denied :          
//            Recall Privileges Denied :           
//            Hold Privileges Denied:              
//            Card Reported Lost :                 
//            Too Many Items Charged :             
//            Too many Items Overdue :             
//            Too Many Renewals :                  
//            Too Many Claims Of Items Returned :  
//            Too Many Items Lost :                
//            Excessive Outstanding Fines :        
//            Excessive Outstanding Fees :         
//            Recall Overdue :                     
//            Too Many Items Billed :              
//      (F) Language:                      000
//      (F) Transaction Date:              20140328    130720
//      (F) Hold Items Count:              0000
//      (F) Overdue Items Count:           0000
//      (F) Charged Items Count:           0000
//      (F) Fine Items Count:              0000
//      (F) Recall Items Count:            0000
//      (F) Unavailable Hold Items Count:  0000
//      (R) Institution Id:
//      (R) Patron Identifier:55544466677788
//      (R) Personal Name:kelso, test adult
//      (R) Patron Library:CENT
//      (O) Hold Items Limit:0050:
//      (O) Overdue Items Limit:0000:
//      (O) Charged Items Limit:0099:
//      (O) Valid Patron:Y:
//      (O) Valid Patron Password:Y:
//      (O) Fee Amount:0.00
//      (O) Home Address:123 sad street nw CALGARY, AB t3g 1m1
//      (O) E-mail Address:21@test.ca
//      (O) Home Phone Number:403-123-4567
//      (O) Currency Type:USD:
//      (O) Expiration Date:20141008    235900
//      (O) Birth date:
//      (O) Profile:ADULT
//      (O) User Category 1:AR
//      (O) DB INVALID DATA CODE FOR THIS MESSAGE:$0.00
//      (O) DM INVALID DATA CODE FOR THIS MESSAGE:$500.00
//      (O) Screen Message:OK
//      (R) Sequence Number : 0 :  matches what was sent
//      (R) Checksum : B6B0 : Checksum OK
        // here we will fill in the customer attributes with the contents of s- the SIP message.
        
//        ANR	Adult Non-resident
//        ATX	Adult Taxpayer
//        ATXB	Adult Business Taxpayer
//        CNR	Child Non-resident
//        CTX	Child Taxpayer
//        CTXB	Child Business Taxpayer
//        ONR	Organization Non-resident
//        OUTNR	Outreach Non-resident
//        SNR	Senior Non-resident
//        STNR	Staff Non-resident
//        STX	Senior Taxpayer
//        STXB	Senior Business Taxpayer
//        YNR	Young Adult Non-resident
//        YTX	Young Adult Taxpayer
//        YTXB	Ya Business Taxpayer
        // CPL uses profiles (checked in MeCardPolicy by default AND
        // CATAGORY1. Here we do additional tests for USER_CAT_1.
        String customerType = message.getField("PE");
        List<String> nonResidentTypes = new ArrayList<>();
        List<String> reciprocalTypes  = new ArrayList<>();
        List<String> juvenileTypes    = new ArrayList<>();
        Properties props     = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        // TODO: If we find a reciprocal.properties, or non_resident.properties, or juvenile.properties
        // load the types for testing.
        // read optional fields from environment. Should be ',' separated.
        // <entry key="reciprocal">EPL-RECIP</entry>
        // <entry key="non-resident">NON-RES</entry>
        // <entry key="juvenile">re,sp, stu, stu10, stu2, stu3</entry>
        PropertyReader.loadDelimitedEntry(props, LibraryPropertyTypes.NON_RESIDENT_TYPES, nonResidentTypes);
        PropertyReader.loadDelimitedEntry(props, LibraryPropertyTypes.RECIPROCAL_TYPES, reciprocalTypes);
        PropertyReader.loadDelimitedEntry(props, LibraryPropertyTypes.JUVENILE_TYPES, juvenileTypes);
        
        // Non-residents
        for (String str: nonResidentTypes)
        {
            if (customerType.compareTo(str) == 0) // Test fails customer.
            {
                customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.FALSE);
            }
        }
        // Reciprocals
        for (String str: reciprocalTypes)
        {
            if (customerType.compareTo(str) == 0) // Test fails customer.
            {
                customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.TRUE);
            }
        }
        // Juv
        for (String str: juvenileTypes)
        {
            if (customerType.compareTo(str) == 0) // Test fails customer.
            {
                customer.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
            }
        }
        
        // Calgary' SIP server doesn't return a 'PA', expiry date field for lifetime
        // memberships. Fix to something reasonable.
        if (message.isEmpty("PA"))
        {
            String expiry = DateComparer.getFutureDate(MeCardPolicy.MAXIMUM_EXPIRY_DAYS);
            customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, expiry);
        }
        
        // TODO CPL does not use LOSTCARD wizard for lost cards. What field do they use??
    }
}
