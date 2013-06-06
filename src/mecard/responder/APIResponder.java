/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.responder;

import mecard.ResponseTypes;

/**
 *
 * @author andrew
 */
public class APIResponder extends ResponderStrategy
{
    private static String NULL_QUERY_RESPONSE_MSG = "API says hello.";

    public APIResponder(String command, boolean debugMode)
    {
        super(command, debugMode);
        this.response.setCode(ResponseTypes.BUSY);
    }

    @Override
    public String getResponse()
    {
        StringBuffer responseBuffer = new StringBuffer();
        switch (request.getCommandType())
        {
            case GET_STATUS:
                this.response.setCode(getServerStatus(responseBuffer));
                this.response.setResponse(responseBuffer.toString());
                break;
            case GET_CUSTOMER:
                this.response.setCode(getUser(responseBuffer));
                this.response.setResponse(responseBuffer.toString());
                break;
            case CREATE_CUSTOMER:
                this.response.setCode(createCustomer(responseBuffer));
                this.response.addResponse(responseBuffer.toString());
                break;
            case UPDATE_CUSTOMER:
                this.response.setCode(updateCustomer(responseBuffer));
                this.response.addResponse(responseBuffer.toString());
                break;
            case NULL:
                this.response.setCode(ResponseTypes.OK);
                this.response.addResponse(APIResponder.NULL_QUERY_RESPONSE_MSG);
                break;
            default:
                this.response.setCode(ResponseTypes.ERROR);
                this.response.addResponse(BImportResponder.class.getName()
                        + " cannot " + request.toString());
        }
        return response.toString();
    }

    private ResponseTypes updateCustomer(StringBuffer responseBuffer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ResponseTypes createCustomer(StringBuffer responseBuffer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ResponseTypes getUser(StringBuffer responseBuffer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ResponseTypes getServerStatus(StringBuffer responseBuffer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
