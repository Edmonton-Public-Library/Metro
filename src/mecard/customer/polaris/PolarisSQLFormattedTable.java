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
package mecard.customer.polaris;

import java.util.HashMap;
import mecard.customer.FormattedTable;

/**
 *
 * @author anisbet
 */
public class PolarisSQLFormattedTable implements FormattedTable
{
    private final String TABLE_NAME;
    private final HashMap<String, String> columns;
    
    /**
     * Mostly used for testing.
     * @param name 
     */
    public PolarisSQLFormattedTable(String name)
    {
        TABLE_NAME   = name;
        this.columns = new HashMap<>();
    }
    
    /**
     * Use this if you already have a hash of keys and values of PolarisTable column
     * names as keys and values to be stored as values.
     * @param headDataMap 
     */
    public PolarisSQLFormattedTable(String name, HashMap<String, String> headDataMap)
    {
        TABLE_NAME   = name;
        this.columns = new HashMap<>();
        for (String key: headDataMap.keySet())
        {
            this.columns.put(key, headDataMap.get(key));
        }
    }
    
    @Override
    public String getData()
    {
        StringBuilder data = new StringBuilder();
        for (String sKeys: this.columns.keySet())
        {
            // The table has no real output value so this is more of a toString() method.
            // The table's values are referenced by name individually.
            data.append(sKeys);
            data.append("='");
            data.append(this.columns.get(sKeys));
            data.append("'\n");
        }
        return data.toString();
    }

    @Override
    public String getHeader()
    {
        StringBuilder data = new StringBuilder();
        for (String sKeys: this.columns.keySet())
        {
            // The table has no real output value so this is more of a toString() method.
            // but just the names of the keys. It is never used in practice.
            data.append(sKeys);
            data.append("'\n");
        }
        return data.toString();
    }

    @Override
    public String getName()
    {
        return TABLE_NAME;
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
    
}
