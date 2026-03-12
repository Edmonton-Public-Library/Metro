/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2026 Edmonton Public Library
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
import java.util.List;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import java.util.*;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiPropertyTypes;
import mecard.config.SDapiUserFields;


/**
 * Used for handling JSON response from /user/patron/barcode/{barcode}{?param}
 * endpoint.
 * 
 * @see SDapiUserPatronSearchResponse
 * @author anisbet
 */
public class SDapiUserPatronBarcodeResponse 
        extends PatronSearchResponse 
{
//    @SerializedName("barcode")
//       private String barcode;
//
//       @SerializedName("birthDate")
//       private String birthDate;
//
////        @SerializedName("createdDate")
////        private String createdDate;
//
//       @SerializedName("alternateID")
//       private String alternateId;
//
//       @SerializedName("firstName")
//       private String firstName;
//
//       @SerializedName("lastName")
//       private String lastName;
//
//       @SerializedName("privilegeExpiresDate")
//       private String expiryDate;
//
//       @SerializedName("profile")
//       private PolicyResource profile;
//
//       @SerializedName("standing")
//       private PolicyResource standing;
//
//       @SerializedName("address1")
//       private List<AddressEntry> address1;
    //{
    //  "resource":"/user/patron",
    //  "key":"218427",
    @SerializedName("key")
    private String userKey;
    private String getUserKey() { return userKey != null ? userKey : ""; }
    
    //  "fields":{
    @SerializedName("fields")
    private CustomerFields fields;
    
    // Inner class to represent all fields
    private static class CustomerFields 
    {

        @SerializedName("barcode")
        private String barcode;

        @SerializedName("birthDate")
        private String birthDate;

//        @SerializedName("createdDate")
//        private String createdDate;
        
        @SerializedName("alternateID")
        private String alternateId;

        @SerializedName("firstName")
        private String firstName;

        @SerializedName("lastName")
        private String lastName;

        @SerializedName("privilegeExpiresDate")
        private String expiryDate;

        @SerializedName("profile")
        private PolicyResource profile;

        @SerializedName("standing")
        private PolicyResource standing;

        @SerializedName("address1")
        private List<AddressEntry> address1;

        // Nested static class for policy resources
        private static class PolicyResource 
        {
            @SerializedName("key")
            private String key;

            private String getKey() { return key != null ? key : ""; }
        }

        // Nested static class for address entries
        private static class AddressEntry 
        {
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
        private String getBarcode() { return barcode != null ? barcode : ""; }
        private String getAlternateId() { return alternateId != null ? alternateId : ""; }
        private String getBirthDate() { return birthDate != null ? birthDate : ""; }
        private String getFirstName() { return firstName != null ? firstName : ""; }
        private String getLastName() { return lastName != null ? lastName : ""; }
        private String getExpiry() { return this.expiryDate != null ? expiryDate : ""; }

        // Convenience method for nested resources
        private String getProfile() { return profile != null ? profile.getKey() : null; }
        private String getStanding() { return standing != null ? standing.getKey() : ""; }

        // Method to find specific address by code
        private String findAddressByCode(String addressCode) 
        {
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
        private String getEmail() { return findAddressByCode(SDapiUserFields.EMAIL.toString()); }
        private String getPostalCode() { return findAddressByCode(SDapiUserFields.POSTALCODE.toString()); }
        private String getPhone() { return findAddressByCode(SDapiUserFields.PHONE.toString()); }
        private String getStreet() { return findAddressByCode(SDapiUserFields.STREET.toString()); }
        // This may need to be dynamic based on ILS setup.
        private String getCity() 
        {
            Properties props = PropertyReader.getProperties(ConfigFileTypes.SIRSIDYNIX_API);
            // This isn't a required property so default to 'false' which
            // means use CITY_SLASH_STATE
            boolean useCityProv = Boolean.parseBoolean(props.getProperty("use-city-province", "false"));
            try
            {
                String[] words;
                if (useCityProv)
                {
                    words = findAddressByCode(SDapiUserFields.CITY_SLASH_PROV.toString())
                        .replaceAll("[^a-zA-Z ]", "").split("\\s+");
                }
                else
                    words = findAddressByCode(SDapiUserFields.CITY_SLASH_STATE.toString())
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
            Properties props = PropertyReader.getProperties(ConfigFileTypes.SIRSIDYNIX_API);
            // This isn't a required property so default to 'false' which
            // means use CITY_SLASH_STATE
            boolean useCityProv = Boolean.parseBoolean(props.getProperty("use-city-province", "false"));
            try
            {
                String[] words;
                if (useCityProv)
                {
                    words = findAddressByCode(SDapiUserFields.CITY_SLASH_PROV.toString())
                                .replaceAll("[^a-zA-Z ]", "").split("\\s+");
                }
                else
                    words = findAddressByCode(SDapiUserFields.CITY_SLASH_STATE.toString())
                                .replaceAll("[^a-zA-Z ]", "").split("\\s+");
                return words[1];
            }
            catch (IndexOutOfBoundsException e)
            {
                return "";
            }
        }
    }
    
    @Override
    public boolean succeeded() 
    {
        return (fields != null && ! fields.getBarcode().isBlank());
    }

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
        return this.getField(SDapiUserFields.PROFILE.toString());
    }

    @Override
    public String getField(String fieldName) 
    {
        try
        {
            if (fieldName.equals(SDapiUserFields.USER_KEY.toString()))
                return this.getUserKey();
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
            if (fieldName.equals(SDapiUserFields.STANDING.toString()))
                return this.getCustomerFields().getStanding();
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
    
    // Getter to easily access the customer fields
    private CustomerFields getCustomerFields() 
    {
        return (fields != null) ? fields : null;
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
        if (date != null && ! date.isBlank())
            return date + time;
        return "";
    }

    @Override
    public boolean isEmpty(String fieldName) 
    {
        return this.getField(fieldName).isBlank();
    }

    @Override
    public String getStanding() 
    {
        return this.getField(SDapiUserFields.STANDING.toString());
    }

    @Override
    public boolean cardReportedLost() 
    {
//        return this.getCustomerProfile().equals("LOST") || this.getCustomerProfile().equals("LOSTCARD");
        String customerCardProfile = this.getCustomerProfile();
        List<String> lostTypes = new ArrayList<>();
        Properties props       = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        // Find the lostcard sentinal types.
        // read optional fields from environment. Should be ',' separated.
        // <entry key="lost-card-sentinel">LOST, LOSTCARD</entry>
        PropertyReader.loadDelimitedEntry(props, LibraryPropertyTypes.LOST_CARD_SENTINEL, lostTypes);

        // Non-residents
        for (String str: lostTypes)
        {
            if (customerCardProfile.equalsIgnoreCase(str)) // Test fails lost card.
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInGoodStanding() 
    {
        return "OK".equals(this.getStanding());
    }

    @Override
    public int getTotalResults() 
    {
        if (fields != null && ! fields.getBarcode().isBlank())
            return 1;
        return 0;
    }
    
    // Static method to parse JSON
    public static SDapiResponse parseJson(String jsonString) 
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiUserPatronBarcodeResponse.class);
    }

}
