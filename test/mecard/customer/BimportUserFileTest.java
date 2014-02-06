
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


public class BimportUserFileTest
{
    private final String testFileName;
    public BimportUserFileTest()
    {
        this.testFileName = "userfiletest.txt";
    }

    /**
     * Test of createFile method, of class BimportUserFile.
     */
    @Test
    public void testCreateFile()
    {
        System.out.println("==create BimportUserFile==");
        List<String> data = new ArrayList<>();
        data.add("M- borrower: 05-17-1980; 07-15-2014; Balzac, Billy Ann S; 9704; 21221012345678\n");
        data.add("borrower_phone: 780-993-9987; h-noTC\n");
        data.add("borrower_address: 1002-9999 115 St Nw;  ; ed; bb@hotmail.com; bb; T5K 0E4; 1; 1\n");
        data.add("borrower_barcode: 21221012345678\n");
        data.add("borrower_bstat: m\n");
        data.add("borrower_bstat: metro\n");
        // If this failes delete the file.
        if (new File(this.testFileName).exists() == false)
        {
            new File(this.testFileName).delete();
        }
        UserFile userFile = new BimportUserFile(this.testFileName);
        userFile.addUserData(data);
        File f = new File(this.testFileName);
        assertTrue(f.exists());
        UserFile repeatUserFile = new UserFile(this.testFileName);
        repeatUserFile.addUserData(data);
        File backup = new File(this.testFileName + ".orig");
        assertTrue(backup.exists());
    }
    
}
