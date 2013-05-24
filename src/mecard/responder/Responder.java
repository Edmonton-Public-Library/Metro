/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.responder;

import java.util.ArrayList;
import java.util.List;
import mecard.ResponseTypes;

/**
 *
 * @author metro
 */
public abstract class Responder
{
    protected List<String> command;
    protected List<String> response;
    protected ResponseTypes state;
    protected final String originalCommand;
    
    protected Responder(String cmd)
    {
        this.state = ResponseTypes.INIT;
        this.originalCommand = cmd;
    }
    
    /**
     * Split the command on the Protocol's delimiter breaking the command 
     * into chunks. The first element on the list is the command itself which
     * can be ignored since it was already dealt with when this object was 
     * created. The second is the MD5 hash of the query salted with the senders 
     * shared secret. The rest of the elements (if any) are arguments to the 
     * command.
     * @param cmd
     * @return 
     */
    protected final List<String> splitCommand(String cmd)
    {
        List<String> args = new ArrayList<String>();
        return args;
    }
    
    protected final String pack(List<String> args)
    {
        return "";
    }

    public abstract String getResponse();

    public ResponseTypes getState()
    {
        return this.state;
    }
}
