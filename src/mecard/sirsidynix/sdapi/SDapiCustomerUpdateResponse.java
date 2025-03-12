/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025  Edmonton Public Library
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

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

/**
 * An update customer response object.
 * @author Andrew Nisbet andrew (@) dev-ils.com
 */
/**
 *
 * @author anisbet
 */
public class SDapiCustomerUpdateResponse extends SDapiResponse
{
    /*
    {
        "resource": "/user/patron",
        "key": "2139681",
        "fields": {
            "displayName": "Norton, Andre",
            "access": {
                "resource": "/policy/userAccess",
                "key": "PUBLIC"
            },
            "alternateID": "",
            "barcode": "21221031494957",
            "birthDate": null,
            "checkoutLocation": {
                "resource": "/policy/location",
                "key": "CHECKEDOUT"
            },
            "createdDate": "2024-10-29",
            "department": "",
            "environment": {
                "resource": "/policy/environment",
                "key": "PUBLIC"
            },
            "firstName": "Andre",
            "language": {
                "resource": "/policy/language",
                "key": "ENGLISH"
            },
            "lastName": "Norton",
            "library": {
                "resource": "/policy/library",
                "key": "EPLWMC"
            },
            "middleName": "",
            "preferredName": "",
            "privilegeExpiresDate": null,
            "profile": {
                "resource": "/policy/userProfile",
                "key": "EPL_ADULT"
            },
            "suffix": "",
            "title": "",
            "usePreferredName": false,
            "webAuthID": "",
            "category01": null,
            "category02": null,
            "category03": null,
            "category04": null,
            "category05": null,
            "category06": null,
            "category07": null,
            "category08": null,
            "category09": null,
            "category10": null,
            "category11": null,
            "category12": null,
            "claimsReturnedCount": 0,
            "standing": {
                "resource": "/policy/patronStanding",
                "key": "OK"
            },
            "groupId": "",
            "circRecordCount": 0,
            "keepCircHistory": "NOHISTORY"
        }
     }
    
    Failed: TODO Find failed response and add it here.
    */

    @Override
    public boolean succeeded() 
    {
        return ! this.hasErrors();
    }

    @Override
    public String errorMessage() 
    {
        if (this.hasErrors())
        {
            StringBuilder sb = new StringBuilder();
            for (Message m : this.messageList)
            {
                sb.append(m.getMessage());
            }
            return sb.toString();
        }
        return "";
    }
    
    // Parsing method
    public static SDapiResponse parseJson(String jsonString) 
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, SDapiCustomerUpdateResponse.class);
    }
    
    private String resource;
    private String key;
    private Fields fields;
    private List<Message> messageList;

    public static class Message 
    {
        private String code;
        private String message;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class Fields 
    {
        private String displayName;
        private PolicyReference access;
        private String alternateID;
        private String barcode;
        private Date birthDate;
        private PolicyReference checkoutLocation;
        private String createdDate;
        private String department;
        private PolicyReference environment;
        private String firstName;
        private PolicyReference language;
        private String lastName;
        private PolicyReference library;
        private String middleName;
        private String preferredName;
        private Date privilegeExpiresDate;
        private PolicyReference profile;
        private String suffix;
        private String title;
        private boolean usePreferredName;
        private String webAuthID;
        private PolicyReference category01;
        private PolicyReference category02;
        private PolicyReference category03;
        private PolicyReference category04;
        private PolicyReference category05;
        private PolicyReference category06;
        private PolicyReference category07;
        private PolicyReference category08;
        private PolicyReference category09;
        private PolicyReference category10;
        private PolicyReference category11;
        private PolicyReference category12;
        private int claimsReturnedCount;
        private PolicyReference standing;
        private String groupId;
        private int circRecordCount;
        private String keepCircHistory;

        // Getters and Setters
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        
        public PolicyReference getAccess() { return access; }
        public void setAccess(PolicyReference access) { this.access = access; }
        
        public String getAlternateID() { return alternateID; }
        public void setAlternateID(String alternateID) { this.alternateID = alternateID; }
        
        public String getBarcode() { return barcode; }
        public void setBarcode(String barcode) { this.barcode = barcode; }
        
        public Date getBirthDate() { return birthDate; }
        public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
        
        public PolicyReference getCheckoutLocation() { return checkoutLocation; }
        public void setCheckoutLocation(PolicyReference checkoutLocation) { this.checkoutLocation = checkoutLocation; }
        
        public String getCreatedDate() { return createdDate; }
        public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
        
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        
        public PolicyReference getEnvironment() { return environment; }
        public void setEnvironment(PolicyReference environment) { this.environment = environment; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public PolicyReference getLanguage() { return language; }
        public void setLanguage(PolicyReference language) { this.language = language; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public PolicyReference getLibrary() { return library; }
        public void setLibrary(PolicyReference library) { this.library = library; }
        
        public String getMiddleName() { return middleName; }
        public void setMiddleName(String middleName) { this.middleName = middleName; }
        
        public String getPreferredName() { return preferredName; }
        public void setPreferredName(String preferredName) { this.preferredName = preferredName; }
        
        public Date getPrivilegeExpiresDate() { return privilegeExpiresDate; }
        public void setPrivilegeExpiresDate(Date privilegeExpiresDate) { this.privilegeExpiresDate = privilegeExpiresDate; }
        
        public PolicyReference getProfile() { return profile; }
        public void setProfile(PolicyReference profile) { this.profile = profile; }
        
        public String getSuffix() { return suffix; }
        public void setSuffix(String suffix) { this.suffix = suffix; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public boolean isUsePreferredName() { return usePreferredName; }
        public void setUsePreferredName(boolean usePreferredName) { this.usePreferredName = usePreferredName; }
        
        public String getWebAuthID() { return webAuthID; }
        public void setWebAuthID(String webAuthID) { this.webAuthID = webAuthID; }
        
        public PolicyReference getCategory01() { return category01; }
        public void setCategory01(PolicyReference category01) { this.category01 = category01; }
        
        public PolicyReference getCategory02() { return category02; }
        public void setCategory02(PolicyReference category02) { this.category02 = category02; }
        
        public PolicyReference getCategory03() { return category03; }
        public void setCategory03(PolicyReference category03) { this.category03 = category03; }
        
        public PolicyReference getCategory04() { return category04; }
        public void setCategory04(PolicyReference category04) { this.category04 = category04; }
        
        public PolicyReference getCategory05() { return category05; }
        public void setCategory05(PolicyReference category05) { this.category05 = category05; }
        
        public PolicyReference getCategory06() { return category06; }
        public void setCategory06(PolicyReference category06) { this.category06 = category06; }
        
        public PolicyReference getCategory07() { return category07; }
        public void setCategory07(PolicyReference category07) { this.category07 = category07; }
        
        public PolicyReference getCategory08() { return category08; }
        public void setCategory08(PolicyReference category08) { this.category08 = category08; }
        
        public PolicyReference getCategory09() { return category09; }
        public void setCategory09(PolicyReference category09) { this.category09 = category09; }
        
        public PolicyReference getCategory10() { return category10; }
        public void setCategory10(PolicyReference category10) { this.category10 = category10; }
        
        public PolicyReference getCategory11() { return category11; }
        public void setCategory11(PolicyReference category11) { this.category11 = category11; }
        
        public PolicyReference getCategory12() { return category12; }
        public void setCategory12(PolicyReference category12) { this.category12 = category12; }
        
        public int getClaimsReturnedCount() { return claimsReturnedCount; }
        public void setClaimsReturnedCount(int claimsReturnedCount) { this.claimsReturnedCount = claimsReturnedCount; }
        
        public PolicyReference getStanding() { return standing; }
        public void setStanding(PolicyReference standing) { this.standing = standing; }
        
        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }
        
        public int getCircRecordCount() { return circRecordCount; }
        public void setCircRecordCount(int circRecordCount) { this.circRecordCount = circRecordCount; }
        
        public String getKeepCircHistory() { return keepCircHistory; }
        public void setKeepCircHistory(String keepCircHistory) { this.keepCircHistory = keepCircHistory; }
    }

    public static class PolicyReference 
    {
        @SerializedName(value = "resource", alternate = {"resource_path"})
        private String resource;
        private String key;
        
        public String getResource() { return resource; }
        public void setResource(String resource) { this.resource = resource; }
        
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
    }

    // Getters and Setters for main class
    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    public Fields getFields() { return fields; }
    public void setFields(Fields fields) { this.fields = fields; }

    public List<Message> getMessageList() { return messageList; }
    public void setMessageList(List<Message> messageList) { this.messageList = messageList; }

    // Helper method to check if the response contains errors
    public boolean hasErrors() 
    {
        return messageList != null && !messageList.isEmpty();
    }
}
