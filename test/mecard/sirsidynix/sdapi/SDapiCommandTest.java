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

import org.junit.Test;

import static org.junit.Assert.*;

import api.CommandStatus;
import java.util.Properties;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiPropertyTypes;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 *
 * @author anisbet
 */
public class SDapiCommandTest 
{
    
    private final Properties sdapiProperties;

    public SDapiCommandTest() 
    {
        this.sdapiProperties = PropertyReader.getProperties(ConfigFileTypes.SIRSIDYNIX_API);
        JsonObject userGson = new JsonObject();
        userGson.addProperty("name", "John Doe");
        userGson.addProperty("age", 30);
        userGson.addProperty("isEmployee", true);
        
        // Pretty print the JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Simple GSON example:");
        System.out.println(gson.toJson(userGson));
    }

    /**
     * Test of execute method, of class SDapiCommand.
     */
    @Test
    public void testExecute() {
//        System.out.println("==execute==");
////        POST: https://{{HOST}}/{{WEBAPP}}/user/staff/login
////        Body: {
////        "login": "{{staffId}}",
////        "password": "{{staffPassword}}"
////        }
//
//        SDapiCommand command = new SDapiCommand.Builder(sdapiProperties, "POST")
//            .endpoint("/user/staff/login")
//            .build();
//        CommandStatus result = command.execute();
//        System.out.println("login toString: '" + command.toString() + "'");
//        System.out.println("Authentication OUTPUT: '" + result.getStdout() + "'");
//        assertNotNull(result);
//        assertTrue(result.getStatus() == ResponseTypes.SUCCESS);
    }

    /**
     * Test of toString method, of class SDapiCommand.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        System.out.println("==execute==");
//        POST: https://{{HOST}}/{{WEBAPP}}/user/staff/login
//        Body: {
//        "login": "{{staffId}}",
//        "password": "{{staffPassword}}"
//        }

        SDapiCommand command = new SDapiCommand.Builder(sdapiProperties, "POST")
            .endpoint("/user/staff/login")
            .build();
        CommandStatus result = command.execute();
        System.out.println("login toString: '" + command.toString() + "'");
//        assertNotNull(result);
//        assertTrue(result.getStatus() == ResponseTypes.SUCCESS);
    }
    
}
