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

import mecard.ResponseTypes;
import mecard.customer.Customer;
import api.Request;
import api.Response;
import mecard.Protocol;
import mecard.customer.CustomerFieldTypes;
import site.mecard.MeCardPolicy;

/**
 *
 * @author metro
 */
public abstract class Responder
{
    protected Request request;
    protected Response response;
    protected final boolean debug;
    
    protected Responder(String cmd, boolean debugMode)
    {
        this.debug = debugMode;
        this.request = new Request(cmd);
        this.response = new Response(ResponseTypes.INIT);
        if (debug)
        {
            System.out.println("CMD:\n  '"+request.toString()+"' '"+request.getCommandType().name()+"'");
            System.out.println("ELE:");
            System.out.println("  S:" + request.getArgs() + ",");
        }
    }
    
    public static String getUnautherizedRequestResponse(String msg)
    {
        Response r = new Response(ResponseTypes.UNAUTHORIZED);
        r.addResponse(msg);
        return r.toString();
    }
    
    public abstract String getResponse();
    
     /**
     * Tests if customer meets the MeCard requirements of membership.
     *
     * @param customer
     * @param additionalData the value of additionalData
     * @return true if the customer meets MeCard requirements for age restrictions etc.
     * and false otherwise.
     */
    protected boolean meetsMeCardRequirements(Customer customer, String additionalData)
    {
        if (customer == null || additionalData == null)
        {
            return false;
        }
        MeCardPolicy policy = MeCardPolicy.getInstanceOf(this.debug);
        if (policy.isEmailable(customer, additionalData) == false) return false;
        if (policy.isInGoodStanding(customer, additionalData) == false) return false;
        if (policy.isMinimumAge(customer, additionalData) == false) return false;
        if (policy.isReciprocal(customer, additionalData)) return false; // reciprocals not allowed.
        if (policy.isResident(customer, additionalData) == false) return false;
        if (policy.isValidCustomerData(customer) == false) return false;
        if (policy.isValidExpiryDate(customer, additionalData) == false) return false;
        if (policy.isLostCard(customer, additionalData)) return false;
        return true;
    }
}
