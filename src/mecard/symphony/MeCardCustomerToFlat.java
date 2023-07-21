/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2023  Edmonton Public Library
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
package mecard.symphony;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mecard.config.CustomerFieldTypes;
import mecard.config.FlatUserTableTypes;
import mecard.config.FlatUserFieldTypes;
import mecard.customer.Customer;
import mecard.util.DateComparer;
import mecard.util.Phone;
import mecard.util.PostalCode;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.MeCardDataToNativeData;

/**
 * This class converts a ME customer object into a flat file.
 * @author Andrew Nisbet <andrew.nisbet@epl.ca>
 */
public class MeCardCustomerToFlat implements MeCardCustomerToNativeFormat
{
    private final List<MeCardDataToFlatData> customerAccount;
    
    public MeCardCustomerToFlat(Customer customer)
    {
        this.customerAccount = new ArrayList<>();
        HashMap<String, String> customerTable = new HashMap<>();
        customerTable.put(FlatUserFieldTypes.USER_ID.toString(), customer.get(CustomerFieldTypes.ID));
        customerTable.put(FlatUserFieldTypes.USER_PIN.toString(), customer.get(CustomerFieldTypes.PIN));
        customerTable.put(FlatUserFieldTypes.USER_FIRST_NAME.toString(), customer.get(CustomerFieldTypes.FIRSTNAME));
        customerTable.put(FlatUserFieldTypes.USER_LAST_NAME.toString(), customer.get(CustomerFieldTypes.LASTNAME));
        if (customer.isEmpty(CustomerFieldTypes.PREFEREDNAME) == false)
        {
            customerTable.put(FlatUserFieldTypes.USER_PREFERRED_NAME.toString(), 
                    customer.get(CustomerFieldTypes.PREFEREDNAME));
        } 
        customerTable.put(FlatUserFieldTypes.USER_PRIV_EXPIRES.toString(), customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES));
        // set todays date as the date privilege granted.
        customerTable.put(FlatUserFieldTypes.USER_PRIV_GRANTED.toString(), DateComparer.ANSIToday());
        if (customer.isEmpty(CustomerFieldTypes.DOB) == false)
        {
            customerTable.put(FlatUserFieldTypes.USER_BIRTH_DATE.name(), customer.get(CustomerFieldTypes.DOB));
        }
        this.customerAccount.add(MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER, customerTable));
        // Address Table
        customerTable = new HashMap<>();
        customerTable.put(FlatUserFieldTypes.STREET.toString(), customer.get(CustomerFieldTypes.STREET));
        if (customer.isEmpty(CustomerFieldTypes.PHONE) == false)
        {
            customerTable.put(FlatUserFieldTypes.PHONE.toString(), Phone.formatPhone(customer.get(CustomerFieldTypes.PHONE)));
        }
        // Symphony uses CITY/STATE as a field (sigh)
        String city     = customer.get(CustomerFieldTypes.CITY);
        String province = customer.get(CustomerFieldTypes.PROVINCE);
        customerTable.put(FlatUserFieldTypes.CITY_SLASH_STATE.toString(), (city + ", " + province.toUpperCase()));
        customerTable.put(FlatUserFieldTypes.POSTALCODE.toString(),
                PostalCode.formatPostalCode(customer.get(CustomerFieldTypes.POSTALCODE)));
        customerTable.put(FlatUserFieldTypes.EMAIL.toString(), customer.get(CustomerFieldTypes.EMAIL));
        this.customerAccount.add(MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER_ADDR1, customerTable));
    }
    
    @Override
    public List<String> getFormattedCustomer()
    {
        List<String> customerList = new ArrayList<>();
        for (MeCardDataToNativeData table: this.customerAccount)
        {
            customerList.add(table.getData());
        }
        return customerList;
    }

    @Override
    public List<String> getFormattedHeader()
    {
        List<String> customerList = new ArrayList<>();
        for (MeCardDataToNativeData table: this.customerAccount)
        {
            if (table.getName().compareTo(FlatUserTableTypes.USER.name()) == 0)
            {
                customerList.add(table.getHeader());
            }
        }
        return customerList;
    }

    @Override
    public boolean setValue(String key, String value)
    {
        for (MeCardDataToNativeData table: this.customerAccount)
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
        for (MeCardDataToNativeData table: this.customerAccount)
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
        for (MeCardDataToNativeData table: this.customerAccount)
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

    @Override
    public void insertTable(MeCardDataToNativeData formattedTable, int index)
    {
        if (index > this.customerAccount.size())
        {
            this.customerAccount.add((MeCardDataToFlatData)formattedTable);
        }
        else if (index < 0)
        {
            this.customerAccount.add(0, (MeCardDataToFlatData)formattedTable);
        }
        else
        {
            this.customerAccount.add(index, (MeCardDataToFlatData)formattedTable);
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
        for (MeCardDataToNativeData table: this.customerAccount)
        {
            if (table.getName().compareTo(tableName) == 0)
            {
                return table.renameKey(originalFieldName, newFieldName);
            }
        }
        return false;
    }

    @Override
    public boolean removeField(String tableName, String fieldName)
    {
        for (MeCardDataToNativeData table: this.customerAccount)
        {
            if (table.getName().compareTo(tableName) == 0)
            {
                return table.deleteValue(fieldName);
            }
        }
        return false;
    }
}
