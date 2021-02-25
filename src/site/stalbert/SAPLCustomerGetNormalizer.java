/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2021  Edmonton Public Library
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
package site.stalbert;

import api.CustomerMessage;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.util.DateComparer;
import site.CustomerGetNormalizer;

/**
 * This class compensates for any SIP2 configuration oddities that SAPL may have.
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public class SAPLCustomerGetNormalizer extends CustomerGetNormalizer
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
//        sent:63                               AO|AA21221012345678|AD64058|AY0AZF374
//        recv:64YYYY      Y   00020130606    115820000000000000000100000000AO|AA21221012345678|AEBILLY, BALZAC|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 12.00|BD7 SIR WINSTON CHURCHILL SQUARE EDMONTON, AB T5J 2V4|BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|PCEPL-THREE|PFM|DB$0.00|DM$0.00|AF#Incorrect password|AY0AZACC6
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
        
        // AF field will contain '#Incorrect password' if the customer enters an invalid pin
        // This gets past from the SIP server if no validation is set.
//        if (c.get(CustomerFieldTypes.RESERVED).compareToIgnoreCase("Invalid PIN for station user") == 0)
        // which is tested in SIPRequestBuilder.
        // Since all SIP servers seem to report incorrect password differently and SIPRequestBuilder
        // specifically checks for the message: 'Invalid PIN for station user' we
        // can translate it here so the check for correct pin will fail. Without it
        // some SIP servers will return customer information without the correct pin
        // because they have not been set up properly. If passed, someone could 
        // get the user information with a barcode alone, and less seriously a
        // registering customer would have an unexpected PIN set on their account
        // at another library.
        // TODO: customize message testing in SIPRequestBuilder to test a standard
        // message or value, and have each emmitter test and set that value based
        // on the message from the SIP server at the host library.
        if (message.getField("AF").startsWith("#Incorrect password"))
        {
            customer.set(CustomerFieldTypes.RESERVED, "Invalid PIN for station user");
        }
                
        // Date of birth in SIP PB not per standard.
        String birthDate = DateComparer.cleanDateTime(message.getField("PB"));
        if (DateComparer.isDate(birthDate))
        {
            customer.set(CustomerFieldTypes.DOB, birthDate); // St. Albert.
        }
        
        // Now we know that STA uses 'PE' for expiry but 'PA' is the industrial 
        // norm, so let's fix that here.
        String cleanDate = DateComparer.cleanDateTime(message.getField("PE"));
        if (DateComparer.isDate(cleanDate))
        {
            customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, cleanDate);
        }
    }
}
