/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
 * The class translates HTTP response codes into ME Libraries status types.
 * @author Andrew Nisbet
 */
public class HttpCommandStatus extends CommandStatus
{
    protected String httpStatusCodeText;
    protected int    httpStatusCode;
    /**
     * Converts the HTTP header response into ME Library status messages.
     * @param httpStatusCode 
     */
    public void setStatus(int httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
        switch(httpStatusCode)
        {
            case 200: // Completed successfully.
                this.httpStatusCodeText = "";
                super.status = ResponseTypes.SUCCESS;
                break;
            case 400: // Bad request
                this.httpStatusCodeText = "Bad request";
                super.status = ResponseTypes.CONFIG_ERROR;
                break;
            case 401: // Unauthorized
                this.httpStatusCodeText = "Unauthorized";
                super.status = ResponseTypes.UNAUTHORIZED;
                break;
            case 403: // Forbidden
                this.httpStatusCodeText = "Forbidden";
                super.status = ResponseTypes.UNAUTHORIZED;
                break;
            case 404: // Resource not found.
                this.httpStatusCodeText = "Resource not found";
                super.status = ResponseTypes.CONFIG_ERROR;
                break;
            case 405: // Method not allowed.
                this.httpStatusCodeText = "Method not allowed";
                super.status = ResponseTypes.CONFIG_ERROR;
                break;
            case 408: // Request timeout
                this.httpStatusCodeText = "Request timed out";
                super.status = ResponseTypes.BUSY;
                break;
            case 500: // Internal server error.
                this.httpStatusCodeText = "Internal server error";
                super.status = ResponseTypes.UNAVAILABLE;
                break;
            case 501: // Not implemented.
                this.httpStatusCodeText = "Not implemented";
                super.status = ResponseTypes.UNAVAILABLE;
                break;
            case 503: // Service unavailable.
                this.httpStatusCodeText = "Service unavailable";
                super.status = ResponseTypes.UNAVAILABLE;
                break;
            case 505: // HTTP version not supported.
                this.httpStatusCodeText = "HTTP version not supported";
                super.status = ResponseTypes.CONFIG_ERROR;
                break;
            default:
                this.httpStatusCodeText = "Unknown error";
                super.status = ResponseTypes.UNKNOWN;
                break;
        }
    }
    
    public boolean okay()
    {
        return httpStatusCode == 200;
    }
    
    public int getHttpStatusCode()
    {
        return httpStatusCode;
    }
    
    @Override
    public String toString()
    {
        return this.httpStatusCodeText;
    }
    
    @Override
    public void setStdout(String xml)
    {
        // Ensures that only xml is in the status objects stdout, that way
        // we can parse it as XML without errors.
        this.stdout.append(xml);
    }
}
