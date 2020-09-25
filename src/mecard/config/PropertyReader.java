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
package mecard.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.exception.ConfigurationException;
import mecard.util.PlaceNameWGet;

/**
 * Property reader reads all the configuration files and ensures that required
 * values are included in each file. The property files are XML based. The order
 * of the elements in the property files are not important.
 * @author Andrew Nisbet andrew.nisbet@epl.ca
 */
public class PropertyReader
{
    public final static String VERSION           = "1.0.01"; // server version
    /** Including this tag with a value like 'user&#64;server.com', will cause 
     * commands to be run remotely through secure shell (ssh).
     * The tag is optional. Leaving it out means 
     * you expect to run the commands on the ILS server itself.
     */
    public final static String SSH_TAG           = "ssh";
    public final static String EXPORT            = "export"; // exporting PATH and UPATH for Unix Symphony
    public final static String LOAD_DIR          = "load-dir";
    private static String CONFIG_DIR             = "";
    private static String BIMPORT_PROPERTY_FILE  = "bimport.properties";
    private static String SYMPHONY_PROPERTY_FILE = "symphony.properties";
    private static String POLARIS_PAPI_FILE      = "papi.properties";
    private static String ENVIRONMENT_FILE       = "environment.properties";
    private static String SIP2_FILE              = "sip2.properties";
    private static String BIMPORT_CITY_MAPPING   = "city_st.properties";
    private static String DEBUG_SETTINGS_FILE    = "debug.properties";
    private static String VARIABLES_FILE         = "sysvar.properties"; // these are system specific variables, like PATH.
    private static String MESSAGES_PROPERTY_FILE = "messages.properties";
    private static String POLARIS_SQL_FILE       = "polaris_sql.properties";
    private static String REGIONAL_NAMES_CONFIG_FILE = "region_config.properties"; // Where to get the properties by URL.
    private static String REGIONAL_NAMES_FILE    = "region.properties"; // File that contains the actual names locally.
        // There are no mandatory variables, so no checking is done.
    private static Properties polarisAPI;            // Default properties needed by Polaris.
    private static Properties symphony;           // Default properties needed to create a user in Symphony.
    private static Properties bimport;            // Properties Metro needs to operate BImport.
    private static Properties environment;        // Basic envrionment values.
    private static Properties sip2;               // Variables used to talk to SIP2.
    private static Properties city;               // Required for Horizon users to translate site specific city codes.
    private static Properties debugProperties;    // Optional config for debugging.
    private static Properties systemVariables;    // Optional no mandatory fields.
    private static Properties messagesProperties; // Messages tailored by local library.
    private static Properties SQLProperties;      // Optional config for sites that use POLARIS_SQL for transactions.
    private static Properties regionalNameConfigProperties; // Location of global XML properties file.
    private static Properties regionalNamesProperties;// Names of places, lookup for util.city and derivations.
    
    /**
     * Parses a list of ',' comma separated types from a given entry in the 
     * environment properties file and returns them as a list for testing.
     *
     * @param props the Java {@link Properties} file that contains the entry
     * of delimited entries.
     * @param libraryPropertyTypes the entry in the environment.properties file
     * to be parsed into a list for returning.
     * @param list the value of list 
     */
    public static void loadDelimitedEntry(
            Properties props, LibraryPropertyTypes libraryPropertyTypes, List<String> list)
    {
        if (list.size() > 0)
        {
            list.clear();
        }
        String stringTypes = props.getProperty(libraryPropertyTypes.toString());
        String[] stringList = stringTypes.split(",");
        for (String s: stringList)
        {
            list.add(s.trim());
        }
    }
    
    /**
     * Sets the config directory, the directory where all the config files can
     * be found.
     * @param configDirectory 
     */
    public static void setConfigDirectory(String configDirectory)
    {
        if(configDirectory != null && configDirectory.isEmpty() == false) 
        {
            if (configDirectory.endsWith(File.separator) == false)
            {
                configDirectory += File.separator;
            }
            CONFIG_DIR = configDirectory;
        }
        else
        {
            CONFIG_DIR = "." + File.separator;
        }
        System.out.println("Metro (MeCard) server version " + VERSION);
        System.out.println(new Date() + " CONFIG: dir set to '" + CONFIG_DIR + "'");
        BIMPORT_PROPERTY_FILE  = CONFIG_DIR + BIMPORT_PROPERTY_FILE;
        SYMPHONY_PROPERTY_FILE = CONFIG_DIR + SYMPHONY_PROPERTY_FILE;
        POLARIS_PAPI_FILE      = CONFIG_DIR + POLARIS_PAPI_FILE;
        MESSAGES_PROPERTY_FILE = CONFIG_DIR + MESSAGES_PROPERTY_FILE;
        ENVIRONMENT_FILE       = CONFIG_DIR + ENVIRONMENT_FILE;
        SIP2_FILE              = CONFIG_DIR + SIP2_FILE;
        BIMPORT_CITY_MAPPING   = CONFIG_DIR + BIMPORT_CITY_MAPPING;
        DEBUG_SETTINGS_FILE    = CONFIG_DIR + DEBUG_SETTINGS_FILE;
        VARIABLES_FILE         = CONFIG_DIR + VARIABLES_FILE;
        POLARIS_SQL_FILE       = CONFIG_DIR + POLARIS_SQL_FILE;
        REGIONAL_NAMES_CONFIG_FILE = CONFIG_DIR + REGIONAL_NAMES_CONFIG_FILE;
        REGIONAL_NAMES_FILE    = CONFIG_DIR + REGIONAL_NAMES_FILE;
    }
    
    /**
     * Returns the configuration directory
     * @return the configuration directory as a String.
     */
//    public static String getConfigDirectory()
//    {
//        return CONFIG_DIR;
//    }
    
    public final static String getRegionFileName()
    {
        return REGIONAL_NAMES_FILE;
    }
    
    public final static String getRegionalConfigFileName()
    {
        return REGIONAL_NAMES_CONFIG_FILE;
    }
    
    /**
     * Gets specific properties from a configuration file.
     * @param type of configuration file to read, ie environment or bimport etc.
     * @return Java properties object filled with values read from the property file.
     */
    public static Properties getProperties(ConfigFileTypes type)
    {
        switch (type)
        {
            case SYMPHONY: // Additional properties that are given to a customer by default at creation time.
                symphony = readPropertyFile(PropertyReader.SYMPHONY_PROPERTY_FILE);
                // Now check manditory fields
                for (SymphonyPropertyTypes sType : SymphonyPropertyTypes.values())
                {
                    if (symphony.get(sType.toString()) == null)
                    {
                        String msg = "'" + sType + "' unset in " + PropertyReader.SYMPHONY_PROPERTY_FILE;
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new ConfigurationException());
                    }
                }
                return symphony;
                
            case ENVIRONMENT:
                environment = readPropertyFile(PropertyReader.ENVIRONMENT_FILE);
                // now check that all mandetory values are here.
                for (LibraryPropertyTypes lType : LibraryPropertyTypes.values())
                {
                    if (environment.get(lType.toString()) == null)
                    {
                        String msg = "'" + lType + "' unset in " + PropertyReader.ENVIRONMENT_FILE;
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new ConfigurationException());
                    }
                }
                return environment;
                
            case SIP2:
                sip2 = readPropertyFile(PropertyReader.SIP2_FILE);
                // now check that all mandetory values are here.
                for (SipPropertyTypes sType : SipPropertyTypes.values())
                {
                    if (sip2.get(sType.toString()) == null)
                    {
                        String msg = "'" + sType + "' unset in " + PropertyReader.SIP2_FILE;
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new ConfigurationException());
                    }
                }
                return sip2;
                
            case BIMPORT:
                bimport = readPropertyFile(PropertyReader.BIMPORT_PROPERTY_FILE);
                // now check that all mandetory values are here.
                for (BImportPropertyTypes bType : BImportPropertyTypes.values())
                {
                    if (bimport.get(bType.toString()) == null)
                    {
                        String msg = "'" + bType + "' unset in " + PropertyReader.BIMPORT_PROPERTY_FILE;
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new ConfigurationException());
                    }
                }
                return bimport;

            case BIMPORT_CITY_MAPPING:
                city = readPropertyFile(PropertyReader.BIMPORT_CITY_MAPPING);
                return city;

            case DEBUG:
                debugProperties = readPropertyFile(PropertyReader.DEBUG_SETTINGS_FILE);
                // now check that all mandetory values are here.
                for (DebugQueryConfigTypes dType : DebugQueryConfigTypes.values())
                {
                    if (debugProperties.get(dType.toString()) == null)
                    {
                        String msg = "'" + dType + "' unset in " + PropertyReader.DEBUG_SETTINGS_FILE;
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new ConfigurationException());
                    }
                }
                return debugProperties;

            case VARS: // Additional variables used by this application as a user on a system like PATH.
                systemVariables = readPropertyFile(PropertyReader.VARIABLES_FILE);
                return systemVariables;
                
            case PAPI:
                polarisAPI = readPropertyFile(PropertyReader.POLARIS_PAPI_FILE);
                // now check that all mandetory values are here.
                for (PAPIPropertyTypes pType : PAPIPropertyTypes.values())
                {
                    if (polarisAPI.get(pType.toString()) == null)
                    {
                        String msg = "'" + pType + "' unset in " + PropertyReader.POLARIS_PAPI_FILE;
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new ConfigurationException());
                    }
                }
                return polarisAPI;
            // POLARIS_SQL required properties if the user is using POLARIS_SQL for any protocol.    
            case POLARIS_SQL:
                SQLProperties = readPropertyFile(PropertyReader.POLARIS_SQL_FILE);
                // now check that all mandetory values are here.
                for (PolarisSQLPropertyTypes sType : PolarisSQLPropertyTypes.values())
                {
                    if (SQLProperties.get(sType.toString()) == null)
                    {
                        String msg = "'" + sType + "' unset in " + PropertyReader.POLARIS_SQL_FILE;
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new ConfigurationException());
                    }
                }
                return SQLProperties;
                
            case MESSAGES:
                messagesProperties = readPropertyFile(PropertyReader.MESSAGES_PROPERTY_FILE);
                // now check that all mandetory values are here.
                for (MessagesTypes mType : MessagesTypes.values())
                {
                    if (messagesProperties.get(mType.toString()) == null)
                    {
                        String msg = "'" + mType + "' unset in " + PropertyReader.MESSAGES_PROPERTY_FILE;
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new ConfigurationException());
                    }
                }
                return messagesProperties;
                
            case REGIONAL_NAMES:
                // If there is a local version of the regional names properties
                if (regionalNamesProperties != null) 
                {
                    return regionalNamesProperties;
                }
                PlaceNameWGet globalRegionalNameReader = new PlaceNameWGet();
                globalRegionalNameReader.generatePropertyFile();
                regionalNamesProperties = readPropertyFile(PropertyReader.REGIONAL_NAMES_FILE);
                if (regionalNamesProperties.isEmpty())
                {
                    String msg = "**Error, failed to generate regional names file. Is there a connection issue? Please check configuration. "; 
                    throw new ConfigurationException(msg);
                }
                return regionalNamesProperties;
                
            case REGIONAL_NAMES_CONFIG:
                // If there is a local version of the regional names properties
                if (regionalNameConfigProperties != null)
                {
                    return regionalNameConfigProperties;
                }
                regionalNameConfigProperties = readPropertyFile(PropertyReader.REGIONAL_NAMES_CONFIG_FILE);
                // now check that all mandetory values are here.
                for (RegionalNamesConfigurationTypes rType : RegionalNamesConfigurationTypes.values())
                {
                    if (regionalNameConfigProperties.get(rType.toString()) == null)
                    {
                        String msg = "'" + rType + "' unset in " + PropertyReader.REGIONAL_NAMES_CONFIG_FILE;
                        Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, new ConfigurationException());
                    }
                }
                return regionalNameConfigProperties;
                
            default:
                throw new UnsupportedOperationException("unsupported property file");
        }
    }

    /**
     * Loads properties from the a given config file and updating an existing
     * property Map or inserts if the values don't exist.
     *
     * @see api.Command
     * @param props
     * @param type
     */
    public static void augmentProperties(Map<String, String> props, ConfigFileTypes type)
    {
        if (props == null)
        {
            return;
        }
        Properties localProps = PropertyReader.getProperties(type);
        Enumeration em = localProps.keys();
        while (em.hasMoreElements())
        {
            String key = (String) em.nextElement();
//            System.out.println("VAR:'" + key + "'='" + localProps.getProperty(key, "") + "'");
            props.put(key, (String) localProps.getProperty(key, ""));
        }
    }    

    /**
     *
     * @param propertyFileName the value of propertyFileName
     * @return the Properties
     */
    private static Properties readPropertyFile(String propertyFileName)
    {
        Properties properties = new Properties();
        try
        {
            FileInputStream fis = new FileInputStream(propertyFileName);
            properties.loadFromXML(fis);
        }
        catch (FileNotFoundException fnf)
        {
            // You can't move this to a test above because it is not possible 
            // to handle these exceptions in your source. You do not have any 
            // control over the initialization process and static{} blocks 
            // cannot be called from your source so that you could surround 
            // them with try-catch.
            // Because you cannot handle any error, it was decided to disallow 
            // exception-throwing in static blocks.
            //
            // Update: Thanks commenters for the correction. The static block 
            // must not throw checked exceptions but still allows 
            // unchecked/runtime-exceptions to be thrown. But according to above 
            // reasons you would be unable to handle these either.
            //
            //
            // Symphony sites don't require a city_st.properties file but classes
            // like AlbertaCity check for city name preferences so let the caller
            // determine if the file is required or not.
            System.out.println("WARNING: " + propertyFileName + " "
                    + "requested but not found in config directory."
                    + "This some files are not required but this one "
                    + "may be. Please consult documentation for more "
                    + "information.");
            return properties;
        }
        catch (NullPointerException npe)
        {
            String msg = "Failed to read messages config file. One must be defined.";
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, msg, npe);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return properties;
    }
}
