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

package mecard.calgary.cplapi;

import mecard.security.AuthenticationData;

/**
 * Simple methods to get customer card number and pin as json.
 * @author anisbet
 */
public class CPLapiCardNumberPin extends AuthenticationData
{
    
    public CPLapiCardNumberPin(){}
    
    public CPLapiCardNumberPin(String cardNumber, String pin)
    {
        this.userId = cardNumber;
        this.password = pin;
    }
    
    @Override
    public String getPatronAuthentication(String cardNumber, String pin)
    {
        StringBuilder loginBodyText = new StringBuilder();
        loginBodyText.append("{\"cardNumber\": \"")
                .append(cardNumber)
                .append("\", \"pin\": \"")
                .append(pin).append("\"}");
        return loginBodyText.toString();
    }
    
    @Override
    public String getStaffAuthentication(String domain, String cardNumber, String pin)
    {
        return getPatronAuthentication(cardNumber, pin);
    }
    
    @Override
    public String toString()
    {
        return this.getPatronAuthentication(this.userId, this.password);
    }
}
