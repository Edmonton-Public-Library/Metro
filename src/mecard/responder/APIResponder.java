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
import api.ILSRequestBuilder;
import mecard.Exception.UnsupportedAPIException;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import api.Command;
import api.ProcessWatcherHandler;
import api.Request;
import api.Response;
import mecard.MetroService;
import mecard.customer.CustomerFieldTypes;

/**
 *
 * @author andrew
 */
public class APIResponder extends CustomerQueryable
    implements StatusQueryable, Createable, Updateable
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
        
        Properties apiProps = MetroService.getProperties(ConfigFileTypes.API);
        String ils = apiProps.getProperty(APIPropertyTypes.ILS_TYPE.toString());
        api = APIRequest.getInstanceOf(ils, debug);
    }

    /**
     *
     * @return the api.Response
     */
    @Override
    public Response getResponse()
    {
        Response response = new Response();
        switch (request.getCommandType())
        {
            case GET_STATUS:
                getILSStatus(response);
                break;
            case GET_CUSTOMER:
                getCustomer(response);
                break;
            case CREATE_CUSTOMER:
                createCustomer(response);
                break;
            case UPDATE_CUSTOMER:
                updateCustomer(response);
                break;
            case NULL:
                response.setCode(ResponseTypes.OK);
                response.setResponse(NULL_QUERY_RESPONSE_MSG);
                break;
            default:
                response.setCode(ResponseTypes.ERROR);
                response.setResponse(APIResponder.class.getName() + " cannot " + request.toString());
        }
        return response;
    }

    @Override
    public void updateCustomer(Response response)
    {
        // I have a customer, I need to reload them into the ils
        Customer customer = request.getCustomer();
        Command apiCommand = api.getUpdateUserCommand(customer, response);
        ProcessWatcherHandler status = apiCommand.execute();
        if (status.getStatus() == ResponseTypes.COMMAND_COMPLETED)
        {
            // alls good so add a nice message.
            response.setResponse("Thank you.");
        }
        // The command thread will block until it gets a message from running process
        // once received, interpret the results for a meaningful message back to 
        // melibraries.ca
        api.interpretResults(null, status, response);
        System.out.println("UPDT_STDOUT:"+status.getStdout());
        System.out.println("UPDT_STDERR:"+status.getStderr());
        response.setCode(status.getStatus()); 
    }

    @Override
    public void createCustomer(Response response)
    {
        // I have a customer, I need to load them into the ils
        Customer customer = request.getCustomer();
        Command apiCommand = api.getCreateUserCommand(customer, response);
        ProcessWatcherHandler status = apiCommand.execute();
        if (status.getStatus() == ResponseTypes.COMMAND_COMPLETED)
        {
            // alls good so add a nice message.
            response.setResponse("Thank you for using the MeCard.");
        }
        System.out.println("CRAT_STDOUT:"+status.getStdout());
        System.out.println("CRAT_STDERR:"+status.getStderr());
        response.setCode(status.getStatus());
    }
    
    @Override
    public void getCustomer(Response response)
    {
        // creates using a generic api command.
        // getCustomerField the user's code from the request object.
        String userId  = this.request.getCustomerField(CustomerFieldTypes.ID);
        String userPin = this.request.getCustomerField(CustomerFieldTypes.PIN);
        // the response buffer is in case the command fails, we can populate
        // it with a meaningful error message(s).
        Command getUserAPI = api.getCustomerCommand(userId, userPin, response);
        ProcessWatcherHandler status = getUserAPI.execute();
        if (status.getStatus() == ResponseTypes.COMMAND_COMPLETED)
        {
            // need to create a customer object from the response.
            CustomerFormatter formatter = api.getFormatter();
            Customer customer = formatter.getCustomer(status.getStdout());
            // Now we have a customer we need to set their 
            response.setCustomer(customer);
        }
        System.out.println("CRAT_STDOUT:"+status.getStdout());
        System.out.println("CRAT_STDERR:"+status.getStderr());
        response.setCode(status.getStatus());
    }

    /**
     *
     * @param response the value of responseBuffer
     */
    @Override
    public void getILSStatus(Response response)
    {
        Command getUserAPI = api.getStatusCommand(response);
        ProcessWatcherHandler status = getUserAPI.execute();
        if (status.getStatus() == ResponseTypes.COMMAND_COMPLETED)
        {
            response.setResponse("status: up");
        }
        else
        {  
            response.setResponse("status: down");
        }
        System.out.println("CHK_STDOUT:"+status.getStdout());
        System.out.println("CHK_STDERR:"+status.getStderr());
        response.setCode(status.getStatus());
    }

    @Override
    public boolean isAuthorized(String suppliedPin, Customer customer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            if (debug)
            {
                System.out.println("REQ_INSTANCE:" + whichAPI);
            }
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
