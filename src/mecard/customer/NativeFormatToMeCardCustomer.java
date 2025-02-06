/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013 - 2025  Edmonton Public Library
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

import java.util.List;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import site.CustomerGetNormalizer;
import site.calgary.CPLCustomerGetNormalizer;
import site.chinook.CARCustomerGetNormalizer;
import site.edmonton.EPLCustomerGetNormalizer;
import site.parklands.PRLCustomerGetNormalizer;
import site.reddeer.RDPLCustomerGetNormalizer;
import site.shortgrass.SLSCustomerGetNormalizer;
import site.stalbert.SAPLCustomerGetNormalizer;
import site.strathcona.STRCustomerGetNormalizer;
import site.trac.TRACCustomerGetNormalizer;
import site.woodbuffalo.WBRLCustomerGetNormalizer;

/**
 * This class formats ILS output into Customer objects. The reverse is carried
 * out by {@link FormattedCustomer} objects.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public abstract class NativeFormatToMeCardCustomer
{
    protected CustomerGetNormalizer customMessageInterpreter = null;
    
    public NativeFormatToMeCardCustomer()
    {
        String libCode = PropertyReader.
            getProperties(ConfigFileTypes.ENVIRONMENT).
            getProperty(LibraryPropertyTypes.LIBRARY_CODE.toString());
        switch (libCode)
        {
            case "EPL":
                this.customMessageInterpreter = new EPLCustomerGetNormalizer();
                break;
            case "SLS":
                this.customMessageInterpreter = new SLSCustomerGetNormalizer();
                break;
            case "CAR":
                this.customMessageInterpreter = new CARCustomerGetNormalizer();
                break;
            case "STR":
                this.customMessageInterpreter = new STRCustomerGetNormalizer();
                break;
            case "TRAC":
                this.customMessageInterpreter = new TRACCustomerGetNormalizer();
                break;
            case "STA":
                this.customMessageInterpreter = new SAPLCustomerGetNormalizer();
                break;
            case "CPL":
                this.customMessageInterpreter = new CPLCustomerGetNormalizer();
                break;
            case "PRL":
                this.customMessageInterpreter = new PRLCustomerGetNormalizer();
                break;
            case "WBRL":
                this.customMessageInterpreter = new WBRLCustomerGetNormalizer();
                break;
            case "RDPL":
                this.customMessageInterpreter = new RDPLCustomerGetNormalizer();
                break;
            default:
                this.customMessageInterpreter = new CustomerGetNormalizer();
        }
    }
    
    /**
     * Converts a string from ILS or SIP into a Customer object.
     * @param list of strings that represent the customer as it would have 
     * been received from the ILS.
     * @return true if the conversion was successful and false otherwise.
     */
    public abstract Customer getCustomer(List<String> list);
    /**
     * Converts a string from ILS or SIP into a Customer object.
     * @param s the string that represent the customer as it would have 
     * been received from the ILS.
     * @return true if the conversion was successful and false otherwise.
     */
    public abstract Customer getCustomer(String s);
}