/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;

/**
 * Utility class used to help with date conversions. Serveral classes use these
 * methods and there are some things to remember:
 * <ol>
 * <li> Metro server, SIP2, Symphony all use ANSI dates ('yyyyMMdd') by default.
 * <li> Java uses 'yyyy-mm-dd' if you ask for today's date.
 * <li> Horizon uses dates in BImport, but the format is set by the user's OS, that 
 * is dates are inserted into Horizon based on what the user selected in their Windows 
 * desktop.
 * </ol>
 * @author andrew
 */
public class DateComparer
{
    public final static String CUSTOMER_DATE_FORMAT = 
            PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT)
            .getProperty(LibraryPropertyTypes.DATE_FORMAT.toString());
    public final static String ANSI_DATE_FORMAT = "yyyyMMdd";
    public final static long MILLS_IN_SECOND = 1000L;
    public final static long SECONDS_IN_MINUTE = 60L;
    public final static long MINUTES_IN_HOUR = 60L;
    public final static long HOURS_IN_DAY = 24L;
    public final static long DAYS_IN_YEAR = 365L;
    public final static long MILLISECONDS_PER_YEAR = MILLS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_YEAR;
    public final static long MILLISECONDS_PER_DAY = MILLS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY;
    public final static long MILLISECONDS_PER_MINUTE = MILLS_IN_SECOND * SECONDS_IN_MINUTE;

    /**
     * Returns the absolute difference between date one (d1) and date two (d2)
     * in years.
     * @param ANSIDate Always an ANSI date argument.
     * @return a positive integer of the number of years the customer has been alive.
     * @throws ParseException if the supplied date is not in ANSI format.
     */
    public static int getYearsOld(String ANSIDate)
            throws ParseException
    {
        DateFormat ANSIDateFormatter = new SimpleDateFormat(DateComparer.ANSI_DATE_FORMAT);
        Date daysAgo = ANSIDateFormatter.parse(ANSIDate);
        Date today = new Date();
        // the delta is in milliseconds.
        long longYears = (today.getTime() - daysAgo.getTime()) / MILLISECONDS_PER_YEAR;
        return (int) longYears;
    }

    /**
     * Computes the number of whole days until account expiry, that is, the
     * hours left in ANSIToday (if any) are not considered when computing expiry.
     * The reason is dates are passed as pure dates, which by default have a
     * start time of midnight. But when we calculate the number of days
     * milliseconds as of now, so April 10 (midnight) - April 9 (10:15 AM) is
     * less than one day, so days until expiry will report 0.
     *
     * @param ANSIExpiryDate date of customer privilege expiry.
     * @return integer of number of dates until expiry. Could be negative.
     * @throws ParseException
     */
    public static int getDaysUntilExpiry(String ANSIExpiryDate)
            throws ParseException
    {
        DateFormat ANSIDateFormatter = new SimpleDateFormat(DateComparer.ANSI_DATE_FORMAT);
        Date expiryDate  = ANSIDateFormatter.parse(ANSIExpiryDate);
        Date today = new Date();
        // the delta is in milliseconds.
        long daysLeft = (expiryDate.getTime() - today.getTime()) / MILLISECONDS_PER_DAY;
        return (int) daysLeft;
    }

    /**
     * Returns ANSIToday's date as an ANSI date ('yyyymmdd' String).
     *
     * @return String of 'yyyymmdd'.
     */
    public final static String ANSIToday()
    {
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateComparer.ANSI_DATE_FORMAT);
        return dateFormat.format(today);
    }
    
    /**
     * Used by clients that need dates formatted for their clients.
     * @param ANSIDate 'yyyymmdd'
     * @return The date converted to the format specified in the environment.properties file.
     * @throws ParseException if ANSI date not provided.
     */
    public final static String ANSIToConfigDate(String ANSIDate)
            throws ParseException
    {
        SimpleDateFormat sdfSrc = new SimpleDateFormat(DateComparer.ANSI_DATE_FORMAT);
        Date myDate = sdfSrc.parse(ANSIDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(CUSTOMER_DATE_FORMAT);
        return dateFormat.format(myDate);
    }

    /**
     * Given the argument number of days in the future, this method will return
     * the ANSI date of that day.
     * @param daysFromNow number of days in the future as an integer minimum 0.
     * @return ANSI date in the future.
     */
    public static String getFutureDate(int daysFromNow)
    {
        if (daysFromNow < 1)
        {
            return DateComparer.ANSIToday();
        }
        Date today = new Date();
        // the delta is in milliseconds.
        long daysInfuture = today.getTime() + (daysFromNow * MILLISECONDS_PER_DAY);
        Date futureDate = new Date(daysInfuture);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateComparer.ANSI_DATE_FORMAT);
        return dateFormat.format(futureDate);
    }
    
    /**
     * Computes if the long file modification time passed as argument two is older than
     * argument 1 modification time in minutes.
     * @param minutes time span of acceptable file age.
     * @param fileModTime last modified time of a file in seconds since Jan 01, 1970.
     * @return true if the fileModTime is greater than the argument minutes and 
     * false otherwise.
     */
    public static boolean isGreaterThanMinutesOld(int minutes, long fileModTime)
    {
        long difference = new Date().getTime() - fileModTime;

        if (difference < minutes * MILLISECONDS_PER_MINUTE) 
        {
            return true;
        }
        return false;
    }
}
