/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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

package mecard.sirsidynix.sdapi;

import api.CustomerMessage;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import mecard.config.FlatUserFieldTypes;
import mecard.config.SDapiUserFields;
/**
 *
 * @author anisbet
 */
public class SDapiUserPatronKeyCustomerMessage 
        extends SDapiResponse
        implements CustomerMessage
{
    
    
    public String getAlternateId()
    {
        try
        {
            return this.fields.alternateId;
        }
        catch (NullPointerException ne)
        {
            return "";
        }
    }

    @SerializedName("key")
    private String userKey;

    @SerializedName("fields")
    private PatronFields fields;

    @Override
    public boolean succeeded() 
    {
        return (this.messageList == null);
    }

    //Failed response
    //    {
    //    "messageList": [
    //        {
    //            "code": "recordNotFound",
    //            "message": "Could not find a(n) /user/patron record with the key 3015800."
    //        }
    //    ]
    //    }
    @SerializedName("messageList")
    private List<MessageList> messageList;
    
    private class MessageList
    {
        @SerializedName("code")
        private String code;
        
        private String getMessageListCode()
        {
            return this.code != null ? this.code : "";
        }
    }
    
    @Override
    public String errorMessage() 
    {
        if (! this.succeeded())
        {
            try
            {
                StringBuilder sbout = new StringBuilder();
                for (MessageList list: messageList)
                {
                    sbout.append(list.getMessageListCode());
                }
                return sbout.toString();
            }
            catch (NullPointerException ne)
            {
                System.out.println("""
                                   **error, the request for customer info was made
                                   but failed to return either a success or 
                                   failed response.
                                   """);
                return "Account not found.";
            }
        }
        return "";
    }

    @Override
    public String getCustomerProfile() 
    {
        try
        {
            return this.fields.getProfileKey();
        }
        catch (NullPointerException ne)
        {
            return "";
        }
    }

    @Override
    public String getField(String fieldName) 
    {
        try
        {
            if (fieldName.equals(SDapiUserFields.USER_KEY.toString()))
                return this.userKey;
            if (fieldName.equals(SDapiUserFields.USER_FIRST_NAME.toString()))
                return this.fields.getFirstName();
            if (fieldName.equals(SDapiUserFields.USER_LAST_NAME.toString()))
                return this.fields.getLastName();
            if (fieldName.equals(SDapiUserFields.USER_ID.toString()))
                return this.fields.getBarcode();
            if (fieldName.equals(SDapiUserFields.USER_ALTERNATE_ID.toString()))
                return this.getAlternateId();
            if (fieldName.equals(SDapiUserFields.EMAIL.toString()))
                return this.fields.getEmail();
            if (fieldName.equals(SDapiUserFields.PROFILE.toString()))
                return this.fields.getProfileKey();
            if (fieldName.equals(SDapiUserFields.PHONE.toString()))
                return this.fields.getPhone();
            if (fieldName.equals(SDapiUserFields.USER_BIRTHDATE.toString()))
                return this.getDateField(fieldName);
            if (fieldName.equals(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
                return this.getDateField(fieldName);
            if (fieldName.equals(SDapiUserFields.CITY_SLASH_PROV.toString()))
                // returns city Edmonton not the CITY/STATE value of 'Edmonton, Alberta'.
                return this.fields.getCity();
            if (fieldName.equals(SDapiUserFields.CITY_SLASH_STATE.toString()))
                // returns city Edmonton not the CITY/STATE value of 'Edmonton, Alberta'.
                return this.fields.getCity();
            if (fieldName.equals(SDapiUserFields.STREET.toString()))
                return this.fields.getStreet();
            if (fieldName.equals(SDapiUserFields.POSTALCODE.toString()))
                return this.fields.getPostalCode();
            if (fieldName.equals(SDapiUserFields.PROV.toString()))
                return this.fields.getProvince();
        }
        catch (NullPointerException | IndexOutOfBoundsException ne)
        {
            return "";
        }
        
        return "";
    }

    @Override
    public String getDateField(String fieldName) 
    {
        String time = "T00:00:00";
        String date = "";
        try
        {
            // Returns any USER dates converted to ANSI.
            if (fieldName.equals(SDapiUserFields.USER_BIRTHDATE.toString()))
                date = this.fields.getBirthDate();
            if (fieldName.equals(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
                date = this.fields.getPrivilegeExpires();
        }
        catch (NullPointerException ne)
        {
            return "";
        }
        if (date != null && ! date.isBlank())
            return date + time;
        return "";
    }

    @Override
    public boolean isEmpty(String fieldName) 
    {
        try
        {
            return this.getField(fieldName).isBlank();
        }
        catch (NullPointerException ne)
        {
            return true;
        }
    }

    @Override
    public String getStanding()
    {
        try
        {
            return this.fields.standing.getKey();
        }
        catch (NullPointerException ne)
        {
            return "";
        }
    }

    @Override
    public boolean cardReportedLost() 
    {
        return this.getCustomerProfile().equals("LOST") || this.getCustomerProfile().equals("LOSTCARD");
    }

    @Override
    public boolean isInGoodStanding() 
    {
        return "OK".equals(this.getStanding());
    }

    // Inner class to represent all fields
    public static class PatronFields 
    {
        @SerializedName("alternateID")
        private String alternateId;
        
        @SerializedName("displayName")
        private String displayName;

        @SerializedName("firstName")
        private String firstName;

        @SerializedName("lastName")
        private String lastName;

//        @SerializedName("middleName")
//        private String middleName;

        @SerializedName("preferredName")
        private String preferredName;

        @SerializedName("barcode")
        private String barcode;

        @SerializedName("birthDate")
        private String birthDate;

        @SerializedName("createdDate")
        private String createdDate;

//        @SerializedName("department")
//        private String department;

//        @SerializedName("groupId")
//        private String groupId;

//        @SerializedName("webAuthID")
//        private String webAuthID;
//
//        @SerializedName("circRecordCount")
//        private Integer circRecordCount;
//
//        @SerializedName("keepCircHistory")
//        private String keepCircHistory;

        // Nested object getters
//        @SerializedName("access")
//        private PolicyResource access;

//        @SerializedName("checkoutLocation")
//        private PolicyResource checkoutLocation;
//
//        @SerializedName("environment")
//        private PolicyResource environment;
//
//        @SerializedName("language")
//        private PolicyResource language;

//        @SerializedName("library")
//        private PolicyResource library;

        @SerializedName("profile")
        private PolicyResource profile;

        @SerializedName("standing")
        private PolicyResource standing;
//
//        @SerializedName("category02")
//        private PolicyResource category02;
//
//        @SerializedName("category04")
//        private PolicyResource category04;
//
//        @SerializedName("category05")
//        private PolicyResource category05;
        
        @SerializedName("privilegeExpiresDate")
        private String expiry;
        
        private String getPrivilegeExpires()
        {
            return expiry;
        }

        @SerializedName("address1")
        private List<AddressEntry> address1;

        // Nested static class for policy resources
        private static class PolicyResource {
//            @SerializedName("resource")
//            private String resource;

            @SerializedName("key")
            private String key;

//            private String getResource() { return resource; }
            private String getKey() { return key; }
        }

        // Nested static class for address entries
        private static class AddressEntry {
            @SerializedName("fields")
            private AddressFields fields;

            private static class AddressFields {
                @SerializedName("code")
                private PolicyResource code;

                @SerializedName("data")
                private String data;

                public String getCode() { return code != null ? code.getKey() : ""; }
                public String getData() { return data; }
            }

            private AddressFields getFields() { return fields; }
        }

        // Getters for all fields
        protected String getDisplayName() { return displayName; }
        protected String getFirstName() { return firstName; }
        protected String getLastName() { return lastName; }
//        public String getMiddleName() { return middleName; }
        protected String getPreferredName() { return preferredName; }
        protected String getBarcode() { return barcode; }
        protected String getBirthDate() { return birthDate; }
        protected String getCreatedDate() { return createdDate; }
//        public String getDepartment() { return department; }
//        public String getGroupId() { return groupId; }
//        public String getWebAuthID() { return webAuthID; }
//        public Integer getCircRecordCount() { return circRecordCount; }
//        public String getKeepCircHistory() { return keepCircHistory; }

        // Convenience methods for nested resources
//        public String getAccessKey() { return access != null ? access.getKey() : null; }
//        public String getLibraryKey() { return library != null ? library.getKey() : null; }
        protected String getProfileKey() { return profile != null ? profile.getKey() : null; }

        // Method to find specific address by code
        private String findAddressByCode(String addressCode) {
            if (address1 != null) {
                for (AddressEntry entry : address1) {
                    if (addressCode.equals(entry.getFields().getCode())) {
                        return entry.getFields().getData();
                    }
                }
            }
            return "";
        }

        // Convenience methods to get specific addresses
        protected String getEmail() { return findAddressByCode(SDapiUserFields.EMAIL.toString()); }
        protected String getPostalCode() { return findAddressByCode(SDapiUserFields.POSTALCODE.toString()); }
        protected String getPhone() { return findAddressByCode(SDapiUserFields.PHONE.toString()); }
        protected String getStreet() { return findAddressByCode(SDapiUserFields.STREET.toString()); }
        // This may need to be done over for Symphony systems that use CITY/PROV etc.
        protected String getCity() 
        {
            try
            {
                String[] words = 
                        findAddressByCode(SDapiUserFields.CITY_SLASH_STATE.toString())
                                .replaceAll("[^a-zA-Z ]", "").split("\\s+");
                return words[0];
            }
            catch (IndexOutOfBoundsException e)
            {
                return "";
            }
        }
        
        protected String getProvince()
        {
            try
            {
                String[] words = 
                        findAddressByCode(SDapiUserFields.CITY_SLASH_STATE.toString())
                                .replaceAll("[^a-zA-Z ]", "").split("\\s+");
                return words[1];
            }
            catch (IndexOutOfBoundsException e)
            {
                return "";
            }
        }
    }

    // Static method to parse JSON
    public static SDapiResponse parseJson(String jsonString) 
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiUserPatronKeyCustomerMessage.class);
    }

    // Getter for userKey
    public String getUserKey() 
    {
        try
        {
            return this.userKey;
        }
        catch (IndexOutOfBoundsException e)
        {
            return "";
        }
    }
}
