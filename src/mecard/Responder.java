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
package mecard;

import api.Command;
import api.CommandStatus;
import api.CustomerMessage;
import mecard.requestbuilder.ILSRequestBuilder;
import java.util.Date;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.MessagesConfigTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.exception.ConfigurationException;
import mecard.exception.MalformedCommandException;
import mecard.exception.MetroSecurityException;
import mecard.exception.UnsupportedCommandException;
import mecard.exception.DummyException;
import mecard.config.PropertyReader;
import mecard.customer.UserFailFile;
import mecard.customer.UserLostFile;
import mecard.exception.BusyException;
import site.CustomerLoadNormalizer;
import site.MeCardPolicy;

/**
 * Responder object handles requests and responses to and from the ILS. The 
 * responder is the adaptor that requests commands from request builders and 
 * then executes them.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class Responder
{
    protected Request request;
    protected final boolean debug;
    private final Properties messageProperties;
    
    /**
     *
     * @param cmd the value of cmd
     * @param debugMode the value of debugMode
     */
    public Responder(Request cmd, boolean debugMode)
    {
        this.debug = debugMode;
        this.request = cmd;
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        if (debug)
        {
            System.out.println("CMD:\n  '"+request.toString()+"' '"+request.getCommandType().name()+"'");
            System.out.println("ELE:");
            System.out.println("  S:" + request.toString()+ ",");
        }
    }
    
    /**
     * Creates canned exception responses if something goes wrong.
     *
     * @param ex the exception thrown
     * @return String value of the response with code.
     */
    public static Response getExceptionResponse(RuntimeException ex)
    {
        Response response = new Response(ResponseTypes.UNKNOWN);
        if (ex instanceof MetroSecurityException)
        {
            response = new Response(ResponseTypes.UNAUTHORIZED);
        }
        else if (ex instanceof MalformedCommandException)
        {
            response = new Response(ResponseTypes.ERROR);
        }
        else if (ex instanceof ConfigurationException)
        {
            response = new Response(ResponseTypes.CONFIG_ERROR);
        }
        else if (ex instanceof BusyException)
        {
            response = new Response(ResponseTypes.BUSY);
        }
        else if (ex instanceof UnsupportedCommandException)
        {
            response = new Response(ResponseTypes.UNKNOWN);
            response.setResponse("Command not implemented, make sure your server is up to date.");
        }
        else if (ex instanceof DummyException)
        {
            response = new Response(ResponseTypes.CONFIG_ERROR);
            response.setResponse("TEST: DummyCommand intentionally threw error.");
        }
        
        response.setResponse(ex.getMessage());
        return response;
    }
    
    /**
     * 
     * @return the Response of the command.
     */
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
            case GET_CUSTOMER:
                getCustomer(response);
                break;
            case GET_STATUS:
                getILSStatus(response);
                break;
            case NULL:
                response.setCode(ResponseTypes.OK);
                response.setResponse("null query back at you.");
                break;
            default:
                response.setCode(ResponseTypes.ERROR);
                response.setResponse(Responder.class.getName() + " cannot " + request.toString());
        }
        return response;
    }
    
    /**
     * Creates and executes the "get customer" command, then 
     * executes the command, and populates the argument Response with the customer
     * object and or a message about the status of the command.
     * @param response object as a container for the results.
     */
    public void getCustomer(Response response)
    {
        String userId  = this.request.getUserId();
        String userPin = this.request.getUserPin();
        
        // So all this stuff will be put to the SIPCommand
        ILSRequestBuilder requestBuilder = ILSRequestBuilder.getInstanceOf(QueryTypes.GET_CUSTOMER, debug);
        Command command = requestBuilder.getCustomerCommand(userId, userPin, response);
        CommandStatus status = command.execute();
        CustomerFormatter customerFormatter = requestBuilder.getFormatter();
        Customer customer = customerFormatter.getCustomer(status.getStdout());
        response.setCustomer(customer);
        // we let the isSuccessful method test and set the customer validity.
        // The response must have some customer data to check certain basic tests
        // so leave this method call here.
        // SIPFormatter() will place AF message in the reserve field. If it is not "OK"
        // then interpretResults() further sets ISVALID to Protocol.FALSE.
        boolean accountFoundAndPINOk  = requestBuilder.isSuccessful(QueryTypes.GET_CUSTOMER, status, response);
        boolean accountDataTestFailed = customer.isFlagSetFalse(CustomerFieldTypes.ISVALID);
        if (accountFoundAndPINOk == false || accountDataTestFailed)
        {
            response.setCustomer(null);
            System.out.println(new Date() + " **Error GET__STDOUT:"+status.getStdout());
            System.out.println(new Date() + " **Error GET__STDERR:"+status.getStderr());
            return;
        }
        // You have this before the test metro requirements b/c it checks for PIN
        // and SIP2 does not return the pin.
        customer.set(CustomerFieldTypes.PIN, userPin);
        StringBuilder failedTests = new StringBuilder();
        // So basic tests PIN, customer found; done, now test the account against system policies for ME.
        CustomerMessage customerMessage = requestBuilder.getCustomerMessage(status.getStdout());
        if (meetsMeCardRequirements(customer, customerMessage, failedTests))
        {
            response.setCode(ResponseTypes.OK);
            System.out.println(new Date() + " POLICY_OUT:"+status.getStdout());
            System.out.println(new Date() + " POLICY_ERR:"+status.getStderr());
        }
        else
        {
            // this can happen if the user is barred, underage, non-resident, reciprocal, lostcard
            // or missing key information.
            response.setCode(ResponseTypes.FAIL);
            response.setResponse(messageProperties.getProperty(MessagesConfigTypes.FAIL_METRO_POLICY.toString()));
            response.setResponse(failedTests.toString());
            response.setCustomer(null);
            System.out.println(new Date() + " **Fail POLICY_OUT:"+status.getStdout());
            System.out.println(new Date() + " **Fail POLICY_OUT:"+status.getStderr());
        }
        
    }
    
    /**
     * Gets the status of the ILS server.
     * @param response
     */
    public void getILSStatus(Response response)
    {
        ILSRequestBuilder requestBuilder = ILSRequestBuilder.getInstanceOf(QueryTypes.GET_STATUS, debug);
        Command statusCommand = requestBuilder.getStatusCommand(response);
        CommandStatus status = statusCommand.execute();
        requestBuilder.isSuccessful(QueryTypes.GET_STATUS, status, response);
        System.out.println(new Date() + " STAT_STDOUT:"+status.getStdout());
        System.out.println(new Date() + " STAT_STDERR:"+status.getStderr());
    }
    
    /**
     * Converts the customer into a ILS-meaningful expression to create a 
     * customer, then executes the command, and populates the argument 
     * response with the results. During creation the customer information is
     * checked for lost card status. This can be computed by the web site or
     * from the originating ILS.
     * 
     * The web site should replace the customer's bar code in it's database 
     * with the new bar code, once the metro server returns a successful message
     * (ResponseTypes.LOST_CARD). The signal means the customer can't use the 
     * card until they confirm with the guest library. Once done staff can run
     * the 'lost card' process.
     * 
     * @param response object
     */
    public void createCustomer(Response response)
    {
        Customer customer = request.getCustomer();
        CustomerLoadNormalizer normalizer = getNormalizerPreformatCustomer(customer, response);
        normalizer.normalizeOnCreate(customer, response);
        ILSRequestBuilder requestBuilder = ILSRequestBuilder.getInstanceOf(QueryTypes.CREATE_CUSTOMER, debug);
        Command command = requestBuilder.getCreateUserCommand(customer, response, normalizer);
        // Don't create users that are lost cards. They have to be done manually by
        // the 'lost card' process. Wait until here before testing because we can 
        // normalize all the customer information and store it so it can be loaded 
        // manually later.
        if (customer.isLostCard())
        {
            String message = messageProperties.getProperty(MessagesConfigTypes.FAIL_LOSTCARD_TEST.toString());
            UserLostFile failFile = new UserLostFile(customer, requestBuilder.getCustomerLoadDirectory());
            failFile.recordUserDataMessage(message);
            response.setCode(ResponseTypes.LOST_CARD);
            response.setResponse(message);
            System.out.println(new Date() + " LOST_CARD:"+customer.get(CustomerFieldTypes.ID));
        }
        else // Regular customer registration.
        {
            CommandStatus status = command.execute();
            System.out.println(new Date() + " CRAT_STDOUT:"+status.getStdout());
            System.out.println(new Date() + " CRAT_STDERR:"+status.getStderr());
            if (requestBuilder.isSuccessful(QueryTypes.CREATE_CUSTOMER, status, response) == false)
            {
                UserFailFile failFile = new UserFailFile(customer, requestBuilder.getCustomerLoadDirectory());
                failFile.setStatus(status);
                System.out.println(new Date() + " CRAT_FAIL:"+customer.get(CustomerFieldTypes.ID));
                throw new ConfigurationException();
            }
        }
    }

    /**
     * Creates the ILS specific command to run to update a customer account, then
     * runs it and places the results into the response object.
     * @param response 
     */
    public void updateCustomer(Response response)
    {
        Customer customer = request.getCustomer();
        CustomerLoadNormalizer normalizer = getNormalizerPreformatCustomer(customer, response);
        normalizer.normalizeOnUpdate(customer, response);
        ILSRequestBuilder requestBuilder = ILSRequestBuilder.getInstanceOf(QueryTypes.UPDATE_CUSTOMER, debug);
        Command command = requestBuilder.getUpdateUserCommand(customer, response, normalizer);
        if (customer.isLostCard())
        {
            String message = messageProperties.getProperty(MessagesConfigTypes.FAIL_LOSTCARD_TEST.toString());
            UserLostFile failFile = new UserLostFile(customer, requestBuilder.getCustomerLoadDirectory());
            failFile.recordUserDataMessage(message);
            response.setCode(ResponseTypes.LOST_CARD);
            response.setResponse(message);
            System.out.println(new Date() + " LOST_CARD:"+customer.get(CustomerFieldTypes.ID));
        }
        else // Regular customer update.
        {
            CommandStatus status = command.execute();
            System.out.println(new Date() + " UPDT_STDOUT:"+status.getStdout());
            System.out.println(new Date() + " UPDT_STDERR:"+status.getStderr());
            if (requestBuilder.isSuccessful(QueryTypes.UPDATE_CUSTOMER, status, response) == false)
            {
                UserFailFile failFile = new UserFailFile(customer, requestBuilder.getCustomerLoadDirectory());
                failFile.setStatus(status);
                System.out.println(new Date() + " UPDT_FAIL:"+customer.get(CustomerFieldTypes.ID));
                throw new ConfigurationException();
            }
        }
    }

    /**
     * Normalizes information from melibraries.ca into a format that the local ILS
     * can handle. Example: some libraries can only accept 4 digit pins. The 
     * rules for making that happen start here. Once completed the response
     * object will contain the changes that were made. In our example an 
     * explanation of that the over-sized PIN was truncated and what the new value
     * is can be added.
     *
     * @param customer
     * @param response
     * @return customer load normalizer.
     */
    
    public CustomerLoadNormalizer getNormalizerPreformatCustomer(Customer customer, Response response)
    {
        if (customer == null)
        {
            throw new MalformedCommandException("Expected customer, but got null.");
        }
        CustomerLoadNormalizer normalizer = CustomerLoadNormalizer.getInstanceOf(debug);
        StringBuilder resultStringBuilder = new StringBuilder();
        ResponseTypes responseType = normalizer.normalize(customer, resultStringBuilder);
        response.setCode(responseType);
        response.setResponse(resultStringBuilder.toString());
        return normalizer;
    }
    
    /**
     * Tests if the customer meets the required MeCard requirements. MeCard 
     * users must be:
     * <ul>
     * <li>Over the age of 18</li>
     * <li>Must to be a reciprocal customer at the home library.</li>
     * <li>Must be in good standing at their home library.</li>
     * <li>Must be a resident of the home library's service area.</li>
     * <li>Must have a valid expiry date.</li>
     * <li>Must have mandatory account fields filled with valid data.</li>
     * </ul>
     *
     * @param customer
     * @param originalMessage
     * @param failResponseMessage buffer to add any fail messages.
     * @return true if the customer meets the MeCard participation requirements
     * and false otherwise.
     */
    protected boolean meetsMeCardRequirements(
            Customer customer, 
            CustomerMessage originalMessage, 
            StringBuilder failResponseMessage)
    {
        if (customer == null || originalMessage == null)
        {
            return false;
        }
        MeCardPolicy policy = MeCardPolicy.getInstanceOf(this.debug);
        // If everything goes well we expect the customer data to be sent back
        // to be loaded. Some libraries use case to distinguish customers, you
        // know who you are, so we standardize important fields for loading on 
        // a regular ILS.
        policy.normalizeCustomerFields(customer);
        if (policy.isEmailable(customer, originalMessage, failResponseMessage) == false) 
        {
            System.out.println("Customer not emailable.");
            return false;
        }
        if (policy.isInGoodStanding(customer, originalMessage, failResponseMessage) == false)
        {
            System.out.println("Customer not in good standing.");
            return false;
        }
        if (policy.isMinimumAge(customer, originalMessage, failResponseMessage) == false)
        {
            System.out.println("Customer not minimum age.");
            return false;
        }
        if (policy.isReciprocal(customer, originalMessage, failResponseMessage) == true)
        {
            System.out.println("Customer cannot join because they are a reciprocal customer.");
            return false;
        } // reciprocals not allowed.
        if (policy.isResident(customer, originalMessage, failResponseMessage) == false) 
        {
            System.out.println("Customer is not resident.");
            return false;
        }
        if (policy.isValidCustomerData(customer, failResponseMessage) == false) 
        {
            System.out.println("**"+customer.toString()+"**");
            System.out.println("Customer's data is not valid.");
            return false;
        }
        if (policy.isValidExpiryDate(customer, originalMessage, failResponseMessage) == false) 
        {
            System.out.println("Customer does not have a valid privilege date.");
            return false;
        }
        if (policy.isLostCard(customer, originalMessage, failResponseMessage)) 
        {
            System.out.println("Customer's card reported as lost.");
            return false;
        }
   
        System.out.println("Customer cleared.");
        return true;
    }
}
