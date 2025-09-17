
package mecard.calgary.cplapi;


import java.util.ArrayList;
import static org.junit.Assert.*;

import java.util.List;
import mecard.config.CustomerFieldTypes;
import mecard.customer.Customer;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class CPLapiToMeCardCustomerTest 
{

    private final String customerJson;
    
    public CPLapiToMeCardCustomerTest() 
    {
        this.customerJson = """
                            {
                                "cardNumber": "21221012345678",
                                "birthDate": "2000-02-29",
                                "firstName": "Balzac",
                                "lastName": "Norton",
                                "expiryDate": "2026-08-22",
                                "profile": "CPL_ADULT",
                                "status": "OK",
                                "address": "1234 37 Avenue",
                                "city": "Calgary",
                                "province": "Alberta",
                                "phoneNumber": "780-333-4444",
                                "emailAddress": "ilsadmins@epl.ca",
                                "gender": "Female",
                                "pin": "123456",
                                "postalCode": "T5J-2V4"
                             }
                            """;
        
    }

    /**
     * Test of getCustomer method, of class CPLapiToMeCardCustomer.
     */
    @Test
    public void testGetCustomer_List() 
    {
        System.out.println("==getCustomer list param==");
        List<String> list = new ArrayList<>();
        list.add("{");
        list.add("    \"cardNumber\": \"21221012345678\",");
        list.add("    \"birthDate\": \"2000-02-29\",");
        list.add("    \"firstName\": \"Balzac\",");
        list.add("    \"lastName\": \"Norton\",");
        list.add("    \"expiryDate\": \"2028-08-22\",");
        list.add("    \"profile\": \"CPL_ADULT\",");
        list.add("    \"status\": \"OK\",");
        list.add("    \"address\": \"1234 37 Avenue\",");
        list.add("    \"city\": \"Calgary\",");
        list.add("    \"province\": \"Alberta\",");
        list.add("    \"phoneNumber\": \"780-333-4444\",");
        list.add("    \"emailAddress\": \"ilsadmins@epl.ca\",");
        list.add("    \"gender\": \"Male\",");
        list.add("    \"pin\": \"123456\",");
        list.add("    \"postalCode\": \"T5J-2V4\"");
        list.add("}");
        CPLapiToMeCardCustomer instance = new CPLapiToMeCardCustomer();
        Customer result = instance.getCustomer(list);
        System.out.println("Customer: "+result.toString());
        assertTrue(result.get(CustomerFieldTypes.PRIVILEGE_EXPIRES).equalsIgnoreCase("20280822"));
    }

    /**
     * Test of getCustomer method, of class CPLapiToMeCardCustomer.
     */
    @Test
    public void testGetCustomer_String() 
    {
        System.out.println("==getCustomer==");
        CPLapiToMeCardCustomer instance = new CPLapiToMeCardCustomer();
        Customer result = instance.getCustomer(this.customerJson);
        System.out.println("Customer: "+result.toString());
        assertTrue(result.get(CustomerFieldTypes.LASTNAME).equalsIgnoreCase("Norton"));
    }
    
}
