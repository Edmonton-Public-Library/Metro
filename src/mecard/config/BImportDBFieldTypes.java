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
 *
 * @author metro
 */
public enum BImportDBFieldTypes
{
    SECOND_ID("second_id"),
    NAME("name"),
    EXPIRY("expiration_date"),
    PIN("pin#"),
    PHONE_TYPE("phone_type"),
    PHONE_NUMBER("phone_no"),
    ADDRESS_1("address1"),
    ADDRESS_2("address2"),
    CITY("city_st"),
    POSTAL_CODE("postal_code"),
    EMAIL_NAME("email_name"),
    EMAIL_ADDRESS("email_address"),
    BARCODE("bbarcode");
    
    private String type;

    private BImportDBFieldTypes(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
