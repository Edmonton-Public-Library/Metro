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

import api.CommandStatus;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import mecard.config.CustomerFieldTypes;

/**
 * Creates a failed customer file in the configuration directory or the current
 * working directory relative to the MeCard.jar, if '-c' is not set when the 
 * server is started.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class UserFailFile extends UserFile 
{
    private final static String path = "logs" + File.separator;
    
    /**
     * Creates a failed customer file in the configuration directory or the current
     * working directory relative to the MeCard.jar, if '-c' is not set when the 
     * server is started.
     * @param customer customer that failed.
     */
    public UserFailFile(Customer customer)
    {
        super(path + customer.get(CustomerFieldTypes.ID) + ".fail");
    }
    
    /**
     * Sets messages so they are saved as a fail file.
     * @param status of the command that failed to create a user.
     */
    public void setStatus(CommandStatus status)
    {
        List<String> data = new ArrayList<>();
        data.add(status.getStdout());
        data.add(status.getStderr());
        this.addUserData(data);
    }
}
