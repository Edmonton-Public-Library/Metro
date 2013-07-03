package json;
import api.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mecard.ResponseTypes;
import mecard.customer.Customer;
import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class ResponseSerializerTest
{
    
    public ResponseSerializerTest()
    {
    }

    /**
     * Test of serialize method, of class ResponseSerializer.
     */
    @Test
    public void testSerialize()
    {
        System.out.println("serialize");
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Response.class, new ResponseSerializer());
        Gson gson = gsonBuilder.create();
        
        Response response = new Response();
        response.setCode(ResponseTypes.SUCCESS);
        response.setResponse("Hello World!");
        Customer c = new Customer();
        response.setCustomer(c);
        System.out.println(">>>"+gson.toJson(response));
    }
}