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
        String thisAddress = "5 St. Anne St., St. Albert, AB, T8N 3Z9 780-433-5567";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        Address2 instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address1 ===");
        thisAddress = "7 Sir Winston Churchill Square Edmonton, AB T5J 2V4";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address2 ===");
        thisAddress = "1277 Elgin Cres., Ft. Saskatchewan AB L6h2j9";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address3 ===");
        thisAddress = "364 8915 147 Avenue NW Edmonton, AB T5E 5Y5 780-702-3114";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address4 ===");
        thisAddress = "10255 PRINCESS ELIZABETH AVENUE, Edmonton, Ab, T5G 0Y1";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address5 ===");
        thisAddress = "209-10511 42 Avenue Edmonton, AB T6J 7G8 780-318-6314";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        
        System.out.println("=== Address6 ===");
        thisAddress = "7705 154 Street NW Edmonton, AB, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
    }
}