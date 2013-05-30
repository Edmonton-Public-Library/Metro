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
public class BImportDBFilesTest
{
    
    public BImportDBFilesTest()
    {
    }

    @Test
    public void testSomeMethod()
    {
        String hName = "header.txt";
        String dName = "data.txt";
        new BImportDBFiles.Builder(hName, dName)
                .barcode("21221012345678")
                .name("Billy, Balzac")
                .expire("12-25-2014")
                .pin("1234")
                .pType("h-noTC")
                .pNumber("7804964058")
                .address1("12345 123 St.")
                .city("Edmonton")
                .postalCode("H0H 0H0")
                .emailName("ilsteam")
                .email("ilsteam@epl.ca")
                .build();
        File f = new File(hName);
        assertTrue(f.exists());
        f = new File(dName);
        assertTrue(f.exists());
    }
}