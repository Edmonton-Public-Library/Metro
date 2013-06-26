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
package mecard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author metro
 */
public class MetroClient
{
    private static int MAX_CLIENTS = 9;

    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    public static int HIGHEST;
    int myNumber;

    MetroClient()
    {
        HIGHEST += 1;
        myNumber = HIGHEST;
    }

    void run()
    {
        String server = "ilsdev1";
        int port = 2004;
        try
        {
            //1. creating a socket to connect to the server
            requestSocket = new Socket(server, port);
            System.out.println("Connected to '"+server+"' in port: "+port);
            //2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            //3: Communicating with the server
            String custCreateReq =
            "[\"QC0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\""
            + "Edmonton\",\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\",\"7804964058\",\"19750822\",\""
            + "20140602\",\"Balzac\",\"Billy\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"Balzac\",\"Billy\"]";
            
            String custUpdateReq =
            "[\"QD0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\""
            + "Edmonton\",\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\",\"7804964058\",\"19750822\",\""
            + "20140602\",\"Balzac\",\"Billy\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"Balzac\",\"Billy\"]";
            do
            {
                try
                {
                    message = (String) in.readObject();
                    System.out.println("server said>" + message);
                    sendMessage("[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]"); // getstatus
                    message = (String) in.readObject();
                    System.out.println("server said>" + message);
                    
                    sendMessage("[\"QB0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"21221015133926\",\"6666\"]"); // getstatus
                    message = (String) in.readObject();
                    System.out.println("server said>" + message);
                    
                    sendMessage(custCreateReq); // getstatus
                    message = (String) in.readObject();
                    System.out.println("server said>" + message);
                    
                    sendMessage(custUpdateReq); // getstatus
                    message = (String) in.readObject();
                    System.out.println("server said>" + message);
                    
                    message = "[\"XX0\"]";
                    sendMessage(message);
                }
                catch (ClassNotFoundException classNot)
                {
                    System.err.println("data received in unknown format");
                }
            }
//            while (!message.equals("XX0|"));
            while (!message.equals("[\"XX0\"]"));
        }
        catch (UnknownHostException unknownHost)
        {
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            //4: Closing connection
            try
            {
                in.close();
                out.close();
                requestSocket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }

    void sendMessage(String msg)
    {
        try
        {
            out.writeObject(msg);
            out.flush();
            System.out.println("client>" + msg);
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public static void main(String args[])
    {

        for (int i = 0; i < MAX_CLIENTS; i++)
        {
            MetroClient client = new MetroClient();
            client.run();
        }

    }
}
