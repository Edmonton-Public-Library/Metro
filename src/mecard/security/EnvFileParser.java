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
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author andrew (at) dev-ils.com
 */
public class EnvFileParser 
{
    private final Map<String, String> envVariables;
    
    /**
     * Reads an environment file and stores all key-value pairs in a Map
     * @param envFilePath Path to the .env file
     * @throws IOException If there's an error reading the file
     */
    public EnvFileParser(String envFilePath) throws IOException 
    {
        this.envVariables = new HashMap<>();
        Path path = Paths.get(envFilePath);
        
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
                        value.startsWith("'") && value.endsWith("'")) 
                    {
                        value = value.substring(1, value.length() - 1);
                    }
                    
                    envVariables.put(key, value);
                }
            }
        }
    }
    
    /**
     * Gets the value for a specific environment variable
     * @param key The environment variable name
     * @return The value if found, null otherwise
     */
    public String getValue(String key) 
    {
        return envVariables.get(key);
    }
    
    /**
     * Gets all environment variables
     * @return Map containing all environment variables
     */
    public Map<String, String> getAllVariables() 
    {
        return new HashMap<>(envVariables); // Return a copy to prevent modification
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        envVariables.forEach((key, value) -> 
            sb.append(key).append(" = ").append(key.toLowerCase().contains("password") ? "xxxxxxx" : value).append("\n")
        );
        return sb.toString();
    }
}
