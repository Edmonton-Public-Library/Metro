/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022 - 2024  Edmonton Public Library
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
 * The properties that are expected to be in the environment.xml file.
 * The enum values are mandatory, others can be added, they will not be enforced.
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public enum LibraryPropertyTypes
{
    STATUS_SERVICE("status-protocol"),
    GET_SERVICE("get-protocol"),
    EXISTS_SERVICE("exists-protocol"),
    UPDATE_SERVICE("update-protocol"),
    CREATE_SERVICE("create-protocol"),
    API_KEY("api-key"),
    LIBRARY_CODE("library-code"),
    DEBUG("debug"), 
    DATE_FORMAT("date-format"),
    METRO_PORT("metro-port"),
    NON_RESIDENT_TYPES("non-resident-types"), // May be empty ',' comma separated
    RECIPROCAL_TYPES("reciprocal-types"), // May be empty
    JUVENILE_TYPES("juvenile-types"), // May be empty: <entry key="juvenile">re,sp, stu</entry>
    CUSTOMER_STANDING_SENTINEL("fail-goodstanding-sentinel"), // Like barred or BLOCKED
    LOST_CARD_SENTINEL("lost-card-sentinel"); // Profile or other indicator that use to
    // determine if the customer's card is a lost card.
    
    private final String type;
    private LibraryPropertyTypes(String s)
    {
        this.type = s;
    }
    
    @Override
    public String toString()
    {
        return this.type;
    }
}
