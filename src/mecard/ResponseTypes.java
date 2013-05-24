/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard;

/**
 *
 * @author metro
 */
public enum ResponseTypes
{
    ERROR("RA9"), // Command was received but failed to execute
    // either it was malformed, empty (null), or not supported.
    INIT("RA0"),
    OK("RA1"),
    BUSY("RA2"),
    UNAVAILABLE("RA3"),
    SUCCESS("RA4"),
    FAIL("RA5"),
    UNAUTHORIZED("RA6");
    
    private String type;
    
    private ResponseTypes(String s)
    {
        this.type = s;
    }
    
    @Override
    public String toString()
    {
        return this.type;
    }
}
