/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either httpVersion 2 of the License, or
 * (at your option) any later httpVersion.
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

package mecard.sirsidynix.sdapi;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * An update customer response object.
 * @author Andrew Nisbet andrew (@) dev-ils.com
 */
/**
 *
 * @author anisbet
 */
public class SDapiCustomerUpdateResponse extends SDapiResponse
{
    /*
    {
        "resource": "/user/patron",
        "key": "2139681",
        "fields": {
            "displayName": "Norton, Andre",
            "access": {
                "resource": "/policy/userAccess",
                "key": "PUBLIC"
            },
            "alternateID": "",
            "barcode": "21221031494957",
            "birthDate": null,
            "checkoutLocation": {
                "resource": "/policy/location",
                "key": "CHECKEDOUT"
            },
            "createdDate": "2024-10-29",
            "department": "",
            "environment": {
                "resource": "/policy/environment",
                "key": "PUBLIC"
            },
            "firstName": "Andre",
            "language": {
                "resource": "/policy/language",
                "key": "ENGLISH"
            },
            "lastName": "Norton",
            "library": {
                "resource": "/policy/library",
                "key": "EPLWMC"
            },
            "middleName": "",
            "preferredName": "",
            "privilegeExpiresDate": null,
            "profile": {
                "resource": "/policy/userProfile",
                "key": "EPL_ADULT"
            },
            "suffix": "",
            "title": "",
            "usePreferredName": false,
            "webAuthID": "",
            "category01": null,
            "category02": null,
            "category03": null,
            "category04": null,
            "category05": null,
            "category06": null,
            "category07": null,
            "category08": null,
            "category09": null,
            "category10": null,
            "category11": null,
            "category12": null,
            "claimsReturnedCount": 0,
            "standing": {
                "resource": "/policy/patronStanding",
                "key": "OK"
            },
            "groupId": "",
            "circRecordCount": 0,
            "keepCircHistory": "NOHISTORY"
        }
     }
    
    Failed: TODO Find failed response and add it here.
    */

    @Override
    public boolean succeeded() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String errorMessage() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    // Parsing method
    public static SDapiResponse parseJson(String jsonString) 
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiUserPatronLoginResponse.class);
    }
}
