/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.config;

/**
 * Mandatory property types of the bimp config file. Note that there are
 * fields 
 * @author metro
 */
public enum BImportPropertyTypes
{
    BIMPORT_DIR("directory"),
    SERVER("server"),
    PASSWORD("password"),
    USER("user"),
    DATABASE("database"),
    SERVER_ALIAS("server-alias"),
    VERSION("version"), // like fm41 any other version is currently untested.
    DEFAULT_BTYPE("btype"), // like bawb
    MAIL_TYPE("mail-type"),
    LOCATION("location"), // Like lalap
    IS_INDEXED("indexed"); // "y = NOT indexed"
//    DATE_FORMAT("date-format"); // the prefered date formatting. now in Environment file.
    
    private String type;

    private BImportPropertyTypes(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
