
package mecard.security;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class SitePasswordRestrictionsTest
{
    
    public SitePasswordRestrictionsTest()
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
     * Test of requiresHashedPassword method, of class SitePasswordRestrictions.
     */
    @Test
    public void testRequiresHashedPassword()
    {
        System.out.println("requiresHashedPassword");
        SitePasswordRestrictions instance = new SitePasswordRestrictions();
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
     * Test of checkPassword method, of class SitePasswordRestrictions.
     */
    @Test
    public void testCheckPassword()
    {
        System.out.println("checkPassword");
        SitePasswordRestrictions instance = new SitePasswordRestrictions();
        assertEquals("Hello7", instance.checkPassword("Hello7"));
        assertEquals("2054162789", instance.checkPassword("1234567890"));
        String password = "1234567890";
        password = instance.checkPassword(password);
        assertEquals("2054162789", password);
    }
    
    @Test
    public void testGetHashedPassword()
    {
        System.out.println("==testGetHashedPassword==");
        String input = "abcdefg";
        int maxLen = 5;
        SitePasswordRestrictions passwordChecker = new SitePasswordRestrictions();
        String output= passwordChecker.getHashedPassword(input, maxLen);
        System.out.println("hashed password: '" + output + "'");
        assertTrue(output.length() == 5);
        assertTrue(output.compareTo("91356") == 0);
        
        input = "abcdefg";
        maxLen = 10;
        output= passwordChecker.getHashedPassword(input, maxLen);
        System.out.println("hashed password: '" + output + "'");
        assertTrue(output.length() == 10);
        assertTrue(output.compareTo("1206291356") == 0);
        
        input = "7konnmrfdudQROEQTZ";
        output= passwordChecker.getHashedPassword(input, 3);
        System.out.println("hashed password: '" + output + "'");
        assertTrue(output.length() == 3);
        assertTrue(output.compareTo("349") == 0);
        
        output= passwordChecker.getHashedPassword(input, 20);
        System.out.println("hashed password: '" + output + "'");
        assertTrue(output.length() == 19);
        assertTrue(output.compareTo("0000000000916722349") == 0);
    }
    
    /**
     * This test allows testing of various settings in the 
     * environment.properties file.
     * 
     * Adjust what you like there and repeat tests. Note that some changes
     * could cause other tests above to fail.
     */
    @Test
    public void testVariousPasswordCharacters()
    {
        System.out.println("==testVariousPasswordCombos==");
        SitePasswordRestrictions passwordChecker = new SitePasswordRestrictions();
        String password = "this that";
        int passwordLen = 10;
        if (passwordChecker.requiresHashedPassword(password))
        {
            System.out.println("password not allowed and hashed to " + 
                passwordChecker.getHashedPassword(password, passwordLen));
        }
        else
        {
            System.out.println("password allowed");
        }
    }

    /**
     * Test of getMaxLength method, of class SitePasswordRestrictions.
     */
    @Test
    public void testGetMaxLength() 
    {
        System.out.println("==getMaxLength==");
        SitePasswordRestrictions instance = new SitePasswordRestrictions();
        int expResult = 10;
        int result = instance.getMaxLength();
        assertEquals(expResult, result);
//        comment out either above for testing default or below for the environment.properties value set.
//        int expResult = 256;
//        int result = instance.getMaxLength();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMinLength method, of class SitePasswordRestrictions.
     */
    @Test
    public void testGetMinLength() 
    {
        System.out.println("==getMinLength==");
        SitePasswordRestrictions instance = new SitePasswordRestrictions();
        int expResult = 3;
        int result = instance.getMinLength();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllowedCharacters method, of class SitePasswordRestrictions.
     */
    @Test
    public void testGetAllowedCharacters() {
        System.out.println("==getAllowedCharacters==");
        SitePasswordRestrictions instance = new SitePasswordRestrictions();
        System.out.println("The allowed password characters are:" + instance.getAllowedCharacters());
    }
}
