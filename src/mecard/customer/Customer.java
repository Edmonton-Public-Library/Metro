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

import java.util.List;
import mecard.Protocol;
import mecard.ProtocolPayload;
import com.google.gson.Gson;
import mecard.Exception.MalformedCommandException;

/**
 * 
 * @author metro
 */
public class Customer extends ProtocolPayload
{
    private String firstName;
    private String lastName;

    public Customer()
    {
        super(CustomerFieldTypes.size());
        firstName = "";
        lastName = "";
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
    public Customer(String c)
    {
        super(CustomerFieldTypes.size());
        firstName = "";
        lastName = "";
        this.splitCustomerFields(c);
    }
    
    private void insertResponse(String s)
    {
        if (payload.size() > 0)
        {
            this.payload.remove(0);
        }
        this.addResponse(s);
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
        Gson gson = new Gson();
        try
        {
            List<String> cmdLine = gson.fromJson(cmd, List.class);
            // ignore the command code and authority (API) token.
            for (int i = 2; i < cmdLine.size(); i++)
            {
//                this.addResponse(cmdLine.get(i));
                this.insertResponse(cmdLine.get(i));
            }
        }
        catch (IndexOutOfBoundsException ex)
        {
            throw new MalformedCommandException("queries must include an API key.");
        }
//        String[] cmdLine = cmd.split("\\" + Protocol.DELIMITER);
//        List<String> cmdList = new ArrayList<String>();
//        for (int i = 2; i < cmdLine.length; i++)
//        {
//            cmdList.add(cmdLine[i]);
//        }
//        // test if we have all the fields. they are supposed to be either
//        // initialized with a default value (See Protocol) or have a value in them.
//        if (cmdList.size() != CustomerFieldTypes.size())
//        {
//            throw new InvalidCustomerException("expected " 
//                    + CustomerFieldTypes.size() 
//                    + " but got " 
//                    + cmdList.size());
//        }
//
//        // 1 for command, 1 for authority token. All commands
//        // TODO: Fix this. it doesn't work
//        for (int i = 0; i < cmdList.size(); i++)
//        {
//            this.payload.set(i, cmdList.get(i));
//        }
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
        this.setPayloadSlot(CustomerFieldTypes.NAME.ordinal(), name);
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
            this.set(CustomerFieldTypes.FIRSTNAME, cName[1].trim());
        }
        this.set(CustomerFieldTypes.LASTNAME, cName[0].trim());
        
    }
    
    /**
     * Sets the customer's field (ft) with the specified value. 
     * @param ft
     * @param value 
     */
    public void set(CustomerFieldTypes ft, String value)
    {
//        System.out.println("CustomerFieldType ordinal:"+ft.toString());
        if (ft == CustomerFieldTypes.FIRSTNAME)
        {
            firstName = value;
        }
        if (ft == CustomerFieldTypes.LASTNAME)
        {
            lastName = value;
        }
        this.setPayloadSlot(ft.ordinal(), value);
    }

    /**
     * Gets the value specified by the argument field type.
     * @param t
     * @return the stored value.
     */
    public String get(CustomerFieldTypes t)
    {
        return this.payload.get(t.ordinal());
    }

    /**
     * Ensures that empty fields are populated with default values.
     * @see Protocol#DEFAULT_FIELD
     */
    private void normalizeFields()
    {
        // set name field if empty
        
        if (! firstName.isEmpty() && ! lastName.isEmpty())
        {
            this.set(CustomerFieldTypes.NAME, 
               get(CustomerFieldTypes.LASTNAME) + ", " + get(CustomerFieldTypes.FIRSTNAME));
        }
        for (int i = 0; i < payload.size(); i++)
        {
            if (this.payload.get(i).trim().isEmpty())
            {
                this.payload.set(i, Protocol.DEFAULT_FIELD);
            }
        }
    }
    
    @Override
    public String toString()
    {
        if (firstName.isEmpty() == false && lastName.isEmpty() == false)
        {
            this.set(CustomerFieldTypes.NAME, (get(CustomerFieldTypes.LASTNAME) + ", " + get(CustomerFieldTypes.FIRSTNAME)));
        }
        normalizeFields();
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        return sb.toString();
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
}
