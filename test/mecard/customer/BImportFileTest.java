
package mecard.customer;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class BImportFileTest
{
    
    public BImportFileTest()
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
                .expire("20140822")
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