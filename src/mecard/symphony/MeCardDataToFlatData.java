/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
package mecard.symphony;

import java.util.HashMap;
import java.util.Set;
import mecard.config.FlatUserExtendedFieldTypes;
import mecard.customer.MeCardDataToNativeData;

/**
 * Storage for customer data that can be used to convert into a flat file.
 * @author Andrew Nisbet andrew at dev-ils.com
 */
public class MeCardDataToFlatData implements MeCardDataToNativeData
{
    private final String name;
    private final HashMap<String, String> columns;
    public static MeCardDataToFlatData getInstanceOf(FlatUserExtendedFieldTypes type, HashMap<String, String> dataFields)
    {
        return new MeCardDataToFlatData(type.name(), dataFields);
    }
    
    private MeCardDataToFlatData(String name, HashMap<String, String> headDataMap)
    {
        this.columns = new HashMap<>();
        for (String key: headDataMap.keySet())
        {
            this.columns.put(key, headDataMap.get(key));
        }
        this.name = name;
    }
    
    @Override
    public String getData()
    {
        StringBuilder data = new StringBuilder();
        for (String sKeys: this.columns.keySet())
        {
            // each entry looks like ".USER_ENVIRONMENT.   |aPUBLIC"
            data.append(".");
            data.append(sKeys);
            data.append(".");
            data.append("   |a");
            data.append(this.columns.get(sKeys));
            data.append("\n");
        }
        return finalizeTable(data);
    }

    @Override
    public String getHeader()
    {
        return "*** DOCUMENT BOUNDARY ***\nFORM=LDUSER\n";
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getValue(String key)
    {
        if (this.columns.containsKey(key))
        {
            return this.columns.get(key);
        }
        return "";
    }

    @Override
    public boolean setValue(String key, String value)
    {
        boolean response = false;
        if (key != null && value != null)
        {
            response = this.columns.containsKey(key);
            this.columns.put(key, value);
        }
        return response;
    }
    
    /**
     * Renames a key in the preserving the original stored value if any.
     * @param originalkey the original key name
     * @param replacementKey the new name for the key
     * @return true if the key could be renamed and false if there was no 
     * key found matching originalKey name. A false leaves the table unaltered.
     */
    @Override
    public boolean renameKey(String originalkey, String replacementKey)
    {
        String originalValue = this.columns.remove(originalkey);
        if (originalValue == null || originalValue.isEmpty())
        {
            return false;
        }
        this.columns.put(replacementKey, originalValue);
        return true;
    }

    public String finalizeTable(StringBuilder data)
    {
        StringBuilder out = new StringBuilder();
        switch (name)
        {
        case "USER_ADDR1":
        case "USER_ADDR2":
        case "USER_XINFO":
            out.append(".");
            out.append(this.name);
            out.append("_BEGIN.\n");
            out.append(data);
            out.append(".");
            out.append(this.name);
            out.append("_END.\n");
            break;
        case "USER":
            out.append(getHeader());
            out.append(data);
            break;
        default:
            out.append(getHeader());
            break;
        }
        return out.toString();
    }

    @Override
    public boolean deleteValue(String key)
    {
        if (this.columns.containsKey(key))
        {
            this.columns.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public Set<String> getKeys() 
    {
        return this.columns.keySet();
    }
}
