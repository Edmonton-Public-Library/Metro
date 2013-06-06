/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.customer;

import java.util.ArrayList;
import java.util.List;
import mecard.Exception.InvalidCustomerException;
import mecard.Protocol;
import mecard.ProtocolPayload;

/**
 * 
 * @author metro
 */
public class Customer extends ProtocolPayload
{

    public Customer()
    {
        super(CustomerFieldTypes.size());
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
        this.splitCustomerFields(c);
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
        String[] cmdLine = cmd.split("\\" + Protocol.DELIMITER);
        List<String> cmdList = new ArrayList<String>();
        for (int i = 2; i < cmdLine.length; i++)
        {
            cmdList.add(cmdLine[i]);
        }
        // test if we have all the fields. they are supposed to be either
        // initialized with a default value (See Protocol) or have a value in them.
        if (cmdList.size() != CustomerFieldTypes.size())
        {
            throw new InvalidCustomerException();
        }

        // 1 for command, 1 for authority token. All commands
        for (int i = 2; i < cmdList.size(); i++)
        {
            this.payload.set(i, cmdList.get(i));
        }
        normalizeFields();
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
            this.setPayloadSlot(CustomerFieldTypes.FIRSTNAME.ordinal(), cName[1].trim());
        }
        this.setPayloadSlot(CustomerFieldTypes.LASTNAME.ordinal(), cName[0].trim());
        normalizeFields();
    }
    
    public void set(CustomerFieldTypes ft, String value)
    {
        this.setPayloadSlot(ft.ordinal(), value);
    }

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
        for (int i = 0; i < CustomerFieldTypes.size(); i++)
        {
            if (this.payload.get(i).length() == 0)
            {
                this.payload.set(i, Protocol.DEFAULT_FIELD);
            }
        }
    }
}
