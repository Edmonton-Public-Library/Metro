/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2020  Edmonton Public Library
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

import mecard.customer.Customer;

/**
 * This class represents a Metro request. It includes the query code, authorization
 * token, customer which may be 'null', user id, and PIN which in some cases may be empty
 * depending on the query type.
 * @author Andrew Nisbet andrew.nisbet@epl.ca or andrew@dev-ils.com
 */
public final class Request
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
     * @return the user id.
     */
    public String getUserId()
    {
        return this.userId;
    }

    /**
     * 
     * @return the user's PIN.
     */
    public String getUserPin()
    {
        return this.pin;
    }
    
    /**
     * 
     * @return the customer object.
     */
    public Customer getCustomer()
    {
        return this.customer;
    }

    /**
     * 
     * @param code of the query type.
     */
    public void setCode(QueryTypes code)
    {
        this.code = code;
    }

    /**
     * 
     * @param authorityToken 
     */
    public void setAuthorityToken(String authorityToken)
    {
        this.authorityToken = authorityToken;
    }

    /**
     * Sets the customer object.
     * @param customer object.
     */
    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    /**
     * Sets the user's PIN. Required by the ILS before returning the user data.
     * @param pin 
     */
    public void setPin(String pin)
    {
        this.pin = pin;
    }

    /**
     * Sets the user's ID, AKA library card or barcode.
     * @param userId 
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}
