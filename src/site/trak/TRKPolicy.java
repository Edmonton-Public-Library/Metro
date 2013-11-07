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
package site.trak;

import api.SIPCustomerMessage;
import java.util.ArrayList;
import java.util.List;
import mecard.Protocol;
import mecard.customer.Customer;
import mecard.config.CustomerFieldTypes;
import mecard.exception.SIPException;
import site.MeCardPolicy;

/**
 * This class needs to be sub-classed by all libraries. All customer's must meet 
 * the defined MeCard policy rules. They must be resident, not reciprocal, must
 * be of minimum age of 18, must have an email address, must be in good standing
 * with their home library, must have all mandatory account information present
 * and valid (within reason), and all must have a valid membership expiry date.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class TRKPolicy extends MeCardPolicy
{
    private List<String> nonResidentBTypes;
    private List<String> reciprocalBTypes;
    private List<String> juvenile;
    private final boolean debug;
    private final static String LOST_CARD = "LOSTCARD";
    
    
    public TRKPolicy(boolean debug)
    {
        this.debug = debug;
        this.nonResidentBTypes = new ArrayList<>();
//        this.nonResidentBTypes.add("ill");    // INTERNAL LIBRARY LOANS
        
        this.reciprocalBTypes = new ArrayList<>();
//        this.reciprocalBTypes.add("recip");   // RECIPROCAL MEMBERSHIP
        
        this.juvenile = new ArrayList<>();
//        this.juvenile.add("nrj");            // NON-RESIDENT JUNIOR
        
    }

    @Override
    public boolean isResident(Customer customer, String meta, StringBuilder responseError)
    {
        //        recv:64              00120131107    134857000000000000000000000000AO203|AA29335002291042|AEHunting, Will|BZ0025|CA0010|CB0100|BLY|CQY|BHCAD|BV0.00|CC10.00|BD433 King Street, Spruce Grove, AB T7X 3B4|BEsthero@yrl.ab.ca|BC|PA17|PEASGY|PSAdult (18-64)|U4(none)|U5|PZT7X 3B4|PX20201025    235959|PYN|AFPatron status is ok.|AGPatron status is ok.|AY1AZ9FF9
        // we can use a SIPMessage object to parse the meta...
//        SIPCustomerMessage sipData;
//        try
//        {
//            sipData = new SIPCustomerMessage(meta);
//            String bType = sipData.getCustomerProfile();
//            for (String s: nonResidentBTypes)
//            {
//                if (bType.compareTo(s) == 0) // if we match on a non resident bType we aren't a resident.
//                {
//                    customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.FALSE);
//                    responseError.append(failResidencyTest);
//                    return false;
//                }
//            }
//        }
//        catch (SIPException ex)
//        {
//            System.out.println(TRKPolicy.class.getName() + " isResident test failed: " + ex.getMessage());
//            responseError.append(failResidencyTest);
//            return false;
//        }
//        
//        customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.TRUE);
        return true;
    }

    @Override
    public boolean isReciprocal(Customer customer, String meta, StringBuilder responseError)
    {
//        SIPCustomerMessage sipData;
//        try
//        {
//            sipData = new SIPCustomerMessage(meta);
//            String bType = sipData.getCustomerProfile();
//            for (String s: this.reciprocalBTypes)
//            {
//                if (bType.compareTo(s) == 0) // if we match on a non resident bType we aren't a resident.
//                {
//                    customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.TRUE);
//                    return true;
//                }
//            }
//        }
//        catch (SIPException ex)
//        {
//            System.out.println(TRKPolicy.class.getName() + " isReciprocal test failed: " + ex.getMessage());
//            responseError.append(failReciprocalTest);
//            return false;
//        }
//        
//        customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.FALSE);
//        responseError.append(failReciprocalTest);
        return false;
    }

    @Override
    public boolean isInGoodStanding(Customer customer, String meta, StringBuilder responseError)
    {
        // because EPL uses SIP to get customer information we can assume that
        // meta will contain BARRED if the customer is not in good standing.
//        SIPCustomerMessage sipData;
//        try
//        {
//            sipData = new SIPCustomerMessage(meta);
//            String message = sipData.getMessage();
//            if (message.contains("barred")) // if we match on a non resident bType we aren't a resident.
//            {
//                customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.FALSE);
//                responseError.append(failGoodstandingTest);
//                return false;
//            }
//        }
//        catch (SIPException ex)
//        {
//            System.out.println(TRKPolicy.class.getName() + " isInGoodStanding test failed: " + ex.getMessage());
//            responseError.append(failGoodstandingTest);
//            return false;
//        }
//
//        customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.TRUE);
        return true;
    }

    @Override
    public boolean isMinimumAge(Customer customer, String meta, StringBuilder responseError)
    {
        // run through all the juv profile types and if one matches then
        // no can do.
//        SIPCustomerMessage sipData;
//        try
//        {
//            sipData = new SIPCustomerMessage(meta);
//            String bType = sipData.getCustomerProfile();
//            for (String s: this.juvenile)
//            {
//                if (bType.compareTo(s) == 0) // if we match juv bType we are a minor.
//                {
//                    customer.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
//                    responseError.append(failMinAgeTest);
//                    return false;
//                }
//            }
//        }
//        catch (SIPException ex)
//        {
//            System.out.println(TRKPolicy.class.getName() + " isMinimumAge test failed: " + ex.getMessage());
//            responseError.append(failMinAgeTest);
//            return false;
//        }
//        
//        customer.set(CustomerFieldTypes.ISMINAGE, Protocol.TRUE);
        return true;
    }

    @Override
    public boolean isLostCard(Customer customer, String meta, StringBuilder responseError)
    {
//        if (meta.contains(LOST_CARD))
//        {
//            customer.set(CustomerFieldTypes.ISLOSTCARD, Protocol.TRUE);
//            if (debug) System.out.println("passed lost card test");
//            return true;
//        }
//        customer.set(CustomerFieldTypes.ISLOSTCARD, Protocol.FALSE);
//        responseError.append(failLostCardTest);
        return false;
    }

}
