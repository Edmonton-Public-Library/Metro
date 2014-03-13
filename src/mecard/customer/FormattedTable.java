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
package mecard.customer;


public interface FormattedTable
{

    /**
     * Creates matching.
     * @return properly formatted bimport data string for the table this represents.
     */
    String getData();

    /**
     *
     * @return properly formatted bimport header string for the table this represents.
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
     * Renames a key in the preserving the original stored value if any.
     * @param originalkey the original key name
     * @param replacementKey the new name for the key
     * @return true if the key could be renamed and false if there was no 
     * key found matching originalKey name. A false leaves the table unaltered.
     */
    boolean renameKey(String originalkey, String replacementKey);
    
}

