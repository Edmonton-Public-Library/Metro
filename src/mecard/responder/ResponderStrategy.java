/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.responder;

import mecard.ResponseTypes;
import mecard.util.Request;
import mecard.util.Response;

/**
 *
 * @author metro
 */
public abstract class ResponderStrategy
{
    protected Request request;
    protected Response response;
    protected final boolean debug;
    
    protected ResponderStrategy(String cmd, boolean debugMode)
    {
        this.debug = debugMode;
        this.request = new Request(cmd);
        this.response = new Response(ResponseTypes.INIT);
        if (debug)
        {
            System.out.println("CMD:\n  '"+request.toString()+"' '"+request.getCommandType().name()+"'");
            System.out.println("ELE:");
            System.out.println("  S:" + request.getArgs() + ",");
        }
    }
    
    public abstract String getResponse();
}
