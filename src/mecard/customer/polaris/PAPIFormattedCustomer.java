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
package mecard.customer.polaris;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PAPIPropertyTypes;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import mecard.customer.FormattedTable;
import mecard.util.Phone;
import mecard.util.PostalCode;

/**
 * The class is used just before the customer is loaded so a library can add
 * additional data before customer creation. Some libraries like to add analytic
 * data before customer load, others rename fields to suit the local requirements.
 * This object takes customer data and converts it into PAPI XML, (or JSON) so 
 * it can be passed in a POST request to the Polaris REST API.
 * @author anisbet
 */
public class PAPIFormattedCustomer implements FormattedCustomer
{
    private FormattedTable customerTable;
    public PAPIFormattedCustomer(Customer customer)
    {
        Properties props     = PropertyReader.getProperties(ConfigFileTypes.PAPI);
        
        customerTable = PAPIFormattedTable.getInstanceOf();
        // Fill in the default required fields for v1 of PAPI web service API.
        customerTable.setValue(
                PAPIElementOrder.C_LOGON_BRANCH_ID.name(), 
                props.getProperty(PAPIPropertyTypes.LOGON_BRANCH_ID.toString()));
        customerTable.setValue(
                PAPIElementOrder.C_LOGON_USER_ID.name(), 
                props.getProperty(PAPIPropertyTypes.LOGON_USER_ID.toString()));
        customerTable.setValue(
                PAPIElementOrder.C_LOGON_WORKSTATION_ID.name(), 
                props.getProperty(PAPIPropertyTypes.LOGON_WORKSTATION_ID.toString()));
        customerTable.setValue(
                PAPIElementOrder.C_PATRON_BRANCH_ID.name(), 
                props.getProperty(PAPIPropertyTypes.PATRON_BRANCH_ID.toString()));
        
        customerTable.setValue(PAPIElementOrder.C_BARCODE.name(), customer.get(CustomerFieldTypes.ID));
        customerTable.setValue(PAPIElementOrder.C_PASSWORD.name(), customer.get(CustomerFieldTypes.PIN));
        customerTable.setValue(PAPIElementOrder.C_NAME_FIRST.name(), customer.get(CustomerFieldTypes.FIRSTNAME));
        customerTable.setValue(PAPIElementOrder.C_NAME_LAST.name(), customer.get(CustomerFieldTypes.LASTNAME));
        // Can't create a user with an expiry date?
//        customerTable.setValue(PAPIElementOrder.USER_PRIV_EXPIRES.name(), customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES));
        // set todays date as the date privilege granted, but not required on Polaris. The date is supplied automatically.
//        customerTable.setValue(PAPIElementOrder.USER_PRIV_GRANTED.name(), DateComparer.ANSIToday());
        if (customer.isEmpty(CustomerFieldTypes.DOB) == false)
        {
            customerTable.setValue(PAPIElementOrder.C_BIRTHDATE.name(), customer.get(CustomerFieldTypes.DOB));
        }
        if (customer.isEmpty(CustomerFieldTypes.SEX) == false)
        {
            String sex = customer.get(CustomerFieldTypes.SEX);
            if (sex.compareTo("M") == 0 || sex.compareTo("F") == 0)
            {
                customerTable.setValue(PAPIElementOrder.C_GENDER.name(), sex);
            }
            else
            {
                customerTable.setValue(PAPIElementOrder.C_GENDER.name(), "N");
            }
        }
        
        customerTable.setValue(PAPIElementOrder.C_STREET_ONE.name(), customer.get(CustomerFieldTypes.STREET));
        if (customer.isEmpty(CustomerFieldTypes.PHONE) == false)
        {
            customerTable.setValue(PAPIElementOrder.C_PHONE_VOICE_1.name(), Phone.formatPhone(customer.get(CustomerFieldTypes.PHONE)));
        }
        customerTable.setValue(PAPIElementOrder.C_CITY.name(), customer.get(CustomerFieldTypes.CITY));
        customerTable.setValue(PAPIElementOrder.C_STATE.name(), customer.get(CustomerFieldTypes.PROVINCE));
        customerTable.setValue(PAPIElementOrder.C_POSTAL_CODE.name(),
                PostalCode.formatPostalCode(customer.get(CustomerFieldTypes.POSTALCODE)));
        customerTable.setValue(PAPIElementOrder.C_EMAIL_ADDRESS.name(), customer.get(CustomerFieldTypes.EMAIL));
    }
    
    
    @Override
    public List<String> getFormattedCustomer()
    {
        List<String> customerAccount = new ArrayList<>();
        customerAccount.add(this.customerTable.getData());
        return customerAccount;
    }

    @Override
    public List<String> getFormattedHeader()
    {
        List<String> customerAccount = new ArrayList<>();
        customerAccount.add(this.customerTable.getHeader());
        return customerAccount;
    }

    @Override
    public boolean setValue(String key, String value)
    {
        return this.customerTable.setValue(key, value);
    }

    @Override
    public boolean insertValue(String tableName, String key, String value)
    {
        // since there is only one table for a Polaris user we don't need the initial value.
        return this.customerTable.setValue(key, value); 
    }

    @Override
    public boolean containsKey(String key)
    {
        String value = this.customerTable.getValue(key);
        return ! value.isEmpty();
    }

    @Override
    public String getValue(String key)
    {
        return this.customerTable.getValue(key);
    }

    @Override
    public void insertTable(FormattedTable formattedTable, int index)
    {
        // There is only one table so the index is irrelivant.
        if (formattedTable != null)
        {
            this.customerTable = formattedTable;
        }
        else
        {
            System.out.println("** Error insertTable: failed because the argument table was null.");
        }
    }

    @Override
    public boolean renameField(String tableName, String originalFieldName, String newFieldName)
    {
        return this.customerTable.renameKey(originalFieldName, newFieldName);
    }

    @Override
    public boolean removeField(String tableName, String fieldName)
    {
        // Polaris has only one table type so the name is not used.
        return this.customerTable.deleteValue(fieldName);
    }
    
    @Override
    public String toString()
    {
        return this.customerTable.toString();
    }
}
