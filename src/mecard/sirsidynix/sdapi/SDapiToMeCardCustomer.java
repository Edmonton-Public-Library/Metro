/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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
package mecard.sirsidynix.sdapi;

import java.util.List;
import mecard.customer.Customer;
import mecard.customer.NativeFormatToMeCardCustomer;

/**
 * Formats the {@link mecard.customer.Customer} into the Polaris web service (PAPI)
 * consumable form which, as of version 7.0 (and perhaps earlier) is XML only.
 * 
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class SDapiToMeCardCustomer implements NativeFormatToMeCardCustomer
{

    @Override
    public Customer getCustomer(List<String> list) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Customer getCustomer(String s) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
