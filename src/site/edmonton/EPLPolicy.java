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
package site.edmonton;

import mecard.Protocol;
import mecard.customer.Customer;
import mecard.config.CustomerFieldTypes;
import site.MeCardPolicy;

/**
 * This class needs to be sub-classed by all libraries. All customer's must meet 
 * the defined MeCard policy rules. They must be resident, not reciprocal, must
 * be of minimum age of 18, must have an email address, must be in good standing
 * with their home library, must have all mandatory account information present
 * and valid (within reason), and all must have a valid membership expiry date.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class EPLPolicy extends MeCardPolicy
{
    private final static String EPL_RECIPROCAL = "EPL-RECIP";
    private final static String EPL_VISITOR    = "EPL-VISITR"; // Non resident
    private final static String[] JUV_PROFILE  = {"EPL-JUV","EPL-JUV01","EPL-JUV05","EPL-JUV10","EPL-JUVNR","EPL-JUVGR","EPL-JUVIND"};
    private final static String INVALID_CONDITION = "BLOCKED";
    private final boolean debug;
    private final static String LOST_CARD = "LOSTCARD";
    
    public EPLPolicy()
    {
        this.debug = false;
    }
    
    public EPLPolicy(boolean debug)
    {
        this.debug = debug;
    }

    @Override
    public boolean isResident(Customer customer, String meta, StringBuilder s)
    {
        if (meta.contains(EPL_VISITOR))
        {
            customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.FALSE);
            if (debug) System.out.println("failed resident test");
            s.append(failResidencyTest);
            return false;
        }
        customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.TRUE);
        return true;
    }

    @Override
    public boolean isReciprocal(Customer customer, String meta, StringBuilder s)
    {
        if (meta.contains(EPL_RECIPROCAL))
        {
            customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.TRUE);
            if (debug) System.out.println("failed reciprocal test");
            s.append(failReciprocalTest);
            return true;
        }
        customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.FALSE); 
        return false;
    }

    @Override
    public boolean isInGoodStanding(Customer customer, String meta, StringBuilder s)
    {
        // because EPL uses SIP to get customer information we can assume that
        // meta will contain BARRED if the customer is not in good standing.
        // TODO: test against dumpflatuser data to see if this assumption holds.
        if (meta.contains(INVALID_CONDITION))
        {
            customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.FALSE);
            if (debug) System.out.println("failed good-standing test");
            s.append(failGoodstandingTest);
            return false;
        }
        customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.TRUE);
        return true;
    }

    @Override
    public boolean isMinimumAge(Customer customer, String meta, StringBuilder s)
    {
        // run through all the juv profile types and if one matches then
        // no can do.
        for (int i = 0; i < JUV_PROFILE.length; i++)
        {
            if (meta.contains(JUV_PROFILE[i]))
            {
                customer.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
                if (debug) System.out.println("failed minimum age test");
                s.append(failMinAgeTest);
                return false;
            }
        }
        customer.set(CustomerFieldTypes.ISMINAGE, Protocol.TRUE);
        return true;
    }

    @Override
    public boolean isLostCard(Customer customer, String meta, StringBuilder s)
    {
        if (meta.contains(LOST_CARD))
        {
            customer.set(CustomerFieldTypes.ISLOSTCARD, Protocol.TRUE);
            if (debug) System.out.println("passed lost card test");
            s.append(failLostCardTest);
            return true;
        }
        customer.set(CustomerFieldTypes.ISLOSTCARD, Protocol.FALSE);
        return false;
    }

}
