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
package site.shortgrass;

import mecard.Response;
import mecard.config.FlatUserExtendedFieldTypes;
import mecard.config.FlatUserFieldTypes;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import site.SymphonyNormalizer;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * 
 * For Shortgrass Library System the actions are to load the customer's default account information
 * required by Symphony.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class SLSCustomerNormalizer extends SymphonyNormalizer
{
//    private final boolean debug;
    
    public SLSCustomerNormalizer(boolean debug)
    {
        super(debug);
    }

    /**
     * This method is used to add additional fields to the formatted customer
     * before loading.
     * @param unformattedCustomer the raw MeCard customer account information as
     * provided by the host library in GSON.
     * @param formattedCustomer the Flat formattedCustomer that will be loaded 
     * by Symphony.
     * @param response the value of response
     */
    @Override
    public void finalize(Customer unformattedCustomer, FormattedCustomer formattedCustomer, Response response)
    {
        // This loads all the mandatory values on SymphonyPropertyTypes.
        this.loadDefaultProfileAttributes(unformattedCustomer, formattedCustomer, response);
        // Currently Shortgrass uses USER_CATEGORY2 to store the 
        // customer's age category but future versions of metro may be required to register 
        // Juveniles. If that is the case you need only get the customers age from the 
        // Minimum age flag or compute on DOB then set the formattedCustomer 
        // field as: 
        formattedCustomer.insertValue(
            FlatUserExtendedFieldTypes.USER.name(), 
            FlatUserFieldTypes.USER_CATEGORY2.toString(), 
            "ADULT"
        );
    }
}
