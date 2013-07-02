package api;
import mecard.ResponseTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrew
 */
public class ResponseTest
{
    
    public ResponseTest()
    {
    }


    /**
     * Test of toString method, of class Response.
     */
    @Test
    public void testToString()
    {
        System.out.println("==toString==");
        Response response = new Response();
        response.setResponse("Test string.");
        String expResult = "{\"code\":\"INIT\",\"responseMessage\":\"Test string.\"}";
        String result = response.toString();
        System.out.println("RESPONSE:"+response);
        assertEquals(expResult, result);
    }
}