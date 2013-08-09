
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
        System.out.println("== getFieldNames ==");
        SIPMessage instance = new SIPMessage(responseTwo);
        List<String> expResult = new ArrayList<String>();
        expResult.add("BX");
        expResult.add("AY");
        expResult.add("AM");
        expResult.add("AN");
        System.out.println("MSG:"+instance);
        System.out.println("CODE:"+instance.getCode());
        System.out.println("CODE:"+instance.getCodeMessage());
        System.out.println("KEYS:"+instance.getFieldNames());
        List<String> result = instance.getFieldNames();
        assertEquals(expResult, result);
    }

    /**
     * Test of getField method, of class SIPMessage.
     */
    @Test
    public void testGetField()
    {
        System.out.println("== testGetField ==");
        SIPMessage instance = new SIPMessage(responseOne);
        String expResult = "YYYYYYYYYYYYYYYY";
        System.out.println("MSG:"+instance);
        System.out.println("KEYS:"+instance.getFieldNames());
        String result = instance.getField("BX");
        assertEquals(expResult.compareTo(result), 0);
    }
    
    /**
     * Test of getField method, of class SIPMessage.
     */
    @Test
    public void testGetBogus()
    {
        System.out.println("== testGetBogus ==");
        try
        {
            SIPMessage instance = new SIPMessage(responseThree);
        }
        catch(Exception ex)
        {
            System.out.println(">>" + ex.getMessage());
            assertTrue(ex instanceof SIPException);
        }
    }
    
    /**
     * Test of getField method, of class SIPMessage.
     */
    @Test
    public void testGetPatronInfoPermission()
    {
        System.out.println("== testGetPatronInfoPermission ==");
        
        SIPMessage instance = new SIPMessage(responseOne);
        String expResult = "Y";
        System.out.println("PATRON_INFO:"+instance.getPatronInfoPermitted());
        String result = instance.getPatronInfoPermitted();
        assertEquals(expResult.compareTo(result), 0);
        
        instance = new SIPMessage(responseTwo);
        expResult = "Y";
        System.out.println("PATRON_INFO:"+instance.getPatronInfoPermitted());
        result = instance.getPatronInfoPermitted();
        assertEquals(expResult.compareTo(result), 0);
        
        instance = new SIPMessage("98YYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|ANSIPCHK|AY1AZE80C");
        System.out.println("PATRON_INFO:"+instance.getPatronInfoPermitted());
        result = instance.getPatronInfoPermitted();
        assertTrue(result.isEmpty());
        
        instance = new SIPMessage("98NYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYNYYYNNYYY|ANSIPCHK|AY1AZE80C");
        expResult = "N";
        System.out.println("PATRON_INFO:"+instance.getPatronInfoPermitted());
        result = instance.getPatronInfoPermitted();
        assertEquals(expResult.compareTo(result), 0);
    }
    
    /**
     * Test of getField method, of class SIPMessage.
     */
    @Test
    public void testGetOnlineStatus()
    {
        System.out.println("== testGetOnlineStatus ==");
        
        SIPMessage instance = new SIPMessage(responseOne);
        String expResult = "Y";
        System.out.println("IS_ONLINE:"+instance.isOnline());
        String result = instance.isOnline();
        assertEquals(expResult.compareTo(result), 0);
        
        instance = new SIPMessage(responseTwo);
        expResult = "Y";
        System.out.println("IS_ONLINE:"+instance.isOnline());
        result = instance.isOnline();
        assertEquals(expResult.compareTo(result), 0);
        
        instance = new SIPMessage("77PYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|ANSIPCHK|AY1AZE80C");
        System.out.println("IS_ONLINE:"+instance.isOnline());
        result = instance.isOnline();
        assertTrue(result.isEmpty());
        
        instance = new SIPMessage("98NYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYNYYYNNYYY|ANSIPCHK|AY1AZE80C");
        expResult = "N";
        System.out.println("IS_ONLINE:"+instance.isOnline());
        result = instance.isOnline();
        assertEquals(expResult.compareTo(result), 0);
    }
}