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
package mecard.requestbuilder;

import api.CommandStatus;
import mecard.Response;
import java.util.Properties;
import json.ResponseDeserializer;
import mecard.MetroService;
import mecard.QueryTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.DebugQueryConfigTypes;
import mecard.customer.CustomerFormatter;

/**
 *
 * @author Andrew Nisbet
 */
public class DummyRequestBuilder extends ILSRequestBuilder
{
    private final String gsonStatus;
    private final String gsonGetCustomer;
    private final String gsonCreateCustomer;
    private final String gsonUpdateCustomer;
    private final String altData;
    
    DummyRequestBuilder(boolean debug)
    {
        Properties props = MetroService.getProperties(ConfigFileTypes.DEBUG);
        gsonStatus = props.getProperty(DebugQueryConfigTypes.GET_STATUS.toString());
        gsonGetCustomer = props.getProperty(DebugQueryConfigTypes.GET_CUSTOMER.toString());
        gsonCreateCustomer = props.getProperty(DebugQueryConfigTypes.CREATE_CUSTOMER.toString());
        gsonUpdateCustomer = props.getProperty(DebugQueryConfigTypes.UPDATE_CUSTOMER.toString());
        altData = props.getProperty(DebugQueryConfigTypes.ALT_DATA_CUSTOMER.toString());
    }

    @Override
    public CustomerFormatter getFormatter()
    {
        throw new UnsupportedOperationException(DummyRequestBuilder.class.getName() 
                + " does not require formatter.");
    }

    @Override
    public void interpretResults(QueryTypes commandType, CommandStatus status, Response response)
    {
        throw new UnsupportedOperationException(DummyRequestBuilder.class.getName() 
                + " does not require results to be interpreted.");
    }
    
    public void getILSStatus(Response response)
    {
        ResponseDeserializer deserializer = new ResponseDeserializer();
        Response r = deserializer.getDeserializedResponse(this.gsonStatus);
        response.setCode(r.getCode());
        response.setResponse(r.getMessage());
        response.setCustomer(r.getCustomer());
    }

    public void getCustomer(Response response)
    {
        ResponseDeserializer deserializer = new ResponseDeserializer();
        Response r = deserializer.getDeserializedResponse(this.gsonGetCustomer);
        response.setCode(r.getCode());
        response.setResponse(r.getMessage());
        response.setCustomer(r.getCustomer());
    }
    
    public void updateCustomer(Response response)
    {
        ResponseDeserializer deserializer = new ResponseDeserializer();
        Response r = deserializer.getDeserializedResponse(this.gsonUpdateCustomer);
        response.setCode(r.getCode());
        response.setResponse(r.getMessage());
        response.setCustomer(r.getCustomer());
    }

    public void createCustomer(Response response)
    {
        ResponseDeserializer deserializer = new ResponseDeserializer();
        Response r = deserializer.getDeserializedResponse(this.gsonCreateCustomer);
        response.setCode(r.getCode());
        response.setResponse(r.getMessage());
        response.setCustomer(r.getCustomer());
    }
}
