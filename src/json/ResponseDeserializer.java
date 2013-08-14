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

import mecard.Request;
import mecard.Response;
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
import mecard.ResponseTypes;
import mecard.customer.Customer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class ResponseDeserializer implements JsonDeserializer<Response>
{

    @Override
    public Response deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
        Response request = new Response();
        JsonObject jsonObject = json.getAsJsonObject();
//        private ResponseTypes code;
//        private String responseMessage;
//        private Customer customer;
        request.setCode(ResponseTypes.valueOf(jsonObject.get("code").getAsString()));
        request.setResponse(jsonObject.get("responseMessage").getAsString());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Customer.class, new CustomerDeserializer());
        Gson gson = gsonBuilder.create();
        Reader data = new StringReader(jsonObject.get("customer").getAsString());
        Customer customer = gson.fromJson(data, Customer.class);
        request.setCustomer(customer);
        return request;
    }

    public Response getDeserializedResponse(String requestString)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Response.class, new ResponseDeserializer());
        Gson gson = gsonBuilder.create();
        Reader data = new StringReader(requestString);
        return gson.fromJson(data, Response.class);
    }

}
