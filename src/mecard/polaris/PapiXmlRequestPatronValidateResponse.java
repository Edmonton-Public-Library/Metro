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
 * The patron validate result message can be used to test if the patron exists
 * in the Polaris database without returning all the customer's data.
 * 
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class PapiXmlRequestPatronValidateResponse extends PapiXmlResponse
{

    private final boolean validPatron;
    private final String barcode;
    private final String expirationDate;

    public PapiXmlRequestPatronValidateResponse(String xml)
    {
        super(xml);
        //<PatronValidateResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
        //  <PAPIErrorCode>0</PAPIErrorCode>
        //  <ErrorMessage>
        //  </ErrorMessage>
        //  <Barcode>21221012345678</Barcode>
        //  <ValidPatron>true</ValidPatron>
        //  <PatronID>2022</PatronID>
        //  <PatronCodeID>1</PatronCodeID>
        //  <AssignedBranchID>3</AssignedBranchID>
        //  <PatronBarcode>21221012345678</PatronBarcode>
        //  <AssignedBranchName>Branch A</AssignedBranchName>
        //  <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>
        //  <OverridePasswordUsed>false</OverridePasswordUsed>
        //</PatronValidateResult>
        if (this.failed())
        {
            this.expirationDate = "0000-00-00T00:00:00.000z";
            this.barcode        = "-1";
            this.validPatron    = false;
            return;
        }
        this.expirationDate     = root.getElementsByTagName("ExpirationDate").item(0).getTextContent();
        this.barcode            = root.getElementsByTagName("Barcode").item(0).getTextContent();
        this.validPatron        = Boolean.valueOf(root.getElementsByTagName("ValidPatron").item(0).getTextContent());
    }
    
    /**
     * Returns true if the patron is a valid customer, and false otherwise.
     * @return Boolean true or false.
     */
    public boolean isValidPatron()
    {
        return this.validPatron;
    }
    
    /**
     * Gets the patron / customer's bar code, user ID, library card number,
     * what-have-you.
     * 
     * @return String of the customer's library card.
     */
    public String getBarcode()
    {
        return this.barcode;
    }
    
    
    /**
     * Gets the customer's account expiry date (and time). This doesn't seem 
     * to be a Polaris date, but rather looks like a SQL timestamp. 
     * Example: 2022-07-30T19:38:30
     * 
     * @return 
     */
    public String getExpiry()
    {
        return this.expirationDate;
    }
}
