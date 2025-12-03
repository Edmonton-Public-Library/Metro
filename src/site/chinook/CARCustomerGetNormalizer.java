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
package site.chinook;

import api.CustomerMessage;
import java.util.Properties;
import mecard.Protocol;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiUserFields;
import mecard.config.SupportedProtocolTypes;
import mecard.customer.Customer;
import site.CustomerGetNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class CARCustomerGetNormalizer extends CustomerGetNormalizer
{
    /**
     * This is the default class for a library that does not require custom
     * interpretation of {@link CustomerMessage}s.
     * @param customer
     * @param message 
     */
    @Override
    public void setCustomerValuesFromSiteSpecificFields(
            Customer customer, 
            CustomerMessage message)
    {
        Properties envProperties = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        if (envProperties.getProperty(LibraryPropertyTypes.GET_SERVICE.toString())
                .equalsIgnoreCase(SupportedProtocolTypes.SIP2.toString()))
        {
            // parse the string appart.
            //        sent:63                               AO|AA25021000719291|AD1861|AY1AZF3AF
            //
            //        recv:64              00020140304    070512000000000000000000000000AO|AA21817002446849|AEGAMACHE, ARMAND|AQLPL|BZ9999|CA9999|CB9999|BLY|CQY|BV 0.00|BDBOX 43 LETHBRIDGE, ALBERTA T1J 3Y3 403-555-1234|BEpwauters@hotmail.com|BF403-555-1234|BHUSD|PA20150218    235900|PD|PCLPLADULT|PELETHCITY|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY0AZACA0

            // here we will fill in the customer attributes with the contents of s- the SIP message.
            // TODO: Shortgrass uses "PG" for "MALE" or "FEMALE"
            String sex = message.getField("PG");
            if (sex.isEmpty() == false)
            {
                if (sex.compareToIgnoreCase("MALE") == 0)
                {
                    // send the first letter, "M" or "F"
                    customer.set(CustomerFieldTypes.SEX, "M");
                }
                else if (sex.compareToIgnoreCase("FEMALE") == 0)
                {
                    // send the first letter, "M" or "F"
                    customer.set(CustomerFieldTypes.SEX, "F");
                }
            }
        }
        else if (envProperties.getProperty(LibraryPropertyTypes.GET_SERVICE.toString())
                .equalsIgnoreCase(SupportedProtocolTypes.SIRSIDYNIX_API.toString()))
        {
            String gender = message.getField(SDapiUserFields.CATEGORY03.toString());
            if (! gender.isBlank())
            {
                if (gender.compareToIgnoreCase("MALE") == 0)
                {
                    // send the first letter, "M" or "F"
                    gender = Protocol.MALE;
                }
                else if (gender.compareToIgnoreCase("FEMALE") == 0)
                {
                    gender = Protocol.FEMALE;
                }
                else
                {
                    gender = Protocol.DEFAULT_FIELD_VALUE;
                }
            }
            else
            {
                gender = Protocol.DEFAULT_FIELD_VALUE;
            }
            customer.set(CustomerFieldTypes.SEX, gender);
        }
    }
}
