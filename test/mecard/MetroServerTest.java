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
public class MetroServerTest
{
    
    private static MetroServer server;
    public MetroServerTest()
    {
        server = new MetroServer();
        server.run();
    }

    /**
     * Test of run method, of class MetroServer.
     */
    @Test
    public void testRun()
    {
        System.out.println("run");
        
        MetroClient client = new MetroClient();
        client.run();
    }

//    /**
//     * Test of sendMessage method, of class MetroServer.
//     */
//    @Test
//    public void testSendMessage()
//    {
//        System.out.println("sendMessage");
//        String msg = "";
//        MetroServer instance = new MetroServer();
//        instance.sendMessage(msg);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of main method, of class MetroServer.
//     */
//    @Test
//    public void testMain()
//    {
//        System.out.println("main");
//        String[] args = null;
//        MetroServer.main(args);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}