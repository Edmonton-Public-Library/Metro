/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025 Edmonton Public Library
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

package mecard.calgary.cplapi;

import api.CustomerMessage;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import mecard.config.CPLapiUserFields;

public class CPLapiGetCustomerResponse 
        extends CPLapiResponse
        implements CustomerMessage
{

    @Override
    public boolean succeeded() 
    {
        return ! this.cardNumber.isBlank();
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
        return this.getProfile();
    }

    @Override
    public String getField(String fieldName) 
    {
        try
        {
            if (fieldName.equals(CPLapiUserFields.USER_FIRST_NAME.toString()))
                return this.getFirstName();
            if (fieldName.equals(CPLapiUserFields.USER_LAST_NAME.toString()))
                return this.getLastName();
            if (fieldName.equals(CPLapiUserFields.USER_ID.toString()))
                return this.getBarcode();
            if (fieldName.equals(CPLapiUserFields.USER_PASSWORD.toString()))
                return this.getPassword();
            if (fieldName.equals(CPLapiUserFields.EMAIL.toString()))
                return this.getEmail();
            if (fieldName.equals(CPLapiUserFields.PROFILE.toString()))
                return this.getProfile();
            if (fieldName.equals(CPLapiUserFields.STATUS.toString()))
                return this.getStatus();
            if (fieldName.equals(CPLapiUserFields.PHONE.toString()))
                return this.getPhone();
            if (fieldName.equals(CPLapiUserFields.USER_BIRTHDATE.toString()))
                return this.getDateField(fieldName);
            if (fieldName.equals(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
                return this.getDateField(fieldName);
            if (fieldName.equals(CPLapiUserFields.CITY.toString()))
                return this.getCity();
            if (fieldName.equals(CPLapiUserFields.STREET.toString()))
                return this.getStreet();
            if (fieldName.equals(CPLapiUserFields.POSTALCODE.toString()))
                return this.getPostalCode();
            if (fieldName.equals(CPLapiUserFields.PROVINCE.toString()))
                return this.getProvince();
            if (fieldName.equals(CPLapiUserFields.GENDER.toString()))
                return this.getGender();
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
        String date = "";
        try
        {
            // Returns any USER dates converted to ANSI.
            if (fieldName.equals(CPLapiUserFields.USER_BIRTHDATE.toString()))
                date = this.getBirthDate();
            if (fieldName.equals(CPLapiUserFields.PRIVILEGE_EXPIRES_DATE.toString()))
                date = this.getExpiry();
        }
        catch (NullPointerException ne)
        {
            return "";
        }
        if (date != null && ! date.isBlank())
            return date;
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
        return this.getStatus();
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
        return "OK".equals(this.getStatus());
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
    private void setProfile(String str) { profile = str; }
    private void setEmail(String str) { this.emailAddress = str; }
    private void setPostalCode(String str) { this.postalCode = str; }
    private void setPhone(String str) { this.phoneNumber = str; }
    private void setStreet(String str) { this.address = str; }
    private void setCity(String str) { this.city = str; }
    private void setProvince(String str) { this.province = str; }
    private void setGender(String str) { this.gender = str; }
    private void setStatus(String str) { this.status = str; }

    // Static method to parse JSON
    public static CPLapiResponse parseJson(String jsonString) {
        Gson gson = new Gson();
        CPLapiResponse errors = gson.fromJson(jsonString, CPLapiErrorResponse.class);
        if (errors.succeeded())
            return gson.fromJson(jsonString, CPLapiGetCustomerResponse.class);
        else
            return errors;
    }
    
    @Override
    public String toString() {
        String p = this.getPassword();
        if (! p.isBlank())
            p = "******";
        return "{" +
                "cardNumber='" + this.getBarcode() + '\'' +
                ", pin='" + p + '\'' +
                ", firstName='" + this.getField("firstName") + '\'' +
                ", lastName='" + this.getField("lastName") + '\'' +
                ", birthDate='" + this.getField("birthDate") + '\'' +
                ", gender='" + this.getField("gender") + '\'' +
                ", emailAddress='" + this.getField("emailAddress") + '\'' +
                ", phoneNumber='" + this.getField("phoneNumber") + '\'' +
                ", address='" + this.getField("address") + '\'' +
                ", city='" + this.getField("city") + '\'' +
                ", province='" + this.getField("province") + '\'' +
                ", postalCode='" + this.getField("postalCode") + '\'' +
                ", expiryDate='" + this.getField("expiryDate") + '\'' +
                ", profile='" + this.getField("profile") + '\'' +
                ", status='" + this.getField("status") + '\'' +
                '}';
    }
}