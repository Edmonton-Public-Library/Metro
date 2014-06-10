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
package api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import mecard.ResponseTypes;
import mecard.exception.ConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

/**
 * This class manages the basic sending and receiving of messages to a web 
 * service.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class WebServiceCommand implements Command
{
    private final URI uri;
    private final String httpVerb;
    private final HttpVersion version;
    private final int connectionTimeout;
    private final int socketTimeout;
    private final ContentType contentType;
    private final String postString;
    private final boolean debug;
    
    public static class Builder
    {
        // optional
        private String uri;
        private String verb;
        private ContentType contentType;
        private String postString;
        private HttpVersion version;
        private int connectionTimeout;
        private int socketTimeout;
        private boolean debug;

        /**
         * Builds settings into the web service call.
         * Includes several default settings:
         * <ul>
         * <li>content type: Application XML</li>
         * <li>HTTP verb: GET</li>
         * <li>HTTP version: 1.1, but "0.9", and "1.0" also permitted.</li>
         * <li>Connection timeout: 3000 ms.</li>
         * <li>Socket timeout: 3000 ms.</li>
         * </ul>
         */
        public Builder()
        { 
            this.contentType       = ContentType.APPLICATION_XML; // default content type, also try XML.
            this.verb              = "GET";
            this.version           = HttpVersion.HTTP_1_1;
            this.connectionTimeout = 5000;
            this.socketTimeout     = 5000;
            this.postString        = "";
            this.debug             = false;
        }

        /**
         * Builds the command and returns a reference to it.
         *
         * @return APICommand reference.
         */
        public WebServiceCommand build()
        {
            return new WebServiceCommand(this);
        }

        /**
         * Sets the URL of the PAPI command.
         * @param uri
         * @return builder object.
         */
        public Builder setURI(String uri)
        {
            this.uri = uri;
            return this;
        }
        
        // TODO add special header methods.
        
        
        public Builder setContentType(String content, ContentType type)
        {
            this.postString  = content;
            this.contentType = type;
            return this;
        }
        
        public Builder debug()
        {
            this.debug = true;
            return this;
        }
        
        public Builder setVersion(String version)
        {
            switch (version)
            {
                case "0.9":
                    this.version = HttpVersion.HTTP_0_9;
                    break;
                case "1.0":
                    this.version = HttpVersion.HTTP_1_0;
                    break;
                case "1.1":
                    this.version = HttpVersion.HTTP_1_1;
                    break;
                default:
                    this.version = HttpVersion.HTTP_1_1;
                    break;
            }
            return this;
        }
        
        public Builder setHTTPVerb(String verb)
        {
            this.verb = verb;
            return this;
        }
    }
    
    /**
     * Create the Web Service.
     * @param builder 
     */
    private WebServiceCommand(Builder builder)
    {
        this.debug = builder.debug;
        try
        {
            this.uri = new URI(builder.uri);
        } 
        catch (URISyntaxException ex)
        {
            throw new ConfigurationException(WebServiceCommand.class.getName() + 
                    "The URL is malformed. Please check "
                    + "your configuration for errors.");
        }
        this.httpVerb          = builder.verb;
        this.postString        = builder.postString;
        this.contentType       = builder.contentType;
        this.version           = builder.version;
        this.connectionTimeout = builder.connectionTimeout;
        this.socketTimeout     = builder.socketTimeout;
    }

    @Override
    public CommandStatus execute()
    {
        CommandStatus status = new CommandStatus();
        try
        {
            switch (this.httpVerb)
            {
                case "POST":
                    status = Request.Post(this.uri)
                            .version(this.version)
                            .useExpectContinue()
                            .connectTimeout(this.connectionTimeout)
                            .socketTimeout(this.socketTimeout)
                            .bodyString(this.postString, this.contentType)
                            .execute()
                            .handleResponse(new ResponseHandler<HTTPCommandStatus>()
                            {
                                @Override
                                public HTTPCommandStatus handleResponse(HttpResponse hr)
                                        throws ClientProtocolException, IOException
                                {
                                    HTTPCommandStatus cs = new HTTPCommandStatus();
                                    StatusLine statusLine = hr.getStatusLine();
                                    HttpEntity entity     = hr.getEntity();
                                    cs.setHttpCode(statusLine.getStatusCode());
                                    cs.setEntity(entity);
                                    cs.setStderr(statusLine.getReasonPhrase());
                                    return cs;
                                }
                            });
                case "GET":
                default:
                    status = Request.Get(this.uri)
                            .version(this.version)
                            .connectTimeout(this.connectionTimeout)
                            .socketTimeout(this.socketTimeout)
                            .execute()
                            .handleResponse(new ResponseHandler<HTTPCommandStatus>()
                            {
                                @Override
                                public HTTPCommandStatus handleResponse(HttpResponse hr)
                                        throws ClientProtocolException, IOException
                                {
                                    HTTPCommandStatus cs = new HTTPCommandStatus();
                                    StatusLine statusLine = hr.getStatusLine();
                                    HttpEntity entity     = hr.getEntity();
                                    cs.setHttpCode(statusLine.getStatusCode());
                                    cs.setEntity(entity);
                                    cs.setStderr(statusLine.getReasonPhrase());
                                    return cs;
                                }
                            });
                    break;
            }
            if (this.debug)
            {
                System.out.println("   CODE RETURNED: '" + status.getStatus()+ "'.");
                System.out.println("CONTOUT RETURNED: '" + status.getStdout()+ "'.");
                System.out.println("CONTERR RETURNED: '" + status.getStderr()+ "'.");
            }
        } 
        catch (ClientProtocolException ex)
        {
            status.setEnded(ResponseTypes.CONFIG_ERROR.ordinal());
            System.out.println(ex.getMessage());
        } 
        catch (IOException ex)
        {
            status.setEnded(ResponseTypes.FAIL.ordinal());
            System.out.println(ex.getMessage());
        } 
        return status;
    }
}
