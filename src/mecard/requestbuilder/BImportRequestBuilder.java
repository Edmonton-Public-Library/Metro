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
package mecard.requestbuilder;

import api.APICommand;
import api.Command;
import api.CommandStatus;
import mecard.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import mecard.QueryTypes;
import mecard.ResponseTypes;
import metro.common.BImportPropertyTypes;
import metro.common.ConfigFileTypes;
import mecard.config.CustomerFieldTypes;
import metro.common.MessagesConfigTypes;
import mecard.customer.BImportBat;
import mecard.customer.BImportFile;
import mecard.customer.Customer;
import mecard.customer.CustomerFormatter;
import mecard.exception.BImportException;
import mecard.exception.UnsupportedCommandException;
import metro.common.PropertyReader;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class BImportRequestBuilder extends ILSRequestBuilder
{
    // Use this to prefix all our files.
    public final static String FILE_NAME_PREFIX = "metro-";
    public final static String BAT_FILE = "-bimp.bat";
    public final static String HEADER_FILE = "-header.txt";
    public final static String DATA_FILE = "-data.txt";
    private static final CharSequence SUCCESS_MARKER = "<ok>";
    private String bimportDir;    // where bimport exe is located.
    private String loadDir; // where to find the batch, header and data files.
    private String serverName;
    private String password;
    private String userName;
    private String database; // we may need another way to distinguish DBs on a server.
    private String uniqueBorrowerTableKey;
    private String bimportVersion; // like fm41
    private String defaultBtype; // like bawb
    private String mailType;
    private String location; // branch? see 'lalap'
    private String isIndexed; // "y = NOT indexed"
    private String batFile;
    private String headerFile;
    private String dataFile;
    private final Properties messageProperties;
    private final boolean debug;
    
    BImportRequestBuilder(boolean debug)
    {
        this.debug = debug;
        this.messageProperties = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        Properties bimpProps = PropertyReader.getProperties(ConfigFileTypes.BIMPORT);
        this.bimportDir = bimpProps.getProperty(BImportPropertyTypes.BIMPORT_DIR.toString());
        this.loadDir = bimpProps.getProperty(BImportPropertyTypes.LOAD_DIR.toString());
        this.serverName = bimpProps.getProperty(BImportPropertyTypes.SERVER.toString());
        this.password = bimpProps.getProperty(BImportPropertyTypes.PASSWORD.toString());
        this.userName = bimpProps.getProperty(BImportPropertyTypes.USER.toString());
        this.database = bimpProps.getProperty(BImportPropertyTypes.DATABASE.toString()); // we may need another way to distinguish DBs on a server.
        this.uniqueBorrowerTableKey = bimpProps.getProperty(BImportPropertyTypes.UNIQUE_BORROWER_TABLE_KEY.toString());
        this.bimportVersion = bimpProps.getProperty(BImportPropertyTypes.VERSION.toString()); // like fm41
        // TODO these should come from the default.properties.
        this.defaultBtype = bimpProps.getProperty(BImportPropertyTypes.DEFAULT_BTYPE.toString()); // like bawb
        this.mailType = bimpProps.getProperty(BImportPropertyTypes.MAIL_TYPE.toString());
        this.location = bimpProps.getProperty(BImportPropertyTypes.LOCATION.toString()); // branch? see 'lalap'
        this.isIndexed = bimpProps.getProperty(BImportPropertyTypes.IS_INDEXED.toString());
    }

    @Override
    public CustomerFormatter getFormatter()
    {
        throw new UnsupportedCommandException(BImportRequestBuilder.class.getName()
                + " BImport does not require a customer formatter.");
    }

    @Override
    public Command getCreateUserCommand(Customer customer, Response response)
    {
        // In this method on this class we create the data file and optionally
        // a batch file at the same time since all three files must have consistent
        // nameing.
        // First thing set up the file names for this customer.
        String transactionId = customer.get(CustomerFieldTypes.ID);
        // compute header and data file names.
        if (loadDir.endsWith(File.separator) == false)
        {
            loadDir += File.separator;
        }
        if (bimportDir.endsWith(File.separator) == false)
        {
            bimportDir += File.separator;
        }
        // bat file name 
        batFile = loadDir + FILE_NAME_PREFIX + transactionId + BAT_FILE;
        // header and data file names.
        headerFile = loadDir + FILE_NAME_PREFIX + transactionId + HEADER_FILE;
        dataFile = loadDir + FILE_NAME_PREFIX + transactionId + DATA_FILE;
        new BImportFile.Builder(headerFile, dataFile)
                .barcode(customer.get(CustomerFieldTypes.ID))
                .pin(customer.get(CustomerFieldTypes.PIN))
                .name(customer.get(CustomerFieldTypes.PREFEREDNAME))
                .address1(customer.get(CustomerFieldTypes.STREET))
                .city(customer.get(CustomerFieldTypes.CITY))
                .postalCode(customer.get(CustomerFieldTypes.POSTALCODE))
                .emailName(computeEmailName(customer.get(CustomerFieldTypes.EMAIL)))
                .email(customer.get(CustomerFieldTypes.EMAIL))
                .preferEmailNotifications(true) // Users must have an email address to use MeCard metro.
                .expire(customer.get(CustomerFieldTypes.PRIVILEGE_EXPIRES))
                .pNumber(customer.get(CustomerFieldTypes.PHONE))
                .build();
        File fTest = new File(headerFile);
        if (fTest.exists() == false)
        {
            throw new BImportException(BImportRequestBuilder.class.getName()
                    + " Could not create header file: '" + headerFile + "'.");
        }
        fTest = new File(dataFile);
        if (fTest.exists() == false)
        {
            throw new BImportException(BImportRequestBuilder.class.getName()
                    + " Could not create data file: '" + dataFile + "'.");
        }
        // Ok the goal is to get the path to the batch file here with the name.
        // The batch file and name have to be built at this time just like SymphonyRequestBuilder.
        BImportBat batch = new BImportBat.Builder(batFile)
                .setBimportPath(bimportDir)
                .server(serverName).password(password)
                .user(userName).database(database)
                .header(headerFile).data(dataFile)
                .borrowerTableKey(uniqueBorrowerTableKey).format(bimportVersion).bType(defaultBtype)
                .mType(mailType).location(location).setIndexed(Boolean.valueOf(isIndexed))
//                .setDebug(debug) // not used in class yet.
                .build();
        List<String> bimportBatExec = new ArrayList<String>();
        batch.getCommandLine(bimportBatExec);
        APICommand command = new APICommand.Builder().commandLine(bimportBatExec).build();
        return command;
    }

    @Override
    public Command getUpdateUserCommand(Customer customer, Response response)
    {
        // Since we use the same command for updating as creating we can do this:
        return getCreateUserCommand(customer, response);
    }

    /**
     *
     * @param commandType the value of commandType
     * @param status the value of status
     * @param response the value of response
     * @return the boolean
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
                    response.setCode(ResponseTypes.OK);
                    response.setResponse(messageProperties.getProperty(MessagesConfigTypes.SUCCESS_JOIN.toString()));
                    System.out.println(new Date() + "Customer account successfully create.");
                    result = true;
                }
                else
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesConfigTypes.ACCOUNT_NOT_CREATED.toString()));
                    System.out.println(new Date() + "Customer account failed to create.");
                    result = false;
                }
                break;
            case UPDATE_CUSTOMER:
                // so if the bimport command was successful it looks like this:
                resultString = status.getStdout();
                if (resultString.contains(BImportRequestBuilder.SUCCESS_MARKER))
                {
                    response.setCode(ResponseTypes.OK);
                    response.setResponse(messageProperties.getProperty(MessagesConfigTypes.SUCCESS_UPDATE.toString()));
                    System.out.println(new Date() + "Customer account successfully updated.");
                    result = true;
                }
                else
                {
                    response.setCode(ResponseTypes.FAIL);
                    response.setResponse(messageProperties.getProperty(MessagesConfigTypes.ACCOUNT_NOT_UPDATED.toString()));
                    System.out.println(new Date() + "Customer account failed to update.");
                    result = false;
                }
                break;
            case NULL:
                response.setCode(ResponseTypes.OK);
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
    
    /** 
     * Horizon has an additional required field, email name, which is just the 
     * user's email name (without the domain). We compute that here.
     * @param email
     * @return String value of email account name (without the domain).
     */
    protected String computeEmailName(String email) 
    {
        return email.split("@")[0];
    }

    @Override
    public boolean tidy()
    {
        // TODO in the mature version use this method to clean up unwanted header, data and batch files.
        return true;
    }

}
