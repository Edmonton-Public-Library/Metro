
package api;

import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.SQLPropertyTypes;
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
     * Test of getConnection method, of class SQLConnector.
     */
    @Test
    public void testGetConnection()
    {
        System.out.println("==getConnection==");
        Properties p = PropertyReader.getProperties(ConfigFileTypes.SQL);
        String host = p.getProperty(SQLPropertyTypes.HOST.toString());
        String driver = p.getProperty(SQLPropertyTypes.CONNECTOR_TYPE.toString());
        String database = p.getProperty(SQLPropertyTypes.DATABASE.toString());
        String user = p.getProperty(SQLPropertyTypes.USERNAME.toString());
        String password = p.getProperty(SQLPropertyTypes.PASSWORD.toString());
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
