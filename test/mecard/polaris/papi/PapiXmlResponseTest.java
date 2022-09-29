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
package mecard.polaris.papi;

import mecard.polaris.papi.PapiXmlResponse;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com> 
 */
public class PapiXmlResponseTest {

    private final String apiVersionXml;
    private final String failedXml;
    
    public PapiXmlResponseTest() 
    {
        this.failedXml = "<PatronBasicDataGetResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><PAPIErrorCode>-3000</PAPIErrorCode><ErrorMessage>Error retrieving patron ID. </ErrorMessage><PatronBasicData i:nil=\"true\" /></PatronBasicDataGetResult>";
        this.apiVersionXml = "<ApiResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><PAPIErrorCode>0</PAPIErrorCode><ErrorMessage></ErrorMessage><Version>7.0.9502.0</Version><Major>7</Major><Minor>0</Minor><Build>9502</Build><Revision>0</Revision></ApiResult>";
    }
    

    /**
     * Test of toString method, of class PapiXmlMessages.
     */
    @Test
    public void testToString() {
        System.out.println("==toString==");
        PapiXmlResponse instance = new PapiXmlResponse(this.apiVersionXml);
        String expResult = "0";
        String result = instance.toString();
        assertEquals(expResult, result);
        
        instance = new PapiXmlResponse(this.failedXml);
        int iResult = -3000;
        int iError = instance.errorCode();
        System.out.println(">>>" + instance.toString());
        assertEquals(expResult, result);
    }
    
}
