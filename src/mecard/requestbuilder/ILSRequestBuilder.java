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
import mecard.Response;
import mecard.QueryTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.exception.UnsupportedCommandException;
import mecard.config.PropertyReader;
import mecard.customer.SIPFormatter;
import site.CustomerLoadNormalizer;

/**
 * ILSRequestBuilder outlines the contract that all implementers promise to fulfill.
 * Note: Programmers implementing a new builder will probably not be able to 
 * use all these methods. Symphony for instance, does not have a good facility 
 * to get the ILS status. In that case sub-class the ILSRequestAdaptor, and make 
 * sure you don't specify that method of response in the environment.properties
 * file.
 * @author andrew
 */
public abstract class ILSRequestBuilder
{
    /**
     *
     *
     * @param queryType the value of queryType
     * @param debug the value of debug
     * @return ILSRequestBuilder appropriate for the query.
     */
    public static ILSRequestBuilder getInstanceOf(QueryTypes queryType, boolean debug)
    {
        // Here we read the properties file to determine what type of request
        // builder we need. It will be based on what type of request we have.
        String serviceType = "";
        switch (queryType)
        {
            case CREATE_CUSTOMER:
                serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.CREATE_SERVICE.toString());
                break;
            case GET_CUSTOMER:
                serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.GET_SERVICE.toString());
                break;
            case GET_STATUS:
                serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.STATUS_SERVICE.toString());
                break;
            case UPDATE_CUSTOMER:
                serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.UPDATE_SERVICE.toString());
                break;
            default:
                throw new UnsupportedCommandException( 
                    " can't respond to request '" + queryType.name() + "'");
        }
        if (debug)
        {
            System.out.println(ILSRequestBuilder.class.getName()
                    + " ILS: '" + serviceType + "' ");
        }
        return mapBuilderType(serviceType, debug);
    }
    
    /**
     * Creates the appropriate responder based on what string value was entered
     * in the environment configuration file.
     *
     * @param debug the value of debug
     * @return RequestBuilder requested in environment.properties files.
     * @throws UnsupportedCommandException if the requested responder is not available
     * because it hasn't been implemented yet, or just because there is a spelling
     * mistake in the environment.properties file wrt the Responder method types.
     * @see ResponderMethodTypes
     */
    private static ILSRequestBuilder mapBuilderType(
            String configRequestedService, boolean debug)
        throws UnsupportedCommandException
    {
        if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.BIMPORT.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'BIMPORT' ");
            return new BImportRequestBuilder(debug);
        }
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.SIP2.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'SIP2' ");
            return new SIPRequestBuilder(debug);
        }
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.SYMPHONY_API.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'SYMPHONY_API' ");
            return new SymphonyRequestBuilder(debug);
        }
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.DEBUG.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'DEBUG' (dummy) ");
            return new DummyRequestBuilder(debug);
        }
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.POLARIS_API.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'POLARIS_API' ");
            return new PAPIRequestBuilder(debug);
        }
        else
        {
            throw new UnsupportedCommandException(configRequestedService + 
                    " can't respond to request ");
        }
    }
    
    /**
     * Gets the CustomerFormatter related to the implementer of the subclass.
     * A formatter can be tailored to a specific library to ensure proper 
     * interpretation of request results, and non-standard field use. For example;
     * consider that EPL uses the field "PF" to designate patron sex, but Shortgrass
     * uses the field 'PF'. That being the case both libraries can subclass a 
     * {@link SIPFormatter} for additional customized SIP2 result interpretation.
     * @return CustomerFormatter.
     */
    public abstract CustomerFormatter getFormatter();

    /**
     * Implementers promise to return a APICommand that, when run, will return the
     * customer's information.
     *
     * @param userId the value of userId
     * @param userPin the value of userPin
     * @param response Buffer to contain useful response information.
<<<<<<< HEAD
     * @return Command ready to run.
=======
     * @return Command for execution of this query type.
>>>>>>> master
     */
    public abstract Command getCustomerCommand(
            String userId, 
            String userPin, 
            Response response
    );
    
    /**
     * Creates a user based on the supplied customer, which must not be null.
     *
     * @param customer
     * @param response
     * @param normalizer the value of normalizer
     * @return command that can be executed on the ILS to create a customer.
     */
    public abstract Command getCreateUserCommand(
            Customer customer, 
            Response response, 
            CustomerLoadNormalizer normalizer
    );
 

    /**
     * Updates a user based on the supplied customer, which must not be null.
     *
     * @param customer
     * @param response
     * @param normalizer the value of normalizer
     * @return command that can be executed on the ILS to update a customer.
     */
    public abstract Command getUpdateUserCommand(
            Customer customer, 
            Response response, 
            CustomerLoadNormalizer normalizer
    );

    /**
     * Gets the status of the ILS.
     *
     * @param response
     * @return APICommand necessary to test the ILS status.
     */
    public abstract Command getStatusCommand(Response response);
    
    /**
     * Interprets the results of the ILS command into a meaningful message for
     * the customer.
     *
     * @param commandType the value of commandType
     * @param status the status of the command that ran on the ILS.
     * @param response the object to be returned to melibraries.ca.
     * @return true if the operation was successful, and false otherwise.
     */
    public abstract boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response);
    
    /**
     * Gives the builder opportunity to clean up leftover temporary files or other
     * operations.
     * @return true if the tidy operation was successful and false otherwise.
     */
    public abstract boolean tidy();

    /**
     * Retrieves the customer message object for analysis in a service-agnostic format.
     * @param stdout The data returned from getCustomer request as output from the ILS.
     * @return customer data from the original source in a universally query-able form.
     */
    public abstract CustomerMessage getCustomerMessage(String stdout);
}
