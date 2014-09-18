
package mecard.customer;
import mecard.customer.horizon.BImportTable;
import java.util.HashMap;
import mecard.config.BImportTableTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrew
 */
public class BImportTableTest
{
    private HashMap<String, String> testMap;
    
    public BImportTableTest()
    {
               // ==== Head =====
//        x- borrower: second_id; name; expiration_date; birth_date
//        borrower_phone: phone_type; phone_no
//        borrower_address: address1; address2; city_st; postal_code; email_name; email_address; send_preoverdue
//        borrower_barcode: bbarcode
//        borrower_bstat: bstat
        
        // ==== Data =====
//        M- borrower: 21221012345677; Balzac, Billy; 04-15-2014; 01-31-1998
//        borrower_phone: h-noTC; 7804964058
//        borrower_address: 12345 123 St.; ; edmonton; H0H 0H0; ilsteam; ilsteam@epl.ca; 1
//        borrower_barcode: 21221012345678
//        borrower_bstat: unknown
        this.testMap = new HashMap<>();
        this.testMap.put("second_id", "21221012345678");
        this.testMap.put("name", "Nisbet" + ", " + "Andrew");
        this.testMap.put("expiration_date", "20140101");
        this.testMap.put("birth_date", "19630822");        
    }

    /**
     * Test of getHeaderLine method, of class BImportTable.
     */
    @Test
    public void testGetHeaderLine()
    {
        System.out.println("==getHeaderLine==");
        BImportTable instance = BImportTable.getInstanceOf(BImportTableTypes.BORROWER_TABLE, this.testMap);
        String result = instance.getHeader();
        System.out.print("HEADER:"+result);
        result = instance.getData();
        System.out.print("  DATA:"+result);
        
        instance.setValue("name", "Billy, Balzac");
        result = instance.getHeader();
        System.out.print("HEADER:"+result);
        result = instance.getData();
        System.out.print("  DATA:"+result);
        
        instance.setValue("name", "Billy, Balzac");
        result = instance.getHeader();
        System.out.print("HEADER:"+result);
        instance.setValue("name", "Smith, Bob");
        result = instance.getData();
        System.out.print("  DATA:"+result);
        
        this.testMap = new HashMap<>();
        this.testMap.put("second_id", "12345");
        this.testMap.put("name", "Smith" + ", " + "David");
        this.testMap.put("expiration_date", "20180101");
        this.testMap.put("birth_date", "19700101");
        
        BImportTable instance1 = BImportTable.getInstanceOf(BImportTableTypes.BORROWER_ADDRESS_TABLE, testMap);
        result = instance1.getHeader();
        System.out.print("HEADER:"+result);
        result = instance1.getData();
        System.out.print("  DATA:"+result);
        
        result = instance.getHeader();
        System.out.print("HEADER:"+result);
        result = instance.getData();
        System.out.print("  DATA:"+result);
    }

    /**
     * Test of getInstanceOf method, of class BImportTable.
     */
    @Test
    public void testGetInstanceOf()
    {
        System.out.println("getInstanceOf");
        BImportTableTypes type = null;
        HashMap<String, String> dataFields = null;
        BImportTable expResult = null;
        BImportTable result = BImportTable.getInstanceOf(type, dataFields);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}