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
package mecard.customer.polaris;

import java.util.List;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;

/**
 * This class is like {@link mecard.customer.symphony.FlatFormattedCustomer} but
 * in the case of Symphony that needs to operate on a list of strings, making 
 * changes to the customer information, then load the customer as a list of 
 * strings (similarly with {@link mecard.customer.horizon.BImportFormattedCustomer})
 * this formatted customer only needs to operate on the customer data, formatting
 * it in minor ways like ensuring the postal code has proper formatting and phone
 * numbers include hyphens '-'. The customer then needs to be referenced like a 
 * regular {@link mecard.customer.Customer} object.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PolarisSQLCustomerFormatter implements CustomerFormatter
{
    public PolarisSQLCustomerFormatter()
    {
        
    }
    
    
    @Override
    public Customer getCustomer(String SQLResultString)
    {
        Customer customer = new Customer();
        
        // parse the String for conversion from SQL to  and load the customer object with it's data.
        
        return customer;
    }

    @Override
    public Customer getCustomer(List<String> customerList)
    {
        return getCustomer(customerList.get(0));
    }
    
}
