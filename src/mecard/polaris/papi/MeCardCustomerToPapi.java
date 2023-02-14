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
package mecard.polaris.papi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.Policies;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PapiPropertyTypes;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.util.Phone;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.MeCardDataToNativeData;
import mecard.polaris.papi.MeCardDataToPapiData.QueryType;
import mecard.util.DateComparer;
import mecard.util.PostalCode;
import mecard.util.Text;

/**
 * The class is used just before the customer is loaded so a library can add
 * additional data before customer creation. Some libraries like to add analytic
 * data before customer load, others rename fields to suit the local requirements.
 * This object takes customer data and converts it into PAPI XML, so 
 * it can be passed in a POST request to the Polaris REST API.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class MeCardCustomerToPapi implements MeCardCustomerToNativeFormat
{
    //    "<PatronRegistrationCreateData>
    //    <LogonBranchID>1</LogonBranchID>
    //    <LogonUserID>1</LogonUserID>
    //    <LogonWorkstationID>1</LogonWorkstationID>
    //    <PatronBranchID>3</PatronBranchID>
    //    <PostalCode>N2V2V4</PostalCode>
    //    <City>Liverpool</City>
    //    <State>NY</State>
    //    <StreetOne>100 Main Street</StreetOne>
    //    <NameFirst>John</NameFirst>
    //    <NameLast>Smith</NameLast>
    //    <PhoneVoice1>555-1212</PhoneVoice1>
    //    <EmailAddress>dude@hotmail.com</EmailAddress>
    //    <UserName>PolarisDude</UserName>
    //    <Barcode>21221012345678</Barcode>
    //    <Password>64058</Password>
    //    <Password2>64058</Password2>
    //    <LegalNameFirst>Johnathan</LegalNameFirst>
    //    <LegalNameLast>Smith</LegalNameLast>
    //    <LegalNameMiddle>Edward</LegalNameMiddle>
    //    <UseLegalNameOnNotices>true</UseLegalNameOnNotices>
    //    <LegalFullName>Johnathan Edward Smith</LegalFullName>
    //    </PatronRegistrationCreateData>"
    private MeCardDataToNativeData customerTable;
    private String serverType;
    public MeCardCustomerToPapi(Customer customer, QueryType type)
    {
        Properties props = PropertyReader.getProperties(ConfigFileTypes.PAPI);
        serverType = props.getProperty(PapiPropertyTypes.SERVER_TYPE.toString(), "sandbox");
        customerTable = MeCardDataToPapiData.getInstanceOf(type);
        // Fill in the default required fields for v1 of PAPI web service API.
        customerTable.setValue(PapiElementOrder.LOGON_BRANCH_ID.name(), 
                props.getProperty(PapiPropertyTypes.LOGON_BRANCH_ID.toString()));
        customerTable.setValue(PapiElementOrder.LOGON_USER_ID.name(), 
                props.getProperty(PapiPropertyTypes.LOGON_USER_ID.toString()));
        customerTable.setValue(PapiElementOrder.LOGON_WORKSTATION_ID.name(), 
                props.getProperty(PapiPropertyTypes.LOGON_WORKSTATION_ID.toString()));
        customerTable.setValue(PapiElementOrder.PATRON_BRANCH_ID.name(), 
                props.getProperty(PapiPropertyTypes.PATRON_BRANCH_ID.toString()));
        // The element order is specified in the PAPI documentation, however the content 
        // is ordered by the customerTable (MeCardDataToPapiData) object.
        customerTable.setValue(PapiElementOrder.BARCODE.name(), customer.get(CustomerFieldTypes.ID));
        customerTable.setValue(PapiElementOrder.PASSWORD.name(), customer.get(CustomerFieldTypes.PIN));
        // This seems to be required or you will get an error -3509 Passwords do not match.
        customerTable.setValue(PapiElementOrder.PASSWORD_2.name(), customer.get(CustomerFieldTypes.PIN));
        customerTable.setValue(PapiElementOrder.NAME_FIRST.name(), customer.get(CustomerFieldTypes.FIRSTNAME));
        customerTable.setValue(PapiElementOrder.NAME_LAST.name(), customer.get(CustomerFieldTypes.LASTNAME));
        //
        // All PAPI sites use the same date formatting, gender formatting etc.
        // so all this code was moved from site specific [Site]CustomerNormalizer class(es).
        //
        // Dates need to be formatted to '2022-07-05T18:45:23.162Z' format.
        // Privilege expiry logic. Use the customer's expiry and if one isn't 
        // set set it to expire in a year.
        String expiry = customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES);
        String addressCheckDate = DateComparer.getFutureDate(Policies.maximumAddressCheckDays());
        if (Text.isUnset(expiry))
        {
            expiry = DateComparer.getFutureDate(Policies.maximumExpiryDays());
            try
            {
                expiry = DateComparer.ANSIToConfigDate(expiry);
                addressCheckDate = DateComparer.ANSIToConfigDate(addressCheckDate);
            } 
            catch (ParseException ex)
            {
                Logger.getLogger(MeCardCustomerToPapi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try
            {
                expiry = DateComparer.ANSIToConfigDate(expiry);
                addressCheckDate = DateComparer.ANSIToConfigDate(addressCheckDate);
            } 
            catch (ParseException ex)
            {
                Logger.getLogger(MeCardCustomerToPapi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        customerTable.setValue(PapiElementOrder.EXPIRATION_DATE.name(), expiry);
        customerTable.setValue(PapiElementOrder.ADDRESS_CHECK_DATE.name(), addressCheckDate);
        // Do the same for Birthday, but birthday can be blank
        String birthday = customer.get(CustomerFieldTypes.DOB);
        if (Text.isSet(birthday))
        {
            try
            {
                birthday = DateComparer.ANSIToConfigDate(birthday);
                customerTable.setValue(PapiElementOrder.BIRTHDATE.name(), birthday);
            } 
            catch (ParseException ex)
            {
                Logger.getLogger(MeCardCustomerToPapi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Finally make sure the postal code has a space.
        //    postal codes. T9H5C5 != T9H 5C5.
        String postalCode = customer.get(CustomerFieldTypes.POSTALCODE);
        // Shouldn't be empty it is a required field, but guard for it just in case.
        if (Text.isSet(postalCode))
        {
            switch (serverType)
            {
                case "production":
                    customerTable.setValue(PapiElementOrder.POSTAL_CODE.name(), 
                        PostalCode.formatPostalCode(postalCode));
                    break;
                case "sandbox":
                default:
                    customerTable.setValue(PapiElementOrder.POSTAL_CODE.name(), postalCode);
                    break;
            }
        }
        // Gender is described in the PAPI documentation so all sites that handle 
        // do so like this. HOWEVER, sites like WBRL no longer collect this 
        // information so Gender has been moved back down to the Customer Normalizers.
        // I have opted to silently ingore gender for all polaris sites.
        
        customerTable.setValue(PapiElementOrder.STREET_ONE.name(), customer.get(CustomerFieldTypes.STREET));
        if (customer.isEmpty(CustomerFieldTypes.PHONE) == false)
        {
            customerTable.setValue(PapiElementOrder.PHONE_VOICE_1.name(), Phone.formatPhone(customer.get(CustomerFieldTypes.PHONE)));
        }
        customerTable.setValue(PapiElementOrder.CITY.name(), customer.get(CustomerFieldTypes.CITY));
        customerTable.setValue(PapiElementOrder.STATE.name(), customer.get(CustomerFieldTypes.PROVINCE));
        customerTable.setValue(PapiElementOrder.EMAIL_ADDRESS.name(), customer.get(CustomerFieldTypes.EMAIL));
        // And set the preferred branch for picking up holds to the customer's assigned branch.
        // This is not strictly necessary, and can be removed safely if the default customer
        // branch for ME card customers is a non-holdable branch.
        customerTable.setValue(PapiElementOrder.REQUEST_PICKUP_BRANCH_ID.name(), 
                props.getProperty(PapiPropertyTypes.PATRON_BRANCH_ID.toString()));
        // The patron code AKA patron code ID or just patron ID is similar to SD's profile.
        customerTable.setValue(PapiElementOrder.PATRON_CODE.name(),
                props.getProperty(PapiPropertyTypes.PATRON_CODE_ID.toString()));
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
