
package mecard.polaris.sql;

import api.CommandStatus;
import mecard.polaris.sql.SQLDescribeCommand;
import mecard.polaris.sql.SQLConnector;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PolarisSQLPropertyTypes;
import mecard.config.PolarisTable;
import mecard.config.PropertyReader;
import org.junit.Test;

/**
 *
 * @author anisbet
 */


public class SQLDescribeCommandTest
{
    private final SQLConnector connector;
    
    public SQLDescribeCommandTest()
    {
        Properties p = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        String host = p.getProperty(PolarisSQLPropertyTypes.HOST.toString());
        String driver = p.getProperty(PolarisSQLPropertyTypes.CONNECTOR_TYPE.toString());
        String database = p.getProperty(PolarisSQLPropertyTypes.DATABASE.toString());
        String databaseId = p.getProperty(PolarisSQLPropertyTypes.USERNAME.toString());
        String databasePassword = p.getProperty(PolarisSQLPropertyTypes.PASSWORD.toString());
        connector = new SQLConnector.Builder(host, driver, database)
                .user(databaseId)
                .password(databasePassword)
                .build();
    }

    /**
     * Test of execute method, of class SQLDescribeCommand.
     */
    @Test
    public void testExecute()
    {
        System.out.println("===Describe execute===");
        // use this version for query DESCRIBE table
        SQLDescribeCommand describe = new SQLDescribeCommand.Builder(connector, PolarisTable.ADDRESSES) //PatronRegistration, Patrons, Addresses, PostalCodes
                .build();
        System.out.println("COMMAND==>"+describe.toString()+"<==");
        CommandStatus status = describe.execute();
        System.out.println("STATUS==>" + status.getStdout() + "<==");
        System.out.println("== Describe execute ==\n");
        CommandStatus expResult = null;
        CommandStatus result = describe.execute();
    }
    
}
