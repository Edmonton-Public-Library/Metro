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
 * A authentication response object.
 * @author anisbet
 */
public class SDapiCustomerCreateResponse extends SDapiResponse 
{

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
