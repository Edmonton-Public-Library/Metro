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

import site.edmonton.EPLPolicy;
import site.ftsaskatchewan.FTSPolicy;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;
import mecard.exception.UnsupportedLibraryException;
import mecard.MetroService;
import mecard.Protocol;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.customer.Customer;
import mecard.config.CustomerFieldTypes;
import mecard.util.DateComparer;
import site.stalbert.STAPolicy;
import site.strathcona.STRPolicy;

/**
 * This class needs to be sub-classed by all libraries. All customer's must meet 
 * the defined MeCard policy rules. They must be resident, not reciprocal, must
 * be of minimum age of 18, must have an email address, must be in good standing
 * with their home library, must have all mandatory account information present
 * and valid (within reason), and all must have a valid membership expiry date.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public abstract class MeCardPolicy
{

    public final static int MINIMUM_YEARS_OF_AGE = 18;
    public final static int MINIMUM_EXPIRY_DAYS = 1;
    public final static int MAXIMUM_EXPIRY_DAYS = 365;
    protected static boolean DEBUG;

    public static MeCardPolicy getInstanceOf(boolean debug)
    {
        DEBUG = debug;
        Properties props = MetroService.getProperties(ConfigFileTypes.ENVIRONMENT);
        String libCode = props.getProperty(LibraryPropertyTypes.LIBRARY_CODE.toString());
        if (DEBUG) System.out.println(new Date() + "LIB_CODE: '" + libCode + "'");
        if (libCode.equalsIgnoreCase(MemberTypes.EPL.name()))
        {
            return new EPLPolicy(DEBUG);
        } 
        else if (libCode.equalsIgnoreCase(MemberTypes.STA.name()))
        {
            return new STAPolicy(DEBUG);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.STR.name()))
        {
            return new STRPolicy(DEBUG);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.FTS.name()))
        {
            return new FTSPolicy(DEBUG);
        }
        else
        {
            throw new UnsupportedLibraryException(libCode);
        }
    }
    
    /**
     * Standardizes the field data for transmission to other libraries. This 
     * ensures that all libraries get proper case information.
     * @param customer 
     */
    public void normalizeCustomerFields(Customer customer)
    {
        String customerData = MeCardPolicy.toDisplayCase(customer.get(CustomerFieldTypes.FIRSTNAME));
        customer.set(CustomerFieldTypes.FIRSTNAME, customerData);
        customerData = MeCardPolicy.toDisplayCase(customer.get(CustomerFieldTypes.LASTNAME));
        customer.set(CustomerFieldTypes.LASTNAME, customerData);
        customerData = MeCardPolicy.toDisplayCase(customer.get(CustomerFieldTypes.STREET));
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
     * @param meta
     * @return true if the customer is resident to their home library
     * and false otherwise.
     */
    public abstract boolean isResident(Customer customer, String meta);

    /**
     * Each library must decide how they compute if a customer is a reciprocal
     * customer. Usually this is done by comparing the btype of profile of the
     * customer.
     * 
     * @param customer
     * @param meta
     * @return true if the customer is a reciprocal member at their home library 
     * and false otherwise.
     */
    public abstract boolean isReciprocal(Customer customer, String meta);

    /**
     * Tests if the customer is in good standing at their home library. The 
     * definition of good standing is not restricted by Metro federation.
     * 
     * @param customer
     * @param meta
     * @return true if the customer is in good standing at home library and
     * false otherwise.
     */
    public abstract boolean isInGoodStanding(Customer customer, String meta);

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
     * @return true if customer is of minimum age and false otherwise.
     */
    public abstract boolean isMinimumAge(Customer customer, String meta);

    /**
     * Tests if the customer is of minimum age. If a date field is available
     * this method will compute how many years old the customer is. If the date
     * field is empty, compute using the abstract method isMinimumAge().
     *
     * @param customer
     * @param meta The extra customer data that the ILS returned when it was
     * queried with getCustomer, but is not required by MeCard customer
     * creation. Things like PROFILE and bType.
     * @return true if the customer was of minimum age and false otherwise.
     */
    public boolean isMinimumAgeByDate(Customer customer, String meta)
    {
        String dateOfBirth = customer.get(CustomerFieldTypes.DOB);
        if (dateOfBirth.compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG)
            {
                System.out.println("customer " + customer.get(CustomerFieldTypes.ID)
                        + " failed minimum age requirement.");
            }
            return false;
        }
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
            return false; // no longer an issue to not have a date. Some libraries don't collect them.
        }
        return false;
    }

    /**
     * Tests and sets the customer's email flag.
     *
     * @param customer
     * @param meta The extra customer data that the ILS returned when it was
     * queried with getCustomer, but is not required by MeCard customer
     * creation. Things like PROFILE and bType.
     * @return true if the customer has an email and false otherwise.
     */
    public boolean isEmailable(Customer customer, String meta)
    {
        if (customer.get(CustomerFieldTypes.EMAIL).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG)
            {
                System.out.println("customer " + customer.get(CustomerFieldTypes.ID)
                        + " failed email requirement.");
            }
            return false;
        }
        return true;
    }

    /**
     * Tests and sets customer is valid flag. Fro a customer to be valid they 
     * must have all the mandatory fields filled with valid information. Mandatory
     * fields are ID, PIN, Name, Street, City, Province, Postal code, Email, 
     * and valid expiry date. A valid expiry date is some date in the future not
     * less than tomorrow.
     * 
     * @param customer
     * @return true if the customer is valid and false otherwise.
     */
    public boolean isValidCustomerData(Customer customer)
    {
        // Test customer fields that they are somewhat valid.
        if (customer.get(CustomerFieldTypes.ID).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG) System.out.println("customer failed barcode requirement.");
            return false;
        }
        if (customer.get(CustomerFieldTypes.PIN).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                    +" failed pin requirement.");
            return false;
        }
        if (customer.get(CustomerFieldTypes.EMAIL).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                    +" failed email requirement.");
            return false;
        }
        // for naming customer can have a name field or the first name last name field populated.
        if (customer.get(CustomerFieldTypes.PREFEREDNAME).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                    +" failed name requirement.");
            return false;
        }
        else
        {
            if (customer.get(CustomerFieldTypes.LASTNAME).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed last name requirement.");
                return false;
            }
            if (customer.get(CustomerFieldTypes.FIRSTNAME).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
            {
                if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                        +" failed first name requirement.");
                return false;
            }
        }
        
        if (customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                    +" failed expiry requirement.");
            return false;
        }
        if (customer.get(CustomerFieldTypes.STREET).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                    +" failed address: street requirement.");
            return false;
        }
        if (customer.get(CustomerFieldTypes.CITY).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                    +" failed address: city requirement.");
            return false;
        }
        if (customer.get(CustomerFieldTypes.PROVINCE).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                    +" failed address: province requirement.");
            return false;
        }
        if (customer.get(CustomerFieldTypes.POSTALCODE).compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            if (DEBUG) System.out.println("customer "+customer.get(CustomerFieldTypes.ID)
                    +" failed address: postal code requirement.");
            return false;
        }

        return true;
    }

    /**
     * Tests if the customer has a valid expiry time limit.
     * @param customer
     * @param meta
     * @return true if customer's expiry is at least tomorrow and at most 365
     * days from now.
     */
    public boolean isValidExpiryDate(Customer customer, String meta)
    {
        String expiryDate = customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES);
        // TODO set Max expiry, no greater than 365 days.
        try
        {
            int expiryDays = DateComparer.getDaysUntilExpiry(expiryDate);
            if (DEBUG) System.out.println("Customer privilege date:"+expiryDate+", computed days: " + expiryDays);
            if (expiryDays >= MeCardPolicy.MINIMUM_EXPIRY_DAYS)
            {
                return true;
            }
        } catch (ParseException ex)
        {
            return false;
        }
        return false;
    }
    
    /**
     * When a customer reports a lost card, the home library should set this flag
     * on the customer's account. The MeCard web site can also tell if a previously
     * registered customer is returning to register a lost card. If so, the customer's
     * lost card flag should be set and it is upto the library what they do with it.
     * @param customer The customer information as will be inserted into the guest ILS.
     * @param meta additional information from the ILS that is not sent to the guest
     * library, stuff like their profile and number of holds etc.
     * @return true if this account is a lost card.
     * @see mecard.customer.Customer
     */
    public abstract boolean isLostCard(Customer customer, String meta);
    
    /**
     * Converts strings to proper case. Used for normalizing names and streets
     * to a proper case convention. One of the libraries uses UPPERCASE for
     * customer first name last name and address fields, which is not helpful
     * for other libraries. Each should expect their data in a constant format.
     * @param s string to be Proper Cased.
     * @return the string in proper case.
     */
    public static String toDisplayCase(String s)
    {
        // Credit to: http://stackoverflow.com/questions/1086123/titlecase-conversion
        final String ACTIONABLE_DELIMITERS = " -"; // these cause the character following
                                                   // to be capitalized
        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) 
        {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
        }
        return sb.toString();
    }
}
