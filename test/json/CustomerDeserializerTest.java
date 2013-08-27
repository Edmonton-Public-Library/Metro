package json;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.io.StringReader;
import mecard.customer.Customer;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class CustomerDeserializerTest
{
    private final String testCustomer;
    
    public CustomerDeserializerTest()
    {
        this.testCustomer = "{\"ID\":\"X\",\"PIN\":\"X\",\"PREFEREDNAME\":\"X\",\"STREET\":\"X\",\"CITY\":\"X\",\"PROVINCE\":\"X\",\"POSTALCODE\":\"X\",\"SEX\":\"X\",\"EMAIL\":\"X\",\"PHONE\":\"X\",\"DOB\":\"X\",\"PRIVILEGE_EXPIRES\":\"X\",\"RESERVED\":\"X\",\"ALTERNATE_ID\":\"X\",\"ISVALID\":\"X\",\"ISMINAGE\":\"X\",\"ISRECIPROCAL\":\"X\",\"ISRESIDENT\":\"X\",\"ISGOODSTANDING\":\"X\",\"ISLOSTCARD\":\"X\",\"FIRSTNAME\":\"X\",\"LASTNAME\":\"X\"}";
    }

    /**
     * Test of deserialize method, of class CustomerDeserializer.
     */
    @Test
    public void testDeserialize()
    {
        System.out.println("deserialize");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Customer.class, new CustomerDeserializer());
        Gson gson = gsonBuilder.create();
        Reader data = new StringReader(testCustomer);
        Customer c = gson.fromJson(data, Customer.class);
        System.out.println("..."+c); // which should look the same since toString serializes a JSON too.
        assertTrue(c.toString().compareToIgnoreCase("[X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X]") == 0);
    }
}