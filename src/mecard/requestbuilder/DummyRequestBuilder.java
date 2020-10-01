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
import api.DummyCommand;
import mecard.Response;
import java.util.Properties;
import mecard.QueryTypes;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.DebugQueryConfigTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.symphony.FlatCustomerFormatter;
import mecard.customer.sip.SIPCustomerFormatter;
import mecard.exception.DummyException;
import mecard.config.PropertyReader;
import mecard.customer.polaris.PolarisSQLCustomerFormatter;
import site.CustomerLoadNormalizer;

/**
 *
 * @author Andrew Nisbet andrew.nisbet@epl.ca
 */
public class DummyRequestBuilder extends ILSRequestBuilder
{
    private final int commandStatus;
    private final String stdout;
    private final String stderr;
    private final boolean debug;
    private final String format;
    
    DummyRequestBuilder(boolean debug)
    {
        this.debug = debug;
        Properties props = PropertyReader.getProperties(ConfigFileTypes.DEBUG);
        String s = props.getProperty(DebugQueryConfigTypes.COMMAND_RESULT_CODE.toString());
        try
        {
            this.commandStatus = Integer.parseInt(s);
        }
        catch (NumberFormatException ex)
        {
            throw new DummyException(DummyRequestBuilder.class.getName()
                    + " supplied result status in properties file must be an integer.");
        }
        this.stdout = props.getProperty(DebugQueryConfigTypes.STDOUT_MESSAGE.toString());
        this.stderr = props.getProperty(DebugQueryConfigTypes.STDERR_MESSAGE.toString());
        this.format = props.getProperty(DebugQueryConfigTypes.MESSAGE_FORMAT.toString());
        if (debug) System.out.println(DummyRequestBuilder.class.getName() + " loaded properties.");
    }

    @Override
    public CustomerFormatter getFormatter()
    {
        if (this.format.compareToIgnoreCase(ResponderMethodTypes.SIP2.toString()) == 0)
        {
            return new SIPCustomerFormatter();
        }
        else if (this.format.compareToIgnoreCase(ResponderMethodTypes.SYMPHONY_API.toString()) == 0)
        {
            return new FlatCustomerFormatter();
        }
        // You __can__ get a customer from the polaris SQL request but it's just a stub now. Use SIP2.
        else if (this.format.compareToIgnoreCase(ResponderMethodTypes.POLARIS_SQL.toString()) == 0)
        {
            return new PolarisSQLCustomerFormatter();
        }
        ///////////// TODO: fix so it returns something reasonable OR other formatters for consistency.
        // BImport doesn't have a formatter; neither does dummy, so if you are asking for one
        // there is a problem with the call you are making.
        throw new DummyException(DummyRequestBuilder.class.getName() 
                + " the is no formatter defined for " + format 
                + "' which was specified in debug.properties.");
    }
    
    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response)
    {
        return getConfiguredResonse(null, response);
    }

    /**
     *
     * @param commandType the value of commandType
     * @param status the value of status
     * @param response the value of response
     * @return true if the command succeeded and false otherwise.
     */
    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response)
    {
        boolean result = false;
        // what does it mean to interpret results that were canned responses in the first place?
        if (this.commandStatus == 0) 
        {
            response.setCode(ResponseTypes.SUCCESS);
            response.setResponse(status.getStdout());
            result = true;
        }
        else
        {
            response.setCode(ResponseTypes.FAIL);
            response.setResponse(status.getStderr());
            result = false;
        }
        return result;
    }
    
    @Override
    public Command getStatusCommand(Response response)
    {
        return getConfiguredResonse(null, response);
    }

    @Override
    public Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        return getConfiguredResonse(customer, response);
    }
    
    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        return getConfiguredResonse(customer, response);
    }
    
    @Override
    public boolean tidy()
    {
        return true;
    }
    
    /**
     * Gets the requested command from debug.properties.
     * @param customer
     * @param response
     * @return Command with canned responses in it. When the command is executed
     * it does not actually run anything on the server.
     */
    protected Command getConfiguredResonse(Customer customer, Response response)
    {
        Command command = new DummyCommand.Builder()
                .setStatus(this.commandStatus)
                .setStdout(this.stdout)
                .setStderr(this.stderr)
                .build();
        return command;
    }

    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        // You could respond by creating a new canned CustomerMessage type.
        throw new UnsupportedOperationException("Not supported in Dummy command.");
    }

    @Override
    public Command testCustomerExists(
            String userId, 
            String userPin, 
            Response response) 
    {
        return getConfiguredResonse(null, response);
    }
}
