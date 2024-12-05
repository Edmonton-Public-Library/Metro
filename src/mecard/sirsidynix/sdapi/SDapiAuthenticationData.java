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

import mecard.security.AuthenticationData;

/**
 *
 * @author anisbet
 */
public class SDapiAuthenticationData implements AuthenticationData
{
    @Override
    public String getPatronAuthentication(String userId, String password)
    {
        //        {
        //            "login": "{{staffId}}",
        //            "password": "{{staffPassword}}"
        //        }
        StringBuilder loginBodyText = new StringBuilder();
        loginBodyText.append("{\"login\": \"")
                .append(userId)
                .append("\", \"password\": \"")
                .append(password).append("\"}");
        return loginBodyText.toString();
    }
    
    @Override
    public String getStaffAuthentication(String domain, String userId, String password)
    {
        //        {
        //            "login": "{{patronId}}",
        //            "password": "{{patronPassword}}"
        //        }
        return getPatronAuthentication(userId, password);
    }
}
