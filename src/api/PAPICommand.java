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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.exception.ConfigurationException;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PAPICommand implements Command
{
    private final URL url;
    private String httpVerb;
    private String contentType;
    
    public static class Builder
    {
        // optional
        private String url;
        private String loginBranchId;
        private String loginUserId;
        private String loginStationId;
        private String verb;
        private String contentType;

        /**
         * Constructor that insists that the command gets at least a status
         * object and a command.
         */
        public Builder()
        { 
            this.contentType = "json"; // default content type, also try XML.
        }

        /**
         * Builds the command and returns a reference to it.
         *
         * @return APICommand reference.
         */
        public PAPICommand build()
        {
            return new PAPICommand(this);
        }

        /**
         * Sets the URL of the PAPI command.
         * @param url
         * @return builder object.
         */
        public Builder setURL(String url)
        {
            this.url = url;
            return this;
        }

        public Builder setLoginBranchId(String loginBranchId)
        {
            this.loginBranchId = loginBranchId;
            return this;
        }
        
        public Builder setContentType(String type)
        {
            this.contentType = type;
            return this;
        }

        public Builder setLoginUserId(String loginUserId)
        {
            this.loginUserId = loginUserId;
            return this;
        }

        public Builder setLoginStationId(String loginStationId)
        {
            this.loginStationId = loginStationId;
            return this;
        }
        
        public Builder setHTTPVerb(String verb)
        {
            this.verb = verb;
            return this;
        }
    }
    
    private PAPICommand(Builder papiBuilder)
    {
        try
        {
            this.url = new URL(papiBuilder.url);
        }
        catch (MalformedURLException ex)
        {
            throw new ConfigurationException(PAPICommand.class.getName() + 
                    "The PAPI URL is malformed. Please check "
                    + "your configuration for errors.");
        }
        this.httpVerb    = papiBuilder.verb;
        this.contentType = papiBuilder.contentType;
    }

    @Override
    public CommandStatus execute()
    {
        CommandStatus status = new CommandStatus();
        try
        {
            HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
            connection.setRequestMethod(this.httpVerb);
            connection.setRequestProperty("Accept", "application/"+this.contentType);
            
            if (connection.getResponseCode() != 200) 
            {
                // Depending on the error we should return a more meaningful exception.
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + connection.getResponseCode());
                status.setStderr(String.valueOf(connection.getResponseCode()));
            }
            else // Non-200 code returned.
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (connection.getInputStream())));

                String output;
                while ((output = br.readLine()) != null) 
                {
                    status.setStdout(output);
                }
                connection.disconnect();
            }
        }
        catch (IOException ex)
        {
            status.setStderr(String.valueOf(ex.getMessage()));
        }
        return status;
    }

}
