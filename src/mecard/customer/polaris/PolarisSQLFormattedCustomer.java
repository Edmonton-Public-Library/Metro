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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PolarisSQLPropertyTypes;
import mecard.config.PolarisTable;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import mecard.customer.FormattedTable;
import mecard.util.DateComparer;
import mecard.util.Phone;
import mecard.util.PostalCode;

/**
 * This object will format the fields so they are ready for insertion into the
 * ILS database with correct formatting. Example: phone number '7805551212' will
 * become '780-555-1212'. The {@link FormattedCustomer} object has the ability 
 * to store any key value pair as strings, the key should agree with the naming 
 * convention used by the RequestBuilder, in this case {@link mecard.requestbuilder.PolarisILSRequestBuilder}.
 * @see {@link PolarisSQLFormattedCustomer.insertValue()}.
 * @author anisbet
 */
public class PolarisSQLFormattedCustomer implements FormattedCustomer
{
    private final List<FormattedTable> customerAccount;
    
    public PolarisSQLFormattedCustomer(Customer customer)
    {
        // Create the master list of tables.
        this.customerAccount = new ArrayList<>();
        // Here is the contents of the properties file.
        Properties props = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        
        HashMap<String, String> table = new HashMap<>();
        PolarisSQLFormattedTable formattedTable = new PolarisSQLFormattedTable(PolarisTable.PATRONS, table);
        this.customerAccount.add(formattedTable);
        table = new HashMap<>();
        formattedTable = new PolarisSQLFormattedTable(PolarisTable.ADDRESSES, table);
        this.customerAccount.add(formattedTable);
        table = new HashMap<>();
        formattedTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_ADDRESSES, table);
        this.customerAccount.add(formattedTable);
        table = new HashMap<>();
        formattedTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_REGISTRATION, table);
        this.customerAccount.add(formattedTable);
        table = new HashMap<>();
        formattedTable = new PolarisSQLFormattedTable(PolarisTable.POSTAL_CODES, table);
        this.customerAccount.add(formattedTable);
        
        // Now to load the data
        //        table.put(PolarisTable.Patrons.BARCODE.toString(), customer.get(CustomerFieldTypes.ID));
        ////////////////////////////// Patrons /////////////////////////////////
        this.populateCustomerData(props, customer);
    }
    
    private void populateCustomerData(Properties props, Customer customer)
    {
        ////////////////////////////// Patrons /////////////////////////////////
        // values set in the properties file.
        this.insertValue(
                PolarisTable.PATRONS, 
                PolarisTable.Patrons.PATRON_CODE_ID.toString(), 
                // read from the properties file.
                props.getProperty(PolarisSQLPropertyTypes.PATRON_CODE_ID.toString(), "25"));
        this.insertValue(
                PolarisTable.PATRONS, 
                PolarisTable.Patrons.ORGANIZATION_ID.toString(), 
                // read from the properties file.
                props.getProperty(PolarisSQLPropertyTypes.ORGANIZATION_ID.toString()));
        this.insertValue(
                PolarisTable.PATRONS, 
                PolarisTable.Patrons.CREATOR_ID.toString(), 
                // read from the properties file.
                props.getProperty(PolarisSQLPropertyTypes.CREATOR_ID.toString()));
        this.insertValue(
                PolarisTable.PATRONS, 
                PolarisTable.Patrons.BARCODE.toString(), 
                customer.get(CustomerFieldTypes.ID));
        //////////////////////////// PatronRegistration ////////////////////////
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION, 
                PolarisTable.PatronRegistration.LANGUAGE_ID.toString(), 
                props.getProperty(PolarisSQLPropertyTypes.LANGUAGE_ID.toString()));
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION, 
                PolarisTable.PatronRegistration.NAME_FIRST.toString(), 
                customer.get(CustomerFieldTypes.FIRSTNAME));
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION, 
                PolarisTable.PatronRegistration.NAME_LAST.toString(), 
                customer.get(CustomerFieldTypes.LASTNAME));
        // Full name (last first)
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION, 
                PolarisTable.PatronRegistration.PATRON_FULL_NAME.toString(), 
                customer.get(CustomerFieldTypes.LASTNAME) 
                        + " " + customer.get(CustomerFieldTypes.FIRSTNAME));
        // And once more for more redundant data...
        // Full name (fist last)
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION, 
                PolarisTable.PatronRegistration.PATRON_FIRST_LAST_NAME.toString(), 
                customer.get(CustomerFieldTypes.FIRSTNAME) 
                        + " " + customer.get(CustomerFieldTypes.LASTNAME));
        // Phone (voice).
        this.insertValue(
            PolarisTable.PATRON_REGISTRATION,
            PolarisTable.PatronRegistration.PHONE_VOICE_1.toString(),
            Phone.formatPhone(customer.get(CustomerFieldTypes.PHONE)));
            
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION, 
                PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), 
                customer.get(CustomerFieldTypes.EMAIL));
        /*
        Plain-text passwords are no longer stored, they are stored in an 
        obfuscated field and a hash password field that are populated by 
        a stored proceedure {call Polaris.Circ_SetPatronPassword(?)}
        */
//        this.insertValue(
//                PolarisTable.PATRON_REGISTRATION, 
//                PolarisTable.PatronRegistration.PASSWORD.toString(), 
//                customer.get(CustomerFieldTypes.PIN));
        // Expiration and DOB date.
        String dob         = "";
        String expiry      = "";
        String lastUpdated = ""; // used for account updates. Should be set to TODAY.
        try
        {
            dob = DateComparer.ANSIToConfigDate(customer.get(CustomerFieldTypes.DOB));
        }
        catch (ParseException e)
        {
            System.out.println("**Warning failed to convert customer DOB '" 
                    + customer.get(CustomerFieldTypes.DOB)
                    + "'. Please check system date config in environment properties.");
        }
        try
        {
            expiry = DateComparer.ANSIToConfigDate(customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES));
        }
        catch (ParseException e)
        {
            System.out.println("**Warning failed to convert customer EXPIRY '" 
                    + customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES)
                    + "'. Please check system date config in environment properties.");
        }
        try
        {
            lastUpdated = DateComparer.ANSIToConfigDate(DateComparer.ANSIToday());
        }
        catch (ParseException e)
        {
            System.out.println("**Warning failed to convert ANSI date (TODAY)'. "
                    + "Please check system date config in environment properties.");
        }
        this.insertValue(
            PolarisTable.PATRON_REGISTRATION, 
            PolarisTable.PatronRegistration.BIRTH_DATE.toString(), 
            dob);
        this.insertValue(
            PolarisTable.PATRON_REGISTRATION, 
            PolarisTable.PatronRegistration.EXPIRATION_DATE.toString(), 
            expiry);
        // Set to the customer's expiry date.
        this.insertValue(
            PolarisTable.PATRON_REGISTRATION, 
            PolarisTable.PatronRegistration.ADDR_CHECK_DATE.toString(), 
            expiry); // Uses the expiry date as the date to next check the account, as per TRAC request.
        // This value is used when updating a customer.
        this.insertValue(
            PolarisTable.PATRON_REGISTRATION, 
            PolarisTable.PatronRegistration.UPDATE_DATE.toString(), 
            lastUpdated);
        
        // Gender
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION, 
                PolarisTable.PatronRegistration.GENDER.toString(), 
                customer.get(CustomerFieldTypes.SEX));
        // delivery option.
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION, 
                PolarisTable.PatronRegistration.DELIVERY_OPTION_ID.toString(), 
                // read from the properties file.
                props.getProperty(PolarisSQLPropertyTypes.DELIVERY_OPTION_ID.toString()));
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION, 
                PolarisTable.PatronRegistration.EMAIL_FORMAT_ID.toString(), 
                // read from the properties file.
                props.getProperty(PolarisSQLPropertyTypes.EMAIL_FORMAT_ID.toString()));
        // Finally the USER_1-5 categories
        // The only Polaris site in our federation sets these in TRACCustomerNormalizer.
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.USER_1.toString(), 
                props.getProperty(PolarisSQLPropertyTypes.USER_1.toString()));
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.USER_2.toString(), 
                props.getProperty(PolarisSQLPropertyTypes.USER_2.toString()));
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.USER_3.toString(), 
                props.getProperty(PolarisSQLPropertyTypes.USER_3.toString()));
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.USER_4.toString(), 
                props.getProperty(PolarisSQLPropertyTypes.USER_4.toString()));
        this.insertValue(
                PolarisTable.PATRON_REGISTRATION,
                PolarisTable.PatronRegistration.USER_5.toString(), 
                props.getProperty(PolarisSQLPropertyTypes.USER_5.toString()));
        /////////////////// Postal Codes ///////////////////
        this.insertValue(
                PolarisTable.POSTAL_CODES, 
                PolarisTable.PostalCodes.POSTAL_CODE.toString(), 
                PostalCode.formatPostalCode(customer.get(CustomerFieldTypes.POSTALCODE)));
        this.insertValue(
                PolarisTable.POSTAL_CODES, 
                PolarisTable.PostalCodes.CITY.toString(), 
                customer.get(CustomerFieldTypes.CITY));
        this.insertValue(
                PolarisTable.POSTAL_CODES, 
                PolarisTable.PostalCodes.STATE.toString(), 
                customer.get(CustomerFieldTypes.PROVINCE).toUpperCase());
        this.insertValue(
                PolarisTable.POSTAL_CODES, 
                PolarisTable.PostalCodes.COUNTRY_ID.toString(), 
                // read from the properties file.
                props.getProperty(PolarisSQLPropertyTypes.COUNTRY_ID.toString())); // normally '2'
        ////////////////// Addresses ///////////////////////////
        this.insertValue(
                PolarisTable.ADDRESSES, 
                PolarisTable.Addresses.STREET_ONE.toString(), 
                customer.get(CustomerFieldTypes.STREET));
        ///////////////// PatronAddresses ///////////////////////////
        this.insertValue(
                PolarisTable.PATRON_ADDRESSES, 
                PolarisTable.PatronAddresses.FREE_TEXT_LABEL.toString(), 
                // read from the properties file.
                props.getProperty(PolarisSQLPropertyTypes.FREE_TEXT_LABEL.toString()));
    }
    
    @Override
    public List<String> getFormattedCustomer()
    {
        List<String> customerList = new ArrayList<>();
        for (FormattedTable table: this.customerAccount)
        {
            customerList.add(table.getData());
        }
        return customerList;
    }

    @Override
    public List<String> getFormattedHeader()
    {
        List<String> customerList = new ArrayList<>();
        for (FormattedTable table: this.customerAccount)
        {
            customerList.add(table.getHeader());
        }
        return customerList;
    }

    /**
     * Searches for a key in the list of tables that has a key that matches the 
     * argument 'key' then sets that value to the argument 'value'. If the value
     * was found and set the method returns true and false otherwise. Note that 
     * several tables may include a similar key like 'PatronID'.
     * This method *will* set any instance in all tables. This is not usually
     * a problem since 'PatronID' should be the same for all SQL tables.
     * @param key of the value you wish to update
     * @param value the value you want to store.
     * @return If the value was found and set the method returns true and false otherwise.
     */
    @Override
    public boolean setValue(String key, String value)
    {
        boolean result = false;
        for (FormattedTable table: this.customerAccount)
        {
            // check if any table contains a value for the provided key, then
            // insert the value and return true if the value was found and false
            // otherwise. 
            if (containsKey(table, key))
            {
                result = true;
                table.setValue(key, value);
            }
        }
        return result;
    }

    /**
     * Inserts the given value into the requested table.
     * @param tableName
     * @param key
     * @param value
     * @return true if the table was found and false otherwise.
     */
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

    /**
     * Searches for a key in any of the tables, but the key must have a non-empty
     * value to return true.
     * @param key
     * @return true if the key was found in at least one table and the value 
     * stored is not empty.
     */
    @Override
    public boolean containsKey(String key)
    {
        // searches all tables for entries.
        boolean result = false;
        for (FormattedTable table: this.customerAccount)
        {
            if (table.getValue(key).isEmpty() == false)
            {
                result = true;
            }
        }
        return result;
    }
    
    /**
     * Use this instead of {@link #containsKey(java.lang.String)} because this
     * method will localize the search to a specific table.
     * @param table
     * @param key
     * @return true if the specified table contains a key and false otherwise.
     */
    private boolean containsKey(FormattedTable table, String key)
    {
        return table.getValue(key).isEmpty() == false;
    }

    /**
     * This method returns all the values stored under any matching key in all
     * the stored tables, separated by a space.
     * @param key search key
     * @return the value(s) stored in any of the corresponding tables. Example
     * if barcode is stored in the Patrons table only the value '21221012345678'
     * could be returned. if the key 'PatronID' is used, it may appear in more 
     * than table and in that case all the references are returned separated 
     * by a space ' '. In the case where 'PatronID' appears in Patrons, Addresses,
     * and PatronRegistrations, the result may look like this: "12345 12345 12345".
     * @see mecard.util.Text for options for selection of a specific value.
     */
    @Override
    public String getValue(String key)
    {
        StringBuilder result = new StringBuilder();
        for (FormattedTable table: this.customerAccount)
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
     * Inserts a table into the list of tables.
     * @param formattedTable
     * @param index which can always be 0 for PolarisSQLFormattedCustomer since
     * order is not important for output. If index is less than 0 the table is
     * inserted at index 0. If the index is greater than the length of the list
     * the table will just be added to the end of the list.
     */
    @Override
    public void insertTable(FormattedTable formattedTable, int index)
    {
        if (index > this.customerAccount.size())
        {
            this.customerAccount.add((PolarisSQLFormattedTable)formattedTable);
        }
        else if (index < 0)
        {
            this.customerAccount.add(0, (PolarisSQLFormattedTable)formattedTable);
        }
        else
        {
            this.customerAccount.add(index, (PolarisSQLFormattedTable)formattedTable);
        }
    }

    /**
     * Renames a field in a given table.
     * @param tableName
     * @param originalFieldName
     * @param newFieldName
     * @return true if the table was found and a non-null and non-empty value 
     * was renamed, and false otherwise. If the value was null or empty the new method 
     * will return false, but the value will be updated.
     */
    @Override
    public boolean renameField(String tableName, String originalFieldName, String newFieldName)
    {
        for (FormattedTable table: this.customerAccount)
        {
            if (table.getName().compareTo(tableName) == 0)
            {
                return table.renameKey(originalFieldName, newFieldName);
            }
        }
        return false;
    }

    /**
     * Removes a stored value from the list of tables.
     * @param tableName
     * @param fieldName
     * @return true if the table was found and the value as referenced by the 
     * argument key was successfully deleted.
     */
    @Override
    public boolean removeField(String tableName, String fieldName)
    {
        for (FormattedTable table: this.customerAccount)
        {
            if (table.getName().compareTo(tableName) == 0)
            {
                return table.deleteValue(fieldName);
            }
        }
        return false;
    }
}
