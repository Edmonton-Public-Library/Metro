/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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


import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class SDapiUserPatronSearchResponseTest 
{
    private String successfulJson;
    private String failedJson;
    public SDapiUserPatronSearchResponseTest() 
    {
        this.successfulJson = """
                                {
                                       "searchQuery": "ID:21221012345678",
                                       "startRow": 1,
                                       "lastRow": 1,
                                       "rowsPerPage": 10,
                                       "totalResults": 1,
                                       "result": [
                                           {
                                               "resource": "/user/patron",
                                               "key": "301585"
                                           }
                                       ]
                                    }
                                """;
        this.failedJson = """
                            {
                                   "searchQuery": "ID:212210123456789",
                                   "startRow": 1,
                                   "lastRow": 10,
                                   "rowsPerPage": 10,
                                   "totalResults": 0,
                                   "result": []
                                }
                            """;
    }

    /**
     * Test of getSearchCount method, of class SDapiUserPatronSearchResponse.
     */
    @Test
    public void testGetCustomerCount() {
        System.out.println("==getCustomerCount==");
        SDapiUserPatronSearchResponse instance = 
                (SDapiUserPatronSearchResponse) SDapiUserPatronSearchResponse.parseJson(this.successfulJson);
        int expResult = 1;
        int result = instance.getSearchCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of succeeded method, of class SDapiUserPatronSearchResponse.
     */
    @Test
    public void testSucceeded() {
        System.out.println("==succeeded==");
        SDapiUserPatronSearchResponse instance = 
                (SDapiUserPatronSearchResponse) SDapiUserPatronSearchResponse.parseJson(this.successfulJson);
        boolean expResult = true;
        boolean result = instance.succeeded();
        assertEquals(expResult, result);
    }

    /**
     * Test of errorMessage method, of class SDapiUserPatronSearchResponse.
     */
    @Test
    public void testErrorMessage() {
        System.out.println("==errorMessage==");
        SDapiUserPatronSearchResponse instance = 
                (SDapiUserPatronSearchResponse) SDapiUserPatronSearchResponse.parseJson(this.failedJson);
        String expResult = "Account not found.";
        String result = instance.errorMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of parseJson method, of class SDapiUserPatronSearchResponse.
     */
    @Test
    public void testParseJson() 
    {
        System.out.println("==parseJson==");
        String jsonString = "{\"result\": []}";
        int expResult = 0;
        SDapiUserPatronSearchResponse instance = 
                (SDapiUserPatronSearchResponse) SDapiUserPatronSearchResponse.parseJson(jsonString);
        int result = instance.getSearchCount();
        assertEquals(expResult, result);
    }
    
}
