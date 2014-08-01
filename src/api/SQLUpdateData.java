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
package api;

/**
 * Manages data type expectations for SQL queries.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class SQLUpdateData extends SQLData
{
    private String value; 
    public SQLUpdateData(String name, SQLData.Type dType, String value)
    {
        super(name, dType);
        this.value = value;
    }

    /**
     * 
     * @return stored value which may be null.
     */
    public String getValue()
    {
        return this.value;
    }

    @Override
    public String toString()
    {
        if (this.value == null)
        {
            return this.name + " is null";
        }
        if (this.dataType == Type.INT)
        {
            return this.name + "=" + this.value;
        }
        return this.name + " = \"" + this.value + "\"";
    }    
}
