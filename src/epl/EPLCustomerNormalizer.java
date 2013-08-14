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
package epl;

import mecard.customer.Customer;
import mecard.site.CustomerLoadNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class EPLCustomerNormalizer extends CustomerLoadNormalizer
{
    private final boolean debug;
    
    public EPLCustomerNormalizer(boolean debug)
    {
        this.debug = debug;
    }
    
    @Override
    public String normalize(Customer c)
    {
        return ""; // no special rules for EPL.
    }
}
