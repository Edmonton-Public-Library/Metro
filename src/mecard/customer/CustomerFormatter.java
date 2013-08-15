/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
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
package mecard.customer;

import java.util.List;

/**
 * This class formats requests and responses to and from the ILS.
 * @author metro
 */
public interface CustomerFormatter
{
    /**
     * Converts a string from ILS or SIP into a Customer object.
     * @param list of strings that represent the customer as it would have 
     * been received from the ILS.
     * @return true if the conversion was successful and false otherwise.
     */
    public Customer getCustomer(List<String> list);
    /**
     * Converts a string from ILS or SIP into a Customer object.
     * @param s the string that represent the customer as it would have 
     * been received from the ILS.
     * @return true if the conversion was successful and false otherwise.
     */
    public Customer getCustomer(String s);
    
    /**
     *
     * @param c the Customer object to convert into a list to be consumed by 
     * a subtype.
     * @return the List<String>
     */
    public List<String> setCustomer(Customer c);
}