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
import api.CustomerMessage;
import api.HTTPCommandStatus;
import api.PAPICommand;
import java.util.Properties;
import mecard.QueryTypes;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PolarisPropertyTypes;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.PapiJSONCustomerFormatter;
import mecard.exception.ConfigurationException;
import site.CustomerLoadNormalizer;

/**
 * This class supports creating commands through Polaris API or PAPI.
 * Polaris uses a restful web service for all ILS transactions. 
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PAPIRequestBuilder extends ILSRequestBuilder
{
    private static String POST = "POST";
    private final Properties messageProperties;
    private final String host;
    private final String loginBranchId;
    private final String loginUserId;
    private final String loginStationId;
    private final String patronBranchId;
    private final String version;
    private final String languageId;
    private final String appId;
    private final String orgId;
    public final static String URL_PREFIX = "PAPIService/REST/public";
    public final static String URL_SUFFIX = "patron";
    
    PAPIRequestBuilder(boolean debug)
    {
        // read all the properties from the Polaris table
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        Properties papiProps   = PropertyReader.getProperties(ConfigFileTypes.POLARIS);
        this.host              = papiProps.getProperty(PolarisPropertyTypes.HOST.toString());
        this.loginBranchId     = papiProps.getProperty(PolarisPropertyTypes.LOGIN_BRANCH_ID.toString());
        this.loginUserId       = papiProps.getProperty(PolarisPropertyTypes.LOGIN_USER_ID.toString());
        this.loginStationId    = papiProps.getProperty(PolarisPropertyTypes.LOGIN_WORKSTATION_ID.toString());
        this.patronBranchId    = papiProps.getProperty(PolarisPropertyTypes.PATRON_BRANCH_ID.toString());
        this.version           = papiProps.getProperty(PolarisPropertyTypes.VERSION.toString());
        this.languageId        = papiProps.getProperty(PolarisPropertyTypes.LANGUAGE_ID.toString());
        this.appId             = papiProps.getProperty(PolarisPropertyTypes.APP_ID.toString());
        this.orgId             = papiProps.getProperty(PolarisPropertyTypes.ORG_ID.toString());
    }
    
    /**
     * Gets the base URL for PAPI requests.
     * @return URL string like:http://host/PAPIServie/REST/public/v1/1033/100/1/patron
     */
    private String getUrlBase()
    {
        // looks like:
        // http://host/PAPIServie/REST/public/v1/1033/100/1/patron
        return "http://" + 
                this.host + "/" +
                URL_PREFIX + "/" +
                this.version + "/" +
                this.languageId + "/" +
                this.appId + "/" +
                this.orgId + "/" +
                URL_SUFFIX;
    }
    
     @Override
    public final Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        Command patronRegistrationCreate = new PAPICommand.Builder()
                .setURL(this.getUrlBase())
                .setLoginBranchId(this.loginBranchId)
                .setLoginUserId(this.loginUserId)
                .setLoginStationId(this.loginStationId)
                .setHTTPVerb(POST)
                .build();
        return patronRegistrationCreate; 
    }
    
    @Override
    public final Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        // http://host/PAPIServie/REST/public/v1/1033/100/1/patron/21221012345678
        Command patronAccountUpdate = new PAPICommand.Builder()
                .setURL(this.getUrlBase() + "/" + customer.get(CustomerFieldTypes.ID))
                .setLoginBranchId(this.loginBranchId)
                .setLoginUserId(this.loginUserId)
                .setLoginStationId(this.loginStationId)
                .setHTTPVerb(POST)
                .build();
        return patronAccountUpdate;
    }
    
    @Override
    public final Command getStatusCommand(Response response)
    {
        // Intentionally left blank should use the SIP2 request.
        throw new ConfigurationException("Polaris does not support ILS status queries "
                + "Please review your environment.properties configuration"); 
    }
    
    @Override
    public final Command getCustomerCommand(String userId, String userPin, Response response)
    {
        // Intentionally left blank should use the SIP2 request.
        throw new ConfigurationException("Polaris does not support accout queries "
                + "Please review your environment.properties configuration"); 
    }
    
    @Override
    public final CustomerFormatter getFormatter()
    {
        return new PapiJSONCustomerFormatter(); // TODO finish this class
    }

    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response)
    {
        // TODO PAPIRequestBuilder.isSuccessful() can check on the error code!
        ResponseTypes responseType = status.getStatus();
        boolean result = false;
        switch(responseType)
        {
            case SUCCESS:
                result = true;
            default:
                result = false;
                break;
        }
        return result;
    }

    @Override
    public boolean tidy()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString()
    {
        return this.getUrlBase();
    }

    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
