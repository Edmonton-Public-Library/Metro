package api;
import mecard.customer.Customer;
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
        String expResult = "[INIT, \"Test string.\"]";
        System.out.println("RESPONSE:"+response);
        assertTrue(expResult.compareTo(response.toString()) == 0);
        
        expResult = "[INIT, \"Test string.\", \"\"]";
        response.setCustomer(new Customer());
        System.out.println("RESPONSE:"+response);
        assertTrue(expResult.compareTo(response.toString()) == 0);
    }
}