
package mecard.polaris.sql;

import mecard.polaris.sql.SQLStoredProcedureCommand;
import mecard.polaris.sql.SQLConnector;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PolarisSQLPropertyTypes;
import mecard.config.PropertyReader;
import org.junit.Test;

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
    /**
     * Test of execute method, of class SQLStoredProcedureCommand with
     * a expected return result set.
     */
    @Test
    public void testCommandResults()
    {
        System.out.println("== test for result set ==");
        SQLStoredProcedureCommand IDX_GatherPatronKeywords = 
                new SQLStoredProcedureCommand.Builder(
                        connector, 
                        "Polaris.IDX_GatherPatronKeywords", 
                        "call")
                .integer("nPatronID", "123456")
                .returns("OTHER")
                .build();
        System.out.println(">>>>" + IDX_GatherPatronKeywords.toString());
    }

}
