package mecard.customer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class FlatUserFormatterTest
{
    private StringBuffer dumpFlatOutput;
    public FlatUserFormatterTest()
    {
        dumpFlatOutput = new StringBuffer();
        dumpFlatOutput.append("*** DOCUMENT BOUNDARY ***\n");
        dumpFlatOutput.append(".USER_ID.   |a21221015133926\n");
        dumpFlatOutput.append(".USER_TITLE.   |aDr\n");
        dumpFlatOutput.append(".USER_NAME.   |aBalzac, William (Dr)\n");
        dumpFlatOutput.append(".USER_FIRST_NAME.   |aWilliam\n");
        dumpFlatOutput.append(".USER_LAST_NAME.   |aBalzac\n");
        dumpFlatOutput.append(".USER_PREFERRED_NAME.   |aDanny\n");
        dumpFlatOutput.append(".USER_NAME_DSP_PREF.   |a0\n");
        dumpFlatOutput.append(".USER_LIBRARY.   |aEPLMNA\n");
        dumpFlatOutput.append(".USER_PROFILE.   |aEPL-ADULT\n");
        dumpFlatOutput.append(".USER_PREF_LANG.   |aENGLISH\n");
        dumpFlatOutput.append(".USER_PIN.   |a64058\n");
        dumpFlatOutput.append(".USER_STATUS.   |aOK\n");
        dumpFlatOutput.append(".USER_ROUTING_FLAG.   |aN\n");
        dumpFlatOutput.append(".USER_CHG_HIST_RULE.   |aCIRCRULE\n");
        dumpFlatOutput.append(".USER_LAST_ACTIVITY.   |a20130611\n");
        dumpFlatOutput.append(".USER_PRIV_GRANTED.   |a20130514\n");
        dumpFlatOutput.append(".USER_PRIV_EXPIRES.   |a20140514\n");
        dumpFlatOutput.append(".USER_BIRTH_DATE.   |aNEVER\n");
        dumpFlatOutput.append(".USER_ACCESS.   |aPUBLIC\n");
        dumpFlatOutput.append(".USER_ENVIRONMENT.   |aPUBLIC\n");
        dumpFlatOutput.append(".USER_MAILINGADDR.   |a1\n");
        dumpFlatOutput.append(".USER_ADDR1_BEGIN.\n");
        dumpFlatOutput.append(".STREET. |a11811 72 Ave.\n");
        dumpFlatOutput.append(".CITY/STATE. |aEdmonton, AB\n");
        dumpFlatOutput.append(".POSTALCODE. |aT6G 2B2\n");
        dumpFlatOutput.append(".PHONE. |a780-340-9998\n");
        dumpFlatOutput.append(".EMAIL. |ailsteam@epl.ca\n");
        dumpFlatOutput.append(".USER_ADDR1_END.\n");
        dumpFlatOutput.append(".USER_ADDR2_BEGIN.\n");
        dumpFlatOutput.append(".USER_ADDR2_END.\n");
        dumpFlatOutput.append(".USER_ADDR3_BEGIN.\n");
        dumpFlatOutput.append(".USER_ADDR3_END.\n");
        dumpFlatOutput.append(".USER_XINFO_BEGIN.\n");
        dumpFlatOutput.append(".NOTIFY_VIA. |aPHONE\n");
        dumpFlatOutput.append(".USER_XINFO_END.\n");
//          1 $<user> $(1303)
//        Symphony $<print:u> $<user:u> $<finished_on> $<wednesday:u>, $<june:u> 12, 2013, 2:40 PM
    }

    /**
     * Test of setCustomer method, of class FlatUserFormatter.
     */
    @Test
    public void testSetCustomer()
    {
        System.out.println("==setCustomer==");
        Customer customer = null;
        FlatUserFormatter formatter = new FlatUserFormatter();
        List<String> expResult = new ArrayList<String>();
        List<String> result = formatter.setCustomer(customer);
        assertEquals(expResult, result);
        fail("Not implemented yet.");
    }

    /**
     * Test of getCustomer method, of class FlatUserFormatter.
     */
    @Test
    public void testGetCustomer_String()
    {
        System.out.println("==getCustomer String==");
        FlatUserFormatter formatter = new FlatUserFormatter();
        String cString = "[\"RA1\",\"232132132132\",\"21221015133926\",\"64058\",\"Balzac, William (Dr)\",\"11811 72 Ave.\",\"Edmonton\",\"AB\",\"T6G2B2\",\"X\",\"ilsteam@epl.ca\",\"7803409998\",\"X\",\"20140514\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"X\",\"William (Dr)\",\"Balzac\"]";
        Customer expResult = new Customer(cString);
//        System.out.println("EXP_CUST_STRING:"+expResult.toString());
        Customer customer = formatter.getCustomer(dumpFlatOutput.toString());
        System.out.println("EXP_CUST_RESULT:"+expResult);
        System.out.println("       CUSTOMER:"+customer);
        assertTrue(expResult.toString().compareTo(customer.toString()) == 0);
    }
}