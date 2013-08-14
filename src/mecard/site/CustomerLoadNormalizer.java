
package mecard.site;

import epl.EPLCustomerNormalizer;
import java.util.Properties;
import mecard.MetroService;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.customer.Customer;
import mecard.exception.UnsupportedLibraryException;
import static mecard.site.MeCardPolicy.DEBUG;
import sta.STACustomerNormalizer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public abstract class CustomerLoadNormalizer 
{
    private static CustomerLoadNormalizer normalizer;
    protected CustomerLoadNormalizer()
    {
        normalizer = null;
    }
    
    /**
     * Normalizes the customer data to local load restrictions like minimum PIN 
     * width.
     * @param c customer object must not be null.
     * @return String message of what was changed that the customer should know about.
     * In the above example they would be notified that St. Albert has their 
     * last 4 digits of their EPL Pin saved.
     */
    public abstract String normalize(Customer c);
            
    public static CustomerLoadNormalizer getInstanceOf(boolean debug)
    {
        if (normalizer != null)
        {
            return normalizer;
        }
        // read the config, find what type of normalizer we need, create it 
        // and return it.
        DEBUG = debug;
        Properties props = MetroService.getProperties(ConfigFileTypes.ENVIRONMENT);
        String libCode = props.getProperty(LibraryPropertyTypes.LIBRARY_CODE.toString());
        if (libCode.equalsIgnoreCase(MemberTypes.EPL.name()))
        {
            normalizer = new EPLCustomerNormalizer(DEBUG);
        } 
        else if (libCode.equalsIgnoreCase(MemberTypes.STA.name()))
        {
            normalizer = new STACustomerNormalizer(DEBUG);
        }
        else
        {
            throw new UnsupportedLibraryException(libCode);
        }
        return normalizer;
    }
}
