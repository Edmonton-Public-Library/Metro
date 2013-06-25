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

import api.SymphonyAPIBuilder;
import java.util.Properties;
import mecard.ResponseTypes;
import mecard.config.APIPropertyTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import api.ILSRequestBuilder;
import mecard.Exception.UnsupportedAPIException;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import api.Command;
import api.ProcessWatcherHandler;
import api.Request;

/**
 *
 * @author andrew
 */
public class APIResponder extends Responder
    implements StatusQueryable, CustomerQueryable, Createable, Updateable
{
    private static String NULL_QUERY_RESPONSE_MSG = "API says hello.";
    private final ILSRequestBuilder api;
    
    /**
     *
     * @param command the value of command
     * @param debugMode the value of debugMode
     */
    public APIResponder(Request command, boolean debugMode)
    {
        super(command, debugMode);
        this.response.setCode(ResponseTypes.BUSY);
        Properties apiProps = PropertyReader.getProperties(ConfigFileTypes.API);
        String ils = apiProps.getProperty(APIPropertyTypes.ILS_TYPE.toString());
        api = APIRequest.getInstanceOf(ils, debug);
    }

    @Override
    public String getResponse()
    {
        StringBuffer responseBuffer = new StringBuffer();
        switch (request.getCommandType())
        {
            case GET_STATUS:
                this.response.setCode(getILSStatus(responseBuffer));
                break;
            case GET_CUSTOMER:
                this.response.setCode(getCustomer(responseBuffer));
                break;
            case CREATE_CUSTOMER:
                this.response.setCode(createCustomer(responseBuffer));
                break;
            case UPDATE_CUSTOMER:
                this.response.setCode(updateCustomer(responseBuffer));
                break;
            case NULL:
                this.response.setCode(ResponseTypes.OK);
                responseBuffer.append(APIResponder.NULL_QUERY_RESPONSE_MSG);
                break;
            default:
                this.response.setCode(ResponseTypes.ERROR);
                responseBuffer.append(APIResponder.class.getName());
                responseBuffer.append(" cannot ");
                responseBuffer.append(request.toString());
        }
        // appending empty buffer puts an empty string on the end of the response.
        if (responseBuffer.length() > 0)
        {
            this.response.addResponse(responseBuffer.toString());
        }
        return response.toString();
    }

    @Override
    public ResponseTypes updateCustomer(StringBuffer responseBuffer)
    {
        // I have a customer, I need to reload them into the ils
        Customer customer = new Customer(request.toString());
        Command apiCommand = api.updateUser(customer, responseBuffer);
        ProcessWatcherHandler status = apiCommand.execute();
        if (status.getStatus() == ResponseTypes.OK)
        {
            // alls good so add a nice message.
            responseBuffer.append("Thank you.");
        }
        return status.getStatus(); 
    }

    @Override
    public ResponseTypes createCustomer(StringBuffer responseBuffer)
    {
        // I have a customer, I need to load them into the ils
        Customer customer = new Customer(request.toString());
        Command apiCommand = api.createUser(customer, responseBuffer);
        ProcessWatcherHandler status = apiCommand.execute();
        if (status.getStatus() == ResponseTypes.OK)
        {
            // alls good so add a nice message.
            responseBuffer.append("Thank you for using the MeCard.");
        }
        return status.getStatus();
    }
    
    @Override
    public ResponseTypes getCustomer(StringBuffer responseBuffer)
    {
        // creates using a generic api command.
        // get the user's code from the request object.
        String userId  = this.request.get(0);
        String userPin = this.request.get(1);
        // the response buffer is in case the command fails, we can populate
        // it with a meaningful error message(s).
        Command getUserAPI = api.getCustomer(userId, userPin, responseBuffer);
        ProcessWatcherHandler commandRun = getUserAPI.execute();
        if (commandRun.getStatus() == ResponseTypes.OK)
        {
            // need to create a customer object from the response.
            CustomerFormatter formatter = api.getFormatter();
            System.out.println("STDOUT"+commandRun.getStdout());
            Customer customer = formatter.getCustomer(commandRun.getStdout());
            responseBuffer.append(customer.toString());
        }
        return commandRun.getStatus();
    }

    @Override
    public ResponseTypes getILSStatus(StringBuffer responseBuffer)
    {
        Command getUserAPI = api.getStatus(responseBuffer);
        ProcessWatcherHandler commandRun = getUserAPI.execute();
        if (commandRun.getStatus() == ResponseTypes.OK)
        {
            responseBuffer.append("status: up");
        }
        else
        {  
            responseBuffer.append("status: down");
        }
        return commandRun.getStatus();
    }

    /**
     * Creates a new api request handler object depending on the type of 
     * API wanted.
     */
    private static class APIRequest
    {
        private static boolean debug;

        private static ILSRequestBuilder getInstanceOf(String whichAPI, boolean b)
        {
            debug = b;
            if (whichAPI.equalsIgnoreCase("Symphony"))
            {
                return new SymphonyAPIBuilder();
            } // example of how to extend
//            else if (apiName.equalsIgnoreCase("Horizon"))
//            {
//                return new SQLAPIBuilder();
//            }
            throw new UnsupportedAPIException(whichAPI);
        }
    } 
}
