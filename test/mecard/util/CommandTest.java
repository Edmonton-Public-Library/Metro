/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import api.Command;
import api.ProcessWatcherHandler;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class CommandTest {
   private List<String> myArgs;
    private List<String> myArgsBad;
    public CommandTest()
    {
        myArgs = new ArrayList<String>();
        myArgs.add("/");
        myArgs.add("/home/metro");
        
        myArgsBad = new ArrayList<String>();
        myArgsBad.add("/home/metro/foo");
    }

    /**
     * Test of execute method, of class Command.
     */
    @Test
    public void testExecute00()
    {
        System.out.println("===command create===");
        
        List<String> cmdArgs = new ArrayList<String>();
        cmdArgs.add("ls");
        Command cmd = new Command.Builder().args(cmdArgs).build();
        ProcessWatcherHandler status = cmd.execute();
        String stdout = status.getStdout();
        assertTrue(stdout.length() > 0);
        System.out.println(">>"+stdout);
    }
    
    /**
     * Test of execute method, of class Command.
     */
    @Test
    public void testExecute01()
    {
        System.out.println("======= testExecute01:: test -la ========");
        // Actual output
//        total 36
//        drwxrwxr-x 8 metro metro 4096 Apr 17 16:10 .
//        drwxrwxr-x 7 metro metro 4096 Apr 16 17:51 ..
//        drwxrwxr-x 6 metro metro 4096 Apr 18 08:12 build
//        -rw-rw-r-- 1 metro metro 3422 Apr 14 08:26 build.xml
//        drwxrwxr-x 2 metro metro 4096 Apr 17 16:10 dist
//        drwxrwxr-x 3 metro metro 4096 Apr 14 08:07 nbproject
//        drwxrwxr-x 4 metro metro 4096 Mar 22 09:25 src
//        drwxrwxr-x 3 metro metro 4096 Apr 14 08:07 test
//        drwxrwxr-x 4 metro metro 4096 Mar 22 13:49 web
        List<String> cmdArgs = new ArrayList<String>();
        cmdArgs.add("ls");
        cmdArgs.add("-la");
        Command cmd = new Command.Builder().args(cmdArgs).build();
        ProcessWatcherHandler status = cmd.execute();
        String stdout = status.getStdout();
        assertTrue(stdout.length() > 0);
        System.out.println(">>"+stdout);
    }
    

//
    @Test
    public void testExecute03()
    {
        System.out.println("======= testExecute03:: test invalid directory list ========");
        List<String> cmdArgs = new ArrayList<String>();
        cmdArgs.add("ls");
        cmdArgs.add("-la");
        cmdArgs.add("foobar");
        Command cmd = new Command.Builder().args(cmdArgs).build();
        ProcessWatcherHandler status = cmd.execute();
        String out = status.getStderr();
        assertTrue(out.length() > 0);
        System.out.println(">>"+out);
    }
        
    @Test
    public void testExecute04()
    {
        System.out.println("test pipe one arg");
        
        System.out.println("======= testExecute04:: pipe to wc ========");
        List<String> cmdArgs = new ArrayList<String>();
        cmdArgs.add("wc");
        cmdArgs.add("-l");
        Command cmd = new Command.Builder().echo("This and that").args(cmdArgs).build();
        ProcessWatcherHandler status = cmd.execute();
        String out = status.getStdout();
        assertTrue(out.length() > 0);
        System.out.println(">>"+out);
    }
        
    /**
     * Test of hasExited method, of class Command.
     */
    @Test
    public void testHasExited()
    {
        System.out.println("===== testHasExited: hasExited ========");
        
        
        List<String> data = new ArrayList<String>();
        data.add("find");
        data.add("/");
        data.add("-name");
        data.add("\"*.pdf\"");
//        data.add("-print");
        Command instance = new Command.Builder().args(data).build();
        ProcessWatcherHandler status = instance.execute();
        String out = status.getStdout();
        assertTrue(out.length() > 0);
        System.out.println(">>"+out);
        
        out = status.getStderr();
        assertTrue(out.length() > 0);
        System.out.println(">>"+out);
    }
}