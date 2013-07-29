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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import mecard.customer.Customer;
import mecard.config.CustomerFieldTypes;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class CustomerDeserializer implements JsonDeserializer<Customer>
{

    @Override
    public Customer deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
        Customer customer = new Customer();
        JsonObject jsonObject = json.getAsJsonObject();
        for (CustomerFieldTypes cTypes: CustomerFieldTypes.values())
        {
            customer.set(cTypes, jsonObject.get(cTypes.toString()).getAsString());
        }
        return customer;
    }

}
