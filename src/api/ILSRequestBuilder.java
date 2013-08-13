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

import mecard.QueryTypes;
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
 */
public interface ILSRequestBuilder
{
    /**
     * Implementers promise to return a APICommand that, when run, will return the
     * customer's information.
     *
     * @param userId the value of userId
     * @param userPin the value of userPin
     * @param response Buffer to contain useful response information.
     */
    public Command getCustomerCommand(String userId, String userPin, Response response);
    
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
    
    
    public Command getCreateUserCommand(Customer customer, Response response);

    /**
     * Updates a user based on the supplied customer, which must not be null.
     *
     * @param customer
     * @param response
     * @return command that can be executed on the ILS to update a customer.
     */
    
    public Command getUpdateUserCommand(Customer customer, Response response);

    /**
     * Gets the status of the ILS.
     *
     * @param response
     * @return APICommand necessary to test the ILS status.
     */
    
    public Command getStatusCommand(Response response);
    
    /**
     * Interprets the results of the ILS command into a meaningful message for
     * the customer.
     *
     * @param commandType the value of commandType
     * @param status the status of the command that ran on the ILS.
     * @param response the object to be returned to melibraries.ca.
     */
    public void interpretResults(QueryTypes commandType, CommandStatus status, Response response);
}
