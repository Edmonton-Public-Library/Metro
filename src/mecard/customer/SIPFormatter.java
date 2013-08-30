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
package mecard.customer;

import api.SIPCustomerMessage;
import api.SIPMessage;
import mecard.config.CustomerFieldTypes;
import java.util.List;
import mecard.util.Address;
import mecard.util.Phone;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SIPFormatter implements CustomerFormatter
{

    public SIPFormatter()
    {
    }

    @Override
    public Customer getCustomer(String s)
    {
//        System.out.println(">>>>> SIP_CUSTOMER:'"+s+"'");
        Customer customer = new Customer();
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
        SIPCustomerMessage sipMessage = new SIPCustomerMessage(s);
        customer.set(CustomerFieldTypes.ID, sipMessage.getField("AA"));
        customer.set(CustomerFieldTypes.PREFEREDNAME, sipMessage.getField("AE"));
        customer.set(CustomerFieldTypes.RESERVED, sipMessage.getField("AF"));
        customer.set(CustomerFieldTypes.EMAIL, sipMessage.getField("BE"));
        // Phone object
        Phone phone = new Phone(sipMessage.getField("BF"));
//        customer.set(CustomerFieldTypes.PHONE, sipMessage.getField("BF"));
        customer.set(CustomerFieldTypes.PHONE, phone.getUnformattedPhone());
        customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, sipMessage.getField("PA"));
        // PE can also be privilege expires, so 
        String possibleDate = sipMessage.getField("PE");
        if (SIPMessage.isDate(possibleDate))
        {
            customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, possibleDate);
        }
        customer.set(CustomerFieldTypes.DOB, sipMessage.getField("PD"));
        customer.set(CustomerFieldTypes.SEX, sipMessage.getField("PF"));
        // Complete address
        Address address = new Address(sipMessage.getField("BD"), true);
        customer.set(CustomerFieldTypes.STREET, address.getStreet());
        customer.set(CustomerFieldTypes.CITY, address.getCity());
        customer.set(CustomerFieldTypes.PROVINCE, address.getProvince());
        customer.set(CustomerFieldTypes.POSTALCODE, address.getPostalCode());
        customer.set(CustomerFieldTypes.PHONE, address.getPhone());
        return customer;
    }

    /**
     *
     * @param c the value of c
     * @return the List<String>
     */
    @Override
    public List<String> setCustomer(Customer c)
    {
        throw new UnsupportedOperationException("SIP does not support customer creation.");
    }

    @Override
    public Customer getCustomer(List<String> list)
    {
        return getCustomer(list.get(0));
    }
}
