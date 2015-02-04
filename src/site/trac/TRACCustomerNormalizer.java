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

import mecard.Response;
import mecard.config.CustomerFieldTypes;
import mecard.config.PolarisTable;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import mecard.util.Phone;
import site.PolarisNormalizer;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class TRACCustomerNormalizer extends PolarisNormalizer
{
    
    public TRACCustomerNormalizer(boolean debug)
    {
        super(debug);
    }

    @Override
    public void finalize(Customer customer, FormattedCustomer formattedCustomer, Response response)
    {   
        // add User1 - User5 and any other fields.
        formattedCustomer.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.USER_1.toString(), 
                "Not in the List");
        formattedCustomer.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.USER_2.toString(), 
                null);
        formattedCustomer.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.USER_3.toString(), 
                null);
        formattedCustomer.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.USER_4.toString(), 
                "(none)");
        formattedCustomer.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.USER_5.toString(), 
                "(none)");
        // Default phone numbers '000-000-0000' could be problematic let's get rid of them here
//        Phone phone = new Phone(customer.get(CustomerFieldTypes.PHONE));
//        if (phone.isUnset())
//        {
//              // Here we remove, but it may be better to insert a different value.
//            formattedCustomer.removeField(
//                    PolarisTable.PATRON_REGISTRATION, 
//                    PolarisTable.PatronRegistration.PHONE_VOICE_1.toString());
//        }
    }
    
    @Override
    public void normalizeOnCreate(Customer customer, Response response)
    {
        // No special action required at this time.
    }

    @Override
    public void normalizeOnUpdate(Customer customer, Response response)
    {
        // No special action required at this time.
    }
}
