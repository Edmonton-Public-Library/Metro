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

import api.Request;
import api.Response;
import mecard.customer.SIPFormatter;
import java.util.Properties;
import mecard.Exception.SIPException;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.SipPropertyTypes;
import mecard.customer.Customer;
import mecard.config.CustomerFieldTypes;
import mecard.customer.CustomerFormatter;
import api.SIPConnector;
import api.SIPRequest;
import mecard.MetroService;

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
    private static SIPConnector sipServer;
    private static String NULL_QUERY_RESPONSE_MSG = "SIP2 responder answers ok";
    
    /**
     *
     * @param command the value of command
     * @param debugMode the value of debugMode
     */
    public SIP2Responder(Request command, boolean debugMode)
    {
        super(command, debugMode);        
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
        SIPRequest sipCustomerRequest = new SIPRequest();
        String userId  = this.request.getUserId();
        String userPin = this.request.getUserPin();
        if (userId.isEmpty() || userPin.isEmpty())
        {
            throw new SIPException("Supplied user id or pin (or both) were empty.");
        }
        String sipResponse = "";
        try
        {
            sipResponse = sipServer.send(sipCustomerRequest.patronInfoRequest(userId, userPin));
        }
        catch(SIPException e)
        {
            // Can happen if the server is down, server not listening on port, login failed
            // or request timedout.
            response.setResponse("service is currently unavailable");
            response.setCode(ResponseTypes.UNAVAILABLE);
        }
        if (isAuthorized(sipResponse, null) == false)
        {
            response.setResponse("invalid PIN");
            response.setCode(ResponseTypes.UNAUTHORIZED);
        }
        CustomerFormatter sipFormatter = new SIPFormatter();
        Customer customer = sipFormatter.getCustomer(sipResponse);
        // You have this before the test metro requirements b/c it checks for PIN
        customer.set(CustomerFieldTypes.PIN, userPin);
        if (meetsMeCardRequirements(customer, sipResponse))
        {
            response.setCustomer(customer);
            response.setCode(ResponseTypes.OK);
        }
        else
        {
            // this can happen if the user is barred, underage, non-resident, reciprocol, lostcard.
            response.setResponse("there is a problem with your account, please contact your home library for assistance");
            response.setCode(ResponseTypes.FAIL);
        }
    }

    /**
     * Gets the status of the server.
     * @param response
     */
    @Override
    public void getILSStatus(Response response)
    {
        SIPRequest sipStatusRequest = new SIPRequest();
        String sipResponse = "";
        try
        {
            sipResponse = sipServer.send(sipStatusRequest.getILSStatus());
        }
        catch(SIPException e)
        {
            response.setCode(ResponseTypes.UNAVAILABLE);
        }
        if (isSuccessful(sipResponse) == false)
        {
            response.setCode(ResponseTypes.FAIL);
        }
        else
        {
            response.setCode(ResponseTypes.OK);
        }
    }

    /** 
     * Tests the response string from the sip server for success.
     * @param sipResponse The response from the sip server.
     * @return true if the command's return code was Ok, and false otherwise.
     */
    private boolean isSuccessful(String sipResponse)
    {
        // test the return codes, convert to a ResponseTypes and return true
        if (sipResponse.length() > 64)
        {
            //recv:98YYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYYYYYNNYYY|ANSIPCHK|AY1AZE80C
            // We need to check that the values at position 2 (online status) 
            // and 56 (Patron Information) are both 'Y'
            if (sipResponse.charAt(2) == 'Y' && sipResponse.charAt(63) == 'Y') // zero indexed don't forget.
            {
                return true;
            }
        }
        return false;
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
