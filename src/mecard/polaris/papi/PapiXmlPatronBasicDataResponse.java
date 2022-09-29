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

/**
 * Basic data of the customer (patron in PAPI).
 * 
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class PapiXmlPatronBasicDataResponse extends PapiXmlResponse
{

    /**
     * @return the barcode
     */
    public String getBarcode() 
    {
        return barcode;
    }

    /**
     * @return the nameFirst
     */
    public String getNameFirst() 
    {
        return nameFirst;
    }

    /**
     * @return the nameLast
     */
    public String getNameLast() 
    {
        return nameLast;
    }

    /**
     * @return the nameMiddle
     */
    public String getNameMiddle() 
    {
        return nameMiddle;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() 
    {
        return phoneNumber;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() 
    {
        return emailAddress;
    }

    /**
     * @return the streetOne
     */
    public String getStreetOne() 
    {
        return streetOne;
    }

    /**
     * @return the city
     */
    public String getCity() 
    {
        return city;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() 
    {
        return postalCode;
    }

    /**
     * @return the country
     */
    public String getCountry() 
    {
        return country;
    }

    /**
     * @return the birthDate
     */
    public String getBirthDate() 
    {
        return birthDate;
    }

    /**
     * @return the expiryDate
     */
    public String getExpiryDate() 
    {
        return expiryDate;
    }

    private String barcode;
    private String nameFirst;
    private String nameLast;
    private String nameMiddle;
    private String phoneNumber;
    private String emailAddress;
    private String streetOne;
    private String city;
    private String postalCode;
    private String country;
    private String birthDate;
    private String expiryDate;
    public PapiXmlPatronBasicDataResponse(String xml)
    {
        super(xml);
        //  <PatronBasicDataGetResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
        //  <PAPIErrorCode>0</PAPIErrorCode>
        //  <ErrorMessage>
        //  </ErrorMessage>
        //  <PatronBasicData>
        //    <Barcode>21221012345678</Barcode>
        //    <NameFirst>Billy</NameFirst>
        //    <NameLast>Balzac</NameLast>
        //    <NameMiddle i:nil="true" />
        //    <PhoneNumber>555-1212</PhoneNumber>
        //    <EmailAddress>dude@hotmail.com</EmailAddress>
        //    <BirthDate i:nil="true" />
        //    <PatronAddresses>
        //      <PatronAddress>
        //        <AddressID>339329</AddressID>
        //        <FreeTextLabel>Home</FreeTextLabel>
        //        <StreetOne>11811 74 Ave.</StreetOne>
        //        <City>Edmonton</City>
        //        <State>AB</State>
        //        <PostalCode>90210</PostalCode>
        //        <Country>USA</Country>
        //        <AddressTypeID>2</AddressTypeID>
        //      </PatronAddress>
        //    </PatronAddresses>
        //    <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>
        //    <PatronNotes i:nil="true" />
        //    <PatronSystemBlocks />
        //  </PatronBasicData>
        //</PatronBasicDataGetResult>
        if (this.failed()) return;
        this.barcode      = root.getElementsByTagName("Barcode").item(0).getTextContent();
        this.nameFirst    = root.getElementsByTagName("NameFirst").item(0).getTextContent();
        this.nameLast     = root.getElementsByTagName("NameLast").item(0).getTextContent();
        this.nameMiddle   = root.getElementsByTagName("NameMiddle").item(0).getTextContent();
        this.phoneNumber  = root.getElementsByTagName("PhoneNumber").item(0).getTextContent();
        this.emailAddress = root.getElementsByTagName("EmailAddress").item(0).getTextContent();
        this.streetOne    = root.getElementsByTagName("StreetOne").item(0).getTextContent();
        this.city         = root.getElementsByTagName("City").item(0).getTextContent();
        this.postalCode   = root.getElementsByTagName("PostalCode").item(0).getTextContent();
        this.country      = root.getElementsByTagName("Country").item(0).getTextContent();
        this.birthDate    = root.getElementsByTagName("BirthDate").item(0).getTextContent();
        this.expiryDate   = root.getElementsByTagName("ExpirationDate").item(0).getTextContent();
    }
}
