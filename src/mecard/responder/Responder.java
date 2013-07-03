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
package mecard.responder;

import mecard.ResponseTypes;
import api.Request;
import api.Response;
import mecard.Exception.MalformedCommandException;
import mecard.Exception.MetroSecurityException;
import mecard.Exception.UnsupportedCommandException;
import mecard.Exception.UnsupportedResponderException;

/**
 *
 * @author metro
 */
public abstract class Responder
{
    protected Request request;
    protected final boolean debug;
    
    /**
     *
     * @param cmd the value of cmd
     * @param debugMode the value of debugMode
     */
    protected Responder(Request cmd, boolean debugMode)
    {
        this.debug = debugMode;
        this.request = cmd;
        if (debug)
        {
            System.out.println("CMD:\n  '"+request.toString()+"' '"+request.getCommandType().name()+"'");
            System.out.println("ELE:");
            System.out.println("  S:" + request.getArgs() + ",");
        }
    }
    
    /**
     *
     *
     * @param ex the exception thrown
     * @return String value of the response with code.
     */
    
    public static Response getExceptionResponse(RuntimeException ex)
    {
        Response response = new Response(ResponseTypes.UNKNOWN);
        if (ex instanceof MetroSecurityException)
        {
            response = new Response(ResponseTypes.UNAUTHORIZED);
        }
        else if (ex instanceof MalformedCommandException)
        {
            response = new Response(ResponseTypes.ERROR);
        }
        else if (ex instanceof UnsupportedCommandException)
        {
            response = new Response(ResponseTypes.UNKNOWN);
            response.setResponse("Command not implemented, make sure your server is up to date.");
        }
        else if (ex instanceof UnsupportedResponderException)
        {
            response = new Response(ResponseTypes.CONFIG_ERROR);
            response.setResponse("The server doesn't seem to be configured correctly.");
        }
        response.setResponse(ex.getMessage());
        return response;
    }
    
    /**
     *
     * @return the api.Response
     */
    public abstract Response getResponse();
}
