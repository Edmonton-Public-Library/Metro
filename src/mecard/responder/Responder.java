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
import mecard.Exception.MalformedCommandException;
import mecard.Exception.MetroSecurityException;
import mecard.Exception.UnsupportedCommandException;
import mecard.Exception.UnsupportedResponderException;
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
    
    /**
     *
     * @param cmd the value of cmd
     * @param debugMode the value of debugMode
     */
    protected Responder(Request cmd, boolean debugMode)
    {
        this.debug = debugMode;
        this.request = cmd;
        this.response = new Response(ResponseTypes.INIT);
        if (debug)
        {
            System.out.println("CMD:\n  '"+request.toString()+"' '"+request.getCommandType().name()+"'");
            System.out.println("ELE:");
            System.out.println("  S:" + request.getArgs() + ",");
        }
    }
    
    /**
     * @param ex the exception thrown
     * @return String value of the response with code.
     */
    public static String getExceptionResponse(RuntimeException ex)
    {
        Response r = new Response(ResponseTypes.UNKNOWN);
        if (ex instanceof MetroSecurityException)
        {
            r = new Response(ResponseTypes.UNAUTHORIZED);
        }
        else if (ex instanceof MalformedCommandException)
        {
            r = new Response(ResponseTypes.ERROR);
        }
        else if (ex instanceof UnsupportedCommandException)
        {
            r = new Response(ResponseTypes.UNKNOWN);
            r.addResponse("Command not implemented, make sure your server is up to date.");
        }
        else if (ex instanceof UnsupportedResponderException)
        {
            r = new Response(ResponseTypes.CONFIG_ERROR);
            r.addResponse("The server doesn't seem to be configured correctly.");
        }
        r.addResponse(ex.getMessage());
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
