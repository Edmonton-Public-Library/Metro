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
    STATUS_READY("RA0"),
    STATUS_NOT_READY("RA1"),
    STATUS_BUSY("RA2"),
    CUSTOMER_EXISTS("RB0"),
    CUSTOMER_NOT_EXIST("RB1"),
    CUSTOMER_CREATED("RC0"),
    CUSTOMER_NOT_CREATED("RC1"),
    CUSTOMER_UPDATED("RD0"),
    CUSTOMER_NOT_UPDATED("RD1");
    
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
