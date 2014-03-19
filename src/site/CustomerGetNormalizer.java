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

import api.CustomerMessage;
import mecard.customer.Customer;
import mecard.customer.Emitter;

/**
 * Subclass if your ILS returns messages that are specific to your site, and 
 * conflict with established standards. Example: some sites have SIP2 configured
 * to return values in incorrect fields. Other libraries have things like patron
 * sex returned in SIP2 field "PF", others in "PG". this allows sites to handle
 * as much or as little customization and interpretation as they like before
 * they 'emit' the customer for consumption at another library
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class CustomerGetNormalizer implements Emitter
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
        return;
    }
}
