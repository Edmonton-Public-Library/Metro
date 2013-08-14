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
package mecard.requestbuilder;

import api.Command;
import api.CommandStatus;
import mecard.Response;
import api.SIPCommand;
import api.SIPConnector;
import api.SIPStatusMessage;
import java.util.Properties;
import mecard.MetroService;
import mecard.Protocol;
import mecard.QueryTypes;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.SipPropertyTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.SIPFormatter;
import mecard.exception.SIPException;

/**
 * Helper class for formatting SIP requests.
 *
 * @author metro
 */
public class SIPRequestBuilder extends ILSRequestBuilder
{
    private static SIPConnector sipServer;
    /**
     *
     * @param debug the value of debug
     */
    SIPRequestBuilder(boolean debug)
    {
        Properties sipProps = MetroService.getProperties(ConfigFileTypes.SIP2);
        String host = sipProps.getProperty(SipPropertyTypes.HOST.toString());
        String port = sipProps.getProperty(SipPropertyTypes.PORT.toString(), "6001"); // port optional in config.
        String user = sipProps.getProperty(SipPropertyTypes.USER.toString(), "");
        String password = sipProps.getProperty(SipPropertyTypes.PASSWORD.toString(), "");
        String timeout = sipProps.getProperty(SipPropertyTypes.TIMEOUT.toString());
        String institutionId = sipProps.getProperty(SipPropertyTypes.INSTITUTION_ID.toString(), "");
        sipServer = new SIPConnector
                .Builder(host, port)
                .sipUser(user)
                .password(password)
                .institution(institutionId)
                .sipUser(user)
                .timeout(timeout)
                .build();
    }

    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response)
    {
        Command command = new SIPCommand.Builder(this.sipServer)
                .setUser(userId, userPin)
                .build();
        return command;       
    }

    @Override
    public CustomerFormatter getFormatter()
    {
        return new SIPFormatter();
    }

    @Override
    public Command getStatusCommand(Response response)
    {
        Command command = new SIPCommand.Builder(this.sipServer)
                .isStatusRequest()
                .build();
        return command;
    }

    @Override
    public void interpretResults(QueryTypes commandType, CommandStatus status, Response response)
    {
        switch (commandType)
        {
            case GET_STATUS:
                if (isSuccessful(status.getStdout()) == false)
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse("SIP2 service currently not available.");
                }
                else
                {
                    response.setCode(ResponseTypes.OK);
                    response.setResponse("Services up.");
                }
                break;
            case NULL:
                response.setCode(ResponseTypes.OK);
                response.setResponse("Null SIP2 command back at you...");
                break;
            case GET_CUSTOMER: // here we need to check and set validity based on messaging from SIP response string.
                Customer c = response.getCustomer();
                if (c.get(CustomerFieldTypes.RESERVED).compareToIgnoreCase("User not found") == 0)
                {
                    c.set(CustomerFieldTypes.ISVALID, Protocol.FALSE);
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse("We cannot find your account."
                            + " If you are sure that your card number and pin "
                            + "are correct, please contact your home library "
                            + "for assistance.");
                }
                else if (c.get(CustomerFieldTypes.RESERVED).compareToIgnoreCase("Invalid PIN for station user") == 0)
                {
                    c.set(CustomerFieldTypes.ISVALID, Protocol.FALSE);
                    response.setCode(ResponseTypes.UNAUTHORIZED);
                    response.setResponse("Your pin does not match the one we have on record.");
                }
                else
                {
                    c.set(CustomerFieldTypes.ISVALID, Protocol.TRUE);
                    response.setCode(ResponseTypes.SUCCESS);
                }
                break;
            case CREATE_CUSTOMER:
            case UPDATE_CUSTOMER:
            default:
                response.setCode(ResponseTypes.UNKNOWN);
                response.setResponse(SIPRequestBuilder.class.getName() 
                        + " doesn't know how to execute the query type: "
                        + commandType.name());
        }
    }
    
    /** 
     * Tests the response string from the sip server for success.
     * @param sipResponse The response String from the sip server.
     * @return true if the command's return code was Ok, and false otherwise.
     */
    protected boolean isSuccessful(String sipResponse)
    {
        try
        {
            SIPStatusMessage message = new SIPStatusMessage(sipResponse);
            if (message.isOnline().compareTo("Y") == 0)
            {
                if (message.getPatronInfoPermitted().compareTo("Y") == 0)
                {
                    return true;
                }
            }
        }
        catch (SIPException ex)
        {
            System.out.println(SIPRequestBuilder.class.getName() 
                    + " Unexpected SIP2 message '" + sipResponse + "'.");
            return false;
        }
        return false;
    }
}