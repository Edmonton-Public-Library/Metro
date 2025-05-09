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

import java.util.List;

/**
 * Implementers normalize and format customer data into a format consumable
 * by the resource that is loading or updating the information (like SQL, or PAPI). 
 * This class is responsible for acquiring the correct formatter, formatting 
 * the customer and managing the customer's record entries during the creation 
 * of a new record on the ILS.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public abstract class MeCardCustomerToNativeFormat
{
    protected boolean debug;
    
    public void setDebug(boolean d)
    {
        this.debug = d;
    }
    
    /**
     * 
     * @return list of strings that represents the customer in the form that
     * can be loaded by a ILS.
     */
    public abstract List<String> getFormattedCustomer();
    
    /**
     * 
     * @return list of strings that represents the customer's meta data if any,
     * can be loaded by a ILS.
     */
    public abstract List<String> getFormattedHeader();
    
    /**
     * Sets a value denoted by key to the value passed in the parameter.
     * This assumes that either the customer data is in a single table by default
     * or that the implementer has implemented a search of all tables if required.
     * 
     * @param key
     * @param value
     * @return true if the value was present and updated and false if the value
     * was not present, but was added.
     */
    public abstract boolean setValue(String key, String value);
    
    /**
     * Inserts a given key value pair into an existing table.
     * @param tableName
     * @param key
     * @param value
     * @return true if the table was found and the key value pair inserted and 
     * false otherwise.
     */
    public abstract boolean insertValue(String tableName, String key, String value);
    
    /**
     * 
     * @param key
     * @return true if the key was found in the formatted customer account and
     * false otherwise.
     */
    public abstract boolean containsKey(String key);
    
    /**
     * Returns the value stored in the entry denoted by the parameter key.
     * @param key
     * @return the value stored by key or an empty string if the key was not found.
     */
    public abstract String getValue(String key);
   
    /**
     * Inserts a Formatted table into the formatted customer object.
     * @param formattedTable the value of formattedTable
     * @param index the value of index
     */
    public abstract void insertTable(MeCardDataToNativeData formattedTable, int index);
    
    /**
     * Renames a field in a table. This can be useful when an ILS has customized
     * customer field names. This would have limited value in Horizon but in 
     * Symphony you could specify that the default field {@link mecard.config.FlatUserFieldTypes#CITY_STATE}
     * could be renamed to {@link mecard.config.FlatUserFieldTypes#CITY_PROVINCE}.
     * 
     * @param tableName Name of the table that contains the field to be renamed.
     * @param originalFieldName String - the original name of the field.
     * @param newFieldName  String - the new name for the field.
     * @return true if the specified field in the specified table could be found
     * and it was renamed to newFieldName, and false otherwise.
     */
    public abstract boolean renameField(String tableName, String originalFieldName, String newFieldName);
    
    /**
     * Removes unused fields from user data before loading.
     * @param tableName table where the field can be found.
     * @param fieldName name of the field to user case sensitive.
     * @return true if the field was removed and false if it couldn't be found.
     */
    public abstract boolean removeField(String tableName, String fieldName);
}
