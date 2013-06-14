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

import java.util.ArrayList;
import java.util.List;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.FlatUserFormatter;
import mecard.util.Command;
import mecard.util.ProcessWatcherHandler;

/**
 *
 * @author andrew
 */


public class SymphonyAPIBuilder implements APIBuilder
{
    private static List<String> seluser;
    private static List<String> dumpflatuser;
    private static List<String> loadFlatUserCreate;
    private static List<String> loadFlatUserUpdate;
    
    public SymphonyAPIBuilder()
    {
        seluser = new ArrayList<String>();
        seluser.add("/home/metro/bimport/seluser");
        seluser.add("-iB"); // expects barcode.
        seluser.add("-oU"); // will output user key.
//        seluser.add("wc");
//        seluser.add("-l"); // expects barcode.
        // Dumpflatuser settings, ready for inclusion in the Command object.
        dumpflatuser = new ArrayList<String>();
        dumpflatuser.add("/home/metro/bimport/dumpflatuser");
        // loadflatuser settings, ready for inclusion in the Command object.
        loadFlatUserCreate = new ArrayList<String>();
        loadFlatUserCreate.add("/home/metro/bimport/loadflatuser");
        // TODO add flags for creating user.
        // Update user command.
        loadFlatUserUpdate = new ArrayList<String>();
        loadFlatUserUpdate.add("/home/metro/bimport/loadflatuser");
        // TODO add flags for updating user.
    }
    
    /**
     *
     *
     * @param userId the value of userId
     * @param userPin the value of userPin
     */
    @Override
    public Command getUser(String userId, String userPin, StringBuffer responseBuffer)
    {
        // for Symphony, this is a two stage process.
        // 1) call seluser to get the user key. If that fails append message to reponsebuffer.
        // if succeeds
        // 2) create a command that will complete the transaction
        Command command = new Command.Builder().echo(userId).args(seluser).build();
        ProcessWatcherHandler commandRun = command.execute();
        switch(commandRun.getStatus())
        {
            case UNAVAILABLE:
                responseBuffer.append("I'm sorry the system is currently unavailable.");
                break;
            case FAIL:
                responseBuffer.append("I can't find your account. "
                        + "Pleae check your library card and try again.");
                responseBuffer.append(commandRun.getStderr());
                break;
            case OK:
                String customerKey = commandRun.getStdout();
                command = new Command.Builder().echo(customerKey).args(dumpflatuser).build();
                break;
            default:
                responseBuffer.append("an error occured while searching for "
                        + "your account, please contact the system's administrator.");
                responseBuffer.append(commandRun.getStderr());
                break;
        }
        return command;
    }

    @Override
    public CustomerFormatter getFormatter()
    {
        return new FlatUserFormatter();
    }

    @Override
    public Command createUser(Customer customer, StringBuffer responseBuffer)
    {
        // we have a customer let's convert them to a flat user.
        List<String> flatUser = getFormatter().setCustomer(customer);
        Command command = new Command.Builder().cat(flatUser).args(loadFlatUserCreate).build();
        return command;
    }

    @Override
    public Command updateUser(Customer customer, StringBuffer responseBuffer)
    {
        // we have a customer let's convert them to a flat user.
        List<String> flatUser = getFormatter().setCustomer(customer);
        Command command = new Command.Builder().cat(flatUser).args(loadFlatUserUpdate).build();
        return command;
    }
    
}
