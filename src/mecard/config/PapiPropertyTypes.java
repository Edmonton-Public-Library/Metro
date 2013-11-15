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
 * Mandatory property types of the Polaris config file. Note that there are
 * fields 
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public enum PapiPropertyTypes
{
    LOAD_DIR("load-dir"), // Directory where to find customer files to load and storage for loaded customers.
    LOGIN_BRANCH_ID("login-branch-id"),
    LOGIN_USER_ID("login-user-id"),
    LOGIN_WORKSTATION_ID("login-workstation-id"),
    PATRON_BRANCH_ID("patron-branch-id"),
    HOST("host"), // host for PAPI service.
    VERSION("version"), 
    LANGUAGE_ID("language-id"), 
    APP_ID("app-id"), 
    ORG_ID("org-id");        
    
    private String type;

    private PapiPropertyTypes(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
