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
package mecard.responder;

import api.Command;
import api.CommandStatus;
import api.Request;
import api.Response;
import mecard.ResponseTypes;
import mecard.customer.Customer;
import mecard.config.CustomerFieldTypes;
import mecard.customer.CustomerFormatter;
import api.SIPRequestBuilder;
import java.util.Date;
import mecard.Protocol;
import mecard.QueryTypes;

/**
 * The SIP2 strategy is meant to retrieve initial information about customers.
 * Some computation is done to allow each library an opportunity to compute
 * answers to questions like is this customer a reciprocal customer?
 * @author andrew
 */
public class SIP2Responder extends CustomerQueryable 
    implements StatusQueryable 
{
    public final static String SIP_AUTHORIZATION_FAILURE = "AFInvalid PIN";
    private static String NULL_QUERY_RESPONSE_MSG = "SIP2 responder answers ok";
    
    /**
     *
     * @param command the value of command
     * @param debugMode the value of debugMode
     */
    public SIP2Responder(Request command, boolean debugMode)
    {
        super(command, debugMode);
    }

    /**
     *
     * @return the api.Response
     */
    @Override
    public Response getResponse()
    {
        // test for the operations that this responder is capable of performing
        // SIP can't create customers, BImport can't query customers.
        Response response = new Response();
        switch (request.getCommandType())
        {
            case GET_CUSTOMER:
                getCustomer(response);
                break;
            case GET_STATUS:
                getILSStatus(response);
                break; // is SIP2 the best way to getCustomerField the ILS status, it is one way.
            case NULL:
                response.setCode(ResponseTypes.OK);
                response.setResponse(NULL_QUERY_RESPONSE_MSG);
                break;
            default:
                response.setCode(ResponseTypes.ERROR);
                response.setResponse(SIP2Responder.class.getName() + " cannot " + request.toString());
        }
        return response;
    }

    /**
     * Gets the customer information.
     * @param response object as a container for the results.
     */
    @Override
    public void getCustomer(Response response)
    {
        String userId  = this.request.getUserId();
        String userPin = this.request.getUserPin();
        
        // So all this stuff will be put to the SIPCommand
        SIPRequestBuilder sipRequestBuilder = new SIPRequestBuilder();
        Command sipCommand = sipRequestBuilder.getCustomerCommand(userId, userPin, response);
        CommandStatus status = sipCommand.execute();
        CustomerFormatter customerFormatter = sipRequestBuilder.getFormatter();
        Customer customer = customerFormatter.getCustomer(status.getStdout());
        response.setCustomer(customer);
        sipRequestBuilder.interpretResults(QueryTypes.GET_CUSTOMER, status, response);
        // SIPFormatter() will place AF message in the reserve field. If it is not "OK"
        // then interpretResults() further sets ISVALID to Protocol.FALSE.
        if (customer.get(CustomerFieldTypes.ISVALID).compareTo(Protocol.FALSE) == 0)
        {
            response.setCustomer(null);
            System.out.println(new Date() + " GET__STDOUT:"+status.getStdout());
            System.out.println(new Date() + " GET__STDERR:"+status.getStderr());
            return;
        }
        // You have this before the test metro requirements b/c it checks for PIN
        // and SIP2 does not return the pin.
        customer.set(CustomerFieldTypes.PIN, userPin);
        if (meetsMeCardRequirements(customer, status.getStdout()))
        {
            response.setCode(ResponseTypes.OK);
        }
        else
        {
            // this can happen if the user is barred, underage, non-resident, reciprocol, lostcard.
            response.setResponse("there is a problem with your account, please contact your home library for assistance");
            response.setCode(ResponseTypes.FAIL);
        }
        System.out.println(new Date() + " GET__STDOUT:"+status.getStdout());
        System.out.println(new Date() + " GET__STDERR:"+status.getStderr());
    }

    /**
     * Gets the status of the server.
     * @param response
     */
    @Override
    public void getILSStatus(Response response)
    {
        SIPRequestBuilder sipRequestBuilder = new SIPRequestBuilder();
        Command sipCommand = sipRequestBuilder.getStatusCommand(response);
        CommandStatus status = sipCommand.execute();
        sipRequestBuilder.interpretResults(QueryTypes.GET_STATUS, status, response);
        System.out.println(new Date() + " STAT_STDOUT:"+status.getStdout());
        System.out.println(new Date() + " STAT_STDERR:"+status.getStderr());
    }

    /**
     * Tests if the request was valid or not based on whether the supplied PIN
     * matched the user's pin.
     *
     * @param suppliedPin
     * @param customer the value of customer
     * @return true if the user is authorized and false otherwise.
     */
    @Override
    public boolean isAuthorized(String suppliedPin, Customer customer)
    {
        if (suppliedPin.contains(SIP2Responder.SIP_AUTHORIZATION_FAILURE))
        {
            return false;
        }
        return true;
    }
}
