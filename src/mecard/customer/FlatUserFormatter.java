/**
 *
 * This class is part of the Metro, MeCard project. Copyright (C) 2013 Andrew
 * Nisbet, Edmonton public Library.
 * 
* This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
* This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
* You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
*/
package mecard.customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @Override
    public boolean setCustomer(Customer customer)
    {
        return true; // does the same as getCustomer(List<String>) but for a single line customer
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
        } // a little dangerous since other libraries may not use CAT2 for gender.
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
