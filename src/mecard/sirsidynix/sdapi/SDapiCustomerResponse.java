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
import mecard.util.DateComparer;

public class SDapiCustomerResponse 
        extends SDapiResponse
        implements CustomerMessage
{
    @SerializedName("searchQuery")
    private String searchQuery;

    @SerializedName("result")
    private List<CustomerResult> result;

    @Override
    public boolean succeeded() 
    {
        return getCustomerFields().getBarcode() != null;
    }

    @Override
    public String errorMessage() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getCustomerProfile() 
    {
        return getCustomerFields().getProfile(); //    .profile.getKey();
    }

    @Override
    public String getField(String fieldName) 
    {
        // Use the FlatUserFieldTypes class and a switch statement.
        if (fieldName.equals(SDapiUserFields.USER_FIRST_NAME.toString()))
            return this.getCustomerFields().getFirstName();
        return "";
    }

    @Override
    public String getDateField(String fieldName) 
    {
//        return DateComparer.getANSIDate(this.fields.getOrDefault(fieldName, ""));
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isEmpty(String fieldName) 
    {
        return this.getField(fieldName).isBlank();
    }

    @Override
    public String getStanding()
    {
        return getCustomerFields().standing.getKey();
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
    public static class CustomerResult {
        @SerializedName("resource")
        private String resource;

        @SerializedName("key")
        private String userKey;

        @SerializedName("fields")
        private CustomerFields fields;
        
        public String getUserKey()
        {
            return this.userKey;
        }
    }

    // Inner class to represent all fields
    public static class CustomerFields {
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

        @SerializedName("firstName")
        private String firstName;

        @SerializedName("lastName")
        private String lastName;

//        @SerializedName("middleName")
//        private String middleName;

//        @SerializedName("preferredName")
//        private String preferredName;

//        @SerializedName("webAuthID")
//        private String webAuthID;

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
        public static class PolicyResource {
//            @SerializedName("resource")
//            private String resource;

            @SerializedName("key")
            private String key;

//            public String getResource() { return resource; }
            public String getKey() { return key; }
        }

        // Nested static class for address entries
        public static class AddressEntry {
            @SerializedName("fields")
            private AddressFields fields;

            public static class AddressFields 
            {
                @SerializedName("code")
                private PolicyResource code;

                @SerializedName("data")
                private String data;

                public String getCode() { return code != null ? code.getKey() : ""; }
                public String getData() { return data; }
            }

            public AddressFields getFields() { return fields; }
        }

        // Getters for all fields
//        public String getDisplayName() { return displayName; }
        public String getBarcode() { return barcode; }
        public String getBirthDate() { return birthDate; }
//        public String getCreatedDate() { return createdDate; }
//        public String getDepartment() { return department; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
//        public String getMiddleName() { return middleName; }
//        public String getPreferredName() { return preferredName; }
//        public String getWebAuthID() { return webAuthID; }
//        public String getGroupId() { return groupId; }
//        public Integer getCircRecordCount() { return circRecordCount; }

        // Convenience methods for nested resources
//        public String getAccessKey() { return access != null ? access.getKey() : null; }
//        public String getLibraryKey() { return library != null ? library.getKey() : null; }
        public String getProfile() { return profile != null ? profile.getKey() : null; }

        // Method to find specific address by code
        public String findAddressByCode(String addressCode) {
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
        public String getEmail() { return findAddressByCode(FlatUserFieldTypes.EMAIL.toString()); }
        public String getPostalCode() { return findAddressByCode(FlatUserFieldTypes.POSTALCODE.toString()); }
        public String getPhone() { return findAddressByCode(FlatUserFieldTypes.PHONE.toString()); }
        public String getStreet() { return findAddressByCode(FlatUserFieldTypes.STREET.toString()); }
        // This may need to be dynamic based on ILS setup.
        public String getCity() { return findAddressByCode(FlatUserFieldTypes.CITY_SLASH_STATE.toString()); }
    }

    // Static method to parse JSON
    public static SDapiResponse parseJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiCustomerResponse.class);
    }

    // Getter to easily access the first result
    public CustomerFields getCustomerFields() 
    {
        return result != null && !result.isEmpty() ? result.get(0).fields : null;
    }
}