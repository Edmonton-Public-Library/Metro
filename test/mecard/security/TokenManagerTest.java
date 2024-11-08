/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2023  Edmonton Public Library
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

import org.junit.Test;

import static org.junit.Assert.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Optional;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class TokenManagerTest {
    
    public TokenManagerTest() {
    }

    /**
     * Test of writeToken method, of class TokenManager.
     */
    @Test
    public void testWriteToken() {
        System.out.println("==writeToken==");
        String token = "ffffffff-ffff-ffff-ffff-ffffffffffff";
        TokenManager tokenManager = new TokenManager();
        tokenManager.writeToken(token, Duration.ofMinutes(10));

        // Retrieve token
//        tokenManager.getToken().ifPresent(token -> System.out.println("Token: " + token));

        // Check if token is expired
        System.out.println("Is token expired? " + tokenManager.isTokenExpired(5));
    }

    /**
     * Test of getToken method, of class TokenManager.
     */
    @Test
    public void testGetToken() {
        System.out.println("getToken");
        TokenManager instance = new TokenManager();
        Optional<String> expResult = null;
        Optional<String> result = instance.getToken();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isTokenExpired method, of class TokenManager.
     */
    @Test
    public void testIsTokenExpired() {
        System.out.println("isTokenExpired");
        long minutes = 0L;
        TokenManager instance = new TokenManager();
        boolean expResult = false;
        boolean result = instance.isTokenExpired(minutes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
