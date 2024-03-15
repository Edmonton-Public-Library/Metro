/*
* Metro allows customers from any affiliate library to join any other member library.
*    Copyright (C) 2022 - 2024  Edmonton Public Library
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
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.polaris.papi.PapiElementOrder;
import mecard.security.CustomerPasswordRestrictions;
import site.CustomerLoadNormalizer;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * @author Andrew Nisbet andrew at dev-ils.com
 */
public final class WBRLCustomerNormalizer extends CustomerLoadNormalizer
{    
    private final CustomerPasswordRestrictions passwordChecker;
    public WBRLCustomerNormalizer(boolean debug)
    {
        super(debug);
        this.passwordChecker = new CustomerPasswordRestrictions();
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
        // customer like ResponseTypes.PIN_CHANGE_REQUIRED.
        ResponseTypes rType = ResponseTypes.SUCCESS;
        String password = customer.get(CustomerFieldTypes.PIN);
        if ( this.passwordChecker.requiresHashedPassword(password) )
        {
            rType = ResponseTypes.PIN_CHANGE_REQUIRED;
        }
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
        // WBRL uses an unusual PIN requirement of between 4-16 digits. 
        // All others will fail, so convert if necessary and return PIN_CHANGE 
        // ResponseTypes rType = ResponseTypes.PIN_CHANGE_REQUIRED;
        String password = unformattedCustomer.get(CustomerFieldTypes.PIN);
        if ( this.passwordChecker.requiresHashedPassword(password) )
        {
            ResponseTypes rType = ResponseTypes.PIN_CHANGE_REQUIRED;
            password = this.passwordChecker.checkPassword(password);
            formattedCustomer.setValue(PapiElementOrder.PASSWORD.name(), password);
            // This seems to be required or you will get an error -3509 Passwords do not match.
            formattedCustomer.setValue(PapiElementOrder.PASSWORD_2.name(), password);
            response.setResponse(password);
            response.setCode(rType);
        }
        // This is required for all customers at WBRL
        formattedCustomer.setValue(PapiElementOrder.USER1.name(), "Inside Alberta (outside RMWB)");
    }
}
