/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2021  Edmonton Public Library
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
package mecard.config;

import java.util.Properties;
import mecard.exception.ConfigurationException;
import mecard.util.Text;

/**
 * Determines what type of ILS is being used based on the types of protocols 
 * used in environment.properties.
 * 
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 * @since 1.1.0
 */
public class ILS 
{
    public enum IlsType
    {
        SIRSI_DYNIX("SIRSI_DYNIX"),
        SYMPHONY("SYMPHONY"),
        HORIZON("HORIZON"),
        POLARIS("POLARIS"),
        UNKNOWN("UNKNOWN");
        
        private final String type;

        private IlsType(String s)
        {
            this.type = s;
        }

        @Override
        public String toString()
        {
            return this.type;
        }
    }
    
    public final IlsType ILS_TYPE;
    // optional field in the sip2.properties file.
    public final static String SIP_ILS_TYPE_TAG = "ils-type";  
    
    /**
     * Determines the type of ILS MeCard connects to by checking the sip2.properties
     * file for an entry called {@link #SIP_ILS_TYPE_TAG}. If there is not a tag
     * of that type in the sip2.properties, then it looks in the 
     * environment.properties file for the 
     * {@link LibraryPropertyTypes}.CREATE_SERVICE tag. If that fails a
     * {@link ConfigurationException} exception is thrown.
     */
    public ILS()
    {
        Properties sip2Properties = PropertyReader.getProperties(ConfigFileTypes.SIP2);
        // get the value from the properties file, but if not found assume SIRSI_DYNIX 
        // which will set the customerMessage type to a standard SIPCustomerMessage().
        String whichIls = sip2Properties.getProperty(SIP_ILS_TYPE_TAG);
        IlsType myType = IlsType.UNKNOWN;;
        try
        {
            // which Ils can still be empty if a tag was entered but it was empty.
            if (whichIls == null || whichIls.isEmpty() 
                    || 0 == whichIls.compareToIgnoreCase(IlsType.SIRSI_DYNIX.toString()))
            {
                // we need to go to the environment and make educated guesses based
                // on the 'create-protocol' tag in the environment.properties.
                Properties envProperties = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
                // get the value from the properties file, but if not found assume SIRSI_DYNIX 
                // which will set the customerMessage type to a standard SIPCustomerMessage().
                String createProtocolStr = envProperties.getProperty(LibraryPropertyTypes.CREATE_SERVICE.toString());
                whichIls = Text.getUpTo(createProtocolStr, "-").toUpperCase();
            }
        
            myType = IlsType.valueOf(whichIls.toUpperCase());
        }
        catch (NullPointerException | IllegalArgumentException ex)
        {
            System.err.println(" **Failed to find ILS type '" 
                + "' in environment.properties or sip2.properties. Try adding "
                + "'<entry key=\"ils-type\">[ILS_NAME]</entry>' to "
                    + "sip2.properties file. For example add 'HORIZON' if "
                    + "your ILS is a Horizon system. Valid values are "
                    + "SYMPHONY, HORIZON, POLARIS and SIRSI_DYNIX.");
        }
        finally
        {
            ILS_TYPE = myType;
        }
    }
    
    /**
     * 
     * @return the String value of the IlsType.
     */
    @Override
    public String toString()
    {
        return ILS_TYPE.toString();
    }
    
    /**
     * 
     * @return The enum type of ILS MeCard works with.
     */
    public IlsType getILSType()
    {
        return this.ILS_TYPE;
    }
}
