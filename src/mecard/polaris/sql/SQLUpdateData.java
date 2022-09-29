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

/**
 * Associates a data value for a column and the intended data type of that 
 * column in a single object.
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class SQLUpdateData extends SQLData
{
    private final String value; 
    private String PROCEDURE_INVOCATION = "fn";
    public SQLUpdateData(String name, SQLData.Type dType, String value)
    {
        super(name, dType);
        this.value = value;
    }

    /**
     * Gets the value to be stored in the column during an update or insert 
     * operation. Values are always stored internally as strings, and coerced,
     * or more accurately, converted into their appropriate data type during 
     * an SQL INSERT of UPDATE command.
     * @return stored value which may be null.
     */
    public String getValue()
    {
        return this.value;
    }
    
    /**
     * This allows you to control the how the procedure is called. Typically
     * you would use 'call', 'fn', or 'EXEC'.
     * @param invocation 
     */
    public void setProcedureInvocation(String invocation)
    {
        this.PROCEDURE_INVOCATION = invocation;
    }
    
    /**
     * Sets the object as a query string, that is, in an insert statement
     * this object would be represented by the '?' in this SQL query:
     * INSERT INTO Table VALUES (?);
     * @return the query string representation of this object.
     */
    public String toQueryString()
    {
        switch (this.dataType)
        {
            case STORED_PROCEEDURE:
                if (this.value == null || this.value.isEmpty())
                {
                    return "{" + PROCEDURE_INVOCATION + " " + this.name + "()}";
                }
                return "{" + PROCEDURE_INVOCATION + " " + this.name + "(?)}";
            default:
                return "?";
        }
    }

    @Override
    public String toString()
    {
        if (this.value == null)
        {
            return this.name + "=NULL";
        }
        switch (this.dataType)
        {
            case INT:
                return this.name + "=" + this.value;
            default:
                return this.name + "='" + this.value + "'";
        }
    }    
}
