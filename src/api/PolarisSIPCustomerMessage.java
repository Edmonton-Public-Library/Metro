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
import mecard.util.DateComparer;

/**
 * This class exists because for some reason SIP2 implementation teams can't 
 * read the 3M SIP2 specification guidelines wrt standard tags. 
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PolarisSIPCustomerMessage extends SIPCustomerMessage
{

    public PolarisSIPCustomerMessage(String message)
    {
        super(message);
    }
    
    
    /////////////////////
    
    /**
     * 
     * @return String of the customer's profile field (PC). 
     * If the message doesn't represent a customer 
     */
    @Override
    public String getCustomerProfile()
    {
        // Polaris 
        String profile = this.getField("PA");
        if (profile == null)
        {
            profile = "";
        }
        return profile;
    }
    
    @Override
    public String getDateField(String fieldName)
    {
        String possibleDate = DateComparer.cleanDateTime(getField(fieldName));
        if (DateComparer.isDate(possibleDate))
        {
            return possibleDate;
        }
        return Protocol.DEFAULT_FIELD_VALUE;
    }
    
    /**
     * 
     * @return String of the message field (AF) or (AG) if AF is empty.
     */
    @Override
    public String getMessage()
    {
        String message = this.getField("AF");
        if (message == null || message.isEmpty())
        {
            message = this.getField("AG");
            if (message == null || message.isEmpty())
            {
                message = "";
            }
        }
        return message;
    }
    
    @Override
    public boolean isInGoodStanding()
    {
        // test if the customer has a message 
        return this.getMessage().contains("ok");
    }
}
