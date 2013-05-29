/**
 *
 * This class is part of the Metro, MeCard project. Copyright (C) 2013 Andrew
 * Nisbet, Edmonton public Library.
 * 
* This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
* This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
* You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
*/
package mecard.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author metro
 */
public class PropertyReader
{

    public final static String BIMPORT_PROPERTY_FILE = "bimport_config.xml";
    public final static String DEFAULT_CREATE_PROPERTY_FILE = "default_create_config.xml";
    public final static String ENVIRONMENT_FILE = "env_config.xml";
    public final static String SIP2_FILE = "sip2_config.xml";
    public final static String API_FILE = "api_config.xml";
    private static Properties defaultPolicies = PropertyReader.getProperties(ConfigFileTypes.DEFAULT_CREATE);
    private static Properties bimport; // don't read by default since this is optional.
    private static Properties environment = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
    private static Properties sip2; // Optional config file.
    private static Properties api; // Optional config file.

    private PropertyReader()
    {
    }

    public static Properties getProperties(ConfigFileTypes type)
    {

        switch (type)
        {
            case DEFAULT_CREATE: // Additional properties that are given to a customer by default at creation time.
                if (defaultPolicies == null)
                {
                    try
                    {
                        defaultPolicies = readProperties(PropertyReader.DEFAULT_CREATE_PROPERTY_FILE);
                    }
                    catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + PropertyReader.DEFAULT_CREATE_PROPERTY_FILE
                                + "'. It is required to message customers of important events.";
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, ex);
                    }
                    catch (NullPointerException npe)
                    {
                        String msg = "Failed to read customer creation default config file. One must be defined.";
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                }
                return defaultPolicies;
            case ENVIRONMENT:
                if (environment == null)
                {
                    try
                    {
                        environment = readProperties(PropertyReader.ENVIRONMENT_FILE);
                    }
                    catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + PropertyReader.ENVIRONMENT_FILE
                                + "'. It may be empty but one must be defined to continue.";
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, ex);
                    }
                    catch (NullPointerException npe)
                    {
                        String msg = "Failed to read environment config file. One must be defined.";
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                    // now check that all mandetory values are here.
                    for (LibraryPropertyTypes lType : LibraryPropertyTypes.values())
                    {
                        if (environment.get(lType.toString()) == null)
                        {
                            String msg = "'" + lType + "' unset in " + PropertyReader.ENVIRONMENT_FILE;
                            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new NullPointerException());
                        }
                    }
                }
                return environment;
            case SIP2:
                if (sip2 == null)
                {
                    try
                    {
                        sip2 = readProperties(PropertyReader.SIP2_FILE);
                    }
                    catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + PropertyReader.SIP2_FILE
                                + "'. It may be empty but one must be defined to continue.";
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, ex);
                    }
                    catch (NullPointerException npe)
                    {
                        String msg = "Failed to read environment config file. One must be defined.";
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                    // now check that all mandetory values are here.
                    for (SipPropertyTypes sType : SipPropertyTypes.values())
                    {
                        if (sip2.get(sType.toString()) == null)
                        {
                            String msg = "'" + sType + "' unset in " + PropertyReader.SIP2_FILE;
                            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new NullPointerException());
                        }
                    }
                }
                return sip2;
            case BIMPORT:
                if (bimport == null)
                {
                    try
                    {
                        bimport = readProperties(PropertyReader.BIMPORT_PROPERTY_FILE);
                    }
                    catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + PropertyReader.BIMPORT_PROPERTY_FILE
                                + "'. It may be empty but one must be defined to continue.";
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, ex);
                    }
                    catch (NullPointerException npe)
                    {
                        String msg = "Failed to read bimport config file. One must be defined.";
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                    // now check that all mandetory values are here.
                    for (BImportPropertyTypes bType : BImportPropertyTypes.values())
                    {
                        if (bimport.get(bType.toString()) == null)
                        {
                            String msg = "'" + bType + "' unset in " + PropertyReader.BIMPORT_PROPERTY_FILE;
                            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new NullPointerException());
                        }
                    }
                }
                return bimport;
            case API:
                if (api == null)
                {
                    try
                    {
                        api = readProperties(PropertyReader.API_FILE);
                    }
                    catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + PropertyReader.API_FILE
                                + "'. It may be empty but one must be defined to continue.";
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, ex);
                    }
                    catch (NullPointerException npe)
                    {
                        String msg = "Failed to read api config file. One must be defined.";
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                    // now check that all mandetory values are here.
                    for (APIPropertyTypes apiType : APIPropertyTypes.values())
                    {
                        if (api.get(apiType.toString()) == null)
                        {
                            String msg = "'" + apiType + "' unset in " + PropertyReader.API_FILE;
                            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new NullPointerException());
                        }
                    }
                }
                return api;
            default:
                throw new UnsupportedOperationException("unsupported property file");
        }
    }

    private static Properties readProperties(String whichFile)
            throws FileNotFoundException
    {

        Properties properties = new Properties();
        try
        {
            FileInputStream fis = new FileInputStream(whichFile);
            properties.loadFromXML(fis);
        }
        // TODO fix so a useful diagnostic message is returned.
        catch (IOException ioe)
        {
            String msg = "Failed to read '" + whichFile + "'. One must be defined.";
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, ioe);
        }
        return properties;
    }
}
