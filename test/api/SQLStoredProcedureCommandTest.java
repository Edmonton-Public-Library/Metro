
package api;

import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PolarisSQLPropertyTypes;
import mecard.config.PropertyReader;
import mecard.requestbuilder.PolarisSQLRequestBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class SQLStoredProcedureCommandTest
{
    private final SQLConnector connector;
    public SQLStoredProcedureCommandTest()
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
     * Test of execute method, of class SQLStoredProcedureCommand.
     */
    @Test
    public void testCommand()
    {
        System.out.println("== test commmand string ==");
        SQLStoredProcedureCommand callHashPasswordCommand = 
                new SQLStoredProcedureCommand.Builder(
                        connector, 
                        "Polaris.Circ_SetPatronPassword", 
                        "EXEC")
                .integer("nPatronID", "123456")
                .string("szPassword", "S3cr3t")
                .build();
        System.out.println(">>>>" + callHashPasswordCommand.toString());
    }
    
}
