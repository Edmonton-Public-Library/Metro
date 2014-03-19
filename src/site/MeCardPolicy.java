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
package site;

import api.CustomerMessage;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import mecard.Protocol;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.customer.Customer;
import mecard.config.CustomerFieldTypes;
import mecard.config.MessagesConfigTypes;
import mecard.util.DateComparer;
import mecard.config.PropertyReader;
import mecard.util.Text;



/**
 * This class needs to be sub-classed by all libraries. All customer's must meet 
 * the defined MeCard policy rules. They must be resident, not reciprocal, must
 * be of minimum age of 18, must have an email address, must be in good standing
 * with their home library, must have all mandatory account information present
 * and valid (within reason), and all must have a valid membership expiry date.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class MeCardPolicy
{

    public final static int MINIMUM_YEARS_OF_AGE = 18;
    public final static int MINIMUM_EXPIRY_DAYS = 1;
    public final static int MAXIMUM_EXPIRY_DAYS = 365;
    protected static String failMinAgeTest;
    protected static String failLostCardTest;
    protected static String failGoodstandingTest;
    protected static String failReciprocalTest;
    protected static String failResidencyTest;
    protected static String failEmailTest;
    protected static String failExpiryTest;
    protected static String failCompletenessTest;
    protected static boolean DEBUG;
    
    protected List<String> nonResidentTypes;
    protected List<String> reciprocalTypes;
    protected List<String> juvenileTypes;
    protected List<String> notInGoodStandingStandingSentinal;
    protected List<String> lostCardSentinals;
    private static MeCardPolicy mePolicy;
    
    private MeCardPolicy()
    {
        this.nonResidentTypes         = new ArrayList<>();
        this.reciprocalTypes          = new ArrayList<>();
        this.juvenileTypes            = new ArrayList<>();
        this.notInGoodStandingStandingSentinal = new ArrayList<>();
        this.lostCardSentinals        = new ArrayList<>();
        
        Properties messageProps     = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        failMinAgeTest       = messageProps.getProperty(MessagesConfigTypes.FAIL_MIN_AGE_TEST.toString());
        failLostCardTest     = messageProps.getProperty(MessagesConfigTypes.FAIL_LOSTCARD_TEST.toString());
        failGoodstandingTest = messageProps.getProperty(MessagesConfigTypes.FAIL_GOODSTANDING_TEST.toString());
        failReciprocalTest   = messageProps.getProperty(MessagesConfigTypes.FAIL_RECIPROCAL_TEST.toString());
        failResidencyTest    = messageProps.getProperty(MessagesConfigTypes.FAIL_RESIDENCY_TEST.toString());
        failEmailTest        = messageProps.getProperty(MessagesConfigTypes.FAIL_EMAIL_TEST.toString());
        failExpiryTest       = messageProps.getProperty(MessagesConfigTypes.FAIL_EXPIRY_TEST.toString());
        failCompletenessTest = messageProps.getProperty(MessagesConfigTypes.FAIL_COMPLETENESS_TEST.toString());

        Properties props     = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        // TODO: If we find a reciprocal.properties, or non_resident.properties, or juvenile.properties
        // load the types for testing.
        // read optional fields from environment. Should be ',' separated.
        // <entry key="reciprocal">EPL-RECIP</entry>
        // <entry key="non-resident">NON-RES</entry>
        // <entry key="juvenile">re,sp, stu, stu10, stu2, stu3</entry>
        PropertyReader.loadDelimitedEntry(props, LibraryPropertyTypes.NON_RESIDENT_TYPES, nonResidentTypes);
        PropertyReader.loadDelimitedEntry(props, LibraryPropertyTypes.RECIPROCAL_TYPES, reciprocalTypes);
        PropertyReader.loadDelimitedEntry(props, LibraryPropertyTypes.JUVENILE_TYPES, juvenileTypes);
        PropertyReader.loadDelimitedEntry(props, LibraryPropertyTypes.LOST_CARD_SENTINEL, lostCardSentinals);
        PropertyReader.loadDelimitedEntry(props, LibraryPropertyTypes.CUSTOMER_STANDING_SENTINEL, notInGoodStandingStandingSentinal);
    }

    public static MeCardPolicy getInstanceOf(boolean debug)
    {
        if (mePolicy == null)
        {
            mePolicy = new MeCardPolicy();
        }
        DEBUG = debug;
        return mePolicy;
    }
    
    /**
     * Standardizes the field data for transmission to other libraries. This 
     * ensures that all libraries get proper case information.
     * @param customer 
     */
    public void normalizeCustomerFields(Customer customer)
    {
        String customerData = Text.toDisplayCase(customer.get(CustomerFieldTypes.FIRSTNAME));
        customer.set(CustomerFieldTypes.FIRSTNAME, customerData);
        customerData = Text.toDisplayCase(customer.get(CustomerFieldTypes.LASTNAME));
        customer.set(CustomerFieldTypes.LASTNAME, customerData);
        customerData = Text.toDisplayCase(customer.get(CustomerFieldTypes.STREET));
        customer.set(CustomerFieldTypes.STREET, customerData);
        customerData = customer.get(CustomerFieldTypes.POSTALCODE).toUpperCase();
        customer.set(CustomerFieldTypes.POSTALCODE, customerData);
    }

    /**
     * Each library must decide how they compute if a customer is a resident
     * customer. Usually this is done by comparing the btype of profile of the
     * customer.
     * 
     * @param customer
     * @param message CustomerMessage any extra data about the customer account like a SIP response.
     * @param s the return message if the customer failed this test.
     * @return true if the customer is resident to their home library
     * and false otherwise.
     */
    
    public boolean isResident(Customer customer, CustomerMessage message, StringBuilder s)
    {
        String customerType = message.getCustomerProfile();
        for (String str: nonResidentTypes)
        {
            if (customerType.compareTo(str) == 0) // if we match on a non resident bType we aren't a resident.
            {
                customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.FALSE);
                s.append(failResidencyTest);
                return false;
            }
        }
        customer.set(CustomerFieldTypes.ISRESIDENT, Protocol.TRUE);
        return true;
    }

    /**
     * Each library must decide how they compute if a customer is a reciprocal
     * customer. Usually this is done by comparing the btype of profile of the
     * customer.
     * 
     * @param customer
     * @param meta any extra data about the customer account like a SIP response.
     * @param s the return message if the customer failed this test.
     * @return true if the customer is a reciprocal member at their home library
     * and false otherwise.
     */
    public boolean isReciprocal(Customer customer, CustomerMessage meta, StringBuilder s)
    {
        String profileType = meta.getCustomerProfile();
        for (String reciprocalProfileName: reciprocalTypes)
        {
            if (profileType.compareTo(reciprocalProfileName) == 0) // if we match on a non resident bType we aren't a resident.
            {
                customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.TRUE);
                s.append(failReciprocalTest);
                return true;
            }
        }
        customer.set(CustomerFieldTypes.ISRECIPROCAL, Protocol.FALSE);
        return false;
    }

    /**
     * Tests if the customer is in good standing at their home library. The 
     * definition of good standing is not restricted by Metro federation.
     * 
     * @param customer
     * @param message any extra data about the customer account like a SIP response.
     * @param s the return message if the customer failed this test.
     * @return true if the customer is in good standing at home library and
     * false otherwise.
     */
    public boolean isInGoodStanding(Customer customer, CustomerMessage message, StringBuilder s)
    {
        // because EPL uses SIP to get customer information we can assume that
        // meta will contain BARRED if the customer is not in good standing.
        String standingMessage = message.getStanding();
        for (String notGoodStandingType: notInGoodStandingStandingSentinal)
        {
            System.out.println("TESTING: '" + notGoodStandingType + "' against '"+standingMessage+"'");
            if (standingMessage.contains(notGoodStandingType)) // TODO Test with bad customers!!!.
            {
                customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.FALSE);
                s.append(failGoodstandingTest);
                return false;
            }
        }
        customer.set(CustomerFieldTypes.ISGOODSTANDING, Protocol.TRUE);
        return true;
    }

    /**
     * Another way to compute the customer age. If the library doesn't collect
     * DOB for customers, or they do it inconsistently this method provides
     * checking of the customers profile (or btype) as a test for minimum age.
     * If your library doesn't have birth dates for customers, or they are kept
     * inconsistently, override this method in your library's strategy so that
     * the customer can always be checked for age restrictions.
     *
     * @param customer the customer as a list of fields formatted from raw ILS
     * query.
     * @param meta The extra customer data that the ILS returned when it was
     * queried with getCustomer, but is not required by MeCard customer
     * creation. Things like PROFILE and bType.
     * @param s the return message if the customer failed this test.
     * @return true if customer is of minimum age and false otherwise.
     */
    public boolean isMinimumAge(Customer customer, CustomerMessage meta, StringBuilder s)
    {
        if (juvenileTypes.isEmpty())
        {
            // to get here the library doesn't have juvenile types so we need to compute by date.
            if (this.isMinimumAgeByDate(customer, meta, s) == false)
            {
                customer.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
                s.append(failMinAgeTest);
                return false;
            }
        }
        else
        {
            // run through all the juv profile types and if one matches then
            // no can do.
            String customerType = meta.getCustomerProfile();
            for (String str: juvenileTypes)
            {
                if (customerType.compareTo(str) == 0) // if we match on a non resident bType we aren't a resident.
                {
                    customer.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
                    s.append(failMinAgeTest);
                    return false;
                }
            }
        }
        customer.set(CustomerFieldTypes.ISMINAGE, Protocol.TRUE);
        return true;
    }

    /**
     * Tests if the customer is of minimum age. If a date field is available
     * this method will compute how many years old the customer is. If the date
     * field is empty, compute using the abstract method isMinimumAge().
     * Use this if you don't have any profiles that separate juveniles from Adults.
     *
     * @param customer
     * @param meta The extra customer data that the ILS returned when it was
     * queried with getCustomer, but is not required by MeCard customer
     * creation. Things like PROFILE and bType. Usually a SIP2 message.
     * @param s the return message if the customer failed this test.
     * @return true if the customer was of minimum age and false otherwise.
     */
    public boolean isMinimumAgeByDate(Customer customer, CustomerMessage meta, StringBuilder s)
    {
        if (customer.isEmpty(CustomerFieldTypes.DOB))
        {
            if (DEBUG)
            {
                System.out.println("customer " + customer.get(CustomerFieldTypes.ID)
                        + " failed minimum age requirement.");
            }
            s.append("date of birth not set.");
            return false;
        }
        String dateOfBirth = customer.get(CustomerFieldTypes.DOB);
        try
        {
            int yearsOld = DateComparer.getYearsOld(dateOfBirth);
            if (yearsOld >= MeCardPolicy.MINIMUM_YEARS_OF_AGE)
            {
                customer.set(CustomerFieldTypes.ISMINAGE, Protocol.TRUE);
                return true;
            }
        } catch (ParseException ex)
        {
            if (DEBUG)
            {
                System.out.println("customer " + customer.get(CustomerFieldTypes.ID)
                        + " tested but failed parse DOB.");
            }
            s.append("invalid birth date.");
            return false; // no longer an issue to not have a date. Some libraries don't collect them.
        }
        s.append(failMinAgeTest);
        return false;
    }

    /**
     * Tests and sets the customer's email flag.
     *
     * @param customer
     * @param meta The extra customer data that the ILS returned when it was
     * queried with getCustomer, but is not required by MeCard customer
     * creation. Things like PROFILE and bType.
     * @param s the return message if the customer failed this test.
     * @return true if the customer has an email and false otherwise.
     */
    public boolean isEmailable(Customer customer, CustomerMessage meta, StringBuilder s)
    {
        if (customer.isEmpty(CustomerFieldTypes.EMAIL))
        {
            if (DEBUG)
            {
                System.out.println("customer " + customer.get(CustomerFieldTypes.ID)
                        + " failed email requirement.");
            }
            s.append(failEmailTest);
            return false;
        }
        return true;
    }

    /**
     * Tests and sets customer is valid flag. From a customer to be valid they 
     * must have all the mandatory fields filled with valid information. Mandatory
     * fields are ID, PIN, Name, Street, City, Province, Postal code, Email, 
     * and valid expiry date. A valid expiry date is some date in the future not
     * less than tomorrow.
     * 
     * @param customer
     * @param s the buffer where potential error messages are placed.
     * @return true if the customer is valid and false otherwise.
     */
    public boolean isValidCustomerData(Customer customer, StringBuilder s)
    {
        StringBuilder sBuff = new StringBuilder(failCompletenessTest);
        boolean returnValue = true;
        try
        {
            // Test customer fields that they are somewhat valid.
            if (customer.isEmpty(CustomerFieldTypes.ID))
            {
                if (DEBUG) System.out.println("customer failed barcode requirement.");
                sBuff.append(":id");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.PIN))
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed pin requirement.");
                sBuff.append(":pin");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.EMAIL))
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed email requirement.");
                sBuff.append(":email");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.LASTNAME))
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed last name requirement.");
                sBuff.append(":last name");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.FIRSTNAME))
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed first name requirement.");
                sBuff.append(":first name");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.PRIVILEGE_EXPIRES))
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed expiry requirement.");
                sBuff.append(":privilege expiry");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.STREET))
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed address: street requirement.");
                sBuff.append(":street");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.CITY))
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed address: city requirement.");
                sBuff.append(":city");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.PROVINCE))
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed address: province requirement.");
                sBuff.append(":province");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.POSTALCODE))
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed address: postal code requirement.");
                sBuff.append(":postal code");
                returnValue = false;
            }
        }
        catch (NullPointerException ex) // if any of the fields didn't get filled in a check of the hash will return null.
        {
            System.out.println("Customer failed isValid test in MecardPolicies, one of required customer fields was null.");
            sBuff.append(":a required field was null");
            returnValue = false;
        }

        if (returnValue == false)
        {
            s.append(sBuff.toString());
        }
        return returnValue;
    }

    /**
     * Tests if the customer has a valid expiry date.
     *
     * @param customer
     * @param meta the message received that contains additional customer information
     * typically a SIP response.
     * @param s the return message if the customer failed this test.
     * @return true if customer's expiry is at least tomorrow and at most 365
     * days from now.
     */
    public boolean isValidExpiryDate(Customer customer, CustomerMessage meta, StringBuilder s)
    {
        String expiryDate = customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES);
        try
        {
            int expiryDays = DateComparer.getDaysUntilExpiry(expiryDate);
            if (DEBUG) System.out.println("Customer privilege date:"
                    + expiryDate + ", computed days: " + expiryDays);
            if (expiryDays >= MeCardPolicy.MINIMUM_EXPIRY_DAYS)
            {
                if (expiryDays > MeCardPolicy.MAXIMUM_EXPIRY_DAYS)
                {
                    // set the customer's expiry to 365 days from now and output the message.
                    String newExpiryOneYearFromNow = DateComparer.getFutureDate(MeCardPolicy.MAXIMUM_EXPIRY_DAYS);
                    customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, newExpiryOneYearFromNow);
                    System.out.println("customer expiry throttled to: '" + newExpiryOneYearFromNow + "'");
                }
                if (DEBUG) System.out.println("Customer passed privilege date.");
                return true;
            }
        } catch (ParseException ex)
        {
            System.out.println("Error parsing date: '" + expiryDate + "'");
            s.append(failExpiryTest);
            return false;
        }
        s.append(failExpiryTest);
        return false;
    }
    
    /**
     * When a customer reports a lost card, the home library should set this flag
     * on the customer's account. The MeCard web site can also tell if a previously
     * registered customer is returning to register a lost card. If so, the customer's
     * lost card flag should be set and it is upto the library what they do with it.
     *
     * @param customer The customer information as will be inserted into the guest ILS.
     * @param customerMessage additional information from the ILS that is not sent to the guest
     * library, stuff like their profile and number of holds etc.
     * @param s the return message if the customer failed this test.
     * @return true if this account is a lost card.
     * @see mecard.customer.Customer
     */
    public boolean isLostCard(Customer customer, CustomerMessage customerMessage, StringBuilder s)
    {
        // Well let's see what the customer's meta information can tell us.
        if (customerMessage.cardReportedLost())
        {
            customer.set(CustomerFieldTypes.ISLOSTCARD, Protocol.TRUE);
            if (DEBUG) System.out.println("card is a lost card");
            s.append(failLostCardTest);
            return true;
        }
        if (DEBUG) System.out.println("card passes lost card test.");
        customer.set(CustomerFieldTypes.ISLOSTCARD, Protocol.FALSE);
        return false;
    }
}
