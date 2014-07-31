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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private final String statementString;
    private final List<SQLData> statementList;
    
    public static class Builder
    {
        private SQLConnector connector;
        private String table; // The name of the table.
        private List<SQLData> columnList; // Column names you wish to see in selection.
        private String whereClause;
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
            this.whereClause = "";
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
        
        public Builder where(String whereClause)
        {
            this.whereClause = whereClause;
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
        this.connector = builder.connector;
        this.statementList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (SQLData dType: builder.columnList)
        {
            sb.append(dType.getName());
            this.statementList.add(dType); // keep for execute()
            if (builder.columnList.size() != this.statementList.size())
            {
                sb.append(", ");
            }
        }
        sb.append(" FROM ");
        sb.append(builder.table);
        if (builder.whereClause != null)
        {
            sb.append(" WHERE ");
            sb.append(builder.where.toString());
        }
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
            // Execute the SQL
            ResultSet rs = statement.executeQuery(statementString);
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
            statement.close();
            // Don't close the connection, other commands might need it.
        }
        catch (SQLException ex)
        {
            System.out.println("**error executing statement '" + 
                    statementString + "'.\n" + ex.getMessage());
            status.setError(ex);
            System.out.println("SQLException MSG:"+status.getStderr());
            status.setResponseType(ResponseTypes.FAIL);
        }
        return status;
    }
    
    @Override
    public String toString()
    {
        return this.statementString;
    }
}
