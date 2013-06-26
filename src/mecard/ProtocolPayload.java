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
package mecard;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
 * The Protocol payload is anything that makes up a request or response, but does not
 * include the command/response code or API key. Underneath this class is a simple 
 * list object with correct formatting for the MeCard protocol.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class ProtocolPayload 
{
    protected List<String> payload;
    
    public ProtocolPayload()
    {
        payload = new ArrayList<String>();
    }
    
    /**
     * Used when a data structure has a number of fields whose field contents are
     * not known at the time of construction. 
     * @see mecard.customer.Customer
     * @param expectedSize 
     */
    public ProtocolPayload(int expectedSize) 
    {
        payload = new ArrayList<String>(expectedSize);
        for (int i = 0; i < expectedSize; i++)
        {
            this.payload.add(Protocol.DEFAULT_FIELD); // initialize customer fields.
        }
    }
    
    /**
     * 
     * @return number of items in the payload, which will include the count of
     * entries that have not been set yet as well.
     */
    public int size()
    {
        return payload.size();
    }
    
    /**
     * Adds text onto the payload when the payload is a Response object.
     * @param s places s on the end of the payload.
     * @see api.Response
     */
    public void addResponse(String s)
    {
        this.payload.add(s);
    }
    
    /**
     * Inserts a string into a specified field in the payload.
     * @param pos the index in the list.
     * @param s the value to store in the specified slot.
     */
    public void setPayloadSlot(int pos, String s)
    {
        this.payload.set(pos, s);
    }
    
    /** 
     * 
     * @return the payload as a List.
     */
    public List<String> getPayload()
    {
        return this.payload;
    }
    
    /**
     * Formats the payload into a JSON string.
     * @return String version of just the payload.
     */
    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this.payload);
    }
}
