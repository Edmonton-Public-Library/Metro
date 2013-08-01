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
 */
package api;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import mecard.exception.MalformedCommandException;
import mecard.MetroService;
import mecard.QueryTypes;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.FlatUserFormatter;
import mecard.customer.SymphonyLoadUserSh;
import mecard.customer.UserFile;

/**
 * SymphonyAPIBuilder creates the commands used to perform the MeCard request
 * contract. Symphony has excellent facilities for answering all requests except
 * getting the ILS status. For that reason if you set your request for ILS status
 * in your environment.properties file to 'Symphony' you will get a 
 * UnsupportedOperationException. The way around it is to devise some symphony
 * API that would return the status then implement it in this calls, then switch
 * the extends ILSRequestAdaptor to implements ILSRequestBuilder.
 * @author andrew
 * @see UnsupportedOperationException
 * @see ILSRequestBuilder
 */
public class SymphonyAPIBuilder implements ILSRequestBuilder
{
    private static List<String> seluser;
    private static List<String> dumpflatuser;
    private static List<String> loadFlatUserCreate;
    private static List<String> loadFlatUserUpdate;
    public final static String USER_FILE_NAME_PREFIX  = "metro_user_";
    public final static String SHELL_FILE_NAME_PREFIX = "metro_load_";
    private final String homeDirectory;
    private final String shell;
    
    public SymphonyAPIBuilder()
    {
        // Lets get the properties from the properties file.
//        Properties properties = MetroService.getProperties(ConfigFileTypes.API);
//        String upath = properties.getProperty(APIPropertyTypes.UPATH.toString());
        Properties defaultProperties = MetroService.getProperties(ConfigFileTypes.DEFAULT_CREATE);
        String homeLibrary = defaultProperties.getProperty("USER_LIBRARY");
        // The default values for creating a user vary from ILS to ILS so there
        // is no error checking for mandatory values of the default.properties file.
        // We do the checking here.
        if (homeLibrary == null || homeLibrary.isEmpty())
        {
            String msg = "Symphony requires a user to be given a default home-library. "
                    + "Please specify one in default.properties config file.";
            throw new MalformedCommandException(msg);
        }
        
        Properties sysProps = MetroService.getProperties(ConfigFileTypes.VARS);
        this.homeDirectory = sysProps.getProperty("METRO_HOME");
        // The default values for creating a user vary from ILS to ILS so there
        // is no error checking for mandatory values of the default.properties file.
        // We do the checking here.
        if (homeDirectory == null || homeDirectory.isEmpty())
        {
            String msg = "Metro requires a METRO_HOME variable to be defined. "
                    + "Please specify one in sysvar.properties config file.";
            throw new MalformedCommandException(msg);
        }
        
        this.shell = sysProps.getProperty("SHELL");
        // The default values for creating a user vary from ILS to ILS so there
        // is no error checking for mandatory values of the default.properties file.
        // We do the checking here.
        if (shell == null || shell.isEmpty())
        {
            String msg = "Metro requires a SHELL variable to be defined. "
                    + "Please specify one in sysvar.properties config file.";
            throw new MalformedCommandException(msg);
        }
        
        seluser = new ArrayList<String>();
//        seluser.add(upath + "seluser");
        seluser.add("seluser");
        seluser.add("-iB"); // expects barcode.
        seluser.add("-oU"); // will output user key.
        // Dumpflatuser settings, ready for inclusion in the Command object.
        dumpflatuser = new ArrayList<String>();
//        dumpflatuser.add(upath + "dumpflatuser");
        dumpflatuser.add("dumpflatuser");
        // loadflatuser settings, ready for inclusion in the Command object.
        loadFlatUserCreate = new ArrayList<String>();
        // /s/sirsi/Unicorn/Bin/loadflatuser -aA -bA -l"ADMIN|PCGUI-DISP" -mc -n -y"EPLMNA"
//        loadFlatUserCreate.add(upath + "loadflatuser");
        loadFlatUserCreate.add("loadflatuser");
        loadFlatUserCreate.add("-aA"); // Add base.
        loadFlatUserCreate.add("-bA"); // Add extended.
        loadFlatUserCreate.add("-l\"ADMIN|PCGUI-DISP\"");
        loadFlatUserCreate.add("-mc"); // Create
        loadFlatUserCreate.add("-n"); // Turn off BRS checking.
        loadFlatUserCreate.add("-y\"" + homeLibrary + "\"");
        loadFlatUserCreate.add("-d"); // write syslog. check Unicorn/Logs/error for results.
        // Update user command.
        loadFlatUserUpdate = new ArrayList<String>();
//        loadFlatUserUpdate.add(upath + "loadflatuser");
        loadFlatUserUpdate.add("loadflatuser");
        loadFlatUserUpdate.add("-aR"); // replace base information
        loadFlatUserUpdate.add("-bR"); // Replace extended information
        loadFlatUserUpdate.add("-l\"ADMIN|PCGUI-DISP\""); // User and station.
        loadFlatUserUpdate.add("-mu"); // update
        loadFlatUserUpdate.add("-n"); // turn off BRS checking.
        loadFlatUserUpdate.add("-d"); // write syslog. check Unicorn/Logs/error for results.
//        loadFlatUserUpdate.add("/s/sirsi/mecard/load.sh");
    }
    
    /**
     *
     *
     * @param userId the value of userId
     * @param userPin the value of userPin
     */
    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response)
    {
        // for Symphony, this is a two stage process.
        // 1) call seluser to get the user key. If that fails append message to reponsebuffer.
        // if succeeds
        // 2) create a command that will complete the transaction
        Command command = new Command.Builder().echo(userId).args(seluser).build();
        ProcessWatcherHandler status = command.execute();
        switch(status.getStatus())
        {
            case UNAVAILABLE:
                response.setResponse("I'm sorry the system is currently unavailable.");
                break;
            case FAIL:
                response.setResponse("I can't find your account. "
                        + "Pleae check your library card and try again."
                        + status.getStderr());
                break;
            case OK:
                String customerKey = status.getStdout();
                command = new Command.Builder().echo(customerKey).args(dumpflatuser).build();
                break;
            default:
                response.setResponse("an error occured while searching for "
                        + "your account, please contact the system's administrator."
                        + status.getStderr());
                break;
        }
//        System.out.println("COMMAND_GET_USER:" + command.toString());
        return command;
    }

    @Override
    public CustomerFormatter getFormatter()
    {
        return new FlatUserFormatter();
    }

    @Override
    public Command getCreateUserCommand(Customer customer, Response response)
    {
        // we have a customer let's convert them to a flat user.
        CustomerFormatter formatter = getFormatter();
        List<String> flatUser = formatter.setCustomer(customer);
        // since printing Lists of strings to loadflatuser fail, we will do it the Sirsi-Dynix
        // way: create a temp file of user data and cat the data to loadflatuser.
        // Job 1: create user data file.
        // 1a create file name
        String userDataFileName = this.homeDirectory 
                + File.separator 
                + SymphonyAPIBuilder.USER_FILE_NAME_PREFIX 
                + customer.get(CustomerFieldTypes.ID) 
                + ".flat";
        UserFile userFile = new UserFile(userDataFileName);
        userFile.addUserData(flatUser);
        // Now create the shell command to run
        String shellFileName = this.homeDirectory 
                + File.separator  
                + SymphonyAPIBuilder.SHELL_FILE_NAME_PREFIX 
                + customer.get(CustomerFieldTypes.ID) 
                + ".sh";
        SymphonyLoadUserSh loadUserFile = new SymphonyLoadUserSh.Builder(shellFileName)
                .setDebug(true)
                // turning on the logging passes the stdout and err to a log file
                // which is much harder to test for success or failure. To over come
                // this we will not direct stdout or stderr, we will then test the 
                // command status buffers.
//                .setLogFile(this.homeDirectory + File.separator + "load.log")
                .setMagicNumber("#!" + this.shell)
                .setFlatUserFile(userDataFileName)
                .setLoadFlatUserCommand(loadFlatUserCreate)
                .build();
        
        Command command = new Command.Builder().args(loadUserFile.getCommandLine()).build();
//        System.out.println("COMMAND_CREATE_USER:" + command.toString());
        return command;
    }

    /**
     *
     * @param customer the value of customer
     * @param response the value of responseBuffer
     */
    @Override
    public Command getUpdateUserCommand(Customer customer, Response response)
    {
        // we have a customer let's convert them to a flat user.
        CustomerFormatter formatter = getFormatter();
        List<String> flatUser = formatter.setCustomer(customer);
        // since printing Lists of strings to loadflatuser fail, we will do it the Sirsi-Dynix
        // way: create a temp file of user data and cat the data to loadflatuser.
        // Job 1: create user data file.
        // 1a create file name
        String userDataFileName = this.homeDirectory 
                + File.separator
                + SymphonyAPIBuilder.USER_FILE_NAME_PREFIX 
                + customer.get(CustomerFieldTypes.ID) 
                + ".flat";
        UserFile userFile = new UserFile(userDataFileName);
        userFile.addUserData(flatUser);
        // Now create the shell command to run
        String shellFileName = this.homeDirectory 
                + File.separator
                + SymphonyAPIBuilder.SHELL_FILE_NAME_PREFIX 
                + customer.get(CustomerFieldTypes.ID) 
                + ".sh";
        SymphonyLoadUserSh loadUserFile = new SymphonyLoadUserSh.Builder(shellFileName)
                .setDebug(true)
                // turning on the logging passes the stdout and err to a log file
                // which is much harder to test for success or failure. To over come
                // this we will not direct stdout or stderr, we will then test the 
                // command status buffers.
//                .setLogFile(this.homeDirectory + File.separator + "load.log")
                .setMagicNumber("#!"+shell)
                .setFlatUserFile(userDataFileName)
                .setLoadFlatUserCommand(loadFlatUserUpdate)
                .build();
        
        Command command = new Command.Builder().args(loadUserFile.getCommandLine()).build();
        System.out.println("COMMAND_UPDATE_USER:" + command.toString());
        return command;
    }

    @Override
    public Command getStatusCommand(Response response)
    {
        throw new UnsupportedOperationException("Not supported. Use SIP2 in configuration."); 
    }

    @Override
    public void interpretResults(QueryTypes commandType, ProcessWatcherHandler status, Response response)
    {
        switch (commandType)
        {
//            case GET_STATUS: // currently handled by default case b/c there is no simple status for Symphony, and we use SIP2.
//                break;
            case NULL:
                response.setCode(ResponseTypes.OK);
                response.setResponse("Null command back at you...");
                break;
            case GET_CUSTOMER:    // see message below all can get an error 111.
                if (status.getStderr().contains("**error number 111"))
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse("We're having trouble finding your account."
                            + " Please contact us, and we'll find out what's going on.");
                }
                else
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    response.setResponse("Customer account retreived successfully.");
                }
                break;
            case CREATE_CUSTOMER:
                if (status.getStderr().contains("**error number 111"))
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse("There was a problem creating your account."
                            + " Please contact us, and we will find out why.");
                }
                else
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    response.setResponse("Welcome aboard, and thanks for using our service and supporting your local libraries!");
                }
                break;
            case UPDATE_CUSTOMER:
                if (status.getStderr().contains("**error number 111"))
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse("There was a problem updating your account."
                            + " Please contact us, and we will get to the bottom of this.");
                }
                else
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    response.setResponse("Thanks for keeping us up-to-date, and for supporting your local libraries!");
                }
                break;
            default:
                response.setCode(ResponseTypes.UNKNOWN);
                response.setResponse(SymphonyAPIBuilder.class.getName() 
                        + " doesn't know how to execute the query type: "
                        + commandType.name());
        }
    }
}
