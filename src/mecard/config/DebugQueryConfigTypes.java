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
public enum DebugQueryConfigTypes
{
    COMMAND_RESULT_CODE("return-code"),// The return code of the command success is usually '0'.
    STDOUT_MESSAGE("stdout"),          // String you want displayed on stdout.
    STDERR_MESSAGE("stderr"),          // String you want displayed on stderr.
    MESSAGE_FORMAT("message-format");  // SIP2, Dumpflatuser what have you.
    
    private String type;
    
    private DebugQueryConfigTypes(String s)
    {
        this.type = s;
    }
    
    @Override
    public String toString()
    {
        return this.type;
    }
}
