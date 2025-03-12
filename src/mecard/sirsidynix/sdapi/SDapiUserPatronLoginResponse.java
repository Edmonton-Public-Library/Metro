/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024 - 2025 Edmonton Public Library
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
 * A authentication response object.
 * @author anisbet
 */
public class SDapiUserPatronLoginResponse extends SDapiResponse
{
    //    {
    //    "pinCreateDate": "2024-03-26",
    //    "pinExpirationDate": null,
    //    "customerName": "BILLY, Balzac",
    //    "sessionToken": "42c08d87-61d0-475a-8480-ddaa05b60506",
    //    "pinStatus": {
    //        "resource": "/policy/userPinStatus",
    //        "key": "A",
    //        "fields": {
    //            "policyNumber": 1,
    //            "description": "$<userpin_active_status>",
    //            "displayName": "A",
    //            "translatedDescription": "User's PIN is active"
    //        }
    //    },
    //    "patronKey": "301585",
    //    "message": null
    //    }
    @SerializedName("sessionToken")
    private String sessionToken;

    @SerializedName("message")
    private String message;
    
    @SerializedName("patronKey")
    private String userKey;

    @SerializedName("customerName")
    private String customerName;
    
    // Failed
    //    {
    //        "messageList": [
    //            {
    //                "code": "unableToLogin",
    //                "message": "Unable to log in."
    //            }
    //        ]
    //     }
    // If there was an error response
    @SerializedName("messageList")
    private List<ErrorMessage> messageList;
    
    public static class ErrorMessage {

        @SerializedName("message")
        private String message;
        
        public String getMessage()
        {
            return message;
        }
    }
    
    // Parsing method
    public static SDapiResponse parseJson(String jsonString) 
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiUserPatronLoginResponse.class);
    }

    public String getSessionToken() 
    {
        if (sessionToken == null)
            return "";
        return sessionToken;
    }
    
    public String getUserKey() 
    {
        if (userKey == null)
            return "";
        return userKey;
    }
    
    public String getCustomerName() 
    {
        if (customerName == null)
            return "";
        return customerName;
    }
    
    @Override
    public boolean succeeded() 
    {
        if (this.userKey == null)
            return false;
        return ! this.userKey.isEmpty();
    }
    
    @Override
    public String errorMessage() 
    {
        StringBuilder sout = new StringBuilder();
        if (message != null)
            sout.append(message).append("\n");
        if (messageList != null)
        {
            for (ErrorMessage em: messageList)
            {
                sout.append(em.getMessage()).append("\n");
            }
        }
        return sout.toString();
    }
    
    @Override
    public String toString()
    {
        try
        {
            return this.userKey;
        }
        catch (IndexOutOfBoundsException e)
        {
            return "";
        }
    }
}
