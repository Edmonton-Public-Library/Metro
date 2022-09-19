/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
package mecard.polaris;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class TokenCacheTest
{

    private final String testToken;
    private final String userId;
    
    public TokenCacheTest( )
    {   
        this.testToken = "ab120-bcdf987-af1234";
        this.userId    = "PolarisStaff";
    }

    /**
     * Test of writeToCache method, of class TokenCache.
     */
    @Test
    public void testCache()
    {
        System.out.println("cache");
        TokenCache instance = new TokenCache(userId, "./");
        boolean expResult = true;
        boolean result = instance.writeToCache("2021-08-20T22:16:02.45", this.testToken);
        assertEquals(expResult, result);
    }

    /**
     * Test of getValidToken method, of class TokenCache.
     */
    @Test
    public void testGetValidToken()
    {
        System.out.println("getToken");
        TokenCache instance = new TokenCache(userId, "./");
        String expResult = this.testToken;
        String result = instance.getValidToken();
        System.out.println("RESULT:" + result);
        assertEquals("", result);
        instance.writeToCache("2023-08-20T22:16:02.45", this.testToken);
        result = instance.getValidToken();
        System.out.println("RESULT:" + result);
        assertEquals(expResult, result);
        instance.writeToCache("2020-08-20T22:16:02.45", this.testToken);
        result = instance.getValidToken();
        System.out.println("RESULT:" + result);
        assertEquals("", result);
    }

    /**
     * Test of writeToCache method, of class TokenCache.
     */
    @Test
    public void testWriteToCache()
    {
        System.out.println("writeToCache");
        TokenCache instance = new TokenCache(userId, "./");
        boolean expResult = true;
        boolean result = instance.writeToCache("2021-08-20T22:16:02.45", this.testToken);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDate method, of class TokenCache.
     */
    @Test
    public void testGetDate()
    {
        System.out.println("getDate");
        TokenCache instance = new TokenCache(userId, "./");
        String result = instance.getDate();
        System.out.println("Cache Date: " + result);
    }
    
}
