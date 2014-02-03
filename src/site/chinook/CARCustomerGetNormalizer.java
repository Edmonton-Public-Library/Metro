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

import api.CustomerMessage;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import site.CustomerGetNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class CARCustomerGetNormalizer extends CustomerGetNormalizer
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
//        sent:63                               AO|AA25021000719291|AD1861|AY1AZF3AF
//
//        recv:64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20
//
//        Response code:64
//        Patron Information Response
//          (O) Patron Status :               
//                Charge Privileges Denied :           
//                Renewal Privileges Denied :          
//                Recall Privileges Denied :           
//                Hold Privileges Denied:              
//                Card Reported Lost :                 
//                Too Many Items Charged :             
//                Too many Items Overdue :             
//                Too Many Renewals :                  
//                Too Many Claims Of Items Returned :  
//                Too Many Items Lost :                
//                Excessive Outstanding Fines :        
//                Excessive Outstanding Fees :         
//                Recall Overdue :                     
//                Too Many Items Billed :              
//          (F) Language:                      000
//          (F) Transaction Date:              20140110    161047
//          (F) Hold Items Count:              0000
//          (F) Overdue Items Count:           0000
//          (F) Charged Items Count:           0000
//          (F) Fine Items Count:              0000
//          (F) Recall Items Count:            0000
//          (F) Unavailable Hold Items Count:  0000
//          (R) Institution Id:
//          (R) Patron Identifier:25021000719291
//          (R) Personal Name:Sherman, William Tecumseh
//          (R) Patron Library:SGMED
//          (O) Hold Items Limit:0100:
//          (O) Overdue Items Limit:0100:
//          (O) Charged Items Limit:0999:
//          (O) Valid Patron:Y:
//          (O) Valid Patron Password:Y:
//          (O) Fee Amount: 0.00
//          (O) Home Address:1864 Savannah Street T1A 3N7
//          (O) E-mail Address:anton@shortgrass.ca
//          (O) Currency Type:USD:
//          (O) Expiration Date:20150108    235900
//          (O) Birth date:19520208
//          (O) Profile:SGMEDA
//          (O) User Category 1:MEDICINEHA
//          (O) User Category 2:ADULT
//          (O) User Category 3:MALE
//          (O) DB INVALID DATA CODE FOR THIS MESSAGE:$0.00
//          (O) DM INVALID DATA CODE FOR THIS MESSAGE:$500.00
//          (O) Screen Message:OK
//          (R) Sequence Number : 1 :  matches what was sent
//          (R) Checksum : AC20 : Checksum OK
        // here we will fill in the customer attributes with the contents of s- the SIP message.
        // TODO: Shortgrass uses "PG" for "MALE" or "FEMALE"
        String sex = message.getField("PG");
        if (sex.isEmpty() == false)
        {
            
            if (sex.compareToIgnoreCase("MALE") == 0)
            {
                // send the first letter, "M" or "F"
                customer.set(CustomerFieldTypes.SEX, "M");
            }
            else if (sex.compareToIgnoreCase("FEMALE") == 0)
            {
                // send the first letter, "M" or "F"
                customer.set(CustomerFieldTypes.SEX, "F");
            }
        }
    }
}
