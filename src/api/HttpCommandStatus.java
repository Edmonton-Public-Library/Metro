/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022 - 2024  Edmonton Public Library
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

import java.net.http.HttpResponse;
import mecard.ResponseTypes;

/**
 * The class translates HTTP response codes into ME Libraries status types.
 * @author Andrew Nisbet
 */
public final class HttpCommandStatus extends CommandStatus
{
    protected String httpStatusCodeText;
    protected int    httpStatusCode;
    protected HttpResponse<String> response;
    
    public HttpCommandStatus() { }
    
    public HttpCommandStatus(HttpResponse<String> response)
    {
        this.setStatus(response.statusCode());
        this.stdout.append(response.body());
    }
    
    /**
     * Converts the HTTP header response into ME Library status messages.
     * @param httpStatusCode 
     */
    public void setStatus(int httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
        switch(httpStatusCode)
        {
            case 200 -> {
                // Completed successfully.
                this.httpStatusCodeText = "";
                super.status = ResponseTypes.SUCCESS;
            }
            case 400 -> {
                // Bad request
                this.httpStatusCodeText = "Bad request";
                super.status = ResponseTypes.CONFIG_ERROR;
            }
            case 401 -> {
                // Unauthorized
                this.httpStatusCodeText = "Unauthorized";
                super.status = ResponseTypes.UNAUTHORIZED;
            }
            case 403 -> {
                // Forbidden
                this.httpStatusCodeText = "Forbidden";
                super.status = ResponseTypes.UNAUTHORIZED;
            }
            case 404 -> {
                // Resource not found.
                this.httpStatusCodeText = "Resource not found";
                super.status = ResponseTypes.CONFIG_ERROR;
            }
            case 405 -> {
                // Method not allowed.
                this.httpStatusCodeText = "Method not allowed";
                super.status = ResponseTypes.CONFIG_ERROR;
            }
            case 408 -> {
                // Request timeout
                this.httpStatusCodeText = "Request timed out";
                super.status = ResponseTypes.BUSY;
            }
            case 500 -> {
                // Internal server error.
                this.httpStatusCodeText = "Internal server error";
                super.status = ResponseTypes.UNAVAILABLE;
            }
            case 501 -> {
                // Not implemented.
                this.httpStatusCodeText = "Not implemented";
                super.status = ResponseTypes.UNAVAILABLE;
            }
            case 503 -> {
                // Service unavailable.
                this.httpStatusCodeText = "Service unavailable";
                super.status = ResponseTypes.UNAVAILABLE;
            }
            case 505 -> {
                // HTTP version not supported.
                this.httpStatusCodeText = "HTTP version not supported";
                super.status = ResponseTypes.CONFIG_ERROR;
            }
            default -> {
                this.httpStatusCodeText = "Unknown error";
                super.status = ResponseTypes.UNKNOWN;
            }
        }
    }
    
    public HttpResponse<String> getResponse()
    {
        return this.response;
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
        // For XML use, doesn't use the super's setStdout() because taht
        // interfers with xml text, causing parsing errors.
        this.stdout.append(xml);
    }
}
