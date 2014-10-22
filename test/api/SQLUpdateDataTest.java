
package api;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class SQLUpdateDataTest
{
    
    public SQLUpdateDataTest()
    {
    }

    /**
     * Test of toString method, of class SQLUpdateData.
     */
    @Test
    public void testToString()
    {
        System.out.println("== toString ==");
        SQLUpdateData sqlData = new SQLUpdateData("Comment", SQLData.Type.STRING, "NULL");
        String expResult = "Comment IS NULL";
        String result = sqlData.toString();
        System.out.println("RESULT:" + result);
        assertTrue(result.compareTo(expResult) == 0);
        sqlData = new SQLUpdateData("GateId",SQLData.Type.INT, "29");
        expResult = "GateId=29";
        result = sqlData.toString();
        System.out.println("RESULT:" + result);
        assertTrue(result.compareTo(expResult) == 0);
        sqlData = new SQLUpdateData("Owner",SQLData.Type.STRING, "Andrew");
        expResult = "Owner=\"Andrew\"";
        result = sqlData.toString();
        System.out.println("RESULT:" + result);
        assertTrue(result.compareTo(expResult) == 0);
    }
    
}
