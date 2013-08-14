package json;
import mecard.Request;
import mecard.Response;
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
public class ResponseDeserializerTest
{
    private final String testRequest;
    
    public ResponseDeserializerTest()
    {
        this.testRequest = "{\"code\":\"SUCCESS\",\"responseMessage\":\"Hello World!\",\"customer\":\"{\\\"ID\\\":\\\"X\\\",\\\"PIN\\\":\\\"X\\\",\\\"NAME\\\":\\\"X\\\",\\\"STREET\\\":\\\"X\\\",\\\"CITY\\\":\\\"X\\\",\\\"PROVINCE\\\":\\\"X\\\",\\\"POSTALCODE\\\":\\\"X\\\",\\\"GENDER\\\":\\\"X\\\",\\\"EMAIL\\\":\\\"X\\\",\\\"PHONE\\\":\\\"X\\\",\\\"DOB\\\":\\\"X\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"X\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"DEFAULT\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"X\\\",\\\"ISMINAGE\\\":\\\"X\\\",\\\"ISRECIPROCAL\\\":\\\"X\\\",\\\"ISRESIDENT\\\":\\\"X\\\",\\\"ISGOODSTANDING\\\":\\\"X\\\",\\\"ISLOSTCARD\\\":\\\"X\\\",\\\"FIRSTNAME\\\":\\\"X\\\",\"LASTNAME\\\":\"X\\\"}\"}";
    }

    /**
     * Test of deserialize method, of class ResponseDeserializer.
     */
    @Test
    public void testDeserialize()
    {
        System.out.println("deserialize");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Response.class, new ResponseDeserializer());
        Gson gson = gsonBuilder.create();
        Reader data = new StringReader(testRequest);
        Request request = gson.fromJson(data, Request.class);
        System.out.println("..."+request); // which should look the same since toString serializes a JSON too.
        assertTrue(request.toString().compareToIgnoreCase(testRequest) == 0);
    }
}