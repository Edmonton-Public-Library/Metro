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
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SQLInsertCommand implements Command 
{
    public final static String NULL = "NULL";
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
        
        /**
         * Inserts a string value into an SQL table.
         * @param cName - name of the column in the table.
         * @param value value to store which can be null, or "NULL".
         * @return builder object.
         */
        public Builder string(String cName, String value)
        {
            if (value == null || value.compareToIgnoreCase(NULL) == 0)
            {
                SQLUpdateData d = new SQLUpdateData(cName, SQLData.Type.DATE, null);
                this.columnList.add(d);
                return this;
            }
            SQLUpdateData s = new SQLUpdateData(cName, SQLData.Type.STRING, value);
            this.columnList.add(s);
            return this;
        }
        
        public Builder date(String dName, String date)
        {
            if (date == null || date.compareToIgnoreCase(NULL) == 0)
            {
                SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.DATE, null);
                this.columnList.add(d);
                return this;
            }
            SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.DATE, date);
            this.columnList.add(d);
            return this;
        }
        
        public Builder date(String dName)
        {
            SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.DATE, null);
            this.columnList.add(d);
            return this;
        }
        
        public Builder dateTime(String dName)
        {
            SQLUpdateData d = new SQLUpdateData(dName, SQLData.Type.TIMESTAMP, null);
            this.columnList.add(d);
            return this;
        }
        
        public Builder smallInt(String sName)
        {
            SQLUpdateData d = new SQLUpdateData(sName, SQLData.Type.SMALL_INT, null);
            this.columnList.add(d);
            return this;
        }
        
        public Builder smallInt(String sName, String smallValue)
        {
            if (smallValue == null || smallValue.compareToIgnoreCase(NULL) == 0)
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
            if (dateTime == null || dateTime.compareToIgnoreCase(NULL) == 0)
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
        
        public Builder tinyInt(String tName, String value)
        {
            if (value == null || value.compareToIgnoreCase(NULL) == 0)
            {
                SQLUpdateData i = new SQLUpdateData(tName, SQLData.Type.TINY_INT, null);
                this.columnList.add(i);
                return this;
            }
            SQLUpdateData i = new SQLUpdateData(tName, SQLData.Type.TINY_INT, value);
            this.columnList.add(i);
            return this;
        }
        
        public Builder tinyInt(String tName)
        {
            SQLUpdateData i = new SQLUpdateData(tName, SQLData.Type.TINY_INT, null);
            this.columnList.add(i);
            return this;
        }
        
        public Builder setChar(String cName, String value)
        {
            if (value == null || value.compareToIgnoreCase(NULL) == 0)
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
        
        public Builder integer(String iName, String value)
        {
            if (value == null || value.compareToIgnoreCase(NULL) == 0)
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
        
        public Builder string(String iName)
        {
            SQLUpdateData i = new SQLUpdateData(iName, SQLData.Type.STRING, null);
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
            if (amount == null || amount.compareToIgnoreCase(NULL) == 0)
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
         * @param mName
         * @return Builder
         */
        public Builder money(String mName)
        {
            SQLUpdateData i = new SQLUpdateData(mName, SQLData.Type.MONEY, null);
            this.columnList.add(i);
            return this;
        }
        
        /**
         * Stores a bit value in SQL type of BIT.
         * @param bName
         * @return Builder.
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
            if (value == null || value.compareToIgnoreCase(NULL) == 0)
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
    
    private PreparedStatement getPreparedStatement() throws SQLException
    {
        Connection connection;
        PreparedStatement pStatement;
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
                else if (dType.getType() == SQLData.Type.MONEY)
                {
                    pStatement.setNull(columnNumber, java.sql.Types.DECIMAL);
                }
                else if (dType.getType() == SQLData.Type.BIT)
                {
                    pStatement.setNull(columnNumber, java.sql.Types.BIT);
                }
                else if (dType.getType() == SQLData.Type.TIMESTAMP)
                {
                    pStatement.setNull(columnNumber, java.sql.Types.TIMESTAMP);
                }
                else if (dType.getType() == SQLData.Type.SMALL_INT)
                {
                    pStatement.setNull(columnNumber, java.sql.Types.SMALLINT);
                }
                else if (dType.getType() == SQLData.Type.TINY_INT)
                {
                    pStatement.setNull(columnNumber, java.sql.Types.TINYINT);
                }
                else if (dType.getType() == SQLData.Type.CHAR)
                {
                    pStatement.setNull(columnNumber, java.sql.Types.CHAR);
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
                else if (dType.getType() == SQLData.Type.MONEY)
                {
//                    pStatement.setDate(columnNumber, Date.valueOf(dType.getValue()));
                    BigDecimal bd = new BigDecimal(dType.getValue());
                    pStatement.setBigDecimal(columnNumber, bd);
                }
                else if (dType.getType() == SQLData.Type.BIT)
                {
                    pStatement.setBoolean(columnNumber, Boolean.valueOf(dType.getValue()));
                }
                else if (dType.getType() == SQLData.Type.TIMESTAMP)
                {
                    pStatement.setTimestamp(columnNumber, java.sql.Timestamp.valueOf(dType.getValue()));
                }
                else if (dType.getType() == SQLData.Type.SMALL_INT)
                {
                    pStatement.setShort(columnNumber, Short.valueOf(dType.getValue()));
                }
                else if (dType.getType() == SQLData.Type.TINY_INT)
                {   // Tiny int similar to SMALL_INT for JDBC.
                    pStatement.setShort(columnNumber, Short.valueOf(dType.getValue()));
                }
                else if (dType.getType() == SQLData.Type.CHAR)
                {   // Char handled as String JDBC.
                    pStatement.setString(columnNumber, dType.getValue());
                }
                else // dType.getType() == SQLData.Type.STRING
                {
                    pStatement.setString(columnNumber, dType.getValue());
                }
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
