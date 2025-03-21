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

import api.CustomerMessage;
import java.util.Properties;
import mecard.Protocol;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import mecard.config.SupportedProtocolTypes;
import mecard.customer.Customer;
import mecard.util.Address3;
import mecard.util.DateComparer;
import site.CustomerGetNormalizer;
import site.MeCardPolicy;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class EPLCustomerGetNormalizer extends CustomerGetNormalizer
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
        Properties envProperties = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        if (envProperties.getProperty(LibraryPropertyTypes.GET_SERVICE.toString())
                .equalsIgnoreCase(SupportedProtocolTypes.SIP2.toString()))
        {
                // parse the string appart.
    //        sent:63                               AO|AA21221012345678|AD64058|AY0AZF374
    //        recv:64YYYY      Y   00020130606    115820000000000000000100000000AO|AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6
    //
    //        Response code:64
    //        Patron Information Response
    //          (O) Patron Status : YYYY      Y   
    //                Charge Privileges Denied :          Y
    //                Renewal Privileges Denied :         Y
    //                Recall Privileges Denied :          Y
    //                Hold Privileges Denied:             Y
    //                Card Reported Lost :                 
    //                Too Many Items Charged :             
    //                Too many Items Overdue :             
    //                Too Many Renewals :                  
    //                Too Many Claims Of Items Returned :  
    //                Too Many Items Lost :                
    //                Excessive Outstanding Fines :       Y
    //                Excessive Outstanding Fees :         
    //                Recall Overdue :                     
    //                Too Many Items Billed :              
    //          (F) Language:                      000
    //          (F) Transaction Date:              20130606    115820
    //          (F) Hold Items Count:              0000
    //          (F) Overdue Items Count:           0000
    //          (F) Charged Items Count:           0000
    //          (F) Fine Items Count:              0001
    //          (F) Recall Items Count:            0000
    //          (F) Unavailable Hold Items Count:  0000
    //          (R) Institution Id:
    //          (R) Patron Identifier:21221012345678
    //          (R) Personal Name:Billy, Balzac
    //          (R) Patron Library:EPLMNA
    //          (O) Hold Items Limit:0025:
    //          (O) Overdue Items Limit:0041:
    //          (O) Charged Items Limit:0040:
    //          (O) Valid Patron:Y:
    //          (O) Valid Patron Password:Y:
    //          (O) Fee Amount: 12.00
    //          (O) Home Address:7 Sir Winston Churchill Square Edmonton, AB T5J 2V4
    //          (O) E-mail Address:ilsteam@epl.ca
    //          (O) Currency Type:USD:
    //          (O) Expiration Date:20140321    235900
    //          (O) Birth date:20050303
    //          (O) Profile:EPL-THREE
    //          (O) User Category 2:M
    //          (O) DB INVALID DATA CODE FOR THIS MESSAGE:$0.00
    //          (O) DM INVALID DATA CODE FOR THIS MESSAGE:$0.00
    //          (O) Screen Message:User BLOCKED
    //          (R) Sequence Number : 0 :  matches what was sent
    //          (R) Checksum : ACC6 : Checksum OK
            // here we will fill in the customer attributes with the contents of s- the SIP message.
            customer.set(CustomerFieldTypes.SEX, message.getField("PF"));
            // Complete address
            System.out.println("===ADDRESS===\nShould be in field BD>>"
                    + message.getField("BD") + "<<");
            Address3 address = new Address3(message.getField("BD"));
            System.out.println(CustomerFieldTypes.STREET + ":" + address.getStreet());
            System.out.println(CustomerFieldTypes.CITY + ":" + address.getCity());
            System.out.println(CustomerFieldTypes.PROVINCE + ":" + address.getProvince());
            System.out.println(CustomerFieldTypes.POSTALCODE + ":" + address.getPostalCode());
            // Next careful, EPL gloms the phone on the end of the address, but if a lib returns
            // the phone in the correct field parsing this will erase the phone we already
            // collected.
            System.out.println(CustomerFieldTypes.PHONE + ":" + address.getPhone());
            System.out.println("===ADDRESS===\n\n");

            // Since EPL has moved all their patrons to no expiry date, the SIP2
            // server has stopped sending the 'PA' field of patron expiry. Now we 
            // haven't heard of another library in the federation that has this policy
            // but we will take care of that here and now.
            // Calgary' SIP server doesn't return a 'PA', expiry date field for lifetime
            // memberships either. Let's fix our customer to something reasonable.
            if (message.isEmpty("PA"))
            {
                String expiry = DateComparer.getFutureDate(MeCardPolicy.MAXIMUM_EXPIRY_DAYS);
                customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, expiry);
            }
        }
        else if (envProperties.getProperty(LibraryPropertyTypes.GET_SERVICE.toString())
                .equalsIgnoreCase(SupportedProtocolTypes.SIRSIDYNIX_API.toString()))
        {
//            String gender = message.getField(SDapiUserFields.CATEGORY02.toString());
//            // EPL uses either 'M', 'F', 'N', or 'X'.
//            if (gender.contains("N"))
//            {
//                gender = Protocol.DEFAULT_FIELD_VALUE;
//            }
//            customer.set(CustomerFieldTypes.SEX, gender);
            customer.set(CustomerFieldTypes.SEX, Protocol.DEFAULT_FIELD_VALUE);
        }
    }
}
