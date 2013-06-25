package mecard.responder;

import api.Request;
import api.Response;
import com.google.gson.Gson;
import java.util.List;
import java.util.Properties;
import mecard.ProtocolPayload;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.DebugQueryConfigTypes;

/**
 * DummyResponder is used to test round trip connections. It returns hard coded
 * canned results for connection testing.
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class DummyRepsonder extends Responder
        implements StatusQueryable, CustomerQueryable, Updateable, Createable
{
    private static Object NULL_QUERY_RESPONSE_MSG = "debug responder reporting";

    private final String gsonStatus;
    private final String gsonGetCustomer;
    private final String gsonCreateCustomer;
    private final String gsonUpdateCustomer;

    public DummyRepsonder(Request command, boolean debugMode)
    {
        super(command, debugMode);
        Properties props = PropertyReader.getProperties(ConfigFileTypes.DEBUG);
        gsonStatus = props.getProperty(DebugQueryConfigTypes.GET_STATUS.toString());
        gsonGetCustomer = props.getProperty(DebugQueryConfigTypes.GET_CUSTOMER.toString());
        gsonCreateCustomer = props.getProperty(DebugQueryConfigTypes.CREATE_CUSTOMER.toString());
        gsonUpdateCustomer = props.getProperty(DebugQueryConfigTypes.UPDATE_CUSTOMER.toString());
    }

    @Override
    public String getResponse()
    {
        this.response.setCode(ResponseTypes.BUSY);
        StringBuffer responseBuffer = new StringBuffer();
        switch (request.getCommandType())
        {
            case GET_STATUS:
                this.response.setCode(getILSStatus(responseBuffer));
                break;
            case GET_CUSTOMER:
                this.response.setCode(getCustomer(responseBuffer));
                break;
            case CREATE_CUSTOMER:
                this.response.setCode(updateCustomer(responseBuffer));
                break;
            case UPDATE_CUSTOMER:
                this.response.setCode(createCustomer(responseBuffer));
                break;
            case NULL:
                this.response.setCode(ResponseTypes.OK);
                responseBuffer.append(DummyRepsonder.NULL_QUERY_RESPONSE_MSG);
                break;
            default:
                this.response.setCode(ResponseTypes.ERROR);
                responseBuffer.append(BImportResponder.class.getName());
                responseBuffer.append(" cannot ");
                responseBuffer.append(request.toString());
        }
        if (responseBuffer.length() > 0)
        {
            this.response.addResponse(responseBuffer.toString());
        }
        return response.toString();
    }

    @Override
    public ResponseTypes getILSStatus(StringBuffer responseBuffer)
    {
        Gson gson = new Gson();
        List<String> gsonResponse = gson.fromJson(this.gsonStatus, List.class);
        return setMessages(gsonResponse, responseBuffer);
    }

    @Override
    public ResponseTypes getCustomer(StringBuffer responseBuffer)
    {
        Gson gson = new Gson();
        List<String> gsonResponse = gson.fromJson(this.gsonGetCustomer, List.class);
        return setMessages(gsonResponse, responseBuffer);
    }
    
    
    @Override
    public ResponseTypes updateCustomer(StringBuffer responseBuffer)
    {
        Gson gson = new Gson();
        List<String> gsonResponse = gson.fromJson(this.gsonUpdateCustomer, List.class);
        return setMessages(gsonResponse, responseBuffer);
    }

    @Override
    public ResponseTypes createCustomer(StringBuffer responseBuffer)
    {
        Gson gson = new Gson();
        List<String> gsonResponse = gson.fromJson(this.gsonCreateCustomer, List.class);
        return setMessages(gsonResponse, responseBuffer);
    }
    
    protected ResponseTypes setMessages(List<String> gsonResponse, StringBuffer responseBuffer)
    {  
        ResponseTypes rType = ResponseTypes.UNKNOWN;
        try
        {
            rType = convertToResponseType(gsonResponse.get(0));
            for (int pos = 1; pos < gsonResponse.size(); pos++)
            {
                if (debug)
                {
                    System.out.println(pos+") '" + gsonResponse.get(pos) + "'");
                }
                this.response.setResponse(gsonResponse.get(pos));
            }
            
        } 
        catch (Exception ex)
        {
            responseBuffer.append("Exception occured in ");
            responseBuffer.append(DummyRepsonder.class.getName());
            responseBuffer.append(ex.getMessage());
        }
        return rType;
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
}
