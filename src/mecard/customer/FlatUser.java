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
package mecard.customer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;

/**
 * This class represents a helper class for the customer represented as a flat
 * user. Limitations: searching for a field in the extended sections will return
 * the last found result, ie, if the user has two addresses, searching for
 * STREET will return one or the other, but indeterminitely. This is due to the
 * fact that the fields are stored as EnumMaps.
 *
 * @see java.util.EnumMap
 * @author andrew
 */
public class FlatUser
{

    private final static String DOC_BOUNDARY = "*** DOCUMENT BOUNDARY ***";
    private EnumMap<FlatUserFieldTypes, String> address1;
    private EnumMap<FlatUserFieldTypes, String> address2;
    private EnumMap<FlatUserFieldTypes, String> xinfo;
    private EnumMap<FlatUserFieldTypes, String> customerFields;

    /**
     * Creates FlatUser.
     */
    public FlatUser()
    {
        this.customerFields = new EnumMap<FlatUserFieldTypes, String>(FlatUserFieldTypes.class);
        this.address1 = new EnumMap<FlatUserFieldTypes, String>(FlatUserFieldTypes.class);
        this.address2 = new EnumMap<FlatUserFieldTypes, String>(FlatUserFieldTypes.class);
        this.xinfo = new EnumMap<FlatUserFieldTypes, String>(FlatUserFieldTypes.class);
    }

    /**
     * Adds a value to the non-extended section by default.
     *
     * @param whichField the value of whichField
     * @param field
     */
    
    public void add(FlatUserFieldTypes whichField, String field)
    {
        this.add(FlatUserExtendedFields.USER, whichField, field);
    }

    /**
     * Adds a field to the extended VED sections of the flat user. Warning:
     * Internally there is no checking to ensure you are placing a valid field
     * in a valid section, that is, you can add a NOTE field to an USER_ADDR1
     * field even thou that is not legal in Symphony.
     *
     * @param extField the enum for the field to add to.
     * @param whichField the enum of the field you are adding.
     * @param field String you are adding as a value to the field.
     */
    
    public void add(FlatUserExtendedFields extField, FlatUserFieldTypes whichField, String field)
    {
        if (field.isEmpty())
        {
            return;
        }
        switch (extField)
        {
            case USER_ADDR1:
                this.address1.put(whichField, field);
                break;
            case USER_ADDR2:
                this.address2.put(whichField, field);
                break;
            case USER_XINFO:
                this.xinfo.put(whichField, field);
                break;
            default:
                this.customerFields.put(whichField, field);
        }
    }

    /**
     * Method returns the value in a given field. If the field is not set it
     * returns an empty string.
     *
     * @param field
     * @return the value of the field or an empty field if none found.
     */
    public String getField(FlatUserFieldTypes field)
    {
        if (this.customerFields.containsKey(field))
        {
            return this.customerFields.get(field);
        }
        if (this.address1.containsKey(field))
        {
            return this.address1.get(field);
        }
        if (this.address2.containsKey(field))
        {
            return this.address2.get(field);
        }
        if (this.xinfo.containsKey(field))
        {
            return this.xinfo.get(field);
        }
        return "";
    }

    /**
     * Gets the customer as a list of Strings formatted in flat format.
     *
     * @return List of the customer fields.
     */
    public List<String> toList()
    {
        List<String> flatData = new ArrayList<String>();
        flatData.add(FlatUser.DOC_BOUNDARY + "\n");
        Set<FlatUserFieldTypes> keys = this.customerFields.keySet();
        for (FlatUserFieldTypes key : keys)
        {
            flatData.add(this.formatLine(key.name(), this.customerFields.get(key)));
        }
        // Address1
        if (this.address1.size() > 0)
        {
            keys = this.address1.keySet();
            flatData.add("." + FlatUserFieldTypes.USER_ADDR1_BEGIN + ".\n");
            for (FlatUserFieldTypes key : keys)
            {
                flatData.add(this.formatLine(key.name(), this.address1.get(key)));
            }
            flatData.add("." + FlatUserFieldTypes.USER_ADDR1_END + ".\n");
        }
        // Address2
        if (this.address2.size() > 0)
        {
            keys = this.address1.keySet();
            flatData.add("." + FlatUserFieldTypes.USER_ADDR2_BEGIN + ".\n");
            for (FlatUserFieldTypes key : keys)
            {
                flatData.add(this.formatLine(key.name(), this.address2.get(key)));
            }
            flatData.add("." + FlatUserFieldTypes.USER_ADDR2_END + ".\n");
        }
        // XINFO
        if (this.xinfo.size() > 0)
        {
            keys = this.xinfo.keySet();
            flatData.add("." + FlatUserFieldTypes.USER_XINFO_BEGIN + ".\n");
            for (FlatUserFieldTypes key : keys)
            {
                flatData.add(this.formatLine(key.name(), this.xinfo.get(key)));
            }
            flatData.add("." + FlatUserFieldTypes.USER_XINFO_END + ".\n");
        }
        return flatData;
    }

    private String formatLine(String fieldName, String fieldValue)
    {
        //.USER_PRIV_GRANTED.   |a20120705
        // Stupid fix for CITY/STATE because sirsi used '/' which can't be used in an enum.
        if (fieldName.equalsIgnoreCase("CITY_STATE"))
        {
            return ".CITY/STATE.   |a" + fieldValue + "\n";
        }
        return "." + fieldName + ".   |a" + fieldValue + "\n";
    }

    /**
     * Adds all the default properties specified in the
     * default_create_config.xml. <b>Note</b> this method does not enter default
     * fields in extended field sections of Address1, Address2 or XINFO for the
     * flat user.
     *
     */
    public void setDefaultProperties()
    {
        Properties defaultCreateProperties = PropertyReader.getProperties(
                ConfigFileTypes.DEFAULT_CREATE);
        Enumeration defaultProperties = defaultCreateProperties.propertyNames();
        FlatUserFieldTypes[] flatUserFieldKeys = FlatUserFieldTypes.values();
        while (defaultProperties.hasMoreElements())
        {
            String defaultPropertyName = (String) defaultProperties.nextElement();
            // now we have to iterate over all the fields to find a match
            for (FlatUserFieldTypes flatUserKey : flatUserFieldKeys)
            {
                if (flatUserKey.name().equalsIgnoreCase(defaultPropertyName))
                {
                    this.add(flatUserKey, defaultCreateProperties.getProperty(defaultPropertyName));
                    break;
                }
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(FlatUser.DOC_BOUNDARY.toString()).append("\n");
        Set<FlatUserFieldTypes> keys = this.customerFields.keySet();
        for (FlatUserFieldTypes key : keys)
        {
            sb.append(this.formatLine(key.name(), this.customerFields.get(key)));
        }
        // Address1
        if (this.address1.size() > 0)
        {
            keys = this.address1.keySet();
            sb.append(".").append(FlatUserFieldTypes.USER_ADDR1_BEGIN).append(".").append("\n");
            for (FlatUserFieldTypes key : keys)
            {
                sb.append(this.formatLine(key.name(), this.address1.get(key)));
            }
            sb.append(".").append(FlatUserFieldTypes.USER_ADDR1_END).append(".").append("\n");
        }
        // Address2
        if (this.address2.size() > 0)
        {
            keys = this.address2.keySet();
            sb.append(".").append(FlatUserFieldTypes.USER_ADDR2_BEGIN).append(".").append("\n");
            for (FlatUserFieldTypes key : keys)
            {
                sb.append(this.formatLine(key.name(), this.address2.get(key)));
            }
            sb.append(".").append(FlatUserFieldTypes.USER_ADDR2_END).append(".").append("\n");
        }
        // XINFO
        if (this.xinfo.size() > 0)
        {
            keys = this.xinfo.keySet();
            sb.append(".").append(FlatUserFieldTypes.USER_XINFO_BEGIN).append(".").append("\n");
            for (FlatUserFieldTypes key : keys)
            {
                sb.append(this.formatLine(key.name(), this.xinfo.get(key)));
            }
            sb.append(".").append(FlatUserFieldTypes.USER_XINFO_END).append(".").append("\n");
        }
        return sb.toString();
    }
}
