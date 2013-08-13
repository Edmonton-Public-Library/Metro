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
package api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import mecard.exception.SIPException;

/**
 * Helps with the interpretation of SIP2 messages.
 * recv:98YYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYYYYYNNYYY|ANSIPCHK|AY1AZE80C
   St. Albert returns
   recv:98YYYYNN01000320130808    1509002.00AOst|AMSt. Albert Public Library|BXYYYYYYYYYYYYYYYY|ANUnassigned|VNSIP 2.00.106 SirsiDynix Inc. 7.5.074.40|AY1AZD3FA
   We need to check that the values at position 2 (online status) 
   and 56 (Patron Information) are both 'Y'
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SIPMessage
{
    protected HashMap<String, String> fields;
    protected final String originalMessage;
    protected final String code;
    protected final String codeBits;

    public SIPMessage(String sipMessage)
            throws SIPException
    {
        this.originalMessage = sipMessage;
        if (sipMessage.contains("|") == false)
        {
            throw new SIPException(SIPMessage.class.getName()
                    + "'" + sipMessage
                    + "' does not appear to be a SIP2 message.");
        }
        this.fields = new HashMap<String, String>();
        // Split the fields
        String[] stringFields = sipMessage.split("\\|");
        try
        {
            this.code = stringFields[0].substring(0, 2);
            this.codeBits = stringFields[0].substring(2);
        }
        catch (IndexOutOfBoundsException ex)
        {
            throw new SIPException(" Index was out of bounds while parsing "
                    + "SIP2 code section. This may not be a SIP2 message, or it "
                    + "be malformed: '" + sipMessage + "'");
        }
        for (int i = 1; i < stringFields.length; i++)
        {
            String key; // 2 letter code like BX
            String value; // The rest of the message.
            try
            {
                key = stringFields[i].substring(0, 2); // 2 letter code like BX
                value = stringFields[i].substring(2); // reset of the message
            }
            catch (IndexOutOfBoundsException ex)
            {
                throw new SIPException(SIPMessage.class.getName()
                        + " Index was out of bounds while parsing "
                        + "SIP2 message. This may not be a SIP2 message, or it "
                        + "be malformed: '" + sipMessage + "'");
            }
            // put it in the hash
            this.fields.put(key, value);
        }
    }
    
    /**
     * 
     * @return String version of the initial SIP2 code.
     */
    public final String getCode()
    {
        return this.code;
    }
    
    /**
     * 
     * @return String of the bit field associated with the code field.
     */
    public final String getCodeMessage()
    {
        return this.codeBits;
    }

    /**
     *
     * @return List of names of fields in the message.
     */
    public final List<String> getFieldNames()
    {
        List<String> myListOfKeys = new ArrayList<String>();
        Set<String> keySet = this.fields.keySet();
        for (String s : keySet)
        {
            myListOfKeys.add(s);
        }
        return myListOfKeys;
    }

    /**
     * 
     * @param which the code of the field you want.
     * @return the contents of that field
     */
    public final String getField(String which)
    {
        return this.fields.get(which.toUpperCase()); // which could return null
    }

    @Override
    public String toString()
    {
        return this.originalMessage;
    }
}