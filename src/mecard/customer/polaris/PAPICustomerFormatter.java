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
package mecard.customer.polaris;

import java.util.List;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;

/**
 * Formats the {@link mecard.customer.Customer} into the Polaris consumable form.
 * The form of a customer is outlined in the Polaris API manual and can be submitted
 * as either JSON or XML. This class will handle the TRAC version, but with enough
 * wiggle room to handle the other.
 * 
 * Note that TRAC uses SIP2 so this class is stubbs only.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PAPICustomerFormatter implements CustomerFormatter
{

    @Override
    public Customer getCustomer(List<String> list)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer getCustomer(String s)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
