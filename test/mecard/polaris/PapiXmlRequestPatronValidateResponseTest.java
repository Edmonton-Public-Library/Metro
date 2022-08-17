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

import mecard.exception.PapiException;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class PapiXmlRequestPatronValidateResponseTest
{

    private final String xmlGood;
    private final String xmlEmpty;
    private final String xmlNull;
    
    public PapiXmlRequestPatronValidateResponseTest()
    {
//        <PatronValidateResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
//          <PAPIErrorCode>0</PAPIErrorCode>
//          <ErrorMessage>
//          </ErrorMessage>
//          <Barcode>21221012345678</Barcode>
//          <ValidPatron>true</ValidPatron>
//          <PatronID>2022</PatronID>
//          <PatronCodeID>1</PatronCodeID>
//          <AssignedBranchID>3</AssignedBranchID>
//          <PatronBarcode>21221012345678</PatronBarcode>
//          <AssignedBranchName>Branch A</AssignedBranchName>
//          <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>
//          <OverridePasswordUsed>false</OverridePasswordUsed>
//        </PatronValidateResult>
        xmlGood = "<PatronValidateResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
"          <PAPIErrorCode>0</PAPIErrorCode>\n" +
"          <ErrorMessage>\n" +
"          </ErrorMessage>\n" +
"          <Barcode>21221012345678</Barcode>\n" +
"          <ValidPatron>true</ValidPatron>\n" +
"          <PatronID>2022</PatronID>\n" +
"          <PatronCodeID>1</PatronCodeID>\n" +
"          <AssignedBranchID>3</AssignedBranchID>\n" +
"          <PatronBarcode>21221012345678</PatronBarcode>\n" +
"          <AssignedBranchName>Branch A</AssignedBranchName>\n" +
"          <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>\n" +
"          <OverridePasswordUsed>false</OverridePasswordUsed>\n" +
"        </PatronValidateResult>";
        xmlEmpty = "";
        xmlNull  = null;
    }

    /**
     * Test of isValidPatron method, of class PapiXmlRequestPatronValidateResponse.
     */
    @Test
    public void testIsValidPatron()
    {
        System.out.println("isValidPatron");
        PapiXmlRequestPatronValidateResponse instance = new PapiXmlRequestPatronValidateResponse(xmlGood);
        boolean expResult = true;
        boolean result = instance.isValidPatron();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBarcode method, of class PapiXmlRequestPatronValidateResponse.
     */
    @Test
    public void testGetBarcode()
    {
        System.out.println("getBarcode");
        PapiXmlRequestPatronValidateResponse instance = new PapiXmlRequestPatronValidateResponse(xmlGood);
        String expResult = "21221012345678";
        String result = instance.getBarcode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getExpiry method, of class PapiXmlRequestPatronValidateResponse.
     */
    @Test
    public void testGetExpiry()
    {
        System.out.println("getExpiry");
        PapiXmlRequestPatronValidateResponse instance = new PapiXmlRequestPatronValidateResponse(xmlGood);
        String expResult = "2022-07-30T19:38:30";
        String result = instance.getExpiry();
        assertEquals(expResult, result);
    }

    /**
     * Test of failedAuthentication method, of class PapiXmlRequestPatronValidateResponse.
     */
    @Test
    public void testFailedAuthentication()
    {
        System.out.println("failedAuthentication");
        PapiXmlRequestPatronValidateResponse instance = new PapiXmlRequestPatronValidateResponse(xmlGood);
        boolean expResult = false;
        boolean result = instance.failed();
        assertEquals(expResult, result);
        try
        {
            instance = new PapiXmlRequestPatronValidateResponse(xmlEmpty);
        }
        catch (Exception pe)
        {
            assertTrue(pe instanceof PapiException);
        }
        try
        {
            instance = new PapiXmlRequestPatronValidateResponse(xmlNull);
        }
        catch (Exception pe)
        {
            assertTrue(pe instanceof PapiException);
        }
        instance = new PapiXmlRequestPatronValidateResponse(xmlGood);
        expResult = false;
        result = instance.failed();
        assertEquals(expResult, result);
    }
    
}
