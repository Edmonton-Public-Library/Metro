package mecard.customer;
import mecard.customer.horizon.BImportFormattedCustomer;
import java.util.ArrayList;
import java.util.List;
import json.RequestDeserializer;
import mecard.Request;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class BImportFormatterTest
{
    
    public BImportFormatterTest()
    {
    }

    /**
     * Test of getCustomer method, of class BImportFormatter.
     */
    @Test
    public void testGetCustomer_List()
    {
        System.out.println("==getCustomer==");
        String custReq =
                "{\"code\":\"GET_CUSTOMER\",\"authorityToken\":\"12345678\",\"userId\":\"21221012345678\",\"pin\":\"64058\",\"customer\":\"{\\\"ID\\\":\\\"21221012345678\\\",\\\"PIN\\\":\\\"64058\\\",\\\"PREFEREDNAME\\\":\\\"Billy, Balzac\\\",\\\"STREET\\\":\\\"12345 123 St.\\\",\\\"CITY\\\":\\\"Edmonton\\\",\\\"PROVINCE\\\":\\\"Alberta\\\",\\\"POSTALCODE\\\":\\\"H0H0H0\\\",\\\"SEX\\\":\\\"M\\\",\\\"EMAIL\\\":\\\"ilsteam@epl.ca\\\",\\\"PHONE\\\":\\\"7804964058\\\",\\\"DOB\\\":\\\"19750822\\\",\\\"PRIVILEGE_EXPIRES\\\":\\\"20140602\\\",\\\"RESERVED\\\":\\\"X\\\",\\\"ALTERNATE_ID\\\":\\\"X\\\",\\\"ISVALID\\\":\\\"Y\\\",\\\"ISMINAGE\\\":\\\"Y\\\",\\\"ISRECIPROCAL\\\":\\\"N\\\",\\\"ISRESIDENT\\\":\\\"Y\\\",\\\"ISGOODSTANDING\\\":\\\"Y\\\",\\\"ISLOSTCARD\\\":\\\"N\\\",\\\"FIRSTNAME\\\":\\\"Balzac\\\",\\\"LASTNAME\\\":\\\"Billy\\\"}\"}";
        RequestDeserializer deserializer = new RequestDeserializer();
        Request request = deserializer.getDeserializedRequest(custReq);
        Customer customer = request.getCustomer();
        BImportFormattedCustomer formatter = new BImportFormattedCustomer(customer);
        List<String> expResult = new ArrayList<>();
        expResult.add("x- borrower: second_id; name; expiration_date; birth_date; pin\r\n");
        expResult.add("borrower_phone: phone_type; phone_no\r\n");
        expResult.add("borrower_address: address1; address2; city_st; postal_code; email_name; email_address; send_preoverdue\r\n");
        expResult.add("borrower_barcode: bbarcode\r\n");

        List<String> result = formatter.getFormattedCustomer();
        for (String s: result)
        {
            System.out.print("=>"+s);
        }
        
        System.out.println();
        expResult.clear();
        expResult.add("M- borrower: 21221012345678; Balzac, Billy; 06-02-2014; 08-22-1975; 64058\r\n");
        expResult.add("borrower_phone: h-noTC; 7804964058\r\n");
        expResult.add("borrower_address: 12345 123 St.; ; edmonton; H0H 0H0; ilsteam; ilsteam@epl.ca; 1\r\n");
        expResult.add("borrower_barcode: 21221012345678\r\n");
        result = formatter.getFormattedHeader();
        for (String s: result)
        {
            System.out.print("=>"+s);
        }
        System.out.println();
        assertEquals(expResult, result);
    }
}