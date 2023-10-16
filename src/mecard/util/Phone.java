/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013-2023  Edmonton Public Library
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
 * Provides phone conversion.
 * @author Andrew Nisbet <andrew.nisbet@epl.ca>
 */
public class Phone 
{
    public final static CharSequence DEFAULT_PHONE_DELIMITER = "-";
    public final static String DEFAULT_PHONE_NUMBER = "000-000-0000";
    public final static String DEFAULT_UNFORMATTED_PHONE_NUMBER = "0000000000";
    public final static int MAX_PHONE_NUMBER_SIZE = 10;
    private final String phone;
    
    public Phone(String p)
    {
        phone = Phone.formatPhone(p);
    }
    
    /**
     * Formats a phone number from 7804366077 to 780[delim]436[delim]6077.
     *
     * @param p
     * @return formatted phone number (area code)-(exchange)-(phone).
     */
    
    public final static String formatPhone(String p)
    {
        return formatPhone(p, false);
    }

    /**
     * Formats a phone number from 7804366077 to 780[delim]436[delim]6077.
     * @param p Phone number suspect.
     * @param debug
     * @return formatted phone number (area code)-(exchange)-(phone).
     */
    public static final String formatPhone(String p, boolean debug)
    {
        String formattedPhone = DEFAULT_PHONE_NUMBER;
        if (p == null) return formattedPhone;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p.length(); i++)
        {
            if (Character.isDigit(p.charAt(i)))
            {
                sb.append(p.charAt(i));
            }
            if (sb.length() == MAX_PHONE_NUMBER_SIZE)
            {
                // Do in this order or the length changes and offset is off by one.
                sb.insert(6, DEFAULT_PHONE_DELIMITER);
                sb.insert(3, DEFAULT_PHONE_DELIMITER);
                formattedPhone = sb.toString();
                if (debug) System.out.println("PHONE_NUM:" + formattedPhone);
                return formattedPhone;
            }
        }
        
        if (debug) System.out.println("Problem parsing phone number:" + p);
        return formattedPhone;
    }
    
    /**
     * Returns the object's stored phone value. If the stored value is null
     * or empty an empty string is returned.
     * @return stored phone string with DEFAULT_PHONE_DELIMITER stripped.
     */
    public String getUnformattedPhone()
    {
        if (phone == null)
        {
            return "";
        }
        return phone.replace("-", "");
    }
    
    /**
     * 
     * @return true if the stored phone number is the DEFAULT_PHONE_NUMBER or
     * the DEFAULT_UNFORMATTED_PHONE_NUMBER, and false otherwise.
     */
    public boolean isUnset()
    {
        return (this.phone.compareTo(DEFAULT_PHONE_NUMBER) == 0
            || this.phone.compareTo(DEFAULT_UNFORMATTED_PHONE_NUMBER) == 0);
    }
    
    @Override
    public String toString()
    {
        if (phone == null)
        {
            return "";
        }
        return phone;
    }
}
