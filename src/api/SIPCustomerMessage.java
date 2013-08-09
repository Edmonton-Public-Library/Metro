/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Andrew Nisbet
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

import mecard.exception.SIPException;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SIPCustomerMessage extends SIPMessage
{
    public SIPCustomerMessage(String message)
    {
        super(message);
        if (this.code.compareTo("64") != 0) // Not a customer response message
        {
            throw new SIPException(SIPCustomerMessage.class.getName()
                    + " This doesn't look like a SIP2 customer response: '" 
                    + message + "'.");
        }
    }
    
    /**
     * 
     * @return String of the customer's profile field (PC). 
     * If the message doesn't represent a customer 
     */
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
}
