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
    protected List<String> payload;
    
    public ProtocolPayload()
    {
        payload = new ArrayList<String>();
    }
    
    public ProtocolPayload(int expectedSize) 
    {
        payload = new ArrayList<String>(expectedSize);
        for (int i = 0; i < expectedSize; i++)
        {
            this.payload.add(Protocol.DEFAULT_FIELD); // initialize customer fields.
        }
    }
    
    public int size()
    {
        return payload.size();
    }
    
    public void addResponse(String s)
    {
        this.payload.add(s);
    }
    
    public void setPayloadSlot(int pos, String s)
    {
        this.payload.set(pos, s);
    }
    
    public List<String> getPayload()
    {
        return this.payload;
    }
    
    /**
     * 
     * @return String version of just the payload.
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (String s: payload)
        {
            sb.append(s);
            sb.append(Protocol.DELIMITER);
        }
        return sb.toString();
    }
}
