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

import mecard.ResponseTypes;
import api.BImportRequestBuilder;
import api.Command;
import api.CommandStatus;
import api.Request;
import api.Response;
import java.util.Date;
import mecard.QueryTypes;
import mecard.customer.Customer;
import site.mecard.CustomerLoadNormalizer;

/**
 * BImport responder has special capabilities to write files to the local file
 * system and then execute local commands against that file.
 *
 * @since 1.1
 * @author metro
 */
public class BImportResponder extends Responder
    implements Updateable
{
    private static String NULL_QUERY_RESPONSE_MSG = "BImport responder null request answered";

    /**
     *
     * @param cmd the value of cmd
     * @param debugMode the value of debugMode
     */
    public BImportResponder(Request cmd, boolean debugMode)
    {
        super(cmd, debugMode);
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
                response.setResponse(BImportResponder.class.getName() + " cannot " + request.toString());
        }
        return response;
    }

    /**
     * Looks confusing but merely converts the customer into a ILS meaningful
     * expression of some sort (for BImport that's a command line expression or
     * bat file name), then executes the command.
     * 
     * @param response object
     */
    @Override
    public void createCustomer(Response response)
    {
        Customer customer = request.getCustomer();
        normalize(response, customer);
        BImportRequestBuilder bimportRequestBuilder = new BImportRequestBuilder();
        Command bimportCommand = bimportRequestBuilder.getCreateUserCommand(customer, response);
        CommandStatus status = bimportCommand.execute();
        bimportRequestBuilder.interpretResults(QueryTypes.CREATE_CUSTOMER, status, response);
        System.out.println(new Date() + " CRAT_STDOUT:"+status.getStdout());
        System.out.println(new Date() + " CRAT_STDERR:"+status.getStderr());
    }

    @Override
    public void updateCustomer(Response response)
    {
        Customer customer = request.getCustomer();
        normalize(response, customer);
        BImportRequestBuilder bimportRequestBuilder = new BImportRequestBuilder();
        Command bimportCommand = bimportRequestBuilder.getUpdateUserCommand(customer, response);
        CommandStatus status = bimportCommand.execute();
        bimportRequestBuilder.interpretResults(QueryTypes.UPDATE_CUSTOMER, status, response);
        System.out.println(new Date() + " UPDT_STDOUT:"+status.getStdout());
        System.out.println(new Date() + " UPDT_STDERR:"+status.getStderr());
    }

    @Override
    public void normalize(Response response, Customer customer)
    {
        if (customer == null)
        {
            return;
        }
        CustomerLoadNormalizer normalizer = CustomerLoadNormalizer.getInstanceOf(debug);
        String changes = normalizer.normalize(customer);
        response.setResponse(changes);
    }
}
