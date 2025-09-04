/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025 Edmonton Public Library
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
package mecard.calgary.cplapi;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import mecard.config.CPLapiUserFields;
import mecard.customer.MeCardDataToNativeData;

/**
 * Storage container that provides a standard interface to use when converting
 * MeCard customer data into a format that can be used by the target ILS, or 
 * web service.
 * 
 * @author Andrew Nisbet andrew at dev-ils.com
 */
public class MeCardDataToCPLapiData implements MeCardDataToNativeData
{

    public enum QueryType
    {
        CREATE,
        UPDATE;
    }
    
    static MeCardDataToCPLapiData getInstanceOf(QueryType type) 
    {
        return new MeCardDataToCPLapiData(type, false);
    }
    
    private MeCardDataToCPLapiData(QueryType type, boolean debug)
    {   }
    
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
        return MeCardDataToCPLapiData.class.toString();
    }

    /**
     * Returns the CPL API data value for a given key ME Card data key. 
     * For example you can request .
     * @param key
     * @return the originalValue associated with this key, or an empty 
     * string if the key is not present.
     */
    @Override
    public String getValue(String key) 
    {        
        if (key.equals(CPLapiUserFields.USER_ID.toString()))
            return this.getBarcode() != null ? this.getBarcode() : "";
        else if (key.equals(CPLapiUserFields.USER_FIRST_NAME.toString()))
            return this.getFirstName() != null ? this.getFirstName() : "";
        else if (key.equals(CPLapiUserFields.USER_LAST_NAME.toString()))
            return this.getLastName() != null ? this.getLastName() : "";
        else if (key.equals(CPLapiUserFields.USER_ID.toString()))
            return this.getBarcode() != null ? this.getBarcode() : "";
        else if (key.equals(CPLapiUserFields.EMAIL.toString()))
            return this.getEmail() != null ? this.getEmail() : "";
        else if (key.equals(CPLapiUserFields.PROFILE.toString()))
            return this.getProfile() != null ? this.getProfile() : "";
        else if (key.equals(CPLapiUserFields.PHONE.toString()))
            return this.getPhone() != null ? this.getPhone() : "";
        else if (key.equals(CPLapiUserFields.USER_BIRTHDATE.toString()))
            return this.getBirthDate() != null ? this.getBirthDate() : "";
        else if (key.equals(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
            return this.getExpiry() != null ? this.getExpiry() : "";
        else if (key.equals(CPLapiUserFields.CITY.toString()))
            return this.getCity() != null ? this.getCity() : "";
        else if (key.equals(CPLapiUserFields.PROVINCE.toString()))
            return this.getProvince() != null ? this.getProvince() : "";
        else if (key.equals(CPLapiUserFields.STREET.toString()))
            return this.getStreet() != null ? this.getStreet() : "";
        else if (key.equals(CPLapiUserFields.POSTALCODE.toString()))
            return this.getPostalCode() != null ? this.getPostalCode() : "";
        else if (key.equals(CPLapiUserFields.USER_PASSWORD.toString()))
            return this.getPassword() != null ? this.getPassword() : "";
        else if (key.equals(CPLapiUserFields.GENDER.toString()))
            return this.getGender() != null ? this.getGender() : "";
        else if (key.equals(CPLapiUserFields.STATUS.toString()))
            return this.getStatus() != null ? this.getStatus() : "";
        else
        {
            System.out.println("*warning, request to get '" + key 
                + "' but MeCardDataToCPLapiData doesn't know what that is.");
            return "";
        }    
    }

    @Override
    public boolean setValue(String key, String value) 
    {
        // this is only used on updates.
        if (key.equals(CPLapiUserFields.USER_FIRST_NAME.toString()))
            this.setFirstName(value);
        else if (key.equals(CPLapiUserFields.USER_LAST_NAME.toString()))
            this.setLastName(value);
        else if (key.equals(CPLapiUserFields.USER_ID.toString()))
            this.setBarcode(value);
        else if (key.equals(CPLapiUserFields.EMAIL.toString()))
            this.setEmail(value);
        else if (key.equals(CPLapiUserFields.PROFILE.toString()))
            this.setProfile(value);
        else if (key.equals(CPLapiUserFields.PHONE.toString()))
            this.setPhone(value);
        else if (key.equals(CPLapiUserFields.USER_BIRTHDATE.toString()))
            this.setBirthDate(value);
        else if (key.equals(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
            this.setExpiry(value);
        else if (key.equals(CPLapiUserFields.CITY.toString()))
            this.setCity(value);
        else if (key.equals(CPLapiUserFields.PROVINCE.toString()))
            this.setProvince(value);
        else if (key.equals(CPLapiUserFields.STREET.toString()))
            this.setStreet(value);
        else if (key.equals(CPLapiUserFields.POSTALCODE.toString()))
            this.setPostalCode(value);
        else if (key.equals(CPLapiUserFields.USER_PASSWORD.toString()))
            this.setPassword(value);
        else if (key.equals(CPLapiUserFields.GENDER.toString()))
            this.setGender(value);
        else if (key.equals(CPLapiUserFields.STATUS.toString()))
            this.setStatus(value);
        else
        {
            System.out.println("*warning, request to set '" + key + "' with '"
                + value + "' but MeCardDataToCPLapiData doesn't know what that is.");
            return false;
        }
        
        return true;
    }

    /**
     * Gives an old key a new name. If the old key doesn't exist no change is made.
     * @param oldKeyName the original key name.
     * @param newKeyName the new name for the key
     * @return true if the key was renamed and false if there was no 
     * original key.
     */
    @Override
    public boolean renameKey(String oldKeyName, String newKeyName) 
    {
        String originalValue = this.getValue(oldKeyName);
        // this is only used on updates.
        if (originalValue != null)
        {
            if (this.setValue(newKeyName, originalValue))
            {
                return this.deleteValue(oldKeyName);
            }
            else
            {
                System.out.println("*warning, no such key " 
                        + oldKeyName + ". No changes made.");
                return false;
            }
        }
        return false;
    }
    
    @SerializedName("cardNumber")
    private String cardNumber;
    @SerializedName("pin")
    private String pin;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("birthDate")
    private String birthDate;
    @SerializedName("gender")
    private String gender;
    @SerializedName("emailAddress")
    private String emailAddress;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("address")
    private String address;
    @SerializedName("city")
    private String city;
    @SerializedName("province")
    private String province;
    @SerializedName("postalCode")
    private String postalCode;
    @SerializedName("expiryDate")
    private String expiryDate;
    @SerializedName("status")
    private String status;
    @SerializedName("profile")
    private String profile;

    // Getters for all fields
    private String getBarcode() { return this.cardNumber != null ? this.cardNumber : ""; }
    private String getPassword() { return this.pin != null ? this.pin : ""; }
    private String getBirthDate() { return this.birthDate != null ? this.birthDate : ""; }
    private String getFirstName() { return this.firstName != null ? this.firstName : ""; }
    private String getLastName() { return this.lastName != null ? this.lastName : ""; }
    private String getExpiry() { return this.expiryDate != null ? this.expiryDate : ""; }
    private String getProfile() { return profile != null ? profile : ""; }
    private String getEmail() { return this.emailAddress != null ? this.emailAddress : ""; }
    private String getPostalCode() { return this.postalCode != null ? this.postalCode : ""; }
    private String getPhone() { return this.phoneNumber != null ? this.phoneNumber : ""; }
    private String getStreet() { return this.address != null ? this.address : ""; }
    private String getCity() { return this.city != null ? this.city : ""; }
    private String getProvince() { return this.province != null ? this.province : ""; }
    private String getGender() { return this.gender != null ? this.gender : ""; }
    private String getStatus() { return this.status != null ? this.status : ""; }
    
    // Setters for all methods.
    private void setBarcode(String str) { this.cardNumber = str; }
    private void setPassword(String str) { this.pin = str; }
    private void setBirthDate(String str) { this.birthDate = str; }
    private void setFirstName(String str) { this.firstName = str; }
    private void setLastName(String str) { this.lastName = str; }
    private void setExpiry(String str) { this.expiryDate = str; }
    private void setProfile(String str) { this.profile = str; }
    private void setEmail(String str) { this.emailAddress = str; }
    private void setPostalCode(String str) { this.postalCode = str; }
    private void setPhone(String str) { this.phoneNumber = str; }
    private void setStreet(String str) { this.address = str; }
    private void setCity(String str) { this.city = str; }
    private void setProvince(String str) { this.province = str; }
    private void setGender(String str) { this.gender = str; }
    private void setStatus(String str) { this.status = str; }

    /**
     * Provides a of all of the customer's data values as understood from
     * Calgary Public Library's API perspective.
     * in a generalized way.
     * @return 
     */
    @Override
    public Set<String> getKeys() 
    {
        List<String> columns = new ArrayList<>();
        for (CPLapiUserFields field : CPLapiUserFields.values()) 
        {
            // only include fields that have values.
            if (! this.getValue(field.toString()).isBlank())
            {
                columns.add(field.toString());
            }
        }
        @SuppressWarnings("unchecked")
        Set<String> s = new LinkedHashSet(columns);
        return s;
    }

    @Override
    public boolean deleteValue(String key) 
    {
        for (CPLapiUserFields field : CPLapiUserFields.values()) 
        {
            if (field.toString().equals(key)) 
            {
                this.setValue(key, null);
                return true;
            }
        }
 
        System.out.println("*warning, request to delete '" + key
            + "' but MeCardDataToCPLapiData doesn't know what that is.");
        return false;
    }

    
    @Override
    public String toString() 
    {
        return this.toJson();
    }
    
}
