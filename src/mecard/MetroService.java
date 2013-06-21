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

package mecard;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;

import org.apache.commons.daemon.*;

public class MetroService implements Daemon
{
    private static ServerSocket serverSocket = null;
    private static boolean listening = true;
    private static String defaultPort = "2004";

    public static void main(String[] args)
    {
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
    }

    @Override
    public void start() throws Exception
    {
        System.out.println(new Date() + " starting service...");
        main(null);
    }

    @Override
    public void stop() throws Exception
    {
        System.out.println(new Date() + " stopping service...");
    }

    @Override
    public void destroy()
    {
        // do cleanup.
        System.out.println(new Date() + " done.");
    }
}
