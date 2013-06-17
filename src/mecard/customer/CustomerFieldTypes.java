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
package mecard.customer;

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
    ID(0), 
    PIN(1),
    NAME(2),  // last name first name comma-separated.
    STREET(3), 
    CITY(4), 
    PROVINCE(5), 
    POSTALCODE(6),
    GENDER(7), 
    EMAIL(8), 
    PHONE(9),
    DOB(10), 
    PRIVILEGE_EXPIRES(11),
    RESERVED(12), // General field can be used for what ever, currently empty.
    DEFAULT(13), // Used for fields that can remain empty like 'province' on Horizon.
    ISVALID(14), // start of flagged fields. This one means the host library has all customer information.
    ISMINAGE(15),
    ISRECIPROCAL(16),
    ISRESIDENT(17),
    ISGOODSTANDING(18),
    ISLOSTCARD(19),
    FIRSTNAME(20),
    LASTNAME(21);
    
    private int type;
    
    public static int size()
    {
        int count = 0;
        for (CustomerFieldTypes c: CustomerFieldTypes.values())
        {
            count++;
        }
        return count;
    }
    
    private CustomerFieldTypes(int type)
    {
        this.type = type;
    }
    
    private CustomerFieldTypes(String type)
    {
        for (CustomerFieldTypes cType : CustomerFieldTypes.values())
        {
            if (type.equalsIgnoreCase(cType.name()))
            {
                this.type = cType.ordinal();
                break;
            }
        }
    }
    
    @Override
    public String toString()
    {
        return this.name();
    }
}
