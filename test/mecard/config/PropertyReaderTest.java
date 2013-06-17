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

import java.util.Properties;
import org.junit.Test;

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
     * Test of getProperties method, of class PropertyReader.
     */
    @Test
    public void testGetProperties() throws Exception
    {
        System.out.println("===getProperties===");
        Properties envProps = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        envProps.list(System.out);
        
        Properties createProps = PropertyReader.getProperties(ConfigFileTypes.DEFAULT_CREATE);
        createProps.list(System.out);

        Properties sip2Props = PropertyReader.getProperties(ConfigFileTypes.SIP2);
        sip2Props.list(System.out);
        
        Properties bimportProps = PropertyReader.getProperties(ConfigFileTypes.BIMPORT);
        bimportProps.list(System.out);
        
        Properties apiProps = PropertyReader.getProperties(ConfigFileTypes.API);
        apiProps.list(System.out);
    }
}