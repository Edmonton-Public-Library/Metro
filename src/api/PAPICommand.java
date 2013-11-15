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

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class PAPICommand implements Command
{

    
    public static class Builder
    {
        // optional
        private String someValue;

        /**
         * Constructor that insists that the command gets at least a status
         * object and a command.
         */
        public Builder()
        {
            this.someValue = "";
        }

        /**
         * Builds the command and returns a reference to it.
         *
         * @return APICommand reference.
         */
        public PAPICommand build()
        {
            return new PAPICommand(this);
        }

        public Builder setURL(String urlBase)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    private PAPICommand(Builder papiBuilder)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CommandStatus execute()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
