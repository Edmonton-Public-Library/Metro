
package mecard.requestbuilder;

import api.Command;
import api.CommandStatus;
import api.CustomerMessage;
import api.PolarisSIPCustomerMessage;
import api.SIPCustomerMessage;
import api.SIPMessage;
import mecard.QueryTypes;
import mecard.Response;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class SIPRequestBuilderTest
{
    
    public SIPRequestBuilderTest()
    {
        
    }

    /**
     * Test of getCustomerMessage method, of class SIPRequestBuilder.
     */
    @Test
    public void testGetCustomerMessage()
    {
        System.out.println("==getCustomerMessage==");
        String customerString = "64YYYY      Y   00020130606    115820000000000000000100000000AO|AA21221012345678|AEBilly, Balzac|AQEPLMNA|BZ0025|CA0041|CB0040|BLY|CQY|BV 12.00|BD7 Sir Winston Churchill Square Edmonton, AB T5J 2V4|BEilsteam@epl.ca|BHUSD|PA20140321    235900|PD20050303|PCEPL-THREE|PFM|DB$0.00|DM$0.00|AFUser BLOCKED|AY0AZACC6";
        ILSRequestBuilder instance = SIPRequestBuilder.getInstanceOf(QueryTypes.GET_CUSTOMER, true);
        // it technically doesn't matter what the customer data is so long as it is legal SIP2.
        // 
        SIPMessage m = (SIPMessage) instance.getCustomerMessage(customerString);
        // If the sip2.properties file contains a 'ils-type' of POLARIS or SIRSI_DYNIX this will determine the
        // type of object that should have been created.
        System.out.println("sip2 properties set to '" + m.getILSType() + "'");
        switch (m.getILSType())
        {
            case POLARIS:
                assertTrue(m instanceof PolarisSIPCustomerMessage);
                break;
            case SIRSI_DYNIX:
                assertTrue(m instanceof SIPCustomerMessage);
                break;
            default:
                fail("You are testing an unknown setting of 'ils-type' in sip2.properties.");
        }
    }
    
}
