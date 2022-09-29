
package mecard.polaris.sql;

import mecard.polaris.sql.SQLData;
import mecard.polaris.sql.SQLUpdateData;
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
        String expResult = "Comment='NULL'";
        String result = sqlData.toString();
        System.out.println("RESULT:" + result);
        assertTrue(result.compareTo(expResult) == 0);
        sqlData = new SQLUpdateData("GateId",SQLData.Type.INT, "29");
        expResult = "GateId=29";
        result = sqlData.toString();
        System.out.println("RESULT:" + result);
        assertTrue(result.compareTo(expResult) == 0);
        sqlData = new SQLUpdateData("Owner",SQLData.Type.STRING, "Andrew");
        expResult = "Owner='Andrew'";
        result = sqlData.toString();
        System.out.println("RESULT:" + result);
        assertTrue(result.compareTo(expResult) == 0);
    }
    
    @Test
    public void testToQueryString()
    {
        System.out.println("== toQueryString ==");
        SQLUpdateData sqlData = new SQLUpdateData("Polaris.ILS_HashPassword", SQLData.Type.STORED_PROCEEDURE, "S3cr3t");
        String expResult = "{fn Polaris.ILS_HashPassword(?)}";
        String result = sqlData.toQueryString();
        System.out.println("RESULT:" + result);
        assertTrue(result.compareTo(expResult) == 0);
        expResult = "S3cr3t";
        result = sqlData.getValue();
        System.out.println("STORED VALUE:" + result);
        assertTrue(result.compareTo(expResult) == 0);
    }
}
