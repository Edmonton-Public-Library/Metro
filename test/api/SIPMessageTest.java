
package api;
import java.util.ArrayList;
import java.util.List;
import mecard.exception.SIPException;
import org.junit.Test;


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
    
    public SIPMessageTest()
    {
        this.responseOne = "98YYYYNN01000320130808    1509002.00AOst|AMSt. Albert Public Library|BXYYYYYYYYYYYYYYYY|ANUnassigned|VNSIP 2.00.106 SirsiDynix Inc. 7.5.074.40|AY1AZD3FA";
        this.responseTwo = "98YYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYYYYYNNYYY|ANSIPCHK|AY1AZE80C";
        this.responseThree = "B|";
    }

    /**
     * Test of getFieldNames method, of class SIPMessage.
     */
    @Test
    public void testGetFieldNames()
    {
        // // System.out.println("== getFieldNames ==");
        SIPMessage instance = new SIPMessage(responseTwo);
        List<String> expResult = new ArrayList<String>();
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
        catch(Exception ex)
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
        // // System.out.println("IS_ONLINE:"+instance.isOnline());
        String result = instance.isOnline();
        assertEquals(expResult.compareTo(result), 0);
        
        instance = new SIPStatusMessage(responseTwo);
        expResult = "Y";
        // // System.out.println("IS_ONLINE:"+instance.isOnline());
        result = instance.isOnline();
        assertEquals(expResult.compareTo(result), 0);
        
        try
        {
            instance = new SIPStatusMessage("77PYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|ANSIPCHK|AY1AZE80C");
            // // System.out.println("IS_ONLINE:"+instance.isOnline());
            result = instance.isOnline();
            assertTrue(result.isEmpty());
        }
        catch (Exception e)
        {
            // Should fail since this is not a status result message.
            assertTrue(e instanceof SIPException);
        }
        
        instance = new SIPStatusMessage("98NYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYNYYYNNYYY|ANSIPCHK|AY1AZE80C");
        expResult = "N";
        // // System.out.println("IS_ONLINE:"+instance.isOnline());
        result = instance.isOnline();
        assertEquals(expResult.compareTo(result), 0);
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
        String result = SIPMessage.cleanDateTime(possibleDate);
        assertTrue(expResult.compareTo(result) == 0);
        
        
        SIPCustomerMessage instance = new SIPCustomerMessage("64              00020130903    143600000000000002000000000010AOsps|AA21974011602274|AENUTTYCOMBE, SHARON|AQsps|BZ0200|CA0020|CB0150|BLY|CQY|BD66 Great Oaks, Sherwood Park, Ab, T8A 0V8|BEredtarot@telus.net|BF780-416-5518|DHSHARON|DJNUTTYCOMBE|PASTAFF|PB19680920|PCs|PE20140903    235900STAFF|PS20140903    235900STAFF|ZYs|AY1AZA949");
        possibleDate = instance.getField("PA");
        System.out.println("PA:"+possibleDate);
        assertFalse(SIPMessage.isDate(possibleDate));
        possibleDate = instance.getField("PE");
        System.out.println("PE:'"+possibleDate+"'");
        assertFalse(SIPMessage.isDate(possibleDate));
        System.out.println("CLEAN:'"+SIPMessage.cleanDateTime(possibleDate)+"'");
        result = SIPMessage.cleanDateTime(possibleDate);
        expResult = "20140903";
        assertTrue(expResult.compareTo(result) == 0);
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
        String possibleDate = "20131231";
        boolean expResult = true;
        boolean result = SIPMessage.isDate(possibleDate);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class SIPMessage.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        SIPMessage instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}