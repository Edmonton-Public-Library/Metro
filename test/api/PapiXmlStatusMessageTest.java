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
package api;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class PapiXmlStatusMessageTest {

    private final String apiVersionXml;
    
    public PapiXmlStatusMessageTest() 
    {
        this.apiVersionXml = "<ApiResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><PAPIErrorCode>0</PAPIErrorCode><ErrorMessage></ErrorMessage><Version>7.0.9502.0</Version><Major>7</Major><Minor>0</Minor><Build>9502</Build><Revision>0</Revision></ApiResult>";
    }

    /**
     * Test of getMinimumComplianceVersion method, of class PapiXmlStatusMessage.
     */
    @Test
    public void testGetMinimumComplianceVersion() {
        System.out.println("getMinimumComplianceVersion");
        PapiXmlStatusMessage instance = new PapiXmlStatusMessage(apiVersionXml);
        int expResult = 7;
        int result = instance.getMinimumComplianceVersion();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPAPIVersion method, of class PapiXmlStatusMessage.
     */
    @Test
    public void testGetPAPIVersion() {
        System.out.println("getPAPIVersion");
        PapiXmlStatusMessage instance = new PapiXmlStatusMessage(apiVersionXml);
        String expResult = "7.0.9502.0";
        String result = instance.getPAPIVersion();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCourseGrainedVersion method, of class PapiXmlStatusMessage.
     */
    @Test
    public void testGetCourseGrainedVersion() {
        System.out.println("getCourseGrainedVersion");
        PapiXmlStatusMessage instance = new PapiXmlStatusMessage(apiVersionXml);
        double expResult = 7.0;
        double result = instance.getCourseGrainedVersion();
        assertEquals(expResult, result, 0);
        assertTrue(result >= expResult);
    }

    /**
     * Test of toString method, of class PapiXmlStatusMessage.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        PapiXmlStatusMessage instance = new PapiXmlStatusMessage(apiVersionXml);
        String expResult = "7.0.9502.0";
        String result = instance.getPAPIVersion();
        assertEquals(expResult, result);
    }
    
}
