
package mecard.config;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public class ILSIT {
    
    public ILSIT() {
    }

    /**
     * Test of toString method, of class ILS.
     */
    @Test
    public void testToString() {
        System.out.println("ILS.toString");
        ILS instance = new ILS();
        // this will depend on what you have set in your environment.properties file.
        String expResult = "BIMPORT";
        String result = instance.toString();
        System.out.println("result: '" + result + "'");
        assertEquals(expResult, result);
    }

    /**
     * Test of getILSType method, of class ILS.
     */
    @Test
    public void testGetILSType() {
        System.out.println("getILSType");
        ILS instance = new ILS();
        ILS.IlsType expResult = ILS.IlsType.HORIZON;
        ILS.IlsType result = instance.getILSType();
        assertEquals(expResult, result);
    }
    
}
