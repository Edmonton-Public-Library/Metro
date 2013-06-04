/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import mecard.Protocol;
import mecard.ProtocolPayload;
import mecard.ResponseTypes;
import mecard.customer.Customer;

/**
 * Simple object to order responses.
 * @author metro
 */
public class Response extends ProtocolPayload
{
    protected ResponseTypes code;
    
    public Response()
    {
        super();
        code = ResponseTypes.INIT;
    }
    
    public Response(ResponseTypes rt)
    {
        super();
        code = rt;
    }

    public void setCode(ResponseTypes code) 
    {
        this.code = code;
    }
    
    public void setCustomer(Customer c)
    {
        this.response = c.getPayload();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(code);
        sb.append(Protocol.DELIMITER);
        sb.append(response.toString());
        return sb.toString();
    }
}
