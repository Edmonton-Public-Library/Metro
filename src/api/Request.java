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

import com.google.gson.Gson;
import mecard.Exception.MalformedCommandException;
import mecard.ProtocolPayload;
import mecard.QueryTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFieldTypes;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class Request //extends ProtocolPayload
{

    private QueryTypes code;
    private String authorityToken;
    private Customer customer;
    private String pin;
    private String userId;
    
    public Request()
    {
        code = QueryTypes.NULL;
    }

    public Request(String request)
    {
        // Instantiate query type ot NULL.
        code = QueryTypes.NULL;
        // split to command into it's parts
        this.splitCommand(request);
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
    private void splitCommand(String cmd)
            throws MalformedCommandException
    {
        Gson gson = new Gson();
        Request r = gson.fromJson(cmd, Request.class);
        this.authorityToken = r.authorityToken;
        this.code = r.code;
        this.customer = r.customer;
        this.pin = r.pin;
        this.userId = r.userId;
    }

    /**
     *
     * @return the request code.
     */
    public QueryTypes getCommandType()
    {
        return this.code;
    }

    /**
     *
     * @return the request arguments of the request, not including any request
     * code or authority token.
     * @see ProtocolPayload#toString()
     */
    public String getArgs()
    {
        return super.toString();
    }

    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this, Request.class);
    }

    public String getTransactionId()
    {
        return this.authorityToken;
    }

    /**
     *
     *
     */
    public String getCustomerField(CustomerFieldTypes cField)
    {
        if (this.customer == null)
        {
            return "";
        }
        return this.customer.get(cField);
    }

    String getUserId()
    {
        return this.userId;
    }

    String getUserPin()
    {
        return this.pin;
    }
    
    public Customer getCustomer()
    {
        return this.customer;
    }

    public void setCode(QueryTypes code)
    {
        this.code = code;
    }

    public void setAuthorityToken(String authorityToken)
    {
        this.authorityToken = authorityToken;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public void setPin(String pin)
    {
        this.pin = pin;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}
