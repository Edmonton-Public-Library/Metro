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
import api.SIPCustomerMessage;
import api.SQLConnector;
import api.SQLInsertCommand;
import api.SQLSelectCommand;
import api.SQLUpdateCommand;
import java.text.ParseException;
import java.util.Properties;
import mecard.QueryTypes;
import mecard.Response;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PropertyReader;
import mecard.config.PolarisSQLPropertyTypes;
import mecard.config.MessagesTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.polaris.PolarisSQLCustomerFormatter;
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
    
    private final String host;
    private final String driver;
    private final String database;
    private final String user;
    private final String password;
    private final Properties messages;
    private SQLConnector connector;
    
    public PolarisSQLRequestBuilder()
    {
        messages = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        
        Properties properties = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        host = properties.getProperty(PolarisSQLPropertyTypes.HOST.toString());
        driver = properties.getProperty(PolarisSQLPropertyTypes.CONNECTOR_TYPE.toString()); // AKA connector-type.
        database = properties.getProperty(PolarisSQLPropertyTypes.DATABASE.toString());
        user = properties.getProperty(PolarisSQLPropertyTypes.USERNAME.toString());
        password = properties.getProperty(PolarisSQLPropertyTypes.PASSWORD.toString());
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
        messages = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        this.host = host;
        this.driver = driver;
        this.database = database;
        this.user = user;
        this.password = password;
    }
    
    @Override
    public CustomerFormatter getFormatter()
    {
        // TODO: implement this class.
        return new PolarisSQLCustomerFormatter();
    }

    @Override
    public Command getCustomerCommand(String barcode, String userPin, Response response)
    {
        // TODO: Normally we would use SIP2 for this command but I am going to
        // do some testing with the Polaris DB.
        this.connector = new SQLConnector.Builder(host, driver, database)
                .user(user)
                .password(password)
                .build();
        /* First we need the PatronID
        SELECT PatronID
        FROM Polaris.Polaris.Patrons
        WHERE Barcode = (Barcode)
        */
        //Polaris, Polaris, PatronRegistration, PatronID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        //Polaris, Polaris, PatronRegistration, LanguageID, 5, smallint, 5, 2, 0, 10, 1, null, null, 5, null, null, 2, YES, 
        //Polaris, Polaris, PatronRegistration, NameFirst, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 3, YES, 
        //Polaris, Polaris, PatronRegistration, NameLast, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 4, YES, 
        //Polaris, Polaris, PatronRegistration, NameMiddle, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 5, YES, 
        //Polaris, Polaris, PatronRegistration, NameTitle, 12, varchar, 8, 8, null, null, 1, null, null, 12, null, 8, 6, YES, 
        //Polaris, Polaris, PatronRegistration, NameSuffix, 12, varchar, 4, 4, null, null, 1, null, null, 12, null, 4, 7, YES, 
        //Polaris, Polaris, PatronRegistration, PhoneVoice1, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 8, YES, 
        //Polaris, Polaris, PatronRegistration, PhoneVoice2, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 9, YES, 
        //Polaris, Polaris, PatronRegistration, PhoneVoice3, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 10, YES, 
        //Polaris, Polaris, PatronRegistration, EmailAddress, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 11, YES, 
        //Polaris, Polaris, PatronRegistration, Password, 12, varchar, 16, 16, null, null, 1, null, null, 12, null, 16, 12, YES, 
        //Polaris, Polaris, PatronRegistration, EntryDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 13, YES, 
        //Polaris, Polaris, PatronRegistration, ExpirationDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 14, YES, 
        //Polaris, Polaris, PatronRegistration, AddrCheckDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 15, YES, 
        //Polaris, Polaris, PatronRegistration, UpdateDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 16, YES, 
        //Polaris, Polaris, PatronRegistration, User1, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 17, YES, 
        //Polaris, Polaris, PatronRegistration, User2, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 18, YES, 
        //Polaris, Polaris, PatronRegistration, User3, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 19, YES, 
        //Polaris, Polaris, PatronRegistration, User4, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 20, YES, 
        //Polaris, Polaris, PatronRegistration, User5, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 21, YES, 
        //Polaris, Polaris, PatronRegistration, Gender, 1, char, 1, 1, null, null, 1, null, null, 1, null, 1, 22, YES, 
        //Polaris, Polaris, PatronRegistration, Birthdate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 23, YES, 
        //Polaris, Polaris, PatronRegistration, RegistrationDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 24, YES, 
        //Polaris, Polaris, PatronRegistration, FormerID, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 25, YES, 
        //Polaris, Polaris, PatronRegistration, ReadingList, -6, tinyint, 3, 1, 0, 10, 0, null, null, -6, null, null, 26, NO, 
        //Polaris, Polaris, PatronRegistration, PhoneFAX, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 27, YES, 
        //Polaris, Polaris, PatronRegistration, DeliveryOptionID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 28, YES, 
        //Polaris, Polaris, PatronRegistration, StatisticalClassID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 29, YES, 
        //Polaris, Polaris, PatronRegistration, CollectionExempt, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 30, NO, 
        //Polaris, Polaris, PatronRegistration, AltEmailAddress, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 31, YES, 
        //Polaris, Polaris, PatronRegistration, ExcludeFromOverdues, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 32, NO, 
        //Polaris, Polaris, PatronRegistration, SDIEmailAddress, 12, varchar, 150, 150, null, null, 1, null, null, 12, null, 150, 33, YES, 
        //Polaris, Polaris, PatronRegistration, SDIEmailFormatID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 34, YES, 
        //Polaris, Polaris, PatronRegistration, SDIPositiveAssent, -7, bit, 1, 1, null, null, 1, null, null, -7, null, null, 35, YES, 
        //Polaris, Polaris, PatronRegistration, SDIPositiveAssentDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 36, YES, 
        //Polaris, Polaris, PatronRegistration, DeletionExempt, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 37, NO, 
        //Polaris, Polaris, PatronRegistration, PatronFullName, 12, varchar, 100, 100, null, null, 1, null, null, 12, null, 100, 38, YES, 
        //Polaris, Polaris, PatronRegistration, ExcludeFromHolds, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 39, NO, 
        //Polaris, Polaris, PatronRegistration, ExcludeFromBills, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 40, NO, 
        //Polaris, Polaris, PatronRegistration, EmailFormatID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 41, NO, 
        //Polaris, Polaris, PatronRegistration, PatronFirstLastName, 12, varchar, 100, 100, null, null, 1, null, null, 12, null, 100, 42, YES, 
        //Polaris, Polaris, PatronRegistration, Username, 12, varchar, 50, 50, null, null, 1, null, null, 12, null, 50, 43, YES, 
        //Polaris, Polaris, PatronRegistration, MergeDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 44, YES, 
        //Polaris, Polaris, PatronRegistration, MergeUserID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 45, YES, 
        //Polaris, Polaris, PatronRegistration, MergeBarcode, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 46, YES, 
        //Polaris, Polaris, PatronRegistration, CellPhone, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 47, YES, 
        //Polaris, Polaris, PatronRegistration, CellPhoneCarrierID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 48, YES, 
        //Polaris, Polaris, PatronRegistration, EnableSMS, -7, bit, 1, 1, null, null, 1, null, null, -7, null, null, 49, YES, 
        //Polaris, Polaris, PatronRegistration, RequestPickupBranchID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 50, YES, 
        //Polaris, Polaris, PatronRegistration, Phone1CarrierID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 51, YES, 
        //Polaris, Polaris, PatronRegistration, Phone2CarrierID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 52, YES, 
        //Polaris, Polaris, PatronRegistration, Phone3CarrierID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 53, YES, 
        //Polaris, Polaris, PatronRegistration, eReceiptOptionID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 54, YES, 
        //Polaris, Polaris, PatronRegistration, TxtPhoneNumber, -6, tinyint, 3, 1, 0, 10, 1, null, null, -6, null, null, 55, YES,
//        SQLDescribeCommand describe = new SQLDescribeCommand.Builder(connector, "PatronRegistration")// use this version for query DESCRIBE table
//            .build();
//        System.out.println("COMMAND==>"+describe.toString()+"<==");
//        CommandStatus status = describe.execute();
//        System.out.println("STATUS==>" + status.getStdout() + "<==");
//        Polaris, Polaris, Patrons, PatronID, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
//        Polaris, Polaris, Patrons, PatronCodeID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 2, NO, 
//        Polaris, Polaris, Patrons, OrganizationID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 3, NO, 
//        Polaris, Polaris, Patrons, CreatorID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 4, NO, 
//        Polaris, Polaris, Patrons, ModifierID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 5, YES, 
//        Polaris, Polaris, Patrons, Barcode, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 6, YES, 
//        Polaris, Polaris, Patrons, SystemBlocks, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 7, NO, 
//        Polaris, Polaris, Patrons, YTDCircCount, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 8, NO, 
//        Polaris, Polaris, Patrons, LifetimeCircCount, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 9, NO, 
//        Polaris, Polaris, Patrons, LastActivityDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 10, YES, 
//        Polaris, Polaris, Patrons, ClaimCount, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 11, YES, 
//        Polaris, Polaris, Patrons, LostItemCount, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 12, YES, 
//        Polaris, Polaris, Patrons, ChargesAmount, 3, money, 19, 21, 4, 10, 0, null, null, 3, null, null, 13, NO, 
//        Polaris, Polaris, Patrons, CreditsAmount, 3, money, 19, 21, 4, 10, 0, null, null, 3, null, null, 14, NO,
        
//        SQLDescribeCommand describePatronsTable = new SQLDescribeCommand.Builder(connector, "Patrons")// use this version for query DESCRIBE table
//            .build();
//        status = describePatronsTable.execute();
//        System.out.println("STATUS==>" + status.getStdout() + "<==");
        
        //Polaris, Polaris, Addresses, AddressID, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        //Polaris, Polaris, Addresses, PostalCodeID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 2, NO, 
        //Polaris, Polaris, Addresses, StreetOne, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 3, YES, 
        //Polaris, Polaris, Addresses, StreetTwo, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 4, YES, 
        //Polaris, Polaris, Addresses, ZipPlusFour, 12, varchar, 4, 4, null, null, 1, null, null, 12, null, 4, 5, YES, 
        //Polaris, Polaris, Addresses, MunicipalityName, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 6, YES,
//        SQLDescribeCommand describeAddressesTable = new SQLDescribeCommand.Builder(connector, "Addresses")// use this version for query DESCRIBE table
//            .build();
//        status = describeAddressesTable.execute();
//        System.out.println("STATUS==>" + status.getStdout() + "<==");
//        PostalCodes
        //Polaris, Polaris, PostalCodes, PostalCodeID, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        //Polaris, Polaris, PostalCodes, PostalCode, 12, varchar, 10, 10, null, null, 1, null, null, 12, null, 10, 2, YES, 
        //Polaris, Polaris, PostalCodes, City, 12, varchar, 32, 32, null, null, 0, null, null, 12, null, 32, 3, NO, 
        //Polaris, Polaris, PostalCodes, State, 12, varchar, 32, 32, null, null, 0, null, null, 12, null, 32, 4, NO, 
        //Polaris, Polaris, PostalCodes, CountryID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 5, YES, 
        //Polaris, Polaris, PostalCodes, County, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 6, YES,
        
//        SQLDescribeCommand postalCodeTable = new SQLDescribeCommand.Builder(connector, "PostalCodes")// use this version for query DESCRIBE table
//            .build();
//        status = postalCodeTable.execute();
//        System.out.println("STATUS==>" + status.getStdout() + "<==");
        
        // PatronAddresses
        //Polaris, Polaris, PatronAddresses, PatronID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        //Polaris, Polaris, PatronAddresses, AddressID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 2, YES, 
        //Polaris, Polaris, PatronAddresses, AddressTypeID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 3, NO, 
        //Polaris, Polaris, PatronAddresses, FreeTextLabel, 12, varchar, 30, 30, null, null, 1, null, null, 12, null, 30, 4, YES, 
        //Polaris, Polaris, PatronAddresses, Verified, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 5, NO, 
        //Polaris, Polaris, PatronAddresses, VerificationDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 6, YES, 
        //Polaris, Polaris, PatronAddresses, PolarisUserID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 7, YES,
//        SQLDescribeCommand patronAddresses = new SQLDescribeCommand.Builder(connector, "PatronAddresses")// use this version for query DESCRIBE table
//            .build();
//        status = patronAddresses.execute();
//        System.out.println("STATUS==>" + status.getStdout() + "<==");
       
        
        System.out.println("USERID:" + barcode);
        SQLSelectCommand selectUser = new SQLSelectCommand.Builder(connector, this.patronsTable)
//            .integer("PatronID")
//            .date("LastActivityDate")
            .string("Barcode")
            .whereString("Barcode", barcode)
            .build();
        System.out.println("COMMAND==>"+selectUser.toString()+"<==");
        
        return selectUser;
    }

    @Override
    public Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        // This is a class variable because the Responder will need to run the last command and return results.
        this.connector = new SQLConnector.Builder(host, driver, database)
                .user(user)
                .password(password)
                .build();
        
        //Polaris, Polaris, Patrons, PatronID, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        //Polaris, Polaris, Patrons, PatronCodeID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 2, NO, 
        //Polaris, Polaris, Patrons, OrganizationID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 3, NO, 
        //Polaris, Polaris, Patrons, CreatorID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 4, NO, 
        //Polaris, Polaris, Patrons, ModifierID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 5, YES, 
        //Polaris, Polaris, Patrons, Barcode, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 6, YES, 
        //Polaris, Polaris, Patrons, SystemBlocks, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 7, NO, 
        //Polaris, Polaris, Patrons, YTDCircCount, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 8, NO, 
        //Polaris, Polaris, Patrons, LifetimeCircCount, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 9, NO, 
        //Polaris, Polaris, Patrons, LastActivityDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 10, YES, 
        //Polaris, Polaris, Patrons, ClaimCount, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 11, YES, 
        //Polaris, Polaris, Patrons, LostItemCount, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 12, YES, 
        //Polaris, Polaris, Patrons, ChargesAmount, 3, money, 19, 21, 4, 10, 0, null, null, 3, null, null, 13, NO, 
        //Polaris, Polaris, Patrons, CreditsAmount, 3, money, 19, 21, 4, 10, 0, null, null, 3, null, null, 14, NO,
//        INSERT INTO Polaris.Polaris.Patrons ( PatronCodeID , OrganizationID , CreatorID , ModifierID , Barcode , 
//                  SystemBlocks , YTDCircCount , LifetimeCircCount , LastActivityDate , ClaimCount , LostItemCount , 
//                  ChargesAmount , CreditsAmount )
        // TODO: These values should be in the polaris_sql.properties file unless they are not unique to an institution.
//        VALUES ( 25 , 303 , 1831 , NULL , (Barcode) , 0 , 0 , 0 , GETDATE() , 0 , 0 , 0, 0)
//
        SQLInsertCommand createPatronCommand = new SQLInsertCommand.Builder(connector, this.patronsTable)
                .integer("PatronCodeID", "25")    // polaris_sql.properties
                .integer("OrganizationID", "303")// polaris_sql.properties
                .integer("CreatorID", "1831")    // polaris_sql.properties
                .integer("ModifierID")
                .string("Barcode", customer.get(CustomerFieldTypes.ID))
                .integer("SystemBlocks", "0")
                .integer("YTDCircCount", "0")
                .integer("LifetimeCircCount", "0")
                .dateTimeNow("LastActivityDate")
                .integer("ClaimCount", "0")
                .integer("LostItemCount", "0")
                .money("ChargesAmount", "0")
                .money("CreditsAmount", "0")
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
//        String polarisPatronID = status.getStdout();
        // Also we have to be careful because multiple id's can be returned if the 
        // customer was created more than once. If that is so get the highest or last number
        // it is 'likely' to be the most recent and most valid.
        // 399565\n399566\n399567\n399568\n...
        String polarisPatronID = Text.lastWord(status.getStdout(), 2);
        System.out.println("PATRON_ID:>>" + polarisPatronID + "<<");
        
        // Get ready with dob and expiry in acceptable format.
        String dob;
        String expiry;
        try
        {
            dob = DateComparer.ANSIToConfigDate(customer.get(CustomerFieldTypes.DOB));
            expiry = DateComparer.ANSIToConfigDate(customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES));
        }
        catch (ParseException e)
        {
            System.out.println("**error failed to convert customer DOB, or expiry " 
                    + customer.get(CustomerFieldTypes.DOB) + " during customer creation.");
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()))
                    .build();
        }
        System.out.println("***PatronID>"+polarisPatronID);
        System.out.println("***     dob>"+dob);
        System.out.println("***  expiry>"+expiry);
        
        
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
                // PatronID, 4, int, 10, PatronID
                .integer("PatronID", polarisPatronID)
                // LanguageID, 5, smallint, 5, '1'
                .smallInt("LanguageID", "1")       // polaris_sql.properties
                // NameFirst, 12, varchar, 32,
                .string("NameFirst", customer.get(CustomerFieldTypes.FIRSTNAME))
                // NameLast, 12, varchar, 32,
                .string("NameLast", customer.get(CustomerFieldTypes.LASTNAME))
                // NameMiddle, 12, varchar, 32,
                .string("NameMiddle", null)
                // NameTitle, 12, varchar, 8,
                .string("NameTitle", null)
                // NameSuffix, 12, varchar, 4,
                .string("NameSuffix", null)
                // PhoneVoice1, 12, varchar, 20,
                .string("PhoneVoice1", customer.get(CustomerFieldTypes.PHONE))
                // PhoneVoice2, 12, varchar, 20,
                .string("PhoneVoice2", null)
                // PhoneVoice3, 12, varchar, 20,
                .string("PhoneVoice3", null)
                // EmailAddress, 12, varchar, 64,
                .string("EmailAddress", customer.get(CustomerFieldTypes.EMAIL))
                // Password, 12, varchar, 16,
                .string("Password", customer.get(CustomerFieldTypes.PIN))
                // EntryDate, 11, datetime, 23,
                .dateTimeNow("EntryDate")      // TEST this, this seems fishy. Could also use DateCompares date methods.
                // ExpirationDate, 11, datetime, 2
                .dateTime("ExpirationDate", expiry) // ****TODO Extend to add time element 
                // AddrCheckDate, 11, datetime, 23,
                .dateTime("AddrCheckDate", expiry)
                // UpdateDate, 11, datetime, 23,
                .dateTimeNow("UpdateDate")    // ****
                // User1, 12, varchar, 64,
                .string("User1", "Not in the List") // Set these during customer normalization.
                // User2, 12, varchar, 64,
                .string("User2", null)       // Set these during customer normalization.
                // User3, 12, varchar, 64,
                .string("User3", null)       // Set these during customer normalization.
                // User4, 12, varchar, 64,
                .string("User4", "(none)")   // The none is actually (none) including the brackets. It actually links to a list of options.
                // User5, 12, varchar, 64,
                .string("User5", "(none)")   // Set these during customer normalization.
                // Customer.SEX is always a single character.
                // Gender, 1, char, 1,
                .setChar("Gender", customer.get(CustomerFieldTypes.SEX))
                // Birthdate, 11, datetime, 23,
                .dateTime("Birthdate", dob)    // Add time 
                // RegistrationDate, 11, datetime, 23,
                .dateTimeNow("RegistrationDate")
                // FormerID, 12, varchar, 20,
                .string("FormerID", null)   // Note for update on lost card.
                // ReadingList, -6, tinyint, 3,
                .tinyInt("ReadingList", "0")
                // PhoneFAX, 12, varchar, 20,
                .string("PhoneFAX", null)
                // DeliveryOptionID, 4, int, 10,
                .integer("DeliveryOptionID", "2") // polaris_sql.properties
                // StatisticalClassID, 4, int, 10,
                .integer("StatisticalClassID")     // null for integer id(?)
                // CollectionExempt, -7, bit, 1,
                .bit("CollectionExempt", "0")  // ***** This is a bit field, TODO test.
                // AltEmailAddress, 12, varchar, 64,
                .string("AltEmailAddress", null)
                // ExcludeFromOverdues, -7, bit, 1,
                .bit("ExcludeFromOverdues", "0")  // ***** This is a bit field, TODO test.
                // SDIEmailAddress, 12, varchar, 150,
                .string("SDIEmailAddress", null)
                // SDIEmailFormatID, 4, int, 10,
                .integer("SDIEmailFormatID", null)        // null integer value.
                // 
                .bit("SDIPositiveAssent", null)  // ***** This is a bit field, TODO test.
                // SDIPositiveAssentDate, 11, datetime, 23,
                .dateTime("SDIPositiveAssentDate")
                // DeletionExempt, -7, bit, 1,
                .bit("DeletionExempt", "0")  // ***** This is a bit field, TODO test.
                // PatronFullName, 12, varchar, 100,
                .string("PatronFullName", customer.get(CustomerFieldTypes.LASTNAME) 
                        + " " + customer.get(CustomerFieldTypes.FIRSTNAME))
                // ExcludeFromHolds, -7, bit, 1,
                .bit("ExcludeFromHolds", "0")  // ***** This is a bit field, TODO test.
                // ExcludeFromBills, -7, bit, 1,
                .bit("ExcludeFromBills", "0")  // ***** This is a bit field, TODO test.
                // EmailFormatID, 4, int, 10,
                .integer("EmailFormatID", "2") // polaris_sql.properties
                // PatronFirstLastName, 12, varchar, 100,
                .string("PatronFirstLastName", customer.get(CustomerFieldTypes.FIRSTNAME) 
                        + " " + customer.get(CustomerFieldTypes.LASTNAME))
                // Username, 12, varchar, 50,
                .string("Username", null)
                // MergeDate, 11, datetime, 23,
                .dateTime("MergeDate") // null
                // MergeUserID, 4, int, 10,
                .integer("MergeUserID", null)
                // MergeBarcode, 12, varchar, 20,
                .string("MergeBarcode", null)
                // CellPhone,  NULL
                .string("CellPhone", null)
                // CellPhoneCarrierID,  NULL
                .integer("CellPhoneCarrierID", null)
                // EnableSMS,  0
                .bit("EnableSMS", "0")  // ***** This is a bit field, TODO test.     
                // RequestPickupBranchID,  NULL
                .integer("RequestPickupBranchID", null)
                // Phone1CarrierID, , NULL
                .integer("Phone1CarrierID", null)
                // Phone2CarrierID,  NULL
                .integer("Phone2CarrierID", null)
                // Phone3CarrierID,  NULL
                .integer("Phone3CarrierID", null)
                // eReceiptOptionID, NULL
                .integer("eReceiptOptionID", null)
                // TxtPhoneNumber, -6, tinyint, 3 TxtPhoneNumber,  NULL
                .tinyInt("TxtPhoneNumber")
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
                .string("PostalCodeID")
                .whereString("PostalCode", customer.get(CustomerFieldTypes.POSTALCODE))
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
        System.out.println("PATRON_POSTALCODE_ID:>>" + postalCodeID + "<< (if empty; good.)");
        
        // Returns an empty string if not found.
        if (postalCodeID.isEmpty())
        {
            //        PostalCodes
            //Polaris, Polaris, PostalCodes, PostalCodeID, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
            //Polaris, Polaris, PostalCodes, PostalCode, 12, varchar, 10, 10, null, null, 1, null, null, 12, null, 10, 2, YES, 
            //Polaris, Polaris, PostalCodes, City, 12, varchar, 32, 32, null, null, 0, null, null, 12, null, 32, 3, NO, 
            //Polaris, Polaris, PostalCodes, State, 12, varchar, 32, 32, null, null, 0, null, null, 12, null, 32, 4, NO, 
            //Polaris, Polaris, PostalCodes, CountryID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 5, YES, 
            //Polaris, Polaris, PostalCodes, County, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 6, YES,

            //        INSERT INTO Polaris.Polaris.PostalCode ( PostalCode , City , State , CountryID , County )
            //        VALUES ( (PostalCode) , (City) , (Province) , 2 , NULL )
            SQLInsertCommand insertPostalCode = new SQLInsertCommand.Builder(connector, this.postalCodes)
                    .string("PostalCode", customer.get(CustomerFieldTypes.POSTALCODE))
                    .string("City", customer.get(CustomerFieldTypes.CITY))
                    .string("State", customer.get(CustomerFieldTypes.PROVINCE))
                    .integer("CountryID", "2") // polaris_sql.properties
                    .string("County", null)
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
            System.out.println("PATRON_POSTALCODE_ID:>>" + postalCodeID + "<<");
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
        //Polaris, Polaris, Addresses, AddressID, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        //Polaris, Polaris, Addresses, PostalCodeID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 2, NO, 
        //Polaris, Polaris, Addresses, StreetOne, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 3, YES, 
        //Polaris, Polaris, Addresses, StreetTwo, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 4, YES, 
        //Polaris, Polaris, Addresses, ZipPlusFour, 12, varchar, 4, 4, null, null, 1, null, null, 12, null, 4, 5, YES, 
        //Polaris, Polaris, Addresses, MunicipalityName, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 6, YES,
//        INSERT INTO Polaris.Polaris.Addresses (PostalCodeID , StreetOne , StreetTwo , ZipPlusFour , MunicipalityName )
//        VALUES ( (PostalCodeID) , (StreetOne) , (StreetTwo) , NULL , NULL )
        SQLInsertCommand insertAddress = new SQLInsertCommand.Builder(connector, this.addressTable)
                .integer("PostalCodeID", postalCodeID)
                .string("StreetOne", customer.get(CustomerFieldTypes.STREET))
                .string("StreetTwo", null)  // we don't have more than one street value.
                .string("ZipPlusFour", null)
                .string("MunicipalityName", null)
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
                .integer("PostalCodeID")
                .whereInteger("AddressID", polarisAddressID)
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
            System.out.println("PATRON_POSTALCODE_ID Found a MATCH!!" + postalCodeIDFromAddressTable);
        }
        else
        {
            //        ELSE Run same query and decrement (AddressID) until we get a match, then store new value as (AddressID).
            System.out.println("PATRON_POSTALCODE_ID did match: postalCodeID='" + postalCodeID
                   + "' and postalCodeIDFromAddressTable='" + postalCodeIDFromAddressTable + "'");
        }
        // ================================== revisit if not getting the correct address ==================
        //        --
        
        
        // ... and finally...
        // PatronAddresses
        //Polaris, Polaris, PatronAddresses, PatronID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        //Polaris, Polaris, PatronAddresses, AddressID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 2, YES, 
        //Polaris, Polaris, PatronAddresses, AddressTypeID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 3, NO, 
        //Polaris, Polaris, PatronAddresses, FreeTextLabel, 12, varchar, 30, 30, null, null, 1, null, null, 12, null, 30, 4, YES, 
        //Polaris, Polaris, PatronAddresses, Verified, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 5, NO, 
        //Polaris, Polaris, PatronAddresses, VerificationDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 6, YES, 
        //Polaris, Polaris, PatronAddresses, PolarisUserID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 7, YES,
//        INSERT INTO Polaris.Polaris.PatronAddresses
//        VALUES ( (PatronID) , (AddressID) , 2 , 'Home' , 0 , NULL , NULL )
//        INSERT INTO Polaris.Polaris.PatronAddresses
//        VALUES ( (PatronID) , (AddressID) , 3 , 'Home' , 0 , NULL , NULL )
//        INSERT INTO Polaris.Polaris.PatronAddresses
//        VALUES ( (PatronID) , (AddressID) , 4 , 'Home' , 0 , NULL , NULL )
//        *Yes, must run this three times, one for each number value
        SQLInsertCommand insertPatronIDAddressID = new SQLInsertCommand.Builder(connector, this.patronAddresses)
                .integer("PatronID", polarisPatronID)
                .integer("AddressID", polarisAddressID)
                .integer("AddressTypeID", "2") // polaris_sql.properties
                .string("FreeTextLabel", "Home") // polaris_sql.properties
                .bit("Verified", "0") 
                .dateTime("VerificationDate") // null
                .integer("PolarisUserID") // null
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
                .integer("PatronID", polarisPatronID)
                .integer("AddressID", polarisAddressID)
                .integer("AddressTypeID", "3") 
                .string("FreeTextLabel", "Home")
                .bit("Verified", "0")
                .dateTime("VerificationDate") // null
                .integer("PolarisUserID") // null
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
                .integer("PatronID", polarisPatronID)
                .integer("AddressID", polarisAddressID)
                .integer("AddressTypeID", "4")
                .string("FreeTextLabel", "Home")
                .bit("Verified", "0")
                .dateTime("VerificationDate") // null
                .integer("PolarisUserID") // null
                .build();
        return insertPatronIDAddressID;
    }

    
    
    
    
    
    
    
    
    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        // This is a class variable because the Responder will need to run the last command and return results.
        this.connector = new SQLConnector.Builder(host, driver, database)
                .user(user)
                .password(password)
                .build();

        //        Retrieve PatronID for use in further Queries
        //
        //        SELECT PatronID
        //        FROM Polaris.Polaris.Patrons
        //        WHERE Barcode = (Barcode)
        // Then recover the PatronID associated with this user.
        SQLSelectCommand getPatronIDCommand = new SQLSelectCommand.Builder(connector, this.patronsTable)
                .string("PatronID")
                .whereString("Barcode", customer.get(CustomerFieldTypes.ID))
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
        String polarisPatronID = Text.lastWord(status.getStdout(), 2);
        System.out.println("PATRON_ID:>>" + polarisPatronID + "<<");
        //Polaris, Polaris, PatronRegistration, PatronID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
        //Polaris, Polaris, PatronRegistration, LanguageID, 5, smallint, 5, 2, 0, 10, 1, null, null, 5, null, null, 2, YES, 
        //Polaris, Polaris, PatronRegistration, NameFirst, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 3, YES, 
        //Polaris, Polaris, PatronRegistration, NameLast, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 4, YES, 
        //Polaris, Polaris, PatronRegistration, NameMiddle, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 5, YES, 
        //Polaris, Polaris, PatronRegistration, NameTitle, 12, varchar, 8, 8, null, null, 1, null, null, 12, null, 8, 6, YES, 
        //Polaris, Polaris, PatronRegistration, NameSuffix, 12, varchar, 4, 4, null, null, 1, null, null, 12, null, 4, 7, YES, 
        //Polaris, Polaris, PatronRegistration, PhoneVoice1, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 8, YES, 
        //Polaris, Polaris, PatronRegistration, PhoneVoice2, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 9, YES, 
        //Polaris, Polaris, PatronRegistration, PhoneVoice3, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 10, YES, 
        //Polaris, Polaris, PatronRegistration, EmailAddress, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 11, YES, 
        //Polaris, Polaris, PatronRegistration, Password, 12, varchar, 16, 16, null, null, 1, null, null, 12, null, 16, 12, YES, 
        //Polaris, Polaris, PatronRegistration, EntryDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 13, YES, 
        //Polaris, Polaris, PatronRegistration, ExpirationDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 14, YES, 
        //Polaris, Polaris, PatronRegistration, AddrCheckDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 15, YES, 
        //Polaris, Polaris, PatronRegistration, UpdateDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 16, YES, 
        //Polaris, Polaris, PatronRegistration, User1, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 17, YES, 
        //Polaris, Polaris, PatronRegistration, User2, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 18, YES, 
        //Polaris, Polaris, PatronRegistration, User3, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 19, YES, 
        //Polaris, Polaris, PatronRegistration, User4, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 20, YES, 
        //Polaris, Polaris, PatronRegistration, User5, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 21, YES, 
        //Polaris, Polaris, PatronRegistration, Gender, 1, char, 1, 1, null, null, 1, null, null, 1, null, 1, 22, YES, 
        //Polaris, Polaris, PatronRegistration, Birthdate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 23, YES, 
        //Polaris, Polaris, PatronRegistration, RegistrationDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 24, YES, 
        //Polaris, Polaris, PatronRegistration, FormerID, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 25, YES, 
        //Polaris, Polaris, PatronRegistration, ReadingList, -6, tinyint, 3, 1, 0, 10, 0, null, null, -6, null, null, 26, NO, 
        //Polaris, Polaris, PatronRegistration, PhoneFAX, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 27, YES, 
        //Polaris, Polaris, PatronRegistration, DeliveryOptionID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 28, YES, 
        //Polaris, Polaris, PatronRegistration, StatisticalClassID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 29, YES, 
        //Polaris, Polaris, PatronRegistration, CollectionExempt, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 30, NO, 
        //Polaris, Polaris, PatronRegistration, AltEmailAddress, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 31, YES, 
        //Polaris, Polaris, PatronRegistration, ExcludeFromOverdues, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 32, NO, 
        //Polaris, Polaris, PatronRegistration, SDIEmailAddress, 12, varchar, 150, 150, null, null, 1, null, null, 12, null, 150, 33, YES, 
        //Polaris, Polaris, PatronRegistration, SDIEmailFormatID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 34, YES, 
        //Polaris, Polaris, PatronRegistration, SDIPositiveAssent, -7, bit, 1, 1, null, null, 1, null, null, -7, null, null, 35, YES, 
        //Polaris, Polaris, PatronRegistration, SDIPositiveAssentDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 36, YES, 
        //Polaris, Polaris, PatronRegistration, DeletionExempt, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 37, NO, 
        //Polaris, Polaris, PatronRegistration, PatronFullName, 12, varchar, 100, 100, null, null, 1, null, null, 12, null, 100, 38, YES, 
        //Polaris, Polaris, PatronRegistration, ExcludeFromHolds, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 39, NO, 
        //Polaris, Polaris, PatronRegistration, ExcludeFromBills, -7, bit, 1, 1, null, null, 0, null, null, -7, null, null, 40, NO, 
        //Polaris, Polaris, PatronRegistration, EmailFormatID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 41, NO, 
        //Polaris, Polaris, PatronRegistration, PatronFirstLastName, 12, varchar, 100, 100, null, null, 1, null, null, 12, null, 100, 42, YES, 
        //Polaris, Polaris, PatronRegistration, Username, 12, varchar, 50, 50, null, null, 1, null, null, 12, null, 50, 43, YES, 
        //Polaris, Polaris, PatronRegistration, MergeDate, 11, datetime, 23, 16, 3, null, 1, null, null, 9, 3, null, 44, YES, 
        //Polaris, Polaris, PatronRegistration, MergeUserID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 45, YES, 
        //Polaris, Polaris, PatronRegistration, MergeBarcode, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 46, YES, 
        //Polaris, Polaris, PatronRegistration, CellPhone, 12, varchar, 20, 20, null, null, 1, null, null, 12, null, 20, 47, YES, 
        //Polaris, Polaris, PatronRegistration, CellPhoneCarrierID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 48, YES, 
        //Polaris, Polaris, PatronRegistration, EnableSMS, -7, bit, 1, 1, null, null, 1, null, null, -7, null, null, 49, YES, 
        //Polaris, Polaris, PatronRegistration, RequestPickupBranchID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 50, YES, 
        //Polaris, Polaris, PatronRegistration, Phone1CarrierID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 51, YES, 
        //Polaris, Polaris, PatronRegistration, Phone2CarrierID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 52, YES, 
        //Polaris, Polaris, PatronRegistration, Phone3CarrierID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 53, YES, 
        //Polaris, Polaris, PatronRegistration, eReceiptOptionID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 54, YES, 
        //Polaris, Polaris, PatronRegistration, TxtPhoneNumber, -6, tinyint, 3, 1, 0, 10, 1, null, null, -6, null, null, 55, YES,
        /*
        UPDATE PATRON REGISTRATION INFORMATION

        UPDATE Polaris.Polaris.PatronRegistration
        SET NameLast = (NameLast) , NameFirst = (NameFirst) , NameMiddle = (NameMiddle) ,
                PhoneVoice1 = (Phone) , EmailAddress = (Email) , Password = (Password) ,
                UpdateDate = GETDATE() , ExpirationDate = (Expiry) , AddrCheckDate = (Expiry) ,
                Gender = (Gender) , PatronFullName = [concatenate name in format Smith, John] ,
                PatronFirstLastname = [concatenate name in format John Smith]
        WHERE PatronID = (PatronID)
        */
        // Get the date today in system format
        String today; // Get ready with dob and expiry in acceptable format.
        String expiry;
        try
        {
            today = DateComparer.ANSIToConfigDate(DateComparer.ANSIToday());
            expiry = DateComparer.ANSIToConfigDate(customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES));
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
        System.out.println("***   today>"+today);
        System.out.println("***  expiry>"+expiry);
        
        SQLUpdateCommand updateCommand = new SQLUpdateCommand.Builder(connector, patronRegistration)
                .string("NameLast", customer.get(CustomerFieldTypes.LASTNAME))
                .string("NameFirst", customer.get(CustomerFieldTypes.FIRSTNAME))
                .string("NameMiddle", "unspecified")
                .string("PhoneVoice1", customer.get(CustomerFieldTypes.PHONE))
                .string("EmailAddress", customer.get(CustomerFieldTypes.EMAIL))
                .string("Password", customer.get(CustomerFieldTypes.PIN))
                .string("UpdateDate", today)
                .string("ExpirationDate", expiry)
                .string("AddrCheckDate", expiry)
                .string("Gender", customer.get(CustomerFieldTypes.SEX))
                .string("PatronFullName", 
                        customer.get(CustomerFieldTypes.LASTNAME)
                        + ", " 
                        + customer.get(CustomerFieldTypes.FIRSTNAME))
                .string("PatronFirstLastname", 
                        customer.get(CustomerFieldTypes.FIRSTNAME)
                        + " " 
                        + customer.get(CustomerFieldTypes.LASTNAME))
                .whereString("PatronID", polarisPatronID)
                .build();
        status = updateCommand.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to update customer account in table: "
                    + this.patronRegistration);
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                    .build();
        } 
        /*
        UPDATE POSTAL CODE

        SELECT PostalCodeID
        FROM Polaris.Polaris.PostalCodes
        WHERE PostalCode = (PostalCode)
        */
        SQLSelectCommand selectPostalCodeID = new SQLSelectCommand.Builder(connector, this.postalCodes)
                .string("PostalCodeID")
                .whereString("PostalCode", customer.get(CustomerFieldTypes.POSTALCODE))
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
        System.out.println("PATRON_POSTALCODE_ID:>>" + postalCodeID + "<< (which can be ok)");

        if (postalCodeID.isEmpty())
        {
            /*
            IF Value IS NULL

            INSERT INTO Polaris.Polaris.PostalCode ( PostalCode , City , State , CountryID , County )
            VALUES ( (PostalCode) , (City) , (Province) , 2 , NULL )
            */
            SQLInsertCommand insertPostalCode = new SQLInsertCommand.Builder(connector, this.postalCodes)
                    .string("PostalCode", customer.get(CustomerFieldTypes.POSTALCODE))
                    .string("City", customer.get(CustomerFieldTypes.CITY))
                    .string("State", customer.get(CustomerFieldTypes.PROVINCE))
                    .integer("CountryID", "2")
                    .string("County", null)
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
        }
        
        /*
        - RETRIVE PostalCodeID from the table as it was just inserted or from existing.
        SELECT PostalCodeID
        FROM Polaris.Polaris.PostalCodes
        WHERE PostalCode = (PostalCode)

        STORE AS VARIABLE (PostalCodeID) 
        */
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
        System.out.println("PATRON_POSTALCODE_ID:>>" + postalCodeID + "<<");
        /*
        ADD ADDRESS

        - Check if address already exists

        SELECT AddressID
        FROM Polaris.Polaris.Addresses
        WHERE PostalCodeID = (PostalCodeID)
        AND StreetOne = (StreetOne)
        AND StreetTWo = (StreetTwo)

        STORE AS (AddressID)
        */
        SQLSelectCommand selectAddressID = new SQLSelectCommand.Builder(connector, this.addressTable)
                .integer("AddressID")
                .whereInteger("PostalCodeID", postalCodeID 
                        + " AND StreetOne = "
                        + customer.get(CustomerFieldTypes.STREET) )
                .build();
        status = selectAddressID.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to find address ID for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.addressTable);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                    .build();
        }
        String polarisAddressID = Text.lastWord(status.getStdout(), 2);
        System.out.println("ADDRESS_ID:>>" + polarisAddressID + "<<");
        
        if (polarisAddressID.isEmpty())
        {
            /*

            IF VALUE IS NULL {

            INSERT INTO Polaris.Polaris.Addresses (PostalCodeID , StreetOne , StreetTwo , ZipPlusFour , MunicipalityName )
            VALUES ( (PostalCodeID) , (StreetOne) , (StreetTwo) , NULL , NULL )
            */
            //        ADD ADDRESS
            //Polaris, Polaris, Addresses, AddressID, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
            //Polaris, Polaris, Addresses, PostalCodeID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 2, NO, 
            //Polaris, Polaris, Addresses, StreetOne, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 3, YES, 
            //Polaris, Polaris, Addresses, StreetTwo, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 4, YES, 
            //Polaris, Polaris, Addresses, ZipPlusFour, 12, varchar, 4, 4, null, null, 1, null, null, 12, null, 4, 5, YES, 
            //Polaris, Polaris, Addresses, MunicipalityName, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 6, YES,
            //        INSERT INTO Polaris.Polaris.Addresses (PostalCodeID , StreetOne , StreetTwo , ZipPlusFour , MunicipalityName )
            //        VALUES ( (PostalCodeID) , (StreetOne) , (StreetTwo) , NULL , NULL )
            SQLInsertCommand insertAddress = new SQLInsertCommand.Builder(connector, this.addressTable)
                    .integer("PostalCodeID", postalCodeID)
                    .string("StreetOne", customer.get(CustomerFieldTypes.STREET))
                    .string("StreetTwo", null)  // we don't have more than one street value.
                    .string("ZipPlusFour", null)
                    .string("MunicipalityName", null)
                    .build();
            status = insertAddress.execute();
            if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
            {
                System.out.println("**error failed during address creation for customer " 
                        + customer.get(CustomerFieldTypes.ID) + " in table: "
                        + this.addressTable);
                // When this command gets run it returns a useful message and error status for customer.
                return new DummyCommand.Builder()
                        .setStatus(1)
                        .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
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
                        .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                        .build();
            }
            polarisAddressID = Text.lastWord(status.getStdout(), 2);
            System.out.println("LAST_ADDRESS_ID:>>" + polarisAddressID + "<<");
        }
        
        /*
        ADD ADDRESS TO PATRON

        INSERT INTO Polaris.Polaris.PatronAddresses
        VALUES ( (PatronID) , (AddressID) , 2 , 'Home' , 0 , NULL , NULL )
        INSERT INTO Polaris.Polaris.PatronAddresses
        VALUES ( (PatronID) , (AddressID) , 3 , 'Home' , 0 , NULL , NULL )
        INSERT INTO Polaris.Polaris.PatronAddresses
        VALUES ( (PatronID) , (AddressID) , 4 , 'Home' , 0 , NULL , NULL )
        *Yes, must run this three times, one for each number value
        }
        */
        /////////////////// Really do we do this for updates too? /////////////////////
        // Number 1 pass...
        SQLInsertCommand insertPatronIDAddressID = new SQLInsertCommand.Builder(connector, this.patronAddresses)
                .integer("PatronID", polarisPatronID)
                .integer("AddressID", polarisAddressID)
                .integer("AddressTypeID", "2")
                .string("FreeTextLabel", "Home")
                .bit("Verified", "0")
                .dateTime("VerificationDate") // null
                .integer("PolarisUserID") // null
                .build();
        status = insertPatronIDAddressID.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed during insertPatronIDAddressID 1 update for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronAddresses);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                    .build();
        }
        // iteration number 2 for AddressTypeID 3
        insertPatronIDAddressID = new SQLInsertCommand.Builder(connector, this.patronAddresses)
                .integer("PatronID", polarisPatronID)
                .integer("AddressID", polarisAddressID)
                .integer("AddressTypeID", "3")
                .string("FreeTextLabel", "Home")
                .bit("Verified", "0")
                .dateTime("VerificationDate") // null
                .integer("PolarisUserID") // null
                .build();
        status = insertPatronIDAddressID.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed during insertPatronIDAddressID 2 update for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronAddresses);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                    .build();
        }
        // iteration number 3 for AddressTypeID 4
        insertPatronIDAddressID = new SQLInsertCommand.Builder(connector, this.patronAddresses)
                .integer("PatronID", polarisPatronID)
                .integer("AddressID", polarisAddressID)
                .integer("AddressTypeID", "4")
                .string("FreeTextLabel", "Home")
                .bit("Verified", "0")
                .dateTime("VerificationDate") // null
                .integer("PolarisUserID") // null
                .build();
        status = insertPatronIDAddressID.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed during insertPatronIDAddressID 3 update for customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronAddresses);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()))
                    .build();
        }
        /*
        IF THE VALUE EXISTED

        UPDATE PATRON ADDRESS
        UPDATE Polaris.Polaris.PatronAddresses
        SET AddressID = (AddressID) 
        WHERE PatronID = (PatronID)
        */
        SQLUpdateCommand updatePatronAddress = new SQLUpdateCommand.Builder(connector, addressTable)
                .string("AddressID", polarisAddressID)
                .whereInteger("PatronID", polarisPatronID)
                .build();
        return updatePatronAddress;
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
        boolean result = false;
        switch(responseType)
        {
            case SUCCESS:
                result = true;
            default:
                result = false;
                break;
        }
        return result;
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
        // TODO implement the remainder of this class.
//        return new SQLCustomerMessage(stdout, true);
        // Not necessary since for now Polaris uses SIP2 so 
        return new SIPCustomerMessage(stdout);
    }
}
