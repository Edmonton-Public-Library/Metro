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
package mecard.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import mecard.Protocol;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;

/**
 * Source:
 * 
 * Municipal Services Branch
 * 17th Floor Commerce Place
 * 10155 - 102 Street Edmonton, Alberta T5J 4L4
 * Phone: 780-427-2225 Fax: 780-420-1016
 * E-mail: lgs.update@gov.ab.ca
 * 
 * Updated December 28, 2012
 * 
 * Since the initial release there has been a requirement to add place names
 * commonly used for Alberta, but not 'sanctioned'. None-the-less that is where
 * mail is sent and that is the address the people that live there refer to.
 * All additional none sanctioned names are listed in the 6xxx range of codes.
 * 
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class AlbertaCity extends City
{
    private static HashMap<String, String> cityMap;
    private static City instance;
    
    public static City getInstanceOf()
    {
        if (instance == null)
        {
            instance = new AlbertaCity();
        }
        return instance;
    }
    
    @Override
    public boolean isPlaceName(String placeName)
    {
        for (String fullPlaceName: cityMap.keySet())
        {
            if (fullPlaceName.compareToIgnoreCase(placeName) == 0)
            {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<String> getPlaceNames(String place)
    {
        List<String> listOfNames = new ArrayList<>();
        for (String fullPlaceName: cityMap.keySet())
        {
            if (fullPlaceName.toLowerCase().endsWith(place.toLowerCase()))
            {
                listOfNames.add(fullPlaceName);
            }
        }
        return listOfNames;
    }
    
    @Override
    public String getCityCode(String cityName)
    {
        String returnCity = Protocol.DEFAULT_FIELD_VALUE;
        for (String fullPlaceName: cityMap.keySet())
        {
            if (fullPlaceName.compareToIgnoreCase(cityName) == 0)
            {
                return cityMap.get(fullPlaceName); 
            }
        }
        return returnCity;
    }
    
    private AlbertaCity()
    {
        // If we don't have a region.properties file get a new one from the URL 
        // listed in region_config.properties. See PropertyReader for more information.
        Properties placeNames = PropertyReader.getProperties(ConfigFileTypes.REGIONAL_NAMES);
        // Now load all those names into the hashmap for processing as before.
        cityMap = new HashMap<>();
        for (String key: placeNames.stringPropertyNames())
        {
            cityMap.put(key, placeNames.getProperty(key));
        }
        System.out.println("Read " + placeNames.size() + " place names.");
        boolean isSpellingMistake = false;
        // Now we overlay place name records with config requested codes for BImport
        // which will be empty if the ILS is not a Horizon system.
        Properties bimportCodeProperties = PropertyReader.getProperties(ConfigFileTypes.BIMPORT_CITY_MAPPING);
        for(String customPlaceName : bimportCodeProperties.stringPropertyNames())
        {
            
            if (cityMap.get(customPlaceName) == null)
            {
                // if this is the first spelling mistake, put this title but
                // only once, any other spelling mistakes will just be listed below it.
                if (isSpellingMistake == false)
                {
                    System.out.println(new Date() +
                            " The following not recognized as official "
                            + "Alberta place name(s):");
                }
                System.out.println("=>'" + customPlaceName + "'<=");
                isSpellingMistake = true;
                continue;
            }
            // Now get the property file's associated code for the given place name
            // (real place name or otherwise). This is the code the ILS admin put 
            // in the properties file. 
            String preferedCode = bimportCodeProperties.getProperty(customPlaceName);
            cityMap.put(customPlaceName, preferedCode);
//            System.out.println(">> overwrite: '" + customPlaceName + "' with '" + preferedCode + "'");
        }
    }
}
