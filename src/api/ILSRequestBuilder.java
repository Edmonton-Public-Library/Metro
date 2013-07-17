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

import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;

/**
 * ILSRequestBuilder outlines the contract that all implementers promise to fulfill.
 * Note: Programmers implementing a new builder will probably not be able to 
 * use all these methods. Symphony for instance, does not have a good facility 
 * to get the ILS status. In that case sub-class the ILSRequestAdaptor, and make 
 * sure you don't specify that method of response in the environment.properties
 * file.
 * @author andrew
 * @see ILSRequestAdaptor
 */
public interface ILSRequestBuilder
{
    /**
     * Implementers promise to return a Command that, when run, will return the
     * customer's information.
     *
     * @param userId the value of userId
     * @param userPin the value of userPin
     * @param response Buffer to contain useful response information.
     */ 
     
    public Command getCustomer(String userId, String userPin, Response response);
    
    /**
     * Gets the CustomerFormatter related to the implementer of the subclass.
     * @return CustomerFormatter.
     */
    public CustomerFormatter getFormatter();

    /**
     * Creates a user based on the supplied customer, which must not be null.
     *
     * @param customer
     * @param response
     * @return command that can be executed on the ILS to create a customer.
     */
    
    
    public Command createUser(Customer customer, Response response);

    /**
     * Updates a user based on the supplied customer, which must not be null.
     * @param customer
     * @param response
     * @return command that can be executed on the ILS to update a customer.
     */
    public Command updateUser(Customer customer, Response response);

    /**
     * Gets the status of the ILS.
     * @param response
     * @return Command necessary to test the ILS status.
     */
    public Command getStatus(Response response);
}
