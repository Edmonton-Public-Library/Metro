package site.trac;

import mecard.Response;
import mecard.config.CustomerFieldTypes;
import mecard.config.PolarisTable;
import mecard.customer.Customer;
import mecard.customer.FormattedCustomer;
import mecard.customer.polaris.PolarisSQLFormattedCustomer;
import mecard.customer.sip.SIPCustomerFormatter;
import mecard.customer.symphony.FlatFormattedCustomer;
import org.junit.Test;
import static org.junit.Assert.*;
import site.shortgrass.SLSCustomerNormalizer;

/**
 *
 * @author anisbet
 */
public class TRACCustomerNormalizerTest
{
    
    public TRACCustomerNormalizerTest()
    {
    }

    /**
     * Test of finalize method, of class TRACCustomerNormalizer.
     */
    @Test
    public void testFinalize()
    {
        System.out.println("==finalize==");
        SIPCustomerFormatter formatter = new SIPCustomerFormatter();
        
        Customer unformattedCustomer = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20");
        FormattedCustomer formattedCustomer = new PolarisSQLFormattedCustomer(unformattedCustomer);
        Response response = new Response();
        SLSCustomerNormalizer instance = new SLSCustomerNormalizer(true);
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
//        assertFalse(formattedCustomer.containsKey("CITY/STATE"));
//        assertTrue(formattedCustomer.containsKey("CITYPROV"));
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
        // Not used at this time
//        System.out.println("normalizeOnUpdate");
//        Customer customer = null;
//        Response response = null;
//        TRACCustomerNormalizer instance = null;
//        instance.normalizeOnUpdate(customer, response);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
