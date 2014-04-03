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

import mecard.config.MessagesConfigTypes;
import mecard.exception.SIPException;

/**
 * Parses SIP2 customer information into a generally usable format.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SIPCustomerMessage 
    extends SIPMessage
    implements CustomerMessage
{
    public enum PATRON_STATUS_FLAGS
    {
        CHARGE_PRIVILEGES_DENIED,
        RENEWAL_PRIVILEGES_DENIED,
        RECALL_PRIVILEGES_DENIED,
        HOLD_PRIVILEGES_DENIED,
        CARD_REPORTED_LOST,
        TOO_MANY_ITEMS_CHARGED,
        TOO_MANY_ITEMS_OVERDUE,
        TOO_MANY_RENEWALS,
        TOO_MANY_CLAIMS_OF_ITEMS_RETURNED,
        TOO_MANY_ITEMS_LOST,
        EXCESSIVE_OUTSTANDING_FINES,
        EXCESSIVE_OUTSTANDING_FEES,
        RECALL_OVERDUE,
        TOO_MANY_ITEMS_BILLED;
        
        public static int size()
        {
            return PATRON_STATUS_FLAGS.values().length;
        }
    }
    
    public SIPCustomerMessage(String message)
    {
        super(message);
        if (this.code.compareTo("64") != 0) // Not a customer response message
        {
            throw new SIPException(this.messageProperties.getProperty(
                    MessagesConfigTypes.UNAVAILABLE_SERVICE.toString()));
        } 
    }
    
    /**
     * 
     * @return String of the customer's profile field (PC). 
     * If the message doesn't represent a customer 
     */
    @Override
    public final String getCustomerProfile()
    {
        String profile = this.getField("PC");
        if (profile == null)
        {
            profile = "";
        }
        return profile;
    }
    
    /**
     * 
     * @return String of the message field (AF).
     */
    public final String getMessage()
    {
        String message = this.getField("AF");
        if (message == null)
        {
            message = "";
        }
        return message;
    }

    @Override
    public String getStanding()
    {
        // 64YY        Y   00020130808    150700000000000000000500000000AOst|AA22222001047624|AETEST, 1|AQst|BZ0030|CA0100|CB0200|BLY|BHCAD|BV16.8|CC9.99|BD5 St. Anne St., St. Albert, AB, T8N 3Z9|BEktroppmann@sapl.ca|BF780-459-1537|DH1|DJTEST|PCmra|PE20130824    235900|PS20130824    235900|ZYmra|AF#You are barred from borrowing - Please refer to the circulation desk.|AY0AZ9A5C
        // The AF field contains a message about standing usually 'barred' appears in it.
        // This method may need to be extended to account for other methods of packing
        // a status message or compute standing based on fines.
        // Parklands reports their profile type as suspended
        // so test if there is a message, if there is likely to have patron info
        // in it, but if there isn't it will likely require a profile check.
        if (this.isEmpty("AF")) // if the message field is empty use the profile.
        {
            return this.getCustomerProfile();
        }
        return this.getMessage();
    }
    
    /**
     * Each of SIP2's 14 fields can have 3 states: 'Y', 'N', or ' ' which signals
     * that that value is not returned by the ILS's SIP2 configuration. This method
     * will test if the flag is set with either 'Y' or 'N', but will not report
     * the flag itself. See {@link #isTrue(api.SIPCustomerMessage.PATRON_STATUS_FLAGS)}
     * and {@link #isFalse(api.SIPCustomerMessage.PATRON_STATUS_FLAGS)}, for 
     * more details.
     * @param flag - that you are interested in testing.
     * @return true if the flag has a value of either 'Y' or 'N' and false otherwise.
     */
    public boolean isReported(PATRON_STATUS_FLAGS flag)
    {
        // SIP allows ' ' or 'Y' to be true, and 'N' to be false.
        char c = this.codeBits.charAt(flag.ordinal());
        return c != ' ';
    }
    
    /**
     * Each of SIP2's 14 fields can have 3 states: 'Y', 'N', or ' ' which signals
     * that that value is not returned by the ILS's SIP2 configuration. This method
     * will test if the flag is set with either 'Y'.
     * See {@link #isReported(api.SIPCustomerMessage.PATRON_STATUS_FLAGS)}
     * and {@link #isFalse(api.SIPCustomerMessage.PATRON_STATUS_FLAGS)}, for 
     * more details.
     * @param flag - that you are interested in testing.
     * @return true if the flag has a value of 'Y' and false otherwise.
     */
    public boolean isTrue(PATRON_STATUS_FLAGS flag)
    {
        char c = this.codeBits.charAt(flag.ordinal());
        return c == 'Y';
    }
    
    /**
     * Each of SIP2's 14 fields can have 3 states: 'Y', 'N', or ' ' which signals
     * that that value is not returned by the ILS's SIP2 configuration. This method
     * will test if the flag is set with 'N'.
     * See {@link #isReported(api.SIPCustomerMessage.PATRON_STATUS_FLAGS)}
     * and {@link #isTrue(api.SIPCustomerMessage.PATRON_STATUS_FLAGS)}, for 
     * more details.
     * @param flag - that you are interested in testing.
     * @return true if the flag has a value of 'N' and false otherwise.
     */
    public boolean isFalse(PATRON_STATUS_FLAGS flag)
    {
        char c = this.codeBits.charAt(flag.ordinal());
        return c == 'N';
    }

    @Override
    public boolean cardReportedLost()
    {
//        sent:63                               AO|AA22222000929152|AD1537|AY0AZF3B1
//        recv:64YYYYY         00020140220    094400000000000000000100000000AOst|AA22222000929152|AECirculation test #2|AQst|BZ0030|CA0100|CB0200|BLY|BHCAD|CC9.99|BD5 St. Anne Street, St. Albert, AB, T8N 3Z9|BEktroppmann@sapl.ca|BF780-459-1537|DJCirculation test #2|LG0|PB19510618|PCra|PE20140812    235900|PS20140812    235900|ZYra|AF#Lost card - please refer to the circulation desk.|AY0AZ954A
//
//        Response code:64
//        Patron Information Response
//          (O) Patron Status : YYYYY         
//                Charge Privileges Denied :          Y
//                Renewal Privileges Denied :         Y
//                Recall Privileges Denied :          Y
//                Hold Privileges Denied:             Y
//                Card Reported Lost :                Y
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
//          (F) Transaction Date:              20140220    094400
//          (F) Hold Items Count:              0000
//          (F) Overdue Items Count:           0000
//          (F) Charged Items Count:           0000
//          (F) Fine Items Count:              0001
//          (F) Recall Items Count:            0000
//          (F) Unavailable Hold Items Count:  0000
//          (R) Institution Id:st
//          (R) Patron Identifier:22222000929152
//          (R) Personal Name:Circulation test #2
//          (R) Patron Library:st
//          (O) Hold Items Limit:0030:
//          (O) Overdue Items Limit:0100:
//          (O) Charged Items Limit:0200:
//          (O) Valid Patron:Y:
//          (O) Currency Type:CAD:
//          (O) Fee Limit:9.99
//          (O) Home Address:5 St. Anne Street, St. Albert, AB, T8N 3Z9
//          (O) E-mail Address:ktroppmann@sapl.ca
//          (O) Home Phone Number:780-459-1537
//          (O) DJ INVALID DATA CODE FOR THIS MESSAGE:Circulation test #2
//          (O) LG INVALID DATA CODE FOR THIS MESSAGE:0
//          (O) Birth year:19510618
//          (O) Profile:ra
//          (O) User Category 1:20140812    235900
//          (O) PS INVALID DATA CODE FOR THIS MESSAGE:20140812    235900
//          (O) ZY INVALID DATA CODE FOR THIS MESSAGE:ra
//          (O) Screen Message:#Lost card - please refer to the circulation desk.
//          (R) Sequence Number : 0 :  matches what was sent
//          (R) Checksum : 954A : Checksum OK
        return this.isReported(PATRON_STATUS_FLAGS.CARD_REPORTED_LOST) 
                && this.isTrue(PATRON_STATUS_FLAGS.CARD_REPORTED_LOST);
    }
    
    @Override
    public boolean isEmpty(String fieldName)
    {
        return super.isEmpty(fieldName) || this.getMessage().isEmpty();
    }
    
    @Override
    public boolean isInGoodStanding()
    {
        // if the value is set to 'Y', or true, the customer is NOT in good standing.
        // By reverse logic, if the field is empty or 'N' (never seen yet) then the 
        // customer IS in good standing.
        return ! this.isTrue(PATRON_STATUS_FLAGS.CHARGE_PRIVILEGES_DENIED);
    }
}
