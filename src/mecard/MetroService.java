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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.config.APIPropertyTypes;
import mecard.config.BImportPropertyTypes;
import mecard.config.ConfigFileTypes;
import static mecard.config.ConfigFileTypes.API;
import static mecard.config.ConfigFileTypes.BIMPORT;
import static mecard.config.ConfigFileTypes.BIMPORT_CITY_MAPPING;
import static mecard.config.ConfigFileTypes.DEBUG;
import static mecard.config.ConfigFileTypes.DEFAULT_CREATE;
import static mecard.config.ConfigFileTypes.ENVIRONMENT;
import static mecard.config.ConfigFileTypes.SIP2;
import mecard.config.DebugQueryConfigTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.SipPropertyTypes;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.commons.daemon.*;
import site.mecard.MemberTypes;
/**
 * This is the entry point of the MeCard server application.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class MetroService implements Daemon
{
    private static String CONFIG_DIR = "";
    private static String BIMPORT_PROPERTY_FILE = "bimport.properties";
    private static String DEFAULT_CREATE_PROPERTY_FILE = "default.properties";
    private static String ENVIRONMENT_FILE = "environment.properties";
    private static String SIP2_FILE = "sip2.properties";
    private static String API_FILE = "api.properties";
    private static String BIMPORT_CITY_MAPPING = "city_st.properties";
    private static String DEBUG_SETTINGS_FILE = "debug.properties";
    
    private static Properties defaultPolicies; // = MetroService.getProperties(ConfigFileTypes.DEFAULT_CREATE);
    private static Properties bimport; // don't read by default since this is optional.
    private static Properties environment; // = MetroService.getProperties(ConfigFileTypes.ENVIRONMENT);
    private static Properties sip2; // Optional config file.
    private static Properties api; // Optional config file.
    private static Properties city; // Optional config file, required for Horizon users.
    private static Properties debugProperties; // Optional config file for providing canned debugging behaviours.
    
    private static ServerSocket serverSocket = null;
    private static boolean listening = true;
    private static String defaultPort = "2004";

    public static void main(String[] args)
    {
        // First get the valid options
        Options options = new Options();
        // add t option c to config directory true=arg required.
        options.addOption("c", true, "configuration file directory path, include all sys dependant dir seperators like '/'.");
        
        try
        {
            // parse the command line.
            CommandLineParser parser = new BasicParser();
            CommandLine cmd;
            cmd = parser.parse(options, args);
             // get c option value
            String configDirectory = cmd.getOptionValue("c");
            if(configDirectory == null) 
            {
                CONFIG_DIR = configDirectory;
            }
            BIMPORT_PROPERTY_FILE = CONFIG_DIR + BIMPORT_PROPERTY_FILE;
            DEFAULT_CREATE_PROPERTY_FILE = CONFIG_DIR + DEFAULT_CREATE_PROPERTY_FILE;
            ENVIRONMENT_FILE = CONFIG_DIR + ENVIRONMENT_FILE;
            SIP2_FILE = CONFIG_DIR + SIP2_FILE;
            API_FILE = CONFIG_DIR + API_FILE;
            BIMPORT_CITY_MAPPING = CONFIG_DIR + BIMPORT_CITY_MAPPING;
            DEBUG_SETTINGS_FILE = CONFIG_DIR + DEBUG_SETTINGS_FILE;
        } 
        catch (ParseException ex)
        {
//            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(new Date() + "Unable to parse command line option.");
        }
        
       
        Properties properties = MetroService.getProperties(ConfigFileTypes.ENVIRONMENT);
        String portString = properties.getProperty(LibraryPropertyTypes.METRO_PORT.toString(), defaultPort);
        
        try
        {
            int port = Integer.parseInt(portString);
            serverSocket = new ServerSocket(port);
        }
        catch (IOException ex)
        {
            String msg = "Could not listen on port: " + portString;
//            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
            System.out.println(new Date() + msg);
        }
        catch (NumberFormatException ex)
        {
            String msg = "Could not parse port number defined in configuration file.";
//            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
            System.out.println(new Date() + msg);
        }

        while (listening)
        {
            try
            {
                new SocketThread(serverSocket.accept()).start();
            }
            catch (IOException ex)
            {
                String msg = "unable to start server socket; either accept or start failed.";
//            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
                System.out.println(new Date() + msg);
            }
        }
        try
        {
            serverSocket.close();
        }
        catch (IOException ex)
        {
            String msg = "failed to close the server socket.";
//            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
            System.out.println(new Date() + msg);
        }
    }

    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception
    {
        System.out.println(new Date() + " initializing service...");
    }

    @Override
    public void start() throws Exception
    {
        System.out.println(new Date() + " starting service...");
//        environment = MetroService.readProperties(ConfigFileTypes.ENVIRONMENT);
        main(null);
    }

    @Override
    public void stop() throws Exception
    {
        System.out.println(new Date() + " stopping service...");
    }

    @Override
    public void destroy()
    {
        // do cleanup.
        System.out.println(new Date() + " done.");
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
                        defaultPolicies = readProperties(MetroService.DEFAULT_CREATE_PROPERTY_FILE);
                    } catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + MetroService.DEFAULT_CREATE_PROPERTY_FILE
                                + "'. It is required to message customers of important events.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
                    } catch (NullPointerException npe)
                    {
                        String msg = "Failed to read customer creation default config file. One must be defined.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                }
                return defaultPolicies;
            case ENVIRONMENT:
                if (environment == null)
                {
                    try
                    {
                        environment = readProperties(MetroService.ENVIRONMENT_FILE);
                    } catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + MetroService.ENVIRONMENT_FILE
                                + "'. It may be empty but one must be defined to continue.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
                    } catch (NullPointerException npe)
                    {
                        String msg = "Failed to read environment config file. One must be defined.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                    // now check that all mandetory values are here.
                    for (LibraryPropertyTypes lType : LibraryPropertyTypes.values())
                    {
                        if (environment.get(lType.toString()) == null)
                        {
                            String msg = "'" + lType + "' unset in " + MetroService.ENVIRONMENT_FILE;
                            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, new NullPointerException());
                        }
                    }
                }
                return environment;
            case SIP2:
                if (sip2 == null)
                {
                    try
                    {
                        sip2 = readProperties(MetroService.SIP2_FILE);
                    } catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + MetroService.SIP2_FILE
                                + "'. It may be empty but one must be defined to continue.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
                    } catch (NullPointerException npe)
                    {
                        String msg = "Failed to read environment config file. One must be defined.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                    // now check that all mandetory values are here.
                    for (SipPropertyTypes sType : SipPropertyTypes.values())
                    {
                        if (sip2.get(sType.toString()) == null)
                        {
                            String msg = "'" + sType + "' unset in " + MetroService.SIP2_FILE;
                            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, new NullPointerException());
                        }
                    }
                }
                return sip2;
            case BIMPORT:
                if (bimport == null)
                {
                    try
                    {
                        bimport = readProperties(MetroService.BIMPORT_PROPERTY_FILE);
                    } catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + MetroService.BIMPORT_PROPERTY_FILE
                                + "'. It may be empty but one must be defined to continue.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
                    } catch (NullPointerException npe)
                    {
                        String msg = "Failed to read bimport config file. One must be defined.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                    // now check that all mandetory values are here.
                    for (BImportPropertyTypes bType : BImportPropertyTypes.values())
                    {
                        if (bimport.get(bType.toString()) == null)
                        {
                            String msg = "'" + bType + "' unset in " + MetroService.BIMPORT_PROPERTY_FILE;
                            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, new NullPointerException());
                        }
                    }
                }
                return bimport;
            case API:
                if (api == null)
                {
                    try
                    {
                        api = readProperties(MetroService.API_FILE);
                    } catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + MetroService.API_FILE
                                + "'. It may be empty but one must be defined to continue.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
                    } catch (NullPointerException npe)
                    {
                        String msg = "Failed to read api config file. One must be defined.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                    // now check that all mandetory values are here.
                    for (APIPropertyTypes apiType : APIPropertyTypes.values())
                    {
                        if (api.get(apiType.toString()) == null)
                        {
                            String msg = "'" + apiType + "' unset in " + MetroService.API_FILE;
                            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, new NullPointerException());
                        }
                    }
                }
                return api;

            case BIMPORT_CITY_MAPPING:
                if (city == null)
                {
                    try
                    {
                        city = readProperties(MetroService.BIMPORT_CITY_MAPPING);
                    } catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + MetroService.BIMPORT_CITY_MAPPING
                                + "'. It may be empty but one must be defined to continue.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
                    } catch (NullPointerException npe)
                    {
                        String msg = "Failed to read bimport config file. One must be defined.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                    // now check that all mandetory values are here.
                    for (MemberTypes mType : MemberTypes.values())
                    {
                        if (city.get(mType.toString()) == null)
                        {
                            String msg = "'" + mType + "' unset in " + MetroService.BIMPORT_CITY_MAPPING;
                            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, new NullPointerException());
                        }
                    }
                }
                return city;

            case DEBUG:
                if (debugProperties == null)
                {
                    try
                    {
                        debugProperties = readProperties(MetroService.DEBUG_SETTINGS_FILE);
                    } catch (FileNotFoundException ex)
                    {
                        String msg = "Failed to find '" + MetroService.DEBUG_SETTINGS_FILE + "'";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
                    } catch (NullPointerException npe)
                    {
                        String msg = "Failed to read debug config file. One must be defined.";
                        Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, npe);
                    }
                    // now check that all mandetory values are here.
                    for (DebugQueryConfigTypes dType : DebugQueryConfigTypes.values())
                    {
                        if (debugProperties.get(dType.toString()) == null)
                        {
                            String msg = "'" + dType + "' unset in " + MetroService.DEBUG_SETTINGS_FILE;
                            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, new NullPointerException());
                        }
                    }
                }
                return debugProperties;


            default:
                throw new UnsupportedOperationException("unsupported property file");
        }
    }

    /**
     * Loads properties from the a given config file and updating an existing
     * property Map or inserts if the values don't exist.
     *
     * @see Command
     * @param props
     * @param type
     */
    public static void augmentProperties(Map<String, String> props, ConfigFileTypes type)
    {
        if (props == null)
        {
            return;
        }
        Properties localProps = MetroService.getProperties(type);
        Enumeration em = localProps.keys();
        while (em.hasMoreElements())
        {
            String key = (String) em.nextElement();
//            System.out.println("Key:" + key + " value:" + localProps.getProperty(key, ""));
            props.put(key, (String) localProps.getProperty(key, ""));
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
        } // TODO fix so a useful diagnostic message is returned.
        catch (IOException ioe)
        {
            String msg = "Failed to read '" + whichFile + "'. One must be defined.";
            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ioe);
        }
        return properties;
    }
}
