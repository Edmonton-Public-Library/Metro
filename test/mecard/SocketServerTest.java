/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class SocketServerTest
{
    
    private static SocketThread server;
    public SocketServerTest()
    {
        fail("Not implemented yet.");
    }

    /**
     * Test of run method, of class SocketThread.
     */
    @Test
    public void testRun()
    {
        System.out.println("run");
        
        MetroClient client = new MetroClient();
        client.run();
    }

//    /**
//     * Test of sendMessage method, of class SocketThread.
//     */
//    @Test
//    public void testSendMessage()
//    {
//        System.out.println("sendMessage");
//        String msg = "";
//        SocketThread instance = new SocketThread();
//        instance.sendMessage(msg);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of main method, of class SocketThread.
//     */
//    @Test
//    public void testMain()
//    {
//        System.out.println("main");
//        String[] args = null;
//        SocketThread.main(args);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}