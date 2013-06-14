/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.responder;

import api.SymphonyAPIBuilder;
import java.util.Properties;
import mecard.ResponseTypes;
import mecard.config.APIPropertyTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import api.APIBuilder;
import java.util.ArrayList;
import java.util.List;
import mecard.Exception.UnsupportedAPIException;
import mecard.customer.Customer;
import mecard.customer.CustomerFieldTypes;
import mecard.customer.CustomerFormatter;
import mecard.util.Command;
import mecard.util.ProcessWatcherHandler;

/**
 *
 * @author andrew
 */
public class APIResponder extends ResponderStrategy
{
    private static String NULL_QUERY_RESPONSE_MSG = "API says hello.";
    private final APIBuilder api;
    
    public APIResponder(String command, boolean debugMode)
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
                this.response.setCode(getServerStatus(responseBuffer));
                this.response.setResponse(responseBuffer.toString());
                break;
            case GET_CUSTOMER:
                this.response.setCode(getUser(responseBuffer));
                this.response.setResponse(responseBuffer.toString());
                break;
            case CREATE_CUSTOMER:
                this.response.setCode(createCustomer(responseBuffer));
                this.response.addResponse(responseBuffer.toString());
                break;
            case UPDATE_CUSTOMER:
                this.response.setCode(updateCustomer(responseBuffer));
                this.response.addResponse(responseBuffer.toString());
                break;
            case NULL:
                this.response.setCode(ResponseTypes.OK);
                this.response.addResponse(APIResponder.NULL_QUERY_RESPONSE_MSG);
                break;
            default:
                this.response.setCode(ResponseTypes.ERROR);
                this.response.addResponse(BImportResponder.class.getName()
                        + " cannot " + request.toString());
        }
        return response.toString();
    }

    protected ResponseTypes updateCustomer(StringBuffer responseBuffer)
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

    protected ResponseTypes createCustomer(StringBuffer responseBuffer)
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
    
    /**
     * Splits apart an in-coming request into it's command, checksum and customer
     * then maps those fields from the string to fields an APIBuilder can use to
     * create commands to execute the request.
     * 
     * @param customerCommands
     * @return f
     */
    protected boolean convert(List<String> customerCommands)
    {
//        for (CustomerFieldTypes fType: CustomerFieldTypes.values())
//        {
//            customerCommands.add(request.get(fType.ordinal()));
//            System.out.println("FIELD:" + fType.name());
//        }
        return true;
    }

    protected ResponseTypes getUser(StringBuffer responseBuffer)
    {
        // creates using a generic api command.
        // get the user's code from the request object.
        String userId  = this.request.get(0);
        String userPin = this.request.get(1);
        // the response buffer is in case the command fails, we can populate
        // it with a meaningful error message(s).
        Command getUserAPI = api.getUser(userId, userPin, responseBuffer);
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

    protected ResponseTypes getServerStatus(StringBuffer responseBuffer)
    {
        throw new UnsupportedOperationException("Not supported, use sip for server status.");
    }

    /**
     * Creates a new api request handler object depending on the type of 
     * API wanted.
     */
    private static class APIRequest
    {
        private static boolean debug;

        private static APIBuilder getInstanceOf(String whichAPI, boolean b)
        {
            debug = b;
            if (whichAPI.equalsIgnoreCase("Symphony"))
            {
                return new SymphonyAPIBuilder();
            }
//            else if (apiName.equalsIgnoreCase("Horizon"))
//            {
//                return new SQLAPIBuilder();
//            }
            throw new UnsupportedAPIException(whichAPI);
        }
    } 
}
