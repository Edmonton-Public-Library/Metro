/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2019  Edmonton Public Library
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
 * Supported versions of Polaris. Default is version 5.6 and coded in polaris_sql.properties 
 * as 'default'.
 * @author Andrew Nisbet andrew.nisbet@epl.ca
 */
public enum PolarisVersion
{
    DEFAULT,     // Literally 'default' which is equivilent to Polaris 5.6.
    SIX_DOT_TWO_ONWARD, // Polaris 6.2 and up.
    SEVEN_DOT_ONE; // Polaris 7.0 and 7.1 and up.
}
