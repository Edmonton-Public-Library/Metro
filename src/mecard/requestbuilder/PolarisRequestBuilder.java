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
package mecard.requestbuilder;

import api.Command;
import api.CommandStatus;
import mecard.QueryTypes;
import mecard.Response;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.PolarisJSONCustomerFormatter;
import site.CustomerLoadNormalizer;

/**
 * Polaris has a restful web service. 
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PolarisRequestBuilder extends ILSRequestBuilder
{

    PolarisRequestBuilder(boolean debug)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Command getStatusCommand(Response response)
    {
        // Intentionally left blank should use the SIP2 request.
        throw new UnsupportedOperationException("Not supported. Use SIP2 in configuration."); 
    }
    
    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response)
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CustomerFormatter getFormatter()
    {
        return new PolarisJSONCustomerFormatter();
    }

    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean tidy()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
