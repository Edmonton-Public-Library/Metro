/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2021 - 2024  Edmonton Public Library
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
package mecard.requestbuilder;

/**
 * Every command that Metro executes must have a strategy defined for how it will
 * execute that command. There are just four strategies for satisfying requests 
 * from the metro server. These values have to be entered in the environment 
 * configuration XML file.
 * @author andrew (at) dev-ils.com
 */
public enum ResponderMethodTypes
{
    SYMPHONY_API("symphony-api"), // Command line API through SSH.
    SIP2("sip2"),
    BIMPORT("bimport"), 
    DEBUG("dummy"),   // Used for testing.
    OUTAGE("outage"), // Used for planned outage. Sets the server into loopback mode.
    SIRSIDYNIX_API("sirsidynix-api"), // Symphony & Horizon web services.
    POLARIS_API("polaris-api"), // restful service.
    POLARIS_SQL("polaris-sql"); // Polaris SQL 
    
    private final String type;
    
    private ResponderMethodTypes(String t)
    {
        this.type = t;
    }
    
    @Override
    public String toString()
    {
        return this.type;
    }
}
