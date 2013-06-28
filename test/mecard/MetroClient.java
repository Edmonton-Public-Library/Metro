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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author metro
 */
public class MetroClient
{
    private static int MAX_CLIENTS = 1;

    Socket requestSocket;
    PrintWriter out;
    BufferedReader in;
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
//        String server = "localhost";
        int port = 2004;
        try
        {
            //1. creating a socket to connect to the server
            requestSocket = new Socket(server, port);
            System.out.println("Connected to '"+server+"' in port: "+port);
            //2. get Input and Output streams
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
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
                message = (String) in.readLine();
                System.out.println("server said>" + message);
                System.out.println("requesting status");
                out.println("[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]"); // getstatus
//
                message = (String) in.readLine();
                System.out.println("server said>" + message);
//
                System.out.println("requesting customer");
                out.println("[\"QB0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"21221015133926\",\"6666\"]"); // getstatus
                message = (String) in.readLine();
                System.out.println("server said>" + message);

                System.out.println("requesting "+custCreateReq);
                out.println(custCreateReq);
                message = (String) in.readLine();
                System.out.println("server said>" + message);

                System.out.println("requesting "+custUpdateReq);
                out.println(custUpdateReq);
                message = (String) in.readLine();
                System.out.println("server said>" + message);
                
                message = "XX0";
                out.println(message);
                if (message.equals("XX0"))
                    break;
            }
//            while (!message.equals("XX0|"));
            while ((message = in.readLine()) != null);
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

    public static void main(String args[])
    {

        for (int i = 0; i < MAX_CLIENTS; i++)
        {
            MetroClient client = new MetroClient();
            client.run();
        }

    }
}
