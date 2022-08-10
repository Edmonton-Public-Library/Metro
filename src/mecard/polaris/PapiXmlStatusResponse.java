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

/**
 * Parses the https://dewey.polarislibrary.com/PAPIService/REST/public/v1/1033/100/1/api
 * web service call, and provides accessors to valuable information about the version
 * of the API being used.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class PapiXmlStatusResponse extends PapiXmlResponse
{
    // Release numbers.
    private int major      = 0;
    private int minor      = 0;
    private String version = "";

    public PapiXmlStatusResponse(String xml) 
    {
        super(xml);
        //    "Version": "7.0.9502.0",
        //    "Major": 7,
        //    "Minor": 0,
        //    "Build": 9502,
        //    "Revision": 0
        if (this.failedResponse) return;
        this.version = root.getElementsByTagName("Version").item(0).getTextContent();
        try
        {
            this.major    = Integer.parseInt(root.getElementsByTagName("Major").item(0).getTextContent());
            this.minor    = Integer.parseInt(root.getElementsByTagName("Minor").item(0).getTextContent());
        }
        catch (NumberFormatException e)
        {
            System.out.println("api.PapiXmlStatusMessage.<init>().\n"
                    + "A returned value is invalid, has the API changed\n"
                    + "or is this the correct response for the job?");
        }
    }
    
    /**
     * Provides the major build number for the PAPI web service, which can be
     * considered the minimum compliance version.
     * 
     * @param expectedVersion String value of the expected API version number 
     * as read from the papi properties file, field: 'minimum-web-service-compliance-version'.
     * @return true if the API version returned from the web service is greater
     * than or equal to the expected version read from the papi properties file,
     * and false otherwise.
     */
    public boolean meetsMinimumApiVersion(String expectedVersion)
    {
        double configVersion = Double.parseDouble(expectedVersion);
        
        return Math.floor(this.getCourseGrainedVersion()) == Math.floor(configVersion);
    }
    
    /**
     * Gets the version number of the PAPI web service as a string.
     * @return String of the PAPI web service being called.
     */
    public String getPAPIVersion()
    {
        return this.toString();
    }
    
    /**
     * Returns the major 'dot' minor version of the PAPI web service being used.
     * 
     * @return Double version of the major and minor numbers conveniently for 
     * comparing versions quickly.
     */
    public double getCourseGrainedVersion()
    {
        StringBuilder courseVersion = new StringBuilder();
        courseVersion.append(this.major);
        courseVersion.append(".");
        courseVersion.append(this.minor);
        return Double.parseDouble(courseVersion.toString());
    }
    
    @Override
    public String toString()
    {
        return this.version;
    }
}
