/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Andrew Nisbet
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

import api.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import mecard.customer.Customer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class ResponseSerializer implements JsonSerializer<Response>
{

    @Override
    public JsonElement serialize(Response response, Type type, JsonSerializationContext context)
    {
        final JsonObject json = new JsonObject();
//        private ResponseTypes code;
//        private String responseMessage;
//        private Customer customer;
        json.addProperty("code", response.getCode().name());
        json.addProperty("responseMessage", response.getMessage());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Customer.class, new CustomerSerializer());
        Gson gson = gsonBuilder.create();
        String jsonCustomer = gson.toJson(response.getCustomer());
        json.addProperty("customer", jsonCustomer);
        return json;
    }

    /**
     *
     * @param response the value of responseObj
     */
    public String getSerializedResponse(Response response)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Response.class, new ResponseSerializer());
        Gson gson = gsonBuilder.create();
        return gson.toJson(response);
    }

}
