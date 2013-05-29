/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

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
        BImportBat b = new BImportBat.Builder("testfile.bat").server("server")
                .password("password").user("user").database("database")
                .header("headerFileName.txt").data("dataFileName").alias("second_id")
                .format("m41").bType("awb").location("alap").setIndexed(true).build();
        f = new File("testfile.bat");
        assertTrue(f.exists());
        
    }
}