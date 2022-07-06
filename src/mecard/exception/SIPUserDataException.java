/*
 * MeCard server customer data exception.
 *    Copyright (C) 2022  Andrew Nisbet for Edmonton Public Library.
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

package mecard.exception;

/**
 * Thrown if customer is not found or the user's pin is invalid.
 * @author Andrew Nisbet
 * @since version 1.3.00
 */


public class SIPUserDataException extends RuntimeException
{
    private final static String initMessage = "User Data Error ";
    
    public SIPUserDataException()
    {
        super(initMessage);
    }
    
    public SIPUserDataException(String msg)
    {
        super(initMessage + msg + " ");
    }
}
