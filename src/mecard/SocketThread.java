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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * This example introduces you to Java socket programming. The server listens
 * for a connection. When a connection is established by a client. The client
 * can send data. In the current example the client sends the message "Hi my
 * server". To terminate the connection, the client sends the message "bye".
 * Then the server sends the message "bye" too. Finally the connection is ended
 * and the server waits for an other connection. The two programs should be
 * runned in the same machine. however if you want to run them in two different
 * machines, you may simply change the adress "localhost" by the IP adress of
 * the machine where you will run the server.
 * <code>http://zerioh.tripod.com/ressources/sockets.html</code>
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SocketThread extends Thread
{

    private Socket connection = null;
    private PrintWriter out;
    private BufferedReader in;
    private String message;
    private final Protocol protocol;

    public SocketThread(Socket socket)
    {
        super("MetroSocket");
        this.connection = socket;
        protocol = new Protocol();
    }

    @Override
    public void run()
    {
        try
        {
            //2. Wait for connection
            System.out.println(new Date() + " Waiting for connection");
            System.out.println(new Date() + " Connection received from " + connection.getInetAddress().getHostName());
            //3. get Input and Output streams
            out = new PrintWriter(connection.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out.println(Protocol.ACKNOWLEDGE);
//            message = new String();
            //4. The two parts communicate via the input and output streams
            while ((message = in.readLine()) != null)
            {

//                message = (String) in.readLine();

                // Catch any protocol related strings, the rest are commands.
                if (message.equals(Protocol.TERMINATE))
                {
                    out.println(Protocol.TERMINATE);
                    break;
                }
                else
                {
                    String response = protocol.processInput(message);
                    out.println(response);
                }
            }
        }
        catch (IOException ex)
        {
            System.err.println(new Date() + " " + ex.getMessage());
        }
        finally
        {
            //4: Closing connection
            try
            {
                in.close();
                out.close();
                connection.close();
                System.out.println(new Date() + " transaction completed.");
            }
            catch (IOException ex)
            {
                System.err.println(new Date() + " ioException " + ex.getMessage());
            }
            catch (NullPointerException ex) // occurs if you try and close a socket that is closed.
            {
                System.err.println(new Date() + " can't close the server's "
                        + "listening socket, was it ever open? " + ex.getMessage());
            }
        }
    }
}
