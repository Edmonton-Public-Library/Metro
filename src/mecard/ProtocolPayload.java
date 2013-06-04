/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple list object with correct formatting for the MeCard protocol formatting.
 * @author metro
 */
public class ProtocolPayload 
{
    protected List<String> response;
    public ProtocolPayload()
    {
        response = new ArrayList<String>();
    }
    
    public ProtocolPayload(int expectedSize) 
    {
        response = new ArrayList<String>(expectedSize);
        for (int i = 0; i < expectedSize; i++)
        {
            this.response.add(""); // initialize customer with empty values for all fields.
        }
    }
    
    public int size()
    {
        return response.size();
    }
    
    public void addPayload(String s)
    {
        this.response.add(s);
    }
    
    public void setPayloadSlot(int pos, String s)
    {
        this.response.set(pos, s);
    }
    
    public List<String> getPayload()
    {
        return this.response;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (String s: response)
        {
            sb.append(s);
            sb.append(Protocol.DELIMITER);
        }
        return sb.toString();
    }
}
