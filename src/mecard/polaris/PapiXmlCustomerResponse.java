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

import api.CustomerMessage;
import java.util.HashMap;
import mecard.util.DateComparer;

/**
 * The PAPI Customer message is the interface between the PAPI XML customer
 * data returned from PAPI which MeCard uses to check the validity of a customer.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class PapiXmlCustomerResponse 
    extends PapiXmlResponse
    implements CustomerMessage
{
    //<PatronBasicDataGetResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
    //  <PAPIErrorCode>0</PAPIErrorCode>
    //  <ErrorMessage>
    //  </ErrorMessage>
    //  <PatronBasicData>
    //    <PatronID>2022</PatronID>
    //    <Barcode>21221012345678</Barcode>
    //    <NameFirst>Billy</NameFirst>
    //    <NameLast>Balzac</NameLast>
    //    <NameMiddle i:nil="true" />
    //    <PhoneNumber>555-1212</PhoneNumber>
    //    <EmailAddress>dude@hotmail.com</EmailAddress>
    //    <ItemsOutCount>0</ItemsOutCount>
    //    <ItemsOverdueCount>0</ItemsOverdueCount>
    //    <ItemsOutLostCount>0</ItemsOutLostCount>
    //    <HoldRequestsTotalCount>0</HoldRequestsTotalCount>
    //    <HoldRequestsCurrentCount>0</HoldRequestsCurrentCount>
    //    <HoldRequestsShippedCount>0</HoldRequestsShippedCount>
    //    <HoldRequestsHeldCount>0</HoldRequestsHeldCount>
    //    <HoldRequestsUnclaimedCount>0</HoldRequestsUnclaimedCount>
    //    <ChargeBalance>0.0000</ChargeBalance>
    //    <CreditBalance>0.0000</CreditBalance>
    //    <DepositBalance>0.0000</DepositBalance>
    //    <NameTitle i:nil="true" />
    //    <NameSuffix i:nil="true" />
    //    <PhoneNumber2 i:nil="true" />
    //    <PhoneNumber3 i:nil="true" />
    //    <Phone1CarrierID>0</Phone1CarrierID>
    //    <Phone2CarrierID>0</Phone2CarrierID>
    //    <Phone3CarrierID>0</Phone3CarrierID>
    //    <CellPhone i:nil="true" />
    //    <CellPhoneCarrierID>0</CellPhoneCarrierID>
    //    <AltEmailAddress i:nil="true" />
    //    <BirthDate i:nil="true" />
    //    <RegistrationDate>2022-07-05T00:00:00</RegistrationDate>
    //    <LastActivityDate>2022-07-05T17:01:14.217</LastActivityDate>
    //    <AddrCheckDate>2027-07-05T00:00:00</AddrCheckDate>
    //    <MessageNewCount>0</MessageNewCount>
    //    <MessageReadCount>0</MessageReadCount>
    //    <PatronOrgID>3</PatronOrgID>
    //    <PatronCodeID>1</PatronCodeID>
    //    <DeliveryOptionID>1</DeliveryOptionID>
    //    <ExcludeFromAlmostOverdueAutoRenew>false</ExcludeFromAlmostOverdueAutoRenew>
    //    <ExcludeFromPatronRecExpiration>false</ExcludeFromPatronRecExpiration>
    //    <ExcludeFromInactivePatron>false</ExcludeFromInactivePatron>
    //    <EReceiptOptionID>0</EReceiptOptionID>
    //    <TxtPhoneNumber>0</TxtPhoneNumber>
    //    <EmailFormatID>2</EmailFormatID>
    //    <LegalNameFirst>Johnathan</LegalNameFirst>
    //    <LegalNameLast>Smith</LegalNameLast>
    //    <LegalNameMiddle>Edward</LegalNameMiddle>
    //    <UseLegalNameOnNotices>true</UseLegalNameOnNotices>
    //    <LegalFullName>Smith, Johnathan Edward</LegalFullName>
    //    <PatronAddresses>
    //      <PatronAddress>
    //        <AddressID>339329</AddressID>
    //        <FreeTextLabel>Home</FreeTextLabel>
    //        <StreetOne>11811 74 Ave.</StreetOne>
    //        <StreetTwo i:nil="true" />
    //        <StreetThree i:nil="true" />
    //        <City>Edmonton</City>
    //        <State>AB</State>
    //        <County>
    //        </County>
    //        <PostalCode>90210</PostalCode>
    //        <ZipPlusFour i:nil="true" />
    //        <Country>USA</Country>
    //        <CountryID>1</CountryID>
    //        <AddressTypeID>2</AddressTypeID>
    //      </PatronAddress>
    //    </PatronAddresses>
    //    <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>
    //    <RequestPickupBranchID>0</RequestPickupBranchID>
    //    <User1 i:nil="true" />
    //    <User2 i:nil="true" />
    //    <User3 i:nil="true" />
    //    <User4 i:nil="true" />
    //    <User5 i:nil="true" />
    //    <PatronNotes i:nil="true" />
    //    <PatronSystemBlocks />
    //  </PatronBasicData>
    //</PatronBasicDataGetResult>
    protected HashMap<String, String> fields;
    /**
     * The string in question is the XML message from the ILS's web service.
     * @param xml XML(?) input message.
     * @param debug 
     */
    public PapiXmlCustomerResponse(String xml, boolean debug)
    {
        super(xml);
        this.fields = new HashMap<>();
        if (this.failedResponse) return;
        PapiElementOrder[] elementNames = PapiElementOrder.values();
        for (PapiElementOrder elementName: elementNames)
        {
            String name = elementName.toString();
            String value;
            try
            {
                value = this.root.getElementsByTagName(name).item(0).getTextContent();
            }
            catch (NullPointerException e)
            {
                continue;
            }
            if (debug)
            {
                System.out.print("elementName::" + name + " ");
                System.out.println("value::" + value + "");
            }
            this.fields.put(elementName.toString(), value);
        }
    }
    
    

    @Override
    public String getCustomerProfile()
    {
        // There is no profile to test so send back an empty string. When compared
        // with the deny list of profiles it will pass.
        return this.fields.getOrDefault(PapiElementOrder.PATRON_NOTES.toString(), "");
    }

    @Override
    public String getField(String fieldName)
    {
        return this.fields.getOrDefault(fieldName, "");
    }

    @Override
    public boolean isEmpty(String fieldName)
    {
        return this.fields.getOrDefault(fieldName, "").isEmpty();
    }

    @Override
    public String getStanding()
    {
        return "";
    }

    @Override
    public boolean cardReportedLost()
    {
        // There may be a need to check notes on the customer's account for
        // this information.
        String lostCardSentinal = "Patron reported lost";
        String patronNotes = this.fields.getOrDefault(PapiElementOrder.PATRON_NOTES.toString(), "");
        return patronNotes.contains(lostCardSentinal);
    }

    @Override
    public boolean isInGoodStanding()
    {
        // There may be a need to check notes on the customer's account for
        // this information, but for now just return the fact that the message 
        // didn't include an error message.
        return this.succeeded();
    }

    @Override
    public String getDateField(String fieldName)
    {
        return DateComparer.getANSIDate(this.fields.getOrDefault(fieldName, ""));
    }
    
    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder();
        for (String s : this.fields.keySet())
        {
            out.append("'").append(s).append("'->'").append(this.fields.get(s)).append("'\n");
        }
        return out.toString();
    }
}
