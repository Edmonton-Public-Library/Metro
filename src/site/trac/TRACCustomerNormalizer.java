/*
* Metro allows customers from any affiliate library to join any other member library.
*    Copyright (C) 2023 - 2025  Edmonton Public Library
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
package site.trac;

import java.text.ParseException;
import java.util.Properties;
import mecard.Protocol;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PolarisTable;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.util.DateComparer;
import mecard.customer.MeCardCustomerToNativeFormat;
//import mecard.polaris.papi.PapiElementOrder;
//import mecard.util.Text;
import site.CustomerLoadNormalizer;

/**
 * Normalizes the customer's data before loading into the local library's ILS.
 * The local library may require certain modifications to a customer account
 * such as minimum PIN width, or application of a computed bStat value.
 * @author Andrew Nisbet andrew@dev-ils.com
 * @since v1.0 2013
 */
public final class TRACCustomerNormalizer extends CustomerLoadNormalizer
{
    private Properties envProperties;
    public TRACCustomerNormalizer(boolean debug)
    {
        super(debug);
        this.envProperties = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
    }

    @Override
    public void finalize(Customer customer, MeCardCustomerToNativeFormat formattedCustomer, Response response)
    {   
        if (envProperties.getProperty(LibraryPropertyTypes.CREATE_SERVICE.toString()).endsWith("sql"))
        {
            // add User1 - User5 and any other fields.
            formattedCustomer.insertValue(
                    PolarisTable.PATRON_REGISTRATION,
                    PolarisTable.PatronRegistration.USER_1.toString(), 
                    "Not in the List");
            formattedCustomer.insertValue(
                    PolarisTable.PATRON_REGISTRATION,
                    PolarisTable.PatronRegistration.USER_2.toString(), 
                    null);
            formattedCustomer.insertValue(
                    PolarisTable.PATRON_REGISTRATION,
                    PolarisTable.PatronRegistration.USER_3.toString(), 
                    null);
            formattedCustomer.insertValue(
                    PolarisTable.PATRON_REGISTRATION,
                    PolarisTable.PatronRegistration.USER_4.toString(), 
                    "(none)");
            formattedCustomer.insertValue(
                    PolarisTable.PATRON_REGISTRATION,
                    PolarisTable.PatronRegistration.USER_5.toString(), 
                    "(none)");
    // Default phone numbers '000-000-0000' could be problematic let's get rid of them here
    //        Phone phone = new Phone(customer.get(CustomerFieldTypes.PHONE));
    //        if (phone.isUnset())
    //        {
    //              // Here we remove, but it may be better to insert a different value.
    //            formattedCustomer.removeField(
    //                    PolarisTable.PATRON_REGISTRATION, 
    //                    PolarisTable.PatronRegistration.PHONE_VOICE_1.toString());
    //        }
    // 2020-02-11 TRAC gender mapping below. ME libraries only passes 'M', 'F', or 'X'
    // for not supplied.
    // Update: 2020-01-20: GenderID Description (From the 'Genders' table as of Polaris 6.3).
    // GenderID = 1 N/A
    // GenderID = 2 Female
    // GenderID = 3 Male
    // GenderID = 4 Non-binary
    // GenderID = 5 Other
    // From the schema:
    // This looks like the output of a discribe table, but Richard Kenig says the GenderID is a regular integer.
    // GENDER("GenderID"), //, 1, char, 1, 1, null, null, 1, null, null, 1, null, 1, 22, YES, See PolarisTable.java.
            String sex = customer.get(CustomerFieldTypes.SEX);
            switch (sex)
            {
                case "M":
                    formattedCustomer.insertValue(
                        PolarisTable.PATRON_REGISTRATION,
                        PolarisTable.PatronRegistration.GENDER_ID.toString(), 
                        "3");
                    break;
                case "F":
                    formattedCustomer.insertValue(
                        PolarisTable.PATRON_REGISTRATION,
                        PolarisTable.PatronRegistration.GENDER_ID.toString(), 
                        "2");
                    break;
                default:
                    formattedCustomer.insertValue(
                        PolarisTable.PATRON_REGISTRATION,
                        PolarisTable.PatronRegistration.GENDER_ID.toString(), 
                        "1");
                    break;
            }
            // Privilege expiry logic. Use the customer's expiry and if one isn't 
            // set set it to expire in a year.
            if (customer.isEmpty(CustomerFieldTypes.PRIVILEGE_EXPIRES))
            {
                String expiry = DateComparer.getFutureDate(365);
                try
                {
                    expiry = DateComparer.ANSIToConfigDate(expiry);
                } 
                catch (ParseException ex)
                {
                    System.out.println("**error while parsing customer's expiry " 
                            + customer.get(CustomerFieldTypes.ID) 
                            + " DOB: '" + customer.get(CustomerFieldTypes.ID)
                            + "' (in TRAC Customer Normalizer)");
                }
                formattedCustomer.insertValue(
                    PolarisTable.PATRON_REGISTRATION,
                    PolarisTable.PatronRegistration.EXPIRATION_DATE.toString(),
                    expiry);
            }
        }
        
        /*
        'polaris-api' usage for the UPDATE_SERVICE can include a birth date at TRAC.
        */
//        if (envProperties.getProperty(LibraryPropertyTypes.UPDATE_SERVICE.toString()).endsWith("api"))
//        {
//            // In previous versions of PAPI the birthdate was not update-able, 
//            // but maybe now it is?? Waiting for confirmation, but in the meantime...
//            String dob = customer.get(CustomerFieldTypes.DOB);
//            if (Text.isSet(dob))
//            {
//                try
//                {
//                    dob = DateComparer.ANSIToConfigDate(dob);
//                    formattedCustomer.setValue(PapiElementOrder.BIRTHDATE.name(), dob);
//                } 
//                catch (ParseException ex)
//                {
//                    System.out.println("**error while parsing customer " 
//                            + customer.get(CustomerFieldTypes.ID) 
//                            + " DOB: '" + dob + "' (in TRAC Customer Normalizer)");
//                }
//            }
//            formattedCustomer.setValue(PapiElementOrder.STATE.name(), customer.get(CustomerFieldTypes.PROVINCE));
//            formattedCustomer.setValue(PapiElementOrder.REQUEST_PICKUP_BRANCH_ID.name(), "172");
              // Doing this in the finalize method will make this happen on both
              // create and update and we only want to do this on update.
              // The problem is the normalizeOnUpdate() method does not take the 
              // Native format customer object.
//            formattedCustomer.removeField("This field intentionally not set", PapiElementOrder.BIRTHDATE.toString());
//        }
    }
    
    @Override
    public void normalizeOnCreate(Customer customer, Response response)
    {
        // No special action required at this time.
    }

    @Override
    public void normalizeOnUpdate(Customer customer, Response response)
    {
        /*
        For some reason, the birthdate on update throws the following error on 
        the TRAC ILS (but not on the dev Sandbox):
        UPDATE XML body: <?xml version="1.0" encoding="UTF-8" standalone="no"?>
        <PatronUpdateData>
        <LogonBranchID>1</LogonBranchID>
        <LogonUserID>
        ...
        <BirthDate>1971-10-05T00:00:00</BirthDate>
        ...
        <ExpirationDate>2026-02-12T00:00:00</ExpirationDate>
        ....
        </Address>
        </PatronAddresses>
        </PatronUpdateData>
        >>[PUT], [https://catalogue.tracpac.ab.ca/PAPIservice/REST/public/v1/1033/100/1/patron/21817010469601], [Fri, 07 Mar 2025 20:36:16 GMT], []<<
        HEADERS RETURNED:
        :status:[200]
        api-supported-versions:[1.0, 2.0]
        CONTOUT RETURNED: 
        <PatronUpdateResult xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
        <PAPIErrorCode>-3540</PAPIErrorCode>
        <ErrorMessage>Invalid Birthdate</ErrorMessage>
        </PatronUpdateResult>
        * Technically, birthdates _should_ never be required to be updated so 
        * remove it now so it doesn't cause the invalid date format error on 
        * TRAC's ILS.
        */
        // 'polaris-api' for update, but not if they roll back to polaris-sql.
        // This is done because Polaris API currently does NOT allow updating
        // a blank (null) birthdate field. Advised that they add a default 
        // birthdate of '1900-01-01' to all non-juvenile accounts so the UPDATE
        // web service does work.
//        if (this.envProperties.getProperty(LibraryPropertyTypes.UPDATE_SERVICE.toString()).endsWith("api"))
//        {
//            customer.setDob(Protocol.DEFAULT_FIELD_VALUE);
//        }
    }
    
    @Override
    public ResponseTypes normalize(Customer c, StringBuilder r)
    {
        return ResponseTypes.SUCCESS; // no special rules for TRAC.
    }
}
