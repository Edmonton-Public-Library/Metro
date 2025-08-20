/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025 Edmonton Public Library
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
package mecard.calgary.cplapi;

import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.customer.MeCardDataToNativeData;


/**
 * The class is used just before the customer is loaded so a library can add
 * additional data before customer creation. Some libraries like to add analytic
 * data before customer load, others rename fields to suit the local requirements.
 * This object takes customer data and converts it into Calgary Public Library's
 * custom web service API.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class MeCardCustomerToCPLapi 
{
    private MeCardDataToNativeData customerTable;
    
    public MeCardCustomerToCPLapi(Customer customer, MeCardDataToCPLapiData.QueryType type)
    {
        // Read the cplapi specific properties from the cplapi.properties file.
        Properties props = PropertyReader.getProperties(ConfigFileTypes.CPL_API);
        customerTable = MeCardDataToCPLapiData.getInstanceOf(type);
    }
}
