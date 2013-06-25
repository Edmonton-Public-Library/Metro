package mecard.responder;

import api.Request;
import java.util.Properties;
import mecard.QueryTypes;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;

/**
 * DummyResponder is used to test round trip connections. It returns hard coded
 * canned results for connection testing.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class DummyRepsonder extends Responder
    implements StatusQueryable, CustomerQueryable, Updateable, Createable
{
    private final String statusResult;

    public DummyRepsonder(Request command, boolean debugMode)
    {
        super(command, debugMode);
        Properties props = PropertyReader.getProperties(ConfigFileTypes.DEBUG);
        statusResult = props.getProperty(QueryTypes.CREATE_CUSTOMER.toString());
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
                this.response.addResponse(responseBuffer.toString());
                break;
            case GET_CUSTOMER:
                this.response.setCode(getCustomer(responseBuffer));
                this.response.addResponse(responseBuffer.toString());
                break;
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

    @Override
    public ResponseTypes getILSStatus(StringBuffer responseBuffer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseTypes getCustomer(StringBuffer responseBuffer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseTypes updateCustomer(StringBuffer responseBuffer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseTypes createCustomer(StringBuffer responseBuffer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
