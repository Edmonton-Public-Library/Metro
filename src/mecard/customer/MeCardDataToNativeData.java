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
package mecard.customer;

import java.util.Set;

/**
 * Storage container that provides a standard interface to use when converting
 * MeCard customer data into a format that can be used by the target ILS, or 
 * web service.
 * 
 * @author Andrew Nisbet andrew at dev-ils.com
 */
public interface MeCardDataToNativeData
{

    /**
     * Creates matching.
     * @return properly formatted customer data string for the table this represents.
     */
    String getData();

    /**
     *
     * @return properly formatted customer header string for the table this represents.
     */
    String getHeader();

    /**
     * 
     * @return name of the formatted table.
     */
    String getName();

    /**
     * Returns the value associated with this key.
     * @param key
     * @return the value associated with this key, or an empty string if the key is not present.
     */
    String getValue(String key);

    /**
     * Adds or changes an a key in the table entry. If the key exists then the
     * value for that key is updated and the return result is true. If the key
     * didn't exist, the key value pair are added, but the return value is false.
     * The return value is false if either the key of value is null, and no
     * changes are made to the table.
     * @param key
     * @param value
     * @return true if a key was found and updated and false if the key or value
     * could not be added because they were null, or the key and value pair did
     * not exist when they were added.
     */
    boolean setValue(String key, String value);
    
    /**
     * Renames a key in the preserving the original stored value. It is not 
     * permissible to add the replacement key if the original key is not found.
     * @param originalkey the original key name
     * @param replacementKey the new name for the key
     * @return true if the key could be renamed and false if there was no 
     * key found matching originalKey name. A false leaves the table unaltered.
     */
    boolean renameKey(String originalkey, String replacementKey);
    
    /**
     * Returns all the column names from within the table.
     * @return 
     */
    Set<String> getKeys();
    
    /**
     * Removes an entry from the user table. Specially used for libraries that 
     * don't use USER_PREFERED_NAME and the like. This method stops the key and 
     * value from being written in the table by removing them.
     * @param key
     * @return true if the key was found, and false otherwise. Any value stored 
     * at the key will be deleted from the table.
     */
    boolean deleteValue(String key);
}

