
package api;

import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.PolarisSQLPropertyTypes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class SQLConnectorTest
{
    
    public SQLConnectorTest()
    {
    }

    /**
     * Test of getConnection method, of class POLARIS_SQLConnector.
     */
//    @Test
//    public void testGetConnection()
//    {
//        System.out.println("==getConnection==");
//        Properties p = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
//        String host = p.getProperty(PolarisSQLPropertyTypes.HOST.toString());
//        String driver = p.getProperty(PolarisSQLPropertyTypes.CONNECTOR_TYPE.toString());
//        String database = p.getProperty(PolarisSQLPropertyTypes.DATABASE.toString());
//        String user = p.getProperty(PolarisSQLPropertyTypes.USERNAME.toString());
//        String password = p.getProperty(PolarisSQLPropertyTypes.PASSWORD.toString());
//        SQLConnector connector = new SQLConnector.Builder(host, driver, database)
//                .user(user)
//                .password(password)
//                .build();
////        String expResult = "jdbc:mysql://mysql.epl.ca:3306/patroncount";
//        String expResult = "jdbc:sqlserver://10.108.1.71:1433/Polaris";
//        String result = connector.toString();
//        assertTrue(expResult.compareTo(result) == 0);
//        connector.close();
//    }
    
    @Test
    public void testGetSecureConnection()
    {
        System.out.println("==getSecureConnection==");
        Properties p = PropertyReader.getProperties(ConfigFileTypes.POLARIS_SQL);
        String host = p.getProperty(PolarisSQLPropertyTypes.HOST.toString());
        String driver = p.getProperty(PolarisSQLPropertyTypes.CONNECTOR_TYPE.toString());
        String database = p.getProperty(PolarisSQLPropertyTypes.DATABASE.toString());
        String user = p.getProperty(PolarisSQLPropertyTypes.USERNAME.toString());
        String password = p.getProperty(PolarisSQLPropertyTypes.PASSWORD.toString());
        String encrypt = p.getProperty(PolarisSQLPropertyTypes.ENCRYPT.toString());
        String tsc = p.getProperty(PolarisSQLPropertyTypes.TRUST_SERVER_CERTIFICATE.toString());
        String itgs = p.getProperty(PolarisSQLPropertyTypes.INTEGRATED_SECURTIY.toString());
        String ts = p.getProperty(PolarisSQLPropertyTypes.TRUST_STORE.toString());
        String tsPsswd = p.getProperty(PolarisSQLPropertyTypes.TRUST_STORE_PASSWORD.toString());
        String hnic = p.getProperty(PolarisSQLPropertyTypes.HOST_NAME_IN_CERTIFICATE.toString());
        SQLConnector connector = new SQLConnector.Builder(host, driver, database)
                .user(user)
                .password(password)
                .encrypt(encrypt)
                .setTrustServerCertificate(tsc)
                .integratedSecurity(itgs)
                .trustStore(ts)
                .trustStorePassword(tsPsswd)
                .hostNameInCertificate(hnic)
                .build();
        String expResult = "jdbc:sqlserver://10.108.1.71:1433/Polaris;encrypt=true;trustServerCertificate=true;";
        String result = connector.toString();
        assertTrue(expResult.compareTo(result) == 0);
        connector.close();
    }
    
}
