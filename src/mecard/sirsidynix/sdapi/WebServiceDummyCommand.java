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

import api.Command;
import api.HttpCommandStatus;
import mecard.exception.DummyException;

/**
 *
 * @author anisbet
 */
public class WebServiceDummyCommand implements Command
{
    
    private final String stdoutResponse;
    private final String stderrResponse;
    private final int status;
    
    public static class Builder
    {
        private int status;
        private String stderr;
        private String stdout;
        
        public Builder()
        {
            this.status = 0;
            this.stderr = "";
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
        
        public WebServiceDummyCommand build()
        {
            return new WebServiceDummyCommand(this);
        }
    }
    
    /**
     * If the user does not specify any userId or pin or that this is a status
     * request and MalformedCommandException will be thrown.
     */
    private WebServiceDummyCommand(Builder b)
    {
        this.status = b.status;
        this.stderrResponse = b.stderr;
        this.stdoutResponse = b.stdout;
    }

    @Override
    public HttpCommandStatus execute()
    {
        HttpCommandStatus commandStatus = new HttpCommandStatus();
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
