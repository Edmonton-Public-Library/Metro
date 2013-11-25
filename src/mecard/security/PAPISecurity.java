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

import com.sun.org.apache.xml.internal.security.utils.Base64;
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
    public static final String AUTHORIZATION_PREFIX = "PWS";
    private final String securityKey;
    
    public static PAPISecurity getInstanceOf()
    {
        return new PAPISecurity();
    }
    
    private PAPISecurity()
    {
        Properties props = PropertyReader.getProperties(ConfigFileTypes.POLARIS);
        this.securityKey = props.getProperty(PolarisPropertyTypes.PAPI_ACCESS_KEY_ID.toString());
    }
    
    /**
     * Computes the PAPI hash signature.
     * @param accessKey
     * @param httpMethod
     * @param uri
     * @param httpDate @see #getPolarisDate() 
     * @param patronPassword which may be empty but not null.
     * @return hash prepared from code taken from Polaris API manual.
     */
    String getPAPIHash(
        String accessKey,
        String httpMethod,
        String uri,
        String httpDate,
        String patronPassword
    ){
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
            String data;
            if (patronPassword.length() > 0)
                data = httpMethod + uri + httpDate + patronPassword;
            else
                data = httpMethod + uri + httpDate;
            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());
            // Convert raw bytes to Hex
            
            // result = Base64.encodeToString(rawHmac, 0);
            // http://stackoverflow.com/questions/13109588/base64-encoding-in-java
            result = Base64.encode(rawHmac);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e1) 
        {
            System.out.println(new Date() + e1.getMessage());
            throw new ConfigurationException("The user key is invalid.");
        }
        return result;
    }
    
    /**
     * Returns the authorization string that must be passed into the HTTP
     * header for privileged operations.
     * @param HTTPMethod
     * @param uri
     * @param patronPassword
     * @return Authorization string - 'PWS [PAPIAccessKeyID]:[Signature]'
     * See pg. 69 HTTP Pocket Reference O'Reilly.
     */
    public String getAuthorization(String HTTPMethod, URI uri, String patronPassword)
    {
        // Authorization - PWS [PAPIAccessKeyID]:[Signature]
        // •PWS must be in caps
        // •No space before or after :
        // •[PAPIAccessKeyID] - Assigned by Polaris
        // •[Signature] - The signature is the following, encoded with SHA1 UTF-8:
        // [HTTPMethod][URI][Date][PatronPassword]
        String signature = this.getPAPIHash(
                this.securityKey,
                HTTPMethod, 
                uri.toASCIIString(), 
                this.getPolarisDate(), 
                this.getPatronPassword()
        );
        return AUTHORIZATION_PREFIX + " " + this.securityKey + ":" + signature;
    }
    
    /**
     * 
     * @return Patron password (if required).
     */
    String getPatronPassword()
    {
        return "";
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

