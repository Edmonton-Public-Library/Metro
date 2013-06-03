/**
*
* This class is part of the Metro, MeCard project.
*    Copyright (C) 2013  Andrew Nisbet, Edmonton public Library.
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
        System.out.println("getYearsOld");
        String date = "1963-08-22"; // "08/22/1963"
        int expResult = 49;
        int result = DateComparer.getYearsOld(date);
        assertEquals(expResult, result);
        
        date = "2013-04-08";
        expResult = 0;
        result = DateComparer.getYearsOld(date);
        assertEquals(expResult, result);
        
        date = "2014-04-08";
        expResult = 0;
        result = DateComparer.getYearsOld(date);
        assertEquals(expResult, result);
        
        date = "2012-04-08";
        expResult = 1;
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
        String date = DateComparer.ANSIToHumanReadable(DateComparer.today());
        int expResult = 0;
        int result = DateComparer.getDaysUntilExpiry(date);
        assertEquals(expResult, result);
        // take today's date and subtract a day.
        int days = Integer.parseInt(DateComparer.today().substring(6, 8));
        date = date.substring(0, 7) + "-"+String.valueOf(days - 1);
        expResult = -1;
        result = DateComparer.getDaysUntilExpiry(date);
        assertEquals(expResult, result);
        
        date = "2013-01-01";
        result = DateComparer.getDaysUntilExpiry(date);
        System.out.println("days since "+date+": "+result);
        
    }

    /**
     * Test of today method, of class DateComparer.
     */
    @Test
    public void testToday()
    {
        System.out.println("today");
        String expResult = "20130603";
        String result = DateComparer.today();
//        System.out.println("RESULT:"+result);
        assertTrue(expResult.compareTo(result) == 0);
    }

    /**
     * Test of formatDate method, of class DateComparer.
     */
    @Test
    public void testFormatDate()
    {
        System.out.println("formatDate");
        String date = "2013-03-29";
        String expResult = "20130329";
        String result = DateComparer.formatDate(date);
        assertTrue(expResult.compareTo(result) == 0);
    }

    /**
     * Test of ANSIToHumanReadable method, of class DateComparer.
     */
    @Test
    public void testANSIToHumanReadable()
    {
        System.out.println("=== ANSIToHumanReadable ===");
        String date = "20000101";
        String expResult = "2000-01-01";
        String result = DateComparer.ANSIToHumanReadable(date);
        assertTrue(expResult.compareTo(result) == 0);
        
        date = "andrew";
        expResult = "";
        result = DateComparer.ANSIToHumanReadable(date);
        assertTrue(expResult.compareTo(result) == 0);
        
        date = "200001015";
        expResult = "";
        result = DateComparer.ANSIToHumanReadable(date);
        assertTrue(expResult.compareTo(result) == 0);
    }
}