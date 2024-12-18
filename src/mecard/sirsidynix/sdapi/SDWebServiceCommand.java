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
import api.HttpCommandStatus;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import mecard.config.SDapiPropertyTypes;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import mecard.ResponseTypes;
import mecard.exception.ConfigurationException;

/**
 * 
 * @author anisbet
 */
public class SDWebServiceCommand implements Command
{

    
    public enum HttpVerb
    {
        GET,
        POST,
        PUT
    }
    
    public final static int DEFAULT_PORT = 443;
    
    private static URI uri;
    private static HttpVerb httpMethod;
    private static boolean debug;
    private static HttpClient httpClient;
    private static String staffClientId;
    private static HttpRequest.BodyPublisher jsonBodyText;
    private static String originAppId;
    private static String sessionToken;
    private HttpCommandStatus status;
    
    public static class Builder
    {
        private boolean debug;
        private HttpVerb httpMethod;
        private Properties webServiceProperties;
        private HttpRequest.BodyPublisher bodyText;
        private int connectionTimeout;
        private HttpClient.Version httpVersion;
        private String staffClientId;
        private String webApp;
        private String baseUrl;
        private int port;
        private URI uri;
        private String originAppId;
        private String sessionToken;
        
        public Builder(
                Properties wsConfigs, 
                String httpMethod
        )
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
            
            
            // Port (optional in SDapiPropertyTypes).
            try
            {
                // Using 'port' instead of value a value set in SDapiPropertyTypes
                // because it is an optional setting.
                String sPort = this.webServiceProperties.getProperty("port", "443");
                this.port = Integer.parseInt(sPort);
            }
            catch (NumberFormatException | ConfigurationException e)
            {
                System.out.println("""
                                *warn: invalid port number set in sdapi.properties 
                                value must be an integer between 0 and 65,535 (though
                                0 - 1023 are reserved). Using default HTTPS port 443.
                                   """);
                this.port = DEFAULT_PORT;
            }
            
            String version = this.webServiceProperties.getProperty(SDapiPropertyTypes.HTTP_VERSION.toString(), "2.0");
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
            
            this.staffClientId = this.webServiceProperties.getProperty(SDapiPropertyTypes.CLIENT_ID.toString());
            this.originAppId = this.webServiceProperties.getProperty(SDapiPropertyTypes.SD_ORIGINATING_APP_ID.toString());
            this.webApp = this.webServiceProperties.getProperty(SDapiPropertyTypes.WEB_APP.toString());
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
                    value must be an integer. Defaulting to 10 seconds.
                                   """);
                this.connectionTimeout = 10;
            }
            
            
            String d = this.webServiceProperties.getProperty(SDapiPropertyTypes.DEBUG.toString(), "false");
            this.debug = Boolean.parseBoolean(d);
        }
        
        /**
         * Adds body text, as JSON, to a request.
         * @param bodyText - text for the body of the request.
         * @return Builder object.
         */
        public Builder bodyText(String bodyText)
        {
            this.bodyText = HttpRequest.BodyPublishers.ofString(
                bodyText, 
                StandardCharsets.UTF_8
            );
            return this;
        }
        
        /**
         * Takes a session token from a successful login, and applies it to the
         * request. An empty string is also permitted.
         * @param sessionToken
         * @return Builder object.
         */
        public Builder sessionToken(String sessionToken)
        {
            // Session token for methods that require staff permissions.
            this.sessionToken = sessionToken;
            return this;
        }
        
        /**
         * Allows setting a web service endpoint. The end point is everything 
         * after the base URL and web app name, such as '/user/staff/login'.
         * @param endpoint Remember to include a leading '/' in your the endpoint argument.
         * @return Builder object.
         */
        public Builder endpoint(String endpoint)
        {
            // Add this together into : https://{{HOST}}/{{WEBAPP}}/user/staff/login
            // or in our case https://sdmtlws01.sirsidynix.net/edpltest_ilsws
            StringBuilder urlString = new StringBuilder();
            if (this.port != DEFAULT_PORT || this.debug == true)
            {
                urlString.append(this.baseUrl)
                        .append(":").append(this.port)
                        .append("/")
                        .append(this.webApp)
                        // remember to include the '/' in your the endpoint arg!
                        .append(endpoint);
            }
            else
            {
                urlString.append(this.baseUrl)
                        .append("/")
                        .append(this.webApp)
                        // remember to include the '/' in your the endpoint arg!
                        .append(endpoint);
            }
            this.uri = URI.create(urlString.toString());
            return this;
        }
        
        /**
         * Builds the command and returns a reference to it.
         *
         * @return Web Service Command reference.
         */
        public SDWebServiceCommand build()
        {
            return new SDWebServiceCommand(this);
        }
    }
    
    /**
     * Create the Web Service.
     * @param builder 
     */
    private SDWebServiceCommand(Builder builder)
    {
//        sd-originating-app-id: {{MyAppID}}
//        x-sirs-clientID: {{CLIENT-ID}}
//        x-sirs-sessionToken: {{ffffffff-ffff-ffff-ffff-ffffffffffff}}
//        The session sessionToken is sent in the response body of the login request.
//        Content-Type: application/vnd.sirsidynix.roa.resource.v2+json
        SDWebServiceCommand.debug         = builder.debug;
        SDWebServiceCommand.httpMethod    = builder.httpMethod;
        SDWebServiceCommand.jsonBodyText  = builder.bodyText;
        // Headers
        // x-sirs-clientId: Like 'VSD'
        SDWebServiceCommand.staffClientId = builder.staffClientId;
        // SD-Originating-App-Id: 'MeCard' used when logging requests.
        SDWebServiceCommand.originAppId   = builder.originAppId;
        // URL string with endpoint.
        SDWebServiceCommand.uri           = builder.uri;
        SDWebServiceCommand.sessionToken  = builder.sessionToken;
        // Create the httpClient.
        SDWebServiceCommand.httpClient    = HttpClient.newBuilder()
            .version(builder.httpVersion)
            .connectTimeout(Duration.ofSeconds(builder.connectionTimeout))
            .build();
    }
    
    
    @Override
    public HttpCommandStatus execute() 
    {
        
        try
        {
            HttpRequest request = null;
            HttpResponse<String> response;
            switch(SDWebServiceCommand.httpMethod)
            {
                case GET -> {
                    // Search, Get user info.
                    request = HttpRequest.newBuilder()
                        .GET()
                        .uri(SDWebServiceCommand.uri)
                        .setHeader("Accept", "*/*")
                        .setHeader("x-sirs-sessionToken", SDWebServiceCommand.sessionToken)
                        .setHeader("SD-Originating-App-Id", SDWebServiceCommand.originAppId)
                        .setHeader("x-sirs-clientID", SDWebServiceCommand.staffClientId)
                        .setHeader("Content-Type", "application/json")
                        .build();
                }
                case POST -> {
                    // Used for getting new session sessionToken.
                    if (SDWebServiceCommand.sessionToken == null || ! SDWebServiceCommand.sessionToken.isBlank())
                    {
                        request = HttpRequest.newBuilder()
                            .POST(SDWebServiceCommand.jsonBodyText)
                            .uri(SDWebServiceCommand.uri)
                            .setHeader("Accept", "*/*")
                            .setHeader("SD-Originating-App-Id", SDWebServiceCommand.originAppId)
                            .setHeader("x-sirs-clientID", SDWebServiceCommand.staffClientId)
                            .setHeader("Content-Type", "application/json")
                            .build();
                    }
                    else // Used for Create user.
                    {
                        request = HttpRequest.newBuilder()
                            .POST(SDWebServiceCommand.jsonBodyText)
                            .uri(SDWebServiceCommand.uri)
                            .setHeader("Accept", "*/*")
                            .setHeader("x-sirs-sessionToken", SDWebServiceCommand.sessionToken)
                            .setHeader("SD-Originating-App-Id", SDWebServiceCommand.originAppId)
                            .setHeader("x-sirs-clientID", SDWebServiceCommand.staffClientId)
                            .setHeader("Content-Type", "application/json")
                            .build();
                    }
                }
                case PUT -> {
                    // Used for updating accounts.
                    request = HttpRequest.newBuilder()
                        .PUT(SDWebServiceCommand.jsonBodyText)
                        .uri(SDWebServiceCommand.uri)
                        .setHeader("Accept", "*/*")
                        .setHeader("x-sirs-sessionToken", SDWebServiceCommand.sessionToken)
                        .setHeader("SD-Originating-App-Id", SDWebServiceCommand.originAppId)
                        .setHeader("x-sirs-clientID", SDWebServiceCommand.staffClientId)
                        .setHeader("Content-Type", "application/vnd.sirsidynix.roa.resource.v2+json")
                        .build();
                }
                default -> { }
            }
            
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            status = new HttpCommandStatus(response);
            
            HttpHeaders responseHeaders = response.headers();
            // Show the command results if debug.
            if (SDWebServiceCommand.debug)
            {
                System.out.println("HEADERS RETURNED:");
                responseHeaders.map().forEach((k,v) -> System.out.println("  " + k + ":" + v));
                System.out.println("   CODE RETURNED: '" + status.getStatus()+ "'.");
                System.out.println("CONTOUT RETURNED: '" + status.getStdout()+ "'.");
                System.out.println("CONTERR RETURNED: '" + status.getStderr()+ "'.");
            }
        } 
        catch (InterruptedException ex)
        {
            status = new HttpCommandStatus();
            status.setEnded(ResponseTypes.UNAVAILABLE.ordinal());
            System.out.println(ex.getMessage());
        } 
        catch (IOException ex)
        {
            status = new HttpCommandStatus();
            status.setEnded(ResponseTypes.FAIL.ordinal());
            System.out.println(ex.getMessage());
        } 
        return status;
    }
    
    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder();
        out.append(SDWebServiceCommand.uri)
            .append(" ")
            .append(SDWebServiceCommand.httpMethod)
            .append("\n");
        if (status != null)
        {
            if (status.getResponse() != null)
            {
                // Output responseHeaders, and obfuscate the security sessionToken.
                for (Map.Entry<String, List<String>> header : status.getResponse().headers().map().entrySet()) 
                {
                    String headerName = header.getKey();
                    String headerValue = String.join(",", header.getValue());

                    // Check if the header is the sessionToken and obfuscate if it is
                    if ("sessionToken".equalsIgnoreCase(headerName)) 
                    {
                        // Replace with obfuscated text or mask part of the token
                        headerValue = obfuscateToken(headerValue);
                    }
                    out.append(headerName).append(": ").append(headerValue).append("\n");
                }
                out.append("URI: ").append(status.getResponse().uri()).append("\n");
            }
            out.append("status:")
                    .append(status.getStatus())
                    .append("\n")
                    .append("body:")
                    .append(status.getStdout());
        } 
        else
        {
            out.append("<pending response>");
        }
        return out.toString();
    }
    
    private static String obfuscateToken(String token) 
    {
        // Adjust the obfuscation logic as needed, e.g., show first 10 characters only
        if (token.length() > 10) 
        {
            return token.substring(0, 10) + "**********";
        } 
        else 
        {
            return "**********"; // in case token is too short
        }
    }
    
}
