package api;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.responder.Responder;



/**
 * Most SIPRequest objects can only do a subset of all requests so this adaptor
 * is used to screen out requests that don't make sense for the type of ILSRequestBuilder
 * object. Example: SIPRequestBuilder cannot create or update customer information,
 * and like-wise, SymphonyAPIBuilder doesn't have an easy way to query the ILS for 
 * it's status.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class ILSRequestAdaptor implements ILSRequestBuilder
{

    @Override
    public Command getCustomer(String userId, String userPin, Response response)
    {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public CustomerFormatter getFormatter()
    {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public Command createUser(Customer customer, Response response)
    {
        throw new UnsupportedOperationException("Not supported."); 
    }
    
    @Override
    public Command updateUser(Customer customer, Response response)
    {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public Command getStatus(Response response)
    {
        throw new UnsupportedOperationException("Not supported."); 
    }

}
