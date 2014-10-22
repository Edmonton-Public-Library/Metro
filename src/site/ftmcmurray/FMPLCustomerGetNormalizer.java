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
package site.ftmcmurray;

import api.CustomerMessage;
import api.SIPMessage;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.util.DateComparer;
import site.CustomerGetNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class FMPLCustomerGetNormalizer extends CustomerGetNormalizer
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
        // AF field will contain '#Incorrect password' if the customer enters an invalid pin
        // This gets past from the SIP server if no validation is set.
        if (message.getField("AF").compareToIgnoreCase("#Incorrect password") == 0)
        {
            customer.set(CustomerFieldTypes.RESERVED, "Invalid PIN for station user");
        }
        // Now we know that Horizon uses 'PE' for expiry but 'PA' is the industrial 
        // norm, so let's fix that here.
        String cleanDate = DateComparer.cleanDateTime(message.getField("PE"));
        if (DateComparer.isDate(cleanDate))
        {
            customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, cleanDate);
        }
    }
}
