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

import api.CustomerMessage;
import api.SIPCustomerMessage;
import java.util.List;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import mecard.util.Phone;
import site.CustomerGetNormalizer;
import site.calgary.CPLCustomerGetNormalizer;

/**
 *
 * @author andrew
 */
public class FlatFormatter implements CustomerFormatter
{
    // This should replace FlatUserFormatter().
    protected CustomerGetNormalizer customMessageInterpreter = null;
    
    FlatFormatter(String stdout)
    {
        String libCode = PropertyReader.
            getProperties(ConfigFileTypes.ENVIRONMENT).
            getProperty(LibraryPropertyTypes.LIBRARY_CODE.toString());
        switch (libCode)
        {
            // They are going with SIP for now.
//            case "CPL":
//                this.customMessageInterpreter = new CPLCustomerGetNormalizer();
//                break;
            default:
                this.customMessageInterpreter = new CustomerGetNormalizer();
        }
    }

    @Override
    public Customer getCustomer(List<String> list)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer getCustomer(String s)
    {
        Customer customer = new Customer();
        CustomerMessage symphonyFlatMessage = new FlatCustomerMessage(s);
//        customer.set(CustomerFieldTypes.ID, symphonyFlatMessage.getField("AA"));
//        customer.set(CustomerFieldTypes.PREFEREDNAME, symphonyFlatMessage.getField("AE"));
//        customer.set(CustomerFieldTypes.RESERVED, symphonyFlatMessage.getField("AF"));
//        customer.set(CustomerFieldTypes.EMAIL, symphonyFlatMessage.getField("BE"));
//        // Phone object
//        Phone phone = new Phone(symphonyFlatMessage.getField("BF"));
//        customer.set(CustomerFieldTypes.PHONE, phone.getUnformattedPhone());
//        ...
        return customer;
    }
    
}
