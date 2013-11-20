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

import mecard.Protocol;
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
}
