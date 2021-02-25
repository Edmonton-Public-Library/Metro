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

import mecard.Response;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import site.HorizonNormalizer;

/**
 * Normalizes the customer's data before loading into SAPL's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public final class SAPLCustomerNormalizer extends HorizonNormalizer
{
    
    public SAPLCustomerNormalizer(boolean debug)
    {
        super(debug);
    }

    @Override
    public void finalize(Customer unformattedCustomer, FormattedCustomer formattedCustomer, Response response)
    {
        // Test and hash the user's password to a 4-digit number if necessary.
        super.finalize(unformattedCustomer, formattedCustomer, response);
        // They also set bstat for sex
        if (unformattedCustomer.isEmpty(CustomerFieldTypes.SEX) == false)
        {
            String sex = unformattedCustomer.get(CustomerFieldTypes.SEX);
            if (sex.compareToIgnoreCase("M") == 0)
            {
                addBStatTable(formattedCustomer, "m");
            }
            else if (sex.compareToIgnoreCase("F") == 0)
            {
                addBStatTable(formattedCustomer, "f");
            }
            else
            {
                addBStatTable(formattedCustomer, "u");
            }
        }
        else
        {
            addBStatTable(formattedCustomer, "u");
        }
        addBStatTable(formattedCustomer, "metro");
    }
}
