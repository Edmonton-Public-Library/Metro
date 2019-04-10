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
 * Basic values needed to make a connection with a JDBC, and default values
 * for tables used during customer registration or update.
 * @author Andrew Nisbet <andrew.nisbet@epl.ca>
 */
public enum PolarisSQLPropertyTypes
{
    // local directory where the customers will be stored before loading.
    LOAD_DIR("load-dir"), // Directory where to find customer files to load and storage for loaded customers.
    HOST("host"), // IP or DNS name of the database server.
    CONNECTOR_TYPE("connector-type"), // 'sql_server, or mysql so far
    DATABASE("database"),        // The name of the database like "Polaris"
    USERNAME("username"),        // User name for the ILS database.
    PASSWORD("password"),        // password for the ILS database
    // Polaris specific default values.
    PATRON_CODE_ID("patron-code-id"), // PatronCodeID the type of patron or profile of the patron as an integer. Like 25.
    ORGANIZATION_ID("organization-id"), // The library the user will belong to. An integer value like 303.
    CREATOR_ID("creator-id"),           // Integer id value of the entity creating customers in the ILS database. Like 1831.
    LANGUAGE_ID("language-id"),         // Integer value of the customer's default language; English = 1.
    DELIVERY_OPTION_ID("delivery-option-id"), // Integer value of default notice delivery method. email = 2.
    EMAIL_FORMAT_ID("email-format-id"),       // Not sure but the default value is 2.
    COUNTRY_ID("country-id"),         // Default country ID of the customer, Cananda = 2. Guess who is 1?
    // Free text lable string; "Home" might be a good choice. In Polaris 6.2 PatronAddresses removed the 
    // next column and replaced it with an column called AddressLabelID, which can't be NULL because it's
    // a foriegn key in the AddressLabels table. From the perspective of configuration
    // this value has changed where in 6.1 it was 'Home', the field in 6.2 takes
    // an integer values of 1, where 1='Home' in Table.AddressLabels.
    ///// These become one-or-the-other required. depending on version of ILS so I'm making them
    // optional, that is, both are an option but one or the other is required, so both must be
    // defined in the polaris_sql.properties file. free-text-label should be 'Home', address-lable-id = 1.
    FREE_TEXT_LABEL("free-text-label"),   // *** Polaris 5.2
    ADDRESS_LABEL_ID("address-label-id"), // *** Polaris 6.2
    USER_1("user-1"),
    USER_2("user-2"),    // Case insensitive
    USER_3("user-3"),
    USER_4("user-4"),  // literally "(none)" will be added  to this category.
    USER_5("user-5");
    
    private String type;

    private PolarisSQLPropertyTypes(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
