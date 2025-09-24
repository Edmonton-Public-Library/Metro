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
import api.CommandStatus;
import api.CustomerMessage;
import api.HttpCommandStatus;
import json.RequestDeserializer;
import mecard.QueryTypes;
import mecard.Request;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.customer.Customer;
import mecard.customer.NativeFormatToMeCardCustomer;
import org.junit.Test;
import site.CustomerLoadNormalizer;

/**
 *
 * @author anisbet
 */
public class CPLapiRequestBuilderTest 
{
    private final Customer updateCustomer;
    private final Customer createCustomer;
    private final String customerJsonData;
    
    public CPLapiRequestBuilderTest() 
    {
        customerJsonData = """
                                  {
                                      "cardNumber": "29065024681012",
                                      "pin": "24681012",
                                      "firstName": "Generic",
                                      "lastName": "Account",
                                      "birthDate": "2020-01-01",
                                      "gender": "NOTELL",
                                      "emailAddress": "andrew@dev-ils.com",
                                      "phoneNumber": null,
                                      "address": "11950 Country Village Link NE",
                                      "city": "CALGARY",
                                      "province": "AB",
                                      "postalCode": "T3K 6E3",
                                      "expiryDate": "2027-08-22",
                                      "status": "OK",
                                      "profile": "ADULT"
        }""";
        String custUpdateReq = """
                            {
                               "code":"UPDATE_CUSTOMER",
                               "authorityToken":"12345678",
                               "userId":"29065024681012",
                               "pin":"24681012",
                               "customer":"{
                                    \\"ID\\":\\"29065024681012\\",
                                    \\"PIN\\":\\"24681012\\",
                                    \\"PREFEREDNAME\\":\\"Generic, Account\\",
                                    \\"STREET\\":\\"11950 Country Village Link NE\\",
                                    \\"CITY\\":\\"CALGARY\\",
                                    \\"PROVINCE\\":\\"AB\\",
                                    \\"POSTALCODE\\":\\"T3K 6E3\\",
                                    \\"SEX\\":\\"M\\",
                                    \\"EMAIL\\":\\"ilsteam@epl.ca\\",
                                    \\"PHONE\\":\\"7804964058\\",
                                    \\"DOB\\":\\"20010822\\",
                                    \\"PRIVILEGE_EXPIRES\\":\\"20270822\\",
                                    \\"RESERVED\\":\\"X\\",
                                    \\"ALTERNATE_ID\\":\\"X\\",
                                    \\"ISVALID\\":\\"Y\\",
                                    \\"ISMINAGE\\":\\"Y\\",
                                    \\"ISRECIPROCAL\\":\\"N\\",
                                    \\"ISRESIDENT\\":\\"Y\\",
                                    \\"ISGOODSTANDING\\":\\"Y\\",
                                    \\"ISLOSTCARD\\":\\"N\\",
                                    \\"FIRSTNAME\\":\\"Generic\\",
                                    \\"LASTNAME\\":\\"Account\\"
                                    }"
                               }""";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custUpdateReq);
        this.updateCustomer = request.getCustomer();
        
        String custCreateReq = """
                            {
                               "code":"CREATE_CUSTOMER",
                               "authorityToken":"12345678",
                               "userId":"21221012345666",
                               "pin":"64058",
                               "customer":"{
                                    \\"ID\\":\\"21221012345666\\",
                                    \\"PIN\\":\\"64058\\",
                                    \\"PREFEREDNAME\\":
                                    \\"Billy, Balzac\\",
                                    \\"STREET\\":\\"7 Sir Winston Churchill Square\\",
                                    \\"CITY\\":\\"Edmonton\\",
                                    \\"PROVINCE\\":\\"Alberta\\",
                                    \\"POSTALCODE\\":\\"H0H0H0\\",
                                    \\"SEX\\":\\"M\\",
                                    \\"EMAIL\\":\\"ilsteam@epl.ca\\",
                                    \\"PHONE\\":\\"7804964058\\",
                                    \\"DOB\\":\\"20010822\\",
                                    \\"PRIVILEGE_EXPIRES\\":\\"20260822\\",
                                    \\"RESERVED\\":\\"X\\",
                                    \\"ALTERNATE_ID\\":\\"X\\",
                                    \\"ISVALID\\":\\"Y\\",
                                    \\"ISMINAGE\\":\\"Y\\",
                                    \\"ISRECIPROCAL\\":\\"N\\",
                                    \\"ISRESIDENT\\":\\"Y\\",
                                    \\"ISGOODSTANDING\\":\\"Y\\",
                                    \\"ISLOSTCARD\\":\\"N\\",
                                    \\"FIRSTNAME\\":\\"Billy\\",
                                    \\"LASTNAME\\":\\"Balzac\\"
                                    }"
                               }""";
        deserializer = new RequestDeserializer();
        request = deserializer.getDeserializedRequest(custCreateReq);
        this.createCustomer = request.getCustomer();
    }

    /**
     * Test of getFormatter method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testGetFormatter() {
        System.out.println("getFormatter");
        CPLapiRequestBuilder instance = null;
        NativeFormatToMeCardCustomer expResult = null;
        NativeFormatToMeCardCustomer result = instance.getFormatter();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCreateUserCommand method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testGetCreateUserCommand() {
        System.out.println("getCreateUserCommand");
        Customer customer = null;
        Response response = null;
        CustomerLoadNormalizer normalizer = null;
        CPLapiRequestBuilder instance = null;
        Command expResult = null;
        Command result = instance.getCreateUserCommand(customer, response, normalizer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUpdateUserCommand method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testGetUpdateUserCommand() {
        System.out.println("getUpdateUserCommand");
        Customer customer = null;
        Response response = null;
        CustomerLoadNormalizer normalizer = null;
        CPLapiRequestBuilder instance = null;
        Command expResult = null;
        Command result = instance.getUpdateUserCommand(customer, response, normalizer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatusCommand method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testGetStatusCommand() 
    {
        System.out.println("==getStatusCommand==");
        Response response = new Response();
        CPLapiRequestBuilder requestBuilder = new CPLapiRequestBuilder(true);

        Command command = requestBuilder.getStatusCommand(response);
        HttpCommandStatus status = (HttpCommandStatus) command.execute();
//        System.out.println("Status:" + status.getStdout());
        requestBuilder.isSuccessful(QueryTypes.GET_STATUS, status, response);
        assertTrue(requestBuilder.isSuccessful(QueryTypes.GET_STATUS, status, response));
        assertTrue(response.getCode() == ResponseTypes.SUCCESS);
    }

    /**
     * Test of testCustomerExists method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testTestCustomerExists() 
    {
        System.out.println("==testCustomerExists==");
        String userId = "29065024681012";
        String userPin = "24681012";
        Response response = new Response();
        CPLapiRequestBuilder requestBuilder = new CPLapiRequestBuilder(true);
        String expResult = """
                            https://cplwebapitest-ecfwb9fbbxg5b2ev.canadacentral-01.azurewebsites.net/api/MeLibraries/VerifyCustomer POST
                            <pending response>""";
        Command command = requestBuilder.testCustomerExists(userId, userPin, response);
//        System.out.println("LOOK HERE:" + command.toString());
        assertEquals(expResult, command.toString());
        HttpCommandStatus status = (HttpCommandStatus) command.execute();
//        System.out.println("Status:" + status.getStdout());
//        requestBuilder.isSuccessful(QueryTypes.GET_CUSTOMER, status, response);
        assertTrue(requestBuilder.isSuccessful(QueryTypes.TEST_CUSTOMER, status, response));
        assertTrue(status.getHttpStatusCode() == 200);
        
        userId = "290650246899999";
        userPin = "24681012";
        command = requestBuilder.testCustomerExists(userId, userPin, response);
        status = (HttpCommandStatus) command.execute();
        assertFalse(requestBuilder.isSuccessful(QueryTypes.TEST_CUSTOMER, status, response));
        System.out.println("RESP:" + response.getCode() + " => " + response.getMessage());
        assertTrue(response.getCode() == ResponseTypes.USER_NOT_FOUND);
    }

    /**
     * Test of isInvalidCredentialsMessageIgnoreCase method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testIsInvalidCredentialsMessageIgnoreCase() 
    {
        System.out.println("==isInvalidCredentialsMessageIgnoreCase==");
        // Test cases
        String[] validTestMessages = {
            "CardNumber/PinNumber: [Invalid Credentials.]",
            "cardnumber/pinnumber: [invalid credentials.]",
            "Error: CardNumber/PinNumber: [Invalid Credentials.] Please try again.",
            "CardNumber/PinNumber: [Invalid Credentials]",  // Missing period
            "CardNumber/PinNumber: Invalid Credentials."   // Missing brackets
        };
        
        String[] inValidTestMessages = {
            null,
            "Some other error message",
        };
        CPLapiRequestBuilder requestBuilder = new CPLapiRequestBuilder(true);
//        System.out.println("Testing valid matches:");
        for (String msg : validTestMessages) 
        {
//            System.out.println(msg + " -> " + requestBuilder.isInvalidCredentialsMessageIgnoreCase(msg));
            assertTrue(requestBuilder.isInvalidCredentialsMessageIgnoreCase(msg));
        }
        
//        System.out.println("\nTesting invalid matches:");
        for (String msg : inValidTestMessages) 
        {
//            System.out.println(msg + " -> " + requestBuilder.isInvalidCredentialsMessageIgnoreCase(msg));
            assertFalse(requestBuilder.isInvalidCredentialsMessageIgnoreCase(msg));
        }
    }

    /**
     * Test of isSuccessful method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testIsSuccessful() {
        System.out.println("isSuccessful");
        QueryTypes commandType = null;
        CommandStatus status = null;
        Response response = null;
        CPLapiRequestBuilder instance = null;
        boolean expResult = false;
        boolean result = instance.isSuccessful(commandType, status, response);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tidy method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testTidy() 
    {
        System.out.println("==tidy==");
        // Trivial test since tidy is not required for this service.
        CPLapiRequestBuilder requestBuilder = new CPLapiRequestBuilder(true);
        assertTrue(requestBuilder.tidy());
    }

    /**
     * Test of getCustomerMessage method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testGetCustomerMessage() 
    {
        System.out.println("==getCustomerMessage==");
        String expectedResult = "{cardNumber='29065024681012', pin='******', firstName='Generic', lastName='Account', birthDate='2020-01-01', gender='NOTELL', emailAddress='andrew@dev-ils.com', phoneNumber='', address='11950 Country Village Link NE', city='CALGARY', province='AB', postalCode='T3K 6E3', expiryDate='2027-08-22', profile='ADULT', status='OK'}";
        CPLapiRequestBuilder instance = new CPLapiRequestBuilder(true);
        CustomerMessage result = instance.getCustomerMessage(this.customerJsonData);
        assertEquals(result.getField("cardNumber"), "29065024681012");
        assertEquals(result.getField("heartRate"), "");
        assertEquals(result.getField("pin"), "24681012");
//        System.out.println("CDgt:'" + result + "'");
//        System.out.println("CDex:'" + expectedResult + "'");
        assertTrue(expectedResult.compareTo(result.toString()) == 0);
    }

    /**
     * Test of getCustomerCommand method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testGetCustomerCommand() 
    {
        System.out.println("==getCustomerCommand==");
        String userId = "29065024681012";
        String userPin = "24681012";
        Response response = new Response();
        CPLapiRequestBuilder requestBuilder = new CPLapiRequestBuilder(true);
        String expResult = """
                            https://cplwebapitest-ecfwb9fbbxg5b2ev.canadacentral-01.azurewebsites.net/api/MeLibraries/GetCustomer POST
                            <pending response>""";
        Command command = requestBuilder.getCustomerCommand(userId, userPin, response);
//        System.out.println("LOOK HERE:" + command.toString());
        assertEquals(expResult, command.toString());
        HttpCommandStatus status = (HttpCommandStatus) command.execute();
        
        System.out.println("response:" + response.getCustomer());
        assertTrue(requestBuilder.isSuccessful(QueryTypes.GET_CUSTOMER, status, response));
        assertTrue(status.getHttpStatusCode() == 200);
        
        userId = "290650246899999";
        userPin = "24681012";
        command = requestBuilder.getCustomerCommand(userId, userPin, response);
        status = (HttpCommandStatus) command.execute();
        assertFalse(requestBuilder.isSuccessful(QueryTypes.GET_CUSTOMER, status, response));
        System.out.println("GET RESP:" + response.getCode() + " => " + response.getMessage());
        assertTrue(response.getCode() == ResponseTypes.USER_PIN_INVALID);
    }

    /**
     * Test of getCustomerLoadDirectory method, of class CPLapiRequestBuilder.
     */
    @Test
    public void testGetCustomerLoadDirectory() 
    {
        System.out.println("==testGetCustomerLoadDirectory==");
        String expected = """
                          /home/anisbet/MeCard/logs/Customers/""";
        SDapiRequestBuilder requestBuilder = new SDapiRequestBuilder(true);
//        System.out.println("LOAD DIR:" + requestBuilder.getCustomerLoadDirectory());
        assertTrue(requestBuilder.getCustomerLoadDirectory().compareTo(expected) == 0);
    }
    
}
