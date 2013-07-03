package mecard.responder;

import api.Request;
import api.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import json.CustomerDeserializer;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.DebugQueryConfigTypes;
import mecard.customer.Customer;
import mecard.customer.CustomerFieldTypes;

/**
 * DummyResponder is used to test round trip connections. It returns hard coded
 * canned results for connection testing.
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class DummyResponder extends CustomerQueryable
        implements StatusQueryable, Updateable, Createable
{
    private static String NULL_QUERY_RESPONSE_MSG = "debug responder reporting";

    private final String gsonStatus;
    private final String gsonGetCustomer;
    private final String gsonCreateCustomer;
    private final String gsonUpdateCustomer;
    private final String altData;

    public DummyResponder(Request command, boolean debugMode)
    {
        super(command, debugMode);
        Properties props = PropertyReader.getProperties(ConfigFileTypes.DEBUG);
        gsonStatus = props.getProperty(DebugQueryConfigTypes.GET_STATUS.toString());
        gsonGetCustomer = props.getProperty(DebugQueryConfigTypes.GET_CUSTOMER.toString());
        gsonCreateCustomer = props.getProperty(DebugQueryConfigTypes.CREATE_CUSTOMER.toString());
        gsonUpdateCustomer = props.getProperty(DebugQueryConfigTypes.UPDATE_CUSTOMER.toString());
        altData = props.getProperty(DebugQueryConfigTypes.ALT_DATA_CUSTOMER.toString());
    }

    /**
     *
     * @return the api.Response
     */
    @Override
    public Response getResponse()
    {
        Response response = new Response();
        switch (request.getCommandType())
        {
            case GET_STATUS:
                getILSStatus(response);
                break;
            case GET_CUSTOMER:
                getCustomer(response);
                break;
            case CREATE_CUSTOMER:
                updateCustomer(response);
                break;
            case UPDATE_CUSTOMER:
                createCustomer(response);
                break;
            case NULL:
                response.setCode(ResponseTypes.OK);
                response.setResponse(DummyResponder.NULL_QUERY_RESPONSE_MSG);
                break;
            default:
                response.setCode(ResponseTypes.ERROR);
                response.setResponse(BImportResponder.class.getName() + " cannot " + request.toString());
        }
        return response;
    }

    @Override
    public void getILSStatus(Response response)
    {
        Gson gson = new Gson();
        List<String> gsonResponse = gson.fromJson(this.gsonStatus, List.class);
        setMessages(gsonResponse, response);
    }


    @Override
    public void getCustomer(Response response)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Customer.class, new CustomerDeserializer());
        Gson gson = gsonBuilder.create();
        Reader data = new StringReader(this.gsonGetCustomer);
        Customer customer = gson.fromJson(data, Customer.class);
        // the 3rd field of this request is the pin.
        System.out.println("PIN:"+this.request.getCustomerField(CustomerFieldTypes.PIN)+customer);
        if (isAuthorized(this.request.getCustomerField(CustomerFieldTypes.PIN), customer))
        {
            if (meetsMeCardRequirements(customer, this.altData))
            {
                response.setCode(ResponseTypes.OK);
                response.setCustomer(customer);
            }
        }
        // TODO fix me
        setMessages(new ArrayList<String>(), response);
    }
    
    
    /**
     *
     *
     */
    
    @Override
    public void updateCustomer(Response response)
    {
        Gson gson = new Gson();
        List<String> gsonResponse = gson.fromJson(this.gsonUpdateCustomer, List.class);
        setMessages(gsonResponse, response);
    }

    /**
     *
     * @param responseBuffer the value of responseBuffer
     */
    @Override
    public void createCustomer(Response response)
    {
        Gson gson = new Gson();
        List<String> gsonResponse = gson.fromJson(this.gsonCreateCustomer, List.class);
        setMessages(gsonResponse, response);
    }
    
    /**
     *
     * @param gsonResponse the value of gsonResponse
     * @param response the value of responseBuffer
     */
    protected void setMessages(List<String> gsonResponse, Response response)
    {
        try
        {
            response.setCode(convertToResponseType(gsonResponse.get(0)));
            StringBuilder sb = new StringBuilder();
            for (int pos = 1; pos < gsonResponse.size(); pos++)
            {
                if (debug)
                {
                    System.out.println(pos+") '" + gsonResponse.get(pos) + "'");
                }
                sb.append(gsonResponse.get(pos));
            }
            response.setResponse(sb.toString());
        } 
        catch (Exception ex)
        {
            response.setResponse("Exception occured in " + DummyResponder.class.getName() + ex.getMessage());
        }
    }


    protected static ResponseTypes convertToResponseType(String sType)
    {
        for (ResponseTypes rType : ResponseTypes.values())
        {
            if (rType.toString().equalsIgnoreCase(sType))
            {
                return rType;
            }
        }
        return ResponseTypes.UNKNOWN;
    }

    @Override
    public boolean isAuthorized(String suppliedPin, Customer customer)
    {
        return (customer.get(CustomerFieldTypes.PIN).compareTo(suppliedPin) == 0);
    }
}
