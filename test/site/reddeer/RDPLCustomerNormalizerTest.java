package site.reddeer;

import mecard.Response;
import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.NativeFormatToMeCardCustomer;
import mecard.sip.SIPToMeCardCustomer;
import mecard.symphony.MeCardCustomerToFlat;

/**
 *
 * @author Andrew Nisbet <andrew.nisbet@epl.ca>
 */


public class RDPLCustomerNormalizerTest
{
    
    public RDPLCustomerNormalizerTest()
    {
    }

    /**
     * Test of finalize method, of class RDPLCustomerNormalizer.
     */
    @Test
    public void testFinalize()
    {
        System.out.println("===finalize===");
        NativeFormatToMeCardCustomer formatter = new SIPToMeCardCustomer();
        Customer unformattedCustomer = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PECALGARY|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20");
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToFlat(unformattedCustomer);
        Response response = new Response();
        RDPLCustomerNormalizer instance = new RDPLCustomerNormalizer(true);
        instance.finalize(unformattedCustomer, formattedCustomer, response);
        assertTrue(formattedCustomer.containsKey("USER_PREFERRED_NAME"));
    }
    
}
