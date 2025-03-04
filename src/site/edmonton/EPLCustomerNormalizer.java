/*
* Metro allows customers from any affiliate library to join any other member library.
*    Copyright (C) 2013 - 2025  Edmonton Public Library
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
import mecard.config.ConfigFileTypes;
import mecard.config.FlatUserTableTypes;
import mecard.config.FlatUserFieldTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiUserFields;
import mecard.config.SupportedProtocolTypes;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;
import site.SymphonyNormalizer;

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
    public void finalize(
            Customer unformattedCustomer, 
            MeCardCustomerToNativeFormat formattedCustomer, 
            Response response
    )
    {
        Properties envProperties = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        if (envProperties.getProperty(LibraryPropertyTypes.CREATE_SERVICE.toString())
                .equalsIgnoreCase(SupportedProtocolTypes.SYMPHONY_API.toString()))
        {
            /*
            ** Note that the two methods insertValue() and removeField() work 
            ** whether you use a GSON or FLAT formatted customer because the GSON
            ** object ignores the table name.
            */


            // No matter what ME customer give implied consent to contact via email when they join.
            // Which at EPL, is USER_CATEGORY5.
            // TODO this has to be reworked for use with SDapiCutomerNormalizer.
            formattedCustomer.insertValue(FlatUserTableTypes.USER.name(), 
                        FlatUserFieldTypes.USER_CATEGORY5.toString(), 
                        "ECONSENT");
            // Suppress preferred user name.
            formattedCustomer.removeField(FlatUserTableTypes.USER.name(),
                FlatUserFieldTypes.USER_PREFERRED_NAME.toString()
            );

            this.loadDefaultProfileAttributes(unformattedCustomer, formattedCustomer, response);
            // Do this last to ensure that any new pasword is emailed to the customer.
            this.testSiteRestrictedPassword(unformattedCustomer, formattedCustomer, response);
        }
        else if (envProperties.getProperty(LibraryPropertyTypes.CREATE_SERVICE.toString())
                .equalsIgnoreCase(SupportedProtocolTypes.SIRSIDYNIX_API.toString()))
        {
            formattedCustomer.insertValue("", SDapiUserFields.CATEGORY05.toString(), "ECONSENT");
            formattedCustomer.removeField("", SDapiUserFields.USE_PREFERRED_NAME.toString());
        }
    }
}
