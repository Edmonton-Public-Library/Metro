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
 * Member types are the current members of Metro Federation.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public enum MemberTypes
{
    EPL, // Edmonton Public Library
    STA, // St. Alberta Public Library
    STR, // Strathcona County Library / Fort Saskatchewan Public Library
    PKL, // Parklands Regional Library
    SLS, // Shortgrass Library System
    CAR, // Chinook Arch Regional Library System
    TRC; // Trac including Yellowhead.
}
