/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2019  Edmonton Public Library
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.Protocol;
import mecard.Policies;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;

/**
 * Utility class used to help with date conversions. Several classes use these
 * methods and there are some things to remember:
 * <ol>
 * <li> Metro server, SIP2, Symphony all use ANSI dates ('yyyyMMdd') by default.
 * <li> Java uses 'yyyy-mm-dd' if you ask for today's date.
 * <li> Horizon uses dates in BImport, but the format is set by the user's OS, that 
 * is dates are inserted into Horizon based on what the user selected in their Windows 
 * desktop.
 * </ol>
 * @author Andrew Nisbet andrew.nisbet@epl.ca
 */
public class DateComparer
{
    public final static String CUSTOMER_DATE_FORMAT = 
            PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT)
            .getProperty(LibraryPropertyTypes.DATE_FORMAT.toString());
    public final static String ANSI_DATE_FORMAT = "yyyyMMdd";
    public final static String RFC1123_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
    // hard coded here instead of environment properties because JDBC java.sql.Timestamp 
    // requires this format in its constructor.
    // http://technet.microsoft.com/en-us/library/ms378878(v=sql.110).aspx
    // http://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html
    public final static String SQL_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    // We can set it in environment properties, but don't because that date is for
    // loading and timestamps for ILS, but this needs to be actual yyyy-MM-dd'T'HH:mm:ss"
//    public final static String SQL_TIMESTAMP_FORMAT = CUSTOMER_DATE_FORMAT;
    
    /**
     * GMT time zone - all HTTP dates are on GMT
     */
    public final static TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");
    public final static Locale LOCALE_US = Locale.US;
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
     * Use HTTP Date format (RFC1123)
     * ddd, dd MMM yyyy HH:mm:ss GMT
     * Example:
     * Wed, 17 Oct 2012 22:23:32 GMT
     * 
     * PolarisDate: ddd, dd MMM yyyy HH:mm:ss GMT
     *              EEE, dd MMM yyyy HH:mm:ss zzz
     * PolarisDate: Wed, 17 Oct 2012 22:23:32 GMT
     * @return date string of time now.
     */
    public static String getRFC1123Date()
    {
        return DateComparer.getRFC1123Date(0);
    }
    
    /**
     * Computes the RFC1123 (Polaris date) minus a given number of hours.
     * This is provided to compensate for hashes that are computed from different
     * timezones to the destination's timezone.
     * For example:
     * Wed, 17 Oct 2022 21:23:32 GMT and a setting of (+)2.0, will return
     * Wed, 17 Oct 2022 19:23:32 GMT 
     * 
     * @param timezoneDeltaHours EDT is +2 from MDT. PDT is -1 from MDT.
     * @return date string of time now, minus the timezone delta.
     */
    public static String getRFC1123Date(int timezoneDeltaHours)
    {
        Date today = new Date();
        int hours = 3600 * 1000;
        Date updatedToday = new Date(today.getTime() + timezoneDeltaHours * hours);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateComparer.RFC1123_DATE_FORMAT);
        dateFormat.setTimeZone(GMT_ZONE);
        return dateFormat.format(updatedToday);
    }
    
    /**
     * Takes a given date and returns the RFC1123 date format (AKA Polaris date)
     * ddd, dd MMM yyyy HH:mm:ss GMT
     * Example:
     * Wed, 17 Oct 2012 22:23:32 GMT
     * @param date
     * @return LocalDate object.
     */
    public static LocalDate getRFC1123Date(String date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }
    
    /**
     * Converts a timestamp to an ANSI date.
     * @param timeStampString in 'yyyy-MM-dd'T'HH:mm:ss' format.
     * @return ANSI format date 'yyyyMMdd', or empty string if there was an error
     * parsing the supplied date.
     */
    public static String getANSIDate(String timeStampString)
    {
        if (timeStampString == null || timeStampString.isEmpty() || timeStampString.equals(Protocol.DEFAULT_FIELD_VALUE))
        {
                return "";
        }
        try
        {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = sdf.parse(timeStampString);
            return new SimpleDateFormat("yyyyMMdd").format(date);
        } catch (ParseException ex)
        {
            Logger.getLogger(DateComparer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
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

        return difference < minutes * MILLISECONDS_PER_MINUTE;
    }

    /**
     * s - timestamp in format read from environment.properties and recommended
     * to be 'yyyy-[m]m-[d]d hh:mm:ss' for SQL based transactions because the 
     * java.sql.Timestamp object's constructor takes a String and parses it,
     * the expectation is that it comes in the format of 'yyyy-[m]m-[d]d hh:mm:ss[.f...]'.
     * The fractional seconds may be omitted. The leading zero for mm and dd 
     * may also be omitted.
     * @return TimeStamp SQL in format: yyyy-[m]m-[d]d hh:mm:ss[.f...]
     */
    public static String getNowSQLTimeStamp()
    {
        // s - timestamp in format yyyy-[m]m-[d]d hh:mm:ss[.f...]. The fractional 
        // seconds may be omitted. The leading zero for mm and dd may also be omitted.
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateComparer.SQL_TIMESTAMP_FORMAT, LOCALE_US);
        return dateFormat.format(today);
    }

    /**
     * This method accepts a possible dateField and returns a clean date.
     * @param possibleDate example: '20131231    235900STAFF' worst case.
     * @return cleaned string if date valid and parse-able as date and Protocol#DEFAULT_FIELD_VALUE
     * otherwise.
     */
    public static String cleanAnsiDateTime(String possibleDate)
    {
        if (possibleDate == null)
        {
            return Protocol.DEFAULT_FIELD_VALUE;
        }
        String[] split = possibleDate.split("\\s{1,}");
        if (split.length == 0 || isAnsiDate(split[0]) == false)
        {
            return Protocol.DEFAULT_FIELD_VALUE;
        }
        return split[0];
    }

    /**
     * Tests if a string looks like a possible date. The check is not strict -
     * a string of 8 digits.
     * @param possibleDate string value. To bass must be 8 single digits like '20130822'.
     * @return true if the string is likely to be an ANSI date and false otherwise.
     */
    public static boolean isAnsiDate(String possibleDate)
    {
        if (possibleDate == null || possibleDate.isEmpty())
        {
            return false;
        }
        return possibleDate.matches("^[1-2][0-9]\\d{2}[0-1][0-9][0-3][0-9]$");
    }
    
    /**
     * Convenience method that takes the customer's expiry date in ANSI format
     * and determines if it is beyond the maximum expiry days set forth in the 
     * {@link Policies} class.
     * 
     * @param ansiCustomerDate date of customer expiry in ANSI (YYYMMDD) format.
     * @return the computed customer expiry date or the ME Libraries policy
     * maximum days or the home library's preferred expiry or which ever is smaller.
     */
    public static String computeExpiryDate(String ansiCustomerDate)
    {
        if (DateComparer.isAnsiDate(ansiCustomerDate))
        {
            // see if this date is more than the max expiry days.
            try
            {
                if (DateComparer.getDaysUntilExpiry(ansiCustomerDate) > Policies.maximumExpiryDays())
                {
                    return DateComparer.getFutureDate(Policies.maximumExpiryDays());
                }
                // else it is less, return the home library's preferred expiry.
                return ansiCustomerDate;
            }
            catch (ParseException ex)
            {
                // Sometimes Symphony will return 'NEVER' which won't parse 
                // so just send back the max days.
                return DateComparer.getFutureDate(Policies.maximumExpiryDays());
            }
        }
        
        return DateComparer.getFutureDate(Policies.maximumExpiryDays());
    }
    
    /**
     * Takes two dates (in standard system format) and compares them.If date1 
     * is less than date2 the value is less than zero, if date1 is 
     * greater than date2 a positive integer is returned, and if the two dates 
     * are equal or the strings could not be parsed 0 is returned.
     * 
     * @param date1 first date as system type.
     * @param date2 second date as system type.
     * @return integer value greater, less than, or equal to 0.
     */
    public static int cmpDates(String date1, String date2)
    {
        return DateComparer.cmpDates(date1, date2, false);
    }
    
    /**
     * Takes two dates (in standard system format) and compares them.If date1 
     * is less than date2 the value is less than zero, if date1 is 
     * greater than date2 a positive integer is returned, and if the two dates 
     * are equal or the strings could not be parsed 0 is returned.
     * 
     * @param date1 first date as system type.
     * @param date2 second date as system type.
     * @param debug true will print the dates that failed to parse and nothing otherwise.
     * @return integer value greater, less than, or equal to 0.
     */
    public static int cmpDates(String date1, String date2, boolean debug)
    {
        try
        {
            LocalDateTime dateTime1 = LocalDateTime.parse(date1);
            LocalDateTime dateTime2 = LocalDateTime.parse(date2);
            return dateTime1.compareTo(dateTime2);
        } 
        catch (DateTimeParseException ex)
        {
            if (debug)
            {
                System.out.println("**error, one or both dates could not be parsed.\n'"
                    + date1 + "', and d2: '" + date2 + "'");
            }
            return 0;
        }
    }
}
