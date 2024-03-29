/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2020  Edmonton Public Library
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
package mecard.polaris.sql;

/**
 * Convenience class for storing both data and data type for SQL statements.
 * 
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class SQLData
{
    public enum Type
    {
        STRING,
        DATE,
        INT,
        MONEY,
        BIT,
        TIMESTAMP,
        TIMESTAMP_NOW,
        SMALL_INT,
        CHAR,
        TINY_INT,
        STORED_PROCEEDURE,
        RESULT_SET;
    }
    
    // Data name.
    protected final String name;
    protected final Type dataType;
    
    /**
     * Constructor that takes the name of the data, and it's SQL type.
     * @param name
     * @param dType 
     */
    public SQLData(String name, Type dType)
    {
        this.name = name;
        this.dataType = dType;
    }

    /**
     * Returns the SQL type of this data supplied at the time of creation.
     * @return type of the data stored.
     */
    public Type getType()
    {
        return this.dataType;
    }

    /**
     * The name of the data.
     * @return the data's name property.
     */
    public String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return this.name + " " + this.dataType.name();
    }
}
