package mecard.horizon;

import mecard.horizon.BImportCustomerLoader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrew
 */
public class BImportCustomerLoaderTest
{
    
    public BImportCustomerLoaderTest()
    {
    }

    /**
     * Test of getFileList method, of class BImportCustomerLoader.
     */
    @Test
    public void testGetFileList()
    {
        System.out.println("==getFileList==");
        String loadDir = "/home/metro/Dropbox/development/MeCard/logs/Customers/";
        String fileSuffix = "-data.txt";
        BImportCustomerLoader instance = new BImportCustomerLoader();
        assertNotNull(instance);
        List<String> expResult = new ArrayList<>();
        expResult.add(loadDir + "metro-21221000016466-data.txt");
        expResult.add(loadDir + "metro-21221000121514-data.txt");
        expResult.add(loadDir + "metro-21221000787082-data.txt");
        List<String> result = instance.getFileList(loadDir, fileSuffix);
        int index = 0;
        for (String s: result)
        {
            assertTrue(expResult.get(index).compareTo(result.get(index)) == 0);
            System.out.print(  "EXP:"+expResult.get(index));
            System.out.println("__RES:"+result.get(index));
            index++;
        }
        
        System.out.println("==next test==");
        fileSuffix = "-header.txt";
        instance = new BImportCustomerLoader();
        expResult = new ArrayList<>();
        expResult.add(loadDir + "metro-21221000016466-header.txt");
        expResult.add(loadDir + "metro-21221000121514-header.txt");
        expResult.add(loadDir + "metro-21221000787082-header.txt");
        result = instance.getFileList(loadDir, fileSuffix);
        index = 0;
        for (String s: result)
        {
            assertTrue(expResult.get(index).compareTo(result.get(index)) == 0);
            System.out.print(  "EXP:"+expResult.get(index));
            System.out.println("__RES:"+result.get(index));
            index++;
        }
        
        System.out.println("==next test==");
        fileSuffix = "-bimp.bat";
        instance = new BImportCustomerLoader();
        expResult = new ArrayList<>();
        expResult.add(loadDir + "metro-21221000016466-bimp.bat");
        expResult.add(loadDir + "metro-21221000121514-bimp.bat");
        expResult.add(loadDir + "metro-21221001659926-bimp.bat");
        result = instance.getFileList(loadDir, fileSuffix);
        index = 0;
        for (String s: result)
        {
            assertTrue(expResult.get(index).compareTo(result.get(index)) == 0);
            System.out.print(  "EXP:"+expResult.get(index));
            System.out.println("__RES:"+result.get(index));
            index++;
        }
        
        System.out.println("==next test==");
        fileSuffix = "-none.txt";
        instance = new BImportCustomerLoader();
        result = instance.getFileList(loadDir, fileSuffix);
        assertTrue(result.isEmpty());
    }
}