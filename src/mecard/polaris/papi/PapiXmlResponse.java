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

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import org.xml.sax.SAXException;
import mecard.exception.PapiException;

/**
 * Base class for parsing XML responses from PAPI. While some web service calls
 * can use JSON, Patron Update Data, Patron Registration Create, and Patron 
 * Registration Update must use XML.
 * 
 * Extend this class for specific messages. This class will parse the XML 
 * passed in the constructor, and return any status codes (as integers) and
 * any error messages (which will be empty strings if there was none).
 * 
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class PapiXmlResponse
{
    protected Element root;
    protected String papiErrorCode;
    protected String errorMessage;
    protected boolean failedResponse;
    
    /**
     * Parses string of XML and provides accessors for status and any 
     * error message.
     * 
     * @param xml String XML response message. 
     */
    public PapiXmlResponse(String xml)
    {
        try 
        {
            /*
            Successful response.
            --------------------
            <PatronRegistrationCreateResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
            <PAPIErrorCode>0</PAPIErrorCode>
            <ErrorMessage>
            </ErrorMessage>
            <Barcode>21221012345678</Barcode>
            <PatronID>2022</PatronID>
            <StatisticalClassID>0</StatisticalClassID>
            </PatronRegistrationCreateResult>
            
            Unsuccessful response.
            ----------------------
            <PatronRegistrationCreateResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
            <PAPIErrorCode>-3502</PAPIErrorCode>
            <ErrorMessage>Invalid patron branch</ErrorMessage>
            <Barcode>21221012345678</Barcode>
            <PatronID>0</PatronID>
            <StatisticalClassID>0</StatisticalClassID>
            </PatronRegistrationCreateResult>
            
            */
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            xmlStringBuilder.append(xml);
            ByteArrayInputStream input;
            try
            {
                input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
                Document document = builder.parse(input);
                this.root = document.getDocumentElement();
                this.papiErrorCode = root.getElementsByTagName("PAPIErrorCode").item(0).getTextContent();
                // A successful empty message can contain a single new line.
                this.errorMessage  = root.getElementsByTagName("ErrorMessage").item(0).getTextContent().stripTrailing();
                this.failedResponse = false;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.failedResponse = true;
                this.papiErrorCode  = "-1";
                this.errorMessage   = ex.getMessage();
                System.out.println(PapiXmlResponse.class.getName()
                        + ex.getMessage());
            }
            catch (IOException | SAXException ex)
            {
                this.failedResponse = true;
                this.papiErrorCode  = "-1";
                this.errorMessage   = ex.getMessage();
                System.out.println(PapiXmlResponse.class.getName()
                        + ex.getMessage());
            }
            catch (NullPointerException ex)
            {
                this.failedResponse = true;
                this.papiErrorCode  = "-1";
                this.errorMessage   = ex.getMessage();
                System.out.println(PapiXmlResponse.class.getName()
                        + ex.getMessage());
                // This can happen if the xml is a request rather than response.
                System.out.println("*warn, didn't find the expected XML element. Was this a request instead of response?");
            }
        } 
        catch (ParserConfigurationException ex) 
        {
            this.failedResponse = true;
            this.papiErrorCode  = "-1";
            this.errorMessage   = ex.getMessage();
            System.out.println(PapiXmlResponse.class.getName()
                    + ex.getMessage());
        }
        finally
        {
            if (this.failedResponse == true)
            {
                this.papiErrorCode  = "-1";
                throw new PapiException(" PAPIResult: " +this.papiErrorCode + " =>'" + this.errorMessage + "'\n"
                        + "  A 'null' result usually means a timeout to the web\n"
                        + "  service. Either the service is busy, down, or there\n"
                        + "  was an authentication issue: ");
            }
        }
    }
    
    /**
     * Returns the PAPI Error Code sent back in all PAPI responses. This is not
     * the HTTP status code, but the web service error code.
     * 
     * @return Integer value of the PAPI Error code.
     */
    public int errorCode()
    {
        try 
        {
            return Integer.parseInt(this.papiErrorCode);
        }
        catch (NumberFormatException e)
        {
            System.out.println("api.PapiXmlMessage.errorCode()\n"
                    + "Expected a signed integer.\n" 
                    + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Generally in PAPI, error codes are negative integers. No error can be '0'
     * or in update calls, can be the number of rows effected or returned.
     * 
     * @return true if there were no errors reported in the response and false
     * otherwise.
     */
    public boolean succeeded()
    {
        return this.errorCode() >= 0;
    }
    
    /**
     * Provides the error message if there was one.
     * 
     * @return String of the error message or an empty string.
     */
    public String errorMessage()
    {
        if (this.errorCode() >= 0)
        {
            return "";
        }
        switch (this.errorCode())
        {
            case -1:
                return this.errorMessage;
            case -2:
                return "Multiple errors. See returned rowset for list of errors.";
            case -3:
                return "PARTIAL FAILURE - Multiple errors (but some items succeeded). See returned rowset for list of errors.";
            case -4:
                return "FAILURE - ERMS error";
            case -5:
                return "Failure. Database error occurred";
            case -6:
                return "Failure. Invalid parameter";
            case -9:
                return "SQL timeout";
            case -10:
                return "Failure. ID does not exist";
            case -11:
                return "Validation_Errors";
            case -201:
                return "Failed to insert entry in addresses table";
            case -221:
                return "Failed to insert entry in PostalCodes table";
            case -222:
                return "Invalid postal code length";
            case -223:
                return "Invalid postal code format"; // if you get this turn off postal code formatting in MeCardCustomerToPapi.java.
            case -501:
                return "Patron personal information change is not allowed: " + this.errorMessage;
            case -3000:
                return "Patron does not exist";
            case -3001:
//                return "Failed to insert entry in Patrons table";      // This is from the manual
                return "Unable to authenticate the patron credentials."; // This is what gets returned.
            case -3400:
                return "Failed to insert entry in Patronaddresses table";
            case -3500:
                return "Country code does not exist";
            case -3501:
                return "Patron branch is not defined";
            case -3502:
                return "Patron branch is not a valid branch";
            case -3503:
                return "Last name is not defined";
            case -3504:
                return "First name is not defined";
            case -3505:
                return "Barcode is already used for another patron";
            case -3506:
                return "Transaction branch is not defined";
            case -3507:
                return "Transaction user is not defined";
            case -3508:
                return "Transaction workstation is not defined";
            case -3509:
                return "Passwords do not match";
            case -3510:
                return "Postal code problems - mismatch city, state, county";
            case -3511:
                return "Postal code problems - mismatch city, state";
            case -3512:
                return "Postal code problems - mismatch city, county";
            case -3513:
                return "Postal code problems - mismatch state, county";
            case -3514:
                return "Postal code problems - mismatch county";
            case -3515:
                return "Postal code problems - mismatch state";
            case -3516:
                return "Postal code problems - mismatch city";
            case -3517:
                return "Postal code problems - postal code not found";
            case -3518:
                return "Invalid Email address";
            case -3519:
                return "Invalid DeliveryMethod Value (No Address for Patron)";
            case -3520:
                return "Invalid DeliveryMethod Value (No Email Address for Patron)";
            case -3521:
                return "Invalid DeliveryMethod Value (No PhoneVoice1 for Patron)";
            case -3522:
                return "Invalid DeliveryMethod Value (No PhoneVoice2 for Patron)";
            case -3523:
                return "Invalid DeliveryMethod Value (No PhoneVoice3 for Patron)";
            case -3524:
                return "Invalid DeliveryMethod Value (No PhoneFax for Patron)";
            case -3525:
                return "Invalid DeliveryMethod Value";
            case -3526:
                return "Invalid EmailFormat Value";
            case -3527:
                return "Invalid ReadingList Value";
            case -3528:
                return "Duplicate name";
            case -3529:
                return "Duplicate username";
            case -3530:
                return "Failed to insert entry in Patron Registration table";
            case -3532:
                return "Invalid PhoneVoice1 value";
            case -3533:
                return "Invalid password format";
            case -3534:
                return "Invalid Password length";
            case -3535:
                return "Patron password change is not allowed";
            case -3536:
                return "Invalid gender value";
            case -3600:
                return "Charge transaction does not exist";
            case -3601:
                return "Charge transaction for this patron does not exist";
            case -3602:
                return "Payment method for payment is invalid";
            case -3603:
                return "Invalid amount is being paid";
            case -3604:
                return "Invalid transaction type being paid";
            case -3605:
                return "General patron account database error";
            case -3606:
                return "Payment transaction does not exist";
            case -3607:
                return "Payment transaction for this patron does not exist";
            case -3608:
                return "Payment transaction cannot be voided because another action taken on payment";
            case -3610:
                return "Payment amount is more than the sum of the charges";
            case -3612:
                return "Invalid PatronCodeID";
            case -3613:
                return "Invalid PhoneVoice2";
            case -3614:
                return "Invalid PhoneVoice3";
            case -3615:
                return "Invalid Alt Email Address";
            case -3616:
                return "Invalid TXTPhoneNumber";
            case -3617:
                return "Invalid PhoneCarrier";
            case -3619:
                return "Invalid DeliveryMethod No Phone";
            case -3620:
                return "Invalid Email Address for EReceipt";
            case -3621:
                return "Patron Is Secure";
            case -4000:
                return "Invalid application ID supplied";
            case -4001:
                return "Invalid patron ID supplied";
            case -4002:
                return "Invalid workstation ID supplied";
            case -4003:
                return "Invalid request ID supplied";
            case -4004:
                return "Invalid requesting org ID supplied";
            case -4005:
                return "Invalid patron barcode";
            case -4006:
                return "Invalid bibliographic record ID supplied";
            case -4007:
                return "Invalid pickup org ID supplied";
            case -4016:
                return "Cannot change pickup branch for request in statusID";
            case -4100:
                return "Invalid request GUID supplied";
            case -4101:
                return "Invalid txn group qualifier supplied";
            case -4102:
                return "Invalid txn qualifier supplied";
            case -4103:
                return "Invalid answer supplied";
            case -4104:
                return "Invalid state supplied";
            case -4201:
                return "Invalid request ID supplied";
            case -4202:
                return "Invalid current org ID supplied";
            case -4203:
                return "Cancel prevented for hold requests with status of Held";
            case -4204:
                return "Cancel prevented for hold request with status of Unclaimed";
            case -4205:
                return "Cancel prevented for hold request with a status of Canceled";
            case -4206:
                return "Cancel prevented for hold request with a status of Expired";
            case -4207:
                return "Cancel prevented for hold request with a status of Out to Patron";
            case -4208:
                return "Cancel prevented for hold request with a status of Shipped";
            case -4300:
                return "No requests available to cancel";
            case -4400:
                return "Invalid Application date supplied";
            case -4401:
                return "Application date must be greater than or equal to today's date";
            case -4402:
                return "Application date must be earlier than 2 years from today";
            case -4403:
                return "Invalid pickup branch assigned to hold request";
            case -4404:
                return "Error occurred loading SA 'days to expire'";
            case -4405:
                return "Request must have a status of Active, Inactive or Pending";
            case -4406:
                return "No requests available to suspend";
            case -4407:
                return "Request status invalid for this process";
            case -4408:
                return "Invalid request status change requested";
            case -4409:
                return "Invalid hold user not supplied reason";
            case -4410:
                return "This is the only item available for hold";
            case -4411:
                return "No other items at other branches are available to fill this hold";
            case -5000:
                return "Invalid OrganizationID specified";
            case -8000:
                return "Invalid PolarisUserID specified";
            case -8001:
                return "Polaris user is not permitted";
            case -8002:
                return "StaffUser_NotSupplied";
            case -8003:
                return "StaffUser_NotFound";
            case -8004:
                return "StaffUser_Account_Disabled";
            case -9000:
                return "Invalid WorkstationID specified";
            case -11000:
                return "Supplied recordSetID is not of type patron";
            case -11001:
                return "RecordSetID does not exist";
            default:
                return "Unknown error: '" + this.papiErrorCode + "'";
        }
    }
    
    @Override
    public String toString()
    {
        return this.papiErrorCode;
    }
    
    public boolean failed()
    {
        return this.failedResponse;
    }
}
