/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2020  Edmonton Public Library
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
package json;

import mecard.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import mecard.QueryTypes;
import mecard.customer.Customer;

/**
 * Converts customer and request from JSON to Request and customer objects.
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public class RequestDeserializer implements JsonDeserializer<Request>
{
    
    @Override
    public Request deserialize(JsonElement json, 
            Type type, 
            JsonDeserializationContext jdc) 
            throws JsonParseException
    {
        Request request = new Request();
        JsonObject jsonObject = json.getAsJsonObject();
//        private QueryTypes code;
//        private String authorityToken;
//        private Customer customer;
//        private String pin;
//        private String userId;
        request.setCode(QueryTypes.valueOf(jsonObject.get("code").getAsString()));
        request.setAuthorityToken(jsonObject.get("authorityToken").getAsString());
        request.setUserId(jsonObject.get("userId").getAsString());
        request.setPin(jsonObject.get("pin").getAsString());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Customer.class, new CustomerDeserializer());
        Gson gson = gsonBuilder.create();
        Reader data = new StringReader(jsonObject.get("customer").getAsString());
        Customer customer = gson.fromJson(data, Customer.class);
        request.setCustomer(customer);
        return request;
    }

    /**
     *
     *
     * @param requestString the request String.
     * @return Request object from request string.
     */
    public Request getDeserializedRequest(String requestString)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Request.class, new RequestDeserializer());
        Gson gson = gsonBuilder.create();
        Reader data = new StringReader(requestString);
        return gson.fromJson(data, Request.class);
    }

}
