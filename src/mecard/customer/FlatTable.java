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
package mecard.customer;

import java.util.HashMap;
import mecard.config.FlatUserExtendedFieldTypes;

/**
 * Represents one of the given sections of a flat file.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class FlatTable implements FormattedTable
{
    private String name;
    private final HashMap<String, String> columns;
    public static FlatTable getInstanceOf(FlatUserExtendedFieldTypes type, HashMap<String, String> dataFields)
    {
        return new FlatTable(type.name(), dataFields);
    }
    
    private FlatTable(String name, HashMap<String, String> headDataMap)
    {
        this.columns = new HashMap<>();
        for (String key: headDataMap.keySet())
        {
            this.columns.put(key, headDataMap.get(key));
        }
        this.name = name;
    }
    
    @Override
    public String getData()
    {
        StringBuilder data = new StringBuilder();
        for (String sKeys: this.columns.keySet())
        {
            // each entry looks like ".USER_ENVIRONMENT.   |aPUBLIC"
            data.append(".");
            data.append(sKeys);
            data.append(".");
            data.append("   |a");
            data.append(this.columns.get(sKeys));
            data.append("\n");
        }
        return finalizeTable(data);
    }

    @Override
    public String getHeader()
    {
        return "*** DOCUMENT BOUNDARY ***\nFORM=LDUSER\n";
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getValue(String key)
    {
        if (this.columns.containsKey(key))
        {
            return this.columns.get(key);
        }
        return "";
    }

    @Override
    public boolean setValue(String key, String value)
    {
        boolean response = false;
        if (key != null && value != null)
        {
            response = this.columns.containsKey(key);
            this.columns.put(key, value);
        }
        return response;
    }

    protected String finalizeTable(StringBuilder data)
    {
        StringBuilder out = new StringBuilder();
        switch (name)
        {
        case "USER_ADDR1":
        case "USER_ADDR2":
        case "USER_XINFO":
            out.append(".");
            out.append(this.name);
            out.append("_BEGIN");
            out.append(".\n");
            out.append(data);
            out.append(".");
            out.append(this.name);
            out.append("_END");
            out.append(".\n");
            break;
        case "USER":
            out.append(getHeader());
            out.append(data);
            break;
        default:
            out.append(getHeader());
            break;
        }
        return out.toString();
    }

}
