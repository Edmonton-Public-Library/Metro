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
 * Base class for parsing XML responses from PAPI. While some web service calls
 * can use JSON, Patron Update Data, Patron Registration Create, and Patron 
 * Registration Update must use XML.
 * 
 * Extend this class for specific messages. This class will parse the XML 
 * passed in the constructor, and return any status codes (as integers) and
 * any error messages (which will be empty strings if there was none).
 * 
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class SDapiJsonResponse
{
    
    protected static Properties messageProperties;
    
    /**
     * Parses string of XML and provides accessors for status and any 
     * error message.
     * 
     * @param json String response message. 
     */
    public SDapiJsonResponse(String json)
    {
        messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        
            /*
            Successful response.
            --------------------
            
            
            Unsuccessful response.
            ----------------------
            
            
            */
        
    }
    
    public boolean succeeded()
    {
        // TODO: finish me
        return true;
    }
    
    /**
     * Provides the error message if there was one.
     * 
     * @return String of the error message or an empty string.
     */
    public String errorMessage()
    {
        return "";
    }
    
}
