/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
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

package mecard.config;

/**
 * All the fields related to Symphony's dumpflatuser.
 * @author andrew
 */
public enum FlatUserFieldTypes
{
    USER_ID("USER_ID"),//21221012345678
    USER_GROUP_ID("USER_GROUP_ID"),//BALZAC
    USER_NAME("USER_NAME"),//Billy"), Balzac
    USER_FIRST_NAME("USER_FIRST_NAME"),//Balzac
    USER_LAST_NAME("USER_LAST_NAME"),//Billy
    USER_PREFERRED_NAME("USER_PREFERRED_NAME"),//Willy
    USER_NAME_DSP_PREF("USER_NAME_DSP_PREF"),//0
    USER_LIBRARY("USER_LIBRARY"),//EPLMNA
    USER_PROFILE("USER_PROFILE"),//EPL-STAFF
    USER_PREF_LANG("USER_PREF_LANG"),//ENGLISH
    USER_PIN("USER_PIN"),//64058
    USER_STATUS("USER_STATUS"),//OK
    USER_ROUTING_FLAG("USER_ROUTING_FLAG"),//Y
    USER_CHG_HIST_RULE("USER_CHG_HIST_RULE"),//ALLCHARGES
    USER_LAST_ACTIVITY("USER_LAST_ACTIVITY"),//20130201
    USER_PRIV_GRANTED("USER_PRIV_GRANTED"),//20120705
    USER_PRIV_EXPIRES("USER_PRIV_EXPIRES"),//20130705
    USER_BIRTH_DATE("USER_BIRTH_DATE"),//20050303
    USER_CATEGORY2("USER_CATEGORY2"),//M - gender, or age group at Shortgrass
    USER_ACCESS("USER_ACCESS"),//PUBLIC
    USER_ENVIRONMENT("USER_ENVIRONMENT"),//PUBLIC
    USER_MAILINGADDR("USER_MAILINGADDR"),//1
    USER_ADDR1_BEGIN("USER_ADDR1_BEGIN"),
    STREET("STREET"),//7 Sir Winston Churchill Square
    CITY_STATE("CITY/STATE"),//Edmonton(""), AB
    POSTALCODE("POSTALCODE"),//T5J 2V4
    PHONE("PHONE"),//780-496-4058
    EMAIL("EMAIL"),//ilsteam@epl.ca
    USER_ADDR1_END("USER_ADDR1_END"),
    USER_ADDR2_BEGIN("USER_ADDR2_BEGIN"),
    USER_ADDR2_END("USER_ADDR2_END"),
    USER_ADDR3_BEGIN("USER_ADDR3_BEGIN"),
    USER_ADDR3_END("USER_ADDR3_END"),
    USER_XINFO_BEGIN("USER_XINFO_BEGIN"),
    NOTIFY_VIA("NOTIFY_VIA"),//PHONE
    NOTE("NOTE"),//ILS Team Test Account - DO NOT REMOVE!
    RETRNMAIL("RETRNMAIL"),//YES
    USER_XINFO_END("USER_XINFO_END");
    
    private String type;

    private FlatUserFieldTypes(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
