/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2020  Edmonton Public Library
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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mecard.ResponseTypes;
import mecard.exception.ConfigurationException;

/**
 * Stored procedure call object.
 * Looks like: "{call demoSp(?, ?)}" in JDBC.
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class SQLStoredProcedureCommand implements Command
{
    private final SQLConnector connector;
    private final List<SQLUpdateData> columnList;
    private final String procedureFunction; // contents of the procedural call.
    private final String procedureInvocation; // EXEC, call, or fn
    // To be able to distinguish the two types of procedure calls either
    // Statement	Use this for general-purpose access to your database. 
    //        Useful when you are using static SQL statements at runtime. 
    //        The Statement interface cannot accept parameters.
    // PreparedStatement	Use this when you plan to use the SQL statements many times. 
    //        The PreparedStatement interface accepts input parameters at runtime.
    // CallableStatement	Use this when you want to access the database stored procedures. 
    //        The CallableStatement interface can also accept runtime input parameters.
    public enum StatementType
    {
        STATEMENT,
        PREPARED_STATEMENT,
        CALLABLE_STATEMENT;
    }
    private final StatementType statementType;
    public enum ReturnType
    {
        NONE,      // don't expect any results.
        OTHER,     // Non-Java object.
        NVARCHAR,  // not currently implemented.
        INTEGER,
        BOOLEAN;
    }
    private final ReturnType returnType;
    
    public static class Builder
    {
        private SQLConnector connector;
        private final List<SQLUpdateData> columnList; // Column names you wish to see in selection.
        private final String procedureFunction;
        private final String procedureInvocation;
        private StatementType statementType;
        private ReturnType   returnType;
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
            this.statementType = StatementType.PREPARED_STATEMENT;
            this.returnType    = ReturnType.NONE;
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
        
        /**
         * Indicates that the call expects a return value.
         * @param returnType call back for results. Currently the supported 
         * types are BOOLEAN, OTHER, NONE, INTEGER, and NVARCHAR.
         * @return Builder.
         */
        public Builder returns(String returnType)
        {
            // REF: https://www.tutorialspoint.com/jdbc/jdbc-statements.htm
            // REF: simple example of return from a procedure call:
            // https://www.tutorialspoint.com/jdbc/jdbc-stored-procedure.htm
            // REF: Example code of setting prarameter values and return values with JDBC.
            // http://www.java2s.com/Code/Java/Database-SQL-JDBC/Gettinganoutputparameterfromastoredprocedure.htm
            // TODO: Finish this by adding code for receiving a return result (ResultSet).
            // All of this says that we need to make this a CallableStatement.
            this.statementType = StatementType.CALLABLE_STATEMENT;
            switch (returnType.toUpperCase())
            {
                case "BOOLEAN":
//                    boolean execute (String SQL): 
//                    Returns a boolean value of 
//                    true if a ResultSet object can be retrieved; otherwise, 
//                    it returns false. Use this method to execute SQL DDL 
//                    statements or when you need to use truly dynamic SQL.
                    this.returnType = ReturnType.BOOLEAN;
                    break;
                case "INTEGER":
//                    int executeUpdate (String SQL): Returns the number of rows 
//                    affected by the execution of the SQL statement. Use this 
//                    method to execute SQL statements for which you expect to 
//                    get a number of rows affected - for example, an INSERT, 
//                    UPDATE, or DELETE statement.
                    // Change this to something if you need a different return type.
                    this.returnType = ReturnType.INTEGER;
                    break;
                case "NVARCHAR":
                    this.returnType = ReturnType.NVARCHAR;
                    break;
                case "NONE":
                    // Returns this object to a statement or a prepared statement.
                    // Choose prepared statement as more likely, that is there 
                    // will be a parameter passed at some point to the SPCall.
                    this.statementType = StatementType.PREPARED_STATEMENT;
                    this.returnType    = ReturnType.NONE;
                    break;
                case "OTHER":
                    // ResultSet executeQuery (String SQL): Returns a ResultSet 
                    // object. Use this method when you expect to get a result 
                    // set, as you would with a SELECT statement.
                    // Use this because we don't need the results and the vendor
                    // will likely return a proprietary object type.
                    this.returnType = ReturnType.OTHER;
                    break;
                default:
                    // will likely return a proprietary object type.
                    this.returnType = ReturnType.OTHER;
                    break;
            }
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
        this.statementType = builder.statementType;
        this.returnType = builder.returnType;
    }
    
    /**
     * 
     * @return String statement string of the procedure.
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
        switch (this.statementType)
        {
            case CALLABLE_STATEMENT:
                statementStrBuilder.append("{? = ");
                break;
            // The rest all prefix the request in JDBC the same way '{fn|call <procedure>([param])'
            case STATEMENT:
            case PREPARED_STATEMENT:
            default:
                statementStrBuilder.append("{");
                break;
        }
        statementStrBuilder.append(this.procedureInvocation).append(" ").append(this.procedureFunction).append("(");
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
    
    @Override
    public CommandStatus execute()
    {
        CommandStatus status = new CommandStatus();
        Connection connection;
        connection = this.connector.getConnection();
        try
        {
            switch (this.statementType)
            {
                case STATEMENT:
                    Statement statement = connection.createStatement();
                    if (statement.execute(this.getStatementString()))
                    {
                        System.out.print("STATEMENT: '" +
                                this.getStatementString() +
                                " returned true.");
                    }
                    else
                    {
                        System.out.print("STATEMENT: '" +
                                this.getStatementString() +
                                " returned false.");
                    }
                    status.setEnded(ResponseTypes.OK.ordinal());
                    statement.close();
                    // Close the statement but not the connection other 
                    // commands could use it.
                    break;
                case CALLABLE_STATEMENT:
                    CallableStatement cStatement;
                    cStatement = connection.prepareCall(
                        this.getStatementString(),
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                    // Now add the parameter values
                    for (int i = 0; i < this.columnList.size(); i++)
                    {
                        SQLUpdateData sqlData = this.columnList.get(i);
                        cStatement.setString((i +1), sqlData.getValue());
                    }
                    // register the return value expected. Since we are using 
                    // a MS Sequel server, I set the type to OTHER, since it 
                    // isn't any of the other JDBCTypes listed.
                    // The result will be the last parameter or the size of the
                    // input parameters +1.
                    int paramIndex = this.columnList.size() +1;
                    switch (this.returnType)
                    {
                        case NVARCHAR:
                            cStatement.registerOutParameter(
                                paramIndex, 
                                java.sql.JDBCType.NVARCHAR);
                            break;
                        case BOOLEAN:
                            cStatement.registerOutParameter(
                                paramIndex, 
                                java.sql.JDBCType.BOOLEAN);
                            break;
                        case INTEGER:
                            cStatement.registerOutParameter(
                                paramIndex, 
                                java.sql.JDBCType.INTEGER);
                            break;
                        case NONE:
                            // don't register an output parameter at all.
                            break;
                        case OTHER:
                            cStatement.registerOutParameter(
                                paramIndex, 
                                java.sql.JDBCType.OTHER);
                            break;
                        default:
                            cStatement.registerOutParameter(
                                paramIndex, 
                                java.sql.JDBCType.OTHER);
                            break;
                    }
                    
                    // Execute the SQL, and yes JDBC has helpfully called the insert statement executeUpdate.
                    cStatement.executeUpdate();
                    // Here I print the return result to output since the rest of 
                    // the ResultSet isn't used. If that changes the code will have
                    // to be modified to use a callback or other mechanizm to
                    // hand-off the results for another process.
                    switch (this.returnType)
                    {
                        case NVARCHAR:
                            System.out.println("return NVARCHAR: '" + 
                                cStatement.getNString(paramIndex) + "'\n");
                            break;
                        case INTEGER:
                            System.out.println("return INTEGER: '" + 
                                cStatement.getInt(paramIndex) + "'\n");
                            break;
                        case BOOLEAN:
                            System.out.println("return BOOLEAN: '" + 
                                cStatement.getBoolean(paramIndex) + "'\n");
                            break;
                        case OTHER:
                            System.out.println("return OTHER: returned object\n");
                            // We currently don't have a use for the results.
//                            ResultSet resultSet = cStatement.getResultSet();
//                            System.out.println("return OTHER: '" + 
//                                cStatement.getGeneratedKeys() + "'\n");
                            System.out.println(
                                cStatement.getObject(paramIndex).toString());
                            break;
                        default:
                            System.out.println("return OTHER: returned object (@ default).\n" +
                                cStatement.getObject(paramIndex).toString());
                            break;
                    }
                    status.setEnded(ResponseTypes.OK.ordinal());
                    cStatement.close();
                    // Close the statement but not the connection other 
                    // commands could use it.
                    break;

                case PREPARED_STATEMENT:
                default:
                    PreparedStatement pStatement = null;
                    pStatement = connection.prepareStatement(
                            this.getStatementString()
                    );
                    // Now add the parameter values
                    for (int i = 0; i < this.columnList.size(); i++)
                    {
                        SQLUpdateData sqlData = this.columnList.get(i);
                        pStatement.setString((i +1), sqlData.getValue());
                    } 
                    // Execute the SQL, and yes JDBC has helpfully called the insert statement executeUpdate.
                    pStatement.executeUpdate();
                    status.setEnded(ResponseTypes.OK.ordinal());
                    pStatement.close();
                    // Don't close the connection, other commands could use it.
                    break;
            }
        }
        catch (SQLException ex)
        {
            System.out.println("**error executing statement: " +
                    this.toString() + "\n" +
                    ex.getMessage());
            status.setError(ex);
            System.out.println("SQLException MSG:" + status.getStderr());
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
