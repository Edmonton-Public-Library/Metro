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

import mecard.horizon.BImportBat;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class BImportBatTest
{
    
    public BImportBatTest()
    {
        
    }

    @Test
    public void testSomeMethod()
    {
        File f = new File("testfile.bat");
        if (f.exists())
        {
            f.delete();
        }
        BImportBat b = new BImportBat.Builder("testfile.bat")
                .setBimportPath("C:\\metro\\logs\\Customers\\bimport.exe").server("'Horizon MSSQL Server'")
                .password("sql54200").user("sa").database("stalbert")
                .header("C:\\metro\\logs\\Customers\\header.txt").data("C:\\metro\\logs\\Customers\\data.txt").borrowerTableKey("second_id")
                .format("m41").bType("metro").mType("dom").location("st").setIndexed(true).build();
        f = new File("testfile.bat");
        assertTrue(f.exists());
        
    }

    /**
     * Test of getCommandLine method, of class BImportBat.
     */
    @Test
    public void testGetCommandLine() {
        System.out.println("==getCommandLine==");
        BImportBat b = new BImportBat.Builder("testfile.bat")
                .setBimportPath("C:\\metro\\logs\\Customers\\bimport.exe").server("'Horizon MSSQL Server'")
                .password("sql54200").user("sa").database("stalbert")
                .header("C:\\metro\\logs\\Customers\\header.txt").data("C:\\metro\\logs\\Customers\\data.txt").borrowerTableKey("second_id")
                .format("m41").bType("metro").mType("dom").location("st").setIndexed(true).build();
        System.out.println("cmd:'"+b.getCommandLine()+"'");
        List<String> resultArray = new ArrayList<>();
        b.getCommandLine(resultArray);
        System.out.println("RSLT_ARRY:" + resultArray);
        
    }

    /**
     * Test of getBatchFileName method, of class BImportBat.
     */
    @Test
    public void testGetBatchFileName() 
    {
        System.out.println("===getBatchFileName===");
        BImportBat b = new BImportBat.Builder().server("server")
                .password("password").user("user").database("database")
                .header("headerFileName.txt").data("dataFileName").borrowerTableKey("second_id")
                .format("m41").bType("awb").location("alap").setIndexed(true).build();
        
        assertTrue(b.getBatchFileName().compareTo("obsolete.bat") == 0);
    }
}