package mecard.customer;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class FlatUserTest
{
    
    public FlatUserTest()
    {
    }

    /**
     * Test of add method, of class FlatUser.
     */
    @Test
    public void testAdd_FlatUserFieldTypes_String()
    {
        System.out.println("==add==");
        FlatUser instance = new FlatUser();
        instance.add(FlatUserFieldTypes.USER_ID, "21221012345678");
        assertTrue(instance.getField(FlatUserFieldTypes.USER_ID).equals("21221012345678"));
        System.out.println(instance);
    }

    /**
     * Test of add method, of class FlatUser.
     */
    @Test
    public void testToList()
    {
        System.out.println("==testToList==");
        FlatUser flatUser = new FlatUser();
        flatUser.add(FlatUserFieldTypes.USER_ID, "21221012345678");
        assertTrue(flatUser.getField(FlatUserFieldTypes.USER_ID).equals("21221012345678"));
        flatUser.add(FlatUserExtendedFields.USER_ADDR1, FlatUserFieldTypes.CITY_STATE, "Edmonton, AB");
        assertTrue(flatUser.getField(FlatUserFieldTypes.CITY_STATE).equals("Edmonton, AB"));
        flatUser.setDefaultProperties();
        List<String> flatUserData = flatUser.toList();
        for (String line: flatUserData)
        {
            System.out.print(line);
        }
        System.out.println(flatUser);
    }

    /**
     * Test of getField method, of class FlatUser.
     */
    @Test
    public void testGetField()
    {
        System.out.println("==getField==");
        FlatUser instance = new FlatUser();
        instance.add(FlatUserFieldTypes.USER_ID, "21221012345678");
        assertTrue(instance.getField(FlatUserFieldTypes.USER_ID).equals("21221012345678"));
        assertTrue(instance.getField(FlatUserFieldTypes.PHONE).equals(""));
    }


    /**
     * Test of setDefaultProperties method, of class FlatUser.
     */
    @Test
    public void testSetDefaultProperties()
    {
        System.out.println("==setDefaultProperties==");
        FlatUser instance = new FlatUser();
        instance.setDefaultProperties();
        assertTrue(instance.getField(FlatUserFieldTypes.USER_LIBRARY).equals("EPLMNA"));
        assertTrue(instance.getField(FlatUserFieldTypes.USER_STATUS).equals("OK"));
        assertTrue(instance.getField(FlatUserFieldTypes.USER_ACCESS).equals("PUBLIC"));
        System.out.println(instance);
    }
    
    /**
     * Test of setDefaultProperties method, of class FlatUser.
     */
    @Test
    public void testSetAddExtended()
    {
        System.out.println("==setAddExtended==");
        FlatUser flatUser = new FlatUser();
        flatUser.setDefaultProperties();
        flatUser.add(FlatUserExtendedFields.USER_ADDR1, FlatUserFieldTypes.STREET, "11811 74 Ave.");
        assertTrue(flatUser.getField(FlatUserFieldTypes.STREET).equals("11811 74 Ave."));
        flatUser.add(FlatUserExtendedFields.USER_ADDR2, FlatUserFieldTypes.STREET, "10111 Jasper ave.");
        assertTrue(flatUser.getField(FlatUserFieldTypes.STREET).equals("11811 74 Ave."));
        flatUser.add(FlatUserExtendedFields.USER_XINFO, FlatUserFieldTypes.NOTE, "Don't lend books to this guy!");
        assertTrue(flatUser.getField(FlatUserFieldTypes.NOTE).equals("Don't lend books to this guy!"));
        flatUser.add(FlatUserExtendedFields.USER_ADDR1, FlatUserFieldTypes.CITY_STATE, "Edmonton, AB");
        assertTrue(flatUser.getField(FlatUserFieldTypes.CITY_STATE).equals("Edmonton, AB"));
        System.out.println(flatUser);
    }
}