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
package site.edmonton;

import java.util.Properties;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.FlatUserExtendedFieldTypes;
import mecard.config.PropertyReader;
import mecard.config.SymphonyPropertyTypes;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import site.CustomerLoadNormalizer;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * 
 * For EPL the actions are to load the customer's default account information
 * required by Symphony.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class EPLCustomerNormalizer extends CustomerLoadNormalizer
{
    private final boolean debug;
    
    public EPLCustomerNormalizer(boolean debug)
    {
        this.debug = debug;
    }
    
    @Override
    public ResponseTypes normalize(Customer c, StringBuilder r)
    {
        return ResponseTypes.SUCCESS; // no special rules for EPL.
    }

    /**
     *
     * @param unformattedCustomer the raw MeCard customer account information.
     * @param formattedCustomer the Flat formattedCustomer
     * @param response the value of response
     */
    @Override
    public void finalize(Customer unformattedCustomer, FormattedCustomer formattedCustomer, Response response)
    {
        // Here we have to do all the final preparations to loading a customer
        // Specifically we will be adding default values to the account
        // like PROFILE etc.
        Properties defaultProps = PropertyReader.getProperties(ConfigFileTypes.SYMPHONY);
        for (SymphonyPropertyTypes defaultType : SymphonyPropertyTypes.values())
        {
            // Since all the CURRENT default types are located in the USER section
            // of the flat file, we don't need to create new FlatFormattedTable objects.
            String key = defaultType.toString();
            String value = defaultProps.get(key).toString();
            formattedCustomer.insertValue(FlatUserExtendedFieldTypes.USER.name(), key, value);
        }
    }
}
