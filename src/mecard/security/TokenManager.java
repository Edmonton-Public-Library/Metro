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

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author anisbet
 */
public class TokenManager 
{
    protected static final String CACHE_PATH = ".sd_token.cache";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Writes the token and expiration time to file
    public void writeToken(String token, Duration validDuration) 
    {
        LocalDateTime expirationTime = LocalDateTime.now().plus(validDuration);
        String data = token + "\n" + expirationTime.format(FORMATTER);
        
        try 
        {
            Files.write(Paths.get(CACHE_PATH), data.getBytes());
        } 
        catch (IOException e) 
        {
            System.err.println("**error writing token to file: " + e.getMessage());
        }
    }

    /**
     * Reads the token from the file.
     * @return token or empty string if the cache file doesn't exist or the 
     * token is not set.
     */
    public String getToken()
    {
        return getToken(false);
    }
    
    /**
     * Reads the token from the file, with optional debug information
     * @param isDebug
     * @return token or empty string if the cache file doesn't exist or the 
     * token is not set. 
     */
    public String getToken(boolean isDebug)
    {
        try 
        {
            String token = Files.readAllLines(Paths.get(CACHE_PATH)).get(0);
            return token;
        } 
        catch (IOException | IndexOutOfBoundsException e) 
        {
            if (isDebug)
            {
                System.err.println("**error reading token from file: " + e.getMessage());
            }
            return "";
        }
    }
    
    /**
     * Checks if the token is older than the specified number of minutes.
     * @param minutes to expiry (as long).
     * @return true if the token is more than 'minutes' old, or is not cached at
     * all, and false otherwise.
     */
    public boolean isTokenExpired(long minutes) 
    {
        return isTokenExpired(minutes, true);
    }
    /**
     * Checks if the token is older than the specified number of minutes.
     * @param minutes to expiry (as long).
     * @param isDebug If true issues error message if cache file is empty or 
     * does not exist.
     * @return true if the token is more than 'minutes' old, or is not cached at
     * all, and false otherwise.
     */
    public boolean isTokenExpired(long minutes, boolean isDebug) 
    {
        try 
        {
            String expirationTimeStr = Files.readAllLines(Paths.get(CACHE_PATH)).get(1);
            LocalDateTime expirationTime = LocalDateTime.parse(expirationTimeStr, FORMATTER);
            return Duration.between(expirationTime, LocalDateTime.now()).toMinutes() >= minutes;
        } 
        catch (IOException | IndexOutOfBoundsException e) 
        {
            if (isDebug)
            {
                System.err.println("Warning, token cache is empty or does not exist: " + e.getMessage());
            }
            return true; // Assume expired if there's an error
        }
    }
}
