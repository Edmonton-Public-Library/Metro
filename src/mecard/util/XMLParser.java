/*
 * Copyright (C) 2013 metro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mecard.util;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import mecard.customer.CustomerFieldTypes;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Reads XML files.
 *
 * @author metro
 */
public class XMLParser
{

    private HashMap<String, EnumMap<CustomerFieldTypes, String>> tables;
//    private EnumMap<CustomerFieldTypes, String> stateMap;

    public XMLParser(String xmlFilePath)
    {
        tables = new HashMap<String, EnumMap<CustomerFieldTypes, String>>();
        try
        {
            File file = new File(xmlFilePath);
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            // Starting at root...
            if (doc.hasChildNodes())
            {
                printNote(doc.getChildNodes());
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void printNote(NodeList nodeList)
    {
        EnumMap<CustomerFieldTypes, String> currentTable =
                new EnumMap<CustomerFieldTypes, String>(CustomerFieldTypes.class);
        for (int count = 0; count < nodeList.getLength(); count++)
        {
            Node tempNode = nodeList.item(count);
            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE)
            {
                // get node name and value
                String nodeName = tempNode.getNodeName();
                if (nodeName.compareTo("table") == 0)
                {
//                    System.out.print("\nTable '" + nodeName + "'");
//                    System.out.println(", value ='" + tempNode.getTextContent() + "'");
                    String tableName = getFirstAttribute(tempNode);
                    System.out.println("Found: "+tableName);
                    this.tables.put(tableName, currentTable);
                    currentTable = new EnumMap<CustomerFieldTypes, String>(CustomerFieldTypes.class);
                }
                else // not table so column
                {
                    if (tempNode.hasAttributes())
                    {
                        String nodeAttrib = getFirstAttribute(tempNode);
                        CustomerFieldTypes fieldType = getFieldType(nodeAttrib);
                        String value = tempNode.getFirstChild().getNodeValue();
                        currentTable.put(fieldType, value);
                        System.out.println(fieldType.toString());
                        System.out.println("column name=" + nodeAttrib + " goes in column "+value);
                    }
                }
                if (tempNode.hasChildNodes())
                {
                    // loop again if has child nodes
                    printNote(tempNode.getChildNodes());
                }
            }
        }
    }

    private String getFirstAttribute(Node tempNode)
    {
        // get attributes names and values
        NamedNodeMap nodeMap = tempNode.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++)
        {
            Node node = nodeMap.item(i);
            return node.getNodeValue();
        }
        return "";
    }

    protected CustomerFieldTypes getFieldType(String nodeAttrib)
    {
//        CustomerFieldTypes type = CustomerFieldTypes.DEFAULT;
//        for (CustomerFieldTypes cType : CustomerFieldTypes.values())
//        {
//            if (nodeAttrib.startsWith(cType.toString()))
//            {
//                type = cType;
//            }
//        }
//        return type;
        return CustomerFieldTypes.valueOf(nodeAttrib);
    }

    HashMap<String, EnumMap<CustomerFieldTypes, String>> getTables()
    {
        return this.tables;
    }
}