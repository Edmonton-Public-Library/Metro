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
import api.SQLDescribeCommand;
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
import mecard.config.SQLPropertyTypes;
import mecard.config.MessagesTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.customer.polaris.PolarisSQLCustomerFormatter;
import mecard.exception.ConfigurationException;
import mecard.util.DateComparer;
import mecard.util.PostalCode;
import site.CustomerLoadNormalizer;

/**
 * Manages messaging and work flow for requests to a Polaris ILS via SQL statements.
 * @author Andrew Nisbet <anisbet@epl.ca>
 * @since 0.8.14_00
 */
public class PolarisSQLRequestBuilder extends ILSRequestBuilder
{
    private final String patronsTable       = "Polaris.Patrons"; // Use this for insert and update.
    private final String patronRegistration = "Polaris.PatronRegistration";
    private final String postalCodes        = "Polaris.PostalCodes";
    private final String addressTable       = "Polaris.Addresses";
    
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
        
        Properties properties = PropertyReader.getProperties(ConfigFileTypes.SQL);
        host = properties.getProperty(SQLPropertyTypes.HOST.toString());
        driver = properties.getProperty(SQLPropertyTypes.CONNECTOR_TYPE.toString());
        database = properties.getProperty(SQLPropertyTypes.DATABASE.toString());
        user = properties.getProperty(SQLPropertyTypes.USERNAME.toString());
        password = properties.getProperty(SQLPropertyTypes.PASSWORD.toString());
    }
    
    /**
     * Constructor for testing purposes.
     * @param host
     * @param driver MY_SQL or SQL_SERVER.
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
//        System.out.println("USERID:" + barcode);
//        SQLSelectCommand selectUser = new SQLSelectCommand.Builder(connector, this.patronsTable)
//            .integer("PatronID")
//            .whereInteger("Barcode", barcode)
//            .build();
//        System.out.println("COMMAND==>"+selectUser.toString()+"<==");
        
        SQLDescribeCommand describe = new SQLDescribeCommand.Builder(connector, "Patrons")// use this version for query DESCRIBE table
            .build();
        System.out.println("COMMAND==>"+describe.toString()+"<==");
        return describe;
    }

    @Override
    public Command getCreateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        // This is a class variable because the Responder will need to run the last command and return results.
        this.connector = new SQLConnector.Builder(host, driver, database)
                .user(user)
                .password(password)
                .build();
        /* First we need the PatronID
        SELECT PatronID
        FROM Polaris.Polaris.Patrons
        WHERE Barcode = (Barcode)
        */
        SQLSelectCommand selectBarcodeCommand = new SQLSelectCommand.Builder(connector, this.patronsTable)
            .string("PatronID")
            .whereInteger("Barcode", customer.get(CustomerFieldTypes.ID))
            .build();
        CommandStatus status = selectBarcodeCommand.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to select customer " 
                    + customer.get(CustomerFieldTypes.ID) + " in table: "
                    + this.patronsTable);
            // When this command gets run it returns a useful message and error status for customer.
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()))
                    .build();
        }
        // STORE AS VARIABLE (PatronID) (This is an Integer)
        String patronBarcode = status.getStdout();
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
        String today = null;
        try
        {
            DateComparer.ANSIToConfigDate(DateComparer.ANSIToday());
        }
        catch (ParseException e)
        {
            System.out.println(SQLUpdateCommand.class.getName() +
                    "Date value failed to convert to requested format");
        } 
        SQLUpdateCommand updateCommand = new SQLUpdateCommand.Builder(connector, patronRegistration)
                .string("NameLast", customer.get(CustomerFieldTypes.LASTNAME))
                .string("NameFirst", customer.get(CustomerFieldTypes.FIRSTNAME))
                .string("NameMiddle", "unspecified")
                .string("PhoneVoice1", customer.get(CustomerFieldTypes.PHONE))
                .string("EmailAddress", customer.get(CustomerFieldTypes.EMAIL))
                .string("Password", customer.get(CustomerFieldTypes.PIN))
                .string("UpdateDate", today)
                .string("ExpirationDate", customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES))
                .string("AddrCheckDate", customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES))
                .string("Gender", customer.get(CustomerFieldTypes.SEX))
                .string("PatronFullName", 
                        customer.get(CustomerFieldTypes.LASTNAME)
                        + ", " 
                        + customer.get(CustomerFieldTypes.FIRSTNAME))
                .string("PatronFirstLastname", 
                        customer.get(CustomerFieldTypes.FIRSTNAME)
                        + " " 
                        + customer.get(CustomerFieldTypes.LASTNAME))
                .whereString("PatronID", patronBarcode)
                .build();
        status = updateCommand.execute();
        if (status.getStatus() != ResponseTypes.COMMAND_COMPLETED)
        {
            System.out.println("**error failed to update customer account in table: "
                    + this.patronRegistration);
            return new DummyCommand.Builder()
                    .setStatus(1)
                    .setStderr(messages.getProperty(MessagesTypes.ACCOUNT_NOT_FOUND.toString()))
                    .build();
        } 
        /*
        UPDATE POSTAL CODE

        SELECT PostalCodeID
        FROM Polaris.Polaris.PostalCodes
        WHERE PostalCode = (PostalCode)
        */
        SQLSelectCommand selectPostalcodeCommand = new SQLSelectCommand.Builder(connector, this.postalCodes)
            .string("PostalCodeID")
            .whereString("PostalCode=", customer.get(CustomerFieldTypes.POSTALCODE))
            .build();
        status = selectPostalcodeCommand.execute();
        if (status.getStdout().compareTo("NULL") == 0)
        {
            /*
            IF Value IS NULL

            INSERT INTO Polaris.Polaris.PostalCode ( PostalCode , City , State , CountryID , County )
            VALUES ( (PostalCode) , (City) , (Province) , 2 , NULL )
            */
            SQLInsertCommand postalcodeInsert = new SQLInsertCommand.Builder(connector, this.postalCodes)
                .string("PostalCode", customer.get(CustomerFieldTypes.POSTALCODE))
                .string("City", customer.get(CustomerFieldTypes.CITY))
                .string("State", customer.get(CustomerFieldTypes.PROVINCE))
                .integer("CountryID", "2")
//                    .string("County", "NULL") // This may have to be commented out since this will probably put the literal value NULL in the country field.
                .build();
            postalcodeInsert.execute();
        }
        
        /*
        - RETRIVE PostalCodeID from the table as it was just inserted or from existing.
        SELECT PostalCodeID
        FROM Polaris.Polaris.PostalCodes
        WHERE PostalCode = (PostalCode)

        STORE AS VARIABLE (PostalCodeID) 
        */
        // The raw postal code from customer is 'A0A0A0', which won't match a correctly
        // stored postal code of 'A0A 0A0'.
        String pCode = PostalCode.formatPostalCode(customer.get(CustomerFieldTypes.POSTALCODE));
        selectPostalcodeCommand = new SQLSelectCommand.Builder(connector, this.postalCodes)
                .integer("PostalCodeID")
                .whereString("PostalCode", pCode)
                .build();
        status = selectPostalcodeCommand.execute();
        String postalCodeId = status.getStdout();
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
        SQLSelectCommand selectAddressIDCommand = new SQLSelectCommand.Builder(connector, this.addressTable)
                .integer("AddressID")
                .whereInteger("PostalCodeID", postalCodeId 
                        + " AND StreetOne = "
                        + customer.get(CustomerFieldTypes.STREET)) // what is this? Why check street one.
                .build();
        status = selectAddressIDCommand.execute();
        if (status.getStdout().compareTo("NULL") == 0)
        {
            /*

            IF VALUE IS NULL {

            INSERT INTO Polaris.Polaris.Addresses (PostalCodeID , StreetOne , StreetTwo , ZipPlusFour , MunicipalityName )
            VALUES ( (PostalCodeID) , (StreetOne) , (StreetTwo) , NULL , NULL )
            */
            SQLInsertCommand insertAddress = new SQLInsertCommand.Builder(connector, this.addressTable)
                    .integer("PostalCodeID", postalCodeId)
                    .string("StreetOne", customer.get(CustomerFieldTypes.STREET))
                    .string("StreetTwo", "") 
                    .string("ZipPlusFour", null) 
                    .string("MunicipalityName", null)
                    .build();
            insertAddress.execute();
        }
        
        
        /*
        SELECT IDENT_CURRENT('Polaris.Addresses') STORE AS VARIABLE (AddressID)
        * NOTE this has to be done immediately - it returns the last inserted value, 
        it's possible for another value to be inserted quite quickly.  
        Can error check this if necessary (Example below)
        --
        SELECT PostalCodeID
        FROM Polaris.Polaris.Addresses
        WHERE AddressID = (AddressID)

        IF Query == (PostalCode) OK
        ELSE Run same query and decrement (AddressID) until we get a match, then store new value as (AddressID).
        --
        */
        // TODO get clarification on the above value selection. Is it the max id in the table?
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
        // Number 1 pass...
//        SQLInsertCommand insertAddressFields = new SQLInsertCommand.Builder(connector, this.addressTable)
//            .integer("PatronID", patronBarcode)
//            .integer("AddressID", addressId)
//            .integer("AddressTypeID ", "2") 
//            .string("FreeTextLabel", "Home") 
//            .integer("Verified ", "0") 
//            .string("VerificationDate", null)
//            .string("PolarisUserID", null)
//            .build();
//        insertAddressFields.execute();
        // Number 2...
//        insertAddressFields = new SQLInsertCommand.Builder(connector, this.addressTable)
//            .integer("PatronID", patronBarcode)
//            .integer("AddressID", addressId)
//            .integer("AddressTypeID ", "3") 
//            .string("FreeTextLabel", "Home") 
//            .integer("Verified ", "0") 
//            .string("VerificationDate", null)
//            .string("PolarisUserID", null)
//            .build();
//        insertAddressFields.execute();
        // Number 3 pass...
//        insertAddressFields = new SQLInsertCommand.Builder(connector, this.addressTable)
//            .integer("PatronID", patronBarcode)
//            .integer("AddressID", addressId)
//            .integer("AddressTypeID ", "4") 
//            .string("FreeTextLabel", "Home") 
//            .integer("Verified ", "0") 
//            .string("VerificationDate", null)
//            .string("PolarisUserID", null)
//            .build();
//        insertAddressFields.execute();
        /*
        IF THE VALUE EXISTED

        UPDATE PATRON ADDRESS
        UPDATE Polaris.Polaris.PatronAddresses
        SET AddressID = (AddressID) 
        WHERE PatronID = (PatronID)
        */
//        SQLUpdateCommand updatePatronAddress = new SQLUpdateCommand.Builder(connector, addressTable)
//                .string("AddressID", addressId)
//                .whereInteger("PatronID", patronId)
//                .build();
//        return updatePatronAddress;
        return null;
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
        // TODO let's parse the returning SQL message for results.
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
        return new SQLCustomerMessage(stdout, true);
    }
}
