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


public class CommandStatus
{
    protected ResponseTypes status;
    protected StringBuffer stdout;
    protected StringBuffer stderr;
    
    CommandStatus()
    { 
        this.stdout = new StringBuffer();
        this.stderr = new StringBuffer();
        this.status = ResponseTypes.INIT;
    }
    
    void setStarted()
    {   
        this.status = ResponseTypes.BUSY;
    }

    void setStdout(String line)
    {
        this.stdout.append(line);
        this.stdout.append("\n");
    }

    void setStderr(String line)
    {
        this.stderr.append(line);
        this.stderr.append("\n");
    }

    void setEnded(int value)
    {
        this.stdout.append(value);
        this.stdout.append("\n");
        this.stderr.append(value);
        this.stderr.append("\n");
        this.status = ResponseTypes.COMMAND_COMPLETED;
    }

    void setError(Throwable th)
    {
        this.stderr.append(th.getMessage());
        this.status = ResponseTypes.ERROR;
    }
    
    /** Allows specific {@link ResponseTypes} to be recorded. {@link #setEnded(int) }
     * performs a similar task but the response code is always set to {@link ResponseTypes#COMMAND_COMPLETED}
     * This method will actually set the the status to a value that can be used
     * later for computing the success of one command before continuing onto another.
     * 
     * @param t the response type of the command that executed.
     * @see #setEnded(int) 
     */
    void setResponseType(ResponseTypes value)
    {
        this.stdout.append(value.ordinal());
        this.stdout.append("\n");
        this.stderr.append(value.ordinal());
        this.stderr.append("\n");
        this.status = value;
    }

    public ResponseTypes getStatus() 
    {
        return this.status;
    }

    public String getStdout() 
    {
        return this.stdout.toString();
    }

    public String getStderr() 
    {
        return this.stderr.toString();
    }
}
