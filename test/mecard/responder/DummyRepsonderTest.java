
package mecard.responder;
import api.Request;
import java.util.List;
import mecard.QueryTypes;
import mecard.ResponseTypes;
import org.junit.Test;



import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class DummyRepsonderTest
{
    
    private Request statusRequest;
    private Request customerRequest;
    private Request createRequest;
    private Request updateRequest;
    private String jsonCustomer;
    
    public DummyRepsonderTest()
    {
        jsonCustomer = "[\"RA1\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\"Edmonton\","
                + "\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\",\"7804964058\",\"19750822\","
                + "\"20140602\",\"Balzac\",\"Billy\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"Balzac\",\"Billy\"]";
        
        String cmd = "[\"QA0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
        statusRequest = new Request(cmd);
        cmd = "[\"QB0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\"]";
        customerRequest = new Request(cmd);
        String custReq =
            "[\"QC0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\""
            + "Edmonton\",\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\",\"7804964058\",\"19750822\",\""
            + "20140602\",\"Balzac\",\"Billy\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"Balzac\",\"Billy\"]";
        createRequest = new Request(custReq);
        custReq =
            "[\"QD0\",\"55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d\",\"21221012345678\",\"6058\",\"Billy, Balzac\",\"12345 123 St.\",\""
            + "Edmonton\",\"Alberta\",\"H0H 0H0\",\"M\",\"ilsteam@epl.ca\",\"7804964058\",\"19750822\",\""
            + "20140602\",\"Balzac\",\"Billy\",\"Y\",\"Y\",\"N\",\"Y\",\"Y\",\"N\",\"Balzac\",\"Billy\"]";
        updateRequest = new Request(custReq);
    }

    /**
     * Test of getResponse method, of class DummyRepsonder.
     */
    @Test
    public void testGetResponse()
    {
        System.out.println("==getResponse==");
        DummyRepsonder responder = new DummyRepsonder(statusRequest, true);
        String expResult = "[\"RA1\"]";
        String result = responder.getResponse();
        assertEquals(expResult, result);
        
        responder = new DummyRepsonder(createRequest, true);
        expResult = "[\"RA1\"]";
        result = responder.getResponse();
        assertEquals(expResult, result);
        
        responder = new DummyRepsonder(updateRequest, true);
        expResult = "[\"RA1\"]";
        result = responder.getResponse();
        assertEquals(expResult, result);
        
        responder = new DummyRepsonder(customerRequest, true);
        expResult = jsonCustomer;
        result = responder.getResponse();
        assertEquals(expResult, result);
    }

    /**
     * Test of convertToResponseType method, of class DummyRepsonder.
     */
    @Test
    public void testConvertToResponseType()
    {
        System.out.println("==convertToResponseType==");
        for (ResponseTypes rType: ResponseTypes.values())
        {
            ResponseTypes expResult = rType;
            ResponseTypes result = DummyRepsonder.convertToResponseType(rType.toString());
            assertEquals(expResult, result);
        }
        ResponseTypes expResult = ResponseTypes.UNKNOWN;
        ResponseTypes result = DummyRepsonder.convertToResponseType("totally off the mark");
        assertEquals(expResult, result);
    }
}