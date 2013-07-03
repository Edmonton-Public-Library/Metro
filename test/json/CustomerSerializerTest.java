
package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mecard.customer.Customer;
import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class CustomerSerializerTest
{
    
    public CustomerSerializerTest()
    {
    }

    /**
     * Test of serialize method, of class CustomerSerializer.
     */
    @Test
    public void testSerialize()
    {
        System.out.println("serialize");
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Customer.class, new CustomerSerializer());
        Gson gson = gsonBuilder.create();
        
        Customer c = new Customer();
        System.out.println(">>>"+gson.toJson(c));
    }
}