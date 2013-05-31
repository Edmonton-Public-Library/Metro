/**
*
* This class is part of the Metro, MeCard project.
*    Copyright (C) 2013  Andrew Nisbet, Edmonton public Library.
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
     *
     * @param customer the value of customer
     * @return the boolean
     */
    public boolean convert(List<String> customer);
}