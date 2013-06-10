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

package epl;

import mecard.Protocol;
import mecard.customer.Customer;
import mecard.customer.CustomerFieldTypes;
import site.mecard.MeCardPolicy;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class EPLMeCardPolicy extends MeCardPolicy
{
    public final static String EPL_RECIPROCAL = "EPL-RECIP";
    public final static String EPL_VISITOR    = "EPL-VISITR"; // Non resident
    // TODO: Fix to read from a unstructured config file.
    public final static String[] JUV_PROFILE  = {"EPL-JUV","EPL-JUV01","EPL-JUV05","EPL-JUV10","EPL-JUVNR","EPL-JUVGR","EPL-JUVIND"};
    public final static String INVALID_CONDITION = "BLOCKED";
    private final boolean debug;
    
    public EPLMeCardPolicy()
    {
        this.debug = false;
    }
    
    public EPLMeCardPolicy(boolean debug)
    {
        this.debug = debug;
    }

    @Override
    public boolean isResident(Customer customer, String meta)
    {
        if (meta.contains(EPL_VISITOR))
        {
            customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.FALSE);
            if (debug) System.out.println("failed resident test");
            return false;
        }
        customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.TRUE);
        return true;
    }

    @Override
    public boolean isReciprocal(Customer customer, String meta)
    {
        if (meta.contains(EPL_RECIPROCAL))
        {
            customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.TRUE);
            if (debug) System.out.println("failed reciprocal test");
            return true;
        }
        customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.FALSE); 
        return false;
    }

    @Override
    public boolean isInGoodStanding(Customer customer, String meta)
    {
        // because EPL uses SIP to get customer information we can assume that
        // meta will contain BARRED if the customer is not in good standing.
        // TODO: test against dumpflatuser data to see if this assumption holds.
        if (meta.contains(INVALID_CONDITION))
        {
            customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.FALSE);
            if (debug) System.out.println("failed good-standing test");
            return false;
        }
        customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.TRUE);
        return true;
    }

    @Override
    public boolean isMinimumAge(Customer customer, String meta)
    {
        // run through all the juv profile types and if one matches then
        // no can do.
        for (int i = 0; i < JUV_PROFILE.length; i++)
        {
            if (meta.contains(JUV_PROFILE[i]))
            {
                customer.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
                if (debug) System.out.println("failed minimum age test");
                return false;
            }
        }
        customer.set(CustomerFieldTypes.ISMINAGE, Protocol.TRUE);
        return true;
    }

}
