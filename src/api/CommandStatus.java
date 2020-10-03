/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2020  Edmonton Public Library
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
 * Represents the results of the execution of an external system command including
 * the exit status and any stderr / stdout results.
 * 
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
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
    
    /**
     * Sets the status of the command to BUSY, to indicate that the process
     * started.
     */
    void setStarted()
    {   
        this.status = ResponseTypes.BUSY;
    }

    /** 
     * Sets the standard output of the command, with a system-dependent
     * newline character appended to the last line of output.
     * 
     * @param line or lines of text from standard out.
     */
    void setStdout(String line)
    {
        this.stdout.append(line);
        this.stdout.append("\n");
    }

    /** 
     * Sets the standard error of the command, with a system-dependent
     * newline character appended to the last line of output.
     * 
     * @param line or lines of text from standard out.
     */
    void setStderr(String line)
    {
        this.stderr.append(line);
        this.stderr.append("\n");
    }

    /** 
     * Appends the exit status of the command to stdout and stderr, then adds a
     * newline character to each output stream when done.
     * @param value exit value of the process as an integer.
     */
    void setEnded(int value)
    {
        this.stdout.append(value);
        this.stdout.append("\n");
        this.stderr.append(value);
        this.stderr.append("\n");
        this.status = ResponseTypes.COMMAND_COMPLETED;
    }

    /**
     * Allows the caller to set an error for the command status in the form of
     * a Java throw-able object for logging and diagnosis issues.
     * @param e the error that was thrown during command execution.
     */
    void setError(Throwable e)
    {
        this.stderr.append(e.getMessage());
        this.status = ResponseTypes.ERROR;
    }
    
    /** 
     * Allows specific {@link ResponseTypes} to be recorded. {@link #setEnded(int) }
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
    
    /**
     * Sets the response type of the command without any formatting.
     * @param type the command's exit status translated into a form the 
     * MeCard server can understand.
     */
    void setResponse(ResponseTypes type)
    {
        this.status = type;
    }

    /**
     * Standard getter for the status.
     * @return the exit status of the command as a MeCard understandable type.
     */
    public ResponseTypes getStatus() 
    {
        return this.status;
    }

    /**
     * Standard getter of the standard out of the completed command.
     * @return string or strings from the command's standard out stream.
     */
    public String getStdout() 
    {
        return this.stdout.toString();
    }

    /**
     * Standard getter of the standard error of the completed command.
     * @return string or strings from the command's standard error stream.
     */
    public String getStderr() 
    {
        return this.stderr.toString();
    }
}
