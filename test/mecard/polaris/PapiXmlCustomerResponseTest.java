/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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

package mecard.polaris;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class PapiXmlCustomerResponseTest
{

    private final String xml;
    
    public PapiXmlCustomerResponseTest()
    {
        xml = "<PatronBasicDataGetResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <PAPIErrorCode>0</PAPIErrorCode>\n" +
                "  <ErrorMessage>\n" +
                "  </ErrorMessage>\n" +
                "  <PatronBasicData>\n" +
                "    <PatronID>2022</PatronID>\n" +
                "    <Barcode>21221012345678</Barcode>\n" +
                "    <NameFirst>Billy</NameFirst>\n" +
                "    <NameLast>Balzac</NameLast>\n" +
                "    <NameMiddle i:nil=\"true\" />\n" +
                "    <PhoneNumber>555-1212</PhoneNumber>\n" +
                "    <EmailAddress>dude@hotmail.com</EmailAddress>\n" +
                "    <ItemsOutCount>0</ItemsOutCount>\n" +
                "    <ItemsOverdueCount>0</ItemsOverdueCount>\n" +
                "    <ItemsOutLostCount>0</ItemsOutLostCount>\n" +
                "    <HoldRequestsTotalCount>0</HoldRequestsTotalCount>\n" +
                "    <HoldRequestsCurrentCount>0</HoldRequestsCurrentCount>\n" +
                "    <HoldRequestsShippedCount>0</HoldRequestsShippedCount>\n" +
                "    <HoldRequestsHeldCount>0</HoldRequestsHeldCount>\n" +
                "    <HoldRequestsUnclaimedCount>0</HoldRequestsUnclaimedCount>\n" +
                "    <ChargeBalance>0.0000</ChargeBalance>\n" +
                "    <CreditBalance>0.0000</CreditBalance>\n" +
                "    <DepositBalance>0.0000</DepositBalance>\n" +
                "    <NameTitle i:nil=\"true\" />\n" +
                "    <NameSuffix i:nil=\"true\" />\n" +
                "    <PhoneNumber2 i:nil=\"true\" />\n" +
                "    <PhoneNumber3 i:nil=\"true\" />\n" +
                "    <Phone1CarrierID>0</Phone1CarrierID>\n" +
                "    <Phone2CarrierID>0</Phone2CarrierID>\n" +
                "    <Phone3CarrierID>0</Phone3CarrierID>\n" +
                "    <CellPhone i:nil=\"true\" />\n" +
                "    <CellPhoneCarrierID>0</CellPhoneCarrierID>\n" +
                "    <AltEmailAddress i:nil=\"true\" />\n" +
                "    <BirthDate i:nil=\"true\" />\n" +
                "    <RegistrationDate>2022-07-05T00:00:00</RegistrationDate>\n" +
                "    <LastActivityDate>2022-07-05T17:01:14.217</LastActivityDate>\n" +
                "    <AddrCheckDate>2027-07-05T00:00:00</AddrCheckDate>\n" +
                "    <MessageNewCount>0</MessageNewCount>\n" +
                "    <MessageReadCount>0</MessageReadCount>\n" +
                "    <PatronOrgID>3</PatronOrgID>\n" +
                "    <PatronCodeID>1</PatronCodeID>\n" +
                "    <DeliveryOptionID>1</DeliveryOptionID>\n" +
                "    <ExcludeFromAlmostOverdueAutoRenew>false</ExcludeFromAlmostOverdueAutoRenew>\n" +
                "    <ExcludeFromPatronRecExpiration>false</ExcludeFromPatronRecExpiration>\n" +
                "    <ExcludeFromInactivePatron>false</ExcludeFromInactivePatron>\n" +
                "    <EReceiptOptionID>0</EReceiptOptionID>\n" +
                "    <TxtPhoneNumber>0</TxtPhoneNumber>\n" +
                "    <EmailFormatID>2</EmailFormatID>\n" +
                "    <LegalNameFirst>Johnathan</LegalNameFirst>\n" +
                "    <LegalNameLast>Smith</LegalNameLast>\n" +
                "    <LegalNameMiddle>Edward</LegalNameMiddle>\n" +
                "    <UseLegalNameOnNotices>true</UseLegalNameOnNotices>\n" +
                "    <LegalFullName>Smith, Johnathan Edward</LegalFullName>\n" +
                "    <PatronAddresses>\n" +
                "      <PatronAddress>\n" +
                "        <AddressID>339329</AddressID>\n" +
                "        <FreeTextLabel>Home</FreeTextLabel>\n" +
                "        <StreetOne>11811 74 Ave.</StreetOne>\n" +
                "        <StreetTwo i:nil=\"true\" />\n" +
                "        <StreetThree i:nil=\"true\" />\n" +
                "        <City>Edmonton</City>\n" +
                "        <State>AB</State>\n" +
                "        <County>\n" +
                "        </County>\n" +
                "        <PostalCode>90210</PostalCode>\n" +
                "        <ZipPlusFour i:nil=\"true\" />\n" +
                "        <Country>USA</Country>\n" +
                "        <CountryID>1</CountryID>\n" +
                "        <AddressTypeID>2</AddressTypeID>\n" +
                "      </PatronAddress>\n" +
                "    </PatronAddresses>\n" +
                "    <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>\n" +
                "    <RequestPickupBranchID>0</RequestPickupBranchID>\n" +
                "    <User1 i:nil=\"true\" />\n" +
                "    <User2 i:nil=\"true\" />\n" +
                "    <User3 i:nil=\"true\" />\n" +
                "    <User4 i:nil=\"true\" />\n" +
                "    <User5 i:nil=\"true\" />\n" +
                "    <PatronNotes i:nil=\"true\" />\n" +
                "    <PatronSystemBlocks />\n" +
                "  </PatronBasicData>\n" +
                "</PatronBasicDataGetResult>";
    }

    /**
     * Test of getCustomerProfile method, of class PapiXmlCustomerResponse.
     */
    @Test
    public void testGetCustomerProfile()
    {
        System.out.println("getCustomerProfile");
        PapiXmlCustomerResponse instance = new PapiXmlCustomerResponse(xml, true);
        String expResult = "";
        // The web service doesn't send back profile but may be encoded in 
        // the customer note field in the future.
        String result = instance.getCustomerProfile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getField method, of class PapiXmlCustomerResponse.
     */
    @Test
    public void testGetField()
    {
        System.out.println("getField");
        String fieldName = PapiElementOrder.BARCODE.toString();
        PapiXmlCustomerResponse instance = new PapiXmlCustomerResponse(xml, true);
        String expResult = "21221012345678";
        String result = instance.getField(fieldName);
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class PapiXmlCustomerResponse.
     */
    @Test
    public void testIsEmpty()
    {
        System.out.println("isEmpty");
        String fieldName = PapiElementOrder.BIRTHDATE.toString();
        PapiXmlCustomerResponse instance = new PapiXmlCustomerResponse(xml, true);
        boolean expResult = true;
        boolean result = instance.isEmpty(fieldName);
        assertEquals(expResult, result);
    }

    /**
     * Test of getStanding method, of class PapiXmlCustomerResponse.
     */
    @Test
    public void testGetStanding()
    {
        System.out.println("getStanding");
        PapiXmlCustomerResponse instance = new PapiXmlCustomerResponse(xml, true);
        String expResult = "";
        String result = instance.getStanding();
        assertEquals(expResult, result);
    }

    /**
     * Test of cardReportedLost method, of class PapiXmlCustomerResponse.
     */
    @Test
    public void testCardReportedLost()
    {
        System.out.println("cardReportedLost");
        PapiXmlCustomerResponse instance = new PapiXmlCustomerResponse(xml, true);
        boolean expResult = false;
        boolean result = instance.cardReportedLost();
        assertEquals(expResult, result);
    }

    /**
     * Test of isInGoodStanding method, of class PapiXmlCustomerResponse.
     */
    @Test
    public void testIsInGoodStanding()
    {
        System.out.println("isInGoodStanding");
        PapiXmlCustomerResponse instance = new PapiXmlCustomerResponse(xml, true);
        boolean expResult = true;
        boolean result = instance.isInGoodStanding();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDateField method, of class PapiXmlCustomerResponse.
     */
    @Test
    public void testGetDateField()
    {
        System.out.println("getDateField");
        String fieldName = PapiElementOrder.EXPIRATION_DATE.toString();
        PapiXmlCustomerResponse instance = new PapiXmlCustomerResponse(xml, true);
        String expResult = "20220730";
        String result = instance.getDateField(fieldName);
        assertEquals(expResult, result);
    }
    
}
