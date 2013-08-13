
package epl;

import mecard.customer.Customer;
import site.mecard.CustomerLoadNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class EPLCustomerNormalizer extends CustomerLoadNormalizer
{
    private final boolean debug;
    
    public EPLCustomerNormalizer(boolean debug)
    {
        this.debug = debug;
    }
    
    @Override
    public String normalize(Customer c)
    {
        return ""; // no special rules for EPL.
    }
}
