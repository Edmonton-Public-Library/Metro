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

/**
 *
 * @author anisbet
 */
public class SDapiJsonCustomerResponse 
    extends SDapiJsonResponse
    implements CustomerMessage
{
    //    {
    //   "searchQuery": "ID:21221012345678",
    //   "startRow": 1,
    //   "lastRow": 1,
    //   "rowsPerPage": 10,
    //   "totalResults": 1,
    //   "result": [
    //       {
    //           "resource": "/user/patron",
    //           "key": "301585",
    //           "fields": {
    //               "displayName": "BILLY, Balzac",
    //               "access": {
    //                   "resource": "/policy/userAccess",
    //                   "key": "PUBLIC"
    //               },
    //               "alternateID": "",
    //               "barcode": "21221012345678",
    //               "birthDate": "2000-02-29",
    //               "checkoutLocation": {
    //                   "resource": "/policy/location",
    //                   "key": "CHECKEDOUT"
    //               },
    //               "createdDate": "2003-11-24",
    //               "department": "PROD",
    //               "environment": {
    //                   "resource": "/policy/environment",
    //                   "key": "PUBLIC"
    //               },
    //               "firstName": "Balzac",
    //               "language": {
    //                   "resource": "/policy/language",
    //                   "key": "ENGLISH"
    //               },
    //               "lastName": "BILLY",
    //               "library": {
    //                   "resource": "/policy/library",
    //                   "key": "EPLMNA"
    //               },
    //               "middleName": "",
    //               "preferredName": "",
    //               "privilegeExpiresDate": null,
    //               "profile": {
    //                   "resource": "/policy/userProfile",
    //                   "key": "EPL_ADULT"
    //               },
    //               "suffix": "",
    //               "title": "",
    //               "usePreferredName": false,
    //               "webAuthID": "",
    //               "category01": null,
    //               "category02": {
    //                   "resource": "/policy/patronCategory02",
    //                   "key": "F"
    //               },
    //               "category03": null,
    //               "category04": {
    //                   "resource": "/policy/patronCategory04",
    //                   "key": "NNELS"
    //               },
    //               "category05": {
    //                   "resource": "/policy/patronCategory05",
    //                   "key": "ECONSENT"
    //               },
    //               "category06": null,
    //               "category07": null,
    //               "category08": null,
    //               "category09": null,
    //               "category10": null,
    //               "category11": null,
    //               "category12": null,
    //               "claimsReturnedCount": 1,
    //               "standing": {
    //                   "resource": "/policy/patronStanding",
    //                   "key": "OK"
    //               },
    //               "groupId": "BALZAC",
    //               "address1": [
    //                   {
    //                       "resource": "/user/patron/address1",
    //                       "key": "22778",
    //                       "fields": {
    //                           "code": {
    //                               "resource": "/policy/patronAddress1",
    //                               "key": "ZIP"
    //                           },
    //                           "data": "T5J-2V4"
    //                       }
    //                   },
    //                   {
    //                       "resource": "/user/patron/address1",
    //                       "key": "87",
    //                       "fields": {
    //                           "code": {
    //                               "resource": "/policy/patronAddress1",
    //                               "key": "EMAIL"
    //                           },
    //                           "data": "ilsadmins@epl.ca"
    //                       }
    //                   },
    //                   {
    //                       "resource": "/user/patron/address1",
    //                       "key": "88",
    //                       "fields": {
    //                           "code": {
    //                               "resource": "/policy/patronAddress1",
    //                               "key": "POSTALCODE"
    //                           },
    //                           "data": "T5J-2V4"
    //                       }
    //                   },
    //                   {
    //                       "resource": "/user/patron/address1",
    //                       "key": "89",
    //                       "fields": {
    //                           "code": {
    //                               "resource": "/policy/patronAddress1",
    //                               "key": "PHONE"
    //                           },
    //                           "data": "780-496-4058"
    //                       }
    //                   },
    //                   {
    //                       "resource": "/user/patron/address1",
    //                       "key": "22779",
    //                       "fields": {
    //                           "code": {
    //                               "resource": "/policy/patronAddress1",
    //                               "key": "CARE/OF"
    //                           },
    //                           "data": null
    //                       }
    //                   },
    //                   {
    //                       "resource": "/user/patron/address1",
    //                       "key": "90",
    //                       "fields": {
    //                           "code": {
    //                               "resource": "/policy/patronAddress1",
    //                               "key": "STREET"
    //                           },
    //                           "data": "7 Sir Winston Churchill Sq."
    //                       }
    //                   },
    //                   {
    //                       "resource": "/user/patron/address1",
    //                       "key": "91",
    //                       "fields": {
    //                           "code": {
    //                               "resource": "/policy/patronAddress1",
    //                               "key": "CITY/STATE"
    //                           },
    //                           "data": "Edmonton, Alberta"
    //                       }
    //                   }
    //               ],
    //               "circRecordCount": 0,
    //               "keepCircHistory": "ALLCHARGES"
    //           }
    //       }
    //   ]
    //}
    
//    @SerializedName("searchQuery")
//    private String searchQuery;

    @SerializedName("result")
    private List<CustomerResult> result;

    // Inner class to represent the result
    public static class CustomerResult {
        @SerializedName("resource")
        private String resource;

        @SerializedName("key")
        private String key;

        @SerializedName("fields")
        private CustomerFields fields;
    }
    
     // Inner class to represent all fields
    public static class CustomerFields {
        @SerializedName("displayName")
        private String displayName;

        @SerializedName("barcode")
        private String barcode;

        @SerializedName("birthDate")
        private String birthDate;

        @SerializedName("createdDate")
        private String createdDate;

        @SerializedName("department")
        private String department;

        @SerializedName("firstName")
        private String firstName;

        @SerializedName("lastName")
        private String lastName;

        @SerializedName("middleName")
        private String middleName;

        @SerializedName("preferredName")
        private String preferredName;

        @SerializedName("webAuthID")
        private String webAuthID;

        @SerializedName("groupId")
        private String groupId;

        @SerializedName("circRecordCount")
        private Integer circRecordCount;

        // Nested object getters
        @SerializedName("access")
        private PolicyResource access;

        @SerializedName("checkoutLocation")
        private PolicyResource checkoutLocation;

        @SerializedName("environment")
        private PolicyResource environment;

        @SerializedName("language")
        private PolicyResource language;

        @SerializedName("library")
        private PolicyResource library;

        @SerializedName("profile")
        private PolicyResource profile;

        @SerializedName("standing")
        private PolicyResource standing;

        @SerializedName("category02")
        private PolicyResource category02;

        @SerializedName("category04")
        private PolicyResource category04;

        @SerializedName("category05")
        private PolicyResource category05;

        @SerializedName("address1")
        private List<AddressEntry> address1;

        // Nested static class for policy resources
        public static class PolicyResource {
            @SerializedName("resource")
            private String resource;

            @SerializedName("key")
            private String key;

            public String getResource() { return resource; }
            public String getKey() { return key; }
        }

        // Nested static class for address entries
        public static class AddressEntry {
            @SerializedName("fields")
            private AddressFields fields;

            public static class AddressFields {
                @SerializedName("code")
                private PolicyResource code;

                @SerializedName("data")
                private String data;

                public String getCode() { return code != null ? code.getKey() : null; }
                public String getData() { return data; }
            }

            public AddressFields getFields() { return fields; }
        }
         // Getters for all fields
        public String getDisplayName() { return displayName; }
        public String getBarcode() { return barcode; }
        public String getBirthDate() { return birthDate; }
        public String getCreatedDate() { return createdDate; }
        public String getDepartment() { return department; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getMiddleName() { return middleName; }
        public String getPreferredName() { return preferredName; }
        public String getWebAuthID() { return webAuthID; }
        public String getGroupId() { return groupId; }
        public Integer getCircRecordCount() { return circRecordCount; }

        // Convenience methods for nested resources
        public String getAccessKey() { return access != null ? access.getKey() : null; }
        public String getLibraryKey() { return library != null ? library.getKey() : null; }
        public String getProfileKey() { return profile != null ? profile.getKey() : null; }

        // Method to find specific address by code
        public String findAddressByCode(String addressCode) {
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
        public String getEmail() { return findAddressByCode("EMAIL"); }
        public String getPostalCode() { return findAddressByCode("POSTALCODE"); }
        public String getPhone() { return findAddressByCode("PHONE"); }
        public String getStreet() { return findAddressByCode("STREET"); }
        public String getCity() { return findAddressByCode("CITY/STATE"); }
    }

    @Override
    public String getCustomerProfile() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getField(String fieldName) 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getDateField(String fieldName) 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isEmpty(String fieldName) 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getStanding() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean cardReportedLost() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isInGoodStanding() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean isValidPatron() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public boolean succeeded() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static SDapiJsonResponse parseJson(String jsonString) 
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiJsonCustomerResponse.class);
    }

    @Override
    public String errorMessage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
