/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
import java.util.Properties;
import mecard.QueryTypes;
import mecard.Response;
import mecard.config.ConfigFileTypes;
import mecard.config.MessagesTypes;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import site.CustomerLoadNormalizer;
import mecard.customer.NativeFormatToMeCardCustomer;

/**
 * Outage request is used if one of the MeCard services is unavailable because
 * of a planned outage.
 * 
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class OutageRequestBuilder extends ILSRequestBuilder 
{
    private final boolean debug;
    private final Properties messages;
    public OutageRequestBuilder(boolean debug)
    {
        this.debug = debug;
        this.messages = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
    }

    @Override
    public NativeFormatToMeCardCustomer getFormatter() {
        throw new UnsupportedOperationException("Not supported during outage.");
    }

    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response) 
    {
        return new DummyCommand.Builder()
            .setStatus(1)
            .setStderr(messages.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()))
            .build();
    }

    @Override
    public Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer) 
    {
        return new DummyCommand.Builder()
            .setStatus(1)
            .setStderr(messages.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()))
            .build(); 
    }

    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer) 
    {
        return new DummyCommand.Builder()
            .setStatus(1)
            .setStderr(messages.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()))
            .build();
    }

    @Override
    public Command getStatusCommand(Response response) 
    {
        return new DummyCommand.Builder()
            .setStatus(1)
            .setStderr(messages.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()))
            .build(); 
    }

    @Override
    public Command testCustomerExists(String userId, String userPin, Response response) 
    {
        return new DummyCommand.Builder()
            .setStatus(1)
            .setStderr(messages.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()))
            .build();
    }

    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response) 
    {
        return false;
    }

    @Override
    public boolean tidy() 
    {
        return true;
    }

    @Override
    public CustomerMessage getCustomerMessage(String stdout) 
    {
        throw new UnsupportedOperationException("Not supported in during outage.");
    }
}
