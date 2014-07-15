package site.reddeer;

import api.CustomerMessage;
import mecard.customer.Customer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class RDPLCustomerGetNormalizerTest
{
    
    public RDPLCustomerGetNormalizerTest()
    {
    }

    /**
     * Test of setCustomerValuesFromSiteSpecificFields method, of class RDPLCustomerGetNormalizer.
     */
    @Test
    public void testSetCustomerValuesFromSiteSpecificFields()
    {
        System.out.println("setCustomerValuesFromSiteSpecificFields");
        Customer customer = null;
        CustomerMessage message = null;
        RDPLCustomerGetNormalizer instance = new RDPLCustomerGetNormalizer();
        instance.setCustomerValuesFromSiteSpecificFields(customer, message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
