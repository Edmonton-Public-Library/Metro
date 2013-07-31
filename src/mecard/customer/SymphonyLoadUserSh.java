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
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.exception.InvalidCustomerException;

/**
 *
 * @author metro
 */
public class SymphonyLoadUserSh
{

    private final String shellFileName;
    private File file;
    private StringBuilder fileContent;
    private boolean isDebugMode;

    public static class Builder
    {
        private String fileName;
        private StringBuilder loadFlatUserCommand;
        private boolean isRunAsShell;
        private String flatFilePath; // 
        private boolean isDebugMode;
        private String logFile;  // may fail if you don't have permissions on the current directory.
        private String magicNumber;
        private String unixCommand = "cat ";
        
        public Builder(String fileName)
        {
            this.fileName     = fileName;
            this.isRunAsShell = true;
            this.loadFlatUserCommand = new StringBuilder();
        }
        
        /**
         * Sets the magic number, that is, the initial 'hash bang' string of a 
         * the Unix shell script. Something like "/usr/bin/bash".
         * @param m the magic number.
         * @return Builder object.
         */
        public Builder setMagicNumber(String m)
        {
            this.magicNumber = m;
            return this;
        }
        
        public Builder setUnixCommand(String c)
        {
            this.unixCommand = c;
            return this;
        }
        
        /**
         * Sets the path and name of the flat user data file.
         * @param p
         * @return Builder object.
         */
        public Builder setFlatUserFile(String p)
        {
            this.flatFilePath = p;
            return this;
        }
        
        /** 
         * Controls where and what the log file name is.
         * @param l string name of the log file.
         * @return Builder object.
         */
        public Builder setLogFile(String l)
        {
            this.logFile = l;
            return this;
        }
        
        /**
         * Turns on debugging.
         * @param b debugging on if true and off otherwise.
         * @return Builder object.
         */
        public Builder setDebug(boolean b)
        {
            this.isDebugMode = b;
            return this;
        }
        
        /**
         * The complete path command and arguments required to run load flat user.
         * This method does no error checking on the supplied command line.
         * @param c command line string to run.
         * @return Builder object.
         */
        public Builder setLoadFlatUserCommand(List<String> c)
        {
            for (String s: c)
            {
                this.loadFlatUserCommand.append(s);
                this.loadFlatUserCommand.append(" ");
            }
            return this;
        }

        /**
         * Triggers the builder object to construct the outer class.
         * @return SymphonyLoadUserSh class.
         */
        public SymphonyLoadUserSh build()
        {
            return new SymphonyLoadUserSh(this);
        }

    }
    
    private boolean isValid(String flatFilePath)
    {
        File f = new File(flatFilePath);
        if (f.exists())
            return true;
        return false;
    }

    private SymphonyLoadUserSh(Builder b)
    {
        isDebugMode = b.isDebugMode;
        fileContent = new StringBuilder();
        if (b.magicNumber != null)
        {
            fileContent.append(b.magicNumber);
            fileContent.append("\n");
        }
        // Usually cat the content of the file, but that can be changed.
        fileContent.append(b.unixCommand);
        if (isValid(b.flatFilePath))
        {
            fileContent.append(b.flatFilePath);
        }
        else
        {
            String msg = "";
            throw new InvalidCustomerException(msg);
        }
        fileContent.append(" | ");
        fileContent.append(b.loadFlatUserCommand.toString());
        if (b.logFile != null)
        {
            fileContent.append(" >> ");
            fileContent.append(b.logFile);
            fileContent.append(" 2>&1");
        }
        fileContent.append("\n");
                
        // do we need to output as a batch file or command line args?
        shellFileName = b.fileName; // regardless ensure the file name is initialized.
        if (b.isRunAsShell)
        {
            // test if this file exists and if it does, delete it and create a new
            // one with the values required.
            this.file = new File(shellFileName);
            if (file.exists())
            {
                file.delete();
            }
            // create a new one.
            file = new File(shellFileName);
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
                String msg = "unable to create load flat user shell file.";
                Logger.getLogger(SymphonyLoadUserSh.class.getName()).log(Level.SEVERE, msg, ex);
            }
            file.setExecutable(true);
            if (b.isDebugMode)
            {
                System.out.print("EXE_SET:" + file.canExecute());
            }
        }
    }
    
    /**
     * 
     * @return the name of the batch file.
     */
    public List<String> getBatchFileName()
    {
        return getCommandLine();
    }
    
    /**
     * 
     *
     * @return the command line necessary to run the shell script.
     */
    
    public List<String> getCommandLine()
    {
        List<String> cmdLine = new ArrayList<String>();
//        if (isDebugMode)
//        {
//            // my god this is bad: TODO think of something better than this!
//            // there is a shell script in this directory.
//            cmdLine.add("/home/metro/bimport/bimport");
//        }
//        else
//        {
            cmdLine.add(shellFileName);
//        }
       
        return cmdLine;
    }
}
