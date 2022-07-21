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
package mecard.horizon;

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
import mecard.requestbuilder.BImportRequestBuilder;
import mecard.util.AlbertaCity;
import mecard.util.City;
import mecard.util.DateComparer;
import mecard.util.Phone;
import mecard.util.PostalCode;
import mecard.util.Text;
import site.HorizonNormalizer;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.MeCardDataToNativeData;

/**
 * Instance of a customer formatted for loading.
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public final class MeCardCustomerToBImport implements MeCardCustomerToNativeFormat
{
    private final List<MeCardDataToBImportData> customerAccount;
    
    public MeCardCustomerToBImport(Customer customer)
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
        if (customer.isLostCard())
        {
            customerTable.put(BImportDBFieldTypes.SECOND_ID.toString(), customer.get(CustomerFieldTypes.ALTERNATE_ID));
        }
        else
        {
            customerTable.put(BImportDBFieldTypes.SECOND_ID.toString(), customer.get(CustomerFieldTypes.ID));
        }
        customerTable.put(BImportDBFieldTypes.NAME.toString(), (customer.get(CustomerFieldTypes.LASTNAME) 
                + ", " + customer.get(CustomerFieldTypes.FIRSTNAME)));
        try
        {
            String expiry = DateComparer.ANSIToConfigDate(customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES));
            customerTable.put(BImportDBFieldTypes.EXPIRY.toString(), expiry);
        } 
        catch (ParseException ex)
        {
            System.out.println(new Date() + " unable to parse expiry '" 
                    + customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES) + "'");
        }
        try
        {
            String dobDate = "19000101";
            if (customer.isEmpty(CustomerFieldTypes.DOB) == false)
            {
                dobDate = customer.get(CustomerFieldTypes.DOB);
            }
            String insertDate = DateComparer.ANSIToConfigDate(dobDate);
            customerTable.put(BImportDBFieldTypes.BIRTH_DATE.toString(), insertDate);
        } 
        catch (ParseException ex)
        {
            System.out.println(new Date() + " unable to parse DOB '" + customer.get(CustomerFieldTypes.DOB) + "'");
        }
        // Add the pin no matter what. It may be a hashed pin of their password
        // or an actual 4-digit pin, but it always should be overlayed since we
        // don't change pins to random digits anymore.
        String pin = customer.get(CustomerFieldTypes.PIN);
        if (Text.isUpToMaxDigits(pin, HorizonNormalizer.MAXIMUM_PIN_WIDTH))
        {
            customerTable.put(BImportDBFieldTypes.PIN.toString(), 
                customer.get(CustomerFieldTypes.PIN));
        }
        else
        {
            // Get the hash of the current password instead of random digits.
            String newPin = Text.getNew4DigitPin(pin);
            customerTable.put(BImportDBFieldTypes.PIN.toString(), newPin);
            System.out.println(new Date()
                + " BimportFormattedCustomer setting pin to: '" 
                + newPin + "' because old pin was '" + pin + "'.");
            
        }
        customerAccount.add(MeCardDataToBImportData.getInstanceOf(BImportTableTypes.BORROWER_TABLE, customerTable));
        
        // Next the phone table
        customerTable.clear();
        customerTable.put(BImportDBFieldTypes.PHONE_TYPE.toString(), "h-noTC");
        String phone = "780-999-1234";
        if (customer.isEmpty(CustomerFieldTypes.PHONE) == false)
        {
            phone = Phone.formatPhone(customer.get(CustomerFieldTypes.PHONE));
        }
        else
        {
            Properties bimpProps = PropertyReader.getProperties(ConfigFileTypes.BIMPORT);
            phone = bimpProps.getProperty(BImportRequestBuilder.PHONE_TAG, phone);
        }
        customerTable.put(BImportDBFieldTypes.PHONE_NUMBER.toString(), phone);
        customerAccount.add(MeCardDataToBImportData.getInstanceOf(BImportTableTypes.BORROWER_PHONE_TABLE, customerTable));
        
        // Address
        customerTable.clear();
        customerTable.put(BImportDBFieldTypes.ADDRESS_1.toString(), customer.get(CustomerFieldTypes.STREET));
        customerTable.put(BImportDBFieldTypes.ADDRESS_2.toString(), " "); // intentionally blank
        // look up the city name in bimport address.
        City city  = AlbertaCity.getInstanceOf();
        String cityCode = city.getCityCode(customer.get(CustomerFieldTypes.CITY));
        customerTable.put(BImportDBFieldTypes.CITY.toString(), cityCode);
        customerTable.put(BImportDBFieldTypes.POSTAL_CODE.toString(), PostalCode.formatPostalCode(customer.get(CustomerFieldTypes.POSTALCODE)));
        String emailName = this.computeEmailName(customer.get(CustomerFieldTypes.EMAIL));
        customerTable.put(BImportDBFieldTypes.EMAIL_NAME.toString(), emailName);
        customerTable.put(BImportDBFieldTypes.EMAIL_ADDRESS.toString(), customer.get(CustomerFieldTypes.EMAIL));
        customerTable.put(BImportDBFieldTypes.SEND_PREOVERDUE.toString(), "1");
        customerTable.put(BImportDBFieldTypes.SEND_NOTICE_BY.toString(), "1");
        customerAccount.add(MeCardDataToBImportData.getInstanceOf(BImportTableTypes.BORROWER_ADDRESS_TABLE, customerTable));
        
        // Borrower Barcode
        customerTable.clear();
        customerTable.put(BImportDBFieldTypes.BARCODE.toString(), customer.get(CustomerFieldTypes.ID));
        customerAccount.add(MeCardDataToBImportData.getInstanceOf(BImportTableTypes.BORROWER_BARCODE_TABLE, customerTable));     
    }
    
    @Override
    public List<String> getFormattedCustomer()
    {
        List<String> customerList = new ArrayList<>();
        this.customerAccount.forEach(table -> {
            customerList.add(table.getData());
        });
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
        this.customerAccount.forEach(table -> {
            customerList.add(table.getHeader());
        });
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
        for (MeCardDataToBImportData table: this.customerAccount)
        {
            // check if this table contains a key as provided since MeCardDataToBImportData will
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
        for (MeCardDataToBImportData table: this.customerAccount)
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
        for (MeCardDataToBImportData table: this.customerAccount)
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
    public void insertTable(MeCardDataToNativeData formattedTable, int index)
    {
        if (index > this.customerAccount.size())
        {
            this.customerAccount.add((MeCardDataToBImportData)formattedTable);
        }
        else if (index < 0)
        {
            this.customerAccount.add(0, (MeCardDataToBImportData)formattedTable);
        }
        else
        {
            this.customerAccount.add(index, (MeCardDataToBImportData)formattedTable);
        }
    }

    @Override
    public boolean insertValue(String tableName, String key, String value)
    {
        for (MeCardDataToNativeData table: this.customerAccount)
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
