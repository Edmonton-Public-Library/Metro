/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025 Edmonton Public Library
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
 * These are tags found in the cplapi.properties file.
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public enum CPLapiPropertyTypes 
{
    LOAD_DIR("load-dir"), // Directory where to find customer files to load and storage for loaded customers.
    ENV("env-file-path"),
    CACHE_PATH("cache-path"),
    HOST("host"),
    BASE_URL("base-url"),
    // DO NOT uncomment the following unless you intend to make it a required field.
    // PORT("port"), Optional in sdapi.properties. Including it here will throw an exception if not set.
    // API_KEY See .env file
    API_VERSION("api-version"),
    CONNECTION_TIMEOUT("connection-timeout"),
    HTTP_VERSION("http-version"),
    DEBUG("debug");
    // These will be added by CPL but confirm with Carolyn.
//    DEFAULT_USER_LIBRARY("default-user-library"),
//    DEFAULT_USER_PROFILE("default-user-profile"),
//    DEFAULT_CHARGE_HISTORY_RULE("default-charge-history-rule"),
//    DEFAULT_RESIDENCE("default-residence"),
//    DEFAULT_AUTO_RENEW("default-auto-renew"),
//    DEFAULT_NOTIFY_VIA("default-notify-via"),
//    DEFAULT_CONTACT_FOR_FUNDRAISING("default-contact-for-fundraising"),
//    DEFAULT_RECEIVE_LIBRARY_NEWS("default-receive-library-news");
    
//    DEFAULT_PROFILE("default-profile"),
//    DEFAULT_LIBRARY("default-library"),
//    DEFAULT_LANGUAGE("default-language"),
//    DEFAULT_STANDING("default-standing"),
//    DEFAULT_KEEP_CIRC_HISTORY("default-keep-circ-history"),
//    DEFAULT_ACCESS("default-access"),
//    DEFAULT_ENVIRONMENT("default-environment");
    
    private final String type;

    private CPLapiPropertyTypes(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
