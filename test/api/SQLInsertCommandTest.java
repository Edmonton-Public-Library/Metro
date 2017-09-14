
package api;

import java.util.Properties;
import json.RequestDeserializer;
import mecard.Request;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.PolarisSQLPropertyTypes;
import mecard.config.PolarisTable;
import mecard.config.PropertyReader;
import mecard.customer.Customer;
import mecard.requestbuilder.PolarisSQLRequestBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class SQLInsertCommandTest
{
    private final PolarisSQLRequestBuilder requestBuilder;
    private final SQLConnector connector;
    private final Customer customer;
    public SQLInsertCommandTest()
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
                "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345999\",\"pin\":\"64056\",\"customer\":\"{\\\"ID\\\":\\\"21221012345999\\\",\\\"PIN\\\":\\\"64056\\\",\\\"PREFEREDNAME\\\":\\\"William, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0H2\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804961212\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20150602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"William\\\"}\"}";
//                "{\"code\":\"UPDATE_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345999\",\"pin\":\"64056\",\"customer\":\"{\\\"ID\\\":\\\"21221012345999\\\",\\\"PIN\\\":\\\"64056\\\",\\\"PREFEREDNAME\\\":\\\"Awsome, Richard\\\",\\\"STREET\\\":\\\"11111 111 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"T6G0H1\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19690822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20150602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Richard\\\",\\\"LASTNAME\\\":\\\"Awesome\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        customer = request.getCustomer();
    }

    /**
     * Test of toString method, of class SQLInsertCommand.
     */
    @Test
    public void testToString()
    {
        System.out.println("==toString==");
        SQLInsertCommand instance = new SQLInsertCommand.Builder(connector, "Polaris.PatronRegistration", true)
            .integer(PolarisTable.PatronRegistration.PATRON_ID.toString(), customer.get(CustomerFieldTypes.ID).substring(0, 5))
            .smallInt(PolarisTable.PatronRegistration.LANGUAGE_ID.toString(), 
                    "1")
            .string(PolarisTable.PatronRegistration.NAME_FIRST.toString(), 
                    customer.get(CustomerFieldTypes.FIRSTNAME))
            .string(PolarisTable.PatronRegistration.NAME_LAST.toString(), 
                    customer.get(CustomerFieldTypes.LASTNAME))
            .string(PolarisTable.PatronRegistration.NAME_MIDDLE.toString(), null)
            .string(PolarisTable.PatronRegistration.NAME_TITLE.toString(), null)
            .string(PolarisTable.PatronRegistration.NAME_SUFFIX.toString(), null)
            .string(PolarisTable.PatronRegistration.PHONE_VOICE_1.toString(), 
                    customer.get(CustomerFieldTypes.PHONE))
            .string(PolarisTable.PatronRegistration.PHONE_VOICE_2.toString(), null)
            .string(PolarisTable.PatronRegistration.PHONE_VOICE_3.toString(), null)
            .string(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString(), 
                    customer.get(CustomerFieldTypes.EMAIL))
    //                DEPRECATED: .string(PolarisTable.PatronRegistration.PASSWORD.toString(), 
    //                        customer.get(PolarisTable.PatronRegistration.PASSWORD.toString()))
            .dateTimeNow("DateNowForSomeReason")
            .dateTime(PolarisTable.PatronRegistration.EXPIRATION_DATE.toString(), "2018-09-14 13:01:23")
            .dateTime(PolarisTable.PatronRegistration.ADDR_CHECK_DATE.toString(), "2018-09-14 13:01:23")
            .dateTimeNow("DateNowForSomeReason")
            .string(PolarisTable.PatronRegistration.USER_1.toString(), "USER_1") // Set these during customer normalization.
            .string(PolarisTable.PatronRegistration.USER_2.toString(), "USER_2")
            .string(PolarisTable.PatronRegistration.USER_3.toString(), "USER_3")
            .string(PolarisTable.PatronRegistration.USER_4.toString(), "USER_4") // The none is actually (none) including the brackets. It actually links to a list of options.
            .string(PolarisTable.PatronRegistration.USER_5.toString(), "USER_5") // Set these during customer normalization.
            .setChar(PolarisTable.PatronRegistration.GENDER.toString(),   // single char.
                    customer.get(CustomerFieldTypes.SEX))
            .dateTime(PolarisTable.PatronRegistration.BIRTH_DATE.toString(), "1963-09-14 13:01:23") 
            .dateTimeNow("DateToday")
            .string(PolarisTable.PatronRegistration.FORMER_ID.toString(), null)   // Note for update on lost card.
            .tinyInt(PolarisTable.PatronRegistration.READING_LIST.toString(), "0")
            .string(PolarisTable.PatronRegistration.PHONE_FAX.toString(), null)
            .integer(PolarisTable.PatronRegistration.DELIVERY_OPTION_ID.toString(), "5")
            .integer(PolarisTable.PatronRegistration.STATISTICAL_CLASS_ID.toString()) // null
            .bit(PolarisTable.PatronRegistration.COLLECTION_EXEMPT.toString(), "0")
            .string(PolarisTable.PatronRegistration.ALT_EMAIL_ADDRESS.toString(), null)
            .bit(PolarisTable.PatronRegistration.EXCLUDE_FROM_OVERDUES.toString(), "0")
            .string(PolarisTable.PatronRegistration.SDI_EMAIL_ADDRESS.toString(), null)
            .integer(PolarisTable.PatronRegistration.SDI_EMAIL_FORMAT_ID.toString(), null) // null integer value.
            .bit(PolarisTable.PatronRegistration.SDI_POSITIVE_ASSENT.toString(), null)
            .dateTime("DateRegistered")
            .bit(PolarisTable.PatronRegistration.DELETION_EXEMPT.toString(), "0")
            .string(PolarisTable.PatronRegistration.PATRON_FULL_NAME.toString(), 
                    customer.get(CustomerFieldTypes.FIRSTNAME))
            .bit(PolarisTable.PatronRegistration.EXCLUDE_FROM_HOLDS.toString(), "0")
            .bit(PolarisTable.PatronRegistration.EXCLUDE_FROM_BILLS.toString(), "0")
            .integer(PolarisTable.PatronRegistration.EMAIL_FORMAT_ID.toString(), "7")
            .string(PolarisTable.PatronRegistration.PATRON_FIRST_LAST_NAME.toString(), 
                    "First_name and Last_name")
            .string(PolarisTable.PatronRegistration.USERNAME.toString(), null)
            .dateTime("LastMergeDate") // null
            .integer(PolarisTable.PatronRegistration.MERGE_USER_ID.toString(), null)
            .string(PolarisTable.PatronRegistration.MERGE_BARCODE.toString(), null)
    //                DEPRECATED: .string(PolarisTable.PatronRegistration.CELL_PHONE.toString(), null)
    //                DEPRECATED: .integer(PolarisTable.PatronRegistration.CELL_PHONE_CARRIER_ID.toString(), null)
            .bit(PolarisTable.PatronRegistration.ENABLE_SMS.toString(), "0") 
            .integer(PolarisTable.PatronRegistration.REQUEST_PICKUP_BRANCH_ID.toString(), null)
            .integer(PolarisTable.PatronRegistration.PHONE1_CARRIER_ID.toString(), null)
            .integer(PolarisTable.PatronRegistration.PHONE2_CARRIER_ID.toString(), null)
            .integer(PolarisTable.PatronRegistration.PHONE3_CARRIER_ID.toString(), null)
            .integer(PolarisTable.PatronRegistration.ERECEIPT_OPTION_ID.toString(), null)
            .tinyInt(PolarisTable.PatronRegistration.TXT_PHONE_NUMBER.toString())
            .bit(PolarisTable.PatronRegistration.EXCLUDE_FROM_ALMOST_OVERDUE_AUTO_RENEW.toString(), "1")
            .bit(PolarisTable.PatronRegistration.EXCLUDE_FROM_PATRON_REC_EXPIRATION.toString(), "1")
            .bit(PolarisTable.PatronRegistration.EXCLUDE_FROM_INACTIVE_PATRON.toString(), "1")
            .bit(PolarisTable.PatronRegistration.DO_NOT_SHOW_E_RECEIPT_PROMPT.toString(), "1")
//            .procedure("Polaris.Circ_SetPatronPassword", customer.get(CustomerFieldTypes.PIN))
            .build();
        System.out.println(">>>" + instance.toString());
    }
}
