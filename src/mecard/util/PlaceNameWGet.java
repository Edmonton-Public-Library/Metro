/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2023  Edmonton Public Library
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
package mecard.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.RegionalNamesConfigurationTypes;
import mecard.customer.UserFile;
import mecard.exception.ConfigurationException;

/**
 * Gets a new version of the regional place name map from a URL.
 * @author anisbet <anisbet@epl.ca>
 * @since 0.9.05
 */
public class PlaceNameWGet
{
    private final Properties configProperties;
    /**
     * Looks up the regional names based on properties set in the 
     * {@link PropertyReader#REGIONAL_NAMES_CONFIG_FILE}.
     * Once found it will write a region.properties file in the configuration directory.
     */
    public PlaceNameWGet()
    {
        configProperties = PropertyReader.getProperties(ConfigFileTypes.REGIONAL_NAMES_CONFIG);
    }

    /**
     * Generates the regional list of names and codes from the remote canonical version. 
     */
    public void generatePropertyFile()
    {
//        String urlString = "http://wiki.melibraries.ca/citycodes.xml";
        String refreshRate = configProperties.getProperty(RegionalNamesConfigurationTypes.REFRESH.toString());
        if (isTimeToRefresh(refreshRate) == false)
        {
            System.out.println("- not time to refresh region.properties file."
                    + " change 'refresh' property in region_config.properties file.");
            return;
        }
        String urlString = configProperties.getProperty(RegionalNamesConfigurationTypes.URL.toString());
        URL url;
        try 
        {
            System.out.println("  Generating new regional place names from URL: " + urlString);
            url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            createRegionalPlaceNamePropertiesFile(con);
            System.out.println("  done.");
        } 
        catch (MalformedURLException e) 
        {
            String msg = "**Error reading citycodes URL. Please check configuration.";
            System.out.println(msg);
            throw new ConfigurationException(msg);
        } 
        catch (IOException e) 
        {
            String msg = "**Error unable to read citycodes from configured URL. Please check configuration.";
            System.out.println(msg);
            throw new ConfigurationException(msg);
        }
    }

    private void createRegionalPlaceNamePropertiesFile(HttpURLConnection connection)
    {
        if(connection != null)
        {
            try 
            {			
               BufferedReader br = 
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
               String input;
               List<String> fileContents = new ArrayList<>();
               while ((input = br.readLine()) != null)
               {
//                  System.out.println(input);
                   fileContents.add(input);
               }
               br.close();
               UserFile configFile = new UserFile(PropertyReader.getRegionFileName());
               configFile.addUserData(fileContents);
            } 
            catch (IOException e)
            {
               String msg = "**Error unable to fetch region place name content. Please check configuration.";
               System.out.println(msg);
               throw new ConfigurationException(msg);
            }
       }
    }

    private boolean isTimeToRefresh(String refreshRate)
    {
        if (refreshRate == null)
        {
            String msg = "**Error reading citycodes URL. Please check configuration.";
            System.out.println(msg + " is '" + PropertyReader.getRegionalConfigFileName() +
                    "' missing?");
            throw new ConfigurationException(msg);
        }
        switch (refreshRate.toUpperCase())
        {
            case "NEVER": return false;
            default: return true; // if there is a spelling error default to always refresh.
        }
    }
}
