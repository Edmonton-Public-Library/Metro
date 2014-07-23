
package api;

import java.text.ParseException;
import java.util.Properties;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.SQLPropertyTypes;
import mecard.util.DateComparer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */
public class SQLCommandTest
{
    private Properties p;
    private final SQLConnector connector;
    
    public SQLCommandTest()
    {
        p = PropertyReader.getProperties(ConfigFileTypes.SQL);
        String host = p.getProperty(SQLPropertyTypes.HOST.toString());
        String driver = p.getProperty(SQLPropertyTypes.CONNECTOR_TYPE.toString());
        String database = p.getProperty(SQLPropertyTypes.DATABASE.toString());
        String user = p.getProperty(SQLPropertyTypes.USERNAME.toString());
        String password = p.getProperty(SQLPropertyTypes.PASSWORD.toString());
        connector = new SQLConnector.Builder(host, driver, database)
                .user(user)
                .password(password)
                .build();
    }
    
     /**
     * Test of execute method, of class SQLInsertCommand.
     */
    @Test
    public void testInsertExecute()
    {
        System.out.println("== Insert ==");
        String myDate = null;
        try
        {
            myDate = DateComparer.ANSIToConfigDate("20131011");
        }
        catch (ParseException e)
        {
            System.out.println("Date could not be parsed." + e.getMessage());
        }
        Command command = new SQLInsertCommand.Builder(connector, "software")
                .setNull("title")
                .integer("station", 77)
                .date("DateInstalled", myDate)
                .build();

        CommandStatus status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
    }
    
     /**
     * Test of execute method, of class SQLUpdateCommand.
     */
    @Test
    public void testUpdateExecute()
    {
        System.out.println("== Update ==");
        String myDate = null;
        try
        {
            myDate = DateComparer.ANSIToConfigDate("20131124");
        }
        catch (ParseException e)
        {
            System.out.println("Date could not be parsed." + e.getMessage());
        }
        Command command = new SQLUpdateCommand.Builder(connector, "software")
                .string("title", "Snadobe Acrobat")
                .integer("station", 5)
//                .date("DateInstalled", "2013-25-12") // this date value fails on MySQL date format:Data truncation: Incorrect date value: '2013-25-12' for column 'DateInstalled' at row 1
                .date("DateInstalled", myDate)
                .where("Id=6")
                .build();

        CommandStatus status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
    }
    
    /**
     * Test of execute method, of class SQLSelectCommand.
     */
    @Test
    public void testSelectExecute()
    {
        System.out.println("== Select ==");
       
        SQLSelectCommand command = new SQLSelectCommand.Builder(connector, "gate_info")
                .string("GateId")
                .string("IpAddress")
                .string("Branch")
                .integer("LastInCount")
                .integer("LastOutCount")
                .build();
        System.out.println("=== CMD:" + command.toString());
        CommandStatus status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        
        command = new SQLSelectCommand.Builder(connector, "gate_info")
                .integer("LastOutCount")
                .build();
        System.out.println("=== CMD:" + command.toString());
        status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        
    }

    /**
     * Test of execute method, of class SQLCommand.
     */
    @Test
    public void testExecute()
    {
        System.out.println("==execute NULL date==");
        Command command = new SQLSelectCommand.Builder(connector, "software")
                .date("DateInstalled")
                .where("Id=1")
                .build();

        CommandStatus status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
    }
    
}
