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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
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
     * @param cmd
     * @return
     */
    private void splitCustomerFields(String cmd)
    {
//        Gson gson = new Gson();
//        Customer c = gson.fromJson(cmd, Customer.class);
//        this.customerFields = c.customerFields;
        
        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<EnumMap<CustomerFieldTypes, String>>(){ }.getType(),
            new EnumMapInstanceCreator<CustomerFieldTypes, String>(CustomerFieldTypes.class)).create();
        this.customerFields = gson.fromJson(cmd, new TypeToken<EnumMap<CustomerFieldTypes, String>>(){ }.getType());
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
//        if ((get(CustomerFieldTypes.FIRSTNAME).compareTo(Protocol.DEFAULT_FIELD) != 0) 
//                && (get(CustomerFieldTypes.LASTNAME).compareTo(Protocol.DEFAULT_FIELD) != 0)
//                && (get(CustomerFieldTypes.LASTNAME).compareTo(Protocol.DEFAULT_FIELD) == 0))
//        {
//            this.set(CustomerFieldTypes.NAME, 
//              (get(CustomerFieldTypes.LASTNAME) + ", " + get(CustomerFieldTypes.FIRSTNAME)));
//        }
        normalizeFields();
//        StringBuilder sb = new StringBuilder();
//        sb.append(super.toString());
//        return sb.toString();
        Gson gson = new Gson();
//        return gson.toJson(this);
//        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<EnumMap<CustomerFieldTypes, String>>(){ }.getType(),
//            new EnumMapInstanceCreator<CustomerFieldTypes, String>(CustomerFieldTypes.class)).create();
        return gson.toJson(this.customerFields);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Customer))
        {
            return false;
        }
        
        if (this.toString().compareTo(o.toString()) != 0)
        {
            return false;
        }
        
        return true;
    }
    
    
    private class EnumMapInstanceCreator<K extends Enum<K>, V> implements
        InstanceCreator<EnumMap<K, V>> 
    {
        private final Class<K> enumClazz;

        public EnumMapInstanceCreator(final Class<K> enumClazz) 
        {
        super();
        this.enumClazz = enumClazz;
        }

        @Override
        public EnumMap<K, V> createInstance(Type type) 
        {
            return new EnumMap<K, V>(enumClazz);
        }
    }
}
