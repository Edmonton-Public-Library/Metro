/*
 * Copyright (C) 2013 metro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mecard.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author metro
 */
public class BImportBat
{

    private final String fileName;
    private File file;

    public static class Builder
    {
        private final String fileName;
        private String user;
        private String server;
        private String password;
        private String database;
        private String headerFileName;
        private String dataFileName;
        private String alias;
        private String format;
        private String bType;
        private String mType;
        private String location;
        private boolean isIndexed;

        public Builder(String fileName)
        {
            this.fileName = fileName;
        }
        
        public Builder setIndexed(boolean i)
        {
            this.isIndexed = i;
            return this;
        }
        
        public Builder location(String l)
        {
            this.location = l;
            return this;
        }
        
        public Builder mType(String m)
        {
            this.mType = m;
            return this;
        }
        
        public Builder bType(String b)
        {
            this.bType = b;
            return this;
        }
        
        public Builder format(String f)
        {
            this.format = f;
            return this;
        }
        
        public Builder alias(String a)
        {
            this.alias = a;
            return this;
        }
        
        public Builder data(String d)
        {
            this.dataFileName = d;
            return this;
        }
        
        public Builder header(String h)
        {
            this.headerFileName = h;
            return this;
        }
        
        public Builder database(String d)
        {
            this.database = d;
            return this;
        }
        
        public Builder password(String p)
        {
            this.password = p;
            return this;
        }

        public Builder server(String s)
        {
            this.server = s;
            return this;
        }

        public Builder user(String u)
        {
            this.user = u;
            return this;
        }

        public BImportBat build()
        {
            return new BImportBat(this);
        }

        private boolean isIndexed()
        {
            return isIndexed;
        }
    }

    private BImportBat(Builder b)
    {
        StringBuilder fileContent = new StringBuilder();
        fileContent.append("bimport");
        if (b.server != null)
        {
            fileContent.append("/s");
            fileContent.append(b.server);
        }
        if (b.password != null)
        {
            fileContent.append("/p");
            fileContent.append(b.password);
        }
        if (b.user != null)
        {
            fileContent.append("/u");
            fileContent.append(b.user);
        }
        if (b.database != null)
        {
            fileContent.append("/d");
            fileContent.append(b.database);
        }
        if (b.headerFileName != null)
        {
            fileContent.append("/h");
            fileContent.append(b.headerFileName);
        }
        if (b.dataFileName != null)
        {
            fileContent.append("/i");
            fileContent.append(b.dataFileName);
        }
        if (b.alias != null)
        {
            fileContent.append("/k");
            fileContent.append(b.alias);
        }
        if (b.format != null)
        {
            fileContent.append("/f");
            fileContent.append(b.format);
        }
        if (b.bType != null)
        {
            fileContent.append("/b");
            fileContent.append(b.bType);
        }
        if (b.mType != null)
        {
            fileContent.append("/m");
            fileContent.append(b.mType); // mail type (default)
        }
        if (b.location != null)
        {
            fileContent.append("/l");
            fileContent.append(b.location);
        }
        if (b.isIndexed())
        {
            fileContent.append("/y");
        }
        this.fileName = b.fileName;
        // test if this file exists and if it does, delete it and create a new
        // one with the values required.
        this.file = new File(fileName);
        if (file.exists())
        {
            file.delete();
        }
        // create a new one.
        file = new File(fileName);
        BufferedWriter bWriter;
        try
        {
            // write the builder contents to the file with the correct switches.
            bWriter = new BufferedWriter(new FileWriter(file));
            bWriter.write(fileContent.toString());
            bWriter.close();
        }
        catch (IOException ex)
        {
            String msg = "unable to create bimport bat file.";
            Logger.getLogger(BImportBat.class.getName()).log(Level.SEVERE, msg, ex);
        }
    }
}
