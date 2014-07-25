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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private final List<SQLUpdateData> columnList;
    private final String table;
    
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
        
        public Builder integer(String iName, String value)
        {
            if (value == null)
            {
                SQLUpdateData i = new SQLUpdateData(iName, SQLData.Type.INT, null);
                this.columnList.add(i);
                return this;
            }
            SQLUpdateData i = new SQLUpdateData(iName, SQLData.Type.INT, value);
            this.columnList.add(i);
            return this;
        }
        
        public Builder integer(String iName)
        {
            SQLUpdateData i = new SQLUpdateData(iName, SQLData.Type.INT, null);
            this.columnList.add(i);
            return this;
        }
        
        public SQLInsertCommand build()
        {
            return new SQLInsertCommand(this);
        }
    }
    
    /**
     * Constructor.
     * @param builder 
     */
    private SQLInsertCommand(Builder builder)
    {
        this.connector = builder.connector;
        this.columnList= builder.columnList;
        this.table     = builder.table;
    }
    
    @Override
    public CommandStatus execute()
    {
        CommandStatus status = new CommandStatus();
        Connection connection = null;
        PreparedStatement pStatement = null;
        try
        {
            StringBuilder statementStrBuilder = new StringBuilder();
            // insert INTO software (station, title, DateInstalled) values (45, "Adobe Acrobat", curdate());
            statementStrBuilder.append("INSERT INTO ");
            statementStrBuilder.append(this.table);
            statementStrBuilder.append(" (");
            for (int i = 0; i < this.columnList.size(); i++)
            {
                SQLUpdateData dType = this.columnList.get(i);
                statementStrBuilder.append(dType.getName());
                if (i + 1 != this.columnList.size())
                {
                    statementStrBuilder.append(", ");
                }
            }
            // We do not allow an empty where clause to limit the update.
            statementStrBuilder.append(") VALUES (");
            for (int i = 0; i < this.columnList.size(); i++)
            {
                statementStrBuilder.append("?");
                if (i + 1 != this.columnList.size())
                {
                    statementStrBuilder.append(", ");
                }
            }
            statementStrBuilder.append(")");
            connection = this.connector.getConnection();
            pStatement = connection.prepareStatement(statementStrBuilder.toString());
            // Now change the values in the prepared statement to include null values.
            for (int i = 0; i < this.columnList.size(); i++)
            {
                SQLUpdateData dType = this.columnList.get(i);
                int columnNumber = i + 1;
                if (dType.getValue() == null)
                {
                    if (dType.getType() == SQLData.Type.INT)
                    {
                        pStatement.setNull(columnNumber, java.sql.Types.INTEGER);
                    }
                    else if (dType.getType() == SQLData.Type.DATE)
                    {
                        pStatement.setNull(columnNumber, java.sql.Types.DATE);
                    }
                    else // dType.getType() == SQLData.Type.STRING
                    {
                        pStatement.setNull(columnNumber, java.sql.Types.VARCHAR);
                    }
                }
                else // Statement doesn't include a null value.
                {
                    if (dType.getType() == SQLData.Type.INT)
                    {
                        pStatement.setInt(columnNumber, Integer.valueOf(dType.getValue()));
                    }
                    else if (dType.getType() == SQLData.Type.DATE)
                    {
                        pStatement.setDate(columnNumber, Date.valueOf(dType.getValue()));
                    }
                    else // dType.getType() == SQLData.Type.STRING
                    {
                        pStatement.setString(columnNumber, dType.getValue());
                    }
                }
            }
            // Execute the SQL, and yes JDBC has helpfully called the insert statement executeUpdate.
            pStatement.executeUpdate();
            status.setEnded(ResponseTypes.OK.ordinal());
            pStatement.close();
            // Don't close the connection, other commands could use it.
        }
        catch (SQLException ex)
        {
            System.out.println("**error executing statement '" + 
                    pStatement.toString() + "'.\n" + ex.getMessage());
            status.setError(ex);
            System.out.println("SQLException MSG:"+status.getStderr());
            status.setResponseType(ResponseTypes.FAIL);
        }
        return status;
    }
    
    @Override
    public String toString()
    {
        return "Not completed yet";
    }
    
}
