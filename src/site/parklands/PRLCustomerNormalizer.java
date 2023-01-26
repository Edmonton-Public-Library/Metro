/*
* Metro allows customers from any affiliate library to join any other member library.
*    Copyright (C) 2019  Edmonton Public Library
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

import mecard.Response;
import mecard.ResponseTypes;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;
import site.CustomerLoadNormalizer;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account.
 * @author Andrew Nisbet <andrew.nisbet@epl.ca>
 */
public final class PRLCustomerNormalizer extends CustomerLoadNormalizer
{    
    public PRLCustomerNormalizer(boolean debug)
    {
        super(debug);
    }
    
    @Override
    public void finalize(Customer unformattedCustomer, MeCardCustomerToNativeFormat formattedCustomer, Response response)
    {
                // add User1 - User5 and any other fields.
//        formattedCustomer.insertValue(
//                PolarisTable.PATRON_REGISTRATION,
//                PolarisTable.PatronRegistration.USER_1.toString(), 
//                "Not in the List");
//        formattedCustomer.insertValue(
//                PolarisTable.PATRON_REGISTRATION,
//                PolarisTable.PatronRegistration.USER_2.toString(), 
//                null);
//        formattedCustomer.insertValue(
//                PolarisTable.PATRON_REGISTRATION,
//                PolarisTable.PatronRegistration.USER_3.toString(), 
//                null);
//        formattedCustomer.insertValue(
//                PolarisTable.PATRON_REGISTRATION,
//                PolarisTable.PatronRegistration.USER_4.toString(), 
//                "(none)");
//        formattedCustomer.insertValue(
//                PolarisTable.PATRON_REGISTRATION,
//                PolarisTable.PatronRegistration.USER_5.toString(), 
//                "(none)");
// Default phone numbers '000-000-0000' could be problematic let's get rid of them here
//        Phone phone = new Phone(customer.get(CustomerFieldTypes.PHONE));
//        if (phone.isUnset())
//        {
//              // Here we remove, but it may be better to insert a different value.
//            formattedCustomer.removeField(
//                    PolarisTable.PATRON_REGISTRATION, 
//                    PolarisTable.PatronRegistration.PHONE_VOICE_1.toString());
//        }
// 2019-12-04 PRL would like it to default to n/a or GenderID=1, .
// Update: 2020-01-20: GenderID        Description (From the 'Genders' table as of Polaris 6.3).
//                     1                      N/A
//                     2                      Female
//                     3                      Male 
// From the schema:
// GENDER("GenderID"), //, 1, char, 1, 1, null, null, 1, null, null, 1, null, 1, 22, YES, See PolarisTable.java.
//        String sex = unformattedCustomer.get(CustomerFieldTypes.SEX);
//        switch (sex)
//        {
//            case "M":
//                formattedCustomer.insertValue(
//                    PolarisTable.PATRON_REGISTRATION,
//                    PolarisTable.PatronRegistration.GENDER.toString(), 
//                    "3");
//                break;
//            case "F":
//                formattedCustomer.insertValue(
//                    PolarisTable.PATRON_REGISTRATION,
//                    PolarisTable.PatronRegistration.GENDER.toString(), 
//                    "2");
//                break;
//            default:
//                formattedCustomer.insertValue(
//                    PolarisTable.PATRON_REGISTRATION,
//                    PolarisTable.PatronRegistration.GENDER.toString(), 
//                    "1");
//                break;
//        }
//        if (unformattedCustomer.isEmpty(CustomerFieldTypes.PRIVILEGE_EXPIRES))
//        {
//            String expiry = DateComparer.getFutureDate(365);
//            try
//            {
//                expiry = DateComparer.ANSIToConfigDate(expiry);
//            } 
//            catch (ParseException ex)
//            {
//                Logger.getLogger(PRLCustomerNormalizer.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            formattedCustomer.insertValue(
//                PolarisTable.PATRON_REGISTRATION,
//                PolarisTable.PatronRegistration.EXPIRATION_DATE.toString(),
//                expiry);
//        }
    }

    @Override
    public ResponseTypes normalize(Customer c, StringBuilder r)
    {
        return ResponseTypes.SUCCESS; // no special rules for Parkland Regional.
    }

    @Override
    public void normalizeOnCreate(Customer customer, Response response)
    {    }

    @Override
    public void normalizeOnUpdate(Customer customer, Response response)
    {    }
}
