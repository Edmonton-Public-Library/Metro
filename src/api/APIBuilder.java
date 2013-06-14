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
import mecard.util.Command;

/**
 *
 * @author andrew
 */
public interface APIBuilder
{
    /**
     *
     *
     * @param userId the value of userId
     * @param userPin the value of userPin
     */ 
    public Command getUser(String userId, String userPin, StringBuffer responseBuffer);
    public CustomerFormatter getFormatter();

    /**
     * Creates a user based on the supplied customer, which must not be null.
     * @param customer
     * @param responseBuffer
     * @return command that can be executed on the ILS to create a customer.
     */
    public Command createUser(Customer customer, StringBuffer responseBuffer);

    /**
     * Updates a user based on the supplied customer, which must not be null.
     * @param customer
     * @param responseBuffer
     * @return command that can be executed on the ILS to update a customer.
     */
    public Command updateUser(Customer customer, StringBuffer responseBuffer);
}
