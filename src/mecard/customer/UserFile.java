/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Andrew Nisbet
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a file out of user data.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class UserFile 
{
    private final String filePath;
    private final File dataFile;
    private final List<String> data;
    
    /**
     * Creates a Flat user file ready for writing user data.
     * @param path 
     */
    public UserFile(String path)
    {
        this.filePath = path;
        this.data     = new ArrayList<String>();
        this.dataFile = createFile(this.filePath);
    }
    
    /**
     * Adds all the flat user data from the argument, then writes and closes 
     * the file. The flat user data must be correctly formatted. This class does
     * no error checking of data.
     * @param data flat user data.
     */
    public void addUserData(List<String> data)
    {
        this.data.addAll(data);
        this.writeContent();
    }
    
    private void writeContent()
    {
        BufferedWriter bWriter;
        try
        {
            // write the builder contents to the file with the correct switches.
            bWriter = new BufferedWriter(new FileWriter(this.dataFile));
            for (String content: this.data)
            {
                bWriter.write(content);
            }
            bWriter.close();
        }
        catch (IOException ex)
        {
            String msg = "unable to create '" + this.dataFile.getName() + "' file.";
            System.out.println("IOException:" + msg);
        }
    }
    
    private File createFile(String name)
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
