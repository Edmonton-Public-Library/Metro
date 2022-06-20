/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2019  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 */
package api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import mecard.exception.ConfigurationException;

/**
 * Builds the SQL connection based builder parameters.
 * @author Andrew Nisbet andrew.nisbet@epl.ca
 */
public class SQLConnector
{
    private final String host;
    private final int    port;
    private final String sqlUser;
    private final String sqlPassword;
    private final String sqlDatabase;
    private final String driverName;
    private final String urlProtocol;
    private Connection connection = null;
    private final String url;
    private DriverType driverType;
    private final String encrypt;
    private final String trustServerCertificate;
    private final String trustStore;
    private final String integratedSecurity;
    private final String trustStorePassword;
    private final String hostNameInCertificate;

    public static class Builder
    {
        private String host;
        private String database;
        private String user;
        private String password;
        private String driver;
        private String encrypt; // mssql server 2019.
        private String trustServerCertificate; // mssql server 2019.
        private String trustStore;
        private String integratedSecurity;
        private String trustStorePassword;
        private String hostNameInCertificate;

        /**
         * Creates builder with minimum constructor arguments.
         *
         * @param host host name of the database.
         * @param driver enum of the driver type. Only MySQL and SQL Server are supported.
         * @param databaseName name of the database to connect with.
         */
        public Builder(String host, String driver, String databaseName)
        {
            this.host     = host;
            this.driver   = driver;
            this.database = databaseName;
            this.encrypt  = "";
            this.trustServerCertificate = "";
            this.trustStore = "";
            this.integratedSecurity = "";
            this.trustStorePassword = "";
            this.hostNameInCertificate = "";
        }

        /**
         * Allows the connection to be constructed with a SQL user.
         *
         * @param user
         * @return Connection Builder
         */
        public Builder user(String user)
        {
            if (user != null && user.length() > 0)
            {
                this.user = user;
            }
            return this;
        }

        /**
         * Allows for the use of a SQL user password if one is set.
         *
         * @param password
         * @return Builder
         */
        public Builder password(String password)
        {
            if (password != null && password.length() > 0)
            {
                this.password = password;
            }
            return this;
        }
        
        /**
         * Allows the setting of encryption over a secure socket layer
         * (Windows mssql server).
         * This is 'true' by default so passing 'false' will turn off this 
         * feature in the URL string.
         *
         * @param encrypt - 'true' or 'false'.
         * @return Builder
         */
        public Builder encrypt(String encrypt)
        {
            this.encrypt = encrypt.toLowerCase();
            return this;
        }
        
        /**
         * Sets switch if the trust server certificate should be used.
         * (Windows mssql server).
         * This is 'true' by default so passing 'false' will turn off this 
         * feature in the URL string.
         *
         * @param useTSC - 'true' or 'false'.
         * @return Builder
         */
        public Builder setTrustServerCertificate(String useTSC)
        {
            this.trustServerCertificate = useTSC.toLowerCase();
            return this;
        }
        
        /**
         * Allows the setting of trust store name.
         * (Windows mssql server).
         *
         * @param ts - trust store name.
         * @return Builder
         */
        public Builder trustStore(String ts)
        {
            this.trustStore = ts;
            return this;
        }
        
        /**
         * Allows the setting of integrated security over a secure socket layer
         * (Windows mssql server).
         * This is 'true' by default so passing 'false' will turn off this 
         * feature in the URL string.
         *
         * @param integratedSecurity - 'true' or 'false'.
         * @return Builder
         */
        public Builder integratedSecurity(String integratedSecurity)
        {
            this.integratedSecurity = integratedSecurity;
            return this;
        }
        
        /**
         * Allows the setting of the trust store password
         * (Windows mssql server).
         *
         * @param trustStorePassword - Password for the trust store.
         * @return Builder
         */
        public Builder trustStorePassword(String trustStorePassword)
        {
            this.trustStorePassword = trustStorePassword;
            return this;
        }
        
        /**
         * Allows the setting of the host name in the certificate.
         * (Windows mssql server).
         * This is 'true' by default so passing 'false' will turn off this 
         * feature in the URL string.
         *
         * @param hostNameInCertificate - Name of the host name in the cert.
         * @return Builder
         */
        public Builder hostNameInCertificate(String hostNameInCertificate)
        {
            this.hostNameInCertificate = hostNameInCertificate;
            return this;
        }

        /**
         * Builds the connection.
         *
         * @return Connection builder.
         */
        public SQLConnector build()
        {
            return new SQLConnector(this);
        }
    }
    
    public enum DriverType
    {
        MY_SQL,
        SQL_SERVER;
    }

    /**
     * Creates a SIPConnector but not without the help of a builder object.
     * Usage: SIPConnector c = SIPConnector.Builder(host, port).build();
     *
     * @param builder
     */
    private SQLConnector(Builder builder)
    {
        this.host        = builder.host;
        this.sqlDatabase = builder.database;
        this.sqlUser     = builder.user;
        this.sqlPassword = builder.password;
        this.encrypt= builder.encrypt;
        this.trustServerCertificate = builder.trustServerCertificate;
        this.trustStore  = builder.trustStore;
        this.integratedSecurity = builder.integratedSecurity;
        this.trustStorePassword = builder.trustStorePassword;
        this.hostNameInCertificate = builder.hostNameInCertificate;
        switch (builder.driver)
        {
            case "MY_SQL":     // mysql-connector-java-5.1.31-bin.jar
                this.driverName  = "com.mysql.jdbc.Driver";
                this.urlProtocol = "jdbc:mysql://";
                this.port        = 3306;
                this.url         = getMySQL_URL();
                this.driverType  = DriverType.MY_SQL;
                break;
            case "SQL_SERVER": // sqljdbc42.jar for Java 8 compatability, tested.
                this.driverName  = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                this.urlProtocol = "jdbc:sqlserver://";
                this.port        = 1433;
                this.url         = getSQLServer_URL();
                this.driverType  = DriverType.SQL_SERVER;
                break;
            default:
                throw new UnsupportedOperationException(
                        SQLConnector.class.getName() 
                        + " unregistered JDBC driver type. (1024)"); // conspicuous easy to find message.
        }
        System.out.println("==connection layer");
        System.out.println("driverName: "+driverName);
        System.out.println("port: "+port);
        System.out.println("url: "+url);
        System.out.println("driverType: "+driverType);
        System.out.println("==DB layer");
        System.out.println("host: "+host);
        System.out.println("sqlDatabase: "+sqlDatabase);
        System.out.println("sqlUser: "+sqlUser);
        System.out.println("sqlPassword: ");
        for (String c: sqlPassword.split(""))
        {
            System.out.print("*");
//            System.out.print(c);
        }
        switch (builder.driver)
        {
            case "MY_SQL":
                break;
            case "SQL_SERVER":
                if (! this.encrypt.isEmpty())
                {
                    System.out.println("encrypt: "+encrypt);
                }
                if (! this.trustServerCertificate.isEmpty())
                {
                    System.out.println("trustServerCertificate: "+trustServerCertificate);
                }
                if (! this.integratedSecurity.isEmpty())
                {
                    System.out.println("integratedSecurity: "+integratedSecurity);
                }
                if (! this.trustStore.isEmpty())
                {
                    System.out.println("trustStore: "+trustStore);
                }
                if (! this.trustStorePassword.isEmpty())
                {
                    System.out.println("trustStorePassword: ***********");
                }
                if (! this.hostNameInCertificate.isEmpty())
                {
                    System.out.println("hostNameInCertificate: "+hostNameInCertificate);
                }
                break;
            default:
                break;
        }                
        System.out.println();

        try
        {
            Class.forName(this.driverName);
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("JDBC Driver not found: " +
                    this.driverName + "::" + ex.getMessage());
            throw new ConfigurationException("sql driver error");
        }
        try
        {
            // build the connection for MySQL
            this.connection = DriverManager.getConnection(
                    this.url, this.sqlUser, this.sqlPassword);
        }
        catch (SQLException ex)
        {
            System.err.println("=======> SQL Connection failed: '" + ex.getMessage() + "'");
            throw new ConfigurationException("sql driver error" + ex.getMessage());
        }
        
        if (connection == null)
        {
            System.out.println("JDBC Failed to make connection.");
            throw new ConfigurationException("sql driver error: null connection.");
        }
        
        System.out.println("SQL connection succeeded");       
    }
    
    private String getMySQL_URL()
    {
        StringBuilder connectionURL = new StringBuilder();
        connectionURL.append(this.urlProtocol);
        connectionURL.append(this.host);
        connectionURL.append(":");
        connectionURL.append(this.port);
        connectionURL.append("/");
        connectionURL.append(this.sqlDatabase);
        return connectionURL.toString();
    }
    
    private String getSQLServer_URL()
    {
        StringBuilder connectionURL = new StringBuilder();
        connectionURL.append(this.urlProtocol);
        connectionURL.append(this.host);
        connectionURL.append(":");
        connectionURL.append(String.valueOf(this.port));
        connectionURL.append(";");
        connectionURL.append("databaseName=");
        connectionURL.append(this.sqlDatabase);
        connectionURL.append(";");
        // For Windows server 2019 the URL possibly should be set as so:
        // url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;
        // From: https://stackoverflow.com/questions/32766114/sql-server-jdbc-error-on-java-8-the-driver-could-not-establish-a-secure-connect
        // See here for encryption and connection information:
        // https://docs.microsoft.com/en-us/sql/connect/jdbc/connecting-with-ssl-encryption?view=sql-server-ver15#configuring-the-connection
        // Config client for encryption: https://docs.microsoft.com/en-us/sql/connect/jdbc/configuring-the-client-for-ssl-encryption?view=sql-server-ver15
        // Encryption support: https://docs.microsoft.com/en-us/sql/connect/jdbc/understanding-ssl-support?view=sql-server-ver15
        // "encrypt=true"
        if (! this.encrypt.isEmpty())
        {
            connectionURL.append("encrypt=");
            connectionURL.append(this.encrypt);
            connectionURL.append(";");
        }
        // "trustServerCertificate=true;"
        if (! this.trustServerCertificate.isEmpty())
        {
            connectionURL.append("trustServerCertificate=");
            connectionURL.append(this.trustServerCertificate);
            connectionURL.append(";");
        }
//        "integratedSecurity=true;"
        if (! this.integratedSecurity.isEmpty())
        {
            connectionURL.append("integratedSecurity=");
            connectionURL.append(this.integratedSecurity);
            connectionURL.append(";");
        }
//        "trustStore=storeName;"
        if (! this.trustStore.isEmpty())
        {
            connectionURL.append("trustStore=");
            connectionURL.append(this.trustStore);
            connectionURL.append(";");
        }
//        "trustStorePassword=storePassword;"
        if (! this.trustStorePassword.isEmpty())
        {
            connectionURL.append("trustStorePassword=");
            connectionURL.append(this.trustStorePassword);
            connectionURL.append(";");
        }
//        "hostNameInCertificate=hostName"
        if (! this.hostNameInCertificate.isEmpty())
        {
            connectionURL.append("hostNameInCertificate=");
            connectionURL.append(this.hostNameInCertificate);
            connectionURL.append(";");
        }
        System.out.println(">>>>"+connectionURL.toString()+"<<<<");
        return connectionURL.toString();
    }
    
    public DriverType getDriverType()
    {
        return this.driverType;
    }
    
    /**
     * Returns Connection to create SQL statements.
     * @return 
     */
    public Connection getConnection()
    {
        return this.connection;
    }
    
    /**
     * Closes the JDBC connection.
     */
    public void close()
    {
        if (connection != null)
        {
            try
            {
                connection.close();
            } catch (SQLException ex)
            {
                System.out.println("**error failed to close connection.");
                throw new ConfigurationException("sql driver error");
            }
        }
    }
    
    @Override
    public String toString()
    {
        return this.url;
    }
}
