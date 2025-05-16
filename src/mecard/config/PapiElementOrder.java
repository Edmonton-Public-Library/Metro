/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022 - 2025  Edmonton Public Library
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
package mecard.config;

/**
 * This is the entire set of create fields accepted by Polaris in WS 4.1.
 * The order must not change. The values preceded by 'C_' are for creating 
 * customer records. The values preceded by 'U_' are update-able fields on
 * a customer's account.
 * 
 * Refactored for WS version 7.X.
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public enum PapiElementOrder
{
    // These next three tags are just for api-verion V2.
    TAG_PATRON_REGISTRATION_DATA_V2("PatronRegistrationDataV2"),
    TAG_ADDRESSES_V2("Addresses"),
    TAG_PATRON_REGISTRATION_ADDRESS_DATA_V2("PatronRegistrationAddressDataV2"),
    // These are V1
    TAG_PATRON_REGISTRATION_CREATE("PatronRegistrationCreateData"),  // Tag name and place holder. All other entries are ordered starting at 1.
    TAG_PATRON_UPDATE_DATA("PatronUpdateData"),
    TAG_PATRON_ADDRESSES("PatronAddresses"),
    TAG_ADDRESS("Address"),
    LOGON_BRANCH_ID("LogonBranchID"),
    LOGON_USER_ID("LogonUserID"),
    LOGON_WORKSTATION_ID("LogonWorkstationID"),
    UPDATE_READING_LIST_FLAG("ReadingListFlag"),  // Update Only.
    UPDATE_EMAIL_FORMAT("EmailFormat"),           // Update Only.
    UPDATE_DELIVERY_OPTION_ID("DeliveryOptionID"),// Update only.
    UPDATE_DELIVERY_OPTION("DeliveryOption"),     // Update only.
    PATRON_BRANCH_ID("PatronBranchID"),
    COUNTY("County"),                             // Order for UPDATE, CREATE places it just before COUNTRY.
    POSTAL_CODE("PostalCode"),
    ZIP_PLUS_FOUR("ZipPlusFour"),
    CITY("City"),
    STATE("State"),
    COUNTRY("Country"),
    COUNTRY_ID("CountryID"),
    ADDRESS_TYPE_ID("AddressTypeID"),
    STREET_ONE("StreetOne"),
    STREET_TWO("StreetTwo"),
    STREET_THREE("StreetThree"),
    NAME_FIRST("NameFirst"),
    NAME_LAST("NameLast"),
    NAME_MIDDLE("NameMiddle"),
    PHONE_NUMBER("PhoneNumber"),    // Patron Basic Data Get only!
    USER1("User1"),
    USER2("User2"),
    USER3("User3"),
    USER4("User4"),
    USER5("User5"),
    GENDER("Gender"),
    BIRTHDATE("BirthDate"),
    PHONE_VOICE_1("PhoneVoice1"),
    PHONE_VOICE_2("PhoneVoice2"),
    PHONE_VOICE_3("PhoneVoice3"),
    PHONE_1_CARRIER_ID("Phone1CarrierID"),
    PHONE_2_CARRIER_ID("Phone2CarrierID"),
    PHONE_3_CARRIER_ID("Phone3CarrierID"),
    EMAIL_ADDRESS("EmailAddress"),
    ALT_EMAIL_ADDRESS("AltEmailAddress"),
    LANGUAGE_ID("LanguageID"),
    USER_NAME("UserName"),
    PASSWORD("Password"),
    PASSWORD_2("Password2"),
    DELIVERY_OPTION_ID("DeliveryOptionID"),
    ENABLE_SMS("Enable SMS"),
    TXT_PHONE_NUMBER("TxtPhoneNumber"),
    BARCODE("Barcode"),
    ERECEIPT_OPTION_ID("EReceiptOPtionID"), // Ereceipt option ID [4.1 only] 2 - Email Address 8 - TXT Messaging 100 - All
    PATRON_CODE("PatronCode"),
    //	<ExpirationDate>2022-07-05T18:45:23.162Z</ExpirationDate>
    EXPIRATION_DATE("ExpirationDate"),
    //	<AddrCheckDate>2022-07-05T18:45:23.162Z</AddrCheckDate>
    ADDRESS_CHECK_DATE("AddrCheckDate"),
    //	<GenderID>0</GenderID>
    GENDER_ID("GenderID"),
    //	<LegalNameFirst>string</LegalNameFirst>
    LEGAL_NAME_FIRST("LegalNameFirst"),
    //	<LegalNameLast>string</LegalNameLast>
    LEGAL_NAME_LAST("LegalNameLast"),
    //	<LegalNameMiddle>string</LegalNameMiddle>
    LEGAL_NAME_MIDDLE("LegalNameMiddle"),
    //	<UseLegalNameOnNotices>true</UseLegalNameOnNotices>
    USE_LEGAL_NAME_ON_NOTICES("UseLegalNameOnNotices"),
    //	<RequestPickupBranchID>0</RequestPickupBranchID>
    REQUEST_PICKUP_BRANCH_ID("RequestPickupBranchID"),
    
    PATRON_NOTES("PatronNotes"),   // Patron Basic Data Get
//	<ExcludeFromAlmostOverdueAutoRenew>true</ExcludeFromAlmostOverdueAutoRenew>
    EXCLUDE_FROM_ALMOST_OVERDUE_AUTO_RENEW("ExcludeFromAlmostOverdueAutoRenew"),  // Update Only.
//	<ExcludeFromPatronRecExpiration>true</ExcludeFromPatronRecExpiration>
    EXCLUDE_FROM_PATRON_REC_EXPIRATION("ExcludeFromPatronRecExpiration"),  // Update Only.
//	<ExcludeFromInactivePatron>true</ExcludeFromInactivePatron>
    EXCLUDE_FROM_INACTIVE_PATRON("ExcludeFromInactivePatron"),  // Update Only.
//			<AddressID>0</AddressID>
    ADDRESS_ID("AddressID"),  // Update Only.
//			<FreeTextLabel>string</FreeTextLabel>
    FREE_TEXT_LABEL("FreeTextLabel");  // Update Only.
    
//    <PatronUpdateData>
//	<LogonBranchID>0</LogonBranchID>
//	<LogonUserID>0</LogonUserID>
//	<LogonWorkstationID>0</LogonWorkstationID>
//	<ReadingListFlag>0</ReadingListFlag>
//	<EmailFormat>0</EmailFormat>
//	<DeliveryOptionID>0</DeliveryOptionID>
//	<DeliveryOption>0</DeliveryOption>
//	<EmailAddress>string</EmailAddress>
//	<AltEmailAddress>string</AltEmailAddress>
//	<EnableSMS>true</EnableSMS>
//	<PhoneVoice1>string</PhoneVoice1>
//	<PhoneVoice2>string</PhoneVoice2>
//	<PhoneVoice3>string</PhoneVoice3>
//	<Phone1CarrierID>0</Phone1CarrierID>
//	<Phone2CarrierID>0</Phone2CarrierID>
//	<Phone3CarrierID>0</Phone3CarrierID>
//	<TxtPhoneNumber>0</TxtPhoneNumber>
//	<EReceiptOptionID>0</EReceiptOptionID>
//	<Password>string</Password>
//	<PatronCode>0</PatronCode>
//	<ExpirationDate>2022-07-05T19:38:30.611Z</ExpirationDate>
//	<AddrCheckDate>2022-07-05T19:38:30.611Z</AddrCheckDate>
//	<ExcludeFromAlmostOverdueAutoRenew>true</ExcludeFromAlmostOverdueAutoRenew>
//	<ExcludeFromPatronRecExpiration>true</ExcludeFromPatronRecExpiration>
//	<ExcludeFromInactivePatron>true</ExcludeFromInactivePatron>
//	<PatronAddresses>
//		<Address>
//			<AddressID>0</AddressID>
//			<FreeTextLabel>string</FreeTextLabel>
//			<StreetOne>string</StreetOne>
//			<StreetTwo>string</StreetTwo>
//			<StreetThree>string</StreetThree>
//			<City>string</City>
//			<State>string</State>
//			<County>string</County>
//			<PostalCode>string</PostalCode>
//			<ZipPlusFour>string</ZipPlusFour>
//			<Country>string</Country>
//			<CountryID>0</CountryID>
//			<AddressTypeID>0</AddressTypeID>
//		</Address>
//	</PatronAddresses>
//	<RequestPickupBranchID>0</RequestPickupBranchID>
//	<User1>string</User1>
//	<User2>string</User2>
//	<User3>string</User3>
//	<User4>string</User4>
//	<User5>string</User5>
//</PatronUpdateData>


    private String type;

    private PapiElementOrder(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
