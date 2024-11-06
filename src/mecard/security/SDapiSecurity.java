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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Reads secure password and staff user ID from env file.
 * @author anisbet
 */
public final class SDSecurity 
{
    private String staffId;
    private String staffPassword;
    
    public SDSecurity(String envFilePath) throws IOException 
    {
        Path path = Paths.get(envFilePath);
        this.staffId = "";
        this.staffPassword = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                // Skip empty lines and comments
                if (line.trim().isEmpty() || line.trim().startsWith("#")) 
                {
                    continue;
                }
                
                // Split on first equals sign
                String[] parts = line.split("=", 2);
                if (parts.length == 2) 
                {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    
                    // Remove quotes if present
                    if (value.startsWith("\"") && value.endsWith("\"") ||
                        value.startsWith("'") && value.endsWith("'")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    
                    if (key.equals("STAFF_PASSWORD")) 
                    {
                        this.staffPassword = value;
                    }
                    
                    if (key.equals("STAFF_ID")) 
                    {
                        this.staffId = value;
                    }
                }
            }
        }
    }
    
    public String getStaffId()
    {
        return this.staffId;
    }
    
    public String getStaffPassword()
    {
        return this.staffPassword;
    }
}
