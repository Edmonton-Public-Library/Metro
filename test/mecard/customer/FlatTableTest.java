package mecard.customer;
import java.util.HashMap;
import mecard.config.FlatUserExtendedFieldTypes;
import mecard.config.FlatUserFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrew
 */
public class FlatTableTest
{
    private StringBuffer dumpFlatOutput;
    
    public FlatTableTest()
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
     * Test of getInstanceOf method, of class FlatTable.
     */
    @Test
    public void testGetInstanceOf()
    {
        System.out.println("==getInstanceOBImportTable==");
        FlatUserExtendedFieldTypes type = FlatUserExtendedFieldTypes.USER;
        HashMap<String, String> dataFields = new HashMap<>();
        FlatTable result = FlatTable.getInstanceOf(type, dataFields);
        System.out.println("TABLE:" + result.getData());
    }

    /**
     * Test of getData method, of class FlatTable.
     */
    @Test
    public void testGetData()
    {
        System.out.println("==getData==");
        
        HashMap<String, String> customerData = new HashMap<>();
        customerData.put("USER_ID", "a21221012345678");
        customerData.put("USER_FIRST_NAME", "Balzac");
        customerData.put("USER_LAST_NAME", "Billy");
        customerData.put("USER_PREFERRED_NAME", "Billy, Balzac");
        customerData.put("USER_LIBRARY", "EPLMNA");
        customerData.put("USER_PROFILE", "EPL-METRO");
        customerData.put("USER_PREF_LANG", "ENGLISH");
        customerData.put("USER_PIN", "64058");
        customerData.put("USER_STATUS", "OK");
        customerData.put("USER_ROUTING_FLAG", "Y");
        customerData.put("USER_CHG_HIST_RULE", "ALLCHARGES");
        customerData.put("USER_PRIV_GRANTED", "20130724");
        customerData.put("USER_PRIV_EXPIRES", "20140602");
        customerData.put("USER_BIRTH_DATE", "19750822");
        customerData.put("USER_CATEGORY2", "M");
        customerData.put("USER_ACCESS", "PUBLIC");
        customerData.put("USER_ENVIRONMENT", "PUBLIC");
        FlatTable flatTable = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER, customerData);
        System.out.println("FLAT:" + flatTable.getData());
    }

    /**
     * Test of getHeader method, of class FlatTable.
     */
    @Test
    public void testGetHeader()
    {
        System.out.println("==getHeader==");
        HashMap<String, String> customerData = new HashMap<>();
        FlatTable flatTable = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER, customerData);
        System.out.print("HEADER:\n" + flatTable.getHeader());
    }

    /**
     * Test of getName method, of class FlatTable.
     */
    @Test
    public void testGetName()
    {
        System.out.println("==getName==");
        HashMap<String, String> customerData = new HashMap<>();
        FlatTable flatTable = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER, customerData);
        System.out.println("NAME:" + flatTable.getName());
        
        flatTable = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER_ADDR1, customerData);
        System.out.println("NAME:" + flatTable.getName());
        
        flatTable = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER_ADDR2, customerData);
        System.out.println("NAME:" + flatTable.getName());
        
        flatTable = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER_XINFO, customerData);
        System.out.println("NAME:" + flatTable.getName());
    }

    /**
     * Test of getValue method, of class FlatTable.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("==getValue==");
        HashMap<String, String> customerData = new HashMap<>();
        customerData.put("USER_ID", "21221012345678");
        FlatTable flatTable = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER, customerData);
//        System.out.println("VALUE:\n" + flatTable.getData());
        String value = flatTable.getValue(FlatUserFieldTypes.USER_ID.name());
        System.out.println("VALUE:" + value);
        assertTrue(value.compareTo("21221012345678") == 0);
    }

    /**
     * Test of setValue method, of class FlatTable.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        HashMap<String, String> customerData = new HashMap<>();
        customerData.put("USER_ID", "21221012345678");
        FlatTable table = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER, customerData);
//        System.out.println("VALUE:\n" + flatTable.getData());
        String value = table.getValue(FlatUserFieldTypes.USER_ID.name());
        System.out.println("VALUE:" + value);
        assertTrue(value.compareTo("21221012345678") == 0);
        
        table.setValue(FlatUserFieldTypes.USER_ID.name(), "999");
        value = table.getValue(FlatUserFieldTypes.USER_ID.name());
        System.out.println("VALUE:" + value);
        assertTrue(value.compareTo("999") == 0);
        // Value not in table.
        value = table.getValue(FlatUserFieldTypes.CITY_STATE.name());
        System.out.println("VALUE:" + value);
        assertTrue(value.compareTo("") == 0);
    }

    /**
     * Test of finalizeTable method, of class FlatTable.
     */
    @Test
    public void testFinalizeTable()
    {
        System.out.println("==finalizeTable==");
        HashMap<String, String> customerData = new HashMap<>();
        FlatTable table = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER, customerData);
        System.out.print(">\n"+table.finalizeTable(new StringBuilder()));
        
        table = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER_ADDR1, customerData);
        System.out.print(">\n"+table.finalizeTable(new StringBuilder()));
        
        table = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER_ADDR1, customerData);
        System.out.print(">\n"+table.finalizeTable(new StringBuilder()));
        
        table = FlatTable.getInstanceOf(FlatUserExtendedFieldTypes.USER_XINFO, customerData);
        System.out.print(">\n"+table.finalizeTable(new StringBuilder()));
    }
}