
package mecard.requestbuilder;

import static org.junit.Assert.*;

import api.Command;
import api.CommandStatus;
import api.CustomerMessage;
import json.RequestDeserializer;
import mecard.QueryTypes;
import mecard.Request;
import mecard.Response;
import mecard.customer.Customer;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.polaris.papi.PapiToMeCardCustomer;
import mecard.polaris.papi.PapiXmlCustomerResponse;
import mecard.polaris.papi.PapiXmlStatusResponse;
import org.junit.Test;
import site.CustomerLoadNormalizer;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class PapiRequestBuilderTest
{

    /*
    ***
    *** WARNING: PAPI will block calls if more than 3 authentication attempts are
    *** WARNING: made within 5 minutes. Run the test, get, and update tests sparingly.
    ***
    */
    private final double expectedVersion;
    private final String existingCustomerId;
    private final String existingCustomerPin;
    private final Customer createCustomer;
    private final Customer updateCustomer;
//    private final String xmlPatronRegistrationCreateData;
    private final boolean runUpdateCustomerTest;
    private final boolean runCreateCustomerTest;
    private final boolean runGetCustomerTest;
    private final boolean runCustomerExistsTest;
    private final String xmlPatronRegistrationCreateResponse;
    private final boolean runStatusTest;
    private final String nonExistingCustomerId;
    private final String nonExistingCustomerPin;
    
    public PapiRequestBuilderTest()
    {
//        xmlPatronRegistrationCreateData = "<PatronRegistrationCreateData>"
//        + "<LogonBranchID>1</LogonBranchID>"
//        + "<LogonUserID>1</LogonUserID>"
//        + "<LogonWorkstationID>1</LogonWorkstationID>"
//        + "<PatronBranchID>3</PatronBranchID>"
//        + "<PostalCode>N2V2V4</PostalCode>"
//        + "<City>Liverpool</City>"
//        + "<State>NY</State>"
//        + "<StreetOne>100 Main Street</StreetOne>"
//        + "<NameFirst>John</NameFirst>"
//        + "<NameLast>Smith</NameLast>"
//        + "<PhoneVoice1>555-1212</PhoneVoice1>"
//        + "<EmailAddress>dude@hotmail.com</EmailAddress>"
//        + "<UserName>PolarisDude</UserName>"
//        + "<Barcode>21221012345678</Barcode>"
//        + "<Password>64058</Password>"
//        + "<Password2>64058</Password2>"
//        + "<LegalNameFirst>Johnathan</LegalNameFirst>"
//        + "<LegalNameLast>Smith</LegalNameLast>"
//        + "<LegalNameMiddle>Edward</LegalNameMiddle>"
//        + "<UseLegalNameOnNotices>true</UseLegalNameOnNotices>"
//        + "<LegalFullName>Johnathan Edward Smith</LegalFullName>"
//        + "</PatronRegistrationCreateData>";
        xmlPatronRegistrationCreateResponse = "<PatronBasicDataGetResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <PAPIErrorCode>0</PAPIErrorCode>\n" +
                "  <ErrorMessage>\n" +
                "  </ErrorMessage>\n" +
                "  <PatronBasicData>\n" +
                "    <PatronID>2022</PatronID>\n" +
                "    <Barcode>21221012345678</Barcode>\n" +
                "    <NameFirst>Billy</NameFirst>\n" +
                "    <NameLast>Balzac</NameLast>\n" +
                "    <NameMiddle i:nil=\"true\" />\n" +
                "    <PhoneNumber>555-1212</PhoneNumber>\n" +
                "    <EmailAddress>dude@hotmail.com</EmailAddress>\n" +
                "    <ItemsOutCount>0</ItemsOutCount>\n" +
                "    <ItemsOverdueCount>0</ItemsOverdueCount>\n" +
                "    <ItemsOutLostCount>0</ItemsOutLostCount>\n" +
                "    <HoldRequestsTotalCount>0</HoldRequestsTotalCount>\n" +
                "    <HoldRequestsCurrentCount>0</HoldRequestsCurrentCount>\n" +
                "    <HoldRequestsShippedCount>0</HoldRequestsShippedCount>\n" +
                "    <HoldRequestsHeldCount>0</HoldRequestsHeldCount>\n" +
                "    <HoldRequestsUnclaimedCount>0</HoldRequestsUnclaimedCount>\n" +
                "    <ChargeBalance>0.0000</ChargeBalance>\n" +
                "    <CreditBalance>0.0000</CreditBalance>\n" +
                "    <DepositBalance>0.0000</DepositBalance>\n" +
                "    <NameTitle i:nil=\"true\" />\n" +
                "    <NameSuffix i:nil=\"true\" />\n" +
                "    <PhoneNumber2 i:nil=\"true\" />\n" +
                "    <PhoneNumber3 i:nil=\"true\" />\n" +
                "    <Phone1CarrierID>0</Phone1CarrierID>\n" +
                "    <Phone2CarrierID>0</Phone2CarrierID>\n" +
                "    <Phone3CarrierID>0</Phone3CarrierID>\n" +
                "    <CellPhone i:nil=\"true\" />\n" +
                "    <CellPhoneCarrierID>0</CellPhoneCarrierID>\n" +
                "    <AltEmailAddress i:nil=\"true\" />\n" +
                "    <BirthDate i:nil=\"true\" />\n" +
                "    <RegistrationDate>2022-07-05T00:00:00</RegistrationDate>\n" +
                "    <LastActivityDate>2022-07-05T17:01:14.217</LastActivityDate>\n" +
                "    <AddrCheckDate>2027-07-05T00:00:00</AddrCheckDate>\n" +
                "    <MessageNewCount>0</MessageNewCount>\n" +
                "    <MessageReadCount>0</MessageReadCount>\n" +
                "    <PatronOrgID>3</PatronOrgID>\n" +
                "    <PatronCodeID>1</PatronCodeID>\n" +
                "    <DeliveryOptionID>1</DeliveryOptionID>\n" +
                "    <ExcludeFromAlmostOverdueAutoRenew>false</ExcludeFromAlmostOverdueAutoRenew>\n" +
                "    <ExcludeFromPatronRecExpiration>false</ExcludeFromPatronRecExpiration>\n" +
                "    <ExcludeFromInactivePatron>false</ExcludeFromInactivePatron>\n" +
                "    <EReceiptOptionID>0</EReceiptOptionID>\n" +
                "    <TxtPhoneNumber>0</TxtPhoneNumber>\n" +
                "    <EmailFormatID>2</EmailFormatID>\n" +
                "    <LegalNameFirst>Johnathan</LegalNameFirst>\n" +
                "    <LegalNameLast>Smith</LegalNameLast>\n" +
                "    <LegalNameMiddle>Edward</LegalNameMiddle>\n" +
                "    <UseLegalNameOnNotices>true</UseLegalNameOnNotices>\n" +
                "    <LegalFullName>Smith, Johnathan Edward</LegalFullName>\n" +
                "    <PatronAddresses>\n" +
                "      <PatronAddress>\n" +
                "        <AddressID>339329</AddressID>\n" +
                "        <FreeTextLabel>Home</FreeTextLabel>\n" +
                "        <StreetOne>11811 74 Ave.</StreetOne>\n" +
                "        <StreetTwo i:nil=\"true\" />\n" +
                "        <StreetThree i:nil=\"true\" />\n" +
                "        <City>Edmonton</City>\n" +
                "        <State>AB</State>\n" +
                "        <County>\n" +
                "        </County>\n" +
                "        <PostalCode>90210</PostalCode>\n" +
                "        <ZipPlusFour i:nil=\"true\" />\n" +
                "        <Country>USA</Country>\n" +
                "        <CountryID>1</CountryID>\n" +
                "        <AddressTypeID>2</AddressTypeID>\n" +
                "      </PatronAddress>\n" +
                "    </PatronAddresses>\n" +
                "    <ExpirationDate>2022-07-30T19:38:30</ExpirationDate>\n" +
                "    <RequestPickupBranchID>0</RequestPickupBranchID>\n" +
                "    <User1 i:nil=\"true\" />\n" +
                "    <User2 i:nil=\"true\" />\n" +
                "    <User3 i:nil=\"true\" />\n" +
                "    <User4 i:nil=\"true\" />\n" +
                "    <User5 i:nil=\"true\" />\n" +
                "    <PatronNotes i:nil=\"true\" />\n" +
                "    <PatronSystemBlocks />\n" +
                "  </PatronBasicData>\n" +
                "</PatronBasicDataGetResult>";
        RequestDeserializer deserializer = new RequestDeserializer();
        String meLibraryRequestCreateCustomer =
// "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12222 144 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0G4\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20230602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
   "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346099\",\"pin\":\"7711\",\"customer\":\"{\\\"ID\\\":\\\"21221012346099\\\",\\\"PIN\\\":\\\"7711\\\",\\\"PREFEREDNAME\\\":\\\"Dickson, Bill\\\",\\\"STREET\\\":\\\"11111 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0G4\\\",\\\"SEX\\\":\\\"F\\\",\\\"EMAIL\\\":\\\"ilsadmins@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19630822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20230602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Bill\\\",\\\"LASTNAME\\\":\\\"Dickson\\\"}\"}";
// "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346004\",\"pin\":\"7711\",\"customer\":\"{\\\"ID\\\":\\\"21221012346004\\\",\\\"PIN\\\":\\\"7711\\\",\\\"PREFEREDNAME\\\":\\\"Blue, Darter\\\",\\\"STREET\\\":\\\"22222 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0G4\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"anisbet@epl.ca\\\",\\\"PHONE\\\":\\\"7805551212\\\",\\\"DOB\\\":\\\"19630822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20230602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Darter\\\",\\\"LASTNAME\\\":\\\"Blue\\\"}\"}";
// "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345999\",\"pin\":\"7711\",\"customer\":\"{\\\"ID\\\":\\\"21221012345999\\\",\\\"PIN\\\":\\\"7711\\\",\\\"PREFEREDNAME\\\":\\\"Awsome, Richard\\\",\\\"STREET\\\":\\\"11111 111 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0H1\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19690822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20150602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Richard\\\",\\\"LASTNAME\\\":\\\"Awesome\\\"}\"}";
        String meLibraryRequestUpdateCustomer =
//    "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346002\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012346002\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12222 144 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0G4\\\",\\\"SEX\\\":\\\"F\\\",\\\"EMAIL\\\":\\\"anisbet@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
    "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346077\",\"pin\":\"7711\",\"customer\":\"{\\\"ID\\\":\\\"21221012346077\\\",\\\"PIN\\\":\\\"7711\\\",\\\"PREFEREDNAME\\\":\\\"Mason, Matt\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0H2\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804961212\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20230602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Matt\\\",\\\"LASTNAME\\\":\\\"Mason\\\"}\"}";
//    "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345999\",\"pin\":\"64056\",\"customer\":\"{\\\"ID\\\":\\\"21221012345999\\\",\\\"PIN\\\":\\\"64056\\\",\\\"PREFEREDNAME\\\":\\\"Awsome, Richard\\\",\\\"STREET\\\":\\\"11111 111 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0H1\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19690822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20150602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Richard\\\",\\\"LASTNAME\\\":\\\"Awesome\\\"}\"}";
        Request createRequest = deserializer.getDeserializedRequest(meLibraryRequestCreateCustomer);
        createCustomer = createRequest.getCustomer();
        Request updateRequest = deserializer.getDeserializedRequest(meLibraryRequestUpdateCustomer);
        updateCustomer = updateRequest.getCustomer();
        // Get status should return a response with a course-grained version number.
        // See PapiXmlStatusMessage and associated tests for more information.
        expectedVersion        = 7.0;
        // Account exists on Dewey have a pin of 64058 those on WBRL have 7711.
//        existingCustomerId     = "21221012346001";
//        existingCustomerPin    = "64058";
//        existingCustomerId     = "21221012346002";
//        existingCustomerPin    = "64058";
//        existingCustomerId     = "21221012346004";
//        existingCustomerPin    = "7711";
//        existingCustomerId     = "21221012346077";
//        existingCustomerPin    = "7711";
        existingCustomerId     = "21221012346099";
        existingCustomerPin    = "7711";
        nonExistingCustomerId  = "21221012346097";
        nonExistingCustomerPin = "12345";
//        existingCustomerId     = "21221012346000";
//        existingCustomerPin    = "7711";
//        existingCustomerId     = "25555000907877";
//        existingCustomerPin    = "7711";
//        existingCustomerId     = "21221012345999"; // Patron ID 33005 at WBRL
//        existingCustomerPin    = "7711";
        // Turn one (or more) to true to run these tests.
        // Be warned that they all require authentication which will cause 
        // a temporary lockout if more than three authentication attempts are
        // made with a five minute period.
        runStatusTest          = false;
        runGetCustomerTest     = true; // Use this to get customer data
        runCreateCustomerTest  = false;
        runUpdateCustomerTest  = false;
        runCustomerExistsTest  = false; // Use this to check if they exist
    }

    /**
     * Test of getCreateUserCommand method, of class PapiRequestBuilder.
     */
    @Test
    public void testGetCreateUserCommand()
    {
        System.out.println("getCreateUserCommand");
        if (this.runCreateCustomerTest)
        {
            Response response = new Response();
            System.out.println("SENDING CUSTOMER DATA: " + this.createCustomer.toString());
            CustomerLoadNormalizer normalizer = CustomerLoadNormalizer.getInstanceOf(true);
            ILSRequestBuilder requestBuilder = new PapiRequestBuilder(true);
            Command createCustomerCommand = requestBuilder.getCreateUserCommand(this.createCustomer, response, normalizer);
            CommandStatus status = createCustomerCommand.execute();
            System.out.println(" create customer STDOUT:'"+status.getStdout()+"'");
            System.out.println(" create customer STDERR::'"+status.getStderr()+"'");
            System.out.println(" create customer RESPONSE:'"+response.getMessage()+"'");
            assertTrue(requestBuilder.isSuccessful(QueryTypes.CREATE_CUSTOMER, status, response));
            requestBuilder.tidy();
        }
        else
        {
            System.out.println("*warn: CREATE test turned off.");
            assertTrue(true);
        }
    }

    /**
     * Test of getUpdateUserCommand method, of class PapiRequestBuilder.
     */
    @Test
    public void testGetUpdateUserCommand()
    {
        System.out.println("getUpdateUserCommand");
        if (this.runUpdateCustomerTest)
        {
            Response response = new Response();
            CustomerLoadNormalizer normalizer = CustomerLoadNormalizer.getInstanceOf(true);
            ILSRequestBuilder requestBuilder = new PapiRequestBuilder(true);
            Command updateCustomerExistsCommand = requestBuilder.getUpdateUserCommand(
                updateCustomer, response, normalizer);
            CommandStatus status = updateCustomerExistsCommand.execute();
            System.out.println(" update customer STDOUT:'"+status.getStdout()+"'");
            System.out.println(" update customer STDERR::'"+status.getStderr()+"'");
            System.out.println(" update customer RESPONSE:'"+response.getMessage()+"'");
            assertTrue(requestBuilder.isSuccessful(QueryTypes.UPDATE_CUSTOMER, status, response));
            requestBuilder.tidy();
        }
        else
        {
            System.out.println("*warn: UPDATE test turned off.");
            assertTrue(true);
        }
    }

    /**
     * Test of getStatusCommand method, of class PapiRequestBuilder.
     */
    @Test
    public void testGetStatusCommand()
    {
        if (this.runStatusTest)
        {
            System.out.println("getStatusCommand");
            Response response = new Response();
            ILSRequestBuilder requestBuilder = new PapiRequestBuilder(true);
            Command statusCommand = requestBuilder.getStatusCommand(response);
            CommandStatus status = statusCommand.execute();
            assertTrue(requestBuilder.isSuccessful(QueryTypes.GET_STATUS, status, response));
            System.out.println(" Get Status STDOUT:"+status.getStdout());
            System.out.println(" Get Status STDERR:"+status.getStderr());
            // This doesn't rely on exact specifics to match like build version etc.
            // See PapiXmlStatusMessageTest for more information and examples.
            PapiXmlStatusResponse papiStatusResponse = new PapiXmlStatusResponse(status.getStdout());
            double result = papiStatusResponse.getCourseGrainedVersion();
//            assertEquals(expectedVersion, result, 0);
            assertTrue(result >= expectedVersion);
            requestBuilder.tidy();
        }
        else
        {
            System.out.println("*warn: STATUS test turned off.");
            assertTrue(true);
        }
    }

    /**
     * Test of getCustomerCommand method, of class PapiRequestBuilder.
     */
    @Test
    public void testGetCustomerCommand()
    {
        System.out.println("getCustomerCommand");
        if (this.runGetCustomerTest)
        {
            Response response = new Response();
            ILSRequestBuilder requestBuilder = new PapiRequestBuilder(true);
            Command customerExistsCommand = requestBuilder.getCustomerCommand(
                    existingCustomerId, existingCustomerPin, response);
            CommandStatus status = customerExistsCommand.execute();
            System.out.println(" get customer STDOUT:"+status.getStdout());
            System.out.println(" get customer STDERR:"+status.getStderr());
            assertTrue(requestBuilder.isSuccessful(QueryTypes.GET_CUSTOMER, status, response));
            // Non existing which we can do with staff auth, but not patron because it bogs down
            // authentication.
            Response nextResponse = new Response();
            requestBuilder = new PapiRequestBuilder(true);
            Command customerNotExistsCommand = requestBuilder.getCustomerCommand(
                    nonExistingCustomerId, nonExistingCustomerPin, nextResponse);
            CommandStatus notStatus = customerNotExistsCommand.execute();
            System.out.println(" get customer STDOUT:"+notStatus.getStdout());
            System.out.println(" get customer STDERR:"+notStatus.getStderr());
            boolean itWorked = requestBuilder.isSuccessful(QueryTypes.GET_CUSTOMER, notStatus, nextResponse);
            assertFalse(itWorked);
            requestBuilder.tidy();
        }
        else
        {
            System.out.println("*warn: GET test turned off.");
            assertTrue(true);
        }
    }

    /**
     * Test of getFormatter method, of class PapiRequestBuilder.
     */
    @Test
    public void testGetFormatter()
    {
        System.out.println("getFormatter");
        PapiRequestBuilder instance = new PapiRequestBuilder(true);
        NativeFormatToMeCardCustomer actualFormatter = instance.getFormatter();
        assertTrue(actualFormatter instanceof PapiToMeCardCustomer);
        // See PapiToMeCardCustomerTest for more specific tests.
    }

    /**
     * Test of isSuccessful method, of class PapiRequestBuilder.
     */
//    @Test
//    public void testIsSuccessful()
//    {
//        System.out.println("isSuccessful");
//        Response response = new Response();
//        ILSRequestBuilder requestBuilder = new PapiRequestBuilder(true);
//        Command statusCommand = requestBuilder.getStatusCommand(response);
//        System.out.println("StatusCommand:: " + statusCommand.toString());
//        CommandStatus status = statusCommand.execute();
//        assertTrue(requestBuilder.isSuccessful(QueryTypes.GET_STATUS, status, response));
//        // We could extend this test to include other response types, but 
//        // they will all get tested eventually within other unit tests.
//    }

    /**
     * Test of tidy method, of class PapiRequestBuilder.
     */
    @Test
    public void testTidy()
    {
        System.out.println("tidy - trivial always passes.");
        assertTrue(true);
    }

    /**
     * Test of toString method, of class PapiRequestBuilder.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString - trivial always passes.");
        assertTrue(true);
    }

    /**
     * Test of getCustomerMessage method, of class PapiRequestBuilder.
     */
    @Test
    public void testGetCustomerMessage()
    {
        System.out.println("getCustomerMessage");
//        PapiRequestBuilder instance = new PapiRequestBuilder(true);
//        CustomerMessage customerMessage = instance.getCustomerMessage(xmlPatronRegistrationCreateResponse);
//        System.out.println("Customer Message:\n" + customerMessage.toString());
//        assertTrue(customerMessage instanceof PapiXmlCustomerResponse);
        assertTrue(true);
    }

    /**
     * Test of testCustomerExists method, of class PapiRequestBuilder.
     */
    @Test
    public void testTestCustomerExists()
    {
        System.out.println("testCustomerExists");
        if (this.runCustomerExistsTest)
        {
            Response response = new Response();
            ILSRequestBuilder requestBuilder = new PapiRequestBuilder(true);
            Command customerExistsCommand = requestBuilder.testCustomerExists(
                    existingCustomerId, existingCustomerPin, response);
            CommandStatus status = customerExistsCommand.execute();
            System.out.println(" test customer STDOUT:"+status.getStdout());
            System.out.println(" test customer STDERR:"+status.getStderr());
            assertTrue(requestBuilder.isSuccessful(QueryTypes.TEST_CUSTOMER, status, response));
            // now test if the customer could not be found.
            // Uncomment this test but it is a bit confusing to read.
//            requestBuilder = new PapiRequestBuilder(true);
//            Command customerDoesntExistCommand = requestBuilder.testCustomerExists(
//                    nonExistingCustomerId, "12345", response);
//            status = customerDoesntExistCommand.execute();
//            System.out.println(" test customer STDOUT:"+status.getStdout());
//            System.out.println(" test customer STDERR:"+status.getStderr());
//            assertFalse(requestBuilder.isSuccessful(QueryTypes.TEST_CUSTOMER, status, response));
            requestBuilder.tidy();
        }
        else
        {
            System.out.println("*warn: EXISTS test turned off.");
            assertTrue(true);
        }
    }
    
}
