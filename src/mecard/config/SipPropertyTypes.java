/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Andrew Nisbet
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
 * Mandatory properties of the sip2_config.xml file. Any entries added here 
 * will be validated when the config file is read, they don't need to be set.
 * @author metro
 */
public enum SipPropertyTypes
{

    HOST("host"),
    PORT("port"), 
    USER("sip-user"), 
    PASSWORD("password"), 
    TIMEOUT("timeout"), 
    INSTITUTION_ID("institution-id");
    
    private String type;

    private SipPropertyTypes(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
