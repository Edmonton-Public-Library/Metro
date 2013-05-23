
package mecard;

import java.util.Date;
//import java.util.Timer;
//import java.util.TimerTask;
import org.apache.commons.daemon.*;

///**
// * This class manages the setup, running and tear-down of MeCard's services.
// * @author metro
// */
//class EchoTask extends TimerTask
//{
//
//    @Override
//    public void run()
//    {
//        System.out.println(new Date() + " running ...");
//    }
//}

public class MetroService implements Daemon
{

//    private static Timer timer = null;
    private static MetroServer server = null;

    public static void main(String[] args)
    {
        if (server == null)
        {
            server = new MetroServer();
            server.run();
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
