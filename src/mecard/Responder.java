/*
 * Metro allows customers from any affiliate library to join any other 
 * member library.
 *    Copyright (C) 2024 - 2025 Edmonton Public Library
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
import mecard.config.MessagesTypes;
import mecard.customer.Customer;
import mecard.exception.ConfigurationException;
import mecard.exception.MalformedCommandException;
import mecard.exception.MetroSecurityException;
import mecard.exception.UnsupportedCommandException;
import mecard.exception.DummyException;
import mecard.config.PropertyReader;
import mecard.customer.DumpUser;
import mecard.exception.BusyException;
import site.CustomerLoadNormalizer;
import site.MeCardPolicy;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.exception.CustomerCreationException;
import mecard.exception.CustomerUpdateException;
import mecard.util.HealthCheck;

/**
 * Responder object handles requests and responses to and from the ILS in a 
 * vendor-neutral way. It does this by using bespoke adapters defined in the
 * environment.properties file. Those adapters are known as request builders and
 * the library may use different services for different requests. For example
 * a library may use SIP to get customer information, a web service to register
 * the customer, but a system monitoring application to get the status of the ILS.
 * 
 * The Responder is oblivious to the complexity of each service which allows it
 * to focus on executing standard commands and getting standardized responses 
 * during the registration process without knowing anything about the underlying 
 * services.
 *  
 * @author Andrew Nisbet andrew at dev-ils.com
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
        System.out.println("CMD:\n  '"+request.toString()+"' '"+request.getCommandType().name()+"'");
        System.out.println("ELE:");
        System.out.println("  S:" + request.toString()+ ",");
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
        // It is possible to get a null exception object.
        if (ex == null)
        {
            response.setResponse("Sorry, there was a problem contact "
                    + " library staff for help with error 111.");
        }
        else if (ex instanceof MetroSecurityException)
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
        else
        {
            response.setResponse(ex.getMessage());
        }
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
            case TEST_CUSTOMER:
                testCustomerExists(response);
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
    private void getCustomer(Response response)
    {
        String userId  = this.request.getUserId();
        String userPin = this.request.getUserPin();
        
        // So all this stuff will be put to the (usually) SIPCommand.
        ILSRequestBuilder requestBuilder = ILSRequestBuilder.getInstanceOf(QueryTypes.GET_CUSTOMER, debug);
        Command command = requestBuilder.getCustomerCommand(userId, userPin, response);
        CommandStatus status = command.execute();
        NativeFormatToMeCardCustomer customerFormatter = requestBuilder.getFormatter();
        // Use the formatter to convert the returned SIP2 info into a ME customer object.
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
            if (this.debug)
            {
                System.out.println(new Date() + " POLICY_OUT: "+(status.getStdout().isBlank() ? "<none>" : status.getStdout()));
                System.out.println(new Date() + " POLICY_ERR: "+(status.getStderr().isBlank() ? "<none>" : status.getStderr()));
            }
        }
        else
        {
            // this can happen if the user is barred, underage, non-resident, reciprocal, lostcard
            // or missing key information.
            response.setCode(ResponseTypes.FAIL);
            response.setResponse(messageProperties.getProperty(MessagesTypes.FAIL_METRO_POLICY.toString()));
            response.setResponse(failedTests.toString());
            response.setCustomer(null);
            System.out.println(new Date() + " **Fail POLICY_OUT:"+status.getStdout());
            System.out.println(new Date() + " **Fail POLICY_OUT:"+status.getStderr());
        }
        requestBuilder.tidy();
    }
    
    /**
     * Tests if the customer has an account at the target library system.
     * @param response 
     */
    private void testCustomerExists(Response response) 
    {
        String userId  = this.request.getUserId();
        String userPin = this.request.getUserPin();
        
        // So all this stuff will be put to the SIPCommand
        ILSRequestBuilder requestService = ILSRequestBuilder.getInstanceOf(
                QueryTypes.TEST_CUSTOMER, debug);
        // It now matters that there is a difference between getting the customer
        // which is fine for SIP but not for Polaris API, so we are no longer 
        // using the getCustomerCommand() as a default.
        Command command = requestService.testCustomerExists(userId, userPin, response);
        CommandStatus status = command.execute();
        // fill in the response with status info and test.
        requestService.isSuccessful(QueryTypes.TEST_CUSTOMER, status, response);
        // Make sure we don't return any customer information. It's still 
        // available in the request if we need to update or create.
        response.setCustomer(null);
        requestService.tidy();
    }
    
    /**
     * Gets the status of the ILS server.
     * @param response
     */
    private void getILSStatus(Response response)
    {
        // Do a health check on the local server.
        response.setResponse(HealthCheck.getServerHealth());
        ILSRequestBuilder requestBuilder = ILSRequestBuilder.getInstanceOf(QueryTypes.GET_STATUS, debug);
        Command statusCommand = requestBuilder.getStatusCommand(response);
        CommandStatus status = statusCommand.execute();
        requestBuilder.isSuccessful(QueryTypes.GET_STATUS, status, response);
        System.out.println(new Date() + " STAT_STDOUT:"+status.getStdout());
        System.out.println(new Date() + " STAT_STDERR:"+status.getStderr());
        requestBuilder.tidy();
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
     * the 'lostcard' process.
     * 
     * @param response object
     */
    private void createCustomer(Response response)
    {
        this.createCustomer(response, true);
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
     * @param response object with messages for the customer and status codes.
     * @param checkCustomerExists tells the 
     */
    private void createCustomer(Response response, boolean checkCustomerExists)
    {
        Customer customer = request.getCustomer();
        if (checkCustomerExists)
        {
            System.out.println(new Date() + " precheck if customer exists...");
            // coopt the request and change it to getCustomer() as step 1.
            // This is to test if the account still exists on the ILS for updating.
            // If it doesn't use the createCustomer method instead.
            // Get customer was designed to receive the user pin and ID already
            // set in the request. Update and Create don't require it specifically.
            request.setCode(QueryTypes.TEST_CUSTOMER);
            request.setUserId(customer.get(CustomerFieldTypes.ID));
            request.setPin(customer.get(CustomerFieldTypes.PIN));
            testCustomerExists(response);
            if (response.getCode() == ResponseTypes.USER_NOT_FOUND)
            {
                // This is the expected result for a create customer request.
                request.setCode(QueryTypes.CREATE_CUSTOMER);
                // To stop multiple messages from being sent to the customer which 
                // would cause confusion clear the response's message string.
                response.reset();
                System.out.println(new Date() + " customer doesn't exist yet.");
                // and carry on with creating the customer.
            }
            else // Account 'not found'. Even on fail safer to try to update an account.
            {
                request.setCode(QueryTypes.UPDATE_CUSTOMER);
                // We just went through the createCustomer process, success or fail
                // don't proceed to update the customer.
                response.reset();
                System.out.println(new Date() + "Warn: customer exists, switching to update.");
                updateCustomer(response, false);
                return;
            } 
        }
        // Carry on with normal customer creation.
        CustomerLoadNormalizer normalizer = getNormalizerPreformatCustomer(customer, response);
        normalizer.normalizeOnCreate(customer, response);
        ILSRequestBuilder requestBuilder = ILSRequestBuilder.getInstanceOf(QueryTypes.CREATE_CUSTOMER, debug);
        Command command = requestBuilder.getCreateUserCommand(customer, response, normalizer);
        if (customer.isLostCard())
        {
            // The responder now lets the RequestBuilder sub-systems handle this because some can now
            // process lost cards, and even those that don't can output the customer file in a system
            // dependant way that is abstracted away for the duties of the Responder. It also prevents
            // multipule messages from displaying to the customer.
            System.out.println(new Date() + " REPLACEMENT_CARD:" + customer.get(CustomerFieldTypes.ID));
        }
        CommandStatus status = command.execute();
        System.out.println(new Date() + " CRAT_STDOUT:"+status.getStdout());
        System.out.println(new Date() + " CRAT_STDERR:"+status.getStderr());
        if (requestBuilder.isSuccessful(QueryTypes.CREATE_CUSTOMER, status, response) == false)
        {
            new DumpUser.Builder(customer, requestBuilder.getCustomerLoadDirectory(), DumpUser.FileType.fail)
                .set(customer) // explicitly outputs user data.
                .build();
            System.out.println(new Date() + " CRAT_FAIL:"+customer.get(CustomerFieldTypes.ID));
            throw new CustomerCreationException(response.getMessage());
        }
        requestBuilder.tidy();
    }
    
    /**
     * Creates the ILS specific command to run to update a customer account, then
     * runs it and places the results into the response object.
     * 
     * If a customer has already registered with a library, but the account has 
     * expired and been removed from the ILS, requesting an update
     * may fail. To address this we first confirm the account exists before we
     * try and update it.
     * @param response 
     * @param checkCustomerExists true if you want to check if the customer exists
     * and false otherwise. If update checks for a customer and one can't be found
     * it will call {@link #createCustomer(mecard.Response, boolean) createCustomer} 
     * so the customer check isn't performed a second time.
     */
    private void updateCustomer(Response response)
    {
        this.updateCustomer(response, true);
    }
    
    
    /**
     * Creates the ILS specific command to run to update a customer account, then
     * runs it and places the results into the response object.
     * 
     * If a customer has already registered with a library, but the account has 
     * expired and been removed from the ILS, requesting an update
     * may fail. To address this we first confirm the account exists before we
     * try and update it.
     * @param response 
     */
    private void updateCustomer(Response response, boolean checkCustomerExists)
    {
        Customer customer = request.getCustomer();
        if (checkCustomerExists)
        {
            System.out.println(new Date() + " precheck if customer exists...");
            // coopt the request and change it to getCustomer() as step 1.
            // This is to test if the account still exists on the ILS for updating.
            // If it doesn't use the createCustomer method instead.
            request.setCode(QueryTypes.GET_CUSTOMER);
            request.setUserId(customer.get(CustomerFieldTypes.ID));
            request.setPin(customer.get(CustomerFieldTypes.PIN));
            testCustomerExists(response);
            // To stop multiple messages from being sent to the customer which 
            // would cause confusion clear the response's message string.
            if (response.getCode() == ResponseTypes.USER_NOT_FOUND)
            {
                System.out.println(new Date() + "Warn: customer doesn't exist "
                        + "switching to create.");
                request.setCode(QueryTypes.CREATE_CUSTOMER);
                response.reset();
                createCustomer(response, false);
                return;
            }
            else
            {
                // expected response.
                System.out.println(new Date() + " customer exists, updating.");
                request.setCode(QueryTypes.UPDATE_CUSTOMER);
                response.reset();
                // And carry on with the udpate.
            }
        }

        // Otherwise update the customer information.
        CustomerLoadNormalizer normalizer = getNormalizerPreformatCustomer(customer, response);
        normalizer.normalizeOnUpdate(customer, response);
        ILSRequestBuilder requestBuilder = ILSRequestBuilder.getInstanceOf(QueryTypes.UPDATE_CUSTOMER, debug);
        Command command = requestBuilder.getUpdateUserCommand(customer, response, normalizer);
        if (customer.isLostCard())
        {
            // The responder now lets the RequestBuilder sub-systems handle this because some can now
            // process lost cards, and even those that don't can output the customer file in a system
            // dependant way that is abstracted away for the duties of the Responder. It also prevents
            // multipule messages from displaying to the customer.
            System.out.println(new Date() + " REPLACEMENT_CARD:" + customer.get(CustomerFieldTypes.ID));
        }
        CommandStatus status = command.execute();
        System.out.println(new Date() + " UPDT_STDOUT:"+status.getStdout());
        System.out.println(new Date() + " UPDT_STDERR:"+status.getStderr());
        if (requestBuilder.isSuccessful(QueryTypes.UPDATE_CUSTOMER, status, response) == false)
        {
            new DumpUser.Builder(customer, requestBuilder.getCustomerLoadDirectory(), DumpUser.FileType.fail)
                .set(customer) // explicitly outputs user data.
                .build();
            System.out.println(new Date() + " UPDT_FAIL:"+customer.get(CustomerFieldTypes.ID));
            throw new CustomerUpdateException(response.getMessage());
        }
        requestBuilder.tidy();
    }

    /**
     * Normalizes information from melibraries.ca into a format that the local ILS
     * can handle. Example: some libraries can only accept 4 digit pins. The 
     * rules for making that happen start here. Once completed the response
     * object will contain the changes that were made. In our example an 
     * explanation of why the user's password was converted to a PIN and what 
     * the new value is can be added.
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
