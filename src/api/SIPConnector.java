/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.exception.SIPException;

/**
 * This class manages all the connections required to communicate with a SIP2 service
 * including opening closing sockets and placing data on the wire. 
 * Expects information to be formatted Usage:
 * <code>SIPConnector instance = new SIPConnector.Builder("eplapp","6001").build();</code>
 * <code>assertTrue(instance.test());</code>
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SIPConnector
{
    // This class could be extended to be a full implementation of a SIP
    // service class with a little work. The class is fine the way it is for Metro however.
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
    private final String locationCode;
    private final boolean debug;
    private boolean turnOnShowConfigSettingsOnDebug; // used to show config settings one time only.
    private final int MAX_RESENDS = 2;
    private int resendAttempts;

    public static class Builder
    {
        private final int port;
        private final String host;
        private String institution;
        private String user;
        private String password;
        private int timeout;
        private String locationCode;
        private boolean debug;
        private boolean turnOnShowConfigSettingsOnDebug;
        
        /**
         * Creates builder with minimum constructor arguments.
         *
         * @param host
         * @param port
         */
        public Builder(String host, String port)
        {
            this.host = host;
            this.locationCode = "";
            try
            {
                this.port = Integer.parseInt(port);
            } 
            catch (NumberFormatException ex)
            {
                if (debug)System.out.println("DEBUG error: "+ ex.getMessage());
                throw new SIPException(SIPConnector.class.getName()
                        + "the port number cannot be parsed as a number");
            }
            // if not specified the timeout is set to 5000, or 5 seconds. That is
            // about all a customer is willing to wait for on the website.
            this.timeout = SIPConnector.DEFAULT_TIMEOUT;
            this.debug = false;
            this.turnOnShowConfigSettingsOnDebug = false;
            this.user = "";
        }

        /**
         * Sets the socket timeout.
         *
         * @param time a number in milliseconds.
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
                if (debug)System.out.println("DEBUG error: "+ ex.getMessage());
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
            if (id != null)
            {
                this.institution = id;
            }
            return this;
        }
        
        public Builder debug(boolean b)
        {
            this.debug = b;
            this.turnOnShowConfigSettingsOnDebug = b;
            return this;
        }
        
        /**
         * Sets the location code (field CP) of a sip request.
         * @param location
         * @return 
         */
        public Builder locationCode(String location)
        {
            if (location != null)
            {
                this.locationCode = location;
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
            if (user != null)
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
            if (password != null)
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
        this.host = builder.host;
        this.port = builder.port;
        this.timeout = builder.timeout;
        this.institutionalId = builder.institution;
        this.sipUser = builder.user;
        this.sipPassword = builder.password;
        this.locationCode = builder.locationCode;
        this.debug = builder.debug;
        this.turnOnShowConfigSettingsOnDebug = builder.turnOnShowConfigSettingsOnDebug;
        this.resendAttempts = 0;
    }
    
    /**
     * The institutional ID is part of the get customer info request '63'. The 
     * SIPConnector is told what the institutional ID is during configuration
     * so the {@link SIPCommand} object needs to know what this value is.
     * @return institutional id if set and an empty string if the value is unset 
     * in configuration.  
     */
    String getInstitutionalID()
    {
        return this.institutionalId;
    }

    /**
     *
     * @return true if the service is up, and patron information is queryable,
     * and false otherwise.
     */
    public boolean test()
    {
        //sent:990   2.00AY1AZFCD8
        //recv:98YYYYYN60000320130424    1135112.00AOInstiturional_id|AMEPLMNA|BXYYYYYYYYYYYNNYYY|ANSIPCHK|AY1AZE80C
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
        String testMessage = "990   2.00AY1AZ";
        this.resendAttempts = 0;
        String results = sendReceive(testMessage + SIPConnector.getCheckSum(testMessage));
        closeConnection();
        SIPStatusMessage m = new SIPStatusMessage(results);
        // On-line Status:    Y
        return m.isOnline();
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
        if (sipUser.isEmpty() == false)
        {
            if (login() == false)
            {
                if (debug) System.out.println("   DEBUG error: login failed, check your settings in sip2.properties.");
                throw new SIPException(SIPConnector.class.getName()
                        + " SIP login failed, incorrect user name or password");
            }
        }
        this.resendAttempts = 0;
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
//        sent:93  CNuserid|COpassword|CP|AY1AZF5D2
//        recv:941AY1AZFDFC
//
//        Response code:94
//        Login Response
//          (F) OK:1
        StringBuilder sb = new StringBuilder();
        sb.append("93  CN");
        sb.append(sipUser);
        sb.append("|CO");
        sb.append(sipPassword);
        // by default the CP field must be in request
        // by default the connector object is built with an empty string.
        sb.append("|CP");
        sb.append(locationCode);
        sb.append("|AY");
        sb.append(SIPConnector.getCheckSum(sb.toString()));
        // Now do the actual login:
        this.resendAttempts = 0;
        String results = sendReceive(sb.toString());
        SIPMessage m = new SIPMessage(results);
        if (m.getCodeMessage().compareTo("1") != 0)
        {
            // Here it seems that the simulator sends an additional checksum like this:
            //Enter Message to Send:ff
            //Preparing to send Free Form Message
            //  Enter command up to sequence number:
            //93  CNSIPCHK51|COSELFC123|CP|AYF765
            //sent:93  CNSIPCHK51|COSELFC123|CP|AYF765AY0AZF518
            //                                        ^       ^ - additional checksum and sequence number.
            //recv:941AY0AZFDFD
//            sb.append("AY").append(getSequenceNumber()).append("AZ").append(SIPConnector.getCheckSum(sb.toString()));
            sb.append("AY0AZ").append(SIPConnector.getCheckSum(sb.toString()));
            results = sendReceive(sb.toString());
            m = new SIPMessage(results);
            if (m.getCodeMessage().compareTo("1") != 0)
            {
                return false;
            }
        }
        return true;
    }

    public static int getSequenceNumber()
    {
        int number = SIPConnector.SEQUENCE_NUMBER;
        SIPConnector.SEQUENCE_NUMBER = (SIPConnector.SEQUENCE_NUMBER + 1) % 10;
        return number;
    }

    private String sendReceive(String sipData)
    {
        if (debug && turnOnShowConfigSettingsOnDebug)
        {
            System.out.println("Your settings in sip2.properties:");
            System.out.println("          host: '" + this.host + "'");
            System.out.println("          user: '" + this.sipUser + "'");
            System.out.println("      password: '*******'");
            System.out.println("Institution id: '" + this.institutionalId + "'");
            System.out.println(" location code: '" + this.locationCode + "'");
            this.turnOnShowConfigSettingsOnDebug = false;
        }
        if (debug)
        {
            System.out.println("DEBUG send: -> '" + sipData + "'");
        }
        // sipData should look like: "63                               AO|AA21221012345678|AD64058|AY0AZF374\r"
        if (sipData.charAt(sipData.length() - 1) != SIPConnector.CONNECTION_TERMINATOR)
        {
            sipData += SIPConnector.CONNECTION_TERMINATOR;
        }
        String line = null;
        out.print(sipData);
        out.flush();
        // This loop will exit after no more data arrives from the stream or
        // the socket connection timeout is exceeded.
        while (true)
        {
            try
            {
                line = in.readLine();
            } 
            catch (IOException ex)
            {
                if (debug)System.out.println("DEBUG error: "+ ex.getMessage());
                Logger.getLogger(SIPConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (line != null)
            {
                break;
            }
        }
        if (debug)
        {
            System.out.println("DEBUG recv: <- '" + line + "'");
        }
        /** Request ACS Resend This message requests the ACS to re-transmit its 
         * last message. It is sent by the SC to the ACS when the checksum in 
         * the received message does not match the value calculated by the SC. 
         * The ACS should respond by re-transmitting its last message.
         * This message should never include a "sequence number"
         * field, even when error detection is enabled. (see "Checksums and Sequence Numbers" below)
         * but would include a "checksum" field since checksums are in use.
         * 96
        */
        SIPMessage sipMessage = new SIPMessage(line);
        if (sipMessage.isResendRequest())
        {
            if (this.resendAttempts >= MAX_RESENDS)
            {
                System.out.println(SIPConnector.class.getName()
                        + "**Error: resend failed " + this.resendAttempts + " times.");
                return line; // send back what we got, what ever sent it may know what to do.
            }
            System.out.println("...resending message '" + sipData + "'");
            this.resendAttempts++;
            return this.sendReceive(sipData);
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
        } 
        catch (UnknownHostException ex)
        {
            if (debug)System.out.println("DEBUG error: "+ ex.getMessage());
            throw new SIPException(SIPConnector.class.getName()
                    + " the requested host '" + host + "' is unknown");
        } 
        catch (IOException ex)
        {
            if (debug)System.out.println("DEBUG error: "+ ex.getMessage());
            throw new SIPException(SIPConnector.class.getName()
                    + " the request for sip connection was refused");
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
            if (debug)System.out.println("DEBUG error: "+ ex.getMessage());
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
