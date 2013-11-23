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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import mecard.exception.ConfigurationException;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PAPIHash 
{
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    public String GetPAPIHash(
        String strAccessKey,
        String strHTTPMethod,
        String strURI,
        String strHTTPDate,
        String strPatronPassword
    ){
        String result = "";
        // Get an hmac_sha1 key from the raw key bytes
        byte[] secretBytes = strAccessKey.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(secretBytes, HMAC_SHA1_ALGORITHM);
        // Get an hmac_sha1 Mac instance and initialize with the signing key
        try
        {
            Mac mac;
            mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            String data = "";
            if (strPatronPassword.length() > 0)
                data = strHTTPMethod + strURI + strHTTPDate + strPatronPassword;
            else
                data = strHTTPMethod + strURI + strHTTPDate;
            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());
            // Convert raw bytes to Hex
            
//            result = Base64.encodeToString(rawHmac, 0);
            // http://stackoverflow.com/questions/13109588/base64-encoding-in-java
            Base64 b64 = new Base64(true);
            result = b64.encodeToString(rawHmac);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e1) 
        {
            System.out.println(new Date() + e1.getMessage());
            throw new ConfigurationException("The user key is invalid.");
        }
        return result;
    }
}

