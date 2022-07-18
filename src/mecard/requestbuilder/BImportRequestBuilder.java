/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2020  Edmonton Public Library
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
package mecard.requestbuilder;

import api.Command;
import api.CommandStatus;
import api.CustomerMessage;
import api.DummyCommand;
import mecard.Response;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import mecard.QueryTypes;
import mecard.ResponseTypes;
import mecard.config.BImportPropertyTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import mecard.config.MessagesTypes;
import mecard.customer.Customer;
import mecard.exception.BImportException;
import mecard.exception.UnsupportedCommandException;
import mecard.config.PropertyReader;
import mecard.horizon.MeCardCustomerToBImport;
import mecard.horizon.BimportUserFile;
import mecard.customer.UserFile;
import mecard.exception.ConfigurationException;
import site.CustomerLoadNormalizer;
import mecard.customer.MeCardCustomerToNativeFormat;
import mecard.customer.NativeFormatToMeCardCustomer;

/**
 * Manages building customer registration requests in a Horizon-centric way. 
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com
 */
public class BImportRequestBuilder extends ILSRequestBuilder
{
    // Optional field in BImport bimport.properties file
    /**
     * Include this value in your bimport.properties file if you want to turn 
     * this setting on, or off on a customer account upon creation. Default it on.
     */
    public final static String SEND_PREOVERDUE = "send-preoverdue";
    // Use this to prefix all our files.
    public final static String FILE_NAME_PREFIX = "metro-";
    public final static String BAT_FILE = "-bimp.bat";
    public final static String HEADER_FILE = "-header.txt";
    // This name extension is used by the customer loader; it contains all customer
    // data that has arrived since the last time the customer loader last ran and
    // gloms it all into one file for bulk loading.
    public final static String DATA_FILE_BIMPORT = "-bimport.txt";
    // Used for an individual's data. Never loaded directly although technically you could.
    public final static String DATA_FILE = "-data.txt";
    public static final CharSequence SUCCESS_MARKER = "<ok>";
    public static final String PHONE_TAG = "default-phone";
    protected String bimportDir;    // where bimport exe is located.
    protected String serverName;
    protected String password;
    protected String userName;
    protected String database; // we may need another way to distinguish DBs on a server.
    protected String uniqueBorrowerTableKey;
    protected String bimportVersion; // like fm41
    protected String defaultBtype; // like bawb
    protected String mailType;
    protected String location; // branch? see 'lalap'
    protected String isIndexed; // "y = NOT indexed"
    protected String batFile;
    protected String headerFile;
    protected String dataFile;
    protected final Properties messageProperties;
    protected final boolean debug;
    
    public BImportRequestBuilder(boolean debug)
    {
        this.debug = debug;
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        Properties bimpProps = PropertyReader.getProperties(ConfigFileTypes.BIMPORT);
        this.bimportDir = bimpProps.getProperty(BImportPropertyTypes.BIMPORT_DIR.toString());
        // Done because the tag is common to all ILS specific properties files.
        this.loadDir = bimpProps.getProperty(PropertyReader.LOAD_DIR);
        this.serverName = bimpProps.getProperty(BImportPropertyTypes.SERVER.toString());
        this.password = bimpProps.getProperty(BImportPropertyTypes.PASSWORD.toString());
        this.userName = bimpProps.getProperty(BImportPropertyTypes.USER.toString());
        this.database = bimpProps.getProperty(BImportPropertyTypes.DATABASE.toString()); // we may need another way to distinguish DBs on a server.
        this.uniqueBorrowerTableKey = bimpProps.getProperty(BImportPropertyTypes.UNIQUE_BORROWER_TABLE_KEY.toString());
        this.bimportVersion = bimpProps.getProperty(BImportPropertyTypes.VERSION.toString()); // like fm41
        this.defaultBtype = bimpProps.getProperty(BImportPropertyTypes.DEFAULT_BTYPE.toString()); // like bawb
        this.mailType = bimpProps.getProperty(BImportPropertyTypes.MAIL_TYPE.toString());
        this.location = bimpProps.getProperty(BImportPropertyTypes.LOCATION.toString()); // branch? see 'lalap'
        this.isIndexed = bimpProps.getProperty(BImportPropertyTypes.IS_INDEXED.toString());
    }

    @Override
    public NativeFormatToMeCardCustomer getFormatter()
    {
        throw new UnsupportedCommandException(BImportRequestBuilder.class.getName()
                + " BImport does not require a customer formatter.");
    }

    @Override
    public Command getCreateUserCommand(
            Customer customer, 
            Response response, 
            CustomerLoadNormalizer normalizer)
    {
        // In this method on this class we create the data file and optionally
        // a batch file at the same time since all three files must have consistent
        // naming.
        // First thing set up the file names for this customer.
        String transactionId = customer.get(CustomerFieldTypes.ID);
        // compute header and data file names.
        if (loadDir.endsWith(File.separator) == false)
        {
            loadDir += File.separator;
        }
        dataFile = loadDir + FILE_NAME_PREFIX + transactionId + DATA_FILE;
        UserFile bimportDataFile = new BimportUserFile(dataFile);
        MeCardCustomerToNativeFormat formattedCustomer = new MeCardCustomerToBImport(customer);
        // Make final changes to the formatted customer before loading as adding bstat.
        // and make sure the pin is replaced if required.
        normalizer.finalize(customer, formattedCustomer, response);
        bimportDataFile.addUserData(formattedCustomer.getFormattedCustomer());
        File fTest = new File(dataFile);
        if (fTest.exists() == false)
        {
            throw new BImportException(BImportRequestBuilder.class.getName()
                    + " Could not create data file: '" + dataFile + "'.");
        }
        // We need to know if there is a header file and if not build one.
        headerFile = loadDir + FILE_NAME_PREFIX + "template" + HEADER_FILE;
        File headerTest = new File(headerFile);
        // Check if it exists because some other metro process might be using it.
        if (headerTest.exists() == false)
        {
            UserFile bimportHeaderFile = new UserFile(headerFile);
            bimportHeaderFile.addUserData(formattedCustomer.getFormattedHeader());
        }
        // We use a scheduled loader to run BImport regularly on existing customer accounts
        // because multiple BImport load requests will cause BImport instances to collide.
        Command command = new DummyCommand.Builder()
                .setStatus(0)
                .setStdout(BImportRequestBuilder.SUCCESS_MARKER.toString())
                .build(); // empty command always returns success because BimportCustomerLoader loads on a timed event.
        return command;
    }

    @Override
    public Command getUpdateUserCommand(Customer customer, Response response, CustomerLoadNormalizer normalizer)
    {
        // This is the same as create. See above for explanation.
        return getCreateUserCommand(customer, response, normalizer);
    }

    /**
     *
     * @param commandType the value of commandType
     * @param status the value of status
     * @param response the value of response
     * @return true if the requested command completed successfully for some 
     * definition of success that's dependent on what the command did.
     */
    @Override
    public boolean isSuccessful(QueryTypes commandType, CommandStatus status, Response response)
    {
        boolean result;
        String resultString = "";
        switch (commandType)
        {
            case CREATE_CUSTOMER:
                // so if the bimport command was successful it looks like this:
                resultString = status.getStdout();
                if (resultString.contains(BImportRequestBuilder.SUCCESS_MARKER))
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.SUCCESS_JOIN.toString()));
                    System.out.println(new Date() + "Customer account successfully create.");
                    result = true;
                }
                else
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_CREATED.toString()));
                    System.out.println(new Date() + "Customer account failed to create.");
                    result = false;
                }
                break;
            case UPDATE_CUSTOMER:
                // so if the bimport command was successful it looks like this:
                resultString = status.getStdout();
                if (resultString.contains(BImportRequestBuilder.SUCCESS_MARKER))
                {
                    response.setCode(ResponseTypes.SUCCESS);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.SUCCESS_UPDATE.toString()));
                    System.out.println(new Date() + "Customer account successfully updated.");
                    result = true;
                }
                else
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesTypes.ACCOUNT_NOT_UPDATED.toString()));
                    System.out.println(new Date() + "Customer account failed to update.");
                    result = false;
                }
                break;
            case NULL:
                response.setCode(ResponseTypes.SUCCESS);
                response.setResponse("Null BImport command back at you...");
                result = true;
                break;
            default:
                response.setCode(ResponseTypes.UNKNOWN);
                response.setResponse(BImportRequestBuilder.class.getName() 
                        + " doesn't know how to execute the query type: "
                        + commandType.name());
                result = false;
        }
        return result;
    }
    
    @Override
    public boolean tidy()
    {
        // TODO in the mature version use this method to clean up unwanted header, data and batch files.
        return true;
    }

    @Override
    public Command getCustomerCommand(String userId, String userPin, Response response)
    {
        throw new ConfigurationException("BImport does not support account queries "
                + "Please review your environment.properties configuration");
    }

    @Override
    public Command getStatusCommand(Response response)
    {
        throw new ConfigurationException("BImport cannot test ILS status "
                + "Please review your environment.properties configuration");
    }
    
    @Override
    public CustomerMessage getCustomerMessage(String stdout)
    {
        throw new UnsupportedOperationException("Not supported in BImport.");
    }

    @Override
    public Command testCustomerExists(String userId, String userPin, Response response) 
    {
        return getCustomerCommand(userId, userPin, response);
    }
}
