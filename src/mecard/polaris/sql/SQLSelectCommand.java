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
package mecard.polaris.sql;

import api.Command;
import api.CommandStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mecard.ResponseTypes;
import mecard.exception.ConfigurationException;

/**
 * Handles basic SQL selection statement via JDBC.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SQLSelectCommand implements Command
{
    private final SQLConnector connector;
    private final List<SQLData> statementList;
    private final String table;
    private final SQLUpdateData whereClause;
    
    public static class Builder
    {
        private SQLConnector connector;
        private String table; // The name of the table.
        private List<SQLData> columnList; // Column names you wish to see in selection.
        private SQLUpdateData where;
        
        public Builder(SQLConnector s, String tableName)
        {
            if (s == null)
            {
                System.out.println(SQLSelectCommand.class.getName() 
                        + " received null connector as argument.");
                throw new ConfigurationException();
            }
            this.connector   = s;
            this.table       = tableName;
            this.columnList  = new ArrayList<>();
            this.where       = null;
        }
        
        public Builder string(String cName)
        {
            SQLData s = new SQLData(cName, SQLData.Type.STRING);
            this.columnList.add(s);
            return this;
        }
        
        public Builder date(String dName)
        {
            SQLData d = new SQLData(dName, SQLData.Type.DATE);
            this.columnList.add(d);
            return this;
        }
        
        public Builder integer(String iName)
        {
            SQLData i = new SQLData(iName, SQLData.Type.INT);
            this.columnList.add(i);
            return this;
        }
        
        public Builder whereString(String key, String value)
        {
            return this.where(key, SQLData.Type.STRING, value);
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
        
        public SQLSelectCommand build()
        {
            return new SQLSelectCommand(this);
        }
    }
    
    private SQLSelectCommand(Builder builder)
    {
        this.connector     = builder.connector;
        this.statementList = builder.columnList;
        this.table         = builder.table;
        this.whereClause   = builder.where;
    }
    
    private PreparedStatement getPreparedStatement() throws SQLException
    {
        // Time to set up the connection.
        Connection connection = this.connector.getConnection();
        StringBuilder statementStrBuilder = new StringBuilder();
        // SELECT value FROM <table> where <condition>=<test>;
        statementStrBuilder.append("SELECT ");

        for (int i = 0; i < this.statementList.size(); i++)
        {
            SQLData dType = this.statementList.get(i);
            statementStrBuilder.append(dType.getName());
            if (i + 1 != this.statementList.size())
            {
                statementStrBuilder.append(", ");
            }
        }
        statementStrBuilder.append(" FROM ");
        statementStrBuilder.append(this.table);
        if (this.whereClause != null)
        {
            statementStrBuilder.append(" WHERE ");
            statementStrBuilder.append(this.whereClause.toString());
        }
        PreparedStatement pStatement = connection.prepareStatement(statementStrBuilder.toString());
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
            ResultSet rs = pStatement.executeQuery();
            while (rs.next())
            {
                StringBuilder output = new StringBuilder();
                // Here we will iterate over the columns the user requested.
                for (SQLData column: this.statementList)
                {
                    switch (column.getType())
                    {
                        case DATE:
                            Date d = rs.getDate(column.getName());
                            if (d == null) 
                            {
                                output.append("NULL");
                            }
                            else
                            {
                                output.append(d.toString());
                            }
                            break;
                        case INT:
                            int i = rs.getInt(column.getName());
                            output.append(String.valueOf(i));
                            break;
                        default:
                            String s = rs.getString(column.getName());
                            if (s == null) 
                            {
                                output.append("NULL");
                            }
                            else
                            {
                                output.append(s);
                            }
                            break;
                    }
                    output.append(" ");
                }
                status.setStdout(output.toString());
            }
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
