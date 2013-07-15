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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import mecard.MetroService;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author metro
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
        Properties envProps = MetroService.getProperties(ConfigFileTypes.ENVIRONMENT);
        envProps.list(System.out);
        
        Properties createProps = MetroService.getProperties(ConfigFileTypes.DEFAULT_CREATE);
        createProps.list(System.out);

        Properties sip2Props = MetroService.getProperties(ConfigFileTypes.SIP2);
        sip2Props.list(System.out);
        
        Properties bimportProps = MetroService.getProperties(ConfigFileTypes.BIMPORT);
        bimportProps.list(System.out);
        
        Properties apiProps = MetroService.getProperties(ConfigFileTypes.API);
        apiProps.list(System.out);
        
        Properties cityProps = MetroService.getProperties(ConfigFileTypes.BIMPORT_CITY_MAPPING);
        cityProps.list(System.out);
        
        Properties debugProps = MetroService.getProperties(ConfigFileTypes.DEBUG);
        debugProps.list(System.out);
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
        MetroService.augmentProperties(props, type);
        int count = 1;
        for (String s: props.keySet())
        {
            System.out.println(count + ") " + s + " == " + props.get(s));
            count++;
        }
        assertTrue(props.size() == 8);
    }
}