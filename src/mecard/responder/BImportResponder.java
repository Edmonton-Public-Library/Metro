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
import mecard.customer.CustomerFieldTypes;
import mecard.util.BImportBat;
import mecard.customer.BImportFormatter;
import api.Command;
import api.ProcessWatcherHandler;
import api.Request;
import api.Response;
import mecard.MetroService;

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
    private static String NULL_QUERY_RESPONSE_MSG = "BImport responder null request answered";
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

    /**
     *
     * @param cmd the value of cmd
     * @param debugMode the value of debugMode
     */
    public BImportResponder(Request cmd, boolean debugMode)
    {
        super(cmd, debugMode);
        Properties bimpProps = MetroService.getProperties(ConfigFileTypes.BIMPORT);
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
//        String transactionId = this.request.getTransactionId();
        String transactionId = this.request.getCustomerField(CustomerFieldTypes.ID);
        batFile = bimportDir + pathSep + FILE_NAME_PREFIX + transactionId + BAT_FILE;
        headerFile = bimportDir + pathSep + FILE_NAME_PREFIX + transactionId + HEADER_FILE;
        dataFile = bimportDir + pathSep + FILE_NAME_PREFIX + transactionId + DATA_FILE;
    }

    /**
     *
     * @return the api.Response
     */
    @Override
    public Response getResponse()
    {
        // test for the operations that this responder is capable of performing
        // SIP can't create customers, BImport can't query customers.
        Response response = new Response();
        switch (request.getCommandType())
        {
            case CREATE_CUSTOMER:
                createCustomer(response);
                break;
            case UPDATE_CUSTOMER:
                updateCustomer(response);
                break;
            case NULL:
                response.setCode(ResponseTypes.OK);
                response.setResponse(NULL_QUERY_RESPONSE_MSG);
                break;
            default:
                response.setCode(ResponseTypes.ERROR);
                response.setResponse(BImportResponder.class.getName() + " cannot " + request.toString());
        }
        return response;
    }

    /**
     * Looks confusing but merely converts the customer into a ILS meaningful
     * expression of some sort (for BImport that's a command line expression or
     * bat file name), then executes the command.
     * 
     * @param response object
     */
    @Override
    public void createCustomer(Response response)
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
                response.setResponse(status.getStdout());
                response.setCode(ResponseTypes.SUCCESS);
                return;
            }
        }
        response.setResponse("Customer conversion failed because the BImport files could not be created.");
        response.setCode(ResponseTypes.FAIL);
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
                .barcode(request.getCustomerField(CustomerFieldTypes.ID))
                .pin(request.getCustomerField(CustomerFieldTypes.PIN))
                .name(request.getCustomerField(CustomerFieldTypes.NAME))
                .address1(request.getCustomerField(CustomerFieldTypes.STREET))
                .city(request.getCustomerField(CustomerFieldTypes.CITY))
                .postalCode(request.getCustomerField(CustomerFieldTypes.POSTALCODE))
                .emailName(computeEmailName(request.getCustomerField(CustomerFieldTypes.EMAIL)))
                .email(request.getCustomerField(CustomerFieldTypes.EMAIL))
                .expire(request.getCustomerField(CustomerFieldTypes.PRIVILEGE_EXPIRES))
                .pNumber(request.getCustomerField(CustomerFieldTypes.PHONE))
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
     * @return String value of email account name (without the domain).
     */
    protected String computeEmailName(String email) 
    {
        return email.split("@")[0];
    }

    @Override
    public void updateCustomer(Response response)
    {
        createCustomer(response);
    }
}
