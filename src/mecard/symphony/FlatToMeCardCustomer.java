/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022 - 2025 Edmonton Public Library
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

import mecard.config.CustomerFieldTypes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mecard.customer.Customer;
import mecard.util.Phone;
import mecard.util.PostalCode;
import mecard.customer.NativeFormatToMeCardCustomer;

/**
 * Formats a customer object to and from flat user format and MeCard Customer
 * types.
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 * @since 1.0
 */
public class FlatToMeCardCustomer extends NativeFormatToMeCardCustomer
{
    public final static String DEFAULT_DATE = "";

    @Override
    public Customer getCustomer(String customerString)
    {
        String[] custStrArray = customerString.split("\n");
        List<String> cList = new ArrayList<>();
        cList.addAll(Arrays.asList(custStrArray));
        return this.getCustomer(cList);
    }

    @Override
    public Customer getCustomer(List<String> customerList)
    {
        Customer customer = new Customer();
        for (String field : customerList)
        {
            String[] keyValue = this.parseRawCustomerData(field);
            String flatUserFieldName = keyValue[0];
            String flatUserFieldValue = keyValue[1];
            // we need the type of field this is
            CustomerFieldTypes fieldType = this.translateToCustomerField(flatUserFieldName);
            if (fieldType != null)
            {
                switch (fieldType)
                {
                // we also have to reformat the date from ANSI
                    case CITY:
                        // Sirsi puts pastes the city and state together so split them on ','
                        String[] cityState = flatUserFieldValue.split(",");
                        customer.set(CustomerFieldTypes.CITY, cityState[0]);
                        if (cityState[1] != null)
                        {
                            customer.set(CustomerFieldTypes.PROVINCE, cityState[1].trim());
                        }   break;
                    case POSTALCODE:
                        PostalCode pCode = new PostalCode(flatUserFieldValue);
                        customer.set(CustomerFieldTypes.POSTALCODE, pCode.toString());
                        break;
                    case DOB:
                    case PRIVILEGE_EXPIRES:
                        // one value that is a valid return value is 'NEVER' so
                        // lets screen for that now.
                        if (flatUserFieldValue.equalsIgnoreCase("NEVER"))
                        {
                            customer.set(fieldType, DEFAULT_DATE);
                        }
                        else
                        {
                            customer.set(fieldType, flatUserFieldValue);
                        }   break;
                    case PHONE:
                        Phone phone = new Phone(flatUserFieldValue);
                        //                    customerObject.set(fieldType, phone.toString());
                        customer.set(fieldType, phone.getUnformattedPhone());
                        break;
                    case PREFEREDNAME:
                        customer.setName(flatUserFieldValue);
                        break;
                    case LASTNAME:
                        customer.set(CustomerFieldTypes.LASTNAME, flatUserFieldValue);
                        break;
                    case FIRSTNAME:
                        customer.set(CustomerFieldTypes.FIRSTNAME, flatUserFieldValue);
                        break;
                    default:
                        customer.set(fieldType, flatUserFieldValue);
                        break;
                }
            }
        }
        return customer;
    }

    /**
     *
     * @param string
     * @return CustomerField that matches a customer field, or null.
     */
    private CustomerFieldTypes translateToCustomerField(String flatUserFieldValue)
    {
        switch (flatUserFieldValue)
        {
            case "USER_ID":
                return CustomerFieldTypes.ID;
            case "USER_NAME":
                return CustomerFieldTypes.PREFEREDNAME;
            case "USER_FIRST_NAME":
                return CustomerFieldTypes.FIRSTNAME;
            case "USER_LAST_NAME":
                return CustomerFieldTypes.LASTNAME;
            case "USER_PIN":
                return CustomerFieldTypes.PIN;
            case "USER_PRIV_EXPIRES":
                return CustomerFieldTypes.PRIVILEGE_EXPIRES;
            case "USER_BIRTH_DATE":
                return CustomerFieldTypes.DOB;
                // USER_CATAGORY2 is used for all types of reasons depending on how
                // the library has defined it.
                // TODO We need to allow libraries to define these dynamically.
                // Until then EPLs 'sex' will not get passed as a value to other libraries
                // but I haven't seen another that collects that metric anyway.
//            case "USER_CATEGORY2":
//                return CustomerFieldTypes.SEX;
            case "STREET":
                return CustomerFieldTypes.STREET;
            case "CITY/STATE":
                return CustomerFieldTypes.CITY;
            case "POSTALCODE":
                return CustomerFieldTypes.POSTALCODE;
            case "PHONE":
                return CustomerFieldTypes.PHONE;
            case "EMAIL":
                return CustomerFieldTypes.EMAIL;
            case "INACTVID":
            case "PREV_ID":
                return CustomerFieldTypes.ALTERNATE_ID;
            default:
                return null;
        }
    }

    private String[] parseRawCustomerData(String d)
    {
        String[] returnStrings =
        {
            "", ""
        }; // initialize to nothing, but not null.
        if (d.contains("|a"))
        {
            String[] splitField = d.split("\\|a");
            if (splitField[1] != null && splitField[1].length() > 0)
            {
                // parse and clean the key and value.
                returnStrings[0] = splitField[0].replaceAll("\\.", "").trim();
                returnStrings[1] = splitField[1].trim();
            }
        }
        return returnStrings;
    }
}
