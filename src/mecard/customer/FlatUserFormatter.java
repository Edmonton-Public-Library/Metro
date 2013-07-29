/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Andrew Nisbet
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
package mecard.customer;

import mecard.config.FlatUserFieldTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.FlatUserExtendedFieldTypes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mecard.Protocol;
import mecard.util.DateComparer;
import mecard.util.PostalCode;

/**
 * Formats a customer object to and from flat user format and MeCard Customer
 * types.
 *
 * @author metro
 * @since 1.0
 */
public class FlatUserFormatter implements CustomerFormatter
{
    public final static String DEFAULT_DATE = "";
    
    /**
     * Converts a customer into a flatUser that can be loaded by Symphony's
     * loadflatuser.
     * @param customer
     * @return Flat user data as a List of Strings.
     */
    @Override
    public List<String> setCustomer(Customer customer)
    {
        if (customer == null)
        {
            return new ArrayList<String>();
        }
        FlatUser flatUser = new FlatUser();
        flatUser.add(FlatUserFieldTypes.USER_ID, customer.get(CustomerFieldTypes.ID));
        flatUser.add(FlatUserFieldTypes.USER_PIN, customer.get(CustomerFieldTypes.PIN));
        flatUser.add(FlatUserFieldTypes.USER_FIRST_NAME, customer.get(CustomerFieldTypes.FIRSTNAME));
        flatUser.add(FlatUserFieldTypes.USER_LAST_NAME, customer.get(CustomerFieldTypes.LASTNAME));
        flatUser.add(FlatUserFieldTypes.USER_PREFERRED_NAME, customer.get(CustomerFieldTypes.NAME));
        // Dates
        flatUser.add(FlatUserFieldTypes.USER_BIRTH_DATE, customer.get(CustomerFieldTypes.DOB));
        flatUser.add(FlatUserFieldTypes.USER_PRIV_EXPIRES, customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES));
        // Address
        flatUser.add(
                FlatUserExtendedFieldTypes.USER_ADDR1, 
                FlatUserFieldTypes.STREET, 
                customer.get(CustomerFieldTypes.STREET));
        // Symphony uses CITY/STATE as a field (sigh)
        String city     = customer.get(CustomerFieldTypes.CITY);
        String province = customer.get(CustomerFieldTypes.PROVINCE);
        flatUser.add(
                FlatUserExtendedFieldTypes.USER_ADDR1, 
                FlatUserFieldTypes.CITY_STATE, 
                city + ", " + province.toUpperCase());
        flatUser.add(
                FlatUserExtendedFieldTypes.USER_ADDR1, 
                FlatUserFieldTypes.POSTALCODE, 
                PostalCode.formatPostalCode(customer.get(CustomerFieldTypes.POSTALCODE)));
        flatUser.add(
                FlatUserExtendedFieldTypes.USER_ADDR1, 
                FlatUserFieldTypes.EMAIL, 
                customer.get(CustomerFieldTypes.EMAIL));
        // Load optional fields.
        flatUser.add(
                FlatUserExtendedFieldTypes.USER_ADDR1, 
                FlatUserFieldTypes.PHONE, 
                mecard.util.Phone.formatPhone(customer.get(CustomerFieldTypes.PHONE)));
        String gender = customer.get(CustomerFieldTypes.GENDER);
        if (gender.contains(Protocol.DEFAULT_FIELD) == false)
        {
            // we will have to revisit this since I don't know how Horizon 
            //  designates different sexes.
            flatUser.add(FlatUserFieldTypes.USER_CATEGORY2, gender);
        }
        // There is not middle name field.
        // set todays date as the date privilege granted.
        flatUser.add(FlatUserFieldTypes.USER_PRIV_GRANTED,  DateComparer.today());
        // Now load all the default values we need to set like PROFILE.
        flatUser.setDefaultProperties();
        return flatUser.toList(); 
    }

    @Override
    public Customer getCustomer(String customerString)
    {
        String[] custStrArray = customerString.split("\n");
        List<String> cList = new ArrayList<String>();
        cList.addAll(Arrays.asList(custStrArray));
        return this.getCustomer(cList);
    }

    @Override
    public Customer getCustomer(List<String> customer)
    {
        Customer customerObject = new Customer();
        for (String field : customer)
        {
            String[] keyValue = this.parseRawCustomerData(field);
            String flatUserFieldName = keyValue[0];
            String flatUserFieldValue = keyValue[1];
            // we need the type of field this is
            CustomerFieldTypes fieldType = this.translateToCustomerField(flatUserFieldName);
            if (fieldType != null)
            {
                if (fieldType == CustomerFieldTypes.CITY)
                {
                    // Sirsi puts pastes the city and state together so split them on ','
                    String[] cityState = flatUserFieldValue.split(",");
                    customerObject.set(CustomerFieldTypes.CITY, cityState[0]);
                    if (cityState[1] != null)
                    {
                        customerObject.set(CustomerFieldTypes.PROVINCE, cityState[1].trim());
                    }
                } // we also have to reformat the date from ANSI
                else if (fieldType == CustomerFieldTypes.POSTALCODE)
                {
                    PostalCode pCode = new PostalCode(flatUserFieldValue);
                    customerObject.set(CustomerFieldTypes.POSTALCODE, pCode.toString());
                }
                else if (fieldType == CustomerFieldTypes.DOB
                            || fieldType == CustomerFieldTypes.PRIVILEGE_EXPIRES)
                {
                    // one value that is a valid return value is 'NEVER' so 
                    // lets screen for that now.
                    if (flatUserFieldValue.equalsIgnoreCase("NEVER"))
                    {
                        customerObject.set(fieldType, DEFAULT_DATE);
                    } 
                    else
                    {
                        customerObject.set(fieldType, flatUserFieldValue);
                    }
                }
                else if (fieldType == CustomerFieldTypes.PHONE)
                {
                    Phone phone = new Phone(flatUserFieldValue);
                    customerObject.set(fieldType, phone.toString());
                }
                else if (fieldType == CustomerFieldTypes.NAME)
                {
                    customerObject.setName(flatUserFieldValue);
                }
//                else if (fieldType == CustomerFieldTypes.LASTNAME)
//                {
//                    customerObject.set(CustomerFieldTypes.LASTNAME, flatUserFieldValue);
//                }
//                else if (fieldType == CustomerFieldTypes.FIRSTNAME)
//                {
//                    customerObject.set(CustomerFieldTypes.FIRSTNAME, flatUserFieldValue);
//                }
                else
                {
                    customerObject.set(fieldType, flatUserFieldValue);
                }    
            }
        }
        return customerObject;
    }

    /**
     *
     * @param string
     * @return CustomerField that matches a customer field, or null.
     */
    private CustomerFieldTypes translateToCustomerField(String flatUserFieldValue)
    {
        if (flatUserFieldValue.equals("USER_ID"))
        {
            return CustomerFieldTypes.ID;
        } 
        else if (flatUserFieldValue.equals("USER_NAME"))
        {
            return CustomerFieldTypes.NAME;
        }
        // uncommenting these lines cause the firt and last names to set in 
        // Customer, but any salutation will not come across.
//        else if (flatUserFieldValue.equals("USER_FIRST_NAME"))
//        {
//            return CustomerFieldTypes.FIRSTNAME;
//        } 
//        else if (flatUserFieldValue.equals("USER_LAST_NAME"))
//        {
//            return CustomerFieldTypes.LASTNAME;
//        } 
        else if (flatUserFieldValue.equals("USER_PIN"))
        {
            return CustomerFieldTypes.PIN;
        } 
        else if (flatUserFieldValue.equals("USER_PRIV_EXPIRES"))
        {
            return CustomerFieldTypes.PRIVILEGE_EXPIRES;
        } 
        else if (flatUserFieldValue.equals("USER_BIRTH_DATE"))
        {
            return CustomerFieldTypes.DOB;
        } 
        // TODO handle variance of library usage of CAT2.
        // a little dangerous since other libraries may not use CAT2 for gender,
        // suggest this be a field that loaded from a property file.
        else if (flatUserFieldValue.equals("USER_CATEGORY2"))
        {
            return CustomerFieldTypes.GENDER;
        } 
        else if (flatUserFieldValue.equals("STREET"))
        {
            return CustomerFieldTypes.STREET;
        } 
        else if (flatUserFieldValue.equals("CITY/STATE"))
        {
            return CustomerFieldTypes.CITY;
        } 
        else if (flatUserFieldValue.equals("POSTALCODE"))
        {
            return CustomerFieldTypes.POSTALCODE;
        } 
        else if (flatUserFieldValue.equals("PHONE"))
        {
            return CustomerFieldTypes.PHONE;
        } 
        else if (flatUserFieldValue.equals("EMAIL"))
        {
            return CustomerFieldTypes.EMAIL;
        } 
        else
        {
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

    private static class Phone
    {
        private String phone;
        public Phone(String p)
        {
            phone = new String();
            if (p != null)
            {
                phone = p.replaceAll("-", "");
                phone = phone.replaceAll("\\(", "");
                phone = phone.replaceAll("\\)", "");
            }
        }
        
        @Override
        public String toString()
        {
            return phone;
        }
    }
}
