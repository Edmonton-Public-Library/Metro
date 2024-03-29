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

import api.CustomerMessage;

/**
 * This interface is used to encapsulate a customer into an object.
 * It is allows the {@link site.MeCardPolicy} object to ask for different facets
 * about the customer such as age, or residency. Though currently not used, this
 * object allows the ME server to determine how to handle the customer based on
 * the answers to standard policy related questions.
 * 
 * @author Andrew Nisbet <andrew.nisbet@epl.ca>
 */
public class SQLCustomerMessage implements CustomerMessage
{
    public SQLCustomerMessage(String message, boolean b)
    {
        // take the incoming message as raw text from a SQL result and parse out results.
    }

    @Override
    public String getCustomerProfile()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getField(String fieldName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty(String fieldName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getStanding()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cardReportedLost()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isInGoodStanding()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDateField(String fieldName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
