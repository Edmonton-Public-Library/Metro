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
package site;

import java.util.Date;
import java.util.HashMap;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.BImportTableTypes;
import mecard.config.CustomerFieldTypes;
import mecard.customer.BImportTable;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import mecard.customer.FormattedTable;
import mecard.util.Text;

/**
 * Horizon common normalization algorithms. This includes things like making
 * sure users' pins are 4 digits.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public abstract class HorizonNormalizer extends CustomerLoadNormalizer
{
    public final static int MAXIMUM_PIN_WIDTH = 4;
    public final static int SENIOR = 65;
    
    protected HorizonNormalizer(boolean debug)
    {
        super(debug);
    }
    
    @Override
    public ResponseTypes normalize(Customer c, StringBuilder responseStringBuilder)
    {
        ResponseTypes rType = ResponseTypes.SUCCESS;
        return rType;
    }
    
    @Override
    public void finalize(Customer unformattedCustomer, FormattedCustomer formattedCustomer, Response response)
    {    }
    
    @Override
    public void normalizeOnCreate(Customer c, Response response)
    {
        ResponseTypes rType = ResponseTypes.SUCCESS;
        String pin = c.get(CustomerFieldTypes.PIN);
        if (Text.isMaximumDigits(pin, MAXIMUM_PIN_WIDTH) == false)
        {
            String newPin = Text.getNew4DigitPin();
            response.setResponse(newPin);
            c.set(CustomerFieldTypes.PIN, newPin);
            System.out.println(new Date() 
                    + " Customer's PIN was not 4 digits as required by Horizon. Set to: '" 
                    + newPin + "'.");
            rType = ResponseTypes.PIN_CHANGE_REQUIRED;
        }
        response.setCode(rType);
    }
    
    @Override
    public void normalizeOnUpdate(Customer customer, Response response)
    {
        ResponseTypes rType = ResponseTypes.SUCCESS;
        // Here is a problem: when we create a customer with a pin that is not 
        // 4 digits, we force a pin change, BUT on update you don't want that. 
        // Fortunately, after running some tests, we discovered that Horizon
        // will NOT update the pin field, if it is provided, but the field is blank.
        // That is, Horizon will leave the pin field untouched if the if the over-
        // writing field is blank.
        customer.set(CustomerFieldTypes.PIN, " ");
        response.setCode(rType);
    }
    
    /**
     * Creates and inserts a new table entry into the bimport file.
     * @param formattedCustomer
     * @param value 
     */
    protected void addBStatTable(FormattedCustomer formattedCustomer, String value)
    {
        FormattedTable table = BImportTable.getInstanceOf(
                BImportTableTypes.BORROWER_BSTAT, new HashMap<String, String>());
        table.setValue("bstat", value);
        formattedCustomer.insertTable(table, 99); // insert at the end.
    }
}
