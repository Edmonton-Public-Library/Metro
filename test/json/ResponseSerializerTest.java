package json;
import mecard.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mecard.ResponseTypes;
import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;

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

    /**
     * Test of getSerializedResponse method, of class ResponseSerializer.
     */
    @Test
    public void testGetSerializedResponse()
    {
        System.out.println("==getSerializedResponse==");
        Response response = new Response();
        ResponseSerializer serializer = new ResponseSerializer();
        String expResult = "{\"code\":\"INIT\",\"responseMessage\":\"\",\"customer\":\"null\"}";
        String result = serializer.getSerializedResponse(response);
        System.out.println("RESULT:"+result);
        assertTrue(expResult.compareTo(result) == 0);
        
        expResult = "{\"code\":\"INIT\",\"responseMessage\":\"Hello World! \",\"customer\":\"null\"}";
        response.setResponse("Hello World!");
        result = serializer.getSerializedResponse(response);
        System.out.println("RESULT:"+result);
        assertTrue(expResult.compareTo(result) == 0);
        
        expResult = "{\"code\":\"INIT\",\"responseMessage\":\"Hello World! \",\"customer\":\"{\\\"ID\\\":\\\"X\\\",\\\"PIN\\\":\\\"X\\\",\\\"PREFEREDNAME\\\":\\\"X\\\",\\\"STREET\\\":\\\"X\\\",\\\"CITY\\\":\\\"X\\\",\\\"PROVINCE\\\":\\\"X\\\",\\\"POSTALCODE\\\":\\\"X\\\",\\\"SEX\\\":\\\"X\\\",\\\"EMAIL\\\":\\\"X\\\",\\\"PHONE\\\":\\\"X\\\",\\\"DOB\\\":\\\"X\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"X\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"X\\\",\\\"ISMINAGE\\\":\\\"X\\\",\\\"ISRECIPROCAL\\\":\\\"X\\\",\\\"ISRESIDENT\\\":\\\"X\\\",\\\"ISGOODSTANDING\\\":\\\"X\\\",\\\"ISLOSTCARD\\\":\\\"X\\\",\\\"FIRSTNAME\\\":\\\"X\\\",\\\"LASTNAME\\\":\\\"X\\\"}\"}";
        response.setCustomer(new Customer());
        result = serializer.getSerializedResponse(response);
        System.out.println("RESULT:"+result);
        assertTrue(expResult.compareTo(result) == 0);
        System.out.println("--REPSONSE:"+response);
    }
}