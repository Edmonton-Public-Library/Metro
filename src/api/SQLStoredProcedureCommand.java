/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2017  Edmonton Public Library
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
 * Stored procedure call object.
 * Looks like: "{call demoSp(?, ?)}" in JDBC.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SQLStoredProcedureCommand implements Command
{
    private final SQLConnector connector;
    private final List<SQLUpdateData> columnList;
    private final String procedureFunction;
    private final String procedureInvocation;
    
    public static class Builder
    {
        private SQLConnector connector;
        private final List<SQLUpdateData> columnList; // Column names you wish to see in selection.
        private final String procedureFunction;
        private final String procedureInvocation;
        public Builder(SQLConnector s, String procedureFunction, String invocationMethod)
        {
            if (s == null)
            {
                System.out.println(SQLInsertCommand.class.getName() 
                        + " received null connector as argument.");
                throw new ConfigurationException();
            }
            this.connector  = s;
            this.columnList = new ArrayList<>();
            this.procedureFunction = procedureFunction;
            this.procedureInvocation = invocationMethod;
        }
        
        /**
         * Stores a (stored) procedure call with a single string parameter.
         * @param parameterName name of the procedure qualified by the database
         * where the procedure is defined. Example: 'Polaris.Circ_SetPatronPassword'
         * @param parameterValue string argument to add to the procedure call.
         * @return Builder.
         */
        public Builder string(String parameterName, String parameterValue)
        {
            SQLUpdateData i = new SQLUpdateData(
                    parameterName, 
                    SQLData.Type.STRING, 
                    parameterValue);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Stores a (stored) procedure call with a single string parameter.
         * @param parameterName name of the procedure qualified by the database
         * where the procedure is defined. Example: 'Polaris.Circ_SetPatronPassword'
         * @param parameterValue string argument to add to the procedure call.
         * @return Builder.
         */
        public Builder integer(String parameterName, String parameterValue)
        {
            SQLUpdateData i = new SQLUpdateData(
                    parameterName, 
                    SQLData.Type.INT, 
                    parameterValue);
            this.columnList.add(i);
            return this;
        }
        
        public SQLStoredProcedureCommand build()
        {
            return new SQLStoredProcedureCommand(this);
        }
    }
    
    /**
     * Constructor.
     * @param builder 
     */
    private SQLStoredProcedureCommand(Builder builder)
    {
        this.connector = builder.connector;
        this.columnList= builder.columnList;
        this.procedureFunction = builder.procedureFunction;
        this.procedureInvocation = builder.procedureInvocation;
    }
    
    /**
     * 
     * @return String version of the insert statement.
     */
    private String getStatementString()
    {
        StringBuilder statementStrBuilder = new StringBuilder();
        /*
        Normally in an insert command we specify it like so:
        String SQL = "{call getEmpName (?, ?)}";
        cstmt = conn.prepareCall (SQL);
        // https://www.tutorialspoint.com/jdbc/jdbc-stored-procedure.htm
        */
        statementStrBuilder.append("{").append(this.procedureInvocation).append(" ").append(this.procedureFunction).append("(");
        for (SQLUpdateData param: this.columnList)
        {
            statementStrBuilder.append("?,");
        }
        // Remove the last comma after the last param.
        if (this.columnList.size() > 0)
        {
            statementStrBuilder = new StringBuilder(statementStrBuilder.substring(0, (statementStrBuilder.length() -1)));
        }
        statementStrBuilder.append(")}");
        System.out.println(">>>>>>" + statementStrBuilder.toString());
        return statementStrBuilder.toString();
    }
    
    /**
     * Populates the parameters of the stored procedure call.
     * @return Prepared SQL statement.
     * @throws SQLException if the statement is malformed.
     */
    private PreparedStatement getPreparedStatement() throws SQLException
    {
        Connection connection;
        PreparedStatement pStatement;
        connection = this.connector.getConnection();
        pStatement = connection.prepareStatement(this.getStatementString());
        // Now add the values
        for (int i = 0; i < this.columnList.size(); i++)
        {
            SQLUpdateData sqlData = this.columnList.get(i);
            pStatement.setString((i +1), sqlData.getValue());
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
            // Execute the SQL, and yes JDBC has helpfully called the insert statement executeUpdate.
            pStatement.executeUpdate();
            status.setEnded(ResponseTypes.OK.ordinal());
            pStatement.close();
            // Don't close the connection, other commands could use it.
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
        StringBuilder sBuff = new StringBuilder();
        sBuff.append(this.getStatementString());
//        sBuff.append(this.get)
        for (int i = 0; i < this.columnList.size(); i++)
        {
            SQLUpdateData sqlData = this.columnList.get(i);
            sBuff.append("\n")
                    .append("col.")
                    .append(i + 1)
                    .append("->")
                    .append(sqlData.toString());
        }
        sBuff.append("\n");
        return sBuff.toString();
    } 
}
