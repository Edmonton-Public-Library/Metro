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
package mecard.responder;

import api.Response;
import mecard.customer.Customer;

/**
 * Indicates that the implementer can update users.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public interface Updateable
{
    /**
     * 
     * @param response 
     */
    public void updateCustomer(Response response);
    
    /**
     * Populates the response object with the results from the createCustomer
     * command. The side effect is the customer is created on the ILS.
     * @param response the value of responseBuffer
     */
    public void createCustomer(Response response);
    
    /**
     * This method is meant to be used to normalize customer data before loading
     * on a local ILS. The motivation for this is St. Albert's PIN requirement
     * that the customer load will fail if the PIN is greater than 4 characters.
     * There is no such restriction at other libraries. A library can implement
     * a policy within the normalize method that massages customer data to ensure
     * correct loading. The advantage is that because the argument response can
     * take a message, we can signal the user of changes we had to make to their 
     * account to make it work.
     * 
     * @param response
     * @param customer the value of customer
     */
    
    public void normalize(Response response, Customer customer);
}
