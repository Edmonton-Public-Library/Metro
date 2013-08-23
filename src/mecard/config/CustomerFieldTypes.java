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
package mecard.config;

/**
 *
 * @author metro
 */
public enum CustomerFieldTypes
{
    // Note to programmer: The order of these is important for the Customer
    // class that uses the order in its @Customer#toString method. That in turn
    // is used for creating the customer's MD5 finger print. We use that for
    // determining if the the customer account has changed.
    ID, 
    PIN,
    PREFEREDNAME,  // last name first name comma-separated.
    STREET, 
    CITY, 
    PROVINCE, 
    POSTALCODE,
    SEX, 
    EMAIL, 
    PHONE,
    DOB, 
    PRIVILEGE_EXPIRES,
    RESERVED, // General field can be used for what ever, used by SIP for messages.
    ALTERNATE_ID, // Used for fields that can remain empty like 'province' on Horizon.
    ISVALID, // start of flagged fields. This one means the host library has all customer information.
    ISMINAGE,
    ISRECIPROCAL,
    ISRESIDENT,
    ISGOODSTANDING,
    ISLOSTCARD,
    FIRSTNAME,
    LASTNAME;
    
    public static int size()
    {
        int count = 0;
        for (CustomerFieldTypes c: CustomerFieldTypes.values())
        {
            count++;
        }
        return count;
    }
}
