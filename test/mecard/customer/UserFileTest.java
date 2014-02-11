package mecard.customer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class UserFileTest
{
    private final String testFileName;
    
    public UserFileTest()
    {
        this.testFileName = "userfiletest.txt";
    }

    /**
     * Test of addUserData method, of class UserFile.
     */
    @Test
    public void testAddUserData()
    {
        System.out.println("==create UserFile==");
        List<String> data = new ArrayList<>();
        data.add("*** DOCUMENT BOUNDARY ***\n");
//        data.add("FORM=LDUSER\n");
        data.add(".USER_ID.   |a21221012345678\n");
        data.add(".USER_FIRST_NAME.   |aBalzac\n");
        data.add(".USER_LAST_NAME.   |aBilly\n");
        data.add(".USER_PREFERRED_NAME.   |aBilly, Balzac\n");
        data.add(".USER_LIBRARY.   |aEPLMNA\n");
        data.add(".USER_PROFILE.   |aEPL-ADULT\n");
        data.add(".USER_PREF_LANG.   |aENGLISH\n");
        data.add(".USER_PIN.   |a64058\n");
        data.add(".USER_STATUS.   |aOK\n");
        data.add(".USER_ROUTING_FLAG.   |aY\n");
        data.add(".USER_CHG_HIST_RULE.   |aALLCHARGES\n");
        data.add(".USER_PRIV_GRANTED.   |a20130731\n");
        data.add(".USER_PRIV_EXPIRES.   |a20140602\n");
        data.add(".USER_BIRTH_DATE.   |a19750822\n");
        data.add(".USER_CATEGORY2.   |aF\n");
        data.add(".USER_ACCESS.   |aPUBLIC\n");
        data.add(".USER_ENVIRONMENT.   |aPUBLIC\n");
        data.add(".USER_ADDR1_BEGIN.\n");
        data.add(".STREET.   |a12345 123 St.\n");
        data.add(".CITY/STATE.   |aEdmonton, ALBERTA\n");
        data.add(".POSTALCODE.   |aH0H 0H0\n");
        data.add(".PHONE.   |a780-496-4058\n");
        data.add(".EMAIL.   |ailsteam@epl.ca\n");
        data.add(".USER_ADDR1_END.\n");
        // If this failes delete the file.
        if (new File(testFileName).exists() == false)
        {
            new File(testFileName).delete();
        }
        UserFile userFile = new UserFile(testFileName);
        userFile.addUserData(data);
        File f = new File(this.testFileName);
        assertTrue(f.exists());
        UserFile repeatUserFile = new UserFile(testFileName);
        repeatUserFile.addUserData(data);
        File backup = new File(this.testFileName + ".orig");
        assertFalse(backup.exists());
    }
}