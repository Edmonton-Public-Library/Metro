
package mecard.security;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class MD5Test
{
    
    public MD5Test()
    {
    }

    /**
     * Test of getHash method, of class MD5.
     */
    @Test
    public void testGetHash()
    {
        System.out.println("==getHash==");
        String customerData = "11811 74 Ave.";
        String expResult = "015ec30556ae3ebda4e47b4c7dc17639";
        String result = MD5.getHash(customerData);
        System.out.println("Digest(in hex format):: " + MD5.getHash(result));
        assertEquals(expResult, result);
    }
}