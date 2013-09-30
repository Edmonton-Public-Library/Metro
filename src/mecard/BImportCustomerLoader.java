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
package mecard;

import api.APICommand;
import api.Command;
import api.CommandStatus;
import api.DummyCommand;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.config.PropertyReader;
import mecard.customer.BImportBat;
import mecard.customer.UserFile;
import mecard.exception.BImportException;
import mecard.requestbuilder.BImportRequestBuilder;
import static mecard.requestbuilder.BImportRequestBuilder.BAT_FILE;
import static mecard.requestbuilder.BImportRequestBuilder.FILE_NAME_PREFIX;
import static mecard.requestbuilder.BImportRequestBuilder.HEADER_FILE;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This class queues the bimport customers and, run as a timed process, will load 
 * all the customers found in the Customer directory since the last time it ran.
 * This is done because bimport cannot be run concurrently.
 * java -cp MeCard.jar mecard.BImportCustomerLoader
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class BImportCustomerLoader 
{
    /**
     * Runs the entire process of loading customer bimport files as a timed 
     * process such as cron or Windows scheduler.
     * @param args 
     */
    public static void main(String args[])
    {
        // First get the valid options
        Options options = new Options();
        // add t option c to config directory true=arg required.
        options.addOption("c", true, "configuration file directory path, include all sys dependant dir seperators like '/'.");
        // add t option c to config directory true=arg required.
        options.addOption("v", false, "Metro server version information.");
        options.addOption("U", false, "execute upload of customer accounts, otherwise just cleans up the directory.");
        try
        {
            // parse the command line.
            CommandLineParser parser = new BasicParser();
            CommandLine cmd;
            cmd = parser.parse(options, args);
            if (cmd.hasOption("v"))
            {
                System.out.println("Metro (MeCard) server version " + PropertyReader.VERSION);
                return; // don't run if user just wants version.
            }
            if (cmd.hasOption("U"))
            {
                uploadCustomers = true;
            }
             // get c option value
            String configDirectory = cmd.getOptionValue("c");
            PropertyReader.setConfigDirectory(configDirectory);
            BImportCustomerLoader loader = new BImportCustomerLoader();
            loader.run();
        } 
        catch (ParseException ex)
        {
            String msg = new Date() + "Unable to parse command line option. Please check your service configuration.";
            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
            System.exit(899); // 799 for mecard
        }
    }

    private BImportLoadRequestBuilder loadRequestBuilder;
    private static boolean uploadCustomers = false;
    
    public BImportCustomerLoader()
    {
        this.loadRequestBuilder = new BImportLoadRequestBuilder(true);
    }
    
    /**
     * Runs the BImport load and cleans the directory. The even if the bimport
     * load cannot be run all but one existing header will be removed, all 
     * batch files will be removed and all patron account files will be concatenated
     * into one big bimport file. The header and bimport file always have unique
     * names and are never removed in this process.
     */
    public void run()
    {
        List<String> fileList = getFileList(
                this.loadRequestBuilder.getLoadDir(), 
                BImportRequestBuilder.HEADER_FILE);
        touchHeader(fileList.get(0));
        clean(fileList); // get rid of the header files.
        fileList = getFileList(
                this.loadRequestBuilder.getLoadDir(), 
                BImportRequestBuilder.BAT_FILE);
        clean(fileList); // get rid of the bat files.
        // This process needs to run to format the user data
        fileList = getFileList(
                this.loadRequestBuilder.getLoadDir(), 
                BImportRequestBuilder.DATA_FILE);
        Command command = this.loadRequestBuilder.loadCustomers(fileList);
        if (uploadCustomers)
        {
            // But only run the command if the user requests.
            CommandStatus status = command.execute();
            String rpt = new Date() + " LOAD_STDOUT:" + status.getStdout() 
                    + "\r\n LOAD_STDERR:" + status.getStderr() + "\r\n";
            Logger.getLogger(MetroService.class.getName()).log(Level.INFO, rpt);
            System.out.println(rpt);
        }
        clean(fileList); // get rid of the bat files. All contents are in the main data file.
    }
   
    /**
     * Creates a list of strings of fully-qualified path names for files within
     * a given directory that end with the argument file suffix.
     * @param loadDir Specifies a directory to search.
     * @param fileSuffix Specifies the type of files to search. The supplied string
     * will be used in the {@link String#endsWith(java.lang.String) } method.
     */
    protected List<String> getFileList(String loadDir, String fileSuffix)
    {
        List<String> textFiles = new ArrayList<>();
        File dir = new File(loadDir);
        for (File file : dir.listFiles()) 
        {
            if (file.getName().endsWith((fileSuffix))) 
            {
                textFiles.add(file.getAbsolutePath());
            }
        }
        return textFiles;
    }
    
    /**
     * Removes any preexisting header file, creating a new one.
     */
    protected void deletePreExistingHeader()
    {
        // Get rid of the old header if it exists.
        File fTest = new File(this.loadRequestBuilder.getHeaderName());
        if (fTest.exists())
        {
            fTest.delete();
        }
    }

    /**
     * Creates a header file from the data in one of the existing headers. If no
     * header files are present a FileNotFoundException is thrown.
     * 
     * @param fileName
     */
    protected void touchHeader(String fileName)
    {
        deletePreExistingHeader();
        File fSrc  = new File(fileName);
        File fDest = new File(this.loadRequestBuilder.getHeaderName());
        if (fSrc.renameTo(fDest))
        {
            System.out.println("File '" + fDest.getAbsolutePath() + "' created.");
        }
        else
        {
            System.out.println("File '" + fileName + "' could not be moved to '" +
                    fDest.getAbsolutePath() + "'\nThere must be a pre-existing header"
                    + " to act as a template for the bimport header that matches"
                    + " the customer's data files.");
        }
    }

    /**
     * 
     * @param fileList names of all files that are to be deleted.
     */
    protected void clean(List<String> fileList)
    {
        if (fileList == null)
        {
            return;
        }
        for (String file: fileList)
        {
            File f = new File(file);
            f.delete();  
        }
    }
    
    /**
     * This class is a specialization of BImportRequestBuilder whose job it is
     * to load 
     */
    final class BImportLoadRequestBuilder extends BImportRequestBuilder
    {
        public BImportLoadRequestBuilder(boolean b)
        {
            super(b);
            // compute header and data file names.
            if (loadDir.endsWith(File.separator) == false)
            {
                loadDir += File.separator;
            }
            if (bimportDir.endsWith(File.separator) == false)
            {
                bimportDir += File.separator;
            } 
            Date today = new Date();
            long longTime = today.getTime();
            batFile    = loadDir + FILE_NAME_PREFIX + longTime + BAT_FILE;
            headerFile = loadDir + FILE_NAME_PREFIX + longTime + HEADER_FILE;
            dataFile   = loadDir + FILE_NAME_PREFIX + longTime + DATA_FILE_BIMPORT;
        }
        
        /**
         * 
         * @return The fully qualified path to the customer load directory 
         * specified in the bimport.properties file.
         */
        final String getLoadDir()
        {
            return this.loadDir;
        }
        
        /**
         * Prepares the command that will load the bimport file.
         * @param files
         * @return command that {@link BImportCustomerLoader} will run.
         */
        final Command loadCustomers(List<String> files)
        {
            File fTest = new File(headerFile);
            if (fTest.exists() == false)
            {
                throw new BImportException(BImportRequestBuilder.class.getName()
                        + " Could not create header file: '" + headerFile + "'.");
            }
            List<String> customersData = getCustomerData(files);
            // if there were no customers to load return a command that does nothing.
            if (customersData.isEmpty())
            {
                return new DummyCommand.Builder()
                .setStatus(0)
                .setStdout(BImportRequestBuilder.SUCCESS_MARKER.toString())
                .build(); // empty command always returns success
            }
            UserFile bimportDataFile = new UserFile(dataFile);
            bimportDataFile.addUserData(customersData);
            // Ok the goal is to get the path to the batch file here with the name.
            BImportBat batch = new BImportBat.Builder(batFile)
                    .setBimportPath(bimportDir)
                    .server(serverName).password(password)
                    .user(userName).database(database)
                    .header(headerFile).data(dataFile)
                    .borrowerTableKey(uniqueBorrowerTableKey).format(bimportVersion).bType(defaultBtype)
                    .mType(mailType).location(location).setIndexed(Boolean.valueOf(isIndexed))
    //                .setDebug(debug) // not used in class yet.
                    .build();
            List<String> bimportBatExec = new ArrayList<>();
            batch.getCommandLine(bimportBatExec);
            Command command = new APICommand.Builder().commandLine(bimportBatExec).build();
            return command;
        }

        /**
         * Takes a list of files and concatenates the contents of all files into
         * a big single bimport file.
         * @param files list of customer data files with fully-qualified path.
         * @return List of the contents of all the files in argument files.
         */
        protected List<String> getCustomerData(List<String> files)
        {
            // for each file open it get the lines append them to the big list and close
            List<String> bigListOfAllCustomerData = new ArrayList<>();
            for (String file: files)
            {
                BufferedReader br = null;
                try 
                {
                    File f = new File(file);
                    if (! f.exists())
                    {
                        continue;
                    }
                    br = new BufferedReader(new FileReader(f));
                    String line;
                    // add all the lines.
                    while ((line = br.readLine()) != null) 
                    {
                       bigListOfAllCustomerData.add(line + "\r\n");
                    }
                    br.close();
                } 
                catch (FileNotFoundException ex)
                {
                    // a file may be missing but keep checking the others.
                    System.out.println("'" + file + "' not found.");
                    continue;
                } 
                catch (IOException ex)
                {
                    Logger.getLogger(BImportCustomerLoader.class.getName()).log(Level.SEVERE, null, ex);
                } 
                finally
                {
                    try
                    {
                        br.close();
                    } catch (IOException | NullPointerException ex)
                    {
                        return bigListOfAllCustomerData;
                    }
                }
            }
            return bigListOfAllCustomerData;
        }

        /**
         * 
         * @return header file name.
         */
        protected String getHeaderName()
        {
            return this.headerFile;
        }
    }
}
