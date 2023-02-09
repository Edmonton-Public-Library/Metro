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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mecard.ResponseTypes;
import mecard.exception.ConfigurationException;
import mecard.util.DateComparer;

/**
 * Handles basic SQL insert statement via JDBC.
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class SQLInsertCommand implements Command 
{
    public final static String NULL = "NULL";
    private final SQLConnector connector;
    private final List<SQLUpdateData> columnList;
    private final String table;
    private final boolean usesNamedColumns;
    
    /**
     * The builder object that constructs the SQL Insert command.
     */
    public static class Builder
    {
        private SQLConnector connector;
        private String table; // The name of the table.
        private List<SQLUpdateData> columnList; // Column names you wish to see in selection.
        private boolean usesNamedColumns;
        
        /**
         * All SQL INSERT commands require a SQL connector, and a target table
         * name for the insert command.
         * @param s SQL connector which must not be null.
         * @param tableName the target table for the insert.
         */
        public Builder(SQLConnector s, String tableName)
        {
            if (s == null)
            {
                System.out.println(SQLInsertCommand.class.getName() 
                        + " received null connector as argument.");
                throw new ConfigurationException();
            }
            this.connector  = s;
            this.table      = tableName;
            this.columnList = new ArrayList<>();
            this.usesNamedColumns = false;
        }
        
        /**
         * Another option for construction that specifies that the columns will 
         * be specified along with the data for the columns. Typically used when
         * populating some of all the possible columns of data. The remaining
         * unmentioned columns are filled with default values.
         * @param s SQL connector which must not be null.
         * @param tableName the target table for the insert.
         * @param useNamedColumQuery specifies that all columns will be named
         * during the insert command. Best practice.
         */
        public Builder(SQLConnector s, String tableName, boolean useNamedColumQuery)
        {
            if (s == null)
            {
                System.out.println(SQLInsertCommand.class.getName() 
                        + " received null connector as argument.");
                throw new ConfigurationException();
            }
            this.connector  = s;
            this.table      = tableName;
            this.columnList = new ArrayList<>();
            this.usesNamedColumns = useNamedColumQuery;
        }
        
        /**
         * Inserts a date value into a named column.
         * @param dName the name of the column.
         * @param date the date value to be stored.
         * @return builder object.
         */
        public Builder date(String dName, String date)
        {
            if (this.isNullOrEmpty(date))
            {
                SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.DATE, null);
                this.columnList.add(d);
                return this;
            }
            SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.DATE, date);
            this.columnList.add(d);
            return this;
        }
        
        /**
         * Inserts a null value into a named date column.
         * @param dName the name of the column that is expected to be a date column.
         * @return builder object.
         */
        public Builder date(String dName)
        {
            SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.DATE, null);
            this.columnList.add(d);
            return this;
        }
        
        /**
         * Inserts a null value into a date time column.
         * @param dName the name of the column that is expected to be a date column.
         * @return builder object.
         */
        public Builder dateTime(String dName)
        {
            SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.TIMESTAMP, null);
            this.columnList.add(d);
            return this;
        }
        
        /**
         * Inserts a null value into a column that is specified to take a small
         * integer, or null value.
         * @param sName the name of the column that is expected to be a small
         * integer column.
         * @return builder object.
         */
        public Builder smallInt(String sName)
        {
            SQLUpdateData d = new SQLUpdateData(sName, SQLData.Type.SMALL_INT, null);
            this.columnList.add(d);
            return this;
        }
        
        /**
         * Inserts a small integer value into a column that is specified to 
         * take a small integer, or null value in database schema.
         * @param sName the name of the column that is expected to be a small
         * integer column.
         * @param smallValue the small integer value to be stored in said column.
         * @return builder object.
         */
        public Builder smallInt(String sName, String smallValue)
        {
            if (this.isNullOrEmpty(smallValue))
            {
                SQLUpdateData d = new SQLUpdateData(sName, SQLData.Type.SMALL_INT, null);
                this.columnList.add(d);
                return this;
            }
            SQLUpdateData d = new SQLUpdateData(sName, SQLData.Type.SMALL_INT, smallValue);
            this.columnList.add(d);
            return this;
        }
        
        /**
         * Enters a given date and time based on the argument.
         * @param dName The name of the field where the timestamp will be stored.
         * @param dateTime The actual date and time.
         * @return Builder object.
         */
        public Builder dateTime(String dName, String dateTime)
        {
            if (this.isNullOrEmpty(dateTime))
            {
                SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.TIMESTAMP, null);
                this.columnList.add(d);
                return this;
            }
            SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.TIMESTAMP, dateTime);
            this.columnList.add(d);
            return this;
        }
        
        /**
         * Use this function in place of 'GETDATE()' function. Creates a default
         * time stamp of 'now' in 'yyyy-MM-dd HH:mm:ss' format.
         * @param dName Name of the field that will get the now timestamp.
         * @return Builder object.
         */
        public Builder dateTimeNow(String dName)
        {
            String now = DateComparer.getNowSQLTimeStamp();
            SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.TIMESTAMP, now);
            this.columnList.add(d);
            return this;
        }
        
        /**
         * Inserts a tiny integer value into a column specified to take that 
         * data type in the database schema.
         * @param tName the name of the column to insert the data.
         * @param value the value to be stored, the tiny integer.
         * @return builder object.
         */
        public Builder tinyInt(String tName, String value)
        {
            if (this.isNullOrEmpty(value))
            {
                SQLUpdateData i = new SQLUpdateData(tName, SQLData.Type.TINY_INT, null);
                this.columnList.add(i);
                return this;
            }
            SQLUpdateData i = new SQLUpdateData(tName, SQLData.Type.TINY_INT, value);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Inserts a null value into a table column that is specified to take
         * a tiny integer.
         * @param tName column name.
         * @return builder object.
         */
        public Builder tinyInt(String tName)
        {
            SQLUpdateData i = new SQLUpdateData(tName, SQLData.Type.TINY_INT, null);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Sets a value in a database table to type CHAR.
         * @param cName char column name.
         * @param value the CHAR value to store.
         * @return builder object.
         */
        public Builder setChar(String cName, String value)
        {
            if (this.isNullOrEmpty(value))
            {
                SQLUpdateData i = new SQLUpdateData(cName, SQLData.Type.CHAR, null);
                this.columnList.add(i);
                return this;
            }
            // Data type is String to JDBC.
            SQLUpdateData i = new SQLUpdateData(cName, SQLData.Type.CHAR, value);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Sets a value in a database table to type integer.
         * @param iName column name.
         * @param value the integer to store.
         * @return builder object.
         */
        public Builder integer(String iName, String value)
        {
            if (this.isNullOrEmpty(value))
            {
                SQLUpdateData i = new SQLUpdateData(iName, SQLData.Type.INT, null);
                this.columnList.add(i);
                return this;
            }
            SQLUpdateData i = new SQLUpdateData(iName, SQLData.Type.INT, value);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Sets the specified field's value in the target table to null as defined
         * by the JDBC definition of null.
         * @param iName column name to store value.
         * @return builder object.
         */
        public Builder integer(String iName)
        {
            SQLUpdateData i = new SQLUpdateData(iName, SQLData.Type.INT, null);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Inserts a string value into an SQL table.
         * @param cName - name of the column in the table.
         * @param value value to store which can be null, or "NULL".
         * @return builder object.
         */
        public Builder string(String cName, String value)
        {
            if (this.isNullOrEmpty(value))
            {
                SQLUpdateData d = new SQLUpdateData(cName, SQLData.Type.DATE, null);
                this.columnList.add(d);
                return this;
            }
            SQLUpdateData s = new SQLUpdateData(cName, SQLData.Type.STRING, value);
            this.columnList.add(s);
            return this;
        }
        
        /**
         * Inserts a null in to a column that specifies 'string' in the schema.
         * @param sName name of the string column.
         * @return builder object.
         */
        public Builder string(String sName)
        {
            SQLUpdateData i = new SQLUpdateData(sName, SQLData.Type.STRING, null);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Stores given money amount in a JDBC compatible data type. Also accepts
         * null as a valid amount.
         * @param mName
         * @param amount, amount of money to store, may be 'null'.
         * @return Builder
         */
        public Builder money(String mName, String amount)
        {
            // Permits objects like PolarisSQLFormattedCustomer to explicitly set
            // a value to "NULL" and actually have it set to null, not "null" as a string.
            if (this.isNullOrEmpty(amount))
            {
                SQLUpdateData i = new SQLUpdateData(mName, SQLData.Type.MONEY, null);
                this.columnList.add(i);
                return this;
            }
            SQLUpdateData i = new SQLUpdateData(mName, SQLData.Type.MONEY, amount);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Stores null money values.
         * @param mName name of the money column.
         * @return Builder object.
         */
        public Builder money(String mName)
        {
            SQLUpdateData i = new SQLUpdateData(mName, SQLData.Type.MONEY, null);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Stores a bit value in SQL type of BIT.
         * @param bName name of the 'bit' column.
         * @return Builder object.
         */
        public Builder bit(String bName)
        {
            SQLUpdateData i = new SQLUpdateData(bName, SQLData.Type.BIT, null);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Creates a SQL bit field.
         * @param bName must be a string of either true or '1', anything else 
         * will be interpreted as being a bit value of '0'. Null permitted permitted.
         * @param value bit value of either true or '1' to be set and anything else
         * will set the value to '0', except null which will store a null value.
         * @return Builder object.
         */
        public Builder bit(String bName, String value)
        {
            SQLUpdateData i;
            if (this.isNullOrEmpty(value))
            {
                i = new SQLUpdateData(bName, SQLData.Type.BIT, null);
                this.columnList.add(i);
                return this;
            }
            else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1"))
            {
                i = new SQLUpdateData(bName, SQLData.Type.BIT, "true");
                this.columnList.add(i);
                return this;
            }
            i = new SQLUpdateData(bName, SQLData.Type.BIT, "false");
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Stores a (stored) procedure call with a single string parameter.
         * @param procedureName name of the procedure qualified by the database
         * where the procedure is defined. Example: 'Polaris.Circ_SetPatronPassword'
         * @param procedureParameter string argument to add to the procedure call.
         * @return Builder.
         */
        public Builder procedure(String procedureName, String procedureParameter)
        {
            SQLUpdateData i = new SQLUpdateData(
                    procedureName, 
                    SQLData.Type.STORED_PROCEEDURE, 
                    procedureParameter);
            this.columnList.add(i);
            return this;
        }
                
        /**
         * Tests if the argument is empty or null for sanitizing input for the
         * query string.
         * @param s string to test.
         * @return true if the string is empty, null, or contains the literal
         * string 'null' (case insensitively).
         */
        private boolean isNullOrEmpty(String s)
        {
            // explicit string "null" ingore case.
            return s == null               // null object type
                    || s.isEmpty()      // empty string like optional birthdates
                    || s.equalsIgnoreCase("NULL");
        }
        
        /**
         * Builds and returns the new SQLInsertCommand object.
         * @return SQLInsertCommand.
         */
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
        this.connector        = builder.connector;
        this.columnList       = builder.columnList;
        this.table            = builder.table;
        this.usesNamedColumns = builder.usesNamedColumns;
    }
    
    /**
     * 
     * @return String version of the insert statement.
     */
    private String getStatementString()
    {
        StringBuilder statementStrBuilder = new StringBuilder();
        // insert INTO software (station, title, DateInstalled) values (45, "Adobe Acrobat", curdate());
        statementStrBuilder.append("INSERT INTO ");
        statementStrBuilder.append(this.table);
        if (this.usesNamedColumns)
        {
            /*
            Normally in an insert command we specify it like so:
            INSERT INTO Table (col_name_1, col_name_2) VALUES ("one", "two");
            but we need to insert into the table with a  

            // INSERT INTO Students VALUES (100,'Zara','Ali', {d '2001-12-16'});
            // https://www.tutorialspoint.com/jdbc/jdbc-stored-procedure.htm*/
            statementStrBuilder.append(" (");

            for (int i = 0; i < this.columnList.size(); i++)
            {
                SQLUpdateData sqlData = this.columnList.get(i);
                statementStrBuilder.append(sqlData.getName());
                if (i + 1 != this.columnList.size())
                {
                    statementStrBuilder.append(", ");
                }
            }
            // We do not allow an empty where clause to limit the update.
            statementStrBuilder.append(") VALUES (");
        }
        else
        {
            statementStrBuilder.append(" VALUES (");
        }
        for (int i = 0; i < this.columnList.size(); i++)
        {
            SQLUpdateData sqlData = this.columnList.get(i);
            statementStrBuilder.append(sqlData.toQueryString());
            if (i + 1 != this.columnList.size())
            {
                statementStrBuilder.append(", ");
            }
        }
        statementStrBuilder.append(")");
        return statementStrBuilder.toString();
    }
    
    /**
     * As the name suggests, prepares the SQL statement for execution.
     * @return a well formed SQL update statement.
     * @throws SQLException 
     */
    private PreparedStatement getPreparedStatement() throws SQLException
    {
        Connection connection;
        PreparedStatement pStatement;
        connection = this.connector.getConnection();
        pStatement = connection.prepareStatement(this.getStatementString());
        // Now add the values to the prepareStatement.
        for (int i = 0; i < this.columnList.size(); i++)
        {
            SQLUpdateData sqlData = this.columnList.get(i);
            int columnNumber = i + 1;
            if (sqlData.getValue() == null)
            {
                if (null != sqlData.getType())
                switch (sqlData.getType())
                {
                    case INT:
                        pStatement.setNull(columnNumber, java.sql.Types.INTEGER);
                        break;
                    case DATE:
                        pStatement.setNull(columnNumber, java.sql.Types.DATE);
                        break;
                    case MONEY:
                        pStatement.setNull(columnNumber, java.sql.Types.DECIMAL);
                        break;
                    case BIT:
                        pStatement.setNull(columnNumber, java.sql.Types.BIT);
                        break;
                    case TIMESTAMP:
                        pStatement.setNull(columnNumber, java.sql.Types.TIMESTAMP);
                        break;
                    case SMALL_INT:
                        pStatement.setNull(columnNumber, java.sql.Types.SMALLINT);
                        break;
                    case TINY_INT:
                        pStatement.setNull(columnNumber, java.sql.Types.TINYINT);
                        break;
                    case CHAR:
                        pStatement.setNull(columnNumber, java.sql.Types.CHAR);
                        break;
                    case STORED_PROCEEDURE:
                        break; // we don't have to add anything, there are no params if null
                        // it's just a call with an empty parameter list, like 'today()'.
                // sqlData.getType() == SQLData.Type.STRING
                    default:
                        pStatement.setNull(columnNumber, java.sql.Types.VARCHAR);
                        break;
                }
            }
            else // Statement doesn't include a null value.
            {
                if (null != sqlData.getType())
                switch (sqlData.getType())
                {
                    case INT:
                        pStatement.setInt(columnNumber, Integer.valueOf(sqlData.getValue()));
                        break;
                    case DATE:
                        pStatement.setDate(columnNumber, Date.valueOf(sqlData.getValue()));
                        break;
                    case MONEY:
                        //                    pStatement.setDate(columnNumber, Date.valueOf(sqlData.getValue()));
                        BigDecimal bd = new BigDecimal(sqlData.getValue());
                        pStatement.setBigDecimal(columnNumber, bd);
                        break;
                    case BIT:
                        pStatement.setBoolean(columnNumber, Boolean.valueOf(sqlData.getValue()));
                        break;
                    case TIMESTAMP:
                        pStatement.setTimestamp(columnNumber, java.sql.Timestamp.valueOf(sqlData.getValue()));
                        break;
                    case SMALL_INT:
                        pStatement.setShort(columnNumber, Short.valueOf(sqlData.getValue()));
                        break;
                    case TINY_INT:
                        // Tiny int similar to SMALL_INT for JDBC.
                        pStatement.setShort(columnNumber, Short.valueOf(sqlData.getValue()));
                        break;
                    case CHAR:
                        // Char handled as String JDBC so fall through to default; same as string.
                    case STORED_PROCEEDURE:
//                        fall through, same as string.
                // sqlData.getType() == SQLData.Type.STRING
                    default:
                        pStatement.setString(columnNumber, sqlData.getValue());
                        break;
                }
            }
        }
        return pStatement;
    }
    
    /** 
     * Used to add an extra value without having to go through the entire 
     * builder just to conditionally add another column.
     * @param name Name of the value which is the column name in the table.
     * @param type The SQL data type like int or bit.
     * @param value What ever that value is supposed to be set to.
     */
    public void oopsForgot(String name, SQLData.Type type, String value)
    {
        SQLUpdateData i = new SQLUpdateData(name, type, value);
        this.columnList.add(i);
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
