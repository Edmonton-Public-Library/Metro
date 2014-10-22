
package mecard.customer;

import mecard.customer.horizon.BimportUserFile;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        List<String> registerData = new ArrayList<>();
        registerData.add("M- borrower: 05-17-1980; 07-15-2014; Balzac, Billy Ann S; 9704; 21221012345678\n");
        registerData.add("borrower_phone: 780-993-9987; h-noTC\n");
        registerData.add("borrower_address: 1002-9999 115 St Nw;  ; ed; bb@hotmail.com; bb; T5K 0E4; 1; 1\n");
        registerData.add("borrower_barcode: 21221012345678\n");
        registerData.add("borrower_bstat: m\n");
        registerData.add("borrower_bstat: metro\n");
        
        List<String> updateData = new ArrayList<>();
        updateData.add("M- borrower: 05-17-1980; 07-15-2014; Balzac, Billy Ann S; ; 21221012345678\n");
        updateData.add("borrower_phone: 780-993-9987; h-noTC\n");
        updateData.add("borrower_address: 1002-9999 115 St Nw;  ; ed; bb@hotmail.com; bb; T5K 0E4; 1; 1\n");
        updateData.add("borrower_barcode: 21221012345678\n");
        updateData.add("borrower_bstat: m\n");
        updateData.add("borrower_bstat: metro\n");
        // If this failes delete the file.
        if (new File(this.testFileName).exists())
        {
            new File(this.testFileName).delete();
        }
        // Clean up any original files too.
        if (new File(this.testFileName + ".orig").exists())
        {
            new File(this.testFileName + ".orig").delete();
        }
        UserFile userFile = new BimportUserFile(this.testFileName);
        userFile.addUserData(registerData);
        File f = new File(this.testFileName);
        assertTrue(f.exists());
        UserFile repeatUserFile = new BimportUserFile(this.testFileName);
        repeatUserFile.addUserData(updateData);
        // The difference is that the original has a pin
        File backup = new File(this.testFileName + ".orig");
        assertTrue(backup.exists());
        assertFalse(this.showDifferences(testFileName + ".orig", testFileName));
        List<String> repeatUpdateData = new ArrayList<>();
        repeatUpdateData.add("M- borrower: 05-17-1980; 07-15-2014; Balzac, Billy Ann S; ; 21221011111111\n");
        repeatUpdateData.add("borrower_phone: 780-993-9987; h-noTC\n");
        repeatUpdateData.add("borrower_address: 1002-9999 115 St Nw;  ; ed; bb@hotmail.com; bb; T5K 0E4; 1; 1\n");
        repeatUpdateData.add("borrower_barcode: 21221012345678\n");
        repeatUpdateData.add("borrower_bstat: m\n");
        repeatUpdateData.add("borrower_bstat: metro\n");
        UserFile repeatUpdateUserFile = new BimportUserFile(this.testFileName);
        repeatUpdateUserFile.addUserData(repeatUpdateData);
        // The difference is that the original has a pin
        backup = new File(this.testFileName + ".orig");
        assertTrue(backup.exists());
        f = new File(this.testFileName);
        assertTrue(f.exists());
        assertFalse(this.showDifferences(testFileName + ".orig", testFileName));
        // and then clean up because you've seen the differences.
        if (new File(this.testFileName).exists())
        {
            new File(this.testFileName).delete();
        }
        // Clean up any original files too.
        if (new File(this.testFileName + ".orig").exists())
        {
            new File(this.testFileName + ".orig").delete();
        }
    }
    
    private boolean showDifferences(String fileOne, String fileTwo) 
    {
        System.out.println(fileOne + " compare to " + fileTwo);
        boolean result = true;
        FileInputStream fstream1 = null;
        FileInputStream fstream2 = null;
        try
        {
            fstream1 = new FileInputStream(fileOne);
            fstream2 = new FileInputStream(fileTwo);
            DataInputStream in1= new DataInputStream(fstream1);
            DataInputStream in2= new DataInputStream(fstream2);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
            String strLine1, strLine2;
            while((strLine1 = br1.readLine()) != null && (strLine2 = br2.readLine()) != null)
            {
                if(strLine1.compareTo(strLine2) != 0)
                {
                    System.out.println("< "+strLine1);
                    System.out.println("> "+strLine2);
                    result = false;
                }
            }   
            
        } 
        catch (FileNotFoundException ex)
        {
            result = false;
        } 
        catch (IOException ex)
        {
            result = false;
        } 
        finally
        {
            try
            {
                fstream1.close();
                fstream2.close();
            } 
            catch (IOException | NullPointerException ex)
            {
                result = false;
            }
        }
        return result;
    }
}
