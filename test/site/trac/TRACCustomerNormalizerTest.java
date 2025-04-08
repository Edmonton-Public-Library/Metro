package site.trac;

import mecard.Response;
import mecard.config.PolarisTable;
import mecard.customer.Customer;
import mecard.polaris.sql.MeCardCustomerToPolarisSQL;
import mecard.sip.SIPToMeCardCustomer;
import org.junit.Test;
import static org.junit.Assert.*;
import mecard.customer.MeCardCustomerToNativeFormat;

/**
 * Note: When testing this class it is vital that you set the environment.properties
 * file to TRAC settings!! Also since this is using a SIPToMeCardCustomer object
 * and TRAC has a specific SIP2 fields that need compensating specifically
 * customer.set(CustomerFieldTypes.PRIVILEGE_EXPIRES, message.getDateField("PX"));
 * customer.set(CustomerFieldTypes.DOB, message.getDateField("BC"));
 * 
 * Once testing in this class is complete the environment.properties 
 * <pre><entry "get-protocol">sip2</entry></pre>
 * can be changed to 
 * <pre><entry "get-protocol">polaris-api</entry></pre>
 * or what-have-you.
 * @author anisbet
 */
public class TRACCustomerNormalizerTest
{
    private Customer unformattedCustomer;
    private SIPToMeCardCustomer formatter;
    public TRACCustomerNormalizerTest()
    {
        this.formatter = new SIPToMeCardCustomer();
        this.unformattedCustomer = this.formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PX20150108    235900|BC19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20");
        System.out.println("Constructor Customer: " + this.unformattedCustomer.toString());
    }

    /**
     * Test of finalize method, of class TRACCustomerNormalizer.
     */
    @Test
    public void testFinalize()
    {
        System.out.println("==finalize==");
        
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToPolarisSQL(this.unformattedCustomer);
        Response response = new Response();
//        SLSCustomerNormalizer instance = new SLSCustomerNormalizer(true);
        TRACCustomerNormalizer instance = new TRACCustomerNormalizer(true);
        instance.finalize(unformattedCustomer, formattedCustomer, response);
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.USER_1.toString()));
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.USER_2.toString()));
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.USER_3.toString()));
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.USER_4.toString()));
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.USER_5.toString()));
        System.out.println("USER_1>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.USER_1.toString()) +"<<<");
        System.out.println("USER_2>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.USER_2.toString()) +"<<<");
        System.out.println("USER_3>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.USER_3.toString()) +"<<<");
        System.out.println("USER_4>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.USER_4.toString()) +"<<<");
        System.out.println("USER_5>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.USER_5.toString()) +"<<<");
        
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.Patrons.PATRON_CODE_ID.toString()));
        System.out.println("PATRON_CODE_ID>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.USER_1.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.Patrons.ORGANIZATION_ID.toString()));
        System.out.println("ORGANIZATION_ID>>"+ formattedCustomer.getValue(PolarisTable.Patrons.ORGANIZATION_ID.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.Patrons.CREATOR_ID.toString()));
        System.out.println("CREATOR_ID>>"+ formattedCustomer.getValue(PolarisTable.Patrons.CREATOR_ID.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.Patrons.BARCODE.toString()));
        System.out.println("BARCODE>>"+ formattedCustomer.getValue(PolarisTable.Patrons.BARCODE.toString()) +"<<<");
        
        //////////////////////////// PatronRegistration ////////////////////////
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.LANGUAGE_ID.toString()));
        System.out.println("LANGUAGE_ID>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.LANGUAGE_ID.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.NAME_FIRST.toString()));
        System.out.println("NAME_FIRST>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.NAME_FIRST.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.NAME_LAST.toString()));
        System.out.println("NAME_LAST>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.NAME_LAST.toString()) +"<<<");
        
        // Full name (last first)
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.PATRON_FULL_NAME.toString()));
        System.out.println("PATRON_FULL_NAME>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.PATRON_FULL_NAME.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.PATRON_FIRST_LAST_NAME.toString()));
        System.out.println("PATRON_FIRST_LAST_NAME>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.PATRON_FIRST_LAST_NAME.toString()) +"<<<");
        
        // Not a required field.
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.PHONE_VOICE_1.toString()));
        System.out.println("PHONE_VOICE_1>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.PHONE_VOICE_1.toString()) +"<<<");

        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()));
        System.out.println("EMAIL_ADDRESS>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.EMAIL_ADDRESS.toString()) +"<<<");
        
//        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.PASSWORD.toString()));
//        System.out.println("PASSWORD>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.PASSWORD.toString()) +"<<<");
        
        // This fails if the proper system date in environment.properties.
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.BIRTH_DATE.toString()));
        System.out.println("BIRTH_DATE>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.BIRTH_DATE.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.EXPIRATION_DATE.toString()));
        System.out.println("EXPIRATION_DATE>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.EXPIRATION_DATE.toString()) +"<<<");
        
//        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.ADDR_CHECK_DATE.toString()));
        System.out.println("ADDR_CHECK_DATE>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.ADDR_CHECK_DATE.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.UPDATE_DATE.toString()));
        System.out.println("UPDATE_DATE>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.UPDATE_DATE.toString()) +"<<<");
        
        // Gender
//        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.GENDER_ID.toString()));
//        System.out.println("GENDER>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.GENDER_ID.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.DELIVERY_OPTION_ID.toString()));
        System.out.println("DELIVERY_OPTION_ID>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.DELIVERY_OPTION_ID.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronRegistration.EMAIL_FORMAT_ID.toString()));
        System.out.println("EMAIL_FORMAT_ID>>"+ formattedCustomer.getValue(PolarisTable.PatronRegistration.EMAIL_FORMAT_ID.toString()) +"<<<");
        
        /////////////////// Postal Codes ///////////////////
        assertTrue(formattedCustomer.containsKey(PolarisTable.PostalCodes.POSTAL_CODE.toString()));
        System.out.println("POSTAL_CODE>>"+ formattedCustomer.getValue(PolarisTable.PostalCodes.POSTAL_CODE.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.PostalCodes.CITY.toString()));
        System.out.println("CITY>>"+ formattedCustomer.getValue(PolarisTable.PostalCodes.CITY.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.PostalCodes.STATE.toString()));
        System.out.println("STATE>>"+ formattedCustomer.getValue(PolarisTable.PostalCodes.STATE.toString()) +"<<<");
        
        assertTrue(formattedCustomer.containsKey(PolarisTable.PostalCodes.COUNTRY_ID.toString()));
        System.out.println("COUNTRY_ID>>"+ formattedCustomer.getValue(PolarisTable.PostalCodes.COUNTRY_ID.toString()) +"<<<");
        
        ////////////////// Addresses ///////////////////////////
        assertTrue(formattedCustomer.containsKey(PolarisTable.Addresses.STREET_ONE.toString()));
        System.out.println("STREET_ONE>>"+ formattedCustomer.getValue(PolarisTable.Addresses.STREET_ONE.toString()) +"<<<");
        
        ///////////////// PatronAddresses ///////////////////////////
        // Not used in 6.1 and above. See PolarisTable.PatronAddresses.ADDRESS_LABEL_ID note.
//        assertTrue(formattedCustomer.containsKey(PolarisTable.PatronAddresses.FREE_TEXT_LABEL.toString()));
//        System.out.println("FREE_TEXT_LABEL>>"+ formattedCustomer.getValue(PolarisTable.PatronAddresses.FREE_TEXT_LABEL.toString()) +"<<<");
    }

    /**
     * Test of normalizeOnCreate method, of class TRACCustomerNormalizer.
     */
    @Test
    public void testNormalizeOnCreate()
    {
        // Not used at this time
//        System.out.println("normalizeOnCreate");
//        Customer customer = null;
//        Response response = null;
//        TRACCustomerNormalizer instance = null;
//        instance.normalizeOnCreate(customer, response);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of normalizeOnUpdate method, of class TRACCustomerNormalizer.
     */
    @Test
    public void testNormalizeOnUpdate()
    {
        System.out.println("==normalizeOnUpdate==");
        System.out.println("Customer: " + this.unformattedCustomer.toString());
        assertTrue(this.unformattedCustomer.hasValidBirthDate());
        Response response = new Response();
        TRACCustomerNormalizer instance = new TRACCustomerNormalizer(true);
        instance.normalizeOnUpdate(this.unformattedCustomer, response);
        assertFalse(this.unformattedCustomer.hasValidBirthDate());
        System.out.println("Customer: " + this.unformattedCustomer.toString());
    }
    
}
