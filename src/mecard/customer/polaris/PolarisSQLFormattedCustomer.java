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
package mecard.customer.polaris;

import java.util.List;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PolarisSQLPropertyTypes;
import mecard.config.PolarisTable;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import mecard.customer.FormattedTable;

/**
 * This object will format the fields so they are ready for insertion into the
 * ILS database with correct formatting. Example: phone number '7805551212' will
 * become '780-555-1212'.
 * @author anisbet
 */
public class PolarisSQLFormattedCustomer implements FormattedCustomer
{
    private FormattedTable patronsTable;
    private FormattedTable addressesTable;
    private FormattedTable patronAddressesTable;
    private FormattedTable postalCodeTable;
    private FormattedTable patronRegistrationTable;
    
    public PolarisSQLFormattedCustomer(Customer customer)
    {
        Properties props = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        patronsTable = new PolarisSQLFormattedTable(PolarisTable.PATRONS);
        addressesTable = new PolarisSQLFormattedTable(PolarisTable.ADDRESSES);
        patronAddressesTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_ADDRESSES);
        postalCodeTable = new PolarisSQLFormattedTable(PolarisTable.POSTAL_CODES);
        patronRegistrationTable = new PolarisSQLFormattedTable(PolarisTable.PATRON_REGISTRATION);
        // Fill in the default required fields for v1 of PAPI web service API.
        patronRegistrationTable.setValue(
                PolarisTable.PatronRegistration.LANGUAGE_ID.toString(),  // table column name
                props.getProperty(PolarisSQLPropertyTypes.LANGUAGE_ID.toString())); // Value from properties file.
    }
    
    @Override
    public List<String> getFormattedCustomer()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getFormattedHeader()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setValue(String key, String value)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean insertValue(String tableName, String key, String value)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsKey(String key)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getValue(String key)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertTable(FormattedTable formattedTable, int index)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean renameField(String tableName, String originalFieldName, String newFieldName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeField(String tableName, String fieldName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
