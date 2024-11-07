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

import api.Command;
import api.CommandStatus;
import api.HttpCommandStatus;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import mecard.config.SDapiPropertyTypes;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;
import mecard.exception.ConfigurationException;
import mecard.security.SDapiSecurity;

/**
 * 
 * @author anisbet
 */
public class SDapiCommand implements Command
{
    public enum HttpVerb
    {
        GET,
        POST,
        PUT
    }
    
    private static URI uri;
    private static HttpVerb httpMethod;
    private static String staffPassword;
    private static String patronAccessToken;
    private static boolean debug;
    private static String staffId;
    private static HttpClient httpClient;
    private static String clientId;
    private static String jsonBodyText;
    
    public static class Builder
    {
        private boolean debug;
        private HttpVerb httpMethod;
        private Properties webServiceProperties;
        private String bodyText;
        private String staffPassword;
        private String staffId;
        private int connectionTimeout;
        private HttpClient.Version httpVersion;
        private String clientId;
        private String appId;
        private String baseUrl;
        private String endPoint;
        private URI uri;
        
        public Builder(Properties wsConfigs, String httpMethod)
        {
            webServiceProperties = wsConfigs;
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
            
            // Get staff ID and password from the .env file
            String envFilePath = this.webServiceProperties.getProperty(SDapiPropertyTypes.ENV.toString());
            if (envFilePath == null || envFilePath.isBlank())
            {
                throw new ConfigurationException("""
                    **error, the sdapi.properties file does not contain an entry
                    for the environment file (.env). The entry should look like this example:
                    <entry key="env-file-path">/MeCard/path/to/.env</entry>
                    Add entry or check for spelling mistakes.
                     """);
            }
            
            
            // Read the staff ID and password.
            try 
            {
                SDapiSecurity sds = new SDapiSecurity(envFilePath);
                this.staffId = sds.getStaffId();
                this.staffPassword = sds.getStaffPassword();
            } 
            catch (IOException e) 
            {
                System.out.println("""
                    **error, expected an .env file but it is missing or can't be found.
                    The .env file should include entries for staff ID and password. For example,
                    STAFF_ID="SomeStaffId"
                    STAFF_PASSWORD="SomeStaffPassword"
                    """ + e);
            } 
            
            
            String version = this.webServiceProperties.getProperty(SDapiPropertyTypes.HTTP_VERSION.toString(), "1.1");
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
            
            this.clientId = this.webServiceProperties.getProperty(SDapiPropertyTypes.CLIENT_ID.toString());
            this.appId = this.webServiceProperties.getProperty(SDapiPropertyTypes.APP_ID.toString());
            this.baseUrl = this.webServiceProperties.getProperty(SDapiPropertyTypes.BASE_URL.toString());
            
            // Connection timeout
            String cTimeout = this.webServiceProperties.getProperty(SDapiPropertyTypes.CONNECTION_TIMEOUT.toString());
            try
            {
                this.connectionTimeout = Integer.parseInt(cTimeout);
            }
            catch (NumberFormatException e)
            {
                System.out.println("""
                    *warn: invalid connection timeout set in sdapi.properties 
                    value must be an integer. Defaulting to 10 seconds.""");
                this.connectionTimeout = 10;
            }
            
            
            String d = this.webServiceProperties.getProperty(SDapiPropertyTypes.DEBUG.toString(), "false");
            this.debug = Boolean.parseBoolean(d);
        }
        
        /**
         * Adds body text to a request.
         * @TODO check if the data inbound is already JSON.
         * @param bodyText - text for the body of the request.
         * @return Builder object.
         */
        public Builder bodyText(JsonObject bodyText)
        {
            // JSON-ify inbound text.
//            this.bodyText = bodyText;
            return this;
        }
        
        /**
         * Allows setting a web service endpoint.
         * @param endpoint
         * @return Builder object.
         */
        public Builder endpoint(String endpoint)
        {
            // Add this together into : https://{{HOST}}/{{WEBAPP}}/user/staff/login
            // or in our case https://sdmtlws01.sirsidynix.net/edpltest_ilsws
            StringBuilder urlString = new StringBuilder();
            urlString.append(this.baseUrl)
                    .append("/")
                    .append(this.appId)
                    .append("/")
                    .append(endpoint);
            this.uri = URI.create(urlString.toString());
            return this;
        }
        
        /**
         * Builds the command and returns a reference to it.
         *
         * @return Web Service Command reference.
         */
        public SDapiCommand build()
        {
            return new SDapiCommand(this);
        }
    }
    
    /**
     * Create the Web Service.
     * @param builder 
     */
    private SDapiCommand(Builder builder)
    {
        SDapiCommand.debug        = builder.debug;
        SDapiCommand.httpMethod   = builder.httpMethod;
        SDapiCommand.jsonBodyText = builder.bodyText;
        SDapiCommand.staffId      = builder.staffId;
        SDapiCommand.staffPassword= builder.staffPassword;
        SDapiCommand.clientId     = builder.clientId;
        SDapiCommand.uri          = builder.uri;

        // Create the httpClient.
        SDapiCommand.httpClient  = HttpClient.newBuilder()
            .version(builder.httpVersion)
            .connectTimeout(Duration.ofSeconds(builder.connectionTimeout))
            .build();
    }
    
    
    @Override
    public CommandStatus execute() 
    {
        HttpCommandStatus status = new HttpCommandStatus();
//        try
//        {
//            HttpRequest request = null;
//            HttpResponse<String> response;
//            switch(PapiCommand.httpMethod)
//            {
//                case GET -> {
//                    if (PapiCommand.useStaffMode)
//                    {
//                        request = HttpRequest.newBuilder()
//                                .GET()
//                                .uri(PapiCommand.uri)
//                                .setHeader("accept", "application/xml")
//                                .setHeader("X-PAPI-AccessToken", PapiCommand.staffPassword)
//                                .setHeader("Authorization", computeAuthorizationToken())
//                                .setHeader("PolarisDate", getPolarisDate())
//                                .build();
//                    }
//                    else
//                    {
//                        request = HttpRequest.newBuilder()
//                                .GET()
//                                .uri(PapiCommand.uri)
//                                .setHeader("accept", "application/xml")
//                                .setHeader("Authorization", computeAuthorizationToken())
//                                .setHeader("PolarisDate", getPolarisDate())
//                                .build();
//                    }
//                }
//                case POST -> {
//                    if (PapiCommand.useStaffMode)
//                    {
//                        request = HttpRequest.newBuilder()
//                                .POST(BodyPublishers.ofString(PapiCommand.xmlBodyText))
//                                .uri(PapiCommand.uri)
//                                .setHeader("accept", "application/xml")
//                                .setHeader("Content-Type", "application/xml")
//                                .setHeader("X-PAPI-AccessToken", PapiCommand.staffPassword)
//                                .setHeader("Authorization", computeAuthorizationToken())
//                                .setHeader("PolarisDate", getPolarisDate())
//                                .build();
//                    }
//                    else
//                    {
//                        request = HttpRequest.newBuilder()
//                                .POST(BodyPublishers.ofString(PapiCommand.xmlBodyText))
//                                .uri(PapiCommand.uri)
//                                .setHeader("accept", "application/xml")
//                                .setHeader("Content-Type", "application/xml")
//                                .setHeader("Authorization", computeAuthorizationToken())
//                                .setHeader("PolarisDate", getPolarisDate())
//                                .build();
//                    }
//                }
//                case PUT -> {
//                    if (PapiCommand.useStaffMode)
//                    {
//                        request = HttpRequest.newBuilder()
//                                .PUT(BodyPublishers.ofString(PapiCommand.xmlBodyText))
//                                .uri(PapiCommand.uri)
//                                .setHeader("accept", "application/xml")
//                                .setHeader("Content-Type", "application/xml")
//                                .setHeader("X-PAPI-AccessToken", PapiCommand.staffPassword)
//                                .setHeader("Authorization", computeAuthorizationToken())
//                                .setHeader("PolarisDate", getPolarisDate())
//                                .build();
//                    }
//                    else
//                    {
//                        request = HttpRequest.newBuilder()
//                                .PUT(BodyPublishers.ofString(PapiCommand.xmlBodyText))
//                                .uri(PapiCommand.uri)
//                                .setHeader("accept", "application/xml")
//                                .setHeader("Content-Type", "application/xml")
//                                .setHeader("Authorization", computeAuthorizationToken())
//                                .setHeader("PolarisDate", getPolarisDate())
//                                .build();
//                    }
//                }
//                default -> {
//                }
//            }
//            
//            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//            HttpHeaders headers = response.headers();
//            status.setStatus(response.statusCode());
//            status.setStdout(response.body());
//            // The rest of the command.
//            if (PapiCommand.debug)
//            {
//                System.out.println("HEADERS RETURNED:");
//                headers.map().forEach((k,v) -> System.out.println("  " + k + ":" + v));
//                System.out.println("   CODE RETURNED: '" + status.getStatus()+ "'.");
//                System.out.println("CONTOUT RETURNED: '" + status.getStdout()+ "'.");
//                System.out.println("CONTERR RETURNED: '" + status.getStderr()+ "'.");
//            }
//        } 
//        catch (InterruptedException ex)
//        {
//            status.setEnded(ResponseTypes.UNAVAILABLE.ordinal());
//            System.out.println(ex.getMessage());
//        } 
//        catch (IOException ex)
//        {
//            status.setEnded(ResponseTypes.FAIL.ordinal());
//            System.out.println(ex.getMessage());
//        } 
        return status;
    }
    
    @Override
    public String toString()
    {
        return SDapiCommand.uri.toASCIIString();
    }
    
}
