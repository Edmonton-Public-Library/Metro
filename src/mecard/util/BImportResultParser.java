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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mecard.requestbuilder.BImportRequestBuilder;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class BImportResultParser
{
    private int successfulCustomers;
    private int failedCustomers;
    private final Pattern resultPattern;
    private final Pattern cardPattern;
    private List<String> loadFailedCustomers;
    
    public BImportResultParser(String results, String reportDirPath)
    {
//record line   action key(s)
//------ ------ ------ ---------------------------------------------------------
//     1      1 modify 23877000204705
//               failed: DbxInsertRow failed: Database Error|Integrity trigger failed:
//record line   action key(s)
//------ ------ ------ ---------------------------------------------------------
//     1      1 modify 21221005573552 <ok>
//
//statistics: 
//  record           1
//  modify           1
        this.successfulCustomers = 0;
        this.failedCustomers     = 0;
        this.resultPattern       = Pattern.compile("^\\d{1,}\\s+\\d{1,}.*");
        this.cardPattern         = Pattern.compile("\\d{2,}");
        this.loadFailedCustomers = new ArrayList<>();
        List<String> allLines = new ArrayList<>();
        allLines.addAll(Arrays.asList(results.split("\n")));
        for (String line: allLines)
        {
            line = line.trim();
            Matcher matcher = this.resultPattern.matcher(line);
            if (matcher.find())
            {
                if (line.endsWith(BImportRequestBuilder.SUCCESS_MARKER.toString()))
                {
                    this.successfulCustomers++;
                }
                else
                {
                    String userId = "99999012345678";
                    if (getUserId(line).isEmpty() == false)
                    {
                        userId = getUserId(line);
                    }
                    this.loadFailedCustomers.add(userId);
                    System.out.println("load fail:"+userId);
                    this.failedCustomers++;
                }
            }
        }
        int total = (this.failedCustomers + this.successfulCustomers);
        System.out.println("Customer loads attempted: " + total);
        System.out.println("success: " + this.successfulCustomers 
                + " fail: " + this.failedCustomers);
    }
    
    
    
    /**
     * 
     * @param resultString a line from the bimport results stdout that is fairly
     * clear should contain a user id ie, '     1      1 modify 21221005573552 <ok>'.
     * @return the user's id or an empty string if the id could not be found.
     */
    protected final String getUserId(String resultString)
    {
        Matcher matcher = this.cardPattern.matcher(resultString);
        if (matcher.find())
        {
            String card = matcher.group();
            return matcher.group();
        }
        return "";
    }
    
    /**
     * 
     * @return the number of successful customer loads.
     */
    public int getSuccessfulCustomers()
    {
        return successfulCustomers;
    }

    /**
     * 
     * @return the number of failed customer loads.
     */
    public int getFailedCustomers()
    {
        return failedCustomers;
    }

    /**
     * 
     * @return List of failed customer ids.
     */
    public List<String> getFailedCustomerKeys()
    {
        return this.loadFailedCustomers;
    }
}
