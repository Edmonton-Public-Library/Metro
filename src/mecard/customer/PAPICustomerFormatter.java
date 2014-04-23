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
 */
package mecard.customer;

import java.util.List;

/**
 * Formats the {@link mecard.customer.Customer} into the Polaris consumable form.
 * The form of a customer is outlined in the Polaris API manual and can be submitted
 * as either JSON or XML. This class will handle the TRAC version, but with enough
 * wiggle room to handle the other.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PAPICustomerFormatter implements CustomerFormatter
{

    @Override
    public Customer getCustomer(List<String> list)
    {
        return getCustomer(list.get(0)); 
    }

    @Override
    public Customer getCustomer(String s)
    {
        // Date conversion will be important
//        Date Format when using JSON
//        Because JSON does not have a standard way of describing dates, the following format must be used:
//        "\/Date(1295352000000)\/"
//        The number represents the number of milliseconds since January 1st 1970 UTC.
//        1295352000000 represents Tuesday, January 18, 2011 7:00:00 AM.
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
