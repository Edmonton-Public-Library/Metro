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
package site;

import java.util.Properties;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.FlatUserExtendedFieldTypes;
import mecard.config.PropertyReader;
import mecard.config.SymphonyPropertyTypes;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;

/**
 * This class can be used for all Symphony related customer processing requirements.
 * The use of classes like this is to facilitate the ILS restrictions like enforcement
 * of 4-digit pins on customer creation.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public abstract class SymphonyNormalizer extends CustomerLoadNormalizer
{
    protected SymphonyNormalizer(boolean debug)
    {
        super(debug);
    }
    
    @Override
    public ResponseTypes normalize(Customer c, StringBuilder r)
    {
        return ResponseTypes.SUCCESS; // no special rules for EPL.
    }
    
    /**
     * Loads all the default profile attributes of a customer that are stored
     * in the symphony.properties file.
     * @param unformattedCustomer raw customer data
     * @param formattedCustomer the formatted customer data that will ultimately 
     * be loaded.
     * @param response where to put any messages for the customer.
     */
    protected void loadDefaultProfileAttributes(Customer unformattedCustomer, MeCardCustomerToNativeFormat formattedCustomer, Response response)
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
    
    @Override
    public void normalizeOnCreate(Customer customer, Response response)
    {
        // No special action required at this time. Override in subclass if desired.
    }

    @Override
    public void normalizeOnUpdate(Customer customer, Response response)
    {
        // No special action required at this time. Override in subclass if desired.
    }
}
