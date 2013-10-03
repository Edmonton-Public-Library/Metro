package mecard;

import java.io.File;
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
     * Test of run method, of class BImportCustomerLoader.
     */
//    @Test
//    public void testRun()
//    {
//        System.out.println("==run==");
//        BImportCustomerLoader instance = new BImportCustomerLoader();
//        instance.run();
//    }

//    /**
//     * Test of getFileList method, of class BImportCustomerLoader.
//     */
//    @Test
//    public void testGetFileList()
//    {
//        System.out.println("==getFileList==");
//        String loadDir = "/Users/andrew/Dropbox/development/MeCard/logs/Customers/";
//        String fileSuffix = "-data.txt";
//        BImportCustomerLoader instance = new BImportCustomerLoader();
//        List<String> expResult = new ArrayList<>();
//        expResult.add(loadDir + "metro-21221000016466-data.txt");
//        expResult.add(loadDir + "metro-21221000121514-data.txt");
//        expResult.add(loadDir + "metro-21221000787082-data.txt");
//        List<String> result = instance.getFileList(loadDir, fileSuffix);
//        int index = 0;
//        for (String s: result)
//        {
//            assertTrue(expResult.get(index).compareTo(result.get(index)) == 0);
//            System.out.print(  "EXP:"+expResult.get(index));
//            System.out.println("__RES:"+result.get(index));
//            index++;
//        }
//        
//        System.out.println("==next test==");
//        fileSuffix = "-header.txt";
//        instance = new BImportCustomerLoader();
//        expResult = new ArrayList<>();
//        expResult.add(loadDir + "metro-21221000016466-header.txt");
//        expResult.add(loadDir + "metro-21221000121514-header.txt");
//        expResult.add(loadDir + "metro-21221000787082-header.txt");
//        result = instance.getFileList(loadDir, fileSuffix);
//        index = 0;
//        for (String s: result)
//        {
//            assertTrue(expResult.get(index).compareTo(result.get(index)) == 0);
//            System.out.print(  "EXP:"+expResult.get(index));
//            System.out.println("__RES:"+result.get(index));
//            index++;
//        }
//        
//        System.out.println("==next test==");
//        fileSuffix = "-bimp.bat";
//        instance = new BImportCustomerLoader();
//        expResult = new ArrayList<>();
//        expResult.add(loadDir + "metro-21221000016466-bimp.bat");
//        expResult.add(loadDir + "metro-21221000121514-bimp.bat");
//        expResult.add(loadDir + "metro-21221001659926-bimp.bat");
//        result = instance.getFileList(loadDir, fileSuffix);
//        index = 0;
//        for (String s: result)
//        {
//            assertTrue(expResult.get(index).compareTo(result.get(index)) == 0);
//            System.out.print(  "EXP:"+expResult.get(index));
//            System.out.println("__RES:"+result.get(index));
//            index++;
//        }
//        
//        System.out.println("==next test==");
//        fileSuffix = "-none.txt";
//        instance = new BImportCustomerLoader();
//        result = instance.getFileList(loadDir, fileSuffix);
//        assertTrue(result.isEmpty());
//    }
//
//    /**
//     * Test of touchHeader method, of class BImportCustomerLoader.
//     */
//    @Test
//    public void testTouchHeader()
//    {
//        System.out.println("==touchHeader=");
//        String fileName = "/Users/andrew/Dropbox/development/MeCard/logs/Customers/metro-21221000121514-header.txt";
//        BImportCustomerLoader instance = new BImportCustomerLoader();
//        instance.touchHeader(fileName);
//        File headerFile = new File("/Users/andrew/Dropbox/development/MeCard/logs/Customers/metro--header.txt");
//        assertTrue(headerFile.exists());
//    }
//
//    /**
//     * Test of clean method, of class BImportCustomerLoader.
//     */
//    @Test
//    public void testClean()
//    {
//        System.out.println("==clean==");
//        String loadDir = "/Users/andrew/Dropbox/development/MeCard/logs/Customers/";
//        String fileSuffix = "-data.txt";
//        BImportCustomerLoader instance = new BImportCustomerLoader();
//        List<String> result = instance.getFileList(loadDir, fileSuffix);
//        assertTrue(result.size() > 0);
//        instance.clean(result);
//        result = instance.getFileList(loadDir, fileSuffix);
//        assertTrue(result.isEmpty());
//        
//        fileSuffix = "-bimp.bat";
//        result = instance.getFileList(loadDir, fileSuffix);
//        assertTrue(result.size() > 0);
//        instance.clean(result);
//        result = instance.getFileList(loadDir, fileSuffix);
//        assertTrue(result.isEmpty());
//        
//        fileSuffix = "-none.txt";
//        result = instance.getFileList(loadDir, fileSuffix);
//        assertFalse(result.size() > 0);
//        instance.clean(result);
//        result = instance.getFileList(loadDir, fileSuffix);
//        assertTrue(result.isEmpty());
//    }

    /**
     * Test of getLockFile method, of class BImportCustomerLoader.
     */
    @Test
    public void testGetLockFile()
    {
        System.out.println("==getLockFile==");
        
        File result = BImportCustomerLoader.getLockFile();
        assertTrue(result.exists());
        File result2 = BImportCustomerLoader.getLockFile();
        assertTrue(result2 == null);
        result.delete();
        result2 = BImportCustomerLoader.getLockFile();
        assertTrue(result.exists());
        result2.delete();
        result = BImportCustomerLoader.getLockFile();
        assertTrue(result.exists());
    }
}