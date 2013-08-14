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

import mecard.QueryTypes;
import mecard.customer.Customer;
import mecard.config.CustomerFieldTypes;

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
        authorityToken = "";
        customer = null;
        pin = "";
        userId = "";
    }

    /**
     *
     * @return the request code.
     */
    public QueryTypes getCommandType()
    {
        return this.code;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[\"");
        sb.append(this.code.name());
        sb.append("\", \"");
        sb.append(this.userId);
        sb.append("\", \"");
        sb.append(this.pin);
        if (customer != null)
        {
            sb.append("\", \"");
            sb.append(this.customer.toString());
        }
        sb.append("\"");
        sb.append("]");
        return sb.toString();
    }

    /**
     * 
     * @return authority token, the API key of the requester.
     */
    public String getTransactionId()
    {
        return this.authorityToken;
    }

    /**
     * 
     * @param cField - the customer field type you want.
     * @return the contents of the field or null if the field is not set.
     */
    public String getCustomerField(CustomerFieldTypes cField)
    {
        if (this.customer == null)
        {
            return "";
        }
        return this.customer.get(cField);
    }

    public String getUserId()
    {
        return this.userId;
    }

    public String getUserPin()
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
