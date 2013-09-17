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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import mecard.config.BImportDBFieldTypes;
import mecard.config.BImportTableTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PropertyReader;
import mecard.requestbuilder.BImportRequestBuilder;

/**
 * This formatter takes Customer data and returns data ready for producing 
 * a BImport file.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class BImportFormatter implements CustomerFormatter
{
            // ==== Head =====
//        x- borrower: second_id; name; expiration_date; birth_date
//        borrower_phone: phone_type; phone_no
//        borrower_address: address1; address2; city_st; postal_code; email_name; email_address; send_preoverdue
//        borrower_barcode: bbarcode
//        borrower_bstat: bstat
        
        // ==== Data =====
//        M- borrower: 21221012345677; Balzac, Billy; 04-15-2014; 01-31-1998
//        borrower_phone: h-noTC; 7804964058
//        borrower_address: 12345 123 St.; ; edmonton; H0H 0H0; ilsteam; ilsteam@epl.ca; 1
//        borrower_barcode: 21221012345678
//        borrower_bstat: unknown
    private boolean isSetDefaultSendPreoverdue;
    private List<BImportTable> headDataList;
    
    public BImportFormatter()
    {
        Properties bimpProps = PropertyReader.getProperties(ConfigFileTypes.BIMPORT);
        // Optional bimport value, defaults to 'on' for default send pre-overdue notices.
        this.isSetDefaultSendPreoverdue = true;
        String sendPreoverdue = bimpProps.getProperty(BImportRequestBuilder.SEND_PREOVERDUE.toString(), "true");
        if (sendPreoverdue.compareToIgnoreCase("false") == 0)
        {
            this.isSetDefaultSendPreoverdue = false;
        }
        this.headDataList = new ArrayList<>();
    }
    
    @Override
    public Customer getCustomer(List<String> list)
    {
        throw new UnsupportedOperationException("BImport does not support "
                + "creating Customers.");
    }

    @Override
    public Customer getCustomer(String s)
    {
        throw new UnsupportedOperationException("BImport does not support "
                + "creating Customers.");
    }
    
    /** Returns the header portion of the customer's data set. 
     * 
     * @return header data, if {@link #setCustomer(mecard.customer.Customer)}
     * has been called and an empty list otherwise.
     */
    public List<String> getCustomerHeader()
    {
        List<String> customerList = new ArrayList<>();
        for (BImportTable table: headDataList)
        {
            customerList.add(table.getHeader());
        }
        return customerList;
    }

    @Override
    public List<String> setCustomer(Customer c)
    {
        // Basic borrower table.
        HashMap<String, String> customerTable = new HashMap<>();
        customerTable.put(BImportDBFieldTypes.SECOND_ID.toString(), c.get(CustomerFieldTypes.ID));
        customerTable.put(BImportDBFieldTypes.NAME.toString(), (c.get(CustomerFieldTypes.LASTNAME) 
                + ", " + c.get(CustomerFieldTypes.FIRSTNAME)));
        customerTable.put(BImportDBFieldTypes.EXPIRY.toString(), c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES));
        if (c.isEmpty(CustomerFieldTypes.DOB) == false)
        {
            customerTable.put(BImportDBFieldTypes.BIRTH_DATE.toString(), c.get(CustomerFieldTypes.DOB));
        }
        // This is checked because on update of a user account you can clear the
        // pin to protect that field from updating, and if the case where their EPL
        // pin is 'CATS', illegal on Horizon, loading the customer would reset 
        // the pin to a random number.
        if (c.isEmpty(CustomerFieldTypes.PIN) == false)
        {
            customerTable.put(BImportDBFieldTypes.PIN.toString(), c.get(CustomerFieldTypes.PIN));
        }
        headDataList.add(BImportTable.getInstanceOBImportTable(BImportTableTypes.BORROWER_TABLE, customerTable));
        
        // Next the phone table
        if (c.isEmpty(CustomerFieldTypes.PHONE) == false)
        {
            customerTable.clear();
            customerTable.put(BImportDBFieldTypes.PHONE_TYPE.toString(), "h-noTC");
            customerTable.put(BImportDBFieldTypes.PHONE_NUMBER.toString(), c.get(CustomerFieldTypes.PHONE));
            headDataList.add(BImportTable.getInstanceOBImportTable(BImportTableTypes.BORROWER_PHONE_TABLE, customerTable));
        }
        
        // Address
        customerTable.clear();
        customerTable.put(BImportDBFieldTypes.ADDRESS_1.toString(), c.get(CustomerFieldTypes.STREET));
        customerTable.put(BImportDBFieldTypes.ADDRESS_2.toString(), " "); // intentionally blank
        customerTable.put(BImportDBFieldTypes.CITY.toString(), c.get(CustomerFieldTypes.CITY));
        customerTable.put(BImportDBFieldTypes.POSTAL_CODE.toString(), c.get(CustomerFieldTypes.POSTALCODE));
        String emailName = this.computeEmailName(c.get(CustomerFieldTypes.EMAIL));
        customerTable.put(BImportDBFieldTypes.EMAIL_NAME.toString(), emailName);
        customerTable.put(BImportDBFieldTypes.EMAIL_ADDRESS.toString(), c.get(CustomerFieldTypes.EMAIL));
        if (this.isSetDefaultSendPreoverdue == true)
        {
            customerTable.put(BImportDBFieldTypes.SEND_PREOVERDUE.toString(), "1");
        }
        headDataList.add(BImportTable.getInstanceOBImportTable(BImportTableTypes.BORROWER_ADDRESS_TABLE, customerTable));
        
        // Borrower Barcode
        customerTable.clear();
        customerTable.put(BImportDBFieldTypes.BARCODE.toString(), c.get(CustomerFieldTypes.ID));
        headDataList.add(BImportTable.getInstanceOBImportTable(BImportTableTypes.BORROWER_BARCODE_TABLE, customerTable));
        
        // Borrower bStat TODO
//        customerTable.clear();
//        customerTable.put(BImportDBFieldTypes.BARCODE.toString(), c.get(CustomerFieldTypes.ID));
//        headDataList.add(BImportTable.getInstanceOBImportTable(BImportTableTypes.BORROWER_BARCODE_TABLE, customerTable));
        
        List<String> customerList = new ArrayList<>();
        for (BImportTable table: headDataList)
        {
            customerList.add(table.getData());
        }
        return customerList;
    }
    
    /** 
     * Horizon has an additional required field, email name, which is just the 
     * user's email name (without the domain). We compute that here.
     * @param email
     * @return String value of email account name (without the domain).
     */
    protected String computeEmailName(String email) 
    {
        return email.split("@")[0];
    }
    
}
