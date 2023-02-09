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
package mecard.customer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a file out of user data, while all exceptions issues messages
 * they don't stop the service.
 * @author Andrew Nisbet andrew (at) dev-ils.com
 */
public class UserFile 
{
    protected Path filePath;
    protected final List<String> data;
    
    /**
     * Creates a user file as a type of log.
     * @param path 
     */
    public UserFile(String path)
    {
        this.filePath = Paths.get(path);
        System.out.println("UserFile: " + this.filePath.toString());
        this.data = new ArrayList<>(); 
    }
    
    /**
     * Adds all the user data from the argument, then writes and closes 
     * the file. This class does no error checking of data.
     * @param data List of user data strings.
     */
    public void addUserData(List<String> data)
    {
        this.data.addAll(data);
        this.writeContent();
    }

    /**
     *
     * @return true if the content was written to file and false otherwise.
     */
    public boolean writeContent()
    {
        try
        {
            Files.write(this.filePath, this.data);
            return true;
        } 
        catch (IOException ex)
        {
            String msg = "*error unable to write to file " + this.filePath.toString() 
                    + "\n" + UserFile.class.getName() + " => " + ex;
            System.out.println(msg);
            System.err.println(msg);
            return false;
        }
    }
    
    /**
     * Creates a new UserFile, deleting the old one if it exists.
     * @param name - fully qualified file name.
     * @return File handle to the user file.
     */
    protected File createFile(String name)
    {
        File file = new File(name);
        if (file.exists())
        {
            file.delete();
        }
        // create a new one.
        file = new File(name);
        return file;
    }
}
