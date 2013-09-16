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
package site.stalbert;

import java.util.Date;
import mecard.ResponseTypes;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.util.Text;
import site.CustomerLoadNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class STACustomerNormalizer extends CustomerLoadNormalizer
{
    public final static int MAXIMUM_PIN_WIDTH = 4;
    private final boolean debug;
    
    public STACustomerNormalizer(boolean debug)
    {
        this.debug = debug;
    }
    
    @Override
    public ResponseTypes normalize(Customer c, StringBuilder responseStringBuilder)
    {
        ResponseTypes rType = ResponseTypes.SUCCESS;
        String pin = c.get(CustomerFieldTypes.PIN);
        if (Text.isMaximumDigits(pin, MAXIMUM_PIN_WIDTH) == false)
        {
            String newPin = Text.getNew4DigitPin();
            responseStringBuilder.append(newPin); // send back the new PIN
            c.set(CustomerFieldTypes.PIN, newPin);
            System.out.println(new Date() + " Customer's PIN was not 4 digits as required by Horizon. Set to: '" 
                    + newPin + "'.");
            rType = ResponseTypes.PIN_CHANGE_REQUIRED;
        }
        return rType;
    }
}
