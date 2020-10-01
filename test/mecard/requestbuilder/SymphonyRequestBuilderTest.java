
package mecard.requestbuilder;

import api.Command;
import api.CommandStatus;
import api.CustomerMessage;
import mecard.QueryTypes;
import mecard.Response;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Test;
import site.CustomerLoadNormalizer;

/**
 *
 * @author anisbet
 */


public class SymphonyRequestBuilderTest
{
    
    public SymphonyRequestBuilderTest()
    {
    }

    /**
     * Test of getCreateUserCommand method, of class SymphonyRequestBuilder.
     */
    @Test
    public void testGetCreateUserCommand()
    {
        System.out.println("==getCreateUserCommand==");
        String property = "DEFER:C:\\Users\\ANisbet\\Dropbox\\development\\MeCard";
        String path = property.substring(SymphonyRequestBuilder.SSH_DEFER_MARKER.length());
        System.out.println("DEFER= '" + path + "'");
    }

    /**
     * Test of getUpdateUserCommand method, of class SymphonyRequestBuilder.
     */
//    @Test
//    public void testGetUpdateUserCommand()
//    {
//        System.out.println("==getUpdateUserCommand==");
//        Customer customer = null;
//        Response response = null;
//        CustomerLoadNormalizer normalizer = null;
//        SymphonyRequestBuilder instance = null;
//        Command expResult = null;
//        Command result = instance.getUpdateUserCommand(customer, response, normalizer);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
