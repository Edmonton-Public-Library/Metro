package json;
import api.Request;
import api.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.io.StringReader;
import mecard.customer.Customer;

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
        System.out.println("==deserialize==");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Request.class, new RequestDeserializer());
        Gson gson = gsonBuilder.create();
        Reader data = new StringReader(testRequest);
        Request request = gson.fromJson(data, Request.class);
//        System.out.println("..."+request); // which should look the same since toString serializes a JSON too.
        assertTrue(request.toString().compareToIgnoreCase("[\"NULL\", \"\", \"\"]") == 0);
        
        data = new StringReader("{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"60458\",\"customer\":\"null\"}");
        request = gson.fromJson(data, Request.class);
//        System.out.println("..."+request); // which should look the same since toString serializes a JSON too.
        assertTrue(request.toString().compareToIgnoreCase("[\"GET_CUSTOMER\", \"21221012345678\", \"60458\"]") == 0);
        
        data = new StringReader("{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"\",\"pin\":\"\",\"customer\":\"{\\\"ID\\\":\\\"X\\\",\\\"PIN\\\":\\\"X\\\",\\\"NAME\\\":\\\"X\\\",\\\"STREET\\\":\\\"X\\\",\\\"CITY\\\":\\\"X\\\",\\\"PROVINCE\\\":\\\"X\\\",\\\"POSTALCODE\\\":\\\"X\\\",\\\"GENDER\\\":\\\"X\\\",\\\"EMAIL\\\":\\\"X\\\",\\\"PHONE\\\":\\\"X\\\",\\\"DOB\\\":\\\"X\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"X\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"DEFAULT\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"X\\\",\\\"ISMINAGE\\\":\\\"X\\\",\\\"ISRECIPROCAL\\\":\\\"X\\\",\\\"ISRESIDENT\\\":\\\"X\\\",\\\"ISGOODSTANDING\\\":\\\"X\\\",\\\"ISLOSTCARD\\\":\\\"X\\\",\\\"FIRSTNAME\\\":\\\"X\\\",\\\"LASTNAME\\\":\\\"X\\\"}\"}");
        request = gson.fromJson(data, Request.class);
        System.out.println("..."+request); // which should look the same since toString serializes a JSON too.
        assertTrue(request.toString().compareToIgnoreCase("[\"CREATE_CUSTOMER\", \"\", \"\", \"[X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X]\"]") == 0);
    }

    /**
     * Test of getDeserializedRequest method, of class RequestDeserializer.
     */
    @Test
    public void testGetDeserializedRequest()
    {
        System.out.println("==getDeserializedRequest==");
        RequestDeserializer serializer = new RequestDeserializer();
        String requestString = "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"zaqwsx123456\",\"pin\":\"64058\",\"userId\":\"21221012345678\",\"customer\":\"null\"}";
        
        Request request = serializer.getDeserializedRequest(requestString);
        System.out.println("REQUEST:"+request.toString());
        System.out.println();
        String expectedRequestString = "[\"GET_CUSTOMER\", \"21221012345678\", \"64058\"]";
        assertTrue(expectedRequestString.compareTo(request.toString()) == 0);
    }
}