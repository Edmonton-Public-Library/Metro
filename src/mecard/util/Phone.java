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
 * Provides phone conversion.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class Phone 
{
    public final static CharSequence DEFAULT_PHONE_DELIMITER = "-";
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
        String formattedPhone = "000-000-0000";
        if (p == null) return formattedPhone;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p.length(); i++)
        {
            if (Character.isDigit(p.charAt(i)))
            {
                sb.append(p.charAt(i));
            }
        }
        // Should now contain just numbers and if correct should be 10 digits for 
        // North AM phones, otherwise it is not a good phone number.
        if (sb.length() != 10)
        {
            return formattedPhone;
        }
        else
        {
            // Do in this order or the length changes and offset is off by one.
            sb.insert(6, DEFAULT_PHONE_DELIMITER);
            sb.insert(3, DEFAULT_PHONE_DELIMITER);
        }
        formattedPhone = sb.toString();
        if (debug) System.out.println("PHONE_NUM:" + formattedPhone);
        return formattedPhone;
    }
    
    public String getUnformattedPhone()
    {
        if (phone == null)
        {
            return "";
        }
        return phone.replace("-", "");
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
