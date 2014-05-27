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
package mecard.security;

import org.apache.commons.codec.binary.Base64;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import mecard.config.ConfigFileTypes;
import mecard.config.PolarisPropertyTypes;
import mecard.config.PropertyReader;
import mecard.exception.ConfigurationException;
import mecard.util.DateComparer;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class PAPISecurity 
{
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
//    // Standard prefix for the 'Authentication:' header.
//    public static final String AUTHORIZATION_PREFIX = "PWS";
    // Value supplied by Polaris used in the 'Authentication:' header.
    public final static String X_PAPI_ACCESSTOKEN_HEADER = "X-PAPI-AccessToken";
    private final String PAPIAccessKeyId;
    private final String PAPISecret;
    
    public static PAPISecurity getInstanceOf()
    {
        return new PAPISecurity();
    }
    
    private PAPISecurity()
    {
        Properties props     = PropertyReader.getProperties(ConfigFileTypes.POLARIS);
        this.PAPIAccessKeyId = props.getProperty(PolarisPropertyTypes.PAPI_ACCESS_KEY_ID.toString());
        this.PAPISecret      = props.getProperty(PolarisPropertyTypes.PAPI_ACCESS_SECRET.toString());
    }
    
    /**
     * Computes the PAPI hash signature.
     * @param accessKey The secret used as a key for hashing, AKA PAPI Access Key
     * @param httpMethod GET or POST
     * @param uri documentation refers to this without the base URL (http://server.domain).
     * @param httpDate @see #getPolarisDate() 
     * @param patronPassword which may be empty, in the case where you are 
     * performing the operation as a staff member, but not null.
     * @return hash prepared from code taken from Polaris API manual.
     */
    String getPAPIHash(
        String accessKey, 
        String httpMethod,
        String uri,
        String httpDate,
        String patronPassword // optional
    )
    {
        // Test at: http://caligatio.github.io/jsSHA/
//        I am trying to develop a client to consume PAPI web services. Specifically I am trying to use the PatronRegistrationCreate web service API to create patrons. I have read through the documentation and found that the REST call is public, and so I assume that the notes from page 9 of the PAPI Reference Guide (Polaris 4.1 Doc rev. 8) apply. I create a 
        String data;
        if (patronPassword.length() > 0)
        {
            // TODO: Pass the secret twice, once as the accessKey and again as the patron password param(?)
            data = httpMethod + uri + httpDate + patronPassword;
        }
        else
        {
            data = httpMethod + uri + httpDate;
        }
        System.out.println("DATA-=> '"+data+"'");
        return this.getHash(accessKey, data);
    }
    
    String getHash(String accessKey, String data)
    {
        String result = "";
        // Get an hmac_sha1 key from the raw key bytes
        byte[] secretBytes = accessKey.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(secretBytes, HMAC_SHA1_ALGORITHM);
        // Get an hmac_sha1 Mac instance and initialize with the signing key
        try
        {
            Mac mac;
            mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            
            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());
            // Convert raw bytes to Hex
            result = Base64.encodeBase64String(rawHmac);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e1) 
        {
            System.out.println(new Date() + e1.getMessage());
            throw new ConfigurationException("The user key is invalid.");
        }
        return result;
    }
    
    /**
     * Used to get a signature for special HTTP X-header. Used in public PAPI web service
     * methods. Since public methods don't take a authentication token in the rest request
     * you must supply a special X-header (X-PAPI-AccessToken) which is the hash of the 
     * HTTP method, URI, and Access Secret.
     * 
     * Using Public Methods as an Authenticated Staff User
     * In some scenarios, as an authenticated staff user, you may want to call a public method that requires the
     * patron’s password. Instead of looking up the patron’s password, you may build the authentication
     * signature using the AccessSecret. Because the public method does not contain the AccessToken in the URI,
     * you simply pass in a custom HTTP header field called X-PAPI-AccessToken. The PAPI Service will look
     * for the X-PAPI-AccessToken header field and act accordingly.
     * Note:
     * This process may fail to work if a firewall or network device is configured to remove non-standard HTTP header
     * fields.
     * 
     * @param HTTPMethod POST, or GET
     * @param uri the authentication URI, like 'http://localhost/PAPIService/REST/public/v1/1033/100/1/patron'
     * @return the signature used on the end of the Authorization HTTP header.
     */
    public String getXPAPIAccessTokenHeader(String HTTPMethod, URI uri)
    {
        // Authorization - PWS [PAPIAccessKeyID]:[Signature]
        // •PWS must be in caps
        // •No space before or after :
        // •[PAPIAccessKeyID] - Assigned by Polaris
        // •[Signature] - The signature is the following, encoded with SHA1 UTF-8:
        // [HTTPMethod][URI][Date][PatronPassword]
        String signature = this.getPAPIHash(
                this.PAPISecret, // From the properties file.
                HTTPMethod, 
                uri.toASCIIString(), 
                this.getPolarisDate(), 
                ""
        );
        return "PWS " + this.PAPIAccessKeyId + ":" + signature;
    }
    
    /**
     * 
     * @param HTTPMethod POST, or GET
     * @param uri the authentication URI, like 'http://localhost/PAPIService/REST/public/v1/1033/100/1/patron/21756003332022'
     * @param patronPassword This, in the case of public methods (of which PatronRegistrationCreate and PatronUpdate both are)
     * is the staff authentication secret that is hashed with the URL to authenticate the desired transaction. See page 9 
     * Polaris Application Programming Interface (PAPI) Reference Guide, Polaris 4.1, PAPI version 1, Document revision 8.
     * @return the signature used on the end of the Authorization HTTP header.
     */
    public String getSignature(String HTTPMethod, URI uri, String patronPassword)
    {
        // Authorization - PWS [PAPIAccessKeyID]:[Signature]
        // •PWS must be in caps
        // •No space before or after :
        // •[PAPIAccessKeyID] - Assigned by Polaris
        // •[Signature] - The signature is the following, encoded with SHA1 UTF-8:
        // [HTTPMethod][URI][Date][PatronPassword]
        String signature = this.getPAPIHash(
                this.PAPISecret, // From the properties file.
                HTTPMethod, 
                uri.toASCIIString(), 
                this.getPolarisDate(), 
                patronPassword // Optional, may be empty.
        );
        return signature;
    }
    
    /**
     * Returns the authorization signature string used in the HTTP
     * header, to get the access token (good for 24 hours, but compute each time)
     * that is required for privileged operations.
     * @param HTTPMethod POST usually.
     * @param uri of the WS operation either patron create or update.
     * @return Authorization string used to compute initial login Authentication header value.
     */
    public String getSignature(String HTTPMethod, URI uri)
    {
        // Authorization - PWS [PAPIAccessKeyID]:[Signature]
        // •PWS must be in caps
        // •No space before or after :
        // •[PAPIAccessKeyID] - Assigned by Polaris
        // •[Signature] - The signature is the following, encoded with SHA1 UTF-8:
        // [HTTPMethod][URI][Date][PatronPassword]
        String signature = this.getPAPIHash(
                this.PAPISecret,
                HTTPMethod, 
                uri.toASCIIString(), 
                this.getPolarisDate(), 
                ""
        );
        return signature;
    }
    
    /**
     * Returns standard HTTP header date format 'ddd, dd MMM yyyy HH:mm:ss GMT'.
     * Example: 'Wed, 17 Oct 2012 22:23:32 GMT'
     * @return HTTP Date format (RFC1123) date  string.
     */
    String getPolarisDate()
    {
        // Use HTTP Date format (RFC1123)
        // ddd, dd MMM yyyy HH:mm:ss GMT
        // Example:
        // Wed, 17 Oct 2012 22:23:32 GMT
        // If you are unable to set the date in the HTTP header, pass the following name:value pair into the header:
        // PolarisDate: ddd, dd MMM yyyy HH:mm:ss GMT
        // PolarisDate: Wed, 17 Oct 2012 22:23:32 GMT
        // Date must be within +/- 30 minutes of current time or request will fail
        return DateComparer.getRFC1123Date();
    }
}

