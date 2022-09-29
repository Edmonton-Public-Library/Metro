/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2019  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 */
package mecard.polaris.sql;

import api.Command;
import api.CommandStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mecard.ResponseTypes;
import mecard.exception.ConfigurationException;

/**
 * This class is the equivalent of the SQL DESCRIBE command. This class
 * isn't used in production but during testing to see if there are changes to
 * a table, or just to confirm the schema of a table.
 * @author Andrew Nisbet <andrew.nisbet@epl.ca>
 */
public class SQLDescribeCommand implements Command
{
    private final String table;
    private final SQLConnector connector;
    private final String cmdString;

    public static class Builder
    {
        private SQLConnector connector;
        private String table; // The name of the table.
        
        public Builder(SQLConnector s, String tableName)
        {
            if (s == null)
            {
                System.out.println(SQLUpdateCommand.class.getName() 
                        + " received null connector as argument.");
                throw new ConfigurationException();
            }
            this.connector  = s;
            this.table = tableName;
        }
        
        public SQLDescribeCommand build()
        {
            return new SQLDescribeCommand(this);
        }
    }
    
    /**
     * Constructor for the DESCRIBE command.
     * @param builder the builder object.
     */
    private SQLDescribeCommand(Builder builder)
    {
        this.connector  = builder.connector;
        this.table      = builder.table;
        switch (this.connector.getDriverType())
        {
            case MY_SQL:
                cmdString = "DESCRIBE ";
                break;
            case SQL_SERVER:
                cmdString = "exec sp_columns ";
                break;
            default:
                throw new UnsupportedOperationException(
                        SQLDescribeCommand.class.getName() 
                        + " unregistered JDBC driver type. (1026)"); // conspicuous easy to find message.
        }
    }
    
    @Override
    public CommandStatus execute()
    {
        CommandStatus status = new CommandStatus();
        PreparedStatement pStatement = null;
        Connection connection;
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append(cmdString);
            sb.append(" ");
            sb.append(this.table);
            connection = this.connector.getConnection();
            pStatement = connection.prepareStatement(sb.toString());
            ResultSet rs = pStatement.executeQuery();
            StringBuilder resultBuilder = new StringBuilder();
//            int columnIndex = 1;
            while (rs.next())
            {
                for (int i = 1; i < rs.getMetaData().getColumnCount(); i++)
                {
                    resultBuilder.append(rs.getString(i));
                    resultBuilder.append(", ");
                }
                resultBuilder.append("\n");
            }
            status.setStdout(resultBuilder.toString());
            status.setEnded(ResponseTypes.OK.ordinal());
            pStatement.close();
            // Don't close the connection, other commands might need it.
        }
        catch (SQLException ex)
        {
            System.out.println("**error executing statement '" + 
                    pStatement + "'.\n" + ex.getMessage());
            status.setError(ex);
            System.out.println("SQLException MSG:"+status.getStderr());
            status.setResponseType(ResponseTypes.FAIL);
        }
        return status;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(cmdString);
        sb.append(this.table);
        return sb.toString();
    }
}
