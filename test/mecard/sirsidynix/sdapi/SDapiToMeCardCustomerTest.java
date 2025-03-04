/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025 Edmonton Public Library
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
 */

package mecard.sirsidynix.sdapi;


import java.util.ArrayList;

import java.util.List;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class SDapiToMeCardCustomerTest 
{
    private String customerJson;

    public SDapiToMeCardCustomerTest() 
    {
        customerJson = """
                        {
                               "resource": "/user/patron",
                               "fields": {
                                   "barcode": "21221031494957",
                                   "lastName": "Norton",
                                   "firstName": "Andre",
                                   "library": {"resource":"/policy/library","key":"EPLMNA"},
                                   "profile": {"resource":"/policy/userProfile","key":"EPL_ADULT"},
                                   "address1": [{
                                             "@resource": "/user/patron/address1",
                                             "@key": "1",
                                             "code": {
                                                 "@resource": "/policy/patronAddress1",
                                                 "@key": "CITY/STATE"
                                             },
                                             "data": "Edmonton, AB"
                                         },
                                         {
                                             "@resource": "/user/patron/address1",
                                             "@key": "5",
                                             "code": {
                                                 "@resource": "/policy/patronAddress1",
                                                 "@key": "POSTALCODE"
                                             },
                                             "data": "T6G 0G4"
                                         },
                                         {
                                             "@resource": "/user/patron/address1",
                                             "@key": "2",
                                             "code": {
                                                 "@resource": "/policy/patronAddress1",
                                                 "@key": "PHONE"
                                             },
                                             "data": "780-555-5555"
                                         },
                                         {
                                             "@resource": "/user/patron/address1",
                                             "@key": "6",
                                             "code": {
                                                 "@resource": "/policy/patronAddress1",
                                                 "@key": "EMAIL"
                                             },
                                             "data": "anorton@hotmail.com"
                                         },
                                         {
                                             "@resource": "/user/patron/address1",
                                             "@key": "4",
                                             "code": {
                                                 "@resource": "/policy/patronAddress1",
                                                 "@key": "STREET"
                                             },
                                             "data": "3532 20 Avenue NW"
                                         }
                                     ]
                               }
                           }
                       """;
    }

    /**
     * Test of getCustomer method, of class SDapiToMeCardCustomer.
     */
    @Test
    public void testGetCustomer_List() 
    {
        System.out.println("==getCustomer list param==");
        List<String> list = new ArrayList<>();
        list.add("{");
        list.add("    \"resource\": \"/user/patron\",");
        list.add("    \"fields\": {");
        list.add("        \"barcode\": \"21221031494957\",");
        list.add("        \"lastName\": \"Norton\",");
        list.add("        \"firstName\": \"Andre\",");
        list.add("        \"library\": {\"resource\":\"/policy/library\",");
        list.add("\"key\":\"EPLMNA\"},");
        list.add("        \"profile\": {\"resource\":\"/policy/userProfile\",");
        list.add("\"key\":\"EPL_ADULT\"},");
        list.add("        \"address1\": [{");
        list.add("                    \"@resource\": \"/user/patron/address1\",");
        list.add("                    \"@key\": \"1\",");
        list.add("                    \"code\": {");
        list.add("                        \"@resource\": \"/policy/patronAddress1\",");
        list.add("                        \"@key\": \"CITY/STATE\"");
        list.add("                    },");
        list.add("                    \"data\": \"Edmonton, AB\"");
        list.add("                },");
        list.add("                {");
        list.add("                    \"@resource\": \"/user/patron/address1\",");
        list.add("                    \"@key\": \"5\",");
        list.add("                    \"code\": {");
        list.add("                        \"@resource\": \"/policy/patronAddress1\",");
        list.add("                        \"@key\": \"POSTALCODE\"");
        list.add("                    },");
        list.add("                    \"data\": \"T6G 0G4\"");
        list.add("                },");
        list.add("                {");
        list.add("                    \"@resource\": \"/user/patron/address1\",");
        list.add("                    \"@key\": \"2\",");
        list.add("                    \"code\": {");
        list.add("                        \"@resource\": \"/policy/patronAddress1\",");
        list.add("                        \"@key\": \"PHONE\"");
        list.add("                    },");
        list.add("                    \"data\": \"780-555-5555\"");
        list.add("                },");
        list.add("                {");
        list.add("                    \"@resource\": \"/user/patron/address1\",");
        list.add("                    \"@key\": \"6\",");
        list.add("                    \"code\": {");
        list.add("                        \"@resource\": \"/policy/patronAddress1\",");
        list.add("                        \"@key\": \"EMAIL\"");
        list.add("                    },");
        list.add("                    \"data\": \"anorton@hotmail.com\"");
        list.add("                },");
        list.add("                {");
        list.add("                    \"@resource\": \"/user/patron/address1\",");
        list.add("                    \"@key\": \"4\",");
        list.add("                    \"code\": {");
        list.add("                        \"@resource\": \"/policy/patronAddress1\",");
        list.add("                        \"@key\": \"STREET\"");
        list.add("                    },");
        list.add("                    \"data\": \"3532 20 Avenue NW\"");
        list.add("                }");
        list.add("            ]");
        list.add("    }");
        list.add("}");
        SDapiToMeCardCustomer instance = new SDapiToMeCardCustomer();
        Customer result = instance.getCustomer(list);
        System.out.println("Customer: "+result.toString());
        assertTrue(result.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).equalsIgnoreCase("20260228"));
    }

    /**
     * Test of getCustomer method, of class SDapiToMeCardCustomer.
     */
    @Test
    public void testGetCustomer_String() 
    {
        System.out.println("==getCustomer==");
        SDapiToMeCardCustomer instance = new SDapiToMeCardCustomer();
        Customer result = instance.getCustomer(this.customerJson);
        System.out.println("Customer: "+result.toString());
        assertTrue(result.get(CustomerFieldTypes.LASTNAME).equalsIgnoreCase("Norton"));
    }
    
}
