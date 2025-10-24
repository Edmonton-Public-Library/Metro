/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022 - 2025  Edmonton Public Library
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
import java.io.File;
import mecard.Response;
import mecard.QueryTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.customer.Customer;
import mecard.exception.UnsupportedCommandException;
import mecard.config.PropertyReader;
import mecard.sip.SIPToMeCardCustomer;
import site.CustomerLoadNormalizer;
import mecard.customer.NativeFormatToMeCardCustomer;

/**
 * ILSRequestBuilder outlines the contract of methods the Responder needs to 
 * register customers. Request builders are bespoke ILS adapters. Each is crafted
 * to use the lingua-franca of the node library's ILS or web service, as well as negotiating
 * any library-specific business policies or environment issues. Fulfilling a 
 * registration request may take several steps all of which the request builder
 * does, as well as testing the results of each step, before passing the final
 * command to the responder to execute and test.
 * 
 * Note: Programmers implementing a new builder will probably not be able to 
 * use all these methods. Symphony for instance, does not have a good facility 
 * to get the ILS status. In that case sub-class the ILSRequestAdaptor, and make 
 * sure you don't specify that method of response in the environment.properties
 * file.
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public abstract class ILSRequestBuilder
{
    // used by failed customer files to determine ILS
    // agnostically, where customers files are loaded from and stored.
    protected String loadDir = "."; // Don't forget to set this in constructor of RequestBuilder.
    
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
            case CREATE_CUSTOMER -> serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.CREATE_SERVICE.toString());
            case TEST_CUSTOMER -> // Entry for environment.properties.
                // <entry key="exists-protocol">polaris-api</entry>
                // If the environment.properties file doesn't contain an
                // entry then the default of the LibraryPropertyTypes.GET_SERVICE
                // will be used.
                serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.EXISTS_SERVICE.toString(),
                        LibraryPropertyTypes.GET_SERVICE.toString());
            case GET_CUSTOMER -> serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.GET_SERVICE.toString());
            case GET_STATUS -> serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.STATUS_SERVICE.toString());
            case UPDATE_CUSTOMER -> serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.UPDATE_SERVICE.toString());
            default -> throw new UnsupportedCommandException( 
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
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.CALGARY_API.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'CALGARY-API' ");
            return new CPLapiRequestBuilder(debug);
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
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.SIRSIDYNIX_API.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'SIRSIDYNIX_API' ");
            return new SDapiRequestBuilder(debug);
        }
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.DEBUG.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'DEBUG' (dummy) ");
            return new DummyRequestBuilder(debug);
        }
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.OUTAGE.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'OUTAGE' (outage) ");
            return new OutageRequestBuilder(debug);
        }
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.POLARIS_API.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'POLARIS_API' ");
            return new PapiRequestBuilder(debug);
        }
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.POLARIS_SQL.toString()))
        {
            if (debug) System.out.println(ILSRequestBuilder.class.getName() + " MAP: 'POLARIS_SQL' ");
            return new PolarisSQLRequestBuilder(debug);
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
     * {@link SIPToMeCardCustomer} for additional customized SIP2 result interpretation.
     * @return NativeFormatToMeCardCustomer.
     */
    public abstract NativeFormatToMeCardCustomer getFormatter();

    /**
     * Implementers promise to return a APICommand that, when run, will return the
     * customer's information.
     *
     * @param userId the value of userId
     * @param userPin the value of userPin
     * @param response Buffer to contain useful response information.
     * @return Command for execution of this query type.
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
    
    /** Gets a command that tests if a customer can be found at the indicated
     * library system.
     * @param userId
     * @param userPin
     * @param response
     * @return Command object that when executed, will answer the question.
     */
    public abstract Command testCustomerExists(
            String userId, 
            String userPin, 
            Response response
    );
    
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
    
    /**
     * This method is used for lost cards which get output to the customer load
     * directory. This directory is located in each ILS property file as a load-dir. 
     * Because the lost user has no insight into which ILS is being used the ILSBuilder
     * must be able to signal the failed customer files with the load directory.
     * Automatically adds trailing file separator if required.
     * @return string of load directory.
     */
    public String getCustomerLoadDirectory()
    {
        if (loadDir.endsWith(File.separator))
        {
            return loadDir;
        }
        return loadDir + File.separator;
    }
}
