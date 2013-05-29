/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import java.util.EnumMap;
import java.util.HashMap;
import mecard.customer.CustomerFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
 */
public class XMLParserTest
{
    
    public XMLParserTest()
    {
    }

    @Test
    public void testSomeMethod()
    {
        // TODO review the generated test code and remove the default call to fail.
        System.out.println("==Constructor==");
        XMLParser p = new XMLParser("bimp_table_config.xml");
    }

    /**
     * Test of getTables method, of class XMLParser.
     */
    @Test
    public void testGetTables()
    {
        System.out.println("==getTables==");
        XMLParser instance = new XMLParser("bimp_table_config.xml");
        HashMap<String, EnumMap<CustomerFieldTypes, String>> result = instance.getTables();
        System.out.println("size = "+result.size());
        assertTrue(result.size() == 4);
        for (String s: result.keySet())
        {
            System.out.println("Table: "+s);
            EnumMap<CustomerFieldTypes, String> table = result.get(s);
            System.out.println("size: "+table.size());
            for (CustomerFieldTypes cType: table.keySet())
            {
                System.out.println("column: "+cType.toString() + table.get(cType));
            }
        }
    }
}