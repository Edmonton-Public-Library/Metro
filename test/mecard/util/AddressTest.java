/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class AddressTest
{
    
    public AddressTest()
    {
    }

    /**
     * Test of getStreet method, of class Address.
     */
    @Test
    public void testAddress()
    {
        System.out.println("=== Address0 ===");
        Address instance = new Address("5 St. Anne St., St. Albert, AB, T8N 3Z9", true);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address1 ===");
        instance = new Address("7 Sir Winston Churchill Square Edmonton, AB T5J 2V4", true);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address1 ===");
        instance = new Address("1277 Elgin Cres., Ft. Saskatchewan AB L6h2j9", true);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("CONTENT:'"+instance+"'");
    }
}