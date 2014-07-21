/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
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
package api;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mecard.ResponseTypes;
import mecard.exception.ConfigurationException;

/**
 * Handles basic SQL insert statement via JDBC.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SQLInsertCommand implements Command 
{
    private final SQLConnector connector;
    private final String statementString;
    
    public static class Builder
    {
        private SQLConnector connector;
        private String table; // The name of the table.
        private List<SQLUpdateData> columnList; // Column names you wish to see in selection.
        
        public Builder(SQLConnector s, String tableName)
        {
            if (s == null)
            {
                System.out.println(SQLInsertCommand.class.getName() 
                        + " received null connector as argument.");
                throw new ConfigurationException();
            }
            this.connector  = s;
            this.table = tableName;
            this.columnList = new ArrayList<>();
        }
        
        public Builder string(String cName, String value)
        {
            SQLUpdateData s = new SQLUpdateData(cName, SQLData.Type.STRING, value);
            this.columnList.add(s);
            return this;
        }
        
        public Builder date(String dName, String date)
        {
            SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.DATE, date);
            this.columnList.add(d);
            return this;
        }
        
        public Builder integer(String iName, int value)
        {
            SQLUpdateData i = new SQLUpdateData(iName, SQLData.Type.INT, String.valueOf(value));
            this.columnList.add(i);
            return this;
        }
        
        public Builder setNull(String columnName)
        {
            SQLUpdateData n = new SQLUpdateData(columnName);
            this.columnList.add(n);
            return this;
        }
        
        public SQLInsertCommand build()
        {
            return new SQLInsertCommand(this);
        }
    }
    
    private SQLInsertCommand(Builder builder)
    {
        this.connector = builder.connector;
        StringBuilder sb = new StringBuilder();
        // insert INTO software (station, title, DateInstalled) values (45, "Adobe Acrobat", curdate());
        sb.append("INSERT INTO ");
        sb.append(builder.table);
        sb.append(" (");
        for (int i = 0; i < builder.columnList.size(); i++)
        {
            SQLUpdateData dType = builder.columnList.get(i);
            sb.append(dType.getName());
            if (i + 1 != builder.columnList.size())
            {
                sb.append(", ");
            }
        }
        // We do not allow an empty where clause to limit the update.
        sb.append(") VALUES (");
        for (int i = 0; i < builder.columnList.size(); i++)
        {
            SQLUpdateData dType = builder.columnList.get(i);
            if (dType.getType() == SQLData.Type.INT)
            {
                sb.append(dType.getValue());
            }
            else // quote the values we are going to change.
            {
                sb.append("\"");
                sb.append(dType.getValue());
                sb.append("\"");
            }
            if (i + 1 != builder.columnList.size())
            {
                sb.append(", ");
            }
        }
        sb.append(")");
        this.statementString = sb.toString();
    }
    
    @Override
    public CommandStatus execute()
    {
        CommandStatus status = new CommandStatus();
        Statement statement = null;
        try
        {
            statement = this.connector.getConnection().createStatement();
            // Execute the SQL, and yes JDBC has helpfully called the insert statement executeUpdate.
            statement.executeUpdate(statementString);
            status.setEnded(ResponseTypes.OK.ordinal());
        }
        catch (SQLException ex)
        {
            System.out.println("**error executing statement '" + 
                    statementString + "'.\n" + ex.getMessage());
            status.setError(ex);
            System.out.println("SQLException MSG:"+status.getStderr());
            status.setResponseType(ResponseTypes.FAIL);
        }
        finally
        {
            if (statement != null)
            {
                try
                {
                    statement.close();
                } catch (SQLException ex)
                {
                    System.out.println("**error " + ex.getMessage());
                    status.setError(ex);
                    status.setResponseType(ResponseTypes.FAIL);
                }
            }
        }
        return status;
    }
    
    @Override
    public String toString()
    {
        return this.statementString;
    }
    
}
