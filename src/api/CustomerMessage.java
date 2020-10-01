/*
* Metro allows customers from any affiliate library to join any other member library.
*    Copyright (C) 2020  Edmonton Public Library
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
package api;

import mecard.Protocol;
import mecard.util.DateComparer;

/**
 * This interface is used to generalize a message (usually a SIP2 customer
 * message). It is allows the {@link site.MeCardPolicy} object to ask for the profile
 * information. Each Policy object performs similar tasks: checks the customer
 * against the policies of too young (juvenile), non-resident, and reciprocal.
 * This CustomerMessage object always knows how to get the profile of the customer response
 * (again, usually SIP2)
 * 
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public interface CustomerMessage
{
    /**
     * Gets the customer profile from the customer message, however that is 
     * defined for the protocol.
     * @return customer's profile.
     */
    public String getCustomerProfile();
    
    /**
     * Selects a specific value from a field of a well-formed returned message.
     * If you think of a SIP2 message as having fields separated by '|' pipe 
     * characters, and each field contains a code and value. The implementer 
     * shall, given a named field, return the value in that field if possible.
     * @param fieldName the named field in the returned message.
     * @return Sting contents for a named field or an empty string if it couldn't
     * be found, is undefined, or the existing field is empty.
     */
    public String getField(String fieldName);
    
    /**
     * The contract for this method states that the supplied field name shall be
     * a field name that may contain a date. If the retrieved field can be cleaned
     * with {@link DateComparer#cleanDateTime(java.lang.String) } and then test
     * true for {@link DateComparer#isDate(java.lang.String) } then the cleaned
     * string shall be returned. The date field is tested as if it was an ANSI 
     * 'yyyymmdd' format, that value will be returned. If the value cannot be 
     * coerced into an ANSI date then the value {@link Protocol#DEFAULT_FIELD_VALUE} 
     * will be returned.
     * @param fieldName
     * @return an ANSI date from the field or {@link Protocol#DEFAULT_FIELD_VALUE}
     * if the stored value could not be coerced into an ANSI date.
     */
    public String getDateField(String fieldName);
    
    /**
     * Tests if a field has a value stored.
     * @param fieldName
     * @return true if the message contains data in the argument field name, and
     * false otherwise.
     */
    public boolean isEmpty(String fieldName);
    
    /**
     * Returns the standing of a customer as defined by the type of ILS's returned
     * Customer Message type.
     * @return standing message from the customer message.
     */
    public String getStanding();
    
    /**
     * Tests if the customer's card has been reported as lost by the ILS's response
     * message for customer information.
     * @return true if the card is reported lost, and false otherwise or by default.
     */
    public boolean cardReportedLost();
    
    /**
     * Tests if a user account is in good standing based on the return message from 
     * the ILS.
     * @return false if the customer account could definitively shown to be not
     * in good standing, and true otherwise.
     */
    public boolean isInGoodStanding();
    
    // TODO: Add this to subclasses.
//    public boolean customerExists();
}
