/*
 * Notes on running this test:
 * The project has been refactored so each library will have its own configuration 
 * directory. When this test runs it will not find the config files in the default 
 * project directory. All you need do is copy the environment.properties, 
 * bimport.properties and city_st.properties files to the project's root directory,
 * test, then remove them after testing so GIT doesn't find them.
 */
package mecard.util;

import mecard.customer.BImportFile;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class BImportFormatterTest
{
    
    public BImportFormatterTest()
    {
    }

    @Test
    public void testSomeMethod()
    {
        String hName = "header.txt";
        String dName = "data.txt";
        new BImportFile.Builder(hName, dName)
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