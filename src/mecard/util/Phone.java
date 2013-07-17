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
     * @param p
     * @return formatted phone number (area code)-(exchange)-(phone).
     */
    public static final String formatPhone(String p, boolean debug)
    {
        if (p == null)
        {
            return "";
        }
        String phNum = p.trim();
        if (phNum.isEmpty())
        {
            return "";
        }
        
        if (p.contains(DEFAULT_PHONE_DELIMITER))
        {
            return phNum;
        }
        int len = phNum.length();
        if (phNum.length() < 4)
        {
            return p;
        }
        // now get the phone number
        String phoneNumber = phNum.substring(len -4);
        if (debug) System.out.println("PHONE_NUM:"+phoneNumber);
        if (phNum.length() < 7) return phoneNumber;
        String exchange = phNum.substring((len -7), (len -4));
        if (debug) System.out.println("EXCHG:"+exchange);
        if (phNum.length() < 10) 
        {
            return exchange + DEFAULT_PHONE_DELIMITER +
               phoneNumber;
        }
        String areaCode = phNum.substring(0, (len -7));
        if (debug) System.out.println("AREA:"+areaCode);
        return areaCode + DEFAULT_PHONE_DELIMITER +
               exchange + DEFAULT_PHONE_DELIMITER +
               phoneNumber;
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
