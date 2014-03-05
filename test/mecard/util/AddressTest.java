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
        
        System.out.println("=== Address7 ===");
        thisAddress = "7705 154 Street NW Sturgeon County, AB, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address8 ===");
        thisAddress = "34 Maple Street NW Smoky River, AB, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address9 ===");
        // bad city name.
        thisAddress = "34 Maple Street NW Mars Bars, AB, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address10 ===");
        // bad city name.
        thisAddress = "34 Maple Street NW Mars County, AB, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address11 ===");
        // bad city name.
        thisAddress = "34 Maple Street NW Mars County, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address12 ===");
        // bad city name.
        thisAddress = "34 Maple Street NW Mars County, T5R 1R6";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address13 (Lucky 13) ===");
        // Confusing city parse looks like 'Box 20 Site 7 Rr1 Red'.
        thisAddress = "BOX 20 SITE 7 RR1, Red Deer, AB, T4N 5E1";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        
        System.out.println("=== Address14 ===");
        thisAddress = "18 Appomattox Street, apt. 9 Medicine Hat, AB T1A 3N7";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address15 ===");
        thisAddress = "31 Chansellorsville Street apt. 3 Medicine Hat, AB T1A 3N7";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address16 ===");
        // 64              00020140115    115700000000000002000000000009AOalap|AA21000004869074|AEWOLBECK, DIXIE|AQahei|BZ0249|CA0010|CB0200|BLY|BHCAD|CC10.|BDBOX 21, Heisler, AB, TOB 2A0|BEwolbeckd9@gmail.com|BF780 889-2114|DHDIXIE|DJWOLBECK|PCra|PE20140328    235900|PS20140328    235900|ZYra|AY1AZB5F8
        thisAddress = "BOX 21, Heisler, AB, T0B 2A0";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        
        System.out.println("=== Address17 ===");
        thisAddress = "BOX 43 LETHBRIDGE, ALBERTA T1J 3Y3 403-555-1234";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        assertTrue(instance.getCity().compareTo("Lethbridge") == 0);
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address18 ===");
        thisAddress = "BOX 43 DRAYTON VALLEY, ALBERTA T1J 3Y3 403-555-1234";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address2(thisAddress);
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address19 ===");
        // TODO this fails to find the right city in this case but the whole address 
        // does get parsed.
        thisAddress = "BOX 43 COUNTY OF LETHBRIDGE, ALBERTA T1J 3Y3 403-555-1234";
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