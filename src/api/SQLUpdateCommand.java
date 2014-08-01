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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mecard.ResponseTypes;
import mecard.exception.ConfigurationException;

/**
 * Handles basic SQL selection statement via JDBC.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SQLUpdateCommand implements Command
{
    private final SQLConnector connector;
    private final List<SQLUpdateData> columnList;
    private final String table;
    private final SQLUpdateData where;
    
    public static class Builder
    {
        private SQLConnector connector;
        private String table; // The name of the table.
        private List<SQLUpdateData> columnList; // Column names you wish to see in selection.
        private SQLUpdateData where;
        
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
            this.columnList = new ArrayList<>();
            this.where = null;
        }
        
        public Builder whereString(String key, String value)
        {
            return this.where(value, SQLData.Type.STRING, value);
        }
        
        public Builder whereInteger(String key, String value)
        {
            return this.where(key, SQLData.Type.INT, value);
        }
        
        public Builder whereDate(String key, String value)
        {
            return this.where(key, SQLData.Type.DATE, value);
        }
        
        private Builder where(String key, SQLData.Type type, String value)
        {
            this.where = new SQLUpdateData(key, type, value);
            return this;
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
            SQLUpdateData i = new SQLUpdateData(iName, SQLData.Type.INT, value);
            this.columnList.add(i);
            return this;
        }
        
        public SQLUpdateCommand build()
        {
            if (this.where == null)
            {
                throw new ConfigurationException("Empty WHERE clause not permitted.");
            }
            return new SQLUpdateCommand(this);
        }
    }
    
    private SQLUpdateCommand(Builder builder)
    {
        this.connector  = builder.connector;
        this.columnList = builder.columnList;
        this.table      = builder.table;
        this.where      = builder.where;
    }
    
    private PreparedStatement getPreparedStatement() throws SQLException
    {
        PreparedStatement pStatement = null;
        Connection connection = null;
        StringBuilder statementStrBuilder = new StringBuilder();
        // update <table> set <col>=<value> where <condition>=<test>;
        statementStrBuilder.append("UPDATE ");
        statementStrBuilder.append(this.table);
        statementStrBuilder.append(" SET ");
        for (int i = 0; i < this.columnList.size(); i++)
        {
            SQLUpdateData dType = this.columnList.get(i);
            statementStrBuilder.append(dType.getName());
            statementStrBuilder.append(" = ?");
            // separate multiple 'set' values.
            if (i + 1 != this.columnList.size())
            {
                statementStrBuilder.append(", ");
            }
        }
        // We do not allow an empty where clause to limit the update.
        statementStrBuilder.append(" WHERE ");
        statementStrBuilder.append(this.where.getName());
        statementStrBuilder.append(" = ?");
        // Time to set up the connection and set any null values.
        connection = this.connector.getConnection();
        pStatement = connection.prepareStatement(statementStrBuilder.toString());
        // Set this here because we need it for the where clause at the end.
        int columnNumber = 0;
        // Now add the values you want to set in the statement
        for (int i = 0; i < this.columnList.size(); i++)
        {
            SQLUpdateData dType = this.columnList.get(i);
            columnNumber++;
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
                    pStatement.setDate(columnNumber, java.sql.Date.valueOf(dType.getValue()));
                }
                else // dType.getType() == SQLData.Type.STRING
                {
                    pStatement.setString(columnNumber, dType.getValue());
                }
            }
        }
        // Now fill out the 'WHERE' template which can have the same restrictions
        columnNumber++;
        if (this.where.getValue() == null)
        {
            if (this.where.getType() == SQLData.Type.INT)
            {
                pStatement.setNull(columnNumber, java.sql.Types.INTEGER);
            }
            else if (this.where.getType() == SQLData.Type.DATE)
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
            if (this.where.getType() == SQLData.Type.INT)
            {
                pStatement.setInt(columnNumber, Integer.valueOf(this.where.getValue()));
            }
            else if (this.where.getType() == SQLData.Type.DATE)
            {
                pStatement.setDate(columnNumber, java.sql.Date.valueOf(this.where.getValue()));
            }
            else // dType.getType() == SQLData.Type.STRING
            {
                pStatement.setString(columnNumber, this.where.getValue());
            }
        }
        return pStatement;
    }
    
    @Override
    public CommandStatus execute()
    {   
        CommandStatus status = new CommandStatus();
        PreparedStatement pStatement = null;
        try
        {
            pStatement = this.getPreparedStatement();
            // Execute the SQL
            pStatement.executeUpdate();
            status.setEnded(ResponseTypes.OK.ordinal());
            pStatement.close();
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
        PreparedStatement pStatement = null;
        String result;
        try
        {
            pStatement = this.getPreparedStatement();
            result = pStatement.toString();
            pStatement.close();
        }
        catch (SQLException ex)
        {
            result = "**error in statement '" + 
                    pStatement + "'.\n" + ex.getMessage();
        }
        return result;
    }
}
