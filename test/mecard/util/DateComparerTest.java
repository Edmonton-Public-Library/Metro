/**
*
* This class is part of the Metro, MeCard project.
*    Copyright (C) 2013 - 2025  Edmonton Public Library, Edmonton public Library.
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

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import site.MeCardPolicy;

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
     * @throws java.lang.Exception if the date can't be parsed.
     */
    @Test
    public void testIsDateExpiry() throws Exception
    {
        System.out.println("== isValidDate ==");
        assertTrue(DateComparer.isAnsiDate("19630822"));
        assertFalse(DateComparer.isAnsiDate("235900STAFF"));
        // 20160209    235900
        assertFalse(DateComparer.isAnsiDate("20160209    235900"));
        assertTrue(DateComparer.isAnsiDate(DateComparer.cleanAnsiDateTime("20160209    235900")));
        
        
        
        String dob         = "";
        String expiry      = "";
        String lastUpdated = ""; // used for account updates. Should be set to TODAY.

        dob = DateComparer.ANSIToConfigDate("19630822");
        System.out.println(">>>>>>>" + dob + "<<<");
        expiry = DateComparer.ANSIToConfigDate("20190722");
        System.out.println(">>>>>>>" + expiry + "<<<");
    }
    
    /**
     * Test of getYearsOld method, of class DateComparer.
     */
    @Test
    public void testCleanAnsiDateTime()
    {
        System.out.println("== cleanDateTime ==");
        assertFalse(DateComparer.isAnsiDate("20131231    235900STAFF"));
        System.out.println("DATE_VALID: "+DateComparer.isAnsiDate("20131231    235900STAFF"));
        String newDate = DateComparer.cleanAnsiDateTime("20131231    235900STAFF");
        System.out.println("DATE_VALID: "+DateComparer.cleanAnsiDateTime("20131231    235900STAFF"));
        assertTrue(DateComparer.isAnsiDate(newDate));
        String newParklandDate = DateComparer.cleanAnsiDateTime("20170607    235900");
        System.out.println("\n\nDATE_VALID: "+newParklandDate);
        assertTrue(DateComparer.isAnsiDate(newParklandDate));
    }

    /**
     * Test of getYearsOld method, of class DateComparer.
     */
    @Test
    public void testGetYearsOld() throws Exception
    {
        System.out.println("==getYearsOld==");
        String date = "19630822"; // "08/22/1963"
        int expResult = 62;
        int result = DateComparer.getYearsOld(date);
        assertEquals(expResult, result);
        
        date = "20130408";
        expResult = 12;
        result = DateComparer.getYearsOld(date);
        assertEquals(expResult, result);
//        
//        date = "20140408";
//        expResult = 4;
//        result = DateComparer.getYearsOld(date);
//        assertEquals(expResult, result);
//        
//        date = "20120408";
//        expResult = 6;
//        result = DateComparer.getYearsOld(date);
//        assertEquals(expResult, result);
//        
//        date = "19980606";
//        expResult = 20;
//        result = DateComparer.getYearsOld(date);
//        assertEquals(expResult, result);
    }
    
    /**
     * Test of getYearsOld method, of class DateComparer.
     */
//    @Test
//    public void testGetDaysUntilExpiry() throws Exception
//    {
//        System.out.println("=== getDaysUntilExpiry ===");
//        ///////////// This test will fail tomorrow unless you change this test date!!!!
//        String date = DateComparer.ANSIToday();
//        int expResult = 0;
//        int result = DateComparer.getDaysUntilExpiry(date);
//        assertEquals(expResult, result);
//        // take ANSIToday's date and subtract a day.
//        System.out.println(">>>" + DateComparer.ANSIToday() +"<<< Andrew");
//        int days = Integer.parseInt(DateComparer.ANSIToday().substring(6, 8));
//        date = date.substring(0, 7) + String.valueOf(days - 1);
//        expResult = -1;
//        result = DateComparer.getDaysUntilExpiry(date);
//        assertEquals(expResult, result);
//        
//        date = "20180101";
//        result = DateComparer.getDaysUntilExpiry(date);
//        System.out.println(">>>days since "+date+": "+result);
//        
//    }

    /**
     * Test of ANSIToday method, of class DateComparer.
     */
    @Test
    public void testToday()
    {
        System.out.println("===today===");
        String expResult = DateComparer.ANSIToday();
        String result = DateComparer.ANSIToday();
        System.out.println("RESULT:"+result);
        assertTrue(expResult.compareTo(result) == 0);
    }
    
    /**
     * Test of dateInFuture method, of class DateComparer.
     */
//    @Test
//    public void testDateInFuture()
//    {
//        System.out.println("===dateInFuture===");
//        String expResult = "20200914";
//        String result = DateComparer.getFutureDate(1);
//        System.out.println("RESULT:"+result);
//        assertTrue(expResult.compareTo(result) == 0);
//        
//        expResult = "20190913";
//        result = DateComparer.getFutureDate(-1);
//        System.out.println("RESULT:"+result);
//        assertTrue(expResult.compareTo(result) == 0);
//        
//        expResult = "20190913";
//        result = DateComparer.getFutureDate(365);
//        System.out.println("RESULT:"+result);
//        assertTrue(expResult.compareTo(result) == 0);
//    }

    /**
     * Test of ANSIToday method, of class DateComparer.
     */
//    @Test
//    public void testANSIToday()
//    {
//        System.out.println("ANSIToday");
//        String expResult = "";
//        String result = DateComparer.ANSIToday();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getRFC1123Date method, of class DateComparer.
     */
    @Test
    public void testGetRFC1123Date()
    {
        System.out.println("ANSIToConfigDate");
        String ANSIDate = "20241217";
        // Note this could fail depending on the config date in the environment.properties file.
        // There are three notable date formats:
        // * SQL timestamp: yyyy-MM-dd HH:mm:ss
        // * PAPI timestamp: yyyy-MM-dd'T'HH:mm:ss
        // * Standard date:  yyyy-MM-dd
        // * ANSI date: yyyyMMdd
//////////// Depending on how the date format is set in config... 
//       String expResult = "2024-12-17 00:00:00";          
        String expResult = "2024-12-17";
        String result = "";
        try
        {
            result = DateComparer.ANSIToConfigDate(ANSIDate);
        } catch (ParseException ex)
        {
            System.out.println("Opps an error occured: " + ex);
        }
        assertEquals(expResult, result);
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

    /**
     * Test of getNowSQLTimeStamp method, of class DateComparer.
     */
    @Test
    public void testGetNowSQLTimeStamp()
    {
        System.out.println("===getNowSQLTimeStamp===");
        String result = DateComparer.getNowSQLTimeStamp();
        System.out.println("TIMESTAMP_NOW:>>"+result+"<<");
    }

    /**
     * Test of getDaysUntilExpiry method, of class DateComparer.
     */
    @Test
    public void testGetDaysUntilExpiry()
    {
        System.out.println("getDaysUntilExpiry");
        String ANSIExpiryDate = "20221231";

        // This will always be different; not sure how to test.
        try
        {
            System.out.println("Days until expiry: " + DateComparer.getDaysUntilExpiry(ANSIExpiryDate));
        }
        catch (ParseException ex)
        {
            assertTrue(false);
        }
        assertTrue(true);
    }

    /**
     * Test of ANSIToday method, of class DateComparer.
     */
    @Test
    public void testANSIToday()
    {
        System.out.println("ANSIToday");
        String expResult = DateComparer.ANSIToday();
        String result = DateComparer.ANSIToday();
        assertEquals(expResult, result);
    }

    /**
     * Test of ANSIToConfigDate method, of class DateComparer.
     */
    @Test
    public void testANSIToConfigDate()
    {
        System.out.println("ANSIToConfigDate");
        //
        // *** Warning this value is set in the environment properties file.
        //
        String ANSIDate = "20211231";
//        String expResult = "12-31-2021";
        // Depends on what the system time format is set to in the environment.properties.
//        String expResult = "2021-12-31T00:00:00";
        // Note this could fail depending on the config date in the environment.properties file.
        // There are three notable date formats:
        // * (Polaris) SQL timestamp: yyyy-MM-dd HH:mm:ss
        // * PAPI timestamp: yyyy-MM-dd'T'HH:mm:ss
        // * Standard date:  yyyy-MM-dd
        // * ANSI date: yyyyMMdd
        //////////// Depending on how the date format is set in config... 
//        String expResult = "2021-12-31 00:00:00";
        String expResult = "2021-12-31";
        String result;
        try
        {
            System.out.println("ANSIToConfigDate: " + DateComparer.ANSIToConfigDate(ANSIDate));
            result = DateComparer.ANSIToConfigDate(ANSIDate);
        }
        catch (ParseException ex)
        {
            result = "";
        }
        assertEquals(expResult, result);
        
        ANSIDate = "20000229";
//        String expResult = "12-31-2021";
        // Depends on what the system time format is set to in the environment.properties.
//        String expResult = "2021-12-31T00:00:00";
        // Note this could fail depending on the config date in the environment.properties file.
        // There are three notable date formats:
        // * SQL timestamp: yyyy-MM-dd HH:mm:ss
        // * PAPI timestamp: yyyy-MM-dd'T'HH:mm:ss
        // * Standard date:  yyyy-MM-dd
        // * ANSI date: yyyyMMdd
        //////////// Depending on how the date format is set in config... 
//        expResult = "2000-02-29 00:00:00";
        expResult = "2000-02-29";
        try
        {
            System.out.println("ANSIToConfigDate: " + DateComparer.ANSIToConfigDate(ANSIDate));
            result = DateComparer.ANSIToConfigDate(ANSIDate);
        }
        catch (ParseException ex)
        {
            result = "";
        }
        assertEquals(expResult, result);
    }

    /**
     * Test of getFutureDate method, of class DateComparer.
     */
    @Test
    public void testGetFutureDate()
    {
        System.out.println("getFutureDate");
        int daysFromNow = 0;
        String expResult = DateComparer.getFutureDate(0);
        String result = DateComparer.getFutureDate(daysFromNow);
        assertEquals(expResult, result);
    }

    /**
     * Test of getRFC1123Date method, of class DateComparer.
     */
    @Test
    public void testGetRFC1123Date_0args()
    {
        System.out.println("getRFC1123Date");

        System.out.println("###### LOOK HERE::: Time now +0 is >>>" + DateComparer.getRFC1123Date());
//        assertEquals(expResult, result);
        // Not sure what a meaningful test would be.
        assertTrue(true);
    }

    /**
     * Test of getRFC1123Date method, of class DateComparer.
     */
    @Test
    public void testGetRFC1123Date_String()
    {
        System.out.println("getRFC1123Date");
        LocalDate expResult = DateComparer.getRFC1123Date(DateComparer.getRFC1123Date());
        LocalDate result = DateComparer.getRFC1123Date(DateComparer.getRFC1123Date());
        System.out.println(">>>" + result.toString());
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getRFC1123Date method, of class DateComparer.
     */
    @Test
    public void testGetRFC1123Date_Long()
    {
        System.out.println("getRFC1123Date long param");
        String result = DateComparer.getRFC1123Date(2);
        System.out.println("###### LOOK HERE::: Time now +2 is >>>" + result);
        result = DateComparer.getRFC1123Date(1);
        System.out.println("###### LOOK HERE::: Time now +1 is >>>" + result);
        result = DateComparer.getRFC1123Date(0);
        System.out.println("###### LOOK HERE::: Time now  0 is >>>" + result);
        result = DateComparer.getRFC1123Date(-1);
        System.out.println("###### LOOK HERE::: Time now -1 is >>>" + result);
        result = DateComparer.getRFC1123Date(-2);
        System.out.println("###### LOOK HERE::: Time now -2 is >>>" + result);
    }

    /**
     * Test of getANSIDateFromDateTimestamp method, of class DateComparer.
     */
    @Test
    public void testGetANSIDateFromDateTimestamp()
    {
        System.out.println("getANSIDate");
        String date = "2022-07-05T17:01:14.217";
        String expResult = "20220705";
        String result = DateComparer.getANSIDateFromDateTimestamp(date);
        System.out.println("THIS IS THE RESULT: '" + result + "'");
        assertEquals(expResult, result);
        // Example from actual PAPI response
        // <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>
        // 2022-07-30T19:38:30
        date = "2022-07-30T19:38:30";
        expResult = "20220730";
        result = DateComparer.getANSIDateFromDateTimestamp(date);
        System.out.println("THIS IS THE RESULT: '" + result + "'");
        assertEquals(expResult, result);
        
        date = "2022-07-30";
        expResult = "20220730";
        result = DateComparer.getANSIDateFromDateTimestamp(date);
        System.out.println("THIS IS THE RESULT: '" + result + "'");
        assertEquals(expResult, result);
    }

    /**
     * Test of isGreaterThanMinutesOld method, of class DateComparer.
     */
    @Test
    public void testIsGreaterThanMinutesOld()
    {
        System.out.println("isGreaterThanMinutesOld");
//        int minutes = 101;
//        long fileModTime = 100L;
//        assertTrue(DateComparer.isGreaterThanMinutesOld(minutes, fileModTime));
        // TODO: Think of a better test.
        assertTrue(true);
    }

    /**
     * Test of isAnsiDate method, of class DateComparertestIsAnsiDate    @Test
    public void testIsDate()
    {
        System.out.println("isDate");
        String possibleDate = "";
        boolean expResult = false;
        boolean result = DateComparer.isAnsiDate(possibleDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}

    /**
     * Test of isAnsiDate method, of class DateComparer.
     */
    @Test
    public void testIsAnsiDate()
    {
        System.out.println("isAnsiDate");
        String possibleDate = "NEVER";
        assertFalse(DateComparer.isAnsiDate(possibleDate));
        possibleDate = "20210101";
        assertTrue(DateComparer.isAnsiDate(possibleDate));
    }
    
    /**
     * Test of isAnsiDate method, of class DateComparer.
     */
    @Test
    public void TestCmpDates()
    {
        System.out.println("cmpDates");
        String d1 = "2021-12-31T00:00:00";
        String d2 = "2021-12-31T00:00:00";
        assertTrue(DateComparer.cmpDates(d1, d2, true) == 0);
        d1 = "2022-12-31T00:00:00";
        d2 = "2021-12-31T00:00:00";
        assertTrue(DateComparer.cmpDates(d1, d2, true) > 0);
        d1 = "2021-12-31T00:00:00";
        d2 = "2022-12-31T00:00:00";
        assertTrue(DateComparer.cmpDates(d1, d2, true) < 0);
        d1 = "";
        d2 = "2022-12-31T00:00:00";
        assertTrue(DateComparer.cmpDates(d1, d2, true) == 0);
        d2 = "";
        d1 = "2022-12-31T00:00:00";
        assertTrue(DateComparer.cmpDates(d1, d2, true) == 0);
        d1 = "";
        d2 = "";
        assertTrue(DateComparer.cmpDates(d1, d2, true) == 0);
    }

    /**
     * Test of computeExpiryDate method, of class DateComparer.
     */
    @Test
    public void testComputeExpiryDate()
    {
        System.out.println("computeExpiryDate");
        String ansiCustomerDate = "20210101";
        String expResult = "20210101";
        String result = DateComparer.computeExpiryDate(ansiCustomerDate);
        assertEquals(expResult, result);
        ansiCustomerDate = "20500101";
        expResult = DateComparer.getFutureDate(MeCardPolicy.maximumExpiryDays());
        result = DateComparer.computeExpiryDate(ansiCustomerDate);
        assertEquals(expResult, result);
        ansiCustomerDate = "18980101";
        expResult = "18980101";
        result = DateComparer.computeExpiryDate(ansiCustomerDate);
        assertEquals(expResult, result);
        ansiCustomerDate = "NEVER";
        expResult = DateComparer.getFutureDate(MeCardPolicy.maximumExpiryDays());
        result = DateComparer.computeExpiryDate(ansiCustomerDate);
        assertEquals(expResult, result);
    }
}
