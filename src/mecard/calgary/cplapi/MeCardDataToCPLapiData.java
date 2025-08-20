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

import java.util.Set;
import mecard.customer.MeCardDataToNativeData;

/**
 *
 * @author anisbet
 */
public class MeCardDataToCPLapiData implements MeCardDataToNativeData
{

    public enum QueryType
    {
        CREATE,
        UPDATE;
    }
    
    static MeCardDataToNativeData getInstanceOf(QueryType type) 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    

    @Override
    public String getData() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getHeader() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getValue(String key) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean setValue(String key, String value) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean renameKey(String originalkey, String replacementKey) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Set<String> getKeys() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteValue(String key) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
//        import com.google.gson.Gson;
//    import com.google.gson.annotations.SerializedName;
//    import java.time.LocalDate;
//    import java.time.format.DateTimeFormatter;
//
//    public class MeCardCustomer {
//
//        @SerializedName("cardNumber")
//        private String cardNumber;
//
//        @SerializedName("pin")
//        private String pin;
//
//        @SerializedName("firstName")
//        private String firstName;
//
//        @SerializedName("lastName")
//        private String lastName;
//
//        @SerializedName("birthDate")
//        private String birthDate; // Format: YYYY-MM-DD
//
//        @SerializedName("gender")
//        private String gender;
//
//        @SerializedName("emailAddress")
//        private String emailAddress;
//
//        @SerializedName("phoneNumber")
//        private String phoneNumber;
//
//        @SerializedName("address")
//        private String address;
//
//        @SerializedName("city")
//        private String city;
//
//        @SerializedName("province")
//        private String province;
//
//        @SerializedName("postalCode")
//        private String postalCode;
//
//        @SerializedName("expiryDate")
//        private String expiryDate; // Format: YYYY-MM-DD
//
//        // Default constructor
//        public MeCardCustomer() {}
//
//        // Constructor with all fields
//        public MeCardCustomer(String cardNumber, String pin, String firstName, String lastName,
//                             String birthDate, String gender, String emailAddress, String phoneNumber,
//                             String address, String city, String province, String postalCode,
//                             String expiryDate) {
//            this.cardNumber = cardNumber;
//            this.pin = pin;
//            this.firstName = firstName;
//            this.lastName = lastName;
//            this.birthDate = birthDate;
//            this.gender = gender;
//            this.emailAddress = emailAddress;
//            this.phoneNumber = phoneNumber;
//            this.address = address;
//            this.city = city;
//            this.province = province;
//            this.postalCode = postalCode;
//            this.expiryDate = expiryDate;
//        }
//
//        // Getters and Setters
//        public String getCardNumber() {
//            return cardNumber;
//        }
//
//        public void setCardNumber(String cardNumber) {
//            this.cardNumber = cardNumber;
//        }
//
//        public String getPin() {
//            return pin;
//        }
//
//        public void setPin(String pin) {
//            this.pin = pin;
//        }
//
//        public String getFirstName() {
//            return firstName;
//        }
//
//        public void setFirstName(String firstName) {
//            this.firstName = firstName;
//        }
//
//        public String getLastName() {
//            return lastName;
//        }
//
//        public void setLastName(String lastName) {
//            this.lastName = lastName;
//        }
//
//        public String getBirthDate() {
//            return birthDate;
//        }
//
//        public void setBirthDate(String birthDate) {
//            this.birthDate = birthDate;
//        }
//
//        // Helper method to set birth date from LocalDate
//        public void setBirthDate(LocalDate birthDate) {
//            this.birthDate = birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
//        }
//
//        // Helper method to get birth date as LocalDate
//        public LocalDate getBirthDateAsLocalDate() {
//            return birthDate != null ? LocalDate.parse(birthDate) : null;
//        }
//
//        public String getGender() {
//            return gender;
//        }
//
//        public void setGender(String gender) {
//            this.gender = gender;
//        }
//
//        public String getEmailAddress() {
//            return emailAddress;
//        }
//
//        public void setEmailAddress(String emailAddress) {
//            this.emailAddress = emailAddress;
//        }
//
//        public String getPhoneNumber() {
//            return phoneNumber;
//        }
//
//        public void setPhoneNumber(String phoneNumber) {
//            this.phoneNumber = phoneNumber;
//        }
//
//        public String getAddress() {
//            return address;
//        }
//
//        public void setAddress(String address) {
//            this.address = address;
//        }
//
//        public String getCity() {
//            return city;
//        }
//
//        public void setCity(String city) {
//            this.city = city;
//        }
//
//        public String getProvince() {
//            return province;
//        }
//
//        public void setProvince(String province) {
//            this.province = province;
//        }
//
//        public String getPostalCode() {
//            return postalCode;
//        }
//
//        public void setPostalCode(String postalCode) {
//            this.postalCode = postalCode;
//        }
//
//        public String getExpiryDate() {
//            return expiryDate;
//        }
//
//        public void setExpiryDate(String expiryDate) {
//            this.expiryDate = expiryDate;
//        }
//
//        // Helper method to set expiry date from LocalDate
//        public void setExpiryDate(LocalDate expiryDate) {
//            this.expiryDate = expiryDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
//        }
//
//        // Helper method to get expiry date as LocalDate
//        public LocalDate getExpiryDateAsLocalDate() {
//            return expiryDate != null ? LocalDate.parse(expiryDate) : null;
//        }
//
//        // Convert to JSON string
//        public String toJson() {
//            Gson gson = new Gson();
//            return gson.toJson(this);
//        }
//
//        // Create from JSON string
//        public static MeCardCustomer fromJson(String json) {
//            Gson gson = new Gson();
//            return gson.fromJson(json, MeCardCustomer.class);
//        }
//
//        @Override
//        public String toString() {
//            return "MeCardCustomer{" +
//                    "cardNumber='" + cardNumber + '\'' +
//                    ", firstName='" + firstName + '\'' +
//                    ", lastName='" + lastName + '\'' +
//                    ", birthDate='" + birthDate + '\'' +
//                    ", gender='" + gender + '\'' +
//                    ", emailAddress='" + emailAddress + '\'' +
//                    ", phoneNumber='" + phoneNumber + '\'' +
//                    ", address='" + address + '\'' +
//                    ", city='" + city + '\'' +
//                    ", province='" + province + '\'' +
//                    ", postalCode='" + postalCode + '\'' +
//                    ", expiryDate='" + expiryDate + '\'' +
//                    '}';
//        }
//
//        // Example usage
//        public static void main(String[] args) {
//            // Create a new customer
//            MeCardCustomer customer = new MeCardCustomer();
//            customer.setCardNumber("1234-5678-9012-3456");
//            customer.setPin("1234");
//            customer.setFirstName("John");
//            customer.setLastName("Doe");
//            customer.setBirthDate("1990-05-15");
//            customer.setGender("Male");
//            customer.setEmailAddress("john.doe@example.com");
//            customer.setPhoneNumber("555-123-4567");
//            customer.setAddress("123 Main Street");
//            customer.setCity("Toronto");
//            customer.setProvince("Ontario");
//            customer.setPostalCode("M5V 3A8");
//            customer.setExpiryDate("2025-12-31");
//
//            // Convert to JSON
//            String json = customer.toJson();
//            System.out.println("Customer as JSON:");
//            System.out.println(json);
//
//            // Convert from JSON
//            MeCardCustomer customerFromJson = MeCardCustomer.fromJson(json);
//            System.out.println("\nCustomer from JSON:");
//            System.out.println(customerFromJson);
//        }
//    }
    
}
