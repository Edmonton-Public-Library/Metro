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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.EnumMap;
import mecard.customer.FormattedTable;

/**
 * This class represents a table of customer data for loading.
 * For PAPI XML, this table represents a complete table of customer data, that 
 * can be added to a REST POST request to create customer.
 * 
 * This class uses a factory method because there are 4 variations of tables.
 * There are create tables which contain a complete set of data about the customer,
 * and which come in 1 of 2 flavors: XML and JSON. This class must be able to 
 * make customer update tables which are (much) more restricted in information,
 * and also come in two flavors: XML and JSON.
 * @author anisbet
 */
public class PAPIFormattedTable implements FormattedTable
{
    /**
     * Content types possible under the Polaris API specification.
     */
    public enum ContentType
    {
        XML,
        JSON;
    }
    
    public enum QueryType
    {
        CREATE,
        UPDATE;
    }
    
    /** This is the entire set of create fields accepted by Polaris in WS 4.1.
     * The order must not change. The values preceded by 'C_' are for creating 
     * customer records. The values preceded by 'U_' are update-able fields on
     * a customer's account.
     */
    public enum Order
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

        private Order(String s)
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
    private final ContentType dataFormat;
    private final QueryType queryType;
    private final EnumMap<Order, String> columns;
    public final static String TABLE_NAME = "USER";
    public final static String DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    /**
     * Default factory method creates a table for creating a user with XML formatting.
     * @return Formatted table ready for submission by RESTful POST.
     */
    public static PAPIFormattedTable getInstanceOf()
    {
        return new PAPIFormattedTable(ContentType.XML, QueryType.CREATE);
    }
    
    /**
     * Specifies the formatting {@link PAPIFormattedTable.ContentType} and defaults
     * to a create user query.
     * @param type
     * @return create query formatted as per argument.
     */
    public static PAPIFormattedTable getInstanceOf(ContentType type)
    {
        return new PAPIFormattedTable(type, QueryType.CREATE);
    }
    
    /**
     * Specifies the formatting {@link PAPIFormattedTable.ContentType}, and 
     * a query type of {@link PAPIFormattedTable.QueryType}.
     * @param type
     * @param qType
     * @return create or update query formatted as per argument.
     */
    public static PAPIFormattedTable getInstanceOf(ContentType type, QueryType qType)
    {
        return new PAPIFormattedTable(type, qType);
    }
    
    private PAPIFormattedTable(ContentType type, QueryType qType)
    {
        this.columns    = new EnumMap<>(Order.class);
        this.dataFormat = type;
        this.queryType  = qType;
    }
    
    @Override
    public String getData()
    {
        StringBuilder sb = new StringBuilder();
        switch (dataFormat)
        {
            case XML:
                formatAsXML(sb);
                break;
            case JSON:
                formatAsJSON(sb);
                break;
            default:
                formatAsXML(sb);
                break;
        }
        return sb.toString();
    }
        
    private void formatAsXML(StringBuilder sb)
    {
        sb.append(PAPIFormattedTable.DECLARATION);
        sb.append(this.createTag(Order.C_PATRON_TAG.toString(), false));
        for (Order coType: this.columns.keySet())
        {
            if (this.columns.get(coType) != null)
            {
                sb.append(this.createTaggedContent(coType.toString(), this.columns.get(coType)));
            }            
        }
        sb.append(this.createTag(Order.C_PATRON_TAG.toString(), true));
    }
    
    /* 
     * Note: this method is not supported yet since the only instance developed
     * against is XML. 
     * @param sb the the data to be converted into JSON.
     */
    private void formatAsJSON(StringBuilder sb)
    {
        // TODO: find out if there is additional enclosing parameters included in 
        // the JSON like the outer tags in XML.
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(PAPIFormattedTable.class, new TableSerializer());
        Gson gson = gsonBuilder.create();
        if (debug)
        {
            System.out.println("JSON:"+gson.toJson(this));
        }
        sb.append(gson.toJson(this));
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
        String value = this.columns.get(Order.C_BARCODE);
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
            Order order = Order.valueOf(key);
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
            this.columns.put(Order.valueOf(key), value);
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(PAPIFormattedTable.class.getName() + 
                    "Couldn't save key: '" + key + 
                    "' with value '" + value + "',"
                    + " No such element.");
            return false;
        }
        return this.columns.containsKey(Order.valueOf(key));
    }

    @Override
    public boolean renameKey(String originalkey, String replacementKey)
    {
        String value = "";
        try
        {
            Order order = Order.valueOf(originalkey);
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
            Order order = Order.valueOf(key);
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
    


    /**
     *
     * @author Andrew Nisbet <anisbet@epl.ca>
     */
    private class TableSerializer implements JsonSerializer<PAPIFormattedTable>
    {

        @Override
        public JsonElement serialize(PAPIFormattedTable papiTable, Type type, JsonSerializationContext context)
        {
            final JsonObject json = new JsonObject();
            for (PAPIFormattedTable.Order papiOrder: PAPIFormattedTable.Order.values())
            {
                String storedValue = papiTable.getValue(papiOrder.name());
                if (storedValue == null || storedValue.isEmpty())
                {
                    continue;
                }
                json.addProperty(papiOrder.toString(), storedValue);
            }
            return json;
        }
    }
}
