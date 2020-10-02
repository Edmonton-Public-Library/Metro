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
package mecard.requestbuilder;

import api.Command;
import api.CommandStatus;
import api.CustomerMessage;
import api.PolarisSIPCustomerMessage;
import mecard.Response;
import api.SIPCommand;
import api.SIPConnector;
import api.SIPCustomerMessage;
import api.SIPMessage;
import api.SIPStatusMessage;
import java.util.Date;
import java.util.Properties;
import mecard.Protocol;
import mecard.QueryTypes;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.MessagesTypes;
import mecard.config.SipPropertyTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.sip.SIPCustomerFormatter;
import mecard.exception.SIPException;
import mecard.config.PropertyReader;
import mecard.exception.ConfigurationException;
import site.CustomerLoadNormalizer;

/**
 * Manages any meaningful request that can be made through SIP2 requests. This
 * naturally excludes things like customer update or create.
 *
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public class SIPRequestBuilder extends ILSRequestBuilder
{
    private static SIPConnector sipServer;
    private final Properties messageProperties;
    public final static String LOCATION_CODE_TAG = "location-code"; // optional field in sip2.properties.
    /**
     * 
     * @param debug True if you would like to see send received raw SIP2 messages
     * and false otherwise.
     */
    SIPRequestBuilder(boolean debug)
    {
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        Properties sipProps = PropertyReader.getProperties(ConfigFileTypes.SIP2);
        String host = sipProps.getProperty(SipPropertyTypes.HOST.toString());
        String port = sipProps.getProperty(SipPropertyTypes.PORT.toString(), "6001"); // port optional in config.
        String user = sipProps.getProperty(SipPropertyTypes.USER.toString(), "");
        String password = sipProps.getProperty(SipPropertyTypes.PASSWORD.toString(), "");
        String timeout = sipProps.getProperty(SipPropertyTypes.TIMEOUT.toString());
        String institutionId = sipProps.getProperty(SipPropertyTypes.INSTITUTION_ID.toString(), "");
        String locationCode = sipProps.getProperty(SIPRequestBuilder.LOCATION_CODE_TAG.toString(), ""); // if not found an empty string is forwarded to the builder.
        sipServer = new SIPConnector
                .Builder(host, port)
                .sipUser(user)
                .password(password)
                .institution(institutionId)
                .sipUser(user)
                .timeout(timeout)
                .locationCode(locationCode)
                .debug(debug)
                .build();
    }

    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response)
    {
        Command command = new SIPCommand.Builder(SIPRequestBuilder.sipServer)
                .setUser(userId, userPin)
                .build();
        return command;       
    }

    @Override
    public CustomerFormatter getFormatter()
    {
        return new SIPCustomerFormatter();
    }

    @Override
    public Command getStatusCommand(Response response)
    {
        Command command = new SIPCommand.Builder(SIPRequestBuilder.sipServer)
                .isStatusRequest()
                .build();
        return command;
    }

    /**
     *
     * @param commandType the value of commandType
     * @param status the value of status
     * @param response the value of response
     * @return the true if the command was successful and false otherwise. The
     * success depends on what type of query you make.
     */
    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response)
    {
        boolean result = false;
        switch (commandType)
        {
            case GET_STATUS:
                if (isSuccessful(status.getStdout()) == false)
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                    System.out.println("SIP2 service currently not available.");
                    result = false;
                }
                else
                {
                    response.setCode(ResponseTypes.OK);
                    response.setResponse("Services up."); // doesn't require personalized message.
                    result = true;
                }
                break;
            case NULL:
                response.setCode(ResponseTypes.OK);
                response.setResponse("Null SIP2 command back at you...");
                result = true;
                break;
            case GET_CUSTOMER: // here we need to check and set validity based on messaging from SIP response string.
                Customer c = response.getCustomer();
                if (c.get(CustomerFieldTypes.RESERVED).compareToIgnoreCase("User not found") == 0)
                {
                    c.set(CustomerFieldTypes.ISVALID, Protocol.FALSE);
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                    System.out.println(new Date() + "customer account not found '" + c.get(CustomerFieldTypes.ID) + "'");
                    result = false;
                }
                else if (c.get(CustomerFieldTypes.RESERVED).compareToIgnoreCase("Invalid PIN for station user") == 0)
                {
                    c.set(CustomerFieldTypes.ISVALID, Protocol.FALSE);
                    response.setCode(ResponseTypes.UNAUTHORIZED);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.USERID_PIN_MISMATCH.toString()));
                    System.out.println("User pin does not match the one on record.");
                    result = false;
                }
                else
                {
                    c.set(CustomerFieldTypes.ISVALID, Protocol.TRUE);
                    response.setCode(ResponseTypes.SUCCESS);
                    result = true;
                }
                break;
            case TEST_CUSTOMER:
                if (status.getStatus() == ResponseTypes.USER_NOT_FOUND)
                {
                    response.setCode(ResponseTypes.USER_NOT_FOUND);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                    System.out.println(new Date() + "customer account not found.");
                    result = false;
                }
                else 
                {
                    response.setCode(status.getStatus());
                    response.setResponse(status.getStatus().name());
                    result = true;
                }
                break;
            case CREATE_CUSTOMER:
            case UPDATE_CUSTOMER:
            default:
                response.setCode(ResponseTypes.UNKNOWN);
                response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                System.out.println(SIPRequestBuilder.class.getName() 
                        + " doesn't know how to execute the query type: "
                        + commandType.name());
                result = false;
        }
        return result;
    }
    
    @Override
    public boolean tidy()
    {
        return true;
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
            if (message.isOnline())
            {
                if (message.getPatronInfoPermitted().compareTo("Y") == 0)
                {
                    System.out.println("**Warning: SIP2 patron info bit set to '" 
                            + message.getPatronInfoPermitted() + "'");
                    // but it doesn't mean you can't get patron info.
                }
                return true;
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

    /**
     * This method has throws a configuration exception because SIP2 is not capable
     * of creating or updating user accounts. Were you thinking of NCIP perhaps?
     * @param customer
     * @param response
     * @param normalizer
     * @return throws {@link  ConfigurationException} if called. This is your signal
     * that you have incorrectly selected 'sip2' for create or update protocols
     * in the environment.properties file.
     */
    @Override
    public Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        throw new ConfigurationException("SIP2 does not support account creation "
                + "Please review your environment.properties configuration");
    }

    /**
     * This method has throws a configuration exception because SIP2 is not capable
     * of creating or updating user accounts. Were you thinking of NCIP perhaps?
     * @param customer
     * @param response
     * @param normalizer
     * @return throws {@link  ConfigurationException} if called. This is your signal
     * that you have incorrectly selected 'sip2' for create or update protocols
     * in the environment.properties file.
     */
    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        throw new ConfigurationException("SIP2 does not support account update "
                + "Please review your environment.properties configuration");
    }
    
    /**
     * SIP2 is implemented differently on every ILS and most ILSs don't honour
     * the tags that 3M specifies. This method will determine which version of
     * SIPCustomerMessage should be returned. This accounts for variation of 
     * SIP2 message responses between ILSs. 
     * 
     * This method is used for tailoring tests for the Responder like isGoodStanding(),
     * the Responder is not liable for knowing the peculiarities of one SIP2 implementation
     * to the next. Passing back a custom SIPMessage on request gets around this
     * problem.
     * 
     * @param stdout
     * @return customerMessage.
     * @see SIPCustomerMessage
     */
    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        SIPMessage message = new SIPMessage(stdout);
        SIPCustomerMessage cMessage;
        switch (message.getILSType())
        {
            case POLARIS:
                cMessage = new PolarisSIPCustomerMessage(stdout);
                break;
            default:
                cMessage = new SIPCustomerMessage(stdout);
               break;
        } 
         return cMessage;
    }

    @Override
    public Command testCustomerExists(
            String userId, 
            String userPin, 
            Response response) 
    {
        // If we can get the customer information they exist. This could 
        // be changed to use a different SIP2 message.
        return getCustomerCommand(userId, userPin, response);
    }
}
