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
 */
package mecard.customer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import mecard.config.CustomerFieldTypes;

/**
 * Dumps user data to a file. Used to write receipts, lost card, and fail card data.
 * @author anisbet <anisbet@epl.ca>
 */
public class DumpUser
{
    public enum FileType
    {
        pass,
        fail,
        lost,
        flat,
        data,
        txt;
    }
    
    public static class Builder
    {
        private final String path;
        private List<String> data;
        /**
         * Note that the constructor uses the customer for the file name. No
         * other data from customer is saved. If you want to dump the contents
         * of customer use the {@link #set(mecard.customer.Customer) } method.
         * @param c customer to be dumped to file.
         * @param path where you want the file to be written.
         * @param type type of file you wish to write.
         * @see FileType
         */
        public Builder(
                Customer c,
                String path, 
                DumpUser.FileType type)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            if (path.endsWith(File.separator) == false)
            {
                sb.append(File.separator);
            }
            sb.append(c.get(CustomerFieldTypes.ID));
            sb.append(".");
            sb.append(type.name());
            this.path = sb.toString();
            this.data = new ArrayList<>();
        }
        
        /**
         * Creates a dump user object where the file name is included at the end
         * of the path parameter. Do not include a file extension.
         * @param path full path to the file including the file's name. 
         * @param type type of output file to write. Uses the value as an extension.
         */
        public Builder(String path, DumpUser.FileType type)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            sb.append(".");
            sb.append(type.name());
            this.path = sb.toString();
            this.data = new ArrayList<>();
        }
        
        public Builder set(String s)
        {
            if (s != null) this.data.add(s);
            return this;
        }
        
        /**
         * Sets the dump file's contents to the values stored in d.
         * @param d data as a list.
         * @return builder object.
         */
        public Builder set(List<String> d)
        {
            if (d != null) this.data.addAll(d);
            return this;
        }
        
        /**
         * Sets the dump file's contents to the data in customer.
         * @param c data as a {@link Customer#toString() } object.
         * @return builder object.
         */
        public Builder set(Customer c)
        {
            if (c != null) this.data.add(c.toString());
            return this;
        }
        
        /**
         * Sets the dump file's contents to the data in customer.
         * @param c data as a {@link MeCardCustomerToNativeFormat } object.
         * @return builder object.
         */
        public Builder set(MeCardCustomerToNativeFormat c)
        {
            if (c != null) this.set(c.getFormattedCustomer());
            return this;
        }
        
        /**
         * Required method to finalize the creation of the DumpUser object.
         * @return DumpUser object.
         */
        public DumpUser build()
        {
            return new DumpUser(this);
        }
    }
    
    private final List<String> data;
    private final String path;
    
    private DumpUser(Builder b)
    {
        this.data = b.data;
        this.path = b.path;
        this.writeContent();
    }
    
    /**
     *
     * @return true if the content was written to file and false otherwise.
     */
    private void writeContent()
    {
        File dataFile = createFile(this.path);
        BufferedWriter bWriter;
        try
        {
            // write the builder contents to the file with the correct switches.
            bWriter = new BufferedWriter(new FileWriter(dataFile));
            for (String content: this.data)
            {
                bWriter.write(content);
            }
            bWriter.close();
        }
        catch (IOException ex)
        {
            String msg = "unable to create \n'" + dataFile.getPath() 
                    + "' file.";
            System.out.println("IOException:" + msg);
        }
    }
    
    /**
     * Creates a new UserFile, deleting the old one if it exists.
     * @param name - fully qualified file name.
     * @return File handle to the user file.
     */
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
    
    /**
     * Returns a string of the path of the DumpUser file.
     * @return string of the file's path.
     */
    public String getPath()
    {
        return this.path;
    }
}
