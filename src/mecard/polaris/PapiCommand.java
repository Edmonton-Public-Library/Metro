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

import api.Command;
import api.CommandStatus;
import api.HttpCommandStatus;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import mecard.ResponseTypes;
import mecard.config.PapiPropertyTypes;
import mecard.exception.ConfigurationException;
import mecard.util.DateComparer;
import org.apache.commons.codec.binary.Base64;


/**
 * This class manages the basic sending and receiving of messages to a web 
 * service.
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class PapiCommand implements Command
{
    public enum HttpVerb
    {
        GET,
        POST,
        PUT
    }
    private static URI uri;
    private static boolean debug;
    private static HttpClient httpClient;
    private static HttpVerb httpMethod;
    private static String xmlBodyText;
    private static String apiUserId;
    private static String apiKey;
    private static String accessToken = "";
    private static int timezoneDelta;
    
    public static class Builder
    {
        private boolean debug;
        private HttpVerb httpMethod;
        private Properties webServiceProperties;
        private URI uri;
        private String bodyText;
        private String apiKey;
        private String apiUserId;
        private HttpClient.Version httpVersion;
        private int connectionTimeout;
        private int timezoneDelta;

        /**
         * Builds a PAPI Command using the resources specified in the papi.properties
         * file and the HTTP method name.
         * 
         * @param wsConfigs
         * @param httpMethod 
         */
        public Builder(Properties wsConfigs, String httpMethod)
        {
            // Contains API key and USER name etc.
            this.webServiceProperties = wsConfigs;
            this.apiKey    = this.webServiceProperties.getProperty(PapiPropertyTypes.API_KEY.toString());
            this.apiUserId = this.webServiceProperties.getProperty(PapiPropertyTypes.API_USER_ID.toString());
            String version = this.webServiceProperties.getProperty(PapiPropertyTypes.HTTP_VERSION.toString(), "1.1");
            switch(version)
            {
                case "1.1":
                    this.httpVersion = HttpClient.Version.HTTP_1_1;
                    break;
                case "2.0":
                    this.httpVersion = HttpClient.Version.HTTP_2;
                    break;
                default:
                    this.httpVersion = HttpClient.Version.HTTP_2;
                    break;
            }
            String cTimeout = this.webServiceProperties.getProperty(PapiPropertyTypes.CONNECTION_TIMEOUT.toString());
            try
            {
                this.connectionTimeout = Integer.parseInt(cTimeout);
            }
            catch (NumberFormatException e)
            {
                System.out.println("*warn: invalid connection timeout set in "
                        + "papi.properties value must be an integer.\n"
                        + "Defaulting to 10 seconds.");
                this.connectionTimeout = 10;
            }
            String d = this.webServiceProperties.getProperty(PapiPropertyTypes.DEBUG.toString(), "false");
            this.debug = Boolean.valueOf(d);
            switch(httpMethod)
            {
                case "GET":
                    this.httpMethod = HttpVerb.GET;
                    break;
                case "POST":
                    this.httpMethod = HttpVerb.POST;
                    break;
                case "PUT":
                    this.httpMethod = HttpVerb.PUT;
                    break;
                default:
                    throw new UnsupportedOperationException(
                    "**error, " + httpMethod + " not supported.");
            }
            // The difference between the time where the command is being run
            // and on the web services server may have to be compensated for.
            String timezoneDifference = this.webServiceProperties.getProperty(
                PapiPropertyTypes.ME_SERVER_TIME_ZONE_DIFFERENCE.toString(), "0");
            try
            {
                this.timezoneDelta = Integer.parseInt(timezoneDifference);
            }
            catch (NumberFormatException e)
            {
                System.out.println("*warn: invalid timezone difference setting\n"
                    + "The 'timezone-difference' must be a in of\n"
                    + "the difference between the timezone where the PapiCommand is\n"
                    + "run with respect to the timezone of the PAPI web services.\n"
                    + "For example, if the web server is in MDT, but the\n"
                    + "MeCard server is in EDT, the value should be set to 2.\n"
                    + "Defaulting to 0.0.");
                this.timezoneDelta = 0;
            }
        }
        
        /**
         * Method to set the URI of the request.
         * 
         * @param uri The Universal Resource Location as a string.
         * @return Builder.
         */
        public Builder uri(String uri)
        {
            this.uri = URI.create(uri);
            return this;
        }
        
        /**
         * Method to set the XML content of the request.
         * 
         * @param xml content of the post or put command.
         * @return Builder.
         */
        public Builder bodyXML(String xml)
        {
            this.bodyText = xml; 
            return this;
        }
        
        /**
         * Builds the command and returns a reference to it.
         *
         * @return Web Service Command reference.
         */
        public PapiCommand build()
        {
            return new PapiCommand(this);
        }
    }
    
    /**
     * Create the Web Service.
     * @param builder 
     */
    private PapiCommand(Builder builder)
    {
        PapiCommand.debug      = builder.debug;
        PapiCommand.httpMethod = builder.httpMethod;
        PapiCommand.uri        = builder.uri;
        PapiCommand.xmlBodyText= builder.bodyText;
        PapiCommand.apiKey     = builder.apiKey;
        PapiCommand.apiUserId  = builder.apiUserId;
        PapiCommand.timezoneDelta= builder.timezoneDelta;
        // Create the httpClient.
        PapiCommand.httpClient = HttpClient.newBuilder()
            .version(builder.httpVersion)
            .connectTimeout(Duration.ofSeconds(builder.connectionTimeout))
            .build();
    }
    
    /**
     * Use the patron's access token for authentication.
     * 
     * Needed for PatronUpdate, and PatronBasicDataGet.
     * 
     * @param accessToken 
     */
    public void accessToken(String accessToken)
    {
        PapiCommand.accessToken = accessToken;
    }

    @Override
    public CommandStatus execute()
    {
        HttpCommandStatus status = new HttpCommandStatus();
        try
        {
            HttpRequest request = null;
            HttpResponse<String> response;
            switch(PapiCommand.httpMethod)
            {
                case GET:
                    request = HttpRequest.newBuilder()
                    .GET()
                    .uri(PapiCommand.uri)
                    .setHeader("accept", "application/xml")
                    .setHeader("Authorization", computeAuthorizationToken())
                    .setHeader("PolarisDate", getPolarisDate())
                    .build();
                    break;
                case POST:
                    request = HttpRequest.newBuilder()
                    .POST(BodyPublishers.ofString(PapiCommand.xmlBodyText))
                    .uri(PapiCommand.uri)
                    .setHeader("accept", "application/xml")
                    .setHeader("Content-Type", "application/xml")
                    .setHeader("Authorization", computeAuthorizationToken())
                    .setHeader("PolarisDate", getPolarisDate())
                    .build();
                    break;
                case PUT:
                    request = HttpRequest.newBuilder()
                    .PUT(BodyPublishers.ofString(PapiCommand.xmlBodyText))
                    .uri(PapiCommand.uri)
                    .setHeader("accept", "application/xml")
                    .setHeader("Content-Type", "application/xml")
                    .setHeader("Authorization", computeAuthorizationToken())
                    .setHeader("PolarisDate", getPolarisDate())
                    .build();
                    break;
                default:
                    break;
            }
            
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            HttpHeaders headers = response.headers();
            status.setStatus(response.statusCode());
            status.setStdout(response.body());
            // The rest of the command.
            if (PapiCommand.debug)
            {
                System.out.println("HEADERS RETURNED:");
                headers.map().forEach((k,v) -> System.out.println(k + ":" + v));
                System.out.println("   CODE RETURNED: '" + status.getStatus()+ "'.");
                System.out.println("CONTOUT RETURNED: '" + status.getStdout()+ "'.");
                System.out.println("CONTERR RETURNED: '" + status.getStderr()+ "'.");
            }
        } 
        catch (InterruptedException ex)
        {
            status.setEnded(ResponseTypes.UNAVAILABLE.ordinal());
            System.out.println(ex.getMessage());
        } 
        catch (IOException ex)
        {
            status.setEnded(ResponseTypes.FAIL.ordinal());
            System.out.println(ex.getMessage());
        } 
        return status;
    }
    
    /**
     * Computes the authorization header based on whether the caller has 
     * specified that the user name and accessToken are required.
     * 
     * @return String of the computed authorization token required for the call.
     */
    private String computeAuthorizationToken()
    {
        StringBuffer authorizationToken = new StringBuffer();
        // "PWS WBRLSOMEUSERID:[signiture]"
        authorizationToken.append("PWS ")
                .append(PapiCommand.apiUserId)
                .append(":")
                .append(getPAPIHash());
        if (PapiCommand.debug)
        {
            System.out.println("DEBUG: authorization header\n'Authorization: " + authorizationToken + "'");
        }
        return authorizationToken.toString();
    }
    
    /**
     * Computes the PAPI hash signature.
     * <ol>
     * <li>accessKey The secret used as a key for hashing, AKA PAPI Access Key</li>
     * <li>httpMethod GET, POST, or PUT</li>
     * <li>uri</li>
     * <li>httpDate @see #getPolarisDate()</li>
     * <li>accessToken which may be empty, in the case where you are performing 
     * the operation as a staff member, but not null.</li>
     * </ol>
     * @return hash of API key, HTTP Method, URI, Polaris Date, and optional 
     * Patron password.
     */
    private String getPAPIHash()
    {
        // Test at: http://caligatio.github.io/jsSHA/
        // I am trying to develop a client to consume PAPI web services. 
        // Specifically I am trying to use the PatronRegistrationCreate 
        // web service API to create patrons. I have read through the 
        // documentation and found that the REST call is public, and 
        // so I assume that the notes from page 9 of the PAPI Reference 
        // Guide (Polaris 4.1 Doc rev. 8) apply. 
        StringBuffer data = new StringBuffer();
        data.append(PapiCommand.httpMethod.name())
                .append(PapiCommand.uri.toASCIIString())
                .append(getPolarisDate())
                // The accessToken can be empty if not a protected REST method.
                .append(PapiCommand.accessToken);
        if (PapiCommand.debug)
        {
            System.out.println("DEBUG: authorization token '" + data + "'");
        }
        // Now compute the hash from the ordered data above.
        String result = "";
        // Get an hmac_sha1 key from the raw key bytes
        byte[] secretBytes = PapiCommand.apiKey.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(secretBytes, "HmacSHA1");
        // Get an hmac_sha1 Mac instance and initialize with the signing key
        try
        {
            Mac mac;
            mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            
            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.toString().getBytes());
            // Convert raw bytes to Hex
            result = Base64.encodeBase64String(rawHmac);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e1) 
        {
            System.out.println(new Date() + e1.getMessage());
            throw new ConfigurationException("Unauthorized API user");
        }
        return result;
    }
    
    /**
     * Returns standard HTTP header date format 'ddd, dd MMM yyyy HH:mm:ss GMT'.
     * Example: 'Wed, 17 Oct 2012 22:23:32 GMT'
     * @return HTTP Date format (RFC1123) date  string.
     */
    private String getPolarisDate()
    {
        // Use HTTP Date format (RFC1123)
        // ddd, dd MMM yyyy HH:mm:ss GMT
        // Example:
        // Wed, 17 Oct 2012 22:23:32 GMT
        // If you are unable to set the date in the HTTP header, pass the following name:value pair into the header:
        // PolarisDate: ddd, dd MMM yyyy HH:mm:ss GMT
        // PolarisDate: Wed, 17 Oct 2012 22:23:32 GMT
        // Date must be within +/- 30 minutes of current time or request will fail
        return DateComparer.getRFC1123Date(PapiCommand.timezoneDelta);
    }
    
    @Override
    public String toString()
    {
        return PapiCommand.httpClient.toString();
    }
}
