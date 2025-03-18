/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024 - 2025 Edmonton Public Library
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
 */

package mecard.sirsidynix.sdapi;

import com.google.gson.JsonSyntaxException;
import java.util.List;
import mecard.config.CustomerFieldTypes;
import mecard.config.SDapiUserFields;
import mecard.customer.Customer;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.util.DateComparer;
import site.MeCardPolicy;

/**
 * Formats the {@link mecard.customer.Customer} into the Symphony web service 
 * consumable form.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class SDapiToMeCardCustomer extends NativeFormatToMeCardCustomer
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
        SDapiUserPatronKeyCustomerResponse customerData;
        try
        {
            customerData = 
                (SDapiUserPatronKeyCustomerResponse) SDapiUserPatronKeyCustomerResponse.parseJson(jsonResponse);
        }
        catch (NullPointerException | JsonSyntaxException e)
        {
            return customer;
        }
        // Populate customer data from response.
        customer.set(CustomerFieldTypes.ID, customerData.getField(SDapiUserFields.USER_ID.toString()));
        customer.set(CustomerFieldTypes.FIRSTNAME, customerData.getField(SDapiUserFields.USER_FIRST_NAME.toString()));
        customer.set(CustomerFieldTypes.LASTNAME, customerData.getField(SDapiUserFields.USER_LAST_NAME.toString()));
        customer.set(CustomerFieldTypes.EMAIL, customerData.getField(SDapiUserFields.EMAIL.toString()));
        customer.set(CustomerFieldTypes.STREET, customerData.getField(SDapiUserFields.STREET.toString()));
        customer.set(CustomerFieldTypes.POSTALCODE, customerData.getField(SDapiUserFields.POSTALCODE.toString()));
        // This actually just returns the city value.
        customer.set(CustomerFieldTypes.CITY, customerData.getField(SDapiUserFields.CITY_SLASH_STATE.toString()));
        // Use this for the province.
        customer.set(CustomerFieldTypes.PROVINCE, customerData.getField(SDapiUserFields.PROV.toString()));
        customer.set(CustomerFieldTypes.PHONE, customerData.getField(SDapiUserFields.PHONE.toString()));
        customer.set(CustomerFieldTypes.ISGOODSTANDING, customerData.getStanding());
        String birthDate = customerData.getDateField(SDapiUserFields.USER_BIRTHDATE.toString());
        if (! birthDate.isEmpty())
        {
            birthDate = DateComparer.getANSIDateFromDateTimestamp(birthDate);
        }
        customer.set(CustomerFieldTypes.DOB, birthDate);
        String expiry = customerData.getField(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString());
        if (expiry == null || expiry.isBlank())
        {
            expiry = DateComparer.getFutureDate(MeCardPolicy.maximumExpiryDays());
        }
        else
        {
            expiry = DateComparer.getANSIDateFromDateTimestamp(expiry);
        }
        customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, expiry);
        // Is there an alternate ID for this customer if so pass it on.
        String altId = customerData.getField(SDapiUserFields.USER_ALTERNATE_ID.toString());
        if (altId != null && ! altId.isBlank())
        {
            customer.set(CustomerFieldTypes.ALTERNATE_ID, altId);
        }
        
        return customer;
    }
}
