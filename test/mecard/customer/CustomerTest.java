package mecard.customer;
import mecard.config.CustomerFieldTypes;
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
    }

    /**
     * Test of toString method, of class Customer.
     */
    @Test
    public void testToString()
    {
        System.out.println("==toString==");
        Customer instance = new Customer();
        String expResult = "[X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X, X]";
        String result = instance.toString();
        assertTrue(expResult.compareTo(result) == 0);
    }

    /**
     * Test of isEmpty method, of class Customer.
     */
    @Test
    public void testIsEmpty()
    {
        System.out.println("==isEmpty==");
        String name = "Doe, John";
        Customer customer = new Customer();
        customer.setName(name);
        customer.set(CustomerFieldTypes.ISLOSTCARD, "Y");
        assertTrue(customer.isLostCard());
        customer.set(CustomerFieldTypes.ISLOSTCARD, "X");
        assertFalse(customer.isLostCard());
    }

    /**
     * Test of isLostCard method, of class Customer.
     */
    @Test
    public void testIsLostCard()
    {
        System.out.println("===isLostCard===");
        String name = "Doe, John";
        Customer customer = new Customer();
        customer.setName(name);
        boolean test = customer.isLostCard();
        assertFalse(customer.isLostCard());
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISGOODSTANDING));
        customer.set(CustomerFieldTypes.ISLOSTCARD, "N");
        assertTrue(customer.isFlagDefined(CustomerFieldTypes.ISLOSTCARD));
        assertTrue(customer.isFlagSetFalse(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetTrue(CustomerFieldTypes.ISLOSTCARD));
        test = customer.isLostCard();
        assertFalse(customer.isLostCard());
        customer.set(CustomerFieldTypes.ISLOSTCARD, "Y");
        assertTrue(customer.isFlagDefined(CustomerFieldTypes.ISLOSTCARD));
        assertTrue(customer.isFlagSetTrue(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetFalse(CustomerFieldTypes.ISLOSTCARD));
        test = customer.isLostCard();
        assertTrue(customer.isLostCard());
        
        customer.set(CustomerFieldTypes.ISLOSTCARD, "X");
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetFalse(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagSetTrue(CustomerFieldTypes.ISLOSTCARD));
        test = customer.isLostCard();
        assertFalse(customer.isLostCard());
        
        assertTrue(customer.isEmpty(CustomerFieldTypes.ISLOSTCARD));
        assertFalse(customer.isFlagDefined(CustomerFieldTypes.ISGOODSTANDING));
    }
}