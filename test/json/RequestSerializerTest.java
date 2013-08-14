package json;
import mecard.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class RequestSerializerTest
{
    
    public RequestSerializerTest()
    {
    }

    /**
     * Test of serialize method, of class RequestSerializer.
     */
    @Test
    public void testSerialize()
    {
        System.out.println("serialize");
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Request.class, new RequestSerializer());
        Gson gson = gsonBuilder.create();
        
        Request request = new Request();
        System.out.println(">>>"+gson.toJson(request));
    }
}