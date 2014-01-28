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
package site.strathcona;

import api.CustomerMessage;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.util.Text;
import site.CustomerGetNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class STRCustomerGetNormalizer extends CustomerGetNormalizer
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
//        recv:64YYYY      Y   00020130606    115820000000000000000100000000AO|AA21221012345678|AEBILLY, BALZAC|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 12.00|BD7 SIR WINSTON CHURCHILL SQUARE EDMONTON, AB T5J 2V4|BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6
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
        // Strathcona has an issue that they emit data in 
        // UPPERCASE for customer names and addresses.
        String upperCaseFieldText = customer.get(CustomerFieldTypes.STREET);
        upperCaseFieldText = Text.toDisplayCase(upperCaseFieldText);
        customer.set(CustomerFieldTypes.STREET, upperCaseFieldText);
        // First Name
        upperCaseFieldText = customer.get(CustomerFieldTypes.FIRSTNAME);
        upperCaseFieldText = Text.toDisplayCase(upperCaseFieldText);
        customer.set(CustomerFieldTypes.FIRSTNAME, upperCaseFieldText);
        // Last Name
        upperCaseFieldText = customer.get(CustomerFieldTypes.LASTNAME);
        upperCaseFieldText = Text.toDisplayCase(upperCaseFieldText);
        customer.set(CustomerFieldTypes.LASTNAME, upperCaseFieldText);
        // prefered name
        upperCaseFieldText = customer.get(CustomerFieldTypes.PREFEREDNAME);
        upperCaseFieldText = Text.toDisplayCase(upperCaseFieldText);
        customer.set(CustomerFieldTypes.PREFEREDNAME, upperCaseFieldText);
    }
}
