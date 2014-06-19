
package mecard.customer;

import api.CommandStatus;
import java.io.File;
import mecard.config.CustomerFieldTypes;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */


public class UserLostFileTest
{
    
    public UserLostFileTest()
    {
        
    }

    /**
     * Test of setStatus method, of class UserLostFile.
     */
    @Test
    public void testSetStatus()
    {
        System.out.println("==setStatus==");
        String name = "Doe, John";
        Customer instance = new Customer();
        instance.setName(name);
        instance.set(CustomerFieldTypes.ISLOSTCARD, "Y");
        boolean test = instance.isLostCard();
        assertTrue(instance.isLostCard());
        instance.set(CustomerFieldTypes.ALTERNATE_ID, "22222012345678");
        instance.set(CustomerFieldTypes.ID, "22222012222222");
        UserLostFile userFile = new UserLostFile(instance, "");
        userFile.recordUserDataMessage("LOST CARD detected. This message is read from message.properties normally.");
        File f = new File("22222012222222.lost");
        assertTrue(f.exists());
        f.delete();
    }
    
}
