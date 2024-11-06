/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013 - 2024 Edmonton Public Library
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
 *
 * @author Andrew Nisbet <andrew@dev-ils.com>
 */
public enum ConfigFileTypes
{
    SYMPHONY,   // Configuration related to Symphony ILS only.
    ENVIRONMENT,  // General configuration for the application.
    SIP2,      // SIP2 settings.
    BIMPORT,   // Maps bimport settings.
    BIMPORT_CITY_MAPPING, // Maps city names to codes for Horizon users.
    PAPI,      // Polaris API
    SIRSIDYNIX_API,       // SirsiDynix API
    POLARIS_SQL,   // Used for general POLARIS_SQL API transactions.
    DEBUG, // used with DummyResponder, this file contains canned results to return to client.
    VARS,  // System variables like PATH, UPATH, etc.
    REGIONAL_NAMES_CONFIG, // Configuration on where to get a fresh region names property file.
    REGIONAL_NAMES, // List of sanctioned regional names XML properties file.
    MESSAGES;  // Custom messages for each library.
}
