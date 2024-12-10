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

/**
 *
 * @author anisbet
 */
public class SDapiJsonCustomerExistsResponse extends SDapiJsonResponse
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
    public static SDapiJsonResponse parseJson(String jsonString) 
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiJsonCustomerExistsResponse.class);
    }
}
