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

import mecard.exception.DummyException;

/**
 * Dummy command does not actually run anything, but games responses as if a 
 * command did run. You can set the responses to anything you want for testing.
 * @author Andrew Nisbet.
 */
public class DummyCommand implements Command
{
    private String stdoutResponse;
    private String stderrResponse;
    private int status;
    
    public static class Builder
    {
        private int status;
        private String stderr;
        private String stdout;
        
        public Builder()
        {
            this.status = 0;
            this.stderr  = "";
            this.stdout = "";
        }
        
        public Builder setStatus(int s)
        {
            this.status = s;
            return this;
        }
        
        public Builder setStdout(String s)
        {
            this.stdout = s;
            return this;
        }
        
        public Builder setStderr(String s)
        {
            this.stderr = s;
            return this;
        }
        
        public DummyCommand build()
        {
            return new DummyCommand(this);
        }
    }
    
    /**
     * If the user does not specify any userId or pin or that this is a status
     * request and MalformedCommandException will be thrown.
     */
    private DummyCommand(Builder b)
    {
        this.status = b.status;
        this.stderrResponse = b.stderr;
        this.stdoutResponse = b.stdout;
    }

    @Override
    public CommandStatus execute()
    {
        CommandStatus commandStatus = new CommandStatus();
        // Here we are going to load the data from the debug.properties file
        // We don't need to actually execute anything so we load the status
        // with bogus information from the properties file.
        commandStatus.setStarted();
        commandStatus.setStdout(this.stdoutResponse);
        if (stderrResponse.isEmpty() == false)
        {
            commandStatus.setError(new DummyException(this.stderrResponse));
        }
        commandStatus.setEnded(this.status);
        
        return commandStatus;
    }
}
