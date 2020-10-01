
package mecard.requestbuilder;

import api.Command;
import api.CommandStatus;
import api.SQLConnector;
import api.SQLDescribeCommand;
import api.SQLSelectCommand;
import java.util.Properties;
import json.RequestDeserializer;
import mecard.Request;
import mecard.Response;
import mecard.config.ConfigFileTypes;
import mecard.config.PolarisSQLPropertyTypes;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import site.CustomerLoadNormalizer;

/**
 *
 * @author anisbet
 */
public class PolarisSQLRequestBuilderTest
{
    private final PolarisSQLRequestBuilder requestBuilder;
    private final SQLConnector connector;
    private final Customer customer;
    public enum WhichTest{
        CONNECT,
        GET_CUSTOMER,
        CREATE_CUSTOMER,
        UPDATE_CUSTOMER;
    }
    private final WhichTest test;
    public PolarisSQLRequestBuilderTest()
    {
        Properties p = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        String host = p.getProperty(PolarisSQLPropertyTypes.HOST.toString());
        String driver = p.getProperty(PolarisSQLPropertyTypes.CONNECTOR_TYPE.toString());
        String database = p.getProperty(PolarisSQLPropertyTypes.DATABASE.toString());
        String databaseId = p.getProperty(PolarisSQLPropertyTypes.USERNAME.toString());
        String databasePassword = p.getProperty(PolarisSQLPropertyTypes.PASSWORD.toString());
        connector = new SQLConnector.Builder(host, driver, database)
                .user(databaseId)
                .password(databasePassword)
                .build();

        this.requestBuilder = new PolarisSQLRequestBuilder(true);
        
        String custReq =
//                "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64056\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64056\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12222 144 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0G4\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
                "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346000\",\"pin\":\"64056\",\"customer\":\"{\\\"ID\\\":\\\"21221012346000\\\",\\\"PIN\\\":\\\"64056\\\",\\\"PREFEREDNAME\\\":\\\"William, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0H2\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804961212\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20150602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"William\\\"}\"}";
//                "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345999\",\"pin\":\"64056\",\"customer\":\"{\\\"ID\\\":\\\"21221012345999\\\",\\\"PIN\\\":\\\"64056\\\",\\\"PREFEREDNAME\\\":\\\"Awsome, Richard\\\",\\\"STREET\\\":\\\"11111 111 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0H1\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19690822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20150602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Richard\\\",\\\"LASTNAME\\\":\\\"Awesome\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        customer = request.getCustomer();
        test = WhichTest.CREATE_CUSTOMER; //CREATE_CUSTOMER GET_CUSTOMER
    }
    
    @Test
    public void testConnection()
    {
        if (test == WhichTest.CONNECT)
        {
            System.out.println("\n== Connection Test ==");
            SQLDescribeCommand describe = new SQLDescribeCommand.Builder(connector, "PatronRegistration")// use this version for query DESCRIBE table
                .build();
            System.out.println("COMMAND==>"+describe.toString()+"<==");
            CommandStatus status = describe.execute();
            System.out.println("STATUS==>" + status.getStdout() + "<==");
            System.out.println("== Connection Test ==\n");
        }
    }

    /**
     * Test of getCustomerCommand method, of class PolarisSQLRequestBuilder.
     */
    @Test
    public void testGetCustomerCommand()
    {
        if (test == WhichTest.GET_CUSTOMER)
        {
            System.out.println("==getCustomerCommand==");
    //        String userId = "29335002291067"; //"29335002291067";
            String userId = "21221012345999"; //"29335002291067";
            String userPin = "64058";

            String patronId = "399591";
            String postalCodeId = "450217";
            String addressId = "492734";

            Response response = new Response();
            Command command = requestBuilder.getCustomerCommand(userId, userPin, response);
            CommandStatus status = command.execute();
            System.out.println("STATUS_out:'"+status.getStdout()+"'");
            System.out.println("STATUS_err:'"+status.getStderr()+"'");
            System.out.println("RESPONSE:'"+response.getMessage()+"'");

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
            SQLSelectCommand selectPatrons = new SQLSelectCommand.Builder(connector, "Polaris.Patrons")
                .integer("PatronID")
                .whereString("PatronID", patronId)
                .integer("PatronCodeID")    // polaris_sql.properties
                .integer("OrganizationID")// polaris_sql.properties
                .integer("CreatorID")    // polaris_sql.properties
                .integer("ModifierID")
                .string("Barcode")
                .integer("SystemBlocks")
                .integer("YTDCircCount")
                .integer("LifetimeCircCount")
                .date("LastActivityDate")
                .integer("ClaimCount")
                .integer("LostItemCount")
                .string("ChargesAmount") // These are money values but string() can read them.
                .string("CreditsAmount") // These are money values but string() can read them.
                .build();

            // Create this entry in the Patrons table.
            status = selectPatrons.execute();
            System.out.println("**RESULTS Patrons:'" + status.getStatus() +  "'");
            System.out.println("STATUS_out:'"+status.getStdout()+"'");
            System.out.println("STATUS_err:'"+status.getStderr()+"'");
            System.out.println("RESPONSE:'"+response.getMessage()+"'\n====================================");

            // Check that the values required are entered into the PatronRegistration table
            SQLSelectCommand selectPatronRegistration = new SQLSelectCommand.Builder(connector, "Polaris.PatronRegistration")
                    // PatronID, 4, int, 10, PatronID
                    .whereString("PatronID", patronId)
                    // LanguageID, 5, smallint, 5, '1'
                    .integer("LanguageID")       // polaris_sql.properties
                    // NameFirst, 12, varchar, 32,
                    .string("NameFirst")
                    // NameLast, 12, varchar, 32,
                    .string("NameLast")
                    // NameMiddle, 12, varchar, 32,
                    .string("NameMiddle")
                    // NameTitle, 12, varchar, 8,
                    .string("NameTitle")
                    // NameSuffix, 12, varchar, 4,
                    .string("NameSuffix")
                    // PhoneVoice1, 12, varchar, 20,
                    .string("PhoneVoice1")
                    // PhoneVoice2, 12, varchar, 20,
                    .string("PhoneVoice2")
                    // PhoneVoice3, 12, varchar, 20,
                    .string("PhoneVoice3")
                    // EmailAddress, 12, varchar, 64,
                    .string("EmailAddress")
                    // Password, 12, varchar, 16,
//                    .string("Password") // DEPRECATED.
                    // EntryDate, 11, datetime, 23,
                    .date("EntryDate")      // TEST this, this seems fishy. Could also use DateCompares date methods.
                    // ExpirationDate, 11, datetime, 2
                    .date("ExpirationDate") // ****TODO Extend to add time element 
                    // AddrCheckDate, 11, datetime, 23,
                    .date("AddrCheckDate")
                    // UpdateDate, 11, datetime, 23,
                    .date("UpdateDate")    // ****
                    // User1, 12, varchar, 64,
                    .string("User1") // polaris_sql.properties
                    // User2, 12, varchar, 64,
                    .string("User2")
                    // User3, 12, varchar, 64,
                    .string("User3")
                    // User4, 12, varchar, 64,
                    .string("User4") // The none is actually (none) including the brackets. It actually links to a list of options.
                    // User5, 12, varchar, 64,
                    .string("User5")
                    // Customer.SEX is always a single character.
                    // Gender, 1, char, 1,
                    .string("Gender")
                    // Birthdate, 11, datetime, 23,
                    .date("Birthdate")    // Add time 
                    // RegistrationDate, 11, datetime, 23,
                    .date("RegistrationDate")
                    // FormerID, 12, varchar, 20,
                    .string("FormerID")   // Note for update on lost card.
                    // ReadingList, -6, tinyint, 3,
                    .integer("ReadingList")
                    // PhoneFAX, 12, varchar, 20,
                    .string("PhoneFAX")
                    // DeliveryOptionID, 4, int, 10,
                    .integer("DeliveryOptionID")
                    // StatisticalClassID, 4, int, 10,
                    .integer("StatisticalClassID")     // null for integer id(?)
                    // CollectionExempt, -7, bit, 1,
                    .integer("CollectionExempt")
                    // AltEmailAddress, 12, varchar, 64,
                    .string("AltEmailAddress")
                    // ExcludeFromOverdues, -7, bit, 1,
                    .integer("ExcludeFromOverdues")
                    // SDIEmailAddress, 12, varchar, 150,
                    .string("SDIEmailAddress")
                    // SDIEmailFormatID, 4, int, 10,
                    .integer("SDIEmailFormatID")        // null integer value.
                    // 
                    .integer("SDIPositiveAssent")
                    // SDIPositiveAssentDate, 11, datetime, 23,
                    .date("SDIPositiveAssentDate")
                    // DeletionExempt, -7, bit, 1,
                    .integer("DeletionExempt")
                    // PatronFullName, 12, varchar, 100,
                    .string("PatronFullName")
                    // ExcludeFromHolds, -7, bit, 1,
                    .integer("ExcludeFromHolds")
                    // ExcludeFromBills, -7, bit, 1,
                    .integer("ExcludeFromBills")
                    // EmailFormatID, 4, int, 10,
                    .integer("EmailFormatID")
                    // PatronFirstLastName, 12, varchar, 100,
                    .string("PatronFirstLastName")
                    // Username, 12, varchar, 50,
                    .string("Username")
                    // MergeDate, 11, datetime, 23,
                    .date("MergeDate") // null
                    // MergeUserID, 4, int, 10,
                    .integer("MergeUserID")
                    // MergeBarcode, 12, varchar, 20,
                    .string("MergeBarcode")
                    // CellPhone,  NULL
//                    .string("CellPhone") // DEPRECATED.
                    // CellPhoneCarrierID,  NULL
//                    .string("CellPhoneCarrierID")  // DEPRECATED.
                    // EnableSMS,  0
                    .string("EnableSMS")    
                    // RequestPickupBranchID,  NULL
                    .string("RequestPickupBranchID")
                    // Phone1CarrierID, , NULL
                    .string("Phone1CarrierID")
                    // Phone2CarrierID,  NULL
                    .string("Phone2CarrierID")
                    // Phone3CarrierID,  NULL
                    .string("Phone3CarrierID")
                    // eReceiptOptionID, NULL
                    .string("eReceiptOptionID")
                    // TxtPhoneNumber, -6, tinyint, 3 TxtPhoneNumber,  NULL
                    .string("TxtPhoneNumber")
                    .string("DoNotShowEReceiptPrompt")
                    .build();

            status = selectPatronRegistration.execute();
            System.out.println("**RESULTS: PatronRegistration'" + status.getStatus() + "'");
            System.out.println("STATUS_out:'"+status.getStdout()+"'");
            System.out.println("STATUS_err:'"+status.getStderr()+"'");
            System.out.println("RESPONSE:'"+response.getMessage()+"'\n====================================");

            //        PostalCodes
            //Polaris, Polaris, PostalCodes, PostalCodeID, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
            //Polaris, Polaris, PostalCodes, PostalCode, 12, varchar, 10, 10, null, null, 1, null, null, 12, null, 10, 2, YES, 
            //Polaris, Polaris, PostalCodes, City, 12, varchar, 32, 32, null, null, 0, null, null, 12, null, 32, 3, NO, 
            //Polaris, Polaris, PostalCodes, State, 12, varchar, 32, 32, null, null, 0, null, null, 12, null, 32, 4, NO, 
            //Polaris, Polaris, PostalCodes, CountryID, 4, int, 10, 4, 0, 10, 1, null, null, 4, null, null, 5, YES, 
            //Polaris, Polaris, PostalCodes, County, 12, varchar, 32, 32, null, null, 1, null, null, 12, null, 32, 6, YES,

            //        INSERT INTO Polaris.Polaris.PostalCode ( PostalCode , City , State , CountryID , County )
            //        VALUES ( (PostalCode) , (City) , (Province) , 2 , NULL )
            SQLSelectCommand selectPostalCode = new SQLSelectCommand.Builder(connector, "Polaris.PostalCodes")
                    .string("PostalCode")
                    .string("City")
                    .string("State")
                    .integer("CountryID")
                    .string("County")
                    .whereInteger("PostalCodeID", postalCodeId)
                    .build();
            status = selectPostalCode.execute();
            System.out.println("**RESULTS: PatronRegistration'" + status.getStatus() + "'");
            System.out.println("STATUS_out:'"+status.getStdout()+"'");
            System.out.println("STATUS_err:'"+status.getStderr()+"'");
            System.out.println("RESPONSE:'"+response.getMessage()+"'\n====================================");

            //Polaris, Polaris, Addresses, AddressID, 4, int identity, 10, 4, 0, 10, 0, null, null, 4, null, null, 1, NO, 
            //Polaris, Polaris, Addresses, PostalCodeID, 4, int, 10, 4, 0, 10, 0, null, null, 4, null, null, 2, NO, 
            //Polaris, Polaris, Addresses, StreetOne, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 3, YES, 
            //Polaris, Polaris, Addresses, StreetTwo, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 4, YES, 
            //Polaris, Polaris, Addresses, ZipPlusFour, 12, varchar, 4, 4, null, null, 1, null, null, 12, null, 4, 5, YES, 
            //Polaris, Polaris, Addresses, MunicipalityName, 12, varchar, 64, 64, null, null, 1, null, null, 12, null, 64, 6, YES,
            // LAST_ADDRESS_ID:>>492721<<
            SQLSelectCommand selectLastAddress = new SQLSelectCommand.Builder(connector, "Polaris.Addresses")
                    .integer("AddressID")
                    .integer("PostalCodeID")
                    .string("StreetOne")
                    .string("StreetTwo")
                    .string("ZipPlusFour")
                    .string("MunicipalityName")
                    .whereInteger("AddressID", addressId)
                    .build();
            status = selectLastAddress.execute();
            System.out.println("**RESULTS: PatronRegistration'" + status.getStatus() + "'");
            System.out.println("STATUS_out:'"+status.getStdout()+"'");
            System.out.println("STATUS_err:'"+status.getStderr()+"'");
            System.out.println("RESPONSE:'"+response.getMessage()+"'\n====================================");

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
            SQLSelectCommand selectPatronAddresses = new SQLSelectCommand.Builder(connector, "Polaris.PatronAddresses")
                    .integer("AddressID")
                    .integer("AddressTypeID")
                    .string("FreeTextLabel")
                    .string("Verified")
                    .string("VerificationDate") // null
                    .string("PolarisUserID") // null
                    .whereInteger("PatronID", patronId)
                    .build();
            status = selectPatronAddresses.execute();
            System.out.println("**RESULTS: selectPatronAddresses'" + status.getStatus() + "'");
            System.out.println("STATUS_out:'"+status.getStdout()+"'");
            System.out.println("STATUS_err:'"+status.getStderr()+"'");
            System.out.println("RESPONSE:'"+response.getMessage()+"'\n====================================");


            requestBuilder.tidy();
        }
    }

    /**
     * Test of getCreateUserCommand method, of class PolarisSQLRequestBuilder.
     */
    @Test
    public void testGetCreateUserCommand()
    {
        if (test == WhichTest.CREATE_CUSTOMER)
        {
            System.out.println("===getCreateUserCommand===");
            Response response = new Response();
            // Normalizer for testing. 
            CustomerLoadNormalizer normalizer = CustomerLoadNormalizer.getInstanceOf(true);
            assertTrue(normalizer != null);
            // This command is the last untested or failed command to run from 
            // requestBuilder.getCreateUserCommand
            Command command = requestBuilder.getCreateUserCommand(customer, response, normalizer);
            System.out.println("COMMAND:'"+command.toString()+"'");
            CommandStatus status = command.execute();
            System.out.println("STATUS_out:'"+status.getStdout()+"'");
            System.out.println("STATUS_err:'"+status.getStderr()+"'");
            System.out.println("RESPONSE:'"+response.getMessage()+"'");
            requestBuilder.tidy();
        }
    }

    /**
     * Test of getUpdateUserCommand method, of class PolarisSQLRequestBuilder.
     */
    @Test
    public void testGetUpdateUserCommand()
    {
        if (test == WhichTest.UPDATE_CUSTOMER)
        {
            System.out.println("== getUpdateUserCommand ==");

            Response response = new Response();
            // Normalizer for testing. 
            CustomerLoadNormalizer normalizer = CustomerLoadNormalizer.getInstanceOf(true);
            assertTrue(normalizer != null);
            Command command = requestBuilder.getUpdateUserCommand(customer, response, normalizer);
            CommandStatus status = command.execute();
            System.out.println("UPDATE STATUS_out:'"+status.getStdout()+"'");
            System.out.println("UPDATE STATUS_err:'"+status.getStderr()+"'");
            System.out.println("UPDATE RESPONSE:'"+response.getMessage()+"'");
            requestBuilder.tidy();
        }
        
    }
}
