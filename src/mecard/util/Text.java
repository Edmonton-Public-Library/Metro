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
package mecard.util;

/**
 * General text handling static methods are found here.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class Text 
{
    private Text(){}
    /**
     * Converts strings to proper case. Used for normalizing names and streets
     * to a proper case convention. One of the libraries uses UPPERCASE for
     * customer first name last name and address fields, which is not helpful
     * for other libraries. Each should expect their data in a constant format.
     * @param s string to be Proper Cased.
     * @return the string in proper case.
     */
    public static String toDisplayCase(String s)
    {
        // Credit to: http://stackoverflow.com/questions/1086123/titlecase-conversion
        final String ACTIONABLE_DELIMITERS = " -"; // these cause the character following
                                                   // to be capitalized
        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) 
        {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
        }
        return sb.toString();
    }
}
