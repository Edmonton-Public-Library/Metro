/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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

import mecard.config.CustomerFieldTypes;
import mecard.Protocol;
import java.util.EnumMap;

/**
 * Convenience class for managing customer data in a standardized way.
 * 
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public class Customer
{
    private final EnumMap<CustomerFieldTypes, String> customerFields;
    
    public Customer()
    {
        customerFields = new EnumMap<>(CustomerFieldTypes.class);
        normalizeFields();
    }

    /**
     * Used when the name passed is in the form of 'last, first' name, this 
     * method parses and populates LASTNAME and FIRSTNAME fields. 
     *
     * @param name like "lastname, firstname"
     */
    public void setName(String name)
    {
        if (name == null || name.length() == 0)
        {
            return;
        }
        this.customerFields.put(CustomerFieldTypes.PREFEREDNAME, name);
        // Sometimes services return names that are the 'lastName, firstName'.
        // we always assume the last name is first. If only one name is supplied
        // we assume it the last name and set the content string to that, other-
        // wise we set the content to last name and firstName to, well, first
        // name
        String deCommadName = name.replaceAll(",", " ");
        String[] cName = new String[2];
        int pos = deCommadName.indexOf(" ");
        if (pos > 0)
        {
            cName[0] = deCommadName.substring(0, pos);
            cName[1] = deCommadName.substring(pos);
        }
        else
        {
            cName[0] = Protocol.DEFAULT_FIELD_VALUE;
            cName[1] = deCommadName; // no spaces so just a last name?
        }
        this.customerFields.put(CustomerFieldTypes.FIRSTNAME, cName[1].trim());
        this.customerFields.put(CustomerFieldTypes.LASTNAME,  cName[0].trim());
    }
    
    /**
     * Sets the customer's field (ft) with the specified value. The new value 
     * will be appended to any existing value in the customer.
     * 
     * @param fieldType
     * @param value 
     */
    public void set(CustomerFieldTypes fieldType, String value)
    {
        if (fieldType.equals(CustomerFieldTypes.PREFEREDNAME))
        {
            this.setName(value);
        }
        else
        {
            this.customerFields.put(fieldType, value);
        }
    }

    /**
     * Gets the value specified by the argument field type.
     * @param t
     * @return the stored value.
     */
    public String get(CustomerFieldTypes t)
    {
        String returnValue = this.customerFields.get(t); // which could return null
        if (returnValue == null)
        {
            return Protocol.DEFAULT_FIELD_VALUE;
        }
        return returnValue;
    }

    /**
     * Ensures that empty fields are populated with default values.
     * @see Protocol#DEFAULT_FIELD_VALUE
     */
    private void normalizeFields()
    {
        // set name field if empty
        for (CustomerFieldTypes cType: CustomerFieldTypes.values())
        {
            if (this.customerFields.get(cType) == null || this.customerFields.get(cType).isEmpty())
            {
                this.customerFields.put(cType, Protocol.DEFAULT_FIELD_VALUE);
            }
        }
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (CustomerFieldTypes cType: CustomerFieldTypes.values())
        {
            if (customerFields.get(cType) != null)
            {
                sb.append(customerFields.get(cType));
                sb.append(", ");
            }            
        }
        // remove the last ', '.
        sb.delete(sb.length()-2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    /**
     * Tests if a given field is empty.
     * @param customerFieldTypes field to test.
     * @return true if the field is set to {@link Protocol#DEFAULT_FIELD_VALUE}, 
     * is null, or an empty string, and false otherwise.
     */
    public boolean isEmpty(CustomerFieldTypes customerFieldTypes)
    {
        if (this.get(customerFieldTypes) == null || 
                this.get(customerFieldTypes).isBlank())
        {
            return true;
        }
        return (this.get(customerFieldTypes).compareTo(
                Protocol.DEFAULT_FIELD_VALUE) == 0);
    }
    
    /**
     * Since there are 3 states we have three functions to test with. This one
     * tests if the customer flat is set to Me Card's definition of TRUE.
     * @param flag customer flags start with IS...
     * @return true if the flag is 'Y' and false if 'N' or 'X'.
     */
    public boolean isFlagSetTrue(CustomerFieldTypes flag)
    {
        String flagValue = this.get(flag);
        return flagValue.compareTo(Protocol.TRUE) == 0;
    }
    
    /**
     * Since there are 3 states we have three functions to test with. This one
     * tests if the customer flat is set to Me Card's definition of False.
     * @param flag customer flags start with IS...
     * @return true if the flag is 'N' and false if 'Y' or 'X'.
     */
    public boolean isFlagSetFalse(CustomerFieldTypes flag)
    {
        String flagValue = this.get(flag);
        return flagValue.compareTo(Protocol.FALSE) == 0;
    }
    
    /**
     * Since there are 3 states we have three functions to test with. This one
     * tests if the customer flat is set to Me Card's definition of {@link Protocol#DEFAULT_FIELD_VALUE}.
     * @param flag customer flags start with IS...
     * @return true if the flag is 'Y' and false if 'N' or 'X'.
     */
    public boolean isFlagDefined(CustomerFieldTypes flag)
    {
        String flagValue = this.get(flag);
        return flagValue.compareTo(Protocol.DEFAULT_FIELD_VALUE) != 0;
    }
    
    /**
     * Tests if the customer has been identified as a lost card.
     * @return true if the customer's lost card field is set and false otherwise.
     */
    public boolean isLostCard()
    {
        return this.isFlagSetTrue(CustomerFieldTypes.ISLOSTCARD);
    }
}
