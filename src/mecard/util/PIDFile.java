/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
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
package mecard.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author anisbet
 */


public class PIDFile
{
    private File file;

    public PIDFile(String path)
    {
        this.file = new File(path);
    }

    public boolean exists()
    {
        return file.exists();
    }

    public boolean touch()
    {
        try
        {
            // write the builder contents to the file with the correct switches.
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(this.file));
            String content = new Date() + ": created.\r\n";
            bWriter.write(content);
            bWriter.close();
            return true;
        }
        catch (IOException ex)
        {
            String msg = "unable to create \n'" + this.file.getPath() 
                    + "' file.";
            System.out.println("IOException:" + msg);
            return false;
        }
    }

    /** Returns the number of seconds since the Unix epoch for the PIDFile.
     * A long value representing the time the file was last modified, 
     * measured in milliseconds since the epoch (00:00:00 GMT, January 1, 1970),
     * or 0L if the file does not exist or if an I/O error occurs.
     * @return long last modified time or 0L on error.
     */
    public long lastModified()
    {
        return this.file.lastModified();
    }

    public boolean delete()
    {
        return this.file.delete();
    }

    public String getAbsolutePath()
    {
        return this.file.getAbsolutePath();
    }
}
