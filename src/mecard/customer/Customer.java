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

import mecard.Protocol;
import java.util.EnumMap;
import mecard.Exception.InvalidCustomerException;

/**
 * 
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class Customer //extends ProtocolPayload
{
    private EnumMap<CustomerFieldTypes, String> customerFields;
    
    public Customer()
    {
        customerFields = new EnumMap<CustomerFieldTypes, String>(CustomerFieldTypes.class);
        normalizeFields();
    }

    /**
     * Takes a string representation of a customer and parses it into the
     * correct fields. Note that this object does not enforce the order of entries
     * since it assumes that the initial customer data was well formed, and there
     * is no way to determine if the content of a field matches the appropriate 
     * field.
     *
     * @param c
     * @deprecated 
     */
    public Customer(String c) throws InvalidCustomerException
    {
        this.customerFields = new EnumMap<CustomerFieldTypes, String>(CustomerFieldTypes.class);
        try
        {
            this.splitCustomerFields(c);
        }
        catch (RuntimeException ex)
        {
            String msg = "The supplied JSON does not represent a valid customer's data.";
            throw new InvalidCustomerException(msg);
        }
        normalizeFields();
    }

    /**
     * Split the commandArguments on the Protocol's delimiter breaking the
     * commandArguments into chunks. The first element on the list is the
     * commandArguments itself which can be ignored since it was already dealt
     * with when this object was created. The second is the MD5 hash of the
     * query salted with the senders shared secret. The rest of the elements (if
     * any) are arguments to the commandArguments.
     *
     * @param customerData
     * @return
     * @deprecated 
     */
    private void splitCustomerFields(String customerData)
    {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(Customer.class, new CustomerDeserializer());
//        Gson gson = gsonBuilder.create();
//        Reader data = new StringReader(customerData);
//        Customer c = gson.fromJson(data, Customer.class);
//        this.customerFields = c.customerFields;
    }

    /**
     * Used when the name passed is in the form of 'last, first' name.
     *
     * @param "lastname, firstname"
     */
    public void setName(String name)
    {
        if (name == null && name.length() == 0)
        {
            return;
        }
        this.customerFields.put(CustomerFieldTypes.NAME, name);
        // Sometimes services return names that are the 'lastName, firstName'.
        // we always assume the last name is first. If only one name is supplied
        // we assume it the last name and set the content string to that, other-
        // wise we set the content to last name and firstName to, well, first
        // name
        if (name.contains(",") == false)
        {
            return;
        }
        String[] cName = name.split(",");
        // Do first name 
        if (cName.length > 1)
        {
            this.customerFields.put(CustomerFieldTypes.FIRSTNAME, cName[1].trim());
        }
        this.customerFields.put(CustomerFieldTypes.LASTNAME, cName[0].trim());
    }
    
    /**
     * Sets the customer's field (ft) with the specified value. 
     * @param ft
     * @param value 
     */
    public void set(CustomerFieldTypes ft, String value)
    {
        if (ft.equals(CustomerFieldTypes.NAME))
        {
            this.setName(value);
        }
        else
        {
            this.customerFields.put(ft, value);
        }
    }

    /**
     * Gets the value specified by the argument field type.
     * @param t
     * @return the stored value.
     */
    public String get(CustomerFieldTypes t)
    {
        return this.customerFields.get(t);
    }

    /**
     * Ensures that empty fields are populated with default values.
     * @see Protocol#DEFAULT_FIELD
     */
    private void normalizeFields()
    {
        // set name field if empty
        for (CustomerFieldTypes cType: CustomerFieldTypes.values())
        {
            if (this.customerFields.get(cType) == null || this.customerFields.get(cType).isEmpty())
            {
                this.customerFields.put(cType, Protocol.DEFAULT_FIELD);
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
            sb.append(customerFields.get(cType));
            sb.append(", ");
        }
        // remove the last ', '.
        sb.delete(sb.length()-2, sb.length());
        sb.append("]");
        return sb.toString();
    }
}
