/**
*
* This class is part of the Metro, MeCard project.
*    Copyright (C) 2013  Edmonton Public Library, Edmonton public Library.
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

package mecard.util;

import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrew
 */
public class DateComparerTest
{
    
    public DateComparerTest()
    {
    }

    /**
     * Test of getYearsOld method, of class DateComparer.
     */
    @Test
    public void testGetYearsOld() throws Exception
    {
        System.out.println("==getYearsOld==");
        String date = "19630822"; // "08/22/1963"
        int expResult = 50;
        int result = DateComparer.getYearsOld(date);
        assertEquals(expResult, result);
        
        date = "20130408";
        expResult = 0;
        result = DateComparer.getYearsOld(date);
        assertEquals(expResult, result);
        
        date = "20140408";
        expResult = 0;
        result = DateComparer.getYearsOld(date);
        assertEquals(expResult, result);
        
        date = "20120408";
        expResult = 1;
        result = DateComparer.getYearsOld(date);
        assertEquals(expResult, result);
        
        date = "19980606";
        expResult = 15;
        result = DateComparer.getYearsOld(date);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getYearsOld method, of class DateComparer.
     */
    @Test
    public void testGetDaysUntilExpiry() throws Exception
    {
        System.out.println("=== getDaysUntilExpiry ===");
        ///////////// This test will fail tomorrow unless you change this test date!!!!
        String date = DateComparer.ANSIToday();
        int expResult = 0;
        int result = DateComparer.getDaysUntilExpiry(date);
        assertEquals(expResult, result);
        // take ANSIToday's date and subtract a day.
        int days = Integer.parseInt(DateComparer.ANSIToday().substring(6, 8));
        date = date.substring(0, 7) + String.valueOf(days - 1);
        expResult = -1;
        result = DateComparer.getDaysUntilExpiry(date);
        assertEquals(expResult, result);
        
        date = "20130101";
        result = DateComparer.getDaysUntilExpiry(date);
        System.out.println(">>>days since "+date+": "+result);
        
    }

    /**
     * Test of ANSIToday method, of class DateComparer.
     */
    @Test
    public void testToday()
    {
        System.out.println("===today===");
        String expResult = "20130806";
        String result = DateComparer.ANSIToday();
        System.out.println("RESULT:"+result);
        assertTrue(expResult.compareTo(result) == 0);
    }
    
    /**
     * Test of dateInFutre method, of class DateComparer.
     */
    @Test
    public void testDateInFuture()
    {
        System.out.println("===dateInFuture===");
        String expResult = "20130914";
        String result = DateComparer.getFutureDate(1);
        System.out.println("RESULT:"+result);
        assertTrue(expResult.compareTo(result) == 0);
        
        expResult = "20130913";
        result = DateComparer.getFutureDate(-1);
        System.out.println("RESULT:"+result);
        assertTrue(expResult.compareTo(result) == 0);
        
        expResult = "20140913";
        result = DateComparer.getFutureDate(365);
        System.out.println("RESULT:"+result);
        assertTrue(expResult.compareTo(result) == 0);
    }

    /**
     * Test of ANSIToday method, of class DateComparer.
     */
    @Test
    public void testANSIToday()
    {
        System.out.println("ANSIToday");
        String expResult = "";
        String result = DateComparer.ANSIToday();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ANSIToConfigDate method, of class DateComparer.
     */
    @Test
    public void testANSIToConfigDate() throws Exception
    {
        System.out.println("ANSIToConfigDate");
        String ANSIDate = "";
        String expResult = "";
        String result = DateComparer.ANSIToConfigDate(ANSIDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of isGreaterThanMinutesOld method, of class DateComparer.
     * Note for this test you will need a file some time in the future.
     */
    @Test
    public void testIsMinutesOld()
    {
        System.out.println("==isMinutesOld==");
        // 1395081104622
        long justMade = new Date().getTime();
        long olderThan = justMade - 16 * 60 * 1000;
        assertTrue(DateComparer.isGreaterThanMinutesOld(15,  justMade));
        assertFalse(DateComparer.isGreaterThanMinutesOld(15, olderThan));
    }
}