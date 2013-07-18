package mecard.customer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class CustomerTest
{
    
    public CustomerTest()
    {
        
    }

    /**
     * Test of setName method, of class Customer.
     */
    @Test
    public void testSetName()
    {
        System.out.println("== setName ==");
        String name = "name, some";
        Customer instance = new Customer();
        instance.setName(name);
        assertTrue(instance.get(CustomerFieldTypes.FIRSTNAME).compareTo("some") == 0);
        assertTrue(instance.get(CustomerFieldTypes.LASTNAME).compareTo("name") == 0);
        assertTrue(instance.get(CustomerFieldTypes.NAME).compareTo(name) == 0);
    }

    /**
     * Test of set method, of class Customer.
     */
    @Test
    public void testSet()
    {
        System.out.println("== set ==");
        CustomerFieldTypes ft = null;
        String value = "value";
        Customer instance = new Customer();
        
        for (CustomerFieldTypes cTypes: CustomerFieldTypes.values())
        {
            instance.set(cTypes, value);
            if (cTypes != CustomerFieldTypes.NAME)
                assertTrue(instance.get(cTypes).compareTo(value) == 0);
        }
    }

}