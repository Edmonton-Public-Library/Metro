/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022 - 2025  Edmonton Public Library
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
package mecard.polaris.papi;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import mecard.customer.MeCardDataToNativeData;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import mecard.config.ConfigFileTypes;
import mecard.config.PapiPropertyTypes;
import mecard.config.PropertyReader;
import mecard.util.VersionComparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Converts MeCard customer data into XML suitable for a POST method body.
 * In other words, this class serializes MeCard data into XML for PAPI
 * For PAPI XML, this table represents a complete table of customer data, that 
 * can be added to a REST POST request to create customer.
 * 
 * This class uses a factory method because there are 4 variations of tables.
 * There are create tables which contain a complete set of data about the customer,
 * and which come in 1 of 2 flavors: XML and JSON. 
 * 
 * Note: while working on PAPI version 7.0+ the specification only supports 
 * customer updates and creates in XML format. Other requests can use JSON 
 * payloads.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class MeCardDataToPapiData implements MeCardDataToNativeData
{

    private final boolean debug;
    public enum QueryType
    {
        CREATE,
        UPDATE;
    }
    
    private final QueryType queryType;
    private final EnumMap<PapiElementOrder, String> columns;
    public static PapiElementOrder TABLE_NAME;
    
    /**
     * Specifies the formatting {@link PAPIFormattedTable.ContentType} and defaults
     * to a create user query.
     * @param type
     * @return create query formatted as per argument.
     */
    public static MeCardDataToPapiData getInstanceOf(QueryType type)
    {
        return new MeCardDataToPapiData(type, false);
    }
    
    /**
     * Specifies the formatting {@link PAPIFormattedTable.ContentType} and defaults
     * to a create user query.
     * @param type
     * @param debug
     * @return create query formatted as per argument.
     */
    public static MeCardDataToPapiData getInstanceOf(QueryType type, boolean debug)
    {
        return new MeCardDataToPapiData(type, debug);
    }
    
    private MeCardDataToPapiData(QueryType qType, boolean debug)
    {
        this.columns    = new EnumMap<>(PapiElementOrder.class);
        this.queryType  = qType;
        this.debug      = debug;
        switch (this.queryType)
        {
            case CREATE:
                MeCardDataToPapiData.TABLE_NAME = PapiElementOrder.TAG_PATRON_REGISTRATION_CREATE;
                break;
            case UPDATE:
                MeCardDataToPapiData.TABLE_NAME = PapiElementOrder.TAG_PATRON_UPDATE_DATA;
                break;
            default:
                throw new UnsupportedOperationException(
                    "The query type " + this.queryType + " has not been defined\n"
                    + "in " + MeCardDataToPapiData.class.getName()
                );
        }
    }
    
    @Override
    public String getData()
    {
        switch (this.queryType)
        {
            case CREATE -> {
                return this.getCreateXml();
            }
            case UPDATE -> {
                return this.getUpdateXml();
            }
            default -> throw new UnsupportedOperationException(
                    "The query type " + this.queryType + " has not been defined\n"
                    + "in " + MeCardDataToPapiData.class.getName()
                );
        }
    }
    
    /**
     * Creates a well formed Patron Registration Create request.
     * 
     * @return String of well formed XML customer data with tags in order.
     */
    protected String getCreateXml()
    {
        // <?xml version="1.0" encoding="UTF-8"?>
        // <PatronRegistrationCreateData>
        //	<LogonBranchID>0</LogonBranchID>
        //	<LogonUserID>0</LogonUserID>
        //	<LogonWorkstationID>0</LogonWorkstationID>
        //	<PatronBranchID>0</PatronBranchID>
        //	<PostalCode>string</PostalCode>
        //	<ZipPlusFour>string</ZipPlusFour>
        //	<City>string</City>
        //	<State>string</State>
        //	<County>string</County>
        //	<CountryID>0</CountryID>
        //	<StreetOne>string</StreetOne>
        //	<StreetTwo>string</StreetTwo>
        //	<StreetThree>string</StreetThree>
        //	<NameFirst>string</NameFirst>
        //	<NameLast>string</NameLast>
        //	<NameMiddle>string</NameMiddle>
        //	<User1>string</User1>
        //	<User2>string</User2>
        //	<User3>string</User3>
        //	<User4>string</User4>
        //	<User5>string</User5>
        //	<Gender>string</Gender>
        //	<Birthdate>2022-07-05T18:45:23.162Z</Birthdate>
        //	<PhoneVoice1>string</PhoneVoice1>
        //	<PhoneVoice2>string</PhoneVoice2>
        //	<PhoneVoice3>string</PhoneVoice3>
        //	<Phone1CarrierID>0</Phone1CarrierID>
        //	<Phone2CarrierID>0</Phone2CarrierID>
        //	<Phone3CarrierID>0</Phone3CarrierID>
        //	<EmailAddress>string</EmailAddress>
        //	<AltEmailAddress>string</AltEmailAddress>
        //	<LanguageID>0</LanguageID>
        //	<UserName>string</UserName>
        //	<Password>string</Password>
        //	<Password2>string</Password2>
        //	<DeliveryOptionID>0</DeliveryOptionID>
        //	<EnableSMS>true</EnableSMS>
        //	<TxtPhoneNumber>0</TxtPhoneNumber>
        //	<Barcode>string</Barcode>
        //	<EReceiptOptionID>0</EReceiptOptionID>
        //	<PatronCode>0</PatronCode>
        //	<ExpirationDate>2022-07-05T18:45:23.162Z</ExpirationDate>
        //	<AddrCheckDate>2022-07-05T18:45:23.162Z</AddrCheckDate>
        //	<GenderID>0</GenderID>
        //	<LegalNameFirst>string</LegalNameFirst>
        //	<LegalNameLast>string</LegalNameLast>
        //	<LegalNameMiddle>string</LegalNameMiddle>
        //	<UseLegalNameOnNotices>true</UseLegalNameOnNotices>
        //	<RequestPickupBranchID>0</RequestPickupBranchID>
        // </PatronRegistrationCreateData>
        StringWriter xmlString = new StringWriter();
        try 
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            // root element: <PatronRegistrationCreateData>
            Element rootElement = doc.createElement(MeCardDataToPapiData.TABLE_NAME.toString());
            doc.appendChild(rootElement);
            
            // Each element from the PapiElementOrder enum.
            for (PapiElementOrder key: PapiElementOrder.values())
            {
                if (this.columns.get(key) != null)
                {
                    Element papiElement = doc.createElement(key.toString());
                    papiElement.appendChild(doc.createTextNode(this.columns.get(key)));
                    rootElement.appendChild(papiElement);
                }            
            }
            
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlString);
            transformer.transform(source, result);

            // Output to console for testing
            if (this.debug)
            {
                StreamResult consoleResult = new StreamResult(System.out);
                transformer.transform(source, consoleResult);
            }
        }
        catch (ParserConfigurationException | TransformerConfigurationException ex)
        {
            System.out.println(MeCardDataToPapiData.class.getName() 
                + "getCreateXml() failed with a parser or transformer configuration exception:"
                + System.lineSeparator() + ex.getLocalizedMessage());
        } 
        catch (TransformerException ex)
        {
            System.out.println(MeCardDataToPapiData.class.getName() 
                + "getCreateXml() failed with a transformer exception:"
                + System.lineSeparator() + ex.getLocalizedMessage());
        }
        
        return xmlString.toString();
    }

    /**
     * Creates a well formed xml Patron Update Data request body.
     * This method supports patron requests to change address. If this option is set in the Polaris
     * Administration PAC profile Patron Access Options - Contact info, Patron can request address change
     * and the patron requests a change, an email message confirming the request is sent to the patron. If this
     * option is set in the Patron Access Options profile, a Verify Patron block is placed on the patron record,
     * and an email message is sent to a staff member. An error message (Address change request not
     * permitted, -501) is sent if the Patron can request address change option is not set.
     * You can remove an email address by sending a blank EmailAddress or Alt email address in the request
     * body; for example: \<EmailAddress\>\<\/EmailAddress\> would remove whatever was in the record.
     * Note: Incoming phone number formats are validated against customer-defined rules in
     * Polaris System Administration.
     * 
     * @return String of well formed XML customer data with tags in order.
     */
    protected String getUpdateXml()
    {
        //<?xml version="1.0" encoding="UTF-8"?>
        //<PatronUpdateData>
        //        <LogonBranchID>0</LogonBranchID>
        //        <LogonUserID>0</LogonUserID>
        //        <LogonWorkstationID>0</LogonWorkstationID>
        //        <ReadingListFlag>0</ReadingListFlag>
        //        <EmailFormat>0</EmailFormat>
        //        <DeliveryOptionID>0</DeliveryOptionID>
        //        <DeliveryOption>0</DeliveryOption>
        //        <EmailAddress>string</EmailAddress>
        //        <AltEmailAddress>string</AltEmailAddress>
        //        <EnableSMS>true</EnableSMS>
        //        <PhoneVoice1>string</PhoneVoice1>
        //        <PhoneVoice2>string</PhoneVoice2>
        //        <PhoneVoice3>string</PhoneVoice3>
        //        <Phone1CarrierID>0</Phone1CarrierID>
        //        <Phone2CarrierID>0</Phone2CarrierID>
        //        <Phone3CarrierID>0</Phone3CarrierID>
        //        <TxtPhoneNumber>0</TxtPhoneNumber>
        //        <EReceiptOptionID>0</EReceiptOptionID>
        //        <Password>string</Password>
        //        <PatronCode>0</PatronCode>
        //        <ExpirationDate>2022-07-05T19:38:30.611Z</ExpirationDate>
        //        <AddrCheckDate>2022-07-05T19:38:30.611Z</AddrCheckDate>
        //        <ExcludeFromAlmostOverdueAutoRenew>true</ExcludeFromAlmostOverdueAutoRenew>
        //        <ExcludeFromPatronRecExpiration>true</ExcludeFromPatronRecExpiration>
        //        <ExcludeFromInactivePatron>true</ExcludeFromInactivePatron>
        //        <PatronAddresses>
        //                <Address>
        //                        <AddressID>0</AddressID>
        //                        <FreeTextLabel>string</FreeTextLabel>
        //                        <StreetOne>string</StreetOne>
        //                        <StreetTwo>string</StreetTwo>
        //                        <StreetThree>string</StreetThree>
        //                        <City>string</City>
        //                        <State>string</State>
        //                        <County>string</County>
        //                        <PostalCode>string</PostalCode>
        //                        <ZipPlusFour>string</ZipPlusFour>
        //                        <Country>string</Country>
        //                        <CountryID>0</CountryID>
        //                        <AddressTypeID>0</AddressTypeID>
        //                </Address>
        //        </PatronAddresses>
        //        <RequestPickupBranchID>0</RequestPickupBranchID>
        //        <User1>string</User1>
        //        <User2>string</User2>
        //        <User3>string</User3>
        //        <User4>string</User4>
        //        <User5>string</User5>
        //</PatronUpdateData>
        StringWriter xmlString = new StringWriter();
        try 
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            // root element: <PatronRegistrationCreateData>
            Element rootElement = doc.createElement(MeCardDataToPapiData.TABLE_NAME.toString());
            doc.appendChild(rootElement);
            
            // Pre-emptively we create a <PatronAddresses> and <Address> tag
            // to be added to the root element later.
            Element patronAddressElement = doc.createElement(PapiElementOrder.TAG_PATRON_ADDRESSES.toString());
            Element addressElement = doc.createElement(PapiElementOrder.TAG_ADDRESS.toString());
            patronAddressElement.appendChild(addressElement);
            // Each element from the PapiElementOrder enum, but in the case
            // of update there are additional tags that need to be created, 
            // then placed in order.
            for (PapiElementOrder key: PapiElementOrder.values())
            {
                if (this.columns.get(key) != null)
                {
                    Element papiElement = doc.createElement(key.toString());
                    papiElement.appendChild(doc.createTextNode(this.columns.get(key)));
                    switch (key)
                    {
                        case ADDRESS_ID:
                        case FREE_TEXT_LABEL:
                        case STREET_ONE:
                        case STREET_TWO:
                        case STREET_THREE:
                        case CITY:
                        case COUNTY:
                        case STATE:
                        case COUNTRY:
                        case COUNTRY_ID:
                        case POSTAL_CODE:
                        case ZIP_PLUS_FOUR:
                        case ADDRESS_TYPE_ID:
                            addressElement.appendChild(papiElement);
                            break;
                        case BIRTHDATE:
                            /*
                            It seems that older version allow only a subset of 
                            customer attributes to be updated, however, in 7.6 
                            more attributes can be updated, and at TRAC the update
                            is actually expecting a birth date, which was previously
                            not allowed to be updated.
                            */
                            Properties papiProperties = PropertyReader.getProperties(ConfigFileTypes.PAPI);
                            // TODO: Fix this with Papiversion
                            String version = papiProperties.getProperty(PapiPropertyTypes.PAPI_VERSION.toString());
                            if (VersionComparator.greaterThanEqualTo(version, "7.6"))
                            {
                                if (this.debug)
                                    System.out.println("getUpdateXml(): adding birthdate to papi update.");
                                rootElement.appendChild(papiElement);
                            }
                            break;
                        // These are required
                        case LOGON_BRANCH_ID:
                        case LOGON_USER_ID:
                        case LOGON_WORKSTATION_ID:
                        // Note that PAPI can only update email, PhoneVoice1, Password
                        // ExpirationDate, and any address value, so filter out 
                        // any others, as they _may_ throw an error if included.
                        case EMAIL_ADDRESS:
                        // PhoneNumber is only returned by Patron Basic Data Get requests.
                        case PHONE_VOICE_1:
                        case PHONE_VOICE_2:
                        case PHONE_VOICE_3:
                        case PASSWORD:
                        case EXPIRATION_DATE:
                            rootElement.appendChild(papiElement);
                            break;
                        default: // No other elements accepted for update.
                            if (this.debug)
                                System.out.println("getUpdateXml(): ignoring " + key);
                            break;
                    }
                }            
            }
            // Now append the patron address element at the end.
            // NOTE:  <RequestPickupBranchID>
            //        <User1>string</User1>
            //        <User2>string</User2>
            //        <User3>string</User3>
            //        <User4>string</User4>
            //        <User5>string</User5>
            // are placed after the <PatronAddresses> element
            // but that is library specific settings, not required,
            // and not supplied through the ME Libraries.
            // Libraries may, however, specify them in the papi.properties.
            // If so you may need to add some code to put the <PatronAddresses>
            // in the correct order, above any of these. I would be surprised
            // if these elements required ordering for an update to work.
            rootElement.appendChild(patronAddressElement);
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlString);
            transformer.transform(source, result);

            // Output to console for testing
            if (this.debug)
            {
                StreamResult consoleResult = new StreamResult(System.out);
                transformer.transform(source, consoleResult);
            }
        }
        catch (ParserConfigurationException | TransformerConfigurationException ex)
        {
            System.out.println(MeCardDataToPapiData.class.getName() 
                + "getUpdateXml() failed with a parser or transformer configuration exception:"
                + System.lineSeparator() + ex.getLocalizedMessage());
        } 
        catch (TransformerException ex)
        {
            System.out.println(MeCardDataToPapiData.class.getName() 
                + "getUpdateXml() failed with a transformer exception:"
                + System.lineSeparator() + ex.getLocalizedMessage());
        }
        
        return xmlString.toString();
    }

    @Override
    public String getHeader()
    {
        String value = this.columns.get(PapiElementOrder.BARCODE);
        if (value == null)
        {
            return "";
        }
        return value;
    }

    @Override
    public String getName()
    {
        return MeCardDataToPapiData.TABLE_NAME.toString();
    }

    @Override
    public String getValue(String papiElementOrderkey)
    {
        String value;
        try
        {
            PapiElementOrder orderKey = PapiElementOrder.valueOf(papiElementOrderkey);
            value = this.columns.get(orderKey);
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(MeCardDataToPapiData.class.getName() + 
                    ".getValue failed: Couldn't use key: '" + papiElementOrderkey + 
                    " as reference in papi table.");
            return "";
        }
        if (value == null)
        {
            return "";
        }
        return value;
    }

    @Override
    public boolean setValue(String papiElementOrderKey, String value)
    {
        PapiElementOrder order;
        try
        {
            order = PapiElementOrder.valueOf(papiElementOrderKey);
            this.columns.put(order, value);
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(MeCardDataToPapiData.class.getName() + 
                    " Couldn't save key: '" + papiElementOrderKey + 
                    "' with value '" + value + "',"
                    + " No such element defined in "
                    + PapiElementOrder.class.getName());
            return false;
        }
        order = PapiElementOrder.valueOf(papiElementOrderKey);
        return this.columns.containsKey(order);
    }

    @Override
    public boolean renameKey(String originalPapiElementOrderkey, 
            String replacementPapiElementOrderKey)
    {
        String value;
        try
        {
            PapiElementOrder orderKey = PapiElementOrder.valueOf(originalPapiElementOrderkey);
            value = this.columns.remove(orderKey);
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(MeCardDataToPapiData.class.getName() + 
                    "renameKey failed: Couldn't use key: '" + originalPapiElementOrderkey + 
                    " as reference in papi table.");
            return false;
        }
        return this.setValue(replacementPapiElementOrderKey, value);
    }

    @Override
    public boolean deleteValue(String papiElementOrderkey)
    {
        try
        {
            PapiElementOrder order = PapiElementOrder.valueOf(papiElementOrderkey);
            String value = this.columns.remove(order);
            // return false if the value is not found.
            if (value == null)
            {
                return false;
            }
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(MeCardDataToPapiData.class.getName() + 
                    "deleteValue failed: Couldn't use key: '" + papiElementOrderkey + 
                    " as reference in papi table.");
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return this.getData();
    }
    
    /**
     * Returns a set of tag names of populated data like 'NameFirst' in 
     * the order required by PAPI.
     * 
     * @return Set of the XML tags for populated customer data in PapiElementOrder
     */
    @Override
    public Set<String> getKeys() 
    {
        List<String> cols = new ArrayList<>();
        for (PapiElementOrder key: PapiElementOrder.values())
        {
            if (this.columns.get(key) != null)
            {
                cols.add(key.toString());
            }
        }
        // Ensure the order of the Set being returned.
        @SuppressWarnings("unchecked")
        Set<String> s = new LinkedHashSet(cols);
        return s;
    }
}
