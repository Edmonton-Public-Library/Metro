
package mecard.customer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.config.CustomerFieldTypes;
import mecard.customer.sip.SIPCustomerFormatter;
import mecard.customer.symphony.FlatFormattedCustomer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */
public class DumpUserTest
{
    
    public DumpUserTest()
    {
    }

    @Test
    public void testSomeMethod()
    {
        System.out.println("==testDumpUser==");
        Customer customer = new Customer();
        new DumpUser.Builder(customer, ".", DumpUser.FileType.txt)
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".txt").exists());
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".txt").length() == 0);
        // Remove that file if it got past the assert
        new File(customer.get(CustomerFieldTypes.ID) + ".txt").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".txt").exists());
        // Test if customer data written.
        SIPCustomerFormatter formatter = new SIPCustomerFormatter();
//        customer = formatter.getCustomer("64              00020160714    122100000000000000000000000000AOalap|AA21000005874453|AELANCE DOGGIE|AQalap|BZ0249|CA0010|CB0200|BLY|BHCAD|CC25.|BD31 Cedar Crescent, Lacombe, AB, T4L 1V1|BElancedoggie@gmail.com|BF403-358-3019|DJLANCE DOGGIE|PCra|PA20170607    235900|PS20170607    235900|ZYra|AY0AZB2B0");
        customer = formatter.getCustomer("64              00120141002    135321000000000000000000000000AO203|AA29335002291042|AEHunting, Will|BZ0025|CA0010|CB0100|BLY|CQY|BHCAD|BV0.00|CC10.00|BD433 King Street, Spruce Grove, AB T7X 3B4|BEstephaniethero@shaw.ca|BF780-962-2003|BC19751002    235959|PA17|PEASGY|PSAdult (18-64)|U4(none)|U5|PZT7X 3B4|PX20201025    235959|PYN|AFPatron status is ok.|AGPatron status is ok.|AY2AZ9694");
//        assertTrue(customer.get(CustomerFieldTypes.DOB).compareTo("19751002") == 0);
//        assertTrue(customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20201025") == 0);
//        assertTrue(customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20170607") == 0); // this is dynamically calculated and is unhelpful during testing in the future.
        assertTrue(customer.get(CustomerFieldTypes.PHONE).compareTo("7809622003") == 0);
        new DumpUser.Builder(customer, ".", DumpUser.FileType.flat)
                .set(customer)
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".flat").exists());
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".flat").length() > 0);
        print(customer.get(CustomerFieldTypes.ID) + ".flat");
        new File(customer.get(CustomerFieldTypes.ID) + ".flat").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".flat").exists());
        
        // Test the FlatFormattedCustomer data test.
//        FormattedCustomer formatter = new FlatFormattedCustomer(customer);
        FormattedCustomer ffc = new FlatFormattedCustomer(customer);
        new DumpUser.Builder(customer, ".", DumpUser.FileType.flat)
                .set(ffc)
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".flat").exists());
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".flat").length() > 0);
        print(customer.get(CustomerFieldTypes.ID) + ".flat");
        new File(customer.get(CustomerFieldTypes.ID) + ".flat").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".flat").exists());
        
        
        
        
        // test the creation of all the types of files.
        new DumpUser.Builder(customer, ".", DumpUser.FileType.flat)
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".flat").exists());
        new File(customer.get(CustomerFieldTypes.ID) + ".flat").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".flat").exists());
        
        new DumpUser.Builder(customer, ".", DumpUser.FileType.txt)
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".txt").exists());
        new File(customer.get(CustomerFieldTypes.ID) + ".txt").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".txt").exists());
        
        new DumpUser.Builder(customer, ".", DumpUser.FileType.data)
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".data").exists());
        new File(customer.get(CustomerFieldTypes.ID) + ".data").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".data").exists());
        
        new DumpUser.Builder(customer, ".", DumpUser.FileType.fail)
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".fail").exists());
        new File(customer.get(CustomerFieldTypes.ID) + ".fail").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".fail").exists());
        
        new DumpUser.Builder(customer, ".", DumpUser.FileType.lost)
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".lost").exists());
        new File(customer.get(CustomerFieldTypes.ID) + ".lost").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".lost").exists());
        
        new DumpUser.Builder(customer, ".", DumpUser.FileType.pass)
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".pass").exists());
        new File(customer.get(CustomerFieldTypes.ID) + ".pass").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".pass").exists());
        
        
        
        // test string method.
        new DumpUser.Builder(customer, ".", DumpUser.FileType.flat)
                .set("Some data of my own choice")
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".flat").exists());
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".flat").length() > 0);
        print(customer.get(CustomerFieldTypes.ID) + ".flat");
        new File(customer.get(CustomerFieldTypes.ID) + ".flat").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".flat").exists());
        
        List<String> customerAsList = ffc.getFormattedCustomer();
        new DumpUser.Builder(customer, ".", DumpUser.FileType.pass)
                .set(customerAsList)
                .build();
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".pass").exists());
        assertTrue(new File(customer.get(CustomerFieldTypes.ID) + ".pass").length() > 0);
        print(customer.get(CustomerFieldTypes.ID) + ".pass");
        new File(customer.get(CustomerFieldTypes.ID) + ".pass").delete();
        assertFalse(new File(customer.get(CustomerFieldTypes.ID) + ".pass").exists());
    }
    
    private void print(String f)
    {
        BufferedReader br = null;
        try 
        {
            br = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                System.out.println(line);
            }
        } 
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(DumpUserTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(DumpUserTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally
        {
            try
            {
                br.close();
            } catch (IOException ex)
            {
                Logger.getLogger(DumpUserTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
