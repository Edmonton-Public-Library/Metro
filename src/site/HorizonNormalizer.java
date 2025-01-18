/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2021 - 2025  Edmonton Public Library
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

import java.util.Date;
import java.util.HashMap;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.BImportDBFieldTypes;
import mecard.config.BImportTableTypes;
import mecard.config.CustomerFieldTypes;
import mecard.horizon.MeCardDataToBImportData;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.MeCardDataToNativeData;
import mecard.security.SitePasswordRestrictions;

/**
 * Horizon common normalization algorithms. This includes things like making
 * sure users' pins are 4 digits.
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public abstract class HorizonNormalizer extends CustomerLoadNormalizer
{
    public final static int SENIOR = 65;
    
    protected final SitePasswordRestrictions passwordChecker;
    
    protected HorizonNormalizer(boolean debug)
    {
        super(debug);
        this.passwordChecker = new SitePasswordRestrictions();
    }
    
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
     * This called during the createCustomer directly and indirectly by the 
     * updateCustomer command.
     * @param unformattedCustomer
     * @param formattedCustomer
     * @param response 
     */
    @Override
    public void finalize(Customer unformattedCustomer, MeCardCustomerToNativeFormat formattedCustomer, Response response)
    {  
        /*** Be sure to call super.finalize() in inherited classes. See 
         * SAPLCustomerNormalizer as an example. 
         ***/
        ResponseTypes rType = ResponseTypes.SUCCESS;
        // If the unformattedCustomer's PIN is a 4-digit PIN then don't hash it, otherwise
        // hash it and signal that in the response message.
        String password = unformattedCustomer.get(CustomerFieldTypes.PIN);
        if (this.passwordChecker.requiresHashedPassword(password))
        {
            password = this.passwordChecker.checkPassword(password);
            formattedCustomer.setValue(BImportDBFieldTypes.PIN.toString(), password);
            System.out.println(new Date()
                + " Customer's PIN was not 4 digits as required by Horizon. "
                + " HN.finalize() set to: '" 
                + password + "'.");
            rType = ResponseTypes.PIN_CHANGE_REQUIRED;
            response.setResponse(password);
        }
        response.setCode(rType);
    }
    
    /**
     * Creates and inserts a new table entry into the bimport file.
     * @param formattedCustomer
     * @param value 
     */
    protected void addBStatTable(MeCardCustomerToNativeFormat formattedCustomer, String value)
    {
        MeCardDataToNativeData table = MeCardDataToBImportData.getInstanceOf(
                BImportTableTypes.BORROWER_BSTAT, new HashMap<>());
        table.setValue("bstat", value);
        formattedCustomer.insertTable(table, 99); // insert at the end.
    }
}
