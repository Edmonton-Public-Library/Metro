/*
* Metro allows customers from any affiliate library to join any other member library.
*    Copyright (C) 2023  Edmonton Public Library
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

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PolarisTable;
import mecard.customer.Customer;
import mecard.util.DateComparer;
import mecard.customer.MeCardCustomerToNativeFormat;
import site.CustomerLoadNormalizer;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * @author Andrew Nisbet andrew@dev-ils.com
 * @since v1.0 2013
 */
public final class TRACCustomerNormalizer extends CustomerLoadNormalizer
{
    
    public TRACCustomerNormalizer(boolean debug)
    {
        super(debug);
    }

    @Override
    public void finalize(Customer customer, MeCardCustomerToNativeFormat formattedCustomer, Response response)
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
// 2020-02-11 TRAC gender mapping below. ME libraries only passes 'M', 'F', or 'X'
// for not supplied.
// Update: 2020-01-20: GenderID Description (From the 'Genders' table as of Polaris 6.3).
// GenderID = 1 N/A
// GenderID = 2 Female
// GenderID = 3 Male
// GenderID = 4 Non-binary
// GenderID = 5 Other
// From the schema:
// This looks like the output of a discribe table, but Richard Kenig says the GenderID is a regular integer.
// GENDER("GenderID"), //, 1, char, 1, 1, null, null, 1, null, null, 1, null, 1, 22, YES, See PolarisTable.java.
        String sex = customer.get(CustomerFieldTypes.SEX);
        switch (sex)
        {
            case "M":
                formattedCustomer.insertValue(
                    PolarisTable.PATRON_REGISTRATION,
                    PolarisTable.PatronRegistration.GENDER_ID.toString(), 
                    "3");
                break;
            case "F":
                formattedCustomer.insertValue(
                    PolarisTable.PATRON_REGISTRATION,
                    PolarisTable.PatronRegistration.GENDER_ID.toString(), 
                    "2");
                break;
            default:
                formattedCustomer.insertValue(
                    PolarisTable.PATRON_REGISTRATION,
                    PolarisTable.PatronRegistration.GENDER_ID.toString(), 
                    "1");
                break;
        }
        // Privilege expiry logic. Use the customer's expiry and if one isn't 
        // set set it to expire in a year.
        if (customer.isEmpty(CustomerFieldTypes.PRIVILEGE_EXPIRES))
        {
            String expiry = DateComparer.getFutureDate(365);
            try
            {
                expiry = DateComparer.ANSIToConfigDate(expiry);
            } 
            catch (ParseException ex)
            {
                Logger.getLogger(TRACCustomerNormalizer.class.getName()).log(Level.SEVERE, null, ex);
            }
            formattedCustomer.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.EXPIRATION_DATE.toString(),
                expiry);
        }
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
    
    @Override
    public ResponseTypes normalize(Customer c, StringBuilder r)
    {
        return ResponseTypes.SUCCESS; // no special rules for TRAC.
    }
}
