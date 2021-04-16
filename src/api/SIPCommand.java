/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2021  Edmonton Public Library
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

import java.util.Date;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import java.util.Properties;
import mecard.config.ILS;
import mecard.exception.SIPException;
import mecard.util.Text;
import site.HorizonNormalizer;

/**
 * Implementation of SIP2 command.
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public class SIPCommand implements Command
{
    private final SIPConnector sipConnector;
    private final String queryString;
    private final String institutionalID;
    private final String userNotFoundMessageString;
    private final ILS ils;
    
    public static class Builder
    {
        private String userId;
        private String pin;
        private final SIPConnector connector;
        private boolean isStatusRequest;
        private String userNotFound;
        private final ILS ilsType;
        
        /**
         * Constructor requires SIP connection to already be available.
         * @param s 
         */
        public Builder(SIPConnector s)
        {
            this.connector = s;
            this.userId = "";
            this.pin = "";
            this.isStatusRequest = false;
            Properties sip2Props = PropertyReader.getProperties(ConfigFileTypes.SIP2);
            // The default _should_ work, but add a 'user-not-found' entry to 
            // the sip2.properties file if your sip server returns a different
            // message in the AF field when a customer lookup fails.
            this.userNotFound = sip2Props.getProperty(
                    "user-not-found", "User not found");
            this.ilsType = new ILS();
        }
        
        /**
         * Sets the customer's ID (barcode) and PIN.
         * @param userId
         * @param pin
         * @return Builder
         */
        public Builder setUser(String userId, String pin)
        {
            this.userId = userId;
            this.pin = pin;
            return this;
        }
        
        /**
         * Sets a flag if the request is for ILS availability.
         * @return Builder
         */
        public Builder isStatusRequest()
        {
            this.isStatusRequest = true;
            return this;
        }
        
        /**
         * Factory method to build the SIP request command.
         * @return 
         */
        public SIPCommand build()
        {
            return new SIPCommand(this);
        }
    }
    
    /**
     * If the user does not specify any userId or pin or that this is a status
     * request and MalformedCommandException will be thrown.
     * @param Builder object.
     */
    private SIPCommand(Builder b)
    {
        this.sipConnector = b.connector;
        // Will return an empty string if not set.
        this.institutionalID = this.sipConnector.getInstitutionalID();
        this.userNotFoundMessageString = b.userNotFound;
        this.ils = b.ilsType;
        if (b.isStatusRequest)
        {
            // St. Albert's    "990   2.00AY1AZFCD8";
            this.queryString = "990   2.00AY1AZFCD8";
        }
        else
        {
            if (b.userId.isEmpty())
            {
                throw new SIPException("Supplied user id was empty.");
            }
            if ( b.pin.isEmpty())
            {
                throw new SIPException("Supplied user pin was empty.");
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
        /*
        * Horizon libraries now store hashed versions of the customers password
        * not just random 4-digit pins. This allows authentication with a 
        * customer's full password from their home library. 
        *
        * This is now required because MeCard does not just take ME libraries 
        * word for a request to be a create or update, but rather checks the
        * ILS to see which is required. So a request to update will check if 
        * there is an account before updating, but we need to check if the 
        * account exists and that would require authentication on most SIP2
        * server configurations.
        */
        if (this.ils.getILSType() == ILS.IlsType.BIMPORT)
        {
            if (!Text.isUpToMaxDigits(userPin, HorizonNormalizer.MAXIMUM_PIN_WIDTH))
            {
                // Get the hash of the current password instead of random digits.
                String newPin = Text.getNew4DigitPin(userPin);
                userPin = newPin;
                System.out.println(new Date() 
                    + " trying with hashed PIN because Customer's PIN was "
                    + "not 4 digits as required by Horizon. Checking with: '" 
                    + newPin + "'.");
            }
        }
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
        status.setResponse(ResponseTypes.BUSY);
        try
        {
            status.setStdout(this.sipConnector.send(this.queryString));
            status.setResponse(ResponseTypes.OK);
            // Check if we found the user or not. Even if this is a status
            // request this won't pass. It may give false negative if any
            // customer information contains 'User not found', but I'd want the
            // request to fail just so we could meet a person that lives on 
            // that street.
            if (status.getStdout().contains(this.userNotFoundMessageString))
            {
                status.setResponse(ResponseTypes.USER_NOT_FOUND);
            }
        }
        catch(SIPException e)
        {
            // Can happen if the server is down, server not listening on port, login failed
            // or request timedout.
            status.setResponseType(ResponseTypes.UNAVAILABLE);
            status.setStderr("service is currently unavailable");
            System.out.println("service is currently unavailable" + e.getMessage());
        }
        
        return status;
    }
}
