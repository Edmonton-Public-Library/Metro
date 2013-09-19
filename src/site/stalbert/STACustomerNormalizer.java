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
package site.stalbert;

import java.util.Date;
import java.util.HashMap;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.BImportDBFieldTypes;
import mecard.config.BImportTableTypes;
import mecard.config.CustomerFieldTypes;
import mecard.customer.BImportTable;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import mecard.customer.FormattedTable;
import mecard.util.Phone;
import mecard.util.PostalCode;
import mecard.util.Text;
import site.CustomerLoadNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class STACustomerNormalizer extends CustomerLoadNormalizer
{
    public final static int MAXIMUM_PIN_WIDTH = 4;
    private final boolean debug;
    public final static int SENIOR = 65;
    
    public STACustomerNormalizer(boolean debug)
    {
        this.debug = debug;
    }
    
    @Override
    public ResponseTypes normalize(Customer c, StringBuilder responseStringBuilder)
    {
        ResponseTypes rType = ResponseTypes.SUCCESS;
        String pin = c.get(CustomerFieldTypes.PIN);
        if (Text.isMaximumDigits(pin, MAXIMUM_PIN_WIDTH) == false)
        {
            String newPin = Text.getNew4DigitPin();
            responseStringBuilder.append(newPin); // send back the new PIN
            c.set(CustomerFieldTypes.PIN, newPin);
            System.out.println(new Date() + " Customer's PIN was not 4 digits as required by Horizon. Set to: '" 
                    + newPin + "'.");
            rType = ResponseTypes.PIN_CHANGE_REQUIRED;
        }
        return rType;
    }

    @Override
    public void finalize(Customer unformattedCustomer, FormattedCustomer formattedCustomer, Response response)
    {
        // Next tidy up fields so they look nicer
        // Phone
        String phoneField = formattedCustomer.getValue(BImportDBFieldTypes.PHONE_NUMBER.name());
        phoneField = Phone.formatPhone(phoneField);
        formattedCustomer.setValue(BImportDBFieldTypes.PHONE_NUMBER.name(), phoneField);
        // PCode
        String postalCode = formattedCustomer.getValue(BImportDBFieldTypes.POSTAL_CODE.name());
        postalCode = PostalCode.formatPostalCode(postalCode);
        formattedCustomer.setValue(BImportDBFieldTypes.POSTAL_CODE.name(), postalCode);
        // They also set bstat for sex
        if (unformattedCustomer.isEmpty(CustomerFieldTypes.SEX) == false)
        {
            String sex = unformattedCustomer.get(CustomerFieldTypes.SEX);
            if (sex.compareToIgnoreCase("M") == 0)
            {
                addBStatTable(formattedCustomer, "m");
            }
            else if (sex.compareToIgnoreCase("F") == 0)
            {
                addBStatTable(formattedCustomer, "f");
            }
            else
            {
                addBStatTable(formattedCustomer, "unknown");
            }
        }
    }

    /**
     * Creates and inserts a new table entry into the bimport file.
     * @param formattedCustomer
     * @param value 
     */
    private void addBStatTable(FormattedCustomer formattedCustomer, String value)
    {
        FormattedTable table = BImportTable.getInstanceOf(
                BImportTableTypes.BORROWER_BSTAT, new HashMap<String, String>());
        table.setValue(BImportTableTypes.BORROWER_BSTAT.toString(), value);
        formattedCustomer.insertTable(table, 99); // insert at the end.
    }
}
