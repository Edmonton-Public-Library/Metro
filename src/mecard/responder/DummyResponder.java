package mecard.responder;

import api.Request;
import api.Response;
import java.util.Properties;
import json.ResponseDeserializer;
import mecard.MetroService;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
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
        Properties props = MetroService.getProperties(ConfigFileTypes.DEBUG);
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
                createCustomer(response);
                break;
            case UPDATE_CUSTOMER:
                updateCustomer(response);
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
        ResponseDeserializer deserializer = new ResponseDeserializer();
        Response r = deserializer.getDeserializedResponse(this.gsonStatus);
        response.setCode(r.getCode());
        response.setResponse(r.getMessage());
        response.setCustomer(r.getCustomer());
    }


    @Override
    public void getCustomer(Response response)
    {
        ResponseDeserializer deserializer = new ResponseDeserializer();
        Response r = deserializer.getDeserializedResponse(this.gsonGetCustomer);
        response.setCode(r.getCode());
        response.setResponse(r.getMessage());
        response.setCustomer(r.getCustomer());
    }
    
    
    /**
     *
     *
     */
    @Override
    public void updateCustomer(Response response)
    {
        ResponseDeserializer deserializer = new ResponseDeserializer();
        Response r = deserializer.getDeserializedResponse(this.gsonUpdateCustomer);
        response.setCode(r.getCode());
        response.setResponse(r.getMessage());
        response.setCustomer(r.getCustomer());
    }

    @Override
    public void createCustomer(Response response)
    {
        ResponseDeserializer deserializer = new ResponseDeserializer();
        Response r = deserializer.getDeserializedResponse(this.gsonCreateCustomer);
        response.setCode(r.getCode());
        response.setResponse(r.getMessage());
        response.setCustomer(r.getCustomer());
    }
    
    protected static ResponseTypes convertToResponseType(String sType)
    {
        ResponseTypes r = ResponseTypes.valueOf(sType);
        return r;
    }

    @Override
    public boolean isAuthorized(String suppliedPin, Customer customer)
    {
        return (customer.get(CustomerFieldTypes.PIN).compareTo(suppliedPin) == 0);
    }
}
