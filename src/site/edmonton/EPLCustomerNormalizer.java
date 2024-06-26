/*
* Metro allows customers from any affiliate library to join any other member library.
*    Copyright (C) 2024  Edmonton Public Library
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

import mecard.Response;
import mecard.config.FlatUserTableTypes;
import mecard.config.FlatUserFieldTypes;
import mecard.customer.Customer;
import site.SymphonyNormalizer;
import mecard.customer.MeCardCustomerToNativeFormat;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * 
 * For EPL the actions are to load the customer's default account information
 * required by Symphony.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class EPLCustomerNormalizer extends SymphonyNormalizer
{
//    private final boolean debug;
    
    public EPLCustomerNormalizer(boolean debug)
    {
        super(debug);
    }

    /**
     * This method is used to add additional fields to the formatted customer
     * before loading.
     * @param unformattedCustomer the raw MeCard customer account information as
     * provided by the host library.
     * @param formattedCustomer the Flat formattedCustomer that will be loaded 
     * by Symphony.
     * @param response the value of response
     */
    @Override
    public void finalize(Customer unformattedCustomer, MeCardCustomerToNativeFormat formattedCustomer, Response response)
    {
        // No matter what ME customer give implied consent to contact via email when they join.
        // Which at EPL, is USER_CATEGORY5.
        formattedCustomer.insertValue(FlatUserTableTypes.USER.name(), 
                    FlatUserFieldTypes.USER_CATEGORY5.toString(), 
                    "ECONSENT");
        // Suppress preferred user name.
        formattedCustomer.removeField(FlatUserTableTypes.USER.name(),
            FlatUserFieldTypes.USER_PREFERRED_NAME.toString()
        );
        this.loadDefaultProfileAttributes(unformattedCustomer, formattedCustomer, response);
    }
}
