/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.customer;

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
     * Used when the name passed is in the form of 'last, first' name.
     * @param "lastname, firstname" 
     */
    public void setName(String name)
    {
        if (name == null && name.length() == 0)
        {
            return ;
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
    }
    
    public void set(CustomerFieldTypes ft, String value)
    {
        this.setPayloadSlot(ft.ordinal(), value);
    }
    
    public String get(CustomerFieldTypes t)
    {
        return this.response.get(t.ordinal());
    }
}
