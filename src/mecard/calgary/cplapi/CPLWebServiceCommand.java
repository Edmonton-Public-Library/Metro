/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025 Edmonton Public Library
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
package mecard.calgary.cplapi;

import api.Command;
import api.CommandStatus;
import api.HttpCommandStatus;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;
import mecard.config.CPLapiPropertyTypes;
import mecard.exception.ConfigurationException;

/**
 *
 * @author anisbet
 */
public class CPLWebServiceCommand implements Command
{
    public enum HttpVerb
    {
        GET,
        POST
    }
    
    public final static int DEFAULT_PORT = 443;
    
    private static URI uri;
    private static CPLWebServiceCommand.HttpVerb httpMethod;
    private static boolean debug;
    private static HttpClient httpClient;
    private static HttpRequest.BodyPublisher jsonBodyText;
    private static String sessionToken;
    private HttpCommandStatus status;
    
    public static class Builder
    {
        private boolean debug;
        private CPLWebServiceCommand.HttpVerb httpMethod;
        private Properties webServiceProperties;
        private HttpRequest.BodyPublisher bodyText;
        private int connectionTimeout;
        private HttpClient.Version httpVersion;
        private String baseUrl;
        private int port;
        private URI uri;
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
                    this.httpMethod = CPLWebServiceCommand.HttpVerb.GET;
                    break;
                case "POST":
                    this.httpMethod = CPLWebServiceCommand.HttpVerb.POST;
                    break;
                default:
                    throw new UnsupportedOperationException(
                    "**error, " + httpMethod + " not supported.");
            }
            
            
            // Port (optional in CPLapiPropertyTypes).
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
            
            String version = this.webServiceProperties.getProperty(CPLapiPropertyTypes.HTTP_VERSION.toString(), "2.0");
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
            
            this.baseUrl = this.webServiceProperties.getProperty(CPLapiPropertyTypes.BASE_URL.toString());
            
            // Connection timeout
            String cTimeout = this.webServiceProperties.getProperty(CPLapiPropertyTypes.CONNECTION_TIMEOUT.toString());
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
            
            
            String d = this.webServiceProperties.getProperty(CPLapiPropertyTypes.DEBUG.toString(), "false");
            this.debug = Boolean.parseBoolean(d);
        }
        
        /**
         * Sets the debug property of the command.
         * @param debug
         * @return 
         */
        public Builder setDebug(boolean debug)
        {
            this.debug = debug;
            return this;
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
            if (this.port != DEFAULT_PORT)
            {
                urlString.append(this.baseUrl)
                        .append(":").append(this.port)
                        .append("/")
                        .append(endpoint);
            }
            else
            {
                urlString.append(this.baseUrl)
                        .append("/")
                        // remember to include the leading '/' in your the endpoint arg!
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
        public CPLWebServiceCommand build()
        {
            return new CPLWebServiceCommand(this);
        }
    }
    
    /**
     * Create the Web Service.
     * @param builder 
     */
    private CPLWebServiceCommand(CPLWebServiceCommand.Builder builder)
    {

        // Content-Type: application/json
        // Accept: application/json
        CPLWebServiceCommand.debug         = builder.debug;
        CPLWebServiceCommand.httpMethod    = builder.httpMethod;
        CPLWebServiceCommand.jsonBodyText  = builder.bodyText;
        // Headers
        
        // URL string with endpoint.
        CPLWebServiceCommand.uri           = builder.uri;
        CPLWebServiceCommand.sessionToken  = builder.sessionToken;
        // Create the httpClient.
        CPLWebServiceCommand.httpClient    = HttpClient.newBuilder()
            .version(builder.httpVersion)
            .connectTimeout(Duration.ofSeconds(builder.connectionTimeout))
            .build();
        if (CPLWebServiceCommand.debug)
            System.out.println("SDWebService toString: " + CPLWebServiceCommand.httpClient.toString());
    }
    
    @Override
    public CommandStatus execute()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
