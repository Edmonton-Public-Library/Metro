/*
 * Copyright (C) 2013 metro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mecard;

import mecard.Exception.UnsupportedCommandException;
import mecard.Exception.MalformedCommandException;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import mecard.responder.BImportResponder;
import mecard.responder.ResponderMethodTypes;
import mecard.responder.ResponderStrategy;
import mecard.responder.SIP2Responder;
import mecard.responder.APIResponder;
import mecard.security.SecurityManager;

/**
 * Responsible for interpreting incoming MeCard requests.
 *
 * The language is loosely based on SIP but it IS NOT SIP in any way. The
 * command structure has pipe-delimited fields. Each command is terminated with
 * a '\r' character, typically:
 * <code>QA0|dfae434324354asdfa344|</code><br/>
 * The second field is a MD5 computed hash of the command, salted with a shared
 * secret, to ensure the authenticity of the request.
 *
 * @author metro
 */
public class Protocol
{
    public final static String DELIMITER   = "|";
    public final static String TERMINATE   = "XX0" + DELIMITER;
    public final static String ACKNOWLEDGE = "XK0" + DELIMITER;
    public final static String ERROR       = "XE0" + DELIMITER;

    public Protocol()
    {  }

    /**
     * Checks incoming commands to the server, parses what the command is and
     * then runs the required activity.
     *
     * @param cmd
     * @return formatted response string.
     */
    public String processInput(String cmd)
    {
        String command = SecurityManager.unEncrypt(cmd);
        ResponderStrategy responder = getResponder(command);
        String response = responder.getResponse();
        return SecurityManager.encrypt(response);
    }

    public static QueryTypes getCommand(String cmd)
            throws MalformedCommandException, UnsupportedCommandException
    {
        if (cmd == null || cmd.length() == 0)
        {
            throw new MalformedCommandException("The received command is empty or null.");
        }
        for (QueryTypes qType : QueryTypes.values())
        {
            if (cmd.startsWith(qType.toString()))
            {
                return qType;
            }
        }
        throw new UnsupportedCommandException();
    }

    /**
     *
     *
     * @param command the value of command
     */
    protected ResponderStrategy getResponder(String command)
    {
        QueryTypes queryType = getCommand(command);
        String serviceType = "";
        switch (queryType)
        {
            case CREATE_CUSTOMER:
                serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.CREATE_SERVICE.toString());
                return mapResponderType(serviceType, command);
            case UPDATE_CUSTOMER:
                serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.UPDATE_SERVICE.toString());
                return mapResponderType(serviceType, command);
            case GET_STATUS:
                serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.STATUS_SERVICE.toString());
                return mapResponderType(serviceType, command);
            case GET_CUSTOMER:
                serviceType = PropertyReader.
                    getProperties(ConfigFileTypes.ENVIRONMENT).
                    getProperty(LibraryPropertyTypes.GET_SERVICE.toString());
                return mapResponderType(serviceType, command);
            default:
                throw new UnsupportedCommandException(); 
        }
    }
    
    /**
     * Creates the appropriate responder based on what string value was entered
     * in the environment configuration file.
     * @param configRequestedService
     * @param command
     * @return
     * @throws UnsupportedCommandException 
     */
    private ResponderStrategy mapResponderType(
            String configRequestedService,
            String command)
        throws UnsupportedCommandException
    {
        if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.BIMPORT.toString()))
        {
            return new BImportResponder(command);
        }
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.SIP2.toString()))
        {
            return new SIP2Responder(command);
        }
        else if (configRequestedService.equalsIgnoreCase(ResponderMethodTypes.LOCAL_CALL.toString()))
        {
            return new APIResponder(command);
        }
        else
        {
            throw new UnsupportedCommandException();
        }
    }
}
