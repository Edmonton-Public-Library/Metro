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

import site.edmonton.EPLCustomerNormalizer;
import java.util.Properties;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.customer.Customer;
import mecard.exception.UnsupportedLibraryException;
import mecard.config.PropertyReader;
import mecard.config.MemberTypes;
import mecard.customer.FormattedCustomer;
import site.stalbert.STACustomerNormalizer;
import site.strathcona.STRCustomerNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public abstract class CustomerLoadNormalizer 
{
    private static CustomerLoadNormalizer normalizer;
    protected CustomerLoadNormalizer()
    {
        normalizer = null;
    }
    
    /**
     * Normalizes the customer data to local load restrictions like minimum PIN 
     * width.
     *
     * @param c customer object must not be null.
     * @param r the response from the normalization process.
     * @return String message of what was changed that the customer should know about.
     * In the above example they would be notified that St. Albert has their 
     * last 4 digits of their EPL Pin saved.
     */
    public abstract ResponseTypes normalize(Customer c, StringBuilder r);
            
    public static CustomerLoadNormalizer getInstanceOf(boolean debug)
    {
        if (normalizer != null)
        {
            return normalizer;
        }
        // read the config, find what type of normalizer we need, create it 
        // and return it.
        Properties props = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        String libCode = props.getProperty(LibraryPropertyTypes.LIBRARY_CODE.toString());
        if (libCode.equalsIgnoreCase(MemberTypes.EPL.name()))
        {
            normalizer = new EPLCustomerNormalizer(debug);
        } 
        else if (libCode.equalsIgnoreCase(MemberTypes.STA.name()))
        {
            normalizer = new STACustomerNormalizer(debug);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.STR.name()))
        {
            normalizer = new STRCustomerNormalizer(debug);
        }
        else
        {
            throw new UnsupportedLibraryException(libCode);
        }
        return normalizer;
    }

    /**
     * This method applies all the final touches to the customer's account pre-
     * loading to the ILS. This is where default values are applied, bStats are 
     * computed. It is the last stop before loading.
     * @param unformattedCustomer the value of unformattedCustomer
     * @param formattedCustomer the value of formattedCustomer
     * @param response the value of response
     */
    
    public abstract void finalize(Customer unformattedCustomer, FormattedCustomer formattedCustomer, Response response);
}
