
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
import mecard.polaris.PapiToMeCardCustomer;
import mecard.polaris.PapiXmlCustomerResponse;
import mecard.polaris.PapiXmlStatusResponse;
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
    private final String xmlPatronRegistrationCreateData;
    private final boolean runUpdateCustomerTest;
    private final boolean runCreateCustomerTest;
    private final boolean runGetCustomerTest;
    private final boolean runCustomerExistsTest;
    
    public PapiRequestBuilderTest()
    {
        xmlPatronRegistrationCreateData = "<PatronRegistrationCreateData>"
        + "<LogonBranchID>1</LogonBranchID>"
        + "<LogonUserID>1</LogonUserID>"
        + "<LogonWorkstationID>1</LogonWorkstationID>"
        + "<PatronBranchID>3</PatronBranchID>"
        + "<PostalCode>N2V2V4</PostalCode>"
        + "<City>Liverpool</City>"
        + "<State>NY</State>"
        + "<StreetOne>100 Main Street</StreetOne>"
        + "<NameFirst>John</NameFirst>"
        + "<NameLast>Smith</NameLast>"
        + "<PhoneVoice1>555-1212</PhoneVoice1>"
        + "<EmailAddress>dude@hotmail.com</EmailAddress>"
        + "<UserName>PolarisDude</UserName>"
        + "<Barcode>21221012345678</Barcode>"
        + "<Password>64058</Password>"
        + "<Password2>64058</Password2>"
        + "<LegalNameFirst>Johnathan</LegalNameFirst>"
        + "<LegalNameLast>Smith</LegalNameLast>"
        + "<LegalNameMiddle>Edward</LegalNameMiddle>"
        + "<UseLegalNameOnNotices>true</UseLegalNameOnNotices>"
        + "<LegalFullName>Johnathan Edward Smith</LegalFullName>"
        + "</PatronRegistrationCreateData>";
        RequestDeserializer deserializer = new RequestDeserializer();
        String meLibraryRequestCreateCustomer =
//                "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64056\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64056\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12222 144 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0G4\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
                "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346001\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012346001\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Black, Carter\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"90210\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804961212\\\",\\\"DOB\\\":\\\"19630822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20230602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Carter\\\",\\\"LASTNAME\\\":\\\"Black\\\"}\"}";
//                "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345999\",\"pin\":\"64056\",\"customer\":\"{\\\"ID\\\":\\\"21221012345999\\\",\\\"PIN\\\":\\\"64056\\\",\\\"PREFEREDNAME\\\":\\\"Awsome, Richard\\\",\\\"STREET\\\":\\\"11111 111 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0H1\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19690822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20150602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Richard\\\",\\\"LASTNAME\\\":\\\"Awesome\\\"}\"}";
        String meLibraryRequestUpdateCustomer =
                "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346001\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012346001\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12222 144 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0G4\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
//                "{\"code\":\"CREATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012346000\",\"pin\":\"64056\",\"customer\":\"{\\\"ID\\\":\\\"21221012346000\\\",\\\"PIN\\\":\\\"64056\\\",\\\"PREFEREDNAME\\\":\\\"William, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0H2\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804961212\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20150602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"William\\\"}\"}";
//                "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345999\",\"pin\":\"64056\",\"customer\":\"{\\\"ID\\\":\\\"21221012345999\\\",\\\"PIN\\\":\\\"64056\\\",\\\"PREFEREDNAME\\\":\\\"Awsome, Richard\\\",\\\"STREET\\\":\\\"11111 111 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0H1\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19690822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20150602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Richard\\\",\\\"LASTNAME\\\":\\\"Awesome\\\"}\"}";
        Request createRequest = deserializer.getDeserializedRequest(meLibraryRequestCreateCustomer);
        createCustomer = createRequest.getCustomer();
        Request updateRequest = deserializer.getDeserializedRequest(meLibraryRequestUpdateCustomer);
        updateCustomer = updateRequest.getCustomer();
        // Get status should return a response with a course-grained version number.
        // See PapiXmlStatusMessage and associated tests for more information.
        expectedVersion        = 7.0;
        existingCustomerId     = "21221012346001";
//        existingCustomerId     = "21221012346000";
        existingCustomerPin    = "64058";
//        existingCustomerPin    = "64056";
        // Turn one (or more) to true to run these tests.
        // Be warned that they all require authentication which will cause 
        // a temporary lockout if more than three authentication attempts are
        // made with a five minute period.
        runGetCustomerTest     = false;
        runCreateCustomerTest  = false;
        runUpdateCustomerTest  = true;
        runCustomerExistsTest  = false;
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
            System.out.println("*warn: CREATE test require authentication which has"
                    + " been turned off. Usually this is done because frequent"
                    + " authentication attempts trigger a temporary lockout");
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
            System.out.println("*warn: UPDATE test requires authentication which has"
                    + " been turned off. Usually this is done because frequent"
                    + " authentication attempts trigger a temporary lockout");
            assertTrue(true);
        }
    }

    /**
     * Test of getStatusCommand method, of class PapiRequestBuilder.
     */
//    @Test
//    public void testGetStatusCommand()
//    {
//        System.out.println("getStatusCommand");
//        Response response = new Response();
//        ILSRequestBuilder requestBuilder = new PapiRequestBuilder(true);
//        Command statusCommand = requestBuilder.getStatusCommand(response);
//        CommandStatus status = statusCommand.execute();
//        assertTrue(requestBuilder.isSuccessful(QueryTypes.GET_STATUS, status, response));
//        System.out.println(" Get Status STDOUT:"+status.getStdout());
//        System.out.println(" Get Status STDERR:"+status.getStderr());
//        // This doesn't rely on exact specifics to match like build version etc.
//        // See PapiXmlStatusMessageTest for more information and examples.
//        PapiXmlStatusResponse papiStatusResponse = new PapiXmlStatusResponse(status.getStdout());
//        double result = papiStatusResponse.getCourseGrainedVersion();
//        assertEquals(expectedVersion, result, 0);
//        assertTrue(result >= expectedVersion);
//        requestBuilder.tidy();
//    }

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
            requestBuilder.tidy();
        }
        else
        {
            System.out.println("*warn: GET test require authentication which has"
                    + " been turned off. Usually this is done because frequent"
                    + " authentication attempts trigger a temporary lockout");
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
        PapiRequestBuilder instance = new PapiRequestBuilder(true);
        CustomerMessage customerMessage = instance.getCustomerMessage(xmlPatronRegistrationCreateData);
        System.out.println("Customer Message:\n" + customerMessage.toString());
        assertTrue(customerMessage instanceof PapiXmlCustomerResponse);
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
//            requestBuilder = new PapiRequestBuilder(true);
//            Command customerDoesntExistCommand = requestBuilder.testCustomerExists(
//                    existingCustomerId, "12345", response);
//            status = customerDoesntExistCommand.execute();
//            System.out.println(" test customer STDOUT:"+status.getStdout());
//            System.out.println(" test customer STDERR:"+status.getStderr());
//            assertFalse(requestBuilder.isSuccessful(QueryTypes.TEST_CUSTOMER, status, response));
//            requestBuilder.tidy();
        }
        else
        {
            System.out.println("*warn: EXISTS test require authentication which has"
                    + " been turned off. Usually this is done because frequent"
                    + " authentication attempts trigger a temporary lockout");
            assertTrue(true);
        }
    }
    
}
