/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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

package mecard.sirsidynix.sdapi;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import mecard.customer.MeCardDataToNativeData;
import mecard.config.SDapiUserFields;

/**
 *
 * @author anisbet
 */
public class MeCardDataToSDapiData implements MeCardDataToNativeData
{
    public enum QueryType
    {
        CREATE,
        UPDATE;
    }
    
    /**
     * Specifies the formatting {@link PAPIFormattedTable.ContentType} and defaults
     * to a create user query.
     * @param type
     * @return create query formatted as per argument.
     */
    public static MeCardDataToSDapiData getInstanceOf(QueryType type)
    {
        return new MeCardDataToSDapiData(type, false);
    }

    private MeCardDataToSDapiData(QueryType type, boolean debug) 
    {   }

    // {
    //    "resource": "/user/patron",
    //    "fields": {
    //        "barcode": "21221031494957",
    //        "lastName": "Norton",
    //        "firstName": "Andre",
    //        "library": {"resource":"/policy/library","key":"EPLMNA"},
    //        "profile": {"resource":"/policy/userProfile","key":"EPL_ADULT"},
    //        "address1": [{
    //                  "@resource": "/user/patron/address1",
    //                  "@key": "1",
    //                  "code": {
    //                      "@resource": "/policy/patronAddress1",
    //                      "@key": "CITY/STATE"
    //                  },
    //                  "data": "Edmonton, AB"
    //              },
    //              {
    //                  "@resource": "/user/patron/address1",
    //                  "@key": "5",
    //                  "code": {
    //                      "@resource": "/policy/patronAddress1",
    //                      "@key": "POSTALCODE"
    //                  },
    //                  "data": "T6G 0G4"
    //              },
    //              {
    //                  "@resource": "/user/patron/address1",
    //                  "@key": "2",
    //                  "code": {
    //                      "@resource": "/policy/patronAddress1",
    //                      "@key": "PHONE"
    //                  },
    //                  "data": "780-555-5555"
    //              },
    //              {
    //                  "@resource": "/user/patron/address1",
    //                  "@key": "6",
    //                  "code": {
    //                      "@resource": "/policy/patronAddress1",
    //                      "@key": "EMAIL"
    //                  },
    //                  "data": "anorton@hotmail.com"
    //              },
    //              {
    //                  "@resource": "/user/patron/address1",
    //                  "@key": "4",
    //                  "code": {
    //                      "@resource": "/policy/patronAddress1",
    //                      "@key": "STREET"
    //                  },
    //                  "data": "3532 20 Avenue NW"
    //              }
    //          ]
    //    }
    //}

    public String toJson() 
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
    /**
     * Creates matching.
     * @return properly formatted customer data string for the table this represents.
     */
    @Override
    public String getData() 
    {
        return this.toJson();
    }

    /**
     *
     * @return properly formatted customer header string for the table this represents.
     */
    @Override
    public String getHeader() 
    {   
        return "";
    }

    /**
     * 
     * @return name of the formatted table.
     */
    @Override
    public String getName() 
    {
        return MeCardDataToSDapiData.class.toString();
    }

    /**
     * Returns the value associated with this key.
     * @param key
     * @return the value associated with this key, or an empty string if the 
     * key is not present.
     */
    @Override
    public String getValue(String key) 
    {
        if (key.equals(SDapiUserFields.USER_KEY.toString()))
            return this.getUserKey() != null ? this.getUserKey() : "";
        else if (key.equals(SDapiUserFields.USER_FIRST_NAME.toString()))
            return this.getFirstName() != null ? this.getFirstName() : "";
        else if (key.equals(SDapiUserFields.USER_LAST_NAME.toString()))
            return this.getLastName() != null ? this.getLastName() : "";
        else if (key.equals(SDapiUserFields.USER_ID.toString()))
            return this.getUserId() != null ? this.getUserId() : "";
        else if (key.equals(SDapiUserFields.USER_LIBRARY.toString()))
            return this.getLibraryKey() != null ? this.getLibraryKey() : "";
        else if (key.equals(SDapiUserFields.USER_ALTERNATE_ID.toString()))
            return this.getAlternateId() != null ? this.getAlternateId() : "";
        else if (key.equals(SDapiUserFields.EMAIL.toString()))
            return this.getEmail() != null ? this.getEmail() : "";
        else if (key.equals(SDapiUserFields.PROFILE.toString()))
            return this.getProfileKey() != null ? this.getProfileKey() : "";
        else if (key.equals(SDapiUserFields.PHONE.toString()))
            return this.getPhone() != null ? this.getPhone() : "";
        else if (key.equals(SDapiUserFields.USER_BIRTHDATE.toString()))
            return this.getBirthDate() != null ? this.getBirthDate() : "";
        else if (key.equals(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
            return this.getExpiry() != null ? this.getExpiry() : "";
        else if (key.equals(SDapiUserFields.CITY_SLASH_PROV.toString()))
            return this.getProvince() != null ? this.getProvince() : "";
        else if (key.equals(SDapiUserFields.CITY_SLASH_STATE.toString()))
            return this.getCity() != null ? this.getCity() : "";
        else if (key.equals(SDapiUserFields.STREET.toString()))
            return this.getStreet() != null ? this.getStreet() : "";
        else if (key.equals(SDapiUserFields.POSTALCODE.toString()))
            return this.getPostalCode() != null ? this.getPostalCode() : "";
        else
        {
            System.out.println("*warning, request to get " + key + 
                " but MeCardDataToSDapiData does not support it.");
            return "";
        }
    }

    /**
     * Adds or changes a key in the table entry. If the key exists then the
     * value for that key is updated and the return result is true. If the key
     * didn't exist, the key value pair are added, but the return value is false.
     * The return value is false if either the key of value is null, and no
     * changes are made to the table.
     * @param key
     * @param value
     * @return true if a key was found and updated and false if the key or value
     * could not be added because they were null, or the key and value pair did
     * not exist when they were added.
     */
    @Override
    public boolean setValue(String key, String value) 
    {
        // this is only used on updates.
        if (key.equals(SDapiUserFields.USER_KEY.toString()))
            this.setUserKey(value);
        else if (key.equals(SDapiUserFields.USER_FIRST_NAME.toString()))
            this.setFirstName(value);
        else if (key.equals(SDapiUserFields.USER_LAST_NAME.toString()))
            this.setLastName(value);
        else if (key.equals(SDapiUserFields.USER_ID.toString()))
            this.setUserId(value);
        else if (key.equals(SDapiUserFields.USER_LIBRARY.toString()))
            this.setLibrary(value);
        else if (key.equals(SDapiUserFields.USER_ALTERNATE_ID.toString()))
            this.setAlternateId(value);
        else if (key.equals(SDapiUserFields.EMAIL.toString()))
            this.setEmail(value);
        else if (key.equals(SDapiUserFields.PROFILE.toString()))
            this.setProfile(value);
        else if (key.equals(SDapiUserFields.PHONE.toString()))
            this.setPhone(value);
        else if (key.equals(SDapiUserFields.USER_BIRTHDATE.toString()))
            this.setBirthDate(value);
        else if (key.equals(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
            this.setExpiry(value);
        else if (key.equals(SDapiUserFields.CITY_SLASH_PROV.toString()))
            this.setCityState(value);
        else if (key.equals(SDapiUserFields.CITY_SLASH_STATE.toString()))
            this.setCityState(value);
        else if (key.equals(SDapiUserFields.STREET.toString()))
            this.setStreet(value);
        else if (key.equals(SDapiUserFields.POSTALCODE.toString()))
            this.setPostalCode(value);
        else
        {
            System.out.println("*warning, request to set '" + key + "' with '"
                + value + "' but MeCardDataToSDapiData doesn't know what that is.");
            return false;
        }
        return true;
    }
    
    /**
     * Renames a key in the preserving the original stored value. It is not 
     * permissible to add the replacement key if the original key is not found.
     * @param originalKey the original key name
     * @param replacementKey the new name for the key
     * @return true if the key could be renamed and false if there was no 
     * key found matching originalKey name. A false leaves the table unaltered.
     */
    @Override
    public boolean renameKey(String originalKey, String replacementKey) 
    {
        String value = this.getValue(originalKey);
        // this is only used on updates.
        if (value != null)
        {
            if (this.setValue(replacementKey, value))
            {
                boolean result = this.deleteValue(originalKey);
                return result;
            }
            else
            {
                System.out.println("*warning, no such key " 
                        + originalKey + ". No changes made.");
                return false;
            }
        }
        return false;
    }

    /**
     * Returns all the column names from within the table.
     * @return 
     */
    @Override
    public Set<String> getKeys() 
    {
        List<String> columns = new ArrayList<>();
        if (this.getUserKey() != null)
            columns.add(SDapiUserFields.USER_KEY.toString());
        if (this.getFirstName() != null)
            columns.add(SDapiUserFields.USER_FIRST_NAME.toString());
        if (this.getLastName() != null)
            columns.add(SDapiUserFields.USER_LAST_NAME.toString());
        if (this.getUserId() != null)
            columns.add(SDapiUserFields.USER_ID.toString());
        if (this.getLibraryKey() != null)
            columns.add(SDapiUserFields.USER_LIBRARY.toString());
        if (this.getAlternateId() != null)
            columns.add(SDapiUserFields.USER_ALTERNATE_ID.toString());
        if (this.getEmail() != null)
            columns.add(SDapiUserFields.EMAIL.toString());
        if (this.getProfileKey() != null)
            columns.add(SDapiUserFields.PROFILE.toString());
        if (this.getPhone() != null)
            columns.add(SDapiUserFields.PHONE.toString());
        if (this.getBirthDate() != null)
            columns.add(SDapiUserFields.USER_BIRTHDATE.toString());
        if (this.getExpiry() != null)
            columns.add(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString());
        if (this.getCity() != null)
            columns.add(SDapiUserFields.CITY_SLASH_STATE.toString());
        if (this.getProvince() != null)
            columns.add(SDapiUserFields.CITY_SLASH_PROV.toString());
        if (this.getStreet() != null)
            columns.add(SDapiUserFields.STREET.toString());
        if (this.getPostalCode() != null)
            columns.add(SDapiUserFields.POSTALCODE.toString());
        @SuppressWarnings("unchecked")
        Set<String> s = new LinkedHashSet(columns);
        return s;
    }

    /**
     * Removes an entry from the user table. Specially used for libraries that 
     * don't use USER_PREFERED_NAME and the like. This method stops the key and 
     * value from being written in the table by removing them.
     * @param key
     * @return true if the key was found, and false otherwise. Any value stored 
     * at the key will be deleted from the table.
     */
    @Override
    public boolean deleteValue(String key) 
    {
        // this is only used on updates.
        if (key.equals(SDapiUserFields.USER_KEY.toString()))
            this.setUserKey(null);
        else if (key.equals(SDapiUserFields.USER_FIRST_NAME.toString()))
            this.setFirstName(null);
        else if (key.equals(SDapiUserFields.USER_LAST_NAME.toString()))
            this.setLastName(null);
        else if (key.equals(SDapiUserFields.USER_ID.toString()))
            this.setUserId(null);
        else if (key.equals(SDapiUserFields.USER_LIBRARY.toString()))
            this.setLibrary(null);
        else if (key.equals(SDapiUserFields.USER_ALTERNATE_ID.toString()))
            this.setAlternateId(null);
        else if (key.equals(SDapiUserFields.EMAIL.toString()))
            this.setEmail(null);
        else if (key.equals(SDapiUserFields.PROFILE.toString()))
            this.setProfile(null);
        else if (key.equals(SDapiUserFields.PHONE.toString()))
            this.setPhone(null);
        else if (key.equals(SDapiUserFields.USER_BIRTHDATE.toString()))
            this.setBirthDate(null);
        else if (key.equals(SDapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
            this.setExpiry(null);
        else if (key.equals(SDapiUserFields.CITY_SLASH_PROV.toString()))
            this.setCityState(null);
        else if (key.equals(SDapiUserFields.CITY_SLASH_STATE.toString()))
            this.setCityState(null);
        else if (key.equals(SDapiUserFields.STREET.toString()))
            this.setStreet(null);
        else if (key.equals(SDapiUserFields.POSTALCODE.toString()))
            this.setPostalCode(null);
        else
        {
            System.out.println("*warning, request to delete unsupported key: '" 
                + key + "'.");
            return false;
        }
        return true;
    }
    
    private void setUserKey(String userKey)
    {
        this.userKey = userKey;
    }
    
    private String getUserKey()
    {
        return this.userKey;
    }
    
    private void setAlternateId(String alternateId)
    {
        this.fields.altId = alternateId;
    }
    
    private void setBirthDate(String dob)
    {
        this.fields.birthDate = dob;
    }
    
    private void setExpiry(String expiry)
    {
        this.fields.expiry = expiry;
    }
    
    private void setLibrary(String branch)
    {
        this.fields.library = new PolicyResource("/policy/library", branch);
    }
    
    private void setProfile(String profile)
    {
        this.fields.profile = new PolicyResource("/policy/userProfile", profile);
    }
    
    private void setUserId(String barcode)
    {
        this.fields.barcode = barcode;
    }
    
    private void setFirstName(String firstName)
    {
        this.fields.firstName = firstName;
    }
    
    private void setLastName(String lastName)
    {
        this.fields.lastName = lastName;
    }
    
    private String getAlternateId()
    {
        return this.fields.altId;
    }

    private String getBirthDate()
    {
        return this.fields.birthDate;
    }

    private String getExpiry()
    {
        return this.fields.expiry;
    }

    private String getUserId()
    {
        return this.fields.barcode;
    }

    private String getFirstName()
    {
        return this.fields.firstName;
    }

    private String getLastName()
    {
        return this.fields.lastName;
    }
    
    private void setCityState(String cityState)
    {
        if (this.fields.address1 == null)
            this.fields.address1 = new ArrayList<>();
        // Add city/state
        this.fields.address1.add(new Address(
            "/user/patron/address1",
            "1",
            new AddressCode("/policy/patronAddress1", "CITY/STATE"),
            cityState
        ));
    }
    
    private void setPostalCode(String postalCode)
    {
        if (this.fields.address1 == null)
            this.fields.address1 = new ArrayList<>();
        // Add postal code
        this.fields.address1.add(new Address(
            "/user/patron/address1",
            "5",
            new AddressCode("/policy/patronAddress1", "POSTALCODE"),
            postalCode
        ));
    }
    
    private void setPhone(String phone)
    {
        if (this.fields.address1 == null)
            this.fields.address1 = new ArrayList<>();
        // Add phone
        this.fields.address1.add(new Address(
            "/user/patron/address1",
            "2",
            new AddressCode("/policy/patronAddress1", "PHONE"),
            phone
        ));
    }
    
    // Add email
    private void setEmail(String email)
    {
        if (this.fields.address1 == null)
            this.fields.address1 = new ArrayList<>();
        this.fields.address1.add(new Address(
            "/user/patron/address1",
            "6",
            new AddressCode("/policy/patronAddress1", "EMAIL"),
            email
        ));
    }
    
    private void setStreet(String street)
    {
        if (this.fields.address1 == null)
            this.fields.address1 = new ArrayList<>();
        this.fields.address1.add(new Address(
            "/user/patron/address1",
            "4",
            new AddressCode("/policy/patronAddress1", "STREET"),
            street
        ));
    }
    
    private String getLibraryKey() { return this.fields.library != null ? this.fields.library.getKey() : null; }
    private String getProfileKey() { return this.fields.profile != null ? this.fields.profile.getKey() : null; }

    // Method to find specific address by code
    private String findAddressByCode(String addressCode) 
    {
        if (this.fields.address1 != null) {
            for (Address entry : this.fields.address1) 
            {
                if (addressCode.equals(entry.code.getKey())) 
                {
                    return entry.getData();
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
//    private String getCitySlashState() { return findAddressByCode(SDapiUserFields.CITY_SLASH_STATE.toString()); }
    private String getCity() 
    {
        try
        {
            String[] words = 
                    findAddressByCode(SDapiUserFields.CITY_SLASH_STATE.toString())
                            .replaceAll("[^a-zA-Z ]", "").split("\\s+");
            return words[0];
        }
        catch (IndexOutOfBoundsException | NullPointerException e)
        {
            return null;
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
        catch (IndexOutOfBoundsException | NullPointerException e)
        {
            return null;
        }
    }
    
    @SerializedName("resource")
    private final String resource = "/user/patron";
    
    // Used when data type is UPDATE.
    @SerializedName("key")
    private String userKey;

    @SerializedName("fields")
    private final Fields fields = new Fields();

    private static class Fields 
    {
        @SerializedName("barcode")
        private String barcode;

        @SerializedName("lastName")
        private String lastName;

        @SerializedName("firstName")
        private String firstName;
        
        @SerializedName("privilegeExpiresDate")
        private String expiry;
        
        @SerializedName("birthDate")
        private String birthDate;
        
        @SerializedName("alternateID")
        private String altId;

        @SerializedName("library")
        private PolicyResource library;

        @SerializedName("profile")
        private PolicyResource profile;

        @SerializedName("address1")
        private List<Address> address1;
    }

    private static class PolicyResource 
    {
        @SerializedName("resource")
        private final String resource;

        @SerializedName("key")
        private final String key;
        
        private String getKey()
        {
            return this.key;
        }

        private PolicyResource(String resource, String key) 
        {
            this.resource = resource;
            this.key = key;
        }
    }

    private static class Address 
    {
        @SerializedName("@resource")
        private final String resource;

        @SerializedName("@key")
        private final String key;

        @SerializedName("code")
        private AddressCode code;

        @SerializedName("data")
        private final String data;
        
        private String getData()
        {
            return this.data;
        }

        private Address(String resource, String key, AddressCode code, String data) 
        {
            this.resource = resource;
            this.key = key;
            this.code = code;
            this.data = data;
        }
    }

    private static class AddressCode 
    {
        @SerializedName("@resource")
        private final String resource;

        @SerializedName("@key")
        private final String key;
        
        private String getKey()
        {
            return this.key;
        }

        private AddressCode(String resource, String key) 
        {
            this.resource = resource;
            this.key = key;
        }
    }
    
    @Override
    public String toString()
    {
        return this.toJson();
    }
}
