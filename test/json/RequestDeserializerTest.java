package json;
import api.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class RequestDeserializerTest
{
    private final String testRequest;
    
    public RequestDeserializerTest()
    {
        this.testRequest = "{\"code\":\"NULL\",\"authorityToken\":\"\",\"userId\":\"\",\"pin\":\"\",\"customer\":\"null\"}";
    }

    /**
     * Test of deserialize method, of class RequestDeserializer.
     */
    @Test
    public void testDeserialize()
    {
        System.out.println("deserialize");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Request.class, new RequestDeserializer());
        Gson gson = gsonBuilder.create();
        Reader data = new StringReader(testRequest);
        Request request = gson.fromJson(data, Request.class);
        System.out.println("..."+request); // which should look the same since toString serializes a JSON too.
        assertTrue(request.toString().compareToIgnoreCase(testRequest) == 0);
    }
}