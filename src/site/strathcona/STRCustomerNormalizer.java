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
package site.strathcona;

import java.text.ParseException;
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
import mecard.util.DateComparer;
import mecard.util.Phone;
import mecard.util.PostalCode;
import mecard.util.Text;
import site.CustomerLoadNormalizer;
import static site.stalbert.STACustomerNormalizer.SENIOR;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class STRCustomerNormalizer extends CustomerLoadNormalizer
{
    public final static int MAXIMUM_PIN_WIDTH = 4;
    private final boolean debug;
    
    public STRCustomerNormalizer(boolean debug)
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
            responseStringBuilder.append(newPin); // Send back new PIN to melibraries.ca.
            c.set(CustomerFieldTypes.PIN, newPin);
            System.out.println(new Date() + " Customer's PIN was not 4 digits as required by Horizon. Set to: '" 
                    + newPin + "'.");
            rType = ResponseTypes.PIN_CHANGE_REQUIRED;
        }
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
        // STR has some special issues related to bStat.
        //        If the patron is 65 or older and their barcode starts with 23877, the bstat should be fssen
        //        If the patron is under 65 and their barcode starts with 23877, the bstat should be fsadu
        //
        //        If the patron is 65 or older and their barcode starts with 21974, the bstat should be s
        //        If the patron is under 65 and their barcode starts with 21974, the bstat should be a 
        if (unformattedCustomer.isEmpty(CustomerFieldTypes.DOB) == false)
        {
            String dob = unformattedCustomer.get(CustomerFieldTypes.DOB);
            String userId = unformattedCustomer.get(CustomerFieldTypes.ID);
            try
            {
                if (DateComparer.getYearsOld(dob) >= SENIOR)
                {
                    if (userId.startsWith("23877"))
                    {
                        addBStatTable(formattedCustomer, "fssen");
                    }
                    else if (userId.startsWith("21974"))
                    {
                        addBStatTable(formattedCustomer, "s");
                    }
                }
                else // Regular adult account.
                {
                    if (userId.startsWith("23877"))
                    {
                        addBStatTable(formattedCustomer, "fsadu");
                    }
                    else if (userId.startsWith("21974"))
                    {
                        addBStatTable(formattedCustomer, "a");
                    }
                }
            } catch (ParseException ex)
            {
                System.out.println(new Date() 
                        + " STR normalizer couldn't parse dob: '" + dob + "'");
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
