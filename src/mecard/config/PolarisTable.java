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
package mecard.config;

/**
 * This static class is a container for all the table column names, standardized
 * in enum containers. Tables are:
 * <ul>
 * <li>Patrons</li>
 * <li>Addresses</li>
 * <li>PatronAddresses</li>
 * <li>PatronRegistration</li>
 * <li>PostalCodes</li>
 * </ul>
 * @author anisbet
 */
public class PolarisTable
{
    public final static String PATRONS = "Patrons";
    public final static String ADDRESSES = "Addresses";
    public final static String PATRON_ADDRESSES = "PatronAddresses";
    public final static String PATRON_REGISTRATION = "PatronRegistration";
    public final static String POSTAL_CODES = "PostalCodes";
    /**
     * All the columns of the Polaris.Patrons table.
     */
    public enum Patrons
    {
        PATRON_ID("PatronID"), // 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        PATRON_CODE_ID("PatronCodeID"), //, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 2, NO, 
        ORGANIZATION_ID("OrganizationID"), //, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 3, NO, 
        CREATOR_ID("CreatorID"), //, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 4, NO, 
        MODIFIER_ID("ModifierID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 5, YES, 
        BARCODE("Barcode"), //, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 6, YES, 
        SYSTEM_BLOCKS("SystemBlocks"), //, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 7, NO, 
        YTD_CIRC_COUNT("YTDCircCount"), //, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 8, NO, 
        LIFE_TIME_CIRC_COUNT("LifetimeCircCount"), //, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 9, NO, 
        LAST_ACTIVITY_DATE("LastActivityDate"), //, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 10, YES, 
        CLAIM_COUNT("ClaimCount"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 11, YES, 
        LOST_ITEM_COUNT("LostItemCount"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 12, YES, 
        CHARGES_AMOUNT("ChargesAmount"), //, 3, money, 19, 21, 4, 10, 0, null, null, 3, null, null, 13, NO, 
        CREDITS_AMOUNT("CreditsAmount"), // 3, money, 19, 21, 4, 10, 0, null, null, 3, null, null, 14, NO,
        /** New to Polaris v.5.2:3 ***/
        RECORD_STATUS_ID("RecordStatusID"), //, 4, int, 10, 4, 0, 10, 0, null, ((1)), 4, null, null, 15, NO, 
        RECORD_STATUS_DATE("RecordStatusDate"), // 11, datetime, 23, 16, 3, null, 0, null, (getdate()), 9, 3, null, 16, NO, 
        YTD_YOU_SAVED_AMOUNT("YTDYouSavedAmount"), // 3, money, 19, 21, 4, 10, 0, null, ((0)), 3, null, null, 17, NO, 
        LIFE_TIME_YOU_SAVED_AMOUNT("LifetimeYouSavedAmount"); // 3, money, 19, 21, 4, 10, 0, null, ((0)), 3, null, null, 18, NO, 
        
        private final String type;

        private Patrons(String s)
        {
            this.type = s;
        }

        @Override
        public String toString()
        {
            return this.type;
        }
    }
    
    /** 
     * All the column names from with the PatronRegistration table.
     */
    public enum PatronRegistration
    {
        PATRON_ID("PatronID"), // 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        LANGUAGE_ID("LanguageID"), //, 5, smallint, 5, 2, 0, 10, 1, null, null, 5, null, null, 2, YES, 
        NAME_FIRST("NameFirst"), //, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 3, YES, 
        NAME_LAST("NameLast"), //, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 4, YES, 
        NAME_MIDDLE("NameMiddle"), //, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 5, YES, 
        NAME_TITLE("NameTitle"), //, 12, varchar, 8, 8, null, null, 1, null, null, 12, null, 8, 6, YES, 
        NAME_SUFFIX("NameSuffix"), //, 12, varchar, 4, 4, null, null, 1, null, null, 12, null, 4, 7, YES, 
        PHONE_VOICE_1("PhoneVoice1"), //, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 8, YES, 
        PHONE_VOICE_2("PhoneVoice2"), //, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 9, YES, 
        PHONE_VOICE_3("PhoneVoice3"), //, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 10, YES, 
        EMAIL_ADDRESS("EmailAddress"), //, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 11, YES, 
        /*
        DEPRECATED: This field is no longer part of the schema. See PasswordHash and ObfuscatedPassword below for more 
        information as of Polaris v.5.3:3
        DEPRECATED but still referenced to get the customers pin from the PolarisSQLFormattedCustomer object.
        */
//        PASSWORD("Password"), // DEPRECATED:, 12, varchar, 16, 16, null, null, 1, null, null, 12, null, 16, 12, YES, 
        ENTRY_DATE("EntryDate"), //, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 13, YES, 
        EXPIRATION_DATE("ExpirationDate"), //, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 14, YES, 
        ADDR_CHECK_DATE("AddrCheckDate"), //, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 15, YES, 
        UPDATE_DATE("UpdateDate"), //, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 16, YES, 
        USER_1("User1"), //, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 17, YES, 
        USER_2("User2"), //, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 18, YES, 
        USER_3("User3"), //, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 19, YES, 
        USER_4("User4"), //, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 20, YES, 
        USER_5("User5"), //, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 21, YES, 
        // This column name is changing to 'GenderID' as of Polaris 6.3. The values come from a new table called 'Genders'.
        // The table has 3 entries currently: 1=N/A, 2='Female', 3='Male'. 2020-01-20.
        GENDER("GenderID"), //, 1, char, 1, 1, null, null, 1, null, null, 1, null, 1, 22, YES, 
        BIRTH_DATE("Birthdate"), //, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 23, YES, 
        REGISTRATION_DATE("RegistrationDate"), //, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 24, YES, 
        FORMER_ID("FormerID"), //, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 25, YES, 
        READING_LIST("ReadingList"), //, -6, tinyint, 3, 1, 0, 10, 0, null, null, -6, null, null, 26, NO, 
        PHONE_FAX("PhoneFAX"), //, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 27, YES, 
        DELIVERY_OPTION_ID("DeliveryOptionID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 28, YES, 
        STATISTICAL_CLASS_ID("StatisticalClassID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 29, YES, 
        COLLECTION_EXEMPT("CollectionExempt"), //, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 30, NO, 
        ALT_EMAIL_ADDRESS("AltEmailAddress"), //, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 31, YES, 
        EXCLUDE_FROM_OVERDUES("ExcludeFromOverdues"), //, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 32, NO, 
        SDI_EMAIL_ADDRESS("SDIEmailAddress"), //, 12, varchar, 150, 150, null, null, 1, null, null, 12, null, 150, 33, YES, 
        SDI_EMAIL_FORMAT_ID("SDIEmailFormatID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 34, YES, 
        SDI_POSITIVE_ASSENT("SDIPositiveAssent"), //, -7, bit, 1, 1, null, null, 1, null, null, -7, null, null, 35, YES, 
        SDI_POSITIVE_ASSENT_DATE("SDIPositiveAssentDate"), //, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 36, YES, 
        DELETION_EXEMPT("DeletionExempt"), //, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 37, NO, 
        PATRON_FULL_NAME("PatronFullName"), //, 12, varchar, 100, 100, null, null, 1, null, null, 12, null, 100, 38, YES, 
        EXCLUDE_FROM_HOLDS("ExcludeFromHolds"), //, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 39, NO, 
        EXCLUDE_FROM_BILLS("ExcludeFromBills"), //, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 40, NO, 
        EMAIL_FORMAT_ID("EmailFormatID"), //, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 41, NO, 
        PATRON_FIRST_LAST_NAME("PatronFirstLastName"), //, 12, varchar, 100, 100, null, null, 1, null, null, 12, null, 100, 42, YES, 
        USERNAME("Username"), //, 12, varchar, 50, 50, null, null, 1, null, null, 12, null, 50, 43, YES, 
        MERGE_DATE("MergeDate"), //, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 44, YES, 
        MERGE_USER_ID("MergeUserID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 45, YES, 
        MERGE_BARCODE("MergeBarcode"), //, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 46, YES,
        // These two values seem to have dissappeared as valid column names, Feb. 4, 2015.
        // as of Polaris 4.1R2 (Build 4.1.1439.2)
//        DEPRECATED: CELL_PHONE("CellPhone"), //, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 47, YES, 
//        DEPRECATED: CELL_PHONE_CARRIER_ID("CellPhoneCarrierID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 48, YES, 
        ENABLE_SMS("EnableSMS"), //, -7, bit, 1, 1, null, null, 1, null, null, -7, null, null, 49, YES, 
        REQUEST_PICKUP_BRANCH_ID("RequestPickupBranchID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 50, YES, 
        PHONE1_CARRIER_ID("Phone1CarrierID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 51, YES, 
        PHONE2_CARRIER_ID("Phone2CarrierID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 52, YES, 
        PHONE3_CARRIER_ID("Phone3CarrierID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 53, YES, 
        ERECEIPT_OPTION_ID("eReceiptOptionID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 54, YES, 
        TXT_PHONE_NUMBER("TxtPhoneNumber"), //, -6, tinyint, 3, 1, 0, 10, 1, null, null, -6, null, null, 55, YES,
        EXCLUDE_FROM_ALMOST_OVERDUE_AUTO_RENEW("ExcludeFromAlmostOverdueAutoRenew"), // bit, default = 1
        EXCLUDE_FROM_PATRON_REC_EXPIRATION("ExcludeFromPatronRecExpiration"), // bit, default = 1
        EXCLUDE_FROM_INACTIVE_PATRON("ExcludeFromInactivePatron"), // bit, default = 1
        // New to Polaris schema 5.2.3
        // Polaris, PatronRegistration, DoNotShowEReceiptPrompt, -7, bit, 1, 1, null, null, 0, null, ((0)), -7, null, null, 56, NO,
        DO_NOT_SHOW_E_RECEIPT_PROMPT("DoNotShowEReceiptPrompt"), // bit, default = 1
        /* July 13, 2017, Updated Sept. 1, 2017.
        Taking a look at the stored procedures surrounding the Patron 
        Registration Create and Update processes, it looks as though the 
        ObfuscatedPassword and PasswordHash columns are generated using functions. 
        The PasswordHash is generated using the dbo.ILS_HashPassword function 
        and the ObfuscatedPassword is calculated using the dbo.ILS_ObfuscateText 
        function. Here is an example of how they are used in the 
        Circ_UpdatePatron stored procedure:
        [PasswordHash] = dbo.ILS_HashPassword(@strPassword),
        [ObfuscatedPassword] = dbo.ILS_ObfuscateText(@strPassword
        *** from the describe command.
        Polaris, Polaris, PatronRegistration, PasswordHash,       12, varchar, 256, 256, null, null, 1, null, null, 12, null, 256, 57, YES, 
        Polaris, Polaris, PatronRegistration, ObfuscatedPassword, 12, varchar, 256, 256, null, null, 1, null, null, 12, null, 256, 58, YES,
        */
        PASSWORD_HASH("PasswordHash"),               // These are not actually used directly. They get populated by a procedure call on Polaris.
        OBFUSCATED_PASSWORD("ObfuscatedPassword"),   // Example: callableStatement = con.prepareCall("{call Polaris.Circ_SetPatronPassword(?)}"); 
        NAME_TITLE_ID("NameTitleID"),                // [int] NULL, this is new to Polaris 6.2
	RBDIGITAL_PATRON_ID("RBdigitalPatronID");    // [int] NULL, this is new to Polaris 6.2
        
        private final String type;

        private PatronRegistration(String s)
        {
            this.type = s;
        }

        @Override
        public String toString()
        {
            return this.type;
        }
    }
    
    public enum Addresses
    {
        ADDRESS_ID("AddressID"), //, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        POSTAL_CODE_ID("PostalCodeID"), //, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 2, NO, 
        STREET_ONE("StreetOne"), //, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 3, YES, 
        STREET_TWO("StreetTwo"), //, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 4, YES, 
        ZIP_PLUS_FOUR("ZipPlusFour"), //, 12, varchar, 4, 4, null, null, 1, null, null, 12, null, 4, 5, YES, 
        MUNICIPALITY_NAME("MunicipalityName"); //, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 6, YES,
        
        private String type;

        private Addresses(String s)
        {
            this.type = s;
        }

        @Override
        public String toString()
        {
            return this.type;
        }
    }
    
    public enum PostalCodes
    {
        POSTAL_CODE_ID("PostalCodeID"), //, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        POSTAL_CODE("PostalCode"), //, 12, varchar, 10, 10, null, null, 1, null, null, 12, null, 10, 2, YES, 
        CITY("City"), //, 12, varchar, 32, 32, null, null, 0, null, null, 12, null, 32, 3, NO, 
        STATE("State"), //, 12, varchar, 32, 32, null, null, 0, null, null, 12, null, 32, 4, NO, 
        COUNTRY_ID("CountryID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 5, YES, 
        COUNTY("County"); //, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 6, YES,
        
        private String type;

        private PostalCodes(String s)
        {
            this.type = s;
        }

        @Override
        public String toString()
        {
            return this.type;
        }
    }
    
    public enum PatronAddresses
    {
        PATRON_ID("PatronID"), //, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        ADDRESS_ID("AddressID"), //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 2, YES, 
        ADDRESS_TYPE_ID("AddressTypeID"), //, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 3, NO, 
        // The FreeTextLabel field has been replaced with AddressLabelID in Polaris 6.2.
        FREE_TEXT_LABEL("FreeTextLabel"), //, 12, varchar, 30, 30, null, null, 1, null, null, 12, null, 30, 4, YES, 
        // Polaris 6.2
        ADDRESS_LABEL_ID("AddressLabelID"), //, fk, int, NOT NULL -- default to the integer '1' which is 'Home' in previous versions of Polaris.
        VERIFIED("Verified"), //, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 5, NO, 
        VERIFICATION_DATE("VerificationDate"), //, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 6, YES, 
        POLARIS_USER_ID("PolarisUserID"); //, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 7, YES,
        
        private String type;

        private PatronAddresses(String s)
        {
            this.type = s;
        }

        @Override
        public String toString()
        {
            return this.type;
        }
    }
    
    private PolarisTable(){} // not meant to be instantiated.
}
