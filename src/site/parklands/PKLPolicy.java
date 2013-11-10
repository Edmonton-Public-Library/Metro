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
package site.parklands;

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
public class PKLPolicy //extends MeCardPolicy
{
    private List<String> nonResidentBTypes;
    private List<String> reciprocalBTypes;
    private List<String> juvenile;
    private final boolean debug;
    private final static String LOST_CARD = "LOSTCARD";
    
    
    public PKLPolicy(boolean debug)
    {
        // Allow List
//        cnib	CNIB materials allowed
//        ra	ADULT
//        st	STAFF
        // end Allow List

        this.debug = debug;
        this.nonResidentBTypes = new ArrayList<>();
        this.nonResidentBTypes.add("nr");//        nr	NON-RESIDENT
        this.nonResidentBTypes.add("tp");//        tp	TEMPORARY
        this.nonResidentBTypes.add("comp");//        comp	Computer Use ONLY
        this.nonResidentBTypes.add("il");//        il	INTER-LIBRARY LOANS
        this.nonResidentBTypes.add("inst");//        inst	INSTITUTION
        this.nonResidentBTypes.add("li");//        li	LIBRARY
        this.nonResidentBTypes.add("lis");//        lis	LIBRARY - SCHOOL
        this.nonResidentBTypes.add("ltd");//        ltd	LIMITED BORROWER
        this.nonResidentBTypes.add("xdel");//        xdel	DO NOT USE - Deletions only        
        
        this.reciprocalBTypes = new ArrayList<>();
        this.reciprocalBTypes.add("re");//        re	RECIPROCAL BORROWER
        this.reciprocalBTypes.add("tal");//        tal	TAL PATRON
        
        this.juvenile = new ArrayList<>();
        this.juvenile.add("rj");//        rj	JUVENILE
        this.juvenile.add("sp");//        sp	SPECIAL
        this.juvenile.add("stu");//        stu	STUDENT
        this.juvenile.add("stu10");//        stu10	STUDENT - 10 DAY LOAN
        this.juvenile.add("stu2");//        stu2	STUDENT - 2 WK LOAN
        this.juvenile.add("stu3");//        stu3	STUDENT -3 WK LOAN
    }

        public boolean isResident(Customer customer, String meta, StringBuilder responseError)
    {
        // 64YY        Y   00020130808    150700000000000000000500000000AOst|AA22222001047624|AETEST, 1|AQst|BZ0030|CA0100|CB0200|BLY|BHCAD|BV16.8|CC9.99|BD5 St. Anne St., St. Albert, AB, T8N 3Z9|BEktroppmann@sapl.ca|BF780-459-1537|DH1|DJTEST|PCmra|PE20130824    235900|PS20130824    235900|ZYmra|AF#You are barred from borrowing - Please refer to the circulation desk.|AY0AZ9A5C
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
//            System.out.println(PKLPolicy.class.getName() + " isResident test failed: " + ex.getMessage());
//            responseError.append(failResidencyTest);
//            return false;
//        }
//        
//        customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.TRUE);
        return true;
    }

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
//            System.out.println(PKLPolicy.class.getName() + " isReciprocal test failed: " + ex.getMessage());
//            responseError.append(failReciprocalTest);
//            return false;
//        }
//        
//        customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.FALSE);
//        responseError.append(failReciprocalTest);
        return false;
    }

        public boolean isInGoodStanding(Customer customer, String meta, StringBuilder responseError)
    {
        // because EPL uses SIP to get customer information we can assume that
        // meta will contain BARRED if the customer is not in good standing.
//        SIPCustomerMessage sipData;
//        try
//        {
//            sipData = new SIPCustomerMessage(meta);
//            String message = sipData.getMessage();
//            if (message.contains("barred")) // TODO Test with bad customers!!!.
//            {
//                customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.FALSE);
//                responseError.append(failGoodstandingTest);
//                return false;
//            }
//        }
//        catch (SIPException ex)
//        {
//            System.out.println(PKLPolicy.class.getName() + " isInGoodStanding test failed: " + ex.getMessage());
//            responseError.append(failGoodstandingTest);
//            return false;
//        }
//
//        customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.TRUE);
        return true;
    }

    
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
//            System.out.println(PKLPolicy.class.getName() + " isMinimumAge test failed: " + ex.getMessage());
//            responseError.append(failMinAgeTest);
//            return false;
//        }
//        
//        customer.set(CustomerFieldTypes.ISMINAGE, Protocol.TRUE);
        return true;
    }

    
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
