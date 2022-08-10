 
package site.strathcona;

import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.sip.SIPToMeCardCustomer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */


public class STRCustomerGetNormalizerTest
{
    
    public STRCustomerGetNormalizerTest()
    {
    }

    /**
     * Test of setCustomerValuesFromSiteSpecificFields method, of class STRCustomerGetNormalizer.
     */
    @Test
    public void testSetCustomerValuesFromSiteSpecificFields()
    {
        System.out.println("==setCustomerValuesFromSiteSpecificFields==");
        SIPToMeCardCustomer formatter = new SIPToMeCardCustomer();
        
        Customer c = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESHERMAN, WILLIAM TECUMSEH|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 SAVANNAH STREET LETHBRIDGE, AB T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20");
        System.out.println("STREET:'" + c.get(CustomerFieldTypes.STREET)+"'");
        System.out.println("FNAME:'" + c.get(CustomerFieldTypes.FIRSTNAME)+"'");
        System.out.println("LNAME:'" + c.get(CustomerFieldTypes.LASTNAME)+"'");
        System.out.println("PNAME:'" + c.get(CustomerFieldTypes.PREFEREDNAME)+"'");
        
        // Remember when testing to set the environment.properties file library to STR!!
        c = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESHERMAN, WILLIAM TECUMSEH|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 SAVANNAH STREET LETHBRIDGE, AB T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AF#Incorrect password|AY1AZAC20");
        System.out.println("RESERVED:'" + c.get(CustomerFieldTypes.RESERVED)+"'");
        assertTrue(c.get(CustomerFieldTypes.RESERVED).compareTo("Invalid PIN for station user")==0);
        
        // Test STR's use of PE for expiry.
        c = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESHERMAN, WILLIAM TECUMSEH|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 SAVANNAH STREET LETHBRIDGE, AB T1A 3N7|BEanton@shortgrass.ca|BHUSD|PD19520208|PCSGMEDA|PE20150108    235900|PFADULT|PGMALE|DB$0.00|DM$500.00|AF#Incorrect password|AY1AZAC20");
        System.out.println("EXPIRY:'" + c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES)+"'");
        System.out.println("STREET:'" + c.get(CustomerFieldTypes.STREET)+"'");
        assertTrue(c.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).compareTo("20150108")==0);
        
        // Test STR's use of PB for DOB.
        c = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESHERMAN, WILLIAM TECUMSEH|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 SAVANNAH STREET LETHBRIDGE, AB T1A 3N7|BEanton@shortgrass.ca|BHUSD|PB19630822     235900|PD19520208|PCSGMEDA|PE20150108    235900|PGMALE|DB$0.00|DM$500.00|AF#Incorrect password|AY1AZAC20");
        System.out.println("DOB:'" + c.get(CustomerFieldTypes.DOB)+"'");
        assertTrue(c.get(CustomerFieldTypes.DOB).compareTo("19630822")==0);
    }
    
}
