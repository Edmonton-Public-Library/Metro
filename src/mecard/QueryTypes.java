/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard;

/**
 *
 * @author metro
 */
public enum QueryTypes
{
    GET_STATUS("QA0"),
    GET_CUSTOMER("QB0"),
    CREATE_CUSTOMER("QC0"),
    UPDATE_CUSTOMER("QD0"), 
    NULL("QN0");
    
    private String type;
    
    private QueryTypes(String s)
    {
        this.type = s;
    }
    
    @Override
    public String toString()
    {
        return this.type;
    }
}
