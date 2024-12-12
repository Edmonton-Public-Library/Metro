/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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
 * @author anisbet
 */
public enum SDapiUserFields
{
    //       "barcode": "21221012345678",
    USER_ID("barcode"),
    USER_KEY("key"), // 301585
    //    "displayName": "BILLY, Balzac",
    DISPLAY_NAME("displayName"),
    //       "access": 
    //           "key": "PUBLIC"
    ACCESS("access"),
    //       "alternateID": "",
    ALTERNATE_ID("alternateID"),
    //       "birthDate": "2000-02-29",
    BIRTHDATE("birthDate"),
    //"checkoutLocation": 
    //    "key": "CHECKEDOUT"
    CHECKOUT_LOCATION("checkoutLocation"),
    //"createdDate": "2003-11-24",
    CREATE_DATE("createdDate"),
    //"department": "PROD",
    DEPARTMENT("department"),
    //"environment": 
    //    "key": "PUBLIC"
    ENVIRONMENT("environment"),
    //"firstName": "Balzac",
    USER_FIRST_NAME("firstName"),
    //"language": 
    //    "key": "ENGLISH"
    LANGUAGE("language"),
    //"lastName": "BILLY",
    LAST_NAME("lastName"),
    //"library": 
    //    "key": "EPLMNA"
    LIBRARY("library"),
    //"middleName": "",
    MIDDLE_NAME("middleName"),
    //"preferredName": "",
    PREFERRED_NAME("preferredName"),
    //"privilegeExpiresDate": null,
    PRIVILEGE_EXPIRES_DATE("privilegeExpiresDate"),
    //"profile": 
    //    "key": "EPL_ADULT"
    PROFILE("profile"),
    //"suffix": "",
    SUFFIX("suffix"),
    //"title": "",
    TITLE("title"),
    //"usePreferredName": false,
    USE_PREFERRED_NAME("usePreferredName"),
    //"webAuthID": "",
    WEB_AUTH_ID("webAuthID"),
    CATEGORY01("category01"), //: null,
    CATEGORY02("category02"), 
    //    "key": "F"
    CATEGORY03("category03"),
    CATEGORY04("category04"),
    //    "key": "NNELS"
    CATEGORY05("category05"), 
    //    "key": "ECONSENT"
    CATEGORY06("category06"),
    CATEGORY07("category07"),
    CATEGORY08("category08"),
    CATEGORY09("category09"),
    CATEGORY10("category10"),
    CATEGORY11("category11"),
    CATEGORY12("category12"),
    CLAIMS_RETURNED_COUNT("claimsReturnedCount"),
    STANDING("standing"), 
    GROUP_ID("groupId"),
    //"address1":
    //                "key": "ZIP"
    ZIP("ZIP"),
    //                "key": "EMAIL"
    EMAIL("EMAIL"),
    //                "key": "POSTALCODE"
    POSTALCODE("POSTALCODE"),
    //                "key": "PHONE"
    PHONE("PHONE"),
    //                "key": "CARE/OF"
    CARE_SLASH_OF("CARE/OF"),
    //                "key": "STREET"
    STREET("STREET"),
    //                "key": "CITY/STATE"
    CITY_SLASH_STATE("CITY/STATE"),
    PROV("PROV"),
    CITY_SLASH_PROV("CITY/PROV"),
    //"circRecordCount": 0,
    CIRC_RECORD_COUNT("circRecordCount"),
    //"circRecordList": [],
    CIRC_RECORD_LIST("circRecordList"),
    //"keepCircHistory": "ALLCHARGES"
    KEEP_CIRC_HISTORY("keepCircHistory");
    
    private final String type;

    private SDapiUserFields(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
