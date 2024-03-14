
package mecard.security;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class CustomerPasswordRestrictionsTest
{
    
    public CustomerPasswordRestrictionsTest()
    {
        // The success of these tests depends on 3 OPTIONAL entries in the 
        // environment.properties file.
        // This describes the maximum length of a password. In the case of
        // WRBL they have an unusual restriction of '16' digits.
        //    <entry key="password-max-length">10</entry>
        // This describes the minimum length of the password; typically this
        // value would be '4'.
        //    <entry key="password-min-length">3</entry>
        // This entry includes all the valid characters allowed by the site's
        // password restriction policy. In this case only e-l-o-H and the digits
        // from 1-8 are allowed.
        //    <entry key="allowed-password-characters">eloH12345678</entry>
    }


    /**
     * Test of requiresHashedPassword method, of class CustomerPasswordRestrictions.
     */
    @Test
    public void testRequiresHashedPassword()
    {
        System.out.println("requiresHashedPassword");
        CustomerPasswordRestrictions instance = new CustomerPasswordRestrictions();
        assertEquals(false, instance.requiresHashedPassword("Hello"));
        // This will pass if you add the following entry to the environment.properties
        // in the projects root directory:
        // <entry key="password-max-length">10</entry>
        assertEquals(true, instance.requiresHashedPassword("HelloIMustbeGoing"));
        // This will pass if you add the following entry to the environment.properties
        // in the projects root directory:
        // <entry key="password-min-length">3</entry>
        assertEquals(false, instance.requiresHashedPassword("Hel"));
        assertEquals(true, instance.requiresHashedPassword("He"));
        
        assertEquals(false, instance.requiresHashedPassword("Hello7"));
        // This will pass if you add the following entry to the environment.properties
        // in the projects root directory:
        // <entry key="allowed-password-characters">eloH7</entry>
        assertEquals(false, instance.requiresHashedPassword("Hello7"));
        
        assertEquals(true, instance.requiresHashedPassword("Hello9"));
    }

    /**
     * Test of checkPassword method, of class CustomerPasswordRestrictions.
     */
    @Test
    public void testCheckPassword()
    {
        System.out.println("checkPassword");
        CustomerPasswordRestrictions instance = new CustomerPasswordRestrictions();
        assertEquals("Hello7", instance.checkPassword("Hello7"));
        assertEquals("2054162789", instance.checkPassword("1234567890"));
        String password = "1234567890";
        password = instance.checkPassword(password);
        assertEquals("2054162789", password);
    }
    
}
