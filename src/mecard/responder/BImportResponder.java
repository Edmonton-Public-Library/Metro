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
package mecard.responder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import mecard.ResponseTypes;
import mecard.config.BImportPropertyTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.customer.CustomerFieldTypes;
import mecard.util.BImportBat;
import mecard.customer.BImportFormatter;
import api.Command;
import api.ProcessWatcherHandler;

/**
 * BImport responder has special capabilities to write files to the local file
 * system and then execute local commands against that file.
 *
 * @since 1.1
 * @author metro
 */
public class BImportResponder extends Responder
    implements Updateable, Createable
{
    // Use this to prefix all our files.

    public final static String FILE_NAME_PREFIX = "metro-";
    public final static String BAT_FILE = "-bimp.bat";
    public final static String HEADER_FILE = "-header.txt";
    public final static String DATA_FILE = "-data.txt";
    private String bimportDir;
    private String serverName;
    private String password;
    private String userName;
    private String database; // we may need another way to distinguish DBs on a server.
    private String serverAlias;
    private String bimportVersion; // like fm41
    private String defaultBtype; // like bawb
    private String mailType;
    private String location; // branch? see 'lalap'
    private String isIndexed; // "y = NOT indexed"
    private String batFile;
    private String headerFile;
    private String dataFile;

    public BImportResponder(String cmd, boolean debugMode)
    {
        super(cmd, debugMode);
        this.response.setCode(ResponseTypes.BUSY);
        Properties bimpProps = PropertyReader.getProperties(ConfigFileTypes.BIMPORT);
        bimportDir = bimpProps.getProperty(BImportPropertyTypes.BIMPORT_DIR.toString());
        serverName = bimpProps.getProperty(BImportPropertyTypes.SERVER.toString());
        password = bimpProps.getProperty(BImportPropertyTypes.PASSWORD.toString());
        userName = bimpProps.getProperty(BImportPropertyTypes.USER.toString());
        database = bimpProps.getProperty(BImportPropertyTypes.DATABASE.toString()); // we may need another way to distinguish DBs on a server.
        serverAlias = bimpProps.getProperty(BImportPropertyTypes.SERVER_ALIAS.toString());
        bimportVersion = bimpProps.getProperty(BImportPropertyTypes.VERSION.toString()); // like fm41
        // TODO these should come from the default.properties.
        defaultBtype = bimpProps.getProperty(BImportPropertyTypes.DEFAULT_BTYPE.toString()); // like bawb
        mailType = bimpProps.getProperty(BImportPropertyTypes.MAIL_TYPE.toString());
        location = bimpProps.getProperty(BImportPropertyTypes.LOCATION.toString()); // branch? see 'lalap'
        isIndexed = bimpProps.getProperty(BImportPropertyTypes.IS_INDEXED.toString());
        
        // compute header and data file names.
        String pathSep;
        if (debug)
        {
            pathSep = "/";
        }
        else
        {
            pathSep = File.pathSeparator;
        }
        String transactionId = this.request.getTransactionId();
        batFile = bimportDir + pathSep + FILE_NAME_PREFIX + transactionId + BAT_FILE;
        headerFile = bimportDir + pathSep + FILE_NAME_PREFIX + transactionId + HEADER_FILE;
        dataFile = bimportDir + pathSep + FILE_NAME_PREFIX + transactionId + DATA_FILE;
    }

    @Override
    public String getResponse()
    {
        // test for the operations that this responder is capable of performing
        // SIP can't create customers, BImport can't query customers.
        StringBuffer responseBuffer = new StringBuffer();
        switch (request.getCommandType())
        {
            case CREATE_CUSTOMER:
                this.response.setCode(updateCustomer(responseBuffer));
                this.response.addResponse(responseBuffer.toString());
                break;
            case UPDATE_CUSTOMER:
                this.response.setCode(createCustomer(responseBuffer));
                this.response.addResponse(responseBuffer.toString());
                break;
            default:
                this.response.setCode(ResponseTypes.ERROR);
                this.response.addResponse(BImportResponder.class.getName()
                        + " cannot " + request.toString());
        }
        return response.toString();
    }

    /**
     * Looks confusing but merely converts the customer into a ILS meaningful
     * expression of some sort (for BImport that's a command line expression or
     * bat file name), then executes the command.
     * @param responseBuffer
     * @return 
     */
    @Override
    public ResponseTypes createCustomer(StringBuffer responseBuffer)
//    protected ResponseTypes submitCustomer(StringBuffer responseBuffer)
    {
        // take the commandArguments, format them to bimport files, execute
        // the batch file.
        List<String> submittableCustomer = new ArrayList<String>();
        // This test checks if the customer is complete.
        if (convert(submittableCustomer))
        {
            Command command = new Command.Builder().args(submittableCustomer).build();
            ProcessWatcherHandler status = command.execute();
            if (status.getStatus() == ResponseTypes.OK)
            {
                responseBuffer.append(status.getStdout());
                return ResponseTypes.SUCCESS;
            }
            else
            {
                responseBuffer.append(status.getStderr());
                return ResponseTypes.FAIL;
            }
        }
        return ResponseTypes.FAIL;
    }
    
    /**
     * Splits apart an in-coming request into it's command, authority token and customer
     * then maps those fields from the string to tables and columns in Horizon.
     * @param customerCommands
     * @return true if the customer files (header and data) were created and 
     * false otherwise.
     */
    protected boolean convert(List<String> customerCommands)
    {
        // here we have to match up the CustomerFields with variable values.
        // the constructor will then make the header and data files.
        new BImportFormatter.Builder(headerFile, dataFile)
                .barcode(request.get(CustomerFieldTypes.ID.ordinal()))
                .pin(request.get(CustomerFieldTypes.PIN.ordinal()))
                .name(request.get(CustomerFieldTypes.NAME.ordinal()))
                .address1(request.get(CustomerFieldTypes.STREET.ordinal()))
                .city(request.get(CustomerFieldTypes.CITY.ordinal()))
                .postalCode(request.get(CustomerFieldTypes.POSTALCODE.ordinal()))
                .emailName(computeEmailName(request.get(CustomerFieldTypes.EMAIL.ordinal())))
                .email(request.get(CustomerFieldTypes.EMAIL.ordinal()))
                .expire(request.get(CustomerFieldTypes.PRIVILEGE_EXPIRES.ordinal()))
                .pNumber(request.get(CustomerFieldTypes.PHONE.ordinal()))
                .build();
        File fTest = new File(headerFile);
        if (fTest.exists() == false)
        {
            return false;
        }
        fTest = new File(dataFile);
        if (fTest.exists() == false)
        {
            return false;
        }
        // load the submittable customer with what you want executed. In bimport's 
        // case it is the command and arguments for loading the customer or, even
        // better the commandline itself.
        // create the bat file.
        BImportBat batch = new BImportBat.Builder(batFile).server(serverName).password(password)
                .user(userName).database(database)
                .header(headerFile).data(dataFile)
                .alias(serverAlias).format(bimportVersion).bType(defaultBtype)
                .mType(mailType).location(location).setIndexed(Boolean.valueOf(isIndexed))
                .setDebug(debug)
                .build();
        customerCommands.add(batch.getCommandLine());
        // alternatively:
        // sc.setCustomerRepresentation(batch.getBatchFileName());
        return true;
    }

    /** 
     * Horizon has an additional required field, email name, which is just the 
     * user's email name (without the domain). We compute that here.
     * @param email
     * @return 
     */
    protected String computeEmailName(String email) 
    {
        return email.split("@")[0];
    }

    @Override
    public ResponseTypes updateCustomer(StringBuffer responseBuffer)
    {
        return createCustomer(responseBuffer);
    }
}
