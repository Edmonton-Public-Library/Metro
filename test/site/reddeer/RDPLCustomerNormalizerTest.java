package site.reddeer;

import mecard.Response;
import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;
import mecard.customer.MeCardCustomerToNativeFormat;

/**
 *
 * @author anisbet
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
        System.out.println("finalize");
        Customer rawCustomer = null;
        MeCardCustomerToNativeFormat formattedCustomer = null;
        Response response = null;
        RDPLCustomerNormalizer instance = null;
        instance.finalize(rawCustomer, formattedCustomer, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
