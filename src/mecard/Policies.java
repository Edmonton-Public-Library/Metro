/*
* Metro allows customers from any affiliate library to join any other member library.
*    Copyright (C) 2022  Edmonton Public Library
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

/**
 * Constants that are imposed on all libraries participating in the ME Libraries
 * network.
 * 
 * These are some of the constants that all nodes agree to before a customer can
 * use the ME Libraries service. For example juveniles can't use the service and
 * the minimum age is 18.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public final class Policies // don't extend.
{
    private final static int MIN_AGE                = 18;
    private final static int MAX_EXPIRY_DAYS        = 365;
    private final static int MAX_CHECK_ADDRESS_DAYS = 365;
    
    private Policies(){} // Don't invoke directly
    
    /**
     * Gets the minimum age for any customer to be able to use ME Libraries.
     * 
     * @return integer of the minimum age for me customers.
     */
    public final static int minimumAge()
    {
        return Policies.MIN_AGE;
    }
    
    /**
     * Gets the maximum number of days before account expires.
     * @return integer of maximum days an account can be active before it
     * expires.
     */
    public final static int maximumExpiryDays()
    {
        return Policies.MAX_EXPIRY_DAYS;
    }
    
    /**
     * Gets the maximum number of days before account expires.
     * @return integer of maximum days an account can be active before it
     * expires.
     */
    public final static int maximumAddressCheckDays()
    {
        return Policies.MAX_CHECK_ADDRESS_DAYS;
    }
}
