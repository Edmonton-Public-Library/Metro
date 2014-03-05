
package mecard.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import mecard.customer.UserFile;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrew
 */
public class BImportResultParserTest
{
    private String resultString;
    private String bigTestString;
    private final String altBigStringTest;
    
    public BImportResultParserTest()
    {
        this.resultString = "//record line   action key(s)\n" +
"------ ------ ------ ---------------------------------------------------------\n" +
"     1      1 modify 23877000204705\n" +
"               failed: DbxInsertRow failed: Database Error|Integrity trigger failed:\n" +
"record line   action key(s)\n" +
"------ ------ ------ ---------------------------------------------------------\n" +
"     1      1 modify 21221005573552 <ok>\n" +
"\n" +
"statistics: \n" +
"  record           1\n" +
"  modify           1";
        // TODO test against multiload.
        this.bigTestString = "record line   action key(s)\n" +
"------ ------ ------ ---------------------------------------------------------\n" +
"     1      1 modify 21000004745878\n" +
"               failed: DbxInsertRow failed: Database Error|Integrity trigger failed: \n" +
"bad city_st in borrower_address\n" +
"The transaction ended in the trigger. The batch has been aborted.\n" +
"-- Database Error|Integrity trigger failed: \n" +
"bad city_st in borrower_address\n" +
"The transaction ended in the trigger. The batch has been a\n" +
"     2      7 modify 21000006126960\n" +
"               failed: DbxInsertRow failed: Database Error|Integrity trigger failed: \n" +
"bad city_st in borrower_address\n" +
"The transaction ended in the trigger. The batch has been aborted.\n" +
"-- Database Error|Integrity trigger failed: \n" +
"bad city_st in borrower_address\n" +
"The transaction ended in the trigger. The batch has been a\n" +
"     3     13 modify 21221002076310 <ok>\n" +
"     4     19 modify 21221002129820 <ok>\n" +
"     5     25 modify 21221009010767 <ok>";
        
        
        
        
        this.altBigStringTest = "record line   action key(s)\n" +
"------ ------ ------ ---------------------------------------------------------\n" +
" 	1  	1 modify 21221015113142 <ok>\n" +
" 	2  	6 modify 21221015582577 <ok>\n" +
" 	3 	11 modify 21221018412939 <ok>\n" +
" 	4 	16 modify 21221020213143\n" +
"           	failed: DbxInsertRow failed: Database Error|Attempted to insert a duplicate row\n" +
"Cannot insert duplicate key row in object 'dbo.borrower_barcode' with unique index 'bbarcode_index'. The duplicate key value is (21221020213143).\n" +
"The statement has been terminated.\n" +
"-- Database Error|Attempted to insert a dupli\n" +
" 	5 	21 modify 21221021764565\n" +
"           	failed: DbxInsertRow failed: Database Error|Attempted to insert a duplicate row\n" +
"Cannot insert duplicate key row in object 'dbo.borrower_barcode' with unique index 'bbarcode_index'. The duplicate key value is (21221021764565).\n" +
"The statement has been terminated.\n" +
"-- Database Error|Attempted to insert a dupli\n" +
"\n" +
"record line   action key(s)\n" +
"------ ------ ------ ---------------------------------------------------------\n" +
" 	6 	26 modify 21221021874166\n" +
"           	failed: DbxInsertRow failed: Database Error|Attempted to insert a duplicate row\n" +
"Cannot insert duplicate key row in object 'dbo.borrower_barcode' with unique index 'bbarcode_index'. The duplicate key value is (21221021874166).\n" +
"The statement has been terminated.\n" +
"-- Database Error|Attempted to insert a dupli\n" +
" 	7 	31 modify 22222001306319 <ok>\n" +
"\n" +
"statistics:\n" +
"  record       	7\n" +
"  modify       	7\n" +
"  error        	3\n" +
"0\n" +
"0";
    }

    @Test
    public void testSomeMethod()
    {
        BImportResultParser rp = new BImportResultParser(resultString, "./");
    }

    /**
     * Test of getUserId method, of class BImportResultParser.
     */
    @Test
    public void testGetUserId()
    {
        System.out.println("==getUserId==");
        String expResult = "23877000204705";
        BImportResultParser instance = new BImportResultParser(resultString, "./");
//        resultString = "     1      1 modify 23877000204705";
        String result = instance.getUserId(resultString);
        assertEquals(expResult, result);
    }

    /**
     * Test of touchFailedCustomerKeys method, of class BImportResultParser.
     */
    @Test
    public void testTouchFailedCustomerKeys()
    {
        System.out.println("===touchFailedCustomerKeys===");
        BImportResultParser instance = new BImportResultParser(resultString, "./");
        List<String> failedCustomerIds = instance.getFailedCustomerKeys();
        for (String userId: failedCustomerIds)
        {
            UserFile touchKey = new UserFile("./" + userId + ".fail");
            touchKey.addUserData(new ArrayList<String>());
        }
        File tKeyFile = new File("./23877000204705.fail");
        assertTrue(tKeyFile.exists());
        
        
        
        instance = new BImportResultParser(bigTestString, "./");
        failedCustomerIds = instance.getFailedCustomerKeys();
        for (String userId: failedCustomerIds)
        {
            UserFile touchKey = new UserFile("./" + userId + ".fail");
            touchKey.addUserData(new ArrayList<String>());
            System.out.println("USER_FAIL_FILE:"+userId);
            assertTrue(new File("./" + userId + ".fail").exists());
        }
        
        
        instance = new BImportResultParser(altBigStringTest, "./");
        failedCustomerIds = instance.getFailedCustomerKeys();
        for (String userId: failedCustomerIds)
        {
            UserFile touchKey = new UserFile("./" + userId + ".fail");
            touchKey.addUserData(new ArrayList<String>());
            System.out.println("USER_FAIL_FILE:"+userId);
            assertTrue(new File("./" + userId + ".fail").exists());
        }
    }

    /**
     * Test of getSuccessfulCustomers method, of class BImportResultParser.
     */
    @Test
    public void testGetSuccessfulCustomers()
    {
        System.out.println("===getSuccessfulCustomers===");
        String expResult = "23877000204705";
        BImportResultParser instance = new BImportResultParser(resultString, "./");
//        String resultString = "     1      1 modify 23877000204705";
        String result = instance.getUserId(resultString);
        assertEquals(expResult, result);
    }

    /**
     * Test of getFailedCustomers method, of class BImportResultParser.
     */
    @Test
    public void testGetFailedCustomers()
    {
        System.out.println("getFailedCustomers");
        BImportResultParser instance = null;
        int expResult = 0;
        int result = instance.getFailedCustomers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFailedCustomerKeys method, of class BImportResultParser.
     */
    @Test
    public void testGetFailedCustomerKeys()
    {
        System.out.println("getFailedCustomerKeys");
        BImportResultParser instance = null;
        List<String> expResult = null;
        List<String> result = instance.getFailedCustomerKeys();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}