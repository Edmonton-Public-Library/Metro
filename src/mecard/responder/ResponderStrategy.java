/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.responder;

import mecard.ResponseTypes;
import mecard.customer.Customer;
import mecard.util.Request;
import mecard.util.Response;
import site.mecard.MeCardPolicy;

/**
 *
 * @author metro
 */
public abstract class ResponderStrategy
{
    protected Request request;
    protected Response response;
    protected final boolean debug;
    
    protected ResponderStrategy(String cmd, boolean debugMode)
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
        return true;
    }
}
