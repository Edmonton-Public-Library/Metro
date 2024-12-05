
package mecard.sirsidynix.sdapi;


import static org.junit.Assert.*;

import mecard.sirsidynix.sdapi.SDapiAuthenticationData;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class SDapiAuthenticationDataTest 
{

    /**
     * Test of getPatronAuthentication method, of class SDapiAuthenticationData.
     */
    @Test
    public void testGetPatronAuthentication() 
    {
        System.out.println("==getPatronAuthentication==");
        String userId = "1234";
        String password = "abcd";
        SDapiAuthenticationData instance = new SDapiAuthenticationData();
        String expResult = """
                           {"login": "1234", "password": "abcd"}""";
        String result = instance.getPatronAuthentication(userId, password);
//        System.out.println("result:" + result);
//        System.out.println("xesult:" + expResult);
//        System.out.println("compareTo:" + expResult.compareTo(result));
        assertTrue(expResult.compareTo(result) == 0);
    }

    /**
     * Test of getStaffAuthentication method, of class SDapiAuthenticationData.
     */
    @Test
    public void testGetStaffAuthentication() 
    {
        System.out.println("==getStaffAuthentication==");
        String userId = "StaffPerson";
        String password = "Sup3rS3cr3tPassord!";
        SDapiAuthenticationData instance = new SDapiAuthenticationData();
        String expResult = """
                           {"login": "StaffPerson", "password": "Sup3rS3cr3tPassord!"}""";
        String result = instance.getStaffAuthentication("", userId, password);
//        System.out.println("result:" + result);
//        System.out.println("xesult:" + expResult);
//        System.out.println("compareTo:" + expResult.compareTo(result));
        assertTrue(expResult.compareTo(result) == 0);
    }
    
}
