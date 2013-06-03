/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.responder;

import mecard.ResponseTypes;
import mecard.responder.ResponderStrategy;

/**
 *
 * @author andrew
 */
public class APIResponder extends ResponderStrategy
{

    public APIResponder(String command, boolean debugMode)
    {
        super(command, debugMode);
        this.state = ResponseTypes.BUSY;
    }

    @Override
    public String getResponse()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
