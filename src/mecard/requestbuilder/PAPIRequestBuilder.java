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
import api.PAPIXMLCustomerMessage;
import java.util.Properties;
import mecard.QueryTypes;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PAPIPropertyTypes;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.polaris.PAPICustomerFormatter;
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
    private final String authenticationDomain;
    private final String authenticationUserName;
    private final String authenticationPassword;
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
        this.messageProperties      = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        Properties papiProps        = PropertyReader.getProperties(ConfigFileTypes.PAPI);
        this.host                   = papiProps.getProperty(PAPIPropertyTypes.HOST.toString());
        this.authenticationDomain   = papiProps.getProperty(PAPIPropertyTypes.AUTHENTICATE_DOMAIN.toString());
        this.authenticationUserName = papiProps.getProperty(PAPIPropertyTypes.AUTHENTICATE_USERNAME.toString());
        this.authenticationPassword = papiProps.getProperty(PAPIPropertyTypes.AUTHENTICATE_PASSWORD.toString());
        this.patronBranchId         = papiProps.getProperty(PAPIPropertyTypes.PATRON_BRANCH_ID.toString());
        this.version                = papiProps.getProperty(PAPIPropertyTypes.VERSION.toString());
        this.languageId             = papiProps.getProperty(PAPIPropertyTypes.LANGUAGE_ID.toString());
        this.appId                  = papiProps.getProperty(PAPIPropertyTypes.APP_ID.toString());
        this.orgId                  = papiProps.getProperty(PAPIPropertyTypes.ORG_ID.toString());
    }
    
    /**
     * Gets the base URL for PAPI requests.
     * Example: http://207.167.28.31/PAPIService/REST/public/v1/1033/100/1/patron/29335002291059/messages
     * 
     * @return URL string like:http://host/PAPIServie/REST/public/v1/1033/100/1/patron
     */
     @Override
    public final Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
//        TODO: 'PWS [PAPIAccessKeyID]:[Signature]'
//     * See pg. 69 HTTP Pocket Reference O'Reilly.
        // Here we will need to authenticate then 
//        Command patronRegistrationCreate = new WebServiceCommand.Builder()
//                .setURL(this.getUrlBase())
//                .setLoginBranchId(this.authenticationDomain)
//                .setLoginUserId(this.authenticationUserName)
//                .setLoginStationId(this.authenticationPassword)
//                .setHTTPVerb(POST)
//                .build();
        // TODO: not going to use WServices for this now so just stubbed. Stop the user from selecting though.
        throw new ConfigurationException("Function not supported yet "
                + "Please review your environment.properties configuration"); 
    }
    
    @Override
    public final Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        // http://host/PAPIServie/REST/public/v1/1033/100/1/patron/21221012345678
        // and
        // http://207.167.28.31/PAPIService/REST/public/v1/1033/100/1/patron/29335002291059/messages
//        Command patronAccountUpdate = new WebServiceCommand.Builder()
//                .setURL(this.getUrlBase() + "/" + customer.get(CustomerFieldTypes.ID))
//                .setLoginBranchId(this.authenticationDomain)
//                .setLoginUserId(this.authenticationUserName)
//                .setLoginStationId(this.authenticationPassword)
//                .setHTTPVerb(POST)
//                .build();
        // TODO: not going to use WServices for this now so just stubbed. Stop the user from selecting though.
        throw new ConfigurationException("Function not supported yet "
                + "Please review your environment.properties configuration");
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
        throw new ConfigurationException("Polaris does not support account queries "
                + "Please review your environment.properties configuration"); 
    }
    
    @Override
    public final CustomerFormatter getFormatter()
    {
        return new PAPICustomerFormatter(); // TODO finish this class
    }

    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response)
    {
        // TODO instead let's parse the returning XML message in a new object type.
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
        return true;
    }
    
    @Override
    public String toString()
    {
        return "";
    }

    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        // TODO this will be an XML customer response message. Since each ILS 
        // that returns XML customer content will be different.
        return new PAPIXMLCustomerMessage(stdout, true);
    }

    @Override
    public Command testCustomerExists(
            String userId, 
            String userPin, 
            Response response) 
    {
        return getCustomerCommand(userId, userPin, response);
    }
}
