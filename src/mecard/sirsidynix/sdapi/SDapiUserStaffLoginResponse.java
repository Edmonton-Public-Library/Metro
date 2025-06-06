/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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
public class SDapiUserStaffLoginResponse extends SDapiResponse
{
    //    {
    //      "staffKey": "776715",
    //      "pinCreateDate": "2024-03-26",
    //      "pinExpirationDate": null,
    //      "name": "Web Service Requests for Online Registration",
    //      "sessionToken": "331789a2-e11d-4f77-ae80-3e4bd216d080",
    //      "pinStatus": {
    //          "resource": "/policy/userPinStatus",
    //          "key": "A",
    //          "fields": {
    //              "policyNumber": 1,
    //              "description": "$<userpin_active_status>",
    //              "displayName": "A",
    //              "translatedDescription": "User's PIN is active"
    //          }
    //      },
    //      "message": null
    //    }
    @SerializedName("sessionToken")
    private String sessionToken;

    @SerializedName("message")
    private String message;
    
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
    public static SDapiResponse parseJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiUserStaffLoginResponse.class);
    }

    public String getSessionToken() 
    {
        if (sessionToken == null)
            return "";
        return sessionToken;
    }
    
    @Override
    public boolean succeeded() 
    {
        return sessionToken != null;
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

}
