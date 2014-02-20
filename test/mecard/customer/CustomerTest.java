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
        Customer instance = new Customer();
        instance.setName(name);
        instance.set(CustomerFieldTypes.ISLOSTCARD, "Y");
        boolean test = instance.isLostCard();
        assertTrue(test);
        instance.set(CustomerFieldTypes.ISLOSTCARD, "X");
        test = instance.isLostCard();
        assertFalse(test);
    }

    /**
     * Test of isLostCard method, of class Customer.
     */
    @Test
    public void testIsLostCard()
    {
        System.out.println("===isLostCard===");
        String name = "Doe, John";
        Customer instance = new Customer();
        instance.setName(name);
        instance.set(CustomerFieldTypes.ISLOSTCARD, "Y");
        boolean test = instance.isLostCard();
        assertTrue(instance.isLostCard());
        
        instance.set(CustomerFieldTypes.ISLOSTCARD, "X");
        test = instance.isLostCard();
        assertFalse(instance.isLostCard());
        
        assertTrue(instance.isEmpty(CustomerFieldTypes.ISLOSTCARD));
    }

}