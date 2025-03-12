/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024 - 2025 Edmonton Public Library
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
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiPropertyTypes;

/**
 * Manages session tokens. Can save, retrieve, and test expiry of web
 * service session tokens.
 * @author anisbet <andrew (at) dev-ils dot com>
 */
public class TokenManager 
{
    private Properties sdApiEnv = PropertyReader.getProperties(ConfigFileTypes.SIRSIDYNIX_API);
    private final String cachePath = sdApiEnv.getProperty(SDapiPropertyTypes.CACHE_PATH.toString(), "./.token.cache");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Used during testing for cleanup.
     * @return String to the cache's path.
     */
    public String getCachePath()
    {
        return this.cachePath;
    }
    
    /** 
     * Writes the token and expiration time to file.
     * @param token 
     * @param validDuration time to expire in minutes.
     */
    public void writeToken(String token, Duration validDuration) 
    {
        LocalDateTime expirationTime = LocalDateTime.now().plus(validDuration);
        String data = token + "\n" + expirationTime.format(FORMATTER);
        
        try 
        {
            Files.write(Paths.get(cachePath), data.getBytes());
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
            String token = Files.readAllLines(Paths.get(cachePath)).get(0);
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
            String expirationTimeStr = Files.readAllLines(Paths.get(cachePath)).get(1);
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

    /**
     * Given a JSON response from a web service, extracts the session token and
     * writes it to cache.
     * @param tokenMarker The string that denotes the session token in the json,
     * like 'sessionToken', probably.
     * @param jsonString response from a web service call.
     * @param d - duration in minutes.
     */
    public void writeTokenFromStdout(String tokenMarker, String jsonString, Duration d) 
    {
        // '{"staffKey":"776715","pinCreateDate":"2024-03-26","pinExpirationDate":null,"name":"Web Service Requests for Online Registration","sessionToken":"08eff918-aefa-44a6-b6a6-51ddd40b38ad","pinStatus":{"resource":"/policy/userPinStatus","key":"A","fields":{"policyNumber":1,"description":"$<userpin_active_status>","displayName":"A","translatedDescription":"User's PIN is active"}},"message":null}'
        // Test for, and save returned 'sessionToken'
        String marker = "\""+tokenMarker+"\":\"";
        int tokenIndex = jsonString.indexOf(marker);
        if (tokenIndex == -1) 
        {
            return;
        }
        
        // Start after the field name and opening quote
        int startIndex = tokenIndex + marker.length();
        
        // Find the closing quote
        int endIndex = jsonString.indexOf("\"", startIndex);
        if (endIndex == -1) {
            return;
        }
        
        this.writeToken(jsonString.substring(startIndex, endIndex), d);
    }
}
