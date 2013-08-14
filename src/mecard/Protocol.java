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
package mecard;

import json.RequestDeserializer;
import json.ResponseSerializer;
import mecard.exception.MetroSecurityException;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.security.SecurityManager;

/**
 * Responsible for interpreting incoming MeCard requests.
 *
 * Requests are sent as JSON strings. All requests must include the API key or a 
 * MetroSecurityException is thrown.
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class Protocol
{
    public final static String DEFAULT_FIELD = "X";
    public final static String TRUE        = "Y";
    public final static String FALSE       = "N";
    public final static String TERMINATE   = "XX0";
    public final static String ACKNOWLEDGE = "XK0";
    public final static String ERROR       = "XE0";
    
    private boolean debugMode;
    
    
    public Protocol()
    { 
        String debug = MetroService.getProperties(ConfigFileTypes.ENVIRONMENT)
                .getProperty(LibraryPropertyTypes.DEBUG.toString());
        debugMode = Boolean.valueOf(debug);
    }

    /**
     * Checks incoming commands to the server, parses what the command, its 
     * security token and if valid then runs the required activity.
     *
     * @param cmd command string request.
     * @return formatted response string.
     */
    public String processInput(String cmd)
    {
        Response response = new Response();
        try
        {
            String jsonCommand = SecurityManager.unEncrypt(cmd);
            RequestDeserializer deserializer = new RequestDeserializer();
            Request request = deserializer.getDeserializedRequest(jsonCommand);
            if (! SecurityManager.isAuthorized(request.getTransactionId()))
            {
                throw new MetroSecurityException("Unrecognized security token");
            }
//            Responder responder = getResponder(request);
            Responder responder = new Responder(request, this.debugMode);
            response = responder.getResponse();
        }
        catch (RuntimeException ex)
        {
            response = Responder.getExceptionResponse(ex);
        }
        finally
        {
            ResponseSerializer responseSerializer = new ResponseSerializer();
            String serializedResponse = responseSerializer.getSerializedResponse(response);
            return SecurityManager.encrypt(serializedResponse);
        }
    }

    /**
     * Gets the responder based on the argument request.
     * @param command the value of command
     * @return Responder appropriate for answering the request as outlined in 
     * the environment.properties file.
     * @see Request
     */
//    protected Responder getResponder(Request command)
//            throws UnsupportedCommandException, UnsupportedResponderException, MalformedCommandException
//    {
//        QueryTypes queryType = command.getCommandType();
//        String serviceType;
//        switch (queryType)
//        {
//            case CREATE_CUSTOMER:
//                serviceType = MetroService.
//                    getProperties(ConfigFileTypes.ENVIRONMENT).
//                    getProperty(LibraryPropertyTypes.CREATE_SERVICE.toString());
//                return mapResponderType(serviceType, command);
//            case UPDATE_CUSTOMER:
//                serviceType = MetroService.
//                    getProperties(ConfigFileTypes.ENVIRONMENT).
//                    getProperty(LibraryPropertyTypes.UPDATE_SERVICE.toString());
//                return mapResponderType(serviceType, command);
//            case GET_STATUS:
//                serviceType = MetroService.
//                    getProperties(ConfigFileTypes.ENVIRONMENT).
//                    getProperty(LibraryPropertyTypes.STATUS_SERVICE.toString());
//                return mapResponderType(serviceType, command);
//            case GET_CUSTOMER:
//                serviceType = MetroService.
//                    getProperties(ConfigFileTypes.ENVIRONMENT).
//                    getProperty(LibraryPropertyTypes.GET_SERVICE.toString());
//                return mapResponderType(serviceType, command);
//            default:
//                throw new UnsupportedCommandException(); 
//        }
//    }
    
    /**
     * Creates the appropriate responder based on what string value was entered
     * in the environment configuration file.
     *
     * @param configRequestedService service requested in environment.properties file.
     * @param command the request sent by the metro web site - a JSON string.
     * @return Responder requested in environment.properties files.
     * @throws UnsupportedCommandException if the requested responder is not available
     * because it hasn't been implemented yet, or just because there is a spelling
     * mistake in the environment.properties file wrt the Responder method types.
     * @see ResponderMethodTypes
     */
    
//    private Responder mapResponderType(
//            String configRequestedService, Request command)
//        throws UnsupportedCommandException
//    {
//        if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.BIMPORT.toString()))
//        {
//            return new BImportResponder(command, debugMode);
//        }
//        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.SIP2.toString()))
//        {
//            return new SIP2Responder(command, debugMode);
//        }
//        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.LOCAL_CALL.toString()))
//        {
//            return new APIResponder(command, debugMode);
//        }
//        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.DEBUG.toString()))
//        {
//            return new DummyResponder(command, debugMode);
//        }
//        else
//        {
//            throw new UnsupportedResponderException(configRequestedService + 
//                    " can't respond to request " + 
//                    command.getCommandType().name());
//        }
//    }
}
