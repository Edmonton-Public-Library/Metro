
package site;

import site.edmonton.EPLCustomerNormalizer;
import java.util.Properties;
import metro.common.ConfigFileTypes;
import metro.common.LibraryPropertyTypes;
import mecard.customer.Customer;
import mecard.exception.UnsupportedLibraryException;
import metro.common.PropertyReader;
import static site.MeCardPolicy.DEBUG;
import site.stalbert.STACustomerNormalizer;
import site.strathcona.STRCustomerNormalizer;

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
        Properties props = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        String libCode = props.getProperty(LibraryPropertyTypes.LIBRARY_CODE.toString());
        if (libCode.equalsIgnoreCase(MemberTypes.EPL.name()))
        {
            normalizer = new EPLCustomerNormalizer(DEBUG);
        } 
        else if (libCode.equalsIgnoreCase(MemberTypes.STA.name()))
        {
            normalizer = new STACustomerNormalizer(DEBUG);
        }
        else if (libCode.equalsIgnoreCase(MemberTypes.STR.name()))
        {
            normalizer = new STRCustomerNormalizer(DEBUG);
        }
        else
        {
            throw new UnsupportedLibraryException(libCode);
        }
        return normalizer;
    }
}
