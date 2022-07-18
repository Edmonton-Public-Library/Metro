
package mecard.sip;
import java.util.ArrayList;
import java.util.List;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.exception.SIPException;
import mecard.util.DateComparer;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SIPMessageTest
{
    private final String responseTwo;
    private final String responseOne;
    private final String responseThree;
    private final String responseLogin;
    
    public SIPMessageTest()
    {
        this.responseOne = "98YYYYNN01000320130808    1509002.00AOst|AMSt. Albert Public Library|BXYYYYYYYYYYYYYYYY|ANUnassigned|VNSIP 2.00.106 SirsiDynix Inc. 7.5.074.40|AY1AZD3FA";
        this.responseTwo = "98YYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYYYYYNNYYY|ANSIPCHK|AY1AZE80C";
        this.responseThree = "B|";
        this.responseLogin = "941AY1AZFDFC";
    }

    /**
     * Test of getFieldNames method, of class SIPMessage.
     */
    @Test
    public void testGetFieldNames()
    {
        // // System.out.println("== getFieldNames ==");
        SIPMessage instance = new SIPMessage(responseTwo);
        List<String> expResult = new ArrayList<>();
        expResult.add("BX");
        expResult.add("AY");
        expResult.add("AM");
        expResult.add("AN");
        // // System.out.println("MSG:"+instance);
        // // System.out.println("CODE:"+instance.getCode());
        // // System.out.println("CODE:"+instance.getCodeMessage());
        // // System.out.println("KEYS:"+instance.getFieldNames());
        List<String> result = instance.getFieldNames();
        assertEquals(expResult, result);
    }

    /**
     * Test of getField method, of class SIPMessage.
     */
    @Test
    public void testGetField()
    {
        // // System.out.println("== testGetField ==");
        SIPMessage instance = new SIPMessage(responseOne);
        String expResult = "YYYYYYYYYYYYYYYY";
        // // System.out.println("MSG:"+instance);
        // // System.out.println("KEYS:"+instance.getFieldNames());
        String result = instance.getField("BX");
        assertEquals(expResult.compareTo(result), 0);
    }
    
    /**
     * Test of getField method, of class SIPMessage.
     */
    @Test
    public void testGetBogus()
    {
        // System.out.println("== testGetBogus ==");
        try
        {
            SIPMessage instance = new SIPMessage(responseThree);
        }
        catch(SIPException ex)
        {
            // // System.out.println(">>" + ex.getMessage());
            assertTrue(ex instanceof SIPException);
        }
    }
    
    /**
     * Test of getField method, of class SIPMessage.
     */
    @Test
    public void testGetPatronInfoPermission()
    {
        // System.out.println("== testGetPatronInfoPermission ==");
        
        SIPStatusMessage instance = new SIPStatusMessage(responseOne);
        String expResult = "Y";
        // // System.out.println("PATRON_INFO:"+instance.getPatronInfoPermitted());
        String result = instance.getPatronInfoPermitted();
        assertEquals(expResult.compareTo(result), 0);
        
        instance = new SIPStatusMessage(responseTwo);
        expResult = "Y";
        // // System.out.println("PATRON_INFO:"+instance.getPatronInfoPermitted());
        result = instance.getPatronInfoPermitted();
        assertEquals(expResult.compareTo(result), 0);
        
        instance = new SIPStatusMessage("98YYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|ANSIPCHK|AY1AZE80C");
        // // System.out.println("PATRON_INFO:"+instance.getPatronInfoPermitted());
        result = instance.getPatronInfoPermitted();
        assertTrue(result.isEmpty());
        
        instance = new SIPStatusMessage("98NYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYNYYYNNYYY|ANSIPCHK|AY1AZE80C");
        expResult = "N";
        // // System.out.println("PATRON_INFO:"+instance.getPatronInfoPermitted());
        result = instance.getPatronInfoPermitted();
        assertEquals(expResult.compareTo(result), 0);
    }
    
    /**
     * Test of getField method, of class SIPMessage.
     */
    @Test
    public void testGetOnlineStatus()
    {
        // System.out.println("== testGetOnlineStatus ==");
        
        SIPStatusMessage instance = new SIPStatusMessage(responseOne);
        String expResult = "Y";
        // // System.out.println("IS_ONLINE:"+instance.getOnlineStatus());
        String result = instance.getOnlineStatus();
        assertEquals(expResult.compareTo(result), 0);
        
        instance = new SIPStatusMessage(responseTwo);
        expResult = "Y";
        // // System.out.println("IS_ONLINE:"+instance.getOnlineStatus());
        result = instance.getOnlineStatus();
        assertEquals(expResult.compareTo(result), 0);
        
        try
        {
            instance = new SIPStatusMessage("77PYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|ANSIPCHK|AY1AZE80C");
            // // System.out.println("IS_ONLINE:"+instance.getOnlineStatus());
            result = instance.getOnlineStatus();
            assertTrue(result.isEmpty());
        }
        catch (Exception e)
        {
            // Should fail since this is not a status result message.
            assertTrue(e instanceof SIPException);
        }
        
        instance = new SIPStatusMessage("98NYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYNYYYNNYYY|ANSIPCHK|AY1AZE80C");
        expResult = "N";
        // // System.out.println("IS_ONLINE:"+instance.getOnlineStatus());
        result = instance.getOnlineStatus();
        assertEquals(expResult.compareTo(result), 0);
        
        // TRAC Response:
        // 98YYYYYN30000320140930	1339572.00AO203|AMSangudo Public Library|BXYYYYYYYNYNNNYYNN|AFSystem status ok.|AGSystem status ok.|AY1AZD6AC
        instance = new SIPStatusMessage("98YYYYYN30000320140930	1339572.00AO203|AMSangudo Public Library|BXYYYYYYYNYNNNYYNN|AFSystem status ok.|AGSystem status ok.|AY1AZD6AC");
        expResult = "Y";
        System.out.println("IS_ONLINE:"+instance.getOnlineStatus());
        result = instance.getOnlineStatus();
        assertEquals(expResult.compareTo(result), 0);
        assertTrue(instance.isOnline());
    }

    /**
     * Test of cleanDateTime method, of class SIPMessage.
     */
    @Test
    public void testCleanDateTime()
    {
        System.out.println("===cleanDateTime===");
        String possibleDate = "20131231    235900STAFF";
        String expResult = "20131231";
        String result = DateComparer.cleanDateTime(possibleDate);
        assertTrue(expResult.compareTo(result) == 0);
        
        
        SIPCustomerMessage instance = new SIPCustomerMessage("64              00020130903    143600000000000002000000000010AOsps|AA21974011602274|AENUTTYCOMBE, SHARON|AQsps|BZ0200|CA0020|CB0150|BLY|CQY|BD66 Great Oaks, Sherwood Park, Ab, T8A 0V8|BEredtarot@telus.net|BF780-416-5518|DHSHARON|DJNUTTYCOMBE|PASTAFF|PB19680920|PCs|PE20140903    235900STAFF|PS20140903    235900STAFF|ZYs|AY1AZA949");
        possibleDate = instance.getField("PA");
        System.out.println("PA:"+possibleDate);
        assertFalse(DateComparer.isDate(possibleDate));
        possibleDate = instance.getField("PE");
        System.out.println("PE:'"+possibleDate+"'");
        assertFalse(DateComparer.isDate(possibleDate));
        System.out.println("CLEAN:'"+DateComparer.cleanDateTime(possibleDate)+"'");
        result = DateComparer.cleanDateTime(possibleDate);
        expResult = "20140903";
        assertTrue(expResult.compareTo(result) == 0);
        
        SIPCustomerFormatter formatter = new SIPCustomerFormatter();
        ////////////////////////// NOTE ////////////////////////////////
        // The normalizer is part of this process and that is controlled in 
        // the environment.properties.
        Customer c = formatter.getCustomer("64              00020130903    143600000000000002000000000010AOsps|AA21974011602274|AENUTTYCOMBE, SHARON|AQsps|BZ0200|CA0020|CB0150|BLY|CQY|BD66 Great Oaks, Sherwood Park, Ab, T8A 0V8|BEredtarot@telus.net|BF780-416-5518|DHSHARON|DJNUTTYCOMBE|PA20151003    235900STAFF|PB19680920|PCs|PS20140903    235900STAFF|ZYs|AY1AZA949");
        System.out.println("C_EXPIRY:'" + c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES)+"'");
        assertTrue(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20151003") == 0);
        
//        formatter = new SIPCustomerFormatter();
//        c = formatter.getCustomer("64              00020140430    084800000000000014000000000001AOalap|AA21000006500560|AEFLYNN, GRACE|AQade|BZ0249|CA0010|CB0200|BLY|BHCAD|CC10.|BDRR#2, Delburne, AB, T0M 0V0|BEflynnstrings@gmail.com|BF403-749-3480|DHGRACE|DJFLYNN|PCra|PE20150430    235900|PS20150430    235900|ZYra|AY1AZB5FB");
//        System.out.println("C_EXPIRY:'" + c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES)+"'");
//        assertTrue(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20140903") == 0);
        
        String cm = "64              00020140430    084800000000000014000000000001AOalap|AA21000006500560|AEFLYNN, GRACE|AQade|BZ0249|CA0010|CB0200|BLY|BHCAD|CC10.|BDRR#2, Delburne, AB, T0M 0V0|BEflynnstrings@gmail.com|BF403-749-3480|DHGRACE|DJFLYNN|PCra|PE20150430    235900|PS20150430    235900|ZYra|AY1AZB5FB";
        instance = new SIPCustomerMessage(cm);
        String cleanDate = DateComparer.cleanDateTime(instance.getField("PE"));
        System.out.println("...CLEAN_DATE: '" + cleanDate + "'");
        assertTrue(cleanDate.equalsIgnoreCase("20150430"));
        // TODO: Test this string
        String cmOther = "64              00020170711    075000000000000000000000000000AOsps|AA21974012381670|AEDUGUID, JENNIFER|AQsps|BZ0150|CA0020|CB0150|BLY|CQY|BD10255 PRINCESS ELIZABETH AVENUE, Sherwood Park, AB, T5G 0Y1|BEjduguid@sclibrary.ca|BF780-862-4431|DHJENNIFER|DJDUGUID|PAstaff|PB19750517|PCs|PE20171231    235900staff|PS20171231    235900staff|ZYs|AY1AZA47A";
        instance = new SIPCustomerMessage(cmOther);
        System.out.println("...ADDRESS for Jennifer: '" + instance.getField("BD") + "'");
    }

    /**
     * Test of getCode method, of class SIPMessage.
     */
    @Test
    public void testGetCode()
    {
        System.out.println("==getCode==");
        SIPMessage instance = new SIPStatusMessage("98NYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYNYYYNNYYY|ANSIPCHK|AY1AZE80C");
        
        String expResult = "98";
        String result = instance.getCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCodeMessage method, of class SIPMessage.
     */
    @Test
    public void testGetCodeMessage()
    {
        System.out.println("==getCodeMessage==");
        SIPMessage instance = new SIPStatusMessage("98NYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYNYYYNNYYY|ANSIPCHK|AY1AZE80C");
        
        String expResult = "NYYYYN60000320130424    1135112.00AOEPLMNA";
        String result = instance.getCodeMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of isDate method, of class SIPMessage.
     */
    @Test
    public void testIsDate()
    {
        System.out.println("===isDate===");
        assertTrue(DateComparer.isDate("20010101"));
        assertFalse(DateComparer.isDate("20150430    235900"));
        assertTrue(DateComparer.isDate("20150430"));
    }

    @Test
    public void testLogin()
    {
        System.out.println("== Login ==");
        SIPMessage instance = new SIPMessage(this.responseLogin);
        String result = instance.getCodeMessage();
        System.out.println("LOGIN_RESULT: '" + result +"'");
        assertTrue("1".compareTo(result) == 0);
    }

    /**
     * Test of getCode method, of class SIPMessage.
     */
    @Test
    public void testIsResendRequest()
    {
        System.out.println("==isResendRequest==");
        SIPMessage instance = new SIPMessage("941AY1AZFDFC");
        assertFalse(instance.isResendRequest());
        instance = new SIPMessage("96AZFEF6");
        assertTrue(instance.isResendRequest());
    }
}