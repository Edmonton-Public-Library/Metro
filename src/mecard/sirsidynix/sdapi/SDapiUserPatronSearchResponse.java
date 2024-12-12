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
package mecard.sirsidynix.sdapi;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author anisbet
 */
public class SDapiUserPatronSearchResponse extends SDapiResponse
{
    //{
    //   "searchQuery": "ID:21221012345678",
    //   "startRow": 1,
    //   "lastRow": 1,
    //   "rowsPerPage": 10,
    //   "totalResults": 1,
    //   "result": [
    //       {
    //           "resource": "/user/patron",
    //           "key": "301585"
    //       }
    //   ]
    //}
    @SerializedName("result")
    private List<ResultList> resultList;
    
    // Convienence class.
    private class ResultList
    {        
        @SerializedName("key")
        private String userKey;
    }
    
    /**
     * Gets the user's primary key.
     * @return ILS user key, or an empty string if the user was not found.
     */
    public String getUserKey()
    {
        return (resultList != null && resultList.size() >= 1) ? resultList.get(0).userKey : "";
    }
    
    // Failed response
    //{
    //   "searchQuery": "ID:212210123456789",
    //   "startRow": 1,
    //   "lastRow": 10,
    //   "rowsPerPage": 10,
    //   "totalResults": 0,
    //   "result": []
    //}
    
    @SerializedName("totalResults")
    private Integer searchCount;
    
    /**
     * Gets the number of users that matched the search criteria.
     * @return count of users found in query results.
     */
    public int getSearchCount()
    {
        if (searchCount == null)
            return 0;
        return searchCount;
    }

    @Override
    public boolean succeeded() 
    {
        return !(searchCount == null || searchCount < 1);
    }
    
    /**
     * Reports if a customer exists or not.
     * @return true if the customer of the search query was found and false
     * otherwise.
     */
    public boolean exists()
    {
        return this.succeeded();
    }

    @Override
    public String errorMessage() 
    {
        if (searchCount == null)
        {
            return "**error, while searching customer.";
        }
        if (searchCount < 1)
        {
            return "Account not found.";
        }
        return "";
    }
    
     /**
     * Parses the JSON response into a meaningful data object.
     * @param jsonString
     * @return 
     */
    public static SDapiResponse parseJson(String jsonString) 
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiUserPatronSearchResponse.class);
    }
}
