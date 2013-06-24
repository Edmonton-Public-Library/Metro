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

package mecard.security;

import java.util.Properties;
import mecard.Exception.MetroSecurityException;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import api.Request;

/**
 *
 * @author metro
 */
public final class SecurityManager 
{
    private SecurityManager()
    { }
    
    /**
     * Tests if the argument token is the API key from the client.
     * @param token pass phrase supplied by client.
     * @return true if the client sent this request and false if the token
     * didn't match the expected API-key value.
     */
    public static final boolean isAuthorized(String token)
    {
        // Compare if the client sent an appropriate API Key.
        Properties properties = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        String expectedKey = properties.getProperty(LibraryPropertyTypes.API_KEY.toString());
        return (token.compareTo(expectedKey) == 0);
    }
    
    public static final String encrypt(String input)
    {
        // TODO add encryption as time permits.
        return input.toString();
    }
    
    public static final String unEncrypt(String input) throws MetroSecurityException
    {
        // TODO add encryption as time permits.
        return input;
    }
}
