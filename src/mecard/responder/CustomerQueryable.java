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
package mecard.responder;

import api.Request;
import api.Response;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import site.mecard.MeCardPolicy;

/**
 * Indicates that the implementer can query the status of theusers.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public abstract class CustomerQueryable extends Responder
{
    public CustomerQueryable(Request command, boolean debugMode)
    {
        super(command, debugMode);
    }
    
    /**
     * Gets the customer account information.
     */
    public abstract void getCustomer(Response response);
    
    /**
     * Tests if the pin supplied from the web site matches the user's account pin.
     * @param suppliedPin - String data used to test against Customer pin.
     * @param customer customer data.
     * @return true if the supplied pin matched the account pin and false otherwise.
     */
    public abstract boolean isAuthorized(String suppliedPin, Customer customer);
    
    /**
     * Tests if the customer meets the required MeCard requirements. MeCard 
     * users must be:
     * <ul>
     * <li>Over the age of 18</li>
     * <li>Must to be a reciprocal customer at the home library.</li>
     * <li>Must be in good standing at their home library.</li>
     * <li>Must be a resident of the home library's service area.</li>
     * <li>Must have a valid expiry date.</li>
     * <li>Must have mandatory account fields filled with valid data.</li>
     * </ul>
     * @param customer
     * @param additionalData
     * @return true if the customer meets the MeCard participation requirements
     * and false otherwise.
     */
    protected boolean meetsMeCardRequirements(Customer customer, String additionalData)
    {
        if (customer == null || additionalData == null)
        {
            return false;
        }
        MeCardPolicy policy = MeCardPolicy.getInstanceOf(this.debug);
        if (policy.isEmailable(customer, additionalData) == false) 
        {
            System.out.println("Customer not emailable.");
            return false;
        }
        if (policy.isInGoodStanding(customer, additionalData) == false)
        {
            System.out.println("Customer not in good standing.");
            return false;
        }
        if (policy.isMinimumAge(customer, additionalData) == false)
        {
            System.out.println("Customer not minimum age.");
            return false;
        }
        if (policy.isReciprocal(customer, additionalData))
        {
            System.out.println("Customer cannot join because they are a reciprocal customer.");
            return false;
        } // reciprocals not allowed.
        if (policy.isResident(customer, additionalData) == false) 
        {
            System.out.println("Customer is not resident.");
            return false;
        }
        if (policy.isValidCustomerData(customer) == false) 
        {
            System.out.println("Customer's data is not valid.");
            return false;
        }
        if (policy.isValidExpiryDate(customer, additionalData) == false) 
        {
            System.out.println("Customer does not have a valid privilege date.");
            return false;
        }
        if (policy.isLostCard(customer, additionalData)) 
        {
            System.out.println("Customer's card reported as lost.");
            return false;
        }
   
        System.out.println("Customer cleared.");
        return true;
    }
}
