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

import mecard.exception.SIPException;

/**
 * 
 * @author andrew
 */
public class SIPCommand implements Command
{
    private final SIPConnector sipConnector;
    private final String queryString;
    private final String institutionalID;
    
    public static class Builder
    {
        private String userId;
        private String pin;
        private final SIPConnector connector;
        private boolean isStatusRequest;
        
        public Builder(SIPConnector s)
        {
            this.connector = s;
            this.userId = "";
            this.pin = "";
            this.isStatusRequest = false;
        }
        
        public Builder setUser(String userId, String pin)
        {
            this.userId = userId;
            this.pin = pin;
            return this;
        }
        
        public Builder isStatusRequest()
        {
            this.isStatusRequest = true;
            return this;
        }
        
        public SIPCommand build()
        {
            return new SIPCommand(this);
        }
    }
    
    /**
     * If the user does not specify any userId or pin or that this is a status
     * request and MalformedCommandException will be thrown.
     */
    private SIPCommand(Builder b)
    {
        this.sipConnector = b.connector;
        // Will return an empty string if not set.
        this.institutionalID = this.sipConnector.getInstitutionalID();
        if (b.isStatusRequest)
        {
            // St. Albert's    "990   2.00AY1AZFCD8";
            this.queryString = "990   2.00AY1AZFCD8";
        }
        else
        {
            if (b.userId.isEmpty() || b.pin.isEmpty())
            {
                throw new SIPException("Supplied user id or pin (or both) were empty.");
            }
            this.queryString = patronInfoRequest(b.userId, b.pin);
        }
    }
    
    /**
     * Creates a SIP Patron Information request given a user id and pin.
     *
     * @param userId
     * @param userPin
     * @return String of SIP formatted request, ready to send.
     */
    protected final String patronInfoRequest(String userId, String userPin)
    {
        // sipData should look like: "63                               AO|AA21221012345678|AD64058|AY1AZF374\r"
        StringBuilder request = new StringBuilder();
        if (userId == null || userPin == null)
        {
            return request.append(SIPConnector.CONNECTION_TERMINATOR).toString();
        }
        request.append("63                               AO");
        request.append(institutionalID);
        request.append("|AA");
        request.append(userId);
        request.append("|AD");
        request.append(userPin);
        request.append("|AY1AZ");
        request.append(SIPConnector.getCheckSum(request.toString()));
        request.append(SIPConnector.CONNECTION_TERMINATOR);
        return request.toString();
    }

    @Override
    public CommandStatus execute()
    {
        CommandStatus status = new CommandStatus();
        try
        {
            status.setStdout(this.sipConnector.send(this.queryString));
        }
        catch(SIPException e)
        {
            // Can happen if the server is down, server not listening on port, login failed
            // or request timedout.
            status.setStderr("service is currently unavailable");
            System.out.println("service is currently unavailable" + e.getMessage());
        }
        
        return status;
    }
}
