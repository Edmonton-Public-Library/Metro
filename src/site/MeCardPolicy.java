/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013 - 2025 Edmonton Public Library
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
import mecard.config.MessagesTypes;
import mecard.util.DateComparer;
import mecard.config.PropertyReader;
import mecard.util.PostalCode;
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

    public final static int MINIMUM_YEARS_OF_AGE   = 18;
    public final static int MINIMUM_EXPIRY_DAYS    = 1;
    public final static int MAXIMUM_EXPIRY_DAYS    = 365;
//    public final static int MAX_EXPIRY_DAYS        = 365;
    public final static int MAX_CHECK_ADDRESS_DAYS = 365;
    protected static String failMinAgeTest;
    protected static String failLostCardTest;
    protected static String failGoodstandingTest;
    protected static String failReciprocalTest;
    protected static String failResidencyTest;
    protected static String failEmailTest;
    protected static String failExpiryTest;
    protected static String failCompletenessTest;
    protected static boolean debug;
    
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
        failMinAgeTest       = messageProps.getProperty(MessagesTypes.FAIL_MIN_AGE_TEST.toString());
        failLostCardTest     = messageProps.getProperty(MessagesTypes.FAIL_LOSTCARD_TEST.toString());
        failGoodstandingTest = messageProps.getProperty(MessagesTypes.FAIL_GOODSTANDING_TEST.toString());
        failReciprocalTest   = messageProps.getProperty(MessagesTypes.FAIL_RECIPROCAL_TEST.toString());
        failResidencyTest    = messageProps.getProperty(MessagesTypes.FAIL_RESIDENCY_TEST.toString());
        failEmailTest        = messageProps.getProperty(MessagesTypes.FAIL_EMAIL_TEST.toString());
        failExpiryTest       = messageProps.getProperty(MessagesTypes.FAIL_EXPIRY_TEST.toString());
        failCompletenessTest = messageProps.getProperty(MessagesTypes.FAIL_COMPLETENESS_TEST.toString());

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
        MeCardPolicy.debug = debug;
        return mePolicy;
    }
    
    /**
     * Gets the minimum age for any customer to be able to use ME Libraries.
     * 
     * @return integer of the minimum age for me customers.
     */
    public final static int minimumAge()
    {
        return MINIMUM_YEARS_OF_AGE;
    }
    
    /**
     * Gets the maximum number of days before account expires.
     * @return integer of maximum days an account can be active before it
     * expires.
     */
    public final static int maximumExpiryDays()
    {
        return MAXIMUM_EXPIRY_DAYS;
    }
    
    /**
     * Gets the maximum number of days before account expires.
     * @return integer of maximum days an account can be active before it
     * expires.
     */
    public final static int maximumAddressCheckDays()
    {
        return MAX_CHECK_ADDRESS_DAYS;
    }
    
    /**
     * Standardizes the field data for transmission to other libraries. This 
     * ensures that all libraries get proper case information.
     * @param customer 
     */
    public void normalizeCustomerFields(Customer customer)
    {
        String customerData = Text.cleanName(customer.get(CustomerFieldTypes.FIRSTNAME));
        customer.set(CustomerFieldTypes.FIRSTNAME, customerData);
        customerData = Text.cleanName(customer.get(CustomerFieldTypes.LASTNAME));
        customer.set(CustomerFieldTypes.LASTNAME, customerData);
        customerData = Text.toDisplayCase(customer.get(CustomerFieldTypes.FIRSTNAME));
        customer.set(CustomerFieldTypes.FIRSTNAME, customerData);
        customerData = Text.toDisplayCase(customer.get(CustomerFieldTypes.LASTNAME));
        customer.set(CustomerFieldTypes.LASTNAME, customerData);
        customerData = Text.toDisplayCase(customer.get(CustomerFieldTypes.STREET));
        customer.set(CustomerFieldTypes.STREET, customerData);
        PostalCode pCode = new PostalCode(customer.get(CustomerFieldTypes.POSTALCODE));
        customer.set(CustomerFieldTypes.POSTALCODE, pCode.toString());
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
        String customerProfile = message.getCustomerProfile();
        for (String nonResidentType : nonResidentTypes)
        {
            if (debug)
                System.out.println("TESTING: '" + nonResidentType + "' against customer residency:'"+customerProfile+"'");
            if (customerProfile.contains(nonResidentType)) // if we match on a non resident bType we aren't a resident.
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
        for (String reciprocalType: reciprocalTypes)
        {
            if (debug)
                System.out.println("TESTING: '" + reciprocalType + "' against customer profile:'"+profileType+"'");
            if (profileType.contains(reciprocalType)) // if we match on a reciprocal customerProfile we aren't a resident.
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
        // But the above test is not reliable; many libraries use the status message
        // as the means to signal customers are not in good standing.
        // because EPL uses SIP to get customer information we can assume that
        // meta will contain BARRED if the customer is not in good standing.
        String customerStanding = message.getStanding();
        for (String notGoodStandingType: notInGoodStandingStandingSentinal)
        {
            if (debug)
                System.out.println("TESTING: '" + notGoodStandingType + "' against customer standing:'"+customerStanding+"'");
            if (customerStanding.contains(notGoodStandingType))
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
        // run through all the juv customerProfile types and if one matches then
        // no can do.
        // Fail if the account matches listed bTypes.
        String customerAgeCategory = meta.getCustomerProfile();
        for (String customerProfile : juvenileTypes)
        {
            if (debug)
                System.out.println("TESTING: '" + customerProfile + "' against customer profile:'"+customerAgeCategory+"'");
            if (customerAgeCategory.contains(customerProfile)) // if we match on a non resident bType we aren't a resident.
            {
                customer.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
                s.append(failMinAgeTest);
                System.out.println("Customer '" + customer.get(CustomerFieldTypes.ID) + "' failed minimum age with profile:'"+customerAgeCategory+"'");
                return false;
            }
        }
        // So didn't match customerProfile of a Juv, but that just means that doesn't 
        // prove that they are min age, so let's check that.
        // If there is a DOB for the customer check that the computed date is
        // greater than the min age.
        if (! customer.hasValidBirthDate())
        {
            customer.set(CustomerFieldTypes.ISMINAGE, Protocol.FALSE);
            s.append(failMinAgeTest);
            System.out.println("Customer '" + customer.get(CustomerFieldTypes.ID) + "' has a recorded DOB of '"
                    +customer.get(CustomerFieldTypes.DOB)+"' which is too young to consent to share their information.");
            return false;
        }
        // All else can pass through, so in summary:
        // If they didn't match a Juv customerProfile AND their computed date is less 
        // than the min age they will get rejected. The bad part is if they don't 
        // have a birthday and aren't explicitly a juv customerProfile they're good.
        customer.set(CustomerFieldTypes.ISMINAGE, Protocol.TRUE);
        return true;
    }

    /**
     * Tests if the customer is of minimum age. If a date field is available
     * this method will compute how many years old the customer is.
     * Use this if you don't have any profiles that separate juveniles from Adults.
     *
     * @param customer
     * @param meta The extra customer data that the ILS returned when it was
     * queried with getCustomer, but is not required by MeCard customer
     * creation. Things like PROFILE and bType.
     * @param s the return message if the customer failed this test.
     * @return true if the customer was of minimum age and false otherwise.
     */
    public boolean isMinimumAgeByDate(Customer customer, CustomerMessage meta, StringBuilder s)
    {
        if (customer.hasValidBirthDate())
        {
            String dob = customer.get(CustomerFieldTypes.DOB);
            try 
            {
                if (DateComparer.getYearsOld(dob) < MeCardPolicy.minimumAge())
                {
                    if (debug)
                    {
                        System.out.println("customer " + customer.get(CustomerFieldTypes.ID)
                                + " tested but failed minimum age." 
                                + customer.get(CustomerFieldTypes.DOB));
                    }
                    s.append(failMinAgeTest);
                    return false;
                }
            } 
            // If there is a problem with parsing the value it's not valid.
            catch (ParseException ex)
            {
                if (debug)
                {
                    System.out.println("*warning: " + customer.get(CustomerFieldTypes.ID)
                        + " date of birth caused a parsing exception. '" 
                        + customer.get(CustomerFieldTypes.DOB) + "'");
                }
            }
        }
        return true;
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
        if (! Text.isValidEmail(customer.get(CustomerFieldTypes.EMAIL)))
        {
            if (debug)
                System.out.println("customer " + customer.get(CustomerFieldTypes.ID)+ " failed email requirement.");
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
                if (debug) 
                    System.out.println("customer failed barcode requirement.");
                sBuff.append(":id");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.PIN))
            {
                if (debug) 
                    System.out.println("customer "+customer.get(CustomerFieldTypes.ID)+" failed pin requirement.");
                sBuff.append(":pin");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.EMAIL))
            {
                if (debug) 
                    System.out.println("customer "+customer.get(CustomerFieldTypes.ID)+" failed email requirement.");
                sBuff.append(":email");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.LASTNAME))
            {
                if (debug) 
                    System.out.println("customer "+customer.get(CustomerFieldTypes.ID)+" failed last name requirement.");
                sBuff.append(":last name");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.FIRSTNAME))
            {
                if (debug) 
                    System.out.println("customer "+customer.get(CustomerFieldTypes.ID)+" failed first name requirement.");
                sBuff.append(":first name");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.PRIVILEGE_EXPIRES))
            {
                String expiry = DateComparer.getFutureDate(MeCardPolicy.MAXIMUM_EXPIRY_DAYS);
                customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, expiry);
//                if (debug) 
//                    System.out.println("customer "+customer.get(CustomerFieldTypes.ID)+" failed expiry requirement.");
//                sBuff.append(":privilege expiry");
//                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.STREET))
            {
                if (debug) 
                    System.out.println("customer "+customer.get(CustomerFieldTypes.ID)+" failed address: street requirement.");
                sBuff.append(":street");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.CITY))
            {
                if (debug) 
                    System.out.println("customer "+customer.get(CustomerFieldTypes.ID)+" failed address: city requirement.");
                sBuff.append(":city");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.PROVINCE))
            {
                if (debug) 
                    System.out.println("customer "+customer.get(CustomerFieldTypes.ID)+" failed address: province requirement.");
                sBuff.append(":province");
                returnValue = false;
            }
            if (customer.isEmpty(CustomerFieldTypes.POSTALCODE))
            {
                if (debug) 
                    System.out.println("customer "+customer.get(CustomerFieldTypes.ID)+" failed address: postal code requirement.");
                sBuff.append(":postal code");
                returnValue = false;
            }
            // Birthdate must be valid because the customer must be of a minimum
            // age to join ME Libraries.
            if (! customer.hasValidBirthDate())
            {
                if (debug) 
                    System.out.println("customer "+customer.get(CustomerFieldTypes.ID)+" invalid birthdate.");
                sBuff.append(":birthdate");
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
        // Symphony systems will return 'NEVER' for lifetime members, however they are
        // throttled to a year on another library system, which is only fair.
        // Turns out that it is becoming more common for libraries not to have
        // expiry dates. EPL doesn't have them, nor does CPL. Horizon may return 
        // an empty field. To adjust for this let's test for empty date strings.
        if (expiryDate.equalsIgnoreCase("NEVER") || 
                customer.isEmpty(CustomerFieldTypes.PRIVILEGE_EXPIRES))
        {
            // set the customer's expiry to 365 days from now and output the message.
            String newExpiryOneYearFromNow = DateComparer.getFutureDate(MeCardPolicy.MAXIMUM_EXPIRY_DAYS);
            customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, newExpiryOneYearFromNow);
            System.out.println("customer expiry throttled to: '" + newExpiryOneYearFromNow + "'");
            return true;
        }
        // Still there could be an error in the way the date is entered or some 
        // extraneous value has been entered so try and parse the value in the date field.
        try
        {
            int expiryDays = DateComparer.getDaysUntilExpiry(expiryDate);
            if (debug) 
                System.out.println("Customer privilege date:"+ expiryDate + ", computed days: " + expiryDays);
            if (expiryDays >= MeCardPolicy.MINIMUM_EXPIRY_DAYS)
            {
                if (expiryDays > MeCardPolicy.MAXIMUM_EXPIRY_DAYS)
                {
                    // set the customer's expiry to 365 days from now and output the message.
                    String newExpiryOneYearFromNow = DateComparer.getFutureDate(MeCardPolicy.MAXIMUM_EXPIRY_DAYS);
                    customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, newExpiryOneYearFromNow);
                    System.out.println("customer expiry throttled to: '" + newExpiryOneYearFromNow + "'");
                }
                if (debug) 
                    System.out.println("Customer passed privilege date.");
                return true;
            }
        } 
        // To get here your expiry isn't 'NEVER' and not a valid date so... ?
        catch (ParseException ex)
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
     * on the customer's account.The MeCard web site can also tell if a previously
 registered customer is returning to register a lost card. If so, the customer's
 lost card flag should be set and it is upto the library what they do with it.
     *
     * @param customer The customer information as will be inserted into the guest ILS.
     * @param customerMessage additional information from the ILS that is not sent to the guest
 library, stuff like their customerProfile and number of holds etc.
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
            if (debug) 
                System.out.println("card is a lost card");
            s.append(failLostCardTest);
            return true;
        }
        if (debug) 
            System.out.println("card passes lost card test.");
        customer.set(CustomerFieldTypes.ISLOSTCARD, Protocol.FALSE);
        return false;
    }
}
