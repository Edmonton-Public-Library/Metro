
package mecard.customer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SymphonyLoadUserShTest
{
    private final String testFileName;
    private final String srcFileName;
    
    public SymphonyLoadUserShTest()
    {
        this.testFileName = "load.sh";
        this.srcFileName  = "userfiletest.txt";
    }

    /**
     * Test of getCommandLine method, of class SymphonyLoadUserSh.
     */
    @Test
    public void testGetCommandLine()
    {
        System.out.println("==test Create SH ==");
        List<String> loadFlatUserUpdate = new ArrayList<String>();
//        loadFlatUserUpdate.add(upath + "loadflatuser");
        loadFlatUserUpdate.add("wc");
        loadFlatUserUpdate.add("-l"); // replace base information
        // alternate test:::
//        loadFlatUserUpdate.add("loadflatuser");
//        loadFlatUserUpdate.add("-aR"); // replace base information
//        loadFlatUserUpdate.add("-bR"); // Replace extended information
//        loadFlatUserUpdate.add("-l\"ADMIN|PCGUI-DISP\""); // User and station.
//        loadFlatUserUpdate.add("-mu"); // update
//        loadFlatUserUpdate.add("-n"); // turn off BRS checking.
//        loadFlatUserUpdate.add("-d"); // write syslog. check Unicorn/Logs/error for results.
        SymphonyLoadUserSh instance = new SymphonyLoadUserSh
                .Builder(testFileName)
                .setDebug(true)
                .setFlatUserFile(srcFileName)
                .setSheBang("#!/bin/bash")
                .setLogFile("symphonyloadusershtest.log")
                .setLoadFlatUserCommand(loadFlatUserUpdate)
                .build();
        assertTrue(instance != null);
        File f = new File(this.testFileName);
        assertTrue(f.exists());
    }
}