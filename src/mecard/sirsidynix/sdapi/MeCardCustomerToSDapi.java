/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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
package mecard.sirsidynix.sdapi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import mecard.Policies;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiUserFields;
import mecard.config.SymphonyPropertyTypes;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.MeCardDataToNativeData;
import mecard.util.DateComparer;
import mecard.util.PostalCode;
import mecard.util.Text;

/**
 * The class is used just before the customer is loaded so a library can add
 * additional data before customer creation. Some libraries like to add analytic
 * data before customer load, others rename fields to suit the local requirements.
 * This object takes customer data and converts it into SirsiDynix REST API.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class MeCardCustomerToSDapi implements MeCardCustomerToNativeFormat
{
    private MeCardDataToNativeData customerTable;
    
    public MeCardCustomerToSDapi(Customer customer, MeCardDataToSDapiData.QueryType type)
    {
        // Read the Symphony specific properties from the symphony.properties file.
        Properties props = PropertyReader.getProperties(ConfigFileTypes.SYMPHONY);
        customerTable = MeCardDataToSDapiData.getInstanceOf(type);
        // Fill in the default required fields for v1 of SD api web service API.
        customerTable.setValue(SDapiUserFields.USER_ID.toString(), 
            customer.get(CustomerFieldTypes.ID));
        customerTable.setValue(SDapiUserFields.USER_FIRST_NAME.toString(), 
            customer.get(CustomerFieldTypes.FIRSTNAME));
        customerTable.setValue(SDapiUserFields.USER_LAST_NAME.toString(), 
            customer.get(CustomerFieldTypes.LASTNAME));
        String altId = customer.get(CustomerFieldTypes.ALTERNATE_ID);
        if (Text.isSet(altId))
            customerTable.setValue(SDapiUserFields.USER_ALTERNATE_ID.toString(), 
                customer.get(CustomerFieldTypes.ALTERNATE_ID));
        customerTable.setValue(SDapiUserFields.EMAIL.toString(), 
            customer.get(CustomerFieldTypes.EMAIL));
        customerTable.setValue(SDapiUserFields.STREET.toString(), 
            customer.get(CustomerFieldTypes.STREET));
        // Note: If your ILS needs CITY_SLASH_PROV this will have to be 
        // modified using [Library]CustomerNormalizer.java for that.
        String citySlashState = customer.get(CustomerFieldTypes.CITY)
                + ", " + customer.get(CustomerFieldTypes.PROVINCE);
        customerTable.setValue(SDapiUserFields.CITY_SLASH_STATE.toString(), 
            citySlashState);
        customerTable.setValue(SDapiUserFields.POSTALCODE.toString(), 
            PostalCode.formatPostalCode(customer.get(CustomerFieldTypes.POSTALCODE)));

        String birthday = customer.get(CustomerFieldTypes.DOB);
        if (Text.isSet(birthday))
        {
            try
            {
                birthday = DateComparer.ANSIToConfigDate(birthday);
                customerTable.setValue(SDapiUserFields.USER_BIRTHDATE.toString(), birthday);
            } 
            catch (ParseException ex)
            {
                System.out.println("**error while parsing customer " 
                        + customer.get(CustomerFieldTypes.ID) 
                        + " DOB: '" + birthday + "'");
            }
        }
        String expiry = customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES);
        if (Text.isUnset(expiry))
        {
            expiry = DateComparer.getFutureDate(Policies.maximumExpiryDays());
            try
            {
                expiry = DateComparer.ANSIToConfigDate(expiry);
            } 
            catch (ParseException ex)
            {
                System.out.println("**error while parsing customer " 
                        + customer.get(CustomerFieldTypes.ID) 
                        + " expiry: '" + expiry + "'");
            }
        }
        else
        {
            try
            {
                expiry = DateComparer.ANSIToConfigDate(expiry);
            } 
            catch (ParseException ex)
            {
                System.out.println("**error while parsing customer " 
                        + customer.get(CustomerFieldTypes.ID) 
                        + " expiry: '" + expiry + "'");
            }
        }
        customerTable.setValue(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString(), expiry);
        String phoneNumber = customer.get(CustomerFieldTypes.PHONE);
        if (Text.isSet(phoneNumber))
        {
            customerTable.setValue(SDapiUserFields.PHONE.toString(), 
                Text.formatPhoneNumber(phoneNumber));
        }
//         Is there a preferred name to add, also pin to handle??
        customerTable.setValue(SDapiUserFields.USER_PASSWORD.toString(), 
            customer.get(CustomerFieldTypes.PIN));
        // Set Defaults.
        //        <entry key="USER_LIBRARY">EPLMNA</entry>
        customerTable.setValue(SDapiUserFields.USER_LIBRARY.toString(), 
                props.getProperty(SymphonyPropertyTypes.USER_LIBRARY.toString()));
        //        <entry key="USER_PROFILE">EPL-METRO</entry>
        customerTable.setValue(SDapiUserFields.PROFILE.toString(), 
                props.getProperty(SymphonyPropertyTypes.USER_PROFILE.toString()));
        //        <entry key="USER_PREF_LANG">ENGLISH</entry>
        customerTable.setValue(SDapiUserFields.LANGUAGE.toString(), 
                props.getProperty(SymphonyPropertyTypes.USER_PREFERED_LANGUAGE.toString()));
        //        <entry key="USER_CHG_HIST_RULE">ALLCHARGES</entry>
        customerTable.setValue(SDapiUserFields.KEEP_CIRC_HISTORY.toString(), 
                props.getProperty(SymphonyPropertyTypes.USER_CHARGE_HISTORY_RULE.toString()));
        //        <entry key="USER_ACCESS">PUBLIC</entry>
        customerTable.setValue(SDapiUserFields.USER_ACCESS.toString(), 
                props.getProperty(SymphonyPropertyTypes.USER_ACCESS.toString()));
        //        <entry key="USER_ENVIRONMENT">PUBLIC</entry>
        customerTable.setValue(SDapiUserFields.ENVIRONMENT.toString(), 
                props.getProperty(SymphonyPropertyTypes.USER_ENVIRONMENT.toString()));
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
    public void insertTable(MeCardDataToNativeData formattedTable, int index) 
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

    /**
     * Renames a field.If the original field is not found no changes are made. For example renameField(firstName, lastName) would replace the firstName key
 to the lastName key without changing the values stored in the original field. 
 Therefore if the contents of firstName was 'Tom' and the contents of lastName 
 was 'Cruise', then after the renameKey() is run, the contents of 
 lastName == 'Tom', and the content 'Cruise' is lost.
     * @param oldFieldName the original, or target field.
     * @param newFieldName the new field name.
     * @return true if the key was renamed and false if there was no original key.
     */
    @Override
    public boolean renameField(String tableName, String oldFieldName, String newFieldName) 
    {
        return this.customerTable.renameKey(newFieldName, oldFieldName);
    }

    @Override
    public boolean removeField(String tableName, String fieldName) 
    {
        return this.customerTable.deleteValue(fieldName);
    }
    
}
