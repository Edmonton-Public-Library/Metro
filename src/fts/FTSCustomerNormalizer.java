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
package fts;

import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.site.CustomerLoadNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class FTSCustomerNormalizer extends CustomerLoadNormalizer
{
    public final static int MAXIMUM_PIN_WIDTH = 4;
    private final boolean debug;
    
    public FTSCustomerNormalizer(boolean debug)
    {
        this.debug = debug;
    }
    
    @Override
    public String normalize(Customer c)
    {
        StringBuilder returnMessage = new StringBuilder();
        if (c.get(CustomerFieldTypes.PIN).length() <= FTSCustomerNormalizer.MAXIMUM_PIN_WIDTH == false)
        {
            String pin = c.get(CustomerFieldTypes.PIN);
            int start = pin.length() - FTSCustomerNormalizer.MAXIMUM_PIN_WIDTH;
            if (start >= 0)
            {
                String newPin = pin.substring(start);
                returnMessage.append("Your pin has been set to '");
                returnMessage.append(newPin);
                returnMessage.append("' to comply with this library's policies.");
                c.set(CustomerFieldTypes.PIN, newPin);
                System.out.println("Customer's PIN was too long trimmed to last "
                    + FTSCustomerNormalizer.MAXIMUM_PIN_WIDTH + " characters.");
            }
        }
        return returnMessage.toString();
    }
}
