package mecard;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.daemon.*;

public class MetroService implements Daemon
{
    private static ServerSocket serverSocket = null;
    private static boolean listening = true;

    public static void main(String[] args)
    {
        try
        {
            serverSocket = new ServerSocket(2004);
        }
        catch (IOException ex)
        {
            String msg = "Could not listen on port: 2004.";
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
        // Get and parse, test port number.
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
