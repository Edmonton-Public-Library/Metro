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
public class Address3Test
{
    
    public Address3Test()
    {
    }
    
    /**
     * Test of getStreet method, of class Address.
     */
    /////// These tests pass but are private in Address3 because they cause a warning 
    // of an overridable method being called from a constructor. To test temporarily
    // make the methods protected and uncomment the code below to run.
//    @Test
//    public void testSetPCodePhoneProvince()
//    {
//        System.out.println("=== SetPCodePhoneProvince ===");
//        String defaultStreet      = "5 St. Anne St.";
//        String defaultCity        = "St. Albert";
//        String defaultProvince    = "XX";
//        String pCode         = "A4A 4A4";
//        String defaultPCode  = "A4A4A4";
//        String defaultPhone  = "444-444-4444";
//        String thisAddress   = defaultStreet + ", " + defaultCity + ", " + defaultProvince + ", " + pCode + " " + defaultPhone;
//        System.out.println("THIS:'" + thisAddress + "'");
//        String argString = "5 St. Anne St., St. Albert, XX, A4A 4A4 444-444-4444";
//        System.out.println(" ARG:'" + argString + "'");
//        assertTrue(argString.compareTo(thisAddress) == 0);
//        
//        String phExpected = "780-433-5567";
//        String pcExpected = "T8N 3Z9";
//        String prExpected = "AB";
//        Address3 instance = new Address3(thisAddress);
//        instance.setPCodePhoneProvince(prExpected + " " + pcExpected + " " + phExpected);
//        assertTrue(instance.getPhone().compareTo(phExpected) == 0);
//        assertTrue(instance.getProvince().compareTo(prExpected) == 0);
//        assertTrue(instance.getPostalCode().compareTo(pcExpected) == 0);
//        
//        phExpected = "";
//        pcExpected = "T8N 3Z9";
//        prExpected = "AB";
//        instance = new Address3(thisAddress);
//        instance.setPCodePhoneProvince(prExpected + " " + pcExpected + " " + phExpected);
//        assertTrue(instance.getPhone().compareTo(defaultPhone) == 0);
//        assertTrue(instance.getProvince().compareTo(prExpected) == 0);
//        assertTrue(instance.getPostalCode().compareTo(pcExpected) == 0);
//        
//        phExpected = "780-433-5567";
//        pcExpected = "";
//        prExpected = "";
//        instance = new Address3(thisAddress);
//        instance.setPCodePhoneProvince(prExpected + " " + pcExpected + " " + phExpected);
//        assertTrue(instance.getPhone().compareTo(phExpected) == 0);
//        assertTrue(instance.getProvince().compareTo("AB") == 0);
//        assertTrue(instance.getPostalCode().compareTo(pCode) == 0);
//        
//        phExpected = "";
//        pcExpected = "";
//        prExpected = "";
//        instance = new Address3(thisAddress);
//        instance.setPCodePhoneProvince(prExpected + " " + pcExpected + " " + phExpected);
//        assertTrue(instance.getPhone().compareTo(defaultPhone) == 0);
//        assertTrue(instance.getProvince().compareTo("AB") == 0);
//        assertTrue(instance.getPostalCode().compareTo(pCode) == 0);
//        
//        // Commonly
//        phExpected = "";
//        pcExpected = "";
//        prExpected = "AB";
//        instance = new Address3(thisAddress);
//        instance.setPCodePhoneProvince(prExpected + " " + pcExpected + " " + phExpected);
//        assertTrue(instance.getPhone().compareTo(defaultPhone) == 0);
//        assertTrue(instance.getProvince().compareTo("AB") == 0);
//        assertTrue(instance.getPostalCode().compareTo(pCode) == 0);
//        
//        phExpected = "";
//        pcExpected = "H0H 0H0";
//        prExpected = "AB";
//        instance = new Address3(thisAddress);
//        instance.setPCodePhoneProvince(prExpected + " " + pcExpected + " " + phExpected);
//        assertTrue(instance.getPhone().compareTo(defaultPhone) == 0);
//        assertTrue(instance.getProvince().compareTo("AB") == 0);
//        assertTrue(instance.getPostalCode().compareTo(pcExpected) == 0);
//    }

    /**
     * Test of getStreet method, of class Address.
     */
    @Test
    public void testAddress()
    {
               
        System.out.println("=== Address27 ===");
        String thisAddress = "10255 PRINCESS ELIZABETH AVENUE, Sherwood Park, AB, T5G 0Y1";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        Address3 instance = new Address3(thisAddress);
        String STREET = "10255 Princess Elizeth Avenue";
        String CITY = "Sherwood Park";
        String POSTALC = "T5G 0Y1";
        String PROVINC = "AB";
        String PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address0 ===");
        thisAddress = "5 St. Anne St., St. Albert, AB, T8N 3Z9 780-433-5567";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        
        STREET = "5 St. Anne St.";
        CITY = "St. Albert";
        POSTALC = "T8N 3Z9";
        PROVINC = "AB";
        PHONE = "780-433-5567";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address1 ===");
        thisAddress = "7 Sir Winston Churchill Square Edmonton, AB T5J 2V4";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "7 Sir Winston Churchill Square";
        CITY = "Edmonton";
        POSTALC = "T5J 2V4";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
//        
//        System.out.println("=== Address2 ===");
//        thisAddress = "1277 Elgin Cres., Ft. Saskatchewan AB L6h2j9";
//        System.out.println("ADDR_COMPLETE:"+thisAddress);
//        instance = new Address3(thisAddress);
//        STREET = "1277 Elgin Cres.";
//        CITY = "Ft. Saskatchewan";
//        POSTALC = "L6H 2J9";
//        PROVINC = "AB";
//        PHONE = "X";
//        System.out.println("STREET:'"+instance.getStreet()+"'");
//        System.out.println("CITY:'"+instance.getCity()+"'");
//        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
//        System.out.println("PROVINC:'"+instance.getProvince()+"'");
//        System.out.println("PHONE:'"+instance.getPhone()+"'");
//        assertTrue(instance.getStreet().compareTo(STREET) == 0);
//        assertTrue(instance.getCity().compareTo(CITY) == 0);
//        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
//        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
//        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
//        System.out.println("CONTENT:'"+instance+"'");
//        
        System.out.println("=== Address3 ===");
        thisAddress = "364 8915 147 Avenue NW Edmonton, AB T5E 5Y5 780-702-3114";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "364 8915 147 Avenue Nw";
        CITY = "Edmonton";
        POSTALC = "T5E 5Y5";
        PROVINC = "AB";
        PHONE = "780-702-3114";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address4 ===");
        thisAddress = "10255 PRINCESS ELIZABETH AVENUE, Edmonton, Ab, T5G 0Y1";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "10255 Princess Elizabeth Avenue";
        CITY = "Edmonton";
        POSTALC = "T5G 0Y1";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address5 ===");
        thisAddress = "209-10511 42 Avenue Edmonton, AB T6J 7G8 780-318-6314";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "209-10511 42 Avenue";
        CITY = "Edmonton";
        POSTALC = "T6J 7G8";
        PROVINC = "AB";
        PHONE = "780-318-6314";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address6 ===");
        thisAddress = "7705 154 Street NW Edmonton, AB, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "7705 154 Street Nw";
        CITY = "Edmonton";
        POSTALC = "T5R 1R6";
        PROVINC = "AB";
        PHONE = "780-893-3959";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address7 ===");
        thisAddress = "7705 154 Street NW Sturgeon County, AB, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "7705 154 Street Nw";
        CITY = "Sturgeon County";
        POSTALC = "T5R 1R6";
        PROVINC = "AB";
        PHONE = "780-893-3959";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address8 ===");
        thisAddress = "34 Maple Street NW Smoky River, AB, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "34 Maple Street Nw";
        CITY = "Smoky River";
        POSTALC = "T5R 1R6";
        PROVINC = "AB";
        PHONE = "780-893-3959";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address9 ===");
        // bad city name.
        thisAddress = "34 Maple Street NW Mars Bars, AB, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "34 Maple Street Nw Mars Bars";
        CITY = "X";
        POSTALC = "T5R 1R6";
        PROVINC = "AB";
        PHONE = "780-893-3959";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address10 ===");
        // bad city name.
        thisAddress = "34 Maple Street NW Mars County, AB T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "34 Maple Street Nw Mars County";
        CITY = "X";
        POSTALC = "T5R 1R6";
        PROVINC = "AB";
        PHONE = "780-893-3959";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address11 ===");
        // bad city name.
        thisAddress = "34 Maple Street Mars County, T5R 1R6 780-893-3959";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "34 Maple Street Mars County";
        CITY = "X";
        POSTALC = "T5R 1R6";
        PROVINC = "X";
        PHONE = "780-893-3959";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address12 ===");
        // bad province.
        thisAddress = "34 Maple Street CALGARY, T5R 1R6";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "34 Maple Street";
        CITY = "Calgary";
        POSTALC = "T5R 1R6";
        PROVINC = "X";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address13 (Lucky 13) ===");
        // Confusing city parse looks like 'Box 20 Site 7 Rr1 Red'.
        thisAddress = "BOX 20 SITE 7 RR1, Red Deer, AB, T4N 5E1";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "Box 20 Site 7 Rr1";
        CITY = "Red Deer";
        POSTALC = "T4N 5E1";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address14 ===");
        thisAddress = "18 Appomattox Street, apt. 9 Medicine Hat, AB T1A 3N7";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "18 Appomattox Street Apt. 9";
        CITY = "Medicine Hat";
        POSTALC = "T1A 3N7";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address15 ===");
        thisAddress = "31 Chansellorsville Street apt. 3 Medicine Hat, AB T1A 3N7";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "31 Chansellorsville Street Apt. 3";
        CITY = "Medicine Hat";
        POSTALC = "T1A 3N7";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address16 ===");
        // 64              00020140115    115700000000000002000000000009AOalap|AA21000004869074|AEWOLBECK, DIXIE|AQahei|BZ0249|CA0010|CB0200|BLY|BHCAD|CC10.|BDBOX 21, Heisler, AB, TOB 2A0|BEwolbeckd9@gmail.com|BF780 889-2114|DHDIXIE|DJWOLBECK|PCra|PE20140328    235900|PS20140328    235900|ZYra|AY1AZB5F8
        thisAddress = "BOX 21, Heisler, AB, T0B 2A0";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "Box 21";
        CITY = "Heisler";
        POSTALC = "T0B 2A0";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        
        System.out.println("=== Address17 ===");
        thisAddress = "BOX 43 LETHBRIDGE, ALBERTA T1J 3Y3 403-555-1234";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "Box 43";
        CITY = "Lethbridge";
        POSTALC = "T1J 3Y3";
        PROVINC = "AB";
        PHONE = "403-555-1234";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address18 ===");
        thisAddress = "BOX 43 DRAYTON VALLEY, ALBERTA T1J 3Y3 403-555-1234";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
       STREET = "Box 43";
        CITY = "Drayton Valley";
        POSTALC = "T1J 3Y3";
        PROVINC = "AB";
        PHONE = "403-555-1234";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address19 ===");
        // TODO this fails to find the right city in this case but the whole address 
        // does get parsed.
        thisAddress = "BOX 43 COUNTY OF LETHBRIDGE, ALBERTA T1J 3Y3 403-555-1234";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "Box 43";
        CITY = "County Of Lethbridge";
        POSTALC = "T1J 3Y3";
        PROVINC = "AB";
        PHONE = "403-555-1234";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        // And all good libraries throw us a new bone..
        //
        System.out.println("=== Address20 ===");
        thisAddress = "123 Aisforapple Drive Calgary AB X0X 0X0";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "123 Aisforapple Drive";
        CITY = "Calgary";
        POSTALC = "X0X 0X0";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address21 ===");
        thisAddress = "616 MacLeod Trail SE Calgary AB T2G 2M2";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "616 Macleod Trail Se";
        CITY = "Calgary";
        POSTALC = "T2G 2M2";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address22 ===");
        thisAddress = "616 MacLeod Trail SE Calgary AB T2G 2M2 403-233-1234";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "616 Macleod Trail Se";
        CITY = "Calgary";
        POSTALC = "T2G 2M2";
        PROVINC = "AB";
        PHONE = "403-233-1234";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        
        
        System.out.println("=== Address23 ===");
        thisAddress = "22 Hidden Valley Garden NW Calgary AB T3A 5X3";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "22 Hidden Valley Garden Nw";
        CITY = "Calgary";
        POSTALC = "T3A 5X3";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address24 ===");
        thisAddress = "42 QUEENS RD W LETHBRIDGE, ALBERTA T1K 3W2 T403-382-9749";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "42 Queens Rd W";
        CITY = "Lethbridge";
        POSTALC = "T1K 3W2";
        PROVINC = "AB";
        PHONE = "403-382-9749";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        
        System.out.println("=== Address25 Parkland ===");
        thisAddress = "BDRR#4, SITE 141, COMP 1, Rocky Mountain House, AB, T4T 2A4";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "Bdrr#4 Site 141 Comp 1";
        CITY = "Rocky Mountain House";
        POSTALC = "T4T 2A4";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
        
        System.out.println("=== Address26 RDPL ===");
        thisAddress = "353 Primary Way Red Deer AB T4N 1T9";
        System.out.println("ADDR_COMPLETE:"+thisAddress);
        instance = new Address3(thisAddress);
        STREET = "353 Primary Way";
        CITY = "Red Deer";
        POSTALC = "T4N 1T9";
        PROVINC = "AB";
        PHONE = "X";
        System.out.println("STREET:'"+instance.getStreet()+"'");
        System.out.println("CITY:'"+instance.getCity()+"'");
        System.out.println("POSTALC:'"+instance.getPostalCode()+"'");
        System.out.println("PROVINC:'"+instance.getProvince()+"'");
        System.out.println("PHONE:'"+instance.getPhone()+"'");
        assertTrue(instance.getStreet().compareTo(STREET) == 0);
        assertTrue(instance.getCity().compareTo(CITY) == 0);
        assertTrue(instance.getPostalCode().compareTo(POSTALC) == 0);
        assertTrue(instance.getProvince().compareTo(PROVINC) == 0);
        assertTrue(instance.getPhone().compareTo(PHONE) == 0);
        System.out.println("CONTENT:'"+instance+"'");
    }
}