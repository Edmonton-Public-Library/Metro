/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either httpVersion 2 of the License, or
 * (at your option) any later httpVersion.
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
package mecard.sirsidynix.sdapi;

import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;


/**
 * Base class for parsing JSON responses from Symphony Web Service API.
 * 
 * Extend this class for specific messages. This class will parse the JSON 
 * and allow querying of data in subclasses.
 * 
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public abstract class SDapiResponse
{
    
    protected static Properties messageProperties;
    
    public SDapiResponse()
    {
        messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
    }
    
    /**
     * Tests if it is a successful response.
     * @return true if the response indicates success, and false otherwise.
     */
    public abstract boolean succeeded();
    
    /**
     * Provides the error message if there was one.
     * 
     * @return String of the error message or an empty string.
     */
    public abstract String errorMessage();
    
}
