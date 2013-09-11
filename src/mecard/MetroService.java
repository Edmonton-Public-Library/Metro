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

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.commons.daemon.*;
/**
 * This is the entry point of the MeCard server application.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class MetroService implements Daemon
{
    public final static String VERSION       = "0.7.6"; // server version
    private static ServerSocket serverSocket = null;
    private static boolean listening         = true;
    private static String defaultPort        = "2004";
    private static String[] ARGS;

    public static void main(String[] args)
    {
        // First get the valid options
        Options options = new Options();
        // add t option c to config directory true=arg required.
        options.addOption("c", true, "configuration file directory path, include all sys dependant dir seperators like '/'.");
        // add t option c to config directory true=arg required.
        options.addOption("v", false, "Metro server version information.");
        try
        {
            // parse the command line.
            CommandLineParser parser = new BasicParser();
            CommandLine cmd;
            cmd = parser.parse(options, args);
            if (cmd.hasOption("v"))
            {
                System.out.println("Metro (MeCard) server version " + VERSION);
            }
             // get c option value
            String configDirectory = cmd.getOptionValue("c");
            PropertyReader.setConfigDirectory(configDirectory);
        } 
        catch (ParseException ex)
        {
//            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(new Date() + "Unable to parse command line option. Please check your service configuration.");
            System.exit(799);
        }
        
        Properties properties = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        String portString = properties.getProperty(LibraryPropertyTypes.METRO_PORT.toString(), defaultPort);
        
        try
        {
            int port = Integer.parseInt(portString);
            serverSocket = new ServerSocket(port);
        }
        catch (IOException ex)
        {
            String msg = "Could not listen on port: " + portString;
//            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
            System.out.println(new Date() + msg);
        }
        catch (NumberFormatException ex)
        {
            String msg = "Could not parse port number defined in configuration file.";
//            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
            System.out.println(new Date() + msg);
        }

        while (listening)
        {
            try
            {
                new SocketThread(serverSocket.accept()).start();
            }
            catch (IOException ex)
            {
                String msg = "unable to start server socket; either accept or start failed.";
//            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
                System.out.println(new Date() + msg);
            }
        }
        try
        {
            serverSocket.close();
        }
        catch (IOException ex)
        {
            String msg = "failed to close the server socket.";
//            Logger.getLogger(MetroService.class.getName()).log(Level.SEVERE, msg, ex);
            System.out.println(new Date() + msg);
        }
    }

    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception
    {
        System.out.println(new Date() + " initializing service...");
        ARGS = dc.getArguments();
    }

    @Override
    public void start() throws Exception
    {
        System.out.println(new Date() + " starting service...");
        main(ARGS);
    }

    @Override
    public void stop() throws Exception
    {
        System.out.println(new Date() + " stopping service...");
        // Stubbed for future cleanup.
    }
    
    /**
     * Windows specific; included to accommodate prunsrv --StopMethod requirement
     * of a method that takes an array of String args.
     * @param args 
     */
    static void stop(String[] args)
    {
        // Used by Windows to stop the service.
        System.out.println(new Date() + " stopping service...");
        listening = false;
        System.exit(0);
    }
    
    /** 
     * Required when using prunsrv (AKA procrun) to start Metro service.
     * @param args parameters to send to main method.
     */
    static void start(String[] args)
    {
        // Used by Windows to stop the service.
        System.out.println(new Date() + " Starting service...");
        listening = true;
        main(args);
    }

    @Override
    public void destroy()
    {
        // do cleanup.
        System.out.println(new Date() + " done.");
    }
}
