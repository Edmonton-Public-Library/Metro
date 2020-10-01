/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2020  Edmonton Public Library
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
package mecard.requestbuilder;

import api.APICommand;
import api.Command;
import api.CommandStatus;
import api.CustomerMessage;
import api.DummyCommand;
import mecard.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import mecard.QueryTypes;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.MessagesTypes;
import mecard.config.SymphonyPropertyTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.symphony.FlatCustomerFormatter;
import mecard.config.PropertyReader;
import api.FlatCustomerMessage;
import java.io.File;
import mecard.config.CustomerFieldTypes;
import mecard.customer.DumpUser;
import mecard.customer.symphony.FlatFormattedCustomer;
import mecard.customer.FormattedCustomer;
import site.CustomerLoadNormalizer;

/**
 * SymphonyRequestBuilder creates the commands used to perform the MeCard request
 * contract. Symphony has excellent facilities for answering all requests except
 * getting the ILS status. For that reason if you set your request for ILS status
 * in your environment.properties file to 'Symphony' you will get a 
 * UnsupportedOperationException. The way around it is to devise some symphony
 * API that would return the status then implement it in this calls, then switch
 * the extends ILSRequestAdaptor to implements ILSRequestBuilder.
 * 
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 * @see UnsupportedOperationException
 * @see ILSRequestBuilder
 */
public class SymphonyRequestBuilder extends ILSRequestBuilder
{
    private static List<String> seluser;
    private static List<String> dumpflatuser;
    private static List<String> loadFlatUserCreate;
    private static List<String> loadFlatUserUpdate;
    private final String sshServer;
    public final static String SSH_DEFER_MARKER = "DEFER:";
    private final Properties messageProperties;
    private final boolean debug;
    
    public SymphonyRequestBuilder(boolean debug)
    {
        // Lets get the properties from the properties file.
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        Properties symphonyProps = PropertyReader.getProperties(ConfigFileTypes.SYMPHONY);
        String homeLibrary = symphonyProps.getProperty(SymphonyPropertyTypes.USER_LIBRARY.toString());
        this.loadDir = symphonyProps.getProperty(PropertyReader.LOAD_DIR);
        // This is an optional tag that if included will run the commands remotely.
        // sshServer should now have either the name of the ssh server or "" if not defined.
        this.sshServer = symphonyProps.getProperty(PropertyReader.SSH_TAG, "");
        this.debug = debug;
        seluser = new ArrayList<>();
        seluser.add("seluser");
        seluser.add("-iB"); // expects barcode.
        seluser.add("-oU"); // will output user key.
        // Dumpflatuser settings, ready for inclusion in the APICommand object.
        dumpflatuser = new ArrayList<>();
        dumpflatuser.add("dumpflatuser");
        // loadflatuser settings, ready for inclusion in the APICommand object.
        loadFlatUserCreate = new ArrayList<>();
        // /s/sirsi/Unicorn/Bin/loadflatuser -aA -bA -l"ADMIN|PCGUI-DISP" -mc -n -y"EPLMNA"
        // This also works in testing and is more forgiving.
        // /s/sirsi/Unicorn/Bin/loadflatuser -aU -bU -l"ADMIN|PCGUI-DISP" -mc -n -y"EPLMNA"
        // Create user command.
        loadFlatUserCreate.add("loadflatuser");
        loadFlatUserCreate.add("-aU"); // Add base.
        loadFlatUserCreate.add("-bU"); // Add extended.
        loadFlatUserCreate.add("-l\"ADMIN|PCGUI-DISP\"");
        loadFlatUserCreate.add("-mc"); // Create
        loadFlatUserCreate.add("-n"); // Turn off BRS checking if -n is used.
        loadFlatUserCreate.add("-y\"" + homeLibrary + "\"");
        loadFlatUserCreate.add("-d"); // write syslog. check Unicorn/Logs/error for results.
        // Update user command.
        loadFlatUserUpdate = new ArrayList<>();
        loadFlatUserUpdate.add("loadflatuser");
        loadFlatUserUpdate.add("-aR"); // replace base information
        loadFlatUserUpdate.add("-bR"); // Replace extended information
        loadFlatUserUpdate.add("-l\"ADMIN|PCGUI-DISP\""); // User and station.
        loadFlatUserUpdate.add("-mu"); // update
        loadFlatUserUpdate.add("-n"); // turn off BRS checking. // doesn't matter for EPL does matter for Shortgrass.
        loadFlatUserUpdate.add("-d"); // write syslog. check Unicorn/Logs/error for results.
    }
    
    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response)
    {
        // for Symphony, this is a two stage process.
        // 1) call seluser to get the user key. If that fails append message to reponsebuffer.
        // if succeeds
        // 2) create a command that will complete the transaction
        APICommand command = new APICommand.Builder().echo(userId).commandLine(seluser).build();
        CommandStatus status = command.execute();
        switch(status.getStatus())
        {
            case UNAVAILABLE:
                response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                System.out.println(new Date() + "system is currently unavailable." + status.getStderr());
                break;
            case USER_NOT_FOUND:
            case FAIL:
                response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                System.out.println(new Date() + "account not found." + status.getStderr());
                break;
            case OK:
                String customerKey = status.getStdout();
                command = new APICommand.Builder().echo(customerKey).commandLine(dumpflatuser).build();
                System.out.println(new Date() + " Ok.");
                break;
            default:
                response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                System.out.println(new Date() + "an error occured while searching for account."
                        + status.getStderr());
                break;
        }
        return command;
    }

    @Override
    public CustomerFormatter getFormatter()
    {
        return new FlatCustomerFormatter();
    }

    @Override
    public Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        // we have a customer let's convert them to a flat user.
        FormattedCustomer formattedCustomer = new FlatFormattedCustomer(customer);
        // apply library centric normalization to the customer account.
        normalizer.finalize(customer, formattedCustomer, response);
        List<String> flatFileLines = formattedCustomer.getFormattedCustomer();
        // Handle lost cards. We do that here before we end up loading the customer as new.
        // This should rarely get used, pretty much ME thinks its a create but the home
        // library has passed it on as a LOST card with the alt id.
        if (customer.isLostCard())
        {
            StringBuilder fileName = new StringBuilder(this.loadDir);
            if (this.loadDir.endsWith(File.separator) == false)
            {
                fileName.append(File.separator);
            }
            fileName.append(customer.get(CustomerFieldTypes.ALTERNATE_ID));
            new DumpUser.Builder(fileName.toString(), DumpUser.FileType.lost)
                .set(flatFileLines)
                .build();
            return new DummyCommand.Builder()
                .setStatus(0)
                .setStdout(messageProperties.getProperty(MessagesTypes.FAIL_LOSTCARD_TEST.toString()))
                .build();
        }
        // Carry on with a regular build.
        // Output the customer's data to file for reference if they have questions.
        DumpUser dumpUser = new DumpUser.Builder(customer, loadDir, DumpUser.FileType.flat)
                .set(flatFileLines)
                .build();
        // This loads the account without ssh, that is, if the service is running 
        // on the same server as the ILS.
        if (this.sshServer.isEmpty()) // or missing all together.
        {
            return new APICommand.Builder().cat(flatFileLines).commandLine(loadFlatUserCreate).build();
        }
        // So the <entry key="ssh">[user]@[someserver.com]</entry> entry isn't
        // empty, it could be using an external script for loading users. This
        // is used for more complicated load proceedures, like broken link lib-
        // braries on Rehat.
        if (this.sshServer.startsWith(SymphonyRequestBuilder.SSH_DEFER_MARKER))
        {
            // the part after the marker is the path to the executable that
            // will load the customer accounts using any method of it's choosing.
            String customerLoaderExecutable = this.sshServer.substring(SymphonyRequestBuilder.SSH_DEFER_MARKER.length());
            List<String> commandsList = new ArrayList<>();
            commandsList.add(customerLoaderExecutable);
            // Pass in any arguments that the customerLoaderExecutable script.
            // The script that I have written just takes the user file as an 
            // argument and does the testing to see if the customer needs 
            // update or create, but if you have another script mentioned in 
            // the <entry key="ssh">DEFER:/metro/Logs/customer/loaduser.sh</entry> entry
            // you could update this to send a flag.
            commandsList.add(dumpUser.getPath());
            commandsList.add("CREATE"); // loaduser.sh script look for this argument.
            return new APICommand.Builder().commandLine(commandsList).build();
        }
        // This runs if the ssh tag is used in the symphony.properties file and
        // is the default configuration for all Unix variant ILSs so far.
        // <entry key="ssh">[user]@[someserver.com]</entry>
        return new APICommand.Builder(this.sshServer).cat(flatFileLines).commandLine(loadFlatUserCreate).build();
    }
    
    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        // we have a customer let's convert them to a flat user.
        FormattedCustomer formattedCustomer = new FlatFormattedCustomer(customer);
        // apply library centric normalization to the customer account.
        normalizer.finalize(customer, formattedCustomer, response);
        List<String> flatFileLines = formattedCustomer.getFormattedCustomer();
        // Handle lost cards. We do that here before we end up loading the customer as new.
        if (customer.isLostCard())
        {
            StringBuilder fileName = new StringBuilder(this.loadDir);
            if (this.loadDir.endsWith(File.separator) == false)
            {
                fileName.append(File.separator);
            }
            fileName.append(customer.get(CustomerFieldTypes.ALTERNATE_ID));
            new DumpUser.Builder(fileName.toString(), DumpUser.FileType.lost)
                .set(flatFileLines)
                .build();
            return new DummyCommand.Builder()
                .setStatus(0)
                .setStdout(messageProperties.getProperty(MessagesTypes.FAIL_LOSTCARD_TEST.toString()))
                .build();
        }
        // Else handle regular customer load.
        // Output the customer's data to file for reference if they have questions.
        DumpUser dumpUser = new DumpUser.Builder(customer, loadDir, DumpUser.FileType.flat)
                .set(flatFileLines)
                .build();
        if (this.sshServer.isEmpty())
        {
            return new APICommand.Builder().cat(flatFileLines).commandLine(loadFlatUserUpdate).build();
        }
        // So the <entry key="ssh">[user]@[someserver.com]</entry> entry isn't
        // empty, it could be using an external script for loading users. This
        // is used for more complicated load proceedures, like broken link lib-
        // braries on Rehat.
        if (this.sshServer.startsWith(SymphonyRequestBuilder.SSH_DEFER_MARKER))
        {
            // the part after the marker is the path to the executable that
            // will load the customer accounts using any method of it's choosing.
            String customerLoaderExecutable = this.sshServer.substring(SymphonyRequestBuilder.SSH_DEFER_MARKER.length());
            List<String> commandsList = new ArrayList<>();
            commandsList.add(customerLoaderExecutable);
            // Pass in any arguments that the customerLoaderExecutable script.
            commandsList.add(dumpUser.getPath());
            // Pass in any arguments that the customerLoaderExecutable script.
            // The script that I have written just takes the user file as an 
            // argument and does the testing to see if the customer needs 
            // update or create, but if you have another script mentioned in 
            // the <entry key="ssh">DEFER:/metro/Logs/customer/loaduser.sh</entry> entry
            // you could update this to send a flag.
            commandsList.add("UPDATE"); // loaduser.sh script look for this argument.
            return new APICommand.Builder().commandLine(commandsList).build();
        }
        // This runs if the ssh tag is used in the symphony.properties file and
        // is the default configuration for all Unix variant ILSs so far.
        // <entry key="ssh">[user]@[someserver.com]</entry>
        return new APICommand.Builder(this.sshServer).cat(flatFileLines).commandLine(loadFlatUserUpdate).build();
    }

    @Override
    public Command getStatusCommand(Response response)
    {
        throw new UnsupportedOperationException("Not supported. Use SIP2 in configuration."); 
    }

    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response)
    {
        boolean result;
        switch (commandType)
        {
//            case GET_STATUS: // currently handled by default case b/c there is no simple status for Symphony, and we use SIP2.
//                break;
            case NULL:
                response.setCode(ResponseTypes.OK);
                response.setResponse("Null command back at you...");
                result = true;
                break;
            case TEST_CUSTOMER:
                if (status.getStderr().contains("**error number 111"))
                {
                    response.setCode(ResponseTypes.USER_NOT_FOUND);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                    System.out.println("account not found.");
                    result = false;
                }
                else
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    response.setResponse("Customer account found.");
                    result = true;
                }
                break;
            case GET_CUSTOMER:    // see message below all can get an error 111.
                if (status.getStderr().contains("**error number 111"))
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()));
                    System.out.println("account not found.");
                    result = false;
                }
                else
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    response.setResponse("Customer account retreived successfully.");
                    result = true;
                }
                break;
            case CREATE_CUSTOMER:
                if (status.getStderr().contains("**error number 111") || status.getStdout().isEmpty())
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()));
                    System.out.println("There was a problem creating account.");
                    result = false;
                }
                else 
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.SUCCESS_JOIN.toString()));
                    System.out.println("customer created.");
                    result = true;
                }
                break;
            case UPDATE_CUSTOMER:
                if (status.getStderr().contains("**error number 111") || status.getStdout().isEmpty())
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()));
                    System.out.println("customer not updated.");
                    result = false;
                }
                else
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.SUCCESS_UPDATE.toString()));
                    System.out.println("customer updated.");
                    result = true;
                }
                break;
            default:
                response.setCode(ResponseTypes.UNKNOWN);
                response.setResponse(messageProperties.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()));
                System.out.println(SymphonyRequestBuilder.class.getName() 
                        + " doesn't know how to execute the query type: "
                        + commandType.name());
                result = false;
        }
        return result;
    }
    
    @Override
    public boolean tidy()
    {
        // TODO future release to include file cleanup and return false if you can't.
        return true;
    }

    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        return new FlatCustomerMessage(stdout);
    }

    @Override
    public Command testCustomerExists(String userId, String userPin, Response response) 
    {
        return getCustomerCommand(userId, userPin, response);
    }
}
