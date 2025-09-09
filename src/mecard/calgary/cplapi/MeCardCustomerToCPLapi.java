/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025 Edmonton Public Library
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

package mecard.calgary.cplapi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import mecard.Protocol;
// If default values need to be added to accounts, those values are read 
// from the cplaip.properties file at runtime. You will need these three
// imports to make that work.
//import java.util.Properties;
//import mecard.config.ConfigFileTypes;
//import mecard.config.PropertyReader;
import mecard.config.CPLapiUserFields;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.MeCardDataToNativeData;
import mecard.util.DateComparer;
import mecard.util.PostalCode;
import mecard.util.Text;
import site.MeCardPolicy;


/**
 * The class is used just before the customer is loaded so a library can add
 * additional data before customer creation. Some libraries like to add analytic
 * data before customer load, others rename fields to suit the local requirements.
 * This object takes customer data and converts it into Calgary Public Library's
 * custom web service API.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class MeCardCustomerToCPLapi extends MeCardCustomerToNativeFormat
{
    private MeCardDataToNativeData customerTable;
    private enum Gender
    {
        MALE("Male"),
        FEMALE("Female"),
        NOTELL("NOTELL");
        
        private final String type;

        private Gender(String s)
        {
            this.type = s;
        }

        @Override
        public String toString()
        {
            return this.type;
        }
    }
    
    public MeCardCustomerToCPLapi(Customer customer, MeCardDataToCPLapiData.QueryType type)
    {
        // Read the cplapi specific properties from the cplapi.properties file
        // if Calgary requires additional default values applied from the 
        // cplapi.properties file.
//        Properties props = PropertyReader.getProperties(ConfigFileTypes.CPL_API);
        
        //  "cardNumber": "string",
        //  "pin": "string",
        //  "firstName": "string",
        //  "lastName": "string",
        //  "birthDate": "YYYY-MM-DD",
        //  "gender": "string",
        //  "emailAddress": "string",
        //  "phoneNumber": "string",
        //  "address": "string",
        //  "city": "string",
        //  "province": "string",
        //  "postalCode": "string",
        //  "expiryDate": "YYYY-MM-DD"
        
        customerTable = MeCardDataToCPLapiData.getInstanceOf(type);
        // ID and password. TODO check if they have any password restrictions.
        customerTable.setValue(CPLapiUserFields.USER_ID.toString(), 
            customer.get(CustomerFieldTypes.ID));
        customerTable.setValue(CPLapiUserFields.USER_PASSWORD.toString(), 
            customer.get(CustomerFieldTypes.PIN));
        customerTable.setValue(CPLapiUserFields.USER_FIRST_NAME.toString(), 
            customer.get(CustomerFieldTypes.FIRSTNAME));
        customerTable.setValue(CPLapiUserFields.USER_LAST_NAME.toString(), 
            customer.get(CustomerFieldTypes.LASTNAME));
        customerTable.setValue(CPLapiUserFields.EMAIL.toString(), 
            customer.get(CustomerFieldTypes.EMAIL));
        customerTable.setValue(CPLapiUserFields.STREET.toString(), 
            customer.get(CustomerFieldTypes.STREET));
        customerTable.setValue(CPLapiUserFields.CITY.toString(),
            customer.get(CustomerFieldTypes.CITY));
        customerTable.setValue(CPLapiUserFields.PROVINCE.toString(),
            customer.get(CustomerFieldTypes.PROVINCE));
        customerTable.setValue(CPLapiUserFields.POSTALCODE.toString(), 
            PostalCode.formatPostalCode(customer.get(CustomerFieldTypes.POSTALCODE)));
        String phoneNumber = customer.get(CustomerFieldTypes.PHONE);
        if (Text.isSet(phoneNumber))
        {
            if (this.debug)
                System.out.println("PHONE#: " + phoneNumber);
            customerTable.setValue(CPLapiUserFields.PHONE.toString(), 
                Text.formatPhoneNumber(phoneNumber));
        }
        String birthday = customer.get(CustomerFieldTypes.DOB);
        if (Text.isSet(birthday))
        {
            try
            {
                birthday = DateComparer.ANSIToConfigDate(birthday);
                customerTable.setValue(CPLapiUserFields.USER_BIRTHDATE.toString(), birthday);
            } 
            catch (ParseException ex)
            {
                if (this.debug)
                    System.out.println("**error while parsing customer " 
                        + customer.get(CustomerFieldTypes.ID) 
                        + " DOB: '" + birthday + "'");
            }
        }
        // Expiry
        String expiry = customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES);
        if (Text.isUnset(expiry))
        {
            expiry = DateComparer.getFutureDate(MeCardPolicy.maximumExpiryDays());
            try
            {
                expiry = DateComparer.ANSIToConfigDate(expiry);
            } 
            catch (ParseException ex)
            {
                if (this.debug)
                    System.out.println("**error while parsing customer " 
                        + customer.get(CustomerFieldTypes.ID) 
                        + " expiry: '" + expiry + "'");
            }
        }
        else // There is a date set in the customer's 'expiry' field.
        {
            try
            {
                expiry = DateComparer.ANSIToConfigDate(expiry);
            } 
            catch (ParseException ex)
            {
                if (this.debug)
                    System.out.println("**error while parsing customer " 
                        + customer.get(CustomerFieldTypes.ID) 
                        + " expiry: '" + expiry + "'");
            }
        }
        customerTable.setValue(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString(), expiry);
        String gender = customer.get(CustomerFieldTypes.SEX);
        if (Text.isSet(gender))
        {
            if (gender.equalsIgnoreCase(Protocol.MALE))
            {
                customerTable.setValue(CPLapiUserFields.GENDER.toString(), Gender.MALE.toString());
            }
            else if (gender.equalsIgnoreCase(Protocol.FEMALE))
            {
                customerTable.setValue(CPLapiUserFields.GENDER.toString(), Gender.FEMALE.toString());
            }
            else // some other variation, but not expected by Calgary, so just 'NOTELL'.
            {
                customerTable.setValue(CPLapiUserFields.GENDER.toString(), Gender.NOTELL.toString());
            }
        }
        else // Default to NOTELL
        {
            customerTable.setValue(CPLapiUserFields.GENDER.toString(), Gender.NOTELL.toString());
        }
        // Deliberately not included: status, profile.  
        // No other default settings requested by CPL at this time.
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

    @Override
    public boolean renameField(String tableName, String originalFieldName, String newFieldName) 
    {
        return this.customerTable.renameKey(newFieldName, originalFieldName);
    }

    @Override
    public boolean removeField(String tableName, String fieldName) 
    {
        return this.customerTable.deleteValue(fieldName);
    }
}
