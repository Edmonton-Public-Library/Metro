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
package mecard.util;

import java.util.Properties;
import mecard.MetroService;
import mecard.config.ConfigFileTypes;
import mecard.site.MemberTypes;

/**
 * Does a lookup for city code in the city_st.properties file.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class City 
{
    /**
     * Does a lookup for city code in the city_st.properties file. The city code
     * is required and the codes are defined by each library that uses Horizon.
     * @param p
     * @return city code suitable for this instance of Horizon's city_st fields.
     */
    public final static String getCity(String p)
    {
        return getCity(p, false);
    }

    /**
     * Does a lookup for city code in the city_st.properties file. The city code
     * is required and the codes are defined by each library that uses Horizon.
     * @param fullCityName
     * @return city code suitable for this instance of Horizon's city_st.
     */
    public static final String getCity(String fullCityName, boolean debug)
    {
        Properties properties = MetroService.getProperties(ConfigFileTypes.BIMPORT_CITY_MAPPING);
        
        if (fullCityName == null) return "";
        if (debug) System.out.println("NAME:"+fullCityName);
        String city = fullCityName.trim();
        if (city.isEmpty()) return "";
        for (MemberTypes cType: MemberTypes.values())
        {
            String cityCode = properties.getProperty(cType.toString());
            if (debug) System.out.println("CITYCODE:"+cityCode+" CTYPE:"+cType.toString());
            if (city.equalsIgnoreCase(cType.toString()))
            {
                return cityCode;
            }
        }
        return fullCityName;
    }
}
