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
     * Returns the originalValue associated with this key.
     * @param key
     * @return the originalValue associated with this key, or an empty string if the 
 key is not present.
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
        else if (key.equals(SDapiUserFields.USER_PASSWORD.toString()))
            return this.getPassword() != null ? this.getPassword() : "";
        else if (key.equals(SDapiUserFields.USER_ACCESS.toString()))
            return this.getAccessKey() != null ? this.getAccessKey() : "";
        else if (key.equals(SDapiUserFields.LANGUAGE.toString()))
            return this.getLanguageKey() != null ? this.getLanguageKey() : "";
        else if (key.equals(SDapiUserFields.STANDING.toString()))
            return this.getStandingKey() != null ? this.getStandingKey() : "";
        else if (key.equals(SDapiUserFields.ENVIRONMENT.toString()))
            return this.getEnvironmentKey()!= null ? this.getEnvironmentKey() : "";
        else if (key.equals(SDapiUserFields.KEEP_CIRC_HISTORY.toString()))
            return this.getKeepCircHistory()!= null ? this.getKeepCircHistory() : "";
        else if (key.equals(SDapiUserFields.CATEGORY01.toString()))
            return this.getCategory01Key() != null ? this.getCategory01Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY02.toString()))
            return this.getCategory02Key() != null ? this.getCategory02Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY03.toString()))
            return this.getCategory03Key() != null ? this.getCategory03Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY04.toString()))
            return this.getCategory04Key() != null ? this.getCategory04Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY05.toString()))
            return this.getCategory05Key() != null ? this.getCategory05Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY06.toString()))
            return this.getCategory06Key() != null ? this.getCategory06Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY07.toString()))
            return this.getCategory07Key() != null ? this.getCategory07Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY08.toString()))
            return this.getCategory08Key() != null ? this.getCategory08Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY09.toString()))
            return this.getCategory09Key() != null ? this.getCategory09Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY10.toString()))
            return this.getCategory10Key() != null ? this.getCategory10Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY11.toString()))
            return this.getCategory11Key() != null ? this.getCategory11Key() : "";
        else if (key.equals(SDapiUserFields.CATEGORY12.toString()))
            return this.getCategory12Key() != null ? this.getCategory12Key() : "";
        else
        {
            System.out.println("*warning, request to get " + key + 
                " but MeCardDataToSDapiData does not support it.");
            return "";
        }
    }

    /**
     * Adds or changes a key in the table entry.If the key exists then the
 value for that key is updated and the return result is true. If the key
 didn't exist, the key value pair are added, but the return value is false.
 The return value is false if either the key of value is null, and no
 changes are made to the table.
     * @param key
     * @param value
     * @return true if a key was found and updated and false if the key or originalValue
 could not be added because they were null, or the key and originalValue pair did
 not exist when they were added.
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
            this.setCityProvince(value);
        else if (key.equals(SDapiUserFields.PROV.toString()))
            this.setCityState(value);
        else if (key.equals(SDapiUserFields.CITY_SLASH_STATE.toString()))
            this.setCityState(value);
        else if (key.equals(SDapiUserFields.STREET.toString()))
            this.setStreet(value);
        else if (key.equals(SDapiUserFields.POSTALCODE.toString()))
            this.setPostalCode(value);
        else if (key.equals(SDapiUserFields.USER_PASSWORD.toString()))
            this.setPassword(value);
        else if (key.equals(SDapiUserFields.USER_ACCESS.toString()))
            this.setAccess(value);
        else if (key.equals(SDapiUserFields.LANGUAGE.toString()))
            this.setLanguage(value);
        else if (key.equals(SDapiUserFields.STANDING.toString()))
            this.setStanding(value);
        else if (key.equals(SDapiUserFields.ENVIRONMENT.toString()))
            this.setEnvironment(value);
        else if (key.equals(SDapiUserFields.KEEP_CIRC_HISTORY.toString()))
            this.setKeepCircHistory(value);
        else if (key.equals(SDapiUserFields.CATEGORY01.toString()))
            this.setCategory01(value);
        else if (key.equals(SDapiUserFields.CATEGORY02.toString()))
            this.setCategory02(value);
        else if (key.equals(SDapiUserFields.CATEGORY03.toString()))
            this.setCategory03(value);
        else if (key.equals(SDapiUserFields.CATEGORY04.toString()))
            this.setCategory04(value);
        else if (key.equals(SDapiUserFields.CATEGORY05.toString()))
            this.setCategory05(value);
        else if (key.equals(SDapiUserFields.CATEGORY06.toString()))
            this.setCategory06(value);
        else if (key.equals(SDapiUserFields.CATEGORY07.toString()))
            this.setCategory07(value);
        else if (key.equals(SDapiUserFields.CATEGORY08.toString()))
            this.setCategory08(value);
        else if (key.equals(SDapiUserFields.CATEGORY09.toString()))
            this.setCategory09(value);
        else if (key.equals(SDapiUserFields.CATEGORY10.toString()))
            this.setCategory10(value);
        else if (key.equals(SDapiUserFields.CATEGORY11.toString()))
            this.setCategory11(value);
        else if (key.equals(SDapiUserFields.CATEGORY12.toString()))
            this.setCategory12(value);
        else
        {
            System.out.println("*warning, request to set '" + key + "' with '"
                + value + "' but MeCardDataToSDapiData doesn't know what that is.");
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
//        if (this.getProvince() != null)
//            columns.add(SDapiUserFields.PROV.toString());
        if (this.getStreet() != null)
            columns.add(SDapiUserFields.STREET.toString());
        if (this.getPostalCode() != null)
            columns.add(SDapiUserFields.POSTALCODE.toString());
        if (this.getPassword() != null)
            columns.add(SDapiUserFields.USER_PASSWORD.toString());
        if (this.getAccessKey() != null)
            columns.add(SDapiUserFields.USER_ACCESS.toString());
        if (this.getLanguageKey() != null)
            columns.add(SDapiUserFields.LANGUAGE.toString());
        if (this.getEnvironmentKey() != null)
            columns.add(SDapiUserFields.ENVIRONMENT.toString());
        if (this.getKeepCircHistory() != null)
            columns.add(SDapiUserFields.KEEP_CIRC_HISTORY.toString());
        if (this.getCategory01Key() != null)
            columns.add(SDapiUserFields.CATEGORY01.toString());
        if (this.getCategory02Key() != null)
            columns.add(SDapiUserFields.CATEGORY02.toString());
        if (this.getCategory03Key() != null)
            columns.add(SDapiUserFields.CATEGORY03.toString());
        if (this.getCategory04Key() != null)
            columns.add(SDapiUserFields.CATEGORY04.toString());
        if (this.getCategory05Key() != null)
            columns.add(SDapiUserFields.CATEGORY05.toString());
        if (this.getCategory06Key() != null)
            columns.add(SDapiUserFields.CATEGORY06.toString());
        if (this.getCategory07Key() != null)
            columns.add(SDapiUserFields.CATEGORY07.toString());
        if (this.getCategory08Key() != null)
            columns.add(SDapiUserFields.CATEGORY08.toString());
        if (this.getCategory09Key() != null)
            columns.add(SDapiUserFields.CATEGORY09.toString());
        if (this.getCategory10Key() != null)
            columns.add(SDapiUserFields.CATEGORY10.toString());
        if (this.getCategory11Key() != null)
            columns.add(SDapiUserFields.CATEGORY11.toString());
        if (this.getCategory12Key() != null)
            columns.add(SDapiUserFields.CATEGORY12.toString());
        @SuppressWarnings("unchecked")
        Set<String> s = new LinkedHashSet(columns);
        return s;
    }

    /**
     * Removes an entry from the user table.Specially used for libraries that 
 don't use USER_PREFERED_NAME and the like. This method stops the key and 
 value from being written in the table by removing them.
     * @param key
     * @return true if the key was found, and false otherwise. Any originalValue stored 
 at the key will be deleted from the table.
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
            this.setCityProvince(null);
       else if (key.equals(SDapiUserFields.PROV.toString()))
            this.setCityState(null);
        else if (key.equals(SDapiUserFields.CITY_SLASH_STATE.toString()))
            this.setCityState(null);
        else if (key.equals(SDapiUserFields.STREET.toString()))
            this.setStreet(null);
        else if (key.equals(SDapiUserFields.POSTALCODE.toString()))
            this.setPostalCode(null);
        else if (key.equals(SDapiUserFields.USER_PASSWORD.toString()))
            this.setPassword(null);
        else if (key.equals(SDapiUserFields.USER_ACCESS.toString()))
            this.setAccess(null);
        else if (key.equals(SDapiUserFields.LANGUAGE.toString()))
            this.setLanguage(null);
        else if (key.equals(SDapiUserFields.STANDING.toString()))
            this.setStanding(null);
        else if (key.equals(SDapiUserFields.ENVIRONMENT.toString()))
            this.setStanding(null);
        else if (key.equals(SDapiUserFields.KEEP_CIRC_HISTORY.toString()))
            this.setKeepCircHistory(null);
        else if (key.equals(SDapiUserFields.CATEGORY01.toString()))
            this.setCategory01(null);
        else if (key.equals(SDapiUserFields.CATEGORY02.toString()))
            this.setCategory02(null);
        else if (key.equals(SDapiUserFields.CATEGORY03.toString()))
            this.setCategory03(null);
        else if (key.equals(SDapiUserFields.CATEGORY04.toString()))
            this.setCategory04(null);
        else if (key.equals(SDapiUserFields.CATEGORY05.toString()))
            this.setCategory05(null);
        else if (key.equals(SDapiUserFields.CATEGORY06.toString()))
            this.setCategory06(null);
        else if (key.equals(SDapiUserFields.CATEGORY07.toString()))
            this.setCategory07(null);
        else if (key.equals(SDapiUserFields.CATEGORY08.toString()))
            this.setCategory08(null);
        else if (key.equals(SDapiUserFields.CATEGORY09.toString()))
            this.setCategory09(null);
        else if (key.equals(SDapiUserFields.CATEGORY10.toString()))
            this.setCategory10(null);
        else if (key.equals(SDapiUserFields.CATEGORY11.toString()))
            this.setCategory11(null);
        else if (key.equals(SDapiUserFields.CATEGORY12.toString()))
            this.setCategory12(null);
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
        if (branch == null || branch.isBlank())
            this.fields.profile = null;
        else
            this.fields.library = new PolicyResource("/policy/library", branch);
    }
    
    private void setProfile(String profile)
    {
        if (profile == null || profile.isBlank())
            this.fields.profile = null;
        else
            this.fields.profile = new PolicyResource("/policy/userProfile", profile);
    }
    
    private void setAccess(String access)
    {
        if (access == null || access.isBlank())
            this.fields.access = null;
        else
            this.fields.access = new PolicyResource("/policy/userAccess", access);
    }
    
    private void setLanguage(String language)
    {
        if (language == null || language.isBlank())
            this.fields.language = null;
        else
            this.fields.language = new PolicyResource("/policy/language", language);
    }
    
    private void setStanding(String standing)
    {
        if (standing == null || standing.isBlank())
            this.fields.standing = null;
        else
            this.fields.standing = new PolicyResource("/policy/patronStanding", standing);
    }
    
    private void setEnvironment(String environment)
    {
        if (environment == null || environment.isBlank())
            this.fields.environment = null;
        else
            this.fields.environment = new PolicyResource("/policy/environment", environment);
    }
    
    private void setCategory01(String cat01)
    {
        if (cat01 == null || cat01.isBlank())
            this.fields.category01 = null;
        else
            this.fields.category01 = new PolicyResource("/policy/patronCategory01", cat01);
    }
    
    private void setCategory02(String cat02)
    {
        if (cat02 == null || cat02.isBlank())
            this.fields.category02 = null;
        else
            this.fields.category02 = new PolicyResource("/policy/patronCategory02", cat02);
    }
    
    private void setCategory03(String cat03)
    {
        if (cat03 == null || cat03.isBlank())
            this.fields.category03 = null;
        else
            this.fields.category03 = new PolicyResource("/policy/patronCategory03", cat03);
    }
    
    private void setCategory04(String cat04)
    {
        if (cat04 == null || cat04.isBlank())
            this.fields.category04 = null;
        else
            this.fields.category04 = new PolicyResource("/policy/patronCategory04", cat04);
    }
    
    private void setCategory05(String cat05)
    {
        if (cat05 == null || cat05.isBlank())
            this.fields.category05 = null;
        else
            this.fields.category05 = new PolicyResource("/policy/patronCategory05", cat05);
    }
    
    private void setCategory06(String cat06)
    {
        if (cat06 == null || cat06.isBlank())
            this.fields.category06 = null;
        else
            this.fields.category06 = new PolicyResource("/policy/patronCategory06", cat06);
    }
    
    private void setCategory07(String cat07)
    {
        if (cat07 == null || cat07.isBlank())
            this.fields.category07 = null;
        else
            this.fields.category07 = new PolicyResource("/policy/patronCategory07", cat07);
    }
    
    private void setCategory08(String cat08)
    {
        if (cat08 == null || cat08.isBlank())
            this.fields.category08 = null;
        else
            this.fields.category08 = new PolicyResource("/policy/patronCategory08", cat08);
    }
    
    private void setCategory09(String cat09)
    {
        if (cat09 == null || cat09.isBlank())
            this.fields.category09 = null;
        else
            this.fields.category09 = new PolicyResource("/policy/patronCategory09", cat09);
    }
    
    private void setCategory10(String cat10)
    {
        if (cat10 == null || cat10.isBlank())
            this.fields.category10 = null;
        else
            this.fields.category10 = new PolicyResource("/policy/patronCategory10", cat10);
    }
    
    private void setCategory11(String cat11)
    {
        if (cat11 == null || cat11.isBlank())
            this.fields.category11 = null;
        else
            this.fields.category11 = new PolicyResource("/policy/patronCategory11", cat11);
    }
    
    private void setCategory12(String cat12)
    {
        if (cat12 == null || cat12.isBlank())
            this.fields.category12 = null;
        else
            this.fields.category12 = new PolicyResource("/policy/patronCategory12", cat12);
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
    
    private void setPassword(String pin)
    {
        this.fields.password = pin;
    }
    
    private void setKeepCircHistory(String circHistory)
    {
        this.fields.keepCircHistory = circHistory;
    }
    
    private String getPassword()
    {
        return this.fields.password;
    }
    
    private String getKeepCircHistory()
    {
        return this.fields.keepCircHistory;
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
    
    private void setCityProvince(String cityProvince)
    {
        if (this.fields.address1 == null)
            this.fields.address1 = new ArrayList<>();
        // Add city/state
        this.fields.address1.add(new Address(
            "/user/patron/address1",
            "1",  // Is this correct as the same as CITY/STATE?
            new AddressCode("/policy/patronAddress1", "CITY/PROV"),
            cityProvince
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
    private String getAccessKey() { return this.fields.access != null ? this.fields.access.getKey() : null; }
    private String getLanguageKey() { return this.fields.language != null ? this.fields.language.getKey() : null; }
    private String getStandingKey() { return this.fields.standing != null ? this.fields.standing.getKey() : null; }
    private String getEnvironmentKey() { return this.fields.environment != null ? this.fields.environment.getKey() : null; }
    private String getCategory01Key() { return this.fields.category01 != null ? this.fields.category01.getKey() : null; }
    private String getCategory02Key() { return this.fields.category02 != null ? this.fields.category02.getKey() : null; }
    private String getCategory03Key() { return this.fields.category03 != null ? this.fields.category03.getKey() : null; }
    private String getCategory04Key() { return this.fields.category04 != null ? this.fields.category04.getKey() : null; }
    private String getCategory05Key() { return this.fields.category05 != null ? this.fields.category05.getKey() : null; }
    private String getCategory06Key() { return this.fields.category06 != null ? this.fields.category06.getKey() : null; }
    private String getCategory07Key() { return this.fields.category07 != null ? this.fields.category07.getKey() : null; }
    private String getCategory08Key() { return this.fields.category08 != null ? this.fields.category08.getKey() : null; }
    private String getCategory09Key() { return this.fields.category09 != null ? this.fields.category09.getKey() : null; }
    private String getCategory10Key() { return this.fields.category10 != null ? this.fields.category10.getKey() : null; }
    private String getCategory11Key() { return this.fields.category11 != null ? this.fields.category11.getKey() : null; }
    private String getCategory12Key() { return this.fields.category12 != null ? this.fields.category12.getKey() : null; }

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
        
        //{
        //   "name": "pin",
        //   "type": "string",
        //   "required": false,
        //   "nullable": true,
        //   "editable": true,
        //   "includedInDefault": false,
        //   "min": 0,
        //   "max": 25,
        //   "returnable": true,
        //   "sendable": true
        //},

        @SerializedName("pin")
        private String password;
        
        @SerializedName("keepCircHistory")
        private String keepCircHistory;

        @SerializedName("library")
        private PolicyResource library;

        @SerializedName("profile")
        private PolicyResource profile;
        
        @SerializedName("access")
        private PolicyResource access;
        
        @SerializedName("language")
        private PolicyResource language;
        
        @SerializedName("standing")
        private PolicyResource standing;
        
        @SerializedName("environment")
        private PolicyResource environment;
        
        @SerializedName("category01")
        private PolicyResource category01;
        
        @SerializedName("category02")
        private PolicyResource category02;
        
        @SerializedName("category03")
        private PolicyResource category03;
        
        @SerializedName("category04")
        private PolicyResource category04;
        
        @SerializedName("category05")
        private PolicyResource category05;
        
        @SerializedName("category06")
        private PolicyResource category06;
        
        @SerializedName("category07")
        private PolicyResource category07;
        
        @SerializedName("category08")
        private PolicyResource category08;
        
        @SerializedName("category09")
        private PolicyResource category09;
        
        @SerializedName("category10")
        private PolicyResource category10;
        
        @SerializedName("category11")
        private PolicyResource category11;
        
        @SerializedName("category12")
        private PolicyResource category12;
        
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
