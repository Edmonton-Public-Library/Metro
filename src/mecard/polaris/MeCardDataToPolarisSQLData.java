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
package mecard.polaris;

import java.util.HashMap;
import java.util.Set;
import mecard.customer.MeCardDataToNativeData;

/**
 * Storage container that provides a standard interface to use when converting
 * MeCard customer data into Polaris SQL statements.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class MeCardDataToPolarisSQLData implements MeCardDataToNativeData
{
    private final String TABLE_NAME;
    private final HashMap<String, String> columns;
    
    /**
     * Mostly used for testing.
     * @param name 
     */
    public MeCardDataToPolarisSQLData(String name)
    {
        TABLE_NAME   = name;
        this.columns = new HashMap<>();
    }
    
    /**
     * Use this if you already have a hash of keys and values of PolarisTable column
     * names as keys and values to be stored as values.
     * @param name - name of the table.
     * @param headDataMap - hash map of data to store.
     */
    public MeCardDataToPolarisSQLData(String name, HashMap<String, String> headDataMap)
    {
        TABLE_NAME   = name;
        this.columns = new HashMap<>();
        for (String key: headDataMap.keySet())
        {
            this.columns.put(key, headDataMap.get(key));
        }
    }
    
    /** 
     * Returns all the column names of the formatted table. The motivation for
     * this feature is to allow SQL commands to take FormattedTable objects as
     * arguments, rather than use complex builders.
     * @return Set of String names of all columns in the formatted table.
     */
    @Override
    public Set<String> getKeys()
    {
        return this.columns.keySet();
    }
    
    /**
     * Outputs all the keys and values separated by an '=' character, with
     * values surrounded by single quotes "'".
     * @return String of the contents of the hash map.
     */
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
            data.append("' ");
        }
        return data.toString().trim();
    }

    /**
     * Returns a space separated string of the names in the key set of the 
     * underlying HashMap.
     * @return String of spaced delimited keys to stored values.
     */
    @Override
    public String getHeader()
    {
        StringBuilder data = new StringBuilder();
        for (String sKeys: this.columns.keySet())
        {
            // The table has no real output value so this is more of a toString() method.
            // but just the names of the keys. It is never used in practice.
            data.append(sKeys);
            data.append(" ");
        }
        return data.toString();
    }

    /**
     * 
     * @return String name of the table.
     */
    @Override
    public String getName()
    {
        return TABLE_NAME;
    }

    /**
     * Returns the values stored for a named case-sensitive key.
     * @param key
     * @return String value referenced by the key if found, and an empty string
     * if the key wasn't found.
     */
    @Override
    public String getValue(String key)
    {
        if (this.columns.containsKey(key))
        {
            return this.columns.get(key);
        }
        return "";
    }

    /**
     * Stores a value referenced by a case-sensitive key name.
     * @param key name of the key as a string that cannot be empty or null. Any 
     * pre-existing values for any named key are overwritten.
     * @param value String value to store. Any existing value is over-written.
     * @return true if a column already exists, and false if the column doesn't
     * exist, or the passed key is empty or null.
     */
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
     * Renames a key. To do this the original value is first removed from the 
     * hash map, then a new key is inserted, with the original value as the 
     * new key's value.
     * 
     * If the key doesn't exist in the hash map or if the value stored is empty
     * or null, the hash map remains unaltered.
     * @param originalkey string name of the key to be replaced.
     * @param replacementKey string of the new key name.
     * @return false if no originalKey could be found,
     * and true if the original key is renamed.
     */
    @Override
    public boolean renameKey(String originalkey, String replacementKey)
    {
        if (this.columns.containsKey(originalkey))
        {
            String originalValue = this.columns.remove(originalkey);
            this.columns.put(replacementKey, originalValue);
            return true;
        }
        return false;
    }

    /**
     * Deletes a key value pair from the formatted table.
     * @param key String named key to delete.
     * @return true if the key was found and its value deleted, and false 
     * otherwise.
     */
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
