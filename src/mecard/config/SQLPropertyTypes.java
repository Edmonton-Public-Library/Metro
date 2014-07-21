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
package mecard.config;

/**
 * Basic values needed to make a connection with a JDBC.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public enum SQLPropertyTypes
{
    // local directory where the customers will be stored before loading.
    LOAD_DIR("load-dir"), // Directory where to find customer files to load and storage for loaded customers.
    HOST("host"),
    CONNECTOR_TYPE("connector-type"), // 'sql_server, or mysql so far
    DATABASE("database"),
    USERNAME("username"),
    PASSWORD("password");
    
    private String type;

    private SQLPropertyTypes(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
