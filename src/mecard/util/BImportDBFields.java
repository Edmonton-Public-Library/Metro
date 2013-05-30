/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

/**
 *
 * @author metro
 */
public enum BImportDBFields
{
    SECOND_ID("second_id"),
    NAME("name"),
    EXPIRY("expiration_date"),
    PIN("pin#"),
    PHONE_TYPE("phone_type"),
    PHONE_NUMBER("phone_no"),
    ADDRESS_1("address1"),
    ADDRESS_2("address2"),
    CITY("city_st"),
    POSTAL_CODE("postal_code"),
    EMAIL_NAME("email_name"),
    EMAIL_ADDRESS("email_address"),
    BARCODE("bbarcode");
    
    private String type;

    private BImportDBFields(String s)
    {
        this.type = s;
    }

    @Override
    public String toString()
    {
        return this.type;
    }
}
