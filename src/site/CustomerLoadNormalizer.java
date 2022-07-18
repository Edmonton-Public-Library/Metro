/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
import site.calgary.CPLCustomerNormalizer;
import site.chinook.CARCustomerNormalizer;
import site.woodbuffalo.WBRLCustomerNormalizer;
import site.parklands.PRLCustomerNormalizer;
import site.reddeer.RDPLCustomerNormalizer;
import site.shortgrass.SLSCustomerNormalizer;
import site.stalbert.SAPLCustomerNormalizer;
import site.strathcona.STRCustomerNormalizer;
import site.trac.TRACCustomerNormalizer;
import mecard.customer.MeCardCustomerToNativeFormat;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com.
 */
public abstract class CustomerLoadNormalizer 
{
    private static CustomerLoadNormalizer normalizer;
    protected boolean debug;
    
    protected CustomerLoadNormalizer(boolean debug)
    {
        normalizer = null;
        this.debug = debug;
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
       
    /**
     * Returns an instance of the appropriate normalizer for the library specified
     * in the environment.properties file.
     * @param debug flat to signal debug mode to be set.
     * @return the customer normalizer.
     */
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
            normalizer = new SAPLCustomerNormalizer(debug);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.STR.name()))
        {
            normalizer = new STRCustomerNormalizer(debug);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.TRAC.name()))
        {
            normalizer = new TRACCustomerNormalizer(debug);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.PKL.name()))
        {
            normalizer = new PRLCustomerNormalizer(debug);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.SLS.name()))
        {
            normalizer = new SLSCustomerNormalizer(debug);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.CAR.name()))
        {
            normalizer = new CARCustomerNormalizer(debug);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.CPL.name()))
        {
            normalizer = new CPLCustomerNormalizer(debug);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.WBRL.name()))
        {
            normalizer = new WBRLCustomerNormalizer(debug);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.RDPL.name()))
        {
            normalizer = new RDPLCustomerNormalizer(debug);
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
    public abstract void finalize(Customer unformattedCustomer, 
            MeCardCustomerToNativeFormat formattedCustomer, 
            Response response);

    /**
     * Breaks out normalization procedures that are specific to creation process.
     * @param customer
     * @param response 
     */
    public abstract void normalizeOnCreate(Customer customer, Response response);

    /**
     * Breaks out normalization procedures that are specific to update process.
     * @param customer
     * @param response 
     */
    public abstract void normalizeOnUpdate(Customer customer, Response response);
}
