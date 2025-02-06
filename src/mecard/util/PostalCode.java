/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013 - 2025 Edmonton Public Library
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

package mecard.util;

import mecard.Protocol;

/**
 * PostalCode is meant as a postal code validator.
 *
 * @author andrew
 */
public class PostalCode
{
    private String postalCode;

    public PostalCode(String code)
    {
        if (isValid(code)) {
            this.postalCode = formatPostalCode(code);
        } else {
            this.postalCode = Protocol.DEFAULT_FIELD_VALUE;
        }
    }

    public static boolean isValid(String code) 
    {
        if (code == null) return false;

        // Remove spaces and convert to uppercase
        code = code.replaceAll("-", "");
        code = code.replaceAll("\\s", "").toUpperCase();

        // Check pattern: A1A1A1 where A is letter, 1 is digit
        return code.matches("[A-Z]\\d[A-Z]\\d[A-Z]\\d");
    }

    public static String formatPostalCode(String code) 
    {
        code = code.replaceAll("-", "");
        // Strip spaces, convert to uppercase
        code = code.replaceAll("\\s", "").toUpperCase();

        // Insert space after 3rd character
        code = code.substring(0, 3) + " " + code.substring(3);
        try
        {
             return code.substring(0, 7);
        }
        catch (StringIndexOutOfBoundsException e)
        {
            return Protocol.DEFAULT_FIELD_VALUE;
        }
    }

    @Override
    public String toString() 
    {
        return postalCode;
    }
}
