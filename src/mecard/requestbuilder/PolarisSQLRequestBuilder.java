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
package mecard.requestbuilder;

import api.Command;
import api.CommandStatus;
import api.CustomerMessage;
import api.DummyCommand;
import api.SQLConnector;
import api.SQLCustomerMessage;
import api.SQLInsertCommand;
import api.SQLSelectCommand;
import api.SQLUpdateCommand;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;
import mecard.Protocol;
import mecard.QueryTypes;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PropertyReader;
import mecard.config.PolarisSQLPropertyTypes;
import mecard.config.MessagesTypes;
import mecard.config.PolarisTable;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.DumpUser;
import mecard.customer.FormattedCustomer;
import mecard.customer.UserLostFile;
import mecard.customer.polaris.PolarisSQLCustomerFormatter;
import mecard.customer.polaris.PolarisSQLFormattedCustomer;
import mecard.exception.ConfigurationException;
import mecard.util.DateComparer;
import mecard.util.Text;
import site.CustomerLoadNormalizer;

/**
 * Manages messaging and work flow for requests to a Polaris ILS via POLARIS_SQL statements.
 * @author Andrew Nisbet <anisbet@epl.ca>
 * @since 0.8.14_00
 */
public class PolarisSQLRequestBuilder extends ILSRequestBuilder
{
    // Use this for insert and update, however describe doesn't need the database namespace 'Polaris'
    // but select does. 
    private final String patronsTable       = "Polaris.Patrons"; 
    private final String patronRegistration = "Polaris.PatronRegistration";
    private final String postalCodes        = "Polaris.PostalCodes";
    private final String addressTable       = "Polaris.Addresses";
    private final String patronAddresses    = "Polaris.PatronAddresses";
    private final boolean debug;
    
    private final String host;
    private final String driver;
    private final String database;
    private final String user;
    private final String password;
    private final String creatorID;
    private final String organizationID;
    private final String patronCodeID;
    private final Properties messages;
    private SQLConnector connector;    // private so it can be closed by Responder externally.
    
    public PolarisSQLRequestBuilder(boolean debug)
    {
        messages = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        this.debug          = debug;
        Properties properties = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        // Done because the tag is common to all ILS specific properties files.
        this.loadDir        = properties.getProperty(PropertyReader.LOAD_DIR.toString());
        this.host           = properties.getProperty(PolarisSQLPropertyTypes.HOST.toString());
        this.driver         = properties.getProperty(PolarisSQLPropertyTypes.CONNECTOR_TYPE.toString()); // AKA connector-type.
        this.database       = properties.getProperty(PolarisSQLPropertyTypes.DATABASE.toString());
        this.user           = properties.getProperty(PolarisSQLPropertyTypes.USERNAME.toString());
        this.password       = properties.getProperty(PolarisSQLPropertyTypes.PASSWORD.toString());
        this.creatorID      = properties.getProperty(PolarisSQLPropertyTypes.CREATOR_ID.toString());
        this.organizationID = properties.getProperty(PolarisSQLPropertyTypes.ORGANIZATION_ID.toString());
        this.patronCodeID   = properties.getProperty(PolarisSQLPropertyTypes.PATRON_CODE_ID.toString());
    }
    
    /**
     * Constructor for testing purposes.
     * @param host
     * @param driver MY_POLARIS_SQL or POLARIS_SQL_SERVER.
     * @param database
     * @param user
     * @param password
     */
    public PolarisSQLRequestBuilder(String host, String driver, String database, String user, String password)
    {
        Properties properties = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        this.debug          = true;
        this.messages       = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        this.loadDir        = properties.getProperty(PolarisSQLPropertyTypes.LOAD_DIR.toString());
        this.host           = host;
        this.driver         = driver;
        this.database       = database;
        this.user           = user;
        this.password       = password;
        this.creatorID      = properties.getProperty(PolarisSQLPropertyTypes.CREATOR_ID.toString());
        this.organizationID = properties.getProperty(PolarisSQLPropertyTypes.ORGANIZATION_ID.toString());
        this.patronCodeID   = properties.getProperty(PolarisSQLPropertyTypes.CREATOR_ID.toString());
    }
    
    @Override
    public CustomerFormatter getFormatter()
    {
        return new PolarisSQLCustomerFormatter();
    }

    @Override
    public Command getCustomerCommand(String barcode, String userPin, Response response)
    {
        // We don't actually use this command, but we should do a look up for
        System.out.println("USERID:" + barcode);
        this.connector = new SQLConnector.Builder(this.host, this.driver, this.database)
            .user(user)
            .password(password)
            .build();
        SQLSelectCommand selectUser = new SQLSelectCommand.Builder(this.connector, this.patronsTable)
            .string(PolarisTable.Patrons.BARCODE.toString())
            .whereString(PolarisTable.Patrons.BARCODE.toString(), barcode)
            .build();
        if (debug)
        {
            System.out.println("COMMAND==>"+selectUser.toString()+"<==");
        }
        return selectUser;
    }

    @Override
    public Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        /////////////////////////// Developer notes ////////////////////////////
        // 
        // If you ever need to add different defaults:
        //   Make an entry in the 'polaris_sql.properties' file, adding an entry 
        //   in PolarisSQLPropertyTypes for the correct spelling. Next change 
        //   the method builder argument to read from the formatted customer instead
        //   of the hard coded value. Add requisit changes to the PolarisSQLFormattedCustomer
        //   AND / OR add the values through the normalization finalize() method process.
        //
        ////////////////////////////////////////////////////////////////////////
        // we have a customer let's convert them to a PolarisSQLFormatted user.
        FormattedCustomer fCustomer = new PolarisSQLFormattedCustomer(customer);
        // apply library centric normalization to the customer account.
        normalizer.finalize(customer, fCustomer, response);
        List<String> userSQLFileLines = fCustomer.getFormattedCustomer();
        // Output the customer's data as a receipt in case they come back with questions.
        new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.txt)
                .set(userSQLFileLines)
                .build();
        // This is a class variable because the Responder will need to run the last command and return results.
        this.connector = new SQLConnector.Builder(this.host, this.driver, this.database)
            .user(user)
            .password(password)
            .build();
        
        //        INSERT INTO Polaris.Polaris.Patrons ( PatronCodeID , OrganizationID , CreatorID , ModifierID , Barcode , 
        //                  SystemBlocks , YTDCircCount , LifetimeCircCount , LastActivityDate , ClaimCount , LostItemCount , 
        //                  ChargesAmount , CreditsAmount )
        //        VALUES ( 25 , 303 , 1831 , NULL , (Barcode) , 0 , 0 , 0 , GETDATE() , 0 , 0 , 0, 0)
        // The first 3 values can be read from polaris_sql.properties OR they are also loaded in the formatted customer.
        SQLInsertCommand createPatronCommand = new SQLInsertCommand.Builder(connector, this.patronsTable) 
            .integer(PolarisTable.Patrons.PATRON_CODE_ID.toString(), patronCodeID)
            .integer(PolarisTable.Patrons.ORGANIZATION_ID.toString(), organizationID)
            .integer(PolarisTable.Patrons.CREATOR_ID.toString(), creatorID)
            .integer(PolarisTable.Patrons.MODIFIER_ID.toString())
            .string(PolarisTable.Patrons.BARCODE.toString(), 
                    fCustomer.getValue(PolarisTable.Patrons.BARCODE.toString()))
            .integer(PolarisTable.Patrons.SYSTEM_BLOCKS.toString(), "0")
            .integer(PolarisTable.Patrons.YTD_CIRC_COUNT.toString(), "0")
            .integer(PolarisTable.Patrons.LIFE_TIME_CIRC_COUNT.toString(), "0")
            .dateTimeNow(PolarisTable.Patrons.LAST_ACTIVITY_DATE.toString())
            .integer(PolarisTable.Patrons.CLAIM_COUNT.toString(), "0")
            .integer(PolarisTable.Patrons.LOST_ITEM_COUNT.toString(), "0")
            .money(PolarisTable.Patrons.CHARGES_AMOUNT.toString(), "0")
            .money(PolarisTable.Patrons.CREDITS_AMOUNT.toString(), "0")
            .build();
        
        // Create this entry in the Patrons table.
        CommandStatus status = createPatronCommand.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to select customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronsTable);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                    .build();
        }
        
        //        Then, Retrieve PatronID for use in further Queries
        //
        //        SELECT PatronID
        //        FROM Polaris.Polaris.Patrons
        //        WHERE Barcode = (Barcode)
        // Then recover the PatronID associated with this user.
        SQLSelectCommand getPatronIDCommand = new SQLSelectCommand.Builder(connector, this.patronsTable)
                .string("PatronID")
                .whereString("Barcode", customer.get(CustomerFieldTypes.ID))
                .build();
        status = getPatronIDCommand.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to find customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronsTable);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                    .build();
        }
        // STORE AS VARIABLE (PatronID) This is an Integer, but all responses Status are strings.
        // Also we have to be careful because multiple id's can be returned if the 
        // customer was created more than once. If that is so get the highest or last number
        // it is 'likely' to be the most recent and most valid.
        // 399565\n399566\n399567\n399568\n...
        String polarisPatronID = Text.lastWord(status.getStdout(), 2);
        if (debug)
        {
            System.out.println("PATRON_ID: '" + polarisPatronID + "'");
        }
        
        String expiry = fCustomer.getValue(PolarisTable.PatronRegistration.EXPIRATION_DATE.toString());
        // Since DOB is an optional field some patrons from other libs are failing
        // because they don't have one. We could put a fake one in or we could put 
        // a null value in.
        // See SQLInsert and SQLUpdateCommand, they both now guard for empty string
        // "NULL" ignore case and null values, and Protocol.DEFAULT_FIELD_VALUE.
        String dob = fCustomer.getValue(PolarisTable.PatronRegistration.BIRTH_DATE.toString());
        if (dob.compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
        {
            dob = "null"; // this could be set to '' or null, SQLInsertCommand or SQLUpdateCommand can handle those but
            // we may need to print the value and both commands understand the text command 'null' or 'NULL'.
        }
        if (debug)
        {
            System.out.println("---PatronID>"+polarisPatronID);
            System.out.println("---     dob>"+dob);
            System.out.println("---  expiry>"+expiry);
        }
        
        
        // *** CREATE PATRON REGISTRATION INFORMATION ***
        //        INSERT INTO Polaris.Polaris.PatronRegistration
        //        VALUES ( (PatronID) , 1 , (NameLast) , (NameFirst) , (NameMiddle) , NULL , NULL , 
        //        (Phone) , NULL , NULL, (Email) , (Password) , GETDATE() , (Expiry) , (Expiry) , 
        //        GETDATE() , 'Not in the List' , NULL , NULL , '(none)' , '(none)' , (Gender) , NULL , 
        //        GETDATE() , NULL , 0 , NULL , 2 , NULL , 0 , NULL , 0 , NULL, NULL, NULL, NULL , 
        //        0 , [(NameLast)+', '+(NameFirst)]* , 0 , 0 , 2 , [(NameFirst)+' '+ (NameLast)]* , 
        //        NULL , NULL , NULL , NULL, NULL , NULL , 0 , NULL , NULL , NULL , NULL , NULL , NULL )
        //        *NOTE - concatenate values in [ ]. 
        // *** Column titles and expected values ***
        SQLInsertCommand createPatronRegistrationCommand = new SQLInsertCommand.Builder(connector, this.patronRegistration)
                .integer(PolarisTable.PatronRegistration.PATRON_ID.toString(), polarisPatronID)
                .smallInt(PolarisTable.PatronRegistration.LANGUAGE_ID.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.LANGUAGE_ID.toString()))
                .string(PolarisTable.PatronRegistration.NAME_FIRST.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.NAME_FIRST.toString()))
                .string(PolarisTable.PatronRegistration.NAME_LAST.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.NAME_LAST.toString()))
                .string(PolarisTable.PatronRegistration.NAME_MIDDLE.toString(), null)
                .string(PolarisTable.PatronRegistration.NAME_TITLE.toString(), null)
                .string(PolarisTable.PatronRegistration.NAME_SUFFIX.toString(), null)
                .string(PolarisTable.PatronRegistration.PHONE_VOICE_1.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.PHONE_VOICE_1.toString()))
                .string(PolarisTable.PatronRegistration.PHONE_VOICE_2.toString(), null)
                .string(PolarisTable.PatronRegistration.PHONE_VOICE_3.toString(), null)
                .string(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()))
                .string(PolarisTable.PatronRegistration.PASSWORD.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.PASSWORD.toString()))
                .dateTimeNow(PolarisTable.PatronRegistration.ENTRY_DATE.toString())
                .dateTime(PolarisTable.PatronRegistration.EXPIRATION_DATE.toString(), expiry)
                .dateTime(PolarisTable.PatronRegistration.ADDR_CHECK_DATE.toString(), expiry)
                .dateTimeNow(PolarisTable.PatronRegistration.UPDATE_DATE.toString())
                .string(PolarisTable.PatronRegistration.USER_1.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.USER_1.toString())) // Set these during customer normalization.
                .string(PolarisTable.PatronRegistration.USER_2.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.USER_2.toString())) // Set these during customer normalization.
                .string(PolarisTable.PatronRegistration.USER_3.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.USER_3.toString())) // Set these during customer normalization.
                .string(PolarisTable.PatronRegistration.USER_4.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.USER_4.toString())) // The none is actually (none) including the brackets. It actually links to a list of options.
                .string(PolarisTable.PatronRegistration.USER_5.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.USER_5.toString())) // Set these during customer normalization.
                .setChar(PolarisTable.PatronRegistration.GENDER.toString(),   // single char.
                        fCustomer.getValue(PolarisTable.PatronRegistration.GENDER.toString()))
                .dateTime(PolarisTable.PatronRegistration.BIRTH_DATE.toString(), dob) 
                .dateTimeNow(PolarisTable.PatronRegistration.REGISTRATION_DATE.toString())
                .string(PolarisTable.PatronRegistration.FORMER_ID.toString(), null)   // Note for update on lost card.
                .tinyInt(PolarisTable.PatronRegistration.READING_LIST.toString(), "0")
                .string(PolarisTable.PatronRegistration.PHONE_FAX.toString(), null)
                .integer(PolarisTable.PatronRegistration.DELIVERY_OPTION_ID.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.DELIVERY_OPTION_ID.toString())) // polaris_sql.properties
                .integer(PolarisTable.PatronRegistration.STATISTICAL_CLASS_ID.toString()) // null
                .bit(PolarisTable.PatronRegistration.COLLECTION_EXEMPT.toString(), "0")
                .string(PolarisTable.PatronRegistration.ALT_EMAIL_ADDRESS.toString(), null)
                .bit(PolarisTable.PatronRegistration.EXCLUDE_FROM_OVERDUES.toString(), "0")
                .string(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString(), null)
                .integer(PolarisTable.PatronRegistration.SDI_EMAIL_FORMAT_ID.toString(), null) // null integer value.
                .bit(PolarisTable.PatronRegistration.SDI_POSITIVE_ASSENT.toString(), null)
                .dateTime(PolarisTable.PatronRegistration.SDI_POSITIVE_ASSENT_DATE.toString())
                .bit(PolarisTable.PatronRegistration.DELETION_EXEMPT.toString(), "0")
                .string(PolarisTable.PatronRegistration.PATRON_FULL_NAME.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.PATRON_FULL_NAME.toString()))
                .bit(PolarisTable.PatronRegistration.EXCLUDE_FROM_HOLDS.toString(), "0")
                .bit(PolarisTable.PatronRegistration.EXCLUDE_FROM_BILLS.toString(), "0")
                .integer(PolarisTable.PatronRegistration.EMAIL_FORMAT_ID.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.EMAIL_FORMAT_ID.toString()))
                .string(PolarisTable.PatronRegistration.PATRON_FIRST_LAST_NAME.toString(), 
                        fCustomer.getValue(PolarisTable.PatronRegistration.PATRON_FIRST_LAST_NAME.toString()))
                .string(PolarisTable.PatronRegistration.USERNAME.toString(), null)
                .dateTime(PolarisTable.PatronRegistration.MERGE_DATE.toString()) // null
                .integer(PolarisTable.PatronRegistration.MERGE_USER_ID.toString(), null)
                .string(PolarisTable.PatronRegistration.MERGE_BARCODE.toString(), null)
//                .string(PolarisTable.PatronRegistration.CELL_PHONE.toString(), null)
//                .integer(PolarisTable.PatronRegistration.CELL_PHONE_CARRIER_ID.toString(), null)
                .bit(PolarisTable.PatronRegistration.ENABLE_SMS.toString(), "0") 
                .integer(PolarisTable.PatronRegistration.REQUEST_PICKUP_BRANCH_ID.toString(), null)
                .integer(PolarisTable.PatronRegistration.PHONE1_CARRIER_ID.toString(), null)
                .integer(PolarisTable.PatronRegistration.PHONE2_CARRIER_ID.toString(), null)
                .integer(PolarisTable.PatronRegistration.PHONE3_CARRIER_ID.toString(), null)
                .integer(PolarisTable.PatronRegistration.ERECEIPT_OPTION_ID.toString(), null)
                .tinyInt(PolarisTable.PatronRegistration.TXT_PHONE_NUMBER.toString())
                .build();
        status = createPatronRegistrationCommand.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to create customer data " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronRegistration);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.UNAVAILABLE_SERVICE.toString()))
                    .build();
        }
        
        // *** ADD POSTAL CODE ***
//
//        SELECT PostalCodeID
//        FROM Polaris.Polaris.PostalCodes
//        WHERE PostalCode = (PostalCode)
//
//        IF Value IS NULL
        SQLSelectCommand selectPostalCodeID = new SQLSelectCommand.Builder(connector, this.postalCodes)
                .string(PolarisTable.PostalCodes.POSTAL_CODE_ID.toString())
                .whereString(PolarisTable.PostalCodes.POSTAL_CODE.toString(), 
                        fCustomer.getValue(PolarisTable.PostalCodes.POSTAL_CODE.toString()))
                .build();
        status = selectPostalCodeID.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed during postal code lookup for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.postalCodes);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                    .build();
        }
        
        String postalCodeID = Text.lastWord(status.getStdout(), 2);
        if (debug)
        {
            System.out.println("PATRON_POSTALCODE_ID:>>" + postalCodeID + "<< (if empty; good.)");
        }
        
        // Returns an empty string if not found.
        if (postalCodeID.isEmpty())
        {
            //        INSERT INTO Polaris.Polaris.PostalCode ( PostalCode , City , State , CountryID , County )
            //        VALUES ( (PostalCode) , (City) , (Province) , 2 , NULL )
            SQLInsertCommand insertPostalCode = new SQLInsertCommand.Builder(connector, this.postalCodes)
                    .string(PolarisTable.PostalCodes.POSTAL_CODE.toString(), 
                            fCustomer.getValue(PolarisTable.PostalCodes.POSTAL_CODE.toString())) // for the formatted value.
                    .string(PolarisTable.PostalCodes.CITY.toString(),
                            fCustomer.getValue(PolarisTable.PostalCodes.CITY.toString()))
                    .string(PolarisTable.PostalCodes.STATE.toString(),
                            fCustomer.getValue(PolarisTable.PostalCodes.STATE.toString()))
                    .integer(PolarisTable.PostalCodes.COUNTRY_ID.toString(),
                            fCustomer.getValue(PolarisTable.PostalCodes.COUNTRY_ID.toString()))
                    .string(PolarisTable.PostalCodes.COUNTY.toString(), null)
                    .build();
            status = insertPostalCode.execute();
            if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
            {
                System.out.println("**error failed during postal code creation for customer " 
                        + customer.get(CustomerFieldTypes.ID) + " in table: "
                        + this.postalCodes);
                // When this command gets run it returns a useful message and error status for customer.
                return new DummyCommand.Builder()
                        .setStatus(1)
                        .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                        .build();
            }
            // if that worked then we can rerun the postal code selection again and get the ID for the next step.
            // Since the query pre-exists just re-run it.
            status = selectPostalCodeID.execute();
            if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
            {
                System.out.println("**error failed during postal code lookup for customer " 
                        + customer.get(CustomerFieldTypes.ID) + " in table: "
                        + this.postalCodes);
                // When this command gets run it returns a useful message and error status for customer.
                return new DummyCommand.Builder()
                        .setStatus(1)
                        .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                        .build();
            }
            postalCodeID = Text.lastWord(status.getStdout(), 2);
            if (debug)
            {
                System.out.println("PATRON_POSTALCODE_ID:>>" + postalCodeID + "<<");
            }
            if (postalCodeID.isEmpty())
            {
                System.out.println("**error failed during postal code lookup for customer " 
                        + customer.get(CustomerFieldTypes.ID) + " in table: "
                        + this.postalCodes);
                // When this command gets run it returns a useful message and error status for customer.
                return new DummyCommand.Builder()
                        .setStatus(1)
                        .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                        .build();
            }
        } // else postal code found, so we should have the id by now.
 
//        ADD ADDRESS
        SQLInsertCommand insertAddress = new SQLInsertCommand.Builder(connector, this.addressTable)
            .integer(PolarisTable.Addresses.POSTAL_CODE_ID.toString(), postalCodeID)
            .string(PolarisTable.Addresses.STREET_ONE.toString(), 
                    fCustomer.getValue(PolarisTable.Addresses.STREET_ONE.toString()))
            // we don't have more than one street value.
            .string(PolarisTable.Addresses.STREET_TWO.toString(), null)
            .string(PolarisTable.Addresses.ZIP_PLUS_FOUR.toString(), null)
            .string(PolarisTable.Addresses.MUNICIPALITY_NAME.toString(), null)
            .build();
        status = insertAddress.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed during address creation creation for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.addressTable);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                    .build();
        }
//        ADD ADDRESS TO PATRON
//        SELECT IDENT_CURRENT('Polaris.Addresses') STORE AS VARIABLE (AddressID)
//        * NOTE this has to be done immediately - it returns the last inserted value, it's possible for another value to be inserted quite quickly.  Can error check this if necessary (Example below)
        SQLSelectCommand selectLastAddressID = new SQLSelectCommand.Builder(connector, this.addressTable)
            .integer("AddressID")
            .whereInteger("AddressID", "IDENT_CURRENT('Polaris.Addresses')") // is there any possible way this will work?
            .build();
        status = selectLastAddressID.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to find IDENT_CURRENT for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.addressTable);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                    .build();
        }
        String polarisAddressID = Text.lastWord(status.getStdout(), 2);
        System.out.println("LAST_ADDRESS_ID:>>" + polarisAddressID + "<<");
//        --
//        SELECT PostalCodeID
//        FROM Polaris.Polaris.Addresses
//        WHERE AddressID = (AddressID)
        SQLSelectCommand selectPostalCodeFromAddressTable = new SQLSelectCommand.Builder(connector, this.addressTable)
            .integer(PolarisTable.Addresses.POSTAL_CODE_ID.toString())
            .whereInteger(PolarisTable.Addresses.ADDRESS_ID.toString(), polarisAddressID)
            .build();
//        IF Query == (PostalCode) OK
        status = selectPostalCodeFromAddressTable.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to find Postal code reference while creating address record for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.addressTable);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                    .build();
        }
        
        
        // So you insert an address in the Polaris.Addresses table and it's given a unique ID,
        // but if your not fast enough, another customer at another site might register at the
        // same time. If you dilly-dally around long enough when you go to get the address ID
        // with IDENT_CURRENT, IDENT_CURRENT may have moved on so you may have to back up.
        // that is try again with a decremented value.
        // ================================== revisit if not getting the correct address ==================
        String postalCodeIDFromAddressTable = Text.lastWord(status.getStdout(), 2);
        System.out.println("PATRON_POSTALCODE_ID from AddressTable:>>" + postalCodeIDFromAddressTable + "<<");
        if (postalCodeID.equalsIgnoreCase(postalCodeIDFromAddressTable))
        {
            //        IF Query == (PostalCode) OK
            if (this.debug)System.out.println("PATRON_POSTALCODE_ID Found a MATCH " + postalCodeIDFromAddressTable);
        }
        else
        {
            //        ELSE Run same query and decrement (AddressID) until we get a match, then store new value as (AddressID).
            if (this.debug) System.out.println("PATRON_POSTALCODE_ID did match: postalCodeID='" + postalCodeID
                   + "' and postalCodeIDFromAddressTable='" + postalCodeIDFromAddressTable + "'");
        }
        // ================================== revisit if not getting the correct address ==================
        // ... and finally...
//        INSERT INTO Polaris.Polaris.PatronAddresses
//        VALUES ( (PatronID) , (AddressID) , 2 , 'Home' , 0 , NULL , NULL )
//        INSERT INTO Polaris.Polaris.PatronAddresses
//        VALUES ( (PatronID) , (AddressID) , 3 , 'Home' , 0 , NULL , NULL )
//        INSERT INTO Polaris.Polaris.PatronAddresses
//        VALUES ( (PatronID) , (AddressID) , 4 , 'Home' , 0 , NULL , NULL )
//        *Yes, must run this three times, one for each number value
        SQLInsertCommand insertPatronIDAddressID = new SQLInsertCommand.Builder(connector, this.patronAddresses)
            .integer(PolarisTable.PatronAddresses.PATRON_ID.toString(), polarisPatronID)
            .integer(PolarisTable.PatronAddresses.ADDRESS_ID.toString(), polarisAddressID)
            .integer(PolarisTable.PatronAddresses.ADDRESS_TYPE_ID.toString(), "2")
            // polaris_sql.properties
            .string(PolarisTable.PatronAddresses.FREE_TEXT_LABEL.toString(), 
                    fCustomer.getValue(PolarisTable.PatronAddresses.FREE_TEXT_LABEL.toString()))
            .bit(PolarisTable.PatronAddresses.VERIFIED.toString(), "0")
            .dateTime(PolarisTable.PatronAddresses.VERIFICATION_DATE.toString()) // null
            .integer(PolarisTable.PatronAddresses.POLARIS_USER_ID.toString())
            .build();
        status = insertPatronIDAddressID.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed during insertPatronIDAddressID 1 creation for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronAddresses);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                    .build();
        }
        // iteration number 2 for AddressTypeID 3
        insertPatronIDAddressID = new SQLInsertCommand.Builder(connector, this.patronAddresses)
            .integer(PolarisTable.PatronAddresses.PATRON_ID.toString(), polarisPatronID)
            .integer(PolarisTable.PatronAddresses.ADDRESS_ID.toString(), polarisAddressID)
            .integer(PolarisTable.PatronAddresses.ADDRESS_TYPE_ID.toString(), "3")
            .string(PolarisTable.PatronAddresses.FREE_TEXT_LABEL.toString(), 
                    fCustomer.getValue(PolarisTable.PatronAddresses.FREE_TEXT_LABEL.toString()))
            .bit(PolarisTable.PatronAddresses.VERIFIED.toString(), "0")
            .dateTime(PolarisTable.PatronAddresses.VERIFICATION_DATE.toString()) // null
            .integer(PolarisTable.PatronAddresses.POLARIS_USER_ID.toString())
            .build();
        status = insertPatronIDAddressID.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed during insertPatronIDAddressID 2 creation for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronAddresses);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                    .build();
        }
        // iteration number 3 for AddressTypeID 4
        insertPatronIDAddressID = new SQLInsertCommand.Builder(connector, this.patronAddresses)
                .integer(PolarisTable.PatronAddresses.PATRON_ID.toString(), polarisPatronID)
                .integer(PolarisTable.PatronAddresses.ADDRESS_ID.toString(), polarisAddressID)
                .integer(PolarisTable.PatronAddresses.ADDRESS_TYPE_ID.toString(), "4")
                .string(PolarisTable.PatronAddresses.FREE_TEXT_LABEL.toString(), 
                        fCustomer.getValue(PolarisTable.PatronAddresses.FREE_TEXT_LABEL.toString()))
                .bit(PolarisTable.PatronAddresses.VERIFIED.toString(), "0")
                .dateTime(PolarisTable.PatronAddresses.VERIFICATION_DATE.toString()) // null
                .integer(PolarisTable.PatronAddresses.POLARIS_USER_ID.toString())
                .build();
        return insertPatronIDAddressID;
    }

    /**
     * This method will update all fields regardless of what data changed.
     * @param customer object from melibraries.ca.
     * @param response response container for storing results.
     * @param normalizer to make last-minute changes to customer objects before loading.
     * @return the last command object to be run the whole process to complete. During
     * the execution of this method several SQL commands will be executed. If the process
     * proceeds normally the ultimate SQL command is sent back to be executed by 
     * the responder.
     */
    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        // we have a customer let's convert them to a PolarisSQLFormatted user.
        FormattedCustomer fCustomer = new PolarisSQLFormattedCustomer(customer);
        // apply library centric normalization to the customer account.
        normalizer.finalize(customer, fCustomer, response);
        List<String> userSQLFileLines = fCustomer.getFormattedCustomer();
        // Output the customer's data as a receipt in case they come back with questions.
        new DumpUser.Builder(customer, this.loadDir, DumpUser.FileType.txt)
                .set(userSQLFileLines)
                .build();
        // This is a class variable because the Responder will need to run the last command and return results.
        this.connector = new SQLConnector.Builder(host, driver, database)
            .user(user)
            .password(password)
            .build();
        // Add code to first check for LOSTCARD and if found search on ALT_ID field first.
        String oldOrNewBarCode = fCustomer.getValue(PolarisTable.Patrons.BARCODE.toString());
        if (customer.isLostCard())
        {
            oldOrNewBarCode = customer.get(CustomerFieldTypes.ALTERNATE_ID);
        }
        SQLSelectCommand getPatronIDCommand = new SQLSelectCommand.Builder(this.connector, this.patronsTable)
            .string(PolarisTable.Patrons.PATRON_ID.toString())
            .whereString(PolarisTable.Patrons.BARCODE.toString(), oldOrNewBarCode)
            .build();
        
        CommandStatus status = getPatronIDCommand.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to find customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronsTable);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()))
                    .build();
        }
        // If the above command completed successfully you will get a result like:
        // 492730 
        // 3
        // We need the second last value from the result set which should be the 
        // patron's ID (not barcode). If the search was unsuccessful the SQL exception
        // is caught in the checking of the command status if statement above.
        String polarisPatronID = Text.lastWord(status.getStdout(), 2);
        if (this.debug)
        {
            System.out.println("PATRON_ID: '" + polarisPatronID + "'");
        }
        // Horizon systems play fast and loose with lost cards. They don't keep
        // track of the previous user id so the customer returns, melibraries.ca thinks
        // that they should be an update but they are actually a replaced card.
        // Horizon systems can't tell us that the account is lost so we guard for it here.
        if (polarisPatronID.isEmpty())
        {
            System.out.println("PATRON_ID: Couldn't find the patron's ID for updating.");
            // Duplicates user.
            // return this.getCreateUserCommand(customer, response, normalizer);
            // or we could do a LOST card but we don't get the original ID from horizon systems.
            UserLostFile lostCustomer = new UserLostFile(customer, this.getCustomerLoadDirectory());
            lostCustomer.recordUserDataMessage("Lost customer possibly from Horizon system (?), "
                    + "search by customer ID.");
            return new DummyCommand.Builder()
                .setStatus(0)
                .setStderr("We may already have you registered, but by a different card number."
                        + " Please contact staff for assistance with updating your account.")
                .build();
        }
        // Confirmed: replace the old bar code with the replacement bar code.
        else if (customer.isLostCard())
        {
            SQLUpdateCommand updatePatronsBarcode = new SQLUpdateCommand.Builder(this.connector, this.patronsTable)
                .string(PolarisTable.Patrons.BARCODE.toString(), fCustomer.getValue(PolarisTable.Patrons.BARCODE.toString()))
                .whereInteger(PolarisTable.Patrons.PATRON_ID.toString(), polarisPatronID)
                .build();
            status = updatePatronsBarcode.execute();
            if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
            {
                System.out.println("**error failed to update patron's barcode: "
                        + this.patronsTable);
                return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                    .build();
            } 
        }
        // Get the date today in system format
        String today; // Get ready with dob and expiry in acceptable format.
        String expiry = fCustomer.getValue(PolarisTable.PatronRegistration.EXPIRATION_DATE.toString());
        try
        {
            today = DateComparer.ANSIToConfigDate(DateComparer.ANSIToday());
        }
        catch (ParseException e)
        {
            System.out.println("**error failed to convert today's date or customer expiry " 
                    + customer.get(CustomerFieldTypes.DOB) + " during customer update.");
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                .setStatus(1)
                .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                .build();
        }
        
        if (this.debug)
        {
            System.out.println("---   today>"+today);
            System.out.println("---  expiry>"+expiry);
        }
        // Now we update the patron Registration table with new data.
        SQLUpdateCommand updatePatronRegistration = new SQLUpdateCommand.Builder(this.connector, this.patronRegistration)
            .string(PolarisTable.PatronRegistration.NAME_LAST.toString(), 
                    fCustomer.getValue(PolarisTable.PatronRegistration.NAME_LAST.toString()))
            .string(PolarisTable.PatronRegistration.NAME_FIRST.toString(), 
                    fCustomer.getValue(PolarisTable.PatronRegistration.NAME_FIRST.toString()))
            .string(PolarisTable.PatronRegistration.NAME_MIDDLE.toString(),
                    "unspecified")
            .string(PolarisTable.PatronRegistration.PHONE_VOICE_1.toString(), 
                    fCustomer.getValue(PolarisTable.PatronRegistration.PHONE_VOICE_1.toString()))
            .string(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), 
                    fCustomer.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()))
            .string(PolarisTable.PatronRegistration.PASSWORD.toString(), 
                    fCustomer.getValue(PolarisTable.PatronRegistration.PASSWORD.toString()))
            .dateTime(PolarisTable.PatronRegistration.UPDATE_DATE.toString(), 
                    today)
            .dateTime(PolarisTable.PatronRegistration.EXPIRATION_DATE.toString(), 
                    expiry)
            .dateTime(PolarisTable.PatronRegistration.ADDR_CHECK_DATE.toString(), 
                    expiry)
            .setChar(PolarisTable.PatronRegistration.GENDER.toString(), 
                    fCustomer.getValue(PolarisTable.PatronRegistration.GENDER.toString()))
            .string(PolarisTable.PatronRegistration.PATRON_FULL_NAME.toString(), 
                    fCustomer.getValue(PolarisTable.PatronRegistration.PATRON_FULL_NAME.toString()))
            .string(PolarisTable.PatronRegistration.PATRON_FIRST_LAST_NAME.toString(), 
                    fCustomer.getValue(PolarisTable.PatronRegistration.PATRON_FIRST_LAST_NAME.toString()))
            .whereInteger(PolarisTable.PatronRegistration.PATRON_ID.toString(), polarisPatronID)
            .build();
        status = updatePatronRegistration.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to update customer account in table: "
                    + this.patronRegistration);
            return new DummyCommand.Builder()
                .setStatus(1)
                .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                .build();
        } 
        // Get the postal code id for the 
        SQLSelectCommand selectPostalCodeID = new SQLSelectCommand.Builder(connector, this.postalCodes)
            .string(PolarisTable.PostalCodes.POSTAL_CODE_ID.toString())
            .whereString(PolarisTable.PostalCodes.POSTAL_CODE.toString(), 
                    fCustomer.getValue(PolarisTable.PostalCodes.POSTAL_CODE.toString()))
            .build();
        status = selectPostalCodeID.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed during postal code lookup for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.postalCodes);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                    .build();
        }
        
        String postalCodeID = Text.lastWord(status.getStdout(), 2);
        if (this.debug) System.out.println("PATRON_POSTALCODE_ID: '" + postalCodeID + "' (can be empty)");
        // The search for the postal code will fail if the customer's postal code changed. If that is the
        // case then we will add a new one and get the PostalCodeID...
        if (postalCodeID.isEmpty())
        {
            SQLInsertCommand insertPostalCode = new SQLInsertCommand.Builder(this.connector, this.postalCodes)
                .string(PolarisTable.PostalCodes.POSTAL_CODE.toString(), 
                        fCustomer.getValue(PolarisTable.PostalCodes.POSTAL_CODE.toString()))
                .string(PolarisTable.PostalCodes.CITY.toString(), 
                        fCustomer.getValue(PolarisTable.PostalCodes.CITY.toString()))
                .string(PolarisTable.PostalCodes.STATE.toString(), 
                        fCustomer.getValue(PolarisTable.PostalCodes.STATE.toString()))
                .integer(PolarisTable.PostalCodes.COUNTRY_ID.toString(), 
                        fCustomer.getValue(PolarisTable.PostalCodes.COUNTRY_ID.toString()))
                .string(PolarisTable.PostalCodes.COUNTY.toString(), null)
                .build();
            status = insertPostalCode.execute();
            if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
            {
                System.out.println("**error failed during postal code creation during update for customer " 
                        + customer.get(CustomerFieldTypes.ID) + " in table: "
                        + this.postalCodes);
                // When this command gets run it returns a useful message and error status for customer.
                return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                    .build();
            }
            // Query already built, so run it again.
            status = selectPostalCodeID.execute();
            if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
            {
                System.out.println("**error failed during postal code lookup for customer " 
                        + customer.get(CustomerFieldTypes.ID) + " in table: "
                        + this.postalCodes);
                // When this command gets run it returns a useful message and error status for customer.
                return new DummyCommand.Builder()
                        .setStatus(1)
                        .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                        .build();
            }
            postalCodeID = Text.lastWord(status.getStdout(), 2);
            if (this.debug) System.out.println("PATRON_POSTALCODE_ID PASS 2: '" + postalCodeID + "' (not ok to be empty)");
        } // end if (postalCodeID.isEmpty())
        ////////////////////////////////////////////////////////////////////////
        //
        // Richard changes, September 25, 2014
        //
        ////////////////////////////////////////////////////////////////////////
        //        SELECT TOP 1 AddressID
        SQLSelectCommand selectAddressID = new SQLSelectCommand.Builder(this.connector, this.patronAddresses)
                .integer(PolarisTable.PatronAddresses.ADDRESS_ID.toString())
                .whereString(PolarisTable.PatronAddresses.PATRON_ID.toString(), polarisPatronID)
                .build();
        // Once that returns we need to grep out the StreetOne match for our customer. This is 
        // required because any one postal code can represent more than one street address.
        status = selectAddressID.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to find address ID for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronAddresses);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                    .build();
        }
        // On success the output may look something like this:
        // '492721
        //  492723
        //  492724 
        //  3'
        // Now grab the top result...
        String polarisAddressID = Text.firstWord(status.getStdout());
        if (this.debug) System.out.println("ADDRESS_ID: '" 
                + polarisAddressID + "' (should not be empty if this IS an update)");
        // Once we have the address id we then can just update the Addresses table.
        Command finalCommand;
        if (polarisAddressID.isEmpty())
        {
            System.out.println("**error failed to find address id for customer " 
                + customer.get(CustomerFieldTypes.ID) + " in table: "
                + this.postalCodes + ". Is this really an update, or a create?");
            // When this command gets run it returns a useful message and error status for customer.
            finalCommand = new DummyCommand.Builder()
                .setStatus(1)
                .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                .build();
        }
        else // found the address id so let's update the Addresses table.
        {
            finalCommand = new SQLUpdateCommand.Builder(this.connector, this.addressTable)
                    .integer(PolarisTable.Addresses.POSTAL_CODE_ID.toString(), postalCodeID)
                    .string(PolarisTable.Addresses.STREET_ONE.toString(), 
                            fCustomer.getValue(PolarisTable.Addresses.STREET_ONE.toString()))
                    .string(PolarisTable.Addresses.STREET_TWO.toString(), null) // we don't have a street two
                    .whereInteger(PolarisTable.Addresses.ADDRESS_ID.toString(), polarisAddressID)
                    .build();
        }
        return finalCommand;
    }
    
    

    @Override
    public Command getStatusCommand(Response response)
    {
        // Intentionally left blank should use the SIP2 request.
        throw new ConfigurationException("Polaris SQL does not support ILS status queries "
                + "Please review your environment.properties configuration");
    }

    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response)
    {
        // TODO let's parse the returning POLARIS_SQL message for results.
        ResponseTypes responseType = status.getStatus();
        switch(responseType)
        {
            case COMMAND_COMPLETED:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean tidy()
    {
        this.connector.close();
        return true;
    }
    
    @Override
    public String toString()
    {
        return PolarisSQLRequestBuilder.class.getName();
    }

    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        // TODO implement the remainder of this class. Technically
        // the PolarisSQLRequestBuilder can getCustomer() but use SIP2
        // instead. Everyone does so so far. If you do decide to use
        // polaris-sql in the environment.properties get-protocol you
        // will need to finish this class.
        return new SQLCustomerMessage(stdout, true);
    }
}
