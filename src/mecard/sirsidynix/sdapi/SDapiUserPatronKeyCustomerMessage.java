

package mecard.sirsidynix.sdapi;

import api.CustomerMessage;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import mecard.config.FlatUserFieldTypes;
/**
 *
 * @author anisbet
 */
public class SDapiUserPatronKeyCustomerMessage 
        extends SDapiResponse
        implements CustomerMessage
{
//    @SerializedName("resource")
//    private String resource;

    @SerializedName("key")
    private String userKey;

    @SerializedName("fields")
    private PatronFields fields;

    @Override
    public boolean succeeded() 
    {
        return getFields().barcode != null && ! getFields().barcode.isBlank();
    }

    @Override
    public String errorMessage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getCustomerProfile() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getField(String fieldName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getDateField(String fieldName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isEmpty(String fieldName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getStanding() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean cardReportedLost() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isInGoodStanding() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Inner class to represent all fields
    public static class PatronFields {
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

//        @SerializedName("standing")
//        private PolicyResource standing;
//
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
            @SerializedName("resource")
            private String resource;

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
        protected String getEmail() { return findAddressByCode(FlatUserFieldTypes.EMAIL.toString()); }
        protected String getPostalCode() { return findAddressByCode(FlatUserFieldTypes.POSTALCODE.toString()); }
        protected String getPhone() { return findAddressByCode(FlatUserFieldTypes.PHONE.toString()); }
        protected String getStreet() { return findAddressByCode(FlatUserFieldTypes.STREET.toString()); }
        // This may need to be done over for Symphony systems that use CITY/PROV etc.
        protected String getCity() { return findAddressByCode(FlatUserFieldTypes.CITY_SLASH_STATE.toString()); }
    }

    // Static method to parse JSON
    public static SDapiResponse parseJson(String jsonString) 
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiUserPatronKeyCustomerMessage.class);
    }

    // Getter for fields
    protected PatronFields getFields() 
    {
        return fields;
    }

    // Getter for userKey
    protected String getUserKey() 
    {
        return userKey;
    }
}
