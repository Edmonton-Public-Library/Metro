/*
 * Metro allows customers from any affiliate library to join any other member library.
<<<<<<< HEAD
 *    Copyright (C) 2022 - 2025  Edmonton Public Library
=======
 *    Copyright (C) 2022 - 2025 Edmonton Public Library
>>>>>>> master
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
package site.woodbuffalo;

import api.CustomerMessage;
import java.text.ParseException;
import mecard.Protocol;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.config.PapiElementOrder;
import mecard.util.DateComparer;
import site.CustomerGetNormalizer;
import site.MeCardPolicy;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class WBRLCustomerGetNormalizer extends CustomerGetNormalizer
{
    /**
     * This class prepares the customer data to be sent to the ME Libraries
     * web site. 
     * 
     * In the case of SIP2, there is customer meta data from the ILS such as 
     * profile, customer status and such. PAPI, and web service calls generally
     * don't return that kind of information so if the environment.properties
     * specify 'polaris-api' for getCustomer requests, the standing and age 
     * restrictions of a customer must be considered here and added to the 
     * customer before it is sent back to the ME Libraries web site. 
     * 
     * interpretation of {@link CustomerMessage}s.
     * @param customer
     * @param message 
     */
    @Override
    public void setCustomerValuesFromSiteSpecificFields(
            Customer customer, 
            CustomerMessage message)
    {
        // Now we know that PAPI returns time stamps, let's convert them to 
        // ME Card (ANSI) time.
        String polarisExpiry = message.getDateField(PapiElementOrder.EXPIRATION_DATE.toString());
        String ansiExpiry = DateComparer.getANSIDate(polarisExpiry);
        customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, DateComparer.computeExpiryDate(ansiExpiry));
        
        String polarisBirthday = message.getDateField(PapiElementOrder.BIRTHDATE.toString());
        String ansiBirthday = DateComparer.getANSIDate(polarisBirthday);
        customer.set(CustomerFieldTypes.DOB, ansiBirthday);
        if (DateComparer.isAnsiDate(ansiBirthday))
        {
            // Test if the customer is minimum age.
            try
            {
                if (DateComparer.getYearsOld(ansiBirthday) >= MeCardPolicy.minimumAge())
                {
                    customer.set(CustomerFieldTypes.ISMINAGE, Protocol.TRUE);
                }
            }
            catch (ParseException ex)
            {
                // _Shouldn't_ get here but never say never.
                System.out.println("*error, funny thing; the customer's"
                    + " age couldn't be calculated from: "
                    + ansiBirthday);
                customer.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
            }
        }
        else // No birthday, no registration.
        {
            customer.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
        }
        
        String gender = message.getField(PapiElementOrder.GENDER.toString());
        switch (gender)
        {
            case "M" -> customer.set(CustomerFieldTypes.SEX, "M");
            case "F" -> customer.set(CustomerFieldTypes.SEX, "F");
            default -> // anything else is just not specified.
                customer.set(CustomerFieldTypes.SEX, "X");
        }
        
        // Some of these may appear in note fields, but until we talk to WBRL
        // and see some data, we'll err on the side of allowing membership. 
        customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.TRUE);
        customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.TRUE);
        customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.FALSE);
        customer.set(CustomerFieldTypes.ISLOSTCARD, Protocol.FALSE);
        customer.set(CustomerFieldTypes.ISVALID, Protocol.TRUE);
    }
}
