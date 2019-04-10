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
package site.parklands;

import api.CustomerMessage;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
//import mecard.util.DateComparer;
import site.CustomerGetNormalizer;

/**
 * This class normalizes information contained in the customer message received
 * from a source for get customer info (usually SIP2). The messages that are
 * returned frequently have to be translated from the source to the customer
 * object. For example, a SIP2 source for Horizon specifies that 'PE' is used 
 * for customer expiry but 'PA' is what the spec says is customer expiry.
 * 
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PKLCustomerGetNormalizer extends CustomerGetNormalizer
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
        /********************* Polaris settings *********************/
        /*
        * For the migration to Polaris we use the SIP setting used by 
        * TRAC's SIP server since Polaris uses different fields for 
        * expiry and DOB.
        * 
        */
        // --> 6300120190228    162220Y         AO|AA21000008565504|AD9156|BP1|BQ5|AY6AZEFB6
        // <-- 64              00120190228    162221000700000000000000000000AO3|AA21000008565504|AEBRODY, MIKE N|BZ0250|CA0010|CB0050|BLY|CQY|BHCAD|BV0.00|CC24.99|AS31000044015736|AS31000040182910|AS31000036558768|AS31000035787863|AS31000007295291|BD3 4546 IRON WOLF PL, Lacombe, AB T4L 1G1|BEtspark@shaw.ca|BF403-302-9156|BC|PA13|PEalap|PSLacombe - City|U1|U2|U3|U4|U5|PZT4L 1G1|PX20190913    235959|PYN|FA0.00|PC70493|PB21000008565504|AFPatron status is ok.|AGPatron status is ok.|AY6AZ83AA
        customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, message.getDateField("PX"));
        customer.set(CustomerFieldTypes.DOB, message.getDateField("BC"));
        /********************* Polaris settings *********************/
    }
}
