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

import java.util.EnumMap;
import mecard.customer.FormattedTable;

/**
 * This class represents a table of customer data for loading.
 * For PAPI XML_CUSTOMER_CREATE, this table represents a complete table of customer data, that 
 can be added to a REST POST request to create customer.
 
 This class uses a factory method because there are 4 variations of tables.
 There are create tables which contain a complete set of data about the customer,
 and which come in 1 of 2 flavors: XML_CUSTOMER_CREATE and JSON. This class must be able to 
 make customer update tables which are (much) more restricted in information,
 and also come in two flavors: XML_CUSTOMER_CREATE and JSON.
 * @author anisbet
 */
public class PAPIFormattedTable implements FormattedTable
{

    public enum DataType
    {
        XML_CREATE,
        XML_UPDATE,
        JSON_CREATE,
        JSON_UPDATE;
    }
    
    /** This is the entire set of create fields accepted by Polaris in WS 4.1.
     * The order must not change.
     */
    public enum CreateOrder
    {
        PATRON_REGISTRATION_CREATE_DATA("PatronRegistrationCreateData"),  // Tag name and place holder. All other entries are ordered starting at 1.
        LOGON_BRANCH_ID("LogonBranchID"),
        LOGON_USER_ID("LogonUserID"),
        LOGON_WORKSTATION_ID("LogonWorkstationID"),
        PATRON_BRANCH_ID("PatronBranchID"),
        POSTAL_CODE("PostalCode"),
        ZIP_PLUS_FOUR("ZipPlusFour"),
        CITY("City"),
        STATE("State"),
        COUNTY("County"),
        COUNTRY_ID("CountryID"),
        STREET_ONE("StreetOne"),
        STREET_TWO("StreetTwo"),
        NAME_FIRST("NameFirst"),
        NAME_LAST("NameLast"),
        NAME_MIDDLE("NameMiddle"),
        USER_1("User1"),
        USER_2("User2"),
        USER_3("User3"),
        USER_4("User4"),
        USER_5("User5"),
        GENDER("Gender"),
        BIRTHDATE("Birthdate"),
        PHONE_VOICE_1("PhoneVoice1"),
        PHONE_VOICE_2("PhoneVoice2"),
        EMAIL_ADDRESS("EmailAddress"),
        LANGUAGE_ID("LanguageID"),
        DELIVERY_OPTION_ID("DeliveryOptionID"),
        USER_NAME("UserName"),
        PASSWORD("Password"),
        PASSWORD_2("Password2"),
        ALT_EMAIL_ADDRESS("AltEmailAddress"),
        PHONE_VOICE_3("PhoneVoice3"),
        PHONE_1_CARRIER_ID("Phone1CarrierID"),
        PHONE_2_CARRIER_ID("Phone2CarrierID"),
        PHONE_3_CARRIER_ID("Phone3CarrierID"),
        ENABLE_SMS("Enable SMS"),
        TXT_PHONE_NUMBER("TxtPhoneNumber"),
        BARCODE("Barcode"),
        ERECEIPT_OPTION_ID("EReceiptOPtionID"); // Ereceipt option ID [4.1 only] 2 - Email Address 8 - TXT Messaging 100 - All
        
        private String type;

        private CreateOrder(String s)
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
     * The following are update-able via Polaris PatronUpdate.
     */
    public enum UpdateOrder
    {
        PATRON_UPDATE_DATA("PatronUpdateData"), // TAG NAME
        LOGON_BRANCH_ID("LogonBranchID"),
        LOGON_USER_ID("LogonUserID"),
        LOGON_WORKSTATION_ID("LogonWorkstationID"),
        READING_LIST_FLAG("ReadingListFlag"),
        EMAIL_FORMAT("EmailFormat"),
        DELIVERY_OPTION("DeliveryOption"),
        EMAIL_ADDRESS("EmailAddress"),
        PHONE_VOICE_1("PhoneVoice1"),
        PASSWORD("Password");
        
        private String type;

        private UpdateOrder(String s)
        {
            this.type = s;
        }

        @Override
        public String toString()
        {
            return this.type;
        }
    }
    
    private boolean debug = true;
    private final DataType dataType;
    private final EnumMap<CreateOrder, String> columns;
    public final static String TABLE_NAME = "USER";
    public final static String DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    public static PAPIFormattedTable getInstanceOf(DataType type)
    {
        return new PAPIFormattedTable(type);
    }
    
    private PAPIFormattedTable(DataType type)
    {
        this.columns = new EnumMap<>(CreateOrder.class);
        this.dataType = type;
    }
    
    @Override
    public String getData()
    {
        StringBuilder sb = new StringBuilder();
        if (dataType == DataType.XML_CREATE)
        {
            formatAsXML(sb);
        }
        else
        {
            formatAsJSON(sb);
        }
        return sb.toString();
    }
        
    private void formatAsXML(StringBuilder sb)
    {
        sb.append(PAPIFormattedTable.DECLARATION);
        sb.append(this.createTag(CreateOrder.PATRON_REGISTRATION_CREATE_DATA.toString(), false));
        for (CreateOrder coType: this.columns.keySet())
        {
            if (this.columns.get(coType) != null)
            {
                sb.append(this.createTaggedContent(coType.toString(), this.columns.get(coType)));
            }            
        }
        sb.append(this.createTag(CreateOrder.PATRON_REGISTRATION_CREATE_DATA.toString(), true));
    }
    
    private void formatAsJSON(StringBuilder sb)
    {
        throw new UnsupportedOperationException("JSON not supported yet."); 
    }
    
    private String createTag(String tagName, boolean isClosedTag)
    {
        if (tagName.isEmpty())
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        if (isClosedTag)
        {
            sb.append("/");
        }
        sb.append(tagName);
        sb.append(">");
        return sb.toString();
    }
    private String createTaggedContent(String tagName, String content)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.createTag(tagName, false));
        sb.append(content);
        sb.append(this.createTag(tagName, true));
        return sb.toString();
    }

    @Override
    public String getHeader()
    {
        String value = this.columns.get(CreateOrder.BARCODE);
        if (value == null)
        {
            return "";
        }
        return value;
    }

    @Override
    public String getName()
    {
        return PAPIFormattedTable.TABLE_NAME;
    }

    @Override
    public String getValue(String key)
    {
        String value = "";
        try
        {
            CreateOrder order = CreateOrder.valueOf(key);
            value = this.columns.get(order);
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(PAPIFormattedTable.class.getName() + 
                    "getValue failed: Couldn't use key: '" + key + 
                    " as reference in papi table.");
            return "";
        }
        if (value == null)
        {
            return "";
        }
        return value;
    }

    @Override
    public boolean setValue(String key, String value)
    {
        try
        {
            this.columns.put(CreateOrder.valueOf(key), value);
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(PAPIFormattedTable.class.getName() + 
                    "Couldn't save key: '" + key + 
                    "' with value '" + value + "',"
                    + " No such element.");
            return false;
        }
        return this.columns.containsKey(CreateOrder.valueOf(key));
    }

    @Override
    public boolean renameKey(String originalkey, String replacementKey)
    {
        String value = "";
        try
        {
            CreateOrder order = CreateOrder.valueOf(originalkey);
            value = this.columns.remove(order);
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(PAPIFormattedTable.class.getName() + 
                    "renameKey failed: Couldn't use key: '" + originalkey + 
                    " as reference in papi table.");
            return false;
        }
        return this.setValue(replacementKey, value);
    }

    @Override
    public boolean deleteValue(String key)
    {
        try
        {
            CreateOrder order = CreateOrder.valueOf(key);
            String value = this.columns.remove(order);
            // return false if the value is not found.
            if (value == null)
            {
                return false;
            }
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(PAPIFormattedTable.class.getName() + 
                    "deleteValue failed: Couldn't use key: '" + key + 
                    " as reference in papi table.");
            return false;
        }
        return true;
    }
    
}
