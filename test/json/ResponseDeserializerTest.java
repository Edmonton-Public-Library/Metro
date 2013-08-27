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
        this.testRequest = "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"userId\":\"\",\"pin\":\"\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\","
                    + "\\\"PIN\\\":\\\"4058\\\","
                    + "\\\"PREFEREDNAME\\\":\\\"Billy, Fembot\\\","
                    + "\\\"STREET\\\":\\\"12345 123 St.\\\","
                    + "\\\"CITY\\\":\\\"Edmonton\\\","
                    + "\\\"PROVINCE\\\":\\\"Alberta\\\","
                    + "\\\"POSTALCODE\\\":\\\"H0H 0H0\\\","
                    + "\\\"SEX\\\":\\\"F\\\","
                    + "\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\","
                    + "\\\"PHONE\\\":\\\"7804964058\\\","
                    + "\\\"DOB\\\":\\\"19750822\\\","
                    + "\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\","
                    + "\\\"RESERVED\\\":\\\"X\\\","
                    + "\\\"ALTERNATE_ID\\\":\\\"X\\\","
                    + "\\\"ISVALID\\\":\\\"Y\\\","
                    + "\\\"ISMINAGE\\\":\\\"Y\\\","
                    + "\\\"ISRECIPROCAL\\\":\\\"N\\\","
                    + "\\\"ISRESIDENT\\\":\\\"Y\\\","
                    + "\\\"ISGOODSTANDING\\\":\\\"Y\\\","
                    + "\\\"ISLOSTCARD\\\":\\\"N\\\","
                    + "\\\"FIRSTNAME\\\":\\\"Fembot\\\","
                    + "\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
    }

    /**
     * Test of deserialize method, of class ResponseDeserializer.
     */
    @Test
    public void testDeserialize()
    {
        System.out.println("deserialize");
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(Response.class, new ResponseDeserializer());
//        Gson gson = gsonBuilder.create();
//        Reader data = new StringReader(testRequest);
//        Request request = gson.fromJson(data, Request.class);
//        System.out.println("..."+request); // which should look the same since toString serializes a JSON too.
//        assertTrue(request.toString().compareToIgnoreCase(testRequest) == 0);
    }
}