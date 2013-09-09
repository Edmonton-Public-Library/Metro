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
public final class STRCustomerNormalizer extends CustomerLoadNormalizer
{
    public final static int MAXIMUM_PIN_WIDTH = 4;
    private final boolean debug;
    
    public STRCustomerNormalizer(boolean debug)
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
            responseStringBuilder.append("Your pin has been set to '");
            responseStringBuilder.append(newPin);
            responseStringBuilder.append("' to comply with this library's policies.");
            c.set(CustomerFieldTypes.PIN, newPin);
            System.out.println(new Date() + " Customer's PIN was not 4 digits as required by Horizon. Set to: '" 
                    + newPin + "'.");
            rType = ResponseTypes.PIN_CHANGE_REQUIRED;
        }
        // Strathcona use UPPERCASE Name and Address for their customers, to make
        // it easier to spot STR and FTS customers.
        // We change the first and last name, but note this does not effect PREFEREDNAME.
        String customerData = c.get(CustomerFieldTypes.FIRSTNAME).toUpperCase();
        c.set(CustomerFieldTypes.FIRSTNAME, customerData);
        customerData = c.get(CustomerFieldTypes.LASTNAME).toUpperCase();
        c.set(CustomerFieldTypes.LASTNAME, customerData);
        customerData = c.get(CustomerFieldTypes.STREET).toUpperCase();
        c.set(CustomerFieldTypes.STREET, customerData);
        // We deliberately don't do City or Province because 1) it is just a visual
        // queue for the staff, 2) there may be translation issues for finding
        // the customer's city in the city_st table. CONFIRMED: there is no
        // translation problem since the City class does a lookup case insensitively
        // but I still recommend not converting case, since it effects other libraries
        // records.
        return rType;
    }
}
