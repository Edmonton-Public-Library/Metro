package mecard.customer;
import java.util.List;
import mecard.config.CustomerFieldTypes;
import org.junit.Test;



import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SIPFormatterTest
{
    
    public SIPFormatterTest()
    {
    }


    /**
     * Test of setCustomer method, of class SIPFormatter.
     */
    @Test
    public void testSetCustomer()
    {
        System.out.println("==getCustomer==");
        SIPFormatter formatter = new SIPFormatter();
        
        Customer c = formatter.getCustomer("64              00020130903    143600000000000002000000000010AOsps|AA21974011602274|AENUTTYCOMBE, SHARON|AQsps|BZ0200|CA0020|CB0150|BLY|CQY|BD66 Great Oaks, Sherwood Park, Ab, T8A 0V8|BEredtarot@telus.net|BF780-416-5518|DHSHARON|DJNUTTYCOMBE|PASTAFF|PB19680920|PCs|PE20140903    235900STAFF|PS20140903    235900STAFF|ZYs|AY1AZA949");
        System.out.println("C_EXPIRY:'" + c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES)+"'");
        assertTrue(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20140903") == 0);
        
        c = formatter.getCustomer("64YYYY          00020130904    114900000000000000000000000002AOsps|AA21974012381670|AEDUGUID, JENNIFER|AQsps|BZ0200|CA0020|CB0150|BLY|CQN|BD10255 PRINCESS ELIZABETH AVENUE, Edmonton, Ab, T5G 0Y1|BEJDUGUID@SCLIBRARY.AB.CA|BF780-862-4431|DHJENNIFER|DJDUGUID|PAstaff|PB19750517|PCs|PE20140226    235900staff|PS20140226    235900staff|ZYs|AF#Incorrect password.|AY1AZ9E34");
        System.out.println("ADDR:'" + c.get(CustomerFieldTypes.STREET) + "'");
        System.out.println("ADDR:'" + c.get(CustomerFieldTypes.CITY) + "'");
        System.out.println("ADDR:'" + c.get(CustomerFieldTypes.PROVINCE) + "'");
        System.out.println("ADDR:'" + c.get(CustomerFieldTypes.POSTALCODE) + "'");
//        assertTrue(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20140903") == 0);
    }
}