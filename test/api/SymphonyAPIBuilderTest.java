package api;

import mecard.util.Command;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SymphonyAPIBuilderTest
{
    
    public SymphonyAPIBuilderTest()
    {
    }

    /**
     * Test of getUser method, of class SymphonyAPIBuilder.
     */
    @Test
    public void testGetUser()
    {
        System.out.println("==getUser==");
        String userId = "21221012345678";
        String userPin = "64058";
        StringBuffer responseBuffer = new StringBuffer();
        SymphonyAPIBuilder api = new SymphonyAPIBuilder();
        Command command = api.getUser(userId, userPin, responseBuffer);
        System.out.println("CMD:" + command.toString());
    }

    /**
     * Test of getFormatter method, of class SymphonyAPIBuilder.
     */
    @Test
    public void testGetFormatter()
    {
        System.out.println("==getFormatter==");
        SymphonyAPIBuilder instance = new SymphonyAPIBuilder();
        assertTrue(instance.getFormatter() != null);
    }
}