/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025  Edmonton Public Library
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
 * @author anisbet
 */
public enum CPLapiUserFields
{
    // "cardNumber": "21221012345678",
    USER_ID("cardNumber"),
    // "birthDate": "2000-02-29",
    USER_BIRTHDATE("birthDate"),
    //"firstName": "Balzac",
    USER_FIRST_NAME("firstName"),
    //"lastName": "BILLY",
    USER_LAST_NAME("lastName"),
    // password.
    USER_PASSWORD("pin"),
    //"expiryDate": null,
    PRIVILEGE_EXPIRES_DATE("expiryDate"),
    // "key": "emailAddress"
    EMAIL("emailAddress"),
    // "key": "postalcode"
    POSTALCODE("postalCode"),
    // "key": "phoneNumber"
    PHONE("phoneNumber"),
    // "key": "address"
    STREET("address"),
    // "key": "city"
    CITY("city"),
    GENDER("gender"),
    PROVINCE("province");
    
    private final String type;

    private CPLapiUserFields(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
