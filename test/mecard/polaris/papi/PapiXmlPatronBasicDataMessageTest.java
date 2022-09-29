/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either httpVersion 2 of the License, or
 * (at your option) any later httpVersion.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 */
package mecard.polaris.papi;

import mecard.polaris.papi.PapiXmlPatronBasicDataResponse;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class PapiXmlPatronBasicDataMessageTest
{

    private final String xml;
    
    public PapiXmlPatronBasicDataMessageTest()
    {
        xml = "<PatronBasicDataGetResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "<PAPIErrorCode>0</PAPIErrorCode>\n" +
            "<ErrorMessage>\n" +
            "</ErrorMessage>\n" +
            "<PatronBasicData>\n" +
            "  <Barcode>21221012345678</Barcode>\n" +
            "  <NameFirst>Billy</NameFirst>\n" +
            "  <NameLast>Balzac</NameLast>\n" +
            "  <NameMiddle i:nil=\"true\" />\n" +
            "  <PhoneNumber>555-1212</PhoneNumber>\n" +
            "  <EmailAddress>dude@hotmail.com</EmailAddress>\n" +
            "  <BirthDate i:nil=\"true\" />\n" +
            "  <PatronAddresses>\n" +
            "    <PatronAddress>\n" +
            "      <AddressID>339329</AddressID>\n" +
            "      <FreeTextLabel>Home</FreeTextLabel>\n" +
            "      <StreetOne>11811 74 Ave.</StreetOne>\n" +
            "      <City>Edmonton</City>\n" +
            "      <State>AB</State>\n" +
            "      <PostalCode>90210</PostalCode>\n" +
            "      <Country>USA</Country>\n" +
            "      <AddressTypeID>2</AddressTypeID>\n" +
            "    </PatronAddress>\n" +
            "  </PatronAddresses>\n" +
            "  <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>\n" +
            "  <PatronNotes i:nil=\"true\" />\n" +
            "  <PatronSystemBlocks />\n" +
            "</PatronBasicData>\n" +
            "</PatronBasicDataGetResult>";
    }

    /**
     * Test of getBarcode method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetBarcode()
    {
        System.out.println("getBarcode");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "21221012345678";
        String result = instance.getBarcode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNameFirst method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetNameFirst()
    {
        System.out.println("getNameFirst");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "Billy";
        String result = instance.getNameFirst();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNameLast method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetNameLast()
    {
        System.out.println("getNameLast");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "Balzac";
        String result = instance.getNameLast();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNameMiddle method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetNameMiddle()
    {
        System.out.println("getNameMiddle");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "";
        String result = instance.getNameMiddle();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPhoneNumber method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetPhoneNumber()
    {
        System.out.println("getPhoneNumber");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "555-1212";
        String result = instance.getPhoneNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmailAddress method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetEmailAddress()
    {
        System.out.println("getEmailAddress");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "dude@hotmail.com";
        String result = instance.getEmailAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStreetOne method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetStreetOne()
    {
        System.out.println("getStreetOne");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "11811 74 Ave.";
        String result = instance.getStreetOne();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCity method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetCity()
    {
        System.out.println("getCity");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "Edmonton";
        String result = instance.getCity();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPostalCode method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetPostalCode()
    {
        System.out.println("getPostalCode");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "90210";
        String result = instance.getPostalCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCountry method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetCountry()
    {
        System.out.println("getCountry");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "USA";
        String result = instance.getCountry();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBirthDate method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetBirthDate()
    {
        System.out.println("getBirthDate");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "";
        String result = instance.getBirthDate();
        assertEquals(expResult, result);
    }

    /**
     * Test of getExpiryDate method, of class PapiXmlPatronBasicDataResponse.
     */
    @Test
    public void testGetExpiryDate()
    {
        System.out.println("getExpiryDate");
        PapiXmlPatronBasicDataResponse instance = new PapiXmlPatronBasicDataResponse(xml);
        String expResult = "2022-07-30T19:38:30";
        String result = instance.getExpiryDate();
        assertEquals(expResult, result);
    }
    
}
