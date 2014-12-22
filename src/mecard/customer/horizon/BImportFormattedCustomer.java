/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013, 2014  Edmonton Public Library
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
package mecard.customer.horizon;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import mecard.config.BImportDBFieldTypes;
import mecard.config.BImportTableTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import mecard.customer.FormattedTable;
import mecard.requestbuilder.BImportRequestBuilder;
import mecard.util.AlbertaCity;
import mecard.util.City;
import mecard.util.DateComparer;
import mecard.util.Phone;
import mecard.util.PostalCode;

/**
 * Instance of a customer formatted for loading.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class BImportFormattedCustomer implements FormattedCustomer
{
    private final List<BImportTable> customerAccount;
    
    public BImportFormattedCustomer(Customer c)
    {
        this.customerAccount = new ArrayList<>();
        // initial formatting of the customer
                // Basic borrower table.
        HashMap<String, String> customerTable = new HashMap<>();
        // Fix lost user cards we do that here:
        //M- borrower: 11-14-1985; 04-09-2015; DXXXXX, TXXXXXX; 8426; 21221000000001 (old barcode)
        //borrower_phone: 780-489-2222; h-noTC
        //borrower_address: 3000 18A STREET NW;  ;8426;tds59@hotmail.com; tds59; T6T 0M4; 1; 1
        //borrower_barcode: 21221000000000 (new barcode)
        //borrower_bstat: a
        if (c.isLostCard())
        {
            customerTable.put(BImportDBFieldTypes.SECOND_ID.toString(), c.get(CustomerFieldTypes.ALTERNATE_ID));
        }
        else
        {
            customerTable.put(BImportDBFieldTypes.SECOND_ID.toString(), c.get(CustomerFieldTypes.ID));
        }
        customerTable.put(BImportDBFieldTypes.NAME.toString(), (c.get(CustomerFieldTypes.LASTNAME) 
                + ", " + c.get(CustomerFieldTypes.FIRSTNAME)));
        try
        {
            String expiry = DateComparer.ANSIToConfigDate(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES));
            customerTable.put(BImportDBFieldTypes.EXPIRY.toString(), expiry);
        } 
        catch (ParseException ex)
        {
            System.out.println(new Date() + " unable to parse expiry '" 
                    + c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES) + "'");
        }
        try
        {
            String dobDate = "19000101";
            if (c.isEmpty(CustomerFieldTypes.DOB) == false)
            {
                dobDate = c.get(CustomerFieldTypes.DOB);
            }
            String insertDate = DateComparer.ANSIToConfigDate(dobDate);
            customerTable.put(BImportDBFieldTypes.BIRTH_DATE.toString(), insertDate);
        } 
        catch (ParseException ex)
        {
            System.out.println(new Date() + " unable to parse DOB '" + c.get(CustomerFieldTypes.DOB) + "'");
        }
        // This is checked because on update of a user account you can clear the
        // pin to protect that field from updating, and if the case where their EPL
        // pin is 'CATS', illegal on Horizon, loading the customer would reset 
        // the pin to a random number.
        if (c.isEmpty(CustomerFieldTypes.PIN) == false)
        {
            customerTable.put(BImportDBFieldTypes.PIN.toString(), c.get(CustomerFieldTypes.PIN));
        }
        customerAccount.add(BImportTable.getInstanceOf(BImportTableTypes.BORROWER_TABLE, customerTable));
        
        // Next the phone table
        customerTable.clear();
        customerTable.put(BImportDBFieldTypes.PHONE_TYPE.toString(), "h-noTC");
        String phone = "780-999-1234";
        if (c.isEmpty(CustomerFieldTypes.PHONE) == false)
        {
            phone = Phone.formatPhone(c.get(CustomerFieldTypes.PHONE));
        }
        else
        {
            Properties bimpProps = PropertyReader.getProperties(ConfigFileTypes.BIMPORT);
            phone = bimpProps.getProperty(BImportRequestBuilder.PHONE_TAG, "780-999-1234");
        }
        customerTable.put(BImportDBFieldTypes.PHONE_NUMBER.toString(), phone);
        customerAccount.add(BImportTable.getInstanceOf(BImportTableTypes.BORROWER_PHONE_TABLE, customerTable));
        
        // Address
        customerTable.clear();
        customerTable.put(BImportDBFieldTypes.ADDRESS_1.toString(), c.get(CustomerFieldTypes.STREET));
        customerTable.put(BImportDBFieldTypes.ADDRESS_2.toString(), " "); // intentionally blank
        // look up the city name in bimport address.
        City city  = AlbertaCity.getInstanceOf();
        String cityCode = city.getCityCode(c.get(CustomerFieldTypes.CITY));
        customerTable.put(BImportDBFieldTypes.CITY.toString(), cityCode);
        customerTable.put(BImportDBFieldTypes.POSTAL_CODE.toString(), PostalCode.formatPostalCode(c.get(CustomerFieldTypes.POSTALCODE)));
        String emailName = this.computeEmailName(c.get(CustomerFieldTypes.EMAIL));
        customerTable.put(BImportDBFieldTypes.EMAIL_NAME.toString(), emailName);
        customerTable.put(BImportDBFieldTypes.EMAIL_ADDRESS.toString(), c.get(CustomerFieldTypes.EMAIL));
        customerTable.put(BImportDBFieldTypes.SEND_PREOVERDUE.toString(), "1");
        customerTable.put(BImportDBFieldTypes.SEND_NOTICE_BY.toString(), "1");
        customerAccount.add(BImportTable.getInstanceOf(BImportTableTypes.BORROWER_ADDRESS_TABLE, customerTable));
        
        // Borrower Barcode
        customerTable.clear();
        customerTable.put(BImportDBFieldTypes.BARCODE.toString(), c.get(CustomerFieldTypes.ID));
        customerAccount.add(BImportTable.getInstanceOf(BImportTableTypes.BORROWER_BARCODE_TABLE, customerTable));     
    }
    
    @Override
    public List<String> getFormattedCustomer()
    {
        List<String> customerList = new ArrayList<>();
        for (BImportTable table: this.customerAccount)
        {
            customerList.add(table.getData());
        }
        // add the modification prefix to the start of the initial line.
        if (customerList.size() > 0)
        {
            String initialEntry = "M- " + customerList.remove(0);
            customerList.add(0, initialEntry);
        }
        return customerList;
    }

    @Override
    public List<String> getFormattedHeader()
    {
        List<String> customerList = new ArrayList<>();
        for (BImportTable table: this.customerAccount)
        {
            customerList.add(table.getHeader());
        }
        if (customerList.size() > 0)
        {
            String initialEntry = "x- " + customerList.remove(0);
            customerList.add(0, initialEntry);
        }
        return customerList;
    }

    @Override
    public boolean setValue(String key, String value)
    {
        for (BImportTable table: this.customerAccount)
        {
            // check if this table contains a key as provided since BImportTable will
            // insert the value if it doesn't exist, and we don't want all the tables
            // to have this value added. 
            if (containsKey(key))
            {
                return table.setValue(key, value);
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(String key)
    {
        // searches all tables for entries.
        boolean result = false;
        for (BImportTable table: this.customerAccount)
        {
            if (table.getValue(key).isEmpty() == false)
            {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String getValue(String key)
    {
        StringBuilder result = new StringBuilder();
        for (BImportTable table: this.customerAccount)
        {
            String value = table.getValue(key);
            if (value.isEmpty() == false)
            {
                result.append(value);
                result.append(" "); // separate duplicate values with a space.
            }
        }
        return result.toString().trim();
    }
    
      /** 
     * Horizon has an additional required field, email name, which is just the 
     * user's email name (without the domain). We compute that here.
     * @param email
     * @return String value of email account name (without the domain).
     */
    public String computeEmailName(String email) 
    {
        return email.split("@")[0];
    }

    /**
     *
     * @param formattedTable which is cast to BImport table here.
     * @param index the value of index
     */
    @Override
    public void insertTable(FormattedTable formattedTable, int index)
    {
        if (index > this.customerAccount.size())
        {
            this.customerAccount.add((BImportTable)formattedTable);
        }
        else if (index < 0)
        {
            this.customerAccount.add(0, (BImportTable)formattedTable);
        }
        else
        {
            this.customerAccount.add(index, (BImportTable)formattedTable);
        }
    }

    @Override
    public boolean insertValue(String tableName, String key, String value)
    {
        for (FormattedTable table: this.customerAccount)
        {
            if (table.getName().compareTo(tableName) == 0)
            {
                table.setValue(key, value);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean renameField(String tableName, String originalFieldName, String newFieldName)
    {
        throw new UnsupportedOperationException("No requirement for this operation yet.");
    }

    @Override
    public boolean removeField(String tableName, String fieldName)
    {
        throw new UnsupportedOperationException("No requirement for this operation yet.");
    }
}
