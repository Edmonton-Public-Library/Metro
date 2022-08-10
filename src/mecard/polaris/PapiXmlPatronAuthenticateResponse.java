/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
package mecard.polaris;

import java.time.LocalDate;
import mecard.util.DateComparer;

/**
 * Encapsulates important information about the patron authentication response.
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public final class PapiXmlPatronAuthenticateResponse extends PapiXmlResponse
{

    private String token;
    private String secret;
    private int patronId;
    private String authExpiry;
    
    public PapiXmlPatronAuthenticateResponse(String xml)
    {
        super(xml);
        /*
            Successful response
            -------------------
            "PAPIErrorCode": 0,
            "ErrorMessage": null,
            "AccessToken": "$2a$10$MD1PFF/65owmn0uMMnV6lesArBATfiXOmNSn7kQwU7YoqoqEVBU3W",
            "AccessSecret": "$2a$10$MD1PFF/65owmn0uMMnV6lesArBATfiXOmNSn7kQwU7YoqoqEVBU3W",
            "PatronID": 2022,
            "AuthExpDate": null
            
            Failed Response
            ---------------
            "PAPIErrorCode": -1,
            "ErrorMessage": "PatronAuthenticationData object is null.",
            "AccessToken": null,
            "AccessSecret": null,
            "PatronID": 0,
            "AuthExpDate": null // which in XML looks like: <AuthExpDate i:nil="true" />
        */
        if (this.failed()) return;
        try 
        {
            this.token      = root.getElementsByTagName("AccessToken").item(0).getTextContent();
            this.secret     = root.getElementsByTagName("AccessSecret").item(0).getTextContent();
            this.patronId   = Integer.parseInt(root.getElementsByTagName("PatronID").item(0).getTextContent());
            // When the token and secret expire.
            this.authExpiry = root.getElementsByTagName("AuthExpDate").item(0).getTextContent();
        }
        catch (NumberFormatException e)
        {
            System.out.println("**error parsing PatronID, expected an integer.");
        }
    }
    
    /**
     * Gets the access token which can be used by staff to authenticate without
     * a patron password, but also needs to be passed to calls that require
     * the patron to authenticate.
     * 
     * @return String access token
     */
    public String getAccessToken()
    {
        return this.token;
    }
    
    /**
     * Gets the access secret which can be used by staff to authenticate without
     * a patron password, but also needs to be passed to calls that require
     * the patron to authenticate.
     * 
     * @return String access secret
     */
    public String getAccessSecret()
    {
        return this.secret;
    }
    
    /**
     * Provides the date of the token's expiration as a 'Polaris' (RFC1123) date 
     * object suitable for date computations.
     * 
     * @return LocalDate object of the token's expiry.
     */
    public LocalDate getTokenExpiration()
    {
        if (this.authExpiry.isEmpty())
        {
            return DateComparer.getRFC1123Date(DateComparer.getRFC1123Date());
        }
        return DateComparer.getRFC1123Date(this.authExpiry);
    }
    
    /**
     * String version of the token's expiry date in 'Polaris' (RFC1123) format.
     * @return String version of token's expiry date.
     */
    public String getTokenExpirationAsString()
    {
        return this.authExpiry;
    }
    
    /**
     * Convenience method to tell if the patron authentication request worked.
     * @return true if the patron authenticated and false otherwise.
     */
    public boolean authenticated()
    {
        return (this.succeeded() && this.patronId > 0);
    }
}
