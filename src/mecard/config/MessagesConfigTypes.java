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
 * These are custom messages libraries can tailor as return messages to customers.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public enum MessagesConfigTypes
{
    SUCCESS_JOIN("success-join"),
    SUCCESS_UPDATE("success-update"),
    ACCOUNT_NOT_FOUND("account-not-found"),
    ACCOUNT_NOT_CREATED("account-not-created"),
    ACCOUNT_NOT_UPDATED("account-not-updated"),
    USERID_PIN_MISMATCH("userid-pin-mismatch"),
    UNAVAILABLE_SERVICE("unavailable-service"),
    FAIL_METRO_POLICY("fail-metro-policy"), // Is not valid, or goodstanding, or underage...
    FAIL_LOCAL_POLICY("fail-local-policy"), // Could load user because customer failed local policy tests.
    FAIL_MIN_AGE_TEST("fail-min-age"),
    FAIL_RECIPROCAL_TEST("fail-reciprocal"),
    FAIL_RESIDENCY_TEST("fail-residency"),
    FAIL_GOODSTANDING_TEST("fail-goodstanding"),
    FAIL_LOSTCARD_TEST("fail-lostcard"),
    FAIL_EMAIL_TEST("fail-email"),
    FAIL_EXPIRY_TEST("fail-expiry"),
    FAIL_COMPLETENESS_TEST("fail-incomplete-information");
    
    private String type;
    
    private MessagesConfigTypes(String s)
    {
        this.type = s;
    }
    
    @Override
    public String toString()
    {
        return this.type;
    }
}
