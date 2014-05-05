package mecard.customer;
import mecard.customer.symphony.FlatFormattedCustomer;
import mecard.Request;
import java.util.ArrayList;
import java.util.List;
import json.RequestDeserializer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class FlatCustomerFormatterTest
{
    private StringBuffer dumpFlatOutput;
    public FlatCustomerFormatterTest()
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
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        Customer customer = request.getCustomer();
        FlatFormattedCustomer formatter = new FlatFormattedCustomer(customer);
        List<String> expResult = new ArrayList<>();
        expResult.add("*** DOCUMENT BOUNDARY ***\n");
        expResult.add(".USER_ID.   |a21221012345678\n");
        expResult.add(".USER_FIRST_NAME.   |aBalzac\n");
        expResult.add(".USER_LAST_NAME.   |aBilly\n");
        expResult.add(".USER_PREFERRED_NAME.   |aBilly, Balzac\n");
        expResult.add(".USER_LIBRARY.   |aEPLMNA\n");
        expResult.add(".USER_PROFILE.   |aEPL-METRO\n");
        expResult.add(".USER_PREF_LANG.   |aENGLISH\n");
        expResult.add(".USER_PIN.   |a64058\n");
        expResult.add(".USER_STATUS.   |aOK\n");
        expResult.add(".USER_ROUTING_FLAG.   |aY\n");
        expResult.add(".USER_CHG_HIST_RULE.   |aALLCHARGES\n");
        expResult.add(".USER_PRIV_GRANTED.   |a20130724\n");
        expResult.add(".USER_PRIV_EXPIRES.   |a20140602\n");
        expResult.add(".USER_BIRTH_DATE.   |a19750822\n");
        expResult.add(".USER_CATEGORY2.   |aM\n");
        expResult.add(".USER_ACCESS.   |aPUBLIC\n");
        expResult.add(".USER_ENVIRONMENT.   |aPUBLIC\n");
        expResult.add(".USER_ADDR1_BEGIN.\n");
        expResult.add(".STREET.   |a12345 123 St.\n");
        expResult.add(".CITY/STATE.   |aEdmonton, ALBERTA\n");
        expResult.add(".POSTALCODE.   |aH0H0H0\n");
        expResult.add(".PHONE.   |a7804964058\n");
        expResult.add(".EMAIL.   |ailsteam@epl.ca\n");
        expResult.add(".USER_ADDR1_END.\n");

        List<String> result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("=>"+s);
        }
        assertEquals(expResult, result);
    }

    
}