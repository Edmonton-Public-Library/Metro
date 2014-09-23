
package api;

import java.text.ParseException;
import java.util.Properties;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.PolarisSQLPropertyTypes;
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
        p = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        String host = p.getProperty(PolarisSQLPropertyTypes.HOST.toString());
        String driver = p.getProperty(PolarisSQLPropertyTypes.CONNECTOR_TYPE.toString());
        String database = p.getProperty(PolarisSQLPropertyTypes.DATABASE.toString());
        String user = p.getProperty(PolarisSQLPropertyTypes.USERNAME.toString());
        String password = p.getProperty(PolarisSQLPropertyTypes.PASSWORD.toString());
        connector = new SQLConnector.Builder(host, driver, database)
                .user(user)
                .password(password)
                .build();
    }
    
     /**
     * Test of execute method, of class POLARIS_SQLInsertCommand.
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
                .string("title", "Pir8 Software")
                .integer("station", "76")
                .date("DateInstalled", null)
                .build();
        System.out.println("COMMAND==>"+command.toString());
        CommandStatus status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        
        command = new SQLInsertCommand.Builder(connector, "software")
                .string("title", "null")
                .integer("station", "3333")
                .date("DateInstalled", "null")
                .build();
        System.out.println("COMMAND==>"+command.toString());
        status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        
        command = new SQLInsertCommand.Builder(connector, "software")
                .string("title", "Pir8 Software")
                .integer("station") // null value for integer.
                .date("DateInstalled", "2014-07-25")
                .build();
        System.out.println("COMMAND==>"+command.toString());
        status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
    }
    
     /**
     * Test of execute method, of class POLARIS_SQLUpdateCommand.
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
                .string("title", "POPPY Cap")
                .integer("station", "4444")
//                .date("DateInstalled", "2013-25-12") // this date value fails on MySQL date format:Data truncation: Incorrect date value: '2013-25-12' for column 'DateInstalled' at row 1
                .date("DateInstalled", null)
                .whereInteger("station", "3333")
                .build();

        CommandStatus status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        
        command = new SQLUpdateCommand.Builder(connector, "software")
                .string("title", "Andrew Acrobat")
                .integer("station", null)
//                .date("DateInstalled", "2013-25-12") // this date value fails on MySQL date format:Data truncation: Incorrect date value: '2013-25-12' for column 'DateInstalled' at row 1
                .date("DateInstalled", myDate)
                .whereInteger("station", "77")
                .build();

        System.out.println("COMMAND==>"+command.toString());
        status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
    }
    
    /**
     * Test of execute method, of class POLARIS_SQLSelectCommand.
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
     * Test of execute method, of class POLARIS_SQLCommand.
     */
    @Test
    public void testExecute()
    {
        System.out.println("==execute NULL date==");
        Command command = new SQLSelectCommand.Builder(connector, "software")
                .date("DateInstalled")
                .whereInteger("Id", "1")
                .build();

        System.out.println("COMMAND==>"+command.toString());
        CommandStatus status = command.execute();
        System.out.println("STATUS: " + status.getStdout() + status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        
        System.out.println(">>>>>>>: station where dateInstalled = null"); 
        command = new SQLSelectCommand.Builder(connector, "software")
                .integer("station")
                .whereInteger("Id", "23")
                .build();
        System.out.println("COMMAND==>"+command.toString());
        status = command.execute();
        System.out.println("RESULT: " + status.getStdout());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        
        System.out.println(">>>>>>>: station where title = null"); 
        command = new SQLSelectCommand.Builder(connector, "software")
                .integer("station")
                .whereString("title", null)
                .build();
        System.out.println("COMMAND==>"+command.toString());
        status = command.execute();
        System.out.println("RESULT: " + status.getStdout());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        
        
        System.out.println("+++++++++++++++++++++++++++++++++++++");
        // NULL value check
        System.out.println(">>>>>>>: station where title = null"); 
        command = new SQLSelectCommand.Builder(connector, "software")
                .integer("station")
                .whereString("title", null)
                .build();
        System.out.println("COMMAND==>"+command.toString());
        status = command.execute();
        System.out.println("RESULT: " + status.getStdout());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        
        System.out.println(">>>>>>>: Id where station = null"); 
        command = new SQLSelectCommand.Builder(connector, "software")
                .integer("Id")
                .whereInteger("station", null)
                .build();
        System.out.println("COMMAND==>"+command.toString());
        status = command.execute();
        System.out.println("RESULT: " + status.getStdout());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        
        System.out.println(">>>>>>>: station where date = null"); 
        command = new SQLSelectCommand.Builder(connector, "software")
                .integer("station")
                .whereString("title", null)
                .build();
        System.out.println("COMMAND==>"+command.toString());
        status = command.execute();
        System.out.println("RESULT: " + status.getStdout());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
        System.out.println("+++++++++++++++++++++++++++++++++++++");
        
        
        
        
        System.out.println(">>>>>>>: station where dateInstalled = 2014-07-25"); 
        // Note null station ids result is '0'
        command = new SQLSelectCommand.Builder(connector, "software")
                .integer("station")
                .whereDate("DateInstalled", "2014-07-25")
                .build();
        System.out.println("COMMAND==>"+command.toString());
        status = command.execute();
        System.out.println("RESULT: " + status.getStdout());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
    }
    
    @Test
    public void testDescribe()
    {
        System.out.println("==execute Descibe table==");
        Command command = new SQLDescribeCommand.Builder(connector, "software")
                .build();

        System.out.println("COMMAND==>"+command.toString());
        CommandStatus status = command.execute();
        System.out.println("STDOUT: '" + status.getStdout() + "' status:"+ status.getStatus());
        assertTrue(status.getStatus() == ResponseTypes.COMMAND_COMPLETED);
    }
    
}
