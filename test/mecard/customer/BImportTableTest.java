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
import mecard.horizon.MeCardDataToBImportData;
import java.util.HashMap;
import mecard.config.BImportTableTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com> 
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
     * Test of getHeaderLine method, of class MeCardDataToBImportData.
     */
    @Test
    public void testGetHeaderLine()
    {
        System.out.println("==getHeaderLine==");
        MeCardDataToBImportData instance = MeCardDataToBImportData.getInstanceOf(BImportTableTypes.BORROWER_TABLE, this.testMap);
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
        
        MeCardDataToBImportData instance1 = MeCardDataToBImportData.getInstanceOf(BImportTableTypes.BORROWER_ADDRESS_TABLE, testMap);
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
     * Test of getInstanceOf method, of class MeCardDataToBImportData.
     */
    @Test
    public void testGetInstanceOf()
    {
        System.out.println("getInstanceOf");
        BImportTableTypes type = null;
        HashMap<String, String> dataFields = null;
        MeCardDataToBImportData expResult = null;
        MeCardDataToBImportData result = MeCardDataToBImportData.getInstanceOf(type, dataFields);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}