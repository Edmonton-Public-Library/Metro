/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024 - 2025  Edmonton Public Library
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
 * These are tags found in the sdapi.properties file.
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public enum SDapiPropertyTypes 
{
    CLIENT_ID("client-id"),
    ENV("env-file-path"),
    CACHE_PATH("cache-path"),
    BASE_URL("base-url"),
    // DO NOT uncomment the following unless you intend to make it a required field.
    // PORT("port"), Optional in sdapi.properties. Including it here will throw an exception if not set.
    // These are necessary so web services knows to populate the policy/cityState
    // or cityProvince fields. The default is cityState.
    // USE_CITY_SLASH_PROVINCE("use-city-province"), // TODO Implement. 
    WEB_APP("x-sirs-clientId"),
    SD_ORIGINATING_APP_ID("sd-originating-app-id"),
    SESSION_TOKEN_EXPIRY_TIME("session-token-expire-time"),
    WEB_SERVICE_VERSION("web-service-version"),
    CONNECTION_TIMEOUT("connection-timeout"),
    HTTP_VERSION("http-version"),
    DEBUG("debug");
    
    private final String type;

    private SDapiPropertyTypes(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
