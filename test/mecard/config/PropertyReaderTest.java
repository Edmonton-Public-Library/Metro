/**
 *
 * This class is part of the Metro, MeCard project. Copyright (C) 2013 Andrew
 * Nisbet, Edmonton public Library.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
*/
package mecard.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PropertyReaderTest
{
    
    public PropertyReaderTest()
    {
    }

    /**
     * Test of getProperties method, of class MetroService.
     */
    @Test
    public void testGetProperties() throws Exception
    {
        System.out.println("===getProperties===");
        Properties envProps = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        envProps.list(System.out);
        
        Properties createProps = PropertyReader.getProperties(ConfigFileTypes.SYMPHONY);
        createProps.list(System.out);

        Properties sip2Props = PropertyReader.getProperties(ConfigFileTypes.SIP2);
        sip2Props.list(System.out);
        
        Properties bimportProps = PropertyReader.getProperties(ConfigFileTypes.BIMPORT);
        bimportProps.list(System.out);
        
        Properties cityProps = PropertyReader.getProperties(ConfigFileTypes.BIMPORT_CITY_MAPPING);
        cityProps.list(System.out);
        
        Properties debugProps = PropertyReader.getProperties(ConfigFileTypes.DEBUG);
        debugProps.list(System.out);
        
        Properties polarisProps = PropertyReader.getProperties(ConfigFileTypes.PAPI);
        polarisProps.list(System.out);
        
        Properties sqlProps = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        sqlProps.list(System.out);
        
        Properties messageProps = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        messageProps.list(System.out);
    }

    /**
     * Test of augmentProperties method, of class MetroService.
     */
    @Test
    public void testAugmentProperties()
    {
        System.out.println("==augmentProperties==");
//        Properties debugProps = MetroService.getProperties(ConfigFileTypes.DEBUG);
//        debugProps.list(System.out);
        Map<String, String> props = new HashMap<String, String>();
        props.put("value_A", "A");
        props.put("value_B", "B");
        ConfigFileTypes type = ConfigFileTypes.DEBUG;
        PropertyReader.augmentProperties(props, type);
        int count = 1;
        for (String s: props.keySet())
        {
            System.out.println(count + ") " + s + " == " + props.get(s));
            count++;
        }
        assertTrue(props.size() == 6);
    }

    /**
     * Test of loadDelimitedEntry method, of class PropertyReader.
     */
    @Test
    public void testLoadDelimitedEntry()
    {
        System.out.println("==loadDelimitedEntry==");
        Properties props = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        props.list(System.out);
        LibraryPropertyTypes libraryPropertyTypes = LibraryPropertyTypes.NON_RESIDENT_TYPES;
        List<String> result = new ArrayList<>(); 
        PropertyReader.loadDelimitedEntry(props, libraryPropertyTypes, result);
        for (String s: result)
        {
            System.out.println("N:" + s);
        }
        libraryPropertyTypes = LibraryPropertyTypes.RECIPROCAL_TYPES;
        PropertyReader.loadDelimitedEntry(props, libraryPropertyTypes, result);
        for (String s: result)
        {
            System.out.println("R:" + s);
        }
        libraryPropertyTypes = LibraryPropertyTypes.JUVENILE_TYPES;
        PropertyReader.loadDelimitedEntry(props, libraryPropertyTypes, result);
        for (String s: result)
        {
            System.out.println("J:" + s);
        }
        libraryPropertyTypes = LibraryPropertyTypes.CUSTOMER_STANDING_SENTINEL;
        PropertyReader.loadDelimitedEntry(props, libraryPropertyTypes, result);
        for (String s: result)
        {
            System.out.println("GOOD_STANDING:" + s);
        }
        System.out.println("==loadDelimitedEntry==");
    }

    /**
     * Test of setConfigDirectory method, of class PropertyReader.
     */
//    @Test
//    public void testSetConfigDirectory()
//    {
//        System.out.println("setConfigDirectory");
//        String configDirectory = "";
//        PropertyReader.setConfigDirectory(configDirectory);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}