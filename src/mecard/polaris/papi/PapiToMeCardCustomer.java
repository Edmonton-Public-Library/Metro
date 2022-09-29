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
 */
package mecard.polaris.papi;

import java.util.List;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.util.DateComparer;
import mecard.customer.NativeFormatToMeCardCustomer;

/**
 * Formats the {@link mecard.customer.Customer} into the Polaris web service (PAPI)
 * consumable form which, as of version 7.0 (and perhaps earlier) is XML only.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class PapiToMeCardCustomer implements NativeFormatToMeCardCustomer
{
    // The following is the response from a Patron Basic Data Get request.
    // https://dewey.polarislibrary.com/PAPIService/REST/public/v1/1033/100/1/patron/21221012345678/basicdata?addresses=true
    //  <PatronBasicDataGetResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
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

    @Override
    public Customer getCustomer(List<String> list)
    {
        StringBuilder sb = new StringBuilder();
        for (String s: list)
        {
            sb.append(s);
        }
        return this.getCustomer(sb.toString());
    }

    @Override
    public Customer getCustomer(String xmlCustomerData)
    {
        Customer customer = new Customer();
        // Parse the XML
        // https://dewey.polarislibrary.com/PAPIService/REST/public/v1/1033/100/1/patron/21221012345678/basicdata?addresses=true
        //  <PatronBasicDataGetResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
        //  <PAPIErrorCode>0</PAPIErrorCode>
        //  <ErrorMessage>
        //  </ErrorMessage>
        //  <PatronBasicData>
        //    <Barcode>21221012345678</Barcode>
        //    <NameFirst>Billy</NameFirst>
        //    <NameLast>Balzac</NameLast>
        //    <PhoneNumber>555-1212</PhoneNumber>
        //    <EmailAddress>dude@hotmail.com</EmailAddress>
        //    <BirthDate i:nil="true" />
        //    <PatronAddresses>
        //      <PatronAddress>
        //        <StreetOne>11811 74 Ave.</StreetOne>
        //        <StreetTwo i:nil="true" />     // optional include
        //        <StreetThree i:nil="true" />   // optional include
        //        <City>Edmonton</City>
        //        <State>AB</State>
        //        <PostalCode>90210</PostalCode>
        //      </PatronAddress>
        //    </PatronAddresses>
        //    <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>
        //    <PatronNotes i:nil="true" />
        //  </PatronBasicData>
        //</PatronBasicDataGetResult>
        PapiXmlCustomerResponse customerData = new PapiXmlCustomerResponse(xmlCustomerData, false);
        customer.set(CustomerFieldTypes.ID, customerData.getField(PapiElementOrder.BARCODE.toString()));
        customer.set(CustomerFieldTypes.FIRSTNAME, customerData.getField(PapiElementOrder.NAME_FIRST.toString()));
        customer.set(CustomerFieldTypes.LASTNAME, customerData.getField(PapiElementOrder.NAME_LAST.toString()));
        customer.set(CustomerFieldTypes.EMAIL, customerData.getField(PapiElementOrder.EMAIL_ADDRESS.toString()));
        customer.set(CustomerFieldTypes.STREET, customerData.getField(PapiElementOrder.STREET_ONE.toString()));
        customer.set(CustomerFieldTypes.POSTALCODE, customerData.getField(PapiElementOrder.POSTAL_CODE.toString()));
        customer.set(CustomerFieldTypes.PROVINCE, customerData.getField(PapiElementOrder.STATE.toString()));
        customer.set(CustomerFieldTypes.CITY, customerData.getField(PapiElementOrder.CITY.toString()));
        // Note that PHONE_NUMBER is ONLY returned in Patron Basic Data Get requests
        // Create and Update refer to it as PhoneVoice1,2,3. 
        String phone = customerData.getField(PapiElementOrder.PHONE_NUMBER.toString());
        if (phone.isEmpty())
        {
            phone = customerData.getField(PapiElementOrder.PHONE_VOICE_1.toString());
        }
        customer.set(CustomerFieldTypes.PHONE, phone);
        String gender = customerData.getField(PapiElementOrder.GENDER.toString());
        if (! gender.isEmpty())
        {
            if (gender.contains("F"))
            {
                gender = "F";
            }
            else if (gender.contains("M"))
            {
                gender = "M";
            }
            else
            {
                gender = "N";
            }
        }
        customer.set(CustomerFieldTypes.SEX, gender);
        String birthDate = customerData.getField(PapiElementOrder.BIRTHDATE.toString());
        if (! birthDate.isEmpty())
        {
            birthDate = DateComparer.getANSIDate(birthDate);
        }
        customer.set(CustomerFieldTypes.DOB, birthDate);
        String expiry = customerData.getField(PapiElementOrder.EXPIRATION_DATE.toString());
        if (! expiry.isEmpty())
        {
            expiry = DateComparer.getANSIDate(expiry);
        }
        customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, expiry);
        customer.set(CustomerFieldTypes.PREFEREDNAME, 
                customerData.getField(PapiElementOrder.USE_LEGAL_NAME_ON_NOTICES.toString()));
        return customer;
    }

}
