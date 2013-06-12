/*
 * Copyright (C) 2013 metro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mecard.responder;

import mecard.customer.SIPCustomerFormatter;
import java.util.Properties;
import mecard.Exception.SIPException;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.SipPropertyTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFieldTypes;
import mecard.customer.CustomerFormatter;
import mecard.util.SIPConnector;
import api.SIPRequest;
import site.mecard.MeCardPolicy;

/**
 * The SIP2 strategy is meant to retrieve initial information about customers.
 * Some computation is done to allow each library an opportunity to compute
 * answers to questions like is this customer a reciprocal customer?
 * @author andrew
 */
public class SIP2Responder extends ResponderStrategy
{
    public final static String SIP_AUTHORIZATION_FAILURE = "AFInvalid PIN";
    private static SIPConnector sipServer;
    
    public SIP2Responder(String command, boolean debugMode)
    {
        super(command, debugMode);
        this.response.setCode(ResponseTypes.BUSY);
        
        Properties sipProps = PropertyReader.getProperties(ConfigFileTypes.SIP2);
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
    public String getResponse()
    {
        // test for the operations that this responder is capable of performing
        // SIP can't create customers, BImport can't query customers.
        StringBuffer responseBuffer = new StringBuffer();
        switch (request.getCommandType())
        {
            case GET_CUSTOMER:
                this.response.setCode(getCustomer(responseBuffer));
                this.response.addResponse(responseBuffer.toString());
                break;
            case GET_STATUS:
                this.response.setCode(getStatus(responseBuffer));
                this.response.addResponse(responseBuffer.toString());
                break; // is SIP2 the best way to get the ILS status, it is one way.
            default:
                this.response.setCode(ResponseTypes.ERROR);
                this.response.addResponse(BImportResponder.class.getName()
                        + " cannot perform operation: " + request.getCommandType().toString());
        }
        return this.response.toString();
    }

    /**
     * Gets the customer information.
     * @param responseBuffer
     * @return the ResponseTypes from the execution of the query.
     */
    private ResponseTypes getCustomer(StringBuffer responseBuffer)
    {
        SIPRequest sipCustomerRequest = new SIPRequest();
        String userId  = this.request.get(0);
        String userPin = this.request.get(1);
        String sipResponse = "";
        try
        {
            sipResponse = sipServer.send(sipCustomerRequest.patronInfoRequest(userId, userPin));
        }
        catch(SIPException e)
        {
            // Can happen if the server is down, server not listening on port, login failed
            // or request timedout.
            responseBuffer.append("service is currently unavailable");
            return ResponseTypes.UNAVAILABLE;
        }
        if (isAuthorized(sipResponse) == false)
        {
            responseBuffer.append("invalid PIN");
            return ResponseTypes.UNAUTHORIZED;
        }
        CustomerFormatter sipFormatter = new SIPCustomerFormatter();
        Customer customer = sipFormatter.getCustomer(sipResponse);
        // You have this before the test metro requirements b/c it checks for PIN
        customer.set(CustomerFieldTypes.PIN, userPin);
        if (meetsMeCardRequirements(customer, sipResponse))
        {
            this.response.setCustomer(customer);
            return ResponseTypes.OK;
        }
        // this can happen if the user is barred, underage, non-resident, reciprocol.
        responseBuffer.append("there is a problem with your account, please contact your home library for assistance");
        return ResponseTypes.FAIL;
    }

    /**
     * Gets the status of the server.
     * @param responseBuffer
     * @return the ResponseTypes from the execution of the query.
     */
    private ResponseTypes getStatus(StringBuffer responseBuffer)
    {
        SIPRequest sipStatusRequest = new SIPRequest();
        String sipResponse = "";
        try
        {
            sipResponse = sipServer.send(sipStatusRequest.getILSStatus());
        }
        catch(SIPException e)
        {
            return ResponseTypes.UNAVAILABLE;
        }
        if (isSuccessful(sipResponse) == false)
        {
            return ResponseTypes.FAIL;
        }
        return ResponseTypes.OK;
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
     * matched the user name.
     * @param sipResponse
     * @return true if the user is authorized and false otherwise.
     */
    private boolean isAuthorized(String sipResponse)
    {
        if (sipResponse.contains(SIP2Responder.SIP_AUTHORIZATION_FAILURE))
        {
            return false;
        }
        return true;
    }
}
