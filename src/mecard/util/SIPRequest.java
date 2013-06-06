/*
 * Copyright (C) 2013 metro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mecard.util;

/**
 * Helper class for formatting SIP requests.
 *
 * @author metro
 */
public class SIPRequest
{

//    public SIPRequest()
//    {
//        super(ConnectorTypes.SIP2);
//    }

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
