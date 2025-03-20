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
 *
 */

package mecard.requestbuilder;


import static org.junit.Assert.*;

import api.Command;
import api.HttpCommandStatus;
import json.RequestDeserializer;
import mecard.QueryTypes;
import mecard.Request;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.NativeFormatToMeCardCustomer;
import org.junit.Test;
import site.CustomerLoadNormalizer;

/**
 * Note: a sdapi.properties file must exist and have valid credentials to run.
 * @author anisbet
 */
public class SDapiRequestBuilderTest 
{
//    private final Customer testCustomer;
//    private final Customer updateCustomer;
    private final boolean runCreateCustomerTest;
    private final boolean runUpdateCustomerTest;
    private final String existingCustomerId;
    private final String existingCustomerPin;
    private final String nonExistingCustomerId;
    private final String nonExistingCustomerPin;
//    private final String updateUserId;
//    private final String updateUserPin;
//    private final String updateUserExpiry;
//    private final String updateUserStanding;
//    private final String createUserId;
//    private final String createUserPin;
    
//    private final String testData;
//    private final Customer testCustomer;
    private final Customer testCustomer;
    
    public SDapiRequestBuilderTest() 
    {
//        createUserId     = "21221012346099";
//        createUserPin    = "7711";
        RequestDeserializer deserializer = new RequestDeserializer();
//        String createCustomerData = """
//                           {
//                            "code": "CREATE_CUSTOMER",
//                            "authorityToken": "12345678",
//                            "userId": "21221012346099",
//                            "pin": "7711",
//                            "customer": {
//                              "ID": "21221012346099",
//                              "PIN": "7711",
//                              "PREFEREDNAME": "William, Tenneson",
//                              "STREET": "12345 144 St.",
//                              "CITY": "Edmonton",
//                              "PROVINCE": "Alberta",
//                              "POSTALCODE": "T6G 0G4",
//                              "SEX": "M",
//                              "EMAIL": "ilsteam@epl.ca",
//                              "PHONE": "7804964058",
//                              "DOB": "19750822",
//                              "PRIVILEGE_EXPIRES": "20260602",
//                              "RESERVED": "X",
//                              "ALTERNATE_ID": "X",
//                              "ISVALID": "Y",
//                              "ISMINAGE": "Y",
//                              "ISRECIPROCAL": "N",
//                              "ISRESIDENT": "Y",
//                              "ISGOODSTANDING": "Y",
//                              "ISLOSTCARD": "N",
//                              "FIRSTNAME": "William",
//                              "LASTNAME": "Tenneson"
//                            }
//                          }
//                          """;
        String createCustomerData = "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346100\",\"pin\":\"8811\",\"customer\":\"{\\\"ID\\\":\\\"21221012346100\\\",\\\"PIN\\\":\\\"8811\\\",\\\"PREFEREDNAME\\\":\\\"Dickerson, Will\\\",\\\"STREET\\\":\\\"11111 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0G4\\\",\\\"SEX\\\":\\\"F\\\",\\\"EMAIL\\\":\\\"ilsadmins@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19630822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20260228\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Will\\\",\\\"LASTNAME\\\":\\\"Dickerson\\\"}\"}";
        
        Request createRequest = deserializer.getDeserializedRequest(createCustomerData);
        testCustomer = createRequest.getCustomer();
        
        String bbKey = "301585";
        // Account exists on Dewey have a pin of 64058 those on WBRL have 7711.
//        existingCustomerId     = "21221012346001";
//        existingCustomerPin    = "64058";
//        existingCustomerId     = "21221012346002";
//        existingCustomerPin    = "64058";
//        existingCustomerId     = "21221012346004";
//        existingCustomerPin    = "7711";
//        existingCustomerId     = "21221012346077";
//        existingCustomerPin    = "7711";
        existingCustomerId     = "21221012345678";
        existingCustomerPin    = "0000";
//        existingCustomerId     = "21221012346099";
//        existingCustomerPin    = "7711";
        nonExistingCustomerId  = "21221012346097";
        nonExistingCustomerPin = "12345";
        runCreateCustomerTest  = false;
        runUpdateCustomerTest  = false;
    }

    
    /**
     * Test of getCreateUserCommand method, of class SDapiRequestBuilder.
     */
    @Test
    public void testGetCreateUserCommand() 
    {
        System.out.println("==getCreateUserCommand==");
        if (runCreateCustomerTest)
        {
            
            Response response = new Response();
            CustomerLoadNormalizer normalizer = CustomerLoadNormalizer.getInstanceOf(true);
            SDapiRequestBuilder requestBuilder = new SDapiRequestBuilder(true);
            Command command = requestBuilder.getCreateUserCommand(testCustomer, response, normalizer);
            HttpCommandStatus status = (HttpCommandStatus) command.execute();
            System.out.println("-------Status:" + status.getStdout());
            assertTrue(requestBuilder.isSuccessful(QueryTypes.CREATE_CUSTOMER, status, response));
        }
    }

    /**
     * Test of getUpdateUserCommand method, of class SDapiRequestBuilder.
     */
    @Test
    public void testGetUpdateUserCommand() 
    {
        System.out.println("==getUpdateUserCommand==");
        if (runUpdateCustomerTest)
        {
            Response response = new Response();
            CustomerLoadNormalizer normalizer = CustomerLoadNormalizer.getInstanceOf(true);
            SDapiRequestBuilder requestBuilder = new SDapiRequestBuilder(true);
            Command command = requestBuilder.getUpdateUserCommand(testCustomer, response, normalizer);
            HttpCommandStatus status = (HttpCommandStatus) command.execute();
            System.out.println("-------Status:" + status.getStdout());
            assertTrue(requestBuilder.isSuccessful(QueryTypes.UPDATE_CUSTOMER, status, response));
        }
    }
/**
     * Test of getCustomerCommand method, of class SDapiRequestBuilder.
     */
    @Test
    public void testGetCustomerCommand() 
    {
        System.out.println("==getCustomerCommand==");
        String userId = "21221012345678";
        String userPin = "0000";
        Response response = new Response();
        SDapiRequestBuilder requestBuilder = new SDapiRequestBuilder(false);

        Command command = requestBuilder.getCustomerCommand(userId, userPin, response);
        HttpCommandStatus status = (HttpCommandStatus) command.execute();
        NativeFormatToMeCardCustomer customerFormatter = requestBuilder.getFormatter();
        // Use the formatter to convert the returned sdapi info into a ME customer object.
        Customer customer = customerFormatter.getCustomer(status.getStdout());
//        response.setCustomer(customer);
//        System.out.println(">>getCustomerCommand Customer: " + System.lineSeparator()
//            + customer.toString());
        assertTrue(requestBuilder.isSuccessful(QueryTypes.GET_CUSTOMER, status, response));
        assertTrue(customer.get(CustomerFieldTypes.ID).compareTo(userId) == 0);
    }

    /**
     * Test of getStatusCommand method, of class SDapiRequestBuilder.
     */
    @Test
    public void testGetStatusCommand() 
    {
        System.out.println("==getStatusCommand==");
        Response response = new Response();
        SDapiRequestBuilder requestBuilder = new SDapiRequestBuilder(false);

        Command command = requestBuilder.getStatusCommand(response);
        HttpCommandStatus status = (HttpCommandStatus) command.execute();
//        System.out.println("Status:" + status.getStdout());
        requestBuilder.isSuccessful(QueryTypes.GET_STATUS, status, response);
        assertTrue(requestBuilder.isSuccessful(QueryTypes.GET_STATUS, status, response));
        assertTrue(response.getCode() == ResponseTypes.SUCCESS);
    }

    /**
     * Test of testCustomerExists method, of class SDapiRequestBuilder.
     */
    @Test
    public void testTestCustomerExists() 
    {
        System.out.println("==testCustomerExists==");
        String userId = this.existingCustomerId;
        String userPin = this.existingCustomerPin;
        Response response = new Response();
        SDapiRequestBuilder requestBuilder = new SDapiRequestBuilder(false);

        Command command = requestBuilder.getCustomerCommand(userId, userPin, response);
        HttpCommandStatus status = (HttpCommandStatus) command.execute();
        NativeFormatToMeCardCustomer customerFormatter = requestBuilder.getFormatter();
        // Use the formatter to convert the returned sdapi info into a ME customer object.
        Customer customer = customerFormatter.getCustomer(status.getStdout());
//        response.setCustomer(customer);
//        System.out.println(">>getCustomerCommand Customer: " + System.lineSeparator()
//            + customer.toString());
        assertTrue(requestBuilder.isSuccessful(QueryTypes.TEST_CUSTOMER, status, response));
        assertTrue(customer.get(CustomerFieldTypes.ID).compareTo(userId) == 0);
        
        response = new Response();
        command = requestBuilder.getCustomerCommand(
                this.nonExistingCustomerId, 
                this.nonExistingCustomerPin, 
                response);
        status = (HttpCommandStatus) command.execute();
        assertFalse(requestBuilder.isSuccessful(QueryTypes.TEST_CUSTOMER, status, response));
    }

    /**
     * Test of getCustomerMessage method, of class SDapiRequestBuilder.
     */
    @Test
    public void testGetCustomerMessage() 
    {
        System.out.println("==getCustomerMessage==");
        String userId = "21221012345678";
        String userPin = "0000";
        Response response = new Response();
        SDapiRequestBuilder requestBuilder = new SDapiRequestBuilder(false);

        Command command = requestBuilder.getCustomerCommand(userId, userPin, response);
        HttpCommandStatus status = (HttpCommandStatus) command.execute();
        NativeFormatToMeCardCustomer customerFormatter = requestBuilder.getFormatter();
        // Use the formatter to convert the returned sdapi info into a ME customer object.
        Customer customer = customerFormatter.getCustomer(status.getStdout());
        response.setCustomer(customer);
        System.out.println(">>getCustomerCommand Customer: " + System.lineSeparator()
            + customer.toString());
        assertTrue(customer.get(CustomerFieldTypes.ID).compareTo(userId) == 0);
        System.out.println("CustomerMessage: " + requestBuilder.getCustomerMessage(status.getStdout()) );
    }
    
    /**
     * Test of getCustomerLoadDirectory() method, of class SDapiRequestBuilder.
     */
    @Test
    public void testGetCustomerLoadDirectory() 
    {
        System.out.println("==testGetCustomerLoadDirectory==");
        Response response = new Response();
        SDapiRequestBuilder requestBuilder = new SDapiRequestBuilder(true);
        System.out.println("LOAD DIR:" + requestBuilder.getCustomerLoadDirectory());
        assertTrue(requestBuilder.getCustomerLoadDirectory() instanceof String);
    }
    
}
