/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2023 - 2024 Edmonton Public Library
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
 * @author Andrew Nisbet andrew(at)dev-ils.com
 */
public enum PapiPropertyTypes
{
    // local directory where the customers will be stored before loading.
    LOAD_DIR("load-dir"), // Directory where to find customer files to load and storage for loaded customers.
    // These are URI components that need to be set in advance.
    PAPI_VERSION("papi-version"), // Required because iii removes functions in minor versions.
    // Difference of timezone from web services server timezone.
    // If the request-hash's time is not within a few minutes of the webservices 
    // server time, the request will fail with a 'Unreachable code' error '-1'.
    ME_SERVER_TIME_ZONE_DIFFERENCE("timezone-difference"),
    HOST("host"), // host for PAPI service.
    REST_PATH("rest-path"),
    API_VERSION("api-version"),
    LANGUAGE_ID("language-id"),
    APP_ID("app-id"), 
    ORG_ID("org-id"),
    // The domain of the web services ie, 'PAPIService/REST/public'.
    INTERNAL_DOMAIN("internal-domain"),
    // PAPI Access key id used for authoriziation supplied by Polaris and used
    // in the HTTP header 'Authorization: PWS [api-user-id]:[signature]
    API_USER_ID("api-user-id"),
    API_KEY("api-key"),
    STAFF_ID("staff-access-id"),
    STAFF_PASSWORD("staff-password"),
    HTTP_VERSION("http-version"), // "1.1" or "2.0".         
    // these are default values applied to a patron on creation
    LOGON_BRANCH_ID("login-branch-id"),
    LOGON_USER_ID("login-user-id"),
    LOGON_WORKSTATION_ID("login-workstation-id"),
    DELIVERY_OPTION_ID("delivery-option-id"),
    ERECEIPT_OPTION_ID("ereceipt-option-id"),
    PATRON_BRANCH_ID("patron-branch-id"),
    PATRON_CODE_ID("patron-code-id"),      // This is equivelant to SD's 'profile'
    CONNECTION_TIMEOUT("connection-timeout"),
    DEBUG("debug"),
    SERVER_TYPE("server-type");
    // These are optional values, here for reference. Don't uncomment unless
    // you want all sites to require these config parameters. If you do decide
    // revisit the code in PolarisPatronPasswordChecker.java.
//    PASSWORD_MAX_LENGTH("password-max-length"),
//    PASSWORD_MIN_LENGTH("password-min-length"),
//    PASSWORD_ALLOWED_CHARS("allowed-password-characters");
    
    private final String type;

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
