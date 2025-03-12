/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013 - 2025  Edmonton Public Library
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
 * Mandatory property types of the Symphony configuration file. Note that there are
 * fields non-mandatory fields:
 * <ol>
 * <li> "load-dir" required directory where flat user files are loaded from.</li>
 * <li> "ssh" [user]@[someserver.com] if you want to run your commands via SSH.</li>
 * </ol>
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public enum SymphonyPropertyTypes
{
    // Defined in PropertyReader, but if used here SymphonyNormalizer thinks it is a specific 
    // value for customer default values, and includes it in the flat file. STILL MANDITORY!
    //LOAD_DIR("load-dir"),     // No longer optional, optional field Directory where to find customer files to load.
    // This list is only Symphony specific properties for customers.
    USER_LIBRARY("USER_LIBRARY"),
    USER_PROFILE("USER_PROFILE"),
    USER_PREFERED_LANGUAGE("USER_PREF_LANG"),
    USER_STATUS("USER_STATUS"),
    USER_ROUTING_FLAG("USER_ROUTING_FLAG"),
    USER_CHARGE_HISTORY_RULE("USER_CHG_HIST_RULE"),
    USER_ACCESS("USER_ACCESS"),
    USER_ENVIRONMENT("USER_ENVIRONMENT");
    
    private final String type;

    private SymphonyPropertyTypes(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
