
package site.shortgrass;

import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.sip.SIPCustomerFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */


public class SLSCustomerGetNormalizerTest
{
    
    public SLSCustomerGetNormalizerTest()
    {
    }

    /**
     * Test of setCustomerValuesFromSiteSpecificFields method, of class SLSCustomGetNormalizer.
     */
    @Test
    public void testSetCustomerValuesFromSiteSpecificFields()
    {
        System.out.println("==setCustomerValuesFromSiteSpecificFields==");
        SIPCustomerFormatter formatter = new SIPCustomerFormatter();
        
        Customer c = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20");
        System.out.println("SEX:'" + c.get(CustomerFieldTypes.SEX)+"'");
        assertTrue(c.get(CustomerFieldTypes.SEX).compareTo("M") == 0);
        
    }
    
}
