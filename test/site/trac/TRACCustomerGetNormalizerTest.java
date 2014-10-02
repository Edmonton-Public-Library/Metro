
package site.trac;

import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.sip.SIPCustomerFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class TRACCustomerGetNormalizerTest
{
    
    public TRACCustomerGetNormalizerTest()
    {
    }

    /**
     * Test of setCustomerValuesFromSiteSpecificFields method, of class TRACCustomerGetNormalizer.
     */
    @Test
    public void testSetCustomerValuesFromSiteSpecificFields()
    {
        System.out.println("==setCustomerValuesFromSiteSpecificFields==");
        SIPCustomerFormatter formatter = new SIPCustomerFormatter();
        
        Customer c = formatter.getCustomer("64              00120140501    114114000000000000000000000000AO203|AA29335002291042|AEHunting, Will|BZ0025|CA0010|CB0100|BLY|CQY|BHCAD|BV0.00|CC10.00|BD433 King Street, Spruce Grove, AB T7X 3B4|BEsthero@yrl.ab.ca|BC|PA17|PEASGY|PSAdult (18-64)|U4(none)|U5|PZT7X 3B4|PX20201025    235959|PYN|AFPatron status is ok.|AGPatron status is ok.|AY2AZA00A");
        System.out.println("PHONE:'" + c.get(CustomerFieldTypes.PHONE)+"'");
        assertTrue(c.get(CustomerFieldTypes.PHONE).compareTo("X") == 0);
        
        // This test is unnecessary since TRAC uses the correct field 'BF' for phone.
        c = formatter.getCustomer("64              00120140501    114114000000000000000000000000AO203|AA29335002291042|AEHunting, Will|BZ0025|CA0010|CB0100|BF780-939-5343|BLY|CQY|BHCAD|BV0.00|CC10.00|BD433 King Street, Spruce Grove, AB T7X 3B4|BEsthero@yrl.ab.ca|BC|PA17|PEASGY|PSAdult (18-64)|U4(none)|U5|PZT7X 3B4|PX20201025    235959|PYN|AFPatron status is ok.|AGPatron status is ok.|AY2AZA00A");
        System.out.println("PHONE:'" + c.get(CustomerFieldTypes.PHONE)+"'");
        assertTrue(c.get(CustomerFieldTypes.PHONE).compareTo("7809395343") == 0);
        
        c = formatter.getCustomer("64              00120140501    114114000000000000000000000000AO203|AA29335002291042|AEHunting, Will|BZ0025|CA0010|CB0100|BF780-939-5343|BLY|CQY|BHCAD|BV0.00|CC10.00|BD433 King Street, Spruce Grove, AB T7X 3B4|BEsthero@yrl.ab.ca|BC|PA17|PEASGY|PSAdult (18-64)|U4(none)|U5|PZT7X 3B4|PX20201025    235959|PYN|AFPatron status is ok.|AGPatron status is ok.|AY2AZA00A");
        System.out.println("EXPIRY:'" + c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES)+"'");
        assertTrue(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20201025") == 0);
        
        c = formatter.getCustomer("64              00120141002    135321000000000000000000000000AO203|AA29335002291042|AEHunting, Will|BZ0025|CA0010|CB0100|BLY|CQY|BHCAD|BV0.00|CC10.00|BD433 King Street, Spruce Grove, AB T7X 3B4|BEstephaniethero@shaw.ca|BF780-962-2003|BC19751002    235959|PA17|PEASGY|PSAdult (18-64)|U4(none)|U5|PZT7X 3B4|PX20201025    235959|PYN|AFPatron status is ok.|AGPatron status is ok.|AY2AZ9694");
        System.out.println("DOB:'" + c.get(CustomerFieldTypes.DOB)+"'");
        assertTrue(c.get(CustomerFieldTypes.DOB).compareTo("19751002") == 0);
        System.out.println("EXPIRY:'" + c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES)+"'");
        assertTrue(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20201025") == 0);
        System.out.println("PHONE:'" + c.get(CustomerFieldTypes.PHONE)+"'");
        assertTrue(c.get(CustomerFieldTypes.PHONE).compareTo("7809622003") == 0);
    }
    
}
