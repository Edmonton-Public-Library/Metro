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
package site.calgary;

import mecard.Response;
import mecard.config.CustomerFieldTypes;
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
public final class CPLCustomerNormalizer extends SymphonyNormalizer
{
//    private final boolean debug;
    
    public CPLCustomerNormalizer(boolean debug)
    {
        super(debug);
    }

    /**
     * This method is used to add additional fields to the formatted customer
     * before loading.
     * @param rawCustomer the raw MeCard customer account information as
     * provided by the host library in GSON.
     * @param formattedCustomer the Flat formattedCustomer that will be loaded 
     * by Symphony.
     * @param response the value of response
     */
    @Override
    public void finalize(Customer rawCustomer, FormattedCustomer formattedCustomer, Response response)
    {
        // This loads all the mandatory values on SymphonyPropertyTypes.
        this.loadDefaultProfileAttributes(rawCustomer, formattedCustomer, response);
        // Currently Calgary uses PHONE1 to store the customer's phone number: 
        formattedCustomer.renameField(
                FlatUserExtendedFieldTypes.USER_ADDR1.name(),
                FlatUserFieldTypes.PHONE.toString(),
                FlatUserFieldTypes.PHONE_1.toString());
        // And yet another variation of "CITY/PROV" not "CITY/STATE".
        formattedCustomer.renameField(
                FlatUserExtendedFieldTypes.USER_ADDR1.name(),
                FlatUserFieldTypes.CITY_SLASH_STATE.toString(),
                FlatUserFieldTypes.CITY_SLASH_PROV.toString());
        
        // Here we will compute USER_CATEGORY3: sex. Test the 
        // unformattedCustomer for sex and set it like you set USER_CATEGORY2
        if (rawCustomer.get(CustomerFieldTypes.SEX).startsWith("F"))
        {
            formattedCustomer.insertValue(
                FlatUserExtendedFieldTypes.USER.name(), 
                FlatUserFieldTypes.USER_CATEGORY3.toString(), 
                "FEMALE"
            );
        }
        else if (rawCustomer.get(CustomerFieldTypes.SEX).startsWith("M"))
        {
            formattedCustomer.insertValue(
                FlatUserExtendedFieldTypes.USER.name(), 
                FlatUserFieldTypes.USER_CATEGORY3.toString(), 
                "MALE"
            );
        }
        // else CPL doesn't have a category of unknown, so either MALE, FEMALE
        // or no USER_CATEGORY3 at all.

        
        formattedCustomer.insertValue(
            FlatUserExtendedFieldTypes.USER.name(),
            FlatUserFieldTypes.USER_CATEGORY1.toString(),
            "CPLAWB" // TODO test for default load.
        );
        
        // This doesn't load by default, but I don't have time to find out why,
        // this will force the issue.
        formattedCustomer.insertValue(
            FlatUserExtendedFieldTypes.USER.name(),
            FlatUserFieldTypes.USER_CATEGORY4.toString(),
            "ALL"
        );
        
        // Suppress preferred user name.
        formattedCustomer.removeField(
            FlatUserExtendedFieldTypes.USER.name(),
            FlatUserFieldTypes.USER_PREFERRED_NAME.toString()
        );
    }
}
