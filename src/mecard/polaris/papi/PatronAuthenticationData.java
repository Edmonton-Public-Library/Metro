/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2022  Edmonton Public Library
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Convenience class to create a well formed XML Patron authentication data document.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class PatronAuthenticationData
{
    enum Tag {
        Barcode,
        Password,
        PatronAuthenticationData;
    }
    
    private PatronAuthenticationData()
    {    }
    
    /**
     * Creates a string version of a PAPI Patron authentication data document as
     * of version 7.0 at least.
     * 
     * @param userId String of the customer's user ID, barcode, 
     * or library card number.
     * @param password String of the customer's password.
     * @return XML string version of the PAPI Patron authentication data document.
     */
    public static String getAuthentication(String userId, String password)
    {
//      <PatronAuthenticationData xmlns:i="http://www.w3.org/2001/XMLSchemainstance">
//         <Barcode>userId</Barcode>
//         <Password>userPin</Password>
//      </PatronAuthenticationData>
        StringWriter xmlString = new StringWriter();
        try 
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            // root element: <PatronRegistrationCreateData>
            Element rootElement = doc.createElement(PatronAuthenticationData.Tag.PatronAuthenticationData.name());
            doc.appendChild(rootElement);
            
            // User ID
            Element userIdElement = doc.createElement(PatronAuthenticationData.Tag.Barcode.name());
            userIdElement.appendChild(doc.createTextNode(userId));
            rootElement.appendChild(userIdElement);
            
            // User Password
            Element userPasswordElement = doc.createElement(PatronAuthenticationData.Tag.Password.name());
            userPasswordElement.appendChild(doc.createTextNode(password));
            rootElement.appendChild(userPasswordElement);

            
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlString);
            transformer.transform(source, result);
        }
        catch (ParserConfigurationException | TransformerConfigurationException ex)
        {
            Logger.getLogger(MeCardDataToPapiData.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (TransformerException ex)
        {
            Logger.getLogger(MeCardDataToPapiData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return xmlString.toString();
    }
}
