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
 * PostalCode is meant as a postal code validator.
 *
 * @author andrew
 */
public class PostalCode
{
    public static int LENGTH = 6;
    private String content;
    private final boolean isValid;
    
    /**
     * Tests and sets the customer's postal code.
     *
     * @param postalCode the customer's postal code.
     */
    public PostalCode(String postalCode)
    {
        this.isValid = this.setContent(postalCode);
    }
    
    public static String formatPostalCode(String postalCode)
    {
        return postalCode.substring(0, 3) + " " + postalCode.substring(3);
    }

    /**
     * Cleans extra characters from the pCode number string and tests the
     * remainder.
     *
     * @param enteredPostalCode
     * @return true if the result it a valid number and false otherwise.
     */
    protected boolean setContent(String enteredPostalCode)
    {
        if (enteredPostalCode == null || enteredPostalCode.length() == 0)
        {
            return false;
        }
        StringBuilder pCode = new StringBuilder();
        for (int i = 0; i < enteredPostalCode.length(); i++)
        {
            Character c = enteredPostalCode.charAt(i);

            if (Character.isDigit(c) || Character.isLetter(c))
            {
                pCode.append(c);
            }
        }

        // Now if everything is ok with the pCode then save it and return true.
        if (pCode.length() == PostalCode.LENGTH)
        {
            for (int i = 0; i < pCode.length(); i++)
            {
                Character c = pCode.charAt(i);
                if (i == 0 || i == 2 || i == 4)
                {
                    if (Character.isLetter(c) == false)
                    {
                        return false;
                    }
                }
                if (i == 1 || i == 3 || i == 5)
                {
                    if (Character.isDigit(c) == false)
                    {
                        return false;
                    }
                }
            }
            this.content = pCode.toString().toUpperCase();
            return true;
        }
        return false;
    }
    
    @Override
    public String toString()
    {
        return this.content;
    }
    
    public boolean isValid()
    {
        return this.isValid;
    }
}
