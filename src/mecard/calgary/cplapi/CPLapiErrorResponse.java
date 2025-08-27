/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025  Edmonton Public Library
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

package mecard.calgary.cplapi;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

/**
 *
 * @author anisbet
 */
public class CPLapiErrorResponse extends CPLapiResponse
{
    //    {
    //      "type": "https://tools.ietf.org/html/rfc7231#section-6.5.1",
    //      "title": "One or more validation errors occurred.",
    //      "status": 400,
    //      "traceId": "400006a3-0000-be00-b63f-84710c7967bb",
    //      "errors": {
    //          "CardNumber/PinNumber": [
    //              "Invalid Credentials."
    //          ]
    //      }
    //    }
    
    // Static method to parse JSON
    public static CPLapiErrorResponse parseJson(String jsonString) 
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, CPLapiErrorResponse.class);
    }
    
    @Override
    public boolean succeeded() 
    {
        // If there are no errors then the response was not an error response.
        return getErrors() == null;
    }

    @Override
    public String errorMessage() 
    {
        StringBuilder sb = new StringBuilder();
        // Access errors
        if (getErrors() != null) 
        {
            getErrors().forEach((key, value) -> {
                sb.append(key).append(": ").append(value);
            });
        }
        return sb.toString();
    }
    
    
    
//    // Constructors
//    public CPLapiErrorResponse() 
//    {}
//    
//    public CPLapiErrorResponse(String type, String title, int status, String traceId, Map<String, List<String>> errors) 
//    {
//        this.type = type;
//        this.title = title;
//        this.status = status;
//        this.traceId = traceId;
//        this.errors = errors;
//    }
    @SerializedName("type")
    private String type;
    @SerializedName("title")
    private String title;
    @SerializedName("status")
    private String httpStatus;
    @SerializedName("traceId")
    private String traceId;
    private Map<String, List<String>> errors;
    
    public String getType() { return this.type != null ? this.type : ""; }
    public String getTitle() { return this.title != null ? this.title : ""; }
    public String getTraceId() { return this.traceId != null ? this.traceId : ""; }
    
    public String getStatus()
    {
        int result;
        try
        {
            result = Integer.parseInt(this.httpStatus);
        }
        catch (NumberFormatException e)
        {
            result = 0;
        }
        return result == 0 ? "" : this.httpStatus;
    }
    
    private Map<String, List<String>> getErrors() 
    {
        return errors;
    }
    
    @Override
    public String toString() 
    {
        return """
               CPLapiErrorResponse {
                type='""" + type + "\'" +
                ",\n title='" + title + "\'" +
                ",\n status=" + httpStatus +
                ",\n traceId='" + traceId + "\'" +
                ",\n errors=" + errors +
                "\n}";
    }
}
