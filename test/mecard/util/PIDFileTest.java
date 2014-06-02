 
package mecard.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class PIDFileTest
{
    
    public PIDFileTest()
    {
        
    }

    /**
     * Test of exists method, of class PIDFile.
     */
    @Test
    public void testExists()
    {
        System.out.println("===exists===");
        PIDFile pid = new PIDFile("deleteME.txt");
        assertFalse(pid.exists());
        System.out.println(">>>"+pid.getAbsolutePath());
        // this value on my Windows machine, obviously different on any other.
        assertTrue(pid.getAbsolutePath().compareTo("C:\\Users\\ANisbet\\Dropbox\\development\\MeCard\\deleteME.txt")==0);
        assertFalse(pid.exists());
    }

    /**
     * Test of touch method, of class PIDFile.
     */
    @Test
    public void testTouch()
    {
        System.out.println("==touch==");
        PIDFile pid = new PIDFile("deleteME.txt");
        assertFalse(pid.exists());
        pid.touch();
        assertTrue(pid.exists());
        pid.delete();
        assertFalse(pid.exists());
    }

    /**
     * Test of lastModified method, of class PIDFile.
     */
    @Test
    public void testLastModified()
    {
        System.out.println("==lastModified==");
        PIDFile pid = new PIDFile("deleteME.txt");
        assertFalse(pid.exists());
        pid.touch();
        assertTrue(pid.exists());
        System.out.println("LASTMODIFIED:"+pid.lastModified());
        pid.delete();
        assertFalse(pid.exists());
    }

    /**
     * Test of delete method, of class PIDFile.
     */
    @Test
    public void testDelete()
    {
        System.out.println("==delete==");
        PIDFile pid = new PIDFile("deleteME.txt");
        assertFalse(pid.exists());
        pid.touch();
        assertTrue(pid.exists());
        pid.delete();
        assertFalse(pid.exists());
    }

    /**
     * Test of getAbsolutePath method, of class PIDFile.
     */
    @Test
    public void testGetAbsolutePath()
    {
        System.out.println("==getAbsolutePath==");
        PIDFile pid = new PIDFile("deleteME.txt");
        System.out.println(">>>"+pid.getAbsolutePath());
        // this value on my Windows machine, obviously different on any other.
        assertTrue(pid.getAbsolutePath().compareTo("C:\\Users\\ANisbet\\Dropbox\\development\\MeCard\\deleteME.txt")==0);
    }
    
}
