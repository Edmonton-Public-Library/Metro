/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.customer;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class BImportBatTest
{
    
    public BImportBatTest()
    {
    }

    @Test
    public void testSomeMethod()
    {
        File f = new File("testfile.bat");
        if (f.exists())
        {
            f.delete();
        }
        BImportBat b = new BImportBat.Builder("testfile.bat")
                .setBimportPath("/home/metro/bimport/bimport").server("server")
                .password("password").user("user").database("database")
                .header("headerFileName.txt").data("dataFileName").alias("second_id")
                .format("m41").bType("awb").location("alap").setIndexed(true).build();
        f = new File("testfile.bat");
        assertTrue(f.exists());
        
    }

    /**
     * Test of getCommandLine method, of class BImportBat.
     */
    @Test
    public void testGetCommandLine() {
        System.out.println("==getCommandLine==");
        BImportBat b = new BImportBat.Builder("testfile.bat")
                .setBimportPath("/home/metro/bimport/bimport").server("server")
                .password("password").user("user").database("database")
                .header("headerFileName.txt").data("dataFileName").alias("second_id")
                .format("m41").bType("awb").location("alap").setIndexed(true).build();
        System.out.println("cmd:'"+b.getCommandLine()+"'");
    }

    /**
     * Test of getBatchFileName method, of class BImportBat.
     */
    @Test
    public void testGetBatchFileName() 
    {
        System.out.println("===getBatchFileName===");
        BImportBat b = new BImportBat.Builder().server("server")
                .password("password").user("user").database("database")
                .header("headerFileName.txt").data("dataFileName").alias("second_id")
                .format("m41").bType("awb").location("alap").setIndexed(true).build();
        
        assertTrue(b.getBatchFileName().compareTo("obsolete.bat") == 0);
    }
}