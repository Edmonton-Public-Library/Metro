
package api;

import java.io.IOException;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class WebServiceTest {
    
//    public WebServiceTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//    }
//
//    @AfterClass
//    public static void tearDownClass() throws Exception {
//    }
//
//    @Before
//    public void setUp() throws Exception {
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }

    @Test
    public void testSomeMethod() 
    {
        System.out.println("TEST constructor: ");
        try
        {
            WebService ws = new WebService();
        }
        catch (IOException e)
        {
            System.err.println("**IO error, " + e.getLocalizedMessage());
        }
        catch (InterruptedException e)
        {
            System.err.println("**error, interrupted connection: "
            + e.getLocalizedMessage()
            + "\nPossibly caused by a timeout.");
        }
    }
    
}
