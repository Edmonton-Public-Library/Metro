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
package api;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

/**
 * This represents a raw http message response. This class can return the http
 * code and content parsed into a useful format.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class HTTPMessage
{
    protected String message;
    
    public HTTPMessage(String content, boolean debug)
    {

    }
    
    @Override
    public String toString()
    {
        return "SOME DATA CORRECTLY FORMED BASED ON ENTITY'S CONTENT TYPE";
    }
}
