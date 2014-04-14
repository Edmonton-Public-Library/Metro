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

import mecard.ResponseTypes;

/**
 * Used to interpret HTTP codes into meaningful Metro response codes.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class HTTPCommandStatus extends CommandStatus
{
    private int httpCode;
    
    public HTTPCommandStatus()
    {
        super();
        this.httpCode = 200;
    }

    public int getHttpCode()
    {
        return httpCode;
    }

    public void setHttpCode(int httpCode)
    {
        this.httpCode = httpCode;
        if (this.httpCode == 200)
        {
            this.status = ResponseTypes.SUCCESS;
        }
        else if (this.httpCode >= 500)
        {
            this.status = ResponseTypes.UNAVAILABLE;
        }
        else
        {
            this.status = ResponseTypes.CONFIG_ERROR;
        }
        System.out.println("HTTP_CODE:" + this.httpCode);
    }
    
    /**
     * 
     * @return the content of the page returned, which could be XML, and 
     * parsed for determining status.
     */
    public String getContent()
    {
        return this.getStdout(); 
    }
}
