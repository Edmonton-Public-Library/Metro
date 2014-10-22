
package api;

import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.PolarisSQLPropertyTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class SQLConnectorTest
{
    
    public SQLConnectorTest()
    {
    }

    /**
     * Test of getConnection method, of class POLARIS_SQLConnector.
     */
    @Test
    public void testGetConnection()
    {
        System.out.println("==getConnection==");
        Properties p = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        String host = p.getProperty(PolarisSQLPropertyTypes.HOST.toString());
        String driver = p.getProperty(PolarisSQLPropertyTypes.CONNECTOR_TYPE.toString());
        String database = p.getProperty(PolarisSQLPropertyTypes.DATABASE.toString());
        String user = p.getProperty(PolarisSQLPropertyTypes.USERNAME.toString());
        String password = p.getProperty(PolarisSQLPropertyTypes.PASSWORD.toString());
        SQLConnector connector = new SQLConnector.Builder(host, driver, database)
                .user(user)
                .password(password)
                .build();
        String expResult = "jdbc:mysql://mysql.epl.ca:3306/patroncount";
        String result = connector.toString();
        assertTrue(expResult.compareTo(result) == 0);
        connector.close();
    }
    
}
