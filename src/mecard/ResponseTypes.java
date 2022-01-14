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
package mecard;

/**
 * The types of responses MeCard server is capable of producing. The order is
 * intentional, lower ordinal values can be over written by higher ordinal values
 * in the response object but not the other way around. This stops error messages
 * from being reset to SUCCESS by other well meaning processes.
 * @see Response
 * @author Andrew Nisbet andrew.nisbet@epl.ca or andrew@dev-ils.com
 */
public enum ResponseTypes
{
    INIT,
    BUSY,
    COMMAND_COMPLETED,
    OK,
    SUCCESS, // be careful when changing the ordering of these values See Response.java.
    PIN_CHANGE_REQUIRED,
    LOST_CARD,
    UNAVAILABLE,
    FAIL,
    CONFIG_ERROR,
    UNAUTHORIZED,
    USER_NOT_FOUND,
    USER_PIN_INVALID,
    UNKNOWN, 
    ERROR; // Command was received but failed to execute either it was malformed, empty (null), or not supported.;
}
