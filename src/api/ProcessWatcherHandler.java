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
package api;

import mecard.ResponseTypes;


public class ProcessWatcherHandler
{
    private ResponseTypes status;
    private StringBuffer stdout;
    private StringBuffer stderr;
    
    ProcessWatcherHandler()
    { 
        stdout = new StringBuffer();
        stderr = new StringBuffer();
        status = ResponseTypes.INIT;
    }
    
    void setStarted()
    {   
        this.status = ResponseTypes.BUSY;
    }

    void setStdout(String line)
    {
        this.stdout.append(line);
        this.stdout.append("|");
    }

    void setStderr(String line)
    {
        this.stderr.append(line);
        this.stderr.append("|");
    }

    void setEnded(int value)
    {
        this.stdout.append(value);
        this.stdout.append("|");
        this.stderr.append(value);
        this.stderr.append("|");
        status = ResponseTypes.OK;
    }

    void setError(Throwable th)
    {
        this.stderr.append(th.getMessage());
        this.status = ResponseTypes.ERROR;
    }

    public ResponseTypes getStatus() 
    {
        return status;
    }

    public String getStdout() 
    {
        return stdout.toString();
    }

    public String getStderr() 
    {
        return stderr.toString();
    }
}
