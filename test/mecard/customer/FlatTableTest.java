/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 */
package mecard.customer;

import mecard.symphony.MeCardDataToFlatData;
import java.util.HashMap;
import mecard.config.FlatUserTableTypes;
import mecard.config.FlatUserFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com> 
 */
public class FlatTableTest
{
    private final StringBuffer dumpFlatOutput;
    
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
     * Test of getInstanceOf method, of class MeCardDataToFlatData.
     */
    @Test
    public void testGetInstanceOf()
    {
        System.out.println("==getInstanceOBImportTable==");
        FlatUserTableTypes type = FlatUserTableTypes.USER;
        HashMap<String, String> dataFields = new HashMap<>();
        MeCardDataToFlatData result = MeCardDataToFlatData.getInstanceOf(type, dataFields);
        System.out.println("TABLE:" + result.getData());
    }

    /**
     * Test of getData method, of class MeCardDataToFlatData.
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
        MeCardDataToFlatData flatTable = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER, customerData);
        System.out.println("FLAT:" + flatTable.getData());
    }

    /**
     * Test of getHeader method, of class MeCardDataToFlatData.
     */
    @Test
    public void testGetHeader()
    {
        System.out.println("==getHeader==");
        HashMap<String, String> customerData = new HashMap<>();
        MeCardDataToFlatData flatTable = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER, customerData);
        System.out.print("HEADER:\n" + flatTable.getHeader());
    }

    /**
     * Test of getName method, of class MeCardDataToFlatData.
     */
    @Test
    public void testGetName()
    {
        System.out.println("==getName==");
        HashMap<String, String> customerData = new HashMap<>();
        MeCardDataToFlatData flatTable = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER, customerData);
        System.out.println("NAME:" + flatTable.getName());
        
        flatTable = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER_ADDR1, customerData);
        System.out.println("NAME:" + flatTable.getName());
        
        flatTable = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER_ADDR2, customerData);
        System.out.println("NAME:" + flatTable.getName());
        
        flatTable = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER_XINFO, customerData);
        System.out.println("NAME:" + flatTable.getName());
    }

    /**
     * Test of getValue method, of class MeCardDataToFlatData.
     */
    @Test
    public void testGetValue()
    {
        System.out.println("==getValue==");
        HashMap<String, String> customerData = new HashMap<>();
        customerData.put("USER_ID", "21221012345678");
        MeCardDataToFlatData flatTable = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER, customerData);
//        System.out.println("VALUE:\n" + flatTable.getData());
        String value = flatTable.getValue(FlatUserFieldTypes.USER_ID.name());
        System.out.println("VALUE:" + value);
        assertTrue(value.compareTo("21221012345678") == 0);
    }
    
    /**
     * Test of renameKey method, of class MeCardDataToFlatData.
     */
    @Test
    public void testRenameKey()
    {
        System.out.println("==renameKey==");
        String newKeyName = "CITYPROV";
        String storedValue= "Edmonton, AB";
        HashMap<String, String> customerData = new HashMap<>();
        customerData.put(FlatUserFieldTypes.CITY_SLASH_STATE.toString(), storedValue);
        MeCardDataToFlatData flatTable = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER, customerData);
//        System.out.println("VALUE:\n" + flatTable.getData());
        String value = flatTable.getValue(FlatUserFieldTypes.CITY_SLASH_STATE.toString());
        System.out.println("VALUE:" + value);
        assertTrue(value.compareTo(storedValue) == 0);
        assertTrue(flatTable.renameKey(FlatUserFieldTypes.CITY_SLASH_STATE.toString(), FlatUserFieldTypes.CITY_PROV.toString()));
        value = flatTable.getValue(FlatUserFieldTypes.CITY_PROV.toString());
        assertTrue(value.compareTo(storedValue) == 0);
        
        // Try to rename a key that isn't in the table.
        assertFalse(flatTable.renameKey(FlatUserFieldTypes.USER_ID.toString(), FlatUserFieldTypes.CITY_PROV.toString()));
        value = flatTable.getValue(FlatUserFieldTypes.CITY_PROV.toString());
        // Show that the original setting is untouched by trial.
        assertTrue(value.compareTo(storedValue) == 0);
        
        // Add the city state key back in.
        flatTable.setValue(FlatUserFieldTypes.CITY_SLASH_STATE.toString(), storedValue + "eh");
        System.out.println("before VALUE:\n" + flatTable.getData());
        assertTrue(flatTable.getValue(FlatUserFieldTypes.CITY_SLASH_STATE.toString()).compareTo(storedValue + "eh")==0);
        value = flatTable.getValue(FlatUserFieldTypes.CITY_PROV.toString());
        assertTrue(value.compareTo(storedValue) == 0);
        System.out.println("before VALUEs:\n" + flatTable.getData());
        // now what happens when we try to rename a key to a key that already exists.
        assertTrue(flatTable.renameKey(FlatUserFieldTypes.CITY_SLASH_STATE.toString(), FlatUserFieldTypes.CITY_PROV.toString()));
        value = flatTable.getValue(FlatUserFieldTypes.CITY_PROV.toString());
        System.out.println("after  VALUEs:\n" + flatTable.getData());
        assertTrue(value.compareTo(storedValue + "eh") == 0);
        System.out.println("after  VALUEs:\n" + flatTable.getData());
    }

    /**
     * Test of setValue method, of class MeCardDataToFlatData.
     */
    @Test
    public void testSetValue()
    {
        System.out.println("==setValue==");
        HashMap<String, String> customerData = new HashMap<>();
        customerData.put("USER_ID", "21221012345678");
        MeCardDataToFlatData table = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER, customerData);
//        System.out.println("VALUE:\n" + flatTable.getData());
        String value = table.getValue(FlatUserFieldTypes.USER_ID.name());
        System.out.println("VALUE:" + value);
        assertTrue(value.compareTo("21221012345678") == 0);
        
        table.setValue(FlatUserFieldTypes.USER_ID.name(), "999");
        value = table.getValue(FlatUserFieldTypes.USER_ID.name());
        System.out.println("VALUE:" + value);
        assertTrue(value.compareTo("999") == 0);
        // Value not in table.
        value = table.getValue(FlatUserFieldTypes.CITY_SLASH_STATE.name());
        System.out.println("VALUE:" + value);
        assertTrue(value.compareTo("") == 0);
    }

    /**
     * Test of finalizeTable method, of class MeCardDataToFlatData.
     */
    @Test
    public void testFinalizeTable()
    {
        System.out.println("==finalizeTable==");
        HashMap<String, String> customerData = new HashMap<>();
        MeCardDataToFlatData table = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER, customerData);
        System.out.print(">\n"+table.finalizeTable(new StringBuilder()));
        
        table = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER_ADDR1, customerData);
        System.out.print(">\n"+table.finalizeTable(new StringBuilder()));
        
        table = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER_ADDR1, customerData);
        System.out.print(">\n"+table.finalizeTable(new StringBuilder()));
        
        table = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER_XINFO, customerData);
        System.out.print(">\n"+table.finalizeTable(new StringBuilder()));
    }

    /**
     * Test of deleteValue method, of class MeCardDataToFlatData.
     */
    @Test
    public void testDeleteValue()
    {
        System.out.println("==***deleteValue***==");
        HashMap<String, String> customerData = new HashMap<>();
        customerData.put("USER_ID", "21221012345678");
        MeCardDataToFlatData table = MeCardDataToFlatData.getInstanceOf(FlatUserTableTypes.USER, customerData);
//        System.out.println("VALUE:\n" + table.getData());
        String value = table.getValue(FlatUserFieldTypes.USER_ID.name());
        System.out.println("before VALUE:" + value);
        assertTrue(value.compareTo("21221012345678") == 0);
        assertTrue(table.deleteValue("USER_ID"));
        value = table.getValue(FlatUserFieldTypes.USER_ID.name());
        System.out.println("after VALUE:" + value);
        assertFalse(table.deleteValue("USER_ID"));
    }
}