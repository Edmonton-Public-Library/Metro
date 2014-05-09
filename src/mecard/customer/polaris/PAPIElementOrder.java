/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
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
package mecard.customer.polaris;

/**
 * This is the entire set of create fields accepted by Polaris in WS 4.1.
 * The order must not change. The values preceded by 'C_' are for creating 
 * customer records. The values preceded by 'U_' are update-able fields on
 * a customer's account.
 * @author anisbet
 */
public enum PAPIElementOrder
{
    C_PATRON_TAG("PatronRegistrationCreateData"),  // Tag name and place holder. All other entries are ordered starting at 1.
    C_LOGON_BRANCH_ID("LogonBranchID"),
    C_LOGON_USER_ID("LogonUserID"),
    C_LOGON_WORKSTATION_ID("LogonWorkstationID"),
    C_PATRON_BRANCH_ID("PatronBranchID"),
    C_POSTAL_CODE("PostalCode"),
    C_ZIP_PLUS_FOUR("ZipPlusFour"),
    C_CITY("City"),
    C_STATE("State"),
    C_COUNTY("County"),
    C_COUNTRY_ID("CountryID"),
    C_STREET_ONE("StreetOne"),
    C_STREET_TWO("StreetTwo"),
    C_NAME_FIRST("NameFirst"),
    C_NAME_LAST("NameLast"),
    C_NAME_MIDDLE("NameMiddle"),
    C_USER_1("User1"),
    C_USER_2("User2"),
    C_USER_3("User3"),
    C_USER_4("User4"),
    C_USER_5("User5"),
    C_GENDER("Gender"),
    C_BIRTHDATE("Birthdate"),
    C_PHONE_VOICE_1("PhoneVoice1"),
    C_PHONE_VOICE_2("PhoneVoice2"),
    C_EMAIL_ADDRESS("EmailAddress"),
    C_LANGUAGE_ID("LanguageID"),
    C_DELIVERY_OPTION_ID("DeliveryOptionID"),
    C_USER_NAME("UserName"),
    C_PASSWORD("Password"),
    C_PASSWORD_2("Password2"),
    C_ALT_EMAIL_ADDRESS("AltEmailAddress"),
    C_PHONE_VOICE_3("PhoneVoice3"),
    C_PHONE_1_CARRIER_ID("Phone1CarrierID"),
    C_PHONE_2_CARRIER_ID("Phone2CarrierID"),
    C_PHONE_3_CARRIER_ID("Phone3CarrierID"),
    C_ENABLE_SMS("Enable SMS"),
    C_TXT_PHONE_NUMBER("TxtPhoneNumber"),
    C_BARCODE("Barcode"),
    C_ERECEIPT_OPTION_ID("EReceiptOPtionID"), // Ereceipt option ID [4.1 only] 2 - Email Address 8 - TXT Messaging 100 - All
    // Supported Update features.
    U_PATRON_UPDATE_DATA("PatronUpdateData"), // TAG NAME
    U_LOGON_BRANCH_ID("LogonBranchID"),
    U_LOGON_USER_ID("LogonUserID"),
    U_LOGON_WORKSTATION_ID("LogonWorkstationID"),
    U_READING_LIST_FLAG("ReadingListFlag"),
    U_EMAIL_FORMAT("EmailFormat"),
    U_DELIVERY_OPTION("DeliveryOption"),
    U_EMAIL_ADDRESS("EmailAddress"),
    U_PHONE_VOICE_1("PhoneVoice1"),
    U_PASSWORD("Password");

    private String type;

    private PAPIElementOrder(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
