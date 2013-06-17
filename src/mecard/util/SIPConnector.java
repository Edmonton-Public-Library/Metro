/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Andrew Nisbet
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
package mecard.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.Exception.SIPException;

/**
 * SIP connection. Expects information to be formatted Usage:
 * <code>SIPConnector instance = new SIPConnector.Builder("eplapp","6001").build();</code>
 * <code>assertTrue(instance.test());</code>
 *
 * @author Andrew Nisbet
 */
public class SIPConnector
{
    // TODO: this class could be extended to be a full implementation of a SIP
    // service class with a little work. The class is fine the way it is for Metro
    // however for security reasons.

    public final static char CONNECTION_TERMINATOR = '\r';
    private static final int DEFAULT_TIMEOUT = 5000;
    private static int SEQUENCE_NUMBER = 0;
    private final String host;
    private final int port;
    private final String institutionalId;
    private final String sipUser;
    private final String sipPassword;
    private final int timeout;
    private Socket sipSocket;
    private BufferedReader in;
    private PrintWriter out;

    public static class Builder
    {

        private final int port;
        private final String host;
        private String institution;
        private String user;
        private String password;
        private int timeout;

        /**
         * Creates builder with minimum constructor arguments.
         *
         * @param host
         * @param port
         */
        public Builder(String host, String port)
        {
            this.host = host;
            try
            {
                this.port = Integer.parseInt(port);
            } catch (NumberFormatException ex)
            {
                throw new SIPException(SIPConnector.class.getName()
                        + "the port number cannot be parsed as a number");
            }
            // if not specified the timeout is set to 5000, or 5 seconds. That is
            // about all a customer is willing to wait for on the website.
            this.timeout = SIPConnector.DEFAULT_TIMEOUT;
        }

        /**
         * Sets the socket timeout.
         *
         * @param timeout a number in milliseconds.
         * @return Connection builder
         */
        public Builder timeout(String time)
        {
            try
            {
                this.timeout = Integer.parseInt(time);
            } 
            catch (NumberFormatException ex)
            {
                System.err.println("timeout set to invalid value. Expected long but got '"
                        + time + "'. Default of " + SIPConnector.DEFAULT_TIMEOUT
                        + " applied.");
                this.timeout = SIPConnector.DEFAULT_TIMEOUT;
            }
            return this;
        }

        /**
         * Allows the sip connection to be constructed with an institution id.
         *
         * @param id
         * @return Connection builder
         */
        public Builder institution(String id)
        {
            if (id != null && id.length() > 0)
            {
                this.institution = id;
            }
            return this;
        }

        /**
         * Allows the connection to be constructed with a SIP user.
         *
         * @param user
         * @return Connection Builder
         */
        public Builder sipUser(String user)
        {
            if (user != null && user.length() > 0)
            {
                this.user = user;
            }
            return this;
        }

        /**
         * Allows for the use of a SIP user password if one is set.
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
         * Builds the connection.
         *
         * @return Connection builder.
         */
        public SIPConnector build()
        {
            return new SIPConnector(this);
        }
    }

    /**
     * Creates a SIPConnector but not without the help of a builder object.
     * Usage: SIPConnector c = SIPConnector.Builder(host, port).build();
     *
     * @param builder
     */
    private SIPConnector(Builder builder)
    {
        host = builder.host;
        port = builder.port;
        timeout = builder.timeout;
        institutionalId = builder.institution;
        sipUser = builder.user;
        sipPassword = builder.password;
    }

    /**
     *
     * @return true if the service is up, and patron information is queriable,
     * and false otherwise.
     */
    public boolean test()
    {
        //sent:990   2.00AY1AZFCD8
        //recv:98YYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYYYYYNNYYY|ANSIPCHK|AY1AZE80C
        // Response code:98
        //ACS Status Response
        //  (F) On-line Status:    Y
        //  (F) Checkin OK:        Y
        //  (F) Checkout OK:       Y
        //  (F) ACS Renewal Policy:Y
        //  (F) Status Update OK:  Y
        //  (F) Off-line OK:       N
        //  (F) Timeout Period:    600
        //  (F) Retries Allowed:   003
        //  (F) Date/Time Sync:    20130424    113511
        //  (F) Protocol Version:  2.00
        //  (R) Institution Id:EPLMNA
        //  (O) Library Name:EPLMNA
        //  (R) Supported Messages : YYYYYYYYYYYNNYYY
        //        Patron Status Request : Y
        //        Checkout :              Y
        //        Checkin :               Y
        //        Block Patron :          Y
        //        SC/ACS Status :         Y
        //        Request SC/ASC Resend : Y
        //        Login :                 Y
        //        Patron Information :    Y
        //        End Patron Session :    Y
        //        Fee Paid :              Y
        //        Item Information :      Y
        //        Item Status Update :    N
        //        Patron Enable :         N
        //        Hold :                  Y
        //        Renew :                 Y
        //        Renew All :             Y
        //  (O) Terminal Name:SIPCHK
        //  (R) Sequence Number : 1 :  matches what was sent
        //  (R) Checksum : E80C : Checksum OK
        openConnection();
        String results = sendReceive("990   2.00AY1AZFCD8");
        closeConnection();
        // we check the 64 character, if it isn't that long
        // an exception will be thrown, but the test should 
        // return false without an exception.
        if (results.length() > 64)
        {
            //recv:98YYYYYN60000320130424    1135112.00AOEPLMNA|AMEPLMNA|BXYYYYYYYYYYYNNYYY|ANSIPCHK|AY1AZE80C
            // We need to check that the values at position 2 (online status) 
            // and 56 (Patron Information) are both 'Y'
            if (results.charAt(2) == 'Y' && results.charAt(63) == 'Y') // zero indexed don't forget.
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param request
     * @return SIP response as a String.
     * @throws SIPException
     */
    public String send(String request) throws SIPException
    {
        openConnection();
        // This system's SIP may not require a username password for the command's
        // action. Check if they set it at build time.
        if (sipUser != null)
        {
            if (login() == false)
            {
                throw new SIPException(SIPConnector.class.getName()
                        + " SIP login failed, incorrect user name or password");
            }
        }
        String results = sendReceive(request);
        // We don't logout, we just close the connection.
        closeConnection();
        return results;
    }

    /**
     * Attempts to login with the assigned username and password.
     *
     * @return true if login succeeded.
     */
    private boolean login()
    {
        // 93  CNadmin|COsomepassword|CP|AY2AZF393
        StringBuilder sb = new StringBuilder();
        sb.append("93  CN");
        sb.append(sipUser);
        sb.append("|CO");
        sb.append(sipPassword);
        sb.append("CP|AY");
        sb.append(SIPConnector.getCheckSum(sb.toString()));
        return true;
    }

    public synchronized static int getSequenceNumber()
    {
        int number = SIPConnector.SEQUENCE_NUMBER;
        SIPConnector.SEQUENCE_NUMBER = (SIPConnector.SEQUENCE_NUMBER + 1) % 10;
        return number;
    }

    private String sendReceive(String sipData)
    {
        // sipData should look like: "63                               AO|AA21221012345678|AD64058|AY0AZF374\r"
        if (sipData.charAt(sipData.length() - 1) != SIPConnector.CONNECTION_TERMINATOR)
        {
            sipData += SIPConnector.CONNECTION_TERMINATOR;
        }
        out.print(sipData);
        out.flush();
        String line = null;
        // This loop will exit after no more data arrives from the stream or
        // the socket connection timeout is exceeded.
        while (true)
        {
            try
            {
                line = in.readLine();
            } catch (IOException ex)
            {
                Logger.getLogger(SIPConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (line != null)
            {
                break;
            }
        }
        return line;
    }

    /**
     * Returns the return code from the received message.
     *
     * @param receivedData the String data received from the SIP server.
     * @return the code returned or -1 if the recvData string cannot be parsed,
     * is too short or missing, or cannot be parsed into an integer.
     * @see SIPConnector#getCode(java.lang.String, int)
     */
    public static int getCode(String receivedData)
    {
        return SIPConnector.getCode(receivedData, 2);
    }

    /**
     * Returns the return code from the received message.
     *
     * @param receivedData the String data received from the SIP server.
     * @param codeLength the value of codeLength, usually two digit code.
     * @return the code returned or -1 if the recvData string cannot be parsed,
     * is too short or missing, or cannot be parsed into an integer.
     * @see SIPConnector#getCode(java.lang.String)
     */
    public static int getCode(String receivedData, int codeLength)
    {
        int code = -1;
        try
        {
            code = Integer.parseInt(receivedData.substring(0, codeLength));
        } catch (NumberFormatException ex)
        {
            return code;
        }
        return code;
    }

    /**
     * Manages the opening of the connection.
     */
    private void openConnection() throws SIPException
    {
        try
        {
            sipSocket = new Socket();
            // This limits the time allowed to establish a connection in the case
            // that the connection is refused of server doesn't exist.
            sipSocket.connect(new InetSocketAddress(host, port), timeout);
            // this stops the SIP request from dragging on after connection succeeds.
            sipSocket.setSoTimeout(timeout);
            in = new BufferedReader(new InputStreamReader(sipSocket.getInputStream()));
            out = new PrintWriter(sipSocket.getOutputStream());
        } catch (UnknownHostException ex)
        {
            throw new SIPException(SIPConnector.class.getName()
                    + "the requested host '" + host + "' is unknown");
        } catch (IOException ex)
        {
            throw new SIPException(SIPConnector.class.getName()
                    + "the request for sip connection was refused");
        }
    }

    private void closeConnection()
    {
        try
        {
            in.close();
            out.close();
            sipSocket.close();
        } catch (IOException ex)
        {
            Logger.getLogger(SIPConnector.class.getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    public static String getCheckSum(String data)
    {
        String chksum = "";
        int value = 0;
        int MASK16BITS = 0xffff;
        for (int i = 0; i < data.length(); i++)
        {
            value += (int) data.charAt(i);
        }
        value = ~value;
        value = value & MASK16BITS;
        value = (value + 1);
        value = value & MASK16BITS;
        chksum = String.format("%X", value);
        return chksum;
    }
}
