
package site.shortgrass;

import mecard.Response;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import mecard.customer.FlatFormattedCustomer;
import mecard.customer.FormattedCustomer;
import mecard.customer.SIPFormatter;
import org.junit.Test;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */


public class SLSCustomerNormalizerTest
{
    
    public SLSCustomerNormalizerTest()
    {
    }

    /**
     * Test of finalize method, of class SLSCustomerNormalizer.
     */
    @Test
    public void testFinalize()
    {
        System.out.println("==finalize==");
        SIPFormatter formatter = new SIPFormatter();
        
        Customer unformattedCustomer = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20");
        FormattedCustomer formattedCustomer = new FlatFormattedCustomer(unformattedCustomer);
        Response response = new Response();
        SLSCustomerNormalizer instance = new SLSCustomerNormalizer(true);
        instance.finalize(unformattedCustomer, formattedCustomer, response);
        assertTrue(formattedCustomer.containsKey("USER_CATEGORY2"));
        assertTrue(formattedCustomer.containsKey("USER_CATEGORY3"));
        System.out.println("UF_CUSTOMER_SEX: " + unformattedCustomer.get(CustomerFieldTypes.SEX));
        System.out.println("USER_CATEGORY3: " + formattedCustomer.getValue("USER_CATEGORY3"));
        
        unformattedCustomer = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGFEMALE|DB$0.00|DM$500.00|AFOK|AY1AZAC20");
        formattedCustomer = new FlatFormattedCustomer(unformattedCustomer);
        instance.finalize(unformattedCustomer, formattedCustomer, response);
        assertTrue(formattedCustomer.containsKey("USER_CATEGORY2"));
        assertTrue(formattedCustomer.containsKey("USER_CATEGORY3"));
        System.out.println("UF_CUSTOMER_SEX: " + unformattedCustomer.get(CustomerFieldTypes.SEX));
        System.out.println("USER_CATEGORY3: " + formattedCustomer.getValue("USER_CATEGORY3"));
        
        unformattedCustomer = formatter.getCustomer("64              00020140110    161047000000000000000000000000AO|AA25021000719291|AESherman, William Tecumseh|AQSGMED|BZ0100|CA0100|CB0999|BLY|CQY|BV 0.00|BD1864 Savannah Street T1A 3N7|BEanton@shortgrass.ca|BHUSD|PA20150108    235900|PD19520208|PCSGMEDA|PEMEDICINEHA|PFADULT|PGUNKNOWN|DB$0.00|DM$500.00|AFOK|AY1AZAC20");
        formattedCustomer = new FlatFormattedCustomer(unformattedCustomer);
        instance.finalize(unformattedCustomer, formattedCustomer, response);
        assertTrue(formattedCustomer.containsKey("USER_CATEGORY2"));
        assertTrue(formattedCustomer.containsKey("USER_CATEGORY3"));
        System.out.println("UF_CUSTOMER_SEX: " + unformattedCustomer.get(CustomerFieldTypes.SEX));
        System.out.println("USER_CATEGORY3: " + formattedCustomer.getValue("USER_CATEGORY3"));
    }
    
}
