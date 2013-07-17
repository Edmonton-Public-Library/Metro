/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Andrew Nisbet
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
import mecard.MetroService;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;

/**
 *
 * @author andrew
 */
public class DateComparer
{
    public final static String CUSTOMER_DATE_FORMAT = 
            MetroService.getProperties(ConfigFileTypes.ENVIRONMENT)
            .getProperty(LibraryPropertyTypes.DATE_FORMAT.toString());//"yyyy-MM-dd";
    public final static String ANSI_DATE_FORMAT = "yyyyMMdd";
    public final static long MILLS_IN_SECOND = 1000L;
    public final static long SECONDS_IN_MINUTE = 60L;
    public final static long MINUTES_IN_HOUR = 60L;
    public final static long HOURS_IN_DAY = 24L;
    public final static long DAYS_IN_YEAR = 365L;
    public final static long MILLISECONDS_PER_YEAR = MILLS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_YEAR;
    public final static long MILLISECONDS_PER_DAY = MILLS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY;

    /**
     * Returns the absolute difference between date one (d1) and date two (d2)
     * in years.
     * @param date
     * @return a positive integer of the number of years
     * @throws ParseException if the supplied date doesn't match the ValidDate
     * date format which is yyyy-mm-dd.
     */
    public static int getYearsOld(String date) throws ParseException
    {
        if (date.length() == 0)
        {
            return -1;
        }
        String newDate = date;
        if (newDate.contains("-") == false)
        {
            newDate = DateComparer.ANSIToHumanReadable(date);
        }
        DateFormat dateFormat = new SimpleDateFormat(DateComparer.CUSTOMER_DATE_FORMAT);
        Date daysAgo = dateFormat.parse(newDate);
        Date today = new Date();
        // the delta is in milliseconds.
        long longYears = (today.getTime() - daysAgo.getTime()) / MILLISECONDS_PER_YEAR;
        return (int) longYears;
    }

    /**
     * Computes the number of whole days until account expiry, that is, the
     * hours left in today (if any) are not considered when computing expiry.
     * The reason is dates are passed as pure dates, which by default have a
     * start time of midnight. But when we calculate the number of days
     * milliseconds as of now, so April 10 (midnight) - April 9 (10:15 AM) is
     * less than one day, so days until expiry will report 0.
     *
     * @param date
     * @return integer of number of dates until expiry. Could be negative.
     * @throws ParseException
     */
    public static int getDaysUntilExpiry(String date) throws ParseException
    {
        DateFormat dateFormat = new SimpleDateFormat(DateComparer.CUSTOMER_DATE_FORMAT);
        String newDate = date;
        if (newDate.contains("-") == false)
        {
            newDate = DateComparer.ANSIToHumanReadable(date);
        }
        Date daysFromNow = dateFormat.parse(newDate);
        Date today = new Date();
        // the delta is in milliseconds.
        long daysLeft = (daysFromNow.getTime() - today.getTime()) / MILLISECONDS_PER_DAY;
        return (int) daysLeft;
    }

    /**
     * Returns today's date as an ANSI date ('yyyymmdd' String).
     *
     * @return String of 'yyyymmdd'.
     */
    public final static String today()
    {
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateComparer.ANSI_DATE_FORMAT);
        return dateFormat.format(today);
    }
    
    public final static String convertToConfigDate(String ANSIDate)
    {
        try
        {
            SimpleDateFormat sdfSrc = new SimpleDateFormat(DateComparer.ANSI_DATE_FORMAT);
            Date myDate = sdfSrc.parse(ANSIDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat(CUSTOMER_DATE_FORMAT);
            return dateFormat.format(myDate);
        } catch (ParseException ex)
        {
            return ANSIDate;
        }
    }

    /**
     * Removes '-' from 'yyyy-mm-dd' date strings.
     *
     * @param date in form of 'yyyy-mm-dd'
     * @return Transforms to a yyyymmdd date, returning an ANSI date.
     */
    public final static String formatDate(String date)
    {
        return date.replace("-", "");
    }

    public final static String ANSIToHumanReadable(String date)
    {
        StringBuilder sb = new StringBuilder();
        if (date.matches("\\d{8}"))
        {
            // expectes '20130521' so will return
            sb.append(date.substring(0, 4));
            sb.append("-");
            sb.append(date.substring(4, 6));
            sb.append("-");
            sb.append(date.substring(6, 8));
        }
        return sb.toString();
    }
}
