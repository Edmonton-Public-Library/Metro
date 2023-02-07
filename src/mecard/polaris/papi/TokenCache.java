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
package mecard.polaris.papi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import mecard.customer.UserFile;
import mecard.util.DateComparer;

/**
 * Writes a date string on the first line and data or token on the second.
 * Class requires write permissions to MeCard's current working directory.
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class TokenCache extends UserFile
{
    /**
     * Constructs a writeToCache file. 
     * @param userId
     * @param loadDirectory String directory without any trailing '/'.
     */
    public TokenCache(String userId, String loadDirectory)
    {
        super(loadDirectory + "." + userId + ".cache");
    }
    
    /**
     * Writes the token to file.
     * @param expiry Timestamp of token expiry
     * @param token string to be stored.
     * @return true if data successfully written and false otherwise.
     */
    public boolean writeToCache(String expiry, String token)
    {
        // Clear the underlying array or we just keep building up cache in file.
        this.data.clear();
        List<String> tokenData = new ArrayList<>();
        tokenData.add(expiry);
        tokenData.add(token);
        this.addUserData(tokenData);
        return this.writeContent();
    }
    
    /**
     * Gets the date of expiry of the token.
     * @return Date in "2023-08-20T22:16:02.45" format.
     */
    public String getDate()
    {
        try
        {
            StringBuilder token = new StringBuilder();
            Scanner in = new Scanner(new FileReader(this.filePath.toFile()));
            token.append(in.nextLine());
            return token.toString();
        } 
        catch (FileNotFoundException ex)
        {
            System.out.println("file not found: " + filePath);
            return "";
        }
    }
    
    /**
     * Returns the token from writeToCache denoted by path or an empty string 
     * if there was no token or the token has expired.
     * 
     * @return token from the writeToCache file, which can be the patron access
     * token or the staff AccessSecret.
     */
    public String getValidToken()
    {
        try
        {
            Scanner in = new Scanner(new FileReader(this.filePath.toFile()));
            String expiry = in.nextLine();
            // Subtle the patron response has a tag for 'AuthExpDate' but is
            // usually (always) empty. The comparison will return 0 if one or
            // both the dates are blank or if the dates are equal if one was 
            // created.
            if (DateComparer.cmpDates(expiry, DateComparer.getNowSQLTimeStamp()) >= 0)
            {
                return in.nextLine();
            }
            else
            {
                return "";
            }
            
        } 
        catch (FileNotFoundException ex)
        {
            System.out.println("file not found: " + this.filePath);
            return "";
        }
    }
}
