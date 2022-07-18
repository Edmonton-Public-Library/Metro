/*
* Metro allows customers from any affiliate library to join any other member library.
*    Copyright (C) 2021  Edmonton Public Library
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
package site.strathcona;

import java.text.ParseException;
import java.util.Date;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.util.DateComparer;
import site.HorizonNormalizer;
import mecard.customer.MeCardCustomerToNativeFormat;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public final class STRCustomerNormalizer extends HorizonNormalizer
{
    public STRCustomerNormalizer(boolean debug)
    {
        super(debug);
    }
    
    @Override
    public ResponseTypes normalize(Customer c, StringBuilder responseStringBuilder)
    {
        ResponseTypes rType = ResponseTypes.SUCCESS;
        // Strathcona use UPPERCASE Name and Address for their customers, to make
        // it easier to spot STR and FTS customers.
        // We change the first and last name, but note this does not effect PREFEREDNAME.
        String customerData = c.get(CustomerFieldTypes.FIRSTNAME).toUpperCase();
        c.set(CustomerFieldTypes.FIRSTNAME, customerData);
        customerData = c.get(CustomerFieldTypes.LASTNAME).toUpperCase();
        c.set(CustomerFieldTypes.LASTNAME, customerData);
        customerData = c.get(CustomerFieldTypes.STREET).toUpperCase();
        c.set(CustomerFieldTypes.STREET, customerData);
        // We deliberately don't do City or Province because 1) it is just a visual
        // queue for the staff, 2) there may be translation issues for finding
        // the customer's city in the city_st table. CONFIRMED: there is no
        // translation problem since the City class does a lookup case insensitively
        // but I still recommend not converting case, since it effects other libraries
        // records.
        return rType;
    }
    
    @Override
    public void finalize(Customer unformattedCustomer, MeCardCustomerToNativeFormat formattedCustomer, Response response)
    {
        // Test and hash the user's password to a 4-digit number if necessary.
        super.finalize(unformattedCustomer, formattedCustomer, response);
        // STR has some special issues related to bStat.
        //        If the patron is 65 or older and their barcode starts with 23877, the bstat should be fssen
        //        If the patron is under 65 and their barcode starts with 23877, the bstat should be fsadu
        //
        //        If the patron is 65 or older and their barcode starts with 21974, the bstat should be s
        //        If the patron is under 65 and their barcode starts with 21974, the bstat should be 'a'.
        if (unformattedCustomer.isEmpty(CustomerFieldTypes.DOB) == false)
        {
            String dob = unformattedCustomer.get(CustomerFieldTypes.DOB);
            try
            {
                if (DateComparer.getYearsOld(dob) >= SENIOR)
                {
                    addBStatTable(formattedCustomer, "s");
                    return;
                }
            } 
            catch (ParseException ex)
            {
                System.out.println(new Date() 
                        + " STR normalizer couldn't parse dob: '" + dob + "'");
                addBStatTable(formattedCustomer, "a");
                return;
            }
        }
        addBStatTable(formattedCustomer, "a");
        // At Jennifer's request add bstat 'nr' for all metro reg customers.
        addBStatTable(formattedCustomer, "nr");
    }

    @Override
    public void normalizeOnCreate(Customer customer, Response response) {
        // Nothing to do.
    }

    @Override
    public void normalizeOnUpdate(Customer customer, Response response) {
        // Nothing to do.
    }
}
