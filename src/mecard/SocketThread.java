/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
 * @author metro
 */
public class SocketThread extends Thread
{

    private Socket connection = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
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
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage(Protocol.ACKNOWLEDGE);
            //4. The two parts communicate via the input and output streams
            do
            {
                try
                {
                    message = (String) in.readObject();
                }
                catch (ClassNotFoundException ex)
                {
                    System.err.println(new Date() + " received unknown object "
                            + ex.getMessage());
                }
                // Catch any protocol related strings, the rest are commands.
                if (message.equals(Protocol.TERMINATE))
                {
                    sendMessage(Protocol.TERMINATE);
                }
                else
                {
                    String response = protocol.processInput(message);
                    sendMessage(response);
                }


            }
            while (!message.equals(Protocol.TERMINATE));
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

    /**
     * Helper method to handle writing to stream and any exceptions that may
     * have been generated.
     * @param msg 
     */
    private void sendMessage(String msg)
    {
        try
        {
            out.writeObject(msg);
            out.flush();
        }
        catch (IOException ex)
        {
            System.err.println(new Date() + " IO error while sending message '"
                   + msg + "' " + ex.getMessage());
        }
    }
}
