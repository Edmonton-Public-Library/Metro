/*
 * Copyright (C) 2013 metro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package mecard.customer;

import java.util.List;

/**
 * Converts a request into a format usable by the ILS. This class does not 
 * provide the ability to convert from ILS because BImport is an import
 * function provider only, not a responder.
 * 
 * The formatter is highly reliant on the customer arriving and departing
 * in a fixed and defined order, see CustomerFieldTypes for more information
 * 
 * @author anisbet
 * @since 1.1
 */
public class MeCard2BImportFormatter implements CustomerFormatter
{
    
    /**
     *
     * @param customer the value of customer
     * @return the boolean
     */
    @Override
    public boolean convert(List<String> customer)
    {
        return true; 
    }
}
