/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022 - 2025 Edmonton Public Library
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

package mecard.polaris.papi;

import mecard.config.PapiElementOrder;
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
        xml = """
              <PatronBasicDataGetResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
                <PAPIErrorCode>0</PAPIErrorCode>
                <ErrorMessage>
                </ErrorMessage>
                <PatronBasicData>
                  <PatronID>2022</PatronID>
                  <Barcode>21221012345678</Barcode>
                  <NameFirst>Billy</NameFirst>
                  <NameLast>Balzac</NameLast>
                  <NameMiddle i:nil="true" />
                  <PhoneNumber>555-1212</PhoneNumber>
                  <EmailAddress>dude@hotmail.com</EmailAddress>
                  <ItemsOutCount>0</ItemsOutCount>
                  <ItemsOverdueCount>0</ItemsOverdueCount>
                  <ItemsOutLostCount>0</ItemsOutLostCount>
                  <HoldRequestsTotalCount>0</HoldRequestsTotalCount>
                  <HoldRequestsCurrentCount>0</HoldRequestsCurrentCount>
                  <HoldRequestsShippedCount>0</HoldRequestsShippedCount>
                  <HoldRequestsHeldCount>0</HoldRequestsHeldCount>
                  <HoldRequestsUnclaimedCount>0</HoldRequestsUnclaimedCount>
                  <ChargeBalance>0.0000</ChargeBalance>
                  <CreditBalance>0.0000</CreditBalance>
                  <DepositBalance>0.0000</DepositBalance>
                  <NameTitle i:nil="true" />
                  <NameSuffix i:nil="true" />
                  <PhoneNumber2 i:nil="true" />
                  <PhoneNumber3 i:nil="true" />
                  <Phone1CarrierID>0</Phone1CarrierID>
                  <Phone2CarrierID>0</Phone2CarrierID>
                  <Phone3CarrierID>0</Phone3CarrierID>
                  <CellPhone i:nil="true" />
                  <CellPhoneCarrierID>0</CellPhoneCarrierID>
                  <AltEmailAddress i:nil="true" />
                  <BirthDate i:nil="true" />
                  <RegistrationDate>2022-07-05T00:00:00</RegistrationDate>
                  <LastActivityDate>2022-07-05T17:01:14.217</LastActivityDate>
                  <AddrCheckDate>2027-07-05T00:00:00</AddrCheckDate>
                  <MessageNewCount>0</MessageNewCount>
                  <MessageReadCount>0</MessageReadCount>
                  <PatronOrgID>3</PatronOrgID>
                  <PatronCodeID>1</PatronCodeID>
                  <DeliveryOptionID>1</DeliveryOptionID>
                  <ExcludeFromAlmostOverdueAutoRenew>false</ExcludeFromAlmostOverdueAutoRenew>
                  <ExcludeFromPatronRecExpiration>false</ExcludeFromPatronRecExpiration>
                  <ExcludeFromInactivePatron>false</ExcludeFromInactivePatron>
                  <EReceiptOptionID>0</EReceiptOptionID>
                  <TxtPhoneNumber>0</TxtPhoneNumber>
                  <EmailFormatID>2</EmailFormatID>
                  <LegalNameFirst>Johnathan</LegalNameFirst>
                  <LegalNameLast>Smith</LegalNameLast>
                  <LegalNameMiddle>Edward</LegalNameMiddle>
                  <UseLegalNameOnNotices>true</UseLegalNameOnNotices>
                  <LegalFullName>Smith, Johnathan Edward</LegalFullName>
                  <PatronAddresses>
                    <PatronAddress>
                      <AddressID>339329</AddressID>
                      <FreeTextLabel>Home</FreeTextLabel>
                      <StreetOne>11811 74 Ave.</StreetOne>
                      <StreetTwo i:nil="true" />
                      <StreetThree i:nil="true" />
                      <City>Edmonton</City>
                      <State>AB</State>
                      <County>
                      </County>
                      <PostalCode>90210</PostalCode>
                      <ZipPlusFour i:nil="true" />
                      <Country>USA</Country>
                      <CountryID>1</CountryID>
                      <AddressTypeID>2</AddressTypeID>
                    </PatronAddress>
                  </PatronAddresses>
                  <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>
                  <RequestPickupBranchID>0</RequestPickupBranchID>
                  <User1 i:nil="true" />
                  <User2 i:nil="true" />
                  <User3 i:nil="true" />
                  <User4 i:nil="true" />
                  <User5 i:nil="true" />
                  <PatronNotes i:nil="true" />
                  <PatronSystemBlocks />
                </PatronBasicData>
              </PatronBasicDataGetResult>""";
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
