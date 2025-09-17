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

import com.google.gson.JsonSyntaxException;
import java.util.List;
import mecard.Protocol;
import mecard.config.CPLapiUserFields;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.util.DateComparer;
import site.MeCardPolicy;

/**
 *
 * @author anisbet
 */
public class CPLapiToMeCardCustomer extends NativeFormatToMeCardCustomer
{

    @Override
    public Customer getCustomer(List<String> list) 
    {
        StringBuilder sb = new StringBuilder();
        for (String s: list)
        {
            sb.append(s);
        }
        return this.getCustomer(sb.toString());
    }

    @Override
    public Customer getCustomer(String jsonResponse) 
    {
        Customer customer = new Customer();
        CPLapiGetCustomerResponse customerData;
        try
        {
            customerData = 
                (CPLapiGetCustomerResponse) CPLapiGetCustomerResponse.parseJson(jsonResponse);
        }
        catch (NullPointerException | JsonSyntaxException e)
        {
            return customer;
        }
        // Populate customer data from response.
        customer.set(CustomerFieldTypes.ID, customerData.getField(CPLapiUserFields.USER_ID.toString()));
        customer.set(CustomerFieldTypes.FIRSTNAME, customerData.getField(CPLapiUserFields.USER_FIRST_NAME.toString()));
        customer.set(CustomerFieldTypes.LASTNAME, customerData.getField(CPLapiUserFields.USER_LAST_NAME.toString()));
        customer.set(CustomerFieldTypes.EMAIL, customerData.getField(CPLapiUserFields.EMAIL.toString()));
        customer.set(CustomerFieldTypes.STREET, customerData.getField(CPLapiUserFields.STREET.toString()));
        customer.set(CustomerFieldTypes.POSTALCODE, customerData.getField(CPLapiUserFields.POSTALCODE.toString()));
        // This actually just returns the city value.
        customer.set(CustomerFieldTypes.CITY, customerData.getField(CPLapiUserFields.CITY.toString()));
        // Use this for the province.
        customer.set(CustomerFieldTypes.PROVINCE, customerData.getField(CPLapiUserFields.PROVINCE.toString()));
        customer.set(CustomerFieldTypes.PHONE, customerData.getField(CPLapiUserFields.PHONE.toString()));
        String gender = customerData.getField(CPLapiUserFields.GENDER.toString());
        if (gender.isEmpty())
        {
            gender = Protocol.DEFAULT_FIELD_VALUE;
        }
        else
        {
            if (gender.startsWith("F"))
            {
                gender = Protocol.FEMALE;
            }
            else if (gender.startsWith("M"))
            {
                gender = Protocol.MALE;
            }
            else // includes anything other than binary genders as in 'NOTELL'
            {
                gender = Protocol.DEFAULT_FIELD_VALUE;
            }
        }
        customer.set(CustomerFieldTypes.SEX, gender);
        customer.set(CustomerFieldTypes.ISGOODSTANDING, customerData.getStanding());
        String birthDate = customerData.getDateField(CPLapiUserFields.USER_BIRTHDATE.toString());
        if (! birthDate.isEmpty())
        {
            birthDate = DateComparer.getANSIDateFromDateTimestamp(birthDate);
        }
        customer.set(CustomerFieldTypes.DOB, birthDate);
        String expiry = customerData.getField(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString());
        if (expiry == null || expiry.isBlank())
        {
            expiry = DateComparer.getFutureDate(MeCardPolicy.maximumExpiryDays());
        }
        else
        {
            expiry = DateComparer.getANSIDateFromDateTimestamp(expiry);
        }
        customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, expiry);
                
        return customer;
    }

}
