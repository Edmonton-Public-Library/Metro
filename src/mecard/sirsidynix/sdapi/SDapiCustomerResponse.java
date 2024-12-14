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
import mecard.config.SDapiUserFields;

public class SDapiCustomerResponse 
        extends SDapiResponse
        implements CustomerMessage
{
//    @SerializedName("searchQuery")
//    private String searchQuery;

    @SerializedName("result")
    private List<CustomerResult> result;

    @Override
    public boolean succeeded() 
    {
        return (result != null && !result.isEmpty());
    }

    // Failed response
    //{
    //   "searchQuery": "ID:212210123456789",
    //   "startRow": 1,
    //   "lastRow": 10,
    //   "rowsPerPage": 10,
    //   "totalResults": 0,
    //   "result": []
    //}
    @Override
    public String errorMessage() 
    {
        if (! this.succeeded())
        {
            return "Account not found.";
        }
        return "";
    }

    @Override
    public String getCustomerProfile() 
    {
        try
        {
            return getCustomerFields().getProfile(); //    .profile.getKey();
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
                return this.result.get(0).getUserKey();
            if (fieldName.equals(SDapiUserFields.USER_FIRST_NAME.toString()))
                return this.getCustomerFields().getFirstName();
            if (fieldName.equals(SDapiUserFields.USER_LAST_NAME.toString()))
                return this.getCustomerFields().getLastName();
            if (fieldName.equals(SDapiUserFields.USER_ID.toString()))
                return this.getCustomerFields().getBarcode();
            if (fieldName.equals(SDapiUserFields.USER_ALTERNATE_ID.toString()))
                return this.getCustomerFields().getAlternateId();
            if (fieldName.equals(SDapiUserFields.EMAIL.toString()))
                return this.getCustomerFields().getEmail();
            if (fieldName.equals(SDapiUserFields.PROFILE.toString()))
                return this.getCustomerFields().getProfile();
            if (fieldName.equals(SDapiUserFields.PHONE.toString()))
                return this.getCustomerFields().getPhone();
            if (fieldName.equals(SDapiUserFields.USER_BIRTHDATE.toString()))
                return this.getDateField(fieldName);
            if (fieldName.equals(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
                return this.getDateField(fieldName);
            if (fieldName.equals(SDapiUserFields.CITY_SLASH_PROV.toString()))
                // returns city Edmonton not the CITY/STATE value of 'Edmonton, Alberta'.
                return this.getCustomerFields().getCity();
            if (fieldName.equals(SDapiUserFields.CITY_SLASH_STATE.toString()))
                // returns city Edmonton not the CITY/STATE value of 'Edmonton, Alberta'.
                return this.getCustomerFields().getCity();
            if (fieldName.equals(SDapiUserFields.STREET.toString()))
                return this.getCustomerFields().getStreet();
            if (fieldName.equals(SDapiUserFields.POSTALCODE.toString()))
                return this.getCustomerFields().getPostalCode();
            if (fieldName.equals(SDapiUserFields.PROV.toString()))
                return this.getCustomerFields().getProvince();
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
                date = this.getCustomerFields().getBirthDate();
            if (fieldName.equals(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
                date = this.getCustomerFields().getExpiry();
        }
        catch (NullPointerException ne)
        {
            return "";
        }
        if (! date.isBlank())
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
            if (getCustomerFields().standing != null)
                return getCustomerFields().standing.getKey();
        } 
        catch (NullPointerException ne)
        {
            return "";
        }
        return "";
    }

    @Override
    public boolean cardReportedLost() 
    {
        // Check the if the PROFILE is LOST or LOSTCARD.
        return this.getCustomerProfile().equals("LOST") || this.getCustomerProfile().equals("LOSTCARD");
    }

    @Override
    public boolean isInGoodStanding() 
    {
        return "OK".equals(this.getStanding());
    }

    // Inner class to represent the result
    private static class CustomerResult {
//        @SerializedName("resource")
//        private String resource;

        @SerializedName("key")
        private String userKey;

        @SerializedName("fields")
        private CustomerFields fields;
        
        private String getUserKey()
        {
            return this.userKey != null ? this.userKey : "";
        }
    }

    // Inner class to represent all fields
    private static class CustomerFields {
//        @SerializedName("displayName")
//        private String displayName;

        @SerializedName("barcode")
        private String barcode;

        @SerializedName("birthDate")
        private String birthDate;

//        @SerializedName("createdDate")
//        private String createdDate;

//        @SerializedName("department")
//        private String department;
        
        @SerializedName("alternateID")
        private String alternateId;

        @SerializedName("firstName")
        private String firstName;

        @SerializedName("lastName")
        private String lastName;

//        @SerializedName("middleName")
//        private String middleName;

//        @SerializedName("preferredName")
//        private String preferredName;

        @SerializedName("privilegeExpiresDate")
        private String expiryDate;

//        @SerializedName("groupId")
//        private String groupId;
//
//        @SerializedName("circRecordCount")
//        private Integer circRecordCount;
//
//        // Nested object getters
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

//        @SerializedName("category02")
//        private PolicyResource category02;
//
//        @SerializedName("category04")
//        private PolicyResource category04;
//
//        @SerializedName("category05")
//        private PolicyResource category05;

        @SerializedName("address1")
        private List<AddressEntry> address1;

        // Nested static class for policy resources
        private static class PolicyResource {
//            @SerializedName("resource")
//            private String resource;

            @SerializedName("key")
            private String key;

//            public String getResource() { return resource; }
            private String getKey() { return key != null ? key : ""; }
        }

        // Nested static class for address entries
        private static class AddressEntry {
            @SerializedName("fields")
            private AddressFields fields;

            private static class AddressFields 
            {
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
//        public String getDisplayName() { return displayName; }
        private String getBarcode() { return barcode != null ? barcode : ""; }
        private String getAlternateId() { return alternateId != null ? alternateId : ""; }
        private String getBirthDate() { return birthDate != null ? birthDate : ""; }
//        public String getCreatedDate() { return createdDate; }
//        public String getDepartment() { return department; }
        private String getFirstName() { return firstName != null ? firstName : ""; }
        private String getLastName() { return lastName != null ? lastName : ""; }
//        public String getMiddleName() { return middleName; }
//        public String getPreferredName() { return preferredName; }
        private String getExpiry() { return this.expiryDate != null ? expiryDate : ""; }
//        public String getGroupId() { return groupId; }
//        public Integer getCircRecordCount() { return circRecordCount; }

        // Convenience methods for nested resources
//        public String getAccessKey() { return access != null ? access.getKey() : null; }
//        public String getLibraryKey() { return library != null ? library.getKey() : null; }
        private String getProfile() { return profile != null ? profile.getKey() : null; }

        // Method to find specific address by code
        private String findAddressByCode(String addressCode) {
            if (address1 != null) {
                for (AddressEntry entry : address1) {
                    if (addressCode.equals(entry.getFields().getCode())) {
                        return entry.getFields().getData();
                    }
                }
            }
            return null;
        }

        // Convenience methods to get specific addresses
//        public String getEmail() { return findAddressByCode("EMAIL"); }
        private String getEmail() { return findAddressByCode(SDapiUserFields.EMAIL.toString()); }
        private String getPostalCode() { return findAddressByCode(SDapiUserFields.POSTALCODE.toString()); }
        private String getPhone() { return findAddressByCode(SDapiUserFields.PHONE.toString()); }
        private String getStreet() { return findAddressByCode(SDapiUserFields.STREET.toString()); }
//        public String getCareOf() { return findAddressByCode(SDapiUserFields.CARE_SLASH_OF.toString()); }
        // This may need to be dynamic based on ILS setup.
        private String getCity() 
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
        
        private String getProvince()
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
    public static SDapiResponse parseJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiCustomerResponse.class);
    }

    // Getter to easily access the first result
    private CustomerFields getCustomerFields() 
    {
        return (result != null && !result.isEmpty()) ? result.get(0).fields : null;
    }
}