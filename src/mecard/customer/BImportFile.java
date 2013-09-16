/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 */
package mecard.customer;

import mecard.config.BImportDBFieldTypes;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import mecard.config.ConfigFileTypes;
import mecard.config.MessagesConfigTypes;
import mecard.config.PropertyReader;
import mecard.exception.BusyException;
import mecard.util.AlbertaCity;
import mecard.util.City;
import mecard.util.DateComparer;
import mecard.util.Phone;

/**
 * This class creates the data and header files.
 * @author metro
 */
public class BImportFile
{
    public final static String BORROWER_TABLE = "borrower";
    public final static String BORROWER_PHONE_TABLE = "borrower_phone"; 
    public final static String BORROWER_ADDRESS_TABLE = "borrower_address"; 
    public final static String BORROWER_BARCODE_TABLE = "borrower_barcode";
    
    public static class Builder
    {
        private final String headerName;
        private final String dataName;
        private String name;
        private String expiry;
        private String pin;
        private String phoneType = "h-noTC"; // TODO move this to the bimport.properties.
        private String phone;
        private String address1;
        private String address2 = "";
        private String city;
        private String postalCode;
        private String email;
        private String barcode;
        private String emailName;
        private boolean isNotifyByEmail;
        private boolean isPreoverdueRequiredByDefault = false;
        
        public Builder(String headerPath, String dataPath)
        {
            this.headerName = headerPath;
            this.dataName   = dataPath;
        }
        
        /**
         * Sets the customer's email name, the string before the domain.
         * @param e
         * @return builder object.
         */
        public Builder emailName(String e)
        {
            this.emailName = e;
            return this;
        }
        
        /**
         * Sets the name of the customer.
         * @param n name.
         * @return builder object. 
         */
        public Builder name(String n)
        {
            this.name = n;
            return this;
        }
        
        /**
         * Sets the customer's bar code.
         * @param b barcode or library card number.
         * @return builder object.
         */
        public Builder barcode(String b)
        {
            this.barcode = b;
            return this;
        }
        
        /**
         * Sets the customer's account expiry date.
         * @param e date. Formatting is system dependant.
         * @return builder object.
         */
        public Builder expire(String e)
        {
            this.expiry = e;
            return this;
        }
        
        /**
         * Sets the customer's PIN.
         * @param p pin
         * @return builder object.
         */
        public Builder pin(String p)
        {
            this.pin = p;
            return this;
        }
        
        /**
         * Sets the customer's phone type.
         * @param p phone type. Defaults to 'h-noTC'.
         * @return builder object.
         */
        public Builder pType(String p)
        {
            this.phoneType = p;
            return this;
        }
        
        /**
         * Sets the customer's phone number.
         * @param p phone number.
         * @return builder object.
         */
        public Builder pNumber(String p)
        {
            this.phone = p;
            return this;
        }
        
        /**
         * Sets the customer's street address.
         * @param a street address without city, postal code or province.
         * @return builder object.
         */
        public Builder address1(String a)
        {
            this.address1 = a;
            return this;
        }
        
        /**
         * Sets the customer's city code (Horizon system dependant, values listed
         * in city_st table.
         * @param c city string code.
         * @return builder object.
         */
        public Builder city(String c)
        {
            this.city = c;
            return this;
        }
         
        /**
         * Sets the customer's postal code.
         * @param p postal code string.
         * @return builder object.
         */
        public Builder postalCode(String p)
        {
            this.postalCode = p;
            return this;
        }
        
        /**
         * Sets the customers email address.
         * @param e the full email address of the customer.
         * @return builder object.
         */
        public Builder email(String e)
        {
            this.email = e;
            return this;
        }
        
        /** Sets the customer's prefer email notifications, defaults to true.
         * 
         * @param b true if you want notices to default to email and false
         * otherwise.
         * @return builder object.
         */
        public Builder preferEmailNotifications(boolean b)
        {
            this.isNotifyByEmail = b;
            return this;
        }
        
        /**
         * Sets the send_preoverdue switch on the customer's account at creation time.
         * @param b true if you want send_preoverdue turned on and false otherwise.
         * @return builder object.
         */
        public Builder setDefaultPreoverdue(boolean b)
        {
            this.isPreoverdueRequiredByDefault = b;
            return this;
        }
        
        public BImportFile build()
        {
            return new BImportFile(this);
        }
    }
    
    private BImportFile(Builder b)
    {
        StringBuilder headerContent = new StringBuilder();
        StringBuilder dataContent   = new StringBuilder();
        File header = createFile(b.headerName);
        File data   = createFile(b.dataName);
        // These fields need to be formatted
        String expiryDate = "";
        try
        {
            expiryDate = DateComparer.ANSIToConfigDate(b.expiry);
        }
        catch (ParseException ex)
        {
            System.out.println(BImportFile.class.getName()
                    + " the supplied date '" + b.expiry + "' failed to convert.");
        }
        // Phone
        String phone = Phone.formatPhone(b.phone);
        // City needs to be mapped.
        // TODO fix to work for other provinces if required.
        City city = AlbertaCity.getInstanceOf();
        // This will get 'a code' for a legal place name, but if the code is not set
        // in the city_st.properties file, the customer's account will not load.
        String cityCode = city.getCityCode(b.city);
//        String cityCode = City.getCity(b.city);
        
        // Table borrower
        headerContent.append("x- "); // add or modify if exists
        headerContent.append(BORROWER_TABLE + ": "); // add or modify if exists
        headerContent.append(BImportDBFieldTypes.SECOND_ID + "; ");
        headerContent.append(BImportDBFieldTypes.NAME + "; ");
        headerContent.append(BImportDBFieldTypes.EXPIRY);
        if (b.pin != null)
        {
            headerContent.append("; ");
            headerContent.append(BImportDBFieldTypes.PIN);
        }
        headerContent.append("\r\n");
        
        dataContent.append("M- "); // add or modify if exists
        dataContent.append(BORROWER_TABLE + ": "); // add or modify if exists
        dataContent.append(b.barcode + "; ");
        dataContent.append(b.name + "; ");
        dataContent.append(expiryDate);    // see above for computation
        if (b.pin != null)
        {
            dataContent.append("; ");
            dataContent.append(b.pin);
        }
        dataContent.append("\r\n");
        
        // Table borrower_phone
        headerContent.append(BORROWER_PHONE_TABLE + ": "); // add or modify if exists
        headerContent.append(BImportDBFieldTypes.PHONE_TYPE + "; ");
        headerContent.append(BImportDBFieldTypes.PHONE_NUMBER + "\r\n");
        
        dataContent.append(BORROWER_PHONE_TABLE + ": "); // add or modify if exists
        dataContent.append(b.phoneType + "; ");
        dataContent.append(phone + "\r\n"); // see computation above.
        
        // Table borrower_address
        headerContent.append(BORROWER_ADDRESS_TABLE + ": "); // add or modify if exists
        headerContent.append(BImportDBFieldTypes.ADDRESS_1 + "; ");
        headerContent.append(BImportDBFieldTypes.ADDRESS_2 + "; ");
        headerContent.append(BImportDBFieldTypes.CITY + "; ");
        headerContent.append(BImportDBFieldTypes.POSTAL_CODE + "; ");
        headerContent.append(BImportDBFieldTypes.EMAIL_NAME + "; ");
        if (b.isNotifyByEmail) headerContent.append(BImportDBFieldTypes.EMAIL_NOTIFICATION + "; ");
        headerContent.append(BImportDBFieldTypes.EMAIL_ADDRESS);
        if (b.isPreoverdueRequiredByDefault)
        {
            headerContent.append("; ");
            headerContent.append(BImportDBFieldTypes.SEND_PREOVERDUE); // Defaults on so not optional field.
        }
        headerContent.append("\r\n");
        
        dataContent.append(BORROWER_ADDRESS_TABLE + ": "); // add or modify if exists
        dataContent.append(b.address1 + "; ");
        dataContent.append(b.address2 + "; ");
        dataContent.append(cityCode + "; ");
        dataContent.append(b.postalCode + "; ");
        dataContent.append(b.emailName + "; ");
        if (b.isNotifyByEmail) dataContent.append("1; "); // careful this is an ENUM type in Horizon. Safe to leave out but if used must match.
        dataContent.append(b.email);
        if (b.isPreoverdueRequiredByDefault)
        {
            dataContent.append("; ");
            dataContent.append("1");
        }
        dataContent.append("\r\n");
        
        // Table borrower_barcode
        headerContent.append(BORROWER_BARCODE_TABLE + ": "); // add or modify if exists
        headerContent.append(BImportDBFieldTypes.BARCODE + "\r\n");
        
        dataContent.append(BORROWER_BARCODE_TABLE + ": "); // add or modify if exists
        dataContent.append(b.barcode + "\r\n");
        
        writeContent(headerContent, header);
        writeContent(dataContent, data);     
    }
    
    private void writeContent(StringBuilder content, File file)
    {
        BufferedWriter bWriter;
        try
        {
            // write the builder contents to the file with the correct switches.
            bWriter = new BufferedWriter(new FileWriter(file));
            bWriter.write(content.toString());
            bWriter.close();
        }
        catch (IOException ex)
        {
            String msg = "unable to create '"+file.getName()+"' file.";
            Logger.getLogger(BImportFile.class.getName()).log(Level.SEVERE, msg, ex);
        }
    }
    
    private File createFile(String name)
    {
        File file = new File(name);
        if (file.exists())
        {
            if (file.delete() == false)
            {
                System.out.println("Could not delete '" 
                        + name + "', is it being used by another process?");
                Properties props = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
                String busyMessage = props.getProperty(MessagesConfigTypes.UNAVAILABLE_SERVICE.toString());
                throw new BusyException(busyMessage);
            }
        }
        // create a new one.
        file = new File(name);
        return file;
    }
}
