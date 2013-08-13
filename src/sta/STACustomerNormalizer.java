
package sta;

import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import site.mecard.CustomerLoadNormalizer;
import site.mecard.MeCardPolicy;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class STACustomerNormalizer extends CustomerLoadNormalizer
{
    public final static int MAXIMUM_PIN_WIDTH = 4;
    private final boolean debug;
    
    public STACustomerNormalizer(boolean debug)
    {
        this.debug = debug;
    }
    
    @Override
    public String normalize(Customer c)
    {
        StringBuilder returnMessage = new StringBuilder();
        if (c.get(CustomerFieldTypes.PIN).length() <= STACustomerNormalizer.MAXIMUM_PIN_WIDTH == false)
        {
            String pin = c.get(CustomerFieldTypes.PIN);
            int start = pin.length() - STACustomerNormalizer.MAXIMUM_PIN_WIDTH;
            if (start >= 0)
            {
                String newPin = pin.substring(start);
                returnMessage.append("Your pin has been set to '");
                returnMessage.append(newPin);
                returnMessage.append("' to comply with this library's policies.");
                c.set(CustomerFieldTypes.PIN, newPin);
                System.out.println("Customer's PIN was too long trimmed to last "
                    + STACustomerNormalizer.MAXIMUM_PIN_WIDTH + " characters.");
            }
        }
        return returnMessage.toString();
    }
}
