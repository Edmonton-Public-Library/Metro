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
package site.trac;

import api.CustomerMessage;
import api.SIPMessage;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.util.DateComparer;
import site.CustomerGetNormalizer;
import site.MeCardPolicy;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class TRACCustomerGetNormalizer extends CustomerGetNormalizer
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

//        sent:93  CNuser|COpassword|CP|AY0AZF5D3
//        recv:64              00120140501    114114000000000000000000000000AO203|AA29335002291042|AEHunting, Will|BZ0025|CA0010|CB0100|BLY|CQY|BHCAD|BV0.00|CC10.00|BD433 King Street, Spruce Grove, AB T7X 3B4|BEsthero@yrl.ab.ca|BC|PA17|PEASGY|PSAdult (18-64)|U4(none)|U5|PZT7X 3B4|PX20201025    235959|PYN|AFPatron status is ok.|AGPatron status is ok.|AY2AZA00A
        // TRAC doesn't collect sex.
        // Their expiry is in field 'PX': 'PX20201025    235959'
        String expireDate = SIPMessage.cleanDateTime(message.getField("PX"));
        if (SIPMessage.isDate(expireDate))
        {
            customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, expireDate);
        }
    }
}
