package mecard.util;
import mecard.Protocol;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class AlbertaCityTest
{
    
    public AlbertaCityTest()
    {
    }

    /**
     * Test of getCityCode method, of class AlbertaCity.
     */
    @Test
    public void testGetCityCode()
    {
        System.out.println("==getCityCode==");
        String ciyName = "Czar";
        City instance = AlbertaCity.getInstanceOf();
        String expResult = "0081";
        String result = instance.getCityCode(ciyName);
        assertTrue(expResult.compareTo(result) == 0);
        
        ciyName = "Heisler";
        instance = AlbertaCity.getInstanceOf();
        expResult = "0145";
        result = instance.getCityCode(ciyName);
        assertTrue(expResult.compareTo(result) == 0);
        
        result = instance.getCityCode("fdsfsd");
        assertTrue(result.compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0);
        
        // Test that the map was updated
        result = instance.getCityCode("Edmonton");
        assertTrue(result.compareTo("ed") == 0);
        
        assertTrue(instance.isPlaceName("Sherwood Park"));
        assertFalse(instance.isPlaceName("Mars"));
        
        System.out.println("NAME_GUESS:"+instance.getPlaceNameLike("Park"));
        assertTrue("Sherwood Park".compareTo(instance.getPlaceNameLike("Park")) == 0);

        System.out.println("NAME_GUESS:"+instance.getPlaceNameLike("Estates"));
        
    }
}