/*
* Metro allows customers from any affiliate library to join any other member library.
*    Copyright (C) 2022  Edmonton Public Library
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

import mecard.Response;
import mecard.ResponseTypes;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;
import site.CustomerLoadNormalizer;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * @author Andrew Nisbet andrew at dev-ils.com
 */
public final class WBRLCustomerNormalizer extends CustomerLoadNormalizer
{    
    public WBRLCustomerNormalizer(boolean debug)
    {
        super(debug);
    }

    /**
     * Call this if you need to normalize the customer data, but only on 
     * a create request.
     * 
     * @param customer
     * @param response 
     */
    @Override
    public void normalizeOnCreate(Customer customer, Response response) 
    {
        // Nothing to do.
    }

    /**
     * Call this if you need to normalize customer information on update only.
     * @param customer
     * @param response 
     */
    @Override
    public void normalizeOnUpdate(Customer customer, Response response) 
    {
        // Nothing to do.
    }

    /**
     * Call this if you need to signal a different response type based on 
     * customer data and any additional information received in a response string.
     * 
     * This is typically used to change the response type of new Horizon 
     * customers to PIN_CHANGE_REQUIRED. 
     * 
     * @param customer
     * @param responseStringBuilder
     * @return 
     */
    @Override
    public ResponseTypes normalize(Customer customer, StringBuilder responseStringBuilder)
    {
        // You would change this if you wanted to signal some event to the 
        // customer like PIN_CHANGE_REQUIRED.
        ResponseTypes rType = ResponseTypes.SUCCESS;
        return rType;
    }

    /**
     * This method applies all the final touches to the customer's account pre-
     * loading to the ILS. This is where default values are applied, bStats are 
     * computed, dates are converted to timestamps etc. 
     * It is the last stop before loading.
     * 
     * @param unformattedCustomer The raw customer data in MeCard format. Often
     * used as reference or in conversion from one data type to another.
     * @param formattedCustomer The customer in native format. In this case 
     * PAPI XML.
     * @param response the response, which may be altered to signal changes to
     * the ME Library's web site and customer, but is usually untouched.
     */
    @Override
    public void finalize(Customer unformattedCustomer, MeCardCustomerToNativeFormat formattedCustomer, Response response)
    {    
        // All PAPI sites use the same date formatting, gender formatting etc.
        // so all this code is being moved up to the MeCardCustomerToPapi class.
        //
        // 2020-02-11 WBRL does not support gender. There is code to handle 
        // gender in a Polaris-like manner, but only if the value is NOT empty.
        // so here any gender value is ignored and a default field value is stored.
        // 
                
//        // Privilege expiry logic. Use the customer's expiry and if one isn't 
//        // set set it to expire in a year.
//        String expiry = unformattedCustomer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES);
//        if (expiry.isEmpty())
//        {
//            expiry = DateComparer.getFutureDate(365);
//            try
//            {
//                expiry = DateComparer.ANSIToConfigDate(expiry);
//            } 
//            catch (ParseException ex)
//            {
//                Logger.getLogger(WBRLCustomerNormalizer.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        else
//        {
//            try
//            {
//                expiry = DateComparer.ANSIToConfigDate(expiry);
//            } 
//            catch (ParseException ex)
//            {
//                Logger.getLogger(WBRLCustomerNormalizer.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        formattedCustomer.setValue(PapiElementOrder.EXPIRATION_DATE.toString(), expiry);
//        
//        // Do the same for Birthday, but birthday can be blank
//        String birthday = unformattedCustomer.get(CustomerFieldTypes.DOB);
//        if (! birthday.isEmpty())
//        {
//            try
//            {
//                birthday = DateComparer.ANSIToConfigDate(birthday);
//                formattedCustomer.setValue(PapiElementOrder.BIRTHDATE.toString(), birthday);
//            } 
//            catch (ParseException ex)
//            {
//                Logger.getLogger(WBRLCustomerNormalizer.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        
//        // Finally make sure the postal code has a space.
//        //    postal codes. T9H5C5 != T9H 5C5.
//        String postalCode = unformattedCustomer.get(CustomerFieldTypes.POSTALCODE);
//        // Shouldn't be empty it is a required field, but guard for it just in case.
//        if (! postalCode.isEmpty())
//        {
//            formattedCustomer.setValue(PapiElementOrder.POSTAL_CODE.toString(), 
//                PostalCode.formatPostalCode(postalCode));
//        }
    }
}
