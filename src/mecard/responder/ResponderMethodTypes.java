/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.responder;

/**
 * Every command that Metro executes must have a strategy defined for how it will
 * execute that command. There are just four strategies for satisfying requests 
 * from the metro server. These values have to be entered in the environment configuration XML file
 * @author andrew
 */
public enum ResponderMethodTypes
{
    LOCAL_CALL("api"), // SQL or API  
    SIP2("sip2"),
    BIMPORT("bimport");
    
    private String type;
    
    private ResponderMethodTypes(String t)
    {
        this.type = t;
    }
    
    @Override
    public String toString()
    {
        return this.type;
    }
}
