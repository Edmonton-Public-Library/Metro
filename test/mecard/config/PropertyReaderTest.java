/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.config;

import java.util.Map;
import java.util.Properties;
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
     * Test of getProperties method, of class PropertyReader.
     */
    @Test
    public void testGetProperties() throws Exception
    {
        System.out.println("getProperties");
        Properties envProps = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        envProps.list(System.out);
        
        
        Properties createProps = PropertyReader.getProperties(ConfigFileTypes.DEFAULT_CREATE);
        createProps.list(System.out);

        
        Properties sip2Props = PropertyReader.getProperties(ConfigFileTypes.SIP2);
        sip2Props.list(System.out);
        
    }

    /**
     * Test of augmentProperties method, of class PropertyReader.
     */
    @Test
    public void testAugmentProperties()
    {
        System.out.println("augmentProperties");
        Map<String, String> props = null;
        ConfigFileTypes type = null;
        PropertyReader.augmentProperties(props, type);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}