/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Andrew Nisbet
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
package api;

import mecard.util.SIPConnector;

/**
 * Helper class for formatting SIP requests.
 *
 * @author metro
 */
public class SIPRequest
{
    /**
     * Creates a SIP Patron Information request given a user id and pin.
     *
     * @param userId
     * @param userPin
     * @return String of SIP formatted request, ready to send.
     */
    public final String patronInfoRequest(String userId, String userPin)
    {
        // sipData should look like: "63                               AO|AA21221012345678|AD64058|AY1AZF374\r"
        StringBuilder request = new StringBuilder();
        if (userId == null || userPin == null)
        {
            return request.append(SIPConnector.CONNECTION_TERMINATOR).toString();
        }
        request.append("63                               AO|AA");
        request.append(userId);
        request.append("|AD");
        request.append(userPin);
        request.append("|AY1AZ");
        request.append(SIPConnector.getCheckSum(request.toString()));
        request.append(SIPConnector.CONNECTION_TERMINATOR);
        return request.toString();
    }

    public final String getILSStatus()
    {
        return "990   2.00AY1AZFCD8";
    }
}
