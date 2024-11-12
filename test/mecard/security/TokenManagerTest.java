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
package mecard.security;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;

import java.time.*;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class TokenManagerTest
{

    public static boolean deleteFile(String filePath) 
    {
        try 
        {
            Path path = Paths.get(filePath);
            Files.delete(path);
            return true;
        } 
        catch (IOException e) 
        {
            return false;
        }
    }
    
    /**
     * Test of getToken method, of class TokenManager.
     */
    @Test
    public void testGetToken() 
    {
        System.out.println("==getToken==");
        TokenManager tokenManager = new TokenManager();
        String expResult = "";
        String result = tokenManager.getToken();
        assertEquals(expResult, result);
        // Now add a token
        String token = "ffffffff-ffff-ffff-ffff-ffffffffffff"; 
        tokenManager.writeToken(token, Duration.ofMinutes(1));
        result = tokenManager.getToken();
        assertEquals(token, result);
    }

    /**
     * Test of isTokenExpired method, of class TokenManager.
     */
    @Test
    public void testIsTokenExpired() 
    {
        System.out.println("==isTokenExpired==");
        TokenManagerTest.deleteFile(TokenManager.CACHE_PATH);
        long minutes = 0L;
        TokenManager tokenManager = new TokenManager();
        boolean expResult = true;
        boolean result = tokenManager.isTokenExpired(minutes);
        assertEquals(expResult, result);
        String token = "ffffffff-ffff-ffff-ffff-ffffffffffff"; 
        tokenManager.writeToken(token, Duration.ofMinutes(1));
        assertFalse(tokenManager.isTokenExpired(1L));
    }
    
    /**
     * Test of writeToken method, of class TokenManager.
     */
    @Test
    public void testWriteToken() 
    {
        System.out.println("==writeToken==");
        TokenManagerTest.deleteFile(TokenManager.CACHE_PATH);
        String token = "ffffffff-ffff-ffff-ffff-ffffffffffff";
        TokenManager tokenManager = new TokenManager();
        tokenManager.writeToken(token, Duration.ofMinutes(10));
        assertFalse(tokenManager.isTokenExpired(5));
    }

    /**
     * Test of getToken method, of class TokenManager.
     */
    @Test
    public void testGetToken_boolean() {
        System.out.println("==getToken explain error==");
        // Delete any cache file if there is one.
        TokenManagerTest.deleteFile(TokenManager.CACHE_PATH);
        boolean isDebug = true;
        TokenManager instance = new TokenManager();
        String expResult = "";
        String result = instance.getToken(isDebug);
        assertEquals(expResult, result);
    }

    /**
     * Test of isTokenExpired method, of class TokenManager.
     */
    @Test
    public void testIsTokenExpired_long() {
        System.out.println("==isTokenExpired==");
        TokenManagerTest.deleteFile(TokenManager.CACHE_PATH);
        long minutes = 0L;
        TokenManager tokenManager = new TokenManager();
        boolean result = tokenManager.isTokenExpired(minutes);
        assertTrue(result);
    }

    /**
     * Test of isTokenExpired method, of class TokenManager.
     */
    @Test
    public void testIsTokenExpired_long_boolean() {
        System.out.println("==isTokenExpired explain error==");
        // Delete any cache file if there is one.
        TokenManagerTest.deleteFile(TokenManager.CACHE_PATH);
        long minutes = 10L;
        boolean isDebug = true;
        TokenManager tokenManager = new TokenManager();
        boolean result = tokenManager.isTokenExpired(minutes, isDebug);
        assertTrue(result);
        String token = "ffffffff-ffff-ffff-ffff-ffffffffffff";
        tokenManager.writeToken(token, Duration.ofMinutes(10));
        assertFalse(tokenManager.isTokenExpired(minutes, isDebug));
    }
    
}
