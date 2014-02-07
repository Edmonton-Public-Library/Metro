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
 */
package mecard.customer;

import java.io.File;

/**
 * The BimportUserFile can accommodate a situation where the original customer's 
 * data will be lost if a bimport load fails. The problem is that the customer's
 * pin will be wiped out if they try to update an account if the load failed.
 * The original file will be over written by the update.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class BimportUserFile extends UserFile
{
    private String pin;
    
    public BimportUserFile(String path)
    {
        super(path);
        this.pin = "";
    }
    
    @Override
    protected File createFile(String name)
    {
        File file = new File(name);
        if (file.exists())
        {
            // Here we fix an issue whereby a user registers on a Horizon system,
            // The system has not had time to load the customer, or there was a
            // problem, then the customer tries to update their account, we could
            // loose the original PIN for the customer.
            File saveFile = new File(name + ".orig");
            if (saveFile.exists() == false)
            {
                // if the original file exists keep it, and get rid of 
                // the last update file, and replace it with the latest information.
                if (file.renameTo(saveFile))
                {
                    System.out.println("Saving file '" + name + "'.");
                }
                else
                {
                    System.out.println("Unable to save customer's original file. "
                            + "'" + name + "'"
                            + "If the customer is updating their account after "
                            + "registration their PIN may be missing. For best "
                            + "customer service contact the customer and update "
                            + "their record.");
                    // Optionally we could throw an exception but that wouldn't
                    // be great for customer service.
                }
            }
            file.delete();
        }
        // create a new one.
        file = new File(name);
        return file;
    }
}
